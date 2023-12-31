/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.adapters.basic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.actions.OpenInNewEditor;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddOperationCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDAddOperationAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public class W11Interface extends WSDLBaseAdapter implements IInterface {

	public List getOperations() {
		List adapterList = new ArrayList();
		populateAdapterList(((PortType) target).getEOperations(), adapterList);

		return adapterList;
	}
	
	public String getName() {
		return ((PortType) target).getQName().getLocalPart();
	}
	
	public String[] getActions(Object object) {
    List actionIDs = new ArrayList();
    actionIDs.add(ASDAddOperationAction.ID);
    actionIDs.add(ASDDeleteAction.ID);
    if (isReadOnly()) {
      actionIDs.add(OpenInNewEditor.ID);
    }
    return (String [])actionIDs.toArray(new String[0]);
	}
	
	public Command getAddOperationCommand() {
		return new W11AddOperationCommand((PortType) target);
	}
	
	public Image getImage() {
		return WSDLEditorPlugin.getInstance().getImage("icons/porttype_obj.gif"); //$NON-NLS-1$
	}
	
	public String getText() {
		return "portType"; //$NON-NLS-1$
	}
	
	public ITreeElement[] getChildren() {
		List operations = getOperations();
		ITreeElement[] treeElements = new ITreeElement[operations.size()];
		
		for (int index = 0; index < operations.size(); index++) {
			treeElements[index] = (ITreeElement) operations.get(index);
		}
		
		return treeElements;
	}
	
	public boolean hasChildren() {
		if (getChildren().length > 0) {
			return true;
		}
		
		return false;
	}
	
	public ITreeElement getParent() {
		return null;
	}
}
