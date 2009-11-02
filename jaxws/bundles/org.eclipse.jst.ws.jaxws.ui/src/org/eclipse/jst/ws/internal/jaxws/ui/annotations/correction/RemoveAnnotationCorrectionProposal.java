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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;

public class RemoveAnnotationCorrectionProposal extends AbstractJavaCorrectionPropsoal {

    private Class<? extends java.lang.annotation.Annotation> annotation;
    private ASTNode parentNode;

    public RemoveAnnotationCorrectionProposal(IInvocationContext invocationContext,
            Class<? extends java.lang.annotation.Annotation> annotation, ASTNode parentNode, String displayString,
            int relevance, Image image) {
        super(invocationContext, displayString, relevance, image);
        this.annotation = annotation;
        this.parentNode = parentNode;
    }

    @Override
    public void addEdits(TextFileChange textChange) throws CoreException {
        ASTRewrite rewriter = ASTRewrite.create(parentNode.getAST());

        if (parentNode instanceof Annotation) {
            Annotation jdtDomAnnotation = (Annotation) parentNode;
            rewriter.remove(jdtDomAnnotation, null);
        }

        if (parentNode instanceof MethodDeclaration) {
            MethodDeclaration methodDeclaration = (MethodDeclaration) parentNode;
            Annotation jdtDomAnnotation = AnnotationUtils.getAnnotation(methodDeclaration.resolveBinding().getJavaElement(), annotation);
            if (jdtDomAnnotation != null) {
                rewriter.remove(jdtDomAnnotation, null);
            }
        }

        if (parentNode.getParent() instanceof TypeDeclaration) {
            TypeDeclaration typeDeclaration = (TypeDeclaration) parentNode.getParent();
            Annotation jdtDomAnnotation = AnnotationUtils.getAnnotation(typeDeclaration.resolveBinding().getJavaElement(), annotation);
            if (jdtDomAnnotation != null) {
                rewriter.remove(jdtDomAnnotation, null);
            }
        }

        textChange.addEdit(rewriter.rewriteAST());
    }

}
