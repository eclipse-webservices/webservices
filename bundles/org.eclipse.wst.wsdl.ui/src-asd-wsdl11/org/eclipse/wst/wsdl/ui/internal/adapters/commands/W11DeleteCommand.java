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
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Binding;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11EndPoint;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Interface;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Operation;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Service;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Type;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IImport;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessage;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.xsd.XSDConcreteComponent;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.Element;

public class W11DeleteCommand extends W11TopLevelElementCommand {
	protected WSDLBaseAdapter object;
	
	public W11DeleteCommand(WSDLBaseAdapter object) {
        super(Messages._UI_ACTION_DELETE, null);
		this.object = object;
	}
	
	public void execute() {
		Element element = null;
		if (object.getTarget() instanceof WSDLElement) {
			element = ((WSDLElement) object.getTarget()).getElement();
		}
		else if (object.getTarget() instanceof XSDConcreteComponent) {
			element = ((XSDConcreteComponent) object.getTarget()).getElement();
		}
		
		try {
			beginRecording(element);
			
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
			else if (object instanceof W11Type) {
				W11Type w11Type = (W11Type) object;
				XSDSchema schema = (XSDSchema)  w11Type.getTarget();
				XSDSchemaExtensibilityElement eeElement = (XSDSchemaExtensibilityElement) schema.eContainer();
				Types type = (Types) eeElement.getContainer();
				type.getEExtensibilityElements().remove(eeElement);
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
		finally {
			endRecording(element);
		}
	}
}
