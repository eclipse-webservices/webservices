/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.tests;


import java.io.FileInputStream;
import java.util.Iterator;

import javax.wsdl.Binding;
import javax.wsdl.BindingFault;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Import;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.Types;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.wst.wsdl.WSDLPlugin;
import org.eclipse.wst.wsdl.tests.util.WSDL4JDefinitionVisitor;
import org.xml.sax.InputSource;


/**
 * @author Kihup Boo
 */
public class WSDL4JAPITest extends WSDL4JDefinitionVisitor
{
  private static String PLUGIN_ABSOLUTE_PATH = WSDLTestsPlugin.getInstallURL();

  private WSDLFactory factory = WSDLPlugin.INSTANCE.createWSDL4JFactory();

  private Definition newDefinition;

  private Message currentMessage;

  private Service currentService;

  private PortType currentPortType;

  private Operation currentOperation;

  private Binding currentBinding;

  private BindingOperation currentBindingOperation;

  // Added for JUnit
  public WSDL4JAPITest(String name)
  {
    super(name);
  }

  /**
   * @param definition
   */
  public WSDL4JAPITest(Definition definition)
  {
    super(definition);
  }

  private void println(String s)
  {
    System.out.println(s);
  }

  protected void visitDefinition(Definition def)
  {
    newDefinition = factory.newDefinition();
    newDefinition.setDocumentationElement(def.getDocumentationElement());
    newDefinition.setQName(def.getQName());
    newDefinition.setTargetNamespace(def.getTargetNamespace());
    newDefinition.setDocumentBaseURI(def.getDocumentBaseURI());

    Iterator iterator = def.getNamespaces().keySet().iterator();
    String prefix = null;
    String namespace = null;

    while (iterator.hasNext())
    {
      prefix = (String)iterator.next();
      namespace = def.getNamespace(prefix);
      newDefinition.addNamespace(prefix, namespace);
    }

    super.visitDefinition(def);
  }

  protected void visitImport(Import wsdlImport)
  {
    Import myImport = newDefinition.createImport();
    newDefinition.addImport(myImport);

    // e.g. <xs:import namespace="http://foo.com" schemaLocation= "bar.xsd"/>
    myImport.setNamespaceURI(wsdlImport.getNamespaceURI());
    myImport.setLocationURI(wsdlImport.getLocationURI());
    myImport.setDocumentationElement(wsdlImport.getDocumentationElement());

    myImport.setDefinition(newDefinition);
  }

  protected void visitTypes(Types types)
  {
    Types myTypes = newDefinition.createTypes();
    myTypes.setDocumentationElement(types.getDocumentationElement());

    /*
     Iterator iterator = types.getExtensibilityElements().iterator();
     ExtensibilityElement extensibilitElement = null;
     while (iterator.hasNext())
     {
     extensibilitElement = (ExtensibilityElement)iterator.next();
     myTypes.addExtensibilityElement(extensibilitElement);
     }*/
    newDefinition.setTypes(myTypes);
  }

  protected void visitPart(Part part)
  {
    Part myPart = newDefinition.createPart();
    myPart.setDocumentationElement(part.getDocumentationElement());
    myPart.setName(part.getName());
    myPart.setElementName(part.getElementName());
    myPart.setTypeName(part.getTypeName());

    Iterator iterator = part.getExtensionAttributes().keySet().iterator();
    QName key = null;
    QName value = null;
    while (iterator.hasNext())
    {
      key = (QName)iterator.next();
      value = (QName)part.getExtensionAttribute(key);
      myPart.setExtensionAttribute(key, value);
    }
    currentMessage.addPart(myPart);
  }

  protected void visitMessage(Message message)
  {
    currentMessage = newDefinition.createMessage();
    super.visitMessage(message);
  }

  protected void visitPortType(PortType portType)
  {
    currentPortType = newDefinition.createPortType();
    currentPortType.setDocumentationElement(portType.getDocumentationElement());
    currentPortType.setQName(portType.getQName());
    currentPortType.setUndefined(portType.isUndefined());
    newDefinition.addPortType(currentPortType);

    super.visitPortType(portType);
  }

  protected void visitOperation(Operation operation)
  {
    currentOperation = newDefinition.createOperation();
    currentOperation.setDocumentationElement(operation.getDocumentationElement());
    currentOperation.setName(operation.getName());
    currentOperation.setStyle(operation.getStyle());
    currentOperation.setUndefined(operation.isUndefined());
    currentOperation.setParameterOrdering(operation.getParameterOrdering());
    currentPortType.addOperation(currentOperation);

    super.visitOperation(operation);
  }

  protected void visitInput(Input input)
  {
    Input myInput = newDefinition.createInput();
    myInput.setDocumentationElement(input.getDocumentationElement());
    myInput.setName(input.getName());
    myInput.setMessage(input.getMessage());
    currentOperation.setInput(myInput);
  }

  protected void visitOutput(Output output)
  {
    Output myOutput = newDefinition.createOutput();
    myOutput.setDocumentationElement(output.getDocumentationElement());
    myOutput.setName(output.getName());
    myOutput.setMessage(output.getMessage());
    currentOperation.setOutput(myOutput);
  }

  protected void visitFault(Fault fault)
  {
    Fault myFault = newDefinition.createFault();
    myFault.setDocumentationElement(fault.getDocumentationElement());
    myFault.setName(fault.getName());
    myFault.setMessage(fault.getMessage());
    currentOperation.addFault(myFault);
  }

