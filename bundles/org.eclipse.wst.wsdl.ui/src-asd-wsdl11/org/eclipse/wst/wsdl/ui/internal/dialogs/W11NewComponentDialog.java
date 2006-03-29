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
package org.eclipse.wst.wsdl.ui.internal.dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.asd.editor.ASDEditorPlugin;
import org.eclipse.wst.wsdl.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.asd.facade.IBinding;
import org.eclipse.wst.wsdl.asd.facade.IDescription;
import org.eclipse.wst.wsdl.asd.facade.IEndPoint;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddBindingCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddInterfaceCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;
import org.eclipse.wst.xsd.adt.edit.IComponentDialog;
import org.eclipse.wst.xsd.editor.internal.dialogs.NewComponentDialog;

public class W11NewComponentDialog implements IComponentDialog {
	protected NewComponentDialog dialog;
	protected IASDObject object;
	protected IFile iFile;
	protected Definition definition;
	protected IASDObject newObject;
	
	public W11NewComponentDialog(IASDObject object, IFile iFile, Definition definition) {
		this.object = object;
		this.iFile = iFile;
		this.definition = definition;
		
		Shell shell = Display.getCurrent().getActiveShell();
		if (object instanceof IEndPoint) {
			String dialogTitle = WSDLEditorPlugin.getWSDLString("_UI_LABEL_NEW_BINDING");
			String baseName = NameUtil.buildUniqueBindingName(definition, "NewBinding");
			dialog = new NewComponentDialog(shell, dialogTitle, baseName);
		}
		else if (object instanceof IBinding) {
			String dialogTitle = WSDLEditorPlugin.getWSDLString("_UI_LABEL_NEW_PORTTYPE");
			String baseName = NameUtil.buildUniquePortTypeName(definition, "NewPortType");
			dialog = new NewComponentDialog(shell, dialogTitle, baseName);
		}
	}
	
	public void setInitialSelection(ComponentSpecification componentSpecification) {
	}

	public ComponentSpecification getSelectedComponent() {
		String qualifier = definition.getPrefix(definition.getTargetNamespace());
		String name = dialog.getName();
		ComponentSpecification spec = new ComponentSpecification(qualifier, name, iFile);
		spec.setObject(newObject);
		return spec;
	}

	public int createAndOpen() {
		int rValue = dialog.createAndOpen();
		
		if (rValue == Window.OK) {
			// Create the new Object
			if (object instanceof IEndPoint) {
				IEndPoint endPoint = (IEndPoint) object;
				IDescription description = endPoint.getOwnerService().getOwnerDescription();
				W11AddBindingCommand command = (W11AddBindingCommand) description.getAddBindingCommand();
				command.setNewBindingName(dialog.getName());
			    CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
			    stack.execute(command);
			    
			    Object newWSDLObject = command.getNewBinding();
			    newObject = (IASDObject) WSDLAdapterFactoryHelper.getInstance().adapt((Notifier) newWSDLObject);
			}
			else if (object instanceof IBinding) {
				IBinding iBinding = (IBinding) object;
				IDescription description = iBinding.getOwnerDescription();
				W11AddInterfaceCommand command = (W11AddInterfaceCommand) description.getAddInterfaceCommand();
				command.setNewPortTypeName(dialog.getName());
			    CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
			    stack.execute(command);
			    
			    Object newWSDLObject = command.getNewPortType();
			    newObject = (IASDObject) WSDLAdapterFactoryHelper.getInstance().adapt((Notifier) newWSDLObject);
			}
		}
		
		return rValue;
	}
}
