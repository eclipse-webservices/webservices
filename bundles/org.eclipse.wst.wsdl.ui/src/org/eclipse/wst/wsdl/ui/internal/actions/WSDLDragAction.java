/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.wst.wsdl.ui.internal.graph.editparts.WSDLTreeNodeEditPart;
import org.w3c.dom.Node;

public abstract class WSDLDragAction extends BaseNodeAction {
	protected WSDLTreeNodeEditPart movingChildEditPart;
	protected Point pointerLocation;
	
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
		return "";
	}
	
	/*
	 * Sub-classes should override this method.
	 */
	public Node getNode() {
		return null;
	}
	
	/*
	 * Sub-classes should override this method.
	 */
	public IFigure getFeedbackFigure() {
		return null;
	}
}
