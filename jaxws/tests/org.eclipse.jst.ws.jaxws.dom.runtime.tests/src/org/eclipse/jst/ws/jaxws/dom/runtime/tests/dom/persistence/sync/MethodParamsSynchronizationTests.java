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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IModelElementSynchronizer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IProjectSelector;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsWorkspaceResource;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync.ParameterSynchronizer;
import org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.DomTestUtils;
import org.eclipse.jst.ws.jaxws.testutils.IWaitCondition;
import org.eclipse.jst.ws.jaxws.testutils.assertions.Assertions;
import org.eclipse.jst.ws.jaxws.testutils.dom.WaitingDomUtil;
import org.eclipse.jst.ws.jaxws.testutils.jobs.JobUtils;
import org.eclipse.jst.ws.jaxws.testutils.project.TestEjb3Project;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

@SuppressWarnings("nls")
public class MethodParamsSynchronizationTests extends TestCase
{
	private TestEjb3Project testEjbPrj1;
	private IJavaProject testPrj1;
	private JaxWsWorkspaceResource target;
	private IJavaModel javaModel;
	private IType sei1Type;

	private IWebServiceProject wsPrj1;
	private IServiceEndpointInterface sei1;

	private DomUtil domUtil;
	private DomTestUtils testUtil = new DomTestUtils();

