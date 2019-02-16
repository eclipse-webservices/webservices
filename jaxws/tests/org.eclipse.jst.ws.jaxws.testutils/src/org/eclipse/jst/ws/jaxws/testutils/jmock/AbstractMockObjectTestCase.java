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

import java.util.Collection;

import org.jmock.core.Constraint;
import org.jmock.core.DynamicMock;
import org.jmock.core.Formatting;
import org.jmock.core.InvocationMatcher;
import org.jmock.core.MockObjectSupportTestCase;
import org.jmock.core.Stub;
import org.jmock.core.matcher.InvokeAtLeastOnceMatcher;
import org.jmock.core.matcher.InvokeAtMostOnceMatcher;
import org.jmock.core.matcher.InvokeCountMatcher;
import org.jmock.core.matcher.InvokeOnceMatcher;
import org.jmock.core.matcher.TestFailureMatcher;
import org.jmock.core.stub.ReturnIteratorStub;
import org.jmock.core.stub.ReturnStub;
import org.jmock.core.stub.ThrowStub;

/**
 * An abstract class from which sub-classing test cases can inherit. This class is equivalent to org.jmock.MockObjectTestCase with the difference that
 * id does not provide an implementation to <c>newCoreMock</c> method and thus is not able to create any proxies. The other major difference is that
 * this class is parameterized. It should be used as a base class for sub-classes that provide strongly typed mocks. This class also take advantage of
 * some of the java 5 sugars. For example "varargs".
 * 
 * @author Hristo Sabev
 * 
 */
public abstract class AbstractMockObjectTestCase extends MockObjectSupportTestCase
{

	public AbstractMockObjectTestCase()
	{
	}

	public AbstractMockObjectTestCase(String name)
	{
		super(name);
	}

	/**
	 * Creates a mock object that mocks the given type. The mock object is named after the type; the exact name is calculated by
	 * {@link #defaultMockNameForType}.
	 * 
	 * @param mockedType
	 *            The type to be mocked.
	 * @return A {@link Mock} object that mocks <var>mockedType</var>.
	 */
	@SuppressWarnings("unchecked")
	public <T> Mock<T> mock(Class mockedType)
	{
		return mock(mockedType, defaultMockNameForType(mockedType));
	}

	/**
	 * Creates a mock object that mocks the given type and is explicitly given a name. The mock object is named after the type; the exact name is
	 * calculated by {@link #defaultMockNameForType}.
	 * 
	 * @param mockedType
	 *            The type to be mocked.
	 * @param roleName
	 *            The name of the mock object
	 * @return A {@link Mock} object that mocks <var>mockedType</var>.
	 */
	@SuppressWarnings("unchecked")
	public <T> Mock<T> mock(Class mockedType, String roleName)
	{
		final Mock<T> newMock = new Mock<T>(newCoreMock(mockedType, roleName));
		registerToVerify(newMock);
		return newMock;
	}

	protected abstract <T> DynamicMock newCoreMock(Class<T> mockedType, String roleName);

	/**
	 * Calculates
	 * 
	 * @param mockedType
	 * @return
	 */
	public String defaultMockNameForType(Class<?> mockedType)
	{
		return "mock" + Formatting.classShortName(mockedType);
	}

	public Stub returnValue(Object o)
	{
		return new ReturnStub(o);
	}

	public Stub returnValue(boolean result)
	{
		return returnValue(new Boolean(result));
	}

	public Stub returnValue(byte result)
	{
		return returnValue(new Byte(result));
	}

	public Stub returnValue(char result)
	{
		return returnValue(new Character(result));
	}

	public Stub returnValue(short result)
	{
		return returnValue(new Short(result));
	}

	public Stub returnValue(int result)
	{
		return returnValue(new Integer(result));
	}

	public Stub returnValue(long result)
	{
		return returnValue(new Long(result));
	}

	public Stub returnValue(float result)
	{
		return returnValue(new Float(result));
	}

	public Stub returnValue(double result)
	{
		return returnValue(new Double(result));
	}

	public Stub returnIterator(Collection<?> collection)
	{
		return new ReturnIteratorStub(collection);
	}

	public Stub returnIterator(Object[] array)
	{
		return new ReturnIteratorStub(array);
	}

	public Stub throwException(Throwable throwable)
	{
		return new ThrowStub(throwable);
	}

	public InvocationMatcher once()
	{
		return new InvokeOnceMatcher();
	}

	public InvocationMatcher atLeastOnce()
	{
		return new InvokeAtLeastOnceMatcher();
	}

	public InvocationMatcher atMostOnce()
	{
		return new InvokeAtMostOnceMatcher();
	}

	public InvocationMatcher exactly(int expectedCount)
	{
		return new InvokeCountMatcher(expectedCount);
	}

	public InvocationMatcher never()
	{
		return new TestFailureMatcher("not expected");
	}

	public InvocationMatcher never(String errorMessage)
	{
		return new TestFailureMatcher("not expected (" + errorMessage + ")");
	}

	/**
	 * @since 1.0.1
	 */
	public Stub onConsecutiveCalls(Stub... stubs)
	{
		return onConsecutiveCalls(stubs);
	}

	/**
	 * @since 1.1.0
	 */
	public Stub doAll(Stub... stubs)
	{
		return doAll(stubs);
	}
	
	public Constraint collectionWithElements(final int expectedCollectionSize, final Object... objects)
	{
		return new Constraint(){

			public boolean eval(Object arg0)
			{
				final Collection<?> col = (Collection<?>)arg0;
				for(Object o : objects)
				{
					if(!col.contains(o))
					{
						return false;
					}
				}
				
				return objects.length == expectedCollectionSize;
			}

			public StringBuffer describeTo(StringBuffer arg0)
			{
				return arg0;
			}};
	}
}
