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
import org.eclipse.jst.ws.jaxws.dom.runtime.DomUtil;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyState;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Jee5DomUtils;

/**
 * Class that defines which properties for {@link IWebParam} are editable
 * 
 * @author Georgi Vachkov
 */
public class ParameterPropertyStateAdapter extends AdapterImpl implements IPropertyState
{
	public boolean isChangeable(EStructuralFeature feature) 	
	{
		assert getTarget() instanceof IWebParam;
		
		final IWebParam webParam = (IWebParam)getTarget();
		if (webParam.getImplementation().equals("return")) {//$NON-NLS-1$
			return false;
		}
		
		if (isOutsideInWebService(webParam)) {
			return false;
		}
		
		switch(feature.getFeatureID()) 
		{
		case DomPackage.IWEB_PARAM__NAME:
			return isNameChangeable(webParam);
		case DomPackage.IWEB_PARAM__PART_NAME:
			return isPartNameChangeable(webParam);			
		case DomPackage.IWEB_PARAM__TARGET_NAMESPACE:
			return isTargetNSChangeable(webParam);
		}
		
		return false;
	}
	
	protected boolean isPartNameChangeable(final IWebParam webParam)
	{
		final IWebMethod webMethod = (IWebMethod)webParam.eContainer();		
		
		return (webMethod.getSoapBindingStyle()==SOAPBindingStyle.RPC &&
				webMethod.getSoapBindingUse()==SOAPBindingUse.LITERAL &&
				webMethod.getSoapBindingParameterStyle()==SOAPBindingParameterStyle.WRAPPED);
	}
	
	protected boolean isNameChangeable(final IWebParam webParam)
	{
		final IWebMethod webMethod = (IWebMethod)webParam.eContainer();		
		if (isDocumentLiteralWrapped(webMethod)) {
			return true;
		}
		
		if (Jee5DomUtils.getInstance().isNameRequired(webParam)) {
			return true;
		}
		
		return webParam.isHeader();
	}
	
	protected boolean isTargetNSChangeable(final IWebParam webParam)
	{
		final IWebMethod webMethod = (IWebMethod)webParam.eContainer();		
		if (isDocumentLiteralWrapped(webMethod)) {
			return true;
		}
		
		return webParam.isHeader();
	}
	
	protected boolean isDocumentLiteralWrapped(final IWebMethod webMethod)
	{
		return !(webMethod.getSoapBindingStyle()==SOAPBindingStyle.RPC ||
				webMethod.getSoapBindingUse()==SOAPBindingUse.ENCODED ||
				webMethod.getSoapBindingParameterStyle()==SOAPBindingParameterStyle.BARE);
	}
	
	protected boolean isOutsideInWebService(final IWebParam webParam)
	{
		final IServiceEndpointInterface sei = (IServiceEndpointInterface)webParam.eContainer().eContainer();		
		return DomUtil.INSTANCE.isOutsideInWebService(sei);
	}
	
	@Override
	public boolean isAdapterForType(Object type) {
		return IPropertyState.class == type;
	}	
}
