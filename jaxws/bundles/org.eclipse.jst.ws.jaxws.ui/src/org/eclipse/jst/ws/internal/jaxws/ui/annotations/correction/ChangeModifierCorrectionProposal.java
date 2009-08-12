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
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;

public class ChangeModifierCorrectionProposal extends AbstractJavaCorrectionPropsoal {
    
    private IBinding binding;
    private int includedModifiers;
    private int excludedModifiers;
    
    
    public ChangeModifierCorrectionProposal(IInvocationContext invocationContext, IBinding binding, 
            int includedModifiers, int excludedModifiers, String displayString, int relevance, Image image) {
        super(invocationContext, displayString, relevance, image);
        this.binding = binding;
        this.includedModifiers = includedModifiers;
        this.excludedModifiers = excludedModifiers;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addEdits(TextFileChange textChange) throws CoreException {
        CompilationUnit astRoot = invocationContext.getASTRoot();
        
        AST ast = astRoot.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);

        ASTNode declaringNode = astRoot.findDeclaringNode(binding);
        
        int consideredModifierFlags = includedModifiers | excludedModifiers; 
        int newModifierFlags = includedModifiers & consideredModifierFlags;

        ListRewrite listRewrite = getListRewrite(declaringNode, rewriter);
        if (listRewrite != null) {
            List<ASTNode> originalList = listRewrite.getOriginalList();
            for (ASTNode astNode : originalList) {
                if (astNode instanceof Modifier) {
                    Modifier modifier = (Modifier) astNode;
                    int modifierFlag = modifier.getKeyword().toFlagValue();
                    if ((consideredModifierFlags & modifierFlag) != 0) {
                        if ((newModifierFlags & modifierFlag) == 0) {
                            listRewrite.remove(modifier, null);
                        }
                        newModifierFlags &= ~modifierFlag;
                    }
                }
            }
                        
            List<Modifier> modifers = ast.newModifiers(newModifierFlags);
            ASTNode lastAnnotation = getLastAnnotation(listRewrite);
            for (Modifier modifier : modifers) {
                int modifierFlag = modifier.getKeyword().toFlagValue();
                if ((modifierFlag & (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED)) != 0) {
                    if (lastAnnotation != null) {
                        listRewrite.insertAfter(modifier, lastAnnotation, null);
                    } else {
                    	listRewrite.insertFirst(modifier, null);
                    }
                }
            }
            
            textChange.addEdit(rewriter.rewriteAST());
        }
    }
    
    @SuppressWarnings("unchecked")
    private ASTNode getLastAnnotation(ListRewrite listRewrite) {
        ASTNode lastAnnotation = null;
        List<ASTNode> rewrittenList = listRewrite.getRewrittenList();
        for (ASTNode astNode : rewrittenList) {
            if (astNode instanceof IExtendedModifier) {
                IExtendedModifier extendedModifier = (IExtendedModifier) astNode;
                if (extendedModifier.isAnnotation()) {
                    lastAnnotation = astNode;
                }
            }
        }
        return lastAnnotation;
    }
    
    private ListRewrite getListRewrite(ASTNode astNode, ASTRewrite astRewrite) {
        if (astNode instanceof MethodDeclaration) {
            MethodDeclaration methodDeclaration = (MethodDeclaration) astNode;
            return astRewrite.getListRewrite(methodDeclaration, MethodDeclaration.MODIFIERS2_PROPERTY);
        }
        if (astNode instanceof TypeDeclaration) {
            TypeDeclaration typeDeclaration = (TypeDeclaration) astNode;
            return astRewrite.getListRewrite(typeDeclaration, TypeDeclaration.MODIFIERS2_PROPERTY);
        }
        
        return null;
    }

}
