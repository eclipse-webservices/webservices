/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.wst.wsdl.tests.extensions;


import java.util.ArrayList;
import java.util.List;

import javax.wsdl.extensions.ExtensibilityElement;
import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeader;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPOperation;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.generator.SOAPContentGenerator;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.generator.extension.ContentGeneratorExtensionFactoryRegistry;
import org.eclipse.wst.wsdl.tests.WSDLTestsPlugin;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * Tests the SOAP binding extensions. 
 */
public class SOAPExtensionsTest extends WSDLExtensionsTest
{
  private static final String HTTP_TRANSPORT_URI = "http://schemas.xmlsoap.org/soap/http"; //$NON-NLS-1$

  private static final String LOCATION_URI = "http://www.example.org/"; //$NON-NLS-1$

  private static final String PLUGIN_ABSOLUTE_PATH = WSDLTestsPlugin.getInstallURL();

  private static final String SOAP_ACTION_URI = "http://www.example.org/SOAPTest/NewOperation";

  private static final SOAPFactory SOAP_FACTORY = SOAPFactory.eINSTANCE;

  private static final String STYLE_DOCUMENT = "document"; //$NON-NLS-1$

  private static final String STYLE_RPC = "rpc"; //$NON-NLS-1$

  private static final String USE_LITERAL = "literal"; //$NON-NLS-1$

