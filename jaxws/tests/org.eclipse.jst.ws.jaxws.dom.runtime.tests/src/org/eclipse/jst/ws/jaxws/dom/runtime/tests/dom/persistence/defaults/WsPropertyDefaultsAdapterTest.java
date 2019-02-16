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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.defaults;

import junit.framework.TestCase;

import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyDefaults;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.defaults.PropertyDefaultsAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.defaults.WsPropertyDefaultsAdapter;

/**
 * Tests for {@link IWebService} default values calculation
 * @author Georgi Vachkov
 */
public class WsPropertyDefaultsAdapterTest extends TestCase
{
	private IWebService webService;
	private IPropertyDefaults defaults;
	
	public void setUp() {
		webService = DomFactory.eINSTANCE.createIWebService();
		webService.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "com.sap.test.MyWs");
		defaults = (IPropertyDefaults)PropertyDefaultsAdapterFactory.INSTANCE.adapt(webService, IPropertyDefaults.class);		
	}
	
	public void testNameDefaultValue()
	{
		webService.setName("Test");
		assertEquals("MyWsService", defaults.getDefault(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
	}
	
	public void testPortNameDefaultValue()
	{
		webService.setPortName("TestPortName");
		assertEquals("MyWsPort",  defaults.getDefault(DomPackage.Literals.IWEB_SERVICE__PORT_NAME));
	}
	
	public void testTargetNamespaceDefaultValue()
	{
		webService.setTargetNamespace("test");
		assertEquals("http://sap.com/test/",  defaults.getDefault(DomPackage.Literals.IWEB_SERVICE__TARGET_NAMESPACE));
	}
	
	public void testAdaptedOnce()
	{
		IPropertyDefaults adapter = (IPropertyDefaults)PropertyDefaultsAdapterFactory.INSTANCE.adapt(webService, IPropertyDefaults.class);
		assertTrue(adapter instanceof WsPropertyDefaultsAdapter);
		assertEquals(1, webService.eAdapters().size());
		assertEquals(defaults, adapter);
	}		
}