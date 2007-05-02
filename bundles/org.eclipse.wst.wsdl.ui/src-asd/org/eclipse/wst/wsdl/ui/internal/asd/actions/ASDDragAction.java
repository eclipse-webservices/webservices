/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.ui.IWorkbenchPart;

public abstract class ASDDragAction extends BaseSelectionAction {
	protected GraphicalEditPart movingChildEditPart;
	protected Point pointerLocation;
	
	public ASDDragAction(IWorkbenchPart part) {
		super(part);
	}
	
	/*
	 * Sub-classes should override this method.
	 */
	public boolean canExecute() {
		return false;
	}
	
	public void execute() {
		run();
	}
	
	/*
	 * Sub-classes should override this method.
	 */
	public String getUndoDescription() {
		return ""; //$NON-NLS-1$
	}
	
	/*
	 * Sub-classes should override this method.
	 */
	public IFigure getFeedbackFigure() {
		return null;
	}
}
