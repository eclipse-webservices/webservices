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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.j2ee.internal.project.IWebNatureConstants;
import org.eclipse.jst.j2ee.internal.web.operations.J2EEWebNatureRuntime;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusException;

public class CheckAxisDeploymentDescriptorsTask extends SimpleCommand {
	
	private MessageUtils msgUtils_;
	private MessageUtils coreMsgUtils_;
	private IProject serverProject;
	private String   moduleName_;
    
  public CheckAxisDeploymentDescriptorsTask( String moduleName )
  {
    String pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
    setDescription(msgUtils_.getMessage("CHECK_WS_DD_TASK_DESCRIPTION"));
    setName(msgUtils_.getMessage("CHECK_WS_DD_TASK_LABEL"));
	
	moduleName_ = moduleName;
  }
	
	public Status execute(Environment env) {
		Status status = new SimpleStatus( "" );
		if(EnvironmentUtils.getResourceContext(env).isOverwriteFilesEnabled()) {
      return status;
		}

	  IPath    filePath = null;
      IProject project = serverProject;
	  	  

      filePath = J2EEUtils.getWebInfPath( project, moduleName_ );
      filePath = filePath.append("/server-config.wsdd");

	  if(filePath==null || filePath.isEmpty())
		{
			status = new SimpleStatus("", coreMsgUtils_.getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR);
			env.getStatusHandler().reportError(status);
			return status;			
		}

		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
		if (file.exists() && !EnvironmentUtils.getResourceContext(env).isOverwriteFilesEnabled())   {
			status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_XML_FILE_OVERWRITE_DISABLED"), Status.WARNING);
			try
			{
				env.getStatusHandler().report(status);
			}
			catch(StatusException se)
			{
				status = new SimpleStatus("", "User aborted",Status.ERROR);
			}
			
			return status;
		}
		return status;
	}

	public void setServerProject(IProject serverProject)
	{
	  this.serverProject = serverProject;
	}
}
