/*******************************************************************************
 * Copyright (c) 2008, 2009 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.consumption.ui.widgets.object;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jst.ws.internal.cxf.consumption.ui.CXFConsumptionUIPlugin;
import org.eclipse.wst.command.internal.env.core.data.Transformer;

@SuppressWarnings("restriction")
public class JAXWSSelectionTransformer implements Transformer {

    public Object transform(Object value) {
        if (value instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) value;
            Object selection = structuredSelection.getFirstElement();
            if (selection instanceof IFile) {
                IFile file = (IFile) selection;
                String fullyQualifiedJavaName = this.getFullyQualifiedJavaName(file);
                return new StructuredSelection(fullyQualifiedJavaName);
            } else if (selection instanceof ICompilationUnit) {
                IResource resource = ((ICompilationUnit) selection).getResource();
                if (resource instanceof IFile) {
                    IFile file = (IFile) resource;
                    String fullyQualifiedJavaName = this.getFullyQualifiedJavaName(file);
                    return new StructuredSelection(fullyQualifiedJavaName);
                }
            }
            if (selection instanceof String) {
                return structuredSelection;
            }
        }
        return value;
    }

    private String getFullyQualifiedJavaName(IFile resource) {
        IProject project = resource.getProject();

        IPath path = resource.getFullPath();

        if (path.getFileExtension() != null) {
            path = path.removeFileExtension();
        }

        String javaFileName = path.lastSegment();

        if (path.isAbsolute()) {
            try {
                IPath javaFolderPath = path.removeLastSegments(1);
                IPackageFragment packageFragment = JavaCore.create(project).findPackageFragment(
                        javaFolderPath);
                return packageFragment.getElementName() + "." + javaFileName; //$NON-NLS-1$
            } catch (JavaModelException jme) {
                CXFConsumptionUIPlugin.log(jme.getStatus());
            }
        }
        return javaFileName;
    }
}
