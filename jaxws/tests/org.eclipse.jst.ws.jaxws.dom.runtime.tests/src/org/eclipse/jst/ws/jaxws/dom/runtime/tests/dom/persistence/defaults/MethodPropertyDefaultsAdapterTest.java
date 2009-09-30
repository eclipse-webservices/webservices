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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.defaults;

import junit.framework.TestCase;

import org.eclipse.jst.ws.jaxws.dom.runtime.IPropertyDefaults;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.defaults.MethodPropertyDefaultsAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.defaults.PropertyDefaultsAdapterFactory;

/**
 * Tests for method default values adapter
 * 
 * @author Georgi Vachkov
 */
public class MethodPropertyDefaultsAdapterTest extends TestCase 
{
	private IWebMethod webMethod;
	private IPropertyDefaults defaults;
	
	public void setUp() 
	{
		webMethod = DomFactory.eINSTANCE.createIWebMethod();
		webMethod.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "myMethod()");
		defaults = (IPropertyDefaults)PropertyDefaultsAdapterFactory.INSTANCE.adapt(webMethod, IPropertyDefaults.class);
	}
	
	public void testMethodName() 
	{
		assertEquals("myMethod", defaults.getDefault(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME));
	}
	
	public void testAdaptedOnce()
	{
		IPropertyDefaults adapter = (IPropertyDefaults)PropertyDefaultsAdapterFactory.INSTANCE.adapt(webMethod, IPropertyDefaults.class);
		assertTrue(adapter instanceof MethodPropertyDefaultsAdapter);
		assertEquals(1, webMethod.eAdapters().size());
		assertEquals(defaults, adapter);
	}
}
