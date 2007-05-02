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
package org.eclipse.wst.wsdl.ui.internal.search.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.window.Window;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IWorkingSetSelectionDialog;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.core.search.scope.WorkingSetSearchScope;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.xsd.ui.internal.editor.XSDEditorPlugin;
import org.eclipse.wst.xsd.ui.internal.search.XSDSearchQuery;

public class WSDLFindReferencesInWorkingSetAction extends WSDLFindReferencesAction{
	
	public WSDLFindReferencesInWorkingSetAction(IEditorPart editor) {
		super(editor);
	}
	
	public void run(){
		IWorkingSet[] workingSets = queryWorkingSets();
		if ( workingSets == null || workingSets.length == 0)
			// The user chooses nothing, no point to continue.
			return;
		String pattern = ""; //$NON-NLS-1$

		WSDLBaseAdapter component = getWSDLNamedComponent();
		IFile file = getCurrentFile();
		QualifiedName[] names = determineMetaAndQualifiedName(component);
		if ( file != null && component != null){
			// Create a scope from the selected working sets
			WorkingSetSearchScope scope = new WorkingSetSearchScope();
			for (int i = 0; i < workingSets.length; i++){
				IAdaptable[] elements = workingSets[i].getElements();
				scope.addAWorkingSetToScope(elements);
			}

			String scopeDescription = "Working Set";    
			XSDSearchQuery searchQuery = 
				new XSDSearchQuery(pattern, file, names[1], names[0], XSDSearchQuery.LIMIT_TO_REFERENCES, scope, scopeDescription);    
			NewSearchUI.activateSearchResultView();
			NewSearchUI.runQueryInBackground(searchQuery);
		}
	}

	/**
	 * Calls a dialog asking the user to choose the working Sets he wants
	 * to do the search on
	 * @return
	 */
	public static IWorkingSet[] queryWorkingSets(){
		Shell shell= XSDEditorPlugin.getShell();
		if (shell == null)
			return null;
		IWorkingSetSelectionDialog dialog =
			PlatformUI.getWorkbench().getWorkingSetManager().createWorkingSetSelectionDialog(shell, true);
		if (dialog.open() == Window.OK) {
			IWorkingSet[] workingSets= dialog.getSelection();
			if (workingSets.length > 0)
				return workingSets;
		}
		return null;
	}
}