  private static final String USE_ENCODED = "encoded"; //$NON-NLS-1$

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(new SOAPExtensionsTest("SOAPExtensionsCreation") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testSOAPExtensionsCreation();
        }
      });

    suite.addTest(new SOAPExtensionsTest("EMFSerialization") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testEMFSerialization();
        }
      });

    suite.addTest(new SOAPExtensionsTest("SOAPExtensionsReconciliation") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testSOAPExtensionsReconciliation();
        }
      });

    suite.addTest(new SOAPExtensionsTest("SOAPContentGenerator") //$NON-NLS-1$
      {
        protected void runTest()
        {
          testSOAPContentGenerator();
        }
      });

    return suite;
  }

  public SOAPExtensionsTest(String name)
  {
    super(name);
  }

  public void testSOAPExtensionsCreation()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/Extensions/SOAP/SOAPTest.wsdl"); //$NON-NLS-1$

      String bindingName = "SOAPTestSOAP"; //$NON-NLS-1$
      String targetNamespace = "http://www.example.org/SOAPTest/"; //$NON-NLS-1$
      QName bindingQName = new QName(targetNamespace, bindingName);
      Binding binding = (Binding)definition.getBinding(bindingQName);

      addSOAPBinding(binding);

      BindingOperation bindingOperation = (BindingOperation)binding.getBindingOperation("NewOperation", null, null); //$NON-NLS-1$

      addSOAPOperation(bindingOperation);

      BindingInput bindingInput = bindingOperation.getEBindingInput();

      addSOAPBindingInput(bindingInput);

      BindingOutput bindingOutput = bindingOperation.getEBindingOutput();

      addSOAPBindingOutput(bindingOutput);

      String faultName = "fault"; //$NON-NLS-1$
      BindingFault bindingFault = (BindingFault)bindingOperation.getBindingFault(faultName);

      addSOAPBindingFault(bindingFault);

      addSOAPAddress(definition);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
  }

  public void testSOAPContentGenerator()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/Extensions/SOAP/SOAPTest.wsdl"); //$NON-NLS-1$

      SOAPContentGenerator contentGenerator = (SOAPContentGenerator)ContentGeneratorExtensionFactoryRegistry.getInstance().getGeneratorClassFromName(
        "SOAP");
      String locationURI = "http://test.org/example"; //$NON-NLS-1$
      contentGenerator.setAddressLocation(locationURI);
      contentGenerator.setStyle(SOAPContentGenerator.STYLE_DOCUMENT);

      String serviceName = "SOAPTest"; //$NON-NLS-1$
      String targetNamespace = "http://www.example.org/SOAPTest/"; //$NON-NLS-1$ 
      QName serviceQName = new QName(targetNamespace, serviceName);
      Service service = (Service)definition.getService(serviceQName);

      Port port = (Port)service.getPort("SOAPTestSOAP"); //$NON-NLS-1$
      List extensibilityElements = port.getExtensibilityElements();
      assertEquals(0, extensibilityElements.size());
      contentGenerator.generatePortContent(port);

      extensibilityElements = port.getExtensibilityElements();
      assertEquals(1, extensibilityElements.size());
      ExtensibilityElement extensibilityElement = (ExtensibilityElement)extensibilityElements.get(0);
      assertTrue(extensibilityElement instanceof SOAPAddress);
      SOAPAddress soapAddress = (SOAPAddress)extensibilityElement;
      assertEquals(locationURI, soapAddress.getLocationURI());

      QName bindingQName = new QName(targetNamespace, "SOAPTestSOAP"); //$NON-NLS-1$
      Binding binding = (Binding)definition.getBinding(bindingQName);

      QName portTypeQName = new QName(targetNamespace, "SOAPTest"); //$NON-NLS-1$
      PortType portType = (PortType)definition.getPortType(portTypeQName);

      contentGenerator.generateBindingContent(binding, portType);

      // TODO Complete this test.
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
  }

  /**
   * This method loads a WSDL model then saves it using the default EMF serialization
   * instead of the WSDL XML format, then loads it again. The intent is to exercise
   * the EMF e* methods to aid in identifying real code coverage issues.  
   */
  public void testEMFSerialization()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/Extensions/SOAP/DocumentLiteralSOAPExample.wsdl", true); //$NON-NLS-1$

      ResourceSet resourceSet = new ResourceSetImpl();
      URI fileURI = URI.createFileURI(PLUGIN_ABSOLUTE_PATH + "samples/generated/DocumentLiteralSOAPExample.xml");
      Resource resource = resourceSet.createResource(fileURI);
      resource.getContents().add(definition);
      resource.save(null);
      resourceSet = new ResourceSetImpl();
      resource = resourceSet.getResource(fileURI, false);

      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/Extensions/SOAP/RPCLiteralSOAPExample.wsdl", true); //$NON-NLS-1$
      fileURI = URI.createFileURI(PLUGIN_ABSOLUTE_PATH + "samples/generated/RPCLiteralSOAPExample.xml");
      resourceSet = new ResourceSetImpl();
      resource = resourceSet.createResource(fileURI);
      resource.getContents().add(definition);
      resource.save(null);
      resourceSet = new ResourceSetImpl();
      resource = resourceSet.getResource(fileURI, true);

      definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/Extensions/SOAP/RPCEncodedSOAPExample.wsdl", true); //$NON-NLS-1$
      fileURI = URI.createFileURI(PLUGIN_ABSOLUTE_PATH + "samples/generated/RPCEncodedSOAPExample.xml");
      resourceSet = new ResourceSetImpl();
      resource = resourceSet.createResource(fileURI);
      resource.getContents().add(definition);
      resource.save(null);
      resourceSet = new ResourceSetImpl();
      resource = resourceSet.getResource(fileURI, true);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
  }

  public void testSOAPExtensionsReconciliation()
  {
    try
    {
      Definition definition = DefinitionLoader.load(PLUGIN_ABSOLUTE_PATH + "samples/Extensions/SOAP/DocumentLiteralSOAPExample.wsdl", true); //$NON-NLS-1$

      String serviceName = "DocumentLiteralSOAPExample"; //$NON-NLS-1$
      String targetNamespace = "http://www.example.org/DocumentLiteralSOAPExample/"; //$NON-NLS-1$
      QName serviceQName = new QName(targetNamespace, serviceName);
      Service service = (Service)definition.getService(serviceQName);
      service.toString();

      Port port = (Port)service.getPort("DocumentLiteralSOAPExampleSOAP"); //$NON-NLS-1$
      List extensibilityElements = port.getExtensibilityElements();
      assertEquals(1, extensibilityElements.size());
      SOAPAddress soapAddress = (SOAPAddress)extensibilityElements.get(0);

      checkStringAttributeReconciliation(soapAddress, SOAPConstants.LOCATION_ATTRIBUTE, "http://www.example.org/", SOAPPackage.Literals.SOAP_ADDRESS__LOCATION_URI);

      String bindingName = "DocumentLiteralSOAPExampleSOAP"; //$NON-NLS-1$
      QName bindingQName = new QName(targetNamespace, bindingName);
      Binding binding = (Binding)definition.getBinding(bindingQName);

      extensibilityElements = binding.getExtensibilityElements();
      assertEquals(1, extensibilityElements.size());
      SOAPBinding soapBinding = (SOAPBinding)extensibilityElements.get(0);

      String style = soapBinding.getStyle();
      assertEquals(STYLE_DOCUMENT, style);

      checkStringAttributeReconciliation(soapBinding, SOAPConstants.STYLE_ATTRIBUTE, STYLE_RPC, SOAPPackage.Literals.SOAP_BINDING__STYLE);

      BindingOperation bindingOperation = (BindingOperation)binding.getBindingOperation("NewOperation", null, null);
      extensibilityElements = bindingOperation.getExtensibilityElements();
      assertEquals(1, extensibilityElements.size());
      SOAPOperation soapOperation = (SOAPOperation)extensibilityElements.get(0);

      style = soapOperation.getStyle();
      assertEquals(STYLE_DOCUMENT, style);

      checkStringAttributeReconciliation(soapOperation, SOAPConstants.STYLE_ATTRIBUTE, STYLE_RPC, SOAPPackage.Literals.SOAP_OPERATION__STYLE);

      BindingInput bindingInput = (BindingInput)bindingOperation.getBindingInput();
      extensibilityElements = bindingInput.getExtensibilityElements();
      assertEquals(2, extensibilityElements.size());

      SOAPBody soapBody = (SOAPBody)extensibilityElements.get(0);

      String use = soapBody.getUse();
      assertEquals(USE_LITERAL, use);

      checkStringAttributeReconciliation(soapBody, SOAPConstants.USE_ATTRIBUTE, USE_ENCODED, SOAPPackage.Literals.SOAP_BODY__USE);

      SOAPHeader soapHeader = (SOAPHeader)extensibilityElements.get(1);

      String part = soapHeader.getPart();
      assertEquals("header", part);

      checkStringAttributeReconciliation(soapHeader, SOAPConstants.PART_ATTRIBUTE, "test", SOAPPackage.Literals.SOAP_HEADER_BASE__PART);

      QName inputMessageQName = new QName(targetNamespace, "NewOperationRequestMsg");
      Message inputMessage = (Message)definition.getMessage(inputMessageQName);
      assertNotNull(inputMessage);

      QName soapHeaderMessageQName = soapHeader.getMessage();
      assertEquals(inputMessageQName, soapHeaderMessageQName);

      Element soapHeaderElement = soapHeader.getElement();
      Attr messageAttribute = soapHeaderElement.getAttributeNode(SOAPConstants.MESSAGE_ATTRIBUTE);

      QName testMessageQName = new QName(targetNamespace, "TestMsg");
      Message testMessage = (Message)definition.getMessage(testMessageQName);
      messageAttribute.setValue("tns:TestMsg");
      assertEquals(testMessage, soapHeader.getEMessage());

      QName message = soapHeader.getMessage();
      assertEquals(testMessageQName, message);

      soapHeader.setMessage(null);
      messageAttribute = soapHeaderElement.getAttributeNode(SOAPConstants.MESSAGE_ATTRIBUTE);
      assertNull(messageAttribute);

      List soapHeaderFaults = soapHeader.getSOAPHeaderFaults();
      assertEquals(1, soapHeaderFaults.size());
      soapHeaderFaults.clear();
      NodeList headerFaultNodes = soapHeaderElement.getElementsByTagName(SOAPConstants.HEADER_FAULT_ELEMENT_TAG);
      assertEquals(0, headerFaultNodes.getLength());

      Document document = soapHeaderElement.getOwnerDocument();
      Element soapHeaderFaultElement = document.createElementNS(SOAPConstants.SOAP_NAMESPACE_URI, SOAPConstants.HEADER_FAULT_ELEMENT_TAG);
      soapHeaderElement.appendChild(soapHeaderFaultElement);
      assertEquals(1, soapHeader.getSOAPHeaderFaults().size());
      soapHeaderFaultElement.setAttribute(SOAPConstants.MESSAGE_ATTRIBUTE, "tns:TestMsg");
      SOAPHeaderFault soapHeaderFault = (SOAPHeaderFault)soapHeader.getSOAPHeaderFaults().get(0);
      assertEquals(testMessageQName, soapHeaderFault.getMessage());
      assertEquals(testMessage, soapHeaderFault.getEMessage());

      BindingFault bindingFault = (BindingFault)bindingOperation.getBindingFault("fault");
      extensibilityElements = bindingFault.getExtensibilityElements();
      assertEquals(1, extensibilityElements.size());
      SOAPFault soapFault = (SOAPFault)extensibilityElements.get(0);

      String name = soapFault.getName();
      assertEquals("fault", name);

      checkStringAttributeReconciliation(soapFault, SOAPConstants.NAME_ATTRIBUTE, "test", SOAPPackage.Literals.SOAP_FAULT__NAME);
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage()); //$NON-NLS-1$
    }
  }
  
  private void addSOAPAddress(Definition definition)
  {
    SOAPAddress soapAddress = SOAP_FACTORY.createSOAPAddress();
    soapAddress.setLocationURI(LOCATION_URI);

    soapAddress.toString();

    String serviceName = "SOAPTest"; //$NON-NLS-1$
    QName serviceQName = new QName(definition.getTargetNamespace(), serviceName);
    Service service = (Service)definition.getService(serviceQName);
    service.toString();

    Port port = (Port)service.getPort("SOAPTestSOAP"); //$NON-NLS-1$
    port.addExtensibilityElement(soapAddress);
    port.toString();
  }

  private void addSOAPBinding(Binding binding)
  {
    SOAPBinding soapBinding = SOAP_FACTORY.createSOAPBinding();

    soapBinding.setStyle(STYLE_DOCUMENT);
    soapBinding.setTransportURI(HTTP_TRANSPORT_URI);

    soapBinding.toString();

    binding.addExtensibilityElement(soapBinding);
    binding.toString();
  }

  private void addSOAPBindingFault(BindingFault bindingFault)
  {
    String faultName = bindingFault.getName();

    SOAPFault soapFault = SOAP_FACTORY.createSOAPFault();
    soapFault.setName(faultName);
    soapFault.toString();

    soapFault.setUse(USE_LITERAL);

    bindingFault.addExtensibilityElement(soapFault);
    bindingFault.toString();
  }

  private void addSOAPBindingInput(BindingInput bindingInput)
  {
    bindingInput.toString();

    SOAPBody inputSOAPBody = SOAP_FACTORY.createSOAPBody();
    bindingInput.addExtensibilityElement(inputSOAPBody);

    inputSOAPBody.setUse(USE_LITERAL);

    List parts = new ArrayList();
    String messageName = "NewOperationRequest"; //$NON-NLS-1$
    Definition enclosingDefinition = bindingInput.getEnclosingDefinition();
    String targetNamespace = enclosingDefinition.getTargetNamespace();
    QName messageQName = new QName(targetNamespace, messageName);
    Definition definition = enclosingDefinition;
    Message message = (Message)definition.getMessage(messageQName);
    Part part = (Part)message.getPart("parameters"); //$NON-NLS-1$
    parts.add(part);
    inputSOAPBody.setParts(parts);
    inputSOAPBody.toString();

    SOAPHeader soapHeader = SOAP_FACTORY.createSOAPHeader();
    bindingInput.addExtensibilityElement(soapHeader);

    soapHeader.setMessage(messageQName);

    String headerPart = "header"; //$NON-NLS-1$
    soapHeader.setPart(headerPart);

    soapHeader.setUse(USE_LITERAL);

    soapHeader.toString();

    addSOAPHeaderFault(messageQName, soapHeader);
  }

  private void addSOAPBindingOutput(BindingOutput bindingOutput)
  {
    // Exercise some of the RPC / encoded stuff.

    SOAPBody soapBody = SOAP_FACTORY.createSOAPBody();
    soapBody.setUse(USE_ENCODED);

    EList encodingStyles = new BasicEList();
    encodingStyles.add("http://schemas.xmlsoap.org/soap/encoding/");
    encodingStyles.add("test");
    
    soapBody.setEncodingStyles(encodingStyles);
    
    List eEncodingStyles = soapBody.getEEncodingStyles();
    assertEquals(2, eEncodingStyles.size());
    
    soapBody.toString();

    bindingOutput.toString();
    bindingOutput.addExtensibilityElement(soapBody);
  }

  private void addSOAPHeaderFault(QName messageQName, SOAPHeader soapHeader)
  {
    SOAPHeaderFault soapHeaderFault = SOAP_FACTORY.createSOAPHeaderFault();
    soapHeader.addSOAPHeaderFault(soapHeaderFault);

    List soapHeaderFaults = soapHeader.getSOAPHeaderFaults();
    assertEquals(1, soapHeaderFaults.size());
    SOAPHeaderFault expectedSoapHeaderFault = (SOAPHeaderFault)soapHeaderFaults.get(0);
    assertEquals(expectedSoapHeaderFault, soapHeaderFault);

    soapHeaderFault.setMessage(messageQName);

    String headerFaultPart = "headerFault1"; //$NON-NLS-1$
    soapHeaderFault.setPart(headerFaultPart);

    // Test the remove part.

    soapHeader.getSOAPHeaderFaults().clear();
  }

  private void addSOAPOperation(BindingOperation bindingOperation)
  {
    SOAPOperation soapOperation = SOAP_FACTORY.createSOAPOperation();
    soapOperation.setStyle(STYLE_DOCUMENT);
    soapOperation.setSoapActionURI(SOAP_ACTION_URI);

    soapOperation.toString();

    bindingOperation.addExtensibilityElement(soapOperation);
    bindingOperation.toString();
  }
}