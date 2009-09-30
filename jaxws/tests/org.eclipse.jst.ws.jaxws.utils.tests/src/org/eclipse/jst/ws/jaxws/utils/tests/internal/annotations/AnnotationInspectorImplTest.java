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
package org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jst.ws.jaxws.testutils.project.ClassLoadingTest;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationGeneratorException;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationWriter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;

public class AnnotationInspectorImplTest extends ClassLoadingTest
{
	private static final String SRC = "src";

	private static final String PCK = "org.eclipse.demo";

	private IType endpoint;

	public void setUp() throws CoreException, IOException, AnnotationGeneratorException
	{
		createJavaProject(SRC, PCK);
		endpoint = createClass("Endpoint.src", "Endpoint");
		AnnotationFactory.removeAnnotationsFromJavaElement(endpoint);
	}

	public void setUpSpecific() throws CoreException, IOException, AnnotationGeneratorException
	{
		createJavaProject(SRC, PCK);
		endpoint = createClass("EndpointAnnInspector.src", "EndpointAnnInspector");
	}

	public void testInspector() throws Exception
	{
		setUp();
		
		try
		{
			AnnotationFactory.createAnnotationInspector(null).inspectType();
			fail("NullPointerException should be thrown!");
		}
		catch (NullPointerException e) 
		{
			assertTrue(true);
		}

		try
		{
			AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(null);
			fail("NullPointerException should be thrown!");
		}
		catch (NullPointerException e) 
		{
			assertTrue(true);
		}

		try
		{
			AnnotationFactory.createAnnotationInspector(endpoint).inspectField(null);
			fail("NullPointerException should be thrown!");
		}
		catch (NullPointerException e) 
		{
			assertTrue(true);
		}

		try
		{
			AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(null);
			fail("NullPointerException should be thrown!");
		}
		catch (NullPointerException e) 
		{
			assertTrue(true);
		}
		
		IValue sv = AnnotationFactory.createStringValue("My Value");

		Set<IParamValuePair> pv = new HashSet<IParamValuePair>();
		pv.add(AnnotationFactory.createParamValuePairValue("param", sv));
		
		IAnnotation<IField> ann = AnnotationFactory.createAnnotation("org.eclipse.test.MyAnnotation", pv, endpoint.getField("field1"));
		AnnotationWriter.getInstance().setAppliedElement(ann, endpoint.getField("field1"));

		Collection<IAnnotation<IField>> annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectField(endpoint.getField("field1"));
		assertNotNull(annotations);
		assertTrue(annotations.size() == 1);
		IAnnotation<IField> annP = annotations.iterator().next();
		AnnotationWriter.getInstance().remove(annP);
		
		annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectField(endpoint.getField("field1"));
		assertNotNull(annotations);
		assertTrue(annotations.size() == 0);
		

		IMethod[] methods = endpoint.getMethods();
		for (IMethod method : methods)
		{
			if(method.getRawParameterNames() != null && method.getRawParameterNames().length > 0)
			{
				for(int i = 0; i < method.getRawParameterNames().length; i++)
				{
					ITypeParameter param = method.getTypeParameter(method.getRawParameterNames()[i]);

					IValue svParam = AnnotationFactory.createStringValue("String Valu Param");
					Set<IParamValuePair> pvParam = new HashSet<IParamValuePair>();
					pvParam.add(AnnotationFactory.createParamValuePairValue("ParamAtribute", svParam));
					IAnnotation<ITypeParameter> annPam = AnnotationFactory.createAnnotation("ParamAnnotation", pvParam, param);
					AnnotationWriter.getInstance().setAppliedElement(annPam, param);
					Collection<IAnnotation<ITypeParameter>> paramAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(param);
					assertNotNull(paramAnnotations);
					assertTrue(paramAnnotations.size() == 1);
					IAnnotation<ITypeParameter> tmpAnn = paramAnnotations.iterator().next();
					
					AnnotationWriter.getInstance().remove(tmpAnn);
					paramAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(param);
					assertNotNull(paramAnnotations);
					assertTrue(paramAnnotations.size() == 0);
					
					annPam = AnnotationFactory.createAnnotation("ParamAnnotation2", pvParam, param);
					AnnotationWriter.getInstance().setAppliedElement(annPam, param);
					paramAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(param);
					assertNotNull(paramAnnotations);
					assertTrue(paramAnnotations.size() == 1);
					AnnotationFactory.removeAnnotationsFromJavaElement(param);
					paramAnnotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(param);
					assertNotNull(paramAnnotations);
					assertTrue(paramAnnotations.size() == 0);
				}
			}
		}
	}
	
