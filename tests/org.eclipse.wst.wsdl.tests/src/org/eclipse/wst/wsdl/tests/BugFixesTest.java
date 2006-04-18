/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests;

import java.util.Iterator;

import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.binding.mime.MIMEContent;
import org.eclipse.wst.wsdl.binding.mime.MIMEFactory;
import org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml;
import org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated;
import org.eclipse.wst.wsdl.binding.mime.MIMEPackage;
import org.eclipse.wst.wsdl.binding.mime.MIMEPart;
import org.eclipse.wst.wsdl.binding.mime.internal.util.MIMEConstants;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;

/**
 * Contains unit tests for reported bugs.
 */
public class BugFixesTest extends TestCase
{
  private String PLUGIN_ABSOLUTE_PATH = WSDLTestsPlugin.getInstallURL();

  public BugFixesTest(String name)
  {
    super(name);
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(new BugFixesTest("TypeAndElementResolution")
    {
      protected void runTest()
      {
        testTypeAndElementResolution();
      }
    });

    suite.addTest(new BugFixesTest("MIMEGetTypeName")
    {
      protected void runTest()
      {
        testReturnsProperQNameForMIMEExtensibilityElements();
      }
    });

    return suite;
  }

  protected void setUp() throws Exception
  {
    super.setUp();

    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl());
    WSDLPackage pkg = WSDLPackage.eINSTANCE;

    // We need this for XSD <import>.
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl());
    XSDPackage xsdpkg = XSDPackage.eINSTANCE;
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=133310
   */
  public void testTypeAndElementResolution()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/TypeAndElementResolution/Test.wsdl"); //$NON-NLS-1$

      // There are two inline schemas, each importing an external schema.
      // The first schema is empty and used just to show the type resolution
      // mechanism's fault.
      // The schema containing the type and element declaration we're interested
      // in is the second schema in the collection.

      XSDSchema inlineSchema = (XSDSchema) definition.getETypes().getSchemas().get(1);

      // The first and only component in this schema is an import.

      XSDImport xsdImport = (XSDImport) inlineSchema.getContents().get(0);

      // The imported schema was resolved when the resource was loaded.
      // This is the schema containing our type/element.

      XSDSchema schema = xsdImport.getResolvedSchema();

      // Now check to make sure the resolved type/element for the messages in
      // the WSDL document
      // are the ones in the schema and not some bogus ones.

      Iterator messagesIterator = definition.getEMessages().iterator();

      while (messagesIterator.hasNext())
      {
        Message message = (Message) messagesIterator.next();
        String name = message.getQName().getLocalPart();
        if (name.equals("testRequest")) //$NON-NLS-1$
        {
          // We know there is only one part in the message and it refers to a
          // type. Make sure the type can be resolved.

          Part part = (Part) message.getEParts().get(0);
          XSDTypeDefinition myType = part.getTypeDefinition();
          assertEquals(schema, myType.getContainer());
        }
        else if (name.equals("testResponse")) //$NON-NLS-1$
        {
          // We know there is only one part in the message and it refers to an
          // element.

          Part part = (Part) message.getEParts().get(0);
          XSDElementDeclaration myElement = part.getElementDeclaration();
          assertEquals(schema, myElement.getContainer());
        }
      }

    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=133953
   */
  public void testReturnsProperQNameForMIMEExtensibilityElements()
  {
    MIMEFactory factory = MIMEPackage.eINSTANCE.getMIMEFactory();

    MIMEContent content = factory.createMIMEContent();
    QName contentElementType = content.getElementType();
    assertEquals(MIMEConstants.MIME_NAMESPACE_URI, contentElementType.getNamespaceURI());
    assertEquals(MIMEConstants.CONTENT_ELEMENT_TAG, contentElementType.getLocalPart());

    MIMEMimeXml mimeXml = factory.createMIMEMimeXml();
    QName mimeXmlElementType = mimeXml.getElementType();
    assertEquals(MIMEConstants.MIME_NAMESPACE_URI, mimeXmlElementType.getNamespaceURI());
    assertEquals(MIMEConstants.MIME_XML_ELEMENT_TAG, mimeXmlElementType.getLocalPart());

    MIMEMultipartRelated multipartRelated = factory.createMIMEMultipartRelated();
    QName multipartRelatedElementType = multipartRelated.getElementType();
    assertEquals(MIMEConstants.MIME_NAMESPACE_URI, multipartRelatedElementType.getNamespaceURI());
    assertEquals(MIMEConstants.MULTIPART_RELATED_ELEMENT_TAG, multipartRelatedElementType.getLocalPart());

    MIMEPart part = factory.createMIMEPart();
    QName partElementType = part.getElementType();
    assertEquals(MIMEConstants.MIME_NAMESPACE_URI, partElementType.getNamespaceURI());
    assertEquals(MIMEConstants.PART_ELEMENT_TAG, partElementType.getLocalPart());
  }
}