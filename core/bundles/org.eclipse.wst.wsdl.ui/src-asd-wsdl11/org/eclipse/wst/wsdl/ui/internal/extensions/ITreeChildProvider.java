/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.extensions;

/**
 * This class allows extension writers to provide specialize tree content behaviour.
 * It is intended to behave similar to the org.eclipse.jface.viewers.ITreeContentProvider.
 * The major difference is that this class is simplified so that only one method is involved.
 * 
 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)  	
 */
public interface ITreeChildProvider
{
	public Object[] getChildren(Object object);
}
