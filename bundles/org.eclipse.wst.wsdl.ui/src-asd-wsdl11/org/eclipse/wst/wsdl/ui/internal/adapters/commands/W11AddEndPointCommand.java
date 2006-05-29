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

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.wizards.PortWizard;

public class W11AddEndPointCommand extends Command implements IASDAddCommand
{
    private Service service;
    private Port port;
    
    public W11AddEndPointCommand(Service service) {
        super(Messages.getString("_UI_ACTION_ADD"));
        this.service = service;
    }
    
    public void execute() {
    	PortWizard wizard = new PortWizard(service);
		WizardDialog wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
		wizardDialog.create();

		int result = wizardDialog.open();
		if (result == Window.OK && service.getEPorts().size() > 0) {
			List ports = service.getEPorts();
			port = (Port) ports.get(ports.size() - 1);
		}
    }

	public Object getNewlyAddedComponent() {
		return port;
	}
}
