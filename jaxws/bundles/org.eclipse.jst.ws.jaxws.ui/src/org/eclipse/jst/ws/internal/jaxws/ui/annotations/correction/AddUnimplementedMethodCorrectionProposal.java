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
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.internal.corext.codemanipulation.CodeGenerationSettings;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility2;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;

@SuppressWarnings("restriction")
public class AddUnimplementedMethodCorrectionProposal extends AbstractJavaCorrectionPropsoal {

    private ASTNode typeDeclaration;
    private String endpointInterface;

    public AddUnimplementedMethodCorrectionProposal(IInvocationContext invocationContext, ASTNode typeDeclaration, 
            String endpointInterface, String displayString, int relevance, Image image) {
        super(invocationContext, displayString, relevance, image);
        this.endpointInterface = endpointInterface;
        this.typeDeclaration = typeDeclaration;
    }

    @Override
    public void addEdits(TextFileChange textChange) throws CoreException {
        IMethodBinding[] unimplementedMethods = getUnimplementedMethods(typeDeclaration, endpointInterface);
        if (unimplementedMethods.length == 0) {
            return;
        }

        ICompilationUnit compilationUnit = invocationContext.getCompilationUnit();
        
        CompilationUnit astRoot = invocationContext.getASTRoot();
        
        AST ast = astRoot.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);
        
        CodeGenerationSettings settings = JavaPreferencesSettings.getCodeGenerationSettings(compilationUnit.getJavaProject());
        settings.overrideAnnotation = false;
        
        AbstractTypeDeclaration abstractTypeDeclaration = (AbstractTypeDeclaration) typeDeclaration;
        ListRewrite listRewrite = rewriter.getListRewrite(abstractTypeDeclaration, 
                abstractTypeDeclaration.getBodyDeclarationsProperty());
 
        ImportRewrite importRewrite = ImportRewrite.create(astRoot, true);

        for (int i = 0; i < unimplementedMethods.length; i++) {
            IMethodBinding methodBinding = unimplementedMethods[i];
            MethodDeclaration methodDeclaration = StubUtility2.createImplementationStub(compilationUnit, rewriter,
                    importRewrite, importRewrite.getDefaultImportRewriteContext(), methodBinding,
                    methodBinding.getDeclaringClass().getName(), settings, false);
            listRewrite.insertLast(methodDeclaration, null);
        }
        textChange.addEdit(rewriter.rewriteAST());
        if (importRewrite.hasRecordedChanges()) {
            textChange.addEdit(importRewrite.rewriteImports(null));
        }
    }
    
    public IMethodBinding[] getMethodsToImplement() {
        return getUnimplementedMethods(typeDeclaration, endpointInterface);
    }
    
    private IMethodBinding[] getUnimplementedMethods(ASTNode typeDeclaration, String endpointInterface) {
        List<IMethodBinding> methodBindings = new ArrayList<IMethodBinding>();
        if (typeDeclaration.getParent() instanceof CompilationUnit) {
            CompilationUnit implementationCompilationUnit = (CompilationUnit) typeDeclaration.getParent();
            if (implementationCompilationUnit.getJavaElement() instanceof ICompilationUnit) {
                try {
                    IJavaProject javaProject = implementationCompilationUnit.getJavaElement().getJavaProject();
                    IType seiType = javaProject.findType(endpointInterface);
                    
                    if (seiType != null) {
                        ICompilationUnit sei = seiType.getCompilationUnit();
                        CompilationUnit seiCompilationUnit = getAST(sei);
                        
                        List<MethodDeclaration> implementationMethods = getMethodDeclarations(
                                implementationCompilationUnit);
                        List<MethodDeclaration> seiMethods = getMethodDeclarations(seiCompilationUnit);
                        for (MethodDeclaration seiMethod : seiMethods) {
                            boolean implemented = false;
                            for (MethodDeclaration implMethod : implementationMethods) {                                
                                if (AnnotationUtils.compareMethods(seiMethod, implMethod)) {
                                    implemented = true;
                                    break;
                                }                   
                            }
                            if (!implemented) {
                                methodBindings.add(seiMethod.resolveBinding());
                            }
                        }
                    }
                } catch (JavaModelException jme) {
                    JAXWSUIPlugin.log(jme.getStatus());
                }
            }
            
        }
        return methodBindings.toArray(new IMethodBinding[methodBindings.size()]);
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
