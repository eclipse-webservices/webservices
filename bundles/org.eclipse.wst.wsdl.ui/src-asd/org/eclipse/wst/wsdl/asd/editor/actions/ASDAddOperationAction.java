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
package org.eclipse.wst.wsdl.asd.editor.actions;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.wsdl.asd.editor.ASDEditorPlugin;
import org.eclipse.wst.wsdl.asd.facade.IInterface;
import org.eclipse.wst.wsdl.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.asd.facade.IOperation;
import org.eclipse.wst.wsdl.asd.facade.IParameter;

public class ASDAddOperationAction extends BaseSelectionAction {	
	public static String ID = "ASDAddOperationAction"; 
	
	public ASDAddOperationAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText("Add Operation");
		setImageDescriptor(ASDEditorPlugin.getImageDescriptor("icons/operation_obj.gif"));
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
			}
		}  
	}
	
	protected boolean calculateEnabled() {
		return true;
	}
}