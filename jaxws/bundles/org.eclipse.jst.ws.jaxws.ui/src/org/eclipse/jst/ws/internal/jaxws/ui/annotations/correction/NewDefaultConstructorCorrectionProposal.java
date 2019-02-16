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
package org.eclipse.jst.ws.internal.jaxws.ui.annotations.correction;

import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jdt.ui.CodeGeneration;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;

public class NewDefaultConstructorCorrectionProposal extends AbstractJavaCorrectionPropsoal {

	private ITypeBinding typeBinding;
	
	public NewDefaultConstructorCorrectionProposal(
			IInvocationContext invocationContext, ITypeBinding typeBinding, String displayString,
			int relevance, Image image) {
		super(invocationContext, displayString, relevance, image);
		this.typeBinding = typeBinding;
	}

	@Override
	@SuppressWarnings("unchecked")
    public void addEdits(TextFileChange textChange) throws CoreException {
        CompilationUnit astRoot = invocationContext.getASTRoot();
        AST ast = astRoot.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);

		ASTNode typeDeclaration = astRoot.findDeclaringNode(typeBinding);

		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();

		SimpleName newNameNode = ast.newSimpleName(typeBinding.getName());

		methodDeclaration.setConstructor(true);

		methodDeclaration.modifiers().addAll(ast.newModifiers(Modifier.PUBLIC));

		methodDeclaration.setName(newNameNode);

		Block body = ast.newBlock();

		String placeHolder = CodeGeneration.getMethodBodyContent(invocationContext.getCompilationUnit(),
                typeBinding.getName(), newNameNode.getIdentifier(), true, "", String.valueOf('\n'));
		if (placeHolder != null) {
		    ASTNode todoNode = rewriter.createStringPlaceholder(placeHolder, ASTNode.RETURN_STATEMENT);
		    body.statements().add(todoNode);
		}
		methodDeclaration.setBody(body);
		
		ChildListPropertyDescriptor property = ((AbstractTypeDeclaration) typeDeclaration).getBodyDeclarationsProperty();
		List<ASTNode> members = (List<ASTNode>) typeDeclaration.getStructuralProperty(property);
		ListIterator<ASTNode> membersIterator = members.listIterator();
		int insertAt = 0;
		while (membersIterator.hasPrevious()) {
			ASTNode astNode = (ASTNode) membersIterator.previous();
			if (astNode instanceof MethodDeclaration && ((MethodDeclaration) astNode).isConstructor()) {
				insertAt = membersIterator.previousIndex() + 1;
			}
		}
		ListRewrite listRewriter= rewriter.getListRewrite(typeDeclaration, property);
		listRewriter.insertAt(methodDeclaration, insertAt, null);
		textChange.addEdit(rewriter.rewriteAST());
	}

}
