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
package org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.DOMAdapterFactoryContentProvider;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ILoadingWsProject;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ILoadingWsProject.ILoadingDummy;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ISEIChildList;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.IWebServiceChildList;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWsDOMRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.ui.DomItemProviderAdapterFactory;
import org.eclipse.jst.ws.jaxws.testutils.dom.WaitingDomUtil;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProjectsUtils;

public class DOMAdapterFactoryContentProviderTest extends TestCase 
{
	private TestProject testPrj1; 
	
	private IPackageFragment modelSync1;
	
	private IWebServiceProject wsProject;
	
	protected JaxWsWorkspaceResource targetResource;
	protected DOMAdapterFactoryContentProvider adapterFactory;
	private DomUtil domUtil;
	
	@Override
	public void setUp() throws Exception
	{
		domUtil = new WaitingDomUtil();
		IProject ejbProject = TestProjectsUtils.createEjb3Project("DOMCntProvTestProject1" + System.currentTimeMillis());
		testPrj1 = new TestProject(ejbProject.getProject());
		modelSync1 = testPrj1.createPackage("org.eclipse.test.modelsync1");
		testPrj1.createType(modelSync1, "Sei1.java", "@javax.jws.WebService(name=\"Sei1Name\") public interface Sei1{\n" +
				"@javax.jws.WebMethod(name=\"parentMethod\") public void voidMethodWithNoArgsInParent();\n" +
				"}");
		testPrj1.createType(modelSync1, "Sei2.java", "@javax.jws.WebService(name=\"Sei2Name\") public interface Sei2 extends Sei1 {\n" +
				"@javax.jws.WebMethod(name=\"voidMethodWithArgs\") public void voidMethodWithArgs(@javax.jws.WebParam(name=\"param1\") java.util.List<String> param1);\n" +
				"}");
		testPrj1.createType(modelSync1, "WS1.java", "@javax.jws.WebService(serviceName=\"WS1Name\", endpointInterface=\"org.eclipse.test.modelsync1.Sei2\") public class WS1 {}");
		testPrj1.createType(modelSync1, "WS2.java", "@javax.jws.WebService(serviceName=\"WS2Name\") public class WS2 {}");
		
		targetResource = new JaxWsWorkspaceResource(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));
		targetResource.load(null);
		
		this.wsProject = domUtil.findProjectByName(targetResource.getDOM(), testPrj1.getProject().getName());
		assertNotNull(this.wsProject);
		
		adapterFactory = new DOMAdapterFactoryContentProvider() 
		{
			@Override
			protected IWebServiceProject getWsProject(final IDOM dom, final IProject project) {
				return wsProject;
			}
		};
		
