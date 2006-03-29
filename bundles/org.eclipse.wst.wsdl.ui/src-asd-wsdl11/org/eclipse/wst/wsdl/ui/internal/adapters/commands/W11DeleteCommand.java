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
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.asd.facade.IImport;
import org.eclipse.wst.wsdl.asd.facade.IMessage;
import org.eclipse.wst.wsdl.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.asd.facade.IParameter;
import org.eclipse.wst.wsdl.asd.facade.IType;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Binding;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11EndPoint;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Interface;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Operation;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Service;

public class W11DeleteCommand extends Command {
	protected WSDLBaseAdapter object;
	
	public W11DeleteCommand(WSDLBaseAdapter object) {
		this.object = object;
	}
	
	public void execute() {
		if (object instanceof W11Service) {
			Service service = (Service) object.getTarget();
			service.getEnclosingDefinition().getEServices().remove(service);
		}
		else if (object instanceof W11EndPoint) {
			Service service = (Service) ((W11Service) ((W11EndPoint) object).getOwnerService()).getTarget();
			Port port = (Port) object.getTarget();
			service.getEPorts().remove(port);
		}
		else if (object instanceof W11Binding) {
			Binding binding = (Binding) object.getTarget();
			binding.getEnclosingDefinition().getEBindings().remove(binding);
		}
		else if (object instanceof W11Interface) {
			PortType portType = (PortType) object.getTarget();
			portType.getEnclosingDefinition().getEPortTypes().remove(portType);
		}
		else if (object instanceof W11Operation) {
			PortType portType = (PortType) ((W11Interface) ((W11Operation) object).getOwnerInterface()).getTarget();
			Operation operation = (Operation) object.getTarget();
			portType.getEOperations().remove(operation);
		}
		else if (object instanceof IParameter) {
			Part part = (Part) object.getTarget();
			Message message = (Message) part.eContainer();
			message.getEParts().remove(part);
		}
		else if (object instanceof IImport) {
			Import theImport = (Import) object.getTarget();
			theImport.getEnclosingDefinition().getEImports().remove(theImport);
		}
		else if (object instanceof IType) {
			
		}
		else if (object instanceof IMessageReference) {
			MessageReference messageRef = (MessageReference) object.getTarget();
			Operation operation = (Operation)messageRef.eContainer();
			if (messageRef instanceof Input) {
				operation.setEInput(null);
			}
			else if (messageRef instanceof Output) {
				operation.setEOutput(null);
			}
			else if (messageRef instanceof Fault) {
				operation.getEFaults().remove(messageRef);
			}
		}
		else if (object instanceof IMessage) {
			Message message = (Message) object.getTarget();
			message.getEnclosingDefinition().getEMessages().remove(message);
		}
	}
}
