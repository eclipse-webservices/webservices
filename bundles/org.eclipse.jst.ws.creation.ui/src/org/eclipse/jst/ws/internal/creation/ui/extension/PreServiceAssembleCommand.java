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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.command.common.AssociateModuleWithEARCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateModuleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.AbstractDataModelOperation;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;

public class PreServiceAssembleCommand extends AbstractDataModelOperation 
{
	private IWebService				webService_;
	private String						project_;
  private String            module_;
	private String						earProject_;
  private String            ear_;
	private String            j2eeLevel_;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
    Environment environment = getEnvironment();
    
	  System.out.println( "In Pre service assemble command." );
    
		// Check if EAR module is req'd, ie. !=null
		if (earProject_==null)
			return Status.OK_STATUS;
	  
	  
		//Create the service EAR module
		CreateModuleCommand command = new CreateModuleCommand();
		command.setProjectName(earProject_);
		command.setModuleName(ear_);
		command.setModuleType(CreateModuleCommand.EAR);
		command.setServerFactoryId(webService_.getWebServiceInfo().getServerFactoryId());
		command.setServerInstanceId( webService_.getWebServiceInfo().getServerInstanceId() );
		command.setJ2eeLevel(j2eeLevel_);
    command.setEnvironment( environment );
		IStatus status = command.execute( monitor, null );
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
    associateCommand.setEnvironment( environment );
		status = associateCommand.execute( monitor, null );
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
