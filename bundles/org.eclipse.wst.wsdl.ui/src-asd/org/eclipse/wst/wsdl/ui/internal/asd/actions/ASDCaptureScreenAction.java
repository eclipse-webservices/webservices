/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.ui.internal.asd.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.wst.xsd.ui.internal.adt.actions.CaptureScreenAction;


/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate
 */
public class ASDCaptureScreenAction  extends ActionDelegate implements IEditorActionDelegate {
	
	/**
	 * The constructor.
	 */
	public ASDCaptureScreenAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		CaptureScreenAction captureScreenAction = new CaptureScreenAction();
		captureScreenAction.run();
	}
	
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
    
	}

}
