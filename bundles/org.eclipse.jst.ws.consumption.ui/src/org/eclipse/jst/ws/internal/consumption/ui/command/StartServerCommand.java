/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070426   162287 makandre@ca.ibm.com - Andrew Mak, Server publish cancel button unavailable
 * 20080509   165327 kathy@ca.ibm.com - Kathy Chan, Use API from IServer to call shouldPublish and shouldRestart
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.command;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.EnvironmentService;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.ILog;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

/**
 * (Re)Starts and publishes the server specifed by the serverInstanceId attribute.
 * 
 *
 */
public class StartServerCommand extends AbstractDataModelOperation
{
  private ILog log;
  private boolean doAsyncPublish_;
  
  private String serverInstanceId;
	
  
  public StartServerCommand()
  {
    log             = EnvironmentService.getEclipseLog();
    doAsyncPublish_ = true;
  }
	 
  public StartServerCommand( boolean doAsyncPublish )
  {
    this();
    
    doAsyncPublish_ = doAsyncPublish;
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    IEnvironment env = getEnvironment();
    IStatus status = Status.OK_STATUS;

    IServer server = ServerCore.findServer(serverInstanceId);
    if (server == null)
    {
      status = StatusUtils.errorStatus(ConsumptionMessages.MSG_ERROR_INSTANCE_NOT_FOUND);
      env.getStatusHandler().reportError(status);
      return status;
    }

    int serverState = server.getServerState();

    //  Publish if required
    if (server.shouldPublish()) {
    	if (server.canPublish().getSeverity() == IStatus.OK)
        {
          status = publish(server, IServer.PUBLISH_INCREMENTAL, monitor );
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError(status);
            return status;
          }
        }
    }
    
    //  start/restart server if required
    	
    switch (serverState)
    {
      case IServer.STATE_STOPPED:
        if (server.canStart(ILaunchManager.RUN_MODE).getSeverity()==IStatus.OK)
        {
          status = start(server, monitor );
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError(status);
            return status;
          }       
        }
        break;
      case IServer.STATE_STARTED:    	
    	boolean shouldRestart = server.shouldRestart();    	
    	
    	if (shouldRestart && server.canRestart(ILaunchManager.RUN_MODE).getSeverity()==IStatus.OK)
        {
          status = restart(server, monitor );
          if (status.getSeverity() == Status.ERROR)
          {
            env.getStatusHandler().reportError(status);
            return status;
          }       
        }
    }
    
    return status;
  }

  private IStatus publish(final IServer server, final int kind, IProgressMonitor monitor )
  {
    IStatus status = Status.OK_STATUS;
    final IStatus[] istatus = new IStatus[1]; 
    monitor.subTask(ConsumptionMessages.PROGRESS_INFO_PUBLISHING_SERVER);
    IRunnableWithProgress runnable = new IRunnableWithProgress()
	{
  		public void run(IProgressMonitor shellMonitor) throws InvocationTargetException, InterruptedException
		{
  			istatus[0] = server.publish(kind, shellMonitor);
		}  		
	};
	
	try
	{
		if( doAsyncPublish_ )
		{	
		  ProgressMonitorDialog dialog = new ProgressMonitorDialog(null);
		  dialog.run(true, true, runnable);
		}
		else
		{
		  runnable.run( monitor );
		}
		
	}
	catch(InvocationTargetException ite)
	{
	  istatus[0] = new org.eclipse.core.runtime.Status( IStatus.ERROR, "id", 0, ite.getMessage(), ite );
	  ite.printStackTrace();
	}
	catch(InterruptedException ie)
	{
	  istatus[0] = new org.eclipse.core.runtime.Status( IStatus.ERROR, "id", 0, ie.getMessage(), ie );
	  ie.printStackTrace();
	}
    
    
	if (istatus[0].getSeverity() != IStatus.OK)
    {
      status = istatus[0];
      if (status.getSeverity() == IStatus.CANCEL)
    	  status = StatusUtils.errorStatus("");
      return status;
    }
    
    log.log(ILog.INFO, 5051, this, "publishProject", "IServer=" + server + ", Publish command completed");
    return status;
  }

  private IStatus restart(IServer server, IProgressMonitor monitor )
  {
    IStatus status = Status.OK_STATUS;
    try
    {
      monitor.subTask(ConsumptionMessages.PROGRESS_INFO_STARTING_SERVER);
      server.synchronousRestart(ILaunchManager.RUN_MODE, monitor);
      log.log(ILog.INFO, 5052, this, "execute", "IServer=" + server + ", Restart command completed");
      return status;
    } catch (CoreException e)
    {
      status = StatusUtils.errorStatus(ConsumptionMessages.MSG_ERROR_SERVER, e);
      return status;
    }

  }

  private IStatus start(IServer server, IProgressMonitor monitor )
  {
    IStatus status = Status.OK_STATUS;
    try
    {
      monitor.subTask(ConsumptionMessages.PROGRESS_INFO_STARTING_SERVER);
      server.synchronousStart(ILaunchManager.RUN_MODE, monitor);
      log.log(ILog.INFO, 5053, this, "execute", "IServer=" + server + ", Start command completed");
      return status;
    } catch (CoreException e)
    {
      status = StatusUtils.errorStatus(ConsumptionMessages.MSG_ERROR_SERVER, e);
      return status;
    }
  }

  public void setServerInstanceId(String serverInstanceId)
  {
    this.serverInstanceId = serverInstanceId;
  }	
}
