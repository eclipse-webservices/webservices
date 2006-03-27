/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests;

import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.wst.wsdl.util.WSDLParser;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDParser;
import org.w3c.dom.Element;

/**
 * Test class used to validate the WSDL model source location tracking
 * mechanism.
 */
public class LocationTrackingTest extends TestCase
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite();
    suite.addTest(new LocationTrackingTest()
    {
      protected void runTest()
      {
        testTracksLocation();
      }
    });
    return suite;
  }

  /**
   * Tests the location tracking mechanism provided by the WSDL model resource
   * loader.
   * 
   * @see WSDLResourceImpl
   * @see WSDLParser
   */
  public void testTracksLocation()
  {
    try
    {
      String fileName = WSDLTestsPlugin.getInstallURL() + "/samples/LoadAndPrintTest.wsdl"; //$NON-NLS-1$
      Definition definition = DefinitionLoader.load(fileName, true, true);
      Assert.assertNotNull(definition);
      Assert.assertTrue(definition.eResource() instanceof WSDLResourceImpl);

      Element definitionElement = definition.getElement();
      assertEquals(1, WSDLParser.getStartLine(definitionElement));

      Types types = definition.getETypes();
      Element typesElement = types.getElement();
      assertEquals(4, WSDLParser.getStartLine(typesElement));

      List typesExtensibilityElements = types.getEExtensibilityElements();

      assertEquals(1, typesExtensibilityElements.size());

      XSDSchemaExtensibilityElement schemaExtension = (XSDSchemaExtensibilityElement) typesExtensibilityElements.get(0);

      XSDSchema schema = schemaExtension.getSchema();

      Element schemaElement = schema.getElement();

      assertEquals(5, XSDParser.getStartLine(schemaElement));

      XSDElementDeclaration requestElementDeclaration = schema.resolveElementDeclaration("NewOperationRequest"); //$NON-NLS-1$

      Element requestElement = requestElementDeclaration.getElement();

      assertEquals(7, XSDParser.getStartLine(requestElement));

      List services = definition.getEServices();
      assertEquals(1, services.size());
      Service service = (Service) services.get(0);

      Element serviceElement = service.getElement();
      assertEquals(42, WSDLParser.getStartLine(serviceElement));
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }
}
