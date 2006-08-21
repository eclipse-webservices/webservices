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
import org.eclipse.wst.wsdl.ui.internal.commands.AddInputParameterCommand;

public class W11AddInputParameterCommand extends W11TopLevelElementCommand implements IASDAddCommand{
	protected Operation operation;
	private Object input;
	
	public W11AddInputParameterCommand(Operation operation) {
        super(Messages._UI_ACTION_ADD_INPUT, operation.getEnclosingDefinition());
		this.operation = operation;
	}
	
	public void execute() {
		try {
			beginRecording(operation.getElement());

			// Determine which Pattern we should use.  For example, ADDBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT
			int pattern = AddBaseParameterCommand.getParameterPattern(operation, true);
			if (pattern == -1) {
				pattern = AddBaseParameterCommand.getParameterPattern(operation);
			}
			AddInputParameterCommand command = new AddInputParameterCommand(operation, pattern);
			command.run();
			
			formatChild(operation.getEInput().getElement());
			formatChild(command.getXSDElementDeclaration().getContainer().getContainer().getContainer().getElement());
			
			input = operation.getEInput();
		}
		finally {
			endRecording(operation.getElement());
		}
	}
	
	public Object getNewlyAddedComponent() {
		return input;
	}
}
