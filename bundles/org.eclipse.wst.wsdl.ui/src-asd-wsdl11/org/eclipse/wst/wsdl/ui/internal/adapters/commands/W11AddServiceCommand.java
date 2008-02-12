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

import java.util.List;

import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.internal.generator.BindingGenerator;
import org.eclipse.wst.wsdl.internal.generator.ContentGenerator;
import org.eclipse.wst.wsdl.internal.generator.PortGenerator;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension.ContentGeneratorUIExtension;
import org.eclipse.wst.wsdl.ui.internal.commands.AddServiceCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;

public class W11AddServiceCommand extends W11TopLevelElementCommand implements IASDAddCommand {
	private Service service;
	
	public W11AddServiceCommand(Definition definition) {
	  super(Messages._UI_ACTION_ADD_SERVICE, definition);
	}
	
	public void execute() {
		try {
			beginRecording(definition.getElement());
			super.execute();
			String newName = NameUtil.buildUniqueServiceName(definition);
			AddServiceCommand command = new AddServiceCommand(definition, newName, false);
			command.run();
			service = (Service) command.getWSDLElement();
			
			PortGenerator portGenerator = new PortGenerator(service);
			
			// Get the first available content generator
			List protocols = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry().getBindingExtensionNames();
			if (protocols.size() >= 1) {
			  String protocol = (String)protocols.get(0);
			  ContentGeneratorUIExtension ext = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry().getExtensionForName(protocol);
			  if (ext != null) {
			    ContentGenerator contentGenerator = BindingGenerator.getContentGenerator(ext.getNamespace());
			    portGenerator.setContentGenerator(contentGenerator);
			  }
			}
			
			portGenerator.setName(NameUtil.buildUniquePortName(service, "NewPort")); //$NON-NLS-1$
			portGenerator.generatePort();
			
			formatChild(service.getElement());
		}
		finally {
			endRecording(definition.getElement());
		}
	}
	
	public Object getNewlyAddedComponent() {
		return service;
	}
}
