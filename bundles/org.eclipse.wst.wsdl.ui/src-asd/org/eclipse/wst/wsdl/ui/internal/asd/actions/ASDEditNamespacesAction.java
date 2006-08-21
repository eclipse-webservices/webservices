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
package org.eclipse.wst.wsdl.ui.internal.asd.actions;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11EditNamespacesCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ICategoryAdapter;
import org.eclipse.wst.wsdl.ui.internal.dialogs.EditNamespacesDialog;

public class ASDEditNamespacesAction extends BaseSelectionAction {
	public static String ID = "ASDEditNamespacesAction";  //$NON-NLS-1$
	
	private IDescription description;
	
	public ASDEditNamespacesAction(IWorkbenchPart part, IDescription description) {
		super(part);
		setId(ID);
		String text = Messages._UI_EDIT_NAMESPACES; //$NON-NLS-1$
		setText(text);
		
		this.description = description;
	}
	
	public void run() {
		Object o = description;
		if (getSelectedObjects().size() > 0) {
			if (o instanceof ICategoryAdapter) {
				o = ((ICategoryAdapter) o).getOwnerDescription();
			}
		}
		
		if (o instanceof IDescription) {
			// TODO: The code below is not generic.  We need to revisit this to ensure it is
			// generic.  IDescription needs a getNamespacesInfo() and getEditNamespacesCommand()...
			IDescription description = (IDescription) o;
			W11Description w11Description = (W11Description) o;
			Definition definition = (Definition) w11Description.getTarget();
			
			IPath path = new Path(definition.getDocumentBaseURI());
			List namespaceInfoList = w11Description.getNamespacesInfo();
			String tns = description.getTargetNamespace();
			EditNamespacesDialog dialog = new EditNamespacesDialog(WSDLEditorPlugin.getShell(), path, Messages._UI_EDIT_NAMESPACES_DIALOG_TITLE, tns, namespaceInfoList); //$NON-NLS-1$
			int rc = dialog.createAndOpen();
			if (rc == IDialogConstants.OK_ID) {
				List newInfoList = dialog.getNamespaceInfoList();
				W11EditNamespacesCommand command = (W11EditNamespacesCommand) w11Description.getEditNamespacesCommand();
				command.setNamespacesInfo(newInfoList);
				command.setTargetNamespace(dialog.getTargetNamespace());
				CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
			    stack.execute(command);
		    }
		}
	}
}
