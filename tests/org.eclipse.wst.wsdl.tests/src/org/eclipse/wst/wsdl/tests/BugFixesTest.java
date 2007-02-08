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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.wsdl.OperationType;
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
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.Types;
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
import org.eclipse.wst.wsdl.util.WSDLConstants;
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

    return suite;
  }

  protected void setUp() throws Exception
  {
    super.setUp();

    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl()); //$NON-NLS-1$
    WSDLPackage pkg = WSDLPackage.eINSTANCE;
    // Silences unused variable warning.
    pkg.eClass();

    // We need this for XSD <import>.
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl()); //$NON-NLS-1$
    XSDPackage xsdpkg = XSDPackage.eINSTANCE;
    // Silences unused variable warning.
    xsdpkg.eClass();
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

    Part responsePart = (Part) responseMessage.getPart("myOperationResponse"); ////$NON-NLS-1$

    XSDElementDeclaration responseElementDeclaration = responsePart.getElementDeclaration();

    assertNotNull(responseElementDeclaration);
    assertNotNull(responseElementDeclaration.getContainer());

    // Check that the request message's part element is resolved OK.
    // This part defines a local namespace prefix

    QName requestMessageQName = new QName(targetNamespace, "myOperationRequest"); ////$NON-NLS-1$
    javax.wsdl.Message requestMessage = definition.getMessage(requestMessageQName);

    Part requestPart = (Part) requestMessage.getPart("myOperationRequest"); ////$NON-NLS-1$

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
    
    PortType portType = (PortType) definition.getEPortTypes().get(0);
    EList operations = portType.getEOperations();
    
    Operation operation = (Operation) operations.get(0);
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
    Binding binding = (Binding) bindings.get(0);
    List bindingOperations = binding.getBindingOperations();
    BindingOperation bindingOperation = (BindingOperation) bindingOperations.get(0);
    BindingFault bindingFault = (BindingFault) bindingOperation.getBindingFault("Operation1Fault"); //$NON-NLS-1$
    Fault fault = bindingFault.getEFault();
    assertNull(fault);

    // Test the normal case when the operation and binding operation are in
    // synch. In this case the fault defined in the operation should match
    // the one obtained from the binding operation's fault.

    List portTypes = definition.getEPortTypes();
    PortType portType = (PortType) portTypes.get(0);
    EList operations = portType.getEOperations();

    Operation operation = (Operation) operations.get(1);
    javax.wsdl.Fault expectedFault1 = operation.getFault("Operation2Fault1"); //$NON-NLS-1$
    javax.wsdl.Fault expectedFault2 = operation.getFault("Operation2Fault2"); //$NON-NLS-1$

    BindingOperation bindingOperation2 = (BindingOperation) bindingOperations.get(1);

    // Make sure the fault obtained from the binding fault is not null and
    // matches the one in the corresponding operation.
    
    BindingFault bindingFault1 = (BindingFault) bindingOperation2.getBindingFault("Operation2Fault1"); //$NON-NLS-1$
    javax.wsdl.Fault actualFault1 = bindingFault1.getEFault();
    assertNotNull(actualFault1);
    assertEquals(expectedFault1, actualFault1);

    // Make sure the fault obtained from the binding fault is not null and
    // matches the one in the corresponding operation.

    BindingFault bindingFault2 = (BindingFault) bindingOperation2.getBindingFault("Operation2Fault2"); //$NON-NLS-1$
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
      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/BugFixes/BindingOperationReconciliation/BindingOperationReconciliation.wsdl", true); //$NON-NLS-1$
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
}