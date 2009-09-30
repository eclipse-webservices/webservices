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
package org.eclipse.jst.ws.jaxws.dom.ui.tests.internal;

import junit.framework.TestCase;

import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.ui.internal.impl.DOMItemPropertyProvider;

/**
 * Tests for {@link DOMItemPropertyProvider} class
 * @author Georgi Vachkov
 */
public class DOMItemPropertyProviderTest extends TestCase 
{
	private DOMItemPropertyProvider provider;
	
	public void setUp()
	{
		provider = new DOMItemPropertyProvider(
				null,
				null,
				"display name",
				"description",
				DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__NAME,
				true,
				false,
				false,
				null, 
				"category",
				new String[0]);
	}
	
	public void testResetPropertyValue()
	{
		final IWebService ws = DomFactory.eINSTANCE.createIWebService();
		ws.eSet(DomPackage.Literals.IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION, "org.eclipse.test.Sei");
		ws.setName("Test");
		provider.resetPropertyValue(ws);
		assertEquals("SeiService", ws.getName());
	}
}
