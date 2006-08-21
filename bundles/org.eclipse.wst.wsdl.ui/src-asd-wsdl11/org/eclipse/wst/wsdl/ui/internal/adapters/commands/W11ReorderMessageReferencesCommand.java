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

import javax.wsdl.OperationType;

import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Operation;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;

public class W11ReorderMessageReferencesCommand extends W11TopLevelElementCommand {
	protected IMessageReference leftSibling;
	protected IMessageReference rightSibling;
	protected IMessageReference movingParameter;
	
	public W11ReorderMessageReferencesCommand(IMessageReference leftSibling, IMessageReference rightSibling, IMessageReference movingParameter) {
        super(Messages._UI_ACTION_REORDER_MESSAGE_REFERENCE, null);
		this.leftSibling = leftSibling;
		this.rightSibling = rightSibling;
		this.movingParameter = movingParameter;
	}
	
	public void execute() {
		IMessageReference leftSibElement = null;
		IMessageReference movingChild = null;
		
		if (leftSibling instanceof IMessageReference) {
			leftSibElement = (IMessageReference) leftSibling; 
		}
		if (movingParameter instanceof IMessageReference) {
			movingChild = (IMessageReference) movingParameter;
		}
		
		W11Operation w11Operation = (W11Operation) movingChild.getOwnerOperation();
		Operation operation = (Operation) w11Operation.getTarget();
		
		try {
			beginRecording(operation.getElement());

			if (movingChild.getKind() == IMessageReference.KIND_INPUT) {
				if (leftSibElement == null) {
					// Input/Output style
					setInputOutputOrder(operation, true);
				}
				else if (leftSibElement != null && leftSibElement.getKind() == IMessageReference.KIND_INPUT) {
					if (rightSibling.getKind() == IMessageReference.KIND_OUTPUT) {
						// Input/Output style
						setInputOutputOrder(operation, true);
					}
				}
				else {
					// Output/Input style
					setInputOutputOrder(operation, false);
				}
			}
			else if (movingChild.getKind() == IMessageReference.KIND_OUTPUT) {
				if (leftSibElement == null) {
					// Output/Input style
					setInputOutputOrder(operation, false);
				}
				else if (leftSibElement != null && leftSibElement.getKind() == IMessageReference.KIND_OUTPUT) {
					if (rightSibling.getKind() == IMessageReference.KIND_INPUT) {
						// Output/Input style
						setInputOutputOrder(operation, false);
					}
				}
				else {
					// Input/Output style
					setInputOutputOrder(operation, true);
				}
			}
		}
		finally {
			endRecording(operation.getElement());
		}
	}
	
	// boolean inputFirst = true if the Input is the first in the order of Input and Output
	private void setInputOutputOrder(Operation operation, boolean inputFirst) {
		// We need to determine more info before we can set the style	
		if (inputFirst && operation.getEInput() != null) {
			// Check if there is an Output
			if (operation.getEOutput() != null) {
				// Order is: Input, Output
				setOperationStyle(operation, OperationType.REQUEST_RESPONSE);
			}
			else {
				// Order is: Input
				setOperationStyle(operation, OperationType.ONE_WAY);
			}
		}
		else if (operation.getEOutput() != null) {
			// Check if there is an Input
			if (operation.getEInput() != null) {
				// Order is: Output, Input
				setOperationStyle(operation, OperationType.SOLICIT_RESPONSE);
			}
			else {
				// Order is: Output
				setOperationStyle(operation, OperationType.NOTIFICATION);
			}
		}
	}
	
	private void setOperationStyle(Operation operation, OperationType operationType) {
		// If there is no style to begin with, don't set one now
		if (operation.getStyle() == null) {
			return;
		}
		
		operation.setStyle(operationType);
	}
}