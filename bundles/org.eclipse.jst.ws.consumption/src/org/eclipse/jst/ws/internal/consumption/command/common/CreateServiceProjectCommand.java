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
package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.consumption.common.WebServiceStartServerRegistry;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;

/**
 * 
 * 
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CreateServiceProjectCommand extends SimpleCommand {

  private boolean requiresEJB = false;  // future implementation perhaps
  private boolean requiresWeb = false;
  private String projectName;
  private String earProjectName;
  private String existingServerId;
  private String serverFactoryId_;
  private String j2eeVersion_;  
  private boolean needEAR_;
  private boolean isServiceProjectEJB_= false;
  private boolean addedProjectToServer_ = false;

  private IServer fExistingServer = null;
  /**
   * Default CTOR
   */
  public CreateServiceProjectCommand() {

    super("org.eclipse.jst.ws.internal.consumption.command.common.CreateServiceProjectCommand",
        "org.eclipse.jst.ws.internal.consumption.command.common.CreateServiceProjectCommand");
  }
  
  public boolean isUndoable(){
  	return true;
  }

  public Status undo(Environment env){
  	Status stat = new SimpleStatus("");
    try	{
    	
      // check existingServer
      if (fExistingServer!=null) {

        // get EAR project
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject earProject = null;
        if (earProjectName!=null && earProjectName.length()>0) {
          earProject = root.getProject(earProjectName);
    	}    
        
    	AbstractStartServer startServerCommand = null;
    	WebServiceStartServerRegistry reg = WebServiceStartServerRegistry.getInstance();
    	startServerCommand = (AbstractStartServer)reg.getServerStartByTypeId(fExistingServer.getServerType().getId());
    	if (earProject!=null) {
    		startServerCommand.runPostServerConfig(fExistingServer, earProject);
    	}
      }
   	}
    catch (CoreException ce ) {
      IStatus embeddedStatus = ce.getStatus();
      stat = EnvironmentUtils.convertIStatusToStatus(embeddedStatus);
      env.getStatusHandler().reportError(stat);
  		return stat;
  	}  	
  	
  	return stat;
  }  
  
  public Status execute(Environment env) {
	
    if (projectName == null) {
      return new SimpleStatus("");
    }
    
  	Status status = new SimpleStatus("");
    IServer[] servers = ServerCore.getServers();
    IServer existingServer = null;    
    fExistingServer = null; 
    for (int i=0; i<servers.length; i++)
    {
      IServer thisServer = (IServer)servers[i];
      IServerWorkingCopy wc = null;
      String thisServerId = null;
      
      if (thisServer!=null) {
      	wc = thisServer.createWorkingCopy();
      	thisServerId = (wc!=null ? wc.getId() : null);
      }
      if (thisServerId.equals(existingServerId)) {
        existingServer = thisServer;
      }
    }
    if (isServiceProjectEJB_)
    {
      CreateEJBProjectCommand c = new CreateEJBProjectCommand();
      c.setEjbProjectName(projectName);
      c.setEarProjectName(earProjectName);
      c.setServiceExistingServer(existingServer);
      c.setServerFactoryId(serverFactoryId_);
      c.setJ2EEVersion(j2eeVersion_);
      status = c.execute(env);
    } 
    else
    {
      CreateWebProjectCommand c = new CreateWebProjectCommand();
      c.setProjectName(projectName);
      c.setEarProjectName(earProjectName);
      c.setExistingServer(existingServer);
      c.setServerFactoryId(serverFactoryId_);
      c.setJ2EEVersion(j2eeVersion_);
      c.setNeedEAR(needEAR_);
      c.setAddedProjectToServer(addedProjectToServer_);
      status = c.execute(env);
    }
    
    try	{
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject earProject = null;
        if (earProjectName!=null && earProjectName.length()>0)
        {
          earProject = root.getProject(earProjectName);
        }    	
    	
   		AbstractStartServer startServerCommand = null;
   		WebServiceStartServerRegistry reg = WebServiceStartServerRegistry.getInstance();
     	startServerCommand = (AbstractStartServer)reg.getServerStartByTypeId(existingServer.getServerType().getId());
    	startServerCommand.runPreServerConfig(existingServer, earProject);
    	fExistingServer = existingServer;
   	}
    catch (CoreException ce ) {
    	IStatus embeddedStatus = ce.getStatus();
    	status = EnvironmentUtils.convertIStatusToStatus(embeddedStatus);
    	env.getStatusHandler().reportError(status);
  		return status;
  	}
  	
  	return status;    
    
  }

  /**
   * @param earProjectName The earProjectName to set.
   * @todo Generated comment
   */
  public void setEarProjectName(String earProjectName) {

    this.earProjectName = earProjectName;
  }
  
  public void setExistingServerId(String existingServerId)
  {
    this.existingServerId = existingServerId;
  }
  /**
   * @param projectName The projectName to set.
   * @todo Generated comment
   */
  public void setProjectName(String projectName) {

    this.projectName = projectName;
  }
  /**
   * @param requiresEJB The requiresEJB to set.
   * @todo Generated comment
   */
  public void setRequiresEJB(boolean requiresEJB) {

    this.requiresEJB = requiresEJB;
  }
  /**
   * @param requiresWeb The requiresWeb to set.
   * @todo Generated comment
   */
  public void setRequiresWeb(boolean requiresWeb) {

    this.requiresWeb = requiresWeb;
  }
  
  public void setServerFactoryId(String serverFactoryId)
  {
  	serverFactoryId_ = serverFactoryId;
  }
  
  public void setJ2EEVersion(String j2eeVersion)
  {
  	j2eeVersion_ = j2eeVersion;	
  }
  
  public void setNeedEAR(boolean needEAR)
  {
    needEAR_ = needEAR;
  }  
  
  public void setIsServiceProjectEJB(boolean isServiceProjectEJB)
  {
    this.isServiceProjectEJB_ = isServiceProjectEJB;
  }
  
  
  public void setAddedProjectToServer(boolean addedProjectToServer) 
  {
    addedProjectToServer_ = addedProjectToServer;
  }
}