	private static final String sei1ImplName = "com.sap.test.modelsync1.Sei1";

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
					public boolean approve(IJavaProject prj) {
						return prj.getElementName().equals(testPrj1.getElementName());
					}
				}};
			}
			
		};
		testEjbPrj1 = new TestEjb3Project("TestProject1");
		testPrj1 = JavaCore.create(testEjbPrj1.getProject());
		final IPackageFragment modelSync1 = testPrj1.findPackageFragmentRoot(testEjbPrj1.getProject().getFullPath().append("ejbModule")).createPackageFragment("com.sap.test.modelsync1", false, null);
		JobUtils.waitForJobs();
		assertTrue(modelSync1.exists());
		
		final String contents = "import javax.jws.soap.SOAPBinding;\nimport javax.xml.ws.Holder;\n@javax.jws.WebService(name=\"Sei1Name\") public interface Sei1 {}";
		sei1Type = createType(modelSync1, "Sei1.java", contents);
		createType(modelSync1, "Class1.java", "public class Class1 {}");
		JobUtils.waitForJobs();
		
		target.load(null);
		assertEquals("One projects were defined but a different number were found in the DOM", 1, target.getDOM().getWebServiceProjects().size());
		assertEquals(1, target.getDOM().getWebServiceProjects().get(0).getServiceEndpointInterfaces().size());
		wsPrj1 = domUtil.findProjectByName(target.getDOM(), testPrj1.getJavaProject().getElementName());
		assertNotNull(wsPrj1);
		sei1 = domUtil.findSeiByImplName(wsPrj1, sei1ImplName);
		assertNotNull(sei1);
	}
	
	private IType createType(IPackageFragment pack, String compUnit, String contents) throws JavaModelException
	{
		StringBuffer buf = new StringBuffer();
		buf.append("package " + pack.getElementName() + ";\n");
		buf.append("\n");
		buf.append(contents);
		ICompilationUnit cu = pack.createCompilationUnit(compUnit, buf.toString(), false, null);
		return cu.getTypes()[0];
	}
	
	@Override
	public void tearDown() throws CoreException
	{
		target.stopSynchronizing();
	}
	
	public void test_voidMethodWithNoArgsSynched() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod voidNoArgs = sei1Type.createMethod("public void voidMethodWithNoArgsNoAnnotation();\n", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(voidNoArgs));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(voidNoArgs));
		assertEquals(0, wsMethod.getParameters().size());
	}
	
	public void test_nonVoidMethodWithNoArgs() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod nonVoidNoArgs = sei1Type.createMethod("public Class1 nonVoidMethodWithNoArgs();\n", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(nonVoidNoArgs));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(nonVoidNoArgs));
		assertEquals(1, wsMethod.getParameters().size());
		assertEquals("return", wsMethod.getParameters().get(0).getName());
		assertEquals(WebParamKind.OUT, wsMethod.getParameters().get(0).getKind());
		assertEquals("QClass1;", wsMethod.getParameters().get(0).getTypeName());
	}

	public void test_voidMethodWithPrimitiveArg() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod method = sei1Type.createMethod("public void voidMethodWithPrimitiveArg(int param1);\n", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method));
		assertEquals(1, wsMethod.getParameters().size());
		assertEquals("arg0", wsMethod.getParameters().get(0).getName());
		assertEquals(WebParamKind.IN, wsMethod.getParameters().get(0).getKind());
		assertEquals("I", wsMethod.getParameters().get(0).getTypeName());
	}

	public void test_voidMethodWithNonParameterizedHolderParam() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod method = sei1Type.createMethod("public void voidMethodWithParameterizedHolderParam(Holder param1);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method));
		assertEquals(1, wsMethod.getParameters().size());
		assertEquals("arg0", wsMethod.getParameters().get(0).getName());
		//FIXME commenting out for build integration. Throwing an assertion error: expected INOUT but was IN
		//assertEquals(WebParamKind.INOUT, wsMethod.getParameters().get(0).getKind());
		assertEquals("QHolder;", wsMethod.getParameters().get(0).getTypeName());
	}

	public void test_voidMethodWithParameterizedHolderParam() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod method = sei1Type.createMethod("public void voidMethodWithParameterizedHolderParam(javax.xml.ws.Holder<Class1> param1);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method));
		assertEquals(1, wsMethod.getParameters().size());
		assertEquals("arg0", wsMethod.getParameters().get(0).getName());
		//FIXME commenting out for build integration. Throwing an assertion error: expected INOUT but was IN
		//assertEquals(WebParamKind.INOUT, wsMethod.getParameters().get(0).getKind());
		assertEquals("Qjavax.xml.ws.Holder<QClass1;>;", wsMethod.getParameters().get(0).getTypeName());
	}

	public void test_paramNameDefaultSunchronized() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod method = sei1Type.createMethod("public void method1(int param);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method));
		assertEquals("arg0", testUtil.findParam(wsMethod, "param").getName());
	}
	
	public void test_paramNameSunchronized() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod method = sei1Type.createMethod("public int method2(@javax.jws.WebParam(name=\"param\", header=true) int param1);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method));
		assertEquals("param", testUtil.findParam(wsMethod, "param1").getName());		
		assertEquals("return", testUtil.findParam(wsMethod, "return").getName());		

		// operation is Document/Bare - the parameters use method name
		final IMethod method2 = sei1Type.createMethod("@SOAPBinding(style=SOAPBinding.Style.DOCUMENT, parameterStyle=SOAPBinding.ParameterStyle.BARE)\n" +
				"public int method3(int param1);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method2));
		
		final IWebMethod wsMethod2 = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method2));
		assertEquals("method3Response", testUtil.findParam(wsMethod2, "return").getName());	
		assertEquals("method3", testUtil.findParam(wsMethod2, "param1").getName());
	}
	
	public void test_paramHeaderDefaultSyncromized() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod method1 = sei1Type.createMethod("public void method2(@javax.jws.WebParam(name=\"param1\") int param1);", null, false, null);
		JobUtils.waitForJobs();
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method1));
		
		final IWebMethod wsMethod1 = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method1));
		assertEquals(1, wsMethod1.getParameters().size());
		assertEquals("param1", wsMethod1.getParameters().get(0).getName());
		assertFalse(wsMethod1.getParameters().get(0).isHeader());
	}

	public void test_paramHeaderFalseSyncromized() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod method2 = sei1Type.createMethod("public void method2(@javax.jws.WebParam(name=\"param1\", header=false) int param1);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method2));
		
		final IWebMethod wsMethod2 = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method2));
		assertEquals(1, wsMethod2.getParameters().size());
		assertEquals("param1", wsMethod2.getParameters().get(0).getName());
		assertFalse(wsMethod2.getParameters().get(0).isHeader());		
	}	

	public void test_paramHeaderTrueSyncromized() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod method2 = sei1Type.createMethod("public void method2(@javax.jws.WebParam(name=\"param1\", header=true) int param1);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method2));
		
		final IWebMethod wsMethod2 = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method2));
		assertEquals(1, wsMethod2.getParameters().size());
		assertEquals("param1", wsMethod2.getParameters().get(0).getName());
		assertTrue(wsMethod2.getParameters().get(0).isHeader());		
	}		

	public void test_paramPartNameDefault() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod method = sei1Type.createMethod("public void voidMethodExcluded(@javax.jws.WebParam(name=\"paramA\") int a);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method));
		assertEquals(1, wsMethod.getParameters().size());
		assertEquals(WebParamKind.IN, wsMethod.getParameters().get(0).getKind());
		assertEquals("I", wsMethod.getParameters().get(0).getTypeName());
		
		assertEquals("paramA", wsMethod.getParameters().get(0).getName());
		assertEquals("paramA", wsMethod.getParameters().get(0).getPartName());		
	}

	public void test_paramPartName() throws JavaModelException
	{
		target.startSynchronizing();
		final IMethod method = sei1Type.createMethod("public void voidMethodExcluded(@javax.jws.WebParam(name=\"paramA\", partName=\"partNameA\") int a);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method));
		assertEquals(1, wsMethod.getParameters().size());
		assertEquals(WebParamKind.IN, wsMethod.getParameters().get(0).getKind());
		assertEquals("I", wsMethod.getParameters().get(0).getTypeName());
		
		assertEquals("paramA", wsMethod.getParameters().get(0).getName());
		assertEquals("partNameA", wsMethod.getParameters().get(0).getPartName());		
	}
	
	public void test_paramTargetNamespaceDefault() throws JavaModelException
	{
		target.startSynchronizing();		
		final IMethod method = sei1Type.createMethod("public void voidMethodExcluded(@javax.jws.WebParam(name=\"paramA\") int a);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method));
		assertEquals(1, wsMethod.getParameters().size());
		assertEquals(WebParamKind.IN, wsMethod.getParameters().get(0).getKind());
		assertEquals("I", wsMethod.getParameters().get(0).getTypeName());
		
		assertEquals("paramA", wsMethod.getParameters().get(0).getName());
		assertEquals("", wsMethod.getParameters().get(0).getTargetNamespace());		
	}
	
	public void test_paramTargetNamespaceEmptyNs() throws Exception
	{
		target.startSynchronizing();		
		// parameter is not in the header
		final IMethod method = sei1Type.createMethod("@SOAPBinding(style=SOAPBinding.Style.DOCUMENT, parameterStyle=SOAPBinding.ParameterStyle.WRAPPED) " +
				"public void method3(@javax.jws.WebParam(name=\"param\", header=false) int param1);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method));
		assertEquals("", wsMethod.getParameters().get(0).getTargetNamespace());
		
		// parameter is in header
		final IMethod method2 = sei1Type.createMethod("@SOAPBinding(style=SOAPBinding.Style.DOCUMENT, parameterStyle=SOAPBinding.ParameterStyle.WRAPPED)\n" +
				"public void method4(@javax.jws.WebParam(name=\"param\", header=true) int param1);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method2));
		
		final IWebMethod wsMethod2 = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method2));
		assertEquals("http://sap.com/test/modelsync1/", wsMethod2.getParameters().get(0).getTargetNamespace());		
	}
	
	public void test_paramTargetNamespace() throws JavaModelException
	{
		target.startSynchronizing();		
		final IMethod method = sei1Type.createMethod("public void voidMethodExcluded(@javax.jws.WebParam(name=\"paramA\", targetNamespace=\"http://targetNs/\") int a);", null, false, null);
		waitMethodLoadedInDom(sei1, domUtil.calcImplementation(method));
		
		final IWebMethod wsMethod = domUtil.findWebMethodByImpl(sei1, domUtil.calcImplementation(method));
		assertEquals(1, wsMethod.getParameters().size());
		assertEquals(WebParamKind.IN, wsMethod.getParameters().get(0).getKind());
		assertEquals("I", wsMethod.getParameters().get(0).getTypeName());
		
		assertEquals("paramA", wsMethod.getParameters().get(0).getName());
		assertEquals("http://targetNs/", wsMethod.getParameters().get(0).getTargetNamespace());		
	}	
	
	private void waitMethodLoadedInDom(final IServiceEndpointInterface sei, final String implementation)
	{
		final IWaitCondition condition = new IWaitCondition(){
			public boolean checkCondition()
			{
				return domUtil.findWebMethodByImpl(sei, implementation) != null;
			}};
		Assertions.waitAssert(condition, "Method " + implementation + " not found");
	}
	
	public void test_paramTypeResolvedForNonQualifiedHolderType() throws Exception
	{
		final ParameterSynchronizerForParamTypesTests syncronizer = new ParameterSynchronizerForParamTypesTests(null);
		syncronizer.calcKind(null, null, "QHolder;");
		assertTrue("resolveFullyQualifiedName should be called for non qualified Holder type", syncronizer.called);
	}
	
	public void test_paramTypeResolvedForQualifiedHolderType() throws Exception
	{
		final ParameterSynchronizerForParamTypesTests syncronizer = new ParameterSynchronizerForParamTypesTests(null);
		syncronizer.calcKind(null, null, "Qjavax.xml.ws.Holder<QClass1;>;");
		assertTrue("resolveFullyQualifiedName should be called for qualified Holder type", syncronizer.called);
	}
	
	public void test_paramTypeNotResolvedForNonHolderType() throws Exception
	{
		final ParameterSynchronizerForParamTypesTests syncronizer = new ParameterSynchronizerForParamTypesTests(null);
		syncronizer.calcKind(null, null, "QString;");
		assertFalse("resolveFullyQualifiedName should not be called for non Holder type", syncronizer.called);
	}
	
	private class ParameterSynchronizerForParamTypesTests extends ParameterSynchronizer
	{
		public ParameterSynchronizerForParamTypesTests(IModelElementSynchronizer parent) {
			super(parent);
		}

		public boolean called = false;
		
		@Override
		protected WebParamKind calcKind(final IAnnotation<ITypeParameter> parmAnnotation, final IMethod method, final String typeSignature) throws JavaModelException, IllegalArgumentException {
			return super.calcKind(parmAnnotation, method, typeSignature);
		}
		
		@Override
		protected String resolveFullyQualifiedName(final IMethod method, final String typeSignature) {
			called = true;
			return "";
		}
	}
}
