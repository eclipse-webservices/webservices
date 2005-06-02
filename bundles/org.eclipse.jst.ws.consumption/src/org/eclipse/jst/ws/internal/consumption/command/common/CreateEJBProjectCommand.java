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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.j2ee.ejb.datamodel.properties.IEjbComponentCreationDataModelProperties;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.earcreation.EARNatureRuntime;
import org.eclipse.jst.j2ee.internal.ejb.archiveoperations.EjbComponentCreationDataModelProvider;
import org.eclipse.jst.j2ee.internal.plugin.J2EEPlugin;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;


public class CreateEJBProjectCommand extends SimpleCommand
{
  private static String LABEL = "%TASK_LABEL_CREATE_EJB_PROJECT";
  private static String DESCRIPTION = "%TASK_DESC_CREATE_EJB_PROJECT";
  private boolean isEJBProjectRequired_ = true;
  private String ejbProjectName_ = null;
  private String earProjectName_ = null;
  
  private IProject serviceProject;
  private IServer serviceExistingServer;
  private String serverFactoryId_;
  private String j2eeVersion_;  
  
 
  public CreateEJBProjectCommand()
  {
    super(LABEL, DESCRIPTION);
  }

  /**
   * Constructor for CreateEJBProjectTask.
   * @param arg0
   * @param arg1
   */
  public CreateEJBProjectCommand(String label, String desc)
  {
    super(label, desc);
    //setRunInWorkspaceModifyOperation(false);
  }

  public Status execute(Environment env)
  {
   try
   {
    if (ejbProjectName_ != null && ejbProjectName_.length() > 0 && earProjectName_ != null && earProjectName_.length() > 0)
    {
      Status status = createEJBProject(ejbProjectName_, earProjectName_, env);
      if (status.getSeverity() == Status.ERROR) 
        env.getStatusHandler().reportError(status);
      return status;
      
    }

    // Get web project and form ejbProjectName
    if (!isEJBProjectRequired_)
      return new SimpleStatus("");
    
    if (ejbProjectName_ == null)
      return new SimpleStatus("");
    
    // EJB Project already exists
    IContainer container = J2EEPlugin.getWorkspace().getRoot().getProject(ejbProjectName_);
    if (container.exists())
    {
      return new SimpleStatus("");
    }

    // Create EJBProjectInfo
    IDataModel projectInfo = DataModelFactory.createDataModel(new EjbComponentCreationDataModelProvider());
    projectInfo.setProperty(IEjbComponentCreationDataModelProperties.PROJECT_NAME, ejbProjectName_ );
    projectInfo.setProperty(IEjbComponentCreationDataModelProperties.CREATE_DEFAULT_SESSION_BEAN,Boolean.TRUE);

    Status status;
    if (earProjectName_ != null)
      status = addEARToProject(earProjectName_, projectInfo);
    else
    {
      status = addEARToProject(serviceProject, projectInfo, env);
    }
    if (status.getSeverity() == Status.ERROR)
      env.getStatusHandler().reportError(status);
    return status;
   }
   catch (Exception e) {
   }
   return new SimpleStatus("");
  }

