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

import javax.wsdl.OperationType;
import javax.xml.namespace.QName;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.wst.wsdl.*;

import org.eclipse.wst.wsdl.tests.util.DefinitionLoader;
import org.eclipse.wst.wsdl.tests.util.DefinitionVisitor;

import org.eclipse.wst.wsdl.binding.soap.*;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;

import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;

import org.w3c.dom.Element;

/**
 * @author Kihup Boo
 */
public class SemanticTest extends DefinitionVisitor
{
  
  //private String wsdlNamespacePrefix;
  //private String xsdNamespacePrefix;
  
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
  public SemanticTest(String name) 
  {
    super(name);
  }
  
  /**
   * @param definition
   */
  public SemanticTest(Definition definition)
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
    println("Visiting definitions...");
    visitDocumentation(def.getDocumentationElement());
    
    QName qname = def.getQName();
    if (qname != null)
      println("name: " + qname.getLocalPart());
    
    String targetNamespace = def.getTargetNamespace();
    if (targetNamespace != null)
      println("targetNamespace: " + targetNamespace);
    
    Iterator iterator = def.getNamespaces().keySet().iterator();
    String prefix = null;
    String namespace = null;

    while (iterator.hasNext())
    {
      prefix = (String)iterator.next();
      namespace = def.getNamespace(prefix);
      println("namespace prefix: " + prefix + ", namespace URI: " + namespace);
    }
    super.visitDefinition(def);
    println("Leaving definitions...");
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitImport(org.eclipse.wst.wsdl.Import)
   */
  protected void visitImport(Import wsdlImport)
  {
    println("Visiting import...");
    // Determine if we <import> a schema.
    if (importingSchema(wsdlImport))
    {
      println("<import>ing XML Schema");
      
      //  <xs:import namespace="http://foo.com" schemaLocation= "bar.xsd"/>
      println("namespace: " + wsdlImport.getNamespaceURI());
      println("schemaLocation: " + wsdlImport.getLocationURI());
      visitDocumentation(wsdlImport.getDocumentationElement());      
    }
  	else
    {
      println("<import>ing WSDL");
      visitDocumentation(wsdlImport.getDocumentationElement());
    }
    println("Leaving import...");
  }
  
