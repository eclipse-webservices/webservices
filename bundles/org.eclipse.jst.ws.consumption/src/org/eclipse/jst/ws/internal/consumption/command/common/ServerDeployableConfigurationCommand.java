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
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerUtil;

public class ServerDeployableConfigurationCommand extends SimpleCommand {

  private String LABEL = "ServerDeployableConfigurationCommand";
  private String DESCRIPTION = "Ensure Deployable is added to Server Configuration";
  private MessageUtils msgUtils_;
  private Boolean creationScenario_ = Boolean.TRUE;
  private TypeRuntimeServer clientIds_;
  private IProject serviceProject_;
  private IProject sampleProject_;
  private String serviceServerTypeID_;
  private String sampleServerTypeID_;
  private IServer serviceExistingServer_;
  private IServer sampleExistingServer_;
  private String serviceProjectURL_;
  private String sampleProjectURL_;
  
  //addedProjectToServer_ is set to true if
  //- the project exists AND
  //- the server exists (i.e. this command did not create it) AND
  //- this command added the project to the server
  private boolean addedProjectToServer_ = false;

  /**
   * Default CTOR
   */
  public ServerDeployableConfigurationCommand() {
    String pluginId = "org.eclipse.jst.ws.consumption";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
    setDescription(DESCRIPTION);
    setName(LABEL);
  }

  /**
   * Default CTOR
   */
  public ServerDeployableConfigurationCommand(boolean creationScenario) {
    String pluginId = "org.eclipse.jst.ws.consumption";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
    setDescription(DESCRIPTION);
    setName(LABEL);
    creationScenario_ = new Boolean(creationScenario);
  }

  /**
   * execute
   */
  public Status execute(Environment env) {

    Status status = new SimpleStatus("");
    try {

      if (!(creationScenario_.booleanValue()) )
        return status;

      // determine client or service project
      IProject project = ((creationScenario_.booleanValue()) ? serviceProject_ : sampleProject_);
      if (project == null)
        return status;

      sampleServerTypeID_ = clientIds_.getServerId();
      
      // determine service or sample server factory Id
      String serverFactoryId = ((creationScenario_.booleanValue()) ? serviceServerTypeID_ : sampleServerTypeID_);

      // get an IServer object; either existing or create one
      IServer server = ((creationScenario_.booleanValue()) ? serviceExistingServer_ : sampleExistingServer_);
      if (server != null) {
      	// find exact server for given project
        IServer[] servers = ServerUtil.getServersByModule(ResourceUtils.getModule(project));
        boolean foundServer = false;
        for (int i = 0; i < servers.length; i++) {
          if (servers[i].equals(server))
            foundServer = true;
        }
        if (!foundServer) {
          Status mmStatus = ServerUtils.getInstance().modifyModules(env, server, ResourceUtils.getModule(project), true, EnvironmentUtils.getIProgressMonitor(env));
          if (mmStatus.getSeverity()==Status.ERROR)
        	return mmStatus;            
          addedProjectToServer_ = true;
        }
      }
      else {
        if (!(creationScenario_.booleanValue()) && serviceServerTypeID_ == sampleServerTypeID_) //Client
        {
          server = serviceExistingServer_;
        }
        else {
          server = ServerUtils.getInstance().createServer(env, ResourceUtils.getModule(project), serverFactoryId, true, EnvironmentUtils.getIProgressMonitor(env));
        }
        if (server == null) {
          status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_CREATE_SERVER"), Status.ERROR);
          env.getStatusHandler().reportError(status);
          return status;
        }
        Status mmStatus = ServerUtils.getInstance().modifyModules(env, server, ResourceUtils.getModule(project), true, EnvironmentUtils.getIProgressMonitor(env));
        if (mmStatus.getSeverity()==Status.ERROR)
        	return mmStatus;          
      }
      
      //
      String projectURL = ResourceUtils.getWebProjectURL(project, serverFactoryId, server);
      if (creationScenario_.booleanValue()) {
        serviceExistingServer_ = server;
        serviceProjectURL_ = projectURL;
        if (serviceServerTypeID_.equals(sampleServerTypeID_) && clientIds_.getServerInstanceId()==null){
        	sampleExistingServer_ = serviceExistingServer_;
        }
      }
      else {
        sampleExistingServer_ = server;
        sampleProjectURL_ = projectURL;
      }
      return status;
    }
    catch (Exception e) {
      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_BAD_SERVER_CONFIG"), Status.ERROR, e);
      env.getStatusHandler().reportError(status);
      return status;
    }
  }

  public void setServiceProject(IProject serviceProject) {
    serviceProject_ = serviceProject;
  }

  public void setSampleProject(IProject sampleProject) {
    sampleProject_ = sampleProject;
  }

  public void setServiceServerTypeID(String serviceServerTypeID) {
    serviceServerTypeID_ = serviceServerTypeID;
  }

  public void setSampleServerTypeID(String sampleServerTypeID) {
    sampleServerTypeID_ = sampleServerTypeID;
  }

  public void setServiceExistingServer(IServer serviceExistingServer) {
    serviceExistingServer_ = serviceExistingServer;
  }

  public IServer getServiceExistingServer() {
    return serviceExistingServer_;
  }

  public String getServiceExistingServerInstId() {
	String id = null;
	IServerWorkingCopy wc = null;
	try {
		if (serviceExistingServer_!=null){
		  wc = serviceExistingServer_.getWorkingCopy();
		  id = (wc!=null ? wc.getId() : null);
		}
	} finally {
		if (wc!=null) {
			wc.release();
		}
	}
	return id;
  }

  public String getSampleExistingServerInstId(){
	String id = null;
	IServerWorkingCopy wc = null;
	try {
		if (sampleExistingServer_!=null){
		  wc = sampleExistingServer_.getWorkingCopy();
		  id = (wc!=null ? wc.getId() : null);
		}
	} finally {
		if (wc!=null) {	
			wc.release();
		}
	}
	return id;
	
  }
  
  public void setSampleExistingServer(IServer sampleExistingServer) {
    sampleExistingServer_ = sampleExistingServer;
  }

  public IServer getSampleExistingServer() {
    return sampleExistingServer_;
  }

  public String getServiceProjectURL() {
    return serviceProjectURL_;
  }

  public String getSampleProjectURL() {
    return sampleProjectURL_;
  }
  
  public void setClientTypeRuntimeServer(TypeRuntimeServer clientId){
  	this.clientIds_ = clientId;
  }
  
  /**
   * @return Returns the addedProjectToServer_.
  */
  public boolean getAddedProjectToServer() {
    return addedProjectToServer_;
  }
}
