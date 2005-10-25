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
import org.eclipse.jst.ws.internal.consumption.command.common.AddModuleToServerCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateServerCommand;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;

public class PreServiceInstallCommand extends AbstractDataModelOperation 
{
	private IWebService				webService_;
	private String						project_;
  private String            module_;
	private String						earProject_;
  private String            ear_;
		
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
      IEnvironment environment = getEnvironment();
      
		  System.out.println( "In Pre service install command." );
			
			if (webService_.getWebServiceInfo().getServerInstanceId()==null)
			{
				CreateServerCommand createServerCommand = new CreateServerCommand();
				createServerCommand.setServerFactoryid(webService_.getWebServiceInfo().getServerFactoryId());
        createServerCommand.setEnvironment( environment );
				IStatus createServerStatus = createServerCommand.execute( monitor, null);
				if (createServerStatus.getSeverity()==Status.OK)
				{
					webService_.getWebServiceInfo().setServerInstanceId(createServerCommand.getServerInstanceId());
				}
				else
				{
					if (createServerStatus.getSeverity()==Status.ERROR)
					{
						environment.getStatusHandler().reportError(createServerStatus);
					}								
					return createServerStatus;
				}
			}
			  
			
			
			AddModuleToServerCommand command = new AddModuleToServerCommand();
			command.setServerInstanceId(webService_.getWebServiceInfo().getServerInstanceId());
			if (earProject_ != null && earProject_.length()>0 && ear_!= null && ear_.length()>0)
			{
				command.setProject(earProject_);
				command.setModule(ear_);
			}
			else
			{
				command.setProject(project_);
				command.setModule(module_);				
			}

      command.setEnvironment( environment );
			IStatus status = command.execute( monitor, null );
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
		
	  public void setWebService( IWebService webService )
	  {
		  webService_ = webService;  
	  }
		
}
