/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;

public class PublishProjectCommand extends EnvironmentalOperation 
{

private MessageUtils msgUtils_;

private String project;
private String serverTypeID;
private IServer existingServer;


/**
 * Default CTOR;
 */
public PublishProjectCommand() {
  String pluginId = "org.eclipse.jst.ws.consumption";
  msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
}

/**
 * Execute the command
 */
public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
{
  Environment env = getEnvironment();
  
  Status status = new SimpleStatus("");
  try
  {
    env.getProgressMonitor().report( msgUtils_.getMessage( "PROGRESS_INFO_PUBLISH_WEB_PROJECT" ) );
    
    if (project == null){
      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_PROJECT_NOT_FOUND"), Status.ERROR); 
      env.getStatusHandler().reportError(status);
      return status;
    }
	
    IProject iProject = (IProject)ResourceUtils.findResource(project);
    IServer instance = ServerUtils.getServerForModule(ResourceUtils.getModule(iProject), serverTypeID, existingServer, true, EnvironmentUtils.getIProgressMonitor(env));
    if (instance == null)
    {
      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_INSTANCE_NOT_FOUND"), Status.ERROR);
      env.getStatusHandler().reportError(status);
      return status;
    }

      IStatus returnedStatus = instance.publish(IServer.PUBLISH_INCREMENTAL, EnvironmentUtils.getIProgressMonitor(env));
      status = EnvironmentUtils.convertIStatusToStatus(returnedStatus);
      
      //getStatusMonitor().reportStatus (instance.publish(getProgressMonitor()));
      env.getLog().log(Log.OK, 5026, this, "execute", new String("project="+project+" successfully published"));
      //Log.write(PublishProjectCommand.class,"execute",Log.OK,"project="+project+" successfully published");
      return status;
  }
  catch (Exception e) {
    status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_PUBLISH"), Status.ERROR, e);
    env.getStatusHandler().reportError(status);
    return status;

  }
}


public void setProject(String project)
{
  this.project = project;
}

public void setServerTypeID(String serverTypeID)
{
  this.serverTypeID = serverTypeID;
}

public void setExistingServer(IServer existingServer)
{
  this.existingServer = existingServer;
}



}
