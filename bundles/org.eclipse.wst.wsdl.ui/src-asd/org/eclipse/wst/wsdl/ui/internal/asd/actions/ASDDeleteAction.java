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

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IEndPoint;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IImport;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessage;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IOperation;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IService;

public class ASDDeleteAction extends BaseSelectionAction {
	public static String ID = "ASDDeleteAction"; 
	
	public ASDDeleteAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText("Delete");
		setImageDescriptor(ASDEditorPlugin.getImageDescriptor("icons/delete_obj.gif"));
	}
	
	public void run() {
		if (getSelectedObjects().size() > 0) {
			Object object = getSelectedObjects().get(0);
			Command command = null;
			
			if (object instanceof IService) {
				command = ((IService) object).getDeleteCommand();
			}
			else if (object instanceof IEndPoint) {
				command = ((IEndPoint) object).getDeleteCommand();
			}
			else if (object instanceof IBinding) {
				command = ((IBinding) object).getDeleteCommand();
			}
			else if (object instanceof IInterface) {
				command = ((IInterface) object).getDeleteCommand();
			}
			else if (object instanceof IOperation) {
				command = ((IOperation) object).getDeleteCommand();
			}
			else if (object instanceof IMessageReference) {
				command = ((IMessageReference) object).getDeleteCommand();
			}
			else if (object instanceof IParameter) {
				command = ((IParameter) object).getDeleteCommand();
			}
			else if (object instanceof IImport) {
				command = ((IImport) object).getDeleteCommand();
			}
			else if (object instanceof IMessage) {
				command = ((IMessage) object).getDeleteCommand();
			}
			
			if (command != null) {
			    CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
			    stack.execute(command);
			}
		}  
	}
	
	protected boolean calculateEnabled() {
		return true;
	}
}
