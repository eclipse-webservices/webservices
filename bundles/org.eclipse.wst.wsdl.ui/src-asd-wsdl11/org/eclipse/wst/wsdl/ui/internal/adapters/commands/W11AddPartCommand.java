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

import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.ui.internal.commands.AddPartCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class W11AddPartCommand extends Command {
    private Message message;
    
    public W11AddPartCommand(Message message) {
        this.message = message;
    }
    
    public void execute() {
    	AddPartCommand command = new AddPartCommand(message, NameUtil.buildUniquePartName(message, "NewPart"));
        command.run();
    }  
}
