/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.adapters.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.commands.AddPortTypeCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;

public class W11AddInterfaceCommand extends W11TopLevelElementCommand implements IASDAddCommand {
	private String newName;
	private PortType newPortType;
	
	public W11AddInterfaceCommand(Definition definition) {
    super(Messages._UI_ACTION_ADD_PORTTYPE, definition);
	}
	
	public void setNewPortTypeName(String newName) {
		this.newName = newName;
	}
	
	public PortType getNewPortType() {
		return newPortType;
	}
	
	public void execute() {
		try {
			beginRecording(definition.getElement());
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
			formatChild(newPortType.getElement());
		}
		finally {
			endRecording(definition.getElement());
		}
	}
	
	public Object getNewlyAddedComponent() {
		return newPortType;
	}
}
