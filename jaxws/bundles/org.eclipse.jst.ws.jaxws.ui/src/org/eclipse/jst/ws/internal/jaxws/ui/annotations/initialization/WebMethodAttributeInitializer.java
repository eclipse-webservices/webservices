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

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.ACTION;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.OPERATION_NAME;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.initialization.AnnotationAttributeInitializer;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;

public class WebMethodAttributeInitializer extends AnnotationAttributeInitializer {
    
    @Override
    public List<MemberValuePair> getMemberValuePairs(IJavaElement javaElement, AST ast,
            Class<? extends Annotation> annotationClass) {

        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        if (javaElement.getElementType() == IJavaElement.METHOD) {
            IMethod method = (IMethod) javaElement;
            IType type = method.getCompilationUnit().findPrimaryType();
            
            MemberValuePair operationValuePair = AnnotationsCore.createStringMemberValuePair(ast, 
                    OPERATION_NAME, getOperationNameValue(type, method));

            MemberValuePair actionValuePair = AnnotationsCore.createStringMemberValuePair(ast, 
                    ACTION, getActionValue(type, method));

            memberValuePairs.add(operationValuePair);
            memberValuePairs.add(actionValuePair);
            
        }
        return memberValuePairs;
    }
    
    public List<ICompletionProposal> getCompletionProposalsForMemberValuePair(IJavaElement javaElement,
            MemberValuePair memberValuePair) {
        
        List<ICompletionProposal> completionProposals = new ArrayList<ICompletionProposal>();
        
        if (javaElement.getElementType() == IJavaElement.METHOD) {
            IMethod method = (IMethod) javaElement;
            IType type = method.getCompilationUnit().findPrimaryType();
            
            String memberValuePairName = memberValuePair.getName().getIdentifier();

            if (memberValuePairName.equals(OPERATION_NAME)) {
                completionProposals.add(createCompletionProposal(getOperationNameValue(type, method), 
                		memberValuePair.getValue()));
            }
            
            if (memberValuePairName.equals(ACTION)) {
                completionProposals.add(createCompletionProposal(getActionValue(type, method),
                		memberValuePair.getValue()));
            }

        }
        return completionProposals;
    }

    private String getOperationNameValue(IType type, IMethod method) {
        try {
            return method.getElementName() + JAXWSUtils.accountForOverloadedMethods(method);
        } catch (JavaModelException jme) {
        	JAXWSUIPlugin.log(jme.getStatus());
        }
        return ""; //$NON-NLS-1$
    }
    
    private String getActionValue(IType type, IMethod method) {
        try {
            String methodName = method.getElementName();
            return "urn:" + methodName.substring(0, 1).toUpperCase()  //$NON-NLS-1$
                + methodName.substring(1) + JAXWSUtils.accountForOverloadedMethods(method);
        } catch (JavaModelException jme) {
        	JAXWSUIPlugin.log(jme.getStatus());
        }
        return ""; //$NON-NLS-1$
    }

}
