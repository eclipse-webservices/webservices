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
import org.eclipse.wst.wsdl.Port;

public class W11SetBindingCommand extends Command {
	private Port port;
	private Binding binding;
	
	public W11SetBindingCommand(Port port, Binding binding) {
		this.port = port;
		this.binding = binding;
	}
	
	public void execute() {
		port.setEBinding(binding);
	}
}
