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
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDomLoadListener;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IProjectSelector;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
/**
 * This test might be executed in non-clean workspace. Thus it should try to ignore any existing projects and work with the test proejcts it creates.
 * @author Hristo Sabev
 *
 */
public class JaxWsWorkspaceResourceTests extends TestCase
{
	private TestProject testProject;
	private IPackageFragment pf;
	private IJavaModel javaModel;
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		testProject = new TestProject();
		testProject.createSourceFolder("src");
		pf = testProject.createPackage("com.sap.test.smoke");
		javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
	}
	
	/**
	 * Verifies that a simple project with one web service class and one sei can be parsed
	 *
	 */
	public void test_Smoke_OneWsAndOneSei() throws CoreException, IOException
	{
		testProject.createType(pf, "Sei1.java", "@javax.jws.WebService public interface Sei1 {}");
		testProject.createType(pf, "WebService1.java", "@javax.jws.WebService(endpointInterface=\"com.sap.test.smoke.Sei1\") public class WebService1{}");
		final JaxWsWorkspaceResource target = createJaxWsWorkspaceResource(testProject);
		target.load(null);
		
		//find the test project and work only with it. ignore any other projects in the workspace
		final List<IWebServiceProject> projects = target.getDOM().getWebServiceProjects();
		IWebServiceProject testWsProject = null;
		for (IWebServiceProject aProject : projects)
		{
			if (aProject.getName().equals(testProject.getJavaProject().getElementName()))
			{
				testWsProject = aProject;
				break;
			}
		}
		if (testWsProject == null)
		{
			fail("WebServiceProject wit name " + testProject.getJavaProject().getElementName() + "is not found in the model");
		}
		
		final List<IServiceEndpointInterface> seis = testWsProject.getServiceEndpointInterfaces();
		assertTrue("More than 1 sei found in the model, while only one test sei was used", seis.size() == 1);
		assertEquals("The sei in the model is with different name", "Sei1", seis.get(0).getName());
		assertEquals("The sei in the model is with different implementation", "com.sap.test.smoke.Sei1", seis.get(0).getImplementation());
		final List<IWebService> webServices = testWsProject.getWebServices();
		assertTrue("More than 1 web service found in the model, while only one test web service was used", webServices.size() == 1);
		assertEquals("The web service in the model is with different name", "WebService1Service", webServices.get(0).getName());
		assertEquals("The web service in the model is with different implementation", "com.sap.test.smoke.WebService1", webServices.get(0).getImplementation());
		assertSame("The service endpoint found in the model is not the same instance as the sei of the web service", webServices.get(0).getServiceEndpoint(), seis.get(0));
	}
	
	public void test_DomLoadedForNoProcessedProjects() throws IOException
	{
		final JaxWsWorkspaceResource target = new JaxWsWorkspaceResource(javaModel) 
		{

			@Override
			public IProjectSelector[] getProjectSelectors()
			{
				return new IProjectSelector[] { new IProjectSelector() 
				{

					public boolean approve(IJavaProject prj)
					{
						return false;
					}
				}};
			}
			
		};
		target.load(null);
		assertNotNull(target.getDOM());
		assertEquals(0, target.getDOM().getWebServiceProjects().size());
	}
	
	private JaxWsWorkspaceResource createJaxWsWorkspaceResource(final TestProject testProject)
	{
		return new JaxWsWorkspaceResource(javaModel) 
		{
			@Override
			public IProjectSelector[] getProjectSelectors() 
			{
				return new IProjectSelector[] {new IProjectSelector()
				{
					public boolean approve(IJavaProject prj) {
						return prj.getElementName().equals(testProject.getJavaProject().getElementName());
					}
				}};
			}
		};
	}
	
	protected class LoadListener implements IWsDomLoadListener
	{
		boolean startCalled;
		boolean finishedCalled;
		
		public void finished() {
			finishedCalled = true;
		}

		public void started() {
			startCalled = true;
		}
	}
	
}
