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
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;

public class RemoveMethodCorrectionProposal extends AbstractJavaCorrectionPropsoal {

    private SimpleName methodName;
    
    public RemoveMethodCorrectionProposal(IInvocationContext invocationContext, SimpleName methodName,
            String displayString, int relevance, Image image) {
        super(invocationContext, displayString, relevance, image);
        this.methodName = methodName;
    }

    @Override
    public void addEdits(TextFileChange textChange) throws CoreException {
        IBinding binding = methodName.resolveBinding();
        CompilationUnit astRoot = invocationContext.getASTRoot();
        IMethodBinding methodBinding = ((IMethodBinding) binding).getMethodDeclaration();
        ASTNode declaration = astRoot.findDeclaringNode(methodBinding);
        AST ast = astRoot.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);
        rewriter.remove(declaration, null);
        textChange.addEdit(rewriter.rewriteAST());
    }

}
