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
package org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.DOMAdapterFactoryContentProvider;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.ISEIChildList;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.IWebServiceChildList;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.actions.WSActionProvider;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProjectsUtils;
import org.eclipse.ui.actions.ActionContext;
import org.eclipse.ui.internal.navigator.NavigatorContentService;
import org.eclipse.ui.internal.navigator.extensions.CommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerSite;
import org.eclipse.ui.navigator.NavigatorContentServiceFactory;

@SuppressWarnings("restriction")
public class WSActionAbstractTest extends MockObjectTestCase 
{
	protected static TestProject testPrj1; 
	protected static TestProject testPrj2; 
	protected static TestProject testPrj3;
	protected static TestProject testPrj4;
	
	private IWebServiceProject wsProject1;
	private IWebServiceProject wsProject2;
	private IWebServiceProject wsProject3;
	private IWebServiceProject wsProject4;
	
	protected IPackageFragment modelSync1;
	protected IPackageFragment modelSync2;
	protected IPackageFragment modelSync3;
	protected IPackageFragment modelSync4;
	
	protected JaxWsWorkspaceResource targetResource;
	protected DOMAdapterFactoryContentProvider adapterFactory;
	
	protected WSActionProvider wsActionProv = null;
	
	@Override
	public void setUp() throws Exception
	{
		if(testPrj1==null)
		{
			IProject ejbProject = TestProjectsUtils.createEjb3Project("DOMWSActionTestProject1" + System.currentTimeMillis());
			testPrj1 = new TestProject(ejbProject.getProject());
			testPrj1.createSourceFolder("src");
			modelSync1 = testPrj1.createPackage("org.eclipse.test.modelsync1");
			testPrj1.createType(modelSync1, "Sei1.java", "@javax.jws.WebService(name=\"Sei1Name\") public interface Sei1{\n" +
					"@javax.jws.WebMethod(operationName=\"parentMethod\") public void voidMethodWithNoArgsInParent();\n" +
					"}");
			testPrj1.createType(modelSync1, "Sei2.java", "@javax.jws.WebService(name=\"Sei2Name\") public interface Sei2 extends Sei1 {\n" +
					"@javax.jws.WebMethod(operationName=\"webMethod\") public void voidMethodWithNoArgsNotInParent();\n" +
					"}");
			testPrj1.createType(modelSync1, "WS1.java", "@javax.ejb.Stateless\n" + "@javax.jws.WebService(serviceName=\"WS1Name\", endpointInterface=\"org.eclipse.test.modelsync1.Sei2\") public class WS1 {}");
			
		}
		
		if(testPrj2==null)
		{
			IProject webProject = TestProjectsUtils.createWeb25Project("DOMWSActionTestProject2" + System.currentTimeMillis());
			testPrj2 = new TestProject(webProject.getProject());
			testPrj2.createSourceFolder("src");
			modelSync2 = testPrj2.createPackage("org.eclipse.test.modelsync2");
			testPrj2.createType(modelSync2, "Sei1.java", "@javax.jws.WebService(name=\"Sei1Name\") public interface Sei1{\n" +
					"@javax.jws.WebMethod(operationName=\"parentMethod\") public void voidMethodWithNoArgsInParent();\n" +
					"}");
			testPrj2.createType(modelSync2, "Sei2.java", "@javax.jws.WebService(name=\"Sei2Name\") public interface Sei2 extends Sei1 {\n" +
					"@javax.jws.WebMethod(operationName=\"webMethod\") public void voidMethodWithNoArgsNotInParent();\n" +
					"}");
			testPrj2.createType(modelSync2, "WS1.java", "@javax.jws.WebService(serviceName=\"WS1Name\", endpointInterface=\"org.eclipse.test.modelsync1.Sei2\") public class WS1 {}");
		}
		
		if(testPrj3==null)
		{
			IProject ejbProject = TestProjectsUtils.createEjb3Project("DOMWSActionTestProject3" + System.currentTimeMillis());
			testPrj3 = new TestProject(ejbProject.getProject());
			testPrj3.createSourceFolder("src");
			modelSync3 = testPrj3.createPackage("org.eclipse.test.modelsync3");
			testPrj3.createType(modelSync3,
					"WS1.java",
					"@javax.jws.WebService(name=\"ImplicitSei1Name\") public class WS1 {\n"
					+ "@javax.jws.WebMethod(operationName=\"superParentMethod\") public void superParentMethod() {}\n"
					+ "}");
			
			testPrj3.createType(modelSync3,
					"WS2.java",
					"@javax.jws.WebService(name=\"ImplicitSei2Name\") public class WS2 extends WS1 {\n"
					+ "@javax.jws.WebMethod(operationName=\"parentMethod\") public void parentMethod() {}\n"
					+ "}");
			
			testPrj3.createType(modelSync3,
					"WS3.java",
					"@javax.jws.WebService(name=\"ImplicitSei3Name\") public class WS3 extends WS2 {\n"
					+ "@javax.jws.WebMethod(operationName=\"webMethod\") public void webMethod(int param) {}\n"
					+ "}");
		}

		if(testPrj4==null)
		{
			IProject webProject = TestProjectsUtils.createWeb25Project("DOMWSActionTestProject4" + System.currentTimeMillis());
			testPrj4 = new TestProject(webProject.getProject());
			testPrj4.createSourceFolder("src");
			modelSync4 = testPrj4.createPackage("org.eclipse.test.modelsync4");
			testPrj4.createType(modelSync4,
					"WS1.java",
					"@javax.jws.WebService(name=\"ImplicitSei1Name\") public class WS1 {\n"
					+ "@javax.jws.WebMethod(operationName=\"superParentMethod\") public void superParentMethod() {}\n"
					+ "}");
			
			testPrj4.createType(modelSync4,
					"WS2.java",
					"@javax.jws.WebService(name=\"ImplicitSei2Name\") public class WS2 extends WS1 {\n"
					+ "@javax.jws.WebMethod(operationName=\"parentMethod\") public void parentMethod() {}\n"
					+ "}");
			
			testPrj4.createType(modelSync4,
					"WS3.java",
					"@javax.jws.WebService(name=\"ImplicitSei3Name\") public class WS3 extends WS2 {\n"
					+ "@javax.jws.WebMethod(operationName=\"webMethod\") public void webMethod() {}\n"
					+ "}");
		}

		targetResource = new JaxWsWorkspaceResource(JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()));
		targetResource.load(null);
		
