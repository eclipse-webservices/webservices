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
package org.eclipse.wst.wsdl.ui.internal.adapters.basic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDAddOperationAction;
import org.eclipse.wst.wsdl.asd.editor.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.asd.editor.outline.ITreeElement;
import org.eclipse.wst.wsdl.asd.facade.IInterface;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddOperationCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11DeleteCommand;

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
		String[] actionIDs = new String[2];
		actionIDs[0] = ASDAddOperationAction.ID;
		actionIDs[1] = ASDDeleteAction.ID;
		
		return actionIDs;
	}
	
	public Command getAddOperationCommand() {
		return new W11AddOperationCommand((PortType) target);
	}
	
	public Command getDeleteCommand() {
		return new W11DeleteCommand(this);
	}
	
	public Image getImage() {
		return WSDLEditorPlugin.getInstance().getImage("icons/porttype_obj.gif");
	}
	
	public String getText() {
		return "portType";
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
