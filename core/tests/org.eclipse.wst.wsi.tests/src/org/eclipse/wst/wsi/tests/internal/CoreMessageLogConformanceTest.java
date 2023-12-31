/*******************************************************************************
 * Copyright (c) 2002-2007 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.tests.internal;

import java.util.List;

import org.eclipse.wst.wsi.internal.WSIPreferences;
import org.eclipse.wst.wsi.internal.WSITestToolsProperties;
import org.eclipse.wst.wsi.internal.analyzer.MessageAnalyzer;

public class CoreMessageLogConformanceTest extends CoreConformanceTest 
{
  public static final String MESSAGE_LOG_BASE_DIRECTORY =
      "testResources/samples/messageLogs";
  public static final String MESSAGE_LOG_EXTENSION = ".wsimsg";
  public static final String WSDL_BASE_DIRECTORY = "testResources/samples/wsdl";
  public static final String WSDL_EXTENSION = ".wsdl";

  public CoreMessageLogConformanceTest(String name) 
  {
      super(name);
  }

  /**
   * JUnit test: validate the message log against the specified WS-I Profile.
   * @param tad he WS-I Test Assertion Document
   * @param testName the name of the test containing the wsdl document
   */
  protected void runTest(String category, String testName, String tadID) {	
      this.tadID = tadID;

      assertNotNull("Problems determining base url", pluginURI);
      String testDirectory  = pluginURI + MESSAGE_LOG_BASE_DIRECTORY + "/" +
              category + "/" + testName;
      String messageLogFile = "file://" + testDirectory + "/" + testName +
              MESSAGE_LOG_EXTENSION;
      String testcaseFile = testDirectory +  "/" + TEST_CASE_FILE;

      // validate the message log document
      MessageAnalyzer analyzer = validateConformance(messageLogFile, tadID);
      assertNotNull("Unknown problems during validation", analyzer);

      // retrieve the expected assertion failures
      List expectedErrors = getExpectedAssertionFailures(testcaseFile);
      assertNotNull("Problems retrieving expected failures", expectedErrors);

      // compare the actual errors with the expected errors
      analyzeResults(analyzer.getAssertionErrors(), expectedErrors);
  }

  /**
   * Validate the wsdl document against the specified WS-I Profile.
   * @param tad  the WS-I Test Assertion Document
   * @param filename the wsdl document
   * @return the WSDLAnalyzer object containing the validation results
   */  
  protected MessageAnalyzer validateConformance(String filename, String tadID) {
      MessageAnalyzer analyzer = null;
      try {
          WSIPreferences preferences = new WSIPreferences();
          preferences.setComplianceLevel( WSITestToolsProperties.STOP_NON_WSI);
          preferences.setTADFile(getTADURI(tadID));

          analyzer = new MessageAnalyzer(filename, preferences);

          // run the conformance check and add errors and warnings as needed
          analyzer.validateConformance();
      } catch (Exception e) {
          return null;
      }	
      return analyzer;
  }

  
  
  /**
   * JUnit test: validate the message log with a WSDL file
   * @param wsdlFile URI of the WSDL file
   * @param WSData String array containing: elementname, namespace, parentname, type
   * @param filename the name of the log and wsdl file (they should be located in the log and wsdl paths respectively)
   */
  
  protected void runTestWithWSDL(String category, String testName, String[] WSData) {	

      assertNotNull("Problems determining base url", pluginURI);
      String testDirectory  = pluginURI + MESSAGE_LOG_BASE_DIRECTORY + "/" +
              category + "/" + testName;
      String messageLogFile = "file://" + testDirectory + "/" + testName +
              MESSAGE_LOG_EXTENSION;
      String testcaseFile = testDirectory +  "/" + TEST_CASE_FILE;
      String wsdlFile = "file://" + pluginURI + WSDL_BASE_DIRECTORY + "/" + category + "/" + testName + WSDL_EXTENSION;
      
      // validate the message log document
      MessageAnalyzer analyzer = validateConformanceWithWSDL(messageLogFile, wsdlFile, WSData);
      assertNotNull("Unknown problems during validation", analyzer);

      // retrieve the expected assertion failures
      List expectedErrors = getExpectedAssertionFailures(testcaseFile);
      assertNotNull("Problems retrieving expected failures", expectedErrors);

      // compare the actual errors with the expected errors
      analyzeResults(analyzer.getAssertionErrors(), expectedErrors);
  }


  /**
   * Validate log with a WSDL file
   * @param wsdlFile URI of the WSDL file
   * @param WSData String array containing: elementname, namespace, parentname, type
   * @param filename the name of the log and wsdl file (they should be located in the log and wsdl paths respectively)
   * @return the WSDLAnalyzer object containing the validation results
   */  

	protected MessageAnalyzer validateConformanceWithWSDL(String filename, String wsdlFile, String[] WSData) {
	    MessageAnalyzer analyzer = null;
	    try {
	        WSIPreferences preferences = new WSIPreferences();
	        preferences.setComplianceLevel( WSITestToolsProperties.STOP_NON_WSI);
	        preferences.setTADFile(getTADURI(tadID));
	
	        analyzer = new MessageAnalyzer(filename, wsdlFile, WSData[0],  WSData[1], WSData[2], WSData[3], preferences);
	
	        // run the conformance check and add errors and warnings as needed
	        analyzer.validateConformance();
	    } catch (Exception e) {
	        return null;
	    }	
	    return analyzer;
	}

}
