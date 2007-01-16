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
import org.eclipse.xsd.XSDConcreteComponent;

public class W11AddOutputParameterCommand extends W11TopLevelElementCommand implements IASDAddCommand {
	protected Operation operation;
	private Object output;
	private int parameterPattern = -1;
	
	public W11AddOutputParameterCommand(Operation operation) {
        super(Messages._UI_ACTION_ADD_OUTPUT, operation.getEnclosingDefinition());
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
				parameterPattern = AddBaseParameterCommand.getParameterPattern(operation, false);
				if (parameterPattern == -1) {
					parameterPattern = AddBaseParameterCommand.getParameterPattern(operation);
				}
			}
			
			AddOutputParameterCommand command = new AddOutputParameterCommand(operation, parameterPattern);
			command.run();
			output = command.getNewlyAddedComponentPart();
			
			formatChild(operation.getEOutput().getElement());
			if (command.getXSDElementDeclaration() != null) {
				// Try to grab the "inner" XSDElement
				output = getNewXSDElement(command.getXSDElementDeclaration());
				formatChild(getXSDParent(command.getXSDElementDeclaration()).getElement());
			}
		}
		finally {
			endRecording(operation.getElement());
		}
	}
	
	protected XSDConcreteComponent getXSDParent(XSDConcreteComponent xsd) {
		XSDConcreteComponent parent = xsd;
		
		for (int index = 0; index < 4; index++) {
			XSDConcreteComponent previous = parent;
			parent = parent.getContainer();
			if (parent == null) {
				parent = previous;
				break;
			}
		}
		
		return parent;
	}
	
	public Object getNewlyAddedComponent() {
		return output;
	}
}