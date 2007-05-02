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

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.wsdl.ui.internal.asd.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IType;
import org.eclipse.wst.wsdl.ui.internal.asd.util.IOpenExternalEditorHelper;

public class ASDOpenSchemaAction extends BaseSelectionAction {
	public static String ID = "ASDOpenSchemaAction";  //$NON-NLS-1$
	
	public ASDOpenSchemaAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText(Messages._UI_ACTION_OPEN_SCHEMA); //$NON-NLS-1$
//		setImageDescriptor(ASDEditorPlugin.getImageDescriptor("icons/binding_obj.gif")); //$NON-NLS-1$
	}
	
	public void run() {
		if (getSelectedObjects().size() > 0) {
			Object o = getSelectedObjects().get(0);

			if (o instanceof IType) {
				IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
				Object helper = editor.getAdapter(IOpenExternalEditorHelper.class);
				if (helper instanceof IOpenExternalEditorHelper) {
					((IOpenExternalEditorHelper) helper).setModel(o);
					((IOpenExternalEditorHelper) helper).openExternalEditor();
				}
			}
		}  
	}
}
