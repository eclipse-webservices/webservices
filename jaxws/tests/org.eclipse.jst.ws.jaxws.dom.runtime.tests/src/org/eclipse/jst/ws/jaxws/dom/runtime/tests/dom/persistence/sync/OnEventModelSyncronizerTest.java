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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource.ServiceModelData;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync.OnEventModelSynchronizer;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;

public class OnEventModelSyncronizerTest extends MockObjectTestCase 
{
	public void testElementChangedResourceLoadChanged()
	{
		JaxWsWorkspaceResource resource = new JaxWsWorkspaceResource(null) {
			@Override
			public boolean isLoadCnaceled() {
				return true;
			}
		} ;
		
		OnEventModelSynchronizer sync = new OnEventModelSynchronizer(resource, null);
		// in case resource load cancel is not processed call to element delta will fail with NPE
		sync.elementChanged(null);		
	}
	
	public void testGuessPrimaryTypeNameCuInDefaultPackage()
	{
		final Mock<IJavaModel> javaModel = mock(IJavaModel.class);
		final JaxWsWorkspaceResource resource = new JaxWsWorkspaceResource(javaModel.proxy());
		
		final Mock<IJavaElement> parent = mock(IJavaElement.class);
		parent.expects(once()).method("getElementName").will(returnValue(""));
		
		final String className = "MyTest";
		final Mock<ICompilationUnit> cu = mock(ICompilationUnit.class);
		cu.expects(once()).method("getParent").will(returnValue(parent.proxy()));
		cu.expects(once()).method("getElementName").will(returnValue(className + ".java"));

		final MyOnEventModelSynchronizer sync = new MyOnEventModelSynchronizer(resource, null);
		assertEquals("Incorrect name guessed", className, sync.guessPrimaryTypeName(cu.proxy()));
	}
	
	public void testGuessPrimaryTypeNameCuInSomePackage()
	{
		final Mock<IJavaModel> javaModel = mock(IJavaModel.class);
		final JaxWsWorkspaceResource resource = new JaxWsWorkspaceResource(javaModel.proxy());
		
		final String pack = "com.sap.demo";
		final Mock<IJavaElement> parent = mock(IJavaElement.class);
		parent.expects(once()).method("getElementName").will(returnValue(pack));
		
		final String className = "MyTest";
		final Mock<ICompilationUnit> cu = mock(ICompilationUnit.class);
		cu.expects(once()).method("getParent").will(returnValue(parent.proxy()));
		cu.expects(once()).method("getElementName").will(returnValue(className + ".java"));

		final MyOnEventModelSynchronizer sync = new MyOnEventModelSynchronizer(resource, null);
		assertEquals("Incorrect name guessed", pack + "." + className, sync.guessPrimaryTypeName(cu.proxy()));
	}
	
	public void testRecursevilyHandleAddedPackagesDoesNotProcessNonWsProjects() throws JavaModelException
	{
		final Mock<IJavaModel> javaModel = mock(IJavaModel.class);
		final JaxWsWorkspaceResource resource = new JaxWsWorkspaceResource(javaModel.proxy());
		final MyOnEventModelSynchronizer sync = new MyOnEventModelSynchronizer(resource, null);
		final Mock<IJavaElementDelta> delta = mock(IJavaElementDelta.class);
		// it is expected that IJavaElementDelta#getAffectedChildren() method will not be called
		sync.recursevilyHandleAddedPackages(delta.proxy());
	}
	
	public class MyOnEventModelSynchronizer extends OnEventModelSynchronizer
	{
		public IWebServiceProject wsProject;
		
		public MyOnEventModelSynchronizer(JaxWsWorkspaceResource resource, ServiceModelData serviceData) {
			super(resource, serviceData);
		}
		
		@Override
		protected String guessPrimaryTypeName(ICompilationUnit cu) {
			return super.guessPrimaryTypeName(cu);
		}
		
		@Override
		protected void recursevilyHandleAddedPackages(IJavaElementDelta rootDelta) throws JavaModelException {
			super.recursevilyHandleAddedPackages(rootDelta);
		}
		
		@Override
		protected IWebServiceProject findProjectByDelta(IJavaElementDelta rootDelta) {
			return wsProject;
		}
	}

}
