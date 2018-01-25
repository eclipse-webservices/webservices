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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.state;

import junit.framework.TestCase;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyState;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state.PropertyStateAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state.SeiPropertyStateAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.state.WsPropertyStateAdapter;

/**
 * Tests for {@link PropertyStateAdapterFactory} class
 * 
 * @author Georgi Vachkov
 */
public class PropertyStateAdapterFactoryTest extends TestCase 
{
	@Override
	public void setUp()
	{
	}

	public void testIsFactoryForType()	
	{		
		assertTrue(PropertyStateAdapterFactory.INSTANCE.isFactoryForType(IPropertyState.class));
		assertFalse(PropertyStateAdapterFactory.INSTANCE.isFactoryForType(Notification.class));
	}

	public void testAdaptWebService()
	{
		IWebService webService = DomFactory.eINSTANCE.createIWebService();
		Adapter adapter1 = PropertyStateAdapterFactory.INSTANCE.adapt(webService, IPropertyState.class);
		assertEquals(1, webService.eAdapters().size());
		assertTrue(adapter1 instanceof WsPropertyStateAdapter);
		
		Adapter adapter2 = PropertyStateAdapterFactory.INSTANCE.adapt(webService, IPropertyState.class);
		assertEquals(1, webService.eAdapters().size());
		
		assertTrue(adapter1==adapter2);
	}
	
	public void testAdaptSei()
	{
		IServiceEndpointInterface sei = DomFactory.eINSTANCE.createIServiceEndpointInterface();
		Adapter adapter1 = PropertyStateAdapterFactory.INSTANCE.adapt(sei, IPropertyState.class);
		assertEquals(1, sei.eAdapters().size());
		assertTrue(adapter1 instanceof SeiPropertyStateAdapter);
		
		Adapter adapter2 = PropertyStateAdapterFactory.INSTANCE.adapt(sei, IPropertyState.class);
		assertEquals(1, sei.eAdapters().size());
		
		assertTrue(adapter1==adapter2);
	}
}
