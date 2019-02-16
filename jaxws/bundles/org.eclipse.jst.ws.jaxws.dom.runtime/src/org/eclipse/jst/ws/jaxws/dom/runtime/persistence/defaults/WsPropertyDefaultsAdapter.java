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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.defaults;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyDefaults;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.utils.JaxWsUtils;

public class WsPropertyDefaultsAdapter extends AdapterImpl implements IPropertyDefaults 
{
	public Object getDefault(EStructuralFeature feature) 
	{
		final IWebService ws = (IWebService)getTarget();
		
		switch(feature.getFeatureID())
		{
		case DomPackage.IWEB_SERVICE__NAME: 
			return JaxWsUtils.getDefaultServiceName(ws.getImplementation());
		case DomPackage.IWEB_SERVICE__PORT_NAME:
			return JaxWsUtils.getDefaultPortName(ws.getImplementation());
		case DomPackage.IWEB_SERVICE__TARGET_NAMESPACE:
			return JaxWsUtils.composeJaxWsTargetNamespaceByFQName(ws.getImplementation());
		}
		
		return ws.eGet(feature);
	}
	
	@Override
	public boolean isAdapterForType(Object type) {
		return IPropertyDefaults.class == type;
	}
}
