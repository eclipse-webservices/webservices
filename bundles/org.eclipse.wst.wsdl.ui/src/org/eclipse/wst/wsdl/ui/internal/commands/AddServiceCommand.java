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

import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;


public final class AddServiceCommand extends WSDLElementCommand
{
  private Definition definition;
  private String localName;
  private Service service;
  
  private boolean createPort = false;
  private final String DEFAULT_PORT_NAME = "NewPort";

  public AddServiceCommand
		(Definition definition,  
		 String localName)
	{
	  this.definition = definition;
	  this.localName = localName;
	}

	public AddServiceCommand
		(Definition definition, 
		 String localName,
		 boolean createPort)
	{
	  this.definition = definition;
	  this.localName = localName;
	  this.createPort = createPort;
	}

  public WSDLElement getWSDLElement()
  {
    return service;
  }

  public void run()
  {
    service = WSDLFactory.eINSTANCE.createService();
    service.setQName(new QName(definition.getTargetNamespace(),localName));
    service.setEnclosingDefinition(definition);
    definition.addService(service);
    
    if (createPort)
    {
      AddPortCommand command = 
        new AddPortCommand(service,DEFAULT_PORT_NAME);
      command.run();
    }
  }
  
  public void setLocalName(String name)
  {
    localName = name;
  }
}
