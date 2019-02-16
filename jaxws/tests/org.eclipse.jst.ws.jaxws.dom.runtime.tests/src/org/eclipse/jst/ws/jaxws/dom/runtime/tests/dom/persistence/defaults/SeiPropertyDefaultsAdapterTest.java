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
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.defaults.PropertyDefaultsAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.defaults.SeiPropertyDefaultsAdapter;

/**
 * Tests for {@link IServiceEndpointInterface} default properties values 
 * @author Georgi Vachkov
 */
public class SeiPropertyDefaultsAdapterTest extends TestCase 
{
	private IServiceEndpointInterface sei;
	private IPropertyDefaults defaults;
	
	public void setUp()
	{
		sei = DomFactory.eINSTANCE.createIServiceEndpointInterface();
		sei.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "com.sap.test.Sei");
		defaults = (IPropertyDefaults)PropertyDefaultsAdapterFactory.INSTANCE.adapt(sei, IPropertyDefaults.class);		
	}
	
	public void testNameDefaultValue()
	{
		sei.setName("Test");
		assertEquals("Sei", defaults.getDefault(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));	
	}
	
	public void testTargetNamespaceDefaultValue()
	{
		sei.setTargetNamespace("test");
		assertEquals("http://sap.com/test/",  defaults.getDefault(DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE));
	}
	
	public void testAdaptedOnce()
	{
		IPropertyDefaults adapter = (IPropertyDefaults)PropertyDefaultsAdapterFactory.INSTANCE.adapt(sei, IPropertyDefaults.class);
		assertTrue(adapter instanceof SeiPropertyDefaultsAdapter);
		assertEquals(1, sei.eAdapters().size());
		assertEquals(defaults, adapter);
	}	
}
