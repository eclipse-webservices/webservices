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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;

public class ChangeReturnTypeCorrectionProposal extends AbstractJavaCorrectionPropsoal {
	
	private TypeDeclaration implTypeDeclaration;
	private MethodDeclaration implMethodDeclaration;
	private String endpointInterface;
	
	public ChangeReturnTypeCorrectionProposal(
			IInvocationContext invocationContext, TypeDeclaration implTypeDeclaration, MethodDeclaration implMethodDeclaration, String endpointInterface, String displayString,
			int relevance, Image image) {
		super(invocationContext, displayString, relevance, image);
	    this.implTypeDeclaration = implTypeDeclaration;
	    this.implMethodDeclaration = implMethodDeclaration;
	    this.endpointInterface = endpointInterface;
	}

	@Override
	public void addEdits(TextFileChange textChange) throws CoreException {
		MethodDeclaration seiMethodDeclaration = getSEIMethodDeclaration();
		if (seiMethodDeclaration == null) {
			return;
		}
		
        ICompilationUnit compilationUnit = invocationContext.getCompilationUnit();
        
        CompilationUnit astRoot = invocationContext.getASTRoot();
        
        AST ast = astRoot.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);
        ITypeBinding returnType = seiMethodDeclaration.resolveBinding().getReturnType();
        ImportRewrite importRewrite = CodeStyleConfiguration.createImportRewrite(compilationUnit, true);
        Type type = importRewrite.addImport(returnType, ast);
        rewriter.set(implMethodDeclaration, MethodDeclaration.RETURN_TYPE2_PROPERTY, type, null);
        rewriter.set(implMethodDeclaration, MethodDeclaration.EXTRA_DIMENSIONS_PROPERTY, new Integer(0), null);
        
        textChange.addEdit(rewriter.rewriteAST());
        if (importRewrite.hasRecordedChanges()) {
        	textChange.addEdit(importRewrite.rewriteImports(null));
        }
	}

    private MethodDeclaration getSEIMethodDeclaration() {
        if (implTypeDeclaration.getParent() instanceof CompilationUnit) {
            CompilationUnit implCompilationUnit = (CompilationUnit) implTypeDeclaration.getParent();
            if (implCompilationUnit.getJavaElement() instanceof ICompilationUnit) {
                try {
                    IJavaProject javaProject = implCompilationUnit.getJavaElement().getJavaProject();
                    IType seiType = javaProject.findType(endpointInterface);
                    
                    if (seiType != null) {
                        ICompilationUnit seiCompilationUnit = seiType.getCompilationUnit();
                        CompilationUnit seiASTRoot = getAST(seiCompilationUnit);

                        List<MethodDeclaration> seiMethodDeclarations = getMethodDeclarations(seiASTRoot);
                        for (MethodDeclaration seiMethodDeclaration : seiMethodDeclarations) {
                        	if (AnnotationUtils.compareMethods(implMethodDeclaration, seiMethodDeclaration)) {
                        		return seiMethodDeclaration;
                        	}
                        }
                    }
                } catch (JavaModelException jme) {
                    JAXWSUIPlugin.log(jme.getStatus());
                }
            }
        }
        return null;
	}

    private List<MethodDeclaration> getMethodDeclarations(CompilationUnit compilationUnit) {
        final List<MethodDeclaration> methodDeclarations = new ArrayList<MethodDeclaration>();
        
        compilationUnit.accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodDeclaration methodDeclaration) {
                methodDeclarations.add(methodDeclaration);
                return false;
            }
        });
        return methodDeclarations;
    }
}