  private Status addEARToProject(String earProjectName, IDataModel projectInfo)
  {
    IProject earProject = null;
    // IProject earProject = ResourcesPlugin.getWorkspace().getRoot().getProject(earProjectName);
    // if (!earProject.exists())
    {    	
      EARNatureRuntime ear = null;
      if (j2eeVersion_!=null && j2eeVersion_.length()>0)
      {
      	ear = J2EEUtils.getEAR(Integer.parseInt(j2eeVersion_));
      }
      else
      {
        ear = J2EEUtils.get13EAR();      
        if (ear != null)
          earProject = ear.getProject();
        else
        {
          ear = J2EEUtils.get12EAR();
          if (ear != null)
            earProject = ear.getProject();
        }
      }
    }

    projectInfo.setProperty(IEjbComponentCreationDataModelProperties.EAR_COMPONENT_NAME, earProject.getName());
    projectInfo.setProperty(IEjbComponentCreationDataModelProperties.ADD_TO_EAR, Boolean.TRUE);
    
    //Set the J2EE version
    String finalJ2EEVersion = null;    
    if (j2eeVersion_ != null && j2eeVersion_.length()>0)
    {
      projectInfo.setProperty(IEjbComponentCreationDataModelProperties.COMPONENT_VERSION, new Integer(j2eeVersion_));
      finalJ2EEVersion = j2eeVersion_;
    }    
    else
    {
      if (earProject != null && earProject.exists())
      {
        EARNatureRuntime ear = EARNatureRuntime.getRuntime(earProject);
        int earVersion = ear.getJ2EEVersion();
        projectInfo.setProperty(IEjbComponentCreationDataModelProperties.COMPONENT_VERSION, new Integer(earVersion));
        finalJ2EEVersion = String.valueOf(earVersion);
      }
      else
      {
        projectInfo.setProperty(IEjbComponentCreationDataModelProperties.COMPONENT_VERSION, new Integer(J2EEVersionConstants.J2EE_1_3_ID));
        finalJ2EEVersion = String.valueOf(J2EEVersionConstants.J2EE_1_3_ID);
      }            
    }
    
    if (serverFactoryId_!=null && serverFactoryId_.length()>0)
    {
		//		TODO - Add this logic to FlexibleProjectCreationDataModel, and op....
//      String runtimeTargetId = ServerUtils.getServerTargetIdFromFactoryId(serverFactoryId_, IServerTargetConstants.EJB_TYPE, j2eeVersion_); 
//      projectInfo.setProperty(EjbComponentCreationDataModel.SERVER_TARGET_ID, runtimeTargetId );
//      projectInfo.setProperty(EjbComponentCreationDataModel.ADD_SERVER_TARGET, Boolean.TRUE);
    }

    IDataModelOperation ejbProjectCreationOperation = projectInfo.getDefaultOperation();    	

    try
    {
      ejbProjectCreationOperation.execute(new NullProgressMonitor(), null);
	  
    }
    catch (ExecutionException e)
    {
      return new SimpleStatus(WebServiceConsumptionPlugin.ID, WebServiceConsumptionPlugin.getMessage("%MSG_ERROR_CANNOT_CREATE_EJB_PROJECT"), Status.ERROR, e);
    }
    return new SimpleStatus("");
  }

