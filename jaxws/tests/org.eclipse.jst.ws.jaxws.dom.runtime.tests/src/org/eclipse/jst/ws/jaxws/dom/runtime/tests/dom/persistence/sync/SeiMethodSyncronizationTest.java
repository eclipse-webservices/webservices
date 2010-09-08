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

import junit.framework.TestCase;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.DomTestUtils;
import org.eclipse.jst.ws.jaxws.testutils.dom.WaitingDomUtil;
import org.eclipse.jst.ws.jaxws.testutils.jobs.JobUtils;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;
import org.eclipse.jst.ws.jaxws.utils.resources.FileUtils;

public class SeiMethodSyncronizationTest extends TestCase {

	private TestProject testPrj1; 
	private JaxWsWorkspaceResource target;
	private IJavaModel javaModel;
	private ICompilationUnit sei1CU;
	private ICompilationUnit someIntfCU;
	
	private IWebServiceProject wsPrj1;
	private IWebService ws1;
	private IServiceEndpointInterface sei1;
	
	private DomTestUtils testUtil = new DomTestUtils();
	private DomUtil domUtil;
		
	private static final String ws1ImplName = "com.sap.test.modelsync1.WS1";
//	private static final String someIntfImplName = "com.sap.test.modelsync1.SomeIntf";
	private static final String sei1ImplName = "com.sap.test.modelsync1.Sei1";
	
	
	public void setUp() throws Exception
	{
		domUtil = new WaitingDomUtil();
		javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		target = new JaxWsWorkspaceResource(javaModel) 
		{

			@Override
			public boolean approveProject(IJavaProject prj) {
				return prj.getElementName().equals(testPrj1.getJavaProject().getElementName());
			}
		};
		testPrj1 = new TestProject("TestProject1");
		testPrj1.createSourceFolder("src");
		final IPackageFragment modelSync1 = testPrj1.createPackage("com.sap.test.modelsync1");
		
		someIntfCU = testPrj1.createType(modelSync1, "SomeIntf.java", "public interface SomeIntf{\n" +
				"public void voidMethodWithNoArgsNoAnnotationInParent();\n" +
				"}").getCompilationUnit();
		
		testPrj1.createType(modelSync1, "Sei1.java", "@javax.jws.WebService(name=\"Sei1Name\") public interface Sei1 extends SomeIntf {\n" +
				"public void voidMethodWithNoArgsNoAnnotation();\n" +
				"@javax.jws.WebMethod(operationName=\"webName\", exclude=true) public void voidMethodWithNoArgsWithAnnotation();\n" +
				"}");
		testPrj1.createType(modelSync1, "WS1.java", "@javax.jws.WebService(serviceName=\"WS1Name\", endpointInterface=\"com.sap.test.modelsync1.Sei1\") public class WS1 {}");
		JobUtils.waitForJobs();
		
		target.load(null);
		assertEquals("One projects were defined but a different number were found in the DOM", 1, target.getDOM().getWebServiceProjects().size());
		assertEquals(1, target.getDOM().getWebServiceProjects().get(0).getServiceEndpointInterfaces().size());
		assertEquals(1, target.getDOM().getWebServiceProjects().get(0).getWebServices().size());
		wsPrj1 = domUtil.findProjectByName(target.getDOM(), testPrj1.getJavaProject().getElementName());
		assertNotNull(wsPrj1);
		ws1 = domUtil.findWsByImplName(wsPrj1, ws1ImplName);
		assertNotNull(ws1);
		sei1 = domUtil.findSeiByImplName(wsPrj1, sei1ImplName);
		assertNotNull(sei1);
		sei1CU = testPrj1.getJavaProject().findType(sei1.getImplementation()).getCompilationUnit();
	}
	
