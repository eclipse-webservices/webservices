/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.util;


import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.StructureEdit;
import org.eclipse.wst.common.componentcore.internal.WorkbenchComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;

public class ClasspathUtils {

	//	Copyright
	public static final String copyright = "(c) Copyright IBM Corporation 2003."; //$NON-NLS-1$

	private static ClasspathUtils instance_; //$NON-NLS-1$
	private static String DIR_CLASSES = "classes"; //$NON-NLS-1$
	private static String DIR_LIB = "lib"; //$NON-NLS-1$
	private static String DOT_JAR = ".jar"; //$NON-NLS-1$
	private static String JAR = "jar"; //$NON-NLS-1$
	private static String WEBINF_LIB = "/WEB-INF/lib"; //$NON-NLS-1$
	private static String WEBINF = "WEB-INF"; //$NON-NLS-1$

	private ClasspathUtils() {
	}

	public static ClasspathUtils getInstance() {
		if (instance_ == null)
			instance_ = new ClasspathUtils();
		return instance_;
	}
	
	public String getClasspathString(IProject project, String module) {
		StringBuffer classpath = new StringBuffer();
		String[] classpathEntries = getClasspath(project, false, module);

		Vector classpathVector = new Vector();
		for (int i = 0; i < classpathEntries.length; i++) {
			if (!classpathVector.contains(classpathEntries[i])) {
				classpathVector.add(classpathEntries[i]);
				classpath.append(classpathEntries[i]);
				classpath.append(";"); //$NON-NLS-1$
			}
		}
		return classpath.toString();
	}

		
	private String[] getClasspath(IProject project, boolean isDependent, String inputModule) {
//		 inputModule is valid only if it's not a dependent project
		String[] moduleClasspath = new String[0];
		ArrayList projectClasspath = new ArrayList();
		boolean needJavaClasspath = false;
		StructureEdit mc = null;
		IFolder webModuleServerRoot = null;
		String resourceLocation = null;
		IFolder webModuleClasses = null;
		
		try {
			String module;
			mc = StructureEdit.getStructureEditForRead(project);
			WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
			for (int i = 0; i < wbcs.length; i++) {
				module = wbcs[i].getName();
				// get the module's classpath
				if (J2EEUtils.isEARComponent(project, module)) {
					moduleClasspath = getClasspathForEARProject(project, module);
				} else if (J2EEUtils.isWebComponent(project, module)) {
					webModuleServerRoot = StructureEdit.getOutputContainerRoot(wbcs[i]);
					if (webModuleServerRoot != null) { 
						webModuleClasses = webModuleServerRoot.getFolder(WEBINF).getFolder(DIR_CLASSES);
						if (webModuleClasses != null)
							moduleClasspath = new String[] { webModuleClasses.getLocation().toOSString() };
					}
				} else if (J2EEUtils.isJavaComponent(project, module)) {
					needJavaClasspath = true;
					webModuleServerRoot = StructureEdit.getOutputContainerRoot(wbcs[i]);
					if (webModuleServerRoot != null) { 
						moduleClasspath = new String[] { webModuleServerRoot.getLocation().toOSString() };
					}
				}
				
				// add module classpath to project classpath
				for (int j = 0; j < moduleClasspath.length; j++) {
					projectClasspath.add(moduleClasspath[j]);
				}
			}
			if (!isDependent) {
				if (J2EEUtils.isWebComponent(project, inputModule)) {
					needJavaClasspath = true;
					moduleClasspath = getWEBINFLib(project, inputModule);
					for (int j = 0; j < moduleClasspath.length; j++) {
						projectClasspath.add(moduleClasspath[j]);
					}
				}
			}
			
			// If there are Web or Java module in the project, get the project's Java classpath
			if (needJavaClasspath) {
				String[] javaClasspath;
				try {
					IJavaProject javaProj = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
					if (javaProj != null) {
						javaClasspath = getClasspathForJavaProject(javaProj);
						for (int j = 0; j < javaClasspath.length; j++) {
							projectClasspath.add(javaClasspath[j]);
						}
					}
				} catch (CoreException e) {
					// not able to get Java classpath, just ignore
				}	
			}
		} finally {
			if (mc != null)
				mc.dispose();
		}
		return (String[]) projectClasspath.toArray(new String[projectClasspath.size()]);
	}

