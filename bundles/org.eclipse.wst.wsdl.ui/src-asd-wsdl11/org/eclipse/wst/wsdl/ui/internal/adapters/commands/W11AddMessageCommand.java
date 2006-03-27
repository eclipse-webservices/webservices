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
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.commands.AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class W11AddMessageCommand extends Command {
    private Definition definition;
	
    public W11AddMessageCommand(Definition definition) {
        this.definition = definition;
    }
    
    public void execute() {
    	AddMessageCommand command = new AddMessageCommand(definition, NameUtil.buildUniqueMessageName(definition, "NewMessage"), true);
        command.run();
    }  
}
