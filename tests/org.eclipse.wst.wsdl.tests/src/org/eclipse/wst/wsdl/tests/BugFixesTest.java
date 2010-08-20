/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.OperationType;
import javax.wsdl.Port;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.xml.WSDLReader;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.UnknownExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.WSDLPlugin;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
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
import org.eclipse.wst.wsdl.internal.util.WSDLUtil;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
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

    suite.addTest(new BugFixesTest("ImportsElementOrder") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testPlacesImportsAfterTheDefinitionElement();
        }
      });

    suite.addTest(new BugFixesTest("ResolveWSDLElement") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testResolvesElementInImports();
        }
      });

    suite.addTest(new BugFixesTest("PartsSerialization") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testSerializesPartsInSOAPBody();
        }
      });

    suite.addTest(new BugFixesTest("ImportsSerialization") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testSerializesImportsBeforeTypes();
        }
      });

    suite.addTest(new BugFixesTest("LocalNamespacePrefixes") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testSupportsLocalNamespacePrefixes();
        }
      });

    suite.addTest(new BugFixesTest("OperationExtensionElements") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testTolleratesExtensionElementsForOperation();
        }
      });

    suite.addTest(new BugFixesTest("ReconcilesBindingFaults") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testReconcilesBindingFaults();
        }
      });

    suite.addTest(new BugFixesTest("DuplicateSAXErrorDiagnostics") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testAvoidDuplicateSAXExceptionDiagnostics();
        }
      });

    suite.addTest(new BugFixesTest("BindingOperationReconciliation") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testBindingOperationReconciliation();
        }
      });

    suite.addTest(new BugFixesTest("FullElementExtensibility") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testFullElementExtensibility();
        }
      });

    suite.addTest(new BugFixesTest("TypesExtensibility") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testTypesExtensibility();
      }
    });

    suite.addTest(new BugFixesTest("AllowNullNamespaceURI") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testAllowNullNamespaceURI();
      }
    });

    suite.addTest(new BugFixesTest("LoadsNamelessDefinition") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testLoadsNamelessDefinition();
      }
    });

    suite.addTest(new BugFixesTest("HandlesDocumentationElements") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testHandlesDocumentationElements();
      }
    });

    suite.addTest(new BugFixesTest("SupportsLocalNSForExtensibilityElements") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testSupportsLocalNSForExtensibilityElements();
      }
    });
    
    suite.addTest(new BugFixesTest("InlineTypesFromImportsAreVisible") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testInlineTypesFromImportsAreVisible();
      }
    });
    
    suite.addTest(new BugFixesTest("PropagatesTargetNamespaceChange") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testPropagatesTargetNamespaceChange();
      }
    });
    
    suite.addTest(new BugFixesTest("RemoveBinding") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testRemoveBinding();
      }
    });
    
    suite.addTest(new BugFixesTest("RemoveMessage") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testRemoveMessage();
      }
    });
    
    suite.addTest(new BugFixesTest("RemovePortType") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testRemovePortType();
      }
    });
    
    suite.addTest(new BugFixesTest("RemoveService") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testRemoveService();
      }
    });
    
    suite.addTest(new BugFixesTest("GetWSDLType") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testGetWSDLType();
      }
    });
    
    suite.addTest(new BugFixesTest("InvalidXSDImports") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testInvalidXSDImports();
      }
    });
    
    suite.addTest(new BugFixesTest("ReconcileNonWSDLElements") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testReconcileNonWSDLElements();
      }
    });        
    
    suite.addTest(new BugFixesTest("ReconcilesImportsWithNoLocation") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testReconcilesImportsWithNoLocation();
      }
    }); 
    
    suite.addTest(new BugFixesTest("ReconcilesExtensibleElements") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testReconcilesExtensibleElements();
      }
    });

    suite.addTest(new BugFixesTest("ImportsWithNonStandardFileExtension") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testImportsWithNonStandardFileExtension();
      }
    });

    suite.addTest(new BugFixesTest("LocalDefaultNamespace") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testSupportsLocalDefaultNamespace();
      }
    });
    
    suite.addTest(new BugFixesTest("SOAPBodyForMIME") //$NON-NLS-1$
    {
      protected void runTest()
      {
        testReconcilesSOAPBodyPartsInMIMEBinding();
      }
    });

    return suite;
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

      XSDSchema inlineSchema = (XSDSchema)definition.getETypes().getSchemas().get(1);

      // The first and only component in this schema is an import.

      XSDImport xsdImport = (XSDImport)inlineSchema.getContents().get(0);

      // The imported schema was resolved when the resource was loaded.
      // This is the schema containing our type/element.

      XSDSchema schema = xsdImport.getResolvedSchema();

      // Now check to make sure the resolved type/element for the messages in
      // the WSDL document
      // are the ones in the schema and not some bogus ones.

      Iterator messagesIterator = definition.getEMessages().iterator();

      while (messagesIterator.hasNext())
      {
        Message message = (Message)messagesIterator.next();
        String name = message.getQName().getLocalPart();
        if (name.equals("testRequest")) //$NON-NLS-1$
        {
          // We know there is only one part in the message and it refers to a
          // type. Make sure the type can be resolved.

          Part part = (Part)message.getEParts().get(0);
          XSDTypeDefinition myType = part.getTypeDefinition();
          assertEquals(schema, myType.getContainer());
        }
        else if (name.equals("testResponse")) //$NON-NLS-1$
        {
          // We know there is only one part in the message and it refers to an
          // element.

          Part part = (Part)message.getEParts().get(0);
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

      Import firstLevelImport = (Import)definition.getImports(targetNamespace).get(0);
      Definition firstLevelDefinition = firstLevelImport.getEDefinition();

      assertEquals(firstLevelDefinition, ((Message)firstLevelMessage).getEnclosingDefinition());
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

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=138033
   */
  public void testSerializesImportsBeforeTypes()
  {
    WSDLFactory factory = WSDLPackage.eINSTANCE.getWSDLFactory();

    String namespace = "testNamespace"; //$NON-NLS-1$

    Definition definition = factory.createDefinition();
    definition.setQName(new QName(namespace, "testDefinition")); //$NON-NLS-1$  
    definition.updateElement();

    Types types = factory.createTypes();
    definition.setTypes(types);

    Import wsdlImport = factory.createImport();
    definition.addImport(wsdlImport);

    Element definitionElement = definition.getElement();
    Element typesElement = types.getElement();
    Element importElement = wsdlImport.getElement();

    NodeList definitionElementChildren = definitionElement.getChildNodes();

    Node firstChild = definitionElementChildren.item(0);

    assertSame(importElement, firstChild);

    Node secondChild = definitionElementChildren.item(1);

    assertSame(typesElement, secondChild);

    // Blow away the backing DOM.

    definition.setElement(null);
    definition.updateElement();

    definitionElement = definition.getElement();
    typesElement = types.getElement();
    importElement = wsdlImport.getElement();

    definitionElementChildren = definitionElement.getChildNodes();

    firstChild = definitionElementChildren.item(0);

    assertSame(importElement, firstChild);

    secondChild = definitionElementChildren.item(1);

    assertSame(typesElement, secondChild);
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=245263
   */
  public void testRemoveMessage()
  {
    try
    {
      // load a wsdl that imports another wsdl
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/WSDL4JRemove/RemoveViaWSDL4J.wsdl", true); //$NON-NLS-1$
      String targetNamespace = definition.getTargetNamespace();
      String importedTargetNamespace = "http://www.example.org/ImportMe/"; //$NON-NLS-1$
      int totalMessages = 5;
      definition.updateElement();
      
      // make sure wsdl was loaded properly
      assertEquals("Initial messages were not properly loaded (checked via definition.getMessages())", totalMessages, definition.getMessages().size()); //$NON-NLS-1$
      assertEquals("Initial messages were not properly loaded (checked via definition.getEMessages())", totalMessages, definition.getEMessages().size()); //$NON-NLS-1$
      Element definitionElement = definition.getElement();
      NodeList messageElements = definitionElement.getElementsByTagNameNS(WSDLConstants.WSDL_NAMESPACE_URI, WSDLConstants.MESSAGE_ELEMENT_TAG);
      assertEquals("Initial messages were not properly loaded (checked via DOM)", totalMessages, messageElements.getLength()); //$NON-NLS-1$
      
      // make sure message we're going to remove currently exists
      QName messageQName = new QName(targetNamespace, "RemoveViaWSDL4JMessageExtra"); //$NON-NLS-1$
      javax.wsdl.Message message = definition.getMessage(messageQName);
      assertNotNull("Unable to find RemoveViaWSDL4JMessageExtra", message); //$NON-NLS-1$
      
      // remove the message
      javax.wsdl.Message removedMessage = definition.removeMessage(messageQName);
      assertEquals("Incorrect message removed", message, removedMessage); //$NON-NLS-1$
      
      // make sure message is gone
      javax.wsdl.Message nonexistMessage = definition.getMessage(messageQName);
      assertNull("RemoveViaWSDL4JMessageExtra still exists in model", nonexistMessage); //$NON-NLS-1$
      
      // make sure there is now 1 less message
      assertEquals("Message was not removed (checked via definition.getMessages())", totalMessages-1, definition.getMessages().size()); //$NON-NLS-1$
      assertEquals("Message was not removed (checked via definition.getEMessages())", totalMessages-1, definition.getEMessages().size()); //$NON-NLS-1$
      definitionElement = definition.getElement();
      messageElements = definitionElement.getElementsByTagNameNS(WSDLConstants.WSDL_NAMESPACE_URI, WSDLConstants.MESSAGE_ELEMENT_TAG);
      assertEquals("Message was not removed (checked via DOM)", totalMessages-1, messageElements.getLength()); //$NON-NLS-1$
      
      // make sure imported message we're going to remove currently exists
      messageQName = new QName(importedTargetNamespace, "ImportMeMessageExtra"); //$NON-NLS-1$
      message = definition.getMessage(messageQName);
      assertNotNull("Unable to find ImportMeMessageExtra", message); //$NON-NLS-1$
      
      // attempt to remove the imported message
      removedMessage = definition.removeMessage(messageQName);
      assertNull("ImportMeMessageExtra was incorrectly removed", removedMessage); //$NON-NLS-1$
      
      // make sure imported message still exists
      message = definition.getMessage(messageQName);
      assertNotNull("ImportMeMessageExtra no longer exists", message); //$NON-NLS-1$
      
      nonexistMessage = definition.removeMessage(new QName(targetNamespace, "doesntexist")); //$NON-NLS-1$
      assertNull("A non-existing message was removed", nonexistMessage); //$NON-NLS-1$
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }      
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=245263
   */
  public void testRemoveService()
  {
    try
    {
      // load a wsdl that imports another wsdl
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/WSDL4JRemove/RemoveViaWSDL4J.wsdl", true); //$NON-NLS-1$
      String targetNamespace = definition.getTargetNamespace();
      String importedTargetNamespace = "http://www.example.org/ImportMe/"; //$NON-NLS-1$
      int totalServices = 2;
      definition.updateElement();
      
      // make sure wsdl was loaded properly
      assertEquals("Initial services were not properly loaded (checked via definition.getServices())", totalServices, definition.getServices().size()); //$NON-NLS-1$
      assertEquals("Initial services were not properly loaded (checked via definition.getEServices())", totalServices, definition.getEServices().size()); //$NON-NLS-1$
      Element definitionElement = definition.getElement();
      NodeList serviceElements = definitionElement.getElementsByTagNameNS(WSDLConstants.WSDL_NAMESPACE_URI, WSDLConstants.SERVICE_ELEMENT_TAG);
      assertEquals("Initial services were not properly loaded (checked via DOM)", totalServices, serviceElements.getLength()); //$NON-NLS-1$
      
      // make sure service we're going to remove currently exists
      QName serviceQName = new QName(targetNamespace, "MainServiceExtra"); //$NON-NLS-1$
      javax.wsdl.Service service = definition.getService(serviceQName);
      assertNotNull("Unable to find MainServiceExtra", service); //$NON-NLS-1$
      
      // remove the service
      javax.wsdl.Service removedService = definition.removeService(serviceQName);
      assertEquals("Incorrect service removed", service, removedService); //$NON-NLS-1$
      
      // make sure service is gone
      javax.wsdl.Service nonexistService = definition.getService(serviceQName);
      assertNull("MainServiceExtra still exists in model", nonexistService); //$NON-NLS-1$
      
      // make sure there is now 1 less service
      assertEquals("Service was not removed (checked via definition.getServices())", totalServices-1, definition.getServices().size()); //$NON-NLS-1$
      assertEquals("Service was not removed (checked via definition.getEServices())", totalServices-1, definition.getEServices().size()); //$NON-NLS-1$
      definitionElement = definition.getElement();
      serviceElements = definitionElement.getElementsByTagNameNS(WSDLConstants.WSDL_NAMESPACE_URI, WSDLConstants.SERVICE_ELEMENT_TAG);
      assertEquals("Service was not removed (checked via DOM)", totalServices-1, serviceElements.getLength()); //$NON-NLS-1$
      
      // make sure imported service we're going to remove currently exists
      serviceQName = new QName(importedTargetNamespace, "ImportServiceExtra"); //$NON-NLS-1$
      service = definition.getService(serviceQName);
      assertNotNull("Unable to find ImportServiceExtra", service); //$NON-NLS-1$
      
      // attempt to remove the imported service
      removedService = definition.removeService(serviceQName);
      assertNull("ImportServiceExtra was incorrectly removed", removedService); //$NON-NLS-1$
      
      // make sure imported service still exists
      service = definition.getService(serviceQName);
      assertNotNull("ImportServiceExtra no longer exists", service); //$NON-NLS-1$
      
      nonexistService = definition.removeService(new QName(targetNamespace, "doesntexist")); //$NON-NLS-1$
      assertNull("A non-existing service was removed", nonexistService); //$NON-NLS-1$
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }      
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=245263
   */
  public void testRemoveBinding()
  {
    try
    {
      // load a wsdl that imports another wsdl
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/WSDL4JRemove/RemoveViaWSDL4J.wsdl", true); //$NON-NLS-1$
      String targetNamespace = definition.getTargetNamespace();
      String importedTargetNamespace = "http://www.example.org/ImportMe/"; //$NON-NLS-1$
      int totalBindings = 2;
      definition.updateElement();
      
      // make sure wsdl was loaded properly
      assertEquals("Initial bindings were not properly loaded (checked via definition.getBindings())", totalBindings, definition.getBindings().size()); //$NON-NLS-1$
      assertEquals("Initial bindings were not properly loaded (checked via definition.getEBindings())", totalBindings, definition.getEBindings().size()); //$NON-NLS-1$
      Element definitionElement = definition.getElement();
      NodeList bindingElements = definitionElement.getElementsByTagNameNS(WSDLConstants.WSDL_NAMESPACE_URI, WSDLConstants.BINDING_ELEMENT_TAG);
      assertEquals("Initial bindings were not properly loaded (checked via DOM)", totalBindings, bindingElements.getLength()); //$NON-NLS-1$
      
      // make sure binding we're going to remove currently exists
      QName bindingQName = new QName(targetNamespace, "MainBindingExtra"); //$NON-NLS-1$
      javax.wsdl.Binding binding = definition.getBinding(bindingQName);
      assertNotNull("Unable to find MainBindingExtra", binding); //$NON-NLS-1$
      
      // remove the binding
      javax.wsdl.Binding removedBinding = definition.removeBinding(bindingQName);
      assertEquals("Incorrect binding removed", binding, removedBinding); //$NON-NLS-1$
      
      // make sure binding is gone
      javax.wsdl.Binding nonexistBinding = definition.getBinding(bindingQName);
      assertNull("MainBindingExtra still exists in model", nonexistBinding); //$NON-NLS-1$
      
      // make sure there is now 1 less binding
      assertEquals("Binding was not removed (checked via definition.getBindings())", totalBindings-1, definition.getBindings().size()); //$NON-NLS-1$
      assertEquals("Binding was not removed (checked via definition.getEBindings())", totalBindings-1, definition.getEBindings().size()); //$NON-NLS-1$
      definitionElement = definition.getElement();
      bindingElements = definitionElement.getElementsByTagNameNS(WSDLConstants.WSDL_NAMESPACE_URI, WSDLConstants.BINDING_ELEMENT_TAG);
      assertEquals("Binding was not removed (checked via DOM)", totalBindings-1, bindingElements.getLength()); //$NON-NLS-1$
      
      // make sure imported binding we're going to remove currently exists
      bindingQName = new QName(importedTargetNamespace, "ImportBindingExtra"); //$NON-NLS-1$
      binding = definition.getBinding(bindingQName);
      assertNotNull("Unable to find ImportBindingExtra", binding); //$NON-NLS-1$
      
      // attempt to remove the imported binding
      removedBinding = definition.removeBinding(bindingQName);
      assertNull("ImportBindingExtra was incorrectly removed", removedBinding); //$NON-NLS-1$
      
      // make sure imported binding still exists
      binding = definition.getBinding(bindingQName);
      assertNotNull("ImportBindingExtra no longer exists", binding); //$NON-NLS-1$
      
      nonexistBinding = definition.removeBinding(new QName(targetNamespace, "doesntexist")); //$NON-NLS-1$
      assertNull("A non-existing binding was removed", nonexistBinding); //$NON-NLS-1$
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }      
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=245263
   */
  public void testRemovePortType()
  {
    try
    {
      // load a wsdl that imports another wsdl
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/WSDL4JRemove/RemoveViaWSDL4J.wsdl", true); //$NON-NLS-1$
      String targetNamespace = definition.getTargetNamespace();
      String importedTargetNamespace = "http://www.example.org/ImportMe/"; //$NON-NLS-1$
      int totalPortTypes = 2;
      definition.updateElement();
      
      // make sure wsdl was loaded properly
      assertEquals("Initial port types were not properly loaded (checked via definition.getPortTypes())", totalPortTypes, definition.getPortTypes().size()); //$NON-NLS-1$
      assertEquals("Initial port types were not properly loaded (checked via definition.getEPortTypes())", totalPortTypes, definition.getEPortTypes().size()); //$NON-NLS-1$
      Element definitionElement = definition.getElement();
      NodeList portTypeElements = definitionElement.getElementsByTagNameNS(WSDLConstants.WSDL_NAMESPACE_URI, WSDLConstants.PORT_TYPE_ELEMENT_TAG);
      assertEquals("Initial port types were not properly loaded (checked via DOM)", totalPortTypes, portTypeElements.getLength()); //$NON-NLS-1$
      
      // make sure port type we're going to remove currently exists
      QName portTypeQName = new QName(targetNamespace, "MainPortTypeExtra"); //$NON-NLS-1$
      javax.wsdl.PortType portType = definition.getPortType(portTypeQName);
      assertNotNull("Unable to find MainPortTypeExtra", portType); //$NON-NLS-1$
      
      // remove the portType
      javax.wsdl.PortType removedPortType = definition.removePortType(portTypeQName);
      assertEquals("Incorrect portType removed", portType, removedPortType); //$NON-NLS-1$
      
      // make sure portType is gone
      javax.wsdl.PortType nonexistPortType = definition.getPortType(portTypeQName);
      assertNull("MainPortTypeExtra still exists in model", nonexistPortType); //$NON-NLS-1$
      
      // make sure there is now 1 less portType
      assertEquals("PortType was not removed (checked via definition.getPortTypes())", totalPortTypes-1, definition.getPortTypes().size()); //$NON-NLS-1$
      assertEquals("PortType was not removed (checked via definition.getEPortTypes())", totalPortTypes-1, definition.getEPortTypes().size()); //$NON-NLS-1$
      definitionElement = definition.getElement();
      portTypeElements = definitionElement.getElementsByTagNameNS(WSDLConstants.WSDL_NAMESPACE_URI, WSDLConstants.PORT_TYPE_ELEMENT_TAG);
      assertEquals("PortType was not removed (checked via DOM)", totalPortTypes-1, portTypeElements.getLength()); //$NON-NLS-1$
      
      // make sure imported portType we're going to remove currently exists
      portTypeQName = new QName(importedTargetNamespace, "ImportPortTypeExtra"); //$NON-NLS-1$
      portType = definition.getPortType(portTypeQName);
      assertNotNull("Unable to find ImportPortTypeExtra", portType); //$NON-NLS-1$
      
      // attempt to remove the imported portType
      removedPortType = definition.removePortType(portTypeQName);
      assertNull("ImportPortTypeExtra was incorrectly removed", removedPortType); //$NON-NLS-1$
      
      // make sure imported portType still exists
      portType = definition.getPortType(portTypeQName);
      assertNotNull("ImportPortTypeExtra no longer exists", portType); //$NON-NLS-1$
      
      nonexistPortType = definition.removePortType(new QName(targetNamespace, "doesntexist")); //$NON-NLS-1$
      assertNull("A non-existing portType was removed", nonexistPortType); //$NON-NLS-1$
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }      
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=150553
   */
  public void testSupportsLocalNamespacePrefixes()
  {
    Definition definition = null;

    try
    {
      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/LocalNamespace/LocalNamespace.wsdl"); //$NON-NLS-1$
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }

    String targetNamespace = "http://tempuri.org/Simple/"; //$NON-NLS-1$

    // Check that the response message's part element is resolved OK.

    QName responseMessageQName = new QName(targetNamespace, "myOperationResponse"); ////$NON-NLS-1$
    javax.wsdl.Message responseMessage = definition.getMessage(responseMessageQName);

    Part responsePart = (Part)responseMessage.getPart("myOperationResponse"); ////$NON-NLS-1$

    XSDElementDeclaration responseElementDeclaration = responsePart.getElementDeclaration();

    assertNotNull(responseElementDeclaration);
    assertNotNull(responseElementDeclaration.getContainer());

    // Check that the request message's part element is resolved OK.
    // This part defines a local namespace prefix

    QName requestMessageQName = new QName(targetNamespace, "myOperationRequest"); ////$NON-NLS-1$
    javax.wsdl.Message requestMessage = definition.getMessage(requestMessageQName);

    Part requestPart = (Part)requestMessage.getPart("myOperationRequest"); ////$NON-NLS-1$

    XSDElementDeclaration requestElementDeclaration = requestPart.getElementDeclaration();

    assertNotNull(requestElementDeclaration);

    // Now to make sure the DOM is reconciled properly and uses the local namespace prefix, 
    // let's try to change the part's element declaration. We'll use the response part element
    // just because it is convenient.

    requestPart.setElementDeclaration(responseElementDeclaration);

    Element partElement = requestPart.getElement();
    String elementAttributeValue = partElement.getAttribute(WSDLConstants.ELEMENT_ATTRIBUTE);

    assertEquals(elementAttributeValue, "parttns:" + responseElementDeclaration.getName());
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=157107
   */
  public void testTolleratesExtensionElementsForOperation()
  {
    Definition definition = null;

    try
    {
      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/OperationStyle/OperationStyleTest.wsdl"); //$NON-NLS-1$
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }

    PortType portType = (PortType)definition.getEPortTypes().get(0);
    EList operations = portType.getEOperations();

    Operation operation = (Operation)operations.get(0);
    OperationType operationType = operation.getStyle();

    assertEquals(OperationType.REQUEST_RESPONSE, operationType);
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=164565
   */
  public void testReconcilesBindingFaults()
  {
    Definition definition = null;

    try
    {
      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/BindingFaultReconciliation/BindingFaultSample.wsdl"); //$NON-NLS-1$
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }

    // Test the abnormal case when the binding operation tries to bind a fault
    // but the fault is missing in the corresponding operation. In this case the
    // fault obtained from the binding operation's fault should be null.

    List bindings = definition.getEBindings();
    Binding binding = (Binding)bindings.get(0);
    List bindingOperations = binding.getBindingOperations();
    BindingOperation bindingOperation = (BindingOperation)bindingOperations.get(0);
    BindingFault bindingFault = (BindingFault)bindingOperation.getBindingFault("Operation1Fault"); //$NON-NLS-1$
    Fault fault = bindingFault.getEFault();
    assertNull(fault);

    // Test the normal case when the operation and binding operation are in
    // synch. In this case the fault defined in the operation should match
    // the one obtained from the binding operation's fault.

    List portTypes = definition.getEPortTypes();
    PortType portType = (PortType)portTypes.get(0);
    EList operations = portType.getEOperations();

    Operation operation = (Operation)operations.get(1);
    javax.wsdl.Fault expectedFault1 = operation.getFault("Operation2Fault1"); //$NON-NLS-1$
    javax.wsdl.Fault expectedFault2 = operation.getFault("Operation2Fault2"); //$NON-NLS-1$

    BindingOperation bindingOperation2 = (BindingOperation)bindingOperations.get(1);

    // Make sure the fault obtained from the binding fault is not null and
    // matches the one in the corresponding operation.

    BindingFault bindingFault1 = (BindingFault)bindingOperation2.getBindingFault("Operation2Fault1"); //$NON-NLS-1$
    javax.wsdl.Fault actualFault1 = bindingFault1.getEFault();
    assertNotNull(actualFault1);
    assertEquals(expectedFault1, actualFault1);

    // Make sure the fault obtained from the binding fault is not null and
    // matches the one in the corresponding operation.

    BindingFault bindingFault2 = (BindingFault)bindingOperation2.getBindingFault("Operation2Fault2"); //$NON-NLS-1$
    javax.wsdl.Fault actualFault2 = bindingFault2.getEFault();
    assertNotNull(actualFault2);
    assertEquals(expectedFault2, actualFault2);
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=161059
   */
  public void testAvoidDuplicateSAXExceptionDiagnostics()
  {
    Definition definition = null;

    try
    {
      // Make sure we track location to allow the WSDLParser to kick in.

      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/DuplicateSAXException/SAXException.wsdl", true, true); //$NON-NLS-1$
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }

    Resource resource = definition.eResource();
    EList errors = resource.getErrors();
    int expectedSize = 1;
    int actualSize = errors.size();
    assertEquals(expectedSize, actualSize);
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=172576
   */
  public void testBindingOperationReconciliation()
  {
    Definition definition = null;

    try
    {
      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH
        + "samples/BugFixes/BindingOperationReconciliation/BindingOperationReconciliation.wsdl", true); //$NON-NLS-1$
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }

    String targetNamespace = "http://www.example.org/BindingOperationReconciliation/"; //$NON-NLS-1$
    QName portTypeQName = new QName(targetNamespace, "BindingOperationReconciliation"); //$NON-NLS-1$
    javax.wsdl.PortType portType = definition.getPortType(portTypeQName);

    String input3Name = "Input3"; //$NON-NLS-1$
    String output3Name = "Output3"; //$NON-NLS-1$

    // Check that the first operation - which has no named input/output is being
    // found.

    String operationName = "NewOperation"; //$NON-NLS-1$
    javax.wsdl.Operation operation1 = portType.getOperation(operationName, null, null);

    QName bindingQName = new QName(targetNamespace, "BindingOperationReconciliationSOAP"); //$NON-NLS-1$
    javax.wsdl.Binding binding = definition.getBinding(bindingQName);

    javax.wsdl.BindingOperation bindingOperation1 = binding.getBindingOperation(operationName, null, null);
    javax.wsdl.Operation actualOperation1 = bindingOperation1.getOperation();

    assertEquals(operation1, actualOperation1);

    // The second operation - which has no named input/output is being found
    // should not be reconciled because the binding specifies the input and
    // output.

    String input2Name = "Input2"; //$NON-NLS-1$
    String output2Name = "Output2"; //$NON-NLS-1$

    String operation2Name = "NewOperation2"; //$NON-NLS-1$

    javax.wsdl.BindingOperation bindingOperation2 = binding.getBindingOperation(operation2Name, input2Name, output2Name);
    javax.wsdl.Operation actualOperation2 = bindingOperation2.getOperation();

    assertEquals(null, actualOperation2);

    // The third operation specifies an input and output name, and the binding
    // operation will reconcile fine because the it also specifies the proper
    // input and output name.

    String operation3Name = "NewOperation3"; //$NON-NLS-1$
    javax.wsdl.Operation operation3 = portType.getOperation(operation3Name, input3Name, output3Name);

    javax.wsdl.BindingOperation bindingOperation3 = binding.getBindingOperation(operation3Name, input3Name, output3Name);
    javax.wsdl.Operation actualOperation3 = bindingOperation3.getOperation();

    assertEquals(operation3, actualOperation3);
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=177852
   */
  public void testFullElementExtensibility()
  {

    Definition definition = null;

    try
    {
      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/FullElementExtensibility/ExtendedWSDL.wsdl", true); //$NON-NLS-1$
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }

    String extensionsNamespaceURI = "http://www.example.org/Extensions/"; //$NON-NLS-1$ 
    EList imports = definition.getEImports();
    assertFalse(imports.isEmpty());
    Import theImport = (Import)imports.get(0);
    checkExtension(theImport, extensionsNamespaceURI, "import"); //$NON-NLS-1$

    EList messages = definition.getEMessages();
    assertFalse(messages.isEmpty());
    Message message = (Message)messages.get(0);
    checkExtension(message, extensionsNamespaceURI, "message"); //$NON-NLS-1$

    Part part = (Part)message.getPart("extendedPart"); //$NON-NLS-1$
    assertNotNull(part);
    checkExtension(part, extensionsNamespaceURI, "part"); //$NON-NLS-1$

    EList portTypes = definition.getEPortTypes();
    assertFalse(portTypes.isEmpty());
    PortType portType = (PortType)portTypes.get(0);
    checkExtension(portType, extensionsNamespaceURI, "portType"); //$NON-NLS-1$

    Operation operation = (Operation)portType.getOperation("extendedOperation", null, null); //$NON-NLS-1$
    assertNotNull(operation);
    checkExtension(operation, extensionsNamespaceURI, "operation"); //$NON-NLS-1$

    Input input = operation.getEInput();
    assertNotNull(input);
    checkExtension(input, extensionsNamespaceURI, "input"); //$NON-NLS-1$

    Output output = operation.getEOutput();
    assertNotNull(output);
    checkExtension(output, extensionsNamespaceURI, "output"); //$NON-NLS-1$

    Fault fault = (Fault)operation.getFault("extendedFault"); //$NON-NLS-1$
    assertNotNull(fault);
    checkExtension(fault, extensionsNamespaceURI, "fault"); //$NON-NLS-1$
  }

  /**
   * Checks the given extensible element to make sure that its assumed one and only extensibility element matches the given namespace and local name.
   * @param extensibleElement the extensible element to test.
   * @param extensionsNamespaceURI the expected namespace URI.
   * @param elementName the expected element name.
   */
  private void checkExtension(ExtensibleElement extensibleElement, String extensionsNamespaceURI, String elementName)
  {
    List extensibilityElements = extensibleElement.getExtensibilityElements();
    assertFalse(extensibilityElements.isEmpty());
    ExtensibilityElement extensibilityElement = (ExtensibilityElement)extensibilityElements.get(0);
    QName elementType = extensibilityElement.getElementType();
    String nsURI = elementType.getNamespaceURI();
    String localPart = elementType.getLocalPart();
    assertEquals(extensionsNamespaceURI, nsURI);
    assertEquals(localPart, elementName);
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=174361
   */
  public void testTypesExtensibility()
  {

    Definition definition = null;

    try
    {
      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/TypesExtensibility/TypesExtensibility.wsdl", true); //$NON-NLS-1$
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }
    
    Types types = definition.getETypes();
    List extensibilityElements = types.getExtensibilityElements();
    assertEquals(3, extensibilityElements.size());
    
    String otherTypesNamespace = "http://www.example.org/OtherTypes/"; //$NON-NLS-1$

    ExtensibilityElement extensibilityElement = (ExtensibilityElement)extensibilityElements.get(0);
    QName elementType = extensibilityElement.getElementType();
    assertEquals(otherTypesNamespace, elementType.getNamespaceURI());
    assertEquals("typeDef", elementType.getLocalPart()); //$NON-NLS-1$

    List schemas = types.getSchemas();
    assertEquals(1, schemas.size());
    
    XSDSchemaExtensibilityElement schemaExtensibilityElement = (XSDSchemaExtensibilityElement)extensibilityElements.get(1);
    XSDSchema schema = schemaExtensibilityElement.getSchema();
    assertNotNull(schema);
    XSDElementDeclaration elementDeclaration = schema.resolveElementDeclaration("test"); //$NON-NLS-1$
    assertNotNull(elementDeclaration);
    assertNotNull(elementDeclaration.getContainer());    
    
    extensibilityElement = (ExtensibilityElement)extensibilityElements.get(2);
    elementType = extensibilityElement.getElementType();
    assertEquals(otherTypesNamespace, elementType.getNamespaceURI());
    assertEquals("typeDef", elementType.getLocalPart()); //$NON-NLS-1$    
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=178555
   */
  public void testAllowNullNamespaceURI()
  {
    Definition definition = null;

    try
    {
      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/NullNamespaceURI/ContactInfoService.wsdl", true); //$NON-NLS-1$
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }

    String targetNamespace = "http://www.example.org/ContactInfoService"; //$NON-NLS-1$

    // The element declaration for the output message part is specified in a
    // schema with no target namespace. It should resolve fine and have a null
    // namespace URI.

    QName output1QName = new QName(targetNamespace, "updatePhoneNumberResponseMsg"); //$NON-NLS-1$
    Message output1Message = (Message)definition.getMessage(output1QName);
    assertNotNull(output1Message);

    Part part1 = (Part)output1Message.getPart("output1"); //$NON-NLS-1$
    assertNotNull(part1);

    QName output2ElementName = part1.getElementName();
    assertNotNull(output2ElementName);
    assertEquals(XMLConstants.NULL_NS_URI, output2ElementName.getNamespaceURI());

    XSDElementDeclaration output2ElementDeclaration = part1.getElementDeclaration();
    assertNotNull(output2ElementDeclaration);
    assertNotNull(output2ElementDeclaration.getContainer());
    assertNull(output2ElementDeclaration.getTargetNamespace());

    // The type definition for the output message part is specified in a
    // schema with no target namespace. It should resolve fine and have a null
    // namespace URI.

    QName output2QName = new QName(targetNamespace, "updateAddressResponseMsg"); //$NON-NLS-1$
    Message output2Message = (Message)definition.getMessage(output2QName);
    assertNotNull(output2Message);

    Part part2 = (Part)output2Message.getPart("output2"); //$NON-NLS-1$
    assertNotNull(part1);

    QName output2TypeName = part2.getTypeName();
    assertNotNull(output2TypeName);
    assertEquals(XMLConstants.NULL_NS_URI, output2TypeName.getNamespaceURI());

    XSDTypeDefinition output2TypeDefinition = part2.getTypeDefinition();
    assertNotNull(output2TypeDefinition);
    assertNotNull(output2TypeDefinition.getContainer());
    assertNull(output2TypeDefinition.getTargetNamespace());
  }
  
  /**
   * https://bugs.eclipse.org/bugs/show_bug.cgi?id=104453
   */
  public void testLoadsNamelessDefinition()
  {
    try
    {
      //Test with nameless definition
      
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/LoadsNamelessDefinition/MissingName.wsdl");
      assertNull(definition.getQName());

      //Test with named definition
      
      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/LoadsNamelessDefinition/WithName.wsdl");
      assertNotNull(definition);
      QName name = definition.getQName(); 
      assertNotNull(name);
      assertEquals("WSDLFile", name.getLocalPart());
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=151674
   * @throws Exception
   */
  public void testHandlesDocumentationElements()
  {
    try
    {
      // Load a sample WSDL document that has documentation elements in all allowed places.
      
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/HandlesDocumentationElements/Documented.wsdl");
      
      // Make sure imports are added after the documentation element.
      
      WSDLFactory factory = WSDLFactory.eINSTANCE;
      Import anImport = factory.createImport();
      anImport.setNamespaceURI("http://www.test.com");
      definition.addImport(anImport);
      
      Element definitionDocumentationElement = definition.getDocumentationElement();
      assertNotNull(definitionDocumentationElement);
      Element expectedImportElement = getNextElement(definitionDocumentationElement);
      Element importElement = anImport.getElement();
      assertEquals(importElement, expectedImportElement);
      
      // This is a bit overkill since the documentation elements are handled in the base class WSDLElementImpl but...
      
      // Make sure new message parts are added after the documentation element and as the last element.

      Message aMessage = (Message)definition.getEMessages().get(0);
      Part newPart = factory.createPart();
      aMessage.addPart(newPart);

      Element messageElement = aMessage.getElement();

      Element messageDocumentationElement = aMessage.getDocumentationElement();
      assertNotNull(messageDocumentationElement);
      Element firstChildElement = getFirstChildElement(messageElement);
      assertEquals(messageDocumentationElement, firstChildElement);
      
      Element partElement = newPart.getElement();
      Element lastChildElement = getLastChildElement(messageElement);
      assertEquals(partElement, lastChildElement);
      
      // Make sure new operations are added after the documentation element and as the last element.

      PortType portType = (PortType)definition.getEPortTypes().get(0);
      Operation newOperation = factory.createOperation();
      portType.addOperation(newOperation);
      
      Element portTypeElement = portType.getElement();

      Element portTypeDocumentationElement = portType.getDocumentationElement();
      assertNotNull(portTypeDocumentationElement);
      firstChildElement = getFirstChildElement(portTypeElement);
      assertEquals(portTypeDocumentationElement, firstChildElement);
      
      Element newOperationElement = newOperation.getElement();
      lastChildElement = getLastChildElement(portTypeElement);
      assertEquals(newOperationElement, lastChildElement);
      
      // Make sure the output element is added after the documentation element and as the last element.
      
      Operation operation = (Operation)portType.getEOperations().get(0);
      Output output = factory.createOutput();
      operation.setOutput(output);

      Element operationElement = operation.getElement();

      Element operationDocumentationElement = operation.getDocumentationElement();
      assertNotNull(operationDocumentationElement);
      firstChildElement = getFirstChildElement(operationElement);
      assertEquals(operationDocumentationElement, firstChildElement);
      
      Element outputElement = output.getElement();
      lastChildElement = getLastChildElement(operationElement);
      assertEquals(outputElement, lastChildElement);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=198390
   */
  public void testSupportsLocalNSForExtensibilityElements()
  {
    try
    {
      String uri = PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/LocalNamespace/LocalNamespace2.wsdl"; //$NON-NLS-1$
      javax.wsdl.factory.WSDLFactory factory = WSDLPlugin.INSTANCE.createWSDL4JFactory();
      WSDLReader wsdlReader = factory.newWSDLReader();
      javax.wsdl.Definition definition = wsdlReader.readWSDL(uri);

      String targetNamespace = "http://www.example.org/example/"; //$NON-NLS-1$

      // Test a local SOAP namespace prefix declaration.

      QName bindingQName = new QName(targetNamespace, "exampleSOAP"); //$NON-NLS-1$
      javax.wsdl.Binding binding = definition.getBinding(bindingQName);
      assertNotNull(binding);
      List extensibilityElements = binding.getExtensibilityElements();
      assertEquals(1, extensibilityElements.size());
      ExtensibilityElement soapBinding = (ExtensibilityElement)extensibilityElements.get(0);
      assertNotNull(soapBinding);
      QName bindingElementType = soapBinding.getElementType();
      assertNotNull(bindingElementType);
      String localPart = bindingElementType.getLocalPart();
      assertEquals(SOAPConstants.BINDING_ELEMENT_TAG, localPart);
      String namespaceURI = bindingElementType.getNamespaceURI();
      assertEquals(SOAPConstants.SOAP_NAMESPACE_URI, namespaceURI);
      assertTrue(soapBinding instanceof javax.wsdl.extensions.soap.SOAPBinding);

      // Test a default SOAP namespace declaration at the port level.

      QName serviceQName = new QName(targetNamespace, "example"); //$NON-NLS-1$
      javax.wsdl.Service service = definition.getService(serviceQName);
      Port port = service.getPort("exampleSOAP");
      extensibilityElements = port.getExtensibilityElements();
      assertEquals(1, extensibilityElements.size());
      ExtensibilityElement soapAddress = (ExtensibilityElement)extensibilityElements.get(0);
      assertNotNull(soapAddress);
      QName portElementType = soapAddress.getElementType();
      assertNotNull(portElementType);
      localPart = portElementType.getLocalPart();
      assertEquals(SOAPConstants.ADDRESS_ELEMENT_TAG, localPart);
      namespaceURI = portElementType.getNamespaceURI();
      assertEquals(SOAPConstants.SOAP_NAMESPACE_URI, namespaceURI);
      assertTrue(soapAddress instanceof javax.wsdl.extensions.soap.SOAPAddress);
    }
    catch (WSDLException e)
    {
      e.printStackTrace();
      fail();
    }
  }

  private Element getNextElement(Element anElement)
  {
    Node node = anElement.getNextSibling();
    while (node != null && node.getNodeType() != Node.ELEMENT_NODE)
    {
      node = node.getNextSibling();
    }
    return (Element)node;
  }

  private Element getFirstChildElement(Element anElement)
  {
    Node node = anElement.getFirstChild();
    while (node != null && node.getNodeType() != Node.ELEMENT_NODE)
    {
      node = node.getNextSibling();
    }
    return (Element)node;
  }

  private Element getLastChildElement(Element anElement)
  {
    Node node = anElement.getLastChild();
    while (node != null && node.getNodeType() != Node.ELEMENT_NODE)
    {
      node = node.getPreviousSibling();
    }
    return (Element)node;
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=208485
   */
  public void testInlineTypesFromImportsAreVisible()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/InlineTypesFromImportsAreVisible/A.wsdl"); //$NON-NLS-1$
      String targetNamespace = "http://A"; //$NON-NLS-1$
      QName messageQName = new QName(targetNamespace, "message" ); //$NON-NLS-1$
      javax.wsdl.Message message = definition.getMessage(messageQName);
      assertNotNull(message);
      Part part = (Part)message.getPart("parameters"); //$NON-NLS-1$
      assertNotNull(part);
      XSDTypeDefinition typeDefinition = part.getTypeDefinition();
      assertNotNull(typeDefinition);
      String namespace = typeDefinition.getTargetNamespace();
      assertEquals("http://B", namespace);
      assertEquals("BType", typeDefinition.getName());
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }      
  }

  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=194096
   */
  public void testPropagatesTargetNamespaceChange()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/TargetNamespace/TargetNamespace.wsdl", true); //$NON-NLS-1$
      
      String oldTargetNamespace = definition.getTargetNamespace();

      QName messageQName = new QName(oldTargetNamespace, "NewOperationRequest");
      javax.wsdl.Message message = definition.getMessage(messageQName);
      assertNotNull(message);

      QName portTypeQName = new QName(oldTargetNamespace, "TargetNamespace");
      javax.wsdl.PortType portType = definition.getPortType(portTypeQName);
      assertNotNull(portType);
      
      QName bindingQName = new QName(oldTargetNamespace, "TargetNamespaceSOAP");
      javax.wsdl.Binding binding = definition.getBinding(bindingQName);
      assertNotNull(binding);

      QName serviceQName = new QName(oldTargetNamespace, "TargetNamespace");
      javax.wsdl.Service service = definition.getService(serviceQName);
      assertNotNull(service);

      String newTargetNamespace = "http://www.example.org/NewTargetNamespace/"; 
      definition.setTargetNamespace(newTargetNamespace);
      Element definitionElement = definition.getElement();
      Attr targetNamespaceNode = definitionElement.getAttributeNode(WSDLConstants.TARGETNAMESPACE_ATTRIBUTE);
      assertNotNull(targetNamespaceNode);
      assertEquals(newTargetNamespace, targetNamespaceNode.getValue());
      
      messageQName = new QName(newTargetNamespace, messageQName.getLocalPart());
      message = definition.getMessage(messageQName);
      assertNotNull(message);
     
      portTypeQName = new QName(newTargetNamespace, portTypeQName.getLocalPart());
      portType = definition.getPortType(portTypeQName);
      assertNotNull(portType);

      bindingQName = new QName(newTargetNamespace, bindingQName.getLocalPart());
      binding = definition.getBinding(bindingQName);
      assertNotNull(binding);

      serviceQName = new QName(newTargetNamespace, serviceQName.getLocalPart());
      service = definition.getService(serviceQName);
      assertNotNull(service);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }      
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=247296
   */
  public void testGetWSDLType()
  {
    try
    {
      // load a wsdl
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/GetWSDLType/BadImport.wsdl", true); //$NON-NLS-1$
      definition.updateElement();

      // test all direct child elements of definition to make sure they are expected type
      Element definitionElement = definition.getElement();
      int type = WSDLUtil.getInstance().getWSDLType(definitionElement);
      assertEquals("Definition type incorrectly identified", WSDLConstants.DEFINITION, type);  //$NON-NLS-1$

      NodeList childNodes = definitionElement.getChildNodes();
      Node n = childNodes.item(0);
      // skip over text node
      if (n.getNodeType() != Node.ELEMENT_NODE)
        n = n.getNextSibling();
      type = WSDLUtil.getInstance().getWSDLType((Element)n);
      assertEquals("Import type incorrectly identified", WSDLConstants.IMPORT, type);  //$NON-NLS-1$
      
      n = n.getNextSibling();
      // skip over text node
      if (n.getNodeType() != Node.ELEMENT_NODE)
        n = n.getNextSibling();
      type = WSDLUtil.getInstance().getWSDLType((Element)n);
      assertEquals("xsd:import type incorrectly identified", -1, type);  //$NON-NLS-1$
      
      n = n.getNextSibling();
      // skip over text node
      if (n.getNodeType() != Node.ELEMENT_NODE)
        n = n.getNextSibling();
      type = WSDLUtil.getInstance().getWSDLType((Element)n);
      assertEquals("Types type incorrectly identified", WSDLConstants.TYPES, type); //$NON-NLS-1$
      
      n = n.getNextSibling();
      // skip over text node
      if (n.getNodeType() != Node.ELEMENT_NODE)
        n = n.getNextSibling();
      type = WSDLUtil.getInstance().getWSDLType((Element)n);
      assertEquals("Message type incorrectly identified", WSDLConstants.MESSAGE, type); //$NON-NLS-1$
      n = n.getNextSibling();
      
      n = n.getNextSibling();
      // skip over text node
      if (n.getNodeType() != Node.ELEMENT_NODE)
        n = n.getNextSibling();
      type = WSDLUtil.getInstance().getWSDLType((Element)n);
      assertEquals("Message type incorrectly identified", WSDLConstants.MESSAGE, type); //$NON-NLS-1$
      
      n = n.getNextSibling();
      // skip over text node
      if (n.getNodeType() != Node.ELEMENT_NODE)
        n = n.getNextSibling();
      type = WSDLUtil.getInstance().getWSDLType((Element)n);
      assertEquals("Port type type incorrectly identified", WSDLConstants.PORT_TYPE, type); //$NON-NLS-1$
      
      n = n.getNextSibling();
      // skip over text node
      if (n.getNodeType() != Node.ELEMENT_NODE)
        n = n.getNextSibling();
      type = WSDLUtil.getInstance().getWSDLType((Element)n);
      assertEquals("Binding type incorrectly identified", WSDLConstants.BINDING, type); //$NON-NLS-1$
      
      n = n.getNextSibling();
      // skip over text node
      if (n.getNodeType() != Node.ELEMENT_NODE)
        n = n.getNextSibling();
      type = WSDLUtil.getInstance().getWSDLType((Element)n);
      assertEquals("Service type incorrectly identified", WSDLConstants.SERVICE, type); //$NON-NLS-1$
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }      
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=247296
   */
  public void testInvalidXSDImports()
  {
    try
    {
      // load a wsdl that contains an xsd:import outside of wsdl:types
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/GetWSDLType/BadImport.wsdl", true); //$NON-NLS-1$
      
      // there should only be one valid wsdl:import
      Map imports = definition.getImports();
      assertEquals("Incorrect number of imports", 1, imports.size()); //$NON-NLS-1$
      
      // the bad xsd:import should be considered an extensibility element
      List extElements = definition.getExtensibilityElements();
      assertEquals("Incorrect number of extensibility elements", 1, extElements.size());  //$NON-NLS-1$
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }      
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/attachment.cgi?bugid=257279
   */
  public void testReconcileNonWSDLElements()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + 
        "samples/BugFixes/ReconcileNonWSDLElements/ReconcileNonWSDLElements.wsdl", true); //$NON-NLS-1$
            
      Definition newDefinition = WSDLFactory.eINSTANCE.createDefinition();
      assertNotNull("The new definition cannot be null",newDefinition);
      
      // The namespace in the WSDL definition is not instances of WSDLElement, it is an
      // instances of NamespaceImpl
      newDefinition.addNamespace("wsdl", "http://example.org"); //$NON-NLS-1$ //$NON-NLS-2$
      newDefinition.updateElement();
      
      Document newDocument = newDefinition.getDocument();
      assertNotNull("The new definition's document cannot be null",newDocument); //$NON-NLS-1$
      Types types = (Types)definition.getTypes();
      assertNotNull("The definition must have Types",types); //$NON-NLS-1$
      Node typesNode = (types.getElement());
      assertNotNull("The Types element cannot be null",typesNode); //$NON-NLS-1$
      Node toImport = newDocument.importNode(typesNode, true);
      
      // Append child will call org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl.elementContentsChanged(Element)
      // which changes isReconciling to true and calls 
      // org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl.reconcileContents(Element) and changes isReconciling to
      // false after.  However, due to a ClassCast exception in reconcileContents, isReconciling is never changed to
      // false.  The result is newly added WSDL elements not showing up in the EMF model.       
      newDefinition.getElement().appendChild(toImport);
      
      // The bug would have set isReconciling to true and so messages will not be reconciled
      assertNotNull("The definition must have messages",definition.getMessages()); //$NON-NLS-1$
      
      Message messageToAdd = ((Message)definition.getMessage(
        new QName("http://www.example.com/ReconcileNonWSDLElements/","NewOperationRequest"))); //$NON-NLS-1$ //$NON-NLS-2$
      Node messageNode = messageToAdd.getElement();
      assertNotNull("The message to add to the new definition cannot be null",messageNode); //$NON-NLS-1$
      Node toImport2 = newDefinition.getDocument().importNode(messageNode,false);
      
      assertTrue("No messages should exist",newDefinition.getEMessages().size() == 0); //$NON-NLS-1$
      newDefinition.getElement().appendChild(toImport2);
      // The bug causes the message to be added
      assertTrue("A message should have been added", newDefinition.getEMessages().size() == 1); //$NON-NLS-1$      
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }
  }

  /**
   * See https://bugs.eclipse.org/bugs/attachment.cgi?bugid=257279
   */
  public void testReconcilesImportsWithNoLocation()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + 
        "samples/BugFixes/ReconcilesImportsWithNoLocation/Main.wsdl", true); //$NON-NLS-1$
      assertNotNull(definition);
      String targetNamespace = "http://www.example.org/B/";
      QName serviceQName = new QName(targetNamespace, "B");
      javax.wsdl.Service service = definition.getService(serviceQName);
      assertNotNull(service);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=236404
   */
  public void testReconcilesExtensibleElements()
  {
    Definition definition = null;

    // The sample WSDL already has an <annotation> extensibility element for each extensible element
    // We will loop through every extensible element and remove the annotation extensibility element
    // and force the model to update, thus calling the reconciliation code. The expected result is that
    // the annotation extensibility element will be removed in the model.
    try
    {
      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/ReconcilesExtensibleElements/ExtensibleElementSample.wsdl"); //$NON-NLS-1$
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }

    // Definition
    ensureExtensibilityElementRemoved(definition, 1);
    
    // Import
    EList imports = definition.getEImports();
    Import myImport = (Import) imports.get(0);
    ensureExtensibilityElementRemoved(myImport, 1);
    
    // Type: original is two because it has <annotation> and <schema>
    Types types = definition.getETypes();
    ensureExtensibilityElementRemoved(types, 2);
    
    // Service
    Service service = (Service)definition.getEServices().get(0);
    ensureExtensibilityElementRemoved(service, 1);
    
    // Port
    org.eclipse.wst.wsdl.Port port = (org.eclipse.wst.wsdl.Port)service.getEPorts().get(0);
    ensureExtensibilityElementRemoved(port, 2);
    
    // Binding: original is 2 because it has <annotation> and <soap:binding>
    Binding binding = port.getEBinding();
    ensureExtensibilityElementRemoved(binding, 2);
    
    // Binding Operation: original is 2 because it has <annotation> and <soap:operation>
    List bindingOperations = binding.getBindingOperations();
    BindingOperation bindingOperation = (BindingOperation)bindingOperations.get(0);
    ensureExtensibilityElementRemoved(bindingOperation, 2);    
    
    // Binding Input: original is 2 because it has <annotation> and <soap:body>
    BindingInput bindingInput = bindingOperation.getEBindingInput();
    ensureExtensibilityElementRemoved(bindingInput, 2);
    
    // Binding Output: original is 2 because it has <annotation> and <soap:body>
    BindingOutput bindingOutput = bindingOperation.getEBindingOutput();
    ensureExtensibilityElementRemoved(bindingOutput, 2);
    
    // Binding Fault: original is 2 because it has <annotation> and <soap:fault>    
    EList bindingFaults = bindingOperation.getEBindingFaults();
    BindingFault bindingFault = (BindingFault)bindingFaults.get(0);
    ensureExtensibilityElementRemoved(bindingFault, 2);
    
    // Port Type
    PortType portType = binding.getEPortType();
    ensureExtensibilityElementRemoved(portType, 1);
    
    // Operation
    EList operations = portType.getEOperations();
    Operation operation = (Operation) operations.get(0);
    ensureExtensibilityElementRemoved(operation, 1);
    
    // Output
    Output output = operation.getEOutput();
    ensureExtensibilityElementRemoved(output, 1);
    
    // Input
    Input input = operation.getEInput();
    ensureExtensibilityElementRemoved(input, 1);
    
    // fault 
    EList faults = operation.getEFaults();
    Fault fault = (Fault) faults.get(0);
    ensureExtensibilityElementRemoved(fault, 1);
    
    // Message
    Message message = input.getEMessage();
    ensureExtensibilityElementRemoved(message, 1);
    
    // Part
    EList parts = message.getEParts();
    Part part = (Part) parts.get(0);
    ensureExtensibilityElementRemoved(part, 1);
  }
  
  /**
   * Remove the first UnknownExtensibilityElement. The expected result is original size will decrement by 1
   */
  private void ensureExtensibilityElementRemoved(ExtensibleElement extensibleElement, int originalSize) 
  {
    List extensibilityElements = extensibleElement.getExtensibilityElements();
    assertEquals(originalSize, extensibilityElements.size());
    UnknownExtensibilityElement unknownExtensibilityElement = null;
    Iterator extensibilityElementsIterator = extensibilityElements.iterator();
    while (extensibilityElementsIterator.hasNext())
    {
      Object object = extensibilityElementsIterator.next();
      if (object instanceof UnknownExtensibilityElement) 
      {
        unknownExtensibilityElement = (UnknownExtensibilityElement) object;
        break;
      }
    }

    if (unknownExtensibilityElement == null) 
    {
      fail("Cannot find the UnknownExtensibilityElement.");
    }
    Element element = unknownExtensibilityElement.getElement();
    extensibleElement.getElement().removeChild(element);
    extensibleElement.elementChanged(extensibleElement.getElement());
    assertEquals(originalSize - 1, extensibilityElements.size());
  }
  
  public void testImportsWithNonStandardFileExtension() 
  {
    String WSDL_NS = "http://www.example.org/ImportWithNonStandardWSDLFileExtension/wsdl0/"; //$NON-NLS-1$ 
    String XSD_NS = "http://www.example.org/NonStandardSchemaFileExtension/xsd0"; //$NON-NLS-1$ 
    
    try
    {
      // load a wsdl that imports another WSDL with non-standard file extension (.wsdl0) which in turn
      // imports a XSD with non-standard file extension (.xsd0)
      // ImportWithNonStandardFileExtension.wsdl also imports a XSD (NonStandardSchemaFileExtension.xsd1) using <wsdl:import>
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/ImportsWithNonStandardFileExtension/ImportWithNonStandardFileExtension.wsdl", true); //$NON-NLS-1$
      
      // there should only be two valid <wsdl:import>s - NonStandardWSDLFileExtension.wsdl0 and NonStandardSchemaFileExtension.xsd1
      EList imports = definition.getEImports();
      assertEquals("Incorrect number of imports", 2, imports.size()); //$NON-NLS-1$
      
      
      for (int i = 0; i < imports.size(); i++) {
        Import myImport = (Import) imports.get(i);
        assertTrue("Incorrect imported namespace", WSDL_NS.equals(myImport.getNamespaceURI()) || XSD_NS.equals(myImport.getNamespaceURI())); //$NON-NLS-1$ 
        if (WSDL_NS.equals(myImport.getNamespaceURI())) {
       // WSDL import: make sure the binding in the imported NonStandardWSDLFileExtension.wsdl0 is resolved
          Definition importedDefinition = myImport.getEDefinition(); 
          assertNotNull(importedDefinition);
          Map bindings = importedDefinition.getBindings();
          assertEquals("Incorrect number of binding elements in imported WSDL", 1, bindings.size());  //$NON-NLS-1$
          
          // Go to the resolved "NewType" complex type element and reads its testXSD0 attribute, and verify it's accessible. 
          List schemas = importedDefinition.getETypes().getSchemas();
          assertEquals(1, schemas.size());
          XSDSchema schema = (XSDSchema)schemas.get(0);
          EList types = schema.getTypeDefinitions();
          assertEquals("Incorrect number of types definitions in the inline schema of the imported WSDL", 1, types.size());  //$NON-NLS-1$
          Object type = types.get(0);
          assertTrue("Not complex type", type instanceof XSDComplexTypeDefinition);
          XSDComplexTypeDefinition complexTypeDefinition = (XSDComplexTypeDefinition) type;
          assertEquals("Incorrect name for the ComplexType imported from NonStandardSchemaFileExtension.xsd0", "NewType", complexTypeDefinition.getName());  //$NON-NLS-1$ $NON-NLS-2$
          String testAttribute = complexTypeDefinition.getElement().getAttribute("testXSD0");  
          assertEquals("Incorrect test attribute for the ComplexType imported from NonStandardSchemaFileExtension.xsd0", "passed", testAttribute); //$NON-NLS-1$ $NON-NLS-2$
        } else {
       // schema import: make sure the complex type in the imported NonStandardSchemaFileExtension.xsd1 is resolved
          // Go to the resolved "ImportedTypeViaWSDLImport" complex type element and reads its testXSD1 attribute, and verify it's accessible.
          XSDSchema schema = myImport.getESchema();
          EList types = schema.getTypeDefinitions();
          assertEquals("Incorrect number of types definitions in imported XSD", 1, types.size());  //$NON-NLS-1$
          
          Object type = types.get(0);
          assertTrue("Not complex type", type instanceof XSDComplexTypeDefinition);
          XSDComplexTypeDefinition complexTypeDefinition = (XSDComplexTypeDefinition) type;
          assertEquals("Incorrect name for the ComplexType imported from NonStandardSchemaFileExtension.xsd1", "ImportedTypeViaWSDLImport", complexTypeDefinition.getName());  //$NON-NLS-1$ $NON-NLS-2$
          String testAttribute = complexTypeDefinition.getElement().getAttribute("testXSD1");  //$NON-NLS-1$
          assertEquals("Incorrect test attribute for the ComplexType imported from NonStandardSchemaFileExtension.xsd1", "passed", testAttribute);  //$NON-NLS-1$ $NON-NLS-2$
        }
      } 
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }      
  }
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=314929
   */
  public void testSupportsLocalDefaultNamespace()
  {
    Definition definition = null;

    try
    {
      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/LocalNamespace/LocalDefaultNamespace.wsdl"); //$NON-NLS-1$
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    }

    String targetNamespace = "http://www.example.org/LocalDefaultNamespace"; //$NON-NLS-1$

    // Check that the response message's part element is resolved OK.

    QName responseMessageQName = new QName(targetNamespace, "TestResponseMessage"); //$NON-NLS-1$
    javax.wsdl.Message responseMessage = definition.getMessage(responseMessageQName);

    Part responsePart = (Part)responseMessage.getPart("parameters"); //$NON-NLS-1$

    XSDElementDeclaration responseElementDeclaration = responsePart.getElementDeclaration();

    assertNotNull(responseElementDeclaration);
    assertNotNull(responseElementDeclaration.getContainer());

    // Check that the request message's part element is resolved OK.

    QName requestMessageQName = new QName(targetNamespace, "TestRequestMessage"); //$NON-NLS-1$
    javax.wsdl.Message requestMessage = definition.getMessage(requestMessageQName);

    Part requestPart = (Part)requestMessage.getPart("parameters"); //$NON-NLS-1$

    XSDElementDeclaration requestElementDeclaration = requestPart.getElementDeclaration();

    assertNotNull(requestElementDeclaration);

    // Now to make sure the DOM is reconciled properly and uses the local namespace prefix, 
    // let's try to change the part's element declaration. We'll use the response part element
    // just because it is convenient.

    requestPart.setElementDeclaration(responseElementDeclaration);

    Element partElement = requestPart.getElement();
    String elementAttributeValue = partElement.getAttribute(WSDLConstants.ELEMENT_ATTRIBUTE);

    assertEquals(elementAttributeValue, responseElementDeclaration.getName());
  }  
  
  /**
   * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=322954
   */
  public void testReconcilesSOAPBodyPartsInMIMEBinding()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/SOAPBodyReconciliationForMIME/SOAPBodyForMIME.wsdl", true); //$NON-NLS-1$

      checkSOAPBodyPartsInMIMEBinding(definition);

      definition.updateElement(true);

      checkSOAPBodyPartsInMIMEBinding(definition);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      fail();
    }    
  }

  /**
   * Called from {@link #testReconcilesSOAPBodyPartsInMIMEBinding()}
   * @param definition the input WSDL definition.
   */
  private void checkSOAPBodyPartsInMIMEBinding(Definition definition)
  {
    // Navigate to the SOAP body element.

    Binding binding = (Binding)definition.getEBindings().get(0);
    List bindingOperations = binding.getBindingOperations();
    BindingOperation bindingOperation = (BindingOperation)bindingOperations.get(0);
    javax.wsdl.BindingInput bindingInput = bindingOperation.getBindingInput();
    List extensibilityElements = bindingInput.getExtensibilityElements();
    MIMEMultipartRelated multipartRelated = (MIMEMultipartRelated)extensibilityElements.get(0);
    EList mimeParts = multipartRelated.getEMIMEPart();
    MIMEPart mimePart = (MIMEPart)mimeParts.get(0);
    List mimePartExtensibilityElements = mimePart.getExtensibilityElements();
    SOAPBody soapBody = (SOAPBody) mimePartExtensibilityElements.get(0);

    // The SOAP body has only one part

    List parts = soapBody.getParts();
    assertEquals(1, parts.size());

    // The part name is part1

    Part part = (Part)parts.get(0);
    String expectedPartName = "part1"; //$NON-NLS-1$
    assertEquals(expectedPartName, part.getName());

    // The DOM reflects the model structure.

    Element element = soapBody.getElement();
    String partsAttributeValue = element.getAttribute(SOAPConstants.PARTS_ATTRIBUTE);
    assertEquals(expectedPartName, partsAttributeValue); //$NON-NLS-1$
  }
}