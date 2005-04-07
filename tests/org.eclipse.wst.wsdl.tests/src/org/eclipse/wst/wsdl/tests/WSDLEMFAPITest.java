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

import java.util.Iterator;

import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.Namespace;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;

import org.eclipse.wst.wsdl.binding.soap.*;

import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.wst.wsdl.tests.util.DefinitionVisitor;
import org.eclipse.wst.wsdl.util.WSDLResourceFactoryImpl;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.w3c.dom.Element;

/**
 * @author Kihup Boo
 */
public class WSDLEMFAPITest extends DefinitionVisitor
{
  private WSDLFactory factory = WSDLFactory.eINSTANCE;
  
  private Definition newDefinition;
  private Message currentMessage;
  private Service currentService;
  private PortType currentPortType;
  private Operation currentOperation;
  private Binding currentBinding;
  private BindingOperation currentBindingOperation;
   
  {
    // This is needed because we don't have the following in the plugin.xml
    //
    //   <extension point = "org.eclipse.emf.extension_parser">
    //     <parser type="wsdl" class="com.ibm.etools.wsdl.util.WSDLResourceFactoryImpl"/>
    //   </extension>
    //
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl());
    WSDLPackage pkg = WSDLPackage.eINSTANCE;
  
    // We need this for XSD <import>.
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl());
    XSDPackage xsdpkg = XSDPackage.eINSTANCE;
  }
  
  // Added for JUnit
  public WSDLEMFAPITest(String name) 
  {
    super(name);
  }
  
  /**
   * @param definition
   */
  public WSDLEMFAPITest(Definition definition)
  {
    super(definition);
  }
