/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.tests.internal;

import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.xsd.XSDSchema;

public class WSDLXMLNSCleanupTests extends BaseTestCase
{
  /**
   * Create a tests suite from this test class.
   * 
   * @return A test suite containing this test class.
   */
  public static Test suite()
  {
    return new TestSuite(WSDLXMLNSCleanupTests.class);
  }

  public void testCleanup001()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/xmlnsCleanup/test/Main001.wsdl");
    importManager.performRemoval(definition);

    List extElements = definition.getETypes().getEExtensibilityElements();

    Map map = (Map) importManager.getSchemaToPrefixMap();
    Set schemas = map.keySet();
    // Two inline schemas
    assertTrue(schemas.size() == 2);
    assertTrue(extElements.size() == 2);

    XSDSchemaExtensibilityElement xsdEE1 = (XSDSchemaExtensibilityElement) extElements.get(0);
    XSDSchema inlineSchema01 = xsdEE1.getSchema();
    Set unusedPrefixes01 = (Set) map.get(inlineSchema01);
    assertTrue(unusedPrefixes01.size() == 2);
    assertTrue(unusedPrefixes01.contains("tns03"));
    assertTrue(unusedPrefixes01.contains("tns04"));

    XSDSchemaExtensibilityElement xsdEE2 = (XSDSchemaExtensibilityElement) extElements.get(1);
    XSDSchema inlineSchema02 = xsdEE2.getSchema();
    Set unusedPrefixes02 = (Set) map.get(inlineSchema02);
    assertTrue(unusedPrefixes02.size() == 2);
    assertTrue(unusedPrefixes02.contains("tns05"));
    assertTrue(unusedPrefixes02.contains("tns06"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 2);
    assertTrue(unusedWSDLPrefixes.contains("tns01"));
    assertTrue(unusedWSDLPrefixes.contains("tns02"));
  }

  public void testCleanup002()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/xmlnsCleanup/test/Main002.wsdl");
    importManager.performRemoval(definition);

    List extElements = definition.getETypes().getEExtensibilityElements();

    Map map = (Map) importManager.getSchemaToPrefixMap();
    Set schemas = map.keySet();
    // One inline schema
    assertTrue(schemas.size() == 1);
    assertTrue(extElements.size() == 1);

    XSDSchemaExtensibilityElement xsdEE1 = (XSDSchemaExtensibilityElement) extElements.get(0);
    XSDSchema inlineSchema01 = xsdEE1.getSchema();
    Set unusedPrefixes01 = (Set) map.get(inlineSchema01);
    assertTrue(unusedPrefixes01.size() == 1);
    assertTrue(unusedPrefixes01.contains("tns02"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 1);
    assertTrue(unusedWSDLPrefixes.contains("tns01"));
  }

  public void testCleanup003()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/xmlnsCleanup/test/Main003.wsdl");
    importManager.performRemoval(definition);

    List extElements = definition.getETypes().getEExtensibilityElements();

    Map map = (Map) importManager.getSchemaToPrefixMap();
    Set schemas = map.keySet();
    // One inline schema
    assertTrue(schemas.size() == 1);
    assertTrue(extElements.size() == 1);

    XSDSchemaExtensibilityElement xsdEE1 = (XSDSchemaExtensibilityElement) extElements.get(0);
    XSDSchema inlineSchema01 = xsdEE1.getSchema();
    Set unusedPrefixes01 = (Set) map.get(inlineSchema01);
    assertTrue(unusedPrefixes01.size() == 1);
    assertTrue(unusedPrefixes01.contains("tns02"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 0);
  }

