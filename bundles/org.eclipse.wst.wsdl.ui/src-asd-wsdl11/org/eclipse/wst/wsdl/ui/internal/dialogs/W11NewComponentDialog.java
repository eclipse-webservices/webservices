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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddBindingCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddInterfaceCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;
import org.eclipse.wst.wsdl.ui.internal.search.IWSDLSearchConstants;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;
import org.eclipse.wst.xsd.ui.internal.adt.edit.IComponentDialog;
import org.eclipse.wst.xsd.ui.internal.dialogs.NewComponentDialog;

public class W11NewComponentDialog implements IComponentDialog {
	protected NewComponentDialog dialog;
	protected QualifiedName qualifiedName;
	protected IFile iFile;
	protected W11Description description;
	protected IASDObject newObject;
	
	public W11NewComponentDialog(QualifiedName qualifiedName, IFile iFile, W11Description description) {
		this.qualifiedName = qualifiedName;
		this.iFile = iFile;
		this.description = description;
		
		Shell shell = Display.getCurrent().getActiveShell();
		if (qualifiedName == IWSDLSearchConstants.BINDING_META_NAME) {
			String dialogTitle = Messages.getString("_UI_LABEL_NEW_BINDING"); //$NON-NLS-1$
			String baseName = NameUtil.buildUniqueBindingName(getDefinition(), "NewBinding"); //$NON-NLS-1$
			List usedNames = NameUtil.getUsedBindingNames(getDefinition());
			dialog = new NewComponentDialog(shell, dialogTitle, baseName, usedNames);
		}
		else if (qualifiedName == IWSDLSearchConstants.PORT_TYPE_META_NAME) {
			String dialogTitle = Messages.getString("_UI_LABEL_NEW_PORTTYPE"); //$NON-NLS-1$
			String baseName = NameUtil.buildUniquePortTypeName(getDefinition(), "NewPortType"); //$NON-NLS-1$
			List usedNames = NameUtil.getUsedPortTypeNames(getDefinition());
			dialog = new NewComponentDialog(shell, dialogTitle, baseName, usedNames);
		}
		else if (qualifiedName == IWSDLSearchConstants.MESSAGE_META_NAME) {
			String dialogTitle = Messages.getString("_UI_LABEL_NEW_MESSAGE"); //$NON-NLS-1$
			String baseName = NameUtil.buildUniqueMessageName(getDefinition(), "NewMessage"); //$NON-NLS-1$
			List usedNames = NameUtil.getUsedMessageNames(getDefinition());
			dialog = new NewComponentDialog(shell, dialogTitle, baseName, usedNames);
		}
	}
	
	private Definition getDefinition() {
		return (Definition) description.getTarget();
	}
	
	public void setInitialSelection(ComponentSpecification componentSpecification) {
	}

	public ComponentSpecification getSelectedComponent() {
		String qualifier = getDefinition().getPrefix(getDefinition().getTargetNamespace());
		String name = dialog.getName();
		ComponentSpecification spec = new ComponentSpecification(qualifier, name, iFile);
		spec.setObject(newObject);
		return spec;
	}

	public int createAndOpen() {
		int rValue = dialog.createAndOpen();
		
		if (rValue == Window.OK) {
			// Create the new Object
			if (qualifiedName == IWSDLSearchConstants.BINDING_META_NAME) {
				W11AddBindingCommand command = (W11AddBindingCommand) description.getAddBindingCommand();
				command.setNewBindingName(dialog.getName());
                execute(command);
			    
			    Object newWSDLObject = command.getNewBinding();
			    newObject = (IASDObject) WSDLAdapterFactoryHelper.getInstance().adapt((Notifier) newWSDLObject);
			}
			else if (qualifiedName == IWSDLSearchConstants.PORT_TYPE_META_NAME) {
				W11AddInterfaceCommand command = (W11AddInterfaceCommand) description.getAddInterfaceCommand();
				command.setNewPortTypeName(dialog.getName());
                execute(command);
			    
			    Object newWSDLObject = command.getNewPortType();
			    newObject = (IASDObject) WSDLAdapterFactoryHelper.getInstance().adapt((Notifier) newWSDLObject);
			}
			else if (qualifiedName == IWSDLSearchConstants.MESSAGE_META_NAME) {
				W11AddMessageCommand command = (W11AddMessageCommand) description.getAddMessageCommand();
				command.setNewMessageName(dialog.getName());
				execute(command);
			    
			    Object newWSDLObject = command.getNewMessage();
			    newObject = (IASDObject) WSDLAdapterFactoryHelper.getInstance().adapt((Notifier) newWSDLObject);
			}
		}
		
		return rValue;
	}
    
    private void execute(Command command) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page.getActiveEditor().getAdapter(CommandStack.class) != null) {
            CommandStack stack = (CommandStack) page.getActiveEditor().getAdapter(CommandStack.class);
            stack.execute(command);
        }
        else {
            command.execute();
        }
    }
}
