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

import java.util.ArrayList;
import java.util.Collection;

import org.jmock.core.Stub;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.StubSequence;

/**
 * Utils for creating more complex jmock stubs
 * 
 * @author Hristo Sabev
 * 
 */
public class StubUtils
{

	/**
	 * Creates a stub that will return the proxy for the next mock in the supplied array on a consequtive call. I.e. the first call returns the proxy
	 * at <c>returnValues[0]</c>, the second call will return the proxy for the mock at <c>returnValues[1]</c>, and so forth
	 * 
	 * @param returnValues -
	 *            the mocks which will be returned on consequtive calls
	 * @return - a stub that will return the proxies for the specified mocks on each consequtive call
	 */
	public static Stub onConsequtiveCallsWillReturnMocks(Mock<?>[] returnValues)
	{
		final Stub[] returnStubs = new Stub[returnValues.length];
		for (int i = 0; i < returnValues.length; i++)
		{
			returnStubs[i] = new ReturnStub(returnValues[i].proxy());
		}
		return new StubSequence(returnStubs);
	}
	
	/**
	 * Converts an array of mocks to an array of proxies for these mocks. All mocks should be mocks for one and the
	 * same type.
	 * @param <T> - the type of the mocked class
	 * @param mocks - the array of mocks to be converted to proxies
	 * @param proxies - an array of the type of the proxy. It will be used to store the proxies for the mocks
	 * @throws IndexOutOfBoundsException - thrown if the arrays are with different sizes.
	 */
	public static <T> void mocksToProxies(Mock<T>[] mocks, T[] proxies) {
		for (int i = 0; i < mocks.length; i++)
		{
			proxies[i] = mocks[i].proxy();
		}
	}
	
	/**
	 * Creates a return stub that will return an array of mock proxies for the supplied array of mocks.
	 * @param returnValues - an array of mocks, whose proxies has to be returned
	 * @return - a stub wich will return an array of proxies in the same order as the passed mocks.
	 * @throws IndexOutOfBoundsException - if the two arrays are with different lengths
	 */
	public static <T>Stub returnArrayOfMocks(Mock<T>[] returnValues, T[] proxies)
	{
		mocksToProxies(returnValues, proxies);
		return new ReturnStub(proxies);
	}

	/**
	 * Creates a stub that on consequtive calls will return the next boolean in the supplied array I.e. the first call returns the boolean at
	 * <c>returnValues[0]</c>, the second call will return the boolean at <c>returnValues[1]</c>, and so forth
	 * 
	 * @param returnValues -
	 *            the booleans which will be returned on consequtive calls
	 * @return - a stub that will return the booleans for each consequtive call
	 */
	public static Stub onConsecutiveCallsWillReturnBoolean(boolean[] returnValues)
	{
		final Stub[] returnStubs = new Stub[returnValues.length];
		for (int i = 0; i < returnValues.length; i++)
		{
			returnStubs[i] = new ReturnStub(returnValues[i]);
		}
		return new StubSequence(returnStubs);
	}

	public static <T> Stub returnCollectionContaining(T[] contained) {
		final Collection<T> coll = new ArrayList<T>();
		for(T o : contained) {
			coll.add(o);
		}
		return new ReturnStub(coll);
	}
	
	public static <T> Stub returnCollectionContaining(T contained) {
		final Collection<T> coll = new ArrayList<T>(1);
		coll.add(contained);
		return new ReturnStub(coll);
	}
}
