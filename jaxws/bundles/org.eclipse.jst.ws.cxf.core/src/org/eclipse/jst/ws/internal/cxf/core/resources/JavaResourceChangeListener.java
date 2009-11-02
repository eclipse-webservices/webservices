/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.core.resources;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;

/**
 * Listens for changes to the <code>IJavaProject</code> src folder.
 *
 */
public class JavaResourceChangeListener implements IResourceChangeListener {
    private List<IResource> changedResources = new ArrayList<IResource>();

    private IPath sourceDirectoryPath;

    /**
     * Constructs a JavaResourceChangeListener instance.
     *
     * @param sourceDirectoryPath
     */
    public JavaResourceChangeListener(IPath sourceDirectoryPath) {
        this.sourceDirectoryPath = sourceDirectoryPath;
    }

    public void resourceChanged(IResourceChangeEvent event) {
        if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
            return;
        }
        IResourceDelta rootDelta = event.getDelta();

        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
        IFolder srcFolder = workspaceRoot.getFolder(sourceDirectoryPath);
        IPath srcFolderPath = srcFolder.getFullPath();
        if (!srcFolderPath.hasTrailingSeparator()) {
            srcFolderPath = srcFolderPath.addTrailingSeparator();
        }

        IResourceDelta javaResourceDelta = rootDelta.findMember(srcFolderPath);

        if (javaResourceDelta == null) {
            return;
        }

        IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
            public boolean visit(IResourceDelta delta) {
                if (delta.getKind() != IResourceDelta.ADDED) {
                    return true;
                }
                IResource resource = delta.getResource();
                changedResources.add(resource);
                return true;
            }
        };
        try {
            javaResourceDelta.accept(visitor);
        } catch (CoreException ce) {
            CXFCorePlugin.log(ce.getStatus());
        }
    }

    public List<IResource> getChangedResources() {
        return changedResources;
    }

}
