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
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite.ImportRewriteContext;
import org.eclipse.jdt.internal.corext.codemanipulation.CodeGenerationSettings;
import org.eclipse.jdt.internal.corext.codemanipulation.ContextSensitiveImportRewriteContext;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility2;
import org.eclipse.jdt.internal.corext.fix.AddUnimplementedMethodsOperation;
import org.eclipse.jdt.internal.corext.fix.LinkedProposalModel;
import org.eclipse.jdt.internal.corext.refactoring.structure.CompilationUnitRewrite;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIMessages;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;

@SuppressWarnings("restriction")
public class AddUnimplementedSEIMethodsOperation extends AddUnimplementedMethodsOperation {
    private ASTNode astNode;
    private String endpointInterface;
    
    public AddUnimplementedSEIMethodsOperation(ASTNode typeNode, String endpointInterface) {
        super(typeNode);
        this.astNode = typeNode;
        this.endpointInterface = endpointInterface;
    }
    
    @Override
    public void rewriteAST(CompilationUnitRewrite compilationUnitRewrite, LinkedProposalModel model) throws CoreException {
        IMethodBinding[] unimplementedMethods = getUnimplementedMethods(astNode, endpointInterface);
        if (unimplementedMethods.length == 0) {
            return;
        }
        
        ImportRewriteContext importRewriteContext = new ContextSensitiveImportRewriteContext(
                (CompilationUnit) astNode.getRoot(), astNode.getStartPosition(), 
                compilationUnitRewrite.getImportRewrite());
        
        ASTRewrite astRewrite = compilationUnitRewrite.getASTRewrite();
        ICompilationUnit compilationUnit = compilationUnitRewrite.getCu();
        CodeGenerationSettings settings = JavaPreferencesSettings.getCodeGenerationSettings(
                compilationUnit.getJavaProject());
        settings.overrideAnnotation = false;
        
        AbstractTypeDeclaration typeDeclaration = (AbstractTypeDeclaration) astNode;
        ListRewrite listRewrite = astRewrite.getListRewrite(typeDeclaration, 
                typeDeclaration.getBodyDeclarationsProperty());
 
        ImportRewrite importRewrite = compilationUnitRewrite.getImportRewrite();

        for (int i= 0; i < unimplementedMethods.length; i++) {
            IMethodBinding methodBinding = unimplementedMethods[i];
            MethodDeclaration methodDeclaration = StubUtility2.createImplementationStub(compilationUnit, 
                    astRewrite, importRewrite, importRewriteContext, methodBinding, 
                    methodBinding.getDeclaringClass().getName(), settings, false);
            listRewrite.insertLast(methodDeclaration, createTextEditGroup(JAXWSUIMessages.ADD_MISSING_METHOD, 
                    compilationUnitRewrite));
        }
    }

    @Override
    public IMethodBinding[] getMethodsToImplement() {
        return getUnimplementedMethods(astNode, endpointInterface);
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
                        CompilationUnit seiCompilationUnit = AnnotationUtils.getASTParser(sei, true);
                        
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
