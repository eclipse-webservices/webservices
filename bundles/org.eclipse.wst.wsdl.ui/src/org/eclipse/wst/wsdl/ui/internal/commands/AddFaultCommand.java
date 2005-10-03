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

import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;


public final class AddFaultCommand extends AddMessageReferenceCommand
{
  private Fault originalFault;
  
  public AddFaultCommand(Operation operation, String name)
  {
    super(operation,name);
  }

  public AddFaultCommand(Operation operation, String name, boolean createMessage)
  {
    super(operation,name,createMessage);
  }
  
  /*
   * Constructor used to create a Fault based on the given Fault.  In essence, this will
   * create a copy of the given Fault.
   */
  public AddFaultCommand(Operation operation, Fault originalFault, String name) {
  	super(operation, name, false);
  	
  	this.originalFault = originalFault;
  }
  
  public void run()
  {
    messageReference = WSDLFactory.eINSTANCE.createFault();
    messageReference.setName(name); 
    messageReference.setEnclosingDefinition(operation.getEnclosingDefinition());
    operation.addFault((Fault)messageReference);
    
    //  TBD - add binding fault

    if (originalFault == null) {
    	if (createMessage)
    		createMessage();
    }
    else {
    	// Do necessary copying of data from original Fault to new Fault
    	if (originalFault.getEMessage() != null) {
//    		String originalMsgName = originalFault.getEMessage().getQName().getLocalPart();
//    		
//    		boolean gened = false;
//    		if (originalFault.eContainer() instanceof Operation)
//    			gened = SmartRenameAction.isMessageNameGenerated(originalMsgName, ((Operation) originalFault.eContainer()).getName(), "");
//    		
//    		String newMsgName = "";
//    		if (gened) {
//    			newMsgName = NameUtil.buildUniqueMessageName(operation.getEnclosingDefinition(), messageReference);
//    		}
//    		else {
//    			Definition definition = operation.getEnclosingDefinition();
//    			Message tMessage = originalFault.getEMessage();
//    			newMsgName = NameUtil.buildUniqueMessageName(definition, tMessage.getQName().getLocalPart());
//    		}
    		
    		createMessage(originalFault, NameUtil.buildUniqueMessageName(operation.getEnclosingDefinition(), messageReference));
    	}
    }
  }
}
