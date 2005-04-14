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
 * XSD validation tests for the WSDL validator.
 */
public class XSDTest extends BaseTestCase
{
  private String XSD_DIR = "XSD/";
  
  /**
   * Create a tests suite from this test class.
   * 
   * @return A test suite containing this test class.
   */
  public static Test suite()
  {
    return new TestSuite(XSDTest.class);
  }
  
  /* (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp()
  {
    super.setUp();
  }
  
  /**
   * Test /XSD/Entities/normalizeEntitiesValid.wsdl
   */
  public void testNormalizeEntitiesValid()
  {
    String testname = "normalizeEntitiesValid";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + XSD_DIR + "Entities/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + XSD_DIR + "Entities/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + XSD_DIR + "Entities/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /XSD/Entities/normalizeEntitiesInvalid.wsdl
   */
  public void testNormalizeEntitiesInvalid()
  {
    String testname = "normalizeEntitiesInvalid";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + XSD_DIR + "Entities/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + XSD_DIR + "Entities/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + XSD_DIR + "Entities/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /XSD/ReferToAnotherInlineType/ReferToAnotherInlineType.wsdl
   */
  public void testReferToAnotherInlineType()
  {
    String testname = "ReferToAnotherInlineType";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + XSD_DIR + "ReferToAnotherInlineType/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + XSD_DIR + "ReferToAnotherInlineType/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + XSD_DIR + "ReferToAnotherInlineType/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /XSD/InlineSchemaGeneratedImports/NoImportForUnprefixedAttribute.wsdl
   */
  public void testNoImportForUnprefixedAttribute()
  {
    String testname = "NoImportForUnprefixedAttribute";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + XSD_DIR + "InlineSchemaGeneratedImports/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + XSD_DIR + "InlineSchemaGeneratedImports/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + XSD_DIR + "InlineSchemaGeneratedImports/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /XSD/OneErrorForInlineXSDRefInvalidInlineXSD/OneErrorForInlineXSDRefInvalidInlineXSD.wsdl
   */
  public void testOneErrorForInlineXSDRefInvalidInlineXSD()
  {
    String testname = "OneErrorForInlineXSDRefInvalidInlineXSD";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + XSD_DIR + "OneErrorForInlineXSDRefInvalidInlineXSD/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + XSD_DIR + "OneErrorForInlineXSDRefInvalidInlineXSD/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + XSD_DIR + "OneErrorForInlineXSDRefInvalidInlineXSD/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /XSD/SchemaDocElemWithSourceAndSubElem/SchemaDocElemWithSourceAndSubElem.wsdl
   */
  public void testSchemaDocElemWithSourceAndSubElem()
  {
    String testname = "SchemaDocElemWithSourceAndSubElem";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + XSD_DIR + "SchemaDocElemWithSourceAndSubElem/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + XSD_DIR + "SchemaDocElemWithSourceAndSubElem/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + XSD_DIR + "SchemaDocElemWithSourceAndSubElem/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /XSD/RestrictionPatternWithColon/RestrictionPatternWithColon.wsdl
   */
  public void testRestrictionPatternWithColon()
  {
    String testname = "RestrictionPatternWithColon";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + XSD_DIR + "RestrictionPatternWithColon/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + XSD_DIR + "RestrictionPatternWithColon/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + XSD_DIR + "RestrictionPatternWithColon/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /XSD/ImportInlineSchemaWithInclude/ImportInlineSchemaWithInclude.wsdl
   */
  public void testImportInlineSchemaWithInclude()
  {
    String testname = "ImportInlineSchemaWithInclude";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + XSD_DIR + "ImportInlineSchemaWithInclude/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + XSD_DIR + "ImportInlineSchemaWithInclude/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + XSD_DIR + "ImportInlineSchemaWithInclude/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
  
  /**
   * Test /XSD/ValueColon/ValueColon.wsdl
   */
  public void testValueColon()
  {
    String testname = "ValueColon";
    String testfile = FILE_PROTOCOL + PLUGIN_ABSOLUTE_PATH + SAMPLES_DIR + XSD_DIR + "ValueColon/" + testname + ".wsdl";
    String loglocation = PLUGIN_ABSOLUTE_PATH + GENERATED_RESULTS_DIR + XSD_DIR + "ValueColon/" + testname + ".wsdl-log";
    String idealloglocation = PLUGIN_ABSOLUTE_PATH + IDEAL_RESULTS_DIR + XSD_DIR + "ValueColon/" + testname + ".wsdl-log";
    
    runTest(testfile, loglocation, idealloglocation);
  }
}
