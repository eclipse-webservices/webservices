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
package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jst.ws.internal.consumption.command.common.AddModuleToServerCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.AssociateModuleWithEARCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateModuleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.server.core.IServer;

public class CreateSampleProjectCommand extends SimpleCommand
{
  private String sampleProject;
  private String sampleProjectEar;
  private IServer existingServer;
  private String serverFactoryId;
  private boolean needEAR;
  private String j2eeVersion;
  
  
  public CreateSampleProjectCommand()
  {
    super("org.eclipse.jst.ws.internal.consumption.ui.wizard.client.common.CreateSampleProjectCommand","org.eclipse.jst.ws.internal.consumption.ui.wizard.client.common.CreateSampleProjectCommand");
  }

  public Status execute(Environment env)
  {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    if (!root.getProject(sampleProject).exists() || !root.getProject(sampleProjectEar).exists())
    {
      //Do the following
      //1. Create the Web project/component to house the sample JSPs.
      //2. If needEAR is true then
      //   - create a EAR project and component if it doesn't exist
      //   - add the Web component to the EAR component
      //3. Add the right component to the server.
      CreateModuleCommand command = new CreateModuleCommand();
	  command.setProjectName(sampleProject);
	  command.setModuleName(sampleProject);			
	  command.setModuleType(CreateModuleCommand.WEB);
	  command.setServerFactoryId(serverFactoryId);
	  command.setServerInstanceId(existingServer.getId());
	  command.setJ2eeLevel(j2eeVersion);
	  Status status = command.execute(env);
	  if (status.getSeverity()==Status.ERROR)
	  {
		  env.getStatusHandler().reportError(status);
		  return status;
	  }
		  
	  if (needEAR)
	  {
			CreateModuleCommand commandEAR = new CreateModuleCommand();
			commandEAR.setProjectName(sampleProjectEar);
			//TODO The EAR component name is not necessarily the same as the project name
			//so the component name needs to somehow be piped into this command
			commandEAR.setModuleName(sampleProjectEar);
			commandEAR.setModuleType(CreateModuleCommand.EAR);
			commandEAR.setServerFactoryId(serverFactoryId);
			commandEAR.setServerInstanceId(existingServer.getId());
			commandEAR.setJ2eeLevel(j2eeVersion);
			status = commandEAR.execute(env);
			if (status.getSeverity()==Status.ERROR)
			{
			  env.getStatusHandler().reportError(status);
			  return status;
			}			
				
				
			//Associate the service module and service EAR
			AssociateModuleWithEARCommand associateCommand = new AssociateModuleWithEARCommand();
			associateCommand.setProject(sampleProject);
			associateCommand.setModule(sampleProject);
			associateCommand.setEARProject(sampleProjectEar);
			//TODO The EAR component name is not necessarily the same as the project name
			//so the component name needs to somehow be piped into this command
			associateCommand.setEar(sampleProjectEar);
			status = associateCommand.execute(env);
			if (status.getSeverity()==Status.ERROR)
			{
				env.getStatusHandler().reportError(status);
				return status;
			}			
				
			//Add the EAR to the server
			AddModuleToServerCommand commandInstall = new AddModuleToServerCommand();
			commandInstall.setServerInstanceId(existingServer.getId());
			commandInstall.setProject(sampleProjectEar);
			//TODO The EAR component name is not necessarily the same as the project name
			//so the component name needs to somehow be piped into this command
			commandInstall.setModule(sampleProjectEar);
				
			status = commandInstall.execute(env);
			if (status.getSeverity()==Status.ERROR)
			{
				env.getStatusHandler().reportError(status);
				return status;
			}							
		}
	    else
		{
		    //Add the Web component to the server
			AddModuleToServerCommand commandInstall = new AddModuleToServerCommand();
			commandInstall.setServerInstanceId(existingServer.getId());
			commandInstall.setProject(sampleProject);
			commandInstall.setModule(sampleProject);
			
			status = commandInstall.execute(env);
			if (status.getSeverity()==Status.ERROR)
			{
				env.getStatusHandler().reportError(status);
				return status;
			}			  
		}
		  
    }
    return new SimpleStatus("");
  }

  public void setSampleProject(String sampleProject)
  {
    this.sampleProject = sampleProject; 	
  }

  public void setSampleProjectEAR(String sampleProjectEar)
  {
  	this.sampleProjectEar = sampleProjectEar;
  }

  public void setExistingServer(IServer existingServer) {

    this.existingServer = existingServer;
  }
  
  public void setServerFactoryId(String serverFactoryId)
  {
  	this.serverFactoryId = serverFactoryId;
  }

  /**
   * @param needEAR The needEAR to set.
   */
  public void setNeedEAR(boolean needEAR)
  {
    this.needEAR = needEAR;
  }
    
  /**
   * @param version The j2eeVersion to set.
   */
  public void setJ2eeVersion(String version)
  {
    j2eeVersion = version;
  }
}
