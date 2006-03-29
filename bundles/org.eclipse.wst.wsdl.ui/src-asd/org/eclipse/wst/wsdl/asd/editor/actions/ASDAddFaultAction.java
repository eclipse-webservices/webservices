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
import org.eclipse.wst.wsdl.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.asd.facade.IOperation;
import org.eclipse.wst.wsdl.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11MessageReference;

public class ASDAddFaultAction extends BaseSelectionAction {
	public static String ID = "ASDAddFaultActionn"; 
	
	public ASDAddFaultAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText("Add Fault");  
		setImageDescriptor(ASDEditorPlugin.getImageDescriptor("icons/fault_obj.gif"));
	}
	
	public void run() {
		if (getSelectedObjects().size() > 0) {
			Object o = getSelectedObjects().get(0);
			IOperation iOperation = null;
			Object possibleFault = null;
			
			if (o instanceof IOperation) {
				iOperation = (IOperation) o;
			}
			else if (o instanceof IMessageReference) {
				iOperation = ((IMessageReference) o).getOwnerOperation();
				possibleFault = ((W11MessageReference) o).getTarget();
			}
			else if (o instanceof IParameter) {
				iOperation = ((IMessageReference) ((IParameter) o).getOwner()).getOwnerOperation();
				possibleFault = ((W11MessageReference) ((IParameter) o).getOwner()).getTarget();
			}
			
			if (iOperation != null) {
				Command command = iOperation.getAddFaultCommand(possibleFault);
			    CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
			    stack.execute(command);
			}
		}  
	}
	
	protected boolean calculateEnabled() {
		return true;
	}
}