/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils.tests.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.utils.StatusUtils;
import org.eclipse.jst.ws.jaxws.utils.resources.FileUtils;
import org.eclipse.jst.ws.jaxws.utils.resources.IFileUtils;
import org.jmock.core.constraint.IsEqual;
import org.jmock.core.constraint.IsSame;

public class FileUtilsUnitTest extends MockObjectTestCase
{
	private final IFileUtils fileUtils = FileUtils.getInstance();
	private Mock<ICompilationUnit> cuMock;
	private final IProgressMonitor monitor = new NullProgressMonitor();
	
	@Override
	protected void setUp() throws Exception
	{
		cuMock = mock(ICompilationUnit.class);
	}
	
	public void testSetCompilationUnitContentDealsCorrectlyWithWorkingCopy() throws JavaModelException
	{
		final String testContent = "MyTestContent";
		final Mock<IBuffer> bufferMock = mock(IBuffer.class);
		bufferMock.expects(once()).method("setContents").with(new IsSame(testContent));
		
		cuMock.expects(once()).method("becomeWorkingCopy");
		cuMock.expects(once()).method("getBuffer").will(returnValue(bufferMock.proxy()));
		cuMock.expects(once()).method("commitWorkingCopy").with(new IsEqual(true), new IsSame(monitor));
		cuMock.expects(once()).method("discardWorkingCopy");

		fileUtils.setCompilationUnitContent(cuMock.proxy(), testContent, true, monitor);
	}
	
	public void testSetCompilationUnitContentDoesNotDiscardWorkingCopyOnJME()
	{
		final JavaModelException testException = new JavaModelException(new CoreException(StatusUtils.statusError("TEST")));
		cuMock.expects(once()).method("becomeWorkingCopy").will(throwException(testException));
		cuMock.expects(never()).method("discardWorkingCopy");
		cuMock.expects(never()).method("commitWorkingCopy");
		
		try
		{
			fileUtils.setCompilationUnitContent(cuMock.proxy(), "123", true, monitor);
		}
		catch(JavaModelException e)
		{
			assertTrue("Test exception was not caught", e == testException);
		}
	}
	
	public void testSetCuContentSavesDirtyEditors() throws JavaModelException
	{
		final boolean[] saveEditorInvoked = new boolean[]{false};
		final boolean[] setContentsInvoked = new boolean[]{false};
		
		final IFileUtils myFileUtils = new FileUtils(){
			public boolean isCompilationUnitDisplayedInDirtyEditor(final ICompilationUnit cu)
			{
				assertTrue("Unexpected compilation unit", cu == cuMock.proxy());
				return true;
			}
			
			@Override
			public void saveEditor(final ICompilationUnit cu)
			{
				assertTrue("Save editors invoked before set CU content", setContentsInvoked[0]);
				assertTrue("Unexpected compilation unit", cu == cuMock.proxy());
				saveEditorInvoked[0] = true;
			}
			
			@Override
			public void setCompilationUnitContent(ICompilationUnit unit, String content, boolean force, IProgressMonitor monitor)
											throws JavaModelException
			{
				assertFalse("Set CU content invoked after editor saved", saveEditorInvoked[0]);
				assertEquals("Unexpected CU content", "MyContent", content);
				setContentsInvoked[0] = true;
			}
		};
		
		myFileUtils.setCompilationUnitContentAndSaveDirtyEditors(cuMock.proxy(), "MyContent", true, null);
		assertTrue("Set contents not invoked", setContentsInvoked[0]);
		assertTrue("Save editors not invoked", saveEditorInvoked[0]);
	}
	
	public void testSetCuContentDoesNotSavesCleanEditors() throws JavaModelException
	{
		final boolean[] isDisplayedInvoked = new boolean[]{false};
		final boolean[] setContentsInvoked = new boolean[]{false};
		
		final IFileUtils myFileUtils = new FileUtils(){
			public boolean isCompilationUnitDisplayedInDirtyEditor(final ICompilationUnit cu)
			{
				assertTrue("Unexpected compilation unit", cu == cuMock.proxy());
				isDisplayedInvoked[0] = true;
				return false;
			}
			
			@Override
			public void saveEditor(final ICompilationUnit cu)
			{
				fail("Unexpected invocation");
			}
			
			@Override
			public void setCompilationUnitContent(ICompilationUnit unit, String content, boolean force, IProgressMonitor monitor)
											throws JavaModelException
			{
				assertFalse("Set CU content invoked after editor saved", isDisplayedInvoked[0]);
				assertEquals("Unexpected CU content", "MyContent", content);
				setContentsInvoked[0] = true;
			}
		};
		
		myFileUtils.setCompilationUnitContentAndSaveDirtyEditors(cuMock.proxy(), "MyContent", true, null);
		assertTrue("Set contents not invoked", setContentsInvoked[0]);
		assertTrue("Is displayed in dirty editor not invoked", isDisplayedInvoked[0]);
	}
	
}