		targetResource.startSynchronizing();
	}
	
	@Override
	public void tearDown() throws CoreException
	{
		targetResource.stopSynchronizing();
		testPrj1.dispose();
	}
	
	public void testDOMAdapterFactoryContentProvider() 
	{
		assertNotNull(adapterFactory.getAdapterFactory());
		assertTrue(adapterFactory.getAdapterFactory() instanceof DomItemProviderAdapterFactory);
	}

	public void testDOMAdapterFactoryContentProviderAdapterFactory() 
	{
		adapterFactory = new DOMAdapterFactoryContentProvider(new DomItemProviderAdapterFactory());
		
		assertNotNull(adapterFactory.getAdapterFactory());
		assertTrue(adapterFactory.getAdapterFactory() instanceof DomItemProviderAdapterFactory);
	}

	public void testGetChildrenListsObject() 
	{
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		
		assertNotNull(children);
		assertEquals(children.length, 1);
		assertTrue(children[0] instanceof IWebServiceProject);
		
		children = adapterFactory.getChildren(children[0]);
		
		assertNotNull(children);
		assertEquals(children.length, 2);
		assertTrue(children[0] instanceof ISEIChildList);
		assertTrue(children[1] instanceof IWebServiceChildList);
		
		Object[] sEIs = adapterFactory.getChildren(children[0]);
		
		assertNotNull(sEIs);
		assertEquals(3, sEIs.length);
		assertTrue(sEIs[0] instanceof IServiceEndpointInterface);
		assertTrue(sEIs[1] instanceof IServiceEndpointInterface);
		assertTrue(sEIs[2] instanceof IServiceEndpointInterface);
		
		Object[] wSs = adapterFactory.getChildren(children[1]);
		
		assertNotNull(wSs);
		assertEquals(wSs.length, 2);
		assertTrue(wSs[0] instanceof IWebService);
		assertTrue(wSs[1] instanceof IWebService);
	}
	
	public void testHasChildrenObject() 
	{
		// IWebServiceProject
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		
		assertTrue(adapterFactory.hasChildren(children[0]));
		
		// ISEIChildList & IWebServiceChildList
		children = adapterFactory.getChildren(children[0]);
		
		assertTrue(adapterFactory.hasChildren(children[0]));
		assertTrue(adapterFactory.hasChildren(children[1]));
		
		// null parameter
		assertFalse(adapterFactory.hasChildren(null));
	}
	
	public void testChildrenIWebService()
	{
		IWebService ws = findWs("org.eclipse.test.modelsync1.WS1");
		assertNotNull(ws);
		assertTrue(adapterFactory.hasChildren(ws));
		assertEquals(1, adapterFactory.getChildren(ws).length);
		assertTrue(adapterFactory.getChildren(ws)[0] instanceof IServiceEndpointInterface);
		
		// check if adapted only once
		DOMAdapterFactoryContentProvider otherFactory = new DOMAdapterFactoryContentProvider();
		otherFactory.getChildren(ws);
		assertEquals(2, ws.eAdapters().size());
	}
	
	public void testChildrenIServiceEndpointInterface()
	{
		IServiceEndpointInterface sei = findSei("org.eclipse.test.modelsync1.Sei2");
		assertNotNull(sei);
		assertTrue(adapterFactory.hasChildren(sei));
		assertEquals(2, adapterFactory.getChildren(sei).length);
		assertTrue(adapterFactory.getChildren(sei)[0] instanceof IWebMethod);
		DOMAdapterFactoryContentProvider otherFactory = new DOMAdapterFactoryContentProvider();
		otherFactory.getChildren(sei);
		assertEquals(2, sei.eAdapters().size());
		
		IWebService ws = findWs("org.eclipse.test.modelsync1.WS2");
		assertNotNull(ws);
		assertFalse(adapterFactory.hasChildren(ws.getServiceEndpoint()));
		assertEquals(0, adapterFactory.getChildren(ws.getServiceEndpoint()).length);
		// check if adapted only once
		otherFactory.getChildren(ws.getServiceEndpoint());
		assertEquals(2, ws.getServiceEndpoint().eAdapters().size());
	}
	
	public void testChildrenWebMethod()
	{
		IServiceEndpointInterface sei = findSei("org.eclipse.test.modelsync1.Sei2");
		assertNotNull(sei);
		
		IWebMethod method = findMethod(sei, "voidMethodWithArgs");
		assertNotNull(method);	
		assertTrue(adapterFactory.hasChildren(method));
		assertEquals(1, adapterFactory.getChildren(method).length);
		assertTrue(adapterFactory.getChildren(method)[0] instanceof IWebParam);
		
		 method = findMethod(sei, "voidMethodWithNoArgsInParent");
		assertNotNull(method);
		assertFalse(adapterFactory.hasChildren(method));
		assertEquals(0, adapterFactory.getChildren(method).length);		
	}
	
	public void testChildrenWebParam()
	{
		IServiceEndpointInterface sei = findSei("org.eclipse.test.modelsync1.Sei2");
		assertNotNull(sei);
		
		IWebMethod method = findMethod(sei, "voidMethodWithArgs");
		assertNotNull(method);
		assertFalse(adapterFactory.hasChildren(method.getParameters().get(0)));
		assertEquals(0, adapterFactory.getChildren(method.getParameters().get(0)).length);
	}
	
	
	private IServiceEndpointInterface findSei(final String implName)
	{
		for(IServiceEndpointInterface sei : wsProject.getServiceEndpointInterfaces()) 
		{
			if(sei.getImplementation().equals(implName)) {
				return sei;
			}
		}
		
		return null;
	}	
	
	private IWebService findWs(final String implName)
	{
		for(IWebService ws : wsProject.getWebServices()) 
		{
			if(ws.getImplementation().equals(implName)) {
				return ws;
			}
		}
		
		return null;
	}	
	
	private IWebMethod findMethod(IServiceEndpointInterface sei, final String methodName) 
	{
		for(IWebMethod method : sei.getWebMethods()) 
		{
			if(method.getName().equals(methodName)) {
				return method;
			}
		}
		
		return null;
	}
	
	public void testGetWSProject()
	{
		adapterFactory = new DOMAdapterFactoryContentProvider();
		
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		assertNotNull(children);
	}
	
	public void testGetWsProjectDomNotLoaded()
	{
		adapterFactory = new DOMAdapterFactoryContentProvider() {
			@Override
			protected IWebServiceProject getWsProject(final IDOM dom, final IProject project) {
				return null;
			}
		};
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		assertEquals(1, children.length);
		assertTrue(children[0] instanceof ILoadingWsProject);
		children = adapterFactory.getChildren(children[0]);
		assertEquals(1, children.length);
		assertTrue(children[0] instanceof ILoadingDummy);
	}
	
	public void testGetWsProjectChildsDomNotLoaded()
	{
		ILoadingWsProject loadingProject = new ILoadingWsProject() {
			public IProject getProject() { return testPrj1.getProject(); }
		};
			
		Object[] children = adapterFactory.getChildren(loadingProject);
		assertEquals(1, children.length);
		assertTrue(children[0] instanceof ILoadingDummy);		
	}
	
	public void testGetSupportingRuntime()
	{
		DOMAdapterFactoryContentProviderMy provider = new DOMAdapterFactoryContentProviderMy();
		IWsDOMRuntimeExtension runtime = provider.getSupportingRuntime(testPrj1.getProject());
		assertNotNull(runtime);
	}
	
	protected class DOMAdapterFactoryContentProviderMy extends DOMAdapterFactoryContentProvider
	{
		@Override
		public IWsDOMRuntimeExtension getSupportingRuntime(final IProject project) {
			return super.getSupportingRuntime(project);
		}
	};
}
