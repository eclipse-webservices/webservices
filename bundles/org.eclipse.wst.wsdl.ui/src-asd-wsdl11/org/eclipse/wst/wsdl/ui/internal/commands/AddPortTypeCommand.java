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
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;


public final class AddPortTypeCommand extends WSDLElementCommand
{
  private Definition definition;
  private String localName;
  private PortType portType;
  
  private boolean createOperation = false;
  private final String DEFAULT_OPERATION_NAME = "NewOperation";

  public AddPortTypeCommand
		(Definition definition,  
		 String localName)
	{
	  this.definition = definition;
	  this.localName = localName;
	}

  public AddPortTypeCommand
		(Definition definition, 
		 String localName,
		 boolean createOperation)
	{
	  this.definition = definition;
	  this.localName = localName;
	  this.createOperation = createOperation;
	}

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.ui.internal.commands.WSDLElementAction#getWSDLElement()
   */
  public WSDLElement getWSDLElement()
  {
    return portType;
  }

  public void run()
  {
    portType = WSDLFactory.eINSTANCE.createPortType();
    portType.setQName(new QName(definition.getTargetNamespace(),localName));
    portType.setEnclosingDefinition(definition);
    definition.addPortType(portType);
    
    if (createOperation)
    {
      AddOperationCommand command = 
        new AddOperationCommand(portType,DEFAULT_OPERATION_NAME,true,true,false);
      command.run();
    }
  }
  
  public void setLocalName(String name)
  {
    localName = name;
  }
}