	// Need to get all modules in the project. If there's a EAR module, get the utility JARs 
	private String[] getUtilityJarClasspath(IProject project) {
		String[] moduleClasspath = new String[0];
		ArrayList utilityJarsClasspath = new ArrayList();
		StructureEdit mc = null;
		try {
			String module;
			mc = StructureEdit.getStructureEditForRead(project);
			WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
			for (int i = 0; i < wbcs.length; i++) {
				module = wbcs[i].getName();
				if (J2EEUtils.isEARComponent(project, module)) {
					moduleClasspath = getClasspathForEARProject(project, module);
					for (int j = 0; j < moduleClasspath.length; j++) {
						utilityJarsClasspath.add(moduleClasspath[j]);
					}
				}
			}

		} finally {
			if (mc != null)
				mc.dispose();
		}
		return (String[]) utilityJarsClasspath.toArray(new String[utilityJarsClasspath.size()]);
	}
	
	private String[] getClasspathForEARProject(IProject project, String module) {
		IPath projectPath =
			project.getProject().getLocation().addTrailingSeparator().append(module).addTrailingSeparator();
		return getDirectoryJarFiles(projectPath);
	}

	private String[] getDirectoryJarFiles(IPath iPath) {
		File dir = iPath.toFile();
		Vector jarsVector = new Vector();
		if (dir.exists() && dir.isDirectory()) {
			String[] filenames = dir.list();
			for (int i = 0; i < filenames.length; i++) {
				if (filenames[i].endsWith(DOT_JAR))
					jarsVector.add(path2String(iPath) + filenames[i]);
			}
		}
		String[] jars = new String[jarsVector.size()];
		jarsVector.copyInto(jars);
		return jars;
	}

	private String[] getWEBINFLib(IProject project, String module) {
		String[] webinfLibJars = new String[0];
		ArrayList anArrayList = new ArrayList();
		try {
			String resourceLocation = null;
					IVirtualComponent component = ComponentCore.createComponent(project, module);
					if (component != null) {
						
						IVirtualFolder webInfLib = component.getFolder(new Path(
								WEBINF_LIB));
						if (webInfLib != null) {
							IVirtualResource[] resources = webInfLib.members();
							IResource aResource = null;
							for (int i = 0; i < resources.length; i++) {
								aResource = resources[i].getUnderlyingResource();
								if (JAR.equalsIgnoreCase(aResource.getFileExtension()))
									anArrayList.add( aResource.getLocation().toOSString());
							}
							if (anArrayList.size() != 0)
								webinfLibJars = (String[]) anArrayList.toArray(new String[anArrayList.size()]);
							}
					}
				} catch (CoreException e) {
				}
		return webinfLibJars;
	}

	private String[] getClasspathForJavaProject(IJavaProject javaProject) {
		ArrayList projectClasspath = new ArrayList();
		try {
			IClasspathEntry[] buildPath =
				javaProject.getResolvedClasspath(true);
			for (int i = 0; i < buildPath.length; i++) {
				String[] buildPathString =
					classpathEntry2String(
						buildPath[i],
						javaProject.getProject());
				for (int j = 0; j < buildPathString.length; j++) {
					projectClasspath.add(buildPathString[j]);
				}
			}
		} catch (JavaModelException jme) {
		}

		String[] utilityJarsClasspath;
		IProject project = javaProject.getProject();
		IProject[] referencingProjects = project.getReferencingProjects();
		for (int i = 0; i < referencingProjects.length; i++) {
			utilityJarsClasspath = getUtilityJarClasspath(referencingProjects[i]);
			for (int j = 0; j < utilityJarsClasspath.length; j++) {
				projectClasspath.add(utilityJarsClasspath[j]);
			}
		}

		return (String[]) projectClasspath.toArray(new String[projectClasspath.size()]);
	}

	private String[] classpathEntry2String(
		IClasspathEntry entry,
		IProject project) 
	{
		switch (entry.getEntryKind()) {
			case IClasspathEntry.CPE_LIBRARY :
			{
				return new String[] { path2String(entry.getPath())};	
			}
			case IClasspathEntry.CPE_PROJECT :
			{			
				return getClasspath(
					ResourcesPlugin.getWorkspace().getRoot().getProject(
						entry.getPath().lastSegment()), true, "");
			}
			case IClasspathEntry.CPE_SOURCE :
				{
					IPath path = entry.getPath();
					if (path.segment(0).equals(project.getName()))
						path = path.removeFirstSegments(1);
					return new String[] {
						path2String(
							project
								.getLocation()
								.addTrailingSeparator()
								.append(
								path))};
				}
			case IClasspathEntry.CPE_VARIABLE :
			{
				return classpathEntry2String(
					JavaCore.getResolvedClasspathEntry(entry),
					project);
			}
			default :
			{
				return new String[] { path2String(entry.getPath())};
			}
		}
	}

	private String path2String(IPath path) {
		return path.toOSString();
	}
}
