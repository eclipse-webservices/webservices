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
 * WSDL validation tests for the WSDL validator.
 */
public class WSDLTest extends BaseTestCase
{
  private String WSDL_DIR = "WSDL/";
  
  /**
   * Create a tests suite from this test class.
   * 
   * @return A test suite containing this test class.
   */
  public static Test suite()
  {
    return new TestSuite(WSDLTest.class);
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
   * IMPORT TESTS
   */
  
  /**
   * Test /WSDL/Import/ImportSchemaWithWSDLImport.wsdl
   */
  public void testImportSchemaWithWSDLImport()
  {
    String testname = "ImportSchemaWithWSDLImport";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/ImportSchemaWithWSDLImportInvalidNS.wsdl
   */
  public void testImportSchemaWithWSDLImportInvalidNS()
  {
    String testname = "ImportSchemaWithWSDLImportInvalidNS";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/ImportWSDLWithWSDL.wsdl
   */
  public void testImportWSDLWithWSDL()
  {
    String testname = "ImportWSDLWithWSDL";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/ImportWSDLWithWSDLInvalidNS.wsdl
   */
  public void testImportWSDLWithWSDLInvalidNS()
  {
    String testname = "ImportWSDLWithWSDLInvalidNS";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/ImportWSDLWithWSDLInvalidFilename.wsdl
   */
  public void testImportWSDLWithWSDLInvalidFilename()
  {
    String testname = "ImportWSDLWithWSDLInvalidFilename";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/WSDLImportingTypes.wsdl
   */
  public void testWSDLImportingTypes()
  {
    String testname = "WSDLImportingTypes";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/ImportNonExistantFile.wsdl
   */
  public void testImportNonExistantFile()
  {
    String testname = "ImportNonExistantFile";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/WSDLNamespaceAAA.wsdl
   */
  public void testWSDLNamespaceAAA()
  {
    String testname = "WSDLNamespaceAAA";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/ImportingWSDLWithImportedSchema.wsdl
   */
  public void testImportingWSDLWithImportedSchema()
  {
    String testname = "ImportingWSDLWithImportedSchema";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/WSDLImportingSchemaInmportingSchema.wsdl
   */
  public void testWSDLImportingSchemaInmportingSchema()
  {
    String testname = "WSDLImportingSchemaImportingSchema";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/ImportXMLInvalidWSDL/ImportXMLInvalidWSDL.wsdl
   */
  public void testImportXMLInvalidWSDL()
  {
    String testname = "ImportXMLInvalidWSDL";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/ImportXMLInvalidWSDL/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/ImportXMLInvalidWSDL/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/ImportXMLInvalidWSDL/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/AlphabeticalOrderOfImports/ImportOneAndTwo.wsdl
   */
  public void testImportOneAndTwo()
  {
    String testname = "ImportOneAndTwo";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/AlphabeticalOrderOfImports/one.wsdl
   */
  public void testImportOneAndTwo_One()
  {
    String testname = "one";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/AlphabeticalOrderOfImports/two.wsdl
   */
  public void testImportOneAndTwo_Two()
  {
    String testname = "two";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/ImportEmptyLocation/ImportEmptyLocation.wsdl
   */
  public void testImportEmptyLocation()
  {
    String testname = "ImportEmptyLocation";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/ImportEmptyLocation/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/ImportEmptyLocation/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/ImportEmptyLocation/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/ImportEmptyNamespace/ImportEmptyNamespace.wsdl
   */
  public void testImportEmptyNamespace()
  {
    String testname = "ImportEmptyNamespace";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/ImportEmptyNamespace/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/ImportEmptyNamespace/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/ImportEmptyNamespace/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/ImportEmptyNamespaceAndLocation/ImportEmptyNamespaceAndLocation.wsdl
   */
  public void testImportEmptyNamespaceAndLocation()
  {
    String testname = "ImportEmptyNamespaceAndLocation";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/ImportEmptyNamespaceAndLocation/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/ImportEmptyNamespaceAndLocation/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/ImportEmptyNamespaceAndLocation/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/ImportNoLocationAttribute/ImportNoLocationAttribute.wsdl
   */
  public void testImportNoLocationAttribute()
  {
    String testname = "ImportNoLocationAttribute";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/ImportNoLocationAttribute/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/ImportNoLocationAttribute/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/ImportNoLocationAttribute/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Import/SimpleImport/test-1.0.wsdl
   */
  public void testSimpleImport()
  {
    String testname = "test-1.0";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/SimpleImport/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/SimpleImport/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/SimpleImport/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * CYCLIC TESTS
   */
  
  /**
   * Test /WSDL/Cyclic/PorttypeRefMessage1.wsdl
   */
  public void testPorttypeRefMessage1()
  {
    String testname = "PorttypeRefMessage1";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Cyclic/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Cyclic/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Cyclic/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/Cyclic/PorttypeRefMessage2.wsdl
   */
  public void testPorttypeRefMessage2()
  {
    String testname = "PorttypeRefMessage2";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Cyclic/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Cyclic/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Cyclic/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/SelfContained/ReferenceInlineTypes.wsdl
   */
  public void testReferenceInlineTypes()
  {
    String testname = "ReferenceInlineTypes";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/SelfContained/Empty.wsdl
   */
  public void testEmpty()
  {
    String testname = "Empty";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/SelfContained/SOAPBodyEncodedNoNamespace.wsdl
   */
  public void testSOAPBodyEncodedNoNamespace()
  {
    String testname = "SOAPBodyEncodedNoNamespace";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/SelfContained/SOAPBodyEncodedWithNamespace.wsdl
   */
  public void testSOAPBodyEncodedWithNamespace()
  {
    String testname = "SOAPBodyEncodedWithNamespace";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/SelfContained/NoDefaultNamespace/NoDefaultNamespace.wsdl
   */
  public void testNoDefaultNamespace()
  {
    String testname = "NoDefaultNamespace";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "SelfContained/NoDefaultNamespace/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "SelfContained/NoDefaultNamespace/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "SelfContained/NoDefaultNamespace/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/BindingElement/InvalidStyle/BindingInvalidStyle.wsdl
   */
  public void testBindingInvalidStyle()
  {
    String testname = "BindingInvalidStyle";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "BindingElement/InvalidStyle/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "BindingElement/InvalidStyle/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "BindingElement/InvalidStyle/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/BindingElement/InvalidUse/BindingInvalidUse.wsdl
   */
  public void testBindingInvalidUse()
  {
    String testname = "BindingInvalidUse";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "BindingElement/InvalidUse/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "BindingElement/InvalidUse/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "BindingElement/InvalidUse/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/MessageElement/InvalidElement/MessageInvalidElement.wsdl
   */
  public void testMessageInvalidElement()
  {
    String testname = "MessageInvalidElement";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "MessageElement/InvalidElement/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "MessageElement/InvalidElement/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "MessageElement/InvalidElement/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/MessageElement/InvalidType/MessageInvalidType.wsdl
   */
  public void testMessageInvalidType()
  {
    String testname = "MessageInvalidType";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "MessageElement/InvalidType/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "MessageElement/InvalidType/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "MessageElement/InvalidType/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/PortTypeElement/InvalidInput/PortTypeInvalidInput.wsdl
   */
  public void testPortTypeInvalidInput()
  {
    String testname = "PortTypeInvalidInput";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "PortTypeElement/InvalidInput/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "PortTypeElement/InvalidInput/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "PortTypeElement/InvalidInput/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/PortTypeElement/InvalidOutput/PortTypeInvalidOutput.wsdl
   */
  public void testPortTypeInvalidOutput()
  {
    String testname = "PortTypeInvalidOutput";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "PortTypeElement/InvalidOutput/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "PortTypeElement/InvalidOutput/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "PortTypeElement/InvalidOutput/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/ServiceElement/InvalidBinding/ServiceInvalidBinding.wsdl
   */
  public void testServiceInvalidBinding()
  {
    String testname = "ServiceInvalidBinding";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "ServiceElement/InvalidBinding/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "ServiceElement/InvalidBinding/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "ServiceElement/InvalidBinding/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /WSDL/ServiceElement/NoAddress/ServiceNoAddress.wsdl
   */
  public void testServiceNoAddress()
  {
    String testname = "ServiceNoAddress";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "ServiceElement/NoAddress/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "ServiceElement/NoAddress/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "ServiceElement/NoAddress/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
}
