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
package org.eclipse.jst.ws.jaxws.utils.annotations;

import static org.eclipse.jst.ws.jaxws.utils.ContractChecker.nullCheckParam;

import java.io.FileNotFoundException;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.AnnotationImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.AnnotationInspectorImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.AnnotationUtils;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.ArrayValueImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.BooleanValueImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.ClassValueImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.IntegerValueImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.ParamValuePairImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.QualifiedNameValueImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.StringValueImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.text.JaxWsUtilMessages;
import org.eclipse.text.edits.MalformedTreeException;


/**
 * Factory class containing common factory and utility methods Annotation processing.
 * 
 * @author Plamen Pavlov
 */
public class AnnotationFactory
{
	/**
	 * Creates complex Annotation, e.g. Annotation of kind \@WebService(param1="value1", param2="value2").
	 * 
	 * @param annotationQName
	 * @param paramValuePairs
	 * 
	 * @return Instantiated Annotation of type IComplexAnnotation.
	 * 
	 * @throws NullPointerException in case <tt>annotationQName</tt> or <tt>paramValuePairs</tt> are null.
	 * @throws IllegalArgumentException in case <tt>annotationQName</tt> is empty String.
	 */
	public static <T extends IJavaElement> IAnnotation<T> createAnnotation(String annotationQName, Set<IParamValuePair> paramValuePairs, final T appliedElement)
	{
		if (annotationQName == null)
		{
			throw new NullPointerException("annotationQName should not be null!"); //$NON-NLS-1$
		}
		if (annotationQName.trim().length() == 0)
		{
			throw new IllegalArgumentException("annotationQName should not be empty String!"); //$NON-NLS-1$
		}

		if (paramValuePairs == null)
		{
			throw new NullPointerException("paramValuePairs should not be null!"); //$NON-NLS-1$
		}

		final AnnotationImpl<T> impl = new AnnotationImpl<T>(annotationQName, paramValuePairs);
		impl.setJavaElement(appliedElement);
		
		return impl;
	}
	
	
	/**
	 * Creates {@link IAnnotation} instance that references <code>appliedElement</code> java element 
	 * @param annotationQName the annotation name
	 * @param appliedElement the java element that this annotation is applied on
	 * @param paramValuePairs the annotation attributes
	 * @return {@link IAnnotation} instance
	 * @throws NullPointerException in case some of params is <code>null</code>
	 * @throws IllegalArgumentException in case <code>annotationQName</code> is empty string
	 */
	public static IAnnotation<? extends IJavaElement> createAnnotation(String annotationQName, IJavaElement appliedElement, Set<IParamValuePair> paramValuePairs)
	{
		nullCheckParam(annotationQName, "annotationQName");	 //$NON-NLS-1$	
		if (annotationQName.trim().length() == 0) {
			throw new IllegalArgumentException("annotationQName should not be empty String!"); //$NON-NLS-1$
		}

		nullCheckParam(paramValuePairs, "paramValuePairs"); //$NON-NLS-1$
		nullCheckParam(appliedElement, "appliedElement"); //$NON-NLS-1$

		final AnnotationImpl<IJavaElement> annotation = new AnnotationImpl<IJavaElement>(annotationQName, paramValuePairs);
		annotation.setAppliedElementWithoutSave(appliedElement);
		
		return annotation;
	}	
	
	/**
	* Creates param-value pair needed in complex Annotations.
	 * 
	 * @param param
	 * @param value
	 * 
	 * @return instance of IParamValuePair
	 * 
	 * @throws NullPointerException in case <code>param</code> or <code>value</code> is <code>null</code>.
	 * @throws IllegalArgumentException in case <code>param</code> parameter is empty String.
	 */
	public static IParamValuePair createParamValuePairValue(String param, IValue value)
	{
		if (param == null)
		{
			throw new NullPointerException("param should not be null!"); //$NON-NLS-1$
		}
		if (param.trim().length() == 0)
		{
			throw new IllegalArgumentException("param should not be empty String!"); //$NON-NLS-1$
		}

		if (value == null)
		{
			throw new NullPointerException("value should not be null!"); //$NON-NLS-1$
		}

		return new ParamValuePairImpl(param, value);
	}
	
