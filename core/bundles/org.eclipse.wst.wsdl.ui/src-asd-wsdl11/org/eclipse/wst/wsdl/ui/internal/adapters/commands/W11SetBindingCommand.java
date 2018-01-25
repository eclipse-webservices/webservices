/*******************************************************************************
 * Copyright (c) 2001, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.adapters.commands;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.internal.generator.ContentGenerator;
import org.eclipse.wst.wsdl.internal.generator.extension.ContentGeneratorExtensionFactoryRegistry;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Binding;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11EndPoint;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;

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
		String bindingProtocol = getBindingProtocol(binding);
		if (bindingProtocol != null) {
	    W11EndPoint portProtocol = getPortProtocol(port);
	    if ((portProtocol == null) || !(bindingProtocol.equals(portProtocol.getProtocol()))) {
        String portAddress = null;
	      if (portProtocol != null) {
	        portAddress = portProtocol.getAddress();
	      }
	      if (portAddress == null) {
	        portAddress = "http://www.example.org"; //$NON-NLS-1$
	      }
	      
        ContentGeneratorExtensionFactoryRegistry factoryRegistry = ContentGeneratorExtensionFactoryRegistry.getInstance();
        ContentGenerator contentGenerator = factoryRegistry.getGeneratorClassFromName(bindingProtocol);
        if (contentGenerator != null) {
          contentGenerator.setAddressLocation(portAddress);
          contentGenerator.generatePortContent(port);
        }
	    }
		}
	}
	
	private static String getBindingProtocol(Binding binding) {
    Notifier notifier = (Notifier)binding;
    Adapter adapter = WSDLAdapterFactoryHelper.getInstance().adapt(notifier);
    if (adapter instanceof W11Binding) {
      return ((W11Binding)adapter).getProtocol();
    }
		
		return null;
	}
	
	private static W11EndPoint getPortProtocol(Port port) {
    Notifier notifier = (Notifier)port;
    Adapter adapter = WSDLAdapterFactoryHelper.getInstance().adapt(notifier);
    if (adapter instanceof W11EndPoint) {
      return (W11EndPoint)adapter;
    }
    
    return null;
	}
}
