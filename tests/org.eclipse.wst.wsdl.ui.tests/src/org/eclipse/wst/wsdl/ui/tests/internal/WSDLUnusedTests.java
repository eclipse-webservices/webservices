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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.xsd.XSDSchemaDirective;

public class WSDLUnusedTests extends BaseTestCase
{
  /**
   * Create a tests suite from this test class.
   * 
   * @return A test suite containing this test class.
   */
  public static Test suite()
  {
    return new TestSuite(WSDLUnusedTests.class);
  }

  public void testUnusedImport001()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/Unused/test/Main1.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getUnusedImports();
    assertTrue(list.size() == 2);
    XSDSchemaDirective d1 = (XSDSchemaDirective) list.get(0);
    assertTrue("A.xsd".equals(d1.getSchemaLocation()));
    XSDSchemaDirective d2 = (XSDSchemaDirective) list.get(1);
    assertTrue("B.xsd".equals(d2.getSchemaLocation()));
  }

  public void testUnusedImport002()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/Unused/test/Main2.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getUnusedImports();
    assertTrue(list.size() == 2);
    XSDSchemaDirective d1 = (XSDSchemaDirective) list.get(0);
    assertTrue("A.xsd".equals(d1.getSchemaLocation()));
    XSDSchemaDirective d2 = (XSDSchemaDirective) list.get(1);
    assertTrue("B.xsd".equals(d2.getSchemaLocation()));
  }

  public void testUnusedImport003()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/Unused/test/Main3.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getUnusedImports();
    assertTrue(list.size() == 2);
    XSDSchemaDirective d1 = (XSDSchemaDirective) list.get(0);
    assertTrue("A.xsd".equals(d1.getSchemaLocation()));
    XSDSchemaDirective d2 = (XSDSchemaDirective) list.get(1);
    assertTrue("B.xsd".equals(d2.getSchemaLocation()));
  }

  public void testUnusedImport004()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/Unused/test/Main4.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getUnusedImports();
    assertTrue(list.size() == 7);
    XSDSchemaDirective d1 = (XSDSchemaDirective) list.get(0);
    assertTrue("A.xsd".equals(d1.getSchemaLocation()));
    XSDSchemaDirective d2 = (XSDSchemaDirective) list.get(1);
    assertTrue("B.xsd".equals(d2.getSchemaLocation()));
    XSDSchemaDirective d3 = (XSDSchemaDirective) list.get(2);
    assertTrue("C.xsd".equals(d3.getSchemaLocation()));
    XSDSchemaDirective d4 = (XSDSchemaDirective) list.get(3);
    assertTrue("C.xsd".equals(d4.getSchemaLocation()));
    XSDSchemaDirective d5 = (XSDSchemaDirective) list.get(4);
    assertTrue("D.xsd".equals(d5.getSchemaLocation()));
    XSDSchemaDirective d6 = (XSDSchemaDirective) list.get(5);
    assertTrue("E.xsd".equals(d6.getSchemaLocation()));
    XSDSchemaDirective d7 = (XSDSchemaDirective) list.get(6);
    assertTrue("F.xsd".equals(d7.getSchemaLocation()));
  }

  public void testUnusedImport005()
  {
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/Unused/test/Main5.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getUnusedImports();
    assertTrue(list.size() == 1);
    XSDSchemaDirective d1 = (XSDSchemaDirective) list.get(0);
    assertTrue("../Import2.xsd".equals(d1.getSchemaLocation()));
  }

  public void testUnusedImport006()
  {
    /* duplicate unused imports in two inline schemas */
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/Unused/test/Main6.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getUnusedImports();
    assertTrue(list.size() == 2);
    XSDSchemaDirective d1 = (XSDSchemaDirective) list.get(0);
    assertTrue("../Import2.xsd".equals(d1.getSchemaLocation()));
    XSDSchemaDirective d2 = (XSDSchemaDirective) list.get(1);
    assertTrue("../Import2.xsd".equals(d2.getSchemaLocation()));
  }

  public void testUnusedImport007()
  {
    /* duplicate unused imports in two inline schemas */
    Definition definition = getDefinition(TC_ROOT_FOLDER + "/Unused/test/Main7.wsdl");
    importManager.performRemoval(definition);
    List list = importManager.getUnusedImports();
    assertTrue(list.size() == 1);
    XSDSchemaDirective d1 = (XSDSchemaDirective) list.get(0);
    assertTrue("../Import3.xsd".equals(d1.getSchemaLocation()));
  }
}