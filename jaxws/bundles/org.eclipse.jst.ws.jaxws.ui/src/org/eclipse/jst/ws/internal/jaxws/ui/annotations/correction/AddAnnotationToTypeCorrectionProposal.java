/*******************************************************************************
 * Copyright (c) 2009, 2011 Shane Clarke.
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
package org.eclipse.jst.ws.internal.jaxws.ui.annotations.correction;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.AnnotationsManager;
import org.eclipse.jst.ws.annotations.core.initialization.IAnnotationAttributeInitializer;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;
import org.eclipse.text.edits.TextEdit;

public class AddAnnotationToTypeCorrectionProposal extends AbstractJavaCorrectionPropsoal {

    private Class<? extends java.lang.annotation.Annotation> annotationClass;

    public AddAnnotationToTypeCorrectionProposal(IInvocationContext invocationContext,
            Class<? extends java.lang.annotation.Annotation> annotationClass, String displayString, int relevance, Image image) {
        super(invocationContext, displayString, relevance, image);
        this.annotationClass = annotationClass;
    }

    @Override
    public void addEdits(TextFileChange textChange) throws CoreException {
        ICompilationUnit compilationUnit = invocationContext.getCompilationUnit();

        CompilationUnit astRoot = invocationContext.getASTRoot();

        AST ast = astRoot.getAST();

        IAnnotationAttributeInitializer annotationAttributeInitializer =
            AnnotationsManager.getAnnotationDefinitionForClass(annotationClass.getCanonicalName()).getAnnotationAttributeInitializer();

        List<MemberValuePair> memberValuePairs = null;
        IType annotationType = JDTUtils.findType(compilationUnit.getJavaProject(), annotationClass.getCanonicalName());
        if (annotationType != null) {
            memberValuePairs = annotationAttributeInitializer.getMemberValuePairs(compilationUnit.findPrimaryType(),
                    ast, annotationType);
        }

        Annotation annotation = AnnotationsCore.createNormalAnnotation(ast, annotationClass.getSimpleName(),
                memberValuePairs);

        textChange.addEdit(AnnotationUtils.createAddAnnotationTextEdit(compilationUnit.findPrimaryType(), annotation));

        ImportRewrite importRewrite = CodeStyleConfiguration.createImportRewrite(compilationUnit, true);
        importRewrite.addImport(annotationClass.getCanonicalName());
        if (importRewrite.hasRecordedChanges()) {
            TextEdit importTextEdit = importRewrite.rewriteImports(null);
            textChange.addEdit(importTextEdit);
        }
    }

}
