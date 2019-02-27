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
package org.eclipse.wst.wsdl.ui.internal.asd.design.editpolicies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.tools.DragEditPartsTracker;

public class ASDGraphNodeDragTracker extends DragEditPartsTracker  {
	protected EditPart editPart; 
	
	public ASDGraphNodeDragTracker(EditPart editPart) {
		super(editPart);
		this.editPart = editPart;
	} 
	
	protected Command getCommand() { 
		Request request = getTargetRequest();
		return editPart.getCommand(request); 
	}
}