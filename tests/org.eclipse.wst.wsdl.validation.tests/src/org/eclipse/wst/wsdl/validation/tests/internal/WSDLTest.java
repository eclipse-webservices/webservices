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
 * 
 * @author Lawrence Mandel, IBM
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/ImportXMLInvalidWSDL/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/ImportXMLInvalidWSDL/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/ImportXMLInvalidWSDL/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  

  /**
   * Test /WSDL/Import/ImportingUsingClasspath.wsdl
   */
//  public void testImportingUsingClasspath()
//  {
//    final IWorkspace workspace = ResourcesPlugin.getWorkspace();
//    String testname = "ImportingUsingClasspath";
//    String importingfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + "ImportingUsingClasspath.wsdl";
//    importingfile = importingfile.replace('/','\\');
//    importingfile = importingfile.substring(5);
//    String importedfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/" + "ImportedUsingClasspath.wsdl";
//    importedfile = importedfile.replace('/','\\');
//    importedfile = importedfile.substring(5);
//    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
//    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/" + testname + ".wsdl-log";
//    
//    String projectName = "Project1";
//    String projectName2 = "Project2";
//    // Create a simple project for ImportedSchemaWithSchemaImport test.
//    IProject project2 = createProject(projectName2, new String[]{importedfile},new String[]{"org.eclipse.jdt.core.javanature", "com.ibm.wtp.jca.ConnectorNature"}, null);
//    IProject project1 = createProject(projectName, new String[]{importingfile},new String[]{"org.eclipse.jdt.core.javanature", "com.ibm.wtp.jca.ConnectorNature"}, new IProject[]{project2});
//    
//    String testfile = "file:/" + project1.getFile("ImportingUsingClasspath.wsdl").getLocation().toString();
//    runTest(testfile, loglocation, idealloglocation);
//    
//    try
//    {
//      project1.delete(true, true, null);
//      project2.delete(true, true, null);
//    }
//    catch(Exception e)
//    {
//    }
//  }
  
  /**
   * Test /WSDL/Import/AlphabeticalOrderOfImports/ImportOneAndTwo.wsdl
   */
  public void testImportOneAndTwo()
  {
    String testname = "ImportOneAndTwo";
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "Import/AlphabeticalOrderOfImports/" + testname + ".wsdl-log";
    
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Cyclic/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "Cyclic/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "SelfContained/" + testname + ".wsdl";
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
    String testfile = PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + WSDL_DIR + "SelfContained/NoDefaultNamespace/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + WSDL_DIR + "SelfContained/NoDefaultNamespace/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + WSDL_DIR + "SelfContained/NoDefaultNamespace/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
}