  public void testCleanup004()
  {
    // Tests null used prefix for WSDL target namespace

    Definition definition = getDefinition(TC_ROOT_FOLDER + "/xmlnsCleanup/test/Main004.wsdl");
    importManager.performRemoval(definition);

    List extElements = definition.getETypes().getEExtensibilityElements();

    Map map = (Map) importManager.getSchemaToPrefixMap();
    Set schemas = map.keySet();
    // One inline schema
    assertTrue(schemas.size() == 1);
    assertTrue(extElements.size() == 1);

    XSDSchemaExtensibilityElement xsdEE1 = (XSDSchemaExtensibilityElement) extElements.get(0);
    XSDSchema inlineSchema01 = xsdEE1.getSchema();
    Set unusedPrefixes01 = (Set) map.get(inlineSchema01);
    assertTrue(unusedPrefixes01.size() == 0);

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 0);
  }

  public void testCleanup005()
  {
    // Test null unused prefix in WSDL xmlns table
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/xmlnsCleanup/test/Main005.wsdl");
    importManager.performRemoval(definition);

    List extElements = definition.getETypes().getEExtensibilityElements();

    Map map = (Map) importManager.getSchemaToPrefixMap();
    Set schemas = map.keySet();
    // One inline schema
    assertTrue(schemas.size() == 1);
    assertTrue(extElements.size() == 1);

    XSDSchemaExtensibilityElement xsdEE1 = (XSDSchemaExtensibilityElement) extElements.get(0);
    XSDSchema inlineSchema01 = xsdEE1.getSchema();
    Set unusedPrefixes01 = (Set) map.get(inlineSchema01);
    assertTrue(unusedPrefixes01.size() == 1);
    assertTrue(unusedPrefixes01.contains("tns02"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 1);
    assertTrue(unusedWSDLPrefixes.contains(null));
  }

  public void testCleanup006()
  {
    // Tests removal of soap xmlns entry in WSDL ns table

    Definition definition = getDefinition(TC_ROOT_FOLDER + "/xmlnsCleanup/test/Main006.wsdl");
    importManager.performRemoval(definition);

    List extElements = definition.getETypes().getEExtensibilityElements();

    Map map = (Map) importManager.getSchemaToPrefixMap();
    Set schemas = map.keySet();
    // One inline schema
    assertTrue(schemas.size() == 1);
    assertTrue(extElements.size() == 1);

    XSDSchemaExtensibilityElement xsdEE1 = (XSDSchemaExtensibilityElement) extElements.get(0);
    XSDSchema inlineSchema01 = xsdEE1.getSchema();

    Set unusedPrefixes01 = (Set) map.get(inlineSchema01);
    assertTrue(unusedPrefixes01.size() == 0);

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 1);
    assertTrue(unusedWSDLPrefixes.contains("soap"));
  }

  public void testCleanup007()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/xmlnsCleanup/test/Main007.wsdl");
    importManager.performRemoval(definition);

    List extElements = definition.getETypes().getEExtensibilityElements();

    Map map = (Map) importManager.getSchemaToPrefixMap();
    Set schemas = map.keySet();
    // Five inline schemas
    assertTrue(schemas.size() == 6);
    assertTrue(extElements.size() == 6);

    XSDSchemaExtensibilityElement xsdEE1 = (XSDSchemaExtensibilityElement) extElements.get(0);
    XSDSchema inlineSchema01 = xsdEE1.getSchema();
    Set unusedPrefixes01 = (Set) map.get(inlineSchema01);
    assertTrue(unusedPrefixes01.size() == 0);

    XSDSchemaExtensibilityElement xsdEE2 = (XSDSchemaExtensibilityElement) extElements.get(1);
    XSDSchema inlineSchema02 = xsdEE2.getSchema();
    Set unusedPrefixes02 = (Set) map.get(inlineSchema02);
    assertTrue(unusedPrefixes02.size() == 0);

    XSDSchemaExtensibilityElement xsdEE3 = (XSDSchemaExtensibilityElement) extElements.get(2);
    XSDSchema inlineSchema03 = xsdEE3.getSchema();
    Set unusedPrefixes03 = (Set) map.get(inlineSchema03);
    assertTrue(unusedPrefixes03.size() == 0);

    XSDSchemaExtensibilityElement xsdEE4 = (XSDSchemaExtensibilityElement) extElements.get(3);
    XSDSchema inlineSchema04 = xsdEE4.getSchema();
    Set unusedPrefixes04 = (Set) map.get(inlineSchema04);
    assertTrue(unusedPrefixes04.size() == 1);
    assertTrue(unusedPrefixes04.contains(null));

    XSDSchemaExtensibilityElement xsdEE5 = (XSDSchemaExtensibilityElement) extElements.get(4);
    XSDSchema inlineSchema05 = xsdEE5.getSchema();
    Set unusedPrefixes05 = (Set) map.get(inlineSchema05);
    assertTrue(unusedPrefixes05.size() == 1);
    assertTrue(unusedPrefixes05.contains("unused01"));

    XSDSchemaExtensibilityElement xsdEE6 = (XSDSchemaExtensibilityElement) extElements.get(5);
    XSDSchema inlineSchema06 = xsdEE6.getSchema();
    Set unusedPrefixes06 = (Set) map.get(inlineSchema06);
    assertTrue(unusedPrefixes06.size() == 1);
    assertTrue(unusedPrefixes06.contains("unused01"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 0);
  }

  public void testCleanup008()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/xmlnsCleanup/test/Main008.wsdl");
    importManager.performRemoval(definition);

    List extElements = definition.getETypes().getEExtensibilityElements();

    Map map = (Map) importManager.getSchemaToPrefixMap();
    Set schemas = map.keySet();
    // Five inline schemas
    assertTrue(schemas.size() == 6);
    assertTrue(extElements.size() == 6);

    XSDSchemaExtensibilityElement xsdEE1 = (XSDSchemaExtensibilityElement) extElements.get(0);
    XSDSchema inlineSchema01 = xsdEE1.getSchema();
    Set unusedPrefixes01 = (Set) map.get(inlineSchema01);
    assertTrue(unusedPrefixes01.size() == 0);

    XSDSchemaExtensibilityElement xsdEE2 = (XSDSchemaExtensibilityElement) extElements.get(1);
    XSDSchema inlineSchema02 = xsdEE2.getSchema();
    Set unusedPrefixes02 = (Set) map.get(inlineSchema02);
    assertTrue(unusedPrefixes02.size() == 0);

    XSDSchemaExtensibilityElement xsdEE3 = (XSDSchemaExtensibilityElement) extElements.get(2);
    XSDSchema inlineSchema03 = xsdEE3.getSchema();
    Set unusedPrefixes03 = (Set) map.get(inlineSchema03);
    assertTrue(unusedPrefixes03.size() == 0);

    XSDSchemaExtensibilityElement xsdEE4 = (XSDSchemaExtensibilityElement) extElements.get(3);
    XSDSchema inlineSchema04 = xsdEE4.getSchema();
    Set unusedPrefixes04 = (Set) map.get(inlineSchema04);
    assertTrue(unusedPrefixes04.size() == 1);
    assertTrue(unusedPrefixes04.contains(null));

    XSDSchemaExtensibilityElement xsdEE5 = (XSDSchemaExtensibilityElement) extElements.get(4);
    XSDSchema inlineSchema05 = xsdEE5.getSchema();
    Set unusedPrefixes05 = (Set) map.get(inlineSchema05);
    assertTrue(unusedPrefixes05.size() == 1);
    assertTrue(unusedPrefixes05.contains("unused01"));

    XSDSchemaExtensibilityElement xsdEE6 = (XSDSchemaExtensibilityElement) extElements.get(5);
    XSDSchema inlineSchema06 = xsdEE6.getSchema();
    Set unusedPrefixes06 = (Set) map.get(inlineSchema06);
    assertTrue(unusedPrefixes06.size() == 1);
    assertTrue(unusedPrefixes06.contains("unused01"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 3);
    assertTrue(unusedWSDLPrefixes.contains(null));
    assertTrue(unusedWSDLPrefixes.contains("wsdlUsed"));
    assertTrue(unusedWSDLPrefixes.contains("used01")); // used in xsd but unused
                                                       // in WSDL
  }

  // Using the test WSDLs from the Unused folder
  public void testCleanup009()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/Unused/test/Main1.wsdl");
    importManager.performRemoval(definition);

    List extElements = definition.getETypes().getEExtensibilityElements();

    Map map = (Map) importManager.getSchemaToPrefixMap();
    Set schemas = map.keySet();
    // Two inline schemas
    assertTrue(schemas.size() == 2);
    assertTrue(extElements.size() == 2);

    XSDSchemaExtensibilityElement xsdEE1 = (XSDSchemaExtensibilityElement) extElements.get(0);
    XSDSchema inlineSchema01 = xsdEE1.getSchema();
    Set unusedPrefixes01 = (Set) map.get(inlineSchema01);
    assertTrue(unusedPrefixes01.size() == 0);

    XSDSchemaExtensibilityElement xsdEE2 = (XSDSchemaExtensibilityElement) extElements.get(1);
    XSDSchema inlineSchema02 = xsdEE2.getSchema();
    Set unusedPrefixes02 = (Set) map.get(inlineSchema02);
    assertTrue(unusedPrefixes02.size() == 0);

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 0);
  }

  // Using the test WSDLs from the Unused folder
  public void testCleanup010()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/Unused/test/Main5.wsdl");
    importManager.performRemoval(definition);

    List extElements = definition.getETypes().getEExtensibilityElements();

    Map map = (Map) importManager.getSchemaToPrefixMap();
    Set schemas = map.keySet();
    // Two inline schemas
    assertTrue(schemas.size() == 2);
    assertTrue(extElements.size() == 2);

    XSDSchemaExtensibilityElement xsdEE1 = (XSDSchemaExtensibilityElement) extElements.get(0);
    XSDSchema inlineSchema01 = xsdEE1.getSchema();
    Set unusedPrefixes01 = (Set) map.get(inlineSchema01);
    assertTrue(unusedPrefixes01.size() == 0);

    XSDSchemaExtensibilityElement xsdEE2 = (XSDSchemaExtensibilityElement) extElements.get(1);
    XSDSchema inlineSchema02 = xsdEE2.getSchema();
    Set unusedPrefixes02 = (Set) map.get(inlineSchema02);
    assertTrue(unusedPrefixes02.size() == 0);

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 0);
  }

  // Using the test WSDLs from the Unused folder
  public void testCleanup011()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/Unused/test/Main7.wsdl");
    importManager.performRemoval(definition);

    List extElements = definition.getETypes().getEExtensibilityElements();

    Map map = (Map) importManager.getSchemaToPrefixMap();
    Set schemas = map.keySet();
    // Two inline schemas
    assertTrue(schemas.size() == 2);
    assertTrue(extElements.size() == 2);

    XSDSchemaExtensibilityElement xsdEE1 = (XSDSchemaExtensibilityElement) extElements.get(0);
    XSDSchema inlineSchema01 = xsdEE1.getSchema();
    Set unusedPrefixes01 = (Set) map.get(inlineSchema01);
    assertTrue(unusedPrefixes01.size() == 0);

    XSDSchemaExtensibilityElement xsdEE2 = (XSDSchemaExtensibilityElement) extElements.get(1);
    XSDSchema inlineSchema02 = xsdEE2.getSchema();
    Set unusedPrefixes02 = (Set) map.get(inlineSchema02);
    assertTrue(unusedPrefixes02.size() == 0);

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 1);
    assertTrue(unusedWSDLPrefixes.contains("xsd1"));
  }

}