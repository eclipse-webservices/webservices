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
package org.eclipse.jst.ws.jaxws.core.tests;

import junit.framework.TestCase;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.MultiTextEdit;

public abstract class AbstractAnnotationTest extends TestCase {
    protected TestJavaProject testJavaProject;
    protected ICompilationUnit source;
    protected CompilationUnit compilationUnit;
    protected AST ast;
    protected ASTRewrite rewriter;
    protected Annotation annotation;
    protected TextFileChange textFileChange;
    
    @Override
    protected void setUp() throws Exception {
        testJavaProject = new TestJavaProject("JavaProject");
        testJavaProject.setAutoBuilding(isAutoBuildingEnabled());
        testJavaProject.enableAnnotationProcessing(isAnnotationProcessingEnabled());

        source = testJavaProject.createCompilationUnit(getPackageName(), getClassName(), getClassContents());

        compilationUnit = getAST(source);
        ast = compilationUnit.getAST();
        rewriter = ASTRewrite.create(ast);
        annotation = getAnnotation();
        textFileChange = new TextFileChange("Add annotation", (IFile) source.getResource());
        MultiTextEdit multiTextEdit = new MultiTextEdit();
        textFileChange.setEdit(multiTextEdit);
    }
    
    protected boolean isAutoBuildingEnabled() {
        return false;
    }

    protected boolean isAnnotationProcessingEnabled() {
        return false;
    }

    protected abstract String getPackageName();
    protected abstract String getClassName();
    protected abstract String getClassContents();
    protected abstract Annotation getAnnotation();
    
    protected boolean executeChange(IProgressMonitor monitor, Change change) {
        if (change == null) {
            return false;
        }

        change.initializeValidationData(monitor);

        PerformChangeOperation changeOperation = new PerformChangeOperation(change);

        try {
            changeOperation.run(monitor);
        } catch (CoreException ce) {
            ce.printStackTrace();
        }
        return changeOperation.changeExecuted();
    }
    
    @Override
    protected void tearDown() throws Exception {
    	deleteProject(testJavaProject.getProject());
    }

    private void deleteProject(IProject project) throws CoreException, InterruptedException {
    	int noAttempts = 0;
    	while (project != null && project.exists() && noAttempts < 5) {
    		try {
    			noAttempts++;
    			if (project.isOpen()) {
    				project.close(null);    				
    			}
    			project.delete(true, true, null);
    		} catch (ResourceException re) {
    			System.out.println(re.getLocalizedMessage());
    			Thread.sleep(1);
    		}
    	}
    }
    
    private CompilationUnit getAST(ICompilationUnit source) {
        final ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(source);
        return (CompilationUnit) parser.createAST(null);
    }
}
