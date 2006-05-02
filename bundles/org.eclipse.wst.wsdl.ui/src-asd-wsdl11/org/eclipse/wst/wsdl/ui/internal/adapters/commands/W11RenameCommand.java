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

import javax.xml.namespace.QName;

import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Binding;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11EndPoint;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Interface;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11MessageReference;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Operation;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForPart;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Service;

public class W11RenameCommand extends Command {
	protected WSDLBaseAdapter object;
	protected String newName = ""; //$NON-NLS-1$
	
	public W11RenameCommand(WSDLBaseAdapter object, String newName) {
        super(Messages.getString("_UI_ACTION_RENAME"));
		this.object = object;
		this.newName = newName;
	}
	
	public void execute() {
		if (object instanceof W11Description) {
			Definition definition = (Definition) object.getTarget();
			String ns = definition.getQName().getNamespaceURI();
			definition.setQName(new QName(ns, newName));
		}
		else if (object instanceof W11Service) {
			Service service = (Service) object.getTarget();
			String ns = service.getQName().getNamespaceURI();
			service.setQName(new QName(ns, newName));
		}
		else if (object instanceof W11EndPoint) {
			Port port = (Port) object.getTarget();
			port.setName(newName);
		}
		else if (object instanceof W11Binding) {
			Binding binding = (Binding) object.getTarget();
			String ns = binding.getQName().getNamespaceURI();
			binding.setQName(new QName(ns, newName));
		}
		else if (object instanceof W11Interface) {
			PortType portType = (PortType) object.getTarget();
			String ns = portType.getQName().getNamespaceURI();
			portType.setQName(new QName(ns, newName));
		}
		else if (object instanceof W11Operation) {
			Operation operation = (Operation) object.getTarget();
			operation.setName(newName);
		}
		else if (object instanceof W11MessageReference) {
			W11Operation w11Operation = (W11Operation) ((W11MessageReference) object).getOwnerOperation();
			Operation operation = w11Operation.getOperation();
			operation.setName(newName);					
		}
		else if (object instanceof W11ParameterForPart) {
			Part part = (Part) ((W11ParameterForPart) object).getTarget();
			part.setName(newName);
		}
	}
}