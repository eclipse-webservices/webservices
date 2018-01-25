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
package org.eclipse.jst.ws.jaxws.utils.annotations;

import java.io.FileNotFoundException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

/**
 * Base interface representing Annotation and the functionality, required by it.
 *
 * @author Plamen Pavlov
 */
public interface IAnnotationBase<T extends IJavaElement>
{
	/** simple annotation type ID */
	public final int SIMPLE_ANNOTATION = 1;

	/** single element annotation type ID */
	public final int SINGLE_ELEMENT_ANNOTATION = 2;

	/** complex annotation type ID */
	public final int COMPLEX_ANNOTATION = 4;

	/**
	 * @return - annotation name
	 */
	public String getAnnotationName();

	/**
	 * Getting the TypeID of current Annotation
	 * 
	 * @return Integer value, which represents the Annotation TypeID
	 */
	public int getType();
	
	/**
	 * Adds current Annotation to specific IJavaElement. It is not possible to have IMember as parameter because there is ITypeParameter as parameter when add to TypParameter is executed.
	 * 
	 * @param javaElement
	 * 
	 * @throws NullPointerException if <tt>javaElement</tt> is null.
	 * @throws IllegalArgumentException in case <code>javaElement</code> is not of the correct type. Supported types are: IType, IMethod, IField, ITypeParameter.
	 * @throws AnnotationGeneratorException.
	 */
	public void setAppliedElement(T javaElement) throws AnnotationGeneratorException;
	
	/**
	 * Remove the current Annotation from the associated JavaElement.
	 * 
	 * @throws BadLocationException 
	 * @throws CoreException 
	 * @throws AnnotationGeneratorException 
	 * @throws FileNotFoundException 
	 * @throws MalformedTreeException 
	 */
	public void remove() throws MalformedTreeException, FileNotFoundException, AnnotationGeneratorException, CoreException, BadLocationException;
	
	/**
	 * Gets the information about location in the source code.
	 * 
	 * @return ILocator
	 */
	public ILocator getLocator();
}
