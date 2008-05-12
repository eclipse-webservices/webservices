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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.internal.generator.BindingGenerator;
import org.eclipse.wst.wsdl.internal.generator.ContentGenerator;
import org.eclipse.wst.wsdl.internal.generator.PortGenerator;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.IASDAddCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension.ContentGeneratorUIExtension;
import org.eclipse.wst.wsdl.ui.internal.asd.contentgenerator.ui.extension.ContentGeneratorUIExtensionRegistry;
import org.eclipse.wst.wsdl.ui.internal.commands.AddServiceCommand;
import org.eclipse.wst.wsdl.ui.internal.util.CreateWSDLElementHelper;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.ServicePolicyHelper;

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
			
			// set the default content generator
			ContentGenerator contentGenerator = null;
			IProject project = getProject(definition);
			String protocol = ServicePolicyHelper.getDefaultBinding(project);
			if (protocol != null) {
				ContentGeneratorUIExtensionRegistry registry = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry();
				ContentGeneratorUIExtension ext = registry.getExtensionForNamespace(protocol);
				if (ext != null) {
					contentGenerator = BindingGenerator.getContentGenerator(ext.getNamespace());
				  }
			}
			// unable to determine default content generator, try again
			if (contentGenerator == null) {
				// Get the first available content generator
				List protocols = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry().getBindingExtensionNames();
				if (protocols.size() >= 1) {
					protocol = (String)protocols.get(0);
					ContentGeneratorUIExtension ext = WSDLEditorPlugin.getInstance().getContentGeneratorUIExtensionRegistry().getExtensionForName(protocol);
					if (ext != null) {
						contentGenerator = BindingGenerator.getContentGenerator(ext.getNamespace());
				  }
				}
			}
			portGenerator.setContentGenerator(contentGenerator);
			portGenerator.setName(NameUtil.buildUniquePortName(service, "NewPort")); //$NON-NLS-1$
		    // go ahead and add required namespaces first before generating port
			CreateWSDLElementHelper.addRequiredNamespaces(contentGenerator, definition);
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

	private IProject getProject(Definition definition) {
		IProject project = null;

		String location = definition.getLocation();
		URL url = null;
		try {
			url = new URL(location);
		}
		catch (MalformedURLException e)	{
			e.printStackTrace();
		}
		if (url != null) {
			URL fileURL = null;
			try {
				fileURL = FileLocator.toFileURL(url);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			if (fileURL != null) {
				IPath path = new Path(fileURL.getPath());
				IFile files[] = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(path);
				IFile file = null;
				if (files.length > 0) {
					// just get the first file
					file = files[0];
				}
				project = file.getProject();
			}
		}
		return project;
	}
}
