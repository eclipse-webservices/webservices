/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.ui.annotations.initialization;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.internal.ui.text.java.JavaTypeCompletionProposal;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

@SuppressWarnings("restriction")
public class BindingTypeCompletionProposal extends JavaTypeCompletionProposal {

    private final String replacementString;
    private final String fullyQualifiedTypeName;
    
    public BindingTypeCompletionProposal(String replacementString, ICompilationUnit compilationUnit,
            int replacementOffset, int replacementLength, Image image, StyledString displayString,
            int relevance, String fullyQualifiedTypeName) {
        super(replacementString, compilationUnit, replacementOffset, replacementLength, image, displayString, 
        		relevance, fullyQualifiedTypeName);
        this.fullyQualifiedTypeName = fullyQualifiedTypeName;
        this.replacementString = replacementString;
    }

    @Override
    protected boolean updateReplacementString(IDocument document, char trigger, int offset, 
            ImportRewrite importRewrite) throws CoreException, BadLocationException {
    	
        if (importRewrite != null && fullyQualifiedTypeName != null && replacementString != null) {        
        	if (fullyQualifiedTypeName.indexOf('.') != -1) {
                IType[] types= importRewrite.getCompilationUnit().getTypes();
                if (types.length > 0 && types[0].getSourceRange().getOffset() <= offset) {
                    boolean importAdded = !importRewrite.addImport(fullyQualifiedTypeName).equals(fullyQualifiedTypeName);
                    if (!importAdded) {
                    	setReplacementString(fullyQualifiedTypeName
								+ replacementString.substring(replacementString.lastIndexOf('.')));
                    }
                    return importAdded;
                }
            }
        }
        return false;
    }
}