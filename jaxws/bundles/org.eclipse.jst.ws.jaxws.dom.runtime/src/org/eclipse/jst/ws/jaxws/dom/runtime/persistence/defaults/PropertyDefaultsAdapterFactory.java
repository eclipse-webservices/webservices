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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyDefaults;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyState;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.DomAdapterFactory;

/**
 * Adapter factory for adapters implementing {@link IPropertyState} interface.
 * 
 * @author Georgi Vachkov
 */
public class PropertyDefaultsAdapterFactory extends DomAdapterFactory
{
	/**
	 * Singleton instance for this adapter factory 
	 */
	public static final PropertyDefaultsAdapterFactory INSTANCE = new PropertyDefaultsAdapterFactory();
	
	@Override
	public boolean isFactoryForType(Object object) {
		return IPropertyDefaults.class == object;
	}
	
	@Override
	public Adapter createIWebServiceAdapter() {
		return new WsPropertyDefaultsAdapter();
	}
	
	@Override
	public Adapter createIServiceEndpointInterfaceAdapter() {
		return new SeiPropertyDefaultsAdapter();
	}	
	
	@Override
	public Adapter createIWebMethodAdapter() {
		return new MethodPropertyDefaultsAdapter();
	}
	
	@Override
	public Adapter createIWebParamAdapter(){
		return new ParameterPropertyDefaultsAdapter();
	}
}