		Iterator<IWebServiceProject> wsProjects = targetResource.getDOM().getWebServiceProjects().iterator();
		
		while(wsProjects.hasNext())
		{
			IWebServiceProject wsProject = wsProjects.next();
			
			if(wsProject.getName().equals(testPrj1.getProject().getName()))
			{
				this.wsProject1 = wsProject;
			}
			else if(wsProject.getName().equals(testPrj2.getProject().getName()))
			{
				this.wsProject2 = wsProject;
			}
			else if(wsProject.getName().equals(testPrj3.getProject().getName()))
			{
				this.wsProject3 = wsProject;
			}
			else if(wsProject.getName().equals(testPrj4.getProject().getName()))
			{
				this.wsProject4 = wsProject;
			}
		}
		
		assertNotNull(this.wsProject1);
		assertNotNull(this.wsProject2);
		assertNotNull(this.wsProject3);
		assertNotNull(this.wsProject4);
		
		adapterFactory = new DOMAdapterFactoryContentProvider() 
		{
			@Override
			protected IWebServiceProject getWsProject(final IDOM dom, final IProject project) {
				if(project.getName().equals(wsProject1.getName()))
				{
					return wsProject1;
				}
				else if(project.getName().equals(wsProject2.getName()))
				{
					return wsProject2;
				}
				else if(project.getName().equals(wsProject3.getName()))
				{
					return wsProject3;
				}
				else if(project.getName().equals(wsProject4.getName()))
				{
					return wsProject4;
				}
				throw new junit.framework.AssertionFailedError("project " + project + "not found in DOM");
			}			
		};

	}
	
	protected IWebService getWSFromEjbProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		children = adapterFactory.getChildren(children[0]);

		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof IWebServiceChildList)
			{
				Object[] wSs = adapterFactory.getChildren(children[ii]);
				
				return (IWebService)wSs[0];
			}
		}
		
		return null;
	}
	
	protected IWebService getWSFromWebProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj2.getProject());
		children = adapterFactory.getChildren(children[0]);

		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof IWebServiceChildList)
			{
				Object[] wSs = adapterFactory.getChildren(children[ii]);
				
				return (IWebService)wSs[0];
			}
		}
		
		return null;
	}
	
	protected IServiceEndpointInterface getSEIExpFromEjbProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		children = adapterFactory.getChildren(children[0]);

		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof ISEIChildList)
			{
				Object[] sEIs = adapterFactory.getChildren(children[ii]);
				
				return (IServiceEndpointInterface)sEIs[0];
			}
		}
		
		return null;
	}
	
	protected IServiceEndpointInterface getSEIFromTestPrj1(String seiName)
	{
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		children = adapterFactory.getChildren(children[0]);

		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof ISEIChildList)
			{
				Object[] sEIs = adapterFactory.getChildren(children[ii]);
				
				for(int jj=0; jj<sEIs.length; jj++)
				{
					if(((IServiceEndpointInterface)sEIs[jj]).getName().equals(seiName))
					{
						return (IServiceEndpointInterface)sEIs[jj];
					}
				}
			}
		}
		
		return null;
	}
	
	protected IWebService getWSFromTestPrj(TestProject testPrj, String wsName)
	{
		Object[] children = adapterFactory.getChildren(testPrj.getProject());
		children = adapterFactory.getChildren(children[0]);

		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof IWebServiceChildList)
			{
				Object[] wSs = adapterFactory.getChildren(children[ii]);
				
				for(int jj=0; jj<wSs.length; jj++)
				{
					if(((IWebService)wSs[jj]).getImplementation().equals(wsName))
					{
						return (IWebService)wSs[jj];
					}
				}
			}
		}
		
		return null;
	}
	
