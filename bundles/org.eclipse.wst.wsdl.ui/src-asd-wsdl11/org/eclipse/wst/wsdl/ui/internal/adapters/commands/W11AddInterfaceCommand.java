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
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.commands.AddPortTypeCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;

public class W11AddInterfaceCommand extends W11TopLevelElementCommand {
	private String newName;
	private PortType newPortType;
	
	public W11AddInterfaceCommand(Definition definition) {
    super(Messages.getString("_UI_ACTION_ADD_PORTTYPE"), definition);
	}
	
	public void setNewPortTypeName(String newName) {
		this.newName = newName;
	}
	
	public PortType getNewPortType() {
		return newPortType;
	}
	
	public void execute() {
    super.execute();
		if (newName == null || newName.equals("")) { //$NON-NLS-1$
			newName = NameUtil.buildUniquePortTypeName(definition, "NewPortType"); //$NON-NLS-1$
		}
		// Add the Port Type
		AddPortTypeCommand command = new AddPortTypeCommand(definition, newName, false);
		command.run();
		
		newPortType = (PortType) command.getWSDLElement();
		
		// Add the Operation
		IInterface iInterface = (IInterface) WSDLAdapterFactoryHelper.getInstance().adapt(newPortType);
		Command addOperationCommand = iInterface.getAddOperationCommand();
		addOperationCommand.execute();
		
		selectNewElement(newPortType);
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
