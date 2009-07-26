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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.internal.ui.text.correction.proposals.CUCorrectionProposal;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.AnnotationsManager;
import org.eclipse.jst.ws.annotations.core.initialization.IAnnotationAttributeInitializer;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.TextEdit;

@SuppressWarnings("restriction")
public class AddAnnotationToTypeProposal extends CUCorrectionProposal {
    private CompilationUnit compilationUnit;
    private ICompilationUnit cu;
    private Class<? extends java.lang.annotation.Annotation> annotationClass;
    
    protected AddAnnotationToTypeProposal(CompilationUnit compilationUnit, String name, ICompilationUnit cu, 
            Class<? extends java.lang.annotation.Annotation> annotationClass, int relevance, Image image) {
        super(name, cu, relevance, image);
        this.compilationUnit = compilationUnit;
        this.cu = cu;
        this.annotationClass = annotationClass;
   }

    @Override
    protected TextChange createTextChange() throws CoreException {
        TextFileChange change = new TextFileChange("AC", (IFile) cu.getResource()); //$NON-NLS-1$
        MultiTextEdit multiTextEdit = new MultiTextEdit();
        change.setEdit(multiTextEdit);
        change.setKeepPreviewEdits(true);
        change.setSaveMode(TextFileChange.LEAVE_DIRTY);
        
        AST ast = compilationUnit.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);

        IAnnotationAttributeInitializer annotationAttributeInitializer = 
            AnnotationsManager.getAnnotationDefinitionForClass(annotationClass).
                getAnnotationAttributeInitializer();
        
        List<MemberValuePair> memberValuePairs = annotationAttributeInitializer.getMemberValuePairs(
                cu.findPrimaryType(), ast, annotationClass);
        
        Annotation annotation = AnnotationsCore.createAnnotation(ast, annotationClass,
                annotationClass.getSimpleName(), memberValuePairs);
        
        try {
            AnnotationUtils.createTypeAnnotationChange(cu, compilationUnit, rewriter, cu.findPrimaryType(),
                    annotation, change);
            
            ImportRewrite importRewrite = CodeStyleConfiguration.createImportRewrite(cu, true);
            importRewrite.addImport(annotationClass.getCanonicalName());
            if (importRewrite.hasRecordedChanges()) {
                TextEdit importTextEdit = importRewrite.rewriteImports(null);
                change.addEdit(importTextEdit);
            }
        } catch (CoreException ce) {
            JAXWSUIPlugin.log(ce.getStatus());
        }
        return change;
    }
}
