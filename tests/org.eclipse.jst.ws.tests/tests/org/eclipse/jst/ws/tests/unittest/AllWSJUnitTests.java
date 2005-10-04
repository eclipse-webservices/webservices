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
//    testSuite.addTest( J2EEUtilsTests.suite() );
   
    String s = System.getProperty("org.eclipse.jst.server.tomcat.50");
    if (s != null && s.length() > 0) {
      testSuite.addTest( ServerCreationTests.suite());
    }
//    testSuite.addTest( ResourceUtilsTests.suite() );


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
