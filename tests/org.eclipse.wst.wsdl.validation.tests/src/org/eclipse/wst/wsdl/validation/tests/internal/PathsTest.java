/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.validation.tests.internal;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for file paths.
 * 
 * @author Lawrence Mandel, IBM
 */
public class PathsTest extends BaseTestCase
{
  private String PATHS_DIR = "Paths/";
  
  /**
   * Create a tests suite from this test class.
   * 
   * @return A test suite containing this test class.
   */
  public static Test suite()
  {
    return new TestSuite(PathsTest.class);
  }
  
  /* (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp()
  {
    super.setUp();
  }
  
  /* (non-Javadoc)
   * @see junit.framework.TestCase#tearDown()
   */
  protected void tearDown() throws Exception
  {
    // Delete the simple project for ImportedSchemaWithSchemaImport test
    super.tearDown();
  }

  /**
   * Test /Paths/Space InPath/SpaceInPathValid.wsdl
   */
  public void testSpaceInPathValid()
  {
    String testname = "SpaceInPathValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Space InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Space InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Space InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Space InPath/SpaceInPathInvalid.wsdl
   */
  public void testSpaceInPathInvalid()
  {
    String testname = "SpaceInPathInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Space InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Space InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Space InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/AngleHat^InPath/AngleHatInPathValid.wsdl
   */
  public void testAngleHatInPathValid()
  {
    String testname = "AngleHatInPathValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "AngleHat^InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "AngleHat^InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "AngleHat^InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/AngleHat^InPath/AngleHatInPathInvalid.wsdl
   */
  public void testAngleHatInPathInvalid()
  {
    String testname = "AngleHatInPathInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "AngleHat^InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "AngleHat^InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "AngleHat^InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/AngleHatInFilename/AngleHat^InFilenameValid.wsdl
   */
  public void testAngleHatInFilenameValid()
  {
    String testname = "AngleHat^InFilenameValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "AngleHatInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "AngleHatInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "AngleHatInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/AngleHatInFilename/AngleHat^InFilenameInvalid.wsdl
   */
  public void testAngleHatInFilenameInvalid()
  {
    String testname = "AngleHat^InFilenameInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "AngleHatInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "AngleHatInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "AngleHatInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/CloseBracket)InPath/CloseBracketInPathValid.wsdl
   */
  public void testCloseBracketInPathValid()
  {
    String testname = "CloseBracketInPathValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "CloseBracket)InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "CloseBracket)InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "CloseBracket)InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/CloseBracket)InPath/CloseBracketInPathInvalid.wsdl
   */
  public void testCloseBracketInPathInvalid()
  {
    String testname = "CloseBracketInPathInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "CloseBracket)InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "CloseBracket)InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "CloseBracket)InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/CloseBracketInFilename/CloseBracket)InFilenameValid.wsdl
   */
  public void testCloseBracketInFilenameValid()
  {
    String testname = "CloseBracket)InFilenameValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "CloseBracketInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "CloseBracketInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "CloseBracketInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/CloseBracketInFilename/CloseBracket)InFilenameInvalid.wsdl
   */
  public void testCloseBracketInFilenameInvalid()
  {
    String testname = "CloseBracket)InFilenameInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "CloseBracketInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "CloseBracketInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "CloseBracketInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Dash-InPath/DashInPathValid.wsdl
   */
  public void testDashInPathValid()
  {
    String testname = "DashInPathValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Dash-InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Dash-InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Dash-InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Dash-InPath/DashInPathInvalid.wsdl
   */
  public void testDashInPathInvalid()
  {
    String testname = "DashInPathInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Dash-InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Dash-InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Dash-InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/DashInFilename/Dash-InFilenameValid.wsdl
   */
  public void testDashInFilenameValid()
  {
    String testname = "Dash-InFilenameValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "DashInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "DashInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "DashInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/DashInFilename/Dash-InFilenameInvalid.wsdl
   */
  public void testDashInFilenameInvalid()
  {
    String testname = "Dash-InFilenameInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "DashInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "DashInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "DashInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Exclamation!InPath/ExclamationInPathValid.wsdl
   */
  public void testExclamationInPathValid()
  {
    String testname = "ExclamationInPathValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Exclamation!InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Exclamation!InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Exclamation!InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Exclamation!InPath/ExclamationInPathInvalid.wsdl
   */
  public void testExclamationInPathInvalid()
  {
    String testname = "ExclamationInPathInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Exclamation!InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Exclamation!InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Exclamation!InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/ExclamationInFilename/Exclamation!InFilenameValid.wsdl
   */
  public void testExclamationInFilenameValid()
  {
    String testname = "Exclamation!InFilenameValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "ExclamationInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "ExclamationInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "ExclamationInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/ExclamationInFilename/Exclamation!InFilenameInvalid.wsdl
   */
  public void testExclamationInFilenameInvalid()
  {
    String testname = "Exclamation!InFilenameInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "ExclamationInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "ExclamationInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "ExclamationInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/OpenBracket(InPath/OpenBracketInPathValid.wsdl
   */
  public void testOpenBracketInPathValid()
  {
    String testname = "OpenBracketInPathValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "OpenBracket(InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "OpenBracket(InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "OpenBracket(InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/OpenBracket(InPath/OpenBracketInPathInvalid.wsdl
   */
  public void testOpenBracketInPathInvalid()
  {
    String testname = "OpenBracketInPathInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "OpenBracket(InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "OpenBracket(InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "OpenBracket(InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/OpenBracketInFilename/OpenBracket(InFilenameValid.wsdl
   */
  public void testOpenBracketInFilenameValid()
  {
    String testname = "OpenBracket(InFilenameValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "OpenBracketInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "OpenBracketInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "OpenBracketInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/OpenBracketInFilename/OpenBracket(InFilenameInvalid.wsdl
   */
  public void testOpenBracketInFilenameInvalid()
  {
    String testname = "OpenBracket(InFilenameInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "OpenBracketInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "OpenBracketInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "OpenBracketInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Period.InPath/PeriodInPathValid.wsdl
   */
  public void testPeriodInPathValid()
  {
    String testname = "PeriodInPathValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Period.InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Period.InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Period.InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Period.InPath/PeriodInPathInvalid.wsdl
   */
  public void testPeriodInPathInvalid()
  {
    String testname = "PeriodInPathInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Period.InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Period.InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Period.InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/PeriodInFilename/Period.InFilenameValid.wsdl
   */
  public void testPeriodInFilenameValid()
  {
    String testname = "Period.InFilenameValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "PeriodInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "PeriodInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "PeriodInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/PeriodInFilename/Period.InFilenameInvalid.wsdl
   */
  public void testPeriodInFilenameInvalid()
  {
    String testname = "Period.InFilenameInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "PeriodInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "PeriodInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "PeriodInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Quote'InPath/QuoteInPathValid.wsdl
   */
  public void testQuoteInPathValid()
  {
    String testname = "QuoteInPathValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Quote'InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Quote'InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Quote'InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Quote'InPath/QuoteInPathInvalid.wsdl
   */
  public void testQuoteInPathInvalid()
  {
    String testname = "QuoteInPathInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Quote'InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Quote'InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Quote'InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/QuoteInFilename/Quote'InFilenameValid.wsdl
   */
  public void testQuoteInFilenameValid()
  {
    String testname = "Quote'InFilenameValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "QuoteInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "QuoteInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "QuoteInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/QuoteInFilename/Quote'InFilenameInvalid.wsdl
   */
  public void testQuoteInFilenameInvalid()
  {
    String testname = "Quote'InFilenameInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "QuoteInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "QuoteInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "QuoteInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/SpaceInFilename/Space InFilenameValid.wsdl
   */
  public void testSpaceInFilenameValid()
  {
    String testname = "Space InFilenameValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "SpaceInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "SpaceInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "SpaceInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/SpaceInFilename/Space InFilenameInvalid.wsdl
   */
  public void testSpaceInFilenameInvalid()
  {
    String testname = "Space InFilenameInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "SpaceInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "SpaceInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "SpaceInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Tilde~InPath/TildeInPathValid.wsdl
   */
  public void testTildeInPathValid()
  {
    String testname = "TildeInPathValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Tilde~InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Tilde~InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Tilde~InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Tilde~InPath/TildeInPathInvalid.wsdl
   */
  public void testTildeInPathInvalid()
  {
    String testname = "TildeInPathInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Tilde~InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Tilde~InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Tilde~InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/TildeInFilename/Tilde~InFilenameValid.wsdl
   */
  public void testTildeInFilenameValid()
  {
    String testname = "Tilde~InFilenameValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "TildeInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "TildeInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "TildeInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/TildeInFilename/Tilde~InFilenameInvalid.wsdl
   */
  public void testTildeInFilenameInvalid()
  {
    String testname = "Tilde~InFilenameInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "TildeInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "TildeInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "TildeInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Underscore_InPath/UnderscoreInPathValid.wsdl
   */
  public void testUnderscoreInPathValid()
  {
    String testname = "UnderscoreInPathValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Underscore_InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Underscore_InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Underscore_InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/Underscore_InPath/UnderscoreInPathInvalid.wsdl
   */
  public void testUnderscoreInPathInvalid()
  {
    String testname = "UnderscoreInPathInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "Underscore_InPath/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "Underscore_InPath/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "Underscore_InPath/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/UnderscoreInFilename/Underscore_InFilenameValid.wsdl
   */
  public void testUnderscoreInFilenameValid()
  {
    String testname = "Underscore_InFilenameValid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "UnderscoreInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "UnderscoreInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "UnderscoreInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /Paths/UnderscoreInFilename/Underscore_InFilenameInvalid.wsdl
   */
  public void testUnderscoreInFilenameInvalid()
  {
    String testname = "Underscore_InFilenameInvalid";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + PATHS_DIR + "UnderscoreInFilename/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + PATHS_DIR + "UnderscoreInFilename/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + PATHS_DIR + "UnderscoreInFilename/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
}
