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

import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddBaseParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddFaultParameterCommand;

public class W11AddFaultParameterCommand extends W11TopLevelElementCommand implements IASDAddCommand {
	protected Operation operation;
	protected Fault fault;
	protected Object newObject;
	private int parameterPattern = -1;
	
	public W11AddFaultParameterCommand(Operation operation, Fault fault) {
        super(Messages._UI_ACTION_ADD_FAULT, operation.getEnclosingDefinition());
		this.operation = operation;
		this.fault = fault;
	}
	
	public void setParameterPattern(int pattern) {
		parameterPattern = pattern;
	}
	
	public void execute() {
		try {
			beginRecording(operation.getElement());

			if (parameterPattern == -1) {
				// Determine which Pattern we should use.  For example, ADDBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT
				parameterPattern = AddFaultParameterCommand.getParameterPatternForFault(operation, fault);
				if (parameterPattern == -1) {
					parameterPattern = AddBaseParameterCommand.getParameterPattern(operation);
				}
			}
			
			AddFaultParameterCommand command = new AddFaultParameterCommand(operation, fault);
			command.setStyle(parameterPattern);
			command.run();
			fault = (Fault) command.getMessageReference();
			newObject = command.getNewlyAddedComponentPart();
			
			if (command.getXSDElementDeclaration() != null) {
				// Try to grab the "inner" XSDElement
				newObject = getNewXSDElement(command.getXSDElementDeclaration());
			}
		}
		finally {
			endRecording(operation.getElement());
		}
	}
	
	public Object getNewlyAddedComponent() {
		return newObject;
	}
}