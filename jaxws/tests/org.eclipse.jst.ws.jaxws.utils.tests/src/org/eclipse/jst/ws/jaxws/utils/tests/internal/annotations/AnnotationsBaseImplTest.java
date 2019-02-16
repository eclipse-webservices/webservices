/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jst.ws.jaxws.testutils.project.ClassLoadingTest;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationGeneratorException;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationWriter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.AnnotationImpl;

public class AnnotationsBaseImplTest extends ClassLoadingTest
{
	private static final String SRC = "src";

	private static final String PCK = "org.eclipse.demo";

	private IType endpoint;

	public void setUp() throws CoreException, IOException, AnnotationGeneratorException
	{
		setUpWitoutRemove();
		AnnotationFactory.removeAnnotationsFromJavaElement(endpoint);
	}

	public void setUpWitoutRemove() throws CoreException, IOException, AnnotationGeneratorException
	{
		createJavaProject(SRC, PCK);
		endpoint = createClass("Endpoint.src", "Endpoint");
	}
	
	public void setUpWitoutRemove2() throws CoreException, IOException, AnnotationGeneratorException
	{
		createJavaProject(SRC, PCK);
		endpoint = createClass("EndpointAnnInspector.src", "EndpointAnnInspector");
	}
	
	public void testBaseAnnotattionFunctionality()
	{
		AnnotationImpl<IType> ann = new AnnotationImpl<IType>("com.spa.test.MyAnnotation", new HashSet<IParamValuePair>());
		assertEquals(ann.getAnnotationName(), "com.spa.test.MyAnnotation");
		assertEquals(ann.getSimpleAnnotationName(), "MyAnnotation");
		//assertEquals(ann.getValueType(), IValue.ANNOTATION_VALUE);
		
		ann = new AnnotationImpl<IType>("MyNewAnnotation", new HashSet<IParamValuePair>());
		assertEquals(ann.getAnnotationName(), "MyNewAnnotation");
		assertEquals(ann.getSimpleAnnotationName(), "MyNewAnnotation");
		//assertEquals(ann.getValueType(), IValue.ANNOTATION_VALUE);
	}
	
	public void testAddAnnotation() throws Exception
	{
		setUp();
		
		IValue qv = AnnotationFactory.createQualifiedNameValue("full.qualified.Name");

		Set<IParamValuePair> pv = new HashSet<IParamValuePair>();
		pv.add(AnnotationFactory.createParamValuePairValue("param", qv));
		
		IAnnotation<IType> ann = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotation", pv, endpoint);
		AnnotationWriter.getInstance().setAppliedElement(ann, endpoint);

		Collection<IAnnotation<IType>> annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		assertNotNull(annotations);
		assertTrue(annotations.size() == 1);
		IAnnotation<IType> tmpAnn = annotations.iterator().next();
		
		AnnotationWriter.getInstance().remove(tmpAnn);
		
		annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		assertTrue(annotations.size() == 0);
		
		IMethod[] methods = endpoint.getMethods();
		for (IMethod method : methods)
		{
			if(method.getRawParameterNames() != null && method.getRawParameterNames().length > 0)
			{
				IAnnotation<IMethod> annM = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotationMethod", pv, method);
				AnnotationWriter.getInstance().setAppliedElement(annM, method);
				assertEquals("full.qualified.Name", annM.getPropertyValue("param"));
				assertNull(annM.getPropertyValue("paramNotExists"));
				Collection<IAnnotation<IMethod>> annotationsM = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method);
				IAnnotation<IMethod> annotatM = annotationsM.iterator().next();
				assertEquals("qualified.Name", annotatM.getPropertyValue("param"));
				
				IValue sv = AnnotationFactory.createStringValue("New String Value");
				annM.getParamValuePairs().add(AnnotationFactory.createParamValuePairValue("NewParam", sv));
				AnnotationWriter.getInstance().update(annM);
				annotationsM = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method);
				assertTrue(annotationsM.size() == 1);
				annotatM = annotationsM.iterator().next();
				assertEquals("New String Value", annotatM.getPropertyValue("NewParam"));
				assertEquals("qualified.Name", annotatM.getPropertyValue("param"));
				AnnotationWriter.getInstance().remove(annotatM);
				annotationsM = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method);
				assertTrue(annotationsM.size() == 0);