//	protected IWebService getWSFromTestPrj1(String wsName)
//	{
//		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
//		children = adapterFactory.getChildren(children[0]);
//
//		for(int ii=0; ii<children.length; ii++)
//		{
//			if(children[ii] instanceof IWebServiceChildList)
//			{
//				Object[] wSs = adapterFactory.getChildren(children[ii]);
//				
//				for(int jj=0; jj<wSs.length; jj++)
//				{
//					if(((IWebService)wSs[jj]).getName().equals(wsName))
//					{
//						return (IWebService)wSs[jj];
//					}
//				}
//			}
//		}
//		
//		return null;
//	}
	
	protected IServiceEndpointInterface getSEIFromTestPrj3(String seiName)
	{
		Object[] children = adapterFactory.getChildren(testPrj3.getProject());
		children = adapterFactory.getChildren(children[0]);

		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof IWebServiceChildList)
			{
				Object[] wSs = adapterFactory.getChildren(children[ii]);
				
				for(int jj=0; jj<wSs.length; jj++)
				{
					if(((IWebService)wSs[jj]).getServiceEndpoint().getName().equals(seiName))
					{
						return ((IWebService)wSs[jj]).getServiceEndpoint();
					}
				}
			}
		}
		
		return null;
	}
	
	protected IServiceEndpointInterface getSEIExpFromWebProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj2.getProject());
		children = adapterFactory.getChildren(children[0]);

		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof ISEIChildList)
			{
				Object[] sEIs = adapterFactory.getChildren(children[ii]);
				
				return (IServiceEndpointInterface)sEIs[0];
			}
		}
		
		return null;
	}
	
	protected IServiceEndpointInterface getSEIImpFromEjbProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj3.getProject());
		children = adapterFactory.getChildren(children[0]);

		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof IWebServiceChildList)
			{
				Object[] wSs = adapterFactory.getChildren(children[ii]);
				
				return ((IWebService)wSs[0]).getServiceEndpoint();
			}
		}
		
		return null;
	}
	
	protected IServiceEndpointInterface getSEIImpFromWebProject()
	{
		
		Object[] children = adapterFactory.getChildren(testPrj4.getProject());
		children = adapterFactory.getChildren(children[0]);

		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof IWebServiceChildList)
			{
				Object[] wSs = adapterFactory.getChildren(children[ii]);
				
				return ((IWebService)wSs[0]).getServiceEndpoint();
			}
		}
		
		return null;
	}
	
	protected IWebMethod getWSMethodInheritedFromEjbProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj3.getProject());
		children = adapterFactory.getChildren(children[0]);
		Object[] wSs = null;
		Object[] webMethods = null;
		
		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof IWebServiceChildList)
			{
				wSs = adapterFactory.getChildren(children[ii]);
				
				break;
			}
		}
		
		assertNotNull(wSs);
		
		for(int ii=0; ii<wSs.length; ii++)
		{
			if(((IWebService)wSs[ii]).getImplementation().equals("org.eclipse.test.modelsync3.WS3"))
			{
				webMethods = adapterFactory.getChildren(((IWebService)wSs[ii]).getServiceEndpoint());
				
				break;
			}
		}
		
		assertNotNull(webMethods);
		
		for(int ii=0; ii<webMethods.length; ii++)
		{
			if(((IWebMethod)webMethods[ii]).getName().equals("superParentMethod"))
			{
				return (IWebMethod)webMethods[ii];
			}
		}
		
		return null;
	}
	
	protected IWebMethod getWSMethodInheritedFromWebProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj4.getProject());
		children = adapterFactory.getChildren(children[0]);

		Object[] wSs = null;
		Object[] webMethods = null;
		
		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof IWebServiceChildList)
			{
				wSs = adapterFactory.getChildren(children[ii]);
				
				break;
			}
		}
		
		assertNotNull(wSs);
		
		for(int ii=0; ii<wSs.length; ii++)
		{
			if(((IWebService)wSs[ii]).getImplementation().equals("org.eclipse.test.modelsync4.WS3"))
			{
				webMethods = adapterFactory.getChildren(((IWebService)wSs[ii]).getServiceEndpoint());
				
				break;
			}
		}
		
		assertNotNull(webMethods);
		
		for(int ii=0; ii<webMethods.length; ii++)
		{
			if(((IWebMethod)webMethods[ii]).getName().equals("superParentMethod"))
			{
				return (IWebMethod)webMethods[ii];
			}
		}
		
		return null;
	}
	
	protected IWebMethod getWSMethodNotInheritedFromEjbProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj3.getProject());
		children = adapterFactory.getChildren(children[0]);

		Object[] wSs = null;
		Object[] webMethods = null;
		
		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof IWebServiceChildList)
			{
				wSs = adapterFactory.getChildren(children[ii]);
				
				break;
			}
		}
		
		assertNotNull(wSs);
		
		for(int ii=0; ii<wSs.length; ii++)
		{
			if(((IWebService)wSs[ii]).getImplementation().equals("org.eclipse.test.modelsync3.WS3"))
			{
				webMethods = adapterFactory.getChildren(((IWebService)wSs[ii]).getServiceEndpoint());
				
				break;
			}
		}
		
		assertNotNull(webMethods);
		
		for(int ii=0; ii<webMethods.length; ii++)
		{
			if(((IWebMethod)webMethods[ii]).getName().equals("webMethod"))
			{
				return (IWebMethod)webMethods[ii];
			}
		}
		
		return null;
	}
	
	protected IWebMethod getWSMethodNotInheritedFromWebProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj4.getProject());
		children = adapterFactory.getChildren(children[0]);

		Object[] wSs = null;
		Object[] webMethods = null;
		
		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof IWebServiceChildList)
			{
				wSs = adapterFactory.getChildren(children[ii]);
				
				break;
			}
		}
		
		assertNotNull(wSs);
		
		for(int ii=0; ii<wSs.length; ii++)
		{
			if(((IWebService)wSs[ii]).getImplementation().equals("org.eclipse.test.modelsync4.WS3"))
			{
				webMethods = adapterFactory.getChildren(((IWebService)wSs[ii]).getServiceEndpoint());
				
				break;
			}
		}
		
		assertNotNull(webMethods);
		
		for(int ii=0; ii<webMethods.length; ii++)
		{
			if(((IWebMethod)webMethods[ii]).getName().equals("webMethod"))
			{
				return (IWebMethod)webMethods[ii];
			}
		}
		
		return null;
	}
	
	protected IWebMethod getSEIMethodInheritedFromEjbProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		children = adapterFactory.getChildren(children[0]);

		Object[] sEIs = null;
		Object[] webMethods = null;
		
		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof ISEIChildList)
			{
				sEIs = adapterFactory.getChildren(children[ii]);
				
				break;
			}
		}
		
		assertNotNull(sEIs);
		
		for(int ii=0; ii<sEIs.length; ii++)
		{
			if(((IServiceEndpointInterface)sEIs[ii]).getName().equals("Sei2Name"))
			{
				webMethods = adapterFactory.getChildren(((IServiceEndpointInterface)sEIs[ii]));
				
				break;
			}
		}
		
		assertNotNull(webMethods);
		
		for(int ii=0; ii<webMethods.length; ii++)
		{
			if(((IWebMethod)webMethods[ii]).getName().equals("parentMethod"))
			{
				return (IWebMethod)webMethods[ii];
			}
		}
		
		return null;
	}
	
	protected IWebMethod getSEIMethodInheritedFromWebProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj2.getProject());
		children = adapterFactory.getChildren(children[0]);

		Object[] sEIs = null;
		Object[] webMethods = null;
		
		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof ISEIChildList)
			{
				sEIs = adapterFactory.getChildren(children[ii]);
				
				break;
			}
		}
		
		assertNotNull(sEIs);
		
		for(int ii=0; ii<sEIs.length; ii++)
		{
			if(((IServiceEndpointInterface)sEIs[ii]).getName().equals("Sei2Name"))
			{
				webMethods = adapterFactory.getChildren(((IServiceEndpointInterface)sEIs[ii]));
				
				break;
			}
		}
		
		assertNotNull(webMethods);
		
		for(int ii=0; ii<webMethods.length; ii++)
		{
			if(((IWebMethod)webMethods[ii]).getName().equals("parentMethod"))
			{
				return (IWebMethod)webMethods[ii];
			}
		}
		
		return null;
	}
	
	protected IWebMethod getSEIMethodNotInheritedFromEjbProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj1.getProject());
		children = adapterFactory.getChildren(children[0]);

		Object[] sEIs = null;
		Object[] webMethods = null;
		
		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof ISEIChildList)
			{
				sEIs = adapterFactory.getChildren(children[ii]);
				
				break;
			}
		}
		
		assertNotNull(sEIs);
		
		for(int ii=0; ii<sEIs.length; ii++)
		{
			if(((IServiceEndpointInterface)sEIs[ii]).getName().equals("Sei2Name"))
			{
				webMethods = adapterFactory.getChildren(((IServiceEndpointInterface)sEIs[ii]));
				
				break;
			}
		}
		
		assertNotNull(webMethods);
		
		for(int ii=0; ii<webMethods.length; ii++)
		{
			if(((IWebMethod)webMethods[ii]).getName().equals("webMethod"))
			{
				return (IWebMethod)webMethods[ii];
			}
		}
		
		return null;
	}
	
	protected IWebMethod getSEIMethodNotInheritedFromWebProject()
	{
		Object[] children = adapterFactory.getChildren(testPrj2.getProject());
		children = adapterFactory.getChildren(children[0]);

		Object[] sEIs = null;
		Object[] webMethods = null;
		
		for(int ii=0; ii<children.length; ii++)
		{
			if(children[ii] instanceof ISEIChildList)
			{
				sEIs = adapterFactory.getChildren(children[ii]);
				
				break;
			}
		}
		
		assertNotNull(sEIs);
		
		for(int ii=0; ii<sEIs.length; ii++)
		{
			if(((IServiceEndpointInterface)sEIs[ii]).getName().equals("Sei2Name"))
			{
				webMethods = adapterFactory.getChildren(((IServiceEndpointInterface)sEIs[ii]));
				
				break;
			}
		}
		
		assertNotNull(webMethods);
		
		for(int ii=0; ii<webMethods.length; ii++)
		{
			if(((IWebMethod)webMethods[ii]).getName().equals("webMethod"))
			{
				return (IWebMethod)webMethods[ii];
			}
		}
		
		return null;
	}
	
	protected void initWsActionProviderWithTreeSelection(Object[] selection)
	{
		wsActionProv = new WSActionProvider();
		
		Mock<TreeViewer> treeViewer = mock(TreeViewer.class);
		CommonActionExtensionSite cAExtSite = createCommonActionExtensionSiteMock(treeViewer.proxy());

		wsActionProv.init(cAExtSite);
		
		TreePath path = new TreePath(selection);
		StructuredSelection sel = new TreeSelection(path);
		ActionContext actCnt = new ActionContext(sel);
		
		wsActionProv.setContext(actCnt);
	}
	
	protected void initWsActionProviderWithStructuredSelection(Object[] selection)
	{
		wsActionProv = new WSActionProvider();

		Mock<TreeViewer> treeViewer = mock(TreeViewer.class);
		CommonActionExtensionSite cAExtSite = createCommonActionExtensionSiteMock(treeViewer.proxy());

		wsActionProv.init(cAExtSite);
		
		StructuredSelection sel = new StructuredSelection(selection);
		ActionContext actCnt = new ActionContext(sel);
		
		wsActionProv.setContext(actCnt);
	}
	
	protected IWebServiceProject getWsProject(IProject project)
	{
		Iterator<IWebServiceProject> wsProjects = targetResource.getDOM().getWebServiceProjects().iterator();
		
		while(wsProjects.hasNext())
		{
			IWebServiceProject wsProject = wsProjects.next();
			
			if(wsProject.getName().equals(project.getName()))
			{
				return wsProject;
			}
		}
		throw new junit.framework.AssertionFailedError("project " + project + "not found in DOM");
	}
	
	protected CommonActionExtensionSite createCommonActionExtensionSiteMock(final StructuredViewer viewer)
	{
		final NavigatorContentService contentService = (NavigatorContentService)NavigatorContentServiceFactory.INSTANCE.createContentService("myviewer");
		
		return new CommonActionExtensionSite("myextensionid", (ICommonViewerSite)mock(ICommonViewerSite.class).proxy(), contentService, viewer);
	}
	
	protected IContributionItem createContributionItemMock(final String itemId)
	{
		Mock<IContributionItem> cntrbItem = mock(IContributionItem.class);
		cntrbItem.stubs().method("getId").will(returnValue(itemId));
		cntrbItem.stubs().method("setParent").will(returnValue(null));
		cntrbItem.stubs().method("isDynamic").will(returnValue(false));
		return cntrbItem.proxy();
	}
	
	protected void fillMenuWithContributionItems(final MenuManager menu, final String... itemIds)
	{
		for(String id : itemIds)
		{
			IContributionItem cntrbItem = createContributionItemMock(id);
			menu.add(cntrbItem);
		}
	}
}
