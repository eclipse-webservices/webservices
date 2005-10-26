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
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.common.ProgressUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;

public class PublishProjectCommand extends AbstractDataModelOperation 
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
  IEnvironment env = getEnvironment();
  
  IStatus status = Status.OK_STATUS;
  try
  {
    ProgressUtils.report( monitor, msgUtils_.getMessage( "PROGRESS_INFO_PUBLISH_WEB_PROJECT" ) );
    
    if (project == null){
      status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_PROJECT_NOT_FOUND") ); 
      env.getStatusHandler().reportError(status);
      return status;
    }
	
    IProject iProject = (IProject)ResourceUtils.findResource(project);
    IServer instance = ServerUtils.getServerForModule(ServerUtils.getModule(iProject), serverTypeID, existingServer, true, monitor);
    if (instance == null)
    {
      status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_INSTANCE_NOT_FOUND") );
      env.getStatusHandler().reportError(status);
      return status;
    }

      IStatus returnedStatus = instance.publish(IServer.PUBLISH_INCREMENTAL, monitor);
      status = returnedStatus;
      
      //getStatusMonitor().reportStatus (instance.publish(getProgressMonitor()));
      env.getLog().log(ILog.OK, 5026, this, "execute", new String("project="+project+" successfully published"));
      //ILog.write(PublishProjectCommand.class,"execute",ILog.OK,"project="+project+" successfully published");
      return status;
  }
  catch (Exception e) {
    status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_PUBLISH"), e);
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
