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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.earcreation.EARNatureRuntime;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.internal.servertarget.IServerTargetConstants;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebModuleCreationDataModel;
import org.eclipse.jst.j2ee.internal.web.archive.operations.WebModuleCreationOperation;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.common.WebServiceStartServerRegistry;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.Log;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;

public class CreateWebProjectCommand extends SimpleCommand {

  private final static String DESCRIPTION = "TASK_DESC_CREATE_WEB_PROJECT";
  private final static String LABEL = "TASK_LABEL_CREATE_WEB_PROJECT";
  private final String DEFAULT_EAR_NAME = "WebServiceEAR";
  private String projectName_ = null;
  private String earProjectName_ = null;
  private boolean proxyCodegenEnabled = true;
  private String serverFactoryId_;
  private String j2eeVersion_;
  private IServer existingServer_;
  private boolean needEAR_;
  private boolean weAddedProjectToServer_ = false;
  private MessageUtils msgUtils_;

  /**
   * Default CTOR
   */
  public CreateWebProjectCommand() {

    super(LABEL, DESCRIPTION);
    String       pluginId = "org.eclipse.jst.ws.consumption";
    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
    //setRunInWorkspaceModifyOperation(false);
  }

  /**
   * Create service Web Project using the parameter Project name Note: to be
   * deprecated
   */
  public CreateWebProjectCommand(String serviceProjectName) {

    super(LABEL, DESCRIPTION);
    projectName_ = serviceProjectName;
    //setRunInWorkspaceModifyOperation(false);
  }

