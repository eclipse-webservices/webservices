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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.annotations;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.testutils.project.ClassLoadingTest;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;

/**
 * AnnotationFactory class test cases.
 * 
 * @author Georgi Vachkov
 */
@SuppressWarnings("nls")
public class AnnotationFactoryTest extends ClassLoadingTest
{
	private static final String SRC = "src";

	private static final String PCK = "org.eclipse.demo";

	private IType endpoint;


	public void testCreateStringValue()
	{
		try
		{
			AnnotationFactory.createStringValue(null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException e)
		{
		}

		IValue strValue = AnnotationFactory.createStringValue("value");
		assertNotNull(strValue);
		assertTrue(strValue.toString().equals("value"));
	}

	public void testCreateParamValuePair()
	{
		IValue strValue = AnnotationFactory.createStringValue("value");

		try
		{
			AnnotationFactory.createParamValuePairValue(null, strValue);
			fail("NullPointerException not thrown");
		} catch (NullPointerException e)
		{
		}

		try
		{
			AnnotationFactory.createParamValuePairValue("", null);
			fail("NullPointerException not thrown");
			fail("IllegalArgumentException not thrown");
		}
		catch (NullPointerException e)
		{
		}
		catch (IllegalArgumentException e) 
		{
		}

		IParamValuePair valuePair = AnnotationFactory.createParamValuePairValue("param", strValue);
		assertNotNull(valuePair);
		assertTrue(valuePair.getValue() instanceof IValue);
		assertTrue(valuePair.getParam().equals("param"));
	}

	public void testCreateComplexAnnotation() throws Exception
	{
		IValue strValue1 = AnnotationFactory.createStringValue("value");
		IParamValuePair valuePair1 = AnnotationFactory.createParamValuePairValue("param", strValue1);

		IValue strValue2 = AnnotationFactory.createStringValue("value1");
		IParamValuePair valuePair2 = AnnotationFactory.createParamValuePairValue("param1", strValue2);
		Set<IParamValuePair> paramValues = new HashSet<IParamValuePair>();
		paramValues.add(valuePair1);
		paramValues.add(valuePair2);

		try
		{
			AnnotationFactory.createAnnotation(null, paramValues,endpoint);
			fail("NullPointerException not thrown");
		} catch (NullPointerException e)
		{
		}

		try
		{
			AnnotationFactory.createAnnotation("", null, endpoint);
			fail("NullPointerException not thrown");
			fail("IllegalArgumentException not thrown");
		}
		catch (NullPointerException e)
		{
		}
		catch (IllegalArgumentException e)
		{
		}
		prepareProject();
		IAnnotation<IType> ca = AnnotationFactory.createAnnotation("name", paramValues,endpoint);
		assertNotNull(ca);
		assertEquals(ca.getParamValuePairs().size(), 2);
	}

	public void testCreateValuePair()
	{
		try
		{
			AnnotationFactory.createParamValuePairValue("", null);
			fail("NullPointerException not thrown");
			fail("IllegalArgumentException not thrown");
		}
		catch (NullPointerException e)
		{
		}
		catch (IllegalArgumentException e)
		{
		}

		try
		{
			AnnotationFactory.createParamValuePairValue(null, null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException e)
		{
		}
	}

	public void testCreateArrayValue()
	{
		try
		{
			AnnotationFactory.createArrayValue(null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException e)
		{
		}

		IValue strValue1 = AnnotationFactory.createStringValue("value1");
		IValue strValue2 = AnnotationFactory.createStringValue("value2");
		Set<IValue> values = new LinkedHashSet<IValue>();
		values.add(strValue1);
		values.add(strValue2);
		
		
		IValue arrValue = AnnotationFactory.createArrayValue(values);
		assertNotNull(arrValue);
		assertTrue(arrValue.toString().equals("[value1, value2]"));
	}

	public void testCreateQualifiedNameValue()
	{
		try
		{
			AnnotationFactory.createQualifiedNameValue(null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException e)
		{
		}

		IValue qName = AnnotationFactory.createQualifiedNameValue("org.eclipse.demo.Test");
		assertNotNull(qName);
		assertTrue(qName.toString().equals("org.eclipse.demo.Test"));
	}

	public void testCreateBooleanValue()
	{
		IValue boolValue = AnnotationFactory.createBooleanValue(true);
		assertNotNull(boolValue);
		assertEquals(boolValue.toString(), "true");
	}

	public void testGetAnnotationsFromClass() throws Exception // $JL-EXC$
	{
		prepareProject();

		Collection<IAnnotation<IType>> annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		assertNotNull(annotations);
		assertTrue(annotations.size() == 2);
	}

//	public void testGetAnnotationsFromField() throws Exception // $JL-EXC$
//	{
//		prepareProject();
//
//		IField field = endpoint.getField("field1");
//		assertNotNull(field);
//
//		IAnnotation[] annotations = AnnotationFactory.createAnnotattionInspector(endpoint).inspectMethod(method) getAnnotationsFromField(field);
//		assertNotNull(annotations);
//		assertTrue(annotations.length == 1);
//	}

	public void testGetAnnotationsFromMethod() throws Exception // $JL-EXC$
	{
		prepareProject();

		IMethod method = endpoint.getMethod("annotatedMethod", new String[] { "QString;" });
		Collection<IAnnotation<IMethod>> annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method);;
		assertNotNull(annotations);
		assertTrue(annotations.size() == 1);
	}

	public void testRemoveAnnotationsIType() throws Exception // $JL-EXC$
	{
		prepareProject();

		AnnotationFactory.removeAnnotationsFromJavaElement(endpoint);

		Collection<IAnnotation<IType>> annotations = AnnotationFactory.createAnnotationInspector(endpoint). inspectType();
		assertTrue(annotations.size() == 0);

		IMethod method = endpoint.getMethod("annotatedMethod", new String[] { "QString;" });
		@SuppressWarnings("unused")
		Collection<IAnnotation<IMethod>> annotationsMethod = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method);
		assertTrue(annotations.size() == 0);
	}

	public void testRemoveAnnotationsITypeSetOfString() throws Exception // $JL-EXC$
	{
		prepareProject();

		Set<String> toRemove = new HashSet<String>();
		toRemove.add("WebService");
		toRemove.add("WebMethod");

		AnnotationFactory.removeAnnotations(endpoint, toRemove);

		Collection<IAnnotation<IType>> annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		assertTrue(annotations.size() == 1);

		IMethod method = endpoint.getMethod("annotatedMethod", new String[] { "QString;" });
		Collection<IAnnotation<IMethod>> annotationsMethod = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method);
		assertTrue(annotationsMethod.size() == 0);
	}

	public void testRemoveAnnotationsIField() throws Exception // $JL-EXC$
	{
		prepareProject();

		IField field = endpoint.getField("field1");
		assertNotNull(field);

		AnnotationFactory.removeAnnotationsFromJavaElement(field);

		Collection<IAnnotation<IType>> annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		assertTrue(annotations.size() == 2);

		IMethod method = endpoint.getMethod("annotatedMethod", new String[] { "QString;" });
		Collection<IAnnotation<IMethod>> annotationsMethod = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method);
		assertTrue(annotationsMethod.size() == 1);

//		annotations = AnnotationFactory.getAnnotationsFromField(field);
//		assertNotNull(annotations);
//		assertTrue(annotations.length == 0);
	}

	public void testRemoveAnnotationsIMethod() throws Exception // $JL-EXC$
	{
		prepareProject();

		IMethod method = endpoint.getMethod("annotatedMethod", new String[] { "QString;" });
		@SuppressWarnings("unused")
		IField field = endpoint.getField("field1");

		AnnotationFactory.removeAnnotationsFromJavaElement(method);

		Collection<IAnnotation<IType>> annotations = AnnotationFactory.createAnnotationInspector(endpoint).inspectType();
		assertTrue(annotations.size() == 2);

		Collection<IAnnotation<IMethod>> annotationsMethod = AnnotationFactory.createAnnotationInspector(endpoint).inspectMethod(method);
		assertTrue(annotationsMethod.size()== 0);

//		annotations = AnnotationFactory.getAnnotationsFromField(field);
//		assertNotNull(annotations);
//		assertTrue(annotations.length == 0);
	}

	private void prepareProject() throws Exception // $JL-EXC$
	{
		createJavaProject(SRC, PCK);
		endpoint = createClass("Endpoint.src", "Endpoint");
	}
}
