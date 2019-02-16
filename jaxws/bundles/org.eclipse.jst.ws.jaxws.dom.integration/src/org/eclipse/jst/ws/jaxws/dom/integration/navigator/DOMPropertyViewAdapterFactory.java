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
package org.eclipse.jst.ws.jaxws.dom.integration.navigator;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

public class DOMPropertyViewAdapterFactory implements IAdapterFactory 
{
	private DOMAdapterFactoryContentProvider adapterFactory;
	
	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) 
	{
        if (adapterType.isInstance(adaptableObject)) 
        {
            return adaptableObject;
        }
        
        if (adapterType == IPropertySourceProvider.class) 
        {
        	return getAdapterFactory();
        }
        
		return null;
	}

	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() 
	{
        return new Class[] {
        		DOMAdapterFactoryContentProvider.class,
        };
	}
	
	protected DOMAdapterFactoryContentProvider getAdapterFactory()
	{
		if (adapterFactory == null) {
			adapterFactory = new DOMAdapterFactoryContentProvider();
		}
		
		return adapterFactory;
	}
}
