/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.performance;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;

import javax.wsdl.WSDLException;

import junit.framework.Assert;

import org.eclipse.test.performance.Dimension;
import org.eclipse.test.performance.PerformanceTestCase;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentWSIContext;
import org.eclipse.wst.wsdl.validation.internal.eclipse.WSDLValidator;

public class ValidateOAGISWSITestcase extends PerformanceTestCase
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
    wsicontext.updateWSICompliances(PersistentWSIContext.STOP_NON_WSI);
    wsicontext = wsui.getWSIAPContext();
    wsicontext.updateWSICompliances(PersistentWSIContext.STOP_NON_WSI);
  } 

  public void testValidateWSDL() throws MalformedURLException, WSDLException
  {
    String oagis80Dir = System.getProperty("oagis80Dir");
    Assert.assertNotNull(oagis80Dir);
    if (!oagis80Dir.endsWith("/") && !oagis80Dir.endsWith("\\"))
      oagis80Dir = oagis80Dir + "/";
    File dir = new File(oagis80Dir + "OAGIS8.0/ws/wsdl");
    if (dir.exists() && dir.isDirectory())
    {
      File[] wsdls = dir.listFiles
      (
        new FileFilter()
        {
          public boolean accept(File pathname)
          {
            return pathname.getName().endsWith(".wsdl");
          }
        }
      );
      tagAsSummary("Validate OAGIS WSDL with WS-I", new Dimension[] {Dimension.ELAPSED_PROCESS, Dimension.WORKING_SET});
	  validator = WSDLValidator.getInstance();
	  startMeasuring();
      for (int i = 0; i < wsdls.length; i++)
        validateWSDL(wsdls[i].toURL().toString());
      stopMeasuring();
      commitMeasurements();
      assertPerformance();
    }
    else
      fail(dir.toString());
  }

  private void validateWSDL(String location) throws WSDLException
  {
    validator.validate(location);
  }
}
