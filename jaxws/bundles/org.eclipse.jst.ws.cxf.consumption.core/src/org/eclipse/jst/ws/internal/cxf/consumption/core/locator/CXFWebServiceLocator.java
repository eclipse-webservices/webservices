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
package org.eclipse.jst.ws.internal.cxf.consumption.core.locator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.wst.ws.internal.wsfinder.AbstractWebServiceLocator;

@SuppressWarnings("restriction")
public class CXFWebServiceLocator extends AbstractWebServiceLocator {

    @Override
    @SuppressWarnings("unchecked")
    public List getWebServiceClients(IProgressMonitor monitor) {
        return super.getWebServiceClients(monitor);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List getWebServiceClients(IProject[] projects, IProgressMonitor monitor) {
        return super.getWebServiceClients(projects, monitor);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List getWebServices(IProgressMonitor monitor) {
        IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
        return getWebServices(projects, monitor);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List getWebServices(IProject[] projects, IProgressMonitor monitor) {
        List webServices = new ArrayList();
        for (IProject project : projects) {
            if (J2EEUtils.isWebComponent(project)) {
                webServices.addAll(getWebServicesFromProject(project));
            }
        }
        return webServices;
    }

    @SuppressWarnings("unchecked")
    private List getWebServicesFromProject(IProject project) {
        return null;
    }

}
