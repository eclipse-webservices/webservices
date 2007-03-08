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
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;


public class RefactoringTest extends TestCase
{
  public RefactoringTest()
  {
    //init();
  }

  public RefactoringTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(new RefactoringTest("Refactoring")
      {
        protected void runTest()
        {
          testRefactoring();
        }
      });

    return suite;
  }

  public void testRefactoring()
  {
    try
    {
      // Before running this test, modify the location of the generated WSDL file
      generateWSDL("RefactoringTest.wsdl");
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

  // defect 6594
  public void renameOperation(Operation op)
  {
    Input input = op.getEInput();
    Assert.assertNotNull(input);
    Message message = input.getEMessage();
    Assert.assertNotNull(message);

    op.setName("Renamed" + op.getName());
    QName newQName = new QName(message.getQName().getNamespaceURI(), "Renamed" + message.getQName().getLocalPart());
    message.setQName(newQName);
  }

  public void generateWSDL(String outputFile) throws Exception
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
    definition.addNamespace("soap", SOAPConstants.SOAP_NAMESPACE_URI);

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
    // Create the second set op Operations
    //

    // Create a Part
    part = WSDLFactory.eINSTANCE.createPart();
    part.setName("Part2");
    part.setTypeName(new QName(WSDLConstants.SCHEMA_FOR_SCHEMA_URI_2001, "string"));

    // Create a Message
    Message inputMessage2 = WSDLFactory.eINSTANCE.createMessage();
    inputMessage2.setQName(new QName(definition.getTargetNamespace(), "InputMessage2"));
    inputMessage2.addPart(part);
    definition.addMessage(inputMessage2);

    // Create a Part
    part = WSDLFactory.eINSTANCE.createPart();
    part.setName("Part3");
    part.setTypeName(new QName(WSDLConstants.SCHEMA_FOR_SCHEMA_URI_2001, "float"));

    // Create a Message - GetTemperatureOutput
    Message outputMessage2 = WSDLFactory.eINSTANCE.createMessage();
    outputMessage2.setQName(new QName(definition.getTargetNamespace(), "OutputMessage2"));
    outputMessage2.addPart(part);
    definition.addMessage(outputMessage2);

    // Create an Input
    Input input2 = WSDLFactory.eINSTANCE.createInput();
    input2.setMessage(inputMessage2);

    // Create an Output - GetTemperatureOutput
    Output output2 = WSDLFactory.eINSTANCE.createOutput();
    output2.setMessage(outputMessage2);

    // Create an Operation - GetTemperatureForZipCode
    Operation operation2 = WSDLFactory.eINSTANCE.createOperation();
    operation2.setName("Operation2");
    operation2.setInput(input2);
    operation2.setOutput(output2);

    portType.addOperation(operation2);

    renameOperation(operation);
    renameOperation(operation2);

    resource.save(null);

  }

}
