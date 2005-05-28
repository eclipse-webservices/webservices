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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Namespace;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.UnknownExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.WSDLPlugin;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeader;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPOperation;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.wst.wsdl.tests.util.DefinitionVisitor;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.w3c.dom.Element;

/**
 * @author Kihup Boo
 */
public class WSDLEMFAPITest extends DefinitionVisitor
{
  private WSDLFactory factory = WSDLFactory.eINSTANCE;
  
  Definition newDefinition;
  private Message currentMessage;
  private Service currentService;
  private PortType currentPortType;
  private Operation currentOperation;
  private Binding currentBinding;
  private BindingOperation currentBindingOperation;
  private ExtensibleElement currentExtensibleElement;
   
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
    newDefinition.setLocation(def.getLocation());
    newDefinition.setEncoding(def.getEncoding());
    
    // getENamespaces does not work.
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
    
    iterator = def.getNamespaces().keySet().iterator();
    prefix = null;
    String namespace = null;

    while (iterator.hasNext())
    {
      prefix = (String)iterator.next();
      namespace = def.getNamespace(prefix);
      newDefinition.addNamespace(prefix,namespace);
    }
    //newDefinition.updateElement();
    
    currentExtensibleElement = def;
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
    types.getSchemas("http://tempuri.org/LoadAndPrintTest/");
    
    currentExtensibleElement = myTypes;
    while (iterator.hasNext())
    {
      ee = (ExtensibilityElement)iterator.next();
      visitExtensibilityElement(ee);
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
    myPart.setTypeDefinition(part.getTypeDefinition());
    
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
    currentMessage.addPart(myPart);
  }

  protected void visitPortType(PortType portType)
  {
    currentPortType = factory.createPortType();
    currentPortType.setDocumentationElement(portType.getDocumentationElement());
    currentPortType.setQName(portType.getQName());
    currentPortType.setUndefined(portType.isUndefined());
    
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
    operation.getEParameterOrdering(); // TBD
    currentOperation.setEnclosingDefinition(newDefinition);
    currentPortType.getEOperations().add(currentOperation);
   
    super.visitOperation(operation);
    //System.out.println("Operation Type is: " + operation.getStyle());
    //operation.setStyle(OperationType.REQUEST_RESPONSE);
    //System.out.println("Operation Type is: " + operation.getStyle());
  }
  
  protected void visitMessage(Message message)
  {
    currentMessage = factory.createMessage();
    currentMessage.setQName(message.getQName());
    currentMessage.setUndefined(message.isUndefined());
    newDefinition.addMessage(currentMessage);

    super.visitMessage(message);
  }
  
  protected void visitInput(Input input)
  {
    MessageReference myInput = factory.createInput();
    myInput.setDocumentationElement(input.getDocumentationElement());    
    myInput.setName(input.getName());
    myInput.getName();
    myInput.setEMessage(input.getEMessage());
    myInput.setEnclosingDefinition(newDefinition);
    currentOperation.setEInput((Input)myInput);
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

    currentExtensibleElement = currentBinding;
    super.visitBinding(binding);
  }
  
  protected void visitBindingOperation(BindingOperation bindingOperation)
  {
    currentBindingOperation = factory.createBindingOperation();
    currentBindingOperation.setDocumentationElement(bindingOperation.getDocumentationElement());   
    currentBindingOperation.setEOperation(bindingOperation.getEOperation());
    currentBindingOperation.setName(bindingOperation.getName());
    currentBinding.getBindingOperations().add(currentBindingOperation);
    
    currentExtensibleElement = currentBindingOperation;
    super.visitBindingOperation(bindingOperation);    
  }
 
  protected void visitBindingInput(BindingInput input)
  {
    BindingInput myInput = factory.createBindingInput();
    myInput.setDocumentationElement(input.getDocumentationElement());    
    myInput.setName(input.getName());
    myInput.setInput(input.getInput());
    myInput.setEInput(input.getEInput());
    currentBindingOperation.setEBindingInput(myInput);
 
    currentExtensibleElement = myInput;
    super.visitBindingInput(input);
  }
  
