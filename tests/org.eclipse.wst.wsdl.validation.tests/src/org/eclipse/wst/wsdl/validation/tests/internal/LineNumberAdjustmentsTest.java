/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.tests.internal;

/**
 * Tests for line number adjustments in the WSDL validator's
 * XML validator.
 */
public class LineNumberAdjustmentsTest extends BaseTestCase 
{
  private String LINE_NUMBER_ADJUSTMENTS_DIR = "LineNumberAdjustments/";

  /**
   * Test /LineNumberAdjustments/cvc-complex-type.2.3/cvc-complex-type.2.3.wsdl
   */
  public void testcvccomplextype23()
  {
    String testname = "cvc-complex-type.2.3";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + LINE_NUMBER_ADJUSTMENTS_DIR + "cvc-complex-type.2.3/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + LINE_NUMBER_ADJUSTMENTS_DIR + "cvc-complex-type.2.3/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + LINE_NUMBER_ADJUSTMENTS_DIR + "cvc-complex-type.2.3/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /LineNumberAdjustments/cvc-complex-type.2.4.b/cvc-complex-type.2.4.b.wsdl
   */
  public void testcvccomplextype24b()
  {
    String testname = "cvc-complex-type.2.4.b";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + LINE_NUMBER_ADJUSTMENTS_DIR + "cvc-complex-type.2.4.b/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + LINE_NUMBER_ADJUSTMENTS_DIR + "cvc-complex-type.2.4.b/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + LINE_NUMBER_ADJUSTMENTS_DIR + "cvc-complex-type.2.4.b/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
}
