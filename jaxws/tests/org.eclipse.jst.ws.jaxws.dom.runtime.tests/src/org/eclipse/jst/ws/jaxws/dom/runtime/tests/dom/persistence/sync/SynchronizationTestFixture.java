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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.sync;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.DomTestUtils;
import org.eclipse.jst.ws.jaxws.testutils.dom.WaitingDomUtil;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.testutils.jobs.JobUtils;
import org.eclipse.jst.ws.jaxws.testutils.project.TestEjb3Project;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;

public class SynchronizationTestFixture extends MockObjectTestCase
{
	protected TestProject testPrj1; 
	protected TestProject testPrj2;
	protected JaxWsWorkspaceResource target;
	protected IJavaModel javaModel;
	
	protected static final String prj1PckName = "com.sap.test.modelsync1";	
	protected static final String prj2PckName = "com.sap.test.modelsync2";
	
	protected static final String ws1ImplName = prj1PckName + ".WS1";
	protected static final String sei1ImplName = prj1PckName + ".Sei1";
	protected static final String ws2ImplName = prj2PckName + ".WS2";
	protected static final String sei2ImplName = prj2PckName + ".Sei2";	
	
	protected IWebServiceProject wsPrj1;
	protected IWebService ws1;
	protected IServiceEndpointInterface sei1;
	
	protected IWebServiceProject wsPrj2;
	protected IWebService ws2;
	protected IServiceEndpointInterface sei2;
	protected Collection<String> allowedProjects;
	
	protected DomUtil domUtil;
	protected DomTestUtils testUtil =  new DomTestUtils();

	public void setUp() throws Exception
	{
		domUtil = new WaitingDomUtil();
		allowedProjects = new ArrayList<String>(3);
		javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		target = createTarget();
		testPrj1 = new TestProject(new TestEjb3Project("TestProject1").getProject());
		testPrj1.createSourceFolder("src");
		final IPackageFragment modelSync1 = testPrj1.createPackage("com.sap.test.modelsync1");
		final String javaModelErrorMessage = "Java Model Error. Just created type not found in the java model";
		assertNotNull(javaModelErrorMessage, testPrj1.createType(modelSync1, "Sei1.java", "@javax.jws.WebService(name=\"Sei1Name\") public interface Sei1 {}"));
		assertNotNull(javaModelErrorMessage, testPrj1.createType(modelSync1, "WS1.java", "@javax.jws.WebService(serviceName=\"WS1Name\", endpointInterface=\"com.sap.test.modelsync1.Sei1\") public class WS1 {}"));
				
		testPrj2 = new TestProject(new TestEjb3Project("TestProject2").getProject());
		testPrj2.createSourceFolder("src");
		final IPackageFragment modelSync2 = testPrj2.createPackage("com.sap.test.modelsync2");
		assertNotNull(javaModelErrorMessage, testPrj2.createType(modelSync2, "Sei2.java", "@javax.jws.WebService(name=\"Sei2Name\") public interface Sei2 {}"));
		assertNotNull(javaModelErrorMessage, testPrj2.createType(modelSync2, "WS2.java", "@javax.jws.WebService(serviceName=\"WS2Name\", endpointInterface=\"com.sap.test.modelsync1.Sei2\") public class WS2 {}"));
		
		allowedProjects.add(testPrj1.getJavaProject().getElementName());
		allowedProjects.add(testPrj2.getJavaProject().getElementName());
		JobUtils.waitForJobs();
		
		target.load(null);
		wsPrj1 = domUtil.findProjectByName(target.getDOM(), testPrj1.getJavaProject().getElementName());
		assertNotNull(wsPrj1);
		
		assertEquals(1, wsPrj1.getServiceEndpointInterfaces().size());
		assertEquals(1, wsPrj1.getWebServices().size());
				
		ws1 = domUtil.findWsByImplName(wsPrj1, ws1ImplName);
		assertNotNull(ws1);
		sei1 = domUtil.findSeiByImplName(wsPrj1, sei1ImplName);
		assertNotNull(sei1);
		wsPrj2 = domUtil.findProjectByName(target.getDOM(), testPrj2.getJavaProject().getElementName());
		assertNotNull(wsPrj2);
		assertEquals(1, wsPrj2.getServiceEndpointInterfaces().size());
		assertEquals(1, wsPrj2.getWebServices().size());
		
		ws2 = domUtil.findWsByImplName(wsPrj2, ws2ImplName);
		assertNotNull(ws2);
		sei2 = domUtil.findSeiByImplName(wsPrj2, sei2ImplName);
		assertNotNull(sei2);
	}
	
	@Override
	public void beforeTestCase() throws CoreException
	{
		enableAutoBuild(ResourcesPlugin.getWorkspace(), false);
	}
	
	@Override
	public void afterTestCase() throws CoreException
	{
		enableAutoBuild(ResourcesPlugin.getWorkspace(), true);
	}
	
	private void enableAutoBuild(IWorkspace ws, boolean enable) throws CoreException
	{
		final IWorkspaceDescription desc = ws.getDescription();
		desc.setAutoBuilding(enable);
		ws.setDescription(desc);
	}
	
	protected Adapter findAdapter(EObject object, Class<?> clazz) {
		for (Adapter adapter: object.eAdapters()) {
			if (adapter.isAdapterForType(clazz)) {
				return adapter; 
			}
		}
		
		return null;
	}
		
	@Override
	public void tearDown() throws CoreException
	{
		target.stopSynchronizing();
		testPrj1.dispose();
		testPrj2.dispose();
	}
	
	private JaxWsWorkspaceResource createTarget()
	{
		return new JaxWsWorkspaceResource(javaModel) 
		{

			@Override
			public boolean approveProject(IJavaProject prj) {
				for (String allowedPrj : allowedProjects)
				{
					if (prj.getElementName().equals(allowedPrj))
					{
						return true;
					}
				}
				return false;
			}
		};
	}	
}
