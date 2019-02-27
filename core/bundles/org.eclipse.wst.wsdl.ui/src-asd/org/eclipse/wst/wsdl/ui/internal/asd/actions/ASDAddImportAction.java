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
package org.eclipse.wst.wsdl.ui.internal.asd.actions;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.dialogs.ImportSelectionDialog;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IImport;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ICategoryAdapter;

public class ASDAddImportAction extends BaseSelectionAction {
	public static String ID = "ASDAddImportAction";  //$NON-NLS-1$
	
	public ASDAddImportAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText(Messages._UI_ACTION_ADD_IMPORT);
		setImageDescriptor(ASDEditorPlugin.getImageDescriptor("icons/import_obj.gif")); //$NON-NLS-1$
	}
	
	public void run() {
		if (getSelectedObjects().size() > 0) {
			Object o = getSelectedObjects().get(0);
			
			if (o instanceof ICategoryAdapter) {
				o = ((ICategoryAdapter) o).getOwnerDescription();
			}
			
			if (o instanceof IDescription) {
				Command command = ((IDescription) o).getAddImportCommand();
			    CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
			    stack.execute(command);
			    
			    IImport theImport = null;
			    if (command instanceof IASDAddCommand) {
			      theImport = (IImport)((IASDAddCommand)command).getNewlyAddedComponent();
			    }
			    
			    boolean selectLocation = WSDLEditorPlugin.getInstance().getAutoOpenImportLocationDialogSetting();
			    if (theImport != null && selectLocation) {
		            ImportSelectionDialog dialog = new ImportSelectionDialog(WSDLEditorPlugin.getShell(), null, true);
		            int rc = dialog.open();
		            if (IDialogConstants.OK_ID == rc) {
		              String locationURI = dialog.getImportLocation();
		              String namespaceURI = dialog.getImportNamespace();
		              Command updateImportCommand = theImport.getUpdateCommand(locationURI, namespaceURI, ""); //$NON-NLS-1$
		              stack.execute(updateImportCommand);
		            }
			    }
			    
			    if (theImport != null) {
			    	performSelection(theImport);
			    }
			}
		}  
	}
}