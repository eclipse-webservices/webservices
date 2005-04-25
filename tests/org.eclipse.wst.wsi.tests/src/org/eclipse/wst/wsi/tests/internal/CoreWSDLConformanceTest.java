/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.tests.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.wst.wsi.internal.WSIPreferences;
import org.eclipse.wst.wsi.internal.WSITestToolsProperties;
import org.eclipse.wst.wsi.internal.analyzer.WSDLAnalyzer;
import org.eclipse.wst.wsi.internal.core.wsdl.WSDLReaderImpl;
import org.eclipse.wst.wsi.internal.core.xml.XMLUtils;
import org.eclipse.wst.wsi.internal.report.AssertionError;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import junit.framework.*;

public class CoreWSDLConformanceTest extends TestCase 
{
  public static final String WSDL_BASE_DIRECTORY = "testResources/samples/wsdl";
  public static final String TEST_CASE_FILE = "testcase.xml";
  public static final String WSDL_EXTENSION = ".wsdl";
  
  public static final String TAD_ID_AP = "AP10";
  public static final String TAD_ID_SSBP = "SSBP10";
  
  protected String pluginURI = null;
  protected String tadID = TAD_ID_AP;
  
  public CoreWSDLConformanceTest(String name) 
  {
      super(name);
	  pluginURI = WSITestsPlugin.getInstallURL();
  }

  protected void setup()
  {
	  
  }
  
