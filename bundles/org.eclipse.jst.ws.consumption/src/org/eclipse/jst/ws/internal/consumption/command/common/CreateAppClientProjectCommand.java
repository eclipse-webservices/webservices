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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.j2ee.applicationclient.internal.creation.AppClientComponentCreationDataModelProvider;
import org.eclipse.jst.j2ee.datamodel.properties.IAppClientComponentCreationDataModelProperties;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;

public class CreateAppClientProjectCommand extends SimpleCommand
{
  private String appClientProjectName_;
  private String earProjectName_;
  private String serverFactoryId_;
  private String j2eeVersion_;  
  

  /**
   * Constructor
   */
  public CreateAppClientProjectCommand()
  {
    super("org.eclipse.jst.ws.internal.consumption.command.common.CreateAppClientProjectCommand", "org.eclipse.jst.ws.internal.consumption.command.common.CreateAppClientProjectCommand");
    //setRunInWorkspaceModifyOperation(false);
  }

  public Status execute(Environment env)
  {
	MessageUtils msgUtils_ = new MessageUtils( "org.eclipse.jst.ws.consumption.plugin", this );
	
    try
    {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      IProject earProject = root.getProject(earProjectName_);
      if (earProject != null && !earProject.exists())
      {
        CreateEARProjectCommand c = new CreateEARProjectCommand();
        c.setEarProjectName(earProjectName_);
        c.setServerFactoryId(serverFactoryId_);
        c.setJ2EEVersion(j2eeVersion_);
        c.execute(env);
        earProject = root.getProject(earProjectName_);
      }
      IProject appClientProject = root.getProject(appClientProjectName_);
      if (appClientProject != null && !appClientProject.exists())
      {
        IDataModel info = DataModelFactory.createDataModel(new AppClientComponentCreationDataModelProvider());
        info.setProperty(IAppClientComponentCreationDataModelProperties.COMPONENT_NAME, appClientProjectName_);
        info.setProperty(IAppClientComponentCreationDataModelProperties.EAR_COMPONENT_NAME, earProjectName_);
        info.setProperty(IAppClientComponentCreationDataModelProperties.ADD_TO_EAR, Boolean.TRUE);
        
        //Set the J2EE version
        String finalJ2EEVersion = null;
        if (j2eeVersion_ != null && j2eeVersion_.length()>0)
        {
          info.setProperty(IAppClientComponentCreationDataModelProperties.COMPONENT_VERSION, new Integer(j2eeVersion_));
          finalJ2EEVersion = j2eeVersion_;
        }                        
        else
        {
          if (earProject != null && earProject.exists())
          {
        	//TODO Remove old Nature references
            //EARNatureRuntime ear = EARNatureRuntime.getRuntime(earProject);
            int earVersion = J2EEUtils.getJ2EEVersion(earProject);
            info.setProperty(IAppClientComponentCreationDataModelProperties.COMPONENT_VERSION, new Integer(earVersion));
            finalJ2EEVersion = String.valueOf(earVersion);
          }
          else
          {
            info.setProperty(IAppClientComponentCreationDataModelProperties.COMPONENT_VERSION, new Integer(J2EEVersionConstants.J2EE_1_3_ID));
            finalJ2EEVersion = String.valueOf(J2EEVersionConstants.J2EE_1_3_ID);
          }            
        }        
        
        //Set the server target
        if (serverFactoryId_!=null && serverFactoryId_.length()>0)
        {
		//TODO - Add this logic to FlexibleProjectCreationDataModel, and op....
		
//          String runtimeTargetId = ServerUtils.getServerTargetIdFromFactoryId(serverFactoryId_, IServerTargetConstants.APP_CLIENT_TYPE, finalJ2EEVersion); 
//		  info.setProperty(AppClientComponentCreationDataModel.SERVER_TARGET_ID, runtimeTargetId );
//          info.setProperty(AppClientComponentCreationDataModel.ADD_SERVER_TARGET, Boolean.TRUE);
        }

        //Create the AppClient project
        IDataModelOperation operation = info.getDefaultOperation();
        operation.execute(new NullProgressMonitor(), null);
      }
    }
    catch (ExecutionException ite)
    {
      Status status = new SimpleStatus(WebServiceConsumptionPlugin.ID, msgUtils_.getMessage("MSG_ERROR_CANNOT_CREATE_APP_CLIENT_PROJECT", new String[] {appClientProjectName_}), Status.ERROR, ite);
      env.getStatusHandler().reportError(status);
      return status;
    }
    return new SimpleStatus("");
  }
  
  public void setAppClientProjectName(String appClientProjectName)
  {
    appClientProjectName_ = appClientProjectName;
  }
  
  public void setEarProjectName(String earProjectName)
  {
    earProjectName_ = earProjectName;
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
