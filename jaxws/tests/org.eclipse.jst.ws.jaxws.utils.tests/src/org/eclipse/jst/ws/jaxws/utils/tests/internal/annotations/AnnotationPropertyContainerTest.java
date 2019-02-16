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

import java.lang.annotation.ElementType;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.AnnotationProperty;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.AnnotationPropertyContainer;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.AttributeTypeEnum;

public class AnnotationPropertyContainerTest extends MockObjectTestCase
{
	private Mock<IType> type = mock(IType.class);
	private Mock<IMethod> method = mock(IMethod.class);
	private Mock<ITypeParameter> typeParameter = mock(ITypeParameter.class);
	
	public void testBase() throws Exception
	{
		AnnotationPropertyContainer container = new AnnotationPropertyContainer();
		try 
		{
			container.addAnnotationProperty(null, null);
			fail("NullPointerException was not thrown!");
		}
		catch (NullPointerException e)
		{
			assertTrue(true);
		}

		try 
		{
			AnnotationProperty annotationProperty = new AnnotationProperty("annotationName", "attributeName", "value", AttributeTypeEnum.STRING);
			container.addAnnotationProperty(annotationProperty, null);
			fail("NullPointerException was not thrown!");
		}
		catch (NullPointerException e)
		{
			assertTrue(true);
		}

		try 
		{
			AnnotationProperty annotationProperty = new AnnotationProperty("annotationName", "attributeName", "value", AttributeTypeEnum.STRING);
			container.addAnnotationProperty(annotationProperty, ElementType.PACKAGE);
			fail("IllegalArgumentException was not thrown!");
		}
		catch (IllegalArgumentException e)
		{
			assertTrue(true);
		}

	}