  public Status execute(Environment env) {

    if (projectName_ == null || projectName_.length() == 0) {
    	env.getLog().log(Log.ERROR, 5044, this, "execute", "Web project name not set");
      Status status = new SimpleStatus(WebServiceConsumptionPlugin.ID, msgUtils_.getMessage("MSG_ERROR_WEB_PROJECT_CREATE"), Status.ERROR, null);
      env.getStatusHandler().reportError(status);
      return status; 
    }

    if (existingServer_ == null)
    {
      env.getLog().log(Log.ERROR, 5044, this, "execute", "Server is null");
      Status status = new SimpleStatus(WebServiceConsumptionPlugin.ID, msgUtils_.getMessage("MSG_ERROR_WEB_PROJECT_CREATE"), Status.ERROR, null);
      env.getStatusHandler().reportError(status);
      return status;      
    }
    
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IProject webProject = root.getProject(projectName_);
    IProject earProject = null;
    if (earProjectName_!=null && earProjectName_.length()>0)
    {
      earProject = root.getProject(earProjectName_);
    }
    
    boolean projectExists = webProject != null && webProject.exists();
    boolean addedToServer = false;
    if (projectExists)
    {
      addedToServer = ServerUtil.containsModule(existingServer_,ResourceUtils.getModule(webProject), new NullProgressMonitor());
    }
    
    boolean earExists = earProject!=null && earProject.exists();
    boolean earAddedToServer = true;
    if (earExists)
    {
      earAddedToServer = ServerUtil.containsModule(existingServer_,ResourceUtils.getModule(earProject), new NullProgressMonitor());
    }
    
    boolean areAssociated = false;
    if (projectExists && earExists)
    {
      areAssociated = J2EEUtils.isEARAssociated(webProject, earProject);
    }
    
    boolean serverRequiresEARRemoval = true;
    try
    {
      // check the Registry to see if the server requires EAR removal
      if (serverFactoryId_!=null)
      {
        WebServiceStartServerRegistry wssReg = WebServiceStartServerRegistry.getInstance();
        serverRequiresEARRemoval = wssReg.isRemoveEARRequired(serverFactoryId_);    		
      }    
    } catch (CoreException ce)
    {
      env.getLog().log(Log.ERROR, 5046, this, "execute", ce);
      Status status = new SimpleStatus(WebServiceConsumptionPlugin.ID, msgUtils_.getMessage("MSG_ERROR_WEB_PROJECT_CREATE"), Status.ERROR, ce);
      env.getStatusHandler().reportError(status);
      return status;
    }
    
    
    if (!needEAR_ && projectExists && addedToServer && !weAddedProjectToServer_)
    {
      return new SimpleStatus("");
    }
    
    // existing project, ear and server configuration
    if (needEAR_ && projectExists && earExists && earAddedToServer && areAssociated && !weAddedProjectToServer_){
    	return new SimpleStatus("");
    }
    
    //Always stop the server
    Status stoppingStatus = stopServer(env);
    if(stoppingStatus.getSeverity() == Status.ERROR)
        return stoppingStatus;
    
    if (!needEAR_ && projectExists && addedToServer)
    {
      return new SimpleStatus("");
    }       
    else if (!needEAR_ && projectExists && !addedToServer)
    {
      Status mmStatus = ServerUtils.getInstance().modifyModules(env, existingServer_, ResourceUtils.getModule(webProject) , true, new NullProgressMonitor());
      if (mmStatus.getSeverity()==Status.ERROR)
    	return mmStatus;      
    }
    else if (!needEAR_ && !projectExists)
    {
      Status status = createWebProject(env);
      if(status.getSeverity() == Status.ERROR)
        return status;
      webProject = root.getProject(projectName_);
      
      Status mmStatus = ServerUtils.getInstance().modifyModules(env, existingServer_, ResourceUtils.getModule(webProject) , true, new NullProgressMonitor());
      if (mmStatus.getSeverity()==Status.ERROR)
    	return mmStatus;    
    }
    else if (projectExists && earExists && areAssociated && addedToServer)
    {
      return new SimpleStatus("");
    }
    else if (projectExists && earExists && areAssociated && !addedToServer)
    {
      Status mmStatus = ServerUtils.getInstance().modifyModules(env, existingServer_, ResourceUtils.getModule(webProject) , true, new NullProgressMonitor());
      if (mmStatus.getSeverity()==Status.ERROR)
    	return mmStatus;    
    }
    else if (projectExists && !earExists)
    {
      //Create the EAR project
      Status status = createEARProject(env);
      if (status.getSeverity() == Status.ERROR)
        return status;
      earProject = root.getProject(earProjectName_);
      
      //Associate the Web project and the EAR project
      J2EEUtils.associateWebProject(webProject, earProject);
      
      //Add to server
      Status mmStatus = ServerUtils.getInstance().modifyModules(env, existingServer_, ResourceUtils.getModule(webProject) , true, new NullProgressMonitor());
      if (mmStatus.getSeverity()==Status.ERROR)
    	return mmStatus;    
    }
    else if (!projectExists && !earExists)
    {
      //Create EAR
      Status status = createEARProject(env);
      if (status.getSeverity() == Status.ERROR)
        return status;
      earProject = root.getProject(earProjectName_);
      
      //Create project
      status = createWebProject(env);
      if(status.getSeverity() == Status.ERROR)
        return status;
      webProject = root.getProject(projectName_);
      
      //Add to server
      Status mmStatus = ServerUtils.getInstance().modifyModules(env, existingServer_, ResourceUtils.getModule(webProject) , true, new NullProgressMonitor());
      if (mmStatus.getSeverity()==Status.ERROR)
    	return mmStatus;      
    }
    else if (!projectExists && earExists)
    {
      //Remove the ear from the server if necessary
      if (earAddedToServer && serverRequiresEARRemoval)
      {
        // Stopping server and removing EAR from Server
        if (existingServer_.getServerState() != IServer.STATE_STOPPED)
        {
          existingServer_.synchronousStop(false);
        }        
        Status mmStatus = ServerUtils.getInstance().modifyModules(env, existingServer_, ResourceUtils.getModule(earProject), false, new NullProgressMonitor());
        if (mmStatus.getSeverity()==Status.ERROR)
        	return mmStatus;      
      }
      
      //Create the Web project
      Status status = createWebProject(env);
      if(status.getSeverity() == Status.ERROR)
        return status;
      webProject = root.getProject(projectName_);

      boolean earAddedToServerFinal = ServerUtil.containsModule(existingServer_,ResourceUtils.getModule(earProject), new NullProgressMonitor());
      if (!earAddedToServerFinal)
      {
      	Status mmStatus = ServerUtils.getInstance().modifyModules(env, existingServer_, ResourceUtils.getModule(webProject) , true, new NullProgressMonitor());  
        if (mmStatus.getSeverity()==Status.ERROR)
        	return mmStatus;      
      }
      
    }
    else if (projectExists && earExists && !areAssociated)
    {
      //Remove the ear from the server if necessary
      if (earAddedToServer && serverRequiresEARRemoval)
      {
        // Stopping server and removing EAR from Server
        if (existingServer_.getServerState() != IServer.STATE_STOPPED)
        {
          existingServer_.synchronousStop(false);
        }        
        Status mmStatus = ServerUtils.getInstance().modifyModules(env, existingServer_, ResourceUtils.getModule(earProject), false, new NullProgressMonitor());
        if (mmStatus.getSeverity()==Status.ERROR)
        	return mmStatus;      
      }
      
      //Associate the Web project and the EAR project
      J2EEUtils.associateWebProject(webProject, earProject);
      
      boolean earAddedToServerFinal = ServerUtil.containsModule(existingServer_,ResourceUtils.getModule(earProject), new NullProgressMonitor());      
      if (!earAddedToServerFinal)
      {
      	Status mmStatus = ServerUtils.getInstance().modifyModules(env, existingServer_, ResourceUtils.getModule(webProject) , true, new NullProgressMonitor());  
        if (mmStatus.getSeverity()==Status.ERROR)
        	return mmStatus;      
      }      
      
    }

    return new SimpleStatus("");
  }

