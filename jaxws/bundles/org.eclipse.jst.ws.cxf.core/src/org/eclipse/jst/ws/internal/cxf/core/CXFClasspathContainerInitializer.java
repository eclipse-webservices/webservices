/*******************************************************************************
 * Copyright (c) 2008, 2010 IONA Technologies PLC
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
package org.eclipse.jst.ws.internal.cxf.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class CXFClasspathContainerInitializer extends ClasspathContainerInitializer {

    @Override
    public void initialize(IPath containerPath, IJavaProject javaProject) throws CoreException {
        CXFClasspathContainer cxfClasspathContainer = new CXFClasspathContainer(containerPath, javaProject);

        if (cxfClasspathContainer.isValid()) {
            JavaCore.setClasspathContainer(containerPath, new IJavaProject[] { javaProject },
                    new IClasspathContainer[] { cxfClasspathContainer }, null);
        }
    }

    @Override
    public boolean canUpdateClasspathContainer(IPath containerPath, IJavaProject project) {
        return true;
    }

    @Override
    public void requestClasspathContainerUpdate(IPath containerPath, IJavaProject javaProject,
            IClasspathContainer containerSuggestion) throws CoreException {
        JavaCore.setClasspathContainer(containerPath, new IJavaProject[] {javaProject},
                new IClasspathContainer[] { containerSuggestion }, null);
    }

}
