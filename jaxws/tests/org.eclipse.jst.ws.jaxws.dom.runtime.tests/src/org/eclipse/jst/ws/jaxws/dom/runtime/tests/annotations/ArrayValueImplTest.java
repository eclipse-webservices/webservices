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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.annotations;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.ArrayValueImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.StringValueImpl;

public class ArrayValueImplTest extends TestCase
{

	public void testHashCode()
	{
		Set<IValue> values1 = new HashSet<IValue>();
		values1.clear();
		values1.add(new StringValueImpl("val1"));
		ArrayValueImpl av1 = new ArrayValueImpl(values1);
		
		Set<IValue> values2 = new HashSet<IValue>();
		values2.clear();
		values2.add(new StringValueImpl("val2"));
		ArrayValueImpl av2 = new ArrayValueImpl(values2);

		assertTrue(av1.hashCode() != av2.hashCode());
	}

	public void testArrayValueImpl()
	{
		try
		{
			new ArrayValueImpl(null);
			fail("NullPointerException not thrown");
		} catch (NullPointerException e)
		{
			assertTrue(true);
		}
	}

	public void testEqualsObject()
	{
		Set<IValue> values1 = new HashSet<IValue>();
		values1.clear();
		values1.add(new StringValueImpl("val1"));
		ArrayValueImpl av1 = new ArrayValueImpl(values1);

		Set<IValue> values2 = new HashSet<IValue>();
		values2.clear();
		values2.add(new StringValueImpl("val2"));
		ArrayValueImpl av2 = new ArrayValueImpl(values2);

		Set<IValue> values3 = new HashSet<IValue>();
		values3.clear();
		values3.add(new StringValueImpl("val1"));
		ArrayValueImpl av3 = new ArrayValueImpl(values3);

		assertFalse(av1.equals(null));
		assertFalse(av1.equals(123));
		assertFalse(av1.equals(av2));

		assertTrue(av1.equals(av3));
	}
}
