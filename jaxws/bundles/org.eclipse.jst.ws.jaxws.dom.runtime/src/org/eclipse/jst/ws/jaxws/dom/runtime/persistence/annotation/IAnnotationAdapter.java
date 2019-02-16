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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

/**
 * Adapter that holds location information for the annotations applied on the
 * java artifact/s which is represent by the DOM object adapted to this interface.
 * 
 * @author Georgi Vachkov
 */
public interface IAnnotationAdapter 
{
	/**
	 * Provides the {@link IAnnotation} for annotation with <code>annFQName</code>. 
	 * @param annFQName the fully qualified name of the annotation
	 * @return locator instance or <code>null</code> if not existing
	 */
	public IAnnotation<? extends IJavaElement> getAnnotation(String annFQName);
	
	/**
	 * Adds annotation to the list of used annotations.
	 * <code>annotation</code> can be <code>null</code> then the locator for this annotation
	 * is removed.
	 * @param annFQName the fully qualified name of the annotation
	 * @param annotation th annotation instance
	 */
	public void addAnnotation(String annFQName, IAnnotation<? extends IJavaElement> annotation);
}
