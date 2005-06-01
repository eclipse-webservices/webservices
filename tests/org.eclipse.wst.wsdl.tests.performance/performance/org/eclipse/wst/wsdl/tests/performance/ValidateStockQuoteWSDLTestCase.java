/**********************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Contributors:
 *    IBM - Initial API and implementation
 **********************************************************************/

package org.eclipse.wst.wsdl.tests.performance;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.test.performance.PerformanceTestCase;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.ui.eclipse.WSDLValidator;

public class ValidateStockQuoteWSDLTestCase extends PerformanceTestCase
{
  private WSDLValidator validator = WSDLValidator.getInstance();
  
  public static Test suite()
  {
    return new TestSuite(ValidateStockQuoteWSDLTestCase.class, "ValidateStockQuoteWSDLTestCase");
  }
  
  public void testValidateStockQuoteWSDL() throws Exception
  {
    startMeasuring();
	
    URL wsdl = PerformancePlugin.getDefault().getBundle().getEntry("data/StockQuote/StockQuote.wsdl");
    IValidationReport valreport = validator.validate(wsdl.toString());
	
	stopMeasuring();
	commitMeasurements();
	assertPerformance();
  }
}
