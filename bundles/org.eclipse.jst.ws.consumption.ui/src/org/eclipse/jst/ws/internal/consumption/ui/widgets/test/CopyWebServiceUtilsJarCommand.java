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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.plugin.WebServiceConsumptionPlugin;
import org.eclipse.jst.ws.internal.consumption.ui.plugin.WebServiceConsumptionUIPlugin;
import org.eclipse.jst.ws.internal.plugin.WebServicePlugin;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.common.StatusHandler;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.provisional.env.core.context.TransientResourceContext;


public class CopyWebServiceUtilsJarCommand extends SimpleCommand {

	private static java.lang.String DESCRIPTION = "Copy WebserviceUtils.jar";
	private static java.lang.String LABEL           = "CopyWebserviceJarCommand";
    private String sampleProject;
    private MessageUtils msgUtils;
	
/**
 * Default CTOR;
 */


public CopyWebServiceUtilsJarCommand()
{
  super(LABEL,DESCRIPTION);
  String pluginId = "org.eclipse.jst.ws.consumption";
  msgUtils = new MessageUtils(pluginId + ".plugin", this);  
}

/**
 * Execute the command
 */
public Status execute(Environment env)
{
  try
  {
    env.getProgressMonitor().report( msgUtils.getMessage( "PROGRESS_INFO_COPY_WEBSERVICE_UTILS" ) );
    IProject sampleIProject = (IProject)ResourceUtils.findResource(sampleProject);    
//    IPath webModulePath = ResourceUtils.getWebModuleServerRoot(sampleIProject).getFullPath();
    IPath webModulePath = J2EEUtils.getFirstWebContentPath(sampleIProject);	
    if (webModulePath == null)
      return new SimpleStatus(WebServiceConsumptionUIPlugin.ID,msgUtils.getMessage("MSG_ERROR_PROJECT_NOT_FOUND"), Status.ERROR);
      
    Status status = copyIFile("webserviceutils.jar",webModulePath,"WEB-INF/lib/webserviceutils.jar", WebServiceConsumptionPlugin.getInstance(),env);
    if(status.getSeverity() == Status.ERROR){
      StatusHandler sHandler = env.getStatusHandler();
      Status errorStatus = new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_FILECOPY_WEBSERVICE_UTILS"), Status.ERROR);
      sHandler.reportError(errorStatus);	
      return status;
     }
      
      
    }
    catch (Exception e) {
      StatusHandler sHandler = env.getStatusHandler();
      Status errorStatus = new SimpleStatus("", msgUtils.getMessage("MSG_ERROR_FILECOPY_WEBSERVICE_UTILS"), Status.ERROR);
      sHandler.reportError(errorStatus);	
      return new SimpleStatus(WebServiceConsumptionUIPlugin.ID,msgUtils.getMessage("MSG_ERROR_FILECOPY_WEBSERVICE_UTILS"),Status.ERROR,e);
    }
  return new SimpleStatus("");
}

/**
 *
 */
private Status copyIFile(String source, IPath targetPath, String targetFile,Environment env)
{
  return copyIFile(source, targetPath, targetFile, WebServicePlugin.getInstance(),env);
}

/**
 *
 */
private Status copyIFile(String source, IPath targetPath, String targetFile, Plugin plugin,Environment env)
{
    
  if (plugin != null)
  {
    IPath target = targetPath.append(new Path(targetFile));
    
    env.getProgressMonitor().report( msgUtils.getMessage( "PROGRESS_INFO_COPYING_FILE" ) );
    try
    {
       ResourceContext context = new TransientResourceContext();
       context.setOverwriteFilesEnabled(true);
       context.setCreateFoldersEnabled(true);
       context.setCheckoutFilesEnabled(true);
       IResource resource = FileResourceUtils.findResource(target);
       if(resource != null) return new SimpleStatus("");
       IFile file = FileResourceUtils.createFile(context, 
       								target,
                                      plugin.openStream(new Path(source)),
                                      env.getProgressMonitor(),
                                      env.getStatusHandler());
    }
    catch (Exception e) {
      return new SimpleStatus(WebServiceConsumptionUIPlugin.ID,msgUtils.getMessage("MSG_ERROR_FILECOPY_WEBSERVICE_UTILS"),Status.ERROR,e);
    }
  }
  return new SimpleStatus("");
}

public void setSampleProject(String sampleProject)
{
  this.sampleProject = sampleProject;  
}



}
