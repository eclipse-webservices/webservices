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
package org.eclipse.wst.wsdl.ui.internal.dialogs;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.ui.internal.search.dialogs.ComponentSpecification;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.asd.facade.IASDObject;
import org.eclipse.wst.xsd.adt.edit.IComponentDialog;

public class W11BrowseComponentDialog implements IComponentDialog {
//	protected WSDLComponentSelectionDialog dialog;
	
	public W11BrowseComponentDialog(IASDObject object, IFile iFile, Definition definition) {
//		Shell shell = Display.getCurrent().getActiveShell();
//		if (object instanceof IEndPoint) {
//			String dialogTitle = WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_BINDING");
//			
//			WSDLComponentSelectionProvider provider = new WSDLComponentSelectionProvider(iFile, definition, WSDLConstants.BINDING);
//			dialog = new WSDLComponentSelectionDialog(shell, dialogTitle, provider);
//			provider.setDialog(dialog);
//		}
//		else if (object instanceof IBinding) {
//
//			String dialogTitle = WSDLEditorPlugin.getWSDLString("_UI_TITLE_SPECIFY_PORTTYPE");
//			
//			WSDLComponentSelectionProvider provider = new WSDLComponentSelectionProvider(iFile, definition, WSDLConstants.PORT_TYPE);
//			dialog = new WSDLComponentSelectionDialog(shell, dialogTitle, provider);
//			provider.setDialog(dialog);
//		}
	}
	
	public void setInitialSelection(ComponentSpecification componentSpecification) {
		// TODO Auto-generated method stub

	}

	public ComponentSpecification getSelectedComponent() {
//		XMLComponentSpecification xmlSpec = dialog.getSelection();
//
//		String qualifier = "";
//		String name = (String) xmlSpec.getAttributeInfo("name");
//		
//		Workspace ws = new Workspace();
//		IFile iFile = (IFile) ws.newResource(new Path(xmlSpec.getFileLocation()), IResource.FILE);
//
//		ComponentSpecification spec = new ComponentSpecification(qualifier, name, iFile);
//		// TODO: rmah: We're pulling a fast one here....  setObject() should really take in an IASDObject....
//		// but we send in an XMLComponentSpecification because we already have helper classes which consumes it
//		spec.setObject(xmlSpec);
//		
//		return spec;
		
		return null;
	}

	public int createAndOpen() {
//	    dialog.setBlockOnOpen(true);
//	    dialog.create();
//		return dialog.open();
		
		return -1;
	}

}