  private Status addEARToProject(IProject serviceProject, IDataModel projectInfo, Environment env)
  {
        EARNatureRuntime ear = null;
        IProject earProject = null;
        boolean addServiceEARToServer = false;
        boolean addDiscoveredEARToServer = false;
        IServer discoveredEARsServer = null;
        EARNatureRuntime[] ears = J2EEUtils.getEARProjects(serviceProject);
        try {    	
      
	    if (ears!=null && ears.length >= 1) {
	      ear = ears[0];      
             earProject = ear.getProject();
             if (serviceExistingServer != null)
             {
               if (serviceExistingServer.getServerState() != IServer.STATE_STOPPED)
                 serviceExistingServer.synchronousStop(false);     
               Status mmStatus = ServerUtils.getInstance().modifyModules(env, serviceExistingServer, ResourceUtils.getModule(earProject), false, new NullProgressMonitor());
               if (mmStatus.getSeverity()==Status.ERROR)
               	return mmStatus;
               addServiceEARToServer = true;
             }

	    }
	    else {
	      //Check to see if any eligible EARs exist in the workspace.
	      ear = J2EEUtils.get12EAR();
	      if (ear!=null)
	      {
	      	earProject = ear.getProject();

	      	// Add default server
              IServer defaultServer = ServerUtils.getDefaultExistingServer(earProject);    
              if (defaultServer!=null)
              {
              	if (defaultServer.getServerState() != IServer.STATE_STOPPED) {
                	 defaultServer.synchronousStop(false);                    
                	 }
              	Status mmStatus = ServerUtils.getInstance().modifyModules(env, defaultServer, ResourceUtils.getModule(earProject), false, new NullProgressMonitor());
                if (mmStatus.getSeverity()==Status.ERROR)
                   	return mmStatus;              	
			addDiscoveredEARToServer = true;
			discoveredEARsServer = defaultServer;			
               }
	       }
	    }
	    
        }
        catch (Exception e)
        {
          return new SimpleStatus(WebServiceConsumptionPlugin.ID, WebServiceConsumptionPlugin.getMessage("%MSG_ERROR_CANNOT_CREATE_EJB_PROJECT"), Status.ERROR, e);
        }
	      
      IProject ejbProject = null;
      //Add the project back to the Server config
      projectInfo.setProperty(IEjbComponentCreationDataModelProperties.EAR_COMPONENT_NAME, earProject.getName());
      projectInfo.setProperty(IEjbComponentCreationDataModelProperties.ADD_TO_EAR, Boolean.TRUE);	
      IDataModelOperation ejbProjectCreationOperation = projectInfo.getDefaultOperation();    	

      try {
        ejbProjectCreationOperation.execute(new NullProgressMonitor(), null);
      }
      catch (ExecutionException e)
      {
        return new SimpleStatus(WebServiceConsumptionPlugin.ID, WebServiceConsumptionPlugin.getMessage("%MSG_ERROR_CANNOT_CREATE_EJB_PROJECT"), Status.ERROR, e);
      }
      //ejbProject = projectInfo.primGetProject();

      //Add the project back to the Server config
      if (addServiceEARToServer)
      {
      	Status mmStatus = ServerUtils.getInstance().modifyModules(env, serviceExistingServer, ResourceUtils.getModule(earProject), true, new NullProgressMonitor());
        if (mmStatus.getSeverity()==Status.ERROR)
           	return mmStatus;  
      }
      if (addDiscoveredEARToServer)
      {
      	Status mmStatus = ServerUtils.getInstance().modifyModules(env, discoveredEARsServer, ResourceUtils.getModule(earProject), true, new NullProgressMonitor());
        if (mmStatus.getSeverity()==Status.ERROR)
           	return mmStatus;  
      }
      return new SimpleStatus("");
  }

