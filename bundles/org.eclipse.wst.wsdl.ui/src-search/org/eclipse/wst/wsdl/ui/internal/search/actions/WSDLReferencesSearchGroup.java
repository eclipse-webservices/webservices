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

import org.eclipse.ui.IEditorPart;
import org.eclipse.wst.xsd.ui.internal.search.SearchMessages;
import org.eclipse.wst.xsd.ui.internal.search.actions.ReferencesSearchGroup;

public class WSDLReferencesSearchGroup extends ReferencesSearchGroup  {
    /**
     * Note: This constructor is for internal use only. Clients should not call this constructor.
     * @param editor
     */
    public WSDLReferencesSearchGroup(IEditorPart editor) {
    	super(editor);
    }
    
    protected void initialize() {
//      fGroupId= ITextEditorActionConstants.GROUP_FIND;
    	
    	fFindReferencesAction= new WSDLFindReferencesAction(fEditor);
        fFindReferencesAction.setText(SearchMessages.Search_FindDeclarationAction_label);
        fFindReferencesAction.setActionDefinitionId("SEARCH_REFERENCES_IN_WORKSPACE");
        //fEditor.setAction("SearchReferencesInWorkspace", fFindReferencesAction); //$NON-NLS-1$

        fFindReferencesInProjectAction= new WSDLFindReferencesInProjectAction(fEditor);
        fFindReferencesInProjectAction.setText(SearchMessages.Search_FindDeclarationsInProjectAction_label);        
        fFindReferencesInProjectAction.setActionDefinitionId("SEARCH_REFERENCES_IN_PROJECT");
        //fEditor.setAction("SearchReferencesInProject", fFindReferencesInProjectAction); //$NON-NLS-1$
    
        fFindReferencesInWorkingSetAction= new WSDLFindReferencesInWorkingSetAction(fEditor);
        fFindReferencesInWorkingSetAction.setText(SearchMessages.Search_FindDeclarationsInWorkingSetAction_label);         
        fFindReferencesInWorkingSetAction.setActionDefinitionId(".SEARCH_REFERENCES_IN_WORKING_SET");
        //fEditor.setAction("SearchReferencesInWorkingSet", fFindReferencesInWorkingSetAction); //$NON-NLS-1$
    }
}
