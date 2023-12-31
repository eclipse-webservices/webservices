/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.adapters.actions;

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessageReference;
import org.eclipse.wst.wsdl.ui.internal.util.ReferenceEditManagerHelper;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.adt.edit.IComponentDialog;

public class W11SetNewMessageAction extends BaseSelectionAction {
	public static String ID = "ASDSetNewMessageAction"; //$NON-NLS-1$
	protected IMessageReference messageReference;
	
	public W11SetNewMessageAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText(Messages._UI_ACTION_NEW_MESSAGE); //$NON-NLS-1$
//		setImageDescriptor(WSDLEditorPlugin.getImageDescriptor("icons/service_obj.gif"));
	}
	
	public void setIMessageReference(IMessageReference messageReference) {
		this.messageReference = messageReference;
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
		
        ComponentReferenceEditManager refManager = ReferenceEditManagerHelper.getMessageReferenceEditManager(messageReference);
        if (messageReference != null && refManager != null) {
			IComponentDialog dialog = refManager.getNewDialog();
			if (dialog.createAndOpen() == Window.OK) {
				ComponentSpecification spec = dialog.getSelectedComponent();
				refManager.modifyComponentReference(messageReference, spec);
			}
		}
		
		messageReference = null;
	}
}