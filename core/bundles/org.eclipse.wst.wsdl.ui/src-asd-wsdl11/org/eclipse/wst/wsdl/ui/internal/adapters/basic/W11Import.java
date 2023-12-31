/*******************************************************************************
 * Copyright (c) 2001, 2009 IBM Corporation and others.
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

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.adapters.actions.W11OpenImportAction;
import org.eclipse.wst.wsdl.ui.internal.adapters.commands.W11UpdateImportCommand;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IImport;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;

public class W11Import extends WSDLBaseAdapter implements IImport {

	public Import getImport() {
		return (Import) target;
	}
	
	public String getNamespace() {
		return getImport().getNamespaceURI();
	}
	
	public String getNamespacePrefix() {
		return getImport().getEnclosingDefinition().getPrefix(getNamespace());
	}
	
	public String getLocation() {
		return getImport().getLocationURI();
	}
	
	public IDescription getOwnerDescription() {
		return (IDescription) owner;
	}
	
	public String getName() {
		Import theImport = (Import) target;
		return theImport.getLocationURI();
	}
	
	public String[] getActions(Object object) {
		String[] actionIDs = new String[2];
		actionIDs[0] = W11OpenImportAction.ID;
		actionIDs[1] = ASDDeleteAction.ID;    
		return actionIDs;
	}
	
	public Image getImage() {
		return WSDLEditorPlugin.getInstance().getImage("icons/import_obj.gif"); //$NON-NLS-1$
	}
	
	public String getText() {
		return "import"; //$NON-NLS-1$
	}
	
	public ITreeElement[] getChildren() {
		return ITreeElement.EMPTY_LIST;
	}
	
	public boolean hasChildren() {
		return false;
	}
	
	public ITreeElement getParent() {
		return null;
	}
	
    public Command getUpdateCommand(String location, String namespaceURI, String prefix) {
      return new W11UpdateImportCommand((Import)target, location, namespaceURI, prefix);
    }
}
