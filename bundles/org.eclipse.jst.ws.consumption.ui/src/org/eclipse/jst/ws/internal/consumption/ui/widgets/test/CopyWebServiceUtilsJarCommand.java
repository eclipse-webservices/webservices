/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.ws.internal.consumption.ui.widgets.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.consumption.ConsumptionMessages;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.ProgressUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.core.context.TransientResourceContext;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;


public class CopyWebServiceUtilsJarCommand extends AbstractDataModelOperation 
{
  private String sampleProject;
	
/**
 * Default CTOR;
 */


public CopyWebServiceUtilsJarCommand()
{

}

/**
 * Execute the command
 */
public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable )
{
  IEnvironment env = getEnvironment();
  
  try
  {
    ProgressUtils.report( monitor, ConsumptionMessages.PROGRESS_INFO_COPY_WEBSERVICE_UTILS );
    IProject sampleIProject = ProjectUtilities.getProject(sampleProject);    
    IPath webModulePath = J2EEUtils.getWebContentPath(sampleIProject);	
    if (webModulePath == null)
      return StatusUtils.errorStatus( ConsumptionMessages.MSG_ERROR_PROJECT_NOT_FOUND );
      
    IStatus status = copyIFile("webserviceutils.jar",webModulePath,"WEB-INF/lib/webserviceutils.jar", WebServiceConsumptionPlugin.getInstance(),env, monitor);
    if(status.getSeverity() == Status.ERROR){
      IStatusHandler sHandler = env.getStatusHandler();
      IStatus errorStatus = StatusUtils.errorStatus( ConsumptionMessages.MSG_ERROR_FILECOPY_WEBSERVICE_UTILS );
      sHandler.reportError(errorStatus);	
      return status;
     }
      
      
    }
    catch (Exception e) {
      IStatusHandler sHandler = env.getStatusHandler();
      IStatus errorStatus = StatusUtils.errorStatus( ConsumptionMessages.MSG_ERROR_FILECOPY_WEBSERVICE_UTILS );
      sHandler.reportError(errorStatus);	
      return StatusUtils.errorStatus( ConsumptionMessages.MSG_ERROR_FILECOPY_WEBSERVICE_UTILS, e);
    }
  return Status.OK_STATUS;
}

/**
 *
 */
private IStatus copyIFile(String source, IPath targetPath, String targetFile, Plugin plugin,IEnvironment env, IProgressMonitor monitor )
{
    
  if (plugin != null)
  {
    IPath target = targetPath.append(new Path(targetFile));
    
    ProgressUtils.report( monitor, ConsumptionMessages.PROGRESS_INFO_COPYING_FILE );
    try
    {
       ResourceContext context = new TransientResourceContext();
       context.setOverwriteFilesEnabled(true);
       context.setCreateFoldersEnabled(true);
       context.setCheckoutFilesEnabled(true);
       IResource resource = FileResourceUtils.findResource(target);
       if(resource != null) return Status.OK_STATUS;
       FileResourceUtils.createFile(context, 
                      							target,
                                    plugin.openStream(new Path(source)),
                                    monitor,
                                    env.getStatusHandler());
    }
    catch (Exception e) {
      return StatusUtils.errorStatus( ConsumptionMessages.MSG_ERROR_FILECOPY_WEBSERVICE_UTILS ,e);
    }
  }
  return Status.OK_STATUS;
}

public void setSampleProject(String sampleProject)
{
  this.sampleProject = sampleProject;  
}


}
