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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.jst.ws.jaxws.testutils.jmock.testcases.BeforeAfterEnabler;
import org.eclipse.jst.ws.jaxws.testutils.jmock.testcases.IBeforeAfterEnabled;
import org.jmock.core.CoreMock;
import org.jmock.core.DynamicMock;
import org.jmock.core.Verifiable;


/**
 * An test case base class for mock objects test case. This one is roughly equivalent to org.jmock.cglib.MockObjectTestCase. There are several
 * important differences though. This test case class assumes that the loader of the test case class is able to load both the class being mocked and
 * the jmock classes. Thus test case class loader is used to create the mock type. This mock test case also provides two new <c>mock</c> methods.
 * These methods can instantiate a mock for a given class by executing the class's constructor. The methods <c></c> and <c></c> inherited from
 * <c>AbstractMockObjectTestCase</c> create mocks without executing the constructor of the mocked class. They use the SUN's reflection mechanism and
 * are able to create classes without a visible default constructor or without constructor at all. This functionallity however is platform specific.
 * It can be compiled and executed only on SUN VMs. Should the code be executed on another VM most likely a <c>NoClassDefFoundError</c> will ocurr.
 * 
 * @author Hristo Sabev
 * 
 */
public abstract class MockObjectTestCase extends AbstractMockObjectTestCase implements IBeforeAfterEnabled
{
	private final BeforeAfterEnabler bae = new BeforeAfterEnabler(this);
	public void afterTestCase() throws Exception
	{
	}

	public void beforeTestCase() throws Exception
	{
	}

	public void runBareInternal() throws Throwable
	{
		super.runBare();
	}

	@Override
	public void runBare() throws Throwable
	{
		bae.runBare();
	}
	
	private List<Mock<?>> mocksToVerify = new Vector<Mock<?>>();

	/**
	 * Constructs a new <c>MockObjectTestCase</c> instance with no name
	 * 
	 */
	public MockObjectTestCase()
	{
	}

	public List<Mock<?>> getMocksToVerify()
	{
		final List<Mock<?>> copy = new ArrayList<Mock<?>>();
		copy.addAll(mocksToVerify);
		return copy;
	}

	@Override
	public void unregisterToVerify(Verifiable arg0)
	{
		mocksToVerify.remove(arg0);
		super.unregisterToVerify(arg0);
	}

	@Override
	public void verify()
	{
		mocksToVerify.clear();
		super.verify();
	}

	/**
	 * Constructs a new <c>MockObjectTestCase</c> instance with the specified name
	 * 
	 * @param name -
	 *            the name of this test case.
	 * @see junit.framework.TestCase
	 */
	public MockObjectTestCase(String name)
	{
		super(name);
	}

	@Override
	protected <T> DynamicMock newCoreMock(Class<T> mockedType, String roleName)
	{
		return new CoreMock(mockedType, roleName);
	}
}
