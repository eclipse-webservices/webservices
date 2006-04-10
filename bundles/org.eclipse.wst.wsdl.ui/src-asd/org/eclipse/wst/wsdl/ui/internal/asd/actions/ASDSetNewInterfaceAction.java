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

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.ui.internal.asd.ASDEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IBinding;
import org.eclipse.wst.wsdl.ui.internal.edit.W11InterfaceReferenceEditManager;
import org.eclipse.wst.xsd.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.adt.edit.IComponentDialog;

public class ASDSetNewInterfaceAction extends BaseSelectionAction {
	public static String ID = "ASDSetNewInterfaceAction";
	protected IBinding binding;
	
	public ASDSetNewInterfaceAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText("Set New PortType...");
//		setImageDescriptor(WSDLEditorPlugin.getImageDescriptor("icons/service_obj.gif"));
	}
	
	public void setIBinding(IBinding binding) {
		this.binding = binding;
	}
	
	public void run() {		
		if (binding == null) {
			if (getSelectedObjects().size() > 0) {
				Object o = getSelectedObjects().get(0);
				if (o instanceof IBinding) {
					binding = (IBinding) o;
				}
			}
		}
		
		if (binding != null) {
			IEditorPart editor = ASDEditorPlugin.getActiveEditor();
			// TODO: rmah: We should not know about W11InterfaceReferenceEditManager here....  We should a better
			// way to retrieve the appropriate Reference Manager
			ComponentReferenceEditManager refManager = (ComponentReferenceEditManager) editor.getAdapter(W11InterfaceReferenceEditManager.class);
			IComponentDialog dialog = refManager.getNewDialog();
			if (dialog.createAndOpen() == Window.OK) {
				ComponentSpecification spec = dialog.getSelectedComponent();
				refManager.modifyComponentReference(binding, spec);
			}
		}
		
		binding = null;
	}
	
	protected boolean calculateEnabled() {
		return true;
	}
}
