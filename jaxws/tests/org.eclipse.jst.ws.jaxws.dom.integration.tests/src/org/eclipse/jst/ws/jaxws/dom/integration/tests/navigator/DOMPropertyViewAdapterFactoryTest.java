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
package org.eclipse.jst.ws.jaxws.dom.integration.tests.navigator;

import junit.framework.TestCase;

import org.eclipse.jst.ws.jaxws.dom.integration.navigator.DOMAdapterFactoryContentProvider;
import org.eclipse.jst.ws.jaxws.dom.integration.navigator.DOMPropertyViewAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.ui.views.properties.IPropertySourceProvider;

public class DOMPropertyViewAdapterFactoryTest extends TestCase 
{
	public void testGetAdapter() 
	{
		DOMPropertyViewAdapterFactory propertyAdapterFactory = new DOMPropertyViewAdapterFactory();
		
		assertNotNull(propertyAdapterFactory.getAdapter(propertyAdapterFactory, IPropertySourceProvider.class));
		assertNull(propertyAdapterFactory.getAdapter(propertyAdapterFactory, IWebService.class));
		assertTrue(propertyAdapterFactory.getAdapter(propertyAdapterFactory, IPropertySourceProvider.class) instanceof DOMAdapterFactoryContentProvider);
		assertTrue(propertyAdapterFactory.getAdapter(propertyAdapterFactory, DOMPropertyViewAdapterFactory.class) instanceof DOMPropertyViewAdapterFactory);
	}

	public void testGetAdapterList() 
	{
		DOMPropertyViewAdapterFactory propertyAdapterFactory = new DOMPropertyViewAdapterFactory();
		
		assertNotNull(propertyAdapterFactory.getAdapterList());
		assertEquals(propertyAdapterFactory.getAdapterList().length, 1);
		assertEquals(propertyAdapterFactory.getAdapterList()[0], DOMAdapterFactoryContentProvider.class);
	}
	
	public void testGetAdapterUsesSameFactory()
	{
		DOMPropertyViewAdapterFactory propertyAdapterFactory = new DOMPropertyViewAdapterFactory();
		Object adapter1 = propertyAdapterFactory.getAdapter(propertyAdapterFactory, IPropertySourceProvider.class);
		assertNotNull(adapter1);
		Object adapter2 = propertyAdapterFactory.getAdapter(propertyAdapterFactory, IPropertySourceProvider.class);
		assertTrue(adapter1==adapter2);
	}

}
