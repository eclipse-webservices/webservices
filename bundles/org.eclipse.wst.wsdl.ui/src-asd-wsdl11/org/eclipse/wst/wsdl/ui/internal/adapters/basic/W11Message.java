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
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11AddPartAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11AddPartCommand;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11DeleteCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IMessage;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;


public class W11Message extends WSDLBaseAdapter implements IMessage {

	public List getParts() {
		List adapterList = new ArrayList();
		populateAdapterList(((Message) target).getEParts(), adapterList);
		return adapterList;
	}
	
	public IDescription getOwnerDescription() {
		return (IDescription) owner;
	}

	public String getName() {
		return ((Message) target).getQName().getLocalPart();
	}
	
	public String[] getActions(Object object) {
		String[] actionIDs = new String[2];
		actionIDs[0] = W11AddPartAction.ID;
		actionIDs[1] = ASDDeleteAction.ID;
		
		return actionIDs;
	}
	
	public Command getAddPartCommand() {
		return new W11AddPartCommand((Message) target);
	}
	
	public Command getDeleteCommand() {
		return new W11DeleteCommand(this);
	}

	public Command getSetNameCommand(String newName) {

		return null;
	}
	
	public Image getImage() {
		return WSDLEditorPlugin.getInstance().getImage("icons/message_obj.gif"); //$NON-NLS-1$
	}
	
	public String getText() {
		return "message";
	}
	
	public ITreeElement[] getChildren() {
		List parts = getParts();
		ITreeElement[] treeElements = new ITreeElement[parts.size()];
		
		for (int index = 0; index < parts.size(); index++) {
			treeElements[index] = (ITreeElement) parts.get(index);
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