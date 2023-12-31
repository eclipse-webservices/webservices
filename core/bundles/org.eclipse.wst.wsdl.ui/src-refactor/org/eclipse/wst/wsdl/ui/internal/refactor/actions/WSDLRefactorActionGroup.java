/*******************************************************************************
 * Copyright (c) 2001, 2010 IBM Corporation and others.
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
package org.eclipse.wst.wsdl.ui.internal.refactor.actions;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ui.internal.refactor.handlers.RenameHandler;
import org.eclipse.wst.xsd.ui.internal.refactor.actions.RenameAction;
import org.eclipse.wst.xsd.ui.internal.refactor.wizard.RefactorActionGroup;

/** 
 * @deprecated This class is replaced by {@link RenameHandler}. 
 */
public class WSDLRefactorActionGroup extends RefactorActionGroup {


	private static final String RENAME_ELEMENT = "org.eclipse.wst.wsdl.ui.refactor.rename.element"; //$NON-NLS-1$

	
	public WSDLRefactorActionGroup(ISelection selection,
			Definition definition) {
		
		super(selection); 
	  	if( definition != null){
			fEditorActions = new ArrayList();
			RenameComponentAction action = new RenameComponentAction(selection, definition);
			fRenameAction = new RenameAction(selection);
			fRenameAction.setRenameComponentAction(action);
			fRenameAction.setActionDefinitionId(RENAME_ELEMENT);
			fEditorActions.add(fRenameAction);
			initAction(fRenameAction, selection);
		}
	}

	public void dispose() {
		//disposeAction(fRenameAction, fSelectionProvider);
		super.dispose();
	}
	
}
