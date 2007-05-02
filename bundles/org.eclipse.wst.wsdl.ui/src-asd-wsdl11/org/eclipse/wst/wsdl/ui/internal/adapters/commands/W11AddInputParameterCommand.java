/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
import org.eclipse.xsd.XSDConcreteComponent;

public class W11AddInputParameterCommand extends W11TopLevelElementCommand implements IASDAddCommand{
	protected Operation operation;
	private Object input;
	private int parameterPattern = -1;
	
	public W11AddInputParameterCommand(Operation operation) {
        super(Messages._UI_ACTION_ADD_INPUT, operation.getEnclosingDefinition());
		this.operation = operation;
	}
	
	public void setParameterPattern(int pattern) {
		parameterPattern = pattern;
	}
	
	public void execute() {
		try {
			beginRecording(operation.getElement());

			if (parameterPattern == -1) {
				// Determine which Pattern we should use.  For example, ADDBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT
				parameterPattern = AddBaseParameterCommand.getParameterPattern(operation, true);
				if (parameterPattern == -1) {
					parameterPattern = AddBaseParameterCommand.getParameterPattern(operation);
				}
			}
			
			AddInputParameterCommand command = new AddInputParameterCommand(operation, parameterPattern);
			command.run();
			input = command.getNewlyAddedComponentPart();
			
			formatChild(operation.getEInput().getElement());
			if (command.getXSDElementDeclaration() != null) {
				// Try to grab the "inner" XSDElement
				input = getNewXSDElement(command.getXSDElementDeclaration());
				formatChild(getXSDParent(command.getXSDElementDeclaration()).getElement());
			}
		}
		finally {
			endRecording(operation.getElement());
		}
	}
	
	protected XSDConcreteComponent getXSDParent(XSDConcreteComponent xsd) {
		XSDConcreteComponent parent = xsd.getSchema();
		return parent;
	}
	
	public Object getNewlyAddedComponent() {
		return input;
	}
}
