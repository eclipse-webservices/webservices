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

import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.commands.AddBaseParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddFaultCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddInputCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddInputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddMessageReferenceCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOperationCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOutputCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddOutputParameterCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class W11AddOperationCommand extends Command implements IASDAddCommand {
	private PortType portType;
	private Operation operation;
	
	public W11AddOperationCommand(PortType portType) {
        super(Messages.getString("_UI_ACTION_ADD_OPERATION"));
		this.portType = portType;
	}
	
	public void execute() {
		String name = NameUtil.getOperationName(portType);
		AddOperationCommand operationCommand = new AddOperationCommand(portType, name);
		operationCommand.run();
		operation = (Operation) operationCommand.getWSDLElement();

		createMessage(operation, IMessageReference.KIND_INPUT);
		createMessage(operation, IMessageReference.KIND_OUTPUT);

		createParameter(operation, null, IMessageReference.KIND_INPUT);
		createParameter(operation, null, IMessageReference.KIND_OUTPUT);

		operation.setStyle(OperationType.REQUEST_RESPONSE);
	}
	
	public Object getNewlyAddedComponent() {
		return operation;
	}
	
	private MessageReference createMessage(Operation operation, int messageKind) {
		MessageReference messageRef = null;
		AddMessageReferenceCommand messageRefCommand = null;
	    if (messageKind == IMessageReference.KIND_INPUT)
	    {
	    	messageRefCommand = new AddInputCommand(operation, null);
	    }
	    else if (messageKind == IMessageReference.KIND_OUTPUT)
	    {
	    	messageRefCommand = new AddOutputCommand(operation, null);
	    }
	    else if (messageKind == IMessageReference.KIND_FAULT)
	    {
	    	messageRefCommand = new AddFaultCommand(operation, NameUtil.getFaultName(operation));
	    }
	    messageRefCommand.run();
	    messageRef = (MessageReference) messageRefCommand.getWSDLElement();
	    
	    AddMessageCommand command = new AddMessageCommand(messageRef.getEnclosingDefinition(), NameUtil.getMessageName(messageRef));
	    command.run();
	    messageRef.setEMessage((Message) command.getWSDLElement());
	    
	    return messageRef;
	}
	
	private void createParameter(Operation operation, Part part, int kind) {
		  IParameter parameter = null;
		  AddBaseParameterCommand addParameterCommand = null;
		  
		  if (kind == IMessageReference.KIND_INPUT) {
			  int parameterPattern = AddBaseParameterCommand.getParameterPattern(portType, true);
			  if (parameterPattern == -1) {
				  parameterPattern = AddBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT;
			  }
			  addParameterCommand = new AddInputParameterCommand(operation, parameterPattern);
		  }
		  else if (kind == IMessageReference.KIND_OUTPUT) {
			  int parameterPattern = AddBaseParameterCommand.getParameterPattern(portType, false);
			  if (parameterPattern == -1) {
				  parameterPattern = AddBaseParameterCommand.PART_ELEMENT_SEQ_ELEMENT;
			  }			  
			  addParameterCommand = new AddOutputParameterCommand(operation, parameterPattern);
		  }
		  
		  if (parameter == null && addParameterCommand != null) {
			  addParameterCommand.run();
		  }
	}
}
