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

import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.Jee5WsDomRuntimeExtension;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.DomValidatorFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IDomValidator;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.JaxWsDomValidator;

/**
 * Tests for {@link JaxWsDomValidator}
 * 
 * @author Georgi Vachkov
 */
public class JaxWsDomValidatorTest extends TestCase 
{
	private JaxWsDomValidator validator;
	
	@Override
	public void setUp() {
		validator = new JaxWsDomValidator();
	}
	
	public void testRegistered()
	{
		Collection<IDomValidator> validators = DomValidatorFactory.INSTANCE.getRegisteredValidators();
		IDomValidator validator=null;
		for (IDomValidator domValidator : validators) {
			if(domValidator instanceof JaxWsDomValidator) {
				validator = domValidator;
				break;
			}
		}
		
		assertNotNull("Jax-ws validator not found", validator);
	}
	
	public void testGetSupportedDomRuntime() {
		assertEquals(Jee5WsDomRuntimeExtension.ID, validator.getSupportedDomRuntime());
	}
}
