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
package org.eclipse.wst.wsdl.ui.internal.commands;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;

public final class AddImportCommand extends WSDLElementCommand
{
  private Definition definition;
  private String namespace;
  private String location;
  private Import importElement;
  
  public AddImportCommand
  	(Definition definition,  
  	 String namespace,
  	 String location)
  {
    this.definition = definition;
    this.namespace = namespace;
    this.location = location;
  }
  
  public WSDLElement getWSDLElement()
  {
    return importElement;
  }

  public void run()
  {
    importElement = WSDLFactory.eINSTANCE.createImport();
    importElement.setNamespaceURI(namespace);
    importElement.setLocationURI(location);
    importElement.setEnclosingDefinition(definition);
    definition.addImport(importElement);
  }
  
  public void setNamespaceURI(String namespace)
  {
    this.namespace = namespace;
  }
  
  public void setLocationURI(String location)
  {
    this.location = location;
  }
}
