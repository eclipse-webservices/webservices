/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
  private String project;
  private String module;
	
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
	    IProject iproject = ProjectUtilities.getProject(project);
	    if (!J2EEUtils.isJavaComponent(iproject))
	    {      
        // Get the IModule as specified by the module name
        IModule imodule = ServerUtils.getModule(iproject, module);

	    	// TODO:  This workaround for 156768 should be removed once the defect is fixed
	    	if (imodule == null) {
	    	    // calling incremental build on the project before trying again
	    		iproject.build(IncrementalProjectBuilder.INCREMENTAL_BUILD,null);
	    		imodule = ServerUtils.getModule(iproject);
	    		if (imodule == null) {  
	    			// return error if module is still null after 1 retry
	    			status = StatusUtils.errorStatus( NLS.bind(ConsumptionMessages.MSG_ERROR_ADD_MODULE, new String[]{module}) );
	    			env.getStatusHandler().reportError(status);
	    			return status;
	    		}
	    	}
	    	// end of workaround for 156768
	    	
	      if (!ServerUtil.containsModule(server, imodule, null))
	      {
	        IModule[] imodules = new IModule[]{imodule};
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
		this.project = project;
	}

	public void setServerInstanceId(String serverInstanceId)
	{
		this.serverInstanceId = serverInstanceId;
	}	
}
