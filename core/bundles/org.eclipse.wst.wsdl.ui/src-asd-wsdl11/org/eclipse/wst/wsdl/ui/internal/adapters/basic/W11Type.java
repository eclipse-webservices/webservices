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

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.ui.internal.WSDLEditorPlugin;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDDeleteAction;
import org.eclipse.wst.wsdl.ui.internal.asd.actions.ASDOpenSchemaAction;
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
		String[] actionIDs = new String[2];
		actionIDs[0] = ASDOpenSchemaAction.ID;
		actionIDs[1] = ASDDeleteAction.ID;	
		return actionIDs;
	}
	
	public Image getImage() {
		return WSDLEditorPlugin.getInstance().getImage("icons/xsd_obj.gif"); //$NON-NLS-1$
	}
	
	public String getText() {
		return "schema"; //$NON-NLS-1$
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
