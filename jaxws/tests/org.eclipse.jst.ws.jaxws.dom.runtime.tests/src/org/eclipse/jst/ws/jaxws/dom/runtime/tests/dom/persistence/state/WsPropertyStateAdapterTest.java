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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.state;

import junit.framework.TestCase;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyState;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state.PropertyStateAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state.WsPropertyStateAdapter;

/**
 * Tests for {@link WsPropertyStateAdapter} class.
 * 
 * @author Georgi Vachkov
 */
public class WsPropertyStateAdapterTest extends TestCase
{
	private IWebService ws;
	
	public void setUp()	{
		ws = DomFactory.eINSTANCE.createIWebService();
	}
	
	public void testIsAdapted() 
	{
		IPropertyState adapter = (IPropertyState)PropertyStateAdapterFactory.INSTANCE.adapt(ws, IPropertyState.class);
		assertNotNull(adapter);		
	}
	
	public void testIsChangebleOutsideInWs() 
	{
		ws.setWsdlLocation("C:/test");
		IPropertyState adapter = (IPropertyState)PropertyStateAdapterFactory.INSTANCE.adapt(ws, IPropertyState.class);

		assertFalse(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_SERVICE__PORT_NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_SERVICE__SERVICE_ENDPOINT));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_SERVICE__TARGET_NAMESPACE));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_SERVICE__WSDL_LOCATION));
	}
	
	public void testIsChangableInOut()
	{
		IPropertyState adapter = (IPropertyState)PropertyStateAdapterFactory.INSTANCE.adapt(ws, IPropertyState.class);

		assertTrue(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
		assertTrue(adapter.isChangeable(DomPackage.Literals.IWEB_SERVICE__PORT_NAME));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_SERVICE__SERVICE_ENDPOINT));
		assertTrue(adapter.isChangeable(DomPackage.Literals.IWEB_SERVICE__TARGET_NAMESPACE));
		assertFalse(adapter.isChangeable(DomPackage.Literals.IWEB_SERVICE__WSDL_LOCATION));
	}
	
	public void testIsChangebleNonWsInstance()
	{
		IServiceEndpointInterface sei = DomFactory.eINSTANCE.createIServiceEndpointInterface();
		IPropertyState adapter = (IPropertyState)PropertyStateAdapterFactory.INSTANCE.adapt(ws, IPropertyState.class);
		((Adapter)adapter).setTarget(sei);

		assertFalse(adapter.isChangeable(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));		
	}
}
