/*******************************************************************************
 * Copyright (c) 2010 by SAP AG, Walldorf.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.validation.model.IConstraintStatus;
import org.eclipse.emf.validation.service.IValidationListener;
import org.eclipse.emf.validation.service.ValidationEvent;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IConstraintStatusExtended;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IProblemLocation;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;

/**
 * A validation listener used in JUnit tests. The listener would mock the validation event and pass it to the delegate in case the validation event is relevant to the observed resource
 */
public class TestValidationListener implements IValidationListener
{
	private final IResource observedResource;
	private final IResource markedResource;
	private final IValidationListener delegate;
	private final MockObjectTestCase mockFactory;

	public TestValidationListener(final IResource observedResource, final IResource markedResource, final IValidationListener delegate, final MockObjectTestCase mockFactory)
	{
		this.observedResource = observedResource;
		this.markedResource = markedResource;
		this.delegate = delegate;
		this.mockFactory = mockFactory;
	}

	public void validationOccurred(ValidationEvent event)
	{
		final List<IConstraintStatus> mockedValidationResults = new LinkedList<IConstraintStatus>();
		for (final IConstraintStatus status : event.getValidationResults())
		{
			Assert.assertTrue("status is expected to be instance of IConstraintStatusExtended", status instanceof IConstraintStatusExtended);

			// No problems
			if (((IConstraintStatusExtended) status).getProblemLocations().isEmpty())
			{
				continue;
			}

			// Filter out validation events which are not relevant to the resource observed
			if (!observedResource.equals(((IConstraintStatusExtended) status).getProblemLocations().iterator().next().getResource()))
			{
				continue;
			}

			final Mock<IConstraintStatusExtended> mockedStatus = mockFactory.mock(IConstraintStatusExtended.class);
			mockedStatus.stubs().method("isOK").will(mockFactory.returnValue(status.isOK()));
			mockedStatus.stubs().method("getMessage").will(mockFactory.returnValue(status.getMessage()));
			mockedStatus.stubs().method("getSeverity").will(mockFactory.returnValue(status.getSeverity()));
			mockedStatus.stubs().method("getConstraint").will(mockFactory.returnValue(status.getConstraint()));
			mockedStatus.stubs().method("getTarget").will(mockFactory.returnValue(status.getTarget()));
			mockedStatus.stubs().method("getProblemLocations").will(mockFactory.returnValue(mockProblemLocations(markedResource, ((IConstraintStatusExtended) status).getProblemLocations())));
			mockedValidationResults.add(mockedStatus.proxy());
		}

		delegate.validationOccurred(mockValidationEvent(event, mockedValidationResults));
	}

	private ValidationEvent mockValidationEvent(final ValidationEvent event, final List<IConstraintStatus> mockedValidationResults)
	{
		try
		{
			// Use reflection to set the "results" member. The ValidationEvent implementation would create it as a unmodifiable collection thus preventing the test from setting the list values in the "normal way"
			final Field f = ValidationEvent.class.getDeclaredField("results");
			f.setAccessible(true);
			f.set(event, Collections.unmodifiableList(mockedValidationResults));
		} catch (SecurityException e)
		{
			throw new IllegalStateException(e);
		} catch (IllegalArgumentException e)
		{
			throw new IllegalStateException(e);
		} catch (NoSuchFieldException e)
		{
			throw new IllegalStateException(e);
		} catch (IllegalAccessException e)
		{
			throw new IllegalStateException(e);
		}

		return event;
	}

	private IProblemLocation mockProblemLocation(final IResource resource, final IProblemLocation realLocation)
	{
		final Mock<IProblemLocation> location = mockFactory.mock(IProblemLocation.class);
		location.stubs().method("getResource").will(mockFactory.returnValue(resource));
		location.stubs().method("getLocator").will(mockFactory.returnValue(realLocation.getLocator()));

		return location.proxy();
	}

	private Set<IProblemLocation> mockProblemLocations(final IResource resource, final Set<IProblemLocation> realLocations)
	{
		final Set<IProblemLocation> result = new HashSet<IProblemLocation>();
		for (final IProblemLocation l : realLocations)
		{
			result.add(mockProblemLocation(resource, l));
		}

		return result;
	}
}