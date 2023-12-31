/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.tests;


import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLPlugin;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeader;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.wst.wsdl.util.ExtensibilityElementFactory;
import org.eclipse.wst.wsdl.util.ExtensibilityElementFactoryRegistry;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class UtilTest extends TestCase
{
  private String PLUGIN_ABSOLUTE_PATH = WSDLTestsPlugin.getInstallURL();

  public UtilTest(String name)
  {
    super(name);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(new UtilTest("WSDLConstants")
      {
        protected void runTest()
        {
          testConstants();
        }
      });

    suite.addTest(new UtilTest("WSDLResourceFactoryImpl")
      {
        protected void runTest()
        {
          testWSDLResourceFactoryImpl();
        }
      });

    suite.addTest(new UtilTest("WSDLResourceImpl")
      {
        protected void runTest()
        {
          testWSDLResourceImpl();
        }
      });
    suite.addTest(new UtilTest("ExtensibilityElementFactory")
      {
        protected void runTest()
        {
          testExtensibilityElementFactory();
        }
      });
    suite.addTest(new UtilTest("ExtensibilityElementFactoryRegistry")
      {
        protected void runTest()
        {
          testExtensibilityElementFactoryRegistry();
        }
      });

    return suite;
  }

  static private Definition definition = null;

  public void testConstants()
  {
    try
    {
      int type = WSDLConstants.nodeType(WSDLConstants.PORT_ELEMENT_TAG);
      Assert.assertTrue("Node type is not that of port", type == 10);

      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/LoadStoreCompare/LoadAndPrintTest.wsdl");
      Assert.assertTrue(definition.eResource() instanceof WSDLResourceImpl);

      int definitionNodeType = WSDLConstants.nodeType(definition.getElement());
      Assert.assertTrue("Node type is not that of definition", definitionNodeType == 1);

      boolean isWSDLNamespace = WSDLConstants.isWSDLNamespace(definition.getTargetNamespace());
      Assert.assertFalse("This should not be the WSDL Namespace", isWSDLNamespace);

      boolean isMatching = WSDLConstants.isMatchingNamespace("http://www.example.org", "http://www.example.org");
      Assert.assertTrue("isMatchingNamespace is incorrect", isMatching);

      String attr = WSDLConstants.getAttribute(definition.getElement(), "name");
      Assert.assertTrue("getAttribute is incorrect", attr.equals("LoadAndPrintTest"));
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }

  public void testWSDLResourceFactoryImpl()
  {
    try
    {
      WSDLResourceFactoryImpl factoryImpl = new WSDLResourceFactoryImpl();
      Resource resource = factoryImpl.createResource(URI.createFileURI("./samples/createResourceTest.wsdl"));
      Assert.assertTrue("Resource is not of type WSDLResourceImpl", resource instanceof WSDLResourceImpl);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }

  public void testWSDLResourceImpl()
  {
    try
    {
      WSDLResourceImpl resourceImpl = new WSDLResourceImpl(URI.createFileURI("./samples/createResourceTest.wsdl"));
      Assert.assertTrue("Resource is not of type WSDLResourceImpl", resourceImpl instanceof WSDLResourceImpl);

      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/LoadStoreCompare/LoadAndPrintTest.wsdl");
      Assert.assertTrue(definition.eResource() instanceof WSDLResourceImpl);
      Element element = definition.getElement();
      Document document = definition.getDocument();

      if (element != null)
      {
        WSDLResourceImpl.serialize(System.out, element, null);
      }

      if (document != null)
      {
        WSDLResourceImpl.serialize(System.out, document, null);
      }

      try
      {
        resourceImpl.attached(definition);
      }
      catch (Exception e)
      {

      }

    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }

  public void testExtensibilityElementFactory()
  {
    try
    {
      ExtensibilityElementFactory factory = WSDLPlugin.INSTANCE.getExtensibilityElementFactory(SOAPConstants.SOAP_NAMESPACE_URI);
      if (factory != null)
      {
        ExtensibilityElement ee = factory.createExtensibilityElement(SOAPConstants.SOAP_NAMESPACE_URI, SOAPConstants.HEADER_ELEMENT_TAG);
        Assert.assertTrue("Problem creating SOAP extensibility element", ee instanceof SOAPHeader);
      }
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }

  class TestExtensibilityElement extends ExtensibilityElementImpl implements ExtensibilityElement
  {
    String ns, name;

    public TestExtensibilityElement(String ns, String name)
    {
      super();
      this.ns = ns;
      this.name = name;
    }

    public QName getElementType()
    {
      if (elementType == null)
      {
        elementType = new QName(ns, name);
      }
      return elementType;
    }
  }

  class WSDLTestFactory implements ExtensibilityElementFactory
  {
    public WSDLTestFactory()
    {
    }

    public ExtensibilityElement createExtensibilityElement(String namespace, String localName)
    {
      return new TestExtensibilityElement(namespace, localName);
    }
  }

  public void testExtensibilityElementFactoryRegistry()
  {
    try
    {
      ExtensibilityElementFactoryRegistry factoryRegistry = WSDLPlugin.INSTANCE.getExtensibilityElementFactoryRegistry();
      factoryRegistry.registerFactory("http://org.eclipse.wst.wsdl.tests", new WSDLTestFactory());

      ExtensibilityElementFactory factory = WSDLPlugin.INSTANCE.getExtensibilityElementFactory("http://org.eclipse.wst.wsdl.tests");
      ExtensibilityElement ee = factory.createExtensibilityElement("http://org.eclipse.wst.wsdl.tests", "TestElement");
      Assert.assertTrue("1. Problem creating custom Test extensibility element", ee instanceof TestExtensibilityElement);
      Assert.assertTrue("2. Problem creating custom Test extensibility element", ee.getElementType().getLocalPart().equals("TestElement"));
      Assert.assertTrue("3. Problem creating custom Test extensibility element", ee.getElementType().getNamespaceURI().equals(
        "http://org.eclipse.wst.wsdl.tests"));

    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }

}
