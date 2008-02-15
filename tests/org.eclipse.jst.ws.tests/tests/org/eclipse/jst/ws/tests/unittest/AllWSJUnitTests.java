/*******************************************************************************
 * Copyright (c) 2004, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 2007104   114835 sengpl@ca.ibm.com - Seng Phung-Lu
 * 20070314  176886 pmoogk@ca.ibm.com - Peter Moogk
 * 20071030  208124 kathy@ca.ibm.com - Kathy Chan
 * 20071031  208124 kathy@ca.ibm.com - Kathy Chan
 * 20071116  208124 sengpl@ca.ibm.com - Seng Phung-Lu
 *******************************************************************************/
package org.eclipse.jst.ws.tests.unittest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.eclipse.jst.ws.tests.util.JUnitUtils;

/**
 * This class is to run all the unittest tests.
 */
public class AllWSJUnitTests extends TestCase implements WSJUnitConstants
{
  /**
   * Method parseArgs.
   * 
   * @param args
   */
  private void parseArgs(Object args)
  {
    // typicially args is an array of strings,
    // not sure when it wouldn't be.
  }

  public static void main(String[] args)
  {
    new AllWSJUnitTests().runMain(args);
  }

  
  public Object runMain(Object args)
  {
    Object result = null;
    try
    {
      TestRunner testRunner = null;

      parseArgs(args);

      testRunner = new TestRunner(System.out);

      Test suite = suite();
      TestResult testResult = testRunner.doRun(suite, false);
      printHeader(testResult);      
    }
    catch (Exception e)
    {
      result = e;
    }

    return result;
  }

  public static Test suite()
  {
    JUnitUtils.enableOverwrite(true);
    
    TestSuite testSuite = new TestSuite();
    testSuite.addTest( ComponentCreationTests.suite() );
    testSuite.addTest( J2EEUtilsTests.suite() );
   
    // Tests which require a Tomcat server
    String s = System.getProperty("org.eclipse.jst.server.tomcat.50");
    if (s != null && s.length() > 0) {
      testSuite.addTest( BUJavaAxisTC50.suite() );
      testSuite.addTest( TDJavaAxisTC50.suite() );
      testSuite.addTest( ClientAxisTC50.suite() );
      testSuite.addTest( ServerCreationTests.suite());

    }
    testSuite.addTest( ResourceUtilsTests.suite() );

    return testSuite;
  }

  /**
   * Prints the header of the report
   */
  protected void printHeader(TestResult result)
  {
    if (result.wasSuccessful())
    {
      System.out.println();
      System.out.print("OK");
      System.out.println(" (" + result.runCount() + " tests)");

    }
    else
    {
      System.out.println();
      System.out.println("FAILURES!!!");
      System.out.println(
        "Tests run: "
          + result.runCount()
          + ",  Failures: "
          + result.failureCount()
          + ",  Errors: "
          + result.errorCount());
    }
  }
}
