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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.OperationEditPart;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;

public class ASDAddOperationAction extends BaseSelectionAction {	
	public static String ID = "ASDAddOperationAction";  //$NON-NLS-1$
	
	public ASDAddOperationAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText(Messages.getString("_UI_ACTION_ADD_OPERATION")); //$NON-NLS-1$
		setImageDescriptor(ASDEditorPlugin.getImageDescriptor("icons/operation_obj.gif")); //$NON-NLS-1$
	}
	
	public void run() {
		if (getSelectedObjects().size() > 0) {
			Object o = getSelectedObjects().get(0);
			IInterface theInterface = null;
			
			if (o instanceof IInterface) {
				theInterface = (IInterface) o;
			}
			else if (o instanceof IOperation) {
				theInterface = ((IOperation) o).getOwnerInterface();
			}
			else if (o instanceof IMessageReference) {
				theInterface = ((IMessageReference) o).getOwnerOperation().getOwnerInterface();
			}
			else if (o instanceof IParameter) {
				theInterface = ((IMessageReference) ((IParameter) o).getOwner()).getOwnerOperation().getOwnerInterface();
			}
			
			if (theInterface != null) {
				Command command = theInterface.getAddOperationCommand();
			    CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
			    stack.execute(command);
			    
			    if (command instanceof IASDAddCommand) {
			    	Object element = ((IASDAddCommand) command).getNewlyAddedComponent();
			    	selectAndDirectEdit(element);
			    }
			}
		}  
	}
	
	protected void doDirectEdit(EditPart ep) {
		if (ep instanceof OperationEditPart) {
			((OperationEditPart) ep).performDirectEdit(null);
		}
	}
}