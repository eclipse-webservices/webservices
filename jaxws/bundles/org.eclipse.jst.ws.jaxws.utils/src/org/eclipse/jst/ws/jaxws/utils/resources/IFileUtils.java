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
package org.eclipse.jst.ws.jaxws.utils.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IEditorPart;

/**
 * Interface that defines common utilies for file manipulation
 * 
 * @author Danail Branekov
 */
public interface IFileUtils {

	/**
	 * Retrieves the editors that are currently displaying the file specfied.
	 * 
	 * @param fileLocation
	 *            Absolute path to the file
	 * @see IResource#getLocation().toOSString()
	 * @return Current opened editors of the file specified or an empty list if such are not opened
	 * @throws FileNotFoundException
	 *             when the file does not exist
	 */
	public abstract List<IEditorPart> getEditorsByFileLocation(
			final String fileLocation) throws FileNotFoundException;

	/**
	 * Determines whether the file specified is displayed within an editor which
	 * has a dirty state.
	 * 
	 * @param fileLocation
	 *            Absolute path to the file
	 * @see IResource#getLocation().toOSString()
	 * @return whether the file is dirty
	 * @throws FileNotFoundException
	 *             when the location specified does not exist
	 */
	public abstract boolean isFileDisplayedInDirtyEditor(
			final String fileLocation) throws FileNotFoundException;

	/**
	 * Saves the dirty state editor
	 * 
	 * @param fileLocation
	 *            Absolute path to the file
	 * @see IResource#getLocation()#toOSString()
	 * @throws FileNotFoundException
	 *             when the location specified does not exis
	 * @throws IllegalStateException
	 *             when the file is not displayed by an editor
	 */
	public abstract void saveEditor(String fileLocation)
			throws FileNotFoundException;

	/**
	 * Saves the dirty state editor
	 * 
	 * @param fileLocation
	 *            Absolute path to the file
	 * @see IResource#getLocation()#toOSString()
	 * @param monitor
	 *            Progress monitor
	 * @throws FileNotFoundException
	 *             when the location specified does not exis
	 * @throws IllegalStateException
	 *             when the file is not displayed by an editor
	 */
	public abstract void saveEditor(String fileLocation,
			IProgressMonitor monitor) throws FileNotFoundException;

	/**
	 * Retrieves a list of dirty editors
	 * 
	 * @return collection containing dirty editors
	 */
	public abstract Collection<IEditorPart> getDirtyEditors();

	/**
	 * Retrieve compilation units for the location specified
	 * 
	 * @param fileLocation
	 *            The absolute path to the file, e.g.
	 *            <code>C:\\dir1\\dir2\\file.name</code>
	 * @see IResource#getLocation()#toOSString()
	 * 
	 * @throws FileNotFoundException
	 *             when the file specified cannot be found in the workspace
	 * @throws IllegalStateException
	 *             when the file specified is not a compilation unit
	 * @throws NullPointerException
	 *             when fileLocation is null
	 * @return The compilation units; cannot be null
	 */
	public abstract List<ICompilationUnit> getCompilationUnits(
			final String fileLocation) throws FileNotFoundException;
	
	/**
	 * Retrieve project unit for the location specified
	 * 
	 * @param fileLocation
	 *            The absolute path to the file, e.g.
	 *            <code>C:\\dir1\\dir2\\file.name</code>
	 * @see IResource#getLocation()#toOSString()
	 * 
	 * @throws FileNotFoundException
	 *             when the file specified cannot be found in the workspace
	 * @throws NullPointerException
	 *             when fileLocation is null
	 * @return The compilation units; cannot be null
	 */
	public abstract IProject getProjectUnit(
			final String fileLocation) throws FileNotFoundException;

	/**
	 * Converts a String to a URL, assuming 'file:' as a default scheme.
	 * 
	 * @param base
	 * @param path
	 * 
	 * @return
	 * 
	 * @throws NullPointerException,
	 *             if the String is neither a valid URL, nor a valid filename.
	 * 
	 */
	public abstract URL fileOrURLToURL(final URL base, final String path)
			throws IOException;

	/**
	 * Creates a new directory in the OS temp folder
	 * 
	 * @return the directory
	 */
	public File createTempDirectory();

	/**
	 * Deletes the specified directory and all its content
	 * 
	 * param directory
	 */
	public void deleteDirectory(final File directory);

	/**
	 * Gets all the files with the provided extension within a project.
	 * 
	 * @param project -
	 *            to be searched
	 * @param extension -
	 *            types of files of interest
	 */
	public IFile[] getFilesByExtension(IProject project, String extension)
			throws CoreException;

