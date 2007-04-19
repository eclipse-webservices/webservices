/*******************************************************************************
 * Copyright (c) 2005 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.performance;

import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.test.performance.PerformanceTestCase;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;
import org.eclipse.wst.wsdl.validation.internal.eclipse.WSDLValidator;

public class ValidateStockQuoteWSDLTestCase extends PerformanceTestCase
{
  private WSDLValidator validator;
  
  /* (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception 
  {
    super.setUp();
    // Set the WS-I preference to ignore so only WSDL errors will be tested.
    WSPlugin wsui = WSPlugin.getInstance();
    PersistentWSIContext wsicontext = wsui.getWSISSBPContext();
    wsicontext.updateWSICompliances(PersistentWSIContext.IGNORE_NON_WSI);
    wsicontext = wsui.getWSIAPContext();
    wsicontext.updateWSICompliances(PersistentWSIContext.IGNORE_NON_WSI);
  }  
  
  public static Test suite()
  {
    return new TestSuite(ValidateStockQuoteWSDLTestCase.class, "ValidateStockQuoteWSDLTestCase");
  }
  
  public void testValidateStockQuoteWSDL() throws Exception
  {
    validator = WSDLValidator.getInstance();
    URL wsdl = PerformancePlugin.getDefault().getBundle().getEntry("data/StockQuote/StockQuote.wsdl");
    String path = wsdl.toString();

    startMeasuring();
    validator.validate(path);
    stopMeasuring();
    commitMeasurements();
    assertPerformance();
  }
}
