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
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.commands.AddImportCommand;

public class W11AddImportCommand extends Command {
	private Definition definition;
	
	public W11AddImportCommand(Definition definition) {
        super(Messages.getString("_UI_ACTION_ADD_IMPORT"));
		this.definition = definition;
	}
	
	public void execute() {
		String namespace = ""; //$NON-NLS-1$
		String location = ""; //$NON-NLS-1$
		AddImportCommand command = new AddImportCommand(definition, namespace, location);
		command.run();
	}
}