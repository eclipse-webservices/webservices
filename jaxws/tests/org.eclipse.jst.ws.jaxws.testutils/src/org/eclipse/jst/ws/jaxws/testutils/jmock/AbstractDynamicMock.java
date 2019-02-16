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
package org.eclipse.jst.ws.jaxws.testutils.jmock;

import org.jmock.core.InvocationDispatcher;

/**
 * This class is equivalent to <c>org.jmock.core.AbstractDynamicMock</c>. The difference is that this class is parameterized. It extends from
 * <c>org.jmock.core.AbstractDynamicMock</c> and is thus backward compatible.
 * 
 * @author Hristo Sabev
 * 
 * @param <T>
 */
public abstract class AbstractDynamicMock<T> extends org.jmock.core.AbstractDynamicMock
{

	public AbstractDynamicMock(Class<T> mockedType, String name, InvocationDispatcher invocationDispatcher)
	{
		super(mockedType, name, invocationDispatcher);
	}

	public AbstractDynamicMock(Class<T> mockedType, String name)
	{
		super(mockedType, name);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<T> getMockedType()
	{
		return super.getMockedType();
	}

	@Override
	public abstract T proxy();
}
