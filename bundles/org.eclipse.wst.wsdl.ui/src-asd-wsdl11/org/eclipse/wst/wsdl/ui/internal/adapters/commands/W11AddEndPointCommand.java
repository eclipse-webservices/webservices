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

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;
import org.eclipse.wst.wsdl.ui.internal.wizards.PortWizard;

public class W11AddEndPointCommand extends Command
{
    private Service service;
    
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
			selectNewElement((Notifier) ports.get(ports.size() - 1));	
		}
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