  protected void visitBindingOutput(BindingOutput output)
  {
    BindingOutput myOutput = factory.createBindingOutput();
    myOutput.setDocumentationElement(output.getDocumentationElement());   
    myOutput.setName(output.getName());
    myOutput.setOutput(output.getOutput());
    myOutput.setEOutput(output.getEOutput());
    currentBindingOperation.setEBindingOutput(myOutput);
    
    currentExtensibleElement = myOutput;
    super.visitBindingOutput(output);
  }
  
  protected void visitBindingFault(BindingFault fault)
  {
    BindingFault myFault = factory.createBindingFault();
    myFault.setDocumentationElement(fault.getDocumentationElement());   
    myFault.setName(fault.getName());
    myFault.setEFault(fault.getEFault());
    myFault.setFault(fault.getFault());
    currentBindingOperation.getEBindingFaults().add(myFault);
    
    currentExtensibleElement = myFault;
    super.visitBindingFault(fault);
  }
 
  protected void visitService(Service service)
  {
    currentService = factory.createService();
    currentService.setDocumentationElement(service.getDocumentationElement());   
    currentService.setQName(service.getQName());
    currentService.setUndefined(service.isUndefined());
    
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
    
    currentExtensibleElement = myPort;
    super.visitPort(port);
  }

  protected void visitExtensibilityElement(ExtensibleElement owner, ExtensibilityElement extensibilityElement)
  {
    // To move up the API test coverage
    owner.getEExtensibilityElements();
    owner.getExtensibilityElements();
    factory.createExtensibilityElement();
    WSDLPlugin.getPlugin();
    WSDLPlugin.INSTANCE.getPluginResourceLocator();
    
    visitExtensibilityElement(extensibilityElement);
  }
  
  private void visitExtensibilityElement(ExtensibilityElement extensibilityElement)
  {
    XSDSchemaExtensibilityElement xsee = null;
    UnknownExtensibilityElement uee = null;
    ExtensibilityElement myEE = null;
    if (extensibilityElement instanceof XSDSchemaExtensibilityElement)
    {
      myEE = factory.createXSDSchemaExtensibilityElement();
      xsee = (XSDSchemaExtensibilityElement)myEE;
      xsee.setSchema(((XSDSchemaExtensibilityElement)extensibilityElement).getSchema());
    }
    else
    {
      myEE = factory.createUnknownExtensibilityElement();
      uee = (UnknownExtensibilityElement)myEE;
      uee.getChildren(); // TBD
    }
    myEE.setElementType(extensibilityElement.getElementType());
    myEE.setRequired(extensibilityElement.getRequired());
    myEE.setRequired(extensibilityElement.isRequired());
    if (currentExtensibleElement != null)
      currentExtensibleElement.addExtensibilityElement(myEE);
    
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
    
    SOAPHeaderBase yourSoapHeader = SOAPFactory.eINSTANCE.createSOAPHeaderBase();
    yourSoapHeader.getEncodingStyles();
    
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
      WSDLEMFAPITest test = new WSDLEMFAPITest(def);
      test.visit();      
      serialize(test.newDefinition);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      Assert.fail(e.toString());
    }     
  } 
  
  private void serialize(Definition def) throws Exception
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("wsdl", new WSDLResourceFactoryImpl());
    WSDLResourceImpl wsdlMainResource = (WSDLResourceImpl)resourceSet.createResource(URI.createURI("*.wsdl"));
    wsdlMainResource.getContents().add(def);

    IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    IProject myWebProject = myWorkspaceRoot.getProject("org.eclipse.wst.wsdl.tests");
    if (!myWebProject.exists())
      myWebProject.create(null);
    
    String baseDir = myWebProject.getLocation().toString();
    
    DefinitionLoader.store(def,baseDir + "/ClonedLoadAndPrintTest.wsdl");
  }
  
  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
  }
}
