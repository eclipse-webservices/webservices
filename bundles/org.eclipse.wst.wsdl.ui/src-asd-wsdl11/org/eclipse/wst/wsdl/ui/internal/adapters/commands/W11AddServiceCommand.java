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

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.commands.AddServiceCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;

public class W11AddServiceCommand extends W11TopLevelElementCommand {
	
	public W11AddServiceCommand(Definition definition) {
	  super(Messages.getString("_UI_ACTION_ADD_SERVICE"), definition);
	}
	
	public void execute() {
    super.execute();
		String newName = NameUtil.buildUniqueServiceName(definition);
		AddServiceCommand command = new AddServiceCommand(definition, newName, true);
		command.run();
		Service service = (Service) command.getWSDLElement();
		if (service.getEPorts().size() > 0) {
			Port port = (Port) service.getEPorts().get(0);
			
			// Set a default protocol
			
			
			// Set a default address
			String address = "http://www.example.org/"; //$NON-NLS-1$
			W11SetAddressCommand addressCommand = new W11SetAddressCommand(port, address);
			addressCommand.execute();			
		}
		
		selectNewElement(service);
	}
	
    // TODO: We should probably be selecting the new element at the "action level"....  However, our actions
    // are currently very generic, so we have no way of getting to the newly created element.  The action
    // only sees these commands as generic Command objects.
    private void selectNewElement(Notifier element) {
    	try {
	    	Object adapted = WSDLAdapterFactoryHelper.getInstance().adapt(element);
	        IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	        if (editor != null && editor.getAdapter(ISelectionProvider.class) != null) {
	        	ISelectionProvider provider = (ISelectionProvider) editor.getAdapter(ISelectionProvider.class);
	        	if (provider != null) {
	        		provider.setSelection(new StructuredSelection(adapted));
	        	}
	        }
    	}
    	catch (Exception e) {}
    }
}