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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.WSDLElement;
import org.eclipse.wst.wsdl.ui.internal.Messages;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.BaseSelectionAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IParameter;
import org.eclipse.wst.wsdl.ui.internal.edit.WSDLXSDTypeReferenceEditManager;
import org.eclipse.wst.wsdl.ui.internal.util.ReferenceEditManagerHelper;
import org.eclipse.wst.wsdl.ui.internal.util.WSDLAdapterFactoryHelper;
import org.eclipse.wst.xsd.ui.internal.adt.edit.ComponentReferenceEditManager;
import org.eclipse.wst.xsd.ui.internal.adt.edit.IComponentDialog;
import org.eclipse.wst.xsd.ui.internal.editor.XSDTypeReferenceEditManager;

public class W11SetNewTypeAction extends BaseSelectionAction {
	public static String ID = "ASDSetNewTypeAction"; //$NON-NLS-1$
	protected WSDLBaseAdapter wsdlBaseAdapter;
	
	public W11SetNewTypeAction(IWorkbenchPart part)	{
		super(part);
		setId(ID);
		setText(Messages._UI_ACTION_NEW_TYPE); //$NON-NLS-1$
//		setImageDescriptor(WSDLEditorPlugin.getImageDescriptor("icons/service_obj.gif"));
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
		
        ComponentReferenceEditManager refManager = ReferenceEditManagerHelper.getXSDTypeReferenceEditManager(wsdlBaseAdapter);
		if (wsdlBaseAdapter != null && refManager != null) {
			IComponentDialog dialog = refManager.getNewDialog();
			if (dialog.createAndOpen() == Window.OK) {
				ComponentSpecification spec = dialog.getSelectedComponent();
				refManager.modifyComponentReference(wsdlBaseAdapter, spec);
			}
		}
		
		wsdlBaseAdapter = null;
	}
    
    protected ComponentReferenceEditManager getComponentReferenceEditManager() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page.getActiveEditor().getAdapter(XSDTypeReferenceEditManager.class) != null) {
            return (ComponentReferenceEditManager) page.getActiveEditor().getAdapter(XSDTypeReferenceEditManager.class);
        }
        else {
            IEditorInput input = page.getActiveEditor().getEditorInput();
            if (input instanceof IFileEditorInput) {
                WSDLElement element = (WSDLElement) wsdlBaseAdapter.getTarget();
                IDescription description = (IDescription) WSDLAdapterFactoryHelper.getInstance().adapt(element.getEnclosingDefinition());
                return new WSDLXSDTypeReferenceEditManager(((IFileEditorInput) input).getFile(), null, description);
            }
        }

        return null;
    }
}