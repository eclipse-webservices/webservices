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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.datamodel.properties.IEarComponentCreationDataModelProperties;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.earcreation.EarComponentCreationDataModelProvider;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusUtils;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;


public class CreateEARProjectCommand extends EnvironmentalOperation
{
  private String earProjectName_;
  private String serverFactoryId_;
  private String j2eeVersion_;  

  /**
   * Constructor
   */
  public CreateEARProjectCommand()
  {
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment  env       = getEnvironment();
	  MessageUtils msgUtils_ = new MessageUtils( "org.eclipse.jst.ws.consumption.plugin", this );
	
    try
    {
      IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(earProjectName_);
      if (project != null && !project.exists())
      {
        IDataModel info = DataModelFactory.createDataModel(new EarComponentCreationDataModelProvider());
        info.setProperty(IEarComponentCreationDataModelProperties.PROJECT_NAME, project.getName());
        
        //Set the J2EE version
        if (j2eeVersion_ != null && j2eeVersion_.length()>0)
        {
          info.setProperty(IEarComponentCreationDataModelProperties.COMPONENT_VERSION, new Integer(j2eeVersion_));
        }
        else
        {
          info.setProperty(IEarComponentCreationDataModelProperties.COMPONENT_VERSION, new Integer(J2EEVersionConstants.J2EE_1_3_ID));
        }
        
        //Set the server target
        if (serverFactoryId_!=null && serverFactoryId_.length()>0)
        {
//			TODO - Add this logic to FlexibleProjectCreationDataModel, and op....
//          String runtimeTargetId = ServerUtils.getServerTargetIdFromFactoryId(serverFactoryId_, IServerTargetConstants.EAR_TYPE, finalJ2EEVersion); 
//          info.setProperty(EARComponentCreationDataModel.SERVER_TARGET_ID, runtimeTargetId );
//          info.setProperty(EARComponentCreationDataModel.ADD_SERVER_TARGET, Boolean.TRUE);
        }

        //Create the EAR
        IDataModelOperation operation = info.getDefaultOperation();
        operation.execute(new NullProgressMonitor(), null);
      }
    }
    catch (ExecutionException ite)
    {
      IStatus status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_CANNOT_CREATE_EAR_PROJECT", new String[] {earProjectName_}), ite);
      env.getStatusHandler().reportError(status);
      return status;
    }
    return Status.OK_STATUS;
  }
  /**
   * @param earProjectName_ The earProjectName_ to set.
   */
  public void setEarProjectName(String earProjectName)
  {
    this.earProjectName_ = earProjectName;
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
