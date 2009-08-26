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
package org.eclipse.jst.ws.internal.cxf.ui.viewers;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIPlugin;

/**
 * <code>ViewerFilter</code> that filters on classes or interfaces within a <code>IJavaProject</code>
 *
 */
public class JavaViewerFilter extends ViewerFilter {

    private IJavaProject javaProject;
    private  boolean filterClasses;
    
    /**
     * Constructs an instance of <code>JavaViewerFilter</code> given a <code>IJavaProject</code> to filter and a 
     * boolean value that controls what to filter in the project.
     * 
     * @param javaProject the java project to filter
     * @param filterClasses true to filter all classes. false to filter all interfaces 
     */
    public JavaViewerFilter(IJavaProject javaProject, boolean filterClasses) {
        this.javaProject = javaProject;
        this.filterClasses = filterClasses;
        
    }
    
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        try {
            if (element instanceof IJavaProject) {
                return javaProject.equals((IJavaProject) element);
            }
            if (element instanceof IPackageFragmentRoot) {
                IPackageFragmentRoot packageFragmentRoot = (IPackageFragmentRoot) element;
                return packageFragmentRoot.getKind() == IPackageFragmentRoot.K_SOURCE;
            }
            if (element instanceof IPackageFragment) {
                IPackageFragment packageFragment = (IPackageFragment) element;
                return packageFragment.hasChildren();
            }
            if (element instanceof ICompilationUnit) {
                ICompilationUnit compilationUnit = (ICompilationUnit) element;
                IType type = compilationUnit.findPrimaryType();
                if (filterClasses) {
                    return type.isInterface() && !type.isAnnotation();
                } else {
                    return type.isClass();
                }

            }
        } catch (JavaModelException jme) {
            CXFUIPlugin.log(jme.getStatus());
        }
        return false;
    }

}