	/**
	 * Creates {@link IValue} instance using provided <code>values</code>.
	 * 
	 * @param values
	 * 
	 * @return instance of {@link IValue}
	 * 
	 * @throws NullPointerException in case <code>values</code> is null.
	 * @throws IllegalArgumentException in case <code>values</code> is empty Set.
	 */
	public static IValue createArrayValue(Set<IValue> values)
	{
		if (values == null)
		{
			throw new NullPointerException("values should not be null!"); //$NON-NLS-1$
		}
		if (values.size() < 1)
		{
			throw new IllegalArgumentException("values should not be empty Set!"); //$NON-NLS-1$
		}

		return new ArrayValueImpl(values);
	}
	
	/**
	 * Creates {@link IValue} instance out of <code>value</code>.
	 * 
	 * @param value
	 * 
	 * @return instance of {@link IValue}
	 */
	public static IValue createBooleanValue(boolean value)
	{
		return new BooleanValueImpl(value);
	}
		
	/**
	 * Creates {@link IValue} instance out of <code>value</code>.
	 * 
	 * @param value
	 * 
	 * @return instance of {@link String}
	 */
	public static IValue createClassValue(String value)
	{
		if (value == null)
		{
			throw new NullPointerException("value should not be null!"); //$NON-NLS-1$
		}
		if(value.equals("")) //$NON-NLS-1$
		{
			throw new IllegalArgumentException("value should not be empty String"); //$NON-NLS-1$
		}
		
		return new ClassValueImpl(value);
	}
	
	/**
	 * Creates {@link IValue} instance out of <code>value</code>.
	 * 
	 * @param value
	 * 
	 * @return instance of {@link IValue}
	 */
	public static IValue createIntegerValue(String value)
	{
		return new IntegerValueImpl(value);
	}

	/**
	 * Creates {@link IValue} instance using provided <code>qualifiedName</code> fully qualified name.
	 * 
	 * @param qualifiedName
	 *
	 * @return instance of {@link IValue}
	 * 
	 * @throws NullPointerException in case <tt>qualifiedName</tt> is null.
	 * @throws IllegalArgumentException in case <code>qualifiedName</code> is not in correct form e.g. could not be empty String, shold be qualified.
	 */
	public static IValue createQualifiedNameValue(String qualifiedName)
	{
		if (qualifiedName == null)
		{
			throw new NullPointerException("qualifiedName should not be null!"); //$NON-NLS-1$
		}

		if (qualifiedName.trim().length() == 0 || qualifiedName.indexOf('.') == -1)
		{
			throw new IllegalArgumentException("qualifiedName is not in correct form!"); //$NON-NLS-1$
		}

		return new QualifiedNameValueImpl(qualifiedName);
	}
	
	/**
	 * Creates {@link IValue} instance out of <code>value</code> content.
	 * 
	 * @param value
	 *
	 * @return instance of {@link IValue}
	 * @throws NullPointerException in case <tt>value</tt> is null.
	 */
	public static IValue createStringValue(String value)
	{
		if (value == null)
		{
			throw new NullPointerException("value should not be null!"); //$NON-NLS-1$
		}

		return new StringValueImpl(value);
	}
	
	
//	/**
//	 * Retrieves all Annotations from class.
//	 * 
//	 * @param javaElement
//	 *
//	 * @return contained annotations in <code>javaElement</code>
//	 * 
//	 * @throws JavaModelException if exception occurs
//	 * @throws NullPointerException if <tt>javaElement</tt> is null.
//	 * @throws IllegalArgumentException in case <code>javaElement</code> is not of the correct type. Supported types are: Itype, IMethod, IField, ITypeParameter
//	 */
//	public static Set<IAnnotation> getAnnotationsFromJavaElement(IJavaElement javaElement) throws JavaModelException
//	{
//		if (javaElement == null)
//		{
//			throw new NullPointerException("javaElement should not be null!");
//		}
//		if(!(javaElement instanceof IType) || !(javaElement instanceof IMethod) || !(javaElement instanceof IField) || !(javaElement instanceof ITypeParameter))
//		{
//			throw new IllegalArgumentException("javaElement is not of correct type!");
//		}
//		pppppp
//	}
	
