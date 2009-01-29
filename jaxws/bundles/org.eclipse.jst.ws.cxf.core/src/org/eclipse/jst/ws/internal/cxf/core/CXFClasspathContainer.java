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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class CXFClasspathContainer implements IClasspathContainer {

	private IPath path;
	private List<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>();
	private String cxfLibraryVersion;
	
	public CXFClasspathContainer(IPath path, IJavaProject javaProject) {
		this.path = path;
		cxfLibraryVersion = CXFCorePlugin.getDefault().getJava2WSContext().getCxfRuntimeVersion();
	}
	
	public IClasspathEntry[] getClasspathEntries() {
		if (classpathEntries.size() == 0) {
	        IPath cxfLibPath = new Path(CXFCorePlugin.getDefault().getJava2WSContext()
	                .getCxfRuntimeLocation());
	        if (!cxfLibPath.hasTrailingSeparator()) {
	            cxfLibPath = cxfLibPath.addTrailingSeparator();
	        }
	        cxfLibPath = cxfLibPath.append("lib"); //$NON-NLS-1$
	        
	        File cxfLibDirectory = new File(cxfLibPath.toOSString());
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

	public String getDescription() {
		return  MessageFormat.format(CXFCoreMessages.CXF_CONTAINER_LIBRARY, cxfLibraryVersion);
	}

	public int getKind() {
		return K_APPLICATION;
	}

	public IPath getPath() {
		return path;
	}

}
