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
	    suite.addTest(new TestSuite(WSDLConformanceSSBPTest.class));
	    suite.addTest(new TestSuite(WSDLConformanceAPTest.class));
	    
	    return suite;
	  }

}
