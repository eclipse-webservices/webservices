/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.testutils.project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.ws.jaxws.testutils.jobs.JobUtils;
import org.eclipse.jst.ws.jaxws.testutils.threading.TestContext;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

public class TestProjectsUtils
{
	/**
	 * Reads content of text file and returns it as String.
	 * 
	 * @param class context
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static String readSource(Class<?> classContext, String filePath) throws IOException
	{
		InputStreamReader isr = new InputStreamReader(classContext.getResourceAsStream(filePath));
		try
		{
			StringBuffer sb = new StringBuffer();
			char[] buff = new char[512];

			int len = 0;
			while ((len = isr.read(buff)) > 0)
			{
				sb.append(String.valueOf(buff, 0, len));
			}

			return sb.toString();

		} finally
		{
			isr.close();
		}
	}

	/**
	 * Starts an indexer job. This method is useful to make sure that all workbench refresh/build processes are over 
	 * @throws JavaModelException
	 */
	public static void waitForIndexer() throws JavaModelException
	{
		new SearchEngine().searchAllTypeNames(null, SearchPattern.R_EXACT_MATCH, 
											  null, SearchPattern.R_CASE_SENSITIVE, 
											  IJavaSearchConstants.CLASS, 
											  SearchEngine.createJavaSearchScope(new IJavaElement[0]), new TypeNameRequestor()
											  {
												@SuppressWarnings("unused")
												public void acceptClass(char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames,
																				String path)
													{
													}
												@SuppressWarnings("unused")
												public void acceptInterface(char[] packageName, char[] simpleTypeName, char[][] enclosingTypeNames,
																				String path)
													{
													}
											  },
											  IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, null);
	}

	
	public static IProject createJavaProject(String projectName) throws CoreException
	{
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		project.create(null);
		project.open(null);
		JobUtils.waitForIndexer();
		
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, null);
		IJavaProject javaP = JavaCore.create(project);
		addToClasspath(javaP, JavaRuntime.getDefaultJREContainerEntry());

