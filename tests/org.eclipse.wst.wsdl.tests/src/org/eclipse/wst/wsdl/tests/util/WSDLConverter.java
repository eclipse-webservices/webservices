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
package org.eclipse.wst.wsdl.tests.util;


import java.io.FileOutputStream;
import java.util.Iterator;

import javax.wsdl.OperationType;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceFactoryImpl;
import org.eclipse.xsd.XSDPackage;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * @author Kihup Boo
 */
public class WSDLConverter extends DefinitionVisitor
{
  private Document doc;

  private Element description;

  private Element currentTypes;

  private Element currentService;

  private Element currentEndpoint;

  private Element currentInterface;

  private Element currentOperation;

  private Element currentBinding;

  private Element currentBindingOperation;

  private Element currentBindingInput;

  private Element currentBindingOutput;

  private Element currentBindingFault;

  private String wsdlNamespacePrefix;

  private String xsdNamespacePrefix;

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
  public WSDLConverter(String name)
  {
    super(name);
  }

  /**
   * @param definition
   */
  public WSDLConverter(Definition definition)
  {
    super(definition);
    // TODO Auto-generated constructor stub
  }

  public void generate20(String filename) throws Exception
  {
    createDocument();
    visit();
    serialize(filename);
  }

  private void serialize(String filename) throws Exception
  {
    Source domSource = new DOMSource(doc);
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    transformer.transform(domSource, new StreamResult(new FileOutputStream(filename)));
  }

  private void createDocument() throws ParserConfigurationException
  {
    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
  }

  private Element createWSDLElement(String name)
  {
    Element element = doc.createElementNS("http://www.w3.org/2004/08/wsdl", name);
    if (wsdlNamespacePrefix != null)
      element.setPrefix(wsdlNamespacePrefix);

    return element;
  }

  private void processDocumentation(Element docElement, Element parent)
  {
    if (docElement == null)
      return;

    Element adoptedDocElement = (Element)doc.importNode(docElement, true);
    parent.appendChild(adoptedDocElement);
  }