	/**
	 * Removes all Annotations from <code>javaElement</code>.
	 * 
	 * @param javaElement
	 *
	 * @throws NullPointerException if <tt>javaElement</tt> is null.
	 * @throws IllegalArgumentException in case <code>javaElement</code> is not of the correct type. Supported types are: Itype, IMethod, IField, ITypeParameter
	 * @throws AnnotationGeneratorException
	 */
	public static void removeAnnotationsFromJavaElement(IJavaElement javaElement) throws AnnotationGeneratorException
	{
		if (javaElement == null)
		{
			throw new NullPointerException("javaElement should not be null!"); //$NON-NLS-1$
		}
		if(!(javaElement instanceof IType) && !(javaElement instanceof org.eclipse.jdt.core.IMethod) && !(javaElement instanceof IField) && !(javaElement instanceof ITypeParameter))
		{
			throw new IllegalArgumentException("javaElement is not of correct type!"); //$NON-NLS-1$
		}
		removeAnnotations(javaElement, null);
	}
	
	/**
	 * Removes Annotations with names contained in <code>annotations</code> from <code>javaElement</code> and its children, if such exist.
	 * 
	 * @param javaElement
	 * @param annotations
	 * 
	 * @throws NullPointerException if <tt>javaElement</tt> or <tt>annotations</tt> is null.
	 * @throws IllegalArgumentException in case <code>javaElement</code> is not of the correct type. Supported types are: Itype, IMethod, IField, ITypeParameter
	 * @throws AnnotationGeneratorException
	 */
	public static void removeAnnotations(IJavaElement javaElement, Set<String> annotations) throws AnnotationGeneratorException
	{
		if (javaElement == null)
		{
			throw new NullPointerException("javaElement should not be null!"); //$NON-NLS-1$
		}
		if(!(javaElement instanceof IType) && !(javaElement instanceof org.eclipse.jdt.core.IMethod) && !(javaElement instanceof IField) && !(javaElement instanceof ITypeParameter))
		{
			throw new IllegalArgumentException("javaElement is not of correct type!"); //$NON-NLS-1$
		}

		try 
		{
			AnnotationUtils.getInstance().removeAnnotations(javaElement, annotations, true);
		} 
		catch (JavaModelException e) 
		{
			throw new AnnotationGeneratorException(e.getMessage(), e.getStatus().getMessage(), e);
		} 
		catch (MalformedTreeException e) 
		{
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.InvalidTreeStateMsg, e);
		} 
		catch (FileNotFoundException e) 
		{
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.CompUnitMissingMsg, e);
		} 
		catch (CoreException e) 
		{
			throw new AnnotationGeneratorException(e.getMessage(), e.getStatus().getMessage(), e);
		}
		catch (BadLocationException e)
		{
			throw new AnnotationGeneratorException(e.getMessage(), JaxWsUtilMessages.CannotPerformEditMsg, e);
		}
	}
	
	/**
	 * Create instance of IAnnotationInspector for a specific IType.
	 * 
	 * @param type - IType, which will be inspected.
	 *  
	 * @return IAnnotationInspector
	 */
	public static IAnnotationInspector createAnnotationInspector(IType type)
	{
		return new AnnotationInspectorImpl(type);
	}
}
