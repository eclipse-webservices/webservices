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
package org.eclipse.wst.wsdl.ui.internal.adapters.commands;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class W11AddMessageCommand extends W11TopLevelElementCommand implements IASDAddCommand {
    private String newName;
    private Message message;
	
    public W11AddMessageCommand(Definition definition) {
        super(Messages._UI_ACTION_ADD_MESSAGE, definition);
    }
    
	public void setNewMessageName(String newName) {
		this.newName = newName;
	}

    public void execute() {
    	try {
    		beginRecording(definition.getElement());
    		super.execute();
			if (newName == null || newName.equals("")) { //$NON-NLS-1$
				newName = NameUtil.buildUniqueMessageName(definition, "NewMessage"); //$NON-NLS-1$
			}
	
	    	AddMessageCommand command = new AddMessageCommand(definition, newName, true);
	        command.run();
	        message = (Message) command.getWSDLElement();
	        formatChild(message.getElement());
    	}
    	finally {
    		endRecording(definition.getElement());
    	}
    }
    
    public Message getNewMessage() {
    	return message;
    }
    
    public Object getNewlyAddedComponent() {
    	return message;
    }
}