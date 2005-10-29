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

package org.eclipse.jst.ws.internal.consumption.command.common;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
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
  private MessageUtils msgUtils_;
	  
  private String serverInstanceId;
	private String project;
	private String module;
	
	public AddModuleToServerCommand()
	{
	  String pluginId = "org.eclipse.jst.ws.consumption";
	  msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	}
	
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
	{
      IEnvironment env = getEnvironment();
      
	    IStatus status = Status.OK_STATUS;	    
	    
	    IServer server = ServerCore.findServer(serverInstanceId);
	    if (server == null)
	    {
	      status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_INSTANCE_NOT_FOUND") );
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
	      IModule imodule = ServerUtils.getModule(iproject);
	      if (!ServerUtil.containsModule(server, imodule, null))
	      {
	        IModule[] imodules = new IModule[]{imodule};
	        serverwc = server.createWorkingCopy();
	        ServerUtil.modifyModules(serverwc, imodules, null, null);
	      }

	    }
	    } catch (CoreException e)
	    {
	      status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_ADD_MODULE", new String[]{module}) );
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
	        status = StatusUtils.errorStatus( msgUtils_.getMessage("MSG_ERROR_ADD_MODULE", new String[] { module }) );
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