  private boolean importingSchema(Import myImport)
  {
    if (myImport.getDefinition() != null) // it is WSDL import
      return false;
    else
      return true;
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitTypes(org.eclipse.wst.wsdl.Types)
   */
  protected void visitTypes(Types types)
  {
    println("Visiting types...");

    Iterator iterator = types.getSchemas().iterator();
    XSDSchema schema = null;
    while (iterator.hasNext())
    {
      schema = (XSDSchema)iterator.next();
      println("in-line schema: " + schema);
    }   
    println("Leaving types...");
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitPart(org.eclipse.wst.wsdl.Part)
   */
  protected void visitPart(Part part)
  {
    println("Visiting part...");
  }

  protected void visitPortType(PortType portType)
  {
    println("Visiting portType...");
  	visitDocumentation(portType.getDocumentationElement());
  	
    QName qname = portType.getQName();
    if (qname != null)
      println("name: " + qname.getLocalPart());
    
    super.visitPortType(portType);
    println("Leaving portType...");
  }
  
  protected void visitOperation(Operation operation)
  {
    println("Visiting operation...");
    visitDocumentation(operation.getDocumentationElement());
	
    String name = operation.getName();
    if (name != null)
      println("name: " + name);

    OperationType opType = operation.getStyle();
    Assert.assertNotNull("Failed determining Operation Type",opType);
    
    if (OperationType.REQUEST_RESPONSE == opType)
      println("op type: " + "in-out");
    else if (OperationType.SOLICIT_RESPONSE == opType)
      println("op type" + "out-in");
    else if (OperationType.NOTIFICATION == opType)
      println("op type" + "out-only");
    else if (OperationType.ONE_WAY == opType)
      println("op type" + "in-only");
    
    super.visitOperation(operation);
    println("Leaving operation...");
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitInput(org.eclipse.wst.wsdl.Input)
   */
  protected void visitInput(Input input)
  {
    println("Visiting input...");
    visitDocumentation(input.getDocumentationElement());
    
    String name = input.getName();
    if (name != null)
      println("name: " + name);
    
    Message message = input.getEMessage();
    Assert.assertNotNull("Failed to resolve the message",message);
    
    println("Leaving input...");
  }
  
  private Element getPartElement(MessageReference messageRef)
  {
    Iterator iterator = messageRef.getEMessage().getEParts().iterator();
    // TBD - for now, take the first part.
    Part part = (Part)iterator.next();
    Element partElement = part.getElement();
    return partElement;
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitOutput(org.eclipse.wst.wsdl.Output)
   */
  protected void visitOutput(Output output)
  {
    println("Visiting output...");
    visitDocumentation(output.getDocumentationElement());
    
    String name = output.getName();
    if (name != null)
      println("name: " + name);
    
    Message message = output.getEMessage();
    Assert.assertNotNull("Failed to resolve the message",message);
    
    println("Leaving output...");
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitFault(org.eclipse.wst.wsdl.Fault)
   */
  protected void visitFault(Fault fault)
  {
    println("Visiting fault...");
    println("Leaving fault...");
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitBinding(org.eclipse.wst.wsdl.Binding)
   */
  protected void visitBinding(Binding binding)
  {
    println("Visiting binding...");
    visitDocumentation(binding.getDocumentationElement());
    
    QName qname = binding.getQName();
    if (qname != null)
      println("name: " + qname.getLocalPart());
    
    PortType portType = binding.getEPortType();
    Assert.assertNotNull("Failed to resolve the portType",portType);

    super.visitBinding(binding);
    Assert.assertTrue("<soapBody> is missing",soapBodyVisited);
    Assert.assertTrue("<soapBinding> is missing",soapBindingVisited);
    Assert.assertTrue("<soapOperation> is missing",soapOperationVisited);
    println("Leaving binding...");
  }
  
  protected void visitBindingOperation(BindingOperation bindingOperation)
  {
    println("Visiting binding operation...");
    visitDocumentation(bindingOperation.getDocumentationElement());
    
    Operation operation = bindingOperation.getEOperation();
    Assert.assertNotNull("Failed to resolve the operation",operation);
    
    if (operation != null)
    {
      String operationName = operation.getName();
      println("name: " + operationName);
    }
 
    super.visitBindingOperation(bindingOperation);
    println("Leaving binding operation...");
  }
 
  protected void visitBindingInput(BindingInput input)
  {
    println("Visiting binding input...");
    visitDocumentation(input.getDocumentationElement());
    
    String inputName = input.getName();
    if (inputName != null)
      println("name: " + inputName);
    
    super.visitBindingInput(input);
    println("Leaving binding input...");
  }
  
  protected void visitBindingOutput(BindingOutput output)
  {
    println("Visiting binding output...");
    visitDocumentation(output.getDocumentationElement());
    
    String outputName = output.getName();
    if (outputName != null)
      println("name: " + outputName);
    
    super.visitBindingOutput(output);
    println("Leaving binding output...");
  }
  
  protected void visitBindingFault(BindingFault fault)
  {
    println("Visiting binding fault...");
    visitDocumentation(fault.getDocumentationElement());
    
    Element faultElement = fault.getElement();
    String faultName = fault.getName();
    if (faultName != null)
      faultElement.setAttribute("name",faultName);
    
    super.visitBindingFault(fault); 
    println("Leaving binding fault...");
  }
 
  protected void visitService(Service service)
  {
    println("Visiting service...");
    visitDocumentation(service.getDocumentationElement());
    
    QName qname = service.getQName();
    Assert.assertNotNull("Validation Error: service is missing the name attribute",qname);
    if (qname != null)
      println("name: " + qname.getLocalPart());
    
    super.visitService(service);
    println("Leaving service...");
  }
  
  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitPort(org.eclipse.wst.wsdl.Port)
   */
  protected void visitPort(Port port)
  {
    println("Visiting port...");
    visitDocumentation(port.getDocumentationElement());
    
    String name = port.getName();
    Assert.assertNotNull("Validation Error: port is missing the name attribute",port);
    if (name != null)
      println("name: " + port.getName());
    
    Binding binding = port.getEBinding();
    Assert.assertNotNull("Failed to resolve the binding",binding);
    
    super.visitPort(port);
    Assert.assertTrue("<soapAddress> is missing",soapAddressVisited);
    println("Leaving port...");
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitExtensibilityElement(org.eclipse.wst.wsdl.ExtensibleElement, org.eclipse.wst.wsdl.ExtensibilityElement)
   */
  protected void visitExtensibilityElement(ExtensibleElement owner, ExtensibilityElement extensibilityElement)
  {
    println("Visiting extensibility element...");
    if (extensibilityElement instanceof SOAPBody)
      visitSOAPBody((SOAPBody)extensibilityElement);
    else if (extensibilityElement instanceof SOAPBinding)
      visitSOAPBinding((SOAPBinding)extensibilityElement);
    else if (extensibilityElement instanceof SOAPAddress)
      visitSOAPAddress((SOAPAddress)extensibilityElement);
    else if (extensibilityElement instanceof SOAPOperation)
      visitSOAPOperation((SOAPOperation)extensibilityElement);
    println("Leaving extensibility element...");
  }
 
  //Needs to improve this part
  private boolean soapOperationVisited = false;
  private void visitSOAPOperation(SOAPOperation soapOperation)
  {
    soapOperationVisited = true;
    println("Visiting SOAPOperation...");
    println("soapAction: " + soapOperation.getSoapActionURI());
    println("Leaving SOAPOperation...");
  }
  
  //Needs to improve this part
  private boolean soapBodyVisited = false;
  private void visitSOAPBody(SOAPBody soapBody)
  {
    soapBodyVisited = true;
    println("Visiting SOAPBody...");
    println("use: " + soapBody.getUse());
    println("Leaving SOAPBody...");
  }
  
  //Needs to improve this part
  private boolean soapBindingVisited = false;
  private void visitSOAPBinding(SOAPBinding soapBinding)
  {
    soapBindingVisited = true;
    println("Visiting SOAPBinding...");
    println("style: " + soapBinding.getStyle());
    println("transport: " + soapBinding.getTransportURI());
    println("Leaving SOAPBinding...");  
  }
 
  // Needs to improve this part
  private boolean soapAddressVisited = false;
  private void visitSOAPAddress(SOAPAddress soapAddress)
  {
    soapAddressVisited = true;
    println("Visiting SOAPAddress...");
    println("location: " + soapAddress.getLocationURI());
    println("Leaving SOAPAddress..."); 
  }
  
  public static Test suite() 
  {
    TestSuite suite = new TestSuite();
    
    suite.addTest
      (new SemanticTest("ModelSemanticTest") 
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