	public void testInspectorSpecificAnnotation() throws Exception
	{
		setUpSpecific();
		
		IAnnotation<IType> inspectedTypeAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectType("org.eclipse.demo.MyAnnotation");
		assertNotNull(inspectedTypeAnnotation);
		assertEquals(inspectedTypeAnnotation.getPropertyValue("myValue"), "MyString"); 
		
		inspectedTypeAnnotation = null;
				
		inspectedTypeAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectType("javax.jws.WebService");
		assertNotNull(inspectedTypeAnnotation);
		assertTrue("javax.jws.WebService".endsWith(inspectedTypeAnnotation.getAnnotationName()));
		assertEquals(inspectedTypeAnnotation.getPropertyValue("serviceName"), "WSImplBeanService"); 
		assertEquals(inspectedTypeAnnotation.getPropertyValue("name"), "WSImplBean"); 
		assertEquals(inspectedTypeAnnotation.getPropertyValue("targetNamespace"), "http://demo/eclipse/org/"); 
		assertEquals(inspectedTypeAnnotation.getPropertyValue("portName"), "WSImplBeanPort"); 

		IField field = endpoint.getField("field1");
		IAnnotation<IField> inspectedFieldAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectField(field, "org.eclipse.demo.MyFieldAnnotation");
		assertNotNull(inspectedFieldAnnotation);
		assertTrue("org.eclipse.demo.MyFieldAnnotation".endsWith(inspectedFieldAnnotation.getAnnotationName()));
		assertEquals(inspectedFieldAnnotation.getPropertyValue("name"), "field1");
		
		inspectedFieldAnnotation = null;
		
		inspectedFieldAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectField(field, "javax.jws.WebField");
		assertNotNull(inspectedFieldAnnotation);
		assertTrue("javax.jws.WebField".endsWith(inspectedFieldAnnotation.getAnnotationName()));
		assertEquals(inspectedFieldAnnotation.getPropertyValue("name"), "MyField");

		IMethod method = endpoint.getMethods()[0];
		IAnnotation<IMethod> inspectedMethodAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method, "org.eclipse.demo.MyMethodAnnotation");
		assertNotNull(inspectedMethodAnnotation);
		assertTrue("org.eclipse.demo.MyMethodAnnotation".endsWith(inspectedMethodAnnotation.getAnnotationName()));
		assertEquals(inspectedMethodAnnotation.getPropertyValue("name"), "annotatedMethod");
		
		inspectedMethodAnnotation = null;
		
		inspectedMethodAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method, "javax.jws.WebMethod");
		assertNotNull(inspectedMethodAnnotation);
		assertTrue("javax.jws.WebMethod".endsWith(inspectedMethodAnnotation.getAnnotationName()));
		assertEquals(inspectedMethodAnnotation.getPropertyValue("exclude"), "false");
		assertEquals(inspectedMethodAnnotation.getPropertyValue("operationName"), "test");

		ITypeParameter typeParameter = method.getTypeParameter("annotatedParam");
		IAnnotation<ITypeParameter> inspectedParamAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(typeParameter, "org.eclipse.demo.MyParamAnnotation");
		assertNotNull(inspectedParamAnnotation);
		assertTrue("org.eclipse.demo.MyParamAnnotation".endsWith(inspectedParamAnnotation.getAnnotationName()));
		assertEquals(inspectedParamAnnotation.getPropertyValue("name"), "annotatedMyParam");
		
		inspectedParamAnnotation = null;
		
		inspectedParamAnnotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectParam(typeParameter, "javax.jws.WebParam");
		assertNotNull(inspectedParamAnnotation);
		assertTrue("javax.jws.WebParam".endsWith(inspectedParamAnnotation.getAnnotationName()));
		assertEquals(inspectedParamAnnotation.getPropertyValue("name"), "annotatedParam");
	}
	
	public void testInspectorLocatorRetrieved() throws Exception
	{
		setUpSpecific();		
		IAnnotation<IType> annotation = AnnotationFactory.createAnnotationInspector(endpoint).inspectType("javax.jws.WebService");
		assertNotNull(annotation);

		assertEquals(11, annotation.getLocator().getLineNumber());
		assertEquals(226, annotation.getLocator().getStartPosition());
		assertEquals(134, annotation.getLocator().getLength());
		
		assertEquals(11, getParam("portName", annotation).getLocator().getLineNumber());
		assertEquals(334, getParam("portName", annotation).getLocator().getStartPosition());
		assertEquals(25, getParam("portName", annotation).getLocator().getLength());		
	}
	
	private IParamValuePair getParam(final String name, final IAnnotation<? extends IJavaElement> annotation) 
	{
		for (IParamValuePair param : annotation.getParamValuePairs()) {
			if(param.getParam().equals(name)) {
				return param;
			}
		}
		
		return null;
	}
	
}
