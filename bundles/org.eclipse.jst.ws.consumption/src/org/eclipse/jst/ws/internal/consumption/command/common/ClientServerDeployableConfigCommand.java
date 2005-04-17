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
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;

public class ClientServerDeployableConfigCommand extends SimpleCommand {

  private String LABEL = "ClientServerDeployableConfigCommand";
  private String DESCRIPTION = "Ensure Deployable is added to Server Configuration for client projects";
  private MessageUtils msgUtils_;
  private Boolean creationScenario_ = Boolean.TRUE;
  private TypeRuntimeServer clientIds_;
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
  public ClientServerDeployableConfigCommand() {
    String pluginId = "org.eclipse.jst.ws.consumption";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
    setDescription(DESCRIPTION);
    setName(LABEL);
  }

  /**
   * Default CTOR
   */
  public ClientServerDeployableConfigCommand(boolean creationScenario) {
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

      // determine client or service project
      IProject project =  sampleProject_;
      if (project == null)
        return status;

      // get sample server factory Id and instanceId
      String serverFactoryId = clientIds_.getServerId();
      String existingServerInstId = clientIds_.getServerInstanceId();
      IServer server = null;
      if (existingServerInstId ==null) {
      	server= sampleExistingServer_;
      }
      else {
      // get an IServer object; either existing or create one
       server = ServerCore.findServer(existingServerInstId);
      }
   
      if (server != null) {
      	// find exact server for given project
        IServer[] servers = ServerUtil.getServersByModule(ResourceUtils.getModule(project), null);
        boolean foundServer = false;
        for (int i = 0; i < servers.length; i++) {
          if (servers[i].equals(server))
            foundServer = true;
        }
        if (!foundServer) {
        	Status modStatus = ServerUtils.getInstance().modifyModules(env ,server, ResourceUtils.getModule(project), true, EnvironmentUtils.getIProgressMonitor(env));
        	if (modStatus.getSeverity()==Status.ERROR)
        		return modStatus;
        	
            addedProjectToServer_ = true;
        }
      }
      else {
        if (ResourceUtils.isTrueJavaProject(project))
        {
          //Don't attempt to add a Java project to a server, therefore passing in null for the module.
          server = ServerUtils.getInstance().createServer(env, null, serverFactoryId, EnvironmentUtils.getIProgressMonitor(env));
        }
        else
        {
          server = ServerUtils.getInstance().createServer(env, ResourceUtils.getModule(project), serverFactoryId, EnvironmentUtils.getIProgressMonitor(env));
        }
        if (server == null) {
          status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_CREATE_SERVER"), Status.ERROR);
          env.getStatusHandler().reportError(status);
          return status;
        }
        
        IModule[] parentModules = server.getRootModules(ResourceUtils.getModule(project),null);
        if (parentModules!=null && parentModules.length!=0) {
          if (!ServerUtil.containsModule(server, ResourceUtils.getModule(project), null)) {
            Status mmStatus = ServerUtils.getInstance().modifyModules(env, server, ResourceUtils.getModule(project), true, EnvironmentUtils.getIProgressMonitor(env));
            if (mmStatus.getSeverity()==Status.ERROR)
              return mmStatus;
          }
        }        

      }
      
      //
      String projectURL = ResourceUtils.getWebProjectURL(project, serverFactoryId, server);
      if (creationScenario_.booleanValue()) {
        serviceExistingServer_ = server;
        serviceProjectURL_ = projectURL;
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
	if (serviceExistingServer_!=null){
	  wc = serviceExistingServer_.createWorkingCopy();
	  id = (wc!=null ? wc.getId() : null);
	}
	return id;
  }

  public void setSampleExistingServer(IServer sampleExistingServer) {
    sampleExistingServer_ = sampleExistingServer;
  }

  public IServer getSampleExistingServer() {
    return sampleExistingServer_;
  }

  public String getSampleExistingServerInstId() {
	String id = null;
	IServerWorkingCopy wc = null;
	if (sampleExistingServer_!=null){
	  wc = sampleExistingServer_.createWorkingCopy();
	  id = (wc!=null ? wc.getId() : null);
	}
	return id;
  }
  
  public String getServiceProjectURL() {
    return serviceProjectURL_;
  }

  public String getSampleProjectURL() {
    return sampleProjectURL_;
  }
  
  public void setClientTypeRuntimeServer(TypeRuntimeServer trs){
  	this.clientIds_ = trs;
  }

  /**
   * @return Returns the addedProjectToServer_.
  */
  public boolean getAddedProjectToServer() {
    return addedProjectToServer_;
  }  
}
