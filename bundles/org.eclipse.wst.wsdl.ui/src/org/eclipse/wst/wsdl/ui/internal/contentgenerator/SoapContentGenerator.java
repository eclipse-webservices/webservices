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
package org.eclipse.wst.wsdl.ui.internal.contentgenerator;  
        
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.w3c.dom.Element;


public class SoapContentGenerator implements ContentGenerator
{ 
  private final int DOCUMENT_LITERAL = 1;
  private final int RPC_LITERAL      = 2;
  private final int RPC_ENCODED      = 3;

  protected int bindingOption = DOCUMENT_LITERAL;
  protected String namespaceValue = "";
  protected String addressLocation = ContentGenerator.ADDRESS_LOCATION;                   
  protected Definition definition;                                    

  protected final static String[] requiredNamespaces = {"http://schemas.xmlsoap.org/wsdl/soap/"};
  protected final static String[] preferredNamespacePrefixes = {"soap"};


  public void init(Definition definition, Object generator, Object[] options)
  {              
    this.definition = definition;
                       
    if (options != null)
    {
      bindingOption = computeBindingOption(options);

      if (options.length > 1 && options[1] != null)
      {
      	addressLocation = (String)options[1];
      }  
    }
    if (definition.getTargetNamespace() != null)
    {
      namespaceValue = definition.getTargetNamespace();
    } 
  }
  
  private int computeBindingOption(Object[] options) {
  	int option = DOCUMENT_LITERAL;
    if (options.length > 0 && options[0] != null)
    {
		boolean isDocumentLiteralOption = ((Boolean)options[0]).booleanValue();
		if (isDocumentLiteralOption)
			option =  DOCUMENT_LITERAL;
		else
			option = RPC_ENCODED;		
		
    	if (options.length >= 3 && options[2] != null) {
    		// Extra info sent in
    		if (((Boolean)options[2]).booleanValue())
    			option = RPC_LITERAL;
    	}
    }

    return option;
  }
     
  public String[] getRequiredNamespaces()
  {
    return requiredNamespaces;
  }
  
  public String[] getPreferredNamespacePrefixes()
  {
    return preferredNamespacePrefixes;
  }  

  public void generatePortContent(Element portElement, Port port)
  {
    Element element = createElement(portElement, "soap", "address");
    element.setAttribute("location", addressLocation);     
  }  

  public void generateBindingContent(Element bindingElement, PortType portType)
  {
    Element element = createElement(bindingElement, "soap", "binding");
    element.setAttribute("style", (bindingOption == DOCUMENT_LITERAL) ? "document" : "rpc"); 
    element.setAttribute("transport", "http://schemas.xmlsoap.org/soap/http");
  }

  public void generateBindingOperationContent(Element bindingOperationElement, Operation operation)
  {
    Element element = createElement(bindingOperationElement, "soap", "operation");
    
    String soapActionValue = namespaceValue;                                        
    if (!soapActionValue.endsWith("/"))
    {                
      soapActionValue += "/";
    }                        
    soapActionValue += operation.getName();
    element.setAttribute("soapAction", soapActionValue);
  }  

  public void generateBindingInputContent(Element bindingInputElement, Input input)
  {
    generateSoapBody(bindingInputElement);
  }

  public void generateBindingOutputContent(Element bindingOutputElement, Output output)
  {
    generateSoapBody(bindingOutputElement);
  }         

  public void generateBindingFaultContent(Element bindingFaultElement, Fault fault)
  {                              
    Element element = createElement(bindingFaultElement, "soap", "fault");      
    element.setAttribute("name", fault.getName()); 
    element.setAttribute("use", (bindingOption == RPC_ENCODED) ? "encoded" : "literal");
    if (bindingOption == RPC_ENCODED)
    {
      element.setAttribute("encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
      element.setAttribute("namespace", namespaceValue);
    }
    else if (bindingOption == RPC_LITERAL) {
        element.setAttribute("namespace", namespaceValue);
    }
  }  

  protected void generateSoapBody(Element parentElement)
  {               
    String soapActionValue = "";
    if (definition.getTargetNamespace() != null)
    {
      soapActionValue = definition.getTargetNamespace();
    }    

    Element element = createElement(parentElement, "soap", "body");      
    element.setAttribute("use", (bindingOption == RPC_ENCODED) ? "encoded" : "literal"); 
    if (bindingOption == RPC_ENCODED)
    {
      element.setAttribute("encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
      element.setAttribute("namespace", namespaceValue);
    }
    else if (bindingOption == RPC_LITERAL) {
    	element.setAttribute("namespace", namespaceValue);
    }
  }

  protected Element createElement(Element parentElement, String prefix, String elementName)
  {                                         
    String name = prefix != null ? (prefix + ":" + elementName) : elementName;
    Element result = parentElement.getOwnerDocument().createElement(name);
    parentElement.appendChild(result);
    return result;
  }
} 