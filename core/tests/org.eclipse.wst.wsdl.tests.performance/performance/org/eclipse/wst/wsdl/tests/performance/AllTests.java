/**********************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM - Initial API and implementation
 **********************************************************************/

package org.eclipse.wst.wsdl.tests.performance;

import org.eclipse.wst.wsdl.tests.performance.scalability.WSDLScalabilityTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite("Test for org.eclipse.wst.wsdl.tests.performance");
    //$JUnit-BEGIN$
    suite.addTestSuite(ReadStockQuoteWSDLTestCase.class);
    suite.addTestSuite(ReadStockQuoteWSDLEMFTestCase.class);
    suite.addTestSuite(ValidateStockQuoteWSDLTestCase.class);
    suite.addTestSuite(ValidateStockQuoteWSITestCase.class);
    suite.addTestSuite(OpenStockQuoteWSDLSetup.class);
    suite.addTestSuite(OpenStockQuoteWSDLTestCase.class);
    suite.addTest(WSDLScalabilityTests.suite());
    //$JUnit-END$
    return suite;
  }
}
