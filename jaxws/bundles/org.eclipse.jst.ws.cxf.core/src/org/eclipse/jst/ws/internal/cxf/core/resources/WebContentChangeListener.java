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
package org.eclipse.jst.ws.internal.cxf.core.resources;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;

/**
 * Listens for changes to the web projects WebContent folder.
 * 
 */
@SuppressWarnings("restriction")
public class WebContentChangeListener implements IResourceChangeListener {
    private List<IResource> changedResources = new ArrayList<IResource>();

    private String projectName;
    
    public WebContentChangeListener(String projectName) {
        this.projectName = projectName;
    }
    
    public void resourceChanged(IResourceChangeEvent event) {
        if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
            return;
        }
        IResourceDelta rootDelta = event.getDelta();

        IPath webContentPath = J2EEUtils.getWebContentPath(ResourcesPlugin.getWorkspace().getRoot()
                .getProject(projectName));
        if (!webContentPath.hasTrailingSeparator()) {
            webContentPath = webContentPath.addTrailingSeparator();
        }
        
        IResourceDelta webResourceDelta = rootDelta.findMember(webContentPath); //$NON-NLS-1$
        if (webResourceDelta == null) {
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
            webResourceDelta.accept(visitor);
        } catch (CoreException ce) {
            CXFCorePlugin.log(ce.getStatus());
        }
    }
    
    public List<IResource> getChangedResources() {
        return changedResources;
    }

}
