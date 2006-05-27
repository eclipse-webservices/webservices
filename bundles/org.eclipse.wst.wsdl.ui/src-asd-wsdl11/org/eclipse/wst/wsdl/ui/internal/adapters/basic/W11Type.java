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

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDOpenSchemaAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ShowPropertiesViewAction;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IDescription;
import org.eclipse.wst.wsdl.ui.internal.asd.facade.IType;
import org.eclipse.wst.wsdl.ui.internal.asd.outline.ITreeElement;
import org.eclipse.xsd.XSDSchema;

public class W11Type extends WSDLBaseAdapter implements IType {
	public IDescription getOwnerDescription() {
		return (IDescription) owner;
	}

	public String getName() {
		XSDSchema schema= (XSDSchema) target;
		return schema.getTargetNamespace();
	}
	
	public String[] getActions(Object object) {
		String[] actionIDs = new String[3];
		actionIDs[0] = ASDOpenSchemaAction.ID;
		actionIDs[1] = ASDDeleteAction.ID;
    actionIDs[2] = ShowPropertiesViewAction.ID;
		
		return actionIDs;
	}
	
	public Image getImage() {
		return WSDLEditorPlugin.getInstance().getImage("icons/xsd_obj.gif"); //$NON-NLS-1$
	}
	
	public String getText() {
		return "schema";
	}
	public ITreeElement[] getChildren() {
		return new ITreeElement[0];
	}

	public boolean hasChildren() {
		return false;
	}

	public ITreeElement getParent() {
		return null;
	}
}