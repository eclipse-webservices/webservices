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
 * 20070501   178129 makandre@ca.ibm.com - Andrew Mak, workspace reports "crash detected" after running WS ant task (cmdline)
 *******************************************************************************/
package org.eclipse.jst.ws.internal.consumption.ui.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class AntWorkspaceSaveCommand extends AbstractDataModelOperation {
	
	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		try {
			// attempt to save the state of the workspace
			ResourcesPlugin.getWorkspace().save(true, monitor);
			return Status.OK_STATUS;
		}
		catch (CoreException e) {
			throw new ExecutionException(e.getMessage(), e);
		}
	}	
}