	public void test_voidMethodWithNoArgsNoAnnotationSynched() throws JavaModelException
	{
		final IMethod methodElem = testPrj1.getJavaProject().findType(sei1.getImplementation()).getMethod("voidMethodWithNoArgsNoAnnotation", new String[0]);
		final IWebMethod wm = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(methodElem));
		assertNotNull(wm);
		assertEquals(methodElem.getElementName(), wm.getName());
		assertTrue(wm.getParameters().isEmpty());
		assertEquals(domUtil.calcImplementation(methodElem), wm.getImplementation());
		assertFalse(wm.isExcluded());
	}
	
	public void test_voidMethodWithNoArgsWithAnnotationSynched() throws JavaModelException
	{
		final IMethod methodElem = testPrj1.getJavaProject().findType(sei1.getImplementation()).getMethod("voidMethodWithNoArgsWithAnnotation", new String[0]);
		final IWebMethod wm = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(methodElem));
		assertNotNull(wm);
		assertEquals("webName", wm.getName());
		assertTrue(wm.getParameters().isEmpty());
		assertEquals(domUtil.calcImplementation(methodElem), wm.getImplementation());
		assertTrue(wm.isExcluded());
	}
	
	public void test_webNameChangedSynched() throws JavaModelException
	{
		final IMethod methodElem = testPrj1.getJavaProject().findType(sei1.getImplementation()).getMethod("voidMethodWithNoArgsWithAnnotation", new String[0]);
		final IWebMethod wm = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(methodElem));
		assertNotNull(wm);
		assertEquals("webName", wm.getName());
		assertTrue(wm.getParameters().isEmpty());
		assertEquals(domUtil.calcImplementation(methodElem), wm.getImplementation());
		assertTrue(wm.isExcluded());
		target.startSynchronizing();
		FileUtils.getInstance().setCompilationUnitContent(sei1CU, "@javax.jws.WebService(name=\"Sei1Name\") public interface Sei1 {\n" +
				"@javax.jws.WebMethod(operationName=\"webNameNew\", exclude=true) public void voidMethodWithNoArgsWithAnnotation();\n" +
				"}", false, null);
		JobUtils.waitForJobs();
		
		assertEquals("webNameNew", wm.getName());
		assertTrue(wm.getParameters().isEmpty());
		assertEquals(domUtil.calcImplementation(methodElem), wm.getImplementation());
		assertTrue(wm.isExcluded());
	}
	
	public void test_excludedChangedSynched() throws JavaModelException
	{
		final IMethod methodElem = testPrj1.getJavaProject().findType(sei1.getImplementation()).getMethod("voidMethodWithNoArgsWithAnnotation", new String[0]);
		final IWebMethod wm = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(methodElem));
		assertNotNull(wm);
		assertEquals("webName", wm.getName());
		assertTrue(wm.getParameters().isEmpty());
		assertEquals(domUtil.calcImplementation(methodElem), wm.getImplementation());
		assertTrue(wm.isExcluded());
		target.startSynchronizing();
		FileUtils.getInstance().setCompilationUnitContent(sei1CU, "@javax.jws.WebService(name=\"Sei1Name\") public interface Sei1 {\n" +
				"@javax.jws.WebMethod(operationName=\"webName\", exclude=false) public void voidMethodWithNoArgsWithAnnotation();\n" +
				"}", false, null);
		JobUtils.waitForJobs();
		
		assertEquals("webName", wm.getName());
		assertTrue(wm.getParameters().isEmpty());
		assertEquals(domUtil.calcImplementation(methodElem), wm.getImplementation());
		assertFalse(wm.isExcluded());
	}

	public void test_deletedMethodSynced() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod methodToDelete = testPrj1.getJavaProject().findType(sei1.getImplementation()).getMethod("voidMethodWithNoArgsNoAnnotation", new String[0]);
		final String methodToDeleteImpl= domUtil.calcImplementation(methodToDelete);
	
		FileUtils.getInstance().setCompilationUnitContent(sei1CU, "@javax.jws.WebService(name=\"Sei1Name\") public interface Sei1 {\n" +
				"@javax.jws.WebMethod(operationName=\"webName\", exclude=true) public void voidMethodWithNoArgsWithAnnotation();\n" +
				"}", false, null);
		JobUtils.waitForJobs();
		
		assertNull(domUtil.findWebMethodByImpl(sei1, methodToDeleteImpl));
	}
	
	public void test_overloadedMethodLoadedProperly() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod newMethod = sei1CU.findPrimaryType().createMethod("@javax.jws.WebMethod(operationName=\"webName\", exclude=true) public void voidMethodWithNoArgsWithAnnotation(int i);", null, false, null);
		JobUtils.waitForJobs();
		
		final IWebMethod newWebMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(newMethod));
		assertNotNull(newWebMethod);
		final IMethod oldMethod = sei1CU.findPrimaryType().getMethod("voidMethodWithNoArgsWithAnnotation", new String[0]);
		assertNotSame(newWebMethod, domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(oldMethod)));
	}
	
	public void test_MethodsFromSuperTypeLoaded() throws JavaModelException
	{
		final IMethod parentMethod = someIntfCU.findPrimaryType().getMethod("voidMethodWithNoArgsNoAnnotationInParent", new String[0]);
		assertNotNull(domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(parentMethod)));
	}
	
	public void test_OverrideMethodsLoadedProperly() throws JavaModelException
	{
		target.startSynchronizing();
		sei1CU.findPrimaryType().createMethod("@javax.jws.WebMethod(operationName=\"webName\", exclude=true) public void voidMethodWithNoArgsNoAnnotationInParent();", null, false, null);
		JobUtils.waitForJobs();

		final IMethod overridenMethod = sei1CU.findPrimaryType().getMethod("voidMethodWithNoArgsNoAnnotationInParent",  new String[0]);
		final IWebMethod wm = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(overridenMethod));
		assertNotNull(wm);
		assertEquals("webName", wm.getName());
		assertTrue(wm.isExcluded());
	}
	
	public void test_ChangedMethodInParentInterfaceSynched() throws JavaModelException
	{
		target.startSynchronizing();
		someIntfCU.getBuffer().save(null, false);
		JobUtils.waitForJobs();
		
		final IMethod parentMethod = someIntfCU.findPrimaryType().getMethod("voidMethodWithNoArgsNoAnnotationInParent", new String[0]);
		final IWebMethod wm = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(parentMethod));
		assertEquals("voidMethodWithNoArgsNoAnnotationInParent", wm.getName());
		assertFalse(wm.isExcluded());
		FileUtils.getInstance().setCompilationUnitContent(someIntfCU, "public interface SomeIntf{\n" +
				"@javax.jws.WebMethod(operationName=\"MethodInParentClassWithNewName\", exclude=true) public void voidMethodWithNoArgsNoAnnotationInParent();\n" +
				"}", false, null);
		JobUtils.waitForJobs();
		
		assertNotNull(wm);
		assertEquals("MethodInParentClassWithNewName", wm.getName());
		assertTrue(wm.isExcluded());
	}
	
	public void test_ChangedSoapBindingSynched() throws Exception
	{
		target.startSynchronizing();	
		testUtil.setContents(sei1CU, "@javax.jws.WebService() public interface Sei1 { public void soapBindingMethod(); }");
		JobUtils.waitForJobs();
		
		final IMethod newMethod = sei1CU.findPrimaryType().getMethod("soapBindingMethod", new String[0]);
		final IWebMethod newWebMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(newMethod));
		assertNotNull(newWebMethod);
		assertEquals(SOAPBindingStyle.DOCUMENT, newWebMethod.getSoapBindingStyle());
		assertEquals(SOAPBindingUse.LITERAL, newWebMethod.getSoapBindingUse());
		assertEquals(SOAPBindingParameterStyle.WRAPPED, newWebMethod.getSoapBindingParameterStyle());
		
		testUtil.setContents(sei1CU, "@javax.jws.WebService() public interface Sei1 { \n" +
				"@javax.jws.soap.SOAPBinding(style=Style.RPC, use=Use.ENCODED, parameterStyle=ParameterStyle.BARE)\n" +
				"public void soapBindingMethod(); }");
		JobUtils.waitForJobs();
		
		assertEquals(SOAPBindingStyle.RPC, newWebMethod.getSoapBindingStyle());
		assertEquals(SOAPBindingUse.ENCODED, newWebMethod.getSoapBindingUse());
		assertEquals(SOAPBindingParameterStyle.BARE, newWebMethod.getSoapBindingParameterStyle());
	}
	
	public void test_SoapBindingOnSeiSynched() throws Exception
	{
		target.startSynchronizing();	
		testUtil.setContents(sei1CU, "@javax.jws.soap.SOAPBinding(style=Style.RPC, use=Use.ENCODED, parameterStyle=ParameterStyle.BARE)\n" + 
				"@javax.jws.WebService() public interface Sei1 { public void soapBindingMethod(); }");
		JobUtils.waitForJobs();
		
		final IMethod newMethod = sei1CU.findPrimaryType().getMethod("soapBindingMethod", new String[0]);
		final IWebMethod newWebMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(newMethod));
		assertEquals(SOAPBindingStyle.RPC, newWebMethod.getSoapBindingStyle());
		assertEquals(SOAPBindingUse.ENCODED, newWebMethod.getSoapBindingUse());
		assertEquals(SOAPBindingParameterStyle.BARE, newWebMethod.getSoapBindingParameterStyle());
	}
	
	public void test_SoapBindingOnSeiOverlappingAnnotationOnMethodSynched() throws Exception
	{
		target.startSynchronizing();
		testUtil.setContents(sei1CU, "@javax.jws.soap.SOAPBinding(style=Style.RPC, use=Use.ENCODED, parameterStyle=ParameterStyle.BARE)\n" + 
				"@javax.jws.WebService() public interface Sei1 { " +
					"@javax.jws.soap.SOAPBinding(style=Style.DOCUMENT, use=Use.LITERAL, parameterStyle=ParameterStyle.WRAPPED)" +
					"public void soapBindingMethod();" + 
				"}");
		JobUtils.waitForJobs();
		
		final IMethod newMethod = sei1CU.findPrimaryType().getMethod("soapBindingMethod", new String[0]);
		final IWebMethod newWebMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(newMethod));
		assertEquals(SOAPBindingStyle.DOCUMENT, newWebMethod.getSoapBindingStyle());
		assertEquals(SOAPBindingUse.LITERAL, newWebMethod.getSoapBindingUse());
		assertEquals(SOAPBindingParameterStyle.WRAPPED, newWebMethod.getSoapBindingParameterStyle());
	}	
	
	public void test_SoapBindingOnSeiWrongOverlappingAnnotationOnMethodSynched() throws Exception
	{
		target.startSynchronizing();	
		testUtil.setContents(sei1CU, "@javax.jws.soap.SOAPBinding(style=Style.RPC, use=Use.ENCODED, parameterStyle=ParameterStyle.BARE)\n" + 
				"@javax.jws.WebService() public interface Sei1 { " +
					"@javax.jws.soap.SOAPBinding(style=Style.DOCUMENT1, use=Use.LITERAL1, parameterStyle=ParameterStyle.WRAPPED1)" +
					"public void soapBindingMethod();" + 
				"}");
		JobUtils.waitForJobs();
		
		final IMethod newMethod = sei1CU.findPrimaryType().getMethod("soapBindingMethod", new String[0]);
		final IWebMethod newWebMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(newMethod));
		assertEquals(SOAPBindingStyle.RPC, newWebMethod.getSoapBindingStyle());
		assertEquals(SOAPBindingUse.ENCODED, newWebMethod.getSoapBindingUse());
		assertEquals(SOAPBindingParameterStyle.BARE, newWebMethod.getSoapBindingParameterStyle());
	}	
}
