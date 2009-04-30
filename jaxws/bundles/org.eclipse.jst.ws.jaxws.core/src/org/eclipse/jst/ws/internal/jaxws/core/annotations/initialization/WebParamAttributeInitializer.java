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
package org.eclipse.jst.ws.internal.jaxws.core.annotations.initialization;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.initialization.AnnotationAttributeInitializer;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;

/**
 * 
 * @author sclarke
 *
 */
public class WebParamAttributeInitializer extends AnnotationAttributeInitializer {
    private static final String NAME = "name";
    
    public WebParamAttributeInitializer() {        
    }
    
    @Override
    public List<MemberValuePair> getMemberValuePairs(ASTNode astNode, AST ast,
            Class<? extends Annotation> annotationClass) {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
    	
    	if (astNode instanceof SingleVariableDeclaration) {
    		MemberValuePair nameValuePair = AnnotationsCore.createStringMemberValuePair(ast, NAME, 
                getName((SingleVariableDeclaration)astNode));
    		memberValuePairs.add(nameValuePair);
    	}
        return memberValuePairs;
    }
    
    public List<ICompletionProposal> getCompletionProposalsForMemberValuePair(ASTNode astNode,
            MemberValuePair memberValuePair) {
    	
        List<ICompletionProposal> completionProposals = new ArrayList<ICompletionProposal>();

    	if (astNode instanceof SingleVariableDeclaration) {
    		String memberValuePairName = memberValuePair.getName().getIdentifier();

    		if (memberValuePairName.equals(NAME)) {
    			completionProposals.add(AnnotationUtils.createCompletionProposal(
                    getName((SingleVariableDeclaration)astNode), memberValuePair.getValue()));
    		}
    	}
    	return completionProposals;
    }
    
    private String getName(SingleVariableDeclaration parameter) {
        return parameter.getName().getIdentifier();
    }
}
