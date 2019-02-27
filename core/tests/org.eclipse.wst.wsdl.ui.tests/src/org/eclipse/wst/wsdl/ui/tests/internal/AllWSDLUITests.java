/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
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
    // Misc bug fixes
    addTest(BugFixesTest.suite());
  }
}
