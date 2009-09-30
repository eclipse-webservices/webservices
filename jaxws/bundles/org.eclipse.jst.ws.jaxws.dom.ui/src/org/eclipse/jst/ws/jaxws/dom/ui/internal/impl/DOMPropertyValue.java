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
package org.eclipse.jst.ws.jaxws.dom.ui.internal.impl;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.jst.ws.jaxws.dom.ui.IDOMPropertyValue;

/**
 * Implementor of {@link IDOMPropertyValue}.
 * 
 * @author Georgi Vachkov
 */
public class DOMPropertyValue extends ItemPropertyDescriptor.PropertyValueWrapper implements IDOMPropertyValue 
{
	public DOMPropertyValue(AdapterFactory adapterFactory, Object object, Object propertyValue, Object nestedPropertySourc) 
	{
		super(adapterFactory, object, propertyValue, nestedPropertySourc);
	}

	public Object getEditableValue() {
		return getEditableValue(null);
	}
}
