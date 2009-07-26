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
package org.eclipse.jst.ws.internal.jaxws.ui.views;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;

public class ClassDialogCellEditor extends DialogCellEditor {

    public ClassDialogCellEditor(Composite parent) {
        super(parent, SWT.NONE);
    }
    
    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
        try {
            SelectionDialog dialog = JavaUI.createTypeDialog(cellEditorWindow.getShell(), 
                    PlatformUI.getWorkbench().getProgressService(), 
                    SearchEngine.createWorkspaceScope(), IJavaElementSearchConstants.CONSIDER_CLASSES, 
                    false, "* "); //$NON-NLS-1$
            
            if (dialog.open() == IDialogConstants.OK_ID) {
                Object[] types = dialog.getResult();
                
                if (types != null && types.length > 0) {
                    IType type = (IType)types[0];
                    if (type.isBinary()) {
                        return type.getFullyQualifiedName();
                    }
                }
            }
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
        }
        return null;
    }
    
    @Override
    protected void updateContents(Object value) {
        if (value != null) {
            getDefaultLabel().setText(value.toString() + ".class"); //$NON-NLS-1$
        } else {
            getDefaultLabel().setText(""); //$NON-NLS-1$
        }
    }
}
