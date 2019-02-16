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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.sync;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WMAnnotationFeatures.WM_ANNOTATION;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IModelElementSynchronizer;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WMAnnotationFeatures;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;

public class WSMethodSynchronizer extends AbstractMethodSynchronizer
{

	public WSMethodSynchronizer(IModelElementSynchronizer parent)
	{
		super(parent);
	}

	@Override
	protected void visitExposableMethods(IMethodVisitor visitor, ITypeHierarchy hierarchy, IAnnotationInspector inspector) throws JavaModelException
	{
		visitRecursively(visitor, hierarchy.getType(), hierarchy, inspector);
	}

	private void visitRecursively(IMethodVisitor visitor, IType type, ITypeHierarchy hierarchy, IAnnotationInspector inspector) throws JavaModelException
	{
		if (type == null)
		{
			return;
		}
	
		assert type.isClass();
		final boolean isWebService = inspector.inspectType(WS_ANNOTATION) != null;
		for (IMethod method : type.getMethods())
		{
			if(method.isConstructor())
			{
				// Constructors can never take part in the porttype
				continue;
			}
			
			final IAnnotation<IMethod> wmAnnotation = inspector.inspectMethod(method, WM_ANNOTATION);
			final boolean isExcludedWithAnnotation = wmAnnotation == null ? false : "true".equals(wmAnnotation//$NON-NLS-1$
											.getPropertyValue(WMAnnotationFeatures.WM_EXCLUDED_ATTRIBUTE));
			if (Modifier.isPublic(method.getFlags()) && !isExcludedWithAnnotation && (isWebService || wmAnnotation != null))
			{
				visitor.visit(method, inspector);
			}
		}
		
		final IType superType = hierarchy.getSuperclass(type);
		if (superType!=null) {
			visitRecursively(visitor, superType, hierarchy, resource().newAnnotationInspector(superType));
		}
	}

}
