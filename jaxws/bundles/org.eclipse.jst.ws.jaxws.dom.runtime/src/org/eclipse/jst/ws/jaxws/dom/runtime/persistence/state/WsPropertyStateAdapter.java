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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyState;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;

/**
 * {@link IPropertyState} adapter for {@link IWebService} objects.
 * 
 * @author Georgi Vachkov
 */
public class WsPropertyStateAdapter extends AdapterImpl implements IPropertyState
{
	public boolean isChangeable(final EStructuralFeature feature) 
	{
		if (!(getTarget() instanceof IWebService)) {
			return false;
		}

		if (isOutsideInWs((IWebService)getTarget())) {
			return false;
		}
		
		switch(feature.getFeatureID())
		{
		case DomPackage.IWEB_SERVICE__NAME: 
			return true;
		case DomPackage.IWEB_SERVICE__PORT_NAME:
			return true;
		case DomPackage.IWEB_SERVICE__TARGET_NAMESPACE:
			return true;
		}
		
		return false;
	}
	
	protected boolean isOutsideInWs(final IWebService ws) 
	{
		return ws.getWsdlLocation() != null;
	}
	
	@Override
	public boolean isAdapterForType(Object type) {
		return IPropertyState.class == type;
	}
}
