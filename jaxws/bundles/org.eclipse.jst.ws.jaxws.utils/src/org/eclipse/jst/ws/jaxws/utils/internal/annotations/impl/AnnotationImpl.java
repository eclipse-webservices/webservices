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

import java.util.Arrays;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;

/**
 * An implementation of {@link IAnnotation}
 * 
 * @author Plamen Pavlov
 */
public class AnnotationImpl<T extends IJavaElement> extends AnnotationBaseImpl<T> implements IAnnotation<T>
{

	/**
	 * @see org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation#getPropertyValue(java.lang.String)
	 */
	public String getPropertyValue(String param)
	{
		for (IParamValuePair pair : getParamValuePairs())
		{
			if (param.equals(pair.getParam()))
			{
				return pair.getValue()!=null ? pair.getValue().toString() : null;
			}
		}
		return null;
	}

	private Set<IParamValuePair> paramValuePairs;

	/**
	 * Constructor
	 * 
	 * @param annotationName -
	 *            the name of annotation
	 * @param paramValuePairs -
	 *            the param=value paris
	 */
	public AnnotationImpl(String annotationName, Set<IParamValuePair> paramValuePairs)
	{
		super(annotationName);
		this.paramValuePairs = paramValuePairs;
	}

	public Set<IParamValuePair> getParamValuePairs()
	{
		return paramValuePairs;
	}

	public void setParamValuePairs(Set<IParamValuePair> paramValuePairs)
	{
		this.paramValuePairs = paramValuePairs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Expression getExpression(CompilationUnit unit, AST ast)
	{
		NormalAnnotation annot = ast.newNormalAnnotation();
		boolean wasAdded = addImports(ast, unit, getAnnotationName());
		if (wasAdded)
		{
			((SimpleName) annot.getTypeName()).setIdentifier(getSimpleAnnotationName());
		} else
		{
			Name name = ast.newName(getAnnotationName());
			annot.setTypeName(name);
		}
		for (IParamValuePair paramValuePair : paramValuePairs)
		{
			MemberValuePair pair = ast.newMemberValuePair();
			pair.getName().setIdentifier(paramValuePair.getParam());
			pair.setValue(((ValueImpl) paramValuePair.getValue()).getExpression(unit, ast));
			annot.values().add(pair);
		}
		return annot;
	}

	@Override
	public int hashCode()
	{
		if(paramValuePairs == null || paramValuePairs.size() < 1)
		{
			return 31 * super.hashCode();
		}
		return 31 * super.hashCode() + Arrays.hashCode(paramValuePairs.toArray(new IParamValuePair[paramValuePairs.size()]));
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!super.equals(obj))
		{
			return false;
		}
		final AnnotationImpl<?> other = (AnnotationImpl<?>) obj;
		return (Arrays.equals(paramValuePairs.toArray(new IParamValuePair[paramValuePairs.size()]), other.paramValuePairs.toArray(new IParamValuePair[paramValuePairs.size()])));
	}

	public T getAppliedElement()
	{
		return this.javaElement;
	}
}
