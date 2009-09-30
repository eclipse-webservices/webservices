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
package org.eclipse.jst.ws.jaxws.utils.tests.internal.annotations;

import junit.framework.TestCase;

import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.ParamValuePairImpl;
import org.eclipse.jst.ws.jaxws.utils.internal.annotations.impl.StringValueImpl;

public class ParamValuePairImplTest extends TestCase
{

	public void testHashCode()
	{
		IValue value = new StringValueImpl("value");
		ParamValuePairImpl pvp = new ParamValuePairImpl("param", value);

		IValue value1 = new StringValueImpl("value1");
		ParamValuePairImpl pvp1 = new ParamValuePairImpl("param1", value1);

		assertFalse(pvp.hashCode() == pvp1.hashCode());
		assertFalse(pvp.equals(null));
		assertFalse(pvp.equals(123));
	}

	public void testEqualsObject()
	{
		IValue value = new StringValueImpl("value");
		ParamValuePairImpl pvp = new ParamValuePairImpl("param", value);

		IValue value1 = new StringValueImpl("value");
		ParamValuePairImpl pvp1 = new ParamValuePairImpl("param", value1);

		IValue value2 = new StringValueImpl("value2");
		ParamValuePairImpl pvp2 = new ParamValuePairImpl("param2", value2);

		assertTrue(pvp.equals(pvp1));
		assertFalse(pvp.equals(pvp2));
		assertTrue(pvp.equals(pvp));

		assertFalse(pvp.equals(null));
		assertFalse(pvp.equals(123));
	}
}
