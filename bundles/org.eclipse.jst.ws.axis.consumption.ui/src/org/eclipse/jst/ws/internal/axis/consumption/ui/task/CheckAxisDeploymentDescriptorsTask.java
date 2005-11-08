/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.task;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.ui.eclipse.EnvironmentUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.StatusException;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class CheckAxisDeploymentDescriptorsTask extends AbstractDataModelOperation {
	
	private IProject serverProject;
    
  public CheckAxisDeploymentDescriptorsTask( )
  {
  }
	
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		IEnvironment env = getEnvironment();
		IStatus status = Status.OK_STATUS;
		if(EnvironmentUtils.getResourceContext(env).isOverwriteFilesEnabled()) {
      return status;
		}

	  IPath    filePath = null;
      IProject project = serverProject;
	  	  

      filePath = J2EEUtils.getWebInfPath( project );
      filePath = filePath.append("/server-config.wsdd");

	  if(filePath==null || filePath.isEmpty())
		{
			status = StatusUtils.errorStatus( AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET);
			env.getStatusHandler().reportError(status);
			return status;			
		}

		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
		if (file.exists() && !EnvironmentUtils.getResourceContext(env).isOverwriteFilesEnabled())   {
			status = StatusUtils.warningStatus( AxisConsumptionUIMessages.MSG_ERROR_XML_FILE_OVERWRITE_DISABLED);
			try
			{
				env.getStatusHandler().report(status);
			}
			catch(StatusException se)
			{
				status = StatusUtils.errorStatus( "User aborted");
			}
			
			return status;
		}
		return status;
	}

	public void setServerProject(IProject serverProject)
	{
	  this.serverProject = serverProject;
	}
}
