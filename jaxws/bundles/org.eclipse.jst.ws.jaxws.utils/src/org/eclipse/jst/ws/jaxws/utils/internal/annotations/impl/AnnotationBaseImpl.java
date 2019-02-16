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

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationBase;

/**
 * Base {@link IAnnotationBase} implementation
 * 
 * @author Plamen Pavlov
 */
public abstract class AnnotationBaseImpl<T extends IJavaElement> extends ValueImpl
{
	private String annotationName;// Container for Annotation Name;
	protected T javaElement; // Conatiner for the parent IJavaElement;
	//private String annotationStringValue;//Conatiner to current String representation of this annotation
	
	private static final String DOT = "."; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param annotationName
	 * 
	 * @throws NullPointerException in case <code>annotationName</code> is null.
	 * @throws IllegalArgumentException in case <code>annotationName</code> is empty String.
	 */
	public AnnotationBaseImpl(String annotationName)
	{
		this.annotationName = annotationName;
	}

	/**
	 * Extracts only the Annotation name in case it is in fully qualified form.
	 * 
	 * @return the simple Annotation name.
	 */
	public String getSimpleAnnotationName()
	{
		int lastDotIndex = annotationName.lastIndexOf(DOT);

		if (lastDotIndex == -1)
		{
			return annotationName;
		}

		return annotationName.substring(lastDotIndex + 1);
	}

	public String getAnnotationName()
	{
		return annotationName;
	}

	/**
	 * Adds Annotation to specific IJavaElement.
	 * 
	 * @param javaElement
	 * 
	 * @throws NullPointerException if <tt>javaElement</tt> is null.
	 * @throws IllegalArgumentException in case <code>javaElement</code> is not of the correct type. Supported types are: IType, IMethod, IField, ITypeParameter.
	 */
	public void setAppliedElementWithoutSave(T javaElement)
	{
		setJavaElement(javaElement);
	}

	public void setJavaElement(T javaElement)
	{
		if (javaElement == null)
		{
			throw new NullPointerException("javaElement should not be null!"); //$NON-NLS-1$
		}
		if(!(javaElement instanceof IType) && !(javaElement instanceof IMethod) && !(javaElement instanceof IField) && !(javaElement instanceof ITypeParameter))
		{
			throw new IllegalArgumentException("javaElement is not of correct type!"); //$NON-NLS-1$
		}

		this.javaElement = javaElement;
	}

	@Override
	public int hashCode()
	{
		return annotationName.hashCode();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final AnnotationBaseImpl<T> other =  (AnnotationBaseImpl<T>)obj;
		return annotationName.equals(other.annotationName);
	}	
}
