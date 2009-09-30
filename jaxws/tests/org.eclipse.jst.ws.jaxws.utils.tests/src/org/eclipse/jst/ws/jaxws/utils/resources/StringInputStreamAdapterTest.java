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
package org.eclipse.jst.ws.jaxws.utils.resources;

import java.io.IOException;

import org.eclipse.jst.ws.jaxws.utils.resources.StringInputStreamAdapter;

import junit.framework.TestCase;

/**
 * Test case for StringInputStreamAdapter class
 * 
 * @author Danail Branekov
 * 
 */
public class StringInputStreamAdapterTest extends TestCase
{
	public void testCreation()
	{
		try
		{
			new StringInputStreamAdapter(null);
			fail("NPE expected");
		} catch (NullPointerException e)
		{
			// expected
			assertTrue(true);
		}

		StringInputStreamAdapter adapter = new StringInputStreamAdapter("ABC");
		assertNotNull(adapter);
	}

	public void testRead()
	{
		try
		{
			StringInputStreamAdapter adapter = new StringInputStreamAdapter("ABC");
			assertTrue(adapter.read() > 0);

			adapter = new StringInputStreamAdapter("");
			assertTrue(adapter.read() == -1);
		} catch (IOException e)
		{
			fail(e.getMessage());
		}
	}
}
