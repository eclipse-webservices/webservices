/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.commands;

import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.xml.core.internal.contentmodel.util.NamespaceTable;
import org.eclipse.wst.xsd.ui.internal.common.commands.AddExtensionCommand;
import org.eclipse.xsd.XSDElementDeclaration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AddExtensionElementCommand extends AddExtensionCommand
{
  ExtensibleElement input;
  XSDElementDeclaration element;
  Element appInfo;
  Element newElement;

  public AddExtensionElementCommand(String label, ExtensibleElement input, XSDElementDeclaration element)
  {
    super(label);
    this.input = input;
    this.element = element;
  }

  public void execute()
  {
    super.execute();
    Element parentElement = input.getElement();
    Document doc = parentElement.getOwnerDocument();
    
    Element newElement = doc.createElementNS(extensionsSchemaSpec.getNamespaceURI(), element.getName());
    
    String prefix = addNamespaceDeclarationIfRequired(doc.getDocumentElement(), "p", extensionsSchemaSpec.getNamespaceURI()); //$NON-NLS-1$
    newElement.setPrefix(prefix);
    parentElement.appendChild(newElement);
  }

  public void undo()
  {
    // TODO (cs) do we need to implement?
  }
  public Object getNewObject()
  {
    return newElement;
  }
  
  private String addNamespaceDeclarationIfRequired(Element rootElement, String prefixHint, String namespace)
  {
    String prefix = null;      
    NamespaceTable namespaceTable = new NamespaceTable(rootElement.getOwnerDocument());
    namespaceTable.addElement(rootElement);
    prefix = namespaceTable.getPrefixForURI(namespace);
    if (prefix == null)
    { 
      String basePrefix = prefixHint;
      prefix = basePrefix;
      String xmlnsColon = "xmlns:"; //$NON-NLS-1$
      String attributeName = xmlnsColon + prefix;
      int count = 0;
      while (rootElement.getAttribute(attributeName) != null)
      {
        count++;
        prefix = basePrefix + count;
        attributeName = xmlnsColon + prefix;
      }      
      rootElement.setAttribute(attributeName, namespace);  
    }    
    return prefix;
  }  
}