  private Status createEJBProject(String ejbProjectName, String earProjectName, Environment env)
  {
    try
    {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      IProject earProject = root.getProject(earProjectName);
      if (earProject != null && !earProject.exists())
      {
        CreateEARProjectCommand c = new CreateEARProjectCommand();
        c.setEarProjectName(earProjectName);
        c.setServerFactoryId(serverFactoryId_);
        c.setJ2EEVersion(j2eeVersion_);
        c.execute(env);
        earProject = root.getProject(earProjectName);
      }
      IProject ejbProject = root.getProject(ejbProjectName);
      if (ejbProject != null && !ejbProject.exists())
      {
        IDataModel info = DataModelFactory.createDataModel(new EjbComponentCreationDataModelProvider());
        info.setProperty(IEjbComponentCreationDataModelProperties.PROJECT_NAME, ejbProjectName);
        info.setProperty(IEjbComponentCreationDataModelProperties.EAR_COMPONENT_NAME, earProjectName);
        info.setProperty(IEjbComponentCreationDataModelProperties.ADD_TO_EAR, Boolean.TRUE);
        
        info.setProperty(IEjbComponentCreationDataModelProperties.CREATE_DEFAULT_SESSION_BEAN,Boolean.TRUE);        
        
        //Set the J2EE version
        String finalJ2EEVersion = null;
        if (j2eeVersion_ != null && j2eeVersion_.length()>0)
        {
          info.setProperty(IEjbComponentCreationDataModelProperties.COMPONENT_VERSION, new Integer(j2eeVersion_));
          finalJ2EEVersion = j2eeVersion_;
        }                                
        else
        {
          if (earProject != null && earProject.exists())
          {
            EARNatureRuntime ear = EARNatureRuntime.getRuntime(earProject);
            int earVersion = ear.getJ2EEVersion();
            info.setProperty(IEjbComponentCreationDataModelProperties.COMPONENT_VERSION, new Integer(earVersion));
            finalJ2EEVersion = String.valueOf(earVersion);
          }
          else
          {
            info.setProperty(IEjbComponentCreationDataModelProperties.COMPONENT_VERSION, new Integer(J2EEVersionConstants.J2EE_1_3_ID));
            finalJ2EEVersion = String.valueOf(J2EEVersionConstants.J2EE_1_3_ID);
          }            
        }
        
        //Set the server target
        if (serverFactoryId_!=null && serverFactoryId_.length()>0)
        {
//			TODO - Add this logic to FlexibleProjectCreationDataModel, and op....
//          String runtimeTargetId = ServerUtils.getServerTargetIdFromFactoryId(serverFactoryId_,IServerTargetConstants.EJB_TYPE, finalJ2EEVersion); 
//          info.setProperty(EjbComponentCreationDataModel.SERVER_TARGET_ID, runtimeTargetId );
//          info.setProperty(EjbComponentCreationDataModel.ADD_SERVER_TARGET, Boolean.TRUE);
        }

        //Create the EJB Project
        IDataModelOperation operation = info.getDefaultOperation();
        operation.execute(new NullProgressMonitor(), null);
      }
      
      // add EAR to Server
      if (serviceExistingServer!=null){
        boolean earAddedToServer = ServerUtil.containsModule(serviceExistingServer,ResourceUtils.getModule(earProject), new NullProgressMonitor());
        
        if (!earAddedToServer) {
          Status mmStatus = ServerUtils.getInstance().modifyModules(env, serviceExistingServer, ResourceUtils.getModule(earProject) , true, new NullProgressMonitor());  
          if (mmStatus.getSeverity()==Status.ERROR)
          	return mmStatus;      
        }        
      }
      
      
    }
    catch (ExecutionException ite)
    {
      return new SimpleStatus(WebServiceConsumptionPlugin.ID, WebServiceConsumptionPlugin.getMessage("%MSG_ERROR_CANNOT_CREATE_EJB_PROJECT"), Status.ERROR, ite);
    }
    return new SimpleStatus("");
  }
  
  /**
   * @param earProjectName The earProjectName to set.
   */
  public void setEarProjectName(String earProjectName)
  {
    this.earProjectName_ = earProjectName;
  }

  /**
   * @param ejbProjectName The ejbProjectName to set.
   */
  public void setEjbProjectName(String ejbProjectName)
  {
    this.ejbProjectName_ = ejbProjectName;
  }

  /**
   * @param isEJBProjectRequired The isEJBProjectRequired to set.
   */
  public void setEJBProjectRequired(boolean isEJBProjectRequired)
  {
    this.isEJBProjectRequired_ = isEJBProjectRequired;
  }

  /**
   * @param ejbProjectName The ejbProjectName to set.
   */
  public void setEJBProjectName(String ejbProjectName) {
    this.ejbProjectName_ = ejbProjectName;
  }

  /**
   * @param serviceExistingServer The serviceExistingServer to set.
   */
  public void setServiceExistingServer(IServer serviceExistingServer) {
    this.serviceExistingServer = serviceExistingServer;
  }

  /**
   * @param serviceProject The serviceProject to set.
   */
  public void setServiceProject(IProject serviceProject) {
    this.serviceProject = serviceProject;
  }
  
  public void setServerFactoryId(String serverFactoryId)
  {
  	serverFactoryId_ = serverFactoryId;
  }
  
  public void setJ2EEVersion(String j2eeVersion)
  {
  	j2eeVersion_ = j2eeVersion;	
  }  

}
