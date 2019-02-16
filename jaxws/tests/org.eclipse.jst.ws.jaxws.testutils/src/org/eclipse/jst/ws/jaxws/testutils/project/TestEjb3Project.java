/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.testutils.project;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;
import org.eclipse.jst.ws.jaxws.utils.exception.MissingResourceException;

public class TestEjb3Project
{
	private IProject ejbProject;

	public TestEjb3Project(final String name) throws Exception
	{
		ejbProject = createEjbModule(name + "_" + Long.toString(System.currentTimeMillis()));
	}
	
	public TestEjb3Project(final String name, final boolean appendUid, final boolean reuseExisting) throws Exception
	{
		final StringBuilder projectName = new StringBuilder(name);
		if (appendUid)
		{
			projectName.append("_");
			projectName.append(Long.toString(System.currentTimeMillis()));
		}
		IProject existingProject = null;
		if (reuseExisting)
		{
			existingProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName.toString());
		}
		if (existingProject == null)
		{
			this.ejbProject = createEjbModule(projectName.toString());
		} else
		{
			this.ejbProject = existingProject;
		}
	}
	
	private IProject createEjbModule(final String name) throws Exception {
		return TestProjectsUtils.createEjb3Project(name);
	}

	public IProject getProject()
	{
		return ejbProject.getProject();
	}

	public IFolder getMetaInfFolder() throws MissingResourceException
	{
		return ejbProject.getProject().getFolder("ejbModule").getFolder("META-INF");
	}

	public IPackageFragment createPackage(String sourceFolder, String packageName) throws JavaModelException
	{
		final IJavaProject javaProject = JavaCore.create(getProject());
		final IFolder folder = getProject().getFolder(sourceFolder);
		final IPackageFragmentRoot srcFolder = javaProject.getPackageFragmentRoot(folder);
		return srcFolder.createPackageFragment(packageName, false, new NullProgressMonitor());
	}

	public IFile createFile(final IFolder folder, final String fileName, final URL inputUrl) throws IOException, CoreException
	{
		ContractChecker.nullCheckParam(folder, "folder");
		ContractChecker.nullCheckParam(fileName, "fileName");
		ContractChecker.nullCheckParam(inputUrl, "inputUrl");
		
		final IFile file = folder.getFile(fileName);
		final InputStream inputStream = inputUrl.openStream();
		try
		{
			file.create(inputStream, false, new NullProgressMonitor());
			return file;
		}
		finally
		{
			inputStream.close();
		}
	}
	
	/** To get a handle to a file located in this project specified by the relative file path.
	 * @param filePath file path relative inside of the project, i.e. the project name must NOT
	 *  be part of the path 
	 * @return the file handle */
	public final IFile getFile(final String filePath)
	{
		ContractChecker.nullCheckParam(filePath, "filePath");
		return this.ejbProject.getProject().getFile(filePath);
	}

	public final void deleteFile(final IFolder folder, final String fileName) throws CoreException, IOException
	{
		ContractChecker.nullCheckParam(folder, "folder");
		ContractChecker.nullCheckParam(fileName, "fileName");
		
		final IFile file = folder.getFile(fileName);
		if (file.exists())
		{
			file.delete(true, new NullProgressMonitor());
		}
	}
	
	/** To create a project folder specified by the relative folder path. All folder segments
	 * have already to exist apart from the last one, i.e. parent folders are not created automatically.
	 * @param folderPath folder path relative inside of the project, i.e. the project name must NOT
	 *  be part of the path 
	 * @return the created folder
	 * @throws CoreException */
	public final IFolder createFolder(final String folderPath) throws CoreException
	{
		ContractChecker.nullCheckParam(folderPath, "folderPath");
		
		final IFolder folder = this.ejbProject.getProject().getFolder(folderPath);
		folder.create(true, true, null);
		return folder;
	}

	/** To get a handle to a project folder specified by the relative folder path.
	 * @param folderPath folder path relative inside of the project, i.e. the project name must NOT
	 *  be part of the path 
	 * @return the folder handle */
	public final IFolder getFolder(final String folderPath)
	{
		ContractChecker.nullCheckParam(folderPath, "folderPath");
		return this.ejbProject.getProject().getFolder(folderPath);
	}
}
