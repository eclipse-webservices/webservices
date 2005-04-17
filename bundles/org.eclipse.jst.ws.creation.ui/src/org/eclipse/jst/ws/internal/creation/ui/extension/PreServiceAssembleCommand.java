/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.creation.ui.extension;

import org.eclipse.jst.ws.internal.consumption.command.common.CreateModuleCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.AssociateModuleWithEARCommand;
import org.eclipse.jst.ws.internal.data.TypeRuntimeServer;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;

public class PreServiceAssembleCommand extends SimpleCommand 
{
	private IWebService				webService_;
	private String						project_;
  private String            module_;
	private String						earProject_;
  private String            ear_;
	private String            j2eeLevel_;

  public Status execute(Environment environment) 
  {
	  System.out.println( "In Pre service assemble command." );
    
		//Create the service EAR module
		CreateModuleCommand command = new CreateModuleCommand();
		command.setProjectName(earProject_);
		command.setModuleName(ear_);
		command.setModuleType(CreateModuleCommand.EAR);
		command.setServerFactoryId(webService_.getWebServiceInfo().getServerFactoryId());
		command.setJ2eeLevel(j2eeLevel_);
		Status status = command.execute(environment);
		if (status.getSeverity()==Status.ERROR)
		{
			environment.getStatusHandler().reportError(status);
		  return status;
		}			
		
		
		//Associate the service module and service EAR
		AssociateModuleWithEARCommand associateCommand = new AssociateModuleWithEARCommand();
		associateCommand.setProject(project_);
		associateCommand.setModule(module_);
		associateCommand.setEARProject(earProject_);
		associateCommand.setEar(ear_);
		status = associateCommand.execute(environment);
		if (status.getSeverity()==Status.ERROR)
		{
			environment.getStatusHandler().reportError(status);		  
		}			
		
		return status;	  
  }
	
  public void setProject( String project )
  {
	  project_ = project;
  }
	  
  public void setModule( String module )
  {
	  module_ = module;
  }	
	
  public void setEarProject( String earProject )
  {
	  earProject_ = earProject;
  }
  
  public void setEar( String ear )
  {
	  ear_ = ear;  
  }
	
 
  public void setJ2eeLevel( String j2eeLevel )
  {
	  j2eeLevel_ = j2eeLevel;  
  }
	
  public void setWebService( IWebService webService )
  {
	  webService_ = webService;  
  }	
}
