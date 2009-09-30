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
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.JaxWsDefaultsCalculator;

/**
 * Class that defines which properties for {@link IWebParam} are editable
 * 
 * @author Georgi Vachkov
 */
public class ParameterPropertyDefaultsAdapter extends AdapterImpl implements IPropertyDefaults
{
	private final JaxWsDefaultsCalculator defCalc = new JaxWsDefaultsCalculator();
	
	@Override
	public boolean isAdapterForType(Object type) {
		return IPropertyDefaults.class == type;
	}
	
	public Object getDefault(EStructuralFeature feature)
	{
		assert getTarget() instanceof IWebParam;
		
		final IWebParam webParam = (IWebParam)getTarget();
		
		switch(feature.getFeatureID()) 
		{
		case DomPackage.IWEB_PARAM__NAME:
			return defCalc().calcWebParamDefaultName(webParam);			
		case DomPackage.IWEB_PARAM__PART_NAME:
			return defCalc().calcWebParamDefaultName(webParam);			
		case DomPackage.IWEB_PARAM__TARGET_NAMESPACE:
			return defCalc().calcWebParamDefaultTargetNS((IWebMethod)webParam.eContainer(), webParam);
		case DomPackage.IWEB_PARAM__HEADER:
			return defCalc().getDefaultParamInHedaer();
		case DomPackage.IWEB_PARAM__KIND:
			return defCalc().getDefaultParamKind();
		}
		
		return webParam.eGet(feature);
	}
	
	protected JaxWsDefaultsCalculator defCalc() {
		return defCalc;
	}
}
