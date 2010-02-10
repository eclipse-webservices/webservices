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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.ws.jaxws.testutils.jobs.JobUtils;
import org.eclipse.jst.ws.jaxws.testutils.threading.TestContext;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * Wrapper utility for maintaining projects which participate in tests fixture. Resource modification operations are always performed outside the UI (main thread). If the caller thread is the UI thread, fork in {@link TestContext} is performed.
 * Resource change operations are performed within a {@link IWorkspaceRunnable} with a scheduling rule of the workspace root<br>
 * Executing resource changes outside the UI thread allows resource change events propagation event when the JUnit tests run in the UI thread. This is especially important for Web service DOM tests as the model is updated on resource changes   
 * 
 * @author Georgi Vachkov, Danail Branekov
 */
@SuppressWarnings("restriction")
public class TestProject
{
	public final static char CLASS_SEPARATOR = '#';
	
	private int idInMillis = 0;
	
	private IProject project;

	private IJavaProject javaProject;

	private IPackageFragmentRoot sourceFolder;

	public TestProject() throws CoreException
	{
		this("");
	}
	
	public TestProject(String name) throws CoreException
	{
		/*
		 * Calculate the name of the test project. The formula: JavaProj_ + <current_time_in_millis? + <idInMillis> is ussed. In this formula
		 * JavaProj_ is a string litteral, current_time_in_millis is obvious. idInMillis is an integer incremented each time a project name is
		 * calculated. This is necessary because the system timer has precision about 10 ms. Thus it's possible that two consecutive project creations
		 * give the same project name. For example the project is creted then the test executes very fast and a new project is created again. In the
		 * same time simply the id is not enough as a different test run might produce the same project name. Thus the combination tim_millis +
		 * idInMillis gives a unique project name
		 */
		final String testProjectName = "JavaProj_" + Long.toString(System.currentTimeMillis()) + "_" + idInMillis++ + name;
		project = workspace().getRoot().getProject(testProjectName);
		
		final IWorkspaceRunnable createProjectOperation = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				project.create(null);
				project.open(null);

				configureJavaProject();
			}
		};
		
		executeWorkspaceRunnable(createProjectOperation);
	}

	private void configureJavaProject() throws CoreException
	{
		javaProject = JavaCore.create(project);

		IFolder binFolder = createBinFolder();

		setJavaNature();
		javaProject.setRawClasspath(new IClasspathEntry[0], null);

		createOutputFolder(binFolder);
		addSystemLibraries();
	}

	public TestProject(final IProject project) throws CoreException
	{
		if (project == null)
		{
			throw new NullPointerException("project should not be null");
		}

		this.project = project;
		this.javaProject = JavaCore.create(project);
		this.sourceFolder = findSourceFolder();
	}

	private IPackageFragmentRoot findSourceFolder() throws JavaModelException
	{
		for (IPackageFragmentRoot pfr : javaProject.getAllPackageFragmentRoots())
		{
			if (pfr.getKind() == IPackageFragmentRoot.K_SOURCE)
			{
				return pfr;
			}
		}

		return null;
	}

	public IProject getProject()
	{
		return project;
	}

	public IJavaProject getJavaProject()
	{
		return javaProject;
	}

	public void addJar(String plugin, String jar) throws MalformedURLException, IOException, CoreException
	{
		Path result = findFileInPlugin(plugin, jar);
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		final IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaCore.newLibraryEntry(result, null, null);
		
		setClasspath(newEntries);
	}

	public IPackageFragment createPackage(final String name) throws CoreException
	{
		final IPackageFragment[] createdPackage = new IPackageFragment[1];
		
		final IWorkspaceRunnable createPackageRunnable = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				if (sourceFolder == null)
					sourceFolder = createSourceFolder("src");
				createdPackage[0] = sourceFolder.createPackageFragment(name, false, null);
			}
		};
		executeWorkspaceRunnable(createPackageRunnable);
		
		return createdPackage[0];
	}

	public IType createType(final IPackageFragment pack, final String cuName, final String source) throws CoreException
	{
		final StringBuffer buf = new StringBuffer();
		buf.append("package " + pack.getElementName() + ";\n");
		buf.append("\n");
		buf.append(source);
		
		final ICompilationUnit[] createdCu = new ICompilationUnit[1];
		final IWorkspaceRunnable createCuRunnable = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				createdCu[0] = pack.createCompilationUnit(cuName, buf.toString(), false, null);
			}
		};
		executeWorkspaceRunnable(createCuRunnable);
		
		return createdCu[0].getTypes()[0];
	}

	
	/**
	 * Creates multiple classes in project. The content of <code>source</code> should be in form -
	 * '#' char (separator for classes) followed by Class name, new line and after that class source without package declaration.<br>
	 * Example:
	 * 
	 * <pre>
	 *  #ImplementsRemote
	 *  public class ImplementsRemote implements java.rmi.Remote
	 *  {
	 *  }
	 * 	
	 *  #NoDefaultConstructor
	 *  public class NoDefaultConstructor implements java.io.Serializable
	 *  {
	 * 	public NoDefaultConstructor(String param) {
	 *  	}
	 *  }
	 * </pre>
	 * 
	 * This method is useful if lots of small classes should be created from a source file.
	 * 
	 * @param sourcesFilePath
	 * @return map of className->IType. that have been created
	 * @throws Exception
	 */
	public Map<String, IType> createTypes(IPackageFragment pack, String source) throws Exception
	{
		Map<String, IType> types = new HashMap<String, IType>();
		int startPos = 0;
		while ((startPos = source.indexOf(CLASS_SEPARATOR)) > -1)
		{
			int endPos = source.indexOf(CLASS_SEPARATOR, startPos + 1);
			if (endPos == -1) {
				endPos = source.length();
			}

			String src = source.substring(startPos + 1, endPos);
			source = source.substring(endPos);

			String className = src.substring(0, src.indexOf('\n')).replaceAll("\r", "").trim();
			src = src.substring(src.indexOf('\n') + 1);

			types.put(className, createType(pack, className + ".java", src));
		}

		return types;
	}		
	
	public void dispose() throws CoreException
	{
		JobUtils.waitForJobs();
		
		final IWorkspaceRunnable disposeProjectRunnable = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				try {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
					project.delete(true, true, null);
				} catch (ResourceException re) {
					/*
					 * silently swallow the resource exception. For some reason this exception gets thrown
					 * from time to time and reports the test failing. The project deletion itself happens
					 * after the test has completed and a failure will not report a problem in the test.
			    	 * Only ResourceException is caught in order not to hide unexpected errors.	
					 */
					return;
				}
			}
		};
		executeWorkspaceRunnable(disposeProjectRunnable);
	}
	
	public void close() throws CoreException
	{
		JobUtils.waitForJobs();
		
		final IWorkspaceRunnable closeRunnable = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				project.close(monitor);		
			}
		};
		executeWorkspaceRunnable(closeRunnable);
	}

	private IFolder createBinFolder() throws CoreException
	{
		IFolder binFolder = project.getFolder("bin");
		binFolder.create(false, true, null);
		return binFolder;
	}

	private void setJavaNature() throws CoreException
	{
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, null);
	}

	private void createOutputFolder(IFolder binFolder) throws JavaModelException
	{
		IPath outputLocation = binFolder.getFullPath();
		javaProject.setOutputLocation(outputLocation, null);
	}

	public IPackageFragmentRoot createSourceFolder(final String name) throws CoreException
	{
		final IPackageFragmentRoot[] createdRoot = new IPackageFragmentRoot[1];
		final IWorkspaceRunnable createRootRunnable = new IWorkspaceRunnable()
		{
			
			public void run(IProgressMonitor monitor) throws CoreException
			{
				IFolder folder = project.getFolder(name);
				folder.create(false, true, null);
				createdRoot[0] = javaProject.getPackageFragmentRoot(folder);

				IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
				IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
				System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
				newEntries[oldEntries.length] = JavaCore.newSourceEntry(createdRoot[0].getPath());
				setClasspath(newEntries);
			}
		};
		executeWorkspaceRunnable(createRootRunnable);
		this.sourceFolder = createdRoot[0];
		
		return sourceFolder;
	}

	private void addSystemLibraries() throws CoreException
	{
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaRuntime.getDefaultJREContainerEntry();
		setClasspath(newEntries);
	}

	private Path findFileInPlugin(String plugin, String file) throws MalformedURLException, IOException
	{
		Bundle pluginBundle = Platform.getBundle(plugin);
		URL jarURL = new URL(pluginBundle.getEntry("/"), file);
		URL localJarURL = FileLocator.toFileURL(jarURL);
		return new Path(localJarURL.getPath());
	}

	public IPackageFragmentRoot getSourceFolder()
	{
		return sourceFolder;
	}
	
	/**
	 * 
	 * @return the container corresponding to the package fragment root, i.e. the source folder.<br>
	 * If this project is a jar/zip (if this is possible at all), the method will return null.
	 */
	public IContainer getSourceFolderContainer()
	{
		try{
			IResource res = getSourceFolder().getCorrespondingResource();
			if(res==null) {
				return null;
			}else {
				IContainer retVal = (IContainer) res;
				return retVal;
			}
		}catch(JavaModelException ex) {
			return null;
		}catch(ClassCastException ex) {
			return null;
		}
	}

	public IFolder createFolder(final String folderName) throws CoreException
	{
		ContractChecker.nullCheckParam(folderName, "folderName");
		final IFolder folder = this.project.getFolder(folderName);
		final IWorkspaceRunnable createFolderRunnable = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				folder.create(false, true, monitor);
			}
		};
		executeWorkspaceRunnable(createFolderRunnable);
		
		return folder;		
	}
	
	/**
	 * Set the project root as source folder
	 * @throws JavaModelException 
	 */
	public void assignProjectRootAsSourceFolder() throws CoreException
	{
		IClasspathEntry defaultSrcFolderEntry = JavaCore.newSourceEntry(project.getFullPath());
		IClasspathEntry[] oldCp = javaProject.getRawClasspath();
		IClasspathEntry[] newCp = new IClasspathEntry[oldCp.length + 1];
		System.arraycopy(oldCp, 0, newCp, 0, oldCp.length);
		newCp[newCp.length - 1] = defaultSrcFolderEntry;
		
		setClasspath(newCp);
		this.sourceFolder =  TestProjectsUtils.getSourceFolder(project, "");
	}
	
	private void executeWorkspaceRunnable(final IWorkspaceRunnable runnable) throws CoreException
	{
		executeWorkspaceRunnable(runnable, new NullProgressMonitor());
	}
	
	private void executeWorkspaceRunnable(final IWorkspaceRunnable runnable, final IProgressMonitor monitor) throws CoreException
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
	
	private void runInTestContext(final IWorkspaceRunnable runnable, final IProgressMonitor pm) throws CoreException
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

	private IWorkspace workspace()
	{
		return ResourcesPlugin.getWorkspace();
	}
	
	private void setClasspath(final IClasspathEntry[] newClasspath) throws CoreException
	{
		final IWorkspaceRunnable setClasspathRunnable = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor) throws CoreException
			{
				javaProject.setRawClasspath(newClasspath, null);
			}
		};
		executeWorkspaceRunnable(setClasspathRunnable);
		
		String log = "\n"+javaProject.getProject().getName()+"\n"+String.valueOf(javaProject.getRawClasspath().length)+"\n"; 

		for(int ii=0; ii < javaProject.getRawClasspath().length; ii++)
		{
			log = log +  javaProject.getRawClasspath()[ii].getPath().toString()+"\n";
		}
		
		if(javaProject.getRawClasspath().length==0)
		{
			log = log + "Classpath not initialized !\n";
		}
		
		ResourcesPlugin.getPlugin().getLog().log(new Status(0,"testOutput",log));
	}
}