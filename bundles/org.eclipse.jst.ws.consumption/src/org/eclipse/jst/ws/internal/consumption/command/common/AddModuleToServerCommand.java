/*******************************************************************************
 * Copyright (c) 2005, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060921 [158210] kathy@ca.ibm.com - Kathy Chan, Calling incremental build on the project before adding to server
 * 20080415   227237 gilberta@ca.ibm.com - Gilbert Andrews
 * 20090311 250984   mahutch@ca.ibm.com - Mark Hutchinson, Use another mechanism to wait for build to be completed
 * 20090518 [252077] tangg@emc.com - Gary Tang, Fail to deploy an EAR project if it contains a module
 *                   kchong@ca.ibm.com - Keith Chong, (updated patch)
 * 20110125   335246 mahutch@ca.ibm.com - Mark Hutchinson, Web Service Client Wizard should not publish JPA projects to the server
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerWorkingCopy;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;

public class AddModuleToServerCommand extends AbstractDataModelOperation
{
  
  private String serverInstanceId;
  private String moduleProjectName = null;
  private String earProjectName = null;
  private String module = null;
  private String earModule = null;
	
	public AddModuleToServerCommand()
	{
	}
	
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
	{
      IEnvironment env = getEnvironment();
      
	    IStatus status = Status.OK_STATUS;	    
	    IServer server = null;
	    if(!(serverInstanceId == null))
	    	server = ServerCore.findServer(serverInstanceId);
	    if (server == null)
	    {
	      status = StatusUtils.errorStatus( ConsumptionMessages.MSG_ERROR_INSTANCE_NOT_FOUND );
	      env.getStatusHandler().reportError(status);
	      return status;
	    }
	   
	    IServerWorkingCopy serverwc = null;
	    
	    try
	    {
	    //Ensure the module is not a Java utility
	    IProject moduleProject = ProjectUtilities.getProject(moduleProjectName);
	    if (!J2EEUtils.isJavaComponent(moduleProject))
	    {
	    	IProject projectToAdd = null;
	    	IModule moduleToAdd;
	    	if (earProjectName != null && earModule != null && earProjectName.length() > 0 && earModule.length() > 0) 
	    	{
	    		//if there is an EAR, that is what we want to publish
	    		projectToAdd = ProjectUtilities.getProject(earProjectName);
	    		// Get the IModule as specified by the module name
		        moduleToAdd = ServerUtils.getModule(projectToAdd, earModule);
	    	} else {
	    		//if there is no EAR then we try and publish the module
	    		projectToAdd = moduleProject;
	    		// Get the IModule as specified by the module name
		        moduleToAdd = ServerUtils.getModule(projectToAdd, module);
	    	}
	    	
	    	// TODO:  This workaround for 156768 should be removed once the defect is fixed
	    	if (moduleToAdd == null) {
	    	    // calling incremental build on the project before trying again
	    		projectToAdd.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,null);
	    		moduleToAdd = ServerUtils.getModule(projectToAdd);
	    		if (moduleToAdd == null) {  
	    			// return error if module is still null after 1 retry
	    			status = StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_ADD_MODULE, new String[]{module}) );
	    			env.getStatusHandler().reportError(status);
	    			return status;
	    		}
	    	}
	    	// end of workaround for 156768
	    	
		    if (!ServerUtil.containsModule(server, moduleToAdd, null))
		    {
		      IModule[] imodules = new IModule[]{moduleToAdd};
		      serverwc = server.createWorkingCopy();
		      ServerUtil.modifyModules(serverwc, imodules, null, null);
		    }
	    }
	    } catch (CoreException e)
	    {
	      status = StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_ADD_MODULE, new String[]{module}), e);
	      env.getStatusHandler().reportError(status);
	      return status;      
	    } finally
	    {
	      try
	      {
	        if (serverwc != null)
	        {
	          serverwc.save(true, null);
	        }
	      } catch (CoreException ce)
	      {
	        status = StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_ADD_MODULE, new String[] { module }), ce);
	        env.getStatusHandler().reportError(status);
	        return status;
	      }      
	    }
	    
		return status;
	}

	public void setModule(String module)
	{
		this.module = module;
	}

	public void setProject(String project)
	{
		this.moduleProjectName = project;
	}
	
	public void setEarProject(String earProject)
	{
		this.earProjectName = earProject;
	}
	
	public void setEarModule(String earModule)
	{
		this.earModule = earModule;
	}

	public void setServerInstanceId(String serverInstanceId)
	{
		this.serverInstanceId = serverInstanceId;
	}	
}
