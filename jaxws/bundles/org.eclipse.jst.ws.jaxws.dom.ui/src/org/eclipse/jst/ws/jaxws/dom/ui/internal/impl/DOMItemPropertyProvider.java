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

import java.beans.PropertyDescriptor;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyDefaults;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyState;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.defaults.PropertyDefaultsAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state.PropertyStateAdapterFactory;

/**
 * Extends {@link PropertyDescriptor} to represent property descriptor for DOM objects. 
 * This class overrides createPropertyValueWrapper form {@link PropertyDescriptor} to provide
 * custom property wrapper class.
 * 
 * @author Georgi Vachkov
 */
public class DOMItemPropertyProvider extends ItemPropertyDescriptor 
{
	public DOMItemPropertyProvider(AdapterFactory adapterFactory,
				ResourceLocator resourceLocator, 
				String displayName,
				String description, 
				EStructuralFeature feature, 
				boolean isSettable,
				boolean multiLine, 
				boolean sortChoices, 
				Object staticImage,
				String category, 
				String[] filterFlags) 
	{
		super(adapterFactory, resourceLocator, displayName, description, feature,
				isSettable, multiLine, sortChoices, staticImage, category, filterFlags);
	}

	@Override
	protected Object createPropertyValueWrapper(Object object, Object propertyValue) 
	{
		return new DOMPropertyValue(adapterFactory, object, propertyValue, null);
	}
	
	@Override
	public boolean canSetProperty(Object object)
	{
		final IPropertyState state = (IPropertyState)PropertyStateAdapterFactory.INSTANCE.adapt(object, IPropertyState.class);
		if (state != null) {
			super.isSettable = state.isChangeable(feature);
		}
		
		return super.canSetProperty(object);
	}

	@Override
	public void resetPropertyValue(Object object)
	{
		final IPropertyDefaults defaults = (IPropertyDefaults)PropertyDefaultsAdapterFactory.INSTANCE.adapt(object, IPropertyDefaults.class);
		if (defaults != null) {
			final Object defaultValue = defaults.getDefault(feature);
			((EObject)object).eSet(feature, defaultValue);
		} 
		else {
			super.resetPropertyValue(object);
		}
	}
}
