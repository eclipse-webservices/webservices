/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060419   132905 cbrealey@ca.ibm.com - Chris Brealey          
 *******************************************************************************/
package org.eclipse.jst.ws.internal.conformance;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

/**
 * @author cbrealey
 * This is a garden variety implementation of IJavaBeanProperty,
 * constructable by default and equipped with getters and setters
 * for the properties of IJavaBeanProperty.
 */
public class JavaBeanProperty implements IJavaBeanProperty
{
	private String name_;
	private boolean indexed_;
	private IMethod getter_;
	private IMethod setter_;
	private IMethod indexedGetter_;
	private IMethod indexedSetter_;
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaBeanProperty#getName()
	 */
	public String getName ()
	{
		return name_;
	}
	
	/**
	 * Sets the "name" property of this Java Bean Property descriptor.
	 * @param name The string value to set.
	 */
	public void setName ( String name )
	{
		name_ = name;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaBeanProperty#isIndexed()
	 */
	public boolean isIndexed ()
	{
		return indexed_;
	}
	
	/**
	 * Sets the "indexed" property of this Java Bean Property descriptor.
	 * @param indexed The boolean value to set.
	 */
	public void setIndexed ( boolean indexed )
	{
		indexed_ = indexed;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaBeanProperty#getGetter()
	 */
	public IMethod getGetter ()
	{
		return getter_;
	}
	
	/**
	 * Sets the "getter" property of this Java Bean Property descriptor.
	 * @param getter The IMethod to set.
	 */
	public void setGetter ( IMethod getter )
	{
		getter_ = getter;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaBeanProperty#getSetter()
	 */
	public IMethod getSetter ()
	{
		return setter_;
	}
	
	/**
	 * Sets the "setter" property of this Java Bean Property descriptor.
	 * @param setter The IMethod to set.
	 */
	public void setSetter ( IMethod setter )
	{
		setter_ = setter;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaBeanProperty#getIndexedGetter()
	 */
	public IMethod getIndexedGetter ()
	{
		return indexedGetter_;
	}
	
	/**
	 * Sets the "indexedGetter" property of this Java Bean Property descriptor.
	 * @param indexedGetter The IMethod to set.
	 */
	public void setIndexedGetter ( IMethod indexedGetter )
	{
		indexedGetter_ = indexedGetter;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaBeanProperty#getIndexedSetter()
	 */
	public IMethod getIndexedSetter ()
	{
		return indexedSetter_;
	}
	
	/**
	 * Sets the "indexedSetter" property of this Java Bean Property descriptor.
	 * @param indexedSetter The IMethod to set.
	 */
	public void setIndexedSetter ( IMethod indexedSetter )
	{
		indexedSetter_ = indexedSetter;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jst.ws.internal.conformance.IJavaBeanProperty#getDeclaringType()
	 */
	public IType getDeclaringType ()
	{
		IMethod method = getGetter();
		if (method == null) method = getSetter();
		if (method == null) method = getIndexedGetter();
		if (method == null) method = getIndexedSetter();
		return method != null ? method.getDeclaringType() : null;
	}
}
