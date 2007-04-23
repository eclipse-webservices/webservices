/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.tests.unittest;

import org.eclipse.wst.ws.tests.data.LocatorWorkspaceSetup;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * This class is to run all the unittest tests.
 */
public class AllWSJUnitTests extends TestCase
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
    TestSuite testSuite = new TestSuite();
 //add unit tests to suite here...
    testSuite.addTest( LocatorWorkspaceSetup.suite());
    testSuite.addTest( WebServiceFinderTests.suite());
    testSuite.addTest( WSDLCopierTests.suite());
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
