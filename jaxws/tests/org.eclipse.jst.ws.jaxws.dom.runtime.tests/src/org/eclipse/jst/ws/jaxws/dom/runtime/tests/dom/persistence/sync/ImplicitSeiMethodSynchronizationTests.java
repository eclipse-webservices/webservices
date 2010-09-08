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
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IProjectSelector;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.DomTestUtils;
import org.eclipse.jst.ws.jaxws.testutils.dom.WaitingDomUtil;
import org.eclipse.jst.ws.jaxws.testutils.jobs.JobUtils;
import org.eclipse.jst.ws.jaxws.testutils.project.TestProject;

public class ImplicitSeiMethodSynchronizationTests extends TestCase
{

	private TestProject testPrj1;

	private JaxWsWorkspaceResource target;

	private IJavaModel javaModel;

	private IType baseClassWithWsAnnot;

	private IType baseClassWithoutWsAnnot;

	private IType wsClass;

	private IWebServiceProject wsPrj1;

	private IWebService ws1;

	private DomTestUtils util = new DomTestUtils();
	private DomUtil domUtil;

	private static final String ws1ImplName = "com.sap.test.modelsync1.WS1";

	public void setUp() throws Exception
	{
		domUtil = new WaitingDomUtil();
		javaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		target = new JaxWsWorkspaceResource(javaModel)
		{

			@Override
			public IProjectSelector[] getProjectSelectors()
			{
				return new IProjectSelector[] { new IProjectSelector()
				{

					public boolean approve(IJavaProject prj)
					{
						return prj.getElementName().equals(testPrj1.getJavaProject().getElementName());
					}
				} };
			}

		};
		testPrj1 = new TestProject("TestProject1");
		testPrj1.createSourceFolder("src");
		final IPackageFragment modelSync1 = testPrj1.createPackage("com.sap.test.modelsync1");

		baseClassWithoutWsAnnot = testPrj1.createType(modelSync1, "BaseClassWithoutWsAnnot.java", "public class BaseClassWithoutWsAnnot {\n"
										+ "@javax.jws.WebMethod public void annotationInBaseNoWSClass(){}\n"
										+ "public void noAnnotationInBaseNoWSClass(){}\n" + "void nonPublicInBaseNoWSClass() {}\n"
										+ "@javax.jws.WebMethod public void toOverride() {}\n"
										+ "@javax.jws.WebMethod(exclude=true) public void excludedPublicInBaseNoWSClass() {}\n" + "}");

		baseClassWithWsAnnot = testPrj1
										.createType(
																		modelSync1,
																		"BaseClassWithWsAnnot.java",
																		"@javax.jws.WebService public class BaseClassWithWsAnnot extends BaseClassWithoutWsAnnot {\n"
																										+ "public void noWMAnnoationInBaseWSClass() {}\n"
																										+ "void nonPublicInBaseWSClass() {}\n"
																										+ "@javax.jws.WebMethod(operationName=\"nameInBaseClassWithAnnot\") public void toOverride() {}\n"
																										+ "@javax.jws.WebMethod(exclude=true) public void excludedPublicInBaseWSClass() {}\n"
																										+ "}");

		wsClass = testPrj1
										.createType(
																		modelSync1,
																		"WS1.java",
																		"@javax.jws.WebService(name=\"ImplicitSei1Name\") public class WS1 extends BaseClassWithWsAnnot {\n"
																										+ "public void noWMAnnotationInWSClass();\n"
																										+ "void nonPublicInWSClass() {}\n"
																										+ "@javax.jws.WebMethod(operationName=\"nameInWsClass\") public void toOverride() {}\n"
																										+ "@javax.jws.WebMethod(exclude=true) public void excludedPublicInWSClass() {}\n"
																										+ "@javax.jws.WebMethod(operationName=\"webName\", exclude=true) public void voidMethodWithNoArgsWithAnnotation(){}\n"
																										+ "}");
		
		JobUtils.waitForJobs();
		
		target.load(null);
		assertEquals("One projects were defined but a different number were found in the DOM", 1, target.getDOM().getWebServiceProjects().size());
		assertEquals(2, target.getDOM().getWebServiceProjects().get(0).getWebServices().size());
		wsPrj1 = domUtil.findProjectByName(target.getDOM(), testPrj1.getJavaProject().getElementName());
		assertNotNull(wsPrj1);
		ws1 = domUtil.findWsByImplName(wsPrj1, ws1ImplName);
		assertNotNull(ws1);
	}

	public void test_methodNoAnnotationSynched() throws JavaModelException
	{
		final IMethod methodElem = util.getExistingMethod(wsClass, "noWMAnnotationInWSClass");
		final IWebMethod wm = findWebMethodByImpl(ws1.getServiceEndpoint(), calcImplementation(methodElem));
		assertNotNull(wm);
		assertEquals(methodElem.getElementName(), wm.getName());
		assertTrue(wm.getParameters().isEmpty());
		assertEquals(calcImplementation(methodElem), wm.getImplementation());
		assertFalse(wm.isExcluded());
	}