	public void testTypeLevel() throws Exception
	{
		AnnotationPropertyContainer container = new AnnotationPropertyContainer();
		AnnotationProperty annotationProperty = new AnnotationProperty("annotationName", "attributeNameBoolean", "true", AttributeTypeEnum.BOOLEAN);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);
		annotationProperty = new AnnotationProperty("annotationName", "attributeNameInteger", "100", AttributeTypeEnum.INTEGER);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);
		annotationProperty = new AnnotationProperty("annotationName", "attributeNameQualified", "org.eclipse.testEnumeration.VALUE", AttributeTypeEnum.QUALIFIED_NAME);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);
		annotationProperty = new AnnotationProperty("annotationName", "attributeNameString", "valueString", AttributeTypeEnum.STRING);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);
		
		assertTrue(container.getTypeAnnotations(type.proxy()).size() == 1);
		assertTrue(container.getParameterAnnotations(typeParameter.proxy()).size() == 0);
		assertTrue(container.getMethodAnnotations(method.proxy()).size() == 0);
		Set<IAnnotation<IType>> typeAnnotations = container.getTypeAnnotations(type.proxy());
		
		for (IAnnotation<IType> annotation : typeAnnotations)
		{
			assertTrue(annotation.getAnnotationName().equals("annotationName"));
			assertTrue(annotation.getParamValuePairs().size() == 4);
			assertTrue(annotation.getPropertyValue("attributeNameBoolean").equals("true"));
			assertTrue(annotation.getPropertyValue("attributeNameInteger").equals("100"));
			assertTrue(annotation.getPropertyValue("attributeNameQualified").equals("org.eclipse.testEnumeration.VALUE"));
			assertTrue(annotation.getPropertyValue("attributeNameString").equals("valueString"));
		}

		container = new AnnotationPropertyContainer();
		annotationProperty = new AnnotationProperty("annotationName1", "attributeNameBoolean", "true", AttributeTypeEnum.BOOLEAN);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);
		annotationProperty = new AnnotationProperty("annotationName2", "attributeNameInteger", "100", AttributeTypeEnum.INTEGER);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);
		annotationProperty = new AnnotationProperty("annotationName3", "attributeNameQualified", "org.eclipse.testEnumeration.VALUE", AttributeTypeEnum.QUALIFIED_NAME);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);
		annotationProperty = new AnnotationProperty("annotationName4", "attributeNameString", "valueString", AttributeTypeEnum.STRING);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);
		
		assertTrue(container.getTypeAnnotations(type.proxy()).size() == 4);
		assertTrue(container.getParameterAnnotations(typeParameter.proxy()).size() == 0);
		assertTrue(container.getMethodAnnotations(method.proxy()).size() == 0);
		typeAnnotations = container.getTypeAnnotations(type.proxy());

		Set<String> names = new HashSet<String>();
		names.clear();
		names.add("annotationName1");
		names.add("annotationName2");
		names.add("annotationName3");
		names.add("annotationName4");
		
		for (IAnnotation<IType> annotation : typeAnnotations)
		{
			assertTrue(names.contains(annotation.getAnnotationName()));
			names.remove(annotation.getAnnotationName());
			if(annotation.getAnnotationName().equals("annotationName1"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);	
				assertTrue(annotation.getPropertyValue("attributeNameBoolean").equals("true"));
			}
			if(annotation.getAnnotationName().equals("annotationName2"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);	
				assertTrue(annotation.getPropertyValue("attributeNameInteger").equals("100"));
			}
			if(annotation.getAnnotationName().equals("annotationName3"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);	
				assertTrue(annotation.getPropertyValue("attributeNameQualified").equals("org.eclipse.testEnumeration.VALUE"));
			}
			if(annotation.getAnnotationName().equals("annotationName4"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);
				assertTrue(annotation.getPropertyValue("attributeNameString").equals("valueString"));				
			}
		}
	}
	
	public void testMethodLevel() throws Exception
	{
		AnnotationPropertyContainer container = new AnnotationPropertyContainer();
		AnnotationProperty annotationProperty = new AnnotationProperty("annotationName", "attributeNameBoolean", "true", AttributeTypeEnum.BOOLEAN);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);
		annotationProperty = new AnnotationProperty("annotationName", "attributeNameInteger", "100", AttributeTypeEnum.INTEGER);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);
		annotationProperty = new AnnotationProperty("annotationName", "attributeNameQualified", "org.eclipse.testEnumeration.VALUE", AttributeTypeEnum.QUALIFIED_NAME);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);
		annotationProperty = new AnnotationProperty("annotationName", "attributeNameString", "valueString", AttributeTypeEnum.STRING);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);
		
		assertTrue(container.getTypeAnnotations(type.proxy()).size() == 0);
		assertTrue(container.getParameterAnnotations(typeParameter.proxy()).size() == 0);
		assertTrue(container.getMethodAnnotations(method.proxy()).size() == 1);
		Set<IAnnotation<IMethod>> methodAnnotations = container.getMethodAnnotations(method.proxy());
		
		for (IAnnotation<IMethod> annotation : methodAnnotations)
		{
			assertTrue(annotation.getAnnotationName().equals("annotationName"));
			assertTrue(annotation.getParamValuePairs().size() == 4);
			assertTrue(annotation.getPropertyValue("attributeNameBoolean").equals("true"));
			assertTrue(annotation.getPropertyValue("attributeNameInteger").equals("100"));
			assertTrue(annotation.getPropertyValue("attributeNameQualified").equals("org.eclipse.testEnumeration.VALUE"));
			assertTrue(annotation.getPropertyValue("attributeNameString").equals("valueString"));
		}

		container = new AnnotationPropertyContainer();
		annotationProperty = new AnnotationProperty("annotationName1", "attributeNameBoolean", "true", AttributeTypeEnum.BOOLEAN);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);
		annotationProperty = new AnnotationProperty("annotationName2", "attributeNameInteger", "100", AttributeTypeEnum.INTEGER);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);
		annotationProperty = new AnnotationProperty("annotationName3", "attributeNameQualified", "org.eclipse.testEnumeration.VALUE", AttributeTypeEnum.QUALIFIED_NAME);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);
		annotationProperty = new AnnotationProperty("annotationName4", "attributeNameString", "valueString", AttributeTypeEnum.STRING);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);
		
		assertTrue(container.getTypeAnnotations(type.proxy()).size() == 0);
		assertTrue(container.getParameterAnnotations(typeParameter.proxy()).size() == 0);
		assertTrue(container.getMethodAnnotations(method.proxy()).size() == 4);
		methodAnnotations = container.getMethodAnnotations(method.proxy());

		Set<String> names = new HashSet<String>();
		names.clear();
		names.add("annotationName1");
		names.add("annotationName2");
		names.add("annotationName3");
		names.add("annotationName4");
		
		for (IAnnotation<IMethod> annotation : methodAnnotations)
		{
			assertTrue(names.contains(annotation.getAnnotationName()));
			names.remove(annotation.getAnnotationName());
			if(annotation.getAnnotationName().equals("annotationName1"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);	
				assertTrue(annotation.getPropertyValue("attributeNameBoolean").equals("true"));
			}
			if(annotation.getAnnotationName().equals("annotationName2"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);	
				assertTrue(annotation.getPropertyValue("attributeNameInteger").equals("100"));
			}
			if(annotation.getAnnotationName().equals("annotationName3"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);	
				assertTrue(annotation.getPropertyValue("attributeNameQualified").equals("org.eclipse.testEnumeration.VALUE"));
			}
			if(annotation.getAnnotationName().equals("annotationName4"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);
				assertTrue(annotation.getPropertyValue("attributeNameString").equals("valueString"));				
			}
		}
	}

	public void testParameterLevel() throws Exception
	{
		AnnotationPropertyContainer container = new AnnotationPropertyContainer();
		AnnotationProperty annotationProperty = new AnnotationProperty("annotationName", "attributeNameBoolean", "true", AttributeTypeEnum.BOOLEAN);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);
		annotationProperty = new AnnotationProperty("annotationName", "attributeNameInteger", "100", AttributeTypeEnum.INTEGER);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);
		annotationProperty = new AnnotationProperty("annotationName", "attributeNameQualified", "org.eclipse.testEnumeration.VALUE", AttributeTypeEnum.QUALIFIED_NAME);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);
		annotationProperty = new AnnotationProperty("annotationName", "attributeNameString", "valueString", AttributeTypeEnum.STRING);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);
		
		assertTrue(container.getTypeAnnotations(type.proxy()).size() == 0);
		assertTrue(container.getParameterAnnotations(typeParameter.proxy()).size() == 1);
		assertTrue(container.getMethodAnnotations(method.proxy()).size() == 0);
		Set<IAnnotation<ITypeParameter>> parameterAnnotations = container.getParameterAnnotations(typeParameter.proxy());
		
		for (IAnnotation<ITypeParameter> annotation : parameterAnnotations)
		{
			assertTrue(annotation.getAnnotationName().equals("annotationName"));
			assertTrue(annotation.getParamValuePairs().size() == 4);
			assertTrue(annotation.getPropertyValue("attributeNameBoolean").equals("true"));
			assertTrue(annotation.getPropertyValue("attributeNameInteger").equals("100"));
			assertTrue(annotation.getPropertyValue("attributeNameQualified").equals("org.eclipse.testEnumeration.VALUE"));
			assertTrue(annotation.getPropertyValue("attributeNameString").equals("valueString"));
		}

		container = new AnnotationPropertyContainer();
		annotationProperty = new AnnotationProperty("annotationName1", "attributeNameBoolean", "true", AttributeTypeEnum.BOOLEAN);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);
		annotationProperty = new AnnotationProperty("annotationName2", "attributeNameInteger", "100", AttributeTypeEnum.INTEGER);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);
		annotationProperty = new AnnotationProperty("annotationName3", "attributeNameQualified", "org.eclipse.testEnumeration.VALUE", AttributeTypeEnum.QUALIFIED_NAME);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);
		annotationProperty = new AnnotationProperty("annotationName4", "attributeNameString", "valueString", AttributeTypeEnum.STRING);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);
		
		assertTrue(container.getTypeAnnotations(type.proxy()).size() == 0);
		assertTrue(container.getParameterAnnotations(typeParameter.proxy()).size() == 4);
		assertTrue(container.getMethodAnnotations(method.proxy()).size() == 0);
		parameterAnnotations = container.getParameterAnnotations(typeParameter.proxy());

		Set<String> names = new HashSet<String>();
		names.clear();
		names.add("annotationName1");
		names.add("annotationName2");
		names.add("annotationName3");
		names.add("annotationName4");
		
		for (IAnnotation<ITypeParameter> annotation : parameterAnnotations)
		{
			assertTrue(names.contains(annotation.getAnnotationName()));
			names.remove(annotation.getAnnotationName());
			if(annotation.getAnnotationName().equals("annotationName1"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);	
				assertTrue(annotation.getPropertyValue("attributeNameBoolean").equals("true"));
			}
			if(annotation.getAnnotationName().equals("annotationName2"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);	
				assertTrue(annotation.getPropertyValue("attributeNameInteger").equals("100"));
			}
			if(annotation.getAnnotationName().equals("annotationName3"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);	
				assertTrue(annotation.getPropertyValue("attributeNameQualified").equals("org.eclipse.testEnumeration.VALUE"));
			}
			if(annotation.getAnnotationName().equals("annotationName4"))
			{
				assertTrue(annotation.getParamValuePairs().size() == 1);
				assertTrue(annotation.getPropertyValue("attributeNameString").equals("valueString"));				
			}
		}
	}

	public void testCombined() throws Exception
	{
		AnnotationPropertyContainer container = new AnnotationPropertyContainer();
		AnnotationProperty annotationProperty = new AnnotationProperty("paramAnnotationName", "paramAttributeNameBoolean", "true", AttributeTypeEnum.BOOLEAN);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);
		annotationProperty = new AnnotationProperty("paramAnnotationName", "paramAttributeNameInteger", "100", AttributeTypeEnum.INTEGER);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);
		annotationProperty = new AnnotationProperty("paramAnnotationName", "paramAttributeNameQualified", "org.eclipse.testEnumeration.PARAM_VALUE", AttributeTypeEnum.QUALIFIED_NAME);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);
		annotationProperty = new AnnotationProperty("paramAnnotationName", "paramAttributeNameString", "paramValueString", AttributeTypeEnum.STRING);
		container.addAnnotationProperty(annotationProperty, ElementType.PARAMETER);

		annotationProperty = new AnnotationProperty("methodAnnotationName", "methodAttributeNameBoolean", "false", AttributeTypeEnum.BOOLEAN);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);
		annotationProperty = new AnnotationProperty("methodAnnotationName", "methodAttributeNameInteger", "101", AttributeTypeEnum.INTEGER);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);
		annotationProperty = new AnnotationProperty("methodAnnotationName", "methodAttributeNameQualified", "org.eclipse.testEnumeration.METHOD_VALUE", AttributeTypeEnum.QUALIFIED_NAME);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);
		annotationProperty = new AnnotationProperty("methodAnnotationName", "methodAttributeNameString", "methodValueString", AttributeTypeEnum.STRING);
		container.addAnnotationProperty(annotationProperty, ElementType.METHOD);

		annotationProperty = new AnnotationProperty("typeAnnotationName", "typeAttributeNameBoolean", "false", AttributeTypeEnum.BOOLEAN);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);
		annotationProperty = new AnnotationProperty("typeAnnotationName", "typeAttributeNameInteger", "1", AttributeTypeEnum.INTEGER);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);
		annotationProperty = new AnnotationProperty("typeAnnotationName", "typeAttributeNameQualified", "org.eclipse.testEnumeration.TYPE_VALUE", AttributeTypeEnum.QUALIFIED_NAME);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);
		annotationProperty = new AnnotationProperty("typeAnnotationName", "typeAttributeNameString", "typeValueString", AttributeTypeEnum.STRING);
		container.addAnnotationProperty(annotationProperty, ElementType.TYPE);

		assertTrue(container.getTypeAnnotations(type.proxy()).size() == 1);
		assertTrue(container.getParameterAnnotations(typeParameter.proxy()).size() == 1);
		assertTrue(container.getMethodAnnotations(method.proxy()).size() == 1);
		
		Set<IAnnotation<ITypeParameter>> parameterAnnotations = container.getParameterAnnotations(typeParameter.proxy());
		for (IAnnotation<ITypeParameter> annotation : parameterAnnotations)
		{
			assertTrue(annotation.getAnnotationName().equals("paramAnnotationName"));
			assertTrue(annotation.getParamValuePairs().size() == 4);
			assertTrue(annotation.getPropertyValue("paramAttributeNameBoolean").equals("true"));
			assertTrue(annotation.getPropertyValue("paramAttributeNameInteger").equals("100"));
			assertTrue(annotation.getPropertyValue("paramAttributeNameQualified").equals("org.eclipse.testEnumeration.PARAM_VALUE"));
			assertTrue(annotation.getPropertyValue("paramAttributeNameString").equals("paramValueString"));
		}

		Set<IAnnotation<IMethod>> methodAnnotations = container.getMethodAnnotations(method.proxy());
		for (IAnnotation<IMethod> annotation : methodAnnotations)
		{
			assertTrue(annotation.getAnnotationName().equals("methodAnnotationName"));
			assertTrue(annotation.getParamValuePairs().size() == 4);
			assertTrue(annotation.getPropertyValue("methodAttributeNameBoolean").equals("false"));
			assertTrue(annotation.getPropertyValue("methodAttributeNameInteger").equals("101"));
			assertTrue(annotation.getPropertyValue("methodAttributeNameQualified").equals("org.eclipse.testEnumeration.METHOD_VALUE"));
			assertTrue(annotation.getPropertyValue("methodAttributeNameString").equals("methodValueString"));
		}

		Set<IAnnotation<IType>> typeAnnotations = container.getTypeAnnotations(type.proxy());
		for (IAnnotation<IType> annotation : typeAnnotations)
		{
			assertTrue(annotation.getAnnotationName().equals("typeAnnotationName"));
			assertTrue(annotation.getParamValuePairs().size() == 4);
			assertTrue(annotation.getPropertyValue("typeAttributeNameBoolean").equals("false"));
			assertTrue(annotation.getPropertyValue("typeAttributeNameInteger").equals("1"));
			assertTrue(annotation.getPropertyValue("typeAttributeNameQualified").equals("org.eclipse.testEnumeration.TYPE_VALUE"));
			assertTrue(annotation.getPropertyValue("typeAttributeNameString").equals("typeValueString"));
		}
	}
}
