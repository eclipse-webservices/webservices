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

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.asd.editor.ASDEditorPlugin;
import org.eclipse.wst.wsdl.asd.editor.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.edit.W11MessageReferenceEditManager;
import org.eclipse.wst.xsd.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.adt.edit.IComponentDialog;

public class W11SetExistingMessageAction extends BaseSelectionAction {
	public static String ID = "ASDSetMessageInterfaceAction";
	protected IMessageReference messageReference;

	
	public W11SetExistingMessageAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText("Set Existing Message...");
//		setImageDescriptor(ASDEditorPlugin.getImageDescriptor("icons/message_obj.gif"));
	}
	
	public void run() {
		if (messageReference == null) {
			if (getSelectedObjects().size() > 0) {
				Object o = getSelectedObjects().get(0);
				if (o instanceof IMessageReference) {
					messageReference = (IMessageReference) o;
				}
			}
		}
		
		if (messageReference != null) {
			IEditorPart editor = ASDEditorPlugin.getActiveEditor();
			ComponentReferenceEditManager refManager = (ComponentReferenceEditManager) editor.getAdapter(W11MessageReferenceEditManager.class);
			IComponentDialog dialog = refManager.getBrowseDialog();
			if (dialog.createAndOpen() == Window.OK) {
				ComponentSpecification spec = dialog.getSelectedComponent();
				refManager.modifyComponentReference(messageReference, spec);
			}
		}
		
		messageReference = null;
	}
	
	protected boolean calculateEnabled() {
		return true;
	}
}