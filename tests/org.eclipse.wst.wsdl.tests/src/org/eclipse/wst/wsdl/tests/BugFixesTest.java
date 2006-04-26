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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.binding.mime.MIMEContent;
import org.eclipse.wst.wsdl.binding.mime.MIMEFactory;
import org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml;
import org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated;
import org.eclipse.wst.wsdl.binding.mime.MIMEPackage;
import org.eclipse.wst.wsdl.binding.mime.MIMEPart;
import org.eclipse.wst.wsdl.binding.mime.internal.util.MIMEConstants;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDConstants;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

    suite.addTest(new BugFixesTest("TypeAndElementResolution") //$NON-NLS-1$
        {
          protected void runTest()
          {
            testTypeAndElementResolution();
          }
        });

    suite.addTest(new BugFixesTest("MIMEGetTypeName") //$NON-NLS-1$
        {
          protected void runTest()
          {
            testReturnsProperQNameForMIMEExtensibilityElements();
          }
        });

    suite.addTest(new BugFixesTest("ImportsElementOrder")
    {
      protected void runTest()
      {
        testPlacesImportsAfterTheDefinitionElement();
      }
    });

    suite.addTest(new BugFixesTest("ResolveWSDLElement")
    {
      protected void runTest()
      {
        testResolvesElementInImports();
      }
    });

    suite.addTest(new BugFixesTest("PartsSerialization")
    {
      protected void runTest()
      {
        testSerializesPartsInSOAPBody();
      }
    });

    return suite;
  }

  protected void setUp() throws Exception
  {
    super.setUp();

    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl()); //$NON-NLS-1$
    WSDLPackage pkg = WSDLPackage.eINSTANCE;

    // We need this for XSD <import>.
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl()); //$NON-NLS-1$
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
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
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

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=137040
   */
  public void testPlacesImportsAfterTheDefinitionElement()
  {
    WSDLFactory factory = WSDLPackage.eINSTANCE.getWSDLFactory();

    String namespace = "testNamespace"; //$NON-NLS-1$

    Definition definition = factory.createDefinition();
    definition.setQName(new QName(namespace, "testDefinition")); //$NON-NLS-1$  
    definition.updateElement();

    Service service = factory.createService();
    service.setQName(new QName(namespace, "testService")); //$NON-NLS-1$
    definition.addService(service);

    Import wsdlImport = factory.createImport();
    definition.addImport(wsdlImport);

    Element definitionElement = definition.getElement();
    Element serviceElement = service.getElement();
    Element importElement = wsdlImport.getElement();

    NodeList definitionElementChildren = definitionElement.getChildNodes();

    Node firstChild = definitionElementChildren.item(0);

    assertSame(importElement, firstChild);

    Node secondChild = definitionElementChildren.item(1);

    assertSame(serviceElement, secondChild);
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=137866
   */
  public void testResolvesElementInImports()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/WSDLElementResolution/main.wsdl"); //$NON-NLS-1$

      String targetNamespace = "http://www.example.com"; //$NON-NLS-1$

      // This test attempts to locate a message located in the first level
      // import.

      QName firstLevelMessageQName = new QName(targetNamespace, "testINPUTmessage"); //$NON-NLS-1$
      javax.wsdl.Message firstLevelMessage = definition.getMessage(firstLevelMessageQName);

      assertNotNull(firstLevelMessage);

      // This test attempts to locate a message located in the second level
      // import.

      QName secondLevelMessageQName = new QName(targetNamespace, "testOUTPUTmessage"); //$NON-NLS-1$
      javax.wsdl.Message secondLevelMessage = definition.getMessage(secondLevelMessageQName);

      assertNotNull(secondLevelMessage);

      // This test ensures that we do a breadth first traversal to keep things
      // working approximatively as the old implementation which used to check
      // only the definition and its first level imports. The first message is
      // defined in firstlevel.wsdl as well as secondlevel.wsdl but the
      // algorithm should find the one in firstlevel.wsdl.

      Import firstLevelImport = (Import) definition.getImports(targetNamespace).get(0);
      Definition firstLevelDefinition = firstLevelImport.getEDefinition();

      assertEquals(firstLevelDefinition, ((Message) firstLevelMessage).getEnclosingDefinition());
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=137990
   */
  public void testSerializesPartsInSOAPBody()
  {
    // Build an in-memory WSDL definition.

    WSDLFactory factory = WSDLPackage.eINSTANCE.getWSDLFactory();

    String targetNamespace = "testNamespace"; //$NON-NLS-1$

    Definition definition = factory.createDefinition();
    definition.setTargetNamespace(targetNamespace);
    definition.setQName(new QName(targetNamespace, "testDefinition")); //$NON-NLS-1$
    definition.addNamespace("tns", targetNamespace); //$NON-NLS-1$
    definition.addNamespace("xsd", XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001); //$NON-NLS-1$
    definition.addNamespace("soap", SOAPConstants.SOAP_NAMESPACE_URI); //$NON-NLS-1$

    Message message = factory.createMessage();
    QName messageQName = new QName(targetNamespace, "testMessage");
    message.setQName(messageQName);
    definition.addMessage(message);

    Part part1 = factory.createPart();
    String part1Name = "part1"; //$NON-NLS-1$ 
    part1.setName(part1Name);
    part1.setTypeName(new QName(XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001, "string")); //$NON-NLS-1$
    message.addPart(part1);

    Part part2 = factory.createPart();
    String part2Name = "part2"; //$NON-NLS-1$ 
    part2.setName(part2Name);
    part2.setTypeName(new QName(XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001, "string")); //$NON-NLS-1$
    message.addPart(part2);

    PortType portType = factory.createPortType();
    QName portQName = new QName(targetNamespace, "testPort"); //$NON-NLS-1$
    portType.setQName(portQName);
    definition.addPortType(portType);

    Operation operation = factory.createOperation();
    String operationName = "testOperation"; //$NON-NLS-1$ 
    operation.setName(operationName);
    portType.addOperation(operation);

    Input input = factory.createInput();
    input.setMessage(message);
    operation.setInput(input);

    Binding binding = factory.createBinding();
    QName bindingQName = new QName(targetNamespace, "testBinding"); //$NON-NLS-1$
    binding.setQName(bindingQName);
    binding.setPortType(portType);
    definition.addBinding(binding);

    BindingOperation bindingOperation = factory.createBindingOperation();
    bindingOperation.setOperation(operation);
    binding.addBindingOperation(bindingOperation);

    BindingInput bindingInput = factory.createBindingInput();
    bindingOperation.setBindingInput(bindingInput);

    SOAPFactory soapFactory = SOAPPackage.eINSTANCE.getSOAPFactory();
    SOAPBody soapBody = soapFactory.createSOAPBody();
    bindingInput.addExtensibilityElement(soapBody);

    definition.updateElement();

    // Test the "no parts" scenario. In this case the parts attribute should not
    // be present.

    Element soapBodyElement = soapBody.getElement();
    Attr partsAttributeNode = soapBodyElement.getAttributeNode(SOAPConstants.PARTS_ATTRIBUTE);
    assertNull(partsAttributeNode);

    // Test the scenario when the body specifies one part. In this case the
    // parts attribute
    // should be present and look like this parts="part1"

    List parts = new ArrayList();
    parts.add(part1);
    soapBody.setParts(parts);

    soapBody.updateElement();

    soapBodyElement = soapBody.getElement();
    String partsAttributeValue = soapBodyElement.getAttribute(SOAPConstants.PARTS_ATTRIBUTE);
    assertEquals(part1Name, partsAttributeValue);

    // Test the scenario when the body specifies two parts. In this case the
    // parts attribute
    // should be present and look like this parts="part1 part2"

    parts.add(part2);
    soapBody.setParts(parts);

    soapBody.updateElement();

    soapBodyElement = soapBody.getElement();
    partsAttributeValue = soapBodyElement.getAttribute(SOAPConstants.PARTS_ATTRIBUTE);
    assertEquals(part1Name + " " + part2Name, partsAttributeValue); //$NON-NLS-1$
  }
}
