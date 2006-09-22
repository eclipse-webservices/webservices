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
package org.eclipse.wst.wsdl.ui.internal.search.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.common.core.search.pattern.QualifiedName;
import org.eclipse.wst.common.core.search.scope.ProjectSearchScope;
import org.eclipse.wst.wsdl.ui.internal.adapters.WSDLBaseAdapter;
import org.eclipse.wst.xsd.ui.internal.search.XSDSearchQuery;

public class WSDLFindReferencesInProjectAction extends WSDLFindReferencesAction{
	public WSDLFindReferencesInProjectAction(IEditorPart editor)
	{
		super(editor);
	}

	public void run()
	{
		String pattern = "";
		WSDLBaseAdapter component = getWSDLNamedComponent();
		IFile file = getCurrentFile();
		QualifiedName[] names = determineMetaAndQualifiedName(component);
		if (file != null && component != null && names != null)
		{
			IPath fullPath = file.getFullPath();
			ProjectSearchScope scope = new ProjectSearchScope(fullPath);
			String scopeDescription = "Project";
			XSDSearchQuery searchQuery = new XSDSearchQuery(pattern, file, names[1], 
					names[0], XSDSearchQuery.LIMIT_TO_REFERENCES, scope, scopeDescription);
			NewSearchUI.activateSearchResultView();
			NewSearchUI.runQueryInBackground(searchQuery);
		}
	}
}
