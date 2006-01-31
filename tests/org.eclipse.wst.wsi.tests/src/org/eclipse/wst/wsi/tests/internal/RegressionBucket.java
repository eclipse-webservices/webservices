/* Copyright (c) 2001, 2004 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/
package org.eclipse.wst.wsi.tests.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.wst.wsi.internal.WSITestToolsProperties;

import junit.framework.Test;
import junit.framework.TestSuite;
/**
 * JUnit tests that runs all of the tests implemented 
 * in this plugin.
 * 
 * @author lauzond
 */
public class RegressionBucket extends TestSuite {

	  public static Test suite() 
	  {
	    TestSuite suite= new TestSuite("Regression Bucket");
	    if (tadIsAvailable(WSITestToolsProperties.SSBP_ASSERTION_FILE))
	      suite.addTest(new TestSuite(WSDLConformanceSSBPTest.class));
	    if (tadIsAvailable(WSITestToolsProperties.AP_ASSERTION_FILE))
	      suite.addTest(new TestSuite(WSDLConformanceAPTest.class));
	    
	    return suite;
	  }
	  
	  public static boolean tadIsAvailable(String tadURI)
	  {
		boolean result = true;
		InputStream is = null;
		try
		{
		  URL url = new URL(tadURI);
		  is = url.openStream();
		  is.close();
		}
		catch (IOException e)
		{
		  result = false;
		}
	    return result;
	  }

}
