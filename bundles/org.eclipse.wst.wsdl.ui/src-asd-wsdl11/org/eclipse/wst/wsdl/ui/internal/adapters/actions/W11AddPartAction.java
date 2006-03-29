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
package org.eclipse.wst.wsdl.ui.internal.adapters.actions;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.asd.editor.ASDEditorPlugin;
import org.eclipse.wst.wsdl.asd.editor.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.asd.facade.IMessage;
import org.eclipse.wst.wsdl.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11MessageReference;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;

public class W11AddPartAction extends BaseSelectionAction {
	public static String ID = "ASDAddPartAction";
	
	public W11AddPartAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText("Add Part");
		setImageDescriptor(WSDLEditorPlugin.getImageDescriptor("icons/part_obj.gif"));
	}
	
	public void run() {
		if (getSelectedObjects().size() > 0) {
			Object o = getSelectedObjects().get(0);
			IMessage message = null;
			
			if (o instanceof W11MessageReference) {
				MessageReference messageRef = (MessageReference) ((W11MessageReference) o).getTarget();
				message = (IMessage) WSDLAdapterFactoryHelper.getInstance().adapt(messageRef.getEMessage());
			}
			else if (o instanceof IParameter) {
				IParameter param = (IParameter) o;
				if (param.getOwner() instanceof IMessage) {
					message = (IMessage) param.getOwner();
				}
				else if (param.getOwner() instanceof W11MessageReference) {
					MessageReference messageRef = (MessageReference) ((W11MessageReference) param.getOwner()).getTarget();
					message = (IMessage) WSDLAdapterFactoryHelper.getInstance().adapt(messageRef.getEMessage());
				}
			}
			else if (o instanceof IMessage) {
				message = (IMessage) o;
			}
			
			if (message != null) {
				Command command = message.getAddPartCommand();
			    CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
			    stack.execute(command);
			}
		}  
	}
	
	protected boolean calculateEnabled() {
		return true;
	}
}