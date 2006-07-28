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

import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddPartCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class W11AddPartCommand extends W11TopLevelElementCommand implements IASDAddCommand {
    private Message message;
    private Part part;
    
    public W11AddPartCommand(Message message) {
        super(Messages.getString("_UI_ACTION_ADD_PART"), message.getEnclosingDefinition());
        this.message = message;
    }
    
    public void execute() {
    	AddPartCommand command = new AddPartCommand(message, NameUtil.buildUniquePartName(message, "NewPart")); //$NON-NLS-1$
        command.run();
        part = (Part) command.getWSDLElement();
        formatChild(part.getElement());
    }
    
	public Object getNewlyAddedComponent() {
		return part;
	}
}
