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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.persistence.annotation.impl;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;
import org.eclipse.jst.ws.jaxws.utils.annotations.ILocator;
import org.eclipse.jst.ws.jaxws.utils.annotations.IParamValuePair;
import org.eclipse.jst.ws.jaxws.utils.annotations.IValue;

public class AbstractLocatorsTest extends MockObjectTestCase 
{
	protected static final int VALUE_OFFSET = 10;
	
	protected Mock<IAnnotation<IType>> annotation;
	protected IParamValuePair param1;
	protected IParamValuePair param2;
	
	@Override
	public void setUp()
	{
		annotation = mock(IAnnotation.class);
		annotation.stubs().method("getLocator").will(returnValue(createLocator(1, 11)));
		
		param1 = createParamValue("param1", 21, 31);
		param2 = createParamValue("param2", 22, 32);
		
		Set<IParamValuePair> pairs = new HashSet<IParamValuePair>();
		pairs.add(param1);
		pairs.add(param2);
		annotation.stubs().method("getParamValuePairs").will(returnValue(pairs));
	}
	
	protected IParamValuePair createParamValue(String name, int startPos, int lenght)
	{
		Mock<IParamValuePair> param = mock(IParamValuePair.class);
		param.stubs().method("getParam").will(returnValue(name));
		param.stubs().method("getLocator").will(returnValue(createLocator(startPos, lenght)));
		Mock<IValue> value = mock(IValue.class);
		value.stubs().method("getLocator").will(returnValue(createLocator(startPos + VALUE_OFFSET, lenght + VALUE_OFFSET)));
		param.stubs().method("getValue").will(returnValue(value.proxy()));		
		
		return param.proxy();
	}

	protected ILocator createLocator(int startPos, int length)
	{
		Mock<ILocator> locator = mock(ILocator.class);
		locator.stubs().method("getLength").will(returnValue(length));
		locator.stubs().method("getStartPosition").will(returnValue(startPos));
		
		return locator.proxy();
	}
}
