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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.commands.AddXSDSchemaCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class W11AddSchemaCommand extends Command {
	private Definition definition;
	
	public W11AddSchemaCommand(Definition definition) {
        super(Messages.getString("_UI_ACTION_ADD_SCHEMA"));
		this.definition = definition;
	}
	
	public void execute() {
		String tns = definition.getTargetNamespace();
		List existingNamespaces = new ArrayList();
		Iterator eeIt = definition.getETypes().getEExtensibilityElements().iterator();
		while (eeIt.hasNext()) {
			Object item = eeIt.next();
			if (item instanceof XSDSchemaExtensibilityElement) {
				String ns = ((XSDSchemaExtensibilityElement) item).getSchema().getTargetNamespace();
				existingNamespaces.add(ns);
			}
		}
		tns = NameUtil.getUniqueNameHelper(tns, existingNamespaces);
		
		AddXSDSchemaCommand command = new AddXSDSchemaCommand(definition, tns);
		command.run();
	}
}