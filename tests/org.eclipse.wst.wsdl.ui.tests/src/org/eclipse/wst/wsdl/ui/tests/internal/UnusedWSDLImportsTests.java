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
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;

public class UnusedWSDLImportsTests extends BaseTestCase
{
  /**
   * Create a tests suite from this test class.
   * 
   * @return A test suite containing this test class.
   */
  public static Test suite()
  {
    return new TestSuite(UnusedWSDLImportsTests.class);
  }

//
// Testing USED WSDL imports (That they aren't removed mistakenly)
//

  public void testUsedWSDLImport001()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/core/Main002.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 0);
    importManager.cleanup();
  }

  public void testUsedWSDLImport002()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/core/Main004.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 0);
  }

  public void testUsedWSDLImport003()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/core/Main006.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 0);
  }

  public void testUsedWSDLImport004()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/core/Main008.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 0);
  }

  //
// Testing of UNUSED WSDL imports
//

  public void testUnusedWSDLImport001()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/core/Main001.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 1);
    Import imp1 = (Import) list.get(0);
    assertTrue(imp1.getNamespaceURI().equals("http://www.example.org/Import001/"));
    assertTrue(imp1.getLocationURI().equals("Import001.wsdl"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 1);
    assertTrue(unusedWSDLPrefixes.contains("wsdl1"));

  }

  public void testUnusedWSDLImport002()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/core/Main003.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 1);
    Import imp1 = (Import) list.get(0);
    assertTrue(imp1.getNamespaceURI().equals("http://www.example.org/Import001/"));
    assertTrue(imp1.getLocationURI().equals("Import001.wsdl"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 1);
    assertTrue(unusedWSDLPrefixes.contains("wsdl1"));
  }

  public void testUnusedWSDLImport003()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/core/Main005.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 1);
    Import imp1 = (Import) list.get(0);
    assertTrue(imp1.getNamespaceURI().equals("http://www.example.org/Import001/"));
    assertTrue(imp1.getLocationURI().equals("Import001.wsdl"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 1);
    assertTrue(unusedWSDLPrefixes.contains("wsdl1"));
  }

  public void testUnusedWSDLImport004()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/core/Main007.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 1);
    Import imp1 = (Import) list.get(0);
    assertTrue(imp1.getNamespaceURI().equals("http://www.example.org/Import001/"));
    assertTrue(imp1.getLocationURI().equals("Import001.wsdl"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 1);
    assertTrue(unusedWSDLPrefixes.contains(null));
  }

  public void testUnusedWSDLImport005()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/core/Main009.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 1);
    Import imp1 = (Import) list.get(0);
    assertTrue(imp1.getNamespaceURI().equals("http://www.example.org/FileNotFound/"));
    assertTrue(imp1.getLocationURI().equals("WSDLFileNotFound.wsdl"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 0);
  }

  public void testUnusedWSDLImport006()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/test/Main001.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 2);
    Import imp1 = (Import) list.get(0);
    assertTrue(imp1.getNamespaceURI().equals("http://www.example.org/Import001/"));
    assertTrue(imp1.getLocationURI().equals("Import001.wsdl"));
    Import imp2 = (Import) list.get(1);
    assertTrue(imp2.getNamespaceURI().equals("http://www.example.org/Import002/"));
    assertTrue(imp2.getLocationURI().equals("Import002.wsdl"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 2);
    assertTrue(unusedWSDLPrefixes.contains("wsdl1"));
    assertTrue(unusedWSDLPrefixes.contains("wsdl2"));
  }

  public void testMixWSDLImport001()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/test/Main002.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 4);
    Import imp1 = (Import) list.get(0);
    assertTrue(imp1.getNamespaceURI().equals("http://www.example.org/Import001/"));
    assertTrue(imp1.getLocationURI().equals("Import001.wsdl"));
    Import imp2 = (Import) list.get(1);
    assertTrue(imp2.getNamespaceURI().equals("http://www.example.org/Import002/"));
    assertTrue(imp2.getLocationURI().equals("Import002.wsdl"));
    Import imp3 = (Import) list.get(2);
    assertTrue(imp3.getNamespaceURI().equals("http://www.example.org/Import"));
    assertTrue(imp3.getLocationURI().equals("../../../XSD/Unused/Import1.xsd"));
    Import imp4 = (Import) list.get(3);
    assertTrue(imp4.getNamespaceURI().equals("http://www.example.org/Import6"));
    assertTrue(imp4.getLocationURI().equals("../../../XSD/Unused/Import6.xsd"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 4);
    assertTrue(unusedWSDLPrefixes.contains("wsdl1"));
    assertTrue(unusedWSDLPrefixes.contains("wsdl2"));
    assertTrue(unusedWSDLPrefixes.contains("xsd1"));
    assertTrue(unusedWSDLPrefixes.contains("xsd2"));
  }

  public void testMixWSDLImport002()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/test/Main003.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 2);
    Import imp1 = (Import) list.get(0);
    assertTrue(imp1.getNamespaceURI().equals("http://www.example.org/Import001/"));
    assertTrue(imp1.getLocationURI().equals("Import001.wsdl"));
    Import imp2 = (Import) list.get(1);
    assertTrue(imp2.getNamespaceURI().equals("http://www.example.org/Import002/"));
    assertTrue(imp2.getLocationURI().equals("Import002.wsdl"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 2);
    assertTrue(unusedWSDLPrefixes.contains("wsdl1"));
    assertTrue(unusedWSDLPrefixes.contains("wsdl2"));
  }

  public void testMixWSDLImport003()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/test/Main004.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 2);
    Import imp1 = (Import) list.get(0);
    assertTrue(imp1.getNamespaceURI().equals("http://www.example.org/Import001/"));
    assertTrue(imp1.getLocationURI().equals("Import001.wsdl"));
    Import imp2 = (Import) list.get(1);
    assertTrue(imp2.getNamespaceURI().equals("http://www.example.org/Import002/"));
    assertTrue(imp2.getLocationURI().equals("Import002.wsdl"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 2);
    assertTrue(unusedWSDLPrefixes.contains("wsdl1"));
    assertTrue(unusedWSDLPrefixes.contains("wsdl2"));
  }

  public void testMixWSDLImport004()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/test/Main005.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 0);

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 0);
  }

  public void testMixWSDLImport005()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/WSDLImports/test/Main006.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getWSDLUnusedImports();
    assertTrue(list.size() == 2);
    Import imp1 = (Import) list.get(0);
    assertTrue(imp1.getNamespaceURI().equals("http://www.example.org/Import"));
    assertTrue(imp1.getLocationURI().equals("../../../XSD/Unused/Import1.xsd"));
    Import imp2 = (Import) list.get(1);
    assertTrue(imp2.getNamespaceURI().equals("http://www.example.org/Import6"));
    assertTrue(imp2.getLocationURI().equals("../../../XSD/Unused/Import6.xsd"));

    Set unusedWSDLPrefixes = importManager.getUnusedWSDLPrefixes();
    assertTrue(unusedWSDLPrefixes.size() == 2);
    assertTrue(unusedWSDLPrefixes.contains("xsd1"));
    assertTrue(unusedWSDLPrefixes.contains("xsd2"));
  }

}
