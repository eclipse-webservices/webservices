/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.wst.command.internal.provisional.env.core.AbstractDataModelOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.ProgressUtils;
import org.eclipse.wst.common.environment.Environment;
import org.eclipse.wst.server.core.IServer;

/**
 * @author sengpl
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ManageServerStartUpCommand extends AbstractDataModelOperation
{

	private MessageUtils msgUtils_;
	
	private Boolean isStartServiceEnabled_;
	private Boolean isTestServiceEnabled_;
	
	private IProject serviceProject_;
	private String serviceServerTypeId_;
	private IServer serviceExistingServer_;
	
	private IProject sampleProject_;
	private String sampleServerTypeId_;
	private IServer sampleExistingServer_;
	
	private boolean isWebProjectStartupRequested_;
	private String  moduleName_;

	/**
	 * Default CTOR;
	 */
	public ManageServerStartUpCommand( String moduleName ) {
		String pluginId = "org.eclipse.jst.ws.consumption";
		msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
		
		moduleName_ = moduleName;
	}
	
	/**
	 * Execute the command
	 */
  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
  {
      Environment env = getEnvironment();
	    IStatus status = Status.OK_STATUS;
	    ProgressUtils.report(monitor, msgUtils_.getMessage("PROGRESS_INFO_START_WEB_PROJECT"));
	 
	    if (isStartServiceEnabled_.booleanValue() && serviceExistingServer_!=null){
	    	//System.out.println("Calling service server start: "+serviceProject_+"  "+serviceServerTypeId_);
	    	StartProjectCommand spc = new StartProjectCommand( moduleName_ );
	    	spc.setServiceProject(serviceProject_);
	    	spc.setServiceServerTypeID(serviceServerTypeId_);
	    	spc.setServiceExistingServer(serviceExistingServer_);
	    	spc.setIsWebProjectStartupRequested(isWebProjectStartupRequested_);
        spc.setEnvironment( env );
	    	spc.execute( null, null );
	    }
	    
	    if(isTestServiceEnabled_.booleanValue()&& sampleExistingServer_!=null && serviceExistingServer_!=null && !sampleExistingServer_.equals(serviceExistingServer_)){
	    	//System.out.println("Calling client server start: "+sampleProject_+"  "+sampleExistingServer__);
	    	StartProjectCommand spc = new StartProjectCommand( moduleName_ );
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
