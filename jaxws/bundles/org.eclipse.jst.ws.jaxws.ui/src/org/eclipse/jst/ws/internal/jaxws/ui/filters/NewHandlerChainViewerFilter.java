/*******************************************************************************
 * Copyright (c) 2010 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.ui.filters;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;

public class NewHandlerChainViewerFilter extends ViewerFilter {
    private static final String XML_FILE_EXTENSION = "xml";  //$NON-NLS-1$

    private IJavaProject javaProject;
    private boolean filterFiles;
    private boolean filterCompilationUnits;

    public NewHandlerChainViewerFilter(IJavaProject javaProject, boolean filterFiles, boolean filterCompilationUnits) {
        this.javaProject = javaProject;
        this.filterFiles = filterFiles;
        this.filterCompilationUnits = filterCompilationUnits;
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        try {
            if (element instanceof IJavaProject) {
                if (javaProject != null) {
                    return javaProject.equals(element);
                }
                return false;

            }
            if (element instanceof IPackageFragmentRoot) {
                IPackageFragmentRoot packageFragmentRoot = (IPackageFragmentRoot) element;
                return packageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE;
            }
            if (element instanceof IPackageFragment) {
                return true;
            }

            if (!filterFiles && element instanceof IFile) {
                IFile file = (IFile) element;
                return file.isAccessible() && file.getFileExtension().equals(XML_FILE_EXTENSION);
            }

            if (!filterCompilationUnits && element instanceof ICompilationUnit) {
            	ICompilationUnit compilationUnit = (ICompilationUnit) element;
                IType type = compilationUnit.findPrimaryType();
                if (type != null) {
                    return type.isClass() || type.isInterface() && !type.isAnnotation();                	
                }
            }
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme);
        }
        return false;
    }
}