/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
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
 * 20060330 128827   kathy@ca.ibm.com - Kathy Chan
 * 20060524   141925 kathy@ca.ibm.com - Kathy Chan
 * 20060529   141422 kathy@ca.ibm.com - Kathy Chan
 * 20070509   182274 kathy@ca.ibm.com - Kathy Chan
 * 20071218	  200193 gilberta@ca.ibm.com - Gilbert Andrews
 * 20071220   213640 kathy@ca.ibm.com - Kathy Chan
 * 20080325   222473 makandre@ca.ibm.com - Andrew Mak, Create EAR version based on the version of modules to be added
 * 20090415   264683 danail.branekov@sap.com - Danail Branekov
 * 20100511   309395 mahutch@ca.ibm.com - Mark Hutchinson, WS Wizard Converting Java Project into Utility Project without any warning
 *******************************************************************************/

package org.eclipse.jst.ws.internal.creation.ui.extension;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.command.common.AssociateModuleWithEARCommand;
import org.eclipse.jst.ws.internal.consumption.command.common.CreateFacetedProjectCommand;
import org.eclipse.jst.ws.internal.consumption.common.FacetUtils;
import org.eclipse.ui.PlatformUI;
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
  private IProject		initialProject_;

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

		  IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(project_);
		  command.setRequiredFacetVersions(FacetUtils.getRequiredEARFacetVersions(project)); 

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

		  //make sure the ear file has been created.
		  
		  if(initialProject_ != null && FacetUtils.isJavaProject(initialProject_)  && context_.getScenario().getValue() == WebServiceScenario.BOTTOMUP) {
			  IProject earProject = ResourcesPlugin.getWorkspace().getRoot().getProject(earProject_);
			  addJavaProjectAsUtilityInModalCtx(initialProject_, earProject, monitor);
		  }		 
	  
	  }
	  return status;	  
  }
  
  private void addJavaProjectAsUtilityInModalCtx(final IProject projectToAdd, final IProject earProject, final IProgressMonitor monitor)
	{
		final IRunnableWithProgress addRunnable = new IRunnableWithProgress()
		{
			public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
			{
				J2EEUtils.addJavaProjectAsUtilityJar(projectToAdd, earProject, monitor);
			}
		};

		try
		{
			ModalContext.run(addRunnable, true, monitor, PlatformUI.getWorkbench().getDisplay());
		} catch (InvocationTargetException e)
		{
			// The executed runnable does not throw checked exceptions therefore if this happens, this is a runtime exception
			throw new RuntimeException(e);
		} catch (InterruptedException e)
		{
			// The executed runnable does not support cancellation and this should never happen
			throw new IllegalStateException(e);
		}
	}
	
  public void setInitialProject(IProject initialProject)
  {
	  initialProject_ = initialProject;  
  }	
    
  public IProject getInitialProject()
  {
	  return initialProject_;  
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
