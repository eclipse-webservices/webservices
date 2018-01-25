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

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jst.ws.annotations.core.AnnotationsManager;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;

public class AnnotationsViewContentProvider implements ITreeContentProvider {

    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof IType) {
            try {
                return ((IType) parentElement).getMethods();
            } catch (JavaModelException jme) {
                JAXWSUIPlugin.log(jme.getStatus());
            }
        }
        return new Object[] {};
    }

    public Object getParent(Object element) {
        return null;
    }

    public boolean hasChildren(Object element) {
        if (element instanceof IType) {
            try {
                return ((IType) element).getMethods().length > 0;
            } catch (JavaModelException jme) {
                JAXWSUIPlugin.log(jme.getStatus());
            }
        }
        return false;
    }

    public Object[] getElements(Object inputElement) {
        if (inputElement != null && inputElement instanceof IJavaElement && ((IJavaElement) inputElement).exists()) {
            IJavaElement javaElement = (IJavaElement) inputElement;
            return AnnotationsManager.getAnnotationTypes(javaElement).toArray();
        }
        return new Object[] {};
    }

    public void dispose() {

    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

}
