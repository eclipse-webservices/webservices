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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.application.operations.EnterpriseApplicationCreationDataModel;
import org.eclipse.jst.j2ee.application.operations.EnterpriseApplicationCreationOperation;
import org.eclipse.jst.j2ee.internal.servertarget.IServerTargetConstants;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;


public class CreateEARProjectCommand extends SimpleCommand
{
  private String earProjectName_;
  private String serverFactoryId_;
  private String j2eeVersion_;  

  /**
   * Constructor
   */
  public CreateEARProjectCommand()
  {
    super("org.eclipse.jst.ws.internal.consumption.command.common.CreateEARProjectCommand", "org.eclipse.jst.ws.internal.consumption.command.common.CreateEARProjectCommand");
    //setRunInWorkspaceModifyOperation(false);
  }

  public Status execute(Environment env)
  {
	MessageUtils msgUtils_ = new MessageUtils( "org.eclipse.jst.ws.consumption.plugin", this );
	
    try
    {
      IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(earProjectName_);
      if (project != null && !project.exists())
      {
        EnterpriseApplicationCreationDataModel info = new EnterpriseApplicationCreationDataModel();
        info.setProperty(EnterpriseApplicationCreationDataModel.PROJECT_NAME, project.getName());
        
        //Set the J2EE version
        String finalJ2EEVersion = null;
        if (j2eeVersion_ != null && j2eeVersion_.length()>0)
        {
          info.setProperty(EnterpriseApplicationCreationDataModel.APPLICATION_VERSION, new Integer(j2eeVersion_));
          finalJ2EEVersion = j2eeVersion_;
        }
        else
        {
          info.setProperty(EnterpriseApplicationCreationDataModel.APPLICATION_VERSION, new Integer(J2EEVersionConstants.J2EE_1_3_ID));
          finalJ2EEVersion = String.valueOf(J2EEVersionConstants.J2EE_1_3_ID);
        }
        
        //Set the server target
        if (serverFactoryId_!=null && serverFactoryId_.length()>0)
        {
          String runtimeTargetId = ServerUtils.getServerTargetIdFromFactoryId(serverFactoryId_, IServerTargetConstants.EAR_TYPE, finalJ2EEVersion); 
          info.setProperty(EnterpriseApplicationCreationDataModel.SERVER_TARGET_ID, runtimeTargetId );
          info.setProperty(EnterpriseApplicationCreationDataModel.ADD_SERVER_TARGET, Boolean.TRUE);
        }

        //Create the EAR
        EnterpriseApplicationCreationOperation operation = new EnterpriseApplicationCreationOperation(info);
        operation.run(new NullProgressMonitor());
      }
    }
    catch (java.lang.reflect.InvocationTargetException ite)
    {
      Status status = new SimpleStatus(WebServiceConsumptionPlugin.ID, msgUtils_.getMessage("MSG_ERROR_CANNOT_CREATE_EAR_PROJECT", new String[] {earProjectName_}), Status.ERROR, ite);
      env.getStatusHandler().reportError(status);
      return status;
    }
    catch (InterruptedException ie)
    {
      Status status = new SimpleStatus(WebServiceConsumptionPlugin.ID, msgUtils_.getMessage("MSG_ERROR_CANNOT_CREATE_EAR_PROJECT", new String[] {earProjectName_}), Status.ERROR, ie);
      env.getStatusHandler().reportError(status);
      return status;
    }
    return new SimpleStatus("");
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