  protected void visitDefinition(Definition def)
  {
    description = createWSDLElement("description");
    processDocumentation(def.getDocumentationElement(), description);

    // TBD - Determine later if we want to convert the document to use SOAP 1.2.
    // However adding these two namespaces may not be harmful.
    description.setAttribute("xmlns:soapenv", "http://www.w3.org/2003/05/soap-envelop");
    description.setAttribute("xmlns:wsoap", "http://www.w3.org/2004/08/soap12");

    String targetNamespace = def.getTargetNamespace();
    if (targetNamespace != null)
      description.setAttribute("targetNamespace", targetNamespace);

    Iterator iterator = def.getNamespaces().keySet().iterator();
    String prefix = null;
    String namespace = null;
    /*
     xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/"
     xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"*/
    while (iterator.hasNext())
    {
      prefix = (String)iterator.next();
      namespace = def.getNamespace(prefix);

      if ("http://schemas.xmlsoap.org/wsdl/".equals(namespace))
      {
        if (prefix.length() == 0) // meant to be default namespace
          description.setAttribute("xmlns", "http://www.w3.org/2004/08/wsdl");
        else
        {
          description.setAttribute("xmlns:" + prefix, "http://www.w3.org/2004/08/wsdl");
          wsdlNamespacePrefix = prefix;
          description.setPrefix(wsdlNamespacePrefix);
        }
        continue;
      }

      // SOAP 1.2
      if ("http://schemas.xmlsoap.org/wsdl/soap/".equals(namespace))
      {
        // SOAP 1.2
        description.setAttribute("xmlns:" + prefix, "http://www.w3.org/2003/05/soap-envelop");

        // WSDL 2.0 binding for SOAP 1.2
        description.setAttribute("xmlns:wsoap", "http://www.w3.org/2004/08/soap12");
        continue;
      }

      //if ("http://schemas.xmlsoap.org/soap/encoding/".equals(namespace))
      //  continue;

      if ("http://www.w3.org/2001/XMLSchema".equals(namespace))
        xsdNamespacePrefix = prefix; // We will use this in visitImport().

      if (prefix.length() == 0) // meant to be default namespace
        description.setAttribute("xmlns" + prefix, namespace);
      else
        description.setAttribute("xmlns:" + prefix, namespace);
    }

    doc.appendChild(description);
    super.visitDefinition(def);

  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitImport(org.eclipse.wst.wsdl.Import)
   */
  protected void visitImport(Import wsdlImport)
  {
    // Determine if we <import> a schema.
    if (importingSchema(wsdlImport))
    {
      currentTypes = createWSDLElement("types");
      description.appendChild(currentTypes);

      //  <xs:import namespace="http://foo.com" schemaLocation= "bar.xsd"/>
      Element schemaImport = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "import");
      if (xsdNamespacePrefix == null) // need to add one
      {
        description.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
        xsdNamespacePrefix = "xs";
        schemaImport.setPrefix(xsdNamespacePrefix);
      }
      else if (!"".equals(xsdNamespacePrefix)) // it is not default namespace
        schemaImport.setPrefix(xsdNamespacePrefix);

      schemaImport.setAttribute("namespace", wsdlImport.getNamespaceURI());
      schemaImport.setAttribute("schemaLocation", wsdlImport.getLocationURI());
      processDocumentation(wsdlImport.getDocumentationElement(), schemaImport);
      currentTypes.appendChild(schemaImport);
    }
    else
    {
      Element importElement = wsdlImport.getElement();
      Element adoptedImportElement = (Element)doc.importNode(importElement, true);
      processDocumentation(wsdlImport.getDocumentationElement(), adoptedImportElement);
      description.appendChild(adoptedImportElement);
    }
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
    // currentTypes may have been created in visitImport().
    if (currentTypes == null)
    {
      currentTypes = createWSDLElement("types");
      processDocumentation(types.getDocumentationElement(), currentTypes);
      description.appendChild(currentTypes);
    }

    Iterator iterator = types.getSchemas().iterator();
    XSDSchema schema = null;
    Element schemaElement = null;
    Element adoptedSchemaElement = null;
    while (iterator.hasNext())
    {
      schema = (XSDSchema)iterator.next();
      schema.updateElement();
      schemaElement = schema.getElement();
      adoptedSchemaElement = (Element)doc.importNode(schemaElement, true);
      currentTypes.appendChild(adoptedSchemaElement);
    }
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitPart(org.eclipse.wst.wsdl.Part)
   */
  protected void visitPart(Part part)
  {
    // TODO Auto-generated method stub
  }

  protected void visitPortType(PortType portType)
  {
    currentInterface = createWSDLElement("interface");
    processDocumentation(portType.getDocumentationElement(), currentInterface);

    Element portTypeElement = portType.getElement();
    if (portTypeElement.hasAttribute("name"))
      currentInterface.setAttribute("name", portTypeElement.getAttribute("name"));

    description.appendChild(currentInterface);
    super.visitPortType(portType);
  }

  protected void visitOperation(Operation operation)
  {
    currentOperation = createWSDLElement("operation");
    processDocumentation(operation.getDocumentationElement(), currentOperation);

    Element operationElement = operation.getElement();
    if (operationElement.hasAttribute("name"))
      currentOperation.setAttribute("name", operationElement.getAttribute("name"));

    OperationType opType = operation.getStyle();
    if (OperationType.REQUEST_RESPONSE == opType)
      currentOperation.setAttribute("pattern", "http://www.w3.org/2004/03/wsdl/in-out");
    else if (OperationType.SOLICIT_RESPONSE == opType)
      currentOperation.setAttribute("pattern", "http://www.w3.org/2004/03/wsdl/out-in");
    else if (OperationType.NOTIFICATION == opType)
      currentOperation.setAttribute("pattern", "http://www.w3.org/2004/03/wsdl/out-only");
    else if (OperationType.ONE_WAY == opType)
      currentOperation.setAttribute("pattern", "http://www.w3.org/2004/03/wsdl/in-only");

    currentInterface.appendChild(currentOperation);
    super.visitOperation(operation);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitInput(org.eclipse.wst.wsdl.Input)
   */
  protected void visitInput(Input input)
  {
    Element currentInput = createWSDLElement("input");
    processDocumentation(input.getDocumentationElement(), currentInput);

    Element inputElement = input.getElement();

    if (inputElement.hasAttribute("name"))
      currentInput.setAttribute("messageLabel", inputElement.getAttribute("name"));

    Element partElement = getPartElement(input);
    if (partElement.hasAttribute("element"))
      currentInput.setAttribute("element", partElement.getAttribute("element"));
    // TBD - what if the part uses "type"?

    currentOperation.appendChild(currentInput);
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
    Element currentOutput = createWSDLElement("output");
    processDocumentation(output.getDocumentationElement(), currentOutput);

    Element outputElement = output.getElement();

    if (outputElement.hasAttribute("name"))
      currentOutput.setAttribute("messageLabel", outputElement.getAttribute("name"));

    Element partElement = getPartElement(output);
    if (partElement.hasAttribute("element"))
      currentOutput.setAttribute("element", partElement.getAttribute("element"));
    // TBD - what if the part uses "type"?

    currentOperation.appendChild(currentOutput);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitFault(org.eclipse.wst.wsdl.Fault)
   */
  protected void visitFault(Fault fault)
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitBinding(org.eclipse.wst.wsdl.Binding)
   */
  protected void visitBinding(Binding binding)
  {
    currentBinding = createWSDLElement("binding");
    processDocumentation(binding.getDocumentationElement(), currentBinding);

    Element bindingElement = binding.getElement();
    if (bindingElement.hasAttribute("name"))
      currentBinding.setAttribute("name", bindingElement.getAttribute("name"));
    if (bindingElement.hasAttribute("type"))
      currentBinding.setAttribute("interface", bindingElement.getAttribute("type"));

    // TBD - is the next line fixed for SOAP?
    currentBinding.setAttribute("type", "http://www.w3.org/2004/08/wsdl/soap12");
    currentBinding.setAttribute("wsoap:protocol", "http://www.w3.org/2003/05/soap/bindings/HTTP");
    currentBinding.setAttribute("wsoap:mepDefault", "http://www.w3.org.2003/05/soap/mep/request-response");

    description.appendChild(currentBinding);
    super.visitBinding(binding);
  }

  protected void visitBindingOperation(BindingOperation operation)
  {
    currentBindingOperation = createWSDLElement("operation");
    processDocumentation(operation.getDocumentationElement(), currentBindingOperation);

    Element operationElement = operation.getElement();
    String operationName = operation.getEOperation().getName();

    // Determine prefix
    String prefix = null;
    String targetNamespace = null;
    Definition def = operation.getEnclosingDefinition();
    if (def != null)
      targetNamespace = def.getTargetNamespace();
    if (targetNamespace != null)
      prefix = def.getPrefix(targetNamespace);

    if (prefix == null)
      prefix = "";
    else
      prefix += ":";

    currentBindingOperation.setAttribute("ref", prefix + operationName);

    currentBinding.appendChild(currentBindingOperation);
    super.visitBindingOperation(operation);
  }

  protected void visitBindingInput(BindingInput input)
  {
    currentBindingInput = createWSDLElement("input");
    processDocumentation(input.getDocumentationElement(), currentBindingInput);

    Element inputElement = input.getElement();
    String inputName = input.getName();
    if (inputName != null)
      inputElement.setAttribute("name", inputName);

    currentBindingOperation.appendChild(currentBindingInput);
    super.visitBindingInput(input);
  }

  protected void visitBindingOutput(BindingOutput output)
  {
    currentBindingOutput = createWSDLElement("output");
    processDocumentation(output.getDocumentationElement(), currentBindingOutput);

    Element outputElement = output.getElement();
    String outputName = output.getName();
    if (outputName != null)
      outputElement.setAttribute("name", outputName);

    currentBindingOperation.appendChild(currentBindingOutput);
    super.visitBindingOutput(output);
  }

  protected void visitBindingFault(BindingFault fault)
  {
    currentBindingFault = createWSDLElement("fault");
    processDocumentation(fault.getDocumentationElement(), currentBindingFault);

    Element faultElement = fault.getElement();
    String faultName = fault.getName();
    if (faultName != null)
      faultElement.setAttribute("name", faultName);

    currentBindingOperation.appendChild(currentBindingFault);
    super.visitBindingFault(fault);
  }

  protected void visitService(Service service)
  {
    currentService = createWSDLElement("service");
    processDocumentation(service.getDocumentationElement(), currentService);

    Element serviceElement = service.getElement();
    if (serviceElement.hasAttribute("name"))
      currentService.setAttribute("name", serviceElement.getAttribute("name"));

    description.appendChild(currentService);
    super.visitService(service);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitPort(org.eclipse.wst.wsdl.Port)
   */
  protected void visitPort(Port port)
  {
    currentEndpoint = createWSDLElement("endpoint");
    processDocumentation(port.getDocumentationElement(), currentEndpoint);

    Element portElement = port.getElement();
    if (portElement.hasAttribute("name"))
      currentEndpoint.setAttribute("name", portElement.getAttribute("name"));
    if (portElement.hasAttribute("binding"))
      currentEndpoint.setAttribute("binding", portElement.getAttribute("binding"));

    // Add interface to current service
    Binding binding = port.getEBinding();
    if (binding != null) // binding could not be resolved
    {
      Element bindingElement = binding.getElement();
      if (bindingElement.hasAttribute("type"))
        currentService.setAttribute("interface", bindingElement.getAttribute("type"));
    }

    currentService.appendChild(currentEndpoint);
    super.visitPort(port);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.tests.util.DefinitionVisitor#visitExtensibilityElement(org.eclipse.wst.wsdl.ExtensibleElement, org.eclipse.wst.wsdl.ExtensibilityElement)
   */
  protected void visitExtensibilityElement(ExtensibleElement owner, ExtensibilityElement extensibilityElement)
  {
    // TBD - It is not just SOAP binding
    if (owner instanceof org.eclipse.wst.wsdl.internal.impl.PortImpl)
    {
      Element soapElement = extensibilityElement.getElement();
      if (soapElement.hasAttribute("location"))
        currentEndpoint.setAttribute("address", soapElement.getAttribute("location"));
    }
    else
    {
      Element domElement = extensibilityElement.getElement();
      Element adoptedDOMElement = (Element)doc.importNode(domElement, true);

      if (owner instanceof org.eclipse.wst.wsdl.internal.impl.DefinitionImpl)
        description.insertBefore(adoptedDOMElement, description.getFirstChild());
      else if (owner instanceof org.eclipse.wst.wsdl.internal.impl.ServiceImpl)
        currentService.insertBefore(adoptedDOMElement, currentService.getFirstChild());
      else if (owner instanceof org.eclipse.wst.wsdl.internal.impl.PortTypeImpl)
        currentInterface.insertBefore(adoptedDOMElement, currentInterface.getFirstChild());
      else if (owner instanceof org.eclipse.wst.wsdl.internal.impl.OperationImpl)
        currentOperation.insertBefore(adoptedDOMElement, currentOperation.getFirstChild());
      else if (owner instanceof org.eclipse.wst.wsdl.internal.impl.BindingImpl)
        currentBinding.insertBefore(adoptedDOMElement, currentBinding.getFirstChild());
      else if (owner instanceof org.eclipse.wst.wsdl.internal.impl.BindingOperationImpl)
        currentBindingOperation.insertBefore(adoptedDOMElement, currentBindingOperation.getFirstChild());
      else if (owner instanceof org.eclipse.wst.wsdl.internal.impl.BindingInputImpl)
        currentBindingInput.insertBefore(adoptedDOMElement, currentBindingInput.getFirstChild());
      else if (owner instanceof org.eclipse.wst.wsdl.internal.impl.BindingOutputImpl)
        currentBindingOutput.insertBefore(adoptedDOMElement, currentBindingOutput.getFirstChild());
      else if (owner instanceof org.eclipse.wst.wsdl.internal.impl.BindingFaultImpl)
        currentBindingFault.insertBefore(adoptedDOMElement, currentBindingFault.getFirstChild());

    }

  }

  public static Test suite()
  {
    TestSuite suite = new TestSuite();

    suite.addTest(new WSDLConverter("Convert")
      {
        protected void runTest()
        {
          testConvert();
        }
      });
    return suite;
  }

  public void testConvert()
  {
    try
    {
      Definition def = DefinitionLoader.load("d:/eclipse301/eclipse/workspace/org.eclipse.wst.wsdl.tests/PTATimeDistribution.wsdl");
      WSDLConverter converter = new WSDLConverter(def);
      converter.generate20("d:/eclipse301/eclipse/workspace/org.eclipse.wst.wsdl.tests/PTATimeDistribution20.wsdl");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {
    junit.textui.TestRunner.run(suite());
    /*
     try
     {
     Definition def = DefinitionLoader.load(args[0]);
     WSDLConverter converter = new WSDLConverter(def);
     converter.generate20("test20.wsdl");
     }
     catch (Exception e)
     {
     e.printStackTrace();
     }*/
  }
}