		return project;
	}
	
	private static void addToClasspath(final IJavaProject javaProject, final IClasspathEntry cpEntry) throws JavaModelException
	{
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = cpEntry;
		javaProject.setRawClasspath(newEntries, null);
	}

	public static IPackageFragmentRoot getSourceFolder(IProject project, String folderName) throws JavaModelException
	{
		IJavaProject javaProject = JavaCore.create(project);
		for(IPackageFragmentRoot root : javaProject.getPackageFragmentRoots())
		{
			if(root.getElementName().equals(folderName))
			{
				return root;
			}
		}
		
		return null;
	}
	
	public static IPackageFragmentRoot createSourceFolder(IProject project, String folderName) throws CoreException
	{
		if(getSourceFolder(project, folderName) != null)
		{
			throw new IllegalStateException("Source folder already exists: " + folderName);
		}

		IFolder srcFolder = project.getFolder(folderName);
		srcFolder.create(true, true, null);
		
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry srcCpEntry = JavaCore.newSourceEntry(srcFolder.getFullPath().makeAbsolute());
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1]; 
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[newEntries.length - 1] = srcCpEntry;
		javaProject.setRawClasspath(newEntries, null);

		return getSourceFolder(project, folderName);
	}

	/**
	 * Imports a file into a project
	 * @param classContext the class which classloader is used to get the source resource 
	 * @param relativeSrcFileLocation source file location relative to the <code>classContext</code> parameter
	 * @param targetResource the target resource; 
	 * @param targetFileName the name of the file to be created
	 * @throws IOException
	 * @returns the file imported
	 */
	public static File importFileIntoProject(Class<?> classContext, String relativeSrcFileLocation, IContainer targetResource, String targetFileName) throws IOException
	{
		String src = TestProjectsUtils.readSource(classContext, relativeSrcFileLocation);
		File f = new File(targetResource.getLocation().toOSString() + "\\" + targetFileName);
		
		if(f.exists())
		{
			throw new IllegalStateException("File already exists: " + f.getAbsolutePath());
		}
		
		if(f.isDirectory())
		{
			throw new IllegalStateException(f.getAbsolutePath() + " is a directory");
		}
		if( f.createNewFile() )
		{
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			out.write(src);
			out.flush();
			out.close();
		}
		
		return f;
	}
	
	public static void deleteWorkspaceProjects() throws JavaModelException
	{
		final IProgressMonitor monitor = new NullProgressMonitor();
		JobUtils.waitForJobs();
		for(IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
		{
			try
			{
				project.delete(false, monitor);
			} catch (CoreException e)
			{
				(new Logger()).logError(e.getMessage());
			}
			JobUtils.waitForJobs();
		}
	}

	public static IProject createEjb3Project(final String projectName) throws CoreException
	{
		final IProject ejbProject = createProjectWithFacet(projectName, new String[]{"jst.java", "jst.ejb"}, new String[]{"5.0", "3.0"});
		final IJavaProject javaP = JavaCore.create(ejbProject);
		
		removeAllSourceFolders(javaP);

		// Set ejbModule folder (created during jst.ejb facet installation) as source folder
		final IFolder srcFolder = ejbProject.getFolder("ejbModule");
		final IClasspathEntry ejbModuleCpEntry = JavaCore.newSourceEntry(srcFolder.getFullPath().makeAbsolute());
		addToClasspath(JavaCore.create(ejbProject), ejbModuleCpEntry);
		
		return ejbProject;
	}
	
	private static void removeAllSourceFolders(final IJavaProject javaP) throws JavaModelException
	{
		final List<IClasspathEntry> newClasspath = new ArrayList<IClasspathEntry>();
		for(IClasspathEntry cpEntry : javaP.getRawClasspath())
		{
			if(cpEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE)
			{
				continue;
			}
			newClasspath.add(cpEntry);
		}
		javaP.setRawClasspath(newClasspath.toArray(new IClasspathEntry[newClasspath.size()]), new NullProgressMonitor());
	}
	
	private static IProject createProjectWithFacet(final String projectName, final String[] facetId, final String[] facetVersion) throws CoreException
	{
		assert facetId.length == facetVersion.length;
		final IProject[] result = new IProject[1];
		try
		{
			TestContext.run(new IRunnableWithProgress(){
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					try
					{
						result[0] = createJavaProject(projectName);
						final IFacetedProject facetedProject = ProjectFacetsManager.create(result[0], true, new NullProgressMonitor());
	
						for(int i = 0; i < facetId.length; i++)
						{
							final IProjectFacet facet = ProjectFacetsManager.getProjectFacet(facetId[i]);
							facetedProject.installProjectFacet(facet.getVersion(facetVersion[i]), null, monitor);
						}
						
					} catch (CoreException e)
					{
						throw new InvocationTargetException(e);
					}
				}}, true, new NullProgressMonitor(), PlatformUI.getWorkbench().getDisplay());
		} catch (InvocationTargetException e)
		{
			if(e.getCause() instanceof CoreException)
			{
				throw (CoreException)e.getCause();
			}
			throw new RuntimeException(e);
		} catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		
		return result[0];
	}

	/**
	 * Creates a dynamic web module project with "jst.web" facet ver. 2.5 installed
	 * @param projectName
	 * @throws CoreException 
	 */
	public static IProject createWeb25Project(final String projectName) throws CoreException
	{
		final IProject webProject = createProjectWithFacet(projectName, new String[]{"jst.java", "jst.web"}, new String[]{"5.0", "2.5"});
		final IJavaProject javaP = JavaCore.create(webProject);

		removeAllSourceFolders(javaP);

		return webProject;
	}
	
	/**
	 * Executes a workspace runnable outside the main thread
	 */
	public static void executeWorkspaceRunnable(final IWorkspaceRunnable runnable) throws CoreException
	{
		executeWorkspaceRunnable(runnable, new NullProgressMonitor());
	}
	
	private static void executeWorkspaceRunnable(final IWorkspaceRunnable runnable, final IProgressMonitor monitor) throws CoreException
	{
		if(Display.getCurrent() == null)
		{
			// Execute the runnable in the current (non-UI) thread
			workspace().run(runnable, monitor);
		}
		else
		{
			runInTestContext(runnable, monitor);
		}
	}
	
	private static void runInTestContext(final IWorkspaceRunnable runnable, final IProgressMonitor pm) throws CoreException
	{
		final IRunnableWithProgress textCtxRunnable = new IRunnableWithProgress()
		{
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
			{
				try
				{
					executeWorkspaceRunnable(runnable, monitor);
				} catch (CoreException e)
				{
					throw new InvocationTargetException(e);
				}
			}
		};
		try
		{
			TestContext.run(textCtxRunnable, true, pm, PlatformUI.getWorkbench().getDisplay());
		} catch (InvocationTargetException e)
		{
			if(e.getCause() instanceof CoreException)
			{
				throw (CoreException)e.getCause();
			}
			
			throw new IllegalStateException("Unexected exception thrown by runnable", e.getCause());
		} catch (InterruptedException e)
		{
			throw new IllegalStateException("Interruption is not supported");
		}
	}

	private static IWorkspace workspace()
	{
		return ResourcesPlugin.getWorkspace();
	}
	
}
