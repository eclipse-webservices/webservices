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
package org.eclipse.jst.ws.jaxws.dom.integration.navigator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.jst.ws.jaxws.dom.ui.IDOMPropertyValue;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * Adapter factory for that creates adapter from {@link IDOMPropertyValue} to
 * {@link IPropertySource} interface. This factory is registered in org.eclipse.core.runtime.adapters extension
 * point and is called when object of type {@link IDOMPropertyValue} needs to be
 * displayed or edited in property sheet.
 * 
 * @author Georgi Vachkov
 */
@SuppressWarnings("unchecked")
public class DOMItemValuePropertySourceFactory implements IAdapterFactory 
{
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object, java.lang.Class)
	 */
	public Object getAdapter(Object adaptableObject, Class adapterType) 
	{
		assert adaptableObject instanceof IDOMPropertyValue;
		
		final IDOMPropertyValue wrapper = (IDOMPropertyValue)adaptableObject;
		
		return new PropertySource(wrapper.getEditableValue(), wrapper);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	public Class[] getAdapterList() {
		return new Class[] {PropertySource.class};
	}
}
