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
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.ui.internal.commands.AddServiceCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class W11AddServiceCommand extends Command {
	private Definition definition;
	
	public W11AddServiceCommand(Definition definition) {
		this.definition = definition;
	}
	
	public void execute() {
		String newName = NameUtil.buildUniqueServiceName(definition);
		AddServiceCommand command = new AddServiceCommand(definition, newName, true);
		command.run();
		Service service = (Service) command.getWSDLElement();
		if (service.getEPorts().size() > 0) {
			Port port = (Port) service.getEPorts().get(0);
			
			// Set a default protocol
			
			
			// Set a default address
			String address = "http://www.example.org/";
			W11SetAddressCommand addressCommand = new W11SetAddressCommand(port, address);
			addressCommand.execute();			
		}
	}
}