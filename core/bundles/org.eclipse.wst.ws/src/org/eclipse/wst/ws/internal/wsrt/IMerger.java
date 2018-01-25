/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070509   182274 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.wst.ws.internal.wsrt;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.wst.common.environment.IStatusHandler;

/*
 * The IMerger interface handles merging of the array of IFiles.  Users of the IMerger interface should:
 * 1. Call load() with the array of IFiles to load and stored in merge models
 * 2. Call merge() to merge the current contents of the IFiles with the merged models that had been stored 
 * previously (at the time load was called).
 * 
 * This class is not meant to be implemented directly.  Implementers should subclass the Merger class instead.
 */
public interface IMerger { 
	
	/**
	 * Load the uris specified so that a merge can be done later on
	 * @param urls
	 * @return IStatus
	 */
	public IStatus load(IFile[] files);
	
	/**
	 * Merge the current content of the array of IFiles with what had been stored when load was first called and write 
	 * the merged content back in the files. 
	 * @param monitor Progress monitor
	 * @param statusHandler The status handler used to handle the writing of the merged content
	 * @return IStatus
	 */
	public IStatus merge(IProgressMonitor monitor, IStatusHandler statusHandler);
}
