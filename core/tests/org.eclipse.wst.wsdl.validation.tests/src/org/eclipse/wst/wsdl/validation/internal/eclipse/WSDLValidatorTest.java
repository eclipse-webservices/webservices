/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.internal.eclipse;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test the Eclipse specific WSDL validator.
 */
public class WSDLValidatorTest extends TestCase 
{
  /**
   * Create a tests suite from this test class.
   * 
   * @return A test suite containing this test class.
   */
  public static Test suite()
  {
    return new TestSuite(WSDLValidatorTest.class);
  }
  
  /**
   * Test to ensure the URI resolver is not null in the Eclipse
   * specific XML validator.
   */
  public void testURIResolverIsRegistered()
  {
	WSDLValidatorWrapper validator = new WSDLValidatorWrapper();
	assertNotNull("The URI resolver is null.", validator.getURIResolver());
  }

}
