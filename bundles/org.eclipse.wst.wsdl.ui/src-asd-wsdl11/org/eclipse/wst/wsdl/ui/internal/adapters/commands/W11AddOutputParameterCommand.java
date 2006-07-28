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

import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddBaseParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOutputParameterCommand;

public class W11AddOutputParameterCommand extends W11TopLevelElementCommand implements IASDAddCommand {
	protected Operation operation;
	private Object output;
	
	public W11AddOutputParameterCommand(Operation operation) {
        super(Messages.getString("_UI_ACTION_ADD_OUTPUT"), operation.getEnclosingDefinition());
		this.operation = operation;
	}
	
	public void execute() {
		// Determine which Pattern we should use.  For example, ADDBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT
		int pattern = AddBaseParameterCommand.getParameterPattern(operation, false);
		if (pattern == -1) {
			pattern = AddBaseParameterCommand.getParameterPattern(operation);
		}
		AddOutputParameterCommand command = new AddOutputParameterCommand(operation, pattern);
		command.run();
		formatChild(operation.getEOutput().getElement());
		formatChild(command.getXSDElementDeclaration().getContainer().getContainer().getContainer().getElement());
		output = operation.getEOutput();
	}
	
	public Object getNewlyAddedComponent() {
		return output;
	}
}