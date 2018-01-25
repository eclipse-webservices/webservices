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
package org.eclipse.jst.ws.jaxws.utils.resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.utils.ContractChecker;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Implementation for file utilities interface
 * 
 * @author Danail Branekov
 * 
 */
public class FileUtils implements IFileUtils {
	private static IFileUtils utils = null;
	private static int index = 0;

	public FileUtils() {
	}

	/**
	 * The factory method
	 * 
	 * @return a FileUtils instance
	 */
	public static IFileUtils getInstance() {
		if (utils == null) {
			utils = new FileUtils();
		}

		return utils;
	}

	public final List<IEditorPart> getEditorsByFileLocation(
			final String fileLocation) throws FileNotFoundException {
		List<ICompilationUnit> units = getCompilationUnits(fileLocation);
		Set<IEditorPart> editors = new HashSet<IEditorPart>();
		for (ICompilationUnit cu : units) {
			final IEditorDescriptor editorDescr = getEditorDescriptor(cu
					.getElementName());
			editors.addAll(getEditorsByCompilationUnit(editorDescr,
					getActiveWorkbenchPage(), cu));

		}

		return Arrays.asList(editors.toArray(new IEditorPart[editors.size()]));
	}

	public final List<ICompilationUnit> getCompilationUnits(
			final String fileLocation) throws FileNotFoundException {
		if (fileLocation == null) {
			throw new NullPointerException("fileLocation"); //$NON-NLS-1$
		}

		IFile[] filesFound = ResourcesPlugin.getWorkspace().getRoot()
				.findFilesForLocationURI(new File(fileLocation).toURI());
		if (filesFound == null || filesFound.length == 0) {
			throw new FileNotFoundException("File: " + fileLocation + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		List<ICompilationUnit> units = new ArrayList<ICompilationUnit>();

		for (IFile file : filesFound) {
			IJavaElement javaElement = JavaCore.create(file);
			if (!(javaElement instanceof ICompilationUnit)) {
				throw new IllegalStateException("File: " + fileLocation + " is not a compilation unit"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			units.add((ICompilationUnit) javaElement);
		}

		return units;
	}
			
	public IProject getProjectUnit(String fileLocation)
			throws FileNotFoundException {
		
		if (fileLocation == null)
		{
			throw new NullPointerException("fileLocation"); //$NON-NLS-1$
		}

		IFile file = getFileForLocation(new Path(fileLocation));
		if (file == null)
		{
			throw new FileNotFoundException("File: " + fileLocation + " does not exist"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return file.getProject();
	}

	public final boolean isFileDisplayedInDirtyEditor(final String fileLocation)
			throws FileNotFoundException {
		return isFileDisplayedInDirtyEditor(getEditorsByFileLocation(fileLocation));
	}

	public boolean isCompilationUnitDisplayedInDirtyEditor(
			final ICompilationUnit unit) {
		return isFileDisplayedInDirtyEditor(getEditorsByCompilationUnit(unit));
	}

	public final void saveEditor(String fileLocation)
			throws FileNotFoundException {
		saveEditor(fileLocation, new NullProgressMonitor());
	}

	public void saveEditor(final String fileLocation,
			final IProgressMonitor monitor) throws FileNotFoundException {
		List<IEditorPart> editors = getEditorsByFileLocation(fileLocation);
		if (editors.size() == 0) {
			throw new IllegalStateException("The file is not displayed by an editor"); //$NON-NLS-1$
		}

		saveEditors(editors, monitor);
	}

	public void saveEditor(final ICompilationUnit cu,
			final IProgressMonitor monitor) {
		List<IEditorPart> editors = getEditorsByCompilationUnit(cu);
		if (editors.size() == 0) {
			throw new IllegalStateException("The file is not displayed by an editor"); //$NON-NLS-1$
		}

		saveEditors(editors, monitor);
	}

	public void saveEditor(final ICompilationUnit cu) {
		List<IEditorPart> editors = getEditorsByCompilationUnit(cu);
		if (editors.size() == 0) {
			throw new IllegalStateException("The file is not displayed by an editor"); //$NON-NLS-1$
		}

		saveEditors(editors, new NullProgressMonitor());
	}

	private void saveEditors(final List<IEditorPart> editors,
			final IProgressMonitor monitor) {
		for (IEditorPart e : editors) {
			if (e.isDirty()) {
				e.doSave(monitor);
			}
		}
	}

	public Collection<IEditorPart> getDirtyEditors() {
		final Set<IEditorInput> inputs = new HashSet<IEditorInput>();
		final Collection<IEditorPart> result = new ArrayList<IEditorPart>();
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
		for (int i = 0; i < windows.length; i++) {
			final IWorkbenchPage[] pages = windows[i].getPages();
			for (int x = 0; x < pages.length; x++) {
				final IEditorPart[] editors = pages[x].getDirtyEditors();
				for (int z = 0; z < editors.length; z++) {
					final IEditorPart ep = editors[z];
					final IEditorInput input = ep.getEditorInput();
					if (!inputs.contains(input)) {
						inputs.add(input);
						result.add(ep);
					}
				}
			}
		}
		return result;
	}

	private IEditorDescriptor getEditorDescriptor(String fileName) {
		IEditorRegistry editorRegistry = PlatformUI.getWorkbench()
				.getEditorRegistry();
		return editorRegistry.getDefaultEditor(fileName);
	}

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
	public final URL fileOrURLToURL(final URL base, final String path)
			throws IOException {
		if (path == null) {
			throw new NullPointerException("path"); //$NON-NLS-1$
		}

		String localPath = path;
		URL localBase = base;

		if (localBase == null) {
			localBase = new URL("file:");//$NON-NLS-1$
		}

		localPath = localPath.replace('\\', '/');
		try {
			String protocol = localBase.getProtocol();
			if (!"http".equals(protocol) //$NON-NLS-1$
					&& !"https".equals(protocol) //$NON-NLS-1$
					&& (localPath.charAt(1) == ':'
							|| localPath.charAt(0) == '/' || localPath
							.charAt(0) == '\\')) {
				localPath = (new File(localPath)).toURI().toURL().toExternalForm();
			}
		} catch (IndexOutOfBoundsException e) {
			// $JL-EXC$
		}

		if (localPath.startsWith("file:") && localPath.length() > 5 //$NON-NLS-1$
				&& localPath.charAt(5) != '/') {
			localPath = "file:/".concat(localPath.substring(5)); //$NON-NLS-1$
		} else {
			if (localPath.startsWith("file://") //$NON-NLS-1$
					&& !localPath.startsWith("file:////") //$NON-NLS-1$
					&& !localPath.startsWith("file://localhost/")) { //$NON-NLS-1$
				localPath = "file:////".concat(localPath.substring("file://" //$NON-NLS-1$ //$NON-NLS-2$
						.length()));
			}
		}
		URL url = new URL(localBase, localPath);
		return url;
	}

	public File createTempDirectory() {
		String tempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		index++;
		File temp1 = new File(tempDir, Integer.toString(this.hashCode()) + "_" //$NON-NLS-1$
				+ index);
		temp1.mkdir();
		return temp1;
	}

	public void deleteDirectory(final File directory) {
		if (directory == null) {
			return;
		}
		if (!directory.exists()) {
			return;
		}
		for (File f : directory.listFiles()) {
			f.delete();
		}
		directory.delete();
	}

	public IFile[] getFilesByExtension(IProject project, String extension)
			throws CoreException {
		ContractChecker.nullCheckParam(extension, "extension"); //$NON-NLS-1$
		ArrayList<IFile> foundFiles = new ArrayList<IFile>();

		getFilesByExtension(foundFiles, project.members(), extension);

		return foundFiles.toArray(new IFile[foundFiles.size()]);
	}

	private void getFilesByExtension(ArrayList<IFile> foundFiles,
			IResource[] currentResources, String extension)
			throws CoreException {
		for (int ii = 0; ii < currentResources.length; ii++) {
			if (currentResources[ii].getType() == IResource.FOLDER) {
				getFilesByExtension(foundFiles,
						((IFolder) currentResources[ii]).members(), extension);
			} else if (currentResources[ii].getType() == IResource.FILE
					&& extension
							.equals(currentResources[ii].getFileExtension())) {
				foundFiles.add((IFile) currentResources[ii]);
			}
		}
	}

	public IFile getFileForLocation(IPath location) {
		return ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(
				location);
	}

	private IWorkbenchPage getActiveWorkbenchPage() {
		if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {
			return null;
		}

		return PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage();
	}

	public List<IEditorPart> getEditorsByCompilationUnit(
			final ICompilationUnit cu) {
		ContractChecker.nullCheckParam(cu, "cu"); //$NON-NLS-1$
		final IEditorDescriptor editorDescr = getEditorDescriptor(cu
				.getElementName());
		final IWorkbenchPage wbPage = getActiveWorkbenchPage();
		if (wbPage == null) {
			return new ArrayList<IEditorPart>(0);
		}

		return getEditorsByCompilationUnit(editorDescr, wbPage, cu);
	}

	private List<IEditorPart> getEditorsByCompilationUnit(
			final IEditorDescriptor editorDescriptor,
			final IWorkbenchPage page, final ICompilationUnit cu) {
		List<IEditorPart> editorPartReturned = new ArrayList<IEditorPart>();

		if (editorDescriptor != null && page != null) {
			for (IEditorReference editorReferece : page.getEditorReferences()) {
				IEditorPart editorPart = editorReferece.getEditor(true);
				if (isEditorApplicableForCu(editorPart, cu)) {
					editorPartReturned.add(editorPart);
				}
			}
		}

		return editorPartReturned;
	}

	private boolean isEditorApplicableForCu(final IEditorPart editor,
			final ICompilationUnit cu) {
		if (editor == null) {
			return false;
		}

		if (!(editor.getEditorInput() instanceof IFileEditorInput)
				|| !(cu.getResource() instanceof IFile)) {
			return false;
		}

		final IFile cuFile = (IFile) cu.getResource();
		final IFileEditorInput editorInput = (IFileEditorInput) editor
				.getEditorInput();

		return cuFile.equals(editorInput.getFile());
	}

	public final boolean isFileDisplayedInDirtyEditor(
			final List<IEditorPart> displayingEditors) {
		ContractChecker.nullCheckParam(displayingEditors, "displayingEditors"); //$NON-NLS-1$
		for (IEditorPart editor : displayingEditors) {
			if (editor.isDirty()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Reads file content as text and returns {@link String}
	 * 
	 * @param sourceFile
	 * @return the content of file
	 * @throws IOException
	 */
	public String getFileContent(final File sourceFile) throws IOException {
		final FileReader fr = new FileReader(sourceFile);
		final StringBuilder builder = new StringBuilder();

		try {
			final char[] buff = new char[1024];
			for (int cnt = 0; (cnt = fr.read(buff)) > -1;) {
				builder.append(buff, 0, cnt);
			}
		} finally {
			fr.close();
		}

		return builder.toString();
	}

	public void setCompilationUnitContent(final ICompilationUnit unit,
			final String content, final boolean force,
			final IProgressMonitor monitor) throws JavaModelException {
		ContractChecker.nullCheckParam(unit, "unit"); //$NON-NLS-1$
		ContractChecker.nullCheckParam(content, "content"); //$NON-NLS-1$

		unit.becomeWorkingCopy(monitor);
		try {
			unit.getBuffer().setContents(content);
			unit.commitWorkingCopy(force, monitor);
		} finally {
			unit.discardWorkingCopy();
		}
	}

	public void setCompilationUnitContentAndSaveDirtyEditors(
			final ICompilationUnit unit, final String content,
			final boolean force, final IProgressMonitor monitor)
			throws JavaModelException {
		setCompilationUnitContent(unit, content, force, monitor);
		if (isCompilationUnitDisplayedInDirtyEditor(unit)) {
			saveEditor(unit);
		}
	}
}
