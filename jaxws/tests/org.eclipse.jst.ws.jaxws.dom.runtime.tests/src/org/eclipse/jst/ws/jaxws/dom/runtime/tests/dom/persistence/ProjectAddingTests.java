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

import java.io.IOException;
import java.text.MessageFormat;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.testutils.dom.WaitingDomUtil;
import org.eclipse.jst.ws.jaxws.testutils.project.TestEjb3Project;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class ProjectAddingTests extends TestCase {
	
	private JaxWsWorkspaceResource target;
	private IJavaModel javaModel;
	private DomUtil util;
	private TestProject testPrj3;;
	
	@Override
	public void setUp() throws IOException
	{
		util = new WaitingDomUtil();
		javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		target = new JaxWsWorkspaceResource(javaModel);
		target.load(null);
		target.startSynchronizing();
		
	}
	
	@Override
	public void tearDown() throws CoreException
	{
		target.stopSynchronizing();
	}
	
	public void test_newWsProjectSynched() throws Exception
	{
		testPrj3 = new TestProject(new TestEjb3Project("TestEJBProject3").getProject());
		final IWebServiceProject wsPrj3 = util.findProjectByName(target.getDOM(), testPrj3.getProject().getName());
		assertNotNull("Newly added project not in dom",wsPrj3);
	}
	
	public void test_newNonWsProjectNotAdded() throws Exception
	{
		testPrj3 = new TestProject("TestProject3");
		assertProjectNotInDOM(target.getDOM(), testPrj3.getProject().getName());
	}
	
	public void test_NonWsProjectIgnorredWhenClosedOrRemoved() throws CoreException
	{
		testPrj3 = new TestProject("TestProject3");
		testPrj3.createSourceFolder("src");
		final IPackageFragment pf = testPrj3.createPackage("com.sap.test.nonwsproject");
		final IType type = testPrj3.createType(pf, "SomeClass.java", "public class SomeClass {}");
		final IFile file = testPrj3.getJavaProject().getJavaModel().getWorkspace().getRoot().getFile(type.getPath());
		IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file);
		testPrj3.getProject().close(null);
	}
	
	public void test_openedNonWsProjectNotAdded() throws Exception
	{
		testPrj3 = new TestProject("TestEJBProject3");
		testPrj3.getProject().close(null);
		testPrj3.getProject().open(null);
		assertProjectNotInDOM(target.getDOM(), testPrj3.getProject().getName());
	}
	
	private void assertProjectNotInDOM(final IDOM dom, final String projectName)
	{
		assertNull(MessageFormat.format("Project {0} unexpectedly found in DOM", projectName), DomUtil.INSTANCE.findProjectByName(dom, projectName));
	}
}
