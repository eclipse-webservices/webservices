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
package org.eclipse.jst.ws.jaxws.testutils.jmock;

import org.jmock.core.DynamicMock;

/**
 * This class is equivalent to <c>org.jmock.Mock</c> class from JMock. The only difference is that this a parameterized (generic) version. This makes
 * it a lot easier for use as no manual casts to the mocked type are required by the programmer. This class extends from <c>org.jmock.Mock</c> so its
 * fully backward compatible.
 * 
 * @see org.jmock.Mock
 */
public class Mock<T> extends org.jmock.Mock
{

	/**
	 * Constructs a new mock for the specified type. The new mock has the specified name. This name will be shown by all reports concerning this mock
	 * instance. Such reports are unmached invocations and so on.
	 * 
	 * @param mockedType -
	 *            the type to be mocked. This could be either class or interface.
	 * @param name -
	 *            name of this mock
	 */
	public Mock(Class<T> mockedType, String name)
	{
		super(mockedType, name);
	}

	/**
	 * Constructs a new mock for the specified type.
	 * 
	 * @param mockedType -
	 *            the type to be mocked. This class needs to be either class or interface.
	 */
	public Mock(Class<T> mockedType)
	{
		super(mockedType);
	}

	/**
	 * Constructs new Mock that wraps the supplied <c>DynamicMock</c> instance.
	 * 
	 * @param arg0 -
	 *            the dynamic mock instance
	 */
	public Mock(DynamicMock arg0)
	{
		super(arg0);
	}

	/**
	 * Obtains the proxy instance for this mock.
	 * 
	 * @return the proxy instance for this mock.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public T proxy()
	{
		return (T) super.proxy();
	}
}
