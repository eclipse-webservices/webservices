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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jst.j2ee.internal.earcreation.EARNatureRuntime;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.eclipse.EclipseLog;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;

public abstract class AbstractStartServer {
 
private IProject project = null;
protected IProgressMonitor monitor;
private Log log_;
private MessageUtils msgUtils_;

public AbstractStartServer()
{
	log_ = new EclipseLog();
    msgUtils_ = new MessageUtils( "org.eclipse.jst.ws.consumption.plugin", this );
}

/**
 * Execute the command
 */
public void StartServer (IProject project, IServer server, IProgressMonitor monitor, boolean restart) throws CoreException
{
	this.project = project;
	this.monitor = monitor;

   try
  {
   	
//    IJavaServer javaServer = (IJavaServer) server.getDelegate();
//    addJarsToClassPath(javaServer);
    validateRemoteServerPath(server);

    if (server.getServerState() != IServer.STATE_STOPPED)  // The Server is running
    { 
      if (server.getServerRestartState())
      {    
      	
        server.synchronousStop(false);
        log_.log(Log.INFO, 5050, this, "StartServer", "project="+project+", Stop command completed, restart needed");
        publishProject(server);
    	startProject(server);
      }
      else
      {
      	
        if (restart) // WEB-INF\lib need to be reloaded
        {
          publishProject(server);      
          restartProject(project, server);
        }         
      }
    }
    else {
   	
    		publishProject(server);
    		startProject(server);
    	}
}

  catch (CoreException ce) {
  	throw ce;
  	}
  catch (Exception e) {
    throw new CoreException(new Status(IStatus.ERROR,WebServiceConsumptionPlugin.ID,0,msgUtils_.getMessage("MSG_ERROR_SERVER"),e));
  }
}

protected void publishProject(IServer server) throws CoreException
{
   if (server.shouldPublish())
    {
      monitor.subTask( msgUtils_.getMessage( "PROGRESS_INFO_PUBLISHING_SERVER" ) );
      IStatus status = server.publish(monitor);
      if (status.getSeverity() != IStatus.OK)
      	throw new CoreException(status);
      log_.log(Log.INFO, 5051, this, "publishProject", "IServer="+server+", Publish command completed");
    }
 }

protected void startProject(IServer server) throws CoreException
{
  try
  {
    monitor.subTask( msgUtils_.getMessage( "PROGRESS_INFO_STARTING_SERVER" ) );    
    server.synchronousStart(ILaunchManager.RUN_MODE, monitor);
    log_.log(Log.INFO, 5052, this, "startProject", "IServer="+server+", Start command completed");
    
  }
  catch (Exception e) {
    throw new CoreException(new Status(IStatus.ERROR,WebServiceConsumptionPlugin.ID,0,msgUtils_.getMessage("MSG_ERROR_SERVER"),e));
  }  
}

protected void restartProject(IProject project, IServer server) throws CoreException
{
  try
  {

    monitor.subTask( msgUtils_.getMessage( "PROGRESS_INFO_STARTING_SERVER" ) );
    EARNatureRuntime[] ears = J2EEUtils.getEARProjects(project);
    for (int i = 0; i < ears.length; i++)
    {
      IProject earProject = ears[i].getProject();
      if (earProject != null && ServerUtil.containsModule(server,ResourceUtils.getModule(earProject), monitor))    
      {
        (new org.eclipse.wst.server.ui.actions.RestartProjectAction(earProject)).run();
        log_.log(Log.INFO, 5053, this, "restartProject", "earProject="+earProject+", Restart project command completed");
       
      }
    }
  }
  catch (Exception e) {
    throw new CoreException( new Status(IStatus.ERROR,WebServiceConsumptionPlugin.ID,0,msgUtils_.getMessage("MSG_ERROR_SERVER"),e));
  }  
}

public void runPreServerConfig(IServer server, IProject EARProject){

	return;
	
}

public void runPostServerConfig(IServer server, IProject EARProject){

	return;
}


protected abstract void addJarsToClassPath(IServer server) throws CoreException;
protected abstract void validateRemoteServerPath(IServer server) throws CoreException;

public void stopServer(IServer server) throws CoreException {

	if (server != null) {

		if (server.getServerState() != IServer.STATE_STOPPED) {
			server.synchronousStop(false);
		}
	}	
	
}

}
