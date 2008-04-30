/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070815   199626 kathy@ca.ibm.com - Kathy Chan
 * 20071130   203826 kathy@ca.ibm.com - Kathy Chan
 * 20080425   220985  - Trung, Server is recreated when prev publish failed
 * 20080429   220985 trungha@ca.ibm.com - Trung Ha
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
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.IWebService;

public class PreServiceInstallCommand extends AbstractDataModelOperation 
{
	private IWebService webService_;
	private String		project_;
    private String      module_;
	private String		earProject_;
    private String      ear_;
    private IContext    context_;
		
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
	  if (context_.getInstall())
	  {
	            IEnvironment environment = getEnvironment();      
			  			
				if (webService_.getWebServiceInfo().getServerInstanceId()==null)
				{
					// Attempt to find a server instance in the workspace matching
					// the server id of the webService_
					// If we can find one, we don't need to create a new server.
					IServer[] allServers = ServerCore.getServers();
					if (allServers != null && allServers.length > 0) {
						for (int i = 0; i < allServers.length; i++) {
							IServer oneServer = allServers[i];
							if (oneServer.getServerType().getId().equals(
									webService_.getWebServiceInfo().getServerFactoryId())) {
								
								webService_.getWebServiceInfo().setServerInstanceId(oneServer.getId());
								webService_.getWebServiceInfo().setServerCreated(true);
							}
						}
					}
					// Cannot find an appropriate server, so we will create one
					else {
						CreateServerCommand createServerCommand = new CreateServerCommand();
						createServerCommand.setServerFactoryid(webService_
								.getWebServiceInfo().getServerFactoryId());
						createServerCommand.setServerRuntimeId(webService_
								.getWebServiceInfo().getServerRuntimeId());
						createServerCommand.setEnvironment(environment);
						IStatus createServerStatus = createServerCommand.execute(
								monitor, null);
						if (createServerStatus.getSeverity() == Status.OK) {
							webService_.getWebServiceInfo().setServerInstanceId(
									createServerCommand.getServerInstanceId());
							webService_.getWebServiceInfo().setServerCreated(true);
						} else {
							if (createServerStatus.getSeverity() == Status.ERROR) {
								environment.getStatusHandler().reportError(
										createServerStatus);
							}
							return createServerStatus;
						}
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
		  return Status.OK_STATUS;
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
	  
	  public void setContext(IContext context)
	  {
		  context_=context;
	  }
		
}
