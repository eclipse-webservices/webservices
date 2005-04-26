/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 */
package org.eclipse.jst.ws.internal.consumption.ui.widgets.extensions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.AbstractStartServer;
import org.eclipse.jst.ws.internal.consumption.common.WebServiceStartServerRegistry;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebServiceClient;

public class ClientExtensionOutputCommand extends SimpleCommand
{
  private String  proxyBean_;
  private boolean genProxy_;
  private String setEndpointMethod;
	private IWebServiceClient webServiceClient_;
  
  private String existingServerId_;
  private String earProjectName_;  
  private IServer fExistingServer = null;
  
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
        if (earProjectName_!=null && earProjectName_.length()>0) {
          earProject = root.getProject(earProjectName_);
    	}    	
    	
    	AbstractStartServer startServerCommand = null;
    	WebServiceStartServerRegistry reg = WebServiceStartServerRegistry.getInstance();
    	startServerCommand = (AbstractStartServer)reg.getServerStartByTypeId(fExistingServer.getServerType().getId());
    	if (earProject!=null) {
    		startServerCommand.runPreServerConfig(fExistingServer, earProject);
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
  
  public Status execute(Environment env){
  	
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
      
      if (thisServerId.equals(existingServerId_))
        existingServer = thisServer;
    }    	
    try	{
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject earProject = null;
        if (earProjectName_!=null && earProjectName_.length()>0)
        {
          earProject = root.getProject(earProjectName_);
        }    	
    	
        if (existingServer!=null){
   		  AbstractStartServer startServerCommand = null;
   		  WebServiceStartServerRegistry reg = WebServiceStartServerRegistry.getInstance();
     	  startServerCommand = (AbstractStartServer)reg.getServerStartByTypeId(existingServer.getServerType().getId());
     	  if (earProject!=null){
     	  	startServerCommand.runPostServerConfig(existingServer, earProject);
     	  	fExistingServer = existingServer;
     	  }
        }
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
   * @return Returns the proxyBean.
   */
  
  public String getProxyBean()
  {
    return webServiceClient_.getWebServiceClientInfo().getImplURL();
  }
  /**
   * @param proxyBean The proxyBean to set.
   */
	/*
  public void setProxyBean(String proxyBean)
  {
    this.proxyBean_ = proxyBean;
  }
  */
  
  public boolean getGenerateProxy()
  {
    //return genProxy_;
		return true;
  }
  
	/*
  public void setGenerateProxy( boolean genProxy )
  {
    genProxy_ = genProxy;
  }
  */
  
  /**
   * @return Returns the setEndpointMethod.
   */
  public String getSetEndpointMethod()
  {
    //return setEndpointMethod;
		return "setEndpoint";
  }
  /**
   * @param setEndpointMethod The setEndpointMethod to set.
   */
	/*
  public void setSetEndpointMethod(String setEndpointMethod)
  {
    this.setEndpointMethod = setEndpointMethod;
  }
  */
  /**
   * @param earProjectName The earProjectName to set.
   */
  public void setEarProjectName(String earProjectName) {
  	this.earProjectName_ = earProjectName;
  }
  /**
   * @param existingServerId The existingServerId to set.
   */
  public void setExistingServerId(String existingServerId) {
  	this.existingServerId_ = existingServerId;
  }  
	
	public void setWebServiceClient(IWebServiceClient wsc)
	{
		webServiceClient_ = wsc;
	}
	
	public String getServerInstanceId()
	{
		return webServiceClient_.getWebServiceClientInfo().getServerInstanceId();
	}
	
	public String getServerFactoryId()
	{
		return webServiceClient_.getWebServiceClientInfo().getServerFactoryId();
	}
}
