/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.wst.command.internal.env.core.common.ProgressUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IServer;

public class ManageServerStartUpCommand extends AbstractDataModelOperation
{

	private Boolean isStartServiceEnabled_;
	private Boolean isTestServiceEnabled_;
	
	private IProject serviceProject_;
	private String serviceServerTypeId_;
	private IServer serviceExistingServer_;
	
	private IProject sampleProject_;
	private String sampleServerTypeId_;
	private IServer sampleExistingServer_;
	
	private boolean isWebProjectStartupRequested_;

	/**
	 * Default CTOR;
	 */
	public ManageServerStartUpCommand( ) {

	}
	
	/**
	 * Execute the command
	 */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
      IEnvironment env = getEnvironment();
	    IStatus status = Status.OK_STATUS;
	    ProgressUtils.report(monitor, ConsumptionMessages.PROGRESS_INFO_START_WEB_PROJECT);
	 
	    if (isStartServiceEnabled_.booleanValue() && serviceExistingServer_!=null){
	    	//System.out.println("Calling service server start: "+serviceProject_+"  "+serviceServerTypeId_);
	    	StartProjectCommand spc = new StartProjectCommand( );
	    	spc.setServiceProject(serviceProject_);
	    	spc.setServiceServerTypeID(serviceServerTypeId_);
	    	spc.setServiceExistingServer(serviceExistingServer_);
	    	spc.setIsWebProjectStartupRequested(isWebProjectStartupRequested_);
	    	spc.setEnvironment( env );
	    	spc.execute( null, null );
	    }
	    
	    if(isTestServiceEnabled_.booleanValue()&& sampleExistingServer_!=null && serviceExistingServer_!=null && !sampleExistingServer_.equals(serviceExistingServer_)){
	    	//System.out.println("Calling client server start: "+sampleProject_+"  "+sampleExistingServer__);
	    	StartProjectCommand spc = new StartProjectCommand( );
	    	spc.setSampleProject(sampleProject_);
	    	spc.setSampleServerTypeID(sampleServerTypeId_);
	    	spc.setSampleExistingServer(sampleExistingServer_);
	    	spc.setCreationScenario(new Boolean("false"));
	    	spc.setEnvironment( env );
	    	spc.execute( null, null );	    	
	    }
	    
	    return status;
	}
	
	/**
	 * @param isStartServiceEnabled The isStartServiceEnabled to set.
	 */
	public void setStartService(Boolean isStartServiceEnabled) {
		this.isStartServiceEnabled_ = isStartServiceEnabled;
	}
	/**
	 * @param isTestServiceEnabled The isTestServiceEnabled to set.
	 */
	public void setTestService(Boolean isTestServiceEnabled) {
		this.isTestServiceEnabled_ = isTestServiceEnabled;
	}
	/**
	 * @param serviceExistingServer The serviceExistingServer to set.
	 */
	public void setServiceExistingServer(IServer serviceExistingServer) {
		this.serviceExistingServer_ = serviceExistingServer;
	}

	/**
	 * @param serviceServerTypeId The serviceServerTypeId to set.
	 */
	public void setServiceServerTypeId(String serviceServerTypeId) {
		this.serviceServerTypeId_ = serviceServerTypeId;
	}
	/**
	 * @param serviceProject The serviceProject to set.
	 */
	public void setServiceProject(IProject serviceProject) {
		this.serviceProject_ = serviceProject;
	}
	
	/**
	 * @param sampleExistingServer The sampleExistingServer to set.
	 */
	public void setSampleExistingServer(IServer sampleExistingServer) {
		this.sampleExistingServer_ = sampleExistingServer;
	}
	/**
	 * @param sampleProject The sampleProject to set.
	 */
	public void setSampleProject(IProject sampleProject) {
		this.sampleProject_ = sampleProject;
	}
	/**
	 * @param sampleServerTypeId The sampleServerTypeId to set.
	 */
	public void setSampleServerTypeId(String sampleServerTypeId) {
		this.sampleServerTypeId_ = sampleServerTypeId;
	}	
	
	/**
	 * @param isRestartProjectNeeded The isRestartProjectNeeded to set.
	 */
	public void setIsWebProjectStartupRequested(boolean isRestartProjectNeeded) {
		this.isWebProjectStartupRequested_ = isRestartProjectNeeded;
	}	
}
