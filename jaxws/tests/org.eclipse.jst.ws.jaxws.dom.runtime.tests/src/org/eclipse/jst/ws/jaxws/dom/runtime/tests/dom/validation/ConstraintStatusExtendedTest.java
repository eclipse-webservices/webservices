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
package org.eclipse.jst.ws.jaxws.dom.runtime.tests.dom.validation;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.model.ConstraintSeverity;
import org.eclipse.emf.validation.model.IModelConstraint;
import org.eclipse.emf.validation.service.IConstraintDescriptor;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IProblemLocation;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.impl.ConstraintStatusExtended;
import org.eclipse.jst.ws.jaxws.testutils.jmock.Mock;
import org.eclipse.jst.ws.jaxws.testutils.jmock.MockObjectTestCase;

public class ConstraintStatusExtendedTest extends MockObjectTestCase
{
	private Mock<IProblemLocation> locationMock; 
	private Mock<IModelConstraint> modelConstraint;
	private Mock<EObject> targetObject;
	
	@Override
	protected void setUp() throws Exception
	{
		locationMock = mock(IProblemLocation.class);
		modelConstraint = mock(IModelConstraint.class);
		targetObject = mock(EObject.class);
		
		final Mock<IConstraintDescriptor> constraintDescr = mock(IConstraintDescriptor.class);
		constraintDescr.stubs().method("getSeverity").will(returnValue(ConstraintSeverity.ERROR));
		constraintDescr.stubs().method("getStatusCode").will(returnValue(0));
		constraintDescr.stubs().method("getPluginId").will(returnValue("mytestpluginid"));
		modelConstraint.stubs().method("getDescriptor").will(returnValue(constraintDescr.proxy()));
	}
	
	public void testCreateWithNullLocation()
	{
		try
		{
			new ConstraintStatusExtended(modelConstraint.proxy(), targetObject.proxy(), IStatus.OK, 0, "TEST", null);
			fail("NPE expected");
		} catch (NullPointerException e)
		{
			//expected
		}
	}
	
	public void testProblemLocationInitialized()
	{
		final Set<IProblemLocation> locations  = new HashSet<IProblemLocation>();
		locations.add(locationMock.proxy());
		
		final ConstraintStatusExtended status = new ConstraintStatusExtended(modelConstraint.proxy(), targetObject.proxy(), IStatus.ERROR, 0, "TEST", locations);
		assertTrue("Problem location not initialized properly", status.getProblemLocations().size() == 1);
		assertTrue("Problem location not initialized properly", status.getProblemLocations().iterator().next() == locationMock.proxy());
	}

	public void testProblemLocationInitialized2()
	{
		final Set<IProblemLocation> locations  = new HashSet<IProblemLocation>();
		locations.add(locationMock.proxy());
		
		final ConstraintStatusExtended status = new ConstraintStatusExtended(modelConstraint.proxy(), targetObject.proxy(), "TEST", locations);
		assertTrue("Problem location not initialized properly", status.getProblemLocations().size() == 1);
		assertTrue("Problem location not initialized properly", status.getProblemLocations().iterator().next() == locationMock.proxy());
	}
	
	public void testProblemLocationIsNotModifiable()
	{
		final Set<IProblemLocation> locations  = new HashSet<IProblemLocation>();
		final ConstraintStatusExtended status = new ConstraintStatusExtended(modelConstraint.proxy(), targetObject.proxy(), IStatus.ERROR, 0, "TEST", locations);
		try
		{
			status.getProblemLocations().add(locationMock.proxy());
			fail("UnsupportedOperationException expected"); 
		}
		catch(UnsupportedOperationException e)
		{
			//expected
		}
	}
}
