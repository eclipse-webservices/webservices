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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.common.WebServiceStartServerRegistry;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.ProgressUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.server.core.IServer;

public class StartProjectCommand extends EnvironmentalOperation 
{

private MessageUtils msgUtils_;

private Boolean creationScenario_ = Boolean.TRUE;

private boolean isWebProjectStartupRequested_;
private IProject serviceProject_;
private IProject sampleProject_;
private String serviceServerTypeID_;
private String sampleServerTypeID_;
private IServer serviceExistingServer_;
private IServer sampleExistingServer_;
private String  moduleName_;

// rm private Model model_;

/**
 * Default CTOR;
 */
public StartProjectCommand( String moduleName ) {
	String pluginId = "org.eclipse.jst.ws.consumption";
	msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	moduleName_ = moduleName;
}

/**
 * Default CTOR;
 */
public StartProjectCommand(boolean creationScenario ) {
	String pluginId = "org.eclipse.jst.ws.consumption";
	msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	creationScenario_ = new Boolean(creationScenario);
}

/**
 * Execute the command
 */
public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
{
    Environment env = getEnvironment();    
  
    IStatus status = Status.OK_STATUS;
    ProgressUtils.report( monitor, msgUtils_.getMessage("PROGRESS_INFO_START_WEB_PROJECT"));
    
    
    IProject project = ((creationScenario_.booleanValue()) ? serviceProject_ : sampleProject_);
    String serverTypeID = ((creationScenario_.booleanValue()) ? serviceServerTypeID_ : sampleServerTypeID_);
    IServer server = ((creationScenario_.booleanValue()) ? serviceExistingServer_ : sampleExistingServer_);   
    if (serverTypeID == null && server!=null) {
    	serverTypeID = server.getServerType().getId();
    }
    if (project == null)
    {
      status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_PROJECT_NOT_FOUND") );  
      env.getStatusHandler().reportError(status);
      return status;
    }	
    IServer instance = ServerUtils.getServerForModule(ServerUtils.getModule(project, moduleName_), serverTypeID, server, true, monitor );
    if (instance == null)
    {
      status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_INSTANCE_NOT_FOUND") );
      env.getStatusHandler().reportError(status);
      return status;
    }

   try
   	{
   		AbstractStartServer startServerCommand = null;
   		WebServiceStartServerRegistry reg = WebServiceStartServerRegistry.getInstance();
     	startServerCommand = (AbstractStartServer)reg.getServerStartByTypeId(instance.getServerType().getId());
    	startServerCommand.StartServer(project, instance, monitor, isWebProjectStartupRequested_);
    	return status;
   	}
  catch (CoreException ce )
  	{
    	IStatus embeddedStatus = ce.getStatus();
    	status = embeddedStatus;
    	env.getStatusHandler().reportError(status);
  		return status;
  	}
   catch (Exception e)
   	{
       status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_SERVER"), e);
       env.getStatusHandler().reportError(status);
   	   return status;
   	}
   
}

public void setCreationScenario(Boolean creationScenario)
{
	creationScenario_ = creationScenario;
}

public void setServiceProject(IProject serviceProject)
{
  serviceProject_ = serviceProject;
}

public void setSampleProject(IProject sampleProject)
{
  sampleProject_ = sampleProject;
}

public void setServiceServerTypeID(String serviceServerTypeID)
{
  serviceServerTypeID_ = serviceServerTypeID;
}

public void setSampleServerTypeID(String sampleServerTypeID)
{
  sampleServerTypeID_ = sampleServerTypeID;
}

public void setServiceExistingServer(IServer serviceExistingServer)
{
  serviceExistingServer_ = serviceExistingServer;
  if (serviceExistingServer_ != null)
    setServiceServerTypeID(serviceExistingServer_.getServerType().getId());
}

public void setSampleExistingServer(IServer sampleExistingServer)
{
  sampleExistingServer_ = sampleExistingServer;
  if (sampleExistingServer_ != null)
    setSampleServerTypeID(sampleExistingServer_.getServerType().getId());
}

public void setIsWebProjectStartupRequested(boolean webProjectStartupRequested)
{
  isWebProjectStartupRequested_ = webProjectStartupRequested;
}

}
