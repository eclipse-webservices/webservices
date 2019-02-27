/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
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

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.commands.AddImportCommand;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;

public class W11AddImportCommand extends W11TopLevelElementCommand implements IASDAddCommand {
	private Object component;
	
	public W11AddImportCommand(Definition definition) {
		super(Messages._UI_ACTION_ADD_IMPORT, definition);
	}
	
	public void execute() {
		try {
			beginRecording(definition.getElement());

			super.execute();
			String namespace = ""; //$NON-NLS-1$
			String location = ""; //$NON-NLS-1$
			AddImportCommand command = new AddImportCommand(definition, namespace, location);
			command.run();
			formatChild(command.getWSDLElement().getElement());
			component = WSDLAdapterFactoryHelper.getInstance().adapt(command.getWSDLElement());
		}
		finally {
			endRecording(definition.getElement());
		}
	}

	public Object getNewlyAddedComponent() {
		return component;
	}
}