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

import java.util.Collection;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaModelException;

public interface IAnnotationInspector
{
	/**
	 * Retrieves all Type level Annotations from <code>type</code>, which is used to instantiate the IAnnotationInspector.
	 * 
	 * @return contained Type level Annotations in <code>type</code> or empty Collection if not present.
	 *
	 * @throws JavaModelException
	 */
	Collection<IAnnotation<IType>> inspectType() throws JavaModelException;

	/**
	 * Retrieves a Type level Annotation with specific QName from <code>type</code>, which is used to instantiate the IAnnotationInspector.
	 * 
	 * @param annotationQName - specified QName for the Annotation.
	 * 
	 * @return Type level Annotation in <code>type</code> or <code>null</code>, if such Annotation do not present.
	 * 
	 * @throws NullPointerException if <code>annotationQName</code> is <code>null</code>.
	 * @throws IllegalArgumentException if <code>annotationQName</code> is empty String.
	 * @throws JavaModelException
	 */
	IAnnotation<IType> inspectType(final String annotationQName) throws JavaModelException;

	/**
	 * Retrieves all Method level Annotations from <code>method</code>.
	 * 
	 * @param method
	 * 
	 * @return contained Method level Annotations in <code>method</code> or empty Collection if not present.
	 * 
	 * @throws NullPointerException if <tt>method</tt> is null.
	 * @throws JavaModelException
	 */
   Collection<IAnnotation<IMethod>> inspectMethod(IMethod method) throws JavaModelException;

	/**
	 * Retrieves a Method level Annotation with specific QName from <code>method</code>.
	 * 
	 * @param method
	 * @param annotationQName - specified QName for the Annotation.
	 * 
	 * @return contained Method level Annotation in <code>method</code> or <code>null</code>.
	 * 
	 * @throws NullPointerException if <tt>method</tt> or <code>annotationQName</code> are <code>null</code>.
	 * @throws IllegalArgumentException if <code>annotationQName</code> is empty String.
	 * @throws JavaModelException
	 */
  IAnnotation<IMethod> inspectMethod(IMethod method, final String annotationQName) throws JavaModelException;

    /**
	 * Retrieves all Field level Annotations from <code>field</code>.
	 * 
	 * @param field
	 * 
	 * @return contained Filed level Annotations in <code>field</code> or empty Collection if not present.
	 * 
	 * @throws NullPointerException if <code>field</code> is <code>null</code>.
	 * @throws JavaModelException
	 */
  Collection<IAnnotation<IField>> inspectField(IField field) throws JavaModelException;
   
    /**
	 * Retrieves a Field level Annotation with specific QName from <code>field</code>.
	 * 
	 * @param field
	 * @param annotationQName - specified QName for the Annotation.
	 * 
	 * @return contained Filed level Annotations in <code>field</code> or <code>null</code>.
	 * 
	 * @throws NullPointerException if <code>field</code> or <code>annotationQName</code> are <code>null</code>.
	 * @throws IllegalArgumentException if <code>annotationQName</code> is empty String.
	 * @throws JavaModelException
	 */
  IAnnotation<IField> inspectField(IField field, final String annotationQName) throws JavaModelException;

    /**
	 * Retrieves all TypeParameter level Annotations from <code>param</code>.
	 * 
	 * @param param
	 * 
	 * @return contained TypeParameter level Annotations in <code>param</code> or empty Collection if not present.
	 * 
	 * @throws NullPointerException if <code>param</code> is <code>null</code>.
	 * @throws JavaModelException
	 */
  Collection<IAnnotation<ITypeParameter>> inspectParam(ITypeParameter param) throws JavaModelException;
   
    /**
	 * Retrieves a TypeParameter level Annotation with specific QName from <code>param</code>.
	 * 
	 * @param param
	 * @param annotationQName - specified QName for the Annotation.
	 * 
	 * @return contained TypeParameter level Annotations in <code>param</code> or <code>null</code>.
	 * 
	 * @throws NullPointerException if <code>param</code> or <code>annotationQName</code> are <code>null</code>.
	 * @throws IllegalArgumentException if <code>annotationQName</code> is empty String.
	 * @throws JavaModelException
	 */
  IAnnotation<ITypeParameter> inspectParam(ITypeParameter param, final String annotationQName) throws JavaModelException;
   
   
   
	
//	/**
//	 * Retrieves all annotations from <code>javaElement</code>.
//	 * 
//	 * @param javaElement
//	 * 
//	 * @return contained annotations in <code>javaElement</code> or empty Colleaction if not present.
//	 * 
//	 * @throws NullPointerException if <tt>javaElement</tt> is null.
//	 * @throws IllegalArgumentException in case <code>javaElement</code> is not of the correct type. Supported types are: Itype, IMethod, IField.
//	 * @throws JavaModelException
//	 */
//	public Collection<IAnnotation> inspect(IJavaElement javaElement) throws JavaModelException;
}
