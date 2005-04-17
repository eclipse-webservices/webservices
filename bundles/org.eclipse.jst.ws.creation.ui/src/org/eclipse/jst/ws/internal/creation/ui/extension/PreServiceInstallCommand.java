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

import org.eclipse.jst.ws.internal.consumption.command.common.AddModuleToServerCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateServerCommand;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.provisional.wsrt.IWebService;

public class PreServiceInstallCommand extends SimpleCommand 
{
	private IWebService				webService_;
	private String						project_;
  private String            module_;
	private String						earProject_;
  private String            ear_;
		
	  public Status execute(Environment environment) 
	  {
		  System.out.println( "In Pre service install command." );
			
			if (webService_.getWebServiceInfo().getServerInstanceId()==null)
			{
				CreateServerCommand createServerCommand = new CreateServerCommand();
				createServerCommand.setServerFactoryid(webService_.getWebServiceInfo().getServerFactoryId());
				Status createServerStatus = createServerCommand.execute(environment);
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

			Status status = command.execute(environment);
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
