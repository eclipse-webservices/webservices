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


public class HttpContentGenerator implements ContentGenerator
{
  protected Definition definition;
  protected boolean isPost;
  protected String addressLocation = ContentGenerator.ADDRESS_LOCATION;

  protected final static String[] requiredNamespaces = { "http://schemas.xmlsoap.org/wsdl/mime/", "http://schemas.xmlsoap.org/wsdl/http/" };
  protected final static String[] preferredNamespacePrefixes = { "mime", "http" };

  public void init(Definition definition, Object generator, Object[] options)
  {
    this.definition = definition;

    if (options != null)
    {
      if (options.length > 0 && options[0] != null)
      {
        Boolean isPostOption = (Boolean) options[0];
        isPost = isPostOption.booleanValue();
      }
      if (options.length > 1 && options[1] != null)
      {
        addressLocation = (String) options[1];
      }
    }
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
    Element element = createElement(portElement, "http", "address");
    element.setAttribute("location", addressLocation);
  }

  public void generateBindingContent(Element bindingElement, PortType portType)
  {
    Element element = createElement(bindingElement, "http", "binding");
    element.setAttribute("verb", isPost ? "POST" : "GET");
  }

  public void generateBindingOperationContent(Element bindingOperationElement, Operation operation)
  {
    Element element = createElement(bindingOperationElement, "http", "operation");
    element.setAttribute("location", "/" + operation.getName());
  }

  public void generateBindingInputContent(Element bindingInputElement, Input input)
  {
    if (isPost)
    {
      Element element = createElement(bindingInputElement, "mime", "content");
      element.setAttribute("type", "application/x-www-form-urlencoded");
    }
    else
    {
      Element element = createElement(bindingInputElement, "http", "urlEncoded");
    }
  }

  public void generateBindingOutputContent(Element bindingOutputElement, Output output)
  {
    Element element = createElement(bindingOutputElement, "mime", "content");
    element.setAttribute("type", "text/xml");
  }

  public void generateBindingFaultContent(Element bindingFaultElement, Fault fault)
  {
    //TODO!!
  }

  protected Element createElement(Element parentElement, String prefix, String elementName)
  {
    String name = prefix != null ? (prefix + ":" + elementName) : elementName;
    Element result = parentElement.getOwnerDocument().createElement(name);
    parentElement.appendChild(result);
    return result;
  }
}