/*  
  private void serialize(String filename) throws Exception
  {
    Source domSource = new DOMSource(doc);
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT,"yes");
    transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
    transformer.transform(domSource,new StreamResult(new FileOutputStream(filename)));
  }
  
  private void createDocument() throws ParserConfigurationException
  {
    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
  }
  
  private Element createWSDLElement(String name)
  {
    Element element = doc.createElementNS("http://www.w3.org/2004/08/wsdl",name);
    if (wsdlNamespacePrefix != null)
      element.setPrefix(wsdlNamespacePrefix);
    
    return element;
  }
 */ 
  private void visitDocumentation(Element docElement)
  {
    if (docElement == null)
      return;
    println("documentation: " + docElement); // TBD - serialize docElement
  }
  
  private void println(String s)
  {
    System.out.println(s);
  }
  
  protected void visitDefinition(Definition def)
  {
    // Use WSDLElement to increase the API coverage in the reports
    WSDLElement root = factory.createDefinition();
    newDefinition = (Definition)root;
    
    root.setDocumentationElement(def.getDocumentationElement());   
    root.getDocumentationElement();
    root.setEnclosingDefinition(newDefinition);
    root.getEnclosingDefinition();
    root.getContainer();
    
    newDefinition.setQName(def.getQName());    
    newDefinition.setTargetNamespace(def.getTargetNamespace());
    newDefinition.setDocumentBaseURI(def.getDocumentBaseURI());
    
    Iterator iterator = def.getENamespaces().iterator();
    Namespace ns = null;   
    String prefix = null;
    String uri = null;
    while (iterator.hasNext())
    {
      ns = factory.createNamespace();
      prefix = ((Namespace)iterator.next()).getPrefix();
      uri = ((Namespace)iterator.next()).getURI();
      ns.setURI(uri);
      ns.setPrefix(prefix);
      newDefinition.getENamespaces().add(ns);
    }
    
    super.visitDefinition(def);
    
    root.setElement(null);
    root.updateElement(true);
    root.getElement();
    root.setElement(null);
    root.updateElement();
  }
  
  protected void visitImport(Import wsdlImport)
  {
    Import myImport = factory.createImport();
    newDefinition.getEImports().add(myImport);
    
    // e.g. <xs:import namespace="http://foo.com" schemaLocation= "bar.xsd"/>
    myImport.setNamespaceURI(wsdlImport.getNamespaceURI());
    myImport.setLocationURI(wsdlImport.getLocationURI());
    myImport.setDocumentationElement(wsdlImport.getDocumentationElement());
    myImport.setEDefinition(newDefinition);
    myImport.setESchema(wsdlImport.getESchema());
    myImport.setSchema(wsdlImport.getSchema()); // TBD - review
    myImport.setEnclosingDefinition(newDefinition);
  }
  
  protected void visitTypes(Types types)
  {
    Types myTypes = factory.createTypes();
    myTypes.setDocumentationElement(types.getDocumentationElement());
    
    Iterator iterator = types.getEExtensibilityElements().iterator();
    ExtensibilityElement ee = null;
    while (iterator.hasNext())
    {
      ee = (ExtensibilityElement)iterator.next();
      myTypes.addExtensibilityElement(ee);
    }
    myTypes.setEnclosingDefinition(newDefinition);
    newDefinition.getETypes();
    newDefinition.setETypes(myTypes);
  }

  protected void visitPart(Part part)
  {
    Part myPart = factory.createPart();
    myPart.setDocumentationElement(part.getDocumentationElement());
    myPart.setName(part.getName());
    myPart.setElementName(part.getElementName());
    myPart.setTypeName(part.getTypeName());
    myPart.setEMessage(part.getEMessage());
    myPart.setElementDeclaration(part.getElementDeclaration());
    
    Iterator iterator = part.getExtensionAttributes().keySet().iterator();
    QName key = null;
    QName value = null;
    while (iterator.hasNext())
    {
      key = (QName)iterator.next();
      value = (QName)part.getExtensionAttribute(key);
      myPart.setExtensionAttribute(key,value);
    }
    currentMessage.setEnclosingDefinition(newDefinition);
    currentMessage.getEParts().add(myPart);
  }

  protected void visitPortType(PortType portType)
  {
    currentPortType = factory.createPortType();
    currentPortType.setDocumentationElement(portType.getDocumentationElement());
    currentPortType.setQName(portType.getQName());
    currentPortType.setUndefined(portType.isUndefined());
    currentPortType.setProxy(portType.isProxy());
    currentPortType.setResourceURI(portType.getResourceURI());
    
    currentPortType.setEnclosingDefinition(newDefinition);
    newDefinition.getEPortTypes().add(currentPortType);
    
    super.visitPortType(portType);
  }
  
  protected void visitOperation(Operation operation)
  {
    currentOperation = factory.createOperation();
    currentOperation.setDocumentationElement(operation.getDocumentationElement());
    currentOperation.setName(operation.getName());
    currentOperation.setStyle(operation.getStyle());
    currentOperation.setUndefined(operation.isUndefined());
    currentService.setProxy(operation.isProxy());
    currentService.setResourceURI(operation.getResourceURI());
    operation.getEParameterOrdering(); // TBD
    currentOperation.setEnclosingDefinition(newDefinition);
    currentPortType.getEOperations().add(currentOperation);
   
    super.visitOperation(operation);
  }
  
  protected void visitInput(Input input)
  {
    Input myInput = factory.createInput();
    myInput.setDocumentationElement(input.getDocumentationElement());    
    myInput.setName(input.getName());
    myInput.setEMessage(input.getEMessage());
    myInput.setEnclosingDefinition(newDefinition);
    currentOperation.setEInput(myInput);
  }

  protected void visitOutput(Output output)
  {
    Output myOutput = factory.createOutput();
    myOutput.setDocumentationElement(output.getDocumentationElement());   
    myOutput.setName(output.getName());
    myOutput.setEMessage(output.getEMessage());
    myOutput.setEnclosingDefinition(newDefinition);
    currentOperation.setEOutput(myOutput);
  }

  protected void visitFault(Fault fault)
  {
    Fault myFault = factory.createFault();
    myFault.setDocumentationElement(fault.getDocumentationElement());   
    myFault.setName(fault.getName());
    myFault.setEMessage(fault.getEMessage());
    myFault.setEnclosingDefinition(newDefinition);
    currentOperation.getEFaults().add(myFault);
  }

  protected void visitBinding(Binding binding)
  {
    currentBinding = factory.createBinding();
    newDefinition.getEBindings().add(currentBinding);
    
    currentBinding.setDocumentationElement(binding.getDocumentationElement());    
    currentBinding.setQName(binding.getQName());
    currentBinding.setEPortType(binding.getEPortType());
    currentBinding.setUndefined(binding.isUndefined());
    currentBinding.setProxy(binding.isProxy());
    currentBinding.setResourceURI(binding.getResourceURI());

    super.visitBinding(binding);
  }
  
  protected void visitBindingOperation(BindingOperation bindingOperation)
  {
    currentBindingOperation = factory.createBindingOperation();
    currentBindingOperation.setDocumentationElement(bindingOperation.getDocumentationElement());   
    currentBindingOperation.setEOperation(bindingOperation.getEOperation());
    currentBindingOperation.setName(bindingOperation.getName());
    currentBinding.getBindingOperations().add(currentBindingOperation);
    
    super.visitBindingOperation(bindingOperation);
  }
 
  protected void visitBindingInput(BindingInput input)
  {
    BindingInput myInput = factory.createBindingInput();
    myInput.setDocumentationElement(input.getDocumentationElement());    
    myInput.setName(input.getName());
    currentBindingOperation.setEBindingInput(myInput);
    
    super.visitBindingInput(input);
  }
  
  protected void visitBindingOutput(BindingOutput output)
  {
    BindingOutput myOutput = factory.createBindingOutput();
    myOutput.setDocumentationElement(output.getDocumentationElement());   
    myOutput.setName(output.getName());
    currentBindingOperation.setEBindingOutput(myOutput);
    
    super.visitBindingOutput(output);
  }
  
  protected void visitBindingFault(BindingFault fault)
  {
    BindingFault myFault = factory.createBindingFault();
    myFault.setDocumentationElement(fault.getDocumentationElement());   
    myFault.setName(fault.getName());
    currentBindingOperation.getEBindingFaults().add(myFault);
    
    super.visitBindingFault(fault); 
  }
 
  protected void visitService(Service service)
  {
    currentService = factory.createService();
    currentService.setDocumentationElement(service.getDocumentationElement());   
    currentService.setQName(service.getQName());
    currentService.setProxy(service.isProxy());
    currentService.setResourceURI(service.getResourceURI());
    
    newDefinition.getEServices().add(currentService);
    
    super.visitService(service);
  }
  
  protected void visitPort(Port port)
  {
    Port myPort = factory.createPort();
    myPort.setDocumentationElement(port.getDocumentationElement());
    myPort.setName(port.getName());
    myPort.setEBinding(port.getEBinding());
    
    currentService.getEPorts().add(myPort);
    
    super.visitPort(port);
  }

  protected void visitExtensibilityElement(ExtensibleElement owner, ExtensibilityElement extensibilityElement)
  {
    if (extensibilityElement instanceof SOAPBody)
      visitSOAPBody((SOAPBody)extensibilityElement);
    else if (extensibilityElement instanceof SOAPBinding)
      visitSOAPBinding((SOAPBinding)extensibilityElement);
    else if (extensibilityElement instanceof SOAPAddress)
      visitSOAPAddress((SOAPAddress)extensibilityElement);
    else if (extensibilityElement instanceof SOAPOperation)
      visitSOAPOperation((SOAPOperation)extensibilityElement);
    else if (extensibilityElement instanceof SOAPFault)
      visitSOAPFault((SOAPFault)extensibilityElement);  
    else if (extensibilityElement instanceof SOAPHeader)
      visitSOAPHeader((SOAPHeader)extensibilityElement);
    else if (extensibilityElement instanceof SOAPHeaderFault)
      visitSOAPHeaderFault((SOAPHeaderFault)extensibilityElement);
  }
 
  private void visitSOAPFault(SOAPFault soapFault)
  {
    SOAPFault mySoapFault = SOAPFactory.eINSTANCE.createSOAPFault();
    mySoapFault.setEncodingStyles(soapFault.getEncodingStyles());
    mySoapFault.setName(soapFault.getName());
    mySoapFault.setNamespaceURI(soapFault.getNamespaceURI());
    mySoapFault.setUse(soapFault.getUse());
  } 
  
  private void visitSOAPHeader(SOAPHeader soapHeader)
  {
    // Use SOAPHeaderBase to increase the API coverage values in the reports
    
    SOAPHeaderBase mySoapHeader = SOAPFactory.eINSTANCE.createSOAPHeader();
    mySoapHeader.setMessage(soapHeader.getMessage());
    mySoapHeader.getMessage();
    mySoapHeader.setPart(soapHeader.getPart());
    mySoapHeader.getPart();
    mySoapHeader.setNamespaceURI(soapHeader.getNamespaceURI());
    mySoapHeader.getNamespaceURI();
    mySoapHeader.setUse(soapHeader.getUse());
    mySoapHeader.getUse();
    ((SOAPHeader)soapHeader).getHeaderFaults(); // TBD
  }
  
  private void visitSOAPHeaderFault(SOAPHeaderFault soapHeaderFault)
  {
    SOAPHeaderFault mySoapHeaderFault = SOAPFactory.eINSTANCE.createSOAPHeaderFault();
    mySoapHeaderFault.setMessage(soapHeaderFault.getMessage());
    mySoapHeaderFault.setPart(soapHeaderFault.getPart());
    mySoapHeaderFault.setNamespaceURI(soapHeaderFault.getNamespaceURI());
    mySoapHeaderFault.setUse(soapHeaderFault.getUse());
  }
  
  private boolean soapOperationVisited = false;
  private void visitSOAPOperation(SOAPOperation soapOperation)
  {
    soapOperationVisited = true;
    SOAPOperation mySoapOperation = SOAPFactory.eINSTANCE.createSOAPOperation();
    mySoapOperation.setSoapActionURI(soapOperation.getSoapActionURI());
    mySoapOperation.setStyle(soapOperation.getStyle());
  }
  
  //Needs to improve this part
  private boolean soapBodyVisited = false;
  private void visitSOAPBody(SOAPBody soapBody)
  {
    soapBodyVisited = true;
    SOAPBody mySoapBody = SOAPFactory.eINSTANCE.createSOAPBody();
    mySoapBody.setEncodingStyles(soapBody.getEncodingStyles());
    mySoapBody.setNamespaceURI(soapBody.getNamespaceURI());
    mySoapBody.setParts(soapBody.getParts());
    mySoapBody.setUse(soapBody.getUse());
  }
  
  //Needs to improve this part
  private boolean soapBindingVisited = false;
  private void visitSOAPBinding(SOAPBinding soapBinding)
  {
    soapBindingVisited = true;
    SOAPBinding mySoapBinding = SOAPFactory.eINSTANCE.createSOAPBinding();
    mySoapBinding.setStyle(soapBinding.getStyle());
    mySoapBinding.setTransportURI(soapBinding.getTransportURI()); 
  }
 
  // Needs to improve this part
  private boolean soapAddressVisited = false;
  private void visitSOAPAddress(SOAPAddress soapAddress)
  {
    soapAddressVisited = true;
    SOAPAddress mySoapAddress = SOAPFactory.eINSTANCE.createSOAPAddress();
    mySoapAddress.setLocationURI(soapAddress.getLocationURI()); 
  }
  
  public static Test suite() 
  {
    TestSuite suite = new TestSuite();
    
    suite.addTest
      (new WSDLEMFAPITest("ModelSemanticTest") 
         {
           protected void runTest() 
           {
             testModelSemantic();
           }
         }
       );
    return suite;
  }
  
  public void testModelSemantic()
  {
    try
    {
      Definition def = DefinitionLoader.load("./samples/LoadAndPrintTest.wsdl",true);
      SemanticTest test = new SemanticTest(def);
      test.visit();
    }
    catch (Exception e)
    {
      Assert.fail(e.toString());
    }     
  } 
  
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }
}