	/**
	 * Retrieves a list of editors that are currently displaying the compilation
	 * unit specified.
	 * 
	 * @param cu
	 *            the compilation unit; must not be null
	 * @return Current editors list or an empty list in case there are not
	 *         editors displaying the file specified
	 * 
	 * @throws NullPointerException
	 *             when the <code>cu</code> parameter is null
	 */
	public abstract List<IEditorPart> getEditorsByCompilationUnit(
			final ICompilationUnit cu);

	/**
	 * Determines whether the compilation unit specified is displayed within an
	 * editor which has a dirty state.
	 * 
	 * @param unit
	 *            The compilation unit
	 * 
	 * @return whether the compilation unit is dirty
	 */
	public abstract boolean isCompilationUnitDisplayedInDirtyEditor(
			final ICompilationUnit unit);

	/**
	 * Saves all the dirty state editors that are currently displaying the
	 * compilation unit specified
	 * 
	 * @param cu
	 *            the compilation unit
	 * @param monitor
	 *            Progress monitor
	 * @throws FileNotFoundException
	 *             when the location specified does not exist
	 * @throws IllegalStateException
	 *             when the file is not displayed by an editor
	 */
	public abstract void saveEditor(ICompilationUnit cu,
			IProgressMonitor monitor);

	/**
	 * Saves all the dirty state editors that are currently displaying the
	 * compilation unit specified
	 * 
	 * @param cu
	 *            the compilation unit
	 * @throws FileNotFoundException
	 *             when the location specified does not exist
	 * @throws IllegalStateException
	 *             when the file is not displayed by an editor
	 */
	public abstract void saveEditor(ICompilationUnit cu);

	/**
	 * Retrieve a file instance for the location specified. This method is a
	 * convenience method equivalent to invoking
	 * <code>ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(location)</code>
	 * 
	 * @param location
	 *            the file location; must be absolute
	 * @return the corresponding file in the workspace, or null if none
	 * @see IWorkspaceRoot#getFileForLocation(IPath)
	 */
	public IFile getFileForLocation(IPath location);

	/**
	 * Reads file content as text and returns {@link String}
	 * 
	 * @param sourceFile
	 * @return the content of file
	 * @throws IOException
	 */
	public String getFileContent(final File sourceFile) throws IOException;

	/**
	 * Sets the content of the compilation unit specified with the
	 * <code>content</code> provided. There is no validation of the content
	 * 
	 * @param cUnit
	 *            the compilation unit. Must not be null. If the compilation
	 *            unit does not exist, it will be created
	 * @param content
	 *            the content to be set. Must not be null
	 * @param force
	 *            a flag to handle the cases when the contents of the original
	 *            resource have changed since this working copy was created
	 * @param monitor
	 *            the monitor to report progress to or null if no progress
	 *            monitoring is required
	 * 
	 * @throws JavaModelException
	 *             when there is a problem with setting compilation unit content
	 * @see ICompilationUnit#becomeWorkingCopy(IProgressMonitor)
	 * @see ICompilationUnit#commitWorkingCopy(boolean, IProgressMonitor)
	 * @see ICompilationUnit#discardWorkingCopy()
	 */
	public void setCompilationUnitContent(ICompilationUnit cUnit,
			String content, boolean force, IProgressMonitor monitor)
			throws JavaModelException;

	/**
	 * Sets the content of the compilation unit specified with the
	 * <code>content</code> provided and saves the dirty editors if any which
	 * display the compilation unit. There is no validation of the content
	 * 
	 * @param cUnit
	 *            the compilation unit. Must not be null. If the compilation
	 *            unit does not exist, it will be created
	 * @param content
	 *            the content to be set. Must not be null
	 * @param force
	 *            a flag to handle the cases when the contents of the original
	 *            resource have changed since this working copy was created
	 * @param monitor
	 *            the monitor to report progress to or null if no progress
	 *            monitoring is required
	 * 
	 * @throws JavaModelException
	 *             when there is a problem with setting compilation unit content
	 * @see ICompilationUnit#becomeWorkingCopy(IProgressMonitor)
	 * @see ICompilationUnit#commitWorkingCopy(boolean, IProgressMonitor)
	 * @see ICompilationUnit#discardWorkingCopy()
	 * @see IFileUtils#setCompilationUnitContent(ICompilationUnit, String,
	 *      boolean, IProgressMonitor)
	 * @see IFileUtils#isFileDisplayedInDirtyEditor(String)
	 * @see IFileUtils#saveEditor(ICompilationUnit, IProgressMonitor)
	 */
	public void setCompilationUnitContentAndSaveDirtyEditors(
			ICompilationUnit cUnit, String content, boolean force,
			IProgressMonitor monitor) throws JavaModelException;
}
