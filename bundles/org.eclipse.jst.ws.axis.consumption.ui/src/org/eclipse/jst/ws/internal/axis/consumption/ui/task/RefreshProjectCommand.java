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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.ws.internal.axis.consumption.ui.plugin.WebServiceAxisConsumptionUIPlugin;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;



public class RefreshProjectCommand extends SimpleCommand
{
	private static final String LABEL = "TASK_LABEL_REFESH_PROJECT";
	private static final String DESCRIPTION = "TASK_DESC_REFESH_PROJECT";
	
	private IProject project;
	private MessageUtils msgUtils_;

	public RefreshProjectCommand()
	{
		msgUtils_ = new MessageUtils( "org.eclipse.jst.ws.internal.axis.consumption.ui.plugin", this );
		setName( msgUtils_.getMessage( LABEL ) );
		setDescription( msgUtils_.getMessage( DESCRIPTION ));
	}

	/**
	* Execute RefreshProjectTask
	*/
	public Status execute(Environment env)
	{
		try
		{
			if (project!=null)
			project.refreshLocal(IProject.DEPTH_INFINITE, new NullProgressMonitor());
		}
		catch (CoreException e)
		{
		  Status status = new SimpleStatus(WebServiceAxisConsumptionUIPlugin.ID, msgUtils_.getMessage("MSG_ERROR_REFRESH_PROJECT"), Status.ERROR, e);
		  env.getStatusHandler().reportError(status);
		  return status;
		}
		return new SimpleStatus("");
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}
}
