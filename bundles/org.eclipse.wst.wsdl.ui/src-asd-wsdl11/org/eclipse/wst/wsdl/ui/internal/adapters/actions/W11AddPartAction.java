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
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.ui.internal.InternalWSDLMultiPageEditor;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11Description;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11MessageReference;
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11ParameterForPart;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessage;
import org.eclipse.wst.wsdl.ui.internal.commands.AddMessageCommand;
import org.eclipse.wst.wsdl.ui.internal.util.NameUtil;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;

public class W11AddPartAction extends BaseSelectionAction {
	public static String ID = "ASDAddPartAction"; //$NON-NLS-1$
	
	public W11AddPartAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText(Messages.getString("_UI_ACTION_ADD_PART")); //$NON-NLS-1$
		setImageDescriptor(WSDLEditorPlugin.getImageDescriptor("icons/part_obj.gif")); //$NON-NLS-1$
	}
	
	public void run() {
		if (getSelectedObjects().size() > 0) {
			Object o = getSelectedObjects().get(0);
			MessageReference messageRef = null;
			IMessage iMessage = null;
			Message message = null;
			
			if (o instanceof W11MessageReference) {
				messageRef = (MessageReference) ((W11MessageReference) o).getTarget();
				message = messageRef.getEMessage();
			}
			else if (o instanceof W11ParameterForPart) {
				W11ParameterForPart param = (W11ParameterForPart) o;
				if (param.getOwner() instanceof IMessage) {
					iMessage = (IMessage) param.getOwner();
				}
				else if (param.getOwner() instanceof W11MessageReference) {
					messageRef = (MessageReference) ((W11MessageReference) param.getOwner()).getTarget();
					message = messageRef.getEMessage();
				}
				
				if (message == null) {
					Part part = (Part) param.getTarget();
					message = (Message) part.eContainer();
				}
			}
			else if (o instanceof IMessage) {
				iMessage = (IMessage) o;
			}
			
			if (message == null && iMessage == null && messageRef != null) {
				InternalWSDLMultiPageEditor editor = (InternalWSDLMultiPageEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
				Definition definition = (Definition) ((W11Description) editor.getModel()).getTarget();
				String messageName = NameUtil.buildUniqueMessageName(definition, "NewMessage"); //$NON-NLS-1$
				AddMessageCommand command = new AddMessageCommand(definition, messageName, false);
				command.run();
				message = (Message) command.getWSDLElement();
				messageRef.setEMessage(message);
			}
			
			if (message != null) {
				iMessage = (IMessage) WSDLAdapterFactoryHelper.getInstance().adapt(message);
			}

			if (iMessage != null) {
				Command command = iMessage.getAddPartCommand();
			    CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
			    stack.execute(command);
			}
		}  
	}
}