				IValue sv3 = AnnotationFactory.createStringValue("New String Value3");
				annM.getParamValuePairs().clear();
				annM.getParamValuePairs().add(AnnotationFactory.createParamValuePairValue("NewParam", sv3));
				AnnotationWriter.getInstance().update(annM);
				annotationsM = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method);
				assertTrue(annotationsM.size() == 1);
				annotatM = annotationsM.iterator().next();
				assertEquals("New String Value3", annotatM.getPropertyValue("NewParam"));

				// clear annotation
				AnnotationWriter.getInstance().remove(annM);

				annotationsM = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method);
				assertTrue(annotationsM.size() == 0);
				
				for(int i = 0; i < method.getRawParameterNames().length; i++)
				{
					ITypeParameter param = method.getTypeParameter(method.getRawParameterNames()[i]);

					IValue svParam = AnnotationFactory.createStringValue("String Valu Param");
					Set<IParamValuePair> pvParam = new HashSet<IParamValuePair>();
					pvParam.add(AnnotationFactory.createParamValuePairValue("ParamAtribute", svParam));
					IAnnotation<ITypeParameter> annP = AnnotationFactory.createAnnotation("ParamAnnotation", pvParam, param);
					AnnotationWriter.getInstance().setAppliedElement(annP, param);
					
					AnnotationWriter.getInstance().remove(annP);
				}
			}
		}
	}
	
	public void testAnnotattionUpdate() throws Exception
	{
		setUp();
		
		IValue sv = AnnotationFactory.createStringValue("StringValue");
		Set<IParamValuePair> spv = new HashSet<IParamValuePair>();
		spv.add(AnnotationFactory.createParamValuePairValue("stringParam", sv));
		IAnnotation<IType> typeAnnotation = AnnotationFactory.createAnnotation("org.eclipse.test.MyTypeAnnotation", spv, endpoint);
		AnnotationWriter.getInstance().setAppliedElement(typeAnnotation, endpoint);

		Collection<IAnnotation<IType>> typeAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		assertNotNull(typeAnnotations);
		assertTrue(typeAnnotations.size() == 1);
		IAnnotation<IType> tempTypeAnnotation = typeAnnotations.iterator().next();
		assertEquals(tempTypeAnnotation.getPropertyValue("stringParam"), "StringValue");
		
		IValue newSV = AnnotationFactory.createStringValue("NewStringValue");
		Set<IParamValuePair> newSPV = new HashSet<IParamValuePair>();
		newSPV.add(AnnotationFactory.createParamValuePairValue("stringParam", newSV));
		AnnotationImpl<IType> newTypeAnnotation = new AnnotationImpl<IType>("org.eclipse.test.MyTypeAnnotation", newSPV);
		newTypeAnnotation.setAppliedElementWithoutSave(endpoint);
		
		AnnotationWriter.getInstance().update(newTypeAnnotation);
		
		typeAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		assertNotNull(typeAnnotations);
		assertTrue(typeAnnotations.size() == 1);
		tempTypeAnnotation = typeAnnotations.iterator().next();
		assertEquals(tempTypeAnnotation.getPropertyValue("stringParam"), "NewStringValue");

		newSV = AnnotationFactory.createStringValue("NewestStringValue");
		newSPV = new HashSet<IParamValuePair>();
		newSPV.add(AnnotationFactory.createParamValuePairValue("newestStringParam", newSV));
		newTypeAnnotation = new AnnotationImpl<IType>("org.eclipse.test.MyTypeAnnotation", newSPV);
		newTypeAnnotation.setAppliedElementWithoutSave(endpoint);
		
		AnnotationWriter.getInstance().update(newTypeAnnotation);

		typeAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		assertNotNull(typeAnnotations);
		assertTrue(typeAnnotations.size() == 1);
		tempTypeAnnotation = typeAnnotations.iterator().next();
		assertNull(tempTypeAnnotation.getPropertyValue("stringParam"));
		assertEquals(tempTypeAnnotation.getPropertyValue("newestStringParam"), "NewestStringValue");

		IMethod firstMethod = endpoint.getMethods()[0];

		IValue iv = AnnotationFactory.createIntegerValue("10");
		Set<IParamValuePair> ipv = new HashSet<IParamValuePair>();
		ipv.add(AnnotationFactory.createParamValuePairValue("integerParam", iv));
		IAnnotation<IMethod> methodAnnotation = AnnotationFactory.createAnnotation("org.eclipse.test.MyMethodAnnotation", ipv, firstMethod);
		AnnotationWriter.getInstance().setAppliedElement(methodAnnotation, firstMethod);

		Collection<IAnnotation<IMethod>> methodAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(firstMethod);
		assertNotNull(methodAnnotations);
		assertTrue(methodAnnotations.size() == 1);
		IAnnotation<IMethod> tempMethodAnnotation = methodAnnotations.iterator().next();
		assertEquals(tempMethodAnnotation.getPropertyValue("integerParam"), "10");
		
		IValue newIV = AnnotationFactory.createIntegerValue("11");
		Set<IParamValuePair> newIPV = new HashSet<IParamValuePair>();
		newIPV.add(AnnotationFactory.createParamValuePairValue("integerParam", newIV));
		AnnotationImpl<IMethod> newMethodAnnotation = new AnnotationImpl<IMethod>("org.eclipse.test.MyMethodAnnotation", newIPV);
		newMethodAnnotation.setAppliedElementWithoutSave(firstMethod);
		
		AnnotationWriter.getInstance().update(newMethodAnnotation);
		
		methodAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(firstMethod);
		assertNotNull(methodAnnotations);
		assertTrue(methodAnnotations.size() == 1);
		tempMethodAnnotation = methodAnnotations.iterator().next();
		assertEquals(tempMethodAnnotation.getPropertyValue("integerParam"), "11");

		newIV = AnnotationFactory.createIntegerValue("100");
		newIPV = new HashSet<IParamValuePair>();
		newIPV.add(AnnotationFactory.createParamValuePairValue("newestIntegerParam", newIV));
		newMethodAnnotation = new AnnotationImpl<IMethod>("org.eclipse.test.MyMethodAnnotation", newIPV);
		newMethodAnnotation.setAppliedElementWithoutSave(firstMethod);
		
		AnnotationWriter.getInstance().update(newMethodAnnotation);

		methodAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(firstMethod);
		assertNotNull(methodAnnotations);
		assertTrue(methodAnnotations.size() == 1);
		tempMethodAnnotation = methodAnnotations.iterator().next();
		assertNull(tempMethodAnnotation.getPropertyValue("integerParam"));
		assertEquals(tempMethodAnnotation.getPropertyValue("newestIntegerParam"), "100");

		ITypeParameter typeParameter = firstMethod.getTypeParameter("annotatedParam");

		IValue bv = AnnotationFactory.createBooleanValue(true);
		Set<IParamValuePair> bpv = new HashSet<IParamValuePair>();
		bpv.add(AnnotationFactory.createParamValuePairValue("booleanParam", bv));
		IAnnotation<ITypeParameter> parameterAnnotation = AnnotationFactory.createAnnotation("org.eclipse.test.MyParameterAnnotation", bpv, typeParameter);
		AnnotationWriter.getInstance().setAppliedElement(parameterAnnotation, typeParameter);

		Collection<IAnnotation<ITypeParameter>> parameterAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(typeParameter);
		assertNotNull(parameterAnnotations);
		assertTrue(parameterAnnotations.size() == 1);
		IAnnotation<ITypeParameter> tempParameterAnnotation = parameterAnnotations.iterator().next();
		assertEquals(tempParameterAnnotation.getPropertyValue("booleanParam"), "true");
		
		IValue newBV = AnnotationFactory.createBooleanValue(false);
		Set<IParamValuePair> newBPV = new HashSet<IParamValuePair>();
		newBPV.add(AnnotationFactory.createParamValuePairValue("booleanParam", newBV));
		AnnotationImpl<ITypeParameter> newParameterAnnotation = new AnnotationImpl<ITypeParameter>("org.eclipse.test.MyParameterAnnotation", newBPV);
		newParameterAnnotation.setAppliedElementWithoutSave(typeParameter);
		
		AnnotationWriter.getInstance().update(newParameterAnnotation);
		
		parameterAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(typeParameter);
		assertNotNull(parameterAnnotations);
		assertTrue(parameterAnnotations.size() == 1);
		tempParameterAnnotation = parameterAnnotations.iterator().next();
		assertEquals(tempParameterAnnotation.getPropertyValue("booleanParam"), "false");

		newBV = AnnotationFactory.createBooleanValue(true);
		newBPV = new HashSet<IParamValuePair>();
		newBPV.add(AnnotationFactory.createParamValuePairValue("newestBooleanParam", newBV));
		newParameterAnnotation = new AnnotationImpl<ITypeParameter>("org.eclipse.test.MyParameterAnnotation", newBPV);
		newParameterAnnotation.setAppliedElementWithoutSave(typeParameter);
		
		AnnotationWriter.getInstance().update(newParameterAnnotation);

		parameterAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(typeParameter);
		assertNotNull(parameterAnnotations);
		assertTrue(parameterAnnotations.size() == 1);
		tempParameterAnnotation = parameterAnnotations.iterator().next();
		assertNull(tempParameterAnnotation.getPropertyValue("booleanParam"));
		assertEquals(tempParameterAnnotation.getPropertyValue("newestBooleanParam"), "true");
		
		
	}
	
	public void testSetAppliedElementWithoutSave() throws Exception
	{
		setUp();
		IValue qv = AnnotationFactory.createQualifiedNameValue("full.qualified.Name");

		Set<IParamValuePair> pv = new HashSet<IParamValuePair>();
		pv.add(AnnotationFactory.createParamValuePairValue("param", qv));
		
		IAnnotation<IType> ann = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotation", pv, endpoint);
		try
		{
			AnnotationWriter.getInstance().setAppliedElement(ann, null);
			fail("NullPointerException was not thrown.");
		}
		catch (NullPointerException e)
		{
			assertTrue(true);
		}
	}
	
	public void testMultyAddAnnotation() throws Exception
	{
		setUpWitoutRemove();
		
		IWorkspaceRunnable runnable = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				try
				{
					Set<String> annoSet = new HashSet<String>();
					annoSet.add("Stateless");
					
					AnnotationFactory.removeAnnotations(endpoint, annoSet);
					
					IValue qv = AnnotationFactory.createQualifiedNameValue("full.qualified.Name");

					Set<IParamValuePair> pv = new HashSet<IParamValuePair>();
					pv.add(AnnotationFactory.createParamValuePairValue("param", qv));
					
					IAnnotation<IType> ann = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotation", pv, endpoint);
					AnnotationWriter.getInstance().setAppliedElement(ann, endpoint);

					IValue sv = AnnotationFactory.createStringValue("StringValue");

					Set<IParamValuePair> spv = new HashSet<IParamValuePair>();
					spv.add(AnnotationFactory.createParamValuePairValue("stringParam", sv));
					
					IAnnotation<IType> sAnn = AnnotationFactory.createAnnotation("org.eclipse.test.MyStringAnnotation", spv, endpoint);
					AnnotationWriter.getInstance().setAppliedElement(sAnn, endpoint);
					
					
					Collection<IAnnotation<IType>> annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
					assertNotNull(annotations);
					assertTrue(annotations.size() == 3);
					IAnnotation<IType> tmpAnn = annotations.iterator().next();
					
					AnnotationWriter.getInstance().remove(tmpAnn);
					
					annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
					assertTrue(annotations.size() == 2);
					
					IMethod[] methods = endpoint.getMethods();
					for (IMethod method : methods)
					{
						IValue smv = AnnotationFactory.createStringValue("StringValue");
						Set<IParamValuePair> smpv = new HashSet<IParamValuePair>();
						smpv.add(AnnotationFactory.createParamValuePairValue("stringParam", smv));
						
						IAnnotation<IMethod> smAnn = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotationMethod", smpv, method);
						AnnotationWriter.getInstance().setAppliedElement(smAnn, method);
						Collection<IAnnotation<IMethod>> annotationsM = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method);
						assertNotNull(annotationsM);
						assertTrue(annotationsM.size() == 2);

						annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
						assertNotNull(annotations);
						assertTrue(annotations.size() == 2);

					}
	
				} catch (Exception e)
				{
					fail(e.getMessage());
				}
			}
		};
		
		getTestProject().getSourceFolder().getResource().getWorkspace().run(runnable, null);

		
		
		
	}
		
	public void testEquals()
	{
		IValue qv = AnnotationFactory.createQualifiedNameValue("full.qualified.Name");

		Set<IParamValuePair> pv = new HashSet<IParamValuePair>();
		pv.add(AnnotationFactory.createParamValuePairValue("param", qv));
		
		IAnnotation<IType> ann1 = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotation1", pv, endpoint);
		IAnnotation<IType> ann2 = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotation2", pv, endpoint);
		IAnnotation<IType> ann3 = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotation1", pv, endpoint);
		
		assertTrue(ann1.equals(ann1));
		assertFalse(ann1.equals(null));
		assertFalse(ann1.equals(qv));
		assertFalse(ann1.equals(ann2));
		assertTrue(ann1.equals(ann3));
	}

	public void testField() throws Exception
	{
		setUp();
		
		IValue qv = AnnotationFactory.createQualifiedNameValue("full.qualified.Name");

		Set<IParamValuePair> pv = new HashSet<IParamValuePair>();
		pv.add(AnnotationFactory.createParamValuePairValue("param", qv));
		
		IAnnotation<IField> ann = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotation", pv, endpoint.getField("field1"));
		AnnotationWriter.getInstance().setAppliedElement(ann, endpoint.getField("field1"));
		assertEquals(ann.getAppliedElement(), endpoint.getField("field1"));
	}
	
	public void testLocator() throws Exception
	{
		setUpWitoutRemove2();
		
		IAnnotation<IType> inspectedTypeAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectType("org.eclipse.demo.MyAnnotation");
		assertNotNull(inspectedTypeAnnotation);
		ILocator locator = inspectedTypeAnnotation.getLocator();
		assertEquals("Unexpected annotation start position", 191, locator.getStartPosition());
		assertEquals("Unexpected length", 33, locator.getLength());
		Set<IParamValuePair> parmValues = inspectedTypeAnnotation.getParamValuePairs();
		for (IParamValuePair paramValuePair : parmValues)
		{
			if(paramValuePair.getParam().equals("myValue"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected annotation parameter start position", 205, locator.getStartPosition());
				assertEquals("Unexpected annotation parameter length", 18, locator.getLength());
			}
		}

		inspectedTypeAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectType("javax.jws.WebService");
		assertNotNull(inspectedTypeAnnotation);
		locator = inspectedTypeAnnotation.getLocator();
		assertEquals("Unexpected @WebService annotation start position", 226, locator.getStartPosition());
		assertEquals("Unexpected @WebService annotation length", 134, locator.getLength());
		parmValues = inspectedTypeAnnotation.getParamValuePairs();
		for (IParamValuePair paramValuePair : parmValues)
		{
			if(paramValuePair.getParam().equals("serviceName"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected serviceName annotation parameter start position", 238, locator.getStartPosition());
				assertEquals("Unexpected serviceName annotation parameter length", 31, locator.getLength());
			}

			if(paramValuePair.getParam().equals("name"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected serviceName annotation parameter start position", 271, locator.getStartPosition());
				assertEquals("Unexpected serviceName annotation parameter length", 17, locator.getLength());
			}

			if(paramValuePair.getParam().equals("targetNamespace"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected targetNamespace annotation parameter start position", 290, locator.getStartPosition());
				assertEquals("Unexpected targetNamespace annotation parameter length", 42, locator.getLength());
			}

			if(paramValuePair.getParam().equals("portName"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected portName annotation parameter start position", 334, locator.getStartPosition());
				assertEquals("Unexpected portName annotation parameter length", 25, locator.getLength());
			}
		}

		IField field = endpoint.getField("field1");
		IAnnotation<IField> inspectedFieldAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectField(field, "org.eclipse.demo.MyFieldAnnotation");
		locator = inspectedFieldAnnotation.getLocator();
		assertEquals("Unexpected MyFieldAnnotation annotation parameter start position", 401, locator.getStartPosition());
		assertEquals("Unexpected MyFieldAnnotation annotation parameter length", 33, locator.getLength());
		parmValues = inspectedFieldAnnotation.getParamValuePairs();
		for (IParamValuePair paramValuePair : parmValues)
		{
			if(paramValuePair.getParam().equals("name"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected name annotation parameter start position", 420, locator.getStartPosition());
				assertEquals("Unexpected name annotation parameter length", 13, locator.getLength());
			}
		}

		inspectedFieldAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectField(field, "javax.jws.WebField");
		locator = inspectedFieldAnnotation.getLocator();
		assertEquals("Unexpected WebField annotation parameter start position", 437, locator.getStartPosition());
		assertEquals("Unexpected WebField annotation parameter length", 25, locator.getLength());
		parmValues = inspectedFieldAnnotation.getParamValuePairs();
		for (IParamValuePair paramValuePair : parmValues)
		{
			if(paramValuePair.getParam().equals("name"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected name annotation parameter start position", 447, locator.getStartPosition());
				assertEquals("Unexpected name annotation parameter length", 14, locator.getLength());
			}
		}

		IMethod method = endpoint.getMethods()[0];
		IAnnotation<IMethod> inspectedMethodAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method, "org.eclipse.demo.MyMethodAnnotation");
		locator = inspectedMethodAnnotation.getLocator();
		assertEquals("Unexpected MyMethodAnnotation annotation parameter start position", 492, locator.getStartPosition());
		assertEquals("Unexpected MyMethodAnnotation annotation parameter length", 43, locator.getLength());
		parmValues = inspectedMethodAnnotation.getParamValuePairs();
		for (IParamValuePair paramValuePair : parmValues)
		{
			if(paramValuePair.getParam().equals("name"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected name annotation parameter start position", 512, locator.getStartPosition());
				assertEquals("Unexpected name annotation parameter length", 22, locator.getLength());
			}
		}

		inspectedMethodAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method, "javax.jws.WebMethod");
		locator = inspectedMethodAnnotation.getLocator();
		assertEquals("Unexpected WebMethod annotation parameter start position", 538, locator.getStartPosition());
		assertEquals("Unexpected WebMethod annotation parameter length", 46, locator.getLength());
		parmValues = inspectedMethodAnnotation.getParamValuePairs();
		for (IParamValuePair paramValuePair : parmValues)
		{
			if(paramValuePair.getParam().equals("exclude"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected exclude annotation parameter start position", 549, locator.getStartPosition());
				assertEquals("Unexpected exclude annotation parameter length", 13, locator.getLength());
			}

			if(paramValuePair.getParam().equals("operationName"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected operationName annotation parameter start position", 563, locator.getStartPosition());
				assertEquals("Unexpected operationName annotation parameter length", 20, locator.getLength());
			}
		}

		ITypeParameter typeParameter = method.getTypeParameter("annotatedParam");
		IAnnotation<ITypeParameter> inspectedParamAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(typeParameter, "org.eclipse.demo.MyParamAnnotation");
		locator = inspectedParamAnnotation.getLocator();
		assertEquals("Unexpected MyParamAnnotation annotation parameter start position", 649, locator.getStartPosition());
		assertEquals("Unexpected MyParamAnnotation annotation parameter length", 43, locator.getLength());
		parmValues = inspectedParamAnnotation.getParamValuePairs();
		for (IParamValuePair paramValuePair : parmValues)
		{
			if(paramValuePair.getParam().equals("name"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected name annotation parameter start position", 668, locator.getStartPosition());
				assertEquals("Unexpected name annotation parameter length", 23, locator.getLength());
			}
		}
		
		inspectedParamAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(typeParameter, "javax.jws.WebParam");
		locator = inspectedParamAnnotation.getLocator();
		assertEquals("Unexpected WebParam annotation parameter start position", 616, locator.getStartPosition());
		assertEquals("Unexpected WebParam annotation parameter length", 32, locator.getLength());
		parmValues = inspectedParamAnnotation.getParamValuePairs();
		for (IParamValuePair paramValuePair : parmValues)
		{
			if(paramValuePair.getParam().equals("name"))
			{
				locator = paramValuePair.getLocator();
				assertEquals("Unexpected name annotation parameter start position", 626, locator.getStartPosition());
				assertEquals("Unexpected name annotation parameter length", 21, locator.getLength());
			}
		}
	}
}
