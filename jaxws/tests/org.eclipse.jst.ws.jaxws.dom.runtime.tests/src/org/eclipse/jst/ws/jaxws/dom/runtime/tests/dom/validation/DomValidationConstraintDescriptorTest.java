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

import junit.framework.TestCase;

import org.eclipse.emf.validation.model.ConstraintSeverity;
import org.eclipse.emf.validation.model.EvaluationMode;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.DomValidationConstraintDescriptor;

public class DomValidationConstraintDescriptorTest extends TestCase 
{
	private DomValidationConstraintDescriptor desc;

	@Override
	public void setUp()
	{
		desc = new DomValidationConstraintDescriptor(){
			public String getId() {return null;}
			public String getPluginId() {return null;}
			public ConstraintSeverity getSeverity() {return null;}
			public int getStatusCode() {return 0;}};
	}
	
	public void testDescriptor()
	{
		assertNull(desc.getBody());
		assertNotNull(desc.getDescription());
		assertEquals(EvaluationMode.BATCH, desc.getEvaluationMode());
		assertNull(desc.getMessagePattern());
		assertNotNull(desc.getName());
		assertTrue(desc.targetsEvent(null));
		assertTrue(desc.targetsTypeOf(null));		
	}
}
