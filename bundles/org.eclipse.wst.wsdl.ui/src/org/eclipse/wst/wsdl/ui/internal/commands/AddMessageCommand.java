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

import java.util.Iterator;
import javax.xml.namespace.QName;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.ui.internal.actions.SmartRenameAction;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;


public final class AddMessageCommand extends WSDLElementCommand
{
  private Definition definition;
  private String localName;
  private Message message;
  private Message originalMessage;
  
  private boolean createPart = false;
  private final String DEFAULT_PART_NAME = "NewPart";
  
  public AddMessageCommand
  	(Definition definition,  
  	 String localName)
  {
    this.definition = definition;
    this.localName = localName;
  }
  
  public AddMessageCommand
		(Definition definition, 
		 String localName,
		 boolean createPart)
	{
	  this.definition = definition;
	  this.localName = localName;
	  this.createPart = createPart;
	}
  
  /*
   * Constructor used to create a Message based on the given Message.  In essence, this will
   * create a copy of the given Message.
   */
  public AddMessageCommand(Definition definition, Message originalMessage, String localName, boolean copyPart) {
    this.definition = definition;
	this.localName = localName;
	this.createPart = copyPart;
  	this.originalMessage = originalMessage;
  }
  
  public void run()
  {
    message = WSDLFactory.eINSTANCE.createMessage();
    message.setQName(new QName(definition.getTargetNamespace(),localName));
    message.setEnclosingDefinition(definition);
    definition.addMessage(message);
    
    if (originalMessage == null) {
    	if (createPart)
    	{
    		AddPartCommand command = 
    			new AddPartCommand(message,DEFAULT_PART_NAME,WSDLConstants.SCHEMA_FOR_SCHEMA_URI_2001,"string",true);
    		command.run();
    	}
    }
    else {
    	if (originalMessage.getEParts() != null) {
    		Iterator it = originalMessage.getEParts().iterator();
    		
    		while (it.hasNext()) {
    			Part part = (Part) it.next();
    			AddPartCommand command;
    			if (SmartRenameAction.isPartNameGenerated(part.getName(), originalMessage.getQName().getLocalPart()))
    				command = new AddPartCommand(message, part, NameUtil.buildUniquePartName(message, message.getQName().getLocalPart()));	
    			else
    				command = new AddPartCommand(message, part, part.getName());
    			
    			
    			command.run();
    		}
    	}
    }
  }

  public void setLocalName(String name)
  {
    this.localName = name;
  }
  
  public WSDLElement getWSDLElement()
  {
    return message;
  }
}
