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
package org.eclipse.jst.ws.internal.cxf.core;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall;

public class CXFClasspathContainer implements IClasspathContainer {

    private IPath path;
    private List<IClasspathEntry> classpathEntries;
    private String cxfLibraryType;
    private String cxfLibraryVersion;
    private String cxfLibraryLocation;
    private IProject project;

    public CXFClasspathContainer(IPath path, IJavaProject javaProject) {
        this.path = path;
        this.project = javaProject.getProject();
        classpathEntries =  new ArrayList<IClasspathEntry>();
        this.cxfLibraryLocation = getCxfRuntimeLocation();
        this.cxfLibraryVersion = getCxfRuntimeVersion();
        this.cxfLibraryType = getCxfRuntimeEdition();
    }

    public IClasspathEntry[] getClasspathEntries() {
        //                if (!cxfLibraryVersion.equals(getCxfRuntimeVersion())) {
        //                    classpathEntries = new ArrayList<IClasspathEntry>();
        //                    this.cxfLibraryLocation = cxfInstallgetCxfRuntimeLocation();
        //                    this.cxfLibraryVersion = getCxfRuntimeVersion();
        //                    this.cxfLibraryEdition = getCxfRuntimeEdition();
        //                }

        if (classpathEntries.size() == 0) {
            File cxfLibDirectory = getCXFLibraryDirectory();
            if (cxfLibDirectory.exists() && cxfLibDirectory.isDirectory()) {
                String[] files = cxfLibDirectory.list();
                for (int i = 0; i < files.length; i++) {
                    File file = new File(cxfLibDirectory.getPath() + File.separator + files[i]);
                    String fileName = file.getName();
                    if (fileName.indexOf(".") != -1
                            && fileName.substring(fileName.lastIndexOf("."), fileName.length()).equals(
                            ".jar")) {
                        classpathEntries.add(JavaCore.newLibraryEntry(new Path(file.getAbsolutePath()), null,
                                new Path("/")));
                    }
                }
            }
        }
        return classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]);
    }

    public boolean isValid() {
        if (cxfLibraryLocation.length() > 0) {
            File cxfLibDirectory = getCXFLibraryDirectory();
            return cxfLibDirectory.exists() && cxfLibDirectory.isDirectory();
        }
        return false;
    }

    public String getDescription() {
        return  MessageFormat.format(CXFCoreMessages.CXF_CONTAINER_LIBRARY, cxfLibraryType,
                cxfLibraryVersion);
    }

    public int getKind() {
        return K_APPLICATION;
    }

    public IPath getPath() {
        return path;
    }

    private CXFInstall getCxfInstall() {
        String installed = CXFCorePlugin.getDefault().getCXFRuntimeVersion(project);
        CXFContext context = CXFCorePlugin.getDefault().getJava2WSContext();
        CXFInstall cxfInstall = context.getInstallations().get(installed);
        return cxfInstall;
    }

    private String getCxfRuntimeLocation() {
        CXFInstall cxfInstall = getCxfInstall();
        if (cxfInstall != null) {
            return cxfInstall.getLocation();
        }

        return CXFCorePlugin.getDefault().getJava2WSContext().getDefaultRuntimeLocation();
    }

    private String getCxfRuntimeVersion() {
        CXFInstall cxfInstall = getCxfInstall();
        if (cxfInstall != null) {
            return cxfInstall.getVersion();
        }

        return CXFCorePlugin.getDefault().getJava2WSContext().getDefaultRuntimeVersion();
    }

    private String getCxfRuntimeEdition() {
        CXFInstall cxfInstall = getCxfInstall();
        if (cxfInstall != null) {
            return cxfInstall.getType();
        }

        return CXFCorePlugin.getDefault().getJava2WSContext().getDefaultRuntimeType();
    }

    private File getCXFLibraryDirectory() {
        IPath cxfLibPath = new Path(cxfLibraryLocation);
        if (!cxfLibPath.hasTrailingSeparator()) {
            cxfLibPath = cxfLibPath.addTrailingSeparator();
        }
        cxfLibPath = cxfLibPath.append("lib"); //$NON-NLS-1$

        File cxfLibDirectory = new File(cxfLibPath.toOSString());
        return cxfLibDirectory;
    }
}
