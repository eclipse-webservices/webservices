/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.actions;

import java.util.Iterator;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IASDObject;

public class ASDDeleteAction extends BaseSelectionAction {
	public static String ID = "ASDDeleteAction";  //$NON-NLS-1$
	
	public ASDDeleteAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText(Messages._UI_ACTION_DELETE); //$NON-NLS-1$
		setImageDescriptor(ASDEditorPlugin.getImageDescriptor("icons/delete_obj.gif")); //$NON-NLS-1$
	}
	
	public void run() {
		Iterator it = getSelectedObjects().iterator();
		while (it.hasNext()) {
			Object object = it.next();
			Command command = null;
			
			if (object instanceof IASDObject) {
				command = ((IASDObject) object).getDeleteCommand();
			}

			if (command != null) {
			    CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
			    stack.execute(command);

			    if (object instanceof WSDLBaseAdapter) {
			    	Object target = ((WSDLBaseAdapter) object).getTarget();
			    	if (target instanceof WSDLElement) {
			    		performSelection(((WSDLElement) target).getEnclosingDefinition());
			    	}
			    }
			}
		}  
	}
}
