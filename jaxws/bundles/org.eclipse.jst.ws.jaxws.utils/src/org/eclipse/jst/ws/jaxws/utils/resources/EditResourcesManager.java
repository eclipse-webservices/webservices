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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.jaxws.utils.StatusUtils;
import org.eclipse.jst.ws.jaxws.utils.internal.text.JaxWsUtilMessages;
import org.eclipse.jst.ws.jaxws.utils.logging.ILogger;
import org.eclipse.jst.ws.jaxws.utils.logging.Logger;

/**
 * This is an utility class which is meant to provide common functionality to save and modify resources. It is a wrapper of the eclipse API and
 * workarounds some DTR issues. Until DTR colleagues fix their coding, this manager must be used on each file modification occasion
 * 
 * @author Danail Branekov
 */
public class EditResourcesManager
{
	private static final ILogger logger = new Logger();
	
	/**
	 * Sets editability of a file. In case the file specified is already writable, <code>Status.OK_STATUS</code> is returned
	 * 
	 * @param file
	 *            The file to be set editable
	 * @param context
	 *            either {@link IWorkspace#VALIDATE_PROMPT}, or the <code>org.eclipse.swt.widgets.Shell</code> that is to be used to parent any
	 *            dialogs with the user, or <code>null</code> if there is no UI context (declared as an <code>Object</code> to avoid any direct
	 *            references on the SWT component)
	 * @return the status of the operation
	 * @throws NullPointerException
	 *             when the file specified is null
	 * @see IWorkspace#VALIDATE_PROMPT
	 */
	public IStatus setFileEditable(IFile file, Object context)
	{
		if (file == null)
		{
			throw new NullPointerException("file"); //$NON-NLS-1$
		}
		if(!file.isReadOnly())
		{
			return Status.OK_STATUS;
		}
		
		IWorkspace workspace = file.getWorkspace();
		return workspace.validateEdit(new IFile[] { file }, context);
	}

	/**
	 * Sets editablility of a file. This is a convenience method equivalent to invoking
	 * <code>setFileEditable(file, IWorkspace.VALIDATE_PROMPT)</code>
	 * 
	 * @param file
	 *            The file to be set editable
	 * @return the status of the operation
	 * @throws NullPointerException
	 *             when the file specified is null
	 * @see EditResourcesManager#setFileEditable(IFile, Object)
	 * @see IWorkspace#VALIDATE_PROMPT
	 * 
	 */
	public IStatus setFileEditable(IFile file)
	{
		return setFileEditable(file, IWorkspace.VALIDATE_PROMPT);
	}

	/**
	 * Sets editablility of an array of files. In case all files specified are already writable, <code>Status.OK_STATUS</code> is returned
	 * 
	 * @param files
	 *            The array of files to be set editable
	 * @param context
	 *            either {@link IWorkspace#VALIDATE_PROMPT}, or the <code>org.eclipse.swt.widgets.Shell</code> that is to be used to parent any
	 *            dialogs with the user, or <code>null</code> if there is no UI context (declared as an <code>Object</code> to avoid any direct
	 *            references on the SWT component)
	 * @return the status of the operation
	 * @throws IllegalArgumentException
	 *             when the array is empty or null
	 */
	public IStatus setFilesEditable(IFile[] files, Object context)
	{
		if (files == null || files.length == 0)
		{
			throw new IllegalArgumentException("Array must not be empty nor null"); //$NON-NLS-1$
		}
		List<IFile> roFiles = new ArrayList<IFile>();
		for(IFile f : files)
		{
			if(f.isReadOnly())
			{
				roFiles.add(f);
			}
		}
		
		if(roFiles.size() == 0)
		{
			return Status.OK_STATUS;
		}

		IWorkspace workspace = roFiles.get(0).getWorkspace();
		return workspace.validateEdit(roFiles.toArray(new IFile[roFiles.size()]), context);
	}

	/**
	 * Sets editablility of an array of files. This is a convenience method equivalent to invoking
	 * <code>setFilesEditable(files, IWorkspace.VALIDATE_PROMPT)</code> 
	 * 
	 * @param files
	 *            The array of files to be set editable
	 * @return the status of the operation
	 * @throws IllegalArgumentException
	 *             when the array is empty or null
	 * @see EditResourcesManager#setFilesEditable(IFile[], Object)
	 */
	public IStatus setFilesEditable(IFile[] files)
	{
		return setFilesEditable(files, IWorkspace.VALIDATE_PROMPT);
	}

	/**
	 * Sets the content of a file
	 * 
	 * @param file
	 *            the file to be set
	 * @param content
	 *            the content to be set
	 * @param updateFlags
	 *            update flags.
	 * @see IFile#setContents(java.io.InputStream, int, IProgressMonitor)
	 * @param progressMonitor
	 *            a progress monitor, or <code>null</code> if progress reporting is not desired
	 * @return status of the operation
	 * @throws NullPointerException
	 *             when <code>file</code> or <code>content</code> is null
	 */
	public IStatus setFileContents(IFile file, String content, int updateFlags, IProgressMonitor progressMonitor)
	{
		if (file == null)
		{
			throw new NullPointerException("file"); //$NON-NLS-1$
		}

		if (content == null)
		{
			throw new NullPointerException("content"); //$NON-NLS-1$
		}

		IStatus editStatus = setFileEditable(file);
		if (editStatus.getSeverity() != IStatus.OK)
		{
			// something went wrong with the validateEdit operation
			return editStatus;
		}

		try
		{
			file.setContents(new StringInputStreamAdapter(content), updateFlags, progressMonitor);
			return Status.OK_STATUS;
		} catch (CoreException e)
		{
			logger.logError(e.getMessage(), e);
			return StatusUtils.statusError(JaxWsUtilMessages.EditResourcesManager_FILE_CONTENTS_CHANGE_FAILED_MSG, e);
		}
	}

	/**
	 * Sets the content of a file. This method is equivalent of invoking
	 * <code>setFileContens(IFile file, String content, (keepHistory ? IFile.KEEP_HISTORY : IResource.NONE) | (force ? IFile.FORCE : IResource.NONE), IProgressMonitor monitor)</code>
	 * 
	 * @param file
	 *            the file to be set
	 * @param content
	 *            the content to be set
	 * @param force
	 *            a flag controlling how to deal with resources that are not in sync with the local file system
	 * @param keepHistory
	 *            a flag indicating whether or not store the current contents in the local history
	 * @param monitor
	 *            a progress monitor, or <code>null</code> if progress reporting is not desired
	 * @return status of the operation
	 * @throws NullPointerException
	 *             when <code>file</code> or <code>content</code> is null
	 * @see EditResourcesManager#setFileContents(IFile, String, int, IProgressMonitor)
	 */
	public IStatus setFileContents(IFile file, String content, boolean force, boolean keepHistory, IProgressMonitor monitor)
	{
		return setFileContents(file, content, ((keepHistory ? IFile.KEEP_HISTORY : IResource.NONE) | (force ? IFile.FORCE : IResource.NONE)), monitor);
	}
}
