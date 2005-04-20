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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.consumption.common.WebServiceStartServerRegistry;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Log;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;

public class CreateClientProjectCommand extends SimpleCommand
{
  private String ID_WEB = "org.eclipse.jst.ws.consumption.ui.clientProjectType.Web";
  private String ID_EJB = "org.eclipse.jst.ws.consumption.ui.clientProjectType.EJB";
  private String ID_APP_CLIENT = "org.eclipse.jst.ws.consumption.ui.clientProjectType.AppClient";
  private String ID_JAVA = "org.eclipse.jst.ws.consumption.ui.clientProjectType.Containerless";

  private boolean proxyCodegenEnabled = true;
  private String clientProjectTypeId;
  private IProject proxyProject;
  private IProject proxyProjectEAR;
  private String existingServerId_;
  private String serverFactoryId_;
  private String j2eeVersion_;
  private boolean needEAR_;
  private boolean addedProjectToServer_ = false;

  private IServer fExistingServer = null;
  private MessageUtils msgUtils_;
  private String moduleName_;
  
  /**
   * Default CTOR
   */
  public CreateClientProjectCommand( String moduleName )
  {
    super("org.eclipse.jst.ws.internal.consumption.command.common.CreateClientProjectCommand", "org.eclipse.jst.ws.internal.consumption.command.common.CreateClientProjectCommand");
    String       pluginId = "org.eclipse.jst.ws.consumption";
    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );  
	
	moduleName_ = moduleName;
  }

  public boolean isUndoable(){
  	return true;
  }

  public Status undo(Environment env){
  	Status stat = new SimpleStatus("");
    try	{
    	
      // check existingServer
      if (fExistingServer!=null) {
	    	
    	AbstractStartServer startServerCommand = null;
    	WebServiceStartServerRegistry reg = WebServiceStartServerRegistry.getInstance();
    	startServerCommand = (AbstractStartServer)reg.getServerStartByTypeId(fExistingServer.getServerType().getId());
    	if (proxyProjectEAR!=null) {
    		startServerCommand.runPostServerConfig(fExistingServer, proxyProjectEAR);
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
  
  public Status execute(Environment env)
  {
  	Status status = new SimpleStatus("");
  	//Get the existing server

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
      if (thisServerId.equals(existingServerId_)){
        existingServer = thisServer;
        fExistingServer = existingServer;
      }
    } 
    
    //Create the project
    if (proxyCodegenEnabled)
    {
      if (clientProjectTypeId == null || clientProjectTypeId.length() <= 0)
        clientProjectTypeId = (WebServicePlugin.getInstance().getProjectTopologyContext().getClientTypes())[0];
      if (ID_WEB.equals(clientProjectTypeId))
      {  
        CreateWebProjectCommand c = new CreateWebProjectCommand();
        c.setProjectName(proxyProject.getName());
        
        if (proxyProjectEAR != null)
          c.setEarProjectName(proxyProjectEAR.getName());
        else
          c.setEarProjectName("");
        
        c.setServerFactoryId(serverFactoryId_);
        c.setJ2EEVersion(j2eeVersion_);            
        c.setExistingServer(existingServer);
        c.setNeedEAR(needEAR_);
        c.setAddedProjectToServer(addedProjectToServer_);
        
        status = c.execute(env);
        if (status.getSeverity()==Status.ERROR){
      	return status;
      }
      
      Status startServerStatus = startServer(env, proxyProject);
      if (startServerStatus.getSeverity()==Status.ERROR)
      	return startServerStatus;
        
      }
      else if (ID_EJB.equals(clientProjectTypeId))
      {
        CreateEJBProjectCommand c = new CreateEJBProjectCommand();
        c.setEjbProjectName(proxyProject.getName());
        c.setEarProjectName(proxyProjectEAR.getName());
        c.setServerFactoryId(serverFactoryId_);
        c.setJ2EEVersion(j2eeVersion_);
        status = c.execute(env);
      }
      else if (ID_APP_CLIENT.equals(clientProjectTypeId))
      {
        CreateAppClientProjectCommand c = new CreateAppClientProjectCommand();
        c.setAppClientProjectName(proxyProject.getName());
        c.setEarProjectName(proxyProjectEAR.getName());
        c.setServerFactoryId(serverFactoryId_);
        c.setJ2EEVersion(j2eeVersion_);
        status = c.execute(env);
      }
      else if (ID_JAVA.equals(clientProjectTypeId))
      {
        CreateJavaProjectCommand c = new CreateJavaProjectCommand();
        c.setProjectName(proxyProject.getName());
        status = c.execute(env);
      }
         
    }

    try	{
    	
   		AbstractStartServer startServerCommand = null;
   		WebServiceStartServerRegistry reg = WebServiceStartServerRegistry.getInstance();
     	startServerCommand = (AbstractStartServer)reg.getServerStartByTypeId(existingServer.getServerType().getId());
     	if (proxyProjectEAR!=null && existingServer!=null) {
     		startServerCommand.runPreServerConfig(existingServer, proxyProjectEAR);
     		fExistingServer = existingServer;
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
  
  private Status startServer(Environment env, IProject webProject){
  	try{
  	    // start server(Bug 4377)
  	    if (needEAR_ &&  fExistingServer != null){
  	    	StartProjectCommand spc = new StartProjectCommand( moduleName_ );
  	    	spc.setSampleProject(webProject);
  	    	spc.setSampleExistingServer(fExistingServer);
  	    	spc.setCreationScenario(new Boolean("false"));
  	    	return spc.execute(env);	    	
  	    	
  	    }  		
  	}
  	catch(Exception ce){
        env.getLog().log(Log.ERROR, 5046, this, "execute", ce);
        Status status = new SimpleStatus(WebServiceConsumptionPlugin.ID, msgUtils_.getMessage("MSG_ERROR_WEB_PROJECT_CREATE"), Status.ERROR, ce);
        env.getStatusHandler().reportError(status);
        return status;		
  	}
  	return new SimpleStatus("");
  }   
  
  public void setProxyCodegenEnabled(boolean proxyCodegenEnabled)
  {
    this.proxyCodegenEnabled = proxyCodegenEnabled;
  }

  /**
   * @param proxyProjectEAR The proxyProjectEAR to set.
   */
  public void setProxyProjectEAR(IProject proxyProjectEAR)
  {
    this.proxyProjectEAR = proxyProjectEAR;
  }

  /**
   * @param proxyProject The proxyProject to set.
   */
  public void setProxyProject(IProject proxyProject)
  {
    this.proxyProject = proxyProject;
  }

  /**
   * @param clientProjectTypeId The clientProjectTypeId to set.
   */
  public void setClientProjectTypeId(String clientProjectTypeId)
  {
    this.clientProjectTypeId = clientProjectTypeId;
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
  
  public void setExistingServerId(String existingServerId)
  {
    existingServerId_ = existingServerId;
  }
  
  public void setAddedProjectToServer(boolean addedProjectToServer) 
  {
    addedProjectToServer_ = addedProjectToServer;
  }   
}
