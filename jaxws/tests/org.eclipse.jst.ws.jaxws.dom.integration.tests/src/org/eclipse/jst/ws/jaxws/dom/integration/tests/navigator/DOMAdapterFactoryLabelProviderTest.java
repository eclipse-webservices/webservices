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
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.DOMAdapterFactoryContentProvider;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.DOMAdapterFactoryLabelProvider;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.testutils.dom.WaitingDomUtil;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProjectsUtils;

public class DOMAdapterFactoryLabelProviderTest extends TestCase 
{
	private TestProject testPrj1; 
	
	protected JaxWsWorkspaceResource targetResource;
	protected DOMAdapterFactoryContentProvider adapterFactory;
	protected DOMAdapterFactoryLabelProvider labelProvider;
	
	private IWebServiceProject wsProject;
	private DomUtil domUtil;
	
	@Override
	public void setUp() throws Exception 
	{
		domUtil = new WaitingDomUtil();
		IProject ejbProject = TestProjectsUtils.createEjb3Project("DOMLblProvTestProject1" + System.currentTimeMillis());
		testPrj1 = new TestProject(ejbProject.getProject());
		final IPackageFragment modelSync1 = testPrj1.createPackage("org.eclipse.test.modelsync1");
		testPrj1.createType(modelSync1, "Sei1.java", "@javax.jws.WebService(name=\"Sei1Name\") public interface Sei1{\n" +
				"@javax.jws.WebMethod(name=\"parentMethod\") public void voidMethodWithNoArgsInParent();\n" +
				"}");
		testPrj1.createType(modelSync1, "Sei2.java", "@javax.jws.WebService(name=\"Sei2Name\") public interface Sei2 extends Sei1 {\n" +
				"@javax.jws.WebMethod(name=\"webMethod\") public void voidMethodWithNoArgsNotInParent();\n" +
				"}");
		testPrj1.createType(modelSync1, "WS1.java", "@javax.jws.WebService(serviceName=\"WS1Name\", endpointInterface=\"org.eclipse.test.modelsync1.Sei2\") public class WS1 {}");
		testPrj1.createType(modelSync1, "WS2.java", "@javax.jws.WebService(serviceName=\"WS2Name\", endpointInterface=\"org.eclipse.test.modelsync1.Sei2\") public class WS2 {}");
		
		targetResource = new JaxWsWorkspaceResource(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));
		targetResource.load(null);
		this.wsProject = domUtil.findProjectByName(targetResource.getDOM(), testPrj1.getProject().getName());
		assertNotNull(this.wsProject);
		
		labelProvider = new DOMAdapterFactoryLabelProvider();
		adapterFactory = new DOMAdapterFactoryContentProvider() 
		{
			@Override
			protected IWebServiceProject getWsProject(final IDOM dom, final IProject project) {
				return wsProject;
			}			
		};
	}

	public void testGetImageObject() 
	{
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		
		assertNotNull(labelProvider.getImage(children[0]));

		children = adapterFactory.getChildren(children[0]);
		
		assertNotNull(labelProvider.getImage(children[0]));
		assertNotNull(labelProvider.getImage(children[1]));
		
		Object[] sEIs = adapterFactory.getChildren(children[0]);
		
		assertNotNull(labelProvider.getImage(sEIs[0]));
		
		assertNotSame(labelProvider.getImage(null), labelProvider.getImage(sEIs[0]));
		assertNotSame(labelProvider.getImage(testPrj1), labelProvider.getImage(sEIs[0]));
		
		Object[] wSs = adapterFactory.getChildren(children[1]);
		
		assertNotNull(labelProvider.getImage(wSs[0]));
		
		assertNotSame(labelProvider.getImage(null), labelProvider.getImage(wSs[0]));
		assertNotSame(labelProvider.getImage(testPrj1), labelProvider.getImage(wSs[0]));
		
		Object[] webMethods = adapterFactory.getChildren(sEIs[0]);
		
		assertNotNull(labelProvider.getImage(webMethods[0]));
		
		assertNotSame(labelProvider.getImage(null), labelProvider.getImage(webMethods[0]));
		assertNotSame(labelProvider.getImage(testPrj1), labelProvider.getImage(webMethods[0]));
	}

	public void testGetTextObject() 
	{
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		
		assertNotNull(labelProvider.getText(children[0]));

		children = adapterFactory.getChildren(children[0]);
		
		assertNotNull(labelProvider.getText(children[0]));
		assertNotNull(labelProvider.getText(children[1]));
		
		Object[] sEIs = adapterFactory.getChildren(children[0]);
		
		assertNotNull(labelProvider.getText(sEIs[0]));
		
		assertNotSame(labelProvider.getText(null), labelProvider.getText(sEIs[0]));
		assertNotSame(labelProvider.getText(testPrj1), labelProvider.getText(sEIs[0]));
		
		Object[] wSs = adapterFactory.getChildren(children[1]);
		
		assertNotNull(labelProvider.getText(wSs[0]));
		
		assertNotSame(labelProvider.getText(null), labelProvider.getText(wSs[0]));
		assertNotSame(labelProvider.getText(testPrj1), labelProvider.getText(wSs[0]));
		
		Object[] webMethods = adapterFactory.getChildren(sEIs[0]);
		
		assertNotNull(labelProvider.getText(webMethods[0]));
		
		assertNotSame(labelProvider.getText(null), labelProvider.getText(webMethods[0]));
		assertNotSame(labelProvider.getText(testPrj1), labelProvider.getText(webMethods[0]));
	}
	
	public void testGetDescription()
	{
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		
		assertNotNull(labelProvider.getDescription(children[0]));

		children = adapterFactory.getChildren(children[0]);
		
		assertNotNull(labelProvider.getDescription(children[0]));
		assertNotNull(labelProvider.getDescription(children[1]));
		
		Object[] sEIs = adapterFactory.getChildren(children[0]);
		
		assertNotNull(labelProvider.getDescription(sEIs[0]));
		
		assertNotSame(labelProvider.getDescription(null), labelProvider.getDescription(sEIs[0]));
		assertNotSame(labelProvider.getDescription(testPrj1), labelProvider.getDescription(sEIs[0]));
		
		Object[] wSs = adapterFactory.getChildren(children[1]);
		
		assertNotNull(labelProvider.getDescription(wSs[0]));
		
		assertNotSame(labelProvider.getDescription(null), labelProvider.getDescription(wSs[0]));
		assertNotSame(labelProvider.getDescription(testPrj1), labelProvider.getDescription(wSs[0]));
		
		Object[] webMethods = adapterFactory.getChildren(sEIs[0]);
		
		assertNotNull(labelProvider.getDescription(webMethods[0]));
		
		assertNotSame(labelProvider.getDescription(null), labelProvider.getDescription(webMethods[0]));
		assertNotSame(labelProvider.getDescription(testPrj1), labelProvider.getDescription(webMethods[0]));
	}
}
