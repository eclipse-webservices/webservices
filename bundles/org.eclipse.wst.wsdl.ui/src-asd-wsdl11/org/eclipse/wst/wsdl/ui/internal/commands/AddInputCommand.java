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

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.ui.internal.actions.SmartRenameAction;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;


public final class AddInputCommand extends AddMessageReferenceCommand
{
  private Input originalInput;
	
  public AddInputCommand(Operation operation, String name)
  {
    super(operation,name);
  }

  public AddInputCommand(Operation operation, String name, boolean createMessage)
  {
    super(operation,name,createMessage);
  }
  
  /*
   * Constructor used to create an Input based on the given Input.  In essence, this will
   * create a copy of the given Input.
   */
  public AddInputCommand(Operation operation, Input originalInput, String name) {
  	super(operation, name, false);
  	
  	this.originalInput = originalInput;
  }
  
  public void run()
  {
    messageReference = WSDLFactory.eINSTANCE.createInput();
  //  messageReference.setName(name); 
    messageReference.setEnclosingDefinition(operation.getEnclosingDefinition());
    operation.setInput((Input)messageReference);
    
    // TBD - add binding input 
    
    if (originalInput == null) {
    	if (createMessage)
    		createMessage();
    }
    else {
    	// Do necessary copying of data from original Input to new Input
    	if (originalInput.getEMessage() != null) {
    		String originalMsgName = originalInput.getEMessage().getQName().getLocalPart();
    		
    		boolean gened = false;
    		if (originalInput.eContainer() instanceof Operation)
    			gened = SmartRenameAction.isMessageNameGenerated(originalMsgName, ((Operation) originalInput.eContainer()).getName(), "Request"); //$NON-NLS-1$
    		
    		String newMsgName = ""; //$NON-NLS-1$
    		if (gened) {
    			newMsgName = NameUtil.buildUniqueMessageName(operation.getEnclosingDefinition(), messageReference);
    		}
    		else {
    			Definition definition = operation.getEnclosingDefinition();
    			Message tMessage = originalInput.getEMessage();
    			newMsgName = NameUtil.buildUniqueMessageName(definition, tMessage.getQName().getLocalPart());
    		}

    		createMessage(originalInput, newMsgName);
    	}
    }
  }
  
}
