/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPOperation;
import org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;


public class WSDLGenerationTest extends TestCase
{
  public WSDLGenerationTest()
  {
    init();
  }

  public WSDLGenerationTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(new WSDLGenerationTest("SampleWSDLGeneration")
      {
        protected void runTest()
        {
          testSampleWSDLGeneration();
        }
      });

    return suite;
  }

  public void testSampleWSDLGeneration()
  {
    try
    {
      generateTemperatureService("./TemperatureService.wsdl");
    }
    catch (Exception e)
    {
      Assert.fail("Test failed due to an exception: " + e.getLocalizedMessage());
    }
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

  public void generateTemperatureService(String outputFile) throws Exception
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    Resource resource = resourceSet.createResource(URI.createFileURI(outputFile));
    resourceSet.getResources().add(resource);

    // Create a Definition - Temperature
    Definition definition = WSDLFactory.eINSTANCE.createDefinition();
    definition.setQName(new QName(WSDLConstants.WSDL_NAMESPACE_URI, "Temparature"));
    resource.getContents().add(definition);

    // Target namespace - http://www.temperature.com
    definition.setTargetNamespace("http://www.temperature.com");
    definition.addNamespace("tns", "http://www.temperature.com");

    // Other namespaces - wsdl, soap, xsd
    definition.addNamespace("wsdl", WSDLConstants.WSDL_NAMESPACE_URI);
    definition.addNamespace("xsd", WSDLConstants.SCHEMA_FOR_SCHEMA_URI_2001);
    definition.getNamespaces().put("soap", SOAPConstants.SOAP_NAMESPACE_URI);

    //
    // Let's start building two messages
    //

    // Create a Part - ZipCode
    Part part = WSDLFactory.eINSTANCE.createPart();
    part.setName("ZipCode");
    part.setTypeName(new QName(WSDLConstants.SCHEMA_FOR_SCHEMA_URI_2001, "string"));

    // Create a Message - GetTemperatureInput
    Message inputMessage = WSDLFactory.eINSTANCE.createMessage();
    inputMessage.setQName(new QName(definition.getTargetNamespace(), "GetTemparatureInput"));
    inputMessage.addPart(part);
    definition.addMessage(inputMessage);

    // Create a Part - Temperature
    part = WSDLFactory.eINSTANCE.createPart();
    part.setName("Temperature");
    part.setTypeName(new QName(WSDLConstants.SCHEMA_FOR_SCHEMA_URI_2001, "float"));

    // Create a Message - GetTemperatureOutput
    Message outputMessage = WSDLFactory.eINSTANCE.createMessage();
    outputMessage.setQName(new QName(definition.getTargetNamespace(), "GetTemparatureOutput"));
    outputMessage.addPart(part);
    definition.addMessage(outputMessage);

    //
    // Next, build a PortType
    //

    // Create an Input - GetTemperatureInput
    Input input = WSDLFactory.eINSTANCE.createInput();
    input.setMessage(inputMessage);

    // Create an Output - GetTemperatureOutput
    Output output = WSDLFactory.eINSTANCE.createOutput();
    output.setMessage(outputMessage);

    // Create an Operation - GetTemperatureForZipCode
    Operation operation = WSDLFactory.eINSTANCE.createOperation();
    operation.setName("GetTemperatureForZipCode");
    operation.setInput(input);
    operation.setOutput(output);

    // Create a PortType
    PortType portType = WSDLFactory.eINSTANCE.createPortType();
    portType.setQName(new QName(definition.getTargetNamespace(), "GetTemparatureInfoSOAP"));
    portType.addOperation(operation);
    definition.addPortType(portType);

    //
    // Now, let's work on Binding
    //

    // Create a Binding - GetTemperatureInfoSOAP
    Binding binding = WSDLFactory.eINSTANCE.createBinding();
    binding.setQName(new QName(definition.getTargetNamespace(), "GetTemparatureInfoSOAP"));
    binding.setPortType(portType);
    definition.addBinding(binding);

    // Create a SOAP Binding
    SOAPBinding soapBinding = SOAPFactory.eINSTANCE.createSOAPBinding();
    soapBinding.setStyle("rpc");
    soapBinding.setTransportURI("http://schemas.xmlsoap.org/soap/http");
    binding.addExtensibilityElement(soapBinding);

    // Create a Binding Operation
    BindingOperation bindingOperation = WSDLFactory.eINSTANCE.createBindingOperation();
    bindingOperation.setName("GetTemperatureForZipCode");
    binding.addBindingOperation(bindingOperation);

    // Create a SOAP Operation
    SOAPOperation soapOperation = SOAPFactory.eINSTANCE.createSOAPOperation();
    soapOperation.setSoapActionURI("http://www.temperature.com/GetTemperatureForZipCode");
    bindingOperation.addExtensibilityElement(soapOperation);

    // Create a SOAP Body
    SOAPBody soapBody = SOAPFactory.eINSTANCE.createSOAPBody();
    soapBody.setUse("encoded");
    soapBody.getEncodingStyles().add("http://schemas.xmlsoap.org/soap/encoding/");
    soapBody.setNamespaceURI("http://www.temperature.com/");

    // Add a part (Temperature) to the SOAP body (Bugzilla 108176)
    java.util.Vector v = new java.util.Vector();
    v.add(part);
    soapBody.setParts(v);
    ((SOAPBodyImpl)soapBody).updateElement();

    // Create a Binding Input
    BindingInput bindingInput = WSDLFactory.eINSTANCE.createBindingInput();
    bindingInput.addExtensibilityElement(soapBody);
    bindingOperation.setBindingInput(bindingInput);

    // Create a SOAP Body
    soapBody = SOAPFactory.eINSTANCE.createSOAPBody();
    soapBody.setUse("encoded");
    soapBody.getEncodingStyles().add("http://schemas.xmlsoap.org/soap/encoding/");
    soapBody.setNamespaceURI("http://www.temperature.com/");

    // Create a Binding Output
    BindingOutput bindingOuput = WSDLFactory.eINSTANCE.createBindingOutput();
    bindingOuput.addExtensibilityElement(soapBody);
    bindingOperation.setBindingOutput(bindingOuput);

    //
    // Finally, we are building a Service
    //

    // Create a SOAP Address
    SOAPAddress soapAddress = SOAPFactory.eINSTANCE.createSOAPAddress();
    soapAddress.setLocationURI("http://todo-some-address/");

    // Create a Port - GetTemperatureInfoSOAP
    Port port = WSDLFactory.eINSTANCE.createPort();
    port.setName("GetTemperatureInfoSOAP");
    port.setBinding(binding);
    port.addExtensibilityElement(soapAddress);

    // Create a Service - TemperatureService
    Service service = WSDLFactory.eINSTANCE.createService();
    service.setQName(new QName(definition.getTargetNamespace(), "TemperatureService"));
    service.addPort(port);
    definition.addService(service);

    //
    // Phew, we are done. Let's serialize it.
    //

    resource.save(null);

  }

  private void init()
  {
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl());
    WSDLPackage pkg = WSDLPackage.eINSTANCE;
  }

}
