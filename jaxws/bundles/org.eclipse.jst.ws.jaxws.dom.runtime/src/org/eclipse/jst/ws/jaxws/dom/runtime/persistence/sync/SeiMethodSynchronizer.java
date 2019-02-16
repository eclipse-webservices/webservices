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

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.IModelElementSynchronizer;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotationInspector;

class SeiMethodSynchronizer extends AbstractMethodSynchronizer
{

	public SeiMethodSynchronizer(IModelElementSynchronizer parent)
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
		for (IMethod method : type.getMethods())
		{
			visitor.visit(method, inspector);
		}

		for (IType superIntf : hierarchy.getSuperInterfaces(type))
		{			
			visitRecursively(visitor, superIntf, hierarchy, resource().newAnnotationInspector(superIntf));
		}
	}
}
