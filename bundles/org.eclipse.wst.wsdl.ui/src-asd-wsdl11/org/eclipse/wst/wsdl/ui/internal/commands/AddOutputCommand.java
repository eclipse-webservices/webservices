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
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.ui.internal.actions.SmartRenameAction;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;


public final class AddOutputCommand extends AddMessageReferenceCommand
{
  private Output originalOutput;
  
  public AddOutputCommand(Operation operation, String name)
  {
    super(operation,name);
  }

  public AddOutputCommand(Operation operation, String name, boolean createMessage)
  {
    super(operation,name,createMessage);
  }
  
  /*
   * Constructor used to create an Output based on the given Output.  In essence, this will
   * create a copy of the given Output.
   */
  public AddOutputCommand(Operation operation, Output originalOutput, String name) {
  	super(operation, name, false);
  	
  	this.originalOutput = originalOutput;
  }
  
  public void run()
  {
    messageReference = WSDLFactory.eINSTANCE.createOutput();
//    messageReference.setName(name);
    messageReference.setEnclosingDefinition(operation.getEnclosingDefinition());
    operation.setOutput((Output)messageReference);
    
    //  TBD - add binding output 
    
    if (originalOutput == null) {
    	if (createMessage)
    		createMessage();
    }
    else {
    	// Do necessary copying of data from original Output to new Output
    	if (originalOutput.getEMessage() != null) {
    		String originalMsgName = originalOutput.getEMessage().getQName().getLocalPart();
		
		boolean gened = false;
		if (originalOutput.eContainer() instanceof Operation)
			gened = SmartRenameAction.isMessageNameGenerated(originalMsgName, ((Operation) originalOutput.eContainer()).getName(), "Response");
		
		String newMsgName = "";
		if (gened) {
			newMsgName = NameUtil.buildUniqueMessageName(operation.getEnclosingDefinition(), messageReference);
		}
		else {
			Definition definition = operation.getEnclosingDefinition();
			Message tMessage = originalOutput.getEMessage();
			newMsgName = NameUtil.buildUniqueMessageName(definition, tMessage.getQName().getLocalPart());
		}
		
    	createMessage(originalOutput, newMsgName);
    	}
    }
  }
}
