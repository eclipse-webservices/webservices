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
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;

public class ClasspathUtils {

	//	Copyright
	public static final String copyright = "(c) Copyright IBM Corporation 2003."; //$NON-NLS-1$

	private static ClasspathUtils instance_; //$NON-NLS-1$
	private static String DOT_JAR = ".jar"; //$NON-NLS-1$
	private static String JAR = "jar"; //$NON-NLS-1$
	private static String WEBINF_LIB = "/WEB-INF/lib"; //$NON-NLS-1$
	
	// workaround for Axis-2146 - lower case list of JARs that may include javax.activation.DataHandler
	private static String[] JARLIST = new String[] {
		  "activation.jar",
		  "geronimo-spec-activation-1.0.2-rc3.jar",
		  "geronimo-spec-j2ee-1.4-rc3.jar",
		  "geronimo-spec-activation-1.0.2-rc4.jar",
		  "geronimo-spec-j2ee-1.4-rc4.jar"
	  };

	private ClasspathUtils() {
	}

	public static ClasspathUtils getInstance() {
		if (instance_ == null)
			instance_ = new ClasspathUtils();
		return instance_;
	}
	
	public String getClasspathString(IProject project) {
		StringBuffer classpath = new StringBuffer();
		String[] classpathEntries = getClasspath(project, false);

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

		
	private String[] getClasspath(IProject project, boolean isDependent) {
		String[] moduleClasspath = new String[0];
		ArrayList projectClasspath = new ArrayList();
		boolean needJavaClasspath = false;
		
		IVirtualComponent comp = ComponentCore.createComponent(project);
		if (comp != null) {
			// get the module's classpath
			
			if (J2EEUtils.isEARComponent(comp)) {
				moduleClasspath = getClasspathForEARProject(project, comp.getName());
//				 add module classpath to project classpath
				for (int j = 0; j < moduleClasspath.length; j++) {
					projectClasspath.add(moduleClasspath[j]);
				}
			} else if (J2EEUtils.isWebComponent(comp) || J2EEUtils.isJavaComponent(comp)) {
				needJavaClasspath = true;
				
				IContainer outputContainer = null;
				IResource fragmentRoot = null;
				IPackageFragmentRoot[] pkgFragmentRoot = ResourceUtils.getJavaPackageFragmentRoots(project);
				ArrayList webModuleClasspath = new ArrayList();
				try {
					for (int i = 0; i < pkgFragmentRoot.length; i++) {
						fragmentRoot = pkgFragmentRoot[i].getCorrespondingResource();
						if (fragmentRoot != null
								&& (fragmentRoot.getProject().equals(project))
								&& (fragmentRoot.getType() != IResource.FILE)) {					
							outputContainer = J2EEProjectUtilities.getOutputContainer(project, pkgFragmentRoot[i]);
							if (outputContainer != null) { 
								webModuleClasspath.add(outputContainer.getLocation().toOSString());
							}
						}
					}
				} catch (JavaModelException e) {
				}
				
				// add Web module classpath to project classpath
				Iterator iter = webModuleClasspath.iterator();
				while (iter.hasNext()) {
					projectClasspath.add((String) iter.next());
				}		
			}
			
			if (!isDependent) {
				if (J2EEUtils.isWebComponent(comp)) {
					needJavaClasspath = true;
					moduleClasspath = getWEBINFLib(project);
					for (int j = 0; j < moduleClasspath.length; j++) {
						projectClasspath.add(moduleClasspath[j]);
					}
				}
			}
			
		} else {
			needJavaClasspath = true;
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
		
		return (String[]) projectClasspath.toArray(new String[projectClasspath.size()]);
	}

	// Need to get all modules in the project. If there's a EAR module, get the utility JARs 
	private String[] getUtilityJarClasspath(IProject project) {
		String[] moduleClasspath = new String[0];
		ArrayList utilityJarsClasspath = new ArrayList();
		
		String module;
		IVirtualComponent comp = ComponentCore.createComponent(project);
		module = comp.getName();
		if (J2EEUtils.isEARComponent(comp)) {
			moduleClasspath = getClasspathForEARProject(project, module);
			for (int j = 0; j < moduleClasspath.length; j++) {
				utilityJarsClasspath.add(moduleClasspath[j]);
			}
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

	private String[] getWEBINFLib(IProject project) {
		String[] webinfLibJars = new String[0];
		ArrayList anArrayList = new ArrayList();
		try {			
					IVirtualComponent component = ComponentCore.createComponent(project);
					if (component != null) {
						
						IVirtualFolder webInfLib = component.getRootFolder().getFolder(new Path(
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
					if (!excludeJar(buildPathString[j]))
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

	// workaround for Axis-2146 - exclude JARs which may include javax.activation.DataHandler 
	// from the classpath passed to Axis emitter
	
	private boolean excludeJar(String buildPathString) {
		
		for (int i=0; i<JARLIST.length; i++) {
			if (buildPathString.toLowerCase().endsWith(JARLIST[i])) {
				return true;
			}
		}
		return false;
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
						entry.getPath().lastSegment()), true);
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
