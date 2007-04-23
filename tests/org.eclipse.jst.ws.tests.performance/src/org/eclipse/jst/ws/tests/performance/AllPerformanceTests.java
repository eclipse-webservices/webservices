/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.tests.performance;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jst.ws.tests.axis.tomcat.v50.perfmsr.PerfmsrBUJavaAxisTC50;
import org.eclipse.jst.ws.tests.axis.tomcat.v50.perfmsr.PerfmsrClientAxisTC50;
import org.eclipse.jst.ws.tests.axis.tomcat.v50.perfmsr.PerfmsrTDJavaAxisTC50;

public class AllPerformanceTests {
  public static Test suite() {
    
    // TODO: Create a test suite 
    TestSuite suite = new TestSuite("Test for org.eclipse.jst.ws.tests.performance");
    //$JUnit-BEGIN$
    suite.addTestSuite(PerfmsrBUJavaAxisTC50.class);
    suite.addTestSuite(PerfmsrTDJavaAxisTC50.class);
    suite.addTestSuite(PerfmsrClientAxisTC50.class);
    //$JUnit-END$
    return suite;
  }
}
