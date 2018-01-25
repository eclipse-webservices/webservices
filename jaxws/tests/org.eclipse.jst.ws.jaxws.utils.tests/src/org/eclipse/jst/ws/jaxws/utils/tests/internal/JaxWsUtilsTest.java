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
package org.eclipse.jst.ws.jaxws.utils.tests.internal;

import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.utils.JaxWsUtils;

public class JaxWsUtilsTest extends MockObjectTestCase
{
	private static final String CLASS_NAME = "TestBean";
	private static final String CLASS_FQ_NAME = "org.eclipse.test." + CLASS_NAME;
	
	
	public void testComposeJaxWsTargetNamespaceByPackage_WithNull()
	{
		try
		{
			JaxWsUtils.composeJaxWsTargetNamespaceByPackage(null);
			fail("NPE expected");
		} catch (Exception e)
		{
			// expected
			assertTrue(true);
		}
	}

	public void testComposeJaxWsTargetNamespaceByPackage()
	{
		String ns = JaxWsUtils.composeJaxWsTargetNamespaceByPackage("");
		assertTrue(ns.equals("http:///"));
		ns = JaxWsUtils.composeJaxWsTargetNamespaceByPackage("a");
		assertTrue(ns.equals("http://a/"));
		ns = JaxWsUtils.composeJaxWsTargetNamespaceByPackage("a.b.c");
		assertTrue(ns.equals("http://b.a/c/"));
		ns = JaxWsUtils.composeJaxWsTargetNamespaceByPackage("a.b.c.d");
		assertTrue(ns.equals("http://b.a/c/d/"));
		ns = JaxWsUtils.composeJaxWsTargetNamespaceByPackage("a.b.c.d.e");
		assertTrue(ns.equals("http://b.a/c/d/e/"));
	}
	
	public void testGetDefaultServiceName()
	{
		try {
			JaxWsUtils.getDefaultServiceName(null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException _){}
		
		assertEquals(CLASS_NAME + "Service",  JaxWsUtils.getDefaultServiceName(CLASS_FQ_NAME));
	}
	
	public void testGetDefaultPorttypeName()
	{
		try {
			JaxWsUtils.getDefaultPorttypeName(null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException _){}
		
		assertEquals(CLASS_NAME,  JaxWsUtils.getDefaultPorttypeName(CLASS_FQ_NAME));
	}
	
	public void testGetDefaultPortName()
	{
		try {
			JaxWsUtils.getDefaultPortName(null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException _){}
		
		assertEquals(CLASS_NAME + "Port",  JaxWsUtils.getDefaultPortName(CLASS_FQ_NAME));
	}
	
}