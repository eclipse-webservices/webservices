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
package org.eclipse.wst.wsdl.ui.internal.asd.design.directedit;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.wst.wsdl.ui.internal.asd.design.editparts.INamedEditPart;

/**
 * Extends the default GEF selection tools to send the
 * mouse events
 */
public class DirectEditSelectionTool extends SelectionTool {
	
	protected INamedEditPart getSelectedPart() {
		if(getCurrentViewer() == null)
			return null;
		EditPart ep = getCurrentViewer().getFocusEditPart();
		if(ep instanceof INamedEditPart && ep.isActive())
			return (INamedEditPart)ep;
		return null;
	}
	
	protected boolean handleButtonDown(int button) {
		super.handleButtonDown(button);
		INamedEditPart selectedPart = getSelectedPart();
		
		if(selectedPart != null && button == 1) {
			Input i = getCurrentInput();
			Point l = translateLocation(i.getMouseLocation());
			
			IFigure f = ((AbstractGraphicalEditPart) selectedPart).getFigure();
			Rectangle bounds = f.getBounds();
			if(bounds.contains(l.x,l.y)) {
				selectedPart.performDirectEdit(translateLocation(new Point(l.x, l.y)));
				return true;
			}
		}
		return true;
	}
	
	protected Point translateLocation(Point mouseLocation) {
		FigureCanvas canvas = (FigureCanvas)getCurrentViewer().getControl();
		Point viewLocation = canvas.getViewport().getViewLocation();
		return new Point(mouseLocation.x + viewLocation.x,mouseLocation.y + viewLocation.y);
	}

	protected boolean handleKeyUp(KeyEvent e) {
		boolean result = super.handleKeyUp(e);
		
		EditPartViewer currentViewer = getCurrentViewer();
		if((e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR) && currentViewer != null) {
			EditPart editPart = currentViewer.getFocusEditPart();
			if (editPart == null)
			{
			  return result;
			}
			
			SelectionRequest request = new SelectionRequest();
			request.setLocation(getLocation());
			request.setType(RequestConstants.REQ_OPEN);
			editPart.performRequest(request);
			return true;
		}
		return result;
	}
}