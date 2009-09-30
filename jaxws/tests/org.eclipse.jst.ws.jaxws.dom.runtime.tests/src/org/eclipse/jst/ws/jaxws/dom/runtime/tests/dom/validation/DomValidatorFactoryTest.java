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

import org.eclipse.jst.ws.jaxws.dom.runtime.validation.DomValidatorFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.IDomValidator;

/**
 * Tests for {@link DomValidatorFactory} class.
 * @author Georgi Vachkov
 */
public class DomValidatorFactoryTest extends TestCase 
{
	public void testGetRegisteredValidators()
	{
		Collection<IDomValidator> validators = DomValidatorFactory.INSTANCE.getRegisteredValidators();
		assertTrue("No validators found", validators.size() > 0);
	}
}