	public void test_methodNoAnnotationInParentWithWSAnnotationSynched() throws JavaModelException
	{
		final IMethod methodElem = util.getExistingMethod(baseClassWithWsAnnot, "noWMAnnoationInBaseWSClass");
		final IWebMethod wm = findWebMethodByImpl(ws1.getServiceEndpoint(), calcImplementation(methodElem));
		assertNotNull(wm);
		assertEquals(methodElem.getElementName(), wm.getName());
		assertTrue(wm.getParameters().isEmpty());
		assertEquals(calcImplementation(methodElem), wm.getImplementation());
		assertFalse(wm.isExcluded());
	}

	public void test_methodNoAnnotationInParentWithoutWsAnnotationNotSynched() throws JavaModelException
	{
		final IMethod methodElem = util.getExistingMethod(baseClassWithoutWsAnnot, "noAnnotationInBaseNoWSClass");
		final IWebMethod wm = findWebMethodByImpl(ws1.getServiceEndpoint(), calcImplementation(methodElem));
		assertNull(wm);
	}

	public void test_methodWithAnnotationInParentWithoutWsAnnotationSynched() throws JavaModelException
	{
		final IMethod methodElemBaseNoWsClass = util.getExistingMethod(baseClassWithoutWsAnnot, "annotationInBaseNoWSClass");
		assertNotNull(findWebMethodByImpl(ws1.getServiceEndpoint(), calcImplementation(methodElemBaseNoWsClass)));

	}

	public void test_nonPublicMethodTroughHierarchyNotSynched() throws JavaModelException
	{
		final IMethod methodElemBaseNoWsClass = util.getExistingMethod(baseClassWithoutWsAnnot, "nonPublicInBaseNoWSClass");
		assertFalse(Modifier.isPublic(methodElemBaseNoWsClass.getFlags()));
		final IMethod methodElemBaseWsClass = util.getExistingMethod(baseClassWithWsAnnot, "nonPublicInBaseWSClass");
		assertFalse(Modifier.isPublic(methodElemBaseWsClass.getFlags()));
		final IMethod methodElemWsClass = util.getExistingMethod(wsClass, "nonPublicInWSClass");
		assertFalse(Modifier.isPublic(methodElemWsClass.getFlags()));

		assertNull(findWebMethodByImpl(ws1.getServiceEndpoint(), calcImplementation(methodElemBaseNoWsClass)));
		assertNull(findWebMethodByImpl(ws1.getServiceEndpoint(), calcImplementation(methodElemBaseWsClass)));
		assertNull(findWebMethodByImpl(ws1.getServiceEndpoint(), calcImplementation(methodElemWsClass)));
	}

	public void test_excludedMethodTroughHierarchyNotSynched() throws JavaModelException
	{
		final IMethod methodElemBaseNoWsClass = util.getExistingMethod(baseClassWithoutWsAnnot, "excludedPublicInBaseNoWSClass");
		assertTrue(Modifier.isPublic(methodElemBaseNoWsClass.getFlags()));
		final IMethod methodElemBaseWsClass = util.getExistingMethod(baseClassWithWsAnnot, "excludedPublicInBaseWSClass");
		assertTrue(Modifier.isPublic(methodElemBaseWsClass.getFlags()));
		final IMethod methodElemWsClass = util.getExistingMethod(wsClass, "excludedPublicInWSClass");
		assertTrue(Modifier.isPublic(methodElemWsClass.getFlags()));

		assertNull(findWebMethodByImpl(ws1.getServiceEndpoint(), calcImplementation(methodElemBaseNoWsClass)));
		assertNull(findWebMethodByImpl(ws1.getServiceEndpoint(), calcImplementation(methodElemBaseWsClass)));
		assertNull(findWebMethodByImpl(ws1.getServiceEndpoint(), calcImplementation(methodElemWsClass)));
	}

	public void test_overrideMethodsLoadedProperly() throws JavaModelException
	{
		final IMethod methodElemBaseNoWsClass = util.getExistingMethod(baseClassWithoutWsAnnot, "toOverride");
		final IWebMethod wm = findWebMethodByImpl(ws1.getServiceEndpoint(), calcImplementation(methodElemBaseNoWsClass));
		assertNotNull(wm);
		assertEquals("nameInWsClass", wm.getName());
	}

	private IWebMethod findWebMethodByImpl(IServiceEndpointInterface sei, String impl)
	{
		for (IWebMethod wm : sei.getWebMethods())
		{
			if (wm.getImplementation().equals(impl))
			{
				return wm;
			}
		}
		return null;
	}

	private String calcImplementation(IMethod m) throws JavaModelException
	{
		return m.getElementName() + m.getSignature();
	}
}
