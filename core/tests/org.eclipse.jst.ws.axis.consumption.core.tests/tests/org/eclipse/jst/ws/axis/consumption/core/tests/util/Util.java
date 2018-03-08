/*******************************************************************************
 * Copyright (c) 2006, 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060317   127456 cbrealey@ca.ibm.com - Chris Brealey
 * 20060711   147862 cbrealey@ca.ibm.com - Chris Brealey
 * 20060711   147864 cbrealey@ca.ibm.com - Chris Brealey
 * 20061211   162288 makandre@ca.ibm.com - Andrew Mak, workspace paths with spaces break Java Editor Launch
 *******************************************************************************/

package org.eclipse.jst.ws.axis.consumption.core.tests.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.common.uriresolver.internal.util.URIEncoder;
import org.osgi.framework.Bundle;

public class Util
{
	/**
	 * Initializes this testsuite.
	 */
	public static void init ()
	{
		String workspaceOSPath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		System.out.println("Workspace = ["+workspaceOSPath+"]");
	}
	
	/**
	 * Create a Java project if it doesn't already exist.
	 * @param name The name of the project to create.
	 * @throws CoreException If the project could not be created.
	 */
	public static IJavaProject createJavaProject ( String name )
	throws CoreException
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject(name);
		if (!project.exists())
		{
			System.out.println("Creating project ["+name+"]");
			project.create(null);
			project.open(null);
	        IProjectDescription desc = project.getDescription();
	        desc.setNatureIds(new String[] {JavaCore.NATURE_ID});
	        ICommand cmd = desc.newCommand();
	        cmd.setBuilderName(JavaCore.BUILDER_ID);
	        desc.setBuildSpec(new ICommand[] {cmd});
	        project.setDescription(desc,null);
		}
		System.out.println("Creating Java project ["+name+"]");
		IJavaProject javaProject = JavaCore.create(project);
		javaProject.open(null);
		return javaProject;
	}
	
	/**
	 * Adds Axis jars and the JRE to the classpath of the given project.
	 * @param javaProject The project to add axis.jar to.
	 * @throws JavaModelException If the jar could not be added.
	 */
	public static void addRequiredJarsToJavaProject ( IJavaProject javaProject )
	throws CoreException
	{
		// Get the current classpath as a list.
		IClasspathEntry[] classpath = javaProject.getRawClasspath();
		List list = new LinkedList(java.util.Arrays.asList(classpath));
		
		// Add the JRE to the list.
		IClasspathEntry con = JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER"));
		list.add(con);
		
		// Add the Axis jars to the list.
		String[] jars = new String[] {
				"org.apache.axis","axis.jar",
				"org.apache.axis","commons-discovery-0.2.jar",
				"org.apache.commons_logging","commons-logging-1.0.4.jar",
				"org.apache.axis","jaxrpc.jar",
				"org.apache.jakarta_log4j","log4j-1.2.8.jar",
				"org.apache.axis","saaj.jar",
				"org.apache.axis","wsdl4j-1.5.1.jar"
		};
		
		for (int i=0; i<jars.length; i+=2)
		{
			IPath jar = getAxisPluginJarPath(jars[i],jars[i+1]);
			System.out.println("Adding jar ["+jar.toString()+"]");
			IClasspathEntry jarEntry = JavaCore.newLibraryEntry(jar,null,null);
			list.add(jarEntry);
		}
		
		// Update the current classpath from the list.
		IClasspathEntry[] newClasspath = (IClasspathEntry[])list.toArray(new IClasspathEntry[0]);
		javaProject.setRawClasspath(newClasspath,null);
	}
	
	/**
	 * Returns the filesystem path to the named jar in the Axis plugin.
	 * @return The filesystem path to the named jar in the Axis plugin.
	 * @throws CoreException If the path could not be computed.
	 */
	public static IPath getAxisPluginJarPath ( String pluginName, String jarName )
	throws CoreException
	{
		Bundle bundle = Platform.getBundle(pluginName);
		if (bundle == null)
		{
			throw new CoreException(new Status(IStatus.ERROR,"",0,"Unable to locate plugin org.apache.axis",null));
		}
		URL axisURL = FileLocator.find(bundle,new Path("lib/"+jarName),null);
		try
		{
			URL localAxisURL = FileLocator.toFileURL(axisURL);
			File file = new File(new URI(URIEncoder.encode(localAxisURL.toString(), "UTF-8")));
			return new Path(file.toString());
		}
		catch (UnsupportedEncodingException uee) {
			throw new CoreException(new Status(IStatus.ERROR,"",0,"Unable to encode jar path for plugin org.apache.axis",uee));			
		}
		catch (IOException ioe)
		{
			throw new CoreException(new Status(IStatus.ERROR,"",0,"Unable to locate plugin org.apache.axis",ioe));
		}
		catch (URISyntaxException use)
		{
			throw new CoreException(new Status(IStatus.ERROR,"",0,"Unable to locate plugin org.apache.axis",use));
		}
	}
	
	/**
	 * Copies examples to the given Java project.
	 * @param javaProject The project to copy the examples to.
	 */
	public static void copyExamplesToJavaProject ( IJavaProject javaProject, Filter filter )
	throws CoreException
	{
		IProject project = javaProject.getProject();
		String sourcePath = project.getName();
		copyExample("org.eclipse.jst.ws.axis.consumption.core.tests","data/axisSource1",sourcePath,filter);
		copyExample("org.eclipse.jst.ws.axis.consumption.core.tests","data/axisSource2",sourcePath,filter);
		project.build(IncrementalProjectBuilder.FULL_BUILD,null);
		try
		{
			System.out.println("Waiting for build to complete");
			Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD,null);
		}
		catch (Throwable t)
		{
			//Do nothing.
		}
	}
	
	/**
	 * Recursively copies files from a plugin to the workspace.
	 * @param sourcePlugin The plugin to copy from.
	 * @param sourceRoot The plugin-related path to copy from.
	 * @param targetWorkspacePath The workspace path to copy to.
	 */
	public static void copyExample ( String sourcePlugin, String sourceRoot, String targetWorkspacePath, Filter filter )
	throws CoreException
	{
		Bundle bundle = Platform.getBundle(sourcePlugin);
		if (bundle == null)
		{
			throw new CoreException(new Status(IStatus.ERROR,"",0,"Unable to locate plugin "+sourcePlugin,null));
		}
		Enumeration e = bundle.getEntryPaths(sourceRoot);
		while (e.hasMoreElements())
		{
			String path = e.nextElement().toString();
			System.out.println("Bundle path = ["+path+"]");
			copyExampleFile(bundle,path,new Path(sourceRoot).segmentCount(),targetWorkspacePath,filter);
		}
	}
	
	public static void copyExampleFile ( Bundle bundle, String path, int offset, String targetWorkspacePath, Filter filter )
	throws CoreException
	{
		IPath reducedPath = new Path(path).removeFirstSegments(offset);
		IPath targetPath = new Path(targetWorkspacePath).append(reducedPath);
		if (path.endsWith("/"))
		{
			System.out.println("Folder = ["+path+"] Target = ["+targetPath+"]");
			IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(targetPath);
			if (!folder.exists())
			{
				folder.create(true,true,null);
			}
			Enumeration e = bundle.getEntryPaths(path);
			while (e.hasMoreElements())
			{
				String subpath = e.nextElement().toString();
				copyExampleFile(bundle,subpath,offset,targetWorkspacePath,filter);
			}
		}
		else
		{
			System.out.println("File = ["+path+"] Target = ["+targetPath+"]");
			if (filter.accept(path))
			{
				System.out.println("Copied ["+path+"]");
				URL url = bundle.getEntry(path);
				copyFile(url,targetPath);
			}
		}
	}
	
	public static void copyFile ( URL url, IPath targetPath )
	throws CoreException
	{
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(targetPath);
		try
		{
			InputStream stream = url.openStream();
			file.create(stream,true,null);
		}
		catch (IOException ioe)
		{
			throw new CoreException(new Status(IStatus.ERROR,"",0,"IOException copying file",ioe));
		}
	}
}
