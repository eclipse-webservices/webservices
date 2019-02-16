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
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;

/**
 * Defines the state of web method properties. In case the method is part of
 * outside-in web service all properties are immutable.
 * 
 * @author Georgi Vachkov
 */
public class MethodPropertyStateAdapter extends AdapterImpl implements IPropertyState 
{
	public boolean isChangeable(EStructuralFeature feature) 
	{
		final IWebMethod method = (IWebMethod)getTarget(); 
		if (isOutideInWs(method)) {
			return false;
		}
		
		switch(feature.getFeatureID())
		{
		case DomPackage.IWEB_METHOD__NAME:
			return true;
		}		
		
		return false;
	}
	
	private boolean isOutideInWs(final IWebMethod method)
	{
		final IServiceEndpointInterface sei = (IServiceEndpointInterface)method.eContainer();
		if (sei==null) {
			return false;
		}
		
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
