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

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.commands.AddBaseParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOutputParameterCommand;

public class W11AddOutputParameterCommand extends Command {
	protected Operation operation;
	
	public W11AddOutputParameterCommand(Operation operation) {
        super(Messages.getString("_UI_ACTION_ADD_OUTPUT"));
		this.operation = operation;
	}
	
	public void execute() {
		// Determine which Pattern we should use.  For example, ADDBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT
		int pattern = getParameterPattern();
		AddOutputParameterCommand command = new AddOutputParameterCommand(operation, pattern);
		command.run();		
	}
	
	private int getParameterPattern() {
		int pattern = AddBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT;
		
		// TODO: rmah: Should we be checking if there's an existing Input.  If so, we should
		// try to determine the pattern from there if we fail to get it from the Output???
		if (operation.getEOutput() != null) {
			Output output = operation.getEOutput();
			
			if (output.getEMessage() != null) {
				Message message = output.getEMessage();
				List parts = message.getEParts();
				
				if (parts.size() > 0) {
					Part part = (Part) parts.get(0);
					if (part.getTypeDefinition() != null) {
						pattern = AddBaseParameterCommand.PART_COMPLEXTYPE_SEQ_ELEMENT;
					}
				}
			}
		}
		
		return pattern;
	}
}