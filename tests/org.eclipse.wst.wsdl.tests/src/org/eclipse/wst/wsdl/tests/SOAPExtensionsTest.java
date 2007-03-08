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

package org.eclipse.wst.wsdl.tests;


import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
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
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;

/**
 * Tests the SOAP binding extensions. 
 */
public class SOAPExtensionsTest extends TestCase
{
  private static final String HTTP_TRANSPORT_URI = "http://schemas.xmlsoap.org/soap/http"; //$NON-NLS-1$

  private static final String LOCATION_URI = "http://www.example.org/"; //$NON-NLS-1$

  private static final String PLUGIN_ABSOLUTE_PATH = WSDLTestsPlugin.getInstallURL();

  private static final String SOAP_ACTION_URI = "http://www.example.org/SOAPTest/NewOperation";

  private static final SOAPFactory SOAP_FACTORY = SOAPFactory.eINSTANCE;

  private static final String STYLE_DOCUMENT = "document"; //$NON-NLS-1$

  private static final String TARGET_NAMESPACE = "http://www.example.org/SOAPTest/"; //$NON-NLS-1$

  private static final String USE_LITERAL = "literal"; //$NON-NLS-1$

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
      QName bindingQName = new QName(TARGET_NAMESPACE, bindingName);
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

  private void addSOAPAddress(Definition definition)
  {
    SOAPAddress soapAddress = SOAP_FACTORY.createSOAPAddress();
    EObject soapAddressEObject = (EObject)soapAddress;
    soapAddress.setLocationURI(LOCATION_URI);
    String locationURI = (String)soapAddressEObject.eGet(SOAPPackage.Literals.SOAP_ADDRESS__LOCATION_URI);
    assertEquals(LOCATION_URI, locationURI);
    assertTrue(soapAddressEObject.eIsSet(SOAPPackage.Literals.SOAP_ADDRESS__LOCATION_URI));

    soapAddress.toString();

    String serviceName = "SOAPTest"; //$NON-NLS-1$
    QName serviceQName = new QName(TARGET_NAMESPACE, serviceName);
    Service service = (Service)definition.getService(serviceQName);
    service.toString();

    Port port = (Port)service.getPort("SOAPTestSOAP"); //$NON-NLS-1$
    port.addExtensibilityElement(soapAddress);
    port.toString();
  }

  private void addSOAPBinding(Binding binding)
  {
    SOAPBinding soapBinding = SOAP_FACTORY.createSOAPBinding();
    EObject soapBindingEObject = ((EObject)soapBinding);

    soapBinding.setStyle(STYLE_DOCUMENT);
    String style = (String)soapBindingEObject.eGet(SOAPPackage.Literals.SOAP_BINDING__STYLE);
    assertEquals(STYLE_DOCUMENT, style);
    assertTrue(soapBindingEObject.eIsSet(SOAPPackage.Literals.SOAP_BINDING__STYLE));

    soapBinding.setTransportURI(HTTP_TRANSPORT_URI);
    String transportURI = (String)soapBindingEObject.eGet(SOAPPackage.Literals.SOAP_BINDING__TRANSPORT_URI);
    assertEquals(HTTP_TRANSPORT_URI, transportURI);
    assertTrue(soapBindingEObject.eIsSet(SOAPPackage.Literals.SOAP_BINDING__TRANSPORT_URI));

    soapBinding.toString();

    binding.addExtensibilityElement(soapBinding);
    binding.toString();
  }

  private void addSOAPBindingFault(BindingFault bindingFault)
  {
    String faultName = bindingFault.getName();

    SOAPFault soapFault = SOAP_FACTORY.createSOAPFault();
    EObject soapFaultEObject = (EObject)soapFault;
    soapFault.setName(faultName);
    soapFault.toString();
    String expectedFaultName = (String)soapFaultEObject.eGet(SOAPPackage.Literals.SOAP_FAULT__NAME);
    assertEquals(faultName, expectedFaultName);
    assertTrue(soapFaultEObject.eIsSet(SOAPPackage.Literals.SOAP_FAULT__NAME));

    soapFault.setUse(USE_LITERAL);
    String use = (String)soapFaultEObject.eGet(SOAPPackage.Literals.SOAP_FAULT__USE);
    assertEquals(USE_LITERAL, use);
    assertTrue(soapFaultEObject.eIsSet(SOAPPackage.Literals.SOAP_FAULT__USE));

    bindingFault.addExtensibilityElement(soapFault);
    bindingFault.toString();
  }

