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
import org.eclipse.wst.wsdl.ui.internal.adapters.basic.W11MessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;

public class ASDAddParameterAction extends BaseSelectionAction { 
    public static String ID = "ASDAddParameterAction";  //$NON-NLS-1$
    
    public ASDAddParameterAction(IWorkbenchPart part)   {
        super(part);
        setId(ID);
        setText("Add Parameter");   //$NON-NLS-1$
        setImageDescriptor(ASDEditorPlugin.getImageDescriptor("icons/input_obj.gif")); //$NON-NLS-1$
    }
    
    public void run() {
        if (getSelectedObjects().size() > 0) {
            Object o = getSelectedObjects().get(0);
            IMessageReference messageReference = null;
            
            if (o instanceof IMessageReference)
            {
              messageReference = (IMessageReference)o;
            }  
            else if (o instanceof IParameter) {
                messageReference = ((IMessageReference) ((IParameter) o).getOwner());
            }
            
            if (messageReference instanceof W11MessageReference) {
                W11MessageReference w11MessageReference = (W11MessageReference)messageReference;              
                Command command = w11MessageReference.getAddParamterCommand();
                CommandStack stack = (CommandStack) ASDEditorPlugin.getActiveEditor().getAdapter(CommandStack.class);
                stack.execute(command);
            }
        }  
    }
}
