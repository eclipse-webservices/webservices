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
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;

/**
 * Adapter that gives default values for {@link IWebMethod} objects
 * 
 * @author Georgi Vachkov
 */
public class MethodPropertyDefaultsAdapter extends AdapterImpl implements IPropertyDefaults
{
	@Override
	public boolean isAdapterForType(Object type) {
		return IPropertyDefaults.class == type;
	}
	
	public Object getDefault(final EStructuralFeature feature) 
	{
		final IWebMethod webMethod = (IWebMethod)getTarget(); 
		switch(feature.getFeatureID())
		{
		case DomPackage.IWEB_METHOD__NAME:
			return getMethodName(webMethod);
		}	
		
		return webMethod.eGet(feature);
	}

	private String getMethodName(final IWebMethod webMethod)
	{
		int pos = webMethod.getImplementation().indexOf('(');
		if (pos > -1) {
			return webMethod.getImplementation().substring(0, pos);
		}
		
		return webMethod.getImplementation();
	}
}
