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
package org.eclipse.wst.wsdl.asd.editor.actions;

import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.asd.editor.ASDEditorPlugin;
import org.eclipse.wst.wsdl.asd.editor.ASDMultiPageEditor;
import org.eclipse.wst.wsdl.asd.facade.IEndPoint;
import org.eclipse.wst.xsd.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.adt.edit.IComponentDialog;

public class ASDSetNewBindingAction extends BaseSelectionAction {
	public static String ID = "ASDSetNewBindingAction";
	protected IEndPoint endPoint;
	
	public ASDSetNewBindingAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText("Set New Binding...");
//		setImageDescriptor(WSDLEditorPlugin.getImageDescriptor("icons/service_obj.gif"));
	}
	
	public void setIEndPoint(IEndPoint endPoint) {
		this.endPoint = endPoint;
	}
	
	public void run() {		
		if (endPoint == null) {
			if (getSelectedObjects().size() > 0) {
				Object o = getSelectedObjects().get(0);
				if (o instanceof IEndPoint) {
					endPoint = (IEndPoint) o;
				}
			}
		}
		
		if (endPoint != null) {
			IEditorPart editor = ASDEditorPlugin.getActiveEditor();
			ComponentReferenceEditManager refManager = ((ASDMultiPageEditor) editor).getSetBindingHelper(endPoint);
			IComponentDialog dialog = refManager.getNewDialog();
			if (dialog.createAndOpen() == Window.OK) {
				ComponentSpecification spec = dialog.getSelectedComponent();
				refManager.modifyComponentReference(endPoint, spec);
			}
		}
		
		endPoint = null;
	}
	
	protected boolean calculateEnabled() {
		return true;
	}
}
