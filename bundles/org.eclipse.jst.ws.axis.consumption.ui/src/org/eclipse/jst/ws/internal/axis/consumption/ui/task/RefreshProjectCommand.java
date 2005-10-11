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
package org.eclipse.jst.ws.internal.axis.consumption.ui.task;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;



public class RefreshProjectCommand extends AbstractDataModelOperation
{
	private IProject project;
	private MessageUtils msgUtils_;

	public RefreshProjectCommand()
	{
		msgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.ui.plugin", this );
	}

	/**
	* Execute RefreshProjectTask
	*/
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		Environment env = getEnvironment();
		try
		{
			if (project!=null)
			project.refreshLocal(IProject.DEPTH_INFINITE, new NullProgressMonitor());
		}
		catch (CoreException e)
		{
		  IStatus status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_REFRESH_PROJECT"), e);
		  env.getStatusHandler().reportError(status);
		  return status;
		}
		return Status.OK_STATUS;
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}
}