  private void addSOAPBindingInput(BindingInput bindingInput)
  {
    bindingInput.toString();
    
    SOAPBody inputSOAPBody = SOAP_FACTORY.createSOAPBody();
    bindingInput.addExtensibilityElement(inputSOAPBody);

    EObject inputSOAPBodyEObject = (EObject)inputSOAPBody;
    inputSOAPBody.setUse(USE_LITERAL);
    String use = (String)inputSOAPBodyEObject.eGet(SOAPPackage.Literals.SOAP_BODY__USE);
    assertEquals(USE_LITERAL, use);
    assertTrue(inputSOAPBodyEObject.eIsSet(SOAPPackage.Literals.SOAP_BODY__USE));

    List parts = new ArrayList();
    String messageName = "NewOperationRequest"; //$NON-NLS-1$
    QName messageQName = new QName(TARGET_NAMESPACE, messageName);
    Definition definition = bindingInput.getEnclosingDefinition();
    Message message = (Message)definition.getMessage(messageQName);
    Part part = (Part)message.getPart("parameters"); //$NON-NLS-1$
    parts.add(part);
    inputSOAPBody.setParts(parts);
    inputSOAPBody.toString();

    SOAPHeader soapHeader = SOAP_FACTORY.createSOAPHeader();
    bindingInput.addExtensibilityElement(soapHeader);
    EObject soapHeaderEObject = (EObject)soapHeader;

    soapHeader.setMessage(messageQName);
    QName actualMessageQName = (QName)soapHeaderEObject.eGet(SOAPPackage.Literals.SOAP_HEADER_BASE__MESSAGE);
    assertEquals(messageQName, actualMessageQName);
    assertTrue(soapHeaderEObject.eIsSet(SOAPPackage.Literals.SOAP_HEADER_BASE__MESSAGE));

    String headerPart = "header"; //$NON-NLS-1$
    soapHeader.setPart(headerPart);
    String actualPart = (String)soapHeaderEObject.eGet(SOAPPackage.Literals.SOAP_HEADER_BASE__PART);
    assertEquals(headerPart, actualPart);
    assertTrue(soapHeaderEObject.eIsSet(SOAPPackage.Literals.SOAP_HEADER_BASE__PART));

    soapHeader.setUse(USE_LITERAL);
    String faultUse = (String)soapHeaderEObject.eGet(SOAPPackage.Literals.SOAP_HEADER_BASE__USE);
    assertEquals(USE_LITERAL, faultUse);
    assertTrue(soapHeaderEObject.eIsSet(SOAPPackage.Literals.SOAP_HEADER_BASE__USE));
    
    soapHeader.toString();

    addSOAPHeaderFault(messageQName, soapHeader);
  }

  private void addSOAPBindingOutput(BindingOutput bindingOutput)
  {
    SOAPBody outputSOAPBody = SOAP_FACTORY.createSOAPBody();
    outputSOAPBody.setUse(USE_LITERAL);
    outputSOAPBody.toString();

    bindingOutput.toString();
    bindingOutput.addExtensibilityElement(outputSOAPBody);
  }

  private void addSOAPHeaderFault(QName messageQName, SOAPHeader soapHeader)
  {
    SOAPHeaderFault soapHeaderFault = SOAP_FACTORY.createSOAPHeaderFault();
    soapHeader.addSOAPHeaderFault(soapHeaderFault);
    
    List soapHeaderFaults = soapHeader.getSOAPHeaderFaults();
    assertEquals(1, soapHeaderFaults.size());
    SOAPHeaderFault expectedSoapHeaderFault = (SOAPHeaderFault)soapHeaderFaults.get(0);
    assertEquals(expectedSoapHeaderFault, soapHeaderFault);
    
    EObject soapHeaderFaultEObject = (EObject)soapHeaderFault;
    
    soapHeaderFault.setMessage(messageQName);
    QName actualFaultMessageQName = (QName)soapHeaderFaultEObject.eGet(SOAPPackage.Literals.SOAP_HEADER_BASE__MESSAGE);
    assertEquals(messageQName, actualFaultMessageQName);
    assertTrue(soapHeaderFaultEObject.eIsSet(SOAPPackage.Literals.SOAP_HEADER_BASE__MESSAGE));

    String headerFaultPart = "headerFault1"; //$NON-NLS-1$
    soapHeaderFault.setPart(headerFaultPart);
    String actualHeaderFaultPart = (String)soapHeaderFaultEObject.eGet(SOAPPackage.Literals.SOAP_HEADER_BASE__PART);
    assertEquals(headerFaultPart, actualHeaderFaultPart);
    assertTrue(soapHeaderFaultEObject.eIsSet(SOAPPackage.Literals.SOAP_HEADER_BASE__PART));
  }

  private void addSOAPOperation(BindingOperation bindingOperation)
  {
    SOAPOperation soapOperation = SOAP_FACTORY.createSOAPOperation();
    EObject soapOperationEObject = (EObject)soapOperation;
    soapOperation.setStyle(STYLE_DOCUMENT);
    String style = (String)soapOperationEObject.eGet(SOAPPackage.Literals.SOAP_OPERATION__STYLE);
    assertEquals(STYLE_DOCUMENT, style);
    assertTrue(soapOperationEObject.eIsSet(SOAPPackage.Literals.SOAP_OPERATION__STYLE));
    soapOperation.setSoapActionURI(SOAP_ACTION_URI);
    String actionURI = (String)soapOperationEObject.eGet(SOAPPackage.Literals.SOAP_OPERATION__SOAP_ACTION_URI);
    assertEquals(SOAP_ACTION_URI, actionURI);
    assertTrue(soapOperationEObject.eIsSet(SOAPPackage.Literals.SOAP_OPERATION__SOAP_ACTION_URI));

    soapOperation.toString();

    bindingOperation.addExtensibilityElement(soapOperation);
    bindingOperation.toString();
  }
}