  private Status createWebProject(Environment env)
  {
    try
    {
    	
    WebModuleCreationDataModel projectInfo = new WebModuleCreationDataModel();
    projectInfo.setProperty(WebModuleCreationDataModel.PROJECT_NAME, projectName_);
    String finalJ2EEVersion = null;
    if (j2eeVersion_ != null && j2eeVersion_.length()>0)
    {
      projectInfo.setProperty(WebModuleCreationDataModel.J2EE_VERSION, new Integer(j2eeVersion_));
      finalJ2EEVersion = j2eeVersion_;
    }        
    else        
    {
      if (earProjectName_ !=null && earProjectName_.length()>0)
      {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject earProject = root.getProject(earProjectName_);
        if (earProject != null && earProject.exists())
        {
          EARNatureRuntime ear = EARNatureRuntime.getRuntime(earProject);
          int earVersion = ear.getJ2EEVersion();
          projectInfo.setProperty(WebModuleCreationDataModel.J2EE_VERSION, new Integer(earVersion));
          finalJ2EEVersion = String.valueOf(earVersion);
        }
        else
        {          
          projectInfo.setProperty(WebModuleCreationDataModel.J2EE_VERSION, new Integer(J2EEVersionConstants.J2EE_1_3_ID));
          finalJ2EEVersion = String.valueOf(J2EEVersionConstants.J2EE_1_3_ID);
        }
      }
      else
      {
        projectInfo.setProperty(WebModuleCreationDataModel.J2EE_VERSION, new Integer(J2EEVersionConstants.J2EE_1_3_ID));
        finalJ2EEVersion = String.valueOf(J2EEVersionConstants.J2EE_1_3_ID);        
      }
      
    }    
    String runtimeTargetId = null;
    if (serverFactoryId_!=null && serverFactoryId_.length()>0)
    {
      runtimeTargetId = ServerUtils.getServerTargetIdFromFactoryId(serverFactoryId_,IServerTargetConstants.WEB_TYPE, finalJ2EEVersion); 
      projectInfo.setProperty(WebModuleCreationDataModel.SERVER_TARGET_ID, runtimeTargetId );
      projectInfo.setProperty(WebModuleCreationDataModel.ADD_SERVER_TARGET, Boolean.TRUE);      
    } 
    
    if (earProjectName_ !=null && earProjectName_.length()>0)
    {
      projectInfo.setProperty(WebModuleCreationDataModel.EAR_PROJECT_NAME, earProjectName_);
      projectInfo.setProperty(WebModuleCreationDataModel.ADD_TO_EAR, Boolean.TRUE);
    }
    else
    {
      projectInfo.setProperty(WebModuleCreationDataModel.ADD_TO_EAR, Boolean.FALSE);
    }
    	
    

    //Create and run the operation
    WebModuleCreationOperation op = new WebModuleCreationOperation(projectInfo);
      op.run(new NullProgressMonitor());    
    return new SimpleStatus("");
    } catch (Exception e)
    {
      env.getLog().log(Log.ERROR, 5046, this, "createWebProject", e);
      Status status = new SimpleStatus(WebServiceConsumptionPlugin.ID, msgUtils_.getMessage("MSG_ERROR_WEB_PROJECT_CREATE"), Status.ERROR, e);
      env.getStatusHandler().reportError(status);
      return status;      
    }
  }
  
  private Status createEARProject(Environment env)
  {
    CreateEARProjectCommand t = new CreateEARProjectCommand();
    t.setEarProjectName(earProjectName_);
    t.setServerFactoryId(serverFactoryId_);
    t.setJ2EEVersion(j2eeVersion_);
    Status status = t.execute(env);
    return status;
  }             
  
  private Status stopServer(Environment env)
  {
 	
    try
   	{
   		WebServiceStartServerRegistry reg = WebServiceStartServerRegistry.getInstance();
   		AbstractStartServer startServerCommand = (AbstractStartServer)reg.getServerStartByTypeId(existingServer_.getServerType().getId());
    	startServerCommand.stopServer(existingServer_);
    	return new SimpleStatus("");
   	}
  catch (CoreException ce )
  	{
    	IStatus embeddedStatus = ce.getStatus();
    	Status status = EnvironmentUtils.convertIStatusToStatus(embeddedStatus);
    	env.getStatusHandler().reportError(status);
  		return status;
  	}  	
  	

  }
  
