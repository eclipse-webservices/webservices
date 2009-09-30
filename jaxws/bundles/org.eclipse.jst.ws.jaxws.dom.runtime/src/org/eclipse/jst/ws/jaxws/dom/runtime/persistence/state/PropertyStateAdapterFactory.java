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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyState;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.DomAdapterFactory;

/**
 * Adapter factory for adapters implementing {@link IPropertyState} interface.
 * 
 * @author Georgi Vachkov
 */
public class PropertyStateAdapterFactory extends DomAdapterFactory
{
	/**
	 * Singleton instance for this adapter factory 
	 */
	public static final PropertyStateAdapterFactory INSTANCE = new PropertyStateAdapterFactory();
	
	@Override
	public boolean isFactoryForType(Object object) {
		return IPropertyState.class == object;
	}
	
	@Override
	public Adapter createIWebServiceAdapter() {
		return new WsPropertyStateAdapter();
	}
	
	@Override
	public Adapter createIServiceEndpointInterfaceAdapter() {
		return new SeiPropertyStateAdapter();
	}	
	
	@Override
	public Adapter createIWebMethodAdapter() {
		return new MethodPropertyStateAdapter();
	}
	
	@Override
	public Adapter createIWebParamAdapter(){
		return new ParameterPropertyStateAdapter();
	}
}