  protected void visitBinding(Binding binding)
  {
    currentBinding = newDefinition.createBinding();
    newDefinition.addBinding(currentBinding);

    currentBinding.setDocumentationElement(binding.getDocumentationElement());
    currentBinding.setQName(binding.getQName());
    currentBinding.setPortType(binding.getPortType());
    currentBinding.setUndefined(binding.isUndefined());

    super.visitBinding(binding);
  }

  protected void visitBindingOperation(BindingOperation bindingOperation)
  {
    currentBindingOperation = newDefinition.createBindingOperation();
    currentBindingOperation.setDocumentationElement(bindingOperation.getDocumentationElement());
    currentBindingOperation.setOperation(bindingOperation.getOperation());
    currentBindingOperation.setName(bindingOperation.getName());
    currentBinding.addBindingOperation(currentBindingOperation);

    super.visitBindingOperation(bindingOperation);
  }

  protected void visitBindingInput(BindingInput input)
  {
    BindingInput myInput = newDefinition.createBindingInput();
    myInput.setDocumentationElement(input.getDocumentationElement());
    myInput.setName(input.getName());
    currentBindingOperation.setBindingInput(myInput);

    super.visitBindingInput(input);
  }

  protected void visitBindingOutput(BindingOutput output)
  {
    BindingOutput myOutput = newDefinition.createBindingOutput();
    myOutput.setDocumentationElement(output.getDocumentationElement());
    myOutput.setName(output.getName());
    currentBindingOperation.setBindingOutput(myOutput);

    super.visitBindingOutput(output);
  }

  protected void visitBindingFault(BindingFault fault)
  {
    BindingFault myFault = newDefinition.createBindingFault();
    myFault.setDocumentationElement(fault.getDocumentationElement());
    myFault.setName(fault.getName());
    currentBindingOperation.addBindingFault(myFault);

    super.visitBindingFault(fault);
  }

  protected void visitService(Service service)
  {
    currentService = newDefinition.createService();
    currentService.setDocumentationElement(service.getDocumentationElement());
    currentService.setQName(service.getQName());
    newDefinition.addService(currentService);

    super.visitService(service);
  }

  protected void visitPort(Port port)
  {
    Port myPort = newDefinition.createPort();
    myPort.setDocumentationElement(port.getDocumentationElement());
    myPort.setName(port.getName());
    myPort.setBinding(port.getBinding());
    currentService.addPort(myPort);

    super.visitPort(port);
  }

  protected void visitExtensibilityElement(ExtensibilityElement extensibilityElement)
  {
    if (extensibilityElement instanceof SOAPBody)
      visitSOAPBody((SOAPBody)extensibilityElement);
    else if (extensibilityElement instanceof SOAPBinding)
      visitSOAPBinding((SOAPBinding)extensibilityElement);
    else if (extensibilityElement instanceof SOAPAddress)
      visitSOAPAddress((SOAPAddress)extensibilityElement);
    else if (extensibilityElement instanceof SOAPOperation)
      visitSOAPOperation((SOAPOperation)extensibilityElement);
  }

  private void visitSOAPOperation(SOAPOperation soapOperation)
  {
    println("Visiting SOAPOperation...");
    println("soapAction: " + soapOperation.getSoapActionURI());
    println("Leaving SOAPOperation...");
  }

  private void visitSOAPBody(SOAPBody soapBody)
  {
    println("Visiting SOAPBody...");
    println("use: " + soapBody.getUse());
    println("Leaving SOAPBody...");
  }

  private void visitSOAPBinding(SOAPBinding soapBinding)
  {
    println("Visiting SOAPBinding...");
    println("style: " + soapBinding.getStyle());
    println("transport: " + soapBinding.getTransportURI());
    println("Leaving SOAPBinding...");
  }

  private void visitSOAPAddress(SOAPAddress soapAddress)
  {
    println("Visiting SOAPAddress...");
    println("location: " + soapAddress.getLocationURI());
    println("Leaving SOAPAddress...");
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(new WSDL4JAPITest("ModelSemanticTest")
      {
        protected void runTest()
        {
          testModelSemantic();
        }
      });
    return suite;
  }

  public void testModelSemantic()
  {
    try
    {
      Definition def = loadDefinitionForWSDL4J("./samples/LoadStoreCompare/LoadAndPrintTest.wsdl");
      WSDL4JAPITest test = new WSDL4JAPITest(def);
      test.visit();
      serialize(test.newDefinition, "./samples/ClonedLoadAndPrintTest.wsdl");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Assert.fail(e.toString());
    }
  }

  private void serialize(Definition definition, String clonedFile) throws Exception
  {
    // WSDLWriter writer = factory.newWSDLWriter();
    // String s = PLUGIN_ABSOLUTE_PATH + clonedFile;
    // writer.writeWSDL(definition, new FileOutputStream(s));
  }

  private Definition loadDefinitionForWSDL4J(String wsdlFile) throws Exception
  {
    WSDLReader reader = factory.newWSDLReader();
    String s = PLUGIN_ABSOLUTE_PATH + wsdlFile;
    Definition definition = reader.readWSDL(s, new InputSource(new FileInputStream(s)));
    return definition;
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }
}
