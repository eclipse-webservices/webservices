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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.IUndoManager;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.text.edits.MultiTextEdit;

public abstract class AbstractJavaCorrectionPropsoal implements IJavaCompletionProposal {

    private int relevance;
    private String displayString;
    private Image image;
    private TextFileChange textChange;

    protected IInvocationContext invocationContext;
    
    public AbstractJavaCorrectionPropsoal(IInvocationContext invocationContext, String displayString, int relevance, Image image) {
        this.invocationContext = invocationContext;
        this.displayString = displayString;
        this.relevance = relevance;
        this.image = image;
    }
    
    public int getRelevance() {
        return relevance;
    }
    
    public abstract void addEdits(TextFileChange textChange) throws CoreException;
    
    private TextFileChange getTextChange() throws CoreException {
        if (textChange == null) {
            ICompilationUnit compilationUnit = invocationContext.getCompilationUnit();
            textChange = new TextFileChange(getDisplayString(), (IFile) compilationUnit.getResource()); //$NON-NLS-1$
            MultiTextEdit multiTextEdit = new MultiTextEdit();
            textChange.setEdit(multiTextEdit);
            textChange.setKeepPreviewEdits(true);
            textChange.setSaveMode(TextFileChange.LEAVE_DIRTY);
            addEdits(textChange);
        }
        return textChange;
    }
    
    public void apply(IDocument document) {
        IProgressMonitor monitor = new NullProgressMonitor();
        IUndoManager manager= RefactoringCore.getUndoManager();
        boolean successful = false;
        Change undoChange = null;
        try {
            textChange = getTextChange();
            textChange.initializeValidationData(monitor);
            RefactoringStatus valid = textChange.isValid(monitor);
            if (valid.isOK()) {
                manager.aboutToPerformChange(textChange);
                undoChange = textChange.perform(monitor);
                successful = true;
            }
        } catch (CoreException ce) {
            JAXWSUIPlugin.log(ce.getStatus());
        } finally {
            manager.changePerformed(textChange, successful);
        }
        if (undoChange != null) {
            undoChange.initializeValidationData(monitor);
            manager.addUndo(undoChange.getName(), undoChange);
        }
    }

    public String getAdditionalProposalInfo() {
        return null;
    }


    public IContextInformation getContextInformation() {
        return null;
    }

    public String getDisplayString() {
        return displayString;
    }

    public Image getImage() {
        return image;
    }

    public Point getSelection(IDocument document) {
        return null;
    }
    
    protected CompilationUnit getAST(ICompilationUnit source) {
        final ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setResolveBindings(true);
        parser.setSource(source);
        return (CompilationUnit) parser.createAST(null);
    }

}
