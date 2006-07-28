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
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddBaseParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddFaultParameterCommand;

public class W11AddFaultParameterCommand extends Command implements IASDAddCommand {
	protected Operation operation;
	protected Fault fault;
	
	public W11AddFaultParameterCommand(Operation operation, Fault fault) {
        super(Messages.getString("_UI_ACTION_ADD_FAULT"));
		this.operation = operation;
		this.fault = fault;
	}
	
	public void execute() {
		// Determine which Pattern we should use.  For example, ADDBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT
		int pattern = AddBaseParameterCommand.getParameterPattern(operation);
		AddFaultParameterCommand command = new AddFaultParameterCommand(operation, fault);
		command.setStyle(pattern);
		command.run();
		fault = command.getFault();
	}
	
	public Object getNewlyAddedComponent() {
		return fault;
	}
}