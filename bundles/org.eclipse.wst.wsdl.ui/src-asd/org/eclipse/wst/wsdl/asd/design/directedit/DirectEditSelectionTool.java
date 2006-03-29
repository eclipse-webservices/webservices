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
package org.eclipse.wst.wsdl.asd.design.directedit;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Tool;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.wst.wsdl.asd.design.editparts.INamedEditPart;

/**
 * Extends the default GEF selection tools to send the
 * mouse events
 */
public class DirectEditSelectionTool extends SelectionTool {
	protected INamedEditPart partUnderCursor = null;
	
	public void mouseDown(MouseEvent me, EditPartViewer viewer) {
		EditPart focusEditPartOld = viewer.getFocusEditPart();
		setViewer(viewer);
		super.mouseDown(me,viewer);
		EditPart focusEditPart = viewer.getFocusEditPart();
		if(focusEditPart instanceof INamedEditPart) {
			Tool tool = focusEditPart.getViewer().getEditDomain().getActiveTool();
			if(!(tool instanceof DirectEditTool)) {
				tool = new DirectEditTool();
				viewer.getEditDomain().setActiveTool(tool);
			}
			if(focusEditPartOld != focusEditPart) {
				tool.mouseDown(me,viewer);
			}
		}
	}
}