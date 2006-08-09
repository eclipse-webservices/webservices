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
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddBindingCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class W11AddBindingCommand extends W11TopLevelElementCommand implements IASDAddCommand {
	private String newName;
	private Binding newBinding;
	
	public W11AddBindingCommand(Definition definition) {
    super(Messages.getString("_UI_ACTION_ADD_BINDING"), definition);
	}
	
	public void setNewBindingName(String newName) {
		this.newName = newName;
	}
	
	public Binding getNewBinding() {
		return newBinding;
	}
	
	public void execute() {
		try {
			beginRecording(definition.getElement());
			super.execute();
			if (newName == null || newName.equals("")) { //$NON-NLS-1$
				newName = NameUtil.buildUniqueBindingName(definition, "NewBinding"); //$NON-NLS-1$
			}
			
			AddBindingCommand command = new AddBindingCommand(definition, newName);
			command.run();
			
			newBinding = (Binding) command.getWSDLElement();
			formatChild(newBinding.getElement());
		}
		finally {
			endRecording(definition.getElement());
		}
	}
	
	public Object getNewlyAddedComponent() {
		return newBinding;
	}
}
