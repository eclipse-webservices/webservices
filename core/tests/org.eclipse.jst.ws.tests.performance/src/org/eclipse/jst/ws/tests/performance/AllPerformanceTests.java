/*******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070502  185208 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20080313  126774 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20090402  263873 mahutch@ca.ibm.com - Mark Hutchinson, Move Axis2 peformance tests to new plugin
 *******************************************************************************/
package org.eclipse.jst.ws.tests.performance;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jst.ws.tests.axis.tomcat.v50.perfmsr.PerfmsrBUJavaAxisTC50;
import org.eclipse.jst.ws.tests.axis.tomcat.v50.perfmsr.PerfmsrClientAxisTC50;
import org.eclipse.jst.ws.tests.axis.tomcat.v50.perfmsr.PerfmsrTDJavaAxisTC50;

public class AllPerformanceTests {
	
  public static Test suite() {
    
    TestSuite suite = new TestSuite("Test for org.eclipse.jst.ws.tests.performance");
    //$JUnit-BEGIN$
    
    // Check for Tomcat 5.0 server
	String s = System.getProperty("org.eclipse.jst.server.tomcat.50");
	if (s==null){
		s = System.getProperty("tomcat50Dir");
	}

	if (s != null && s.length() > 0) {
	    suite.addTestSuite(PerfmsrBUJavaAxisTC50.class);
	    suite.addTestSuite(PerfmsrTDJavaAxisTC50.class);
	    suite.addTestSuite(PerfmsrClientAxisTC50.class);
	} else {
		System.err.println("Warning: Tomcat 5.0 not found - performance tests skipped");
	}

	
	
    //$JUnit-END$
    return suite;
  }
}
