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
import org.eclipse.wst.xsd.ui.internal.common.commands.AddExtensionCommand;
import org.eclipse.xsd.XSDElementDeclaration;
import org.w3c.dom.Attr;
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
    Element rootElement = doc.createElementNS(extensionsSchemaSpec.getNamespaceURI(), element.getName());
    
//  TODO (cs) gotta fix this... need a simple way to compute a unique prefix
//    createUniquePrefix(parentElement);
    String prefix = "p"; 
    rootElement.setPrefix(prefix);
    newElement = rootElement;
    
    Attr nsURIAttribute = doc.createAttribute("xmlns:"+prefix);
    nsURIAttribute.setValue(extensionsSchemaSpec.getNamespaceURI());
    rootElement.setAttributeNode(nsURIAttribute);
    parentElement.appendChild(rootElement);
  }

  public void undo()
  {
    // TODO (cs) do we need to implement?
  }
  public Object getNewObject()
  {
    return newElement;
  }
}
