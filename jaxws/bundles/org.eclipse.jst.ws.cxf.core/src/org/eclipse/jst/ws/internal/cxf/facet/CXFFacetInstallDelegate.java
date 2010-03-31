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
package org.eclipse.jst.ws.internal.cxf.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.apt.core.util.AptConfig;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.internal.cxf.core.CXFCoreMessages;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.wst.common.project.facet.core.IDelegate;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;

/**
 * Adds the CXF classpath container to the project.
 * <p>
 * Also sets up the web projects application deployment descriptor (web.xml file)
 * to use cxf-servlet or the Spring Application context (WEB-INF/beans.xml) for
 * endpoint configuration. Depends on a setting in the CXF preferences.
 * 
 */
public class CXFFacetInstallDelegate implements IDelegate {

    public void execute(final IProject project, IProjectFacetVersion fv, Object config,
            IProgressMonitor monitor) throws CoreException {

        CXFDataModel model = (CXFDataModel) config;

        //Set project default
        CXFCorePlugin.getDefault().setCXFRuntimeVersion(project, model.getDefaultRuntimeVersion());

        if (CXFCorePlugin.getDefault().getJava2WSContext().getDefaultRuntimeLocation().equals("")) { //$NON-NLS-1$
            throw new CoreException(new Status(Status.ERROR, CXFCorePlugin.PLUGIN_ID,
                    CXFCoreMessages.CXF_FACET_INSTALL_DELEGATE_RUNTIME_LOCATION_NOT_SET));
        }

        IPath cxfLibPath = new Path(model.getDefaultRuntimeLocation());

        if (!cxfLibPath.hasTrailingSeparator()) {
            cxfLibPath = cxfLibPath.addTrailingSeparator();
        }
        cxfLibPath = cxfLibPath.append("lib"); //$NON-NLS-1$

        IClasspathAttribute jstComponentDependency =
            JavaCore.newClasspathAttribute("org.eclipse.jst.component.dependency", "/WEB-INF/lib"); //$NON-NLS-1$
        IClasspathEntry cxfClasspathContainer =
            JavaCore.newContainerEntry(new Path(CXFCorePlugin.CXF_CLASSPATH_CONTAINER_ID),
                    new IAccessRule[0],
                    CXFCorePlugin.getDefault().getJava2WSContext().isExportCXFClasspathContainer()
                    ? new IClasspathAttribute[]{jstComponentDependency} : new IClasspathAttribute[]{},
                            true);

        JDTUtils.addToClasspath(JavaCore.create(project), cxfClasspathContainer);

        if (CXFCorePlugin.getDefault().getJava2WSContext().isAnnotationProcessingEnabled()) {
            AptConfig.setEnabled(JavaCore.create(project), true);
        }
    }

}
