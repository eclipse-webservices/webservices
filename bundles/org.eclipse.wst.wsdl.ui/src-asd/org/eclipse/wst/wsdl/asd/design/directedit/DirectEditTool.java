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

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.wst.wsdl.asd.design.editparts.INamedEditPart;

public class DirectEditTool extends AbstractTool {
	public DirectEditTool() {}
	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
	 */
	protected String getCommandName() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
	 */
	protected String getDebugName() {
		return null;
	}
	
	protected INamedEditPart getSelectedPart() {
		if(getCurrentViewer() == null)
			return null;
		EditPart ep = getCurrentViewer().getFocusEditPart();
		if(ep instanceof INamedEditPart && ep.isActive())
			return (INamedEditPart)ep;
		return null;
	}

	protected boolean handleFocusGained() {
		return false;
	}
	
	protected boolean handleFocusLost() {
		return false;
	}	
	
	protected boolean handleButtonDown(int button) {
		INamedEditPart selectedPart = getSelectedPart();
		
		if(selectedPart != null) {
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
	
	public void mouseDown(MouseEvent me, EditPartViewer viewer) {
		super.mouseDown(me,viewer);

		IFigure f = null;
		Point loc = translateLocation(new Point(me.x,me.y));
		if(f == null || !f.getBounds().contains(loc) || childContains(f,loc)) {
			EditDomain ed = getDomain();
			ed.loadDefaultTool();
			ed.getActiveTool().mouseDown(me,viewer);
		}
	}
	private boolean childContains(IFigure f,Point loc) {
		if(f == null)
			return false;
		List children = f.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			IFigure child = (IFigure) iter.next();
			if(child.getBounds().contains(loc) || childContains(child,loc))
				return true;
		}
		return false;
	}

	protected Point translateLocation(Point mouseLocation) {
		FigureCanvas canvas = (FigureCanvas)getCurrentViewer().getControl();
		Point viewLocation = canvas.getViewport().getViewLocation();
		return new Point(mouseLocation.x + viewLocation.x,mouseLocation.y + viewLocation.y);
	}
}