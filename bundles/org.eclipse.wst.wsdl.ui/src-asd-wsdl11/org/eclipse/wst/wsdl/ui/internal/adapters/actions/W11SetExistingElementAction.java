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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.util.ReferenceEditManagerHelper;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.adt.edit.IComponentDialog;

public class W11SetExistingElementAction extends BaseSelectionAction {
	public static String ID = "ASDSetExistingElementAction"; //$NON-NLS-1$
	protected WSDLBaseAdapter wsdlBaseAdapter;
	
	public W11SetExistingElementAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(Messages.getString("_UI_ACTION_EXISTING_ELEMENT")); //$NON-NLS-1$
//		setImageDescriptor(ASDEditorPlugin.getImageDescriptor("icons/message_obj.gif"));
	}
	
	public void run() {
		if (wsdlBaseAdapter == null) {
			if (getSelectedObjects().size() > 0) {
				Object o = getSelectedObjects().get(0);
				if (o instanceof IParameter && o instanceof WSDLBaseAdapter) {
					wsdlBaseAdapter = (WSDLBaseAdapter) o;
				}
			}
		}
		
        ComponentReferenceEditManager refManager = ReferenceEditManagerHelper.getXSDElementReferenceEditManager(wsdlBaseAdapter);
		if (wsdlBaseAdapter != null && refManager != null) {
			IComponentDialog dialog = refManager.getBrowseDialog();
			if (dialog.createAndOpen() == Window.OK) {
				ComponentSpecification spec = dialog.getSelectedComponent();
				refManager.modifyComponentReference(wsdlBaseAdapter.getTarget(), spec);
			}
		}
		
		wsdlBaseAdapter = null;
	}
}