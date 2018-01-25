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
package org.eclipse.jst.ws.internal.jaxws.ui.annotations.correction;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;

public class RemoveMemberValuePairCorrectionProposal extends AbstractJavaCorrectionPropsoal {
    
    private MemberValuePair memberValuePair;
    private boolean removeAllOtherMVPs;
    
    public RemoveMemberValuePairCorrectionProposal(IInvocationContext invocationContext,
            MemberValuePair memberValuePair, boolean removeAllOtherMVPs, String displayString, int relevance, 
            Image image) {
        super(invocationContext, displayString, relevance, image);
        this.memberValuePair = memberValuePair;
        this.removeAllOtherMVPs = removeAllOtherMVPs;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addEdits(TextFileChange textChange) throws CoreException {
        if (memberValuePair.getParent() instanceof NormalAnnotation) {
            NormalAnnotation annotation = (NormalAnnotation) memberValuePair.getParent();

            ASTRewrite astRewrite = ASTRewrite.create(annotation.getAST());
            if (removeAllOtherMVPs) {
                List<MemberValuePair> memberValuePairs = annotation.values();
                for (MemberValuePair otherMemberValuePair : memberValuePairs) {
                    if (!otherMemberValuePair.equals(memberValuePair)) {
                        astRewrite.remove(otherMemberValuePair, null);
                    }
                }
            } else {
                astRewrite.remove(memberValuePair, null);
            }
            textChange.addEdit(astRewrite.rewriteAST());   
        }
    }

}
