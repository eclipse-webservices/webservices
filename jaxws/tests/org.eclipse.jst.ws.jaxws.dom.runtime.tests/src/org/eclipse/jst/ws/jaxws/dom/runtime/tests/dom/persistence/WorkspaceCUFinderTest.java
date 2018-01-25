/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WsDOMLoadCanceledException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.ICompilationUnitFinder;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.ICompilationUnitHandler;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IProjectSelector;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WorkspaceCUFinder;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;


public class WorkspaceCUFinderTest extends CompilationUnitFinderContractTest
{

	@Override
	protected ICompilationUnitFinder createTarget()
	{
		final IProjectSelector[] ps = new IProjectSelector[projectSelectors().size()];
		projectSelectors().toArray(ps);
		return new WorkspaceCUFinder(javaModel(), ps);
	}
	
	public void testProgressMonitorUpdated() throws CoreException, WsDOMLoadCanceledException
	{
		Mock<IProgressMonitor> monitor = mock(IProgressMonitor.class);
		monitor.stubs().method("isCanceled").will(returnValue(false));
		monitor.expects(once()).method("beginTask");
		monitor.expects(once()).method("done");
		monitor.expects(exactly(2)).method("worked");
		
		ICompilationUnitFinder cuFinder = setUpForProgressMonitorTests();
		Mock<ICompilationUnitHandler> cuHandler = mock(ICompilationUnitHandler.class);
		cuHandler.stubs().method("handle");
		cuHandler.stubs().method("started");
		cuHandler.stubs().method("finished");
		
		cuFinder.find(monitor.proxy(), cuHandler.proxy());		
	}	
	
	public void testCnacelationRequestsProcessed() throws CoreException
	{
		Mock<IProgressMonitor> monitor = mock(IProgressMonitor.class);
		monitor.expects(once()).method("beginTask");
		monitor.expects(once()).method("done");		
		monitor.expects(once()).method("isCanceled").will(returnValue(true));
		
		ICompilationUnitFinder cuFinder = setUpForProgressMonitorTests();
		Mock<ICompilationUnitHandler> cuHandler = mock(ICompilationUnitHandler.class);
		cuHandler.stubs().method("handle");
		cuHandler.stubs().method("started");
		
		try {
			cuFinder.find(monitor.proxy(), cuHandler.proxy());
			fail("SynchronizationCanceledException not thrown");
		} catch (WsDOMLoadCanceledException e) {
			// expected
		}	
	}
	
	private ICompilationUnitFinder setUpForProgressMonitorTests() throws CoreException
	{
		final TestProject project1 = new TestProject();
		project1.createSourceFolder("src");
		final IPackageFragment pf1 = project1.createPackage("com.sap.test");
		project1.createType(pf1, "Cu1.java", "class Cu1 {}");
		project1.createType(pf1, "Cu2.java", "class Cu2 {}");
		
		IProjectSelector [] slectors = new IProjectSelector[] {
				new IProjectSelector() {
					public boolean approve(IJavaProject prj) {
						return project1.getProject().getName().equals(prj.getProject().getName());
					}
				}
		};
				
		return new WorkspaceCUFinder(javaModel(), slectors);
	}
}
