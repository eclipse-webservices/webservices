/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.extension;

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