  /**
   * JUnit test: validate the wsdl document against the specified WS-I Profile.
   * @param tad he WS-I Test Assertion Document
   * @param testName the name of the test containing the wsdl document
   */
  protected void runTest(String category, String testName, String tadID)
  {	
	this.tadID = tadID;

    assertNotNull("Problems determining base url", pluginURI);
    String testDirectory  = pluginURI + WSDL_BASE_DIRECTORY + "/" + category + "/" + testName;
    String wsdlFile = "file://" + testDirectory + "/" + testName + WSDL_EXTENSION;
    String testcaseFile = testDirectory +  "/" + TEST_CASE_FILE;
    
	// validate the WSDL document
    WSDLAnalyzer analyzer = validateConformance(wsdlFile, tadID);
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
  protected WSDLAnalyzer validateConformance(String filename, String tadID)
  {
	WSDLAnalyzer analyzer = null;
    try 
    {
   	  WSIPreferences preferences = new WSIPreferences();
   	  preferences.setComplianceLevel( WSITestToolsProperties.STOP_NON_WSI);
	  preferences.setTADFile(getTADURI(tadID));
		
	  analyzer = new WSDLAnalyzer(filename, preferences);
	  setConfigurations(filename, analyzer);
		
	  // run the conformance check and add errors and warnings as needed
	  analyzer.validateConformance();
    }    
    catch (Exception e)
    {
      return null;
    }	
	return analyzer;
  }
  
  /**
   * Retrieve the expected assertion failures.
   * @param filename - the location of the testcase.xml file.
   * @return the expected assertion failures.
   */
  protected List getExpectedAssertionFailures(String filename)
  {
	List assertionFailures = new ArrayList();
    try
	{
	  DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	  Document doc = builder.parse(filename);
	  NodeList list = doc.getElementsByTagName("assertion");
	  if (list != null)
	  for (int i=0; i<list.getLength(); i++)
	  {
		Element element = (Element)list.item(i);
		String tad = element.getAttribute("tadID");
		if ((tad != null) && tad.equals(this.tadID))
		   assertionFailures.add(element.getAttribute("id"));
	  }
	}
	catch (Exception e)
	{
	  assertionFailures = null;
	}
	  return assertionFailures;
  }
  
  /**
   * Compare the actual errors with the expected errors.
   * @param errors the actual assertion failures
   * @param expectedErrors the expected assertion failures
   */
  protected void analyzeResults(List errors, List expectedErrors)
  {
	 List actualErrors = new ArrayList();
	 if ((errors != null) && (expectedErrors != null))
	 {
       Iterator i = errors.iterator();
       while (i.hasNext())
	   {
		 AssertionError e = (AssertionError)i.next();
		 String assertionId = e.getAssertionID();
		 actualErrors.add(assertionId);
		 
		 if (!expectedErrors.contains(assertionId))
		 {
			fail("Unexpected assertion failure: " + assertionId); 
		 }
	   }
	   
	   i = expectedErrors.iterator();
       while (i.hasNext())
	   {
		 String assertionId = (String)i.next();
		 
		 if (!actualErrors.contains(assertionId))
		 {
			fail("Expected assertion failure: " + assertionId); 
		 }
	   }
	   assertEquals(expectedErrors.size(), errors.size());
	 }
  }

  /**
   * Gather configuration information from the WSDL file.
   * @param fileName the wsdl location.
   * @param a WSDLAnalyzer object containg configuration info.
   */
  protected void setConfigurations(String filename, WSDLAnalyzer analyzer)
  {
    try
    {
      WSDLReader wsdlReader = new WSDLReaderImpl();

      // Set features
      wsdlReader.setFeature(com.ibm.wsdl.Constants.FEATURE_VERBOSE, false);
      wsdlReader.setFeature(com.ibm.wsdl.Constants.FEATURE_IMPORT_DOCUMENTS, true);

      // Parse the WSDL document
      Document document = XMLUtils.parseXMLDocument(filename);
	  Definition definition = wsdlReader.readWSDL(filename, document);
      //Definition definition = reader.readWSDL(filename);

	  String namespace = definition.getTargetNamespace();

	  // get all the collections we may need to validate
	  Collection services = definition.getServices().values();
	  Collection bindings = definition.getBindings().values();
	  Collection portTypes = definition.getPortTypes().values();
	  Collection messages = definition.getMessages().values();

	  // The WS-I conformance tools require that each service be analyzed separately.
	  // Get all the services and analyze them.
	  if (services != null && !services.isEmpty())
	  {
	    Iterator serviceIterator = services.iterator();

	    while (serviceIterator.hasNext())
	    {
	      Service service = (Service) serviceIterator.next();

	      String servicename = service.getQName().getLocalPart();

	      Collection ports = service.getPorts().values();
	      if (ports != null && !ports.isEmpty())
	      {
	        // The WS-I tools must be called once for each port within each service.
	        Iterator portIterator = ports.iterator();
	        while (portIterator.hasNext())
	        {
	          Port port = (Port) portIterator.next();
			  analyzer.addConfigurationToTest(servicename, namespace, port.getName(), WSDLAnalyzer.PORT);
	        }
	      }
	      // validate at the binding level - check for every binding
	      else if (bindings != null && !bindings.isEmpty())
	      {
	        Iterator bindingIterator = bindings.iterator();

	        while (bindingIterator.hasNext())
	        {
	          Binding binding = (Binding) bindingIterator.next();
	          String bindingname = binding.getQName().getLocalPart();
              analyzer.addConfigurationToTest(null, namespace, bindingname, WSDLAnalyzer.BINDING);
	        }
	      }
	      // validate at the portType level - check for every portType
	      else if (portTypes != null && !portTypes.isEmpty())
	      {
	        Iterator portTypeIterator = portTypes.iterator();
	        while (portTypeIterator.hasNext())
	        {
	          PortType portType = (PortType) portTypeIterator.next();
	          String portTypename = portType.getQName().getLocalPart();
              analyzer.addConfigurationToTest(null, namespace, portTypename, WSDLAnalyzer.PORTTYPE);
	        }
	      }
	      // validate at the message level - check for every message
	      else if (messages != null && !messages.isEmpty())
	      {
	        Iterator messageIterator = messages.iterator();
	        while (messageIterator.hasNext())
	        {
	          Message message = (Message) messageIterator.next();
	          String messagename = message.getQName().getLocalPart();
              analyzer.addConfigurationToTest(null, namespace, messagename, WSDLAnalyzer.MESSAGE);
	        }
	      }
	    }
	  }
    }
	catch (Exception e)
	{
      fail("Unexpected problems setting configurations");
	}
  }
  
  /**
   * Return the location of the given tad.
   * @param tid the tad id.
   * @return the location of the tad.
   */
  protected String getTADURI(String tid)
  {
	  if (tid.equals(TAD_ID_AP))
	     return WSITestToolsProperties.AP_ASSERTION_FILE;
	  else
		 return WSITestToolsProperties.SSBP_ASSERTION_FILE;
  }
}
