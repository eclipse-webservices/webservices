/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.ui.annotations.contentassist;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.CompletionContext;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jst.ws.annotations.core.AnnotationsManager;
import org.eclipse.jst.ws.annotations.core.initialization.IAnnotationAttributeInitializer;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;

public class AnnotationCompletionProposalComputer implements IJavaCompletionProposalComputer {

    public AnnotationCompletionProposalComputer() {
        super();
    }

    @SuppressWarnings("unchecked")
    public List computeCompletionProposals(ContentAssistInvocationContext context, IProgressMonitor monitor) {
        if (context instanceof JavaContentAssistInvocationContext) {
            return computeCompletionProposals((JavaContentAssistInvocationContext) context);
        }
        return Collections.emptyList();
    }

    private List<ICompletionProposal> computeCompletionProposals(JavaContentAssistInvocationContext context) {
        CompletionContext completionContext = context.getCoreContext();
        int tokenStart = completionContext.getOffset();

        ICompilationUnit source = context.getCompilationUnit();
        try {
            IJavaElement javaElement = source.getElementAt(tokenStart);
            if (javaElement != null) {
                switch(javaElement.getElementType()) {
                case IJavaElement.PACKAGE_DECLARATION:
                case IJavaElement.TYPE:
                case IJavaElement.FIELD:
                    return getCompletionProposalsForJavaElement(AnnotationUtils.getAnnotations(
                            javaElement), javaElement, tokenStart);
                case IJavaElement.METHOD:
                    ILocalVariable localVariable = AnnotationUtils.getLocalVariable((IMethod) javaElement, tokenStart);
                    if (localVariable != null) {
                        return getCompletionProposalsForJavaElement(AnnotationUtils.getAnnotations(
                                localVariable), localVariable, tokenStart);
                    }  else {
                        return getCompletionProposalsForJavaElement(AnnotationUtils.getAnnotations(
                                javaElement), javaElement, tokenStart);
                    }
                }
            }
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
        }
        return Collections.emptyList();
    }

    private List<ICompletionProposal> getCompletionProposalsForJavaElement(List<Annotation> annotations,
            IJavaElement javaElement, int offset) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof NormalAnnotation) {
                NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;
                MemberValuePair memberValuePair = getMemberValuePairForPosition(normalAnnotation, offset);
                if (memberValuePair != null) {
                    return getCompletionProposalsForJavaElement(normalAnnotation, memberValuePair,
                            javaElement);
                }
            }

            if (annotation instanceof SingleMemberAnnotation) {
                SingleMemberAnnotation singleMemberAnnotation = (SingleMemberAnnotation) annotation;
                if (isWithinSingleMemberAnnotationValue(singleMemberAnnotation, offset)) {
                    return getCompletionProposalsForJavaElement(singleMemberAnnotation, javaElement);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<ICompletionProposal> getCompletionProposalsForJavaElement(NormalAnnotation annotation,
            MemberValuePair memberValuePair, IJavaElement javaElement) {
        IAnnotationAttributeInitializer annotationAttributeInitializer = AnnotationsManager
        .getAnnotationAttributeInitializerForName(annotation.getTypeName());
        if (annotationAttributeInitializer != null) {
            return annotationAttributeInitializer.getCompletionProposalsForMemberValuePair(javaElement,
                    memberValuePair);
        }
        return Collections.emptyList();
    }

    private List<ICompletionProposal> getCompletionProposalsForJavaElement(SingleMemberAnnotation annotation,
            IJavaElement javaElement) {
        IAnnotationAttributeInitializer annotationAttributeInitializer = AnnotationsManager
        .getAnnotationAttributeInitializerForName(annotation.getTypeName());
        if (annotationAttributeInitializer != null) {
            return annotationAttributeInitializer.getCompletionProposalsForSingleMemberAnnotation(javaElement,
                    annotation);
        }
        return Collections.emptyList();
    }


    @SuppressWarnings("unchecked")
    private MemberValuePair getMemberValuePairForPosition(NormalAnnotation normalAnnotation, int offset) {
        List<MemberValuePair> memberValuePairs = normalAnnotation.values();
        for (MemberValuePair memberValuePair : memberValuePairs) {
            Expression value = memberValuePair.getValue();
            int valueStartPosition = value.getStartPosition();
            int valueLength = value.getLength();
            if (offset >= valueStartPosition
                    && offset <= valueStartPosition + valueLength) {
                return memberValuePair;
            }
        }
        return null;
    }

    private boolean isWithinSingleMemberAnnotationValue(SingleMemberAnnotation singleMemberAnnotation, int offset) {
        Expression value = singleMemberAnnotation.getValue();
        if (value != null) {
            int valueStartPosition = value.getStartPosition();
            int valueLength = value.getLength();
            return (offset >= valueStartPosition && offset <= valueStartPosition + valueLength);
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public List computeContextInformation(ContentAssistInvocationContext context, IProgressMonitor monitor) {
        return Collections.emptyList();
    }

    public String getErrorMessage() {
        return null;
    }

    public void sessionEnded() {
    }

    public void sessionStarted() {
    }
}
