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
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jst.ws.internal.cxf.ui.CXFUIPlugin;

/**
 * <code>ViewerFilter</code> that filters on classes or interfaces within a <code>IJavaProject</code>
 *
 */
public class JavaViewerFilter extends ViewerFilter {

    private IJavaProject javaProject;
    private int elementKinds;
    
    /**
     * Constructs an instance of <code>JavaViewerFilter</code> given a <code>IJavaProject</code> and an
     * <code>IJavaSearchConstants</code> element kind to search for. All other elements are filtered.
     * 
     * @param javaProject the java project to filter
     * @param elementKinds a flag defining nature of searched elements; the only valid values are: 
     *  <code>IJavaSearchConstants.CLASS</code>
     *  <code>IJavaSearchConstants.INTERFACE</code>
     *  <code>IJavaSearchConstants.CLASS_AND_INTERFACE</code>
     */
    public JavaViewerFilter(IJavaProject javaProject, int elementKinds) {
        this.javaProject = javaProject;
        this.elementKinds = elementKinds;
        
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
                switch (elementKinds) {
                case IJavaSearchConstants.CLASS:
                    return type.isClass();
                case IJavaSearchConstants.INTERFACE:
                    return type.isInterface() && !type.isAnnotation();
                case IJavaSearchConstants.CLASS_AND_INTERFACE:
                    return type.isClass() || (type.isInterface() && !type.isAnnotation());
                }

            }
        } catch (JavaModelException jme) {
            CXFUIPlugin.log(jme.getStatus());
        }
        return false;
    }

}
