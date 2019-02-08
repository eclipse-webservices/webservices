/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060131 121071   rsinha@ca.ibm.com - Rupam Kuehner          
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20080325   222473 makandre@ca.ibm.com - Andrew Mak, Create EAR version based on the version of modules to be added
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.extension;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
import org.eclipse.jst.ws.internal.consumption.command.common.AssociateModuleWithEARCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateFacetedProjectCommand;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.IWebServiceClient;

public class PreClientAssembleCommand extends AbstractDataModelOperation 
{
  private IWebServiceClient       webServiceClient_;
  private String                  project_;
  private String                  module_;
  private String                  earProject_;
  private String                  ear_;
  private IContext          	  context_;
  
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
	  IStatus status = Status.OK_STATUS;

	  if (context_ != null && context_.getAssemble()) {
		  IEnvironment environment = getEnvironment();

		  // Check if EAR module is req'd, ie. !=null
		  if (earProject_==null)
			  return Status.OK_STATUS;

		  //  Create the client EAR module
		  CreateFacetedProjectCommand command = new CreateFacetedProjectCommand();
		  command.setProjectName(earProject_);
		  command.setTemplateId(IJ2EEModuleConstants.JST_EAR_TEMPLATE);

		  IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(project_);
		  command.setRequiredFacetVersions(FacetUtils.getRequiredEARFacetVersions(project)); 

		  command.setServerFactoryId(webServiceClient_.getWebServiceClientInfo().getServerFactoryId());
		  command.setServerInstanceId(webServiceClient_.getWebServiceClientInfo().getServerInstanceId());
		  status = command.execute( monitor, adaptable );
		  if (status.getSeverity() == Status.ERROR)
		  {
			  environment.getStatusHandler().reportError( status );
			  return status;
		  }        


		  //Associate the client module and service EAR
		  AssociateModuleWithEARCommand associateCommand = new AssociateModuleWithEARCommand();
		  associateCommand.setProject(project_);
		  associateCommand.setModule(module_);
		  associateCommand.setEARProject(earProject_);
		  associateCommand.setEar(ear_);
		  associateCommand.setEnvironment( environment );
		  status = associateCommand.execute( monitor, adaptable );
		  if (status.getSeverity()==Status.ERROR)
		  {
			  environment.getStatusHandler().reportError(  status );     
		  }     

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
  
  public void setWebService( IWebServiceClient webServiceClient )
  {
    webServiceClient_ = webServiceClient;  
  }   
  
  public void setContext (IContext context) {
	  context_ = context;
  }
}
