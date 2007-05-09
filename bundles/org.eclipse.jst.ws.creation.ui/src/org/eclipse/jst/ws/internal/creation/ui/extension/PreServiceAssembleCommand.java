/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060131 121071   rsinha@ca.ibm.com - Rupam Kuehner     
 * 20060330 128827   kathy@ca.ibm.com - Kathy Chan
 * 20060524   141925 kathy@ca.ibm.com - Kathy Chan
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20070509   182274 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.jst.ws.internal.creation.ui.extension;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
import org.eclipse.jst.ws.internal.consumption.command.common.AssociateModuleWithEARCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateFacetedProjectCommand;
import org.eclipse.jst.ws.internal.consumption.common.RequiredFacetVersion;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.plugin.WSPlugin;
import org.eclipse.wst.ws.internal.preferences.PersistentMergeContext;
import org.eclipse.wst.ws.internal.wsrt.IContext;
import org.eclipse.wst.ws.internal.wsrt.IWebService;
import org.eclipse.wst.ws.internal.wsrt.IMerger;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class PreServiceAssembleCommand extends AbstractDataModelOperation 
{
	private IWebService				webService_;
	private String						project_;
  private String            module_;
	private String						earProject_;
  private String            ear_;
  private IContext          context_;

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
	  IEnvironment environment = getEnvironment();
	  IStatus status = Status.OK_STATUS;
	  
	  // For top down scenarios, merge the content of the skeleton files with the previous version stored.	  
	  // The Web service extensions triggers the storing of the content of the old skeleton file by 
	  // calling WebServiceInfo.setImplURLs() before the new skeleton is generated in the extension's 
	  // develop() method.
	  
	  if (context_.getScenario().getValue() == WebServiceScenario.TOPDOWN) {
		  PersistentMergeContext mergeContext = WSPlugin.getInstance().getMergeContext();
		  IMerger merger = webService_.getWebServiceInfo().getMerger();
		  if (mergeContext.isSkeletonMergeEnabled() && merger != null) {
			  status = merger.merge(monitor, getEnvironment().getStatusHandler());	
			  if (status.getSeverity() == Status.ERROR) {
				  environment.getStatusHandler().reportError(status);
				  return status;
			  }		
		  }
	  }
	  
	  if (context_.getAssemble()) {

		  // Check if EAR module is req'd, ie. !=null
		  if (earProject_==null)
			  return Status.OK_STATUS;



		  //Create the service EAR module

		  CreateFacetedProjectCommand command = new CreateFacetedProjectCommand();
		  command.setProjectName(earProject_);
		  command.setTemplateId(IJ2EEModuleConstants.JST_EAR_TEMPLATE);

		  // RequiredFacetVersions is set to an empty array because we don't need to impose any additional constraints.
		  // We just want to create the highest level of EAR project that the selected server supports.
		  command.setRequiredFacetVersions(new RequiredFacetVersion[0]); 

		  command.setServerFactoryId(webService_.getWebServiceInfo().getServerFactoryId());
		  command.setServerInstanceId(webService_.getWebServiceInfo().getServerInstanceId());
		  status = command.execute( monitor, adaptable );
		  if (status.getSeverity() == Status.ERROR)
		  {
			  environment.getStatusHandler().reportError( status );
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
  
  public void setContext (IContext context) {
	  context_ = context;
  }
}
