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
package org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl;

import java.lang.annotation.ElementType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jst.ws.jaxws.utils.annotations.AnnotationFactory;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationPropertyContainer;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;

public class AnnotationPropertyContainer implements IAnnotationPropertyContainer
{
	private Map<String, Set<IParamValuePair>> typeAnnotationName2ParamValuesMap = 
		new HashMap<String, Set<IParamValuePair>>();
	private Map<String, Set<IParamValuePair>> methodAnnotationName2ParamValuesMap = 
		new HashMap<String, Set<IParamValuePair>>();
	private Map<String, Set<IParamValuePair>> parameterAnnotationName2ParamValuesMap = 
		new HashMap<String, Set<IParamValuePair>>();
	
	/**
	 * Add Annotation Property to the container. 
	 * 
	 * @throws NullPointerException if the <code>target</code> or <code>annotationProperty</code> is <code>null</code>.
	 */
	public void addAnnotationProperty(final AnnotationProperty annotationProperty, final ElementType target)
	{
		if(annotationProperty == null)
		{
			throw new NullPointerException("annotationProperty could not be null!"); //$NON-NLS-1$
		}
		if(target == null)
		{
			throw new NullPointerException("target could not be null!"); //$NON-NLS-1$
		}

		if(!(target.equals(ElementType.TYPE)) && !(target.equals(ElementType.METHOD)) && !(target.equals(ElementType.PARAMETER)))
		{
			throw new IllegalArgumentException("target parameter is not of correct type"); //$NON-NLS-1$
		}
		Map<String, Set<IParamValuePair>> annotationName2ParamValuesMap = null;
		
		if(target.equals(ElementType.TYPE))
		{
			annotationName2ParamValuesMap = typeAnnotationName2ParamValuesMap;
		}
		if(target.equals(ElementType.METHOD))
		{
			annotationName2ParamValuesMap = methodAnnotationName2ParamValuesMap;
		}
		if(target.equals(ElementType.PARAMETER))
		{
			annotationName2ParamValuesMap = parameterAnnotationName2ParamValuesMap;
		}
		
		
		Set<IParamValuePair> paramValuesSet = annotationName2ParamValuesMap.get(annotationProperty.getAnnotationName());
		if(paramValuesSet == null)
		{
			paramValuesSet = new HashSet<IParamValuePair>();
			annotationName2ParamValuesMap.put(annotationProperty.getAnnotationName(), paramValuesSet);
		}

		IValue value = null; 
		if(annotationProperty.getAttributeType() == null)
		{
			return;
		}
		
		if(annotationProperty.getAttributeType().equals(AttributeTypeEnum.BOOLEAN))
		{
			value = AnnotationFactory.createBooleanValue(new Boolean(annotationProperty.getValue()));
		}
		if(annotationProperty.getAttributeType().equals(AttributeTypeEnum.CLASS))
		{
			//TODO change this!!!!!!
			//value = AnnotationFactory.createClassValue(annotationProperty.getValue());
		}
		if(annotationProperty.getAttributeType().equals(AttributeTypeEnum.INTEGER))
		{
			value = AnnotationFactory.createIntegerValue(annotationProperty.getValue());
		}
		if(annotationProperty.getAttributeType().equals(AttributeTypeEnum.QUALIFIED_NAME))
		{
			value = AnnotationFactory.createQualifiedNameValue(annotationProperty.getValue());
		}
		if(value == null)
		{
			value = AnnotationFactory.createStringValue(annotationProperty.getValue());
		}

		IParamValuePair paramValuePair = AnnotationFactory.createParamValuePairValue(annotationProperty.getAttributeName(), value);
		
		paramValuesSet.add(paramValuePair);
	}
	/**
	 * @param ITypeParameter 
	 * @return Set with all Annotation from the container, which are ITypeParameter relevant.
	 */
	public Set<IAnnotation<ITypeParameter>> getParameterAnnotations(ITypeParameter tParam)
	{
		return getAnnotations(ElementType.PARAMETER, tParam);
	}

	/**
	 * @return Set with all Annotation from the container, which are IType relevant.
	 */
	public Set<IAnnotation<IType>> getTypeAnnotations(IType type)
	{
		return getAnnotations(ElementType.TYPE, type);
	}

	/**
	 * @return Set with all Annotation from the container, which are IMethod relevant.
	 */
	public Set<IAnnotation<IMethod>> getMethodAnnotations(IMethod method)
	{
		return getAnnotations(ElementType.METHOD , method);
	}
	
	private <T extends IJavaElement> Set<IAnnotation<T>> getAnnotations(final ElementType target, final T iTarget)
	{
		if(!(target.equals(ElementType.TYPE)) && !(target.equals(ElementType.METHOD)) && !(target.equals(ElementType.PARAMETER)))
		{
			throw new IllegalArgumentException("target parameter is not of correct type"); //$NON-NLS-1$
		}

		Map<String, Set<IParamValuePair>> annotationName2ParamValuesMap = null;
		if(target.equals(ElementType.TYPE))
		{
			annotationName2ParamValuesMap = typeAnnotationName2ParamValuesMap;
		}
		if(target.equals(ElementType.METHOD))
		{
			annotationName2ParamValuesMap = methodAnnotationName2ParamValuesMap;
		}
		if(target.equals(ElementType.PARAMETER))
		{
			annotationName2ParamValuesMap = parameterAnnotationName2ParamValuesMap;
		}
		Set<IAnnotation<T>> result = new HashSet<IAnnotation<T>>();
		Set<String> keys = annotationName2ParamValuesMap.keySet();
		for (String key : keys)
		{
			Set<IParamValuePair> paramValuePairsSet = annotationName2ParamValuesMap.get(key);
			IAnnotation<T> annotation = AnnotationFactory.createAnnotation(key, paramValuePairsSet, iTarget);
			result.add(annotation);
		}
		
		return result;
	}	
}
