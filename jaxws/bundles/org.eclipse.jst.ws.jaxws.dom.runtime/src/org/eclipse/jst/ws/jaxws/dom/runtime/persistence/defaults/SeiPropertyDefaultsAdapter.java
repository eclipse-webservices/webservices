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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.defaults;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyDefaults;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.utils.JaxWsUtils;

/**
 * Adapter that provides default values for {@link IServiceEndpointInterface} properties 
 * 
 * @author Georgi Vachkov
 */
public class SeiPropertyDefaultsAdapter extends AdapterImpl implements IPropertyDefaults 
{
	public Object getDefault(EStructuralFeature feature) 
	{
		final IServiceEndpointInterface sei = (IServiceEndpointInterface)getTarget();
		
		switch(feature.getFeatureID())
		{
		case DomPackage.ISERVICE_ENDPOINT_INTERFACE__NAME:
			return JaxWsUtils.getDefaultPorttypeName(sei.getImplementation());
		case DomPackage.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE:
			return JaxWsUtils.composeJaxWsTargetNamespaceByFQName(sei.getImplementation());
		}			
		
		return sei.eGet(feature);
	}
	
	@Override
	public boolean isAdapterForType(Object type) {
		return IPropertyDefaults.class == type;
	}
}
