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
import java.net.URL;
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
import org.eclipse.jst.j2ee.internal.earcreation.EARNatureRuntime;
import org.eclipse.jst.j2ee.internal.earcreation.IEARNatureConstants;
import org.eclipse.jst.j2ee.internal.project.IWebNatureConstants;
import org.eclipse.jst.j2ee.internal.web.operations.J2EEWebNatureRuntime;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.StructureEdit;
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

	private ClasspathUtils() {
	}

	public static ClasspathUtils getInstance() {
		if (instance_ == null)
			instance_ = new ClasspathUtils();
		return instance_;
	}

	public String getClasspathString(IProject project) {
		return getClasspathString(project, J2EEUtils.getFirstWebModuleName(project));
	}
	
	public String getClasspathString(IProject project, String module) {
		StringBuffer classpath = new StringBuffer();
		String[] classpathEntries = getClasspath(project, false);
		String resourceLocation = null;
		
		// TODO: workaround for 90515 and 90560
		try {
	
			IVirtualComponent component = ComponentCore.createComponent(project, module);
			if (component != null) {
				
				IFolder webModuleClasses = null;
				StructureEdit mc = null;
				try {
					mc = StructureEdit.getStructureEditForRead(project);
					WorkbenchComponent[] wbcs = mc.getWorkbenchModules();
					if (wbcs.length!=0) {
						IFolder  webModuleServerRoot = StructureEdit.getOutputContainerRoot(wbcs[0]);
						webModuleClasses  = webModuleServerRoot.getFolder("WEB-INF").getFolder("classes");
						resourceLocation = webModuleClasses.getLocation().toOSString();
						classpath.append(resourceLocation);
						classpath.append(";"); //$NON-NLS-1$
					}
				}
				finally{
					if (mc!=null)
						mc.dispose();			
				}
				
				IVirtualFolder webInfLib = component.getFolder(new Path(
						"/WEB-INF/lib"));
				if (webInfLib != null) {
					IVirtualResource[] resources = webInfLib.members();
					IResource aResource = null;
					
					for (int i = 0; i < resources.length; i++) {
						aResource = resources[i].getUnderlyingResource();
						resourceLocation = aResource.getLocation().toOSString();

						System.out.println(resourceLocation);
						classpath.append(resourceLocation);
						classpath.append(";"); //$NON-NLS-1$
					}
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// end of workaround
		
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

	public URL[] getClasspathURL(IProject project) {
		String[] classpathEntries = getClasspath(project, false);
		Vector classpathVector = new Vector();
		for (int i = 0; i < classpathEntries.length; i++) {
			try {
				File file = new File(classpathEntries[i]);
				//        System.out.println("adding project CP["+i+"] = "+file);
				URL url = file.toURL();
				if (!classpathVector.contains(url))
					classpathVector.add(url);
			} catch (Throwable t) {
			}
		}
		addLibClassspath(classpathVector);
		// add the plugin lib directory jars to classpath
		URL[] classpath = new URL[classpathVector.size()];
		classpathVector.copyInto(classpath);
		return classpath;
	}

	private void addLibClassspath(Vector classpathVector) {
	}

	private String[] getClasspath(IProject project, boolean isDependent) {
		try {
			String[] classpath;
			EARNatureRuntime earProject = castToEARProject(project);
			if (earProject != null)
				return getClasspathForEARProject(earProject);
			else if (project.hasNature(IWebNatureConstants.J2EE_NATURE_ID))
				return getClasspathForWebProject(
					(J2EEWebNatureRuntime) project.getNature(
						IWebNatureConstants.J2EE_NATURE_ID),
					isDependent);
			else if (project.hasNature(JavaCore.NATURE_ID))
				return getClasspathForJavaProject(
					(IJavaProject) project.getNature(JavaCore.NATURE_ID));
			else
				return new String[0];
		} catch (CoreException ce) {
			return new String[0];
		}
	}

	private EARNatureRuntime castToEARProject(IProject project) {
		try {
			String[] earNatures = IEARNatureConstants.NATURE_IDS;
			for (int i = 0; i < earNatures.length; i++) {
				if (project.hasNature(earNatures[i]))
					return (EARNatureRuntime) project.getNature(earNatures[i]);
			}
			return null;
		} catch (CoreException ce) {
			return null;
		}
	}

	private String[] getClasspathForEARProject(EARNatureRuntime earProject) {
		IPath earPath =
			earProject.getProject().getLocation().addTrailingSeparator();
		return getDirectoryJarFiles(earPath);
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

	private String[] getClasspathForWebProject(
		J2EEWebNatureRuntime webProject,
		boolean isDependent) {
		String[] webinfClasses = getWEBINFClasses(webProject);
		String[] webinfLib;
		String[] javaClasspath;
		if (isDependent) {
			webinfLib = new String[0];
			javaClasspath = new String[0];
		} else {
			webinfLib = getWEBINFLib(webProject);
			javaClasspath =
				getClasspathForJavaProject(webProject.getJ2EEJavaProject());
		}
		String[] webClasspath =
			new String[webinfClasses.length
				+ webinfLib.length
				+ javaClasspath.length];
		int index = 0;
		for (int i = 0; i < webinfClasses.length; i++) {
			webClasspath[index] = webinfClasses[i];
			index++;
		}
		for (int i = 0; i < webinfLib.length; i++) {
			webClasspath[index] = webinfLib[i];
			index++;
		}
		for (int i = 0; i < javaClasspath.length; i++) {
			webClasspath[index] = javaClasspath[i];
			index++;
		}
		return webClasspath;
	}

	private String[] getWEBINFClasses(J2EEWebNatureRuntime webProject) {
		IPath webinf =
			webProject
				.getProject()
				.getLocation()
				.addTrailingSeparator()
				.append(
				webProject.getWEBINFPath());
		IPath classes =
			((IPath) webinf.clone()).addTrailingSeparator().append(DIR_CLASSES);
		return new String[] { path2String(classes)};
	}

	private String[] getWEBINFLib(J2EEWebNatureRuntime webProject) {
		IPath webinf =
			webProject
				.getProject()
				.getLocation()
				.addTrailingSeparator()
				.append(
				webProject.getWEBINFPath());
		IPath lib =
			((IPath) webinf.clone())
				.addTrailingSeparator()
				.append(DIR_LIB)
				.addTrailingSeparator();
		return getDirectoryJarFiles(lib);
	}

	private String[] getClasspathForJavaProject(IJavaProject javaProject) {
		String[] javaBuildPath;
		try {
			IClasspathEntry[] buildPath =
				javaProject.getResolvedClasspath(true);
			Vector v = new Vector();
			for (int i = 0; i < buildPath.length; i++) {
				String[] buildPathString =
					classpathEntry2String(
						buildPath[i],
						javaProject.getProject());
				for (int j = 0; j < buildPathString.length; j++) {
					v.add(buildPathString[j]);
				}
			}
			javaBuildPath = new String[v.size()];
			v.copyInto(javaBuildPath);
		} catch (JavaModelException jme) {
			javaBuildPath = new String[0];
		}

		String[] earClasspath;
		Vector utilityJarsVector = new Vector();
		IProject project = javaProject.getProject();
		IProject[] referencingProjects = project.getReferencingProjects();
		for (int i = 0; i < referencingProjects.length; i++) {
			EARNatureRuntime earProject =
				castToEARProject(referencingProjects[i]);
			if (earProject != null) {
				String[] utilityJars = getClasspathForEARProject(earProject);
				for (int j = 0; j < utilityJars.length; j++) {
					utilityJarsVector.add(utilityJars[j]);
				}
			}
		}
		earClasspath = new String[utilityJarsVector.size()];
		utilityJarsVector.copyInto(earClasspath);

		String[] javaClasspath =
			new String[javaBuildPath.length + earClasspath.length];
		int index = 0;
		for (int i = 0; i < javaBuildPath.length; i++) {
			javaClasspath[index] = javaBuildPath[i];
			index++;
		}
		for (int i = 0; i < earClasspath.length; i++) {
			javaClasspath[index] = earClasspath[i];
			index++;
		}
		return javaClasspath;
	}

	private String[] classpathEntry2String(
		IClasspathEntry entry,
		IProject project) {
		switch (entry.getEntryKind()) {
			case IClasspathEntry.CPE_LIBRARY :
				return new String[] { path2String(entry.getPath())};
			case IClasspathEntry.CPE_PROJECT :
				return getClasspath(
					ResourcesPlugin.getWorkspace().getRoot().getProject(
						entry.getPath().lastSegment()),
					true);
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
				return classpathEntry2String(
					JavaCore.getResolvedClasspathEntry(entry),
					project);
			default :
				return new String[] { path2String(entry.getPath())};
		}
	}

	private String path2String(IPath path) {
		return path.toOSString();
	}
}
