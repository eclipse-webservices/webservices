/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.common.environment.EnvironmentService;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.server.core.IServer;

public abstract class AbstractStartServer {
 
protected IProgressMonitor monitor;
private ILog log_;

public AbstractStartServer()
{
	log_ = EnvironmentService.getEclipseLog();
}

/**
 * Execute the command
 */
public void StartServer (IProject project, IServer server, IProgressMonitor monitor, boolean restart) throws CoreException
{
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
        log_.log(ILog.INFO, 5050, this, "StartServer", "project="+project+", Stop command completed, restart needed");
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
    throw new CoreException(new Status(IStatus.ERROR,WebServiceConsumptionPlugin.ID,0,ConsumptionMessages.MSG_ERROR_SERVER,e));
  }
}

protected void publishProject(IServer server) throws CoreException
{
      monitor.subTask( ConsumptionMessages.PROGRESS_INFO_PUBLISHING_SERVER );
      IStatus status = server.publish(IServer.PUBLISH_INCREMENTAL, monitor);
      if (status.getSeverity() != IStatus.OK)
      	throw new CoreException(status);
      log_.log(ILog.INFO, 5051, this, "publishProject", "IServer="+server+", Publish command completed");
 }

protected void startProject(IServer server) throws CoreException
{
  try
  {
    monitor.subTask( ConsumptionMessages.PROGRESS_INFO_STARTING_SERVER );    
    server.synchronousStart(ILaunchManager.RUN_MODE, monitor);
    log_.log(ILog.INFO, 5052, this, "startProject", "IServer="+server+", Start command completed");
    
  }
  catch (Exception e) {
    throw new CoreException(new Status(IStatus.ERROR,WebServiceConsumptionPlugin.ID,0,ConsumptionMessages.MSG_ERROR_SERVER,e));
  }  
}

protected void restartProject(IProject project, IServer server) throws CoreException
{
  //Do nothing
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
