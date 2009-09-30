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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation;

import junit.framework.TestCase;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.JaxWsDomValidator;
import org.eclipse.jst.ws.jaxws.testutils.IWaitCondition;
import org.eclipse.jst.ws.jaxws.testutils.assertions.Assertions;
import org.eclipse.jst.ws.jaxws.testutils.assertions.ConditionCheckException;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.utils.resources.FileUtils;

public class ValidationTestsSetUp extends TestCase
{
	protected TestProject testProject;
	protected JaxWsWorkspaceResource target;
	protected IPackageFragment testPack;
	protected JaxWsDomValidator validator;
	protected DomUtil util = DomUtil.INSTANCE;
	
	@Override
	public void setUp() throws Exception
	{
		testProject = new TestProject();
		testProject.createSourceFolder("src");
		testPack = testProject.createPackage("test");
		validator = new JaxWsDomValidator();
		
		IJavaModel javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		target = new JaxWsWorkspaceResource(javaModel) 
		{
			@Override
			public boolean approveProject(IJavaProject prj) {
				return prj.getElementName().equals(testProject.getJavaProject().getElementName());
			}
		};
		target.load(null);
		target.startSynchronizing();
	}

	@Override
	public void tearDown()
	{
		try {
			testProject.dispose();
		} catch (Exception _) {}
	}

	protected IWebService findWs(String wsFQName)
	{
		final IWebServiceProject wsProject = util.findProjectByName(target.getDOM(), testProject.getProject().getName());
		return util.findWsByImplName(wsProject, wsFQName);			
	}

	protected IServiceEndpointInterface findSei(String seiFQName) 
	{
		final IWebServiceProject wsProject = util.findProjectByName(target.getDOM(), testProject.getProject().getName());
		return util.findSeiByImplName(wsProject, seiFQName);		
	}
	
	protected void setContents(final ICompilationUnit cu, final String contents) throws JavaModelException 
	{
		String newContent = "import " + testPack.getElementName() + ";\n" + contents;
		FileUtils.getInstance().setCompilationUnitContent(cu, newContent, false, null);
	}
	
	protected void waitUntilMarkersAppear(final IResource resource, final String markerId, final boolean includeSubtypes, final int depth)
	{
		final IWaitCondition condition = new IWaitCondition(){
			public boolean checkCondition() throws ConditionCheckException
			{
				try
				{
					return resource.findMarkers(markerId, includeSubtypes, depth).length > 0;
				} catch (CoreException e)
				{
					throw new ConditionCheckException(e);
				}
			}};
		Assertions.waitAssert(condition, "Error markers did not disappear");
	}
	
	protected void waitUntilMarkersDisappear(final IResource resource, final String markerId, final boolean includeSubtypes, final int depth)
	{
		final IWaitCondition condition = new IWaitCondition(){
			public boolean checkCondition() throws ConditionCheckException
			{
				try
				{
					return resource.findMarkers(markerId, includeSubtypes, depth).length == 0;
				} catch (CoreException e)
				{
					throw new ConditionCheckException(e);
				}
			}};
			Assertions.waitAssert(condition, "Error markers did not appear");
	}
	
}
