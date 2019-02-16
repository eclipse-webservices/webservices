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

import java.lang.annotation.ElementType;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.AnnotationProperty;

/**
 * Interface for annotations container.
 * 
 * @author Plamen Pavlov
 *
 */
public interface IAnnotationPropertyContainer
{
	public void addAnnotationProperty(final AnnotationProperty annotationProperty, final ElementType target);
	
	public Set<IAnnotation<ITypeParameter>> getParameterAnnotations(ITypeParameter tParam);

	public Set<IAnnotation<IType>> getTypeAnnotations(IType type);

	public Set<IAnnotation<IMethod>> getMethodAnnotations(IMethod method);
}
