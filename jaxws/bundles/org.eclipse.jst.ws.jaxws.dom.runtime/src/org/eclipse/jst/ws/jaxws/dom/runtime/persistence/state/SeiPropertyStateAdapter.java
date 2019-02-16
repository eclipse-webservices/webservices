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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyState;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;

/**
 * {@link IPropertyState} adapter for {@link IServiceEndpointInterface} objects.
 *  
 * @author Georgi Vachkov
 */
public class SeiPropertyStateAdapter extends AdapterImpl implements IPropertyState
{
	public boolean isChangeable(EStructuralFeature feature) 
	{
		if (!(getTarget() instanceof IServiceEndpointInterface)) {
			return false;
		}
		
		final IServiceEndpointInterface sei = (IServiceEndpointInterface)getTarget();
		if (isOutsideInWs(sei)) {
			return false;
		}
		
		switch(feature.getFeatureID())
		{
		case DomPackage.ISERVICE_ENDPOINT_INTERFACE__NAME:
			return true;
		case DomPackage.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE:
			return !sei.isImplicit();
		}
		
		return false;
	}

	protected boolean isOutsideInWs(IServiceEndpointInterface sei) 
	{		
		for (IWebService webService : sei.getImplementingWebServices()) {
			if (webService.getWsdlLocation()!=null) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isAdapterForType(Object type) {
		return IPropertyState.class == type;
	}
}
