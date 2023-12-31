/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.commands;

import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.UnknownExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.internal.impl.UnknownExtensibilityElementImpl;

public final class AddUnknownExtensibilityElementCommand extends WSDLElementCommand
{
  private ExtensibleElement extensibleElement;
  private String namespaceURI;
  private String localName;
  private UnknownExtensibilityElement extensibilityElement;
  private Map properties;
 
  public AddUnknownExtensibilityElementCommand
    (ExtensibleElement extensibleElement, 
     String namespaceURI,
     String localName)
  {
    this.extensibleElement = extensibleElement;
    this.namespaceURI = namespaceURI;
    this.localName = localName;
  }
  
  public AddUnknownExtensibilityElementCommand
    (ExtensibleElement extensibleElement, 
     String namespaceURI,
     String localName,
     Map properties)
  {
    this.extensibleElement = extensibleElement;
    this.namespaceURI = namespaceURI;
    this.localName = localName;
    this.properties = properties;
  }
  
  public WSDLElement getWSDLElement()
  {
    return extensibilityElement;
  }

  public void run()
  {
  	extensibilityElement = WSDLFactory.eINSTANCE.createUnknownExtensibilityElement();
  	extensibilityElement.setElementType(new QName(namespaceURI,localName));
  	
    if (properties != null)
    {
      Iterator iterator = properties.entrySet().iterator();
      Map.Entry entry = null;
      while (iterator.hasNext())
      {
      	entry = (Map.Entry)iterator.next();
      	String attribute = (String)entry.getKey();
      	String value = (String)entry.getValue();      	
      	((UnknownExtensibilityElementImpl)extensibilityElement).setAttribute(attribute,value);
      }
    }
    
  	extensibilityElement.setEnclosingDefinition(extensibleElement.getEnclosingDefinition());
  	extensibleElement.addExtensibilityElement(extensibilityElement);
  }
}
