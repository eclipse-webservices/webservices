/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.tests.internal;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Wrapper test suite for all WSDL UI tests.
 */
public class AllWSDLUITests extends TestSuite
{
  /**
   * Create this test suite.
   * 
   * @return This test suite.
   */
  public static Test suite()
  {
    return new AllWSDLUITests();
  }

  /**
   * Constructor
   */
  public AllWSDLUITests()
  {
    super("AllWSDLUITests");
    // Unused XSD imports tests in WSDL files
    addTest(WSDLUnusedTests.suite());
    // WSDL xml ns table cleanup
    addTest(WSDLXMLNSCleanupTests.suite());
    // Unused WSDL Imports
    addTest(UnusedWSDLImportsTests.suite());
  }
}
