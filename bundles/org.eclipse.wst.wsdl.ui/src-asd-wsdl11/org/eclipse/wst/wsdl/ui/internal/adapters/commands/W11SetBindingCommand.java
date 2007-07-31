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

import java.util.Iterator;
import java.util.List;

import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.binding.http.HTTPAddress;
import org.eclipse.wst.wsdl.binding.http.HTTPBinding;
import org.eclipse.wst.wsdl.binding.http.HTTPFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.ui.internal.Messages;

public class W11SetBindingCommand extends W11TopLevelElementCommand {
	private Port port;
	private Binding binding;
	
	public W11SetBindingCommand(Port port, Binding binding) {
        super(Messages._UI_ACTION_SET_BINDING, port.getEnclosingDefinition());
		this.port = port;
		this.binding = binding;
	}
	
	public void execute() {
		try {
			beginRecording(port.getElement());
			port.setEBinding(binding);

			updatePortProtocol(port, binding);
		}
		finally {
			endRecording(port.getElement());
		}
	}
	
	public static void updatePortProtocol(Port port, Binding binding) {
		String portAddress = null;
		Object bindingProtocol = getBindingProtocol(binding);
		Object portProtocol = getPortProtocol(port);
		
		if (portProtocol instanceof SOAPAddress) {
			portAddress = ((SOAPAddress) portProtocol).getLocationURI();
		}
		else if (portProtocol instanceof HTTPAddress) {
			portAddress = ((HTTPAddress) portProtocol).getLocationURI();
		}

		if (portAddress == null) {
			portAddress = "http://www.example.org";
		}
		
		if (bindingProtocol instanceof SOAPBinding && !(portProtocol instanceof SOAPAddress)) {
			SOAPAddress soap = SOAPFactory.eINSTANCE.createSOAPAddress();
			soap.setLocationURI(portAddress);
			setNewProtocol(port, soap);
		}
		else if (bindingProtocol instanceof HTTPBinding && !(portProtocol instanceof HTTPAddress)) {
			HTTPAddress http = HTTPFactory.eINSTANCE.createHTTPAddress();
			http.setLocationURI(portAddress);
			setNewProtocol(port, http);
		}
	}
	
	private static Object getBindingProtocol(Binding binding) {
		Iterator it = binding.getEExtensibilityElements().iterator();
		while (it.hasNext()) {
			Object item = it.next();
			if (item instanceof SOAPBinding || item instanceof HTTPBinding) {
				return item;
			}
		}
		
		return null;
	}
	
	private static Object getPortProtocol(Port port) {
		
		Iterator it = port.getEExtensibilityElements().iterator();
        while (it.hasNext()) {
        	Object item = it.next();
        	if (item instanceof SOAPAddress) {
        		return item;
        	}
        	else if (item instanceof HTTPAddress) {
        		return item;
        	}
        }	

		return null;
	}
	
	private static void setNewProtocol(Port port, ExtensibilityElement element) {
		List existingElements = port.getEExtensibilityElements();
		for (int index = 0; index < existingElements.size(); index++) {
			Object item = existingElements.get(index);
			if (item instanceof SOAPAddress || item instanceof HTTPAddress) {
				existingElements.remove(index);
				break;
			}
		}

		port.addExtensibilityElement(element);
	}
}