  // To be deprecated
  /*
  private Status createWebProject(String webProjectName, String earProjectName, Environment env) {

    try {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      IProject earProject = root.getProject(earProjectName);
      if (earProject != null && !earProject.exists()) {
        CreateEARProjectCommand c = new CreateEARProjectCommand();
        c.setEarProjectName(earProjectName);
        c.setServerFactoryId(serverFactoryId_);
        c.setJ2EEVersion(j2eeVersion_);
        c.execute(env);
        earProject = root.getProject(earProjectName);
      }
      IProject webProject = root.getProject(webProjectName);
      if (webProject != null && !webProject.exists()) {
        WebModuleCreationDataModel info = new WebModuleCreationDataModel();
        info.setProperty(WebModuleCreationDataModel.PROJECT_NAME, webProjectName);
        info.setProperty(WebModuleCreationDataModel.EAR_PROJECT_NAME, earProjectName);
        //info.setProperty(WebModuleCreationDataModel.ADD_TO_EAR, Boolean.TRUE);
        info.setProperty(WebModuleCreationDataModel.ADD_TO_EAR, Boolean.FALSE);
        if (serverFactoryId_!=null && serverFactoryId_.length()>0)
        {
          String runtimeTargetId = ServerUtils.getRuntimeTargetIdFromFactoryId(serverFactoryId_); 
          info.setProperty(WebModuleCreationDataModel.SERVER_TARGET_ID, runtimeTargetId );
          info.setProperty(WebModuleCreationDataModel.ADD_SERVER_TARGET, Boolean.TRUE);
        }
        if (j2eeVersion_ != null && j2eeVersion_.length()>0)
        {
          info.setProperty(WebModuleCreationDataModel.J2EE_VERSION, new Integer(j2eeVersion_));
        }                

        WebProjectCreationOperation operation = new WebProjectCreationOperation(info);
        operation.run(new NullProgressMonitor());
      }
    }
    catch (java.lang.reflect.InvocationTargetException ite) {
      return new SimpleStatus(WebServiceConsumptionPlugin.ID, msgUtils_.getMessage("MSG_ERROR_WEB_PROJECT_CREATE"), Status.ERROR,
          ite);
    }
    catch (InterruptedException ie) {
      return new SimpleStatus(WebServiceConsumptionPlugin.ID, msgUtils_.getMessage("MSG_ERROR_WEB_PROJECT_CREATE"), Status.ERROR,
          ie);
    }
    return new SimpleStatus("");
  }
  */
  
  private String getNewEARName() {

    String newEARName = DEFAULT_EAR_NAME;
    //Check to see if it exists
    IProject project = ResourceUtils.getWorkspace().getRoot().getProject(newEARName);
    return newEARName;
  }

  
  private IProject getDefaultEARProject(IProject project) {

    try {
      EARNatureRuntime[] ears = null;
      EARNatureRuntime ear = null;
      ears = J2EEProjectUtilities.getReferencingEARProjects(project);
      if (ears[0] != null && ears.length >= 1) {
        // found an EAR containing the web project
        return ears[0].getProject();
      }

      if (j2eeVersion_!=null && j2eeVersion_.length()>0)
      {
      	ear = J2EEUtils.getEAR(Integer.parseInt(j2eeVersion_));
      }
      else
      {
        ear = J2EEUtils.get12EAR();
        if (ear == null)
          ear = J2EEUtils.get13EAR();
        if (ear == null)
          ear = J2EEUtils.getEAR(J2EEVersionConstants.J2EE_1_4_ID);
      }
      
      if (ear != null)
        return ear.getProject();
      else {
        String earName = getNewEARName();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        return root.getProject(earName);
      }
    }
    catch (Exception e) {
      return null;
    }

  }

  private boolean isTomcat(String runtimeTargetId)
  {
    if (runtimeTargetId == null || runtimeTargetId.length()==0)
    {
      return false;
    }
    else
    {
      if (runtimeTargetId.equals("org.eclipse.jst.server.tomcat.41.runtime") ||
      	  runtimeTargetId.equals("org.eclipse.jst.server.tomcat.40.runtime") ||
		  runtimeTargetId.equals("org.eclipse.jst.server.tomcat.32.runtime"))
      {
      	return true;
      }
      else
      	return false;
    }
  }
  
  /**
   * @param earProjectName
   *          The earProjectName to set.
   */
  public void setEarProjectName(String earProjectName) {

    this.earProjectName_ = earProjectName;
  }

  /**
   * @param projectName
   *          The projectName to set.
   */
  public void setProjectName(String projectName) {

    this.projectName_ = projectName;
  }

  /**
   * @param existingServer
   *          The existingServer to set.
   */
  public void setExistingServer(IServer existingServer) {

    this.existingServer_ = existingServer;
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
  
  public void setAddedProjectToServer(boolean addedProjectToServer) 
  {
    weAddedProjectToServer_ = addedProjectToServer;
  }  
}
