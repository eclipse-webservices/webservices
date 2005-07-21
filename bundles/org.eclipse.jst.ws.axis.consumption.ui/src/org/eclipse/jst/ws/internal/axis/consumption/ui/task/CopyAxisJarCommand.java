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
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.command.internal.provisional.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.provisional.env.core.context.TransientResourceContext;


public class CopyAxisJarCommand extends SimpleCommand {

  public static String AXIS_RUNTIME_PLUGIN_ID = "org.apache.axis"; //$NON-NLS-1$
  public static String[] JARLIST = new String[] {
	  "axis-ant.jar",
	  "axis.jar",
	  "commons-discovery-0.2.jar",
	  "commons-logging-1.0.4.jar",
	  "jaxrpc.jar",
	  "log4j-1.2.8.jar",
	  "saaj.jar",
	  "wsdl4j-1.5.1.jar"
  };

  private String DESCRIPTION = "TASK_DESC_COPY_JARS_TO_PROJECT";
  private String LABEL = "TASK_LABEL_COPY_JARS_TO_PROJECT";

  private MessageUtils msgUtils_;
  private MessageUtils baseConMsgUtils_;
  private IProject project;
  private Boolean projectRestartRequired_ = Boolean.FALSE;
  private String  moduleName_;

  /**
   * Default CTOR;
   */
  public CopyAxisJarCommand( String moduleName ) {
    String pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
    baseConMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.consumption.plugin", this );
    setDescription(msgUtils_.getMessage(DESCRIPTION));
    setName(msgUtils_.getMessage(LABEL));
	
	moduleName_ = moduleName;
  }

  /**
   * Execute the command
   */
  public Status execute(Environment env) {
    Status status = new SimpleStatus("");
    env.getProgressMonitor().report(msgUtils_.getMessage("PROGRESS_INFO_COPY_AXIS_CFG"));
    copyAxisJarsToProject(project, status, env);
    return status;

  }

  private void copyAxisJarsToProject(IProject project, Status status, Environment env) {
//    IPath webModulePath = ResourceUtils.getWebModuleServerRoot(project).getFullPath();
	IPath webModulePath = J2EEUtils.getWebContentPath( project, moduleName_ );
    if (webModulePath == null) {
      status = new SimpleStatus("", baseConMsgUtils_.getMessage("MSG_ERROR_PROJECT_NOT_FOUND"), Status.ERROR);
      env.getStatusHandler().reportError(status);
      return;
    }
	
	for (int i=0; i<JARLIST.length; ) {
		copyIFile("lib/"+JARLIST[i], webModulePath, "WEB-INF/lib/"+JARLIST[i++], status, env); 
	    if (status.getSeverity() == Status.ERROR)
	      return;
	}
    return;
  }

  /**
   *  
   */
  private void copyIFile(String source, IPath targetPath, String targetFile, Status status, Environment env) {
    IPath target = targetPath.append(new Path(targetFile));
    env.getProgressMonitor().report(baseConMsgUtils_.getMessage("PROGRESS_INFO_COPYING_FILE"));

    try {
      ResourceContext context = new TransientResourceContext();
      context.setOverwriteFilesEnabled(true);
      context.setCreateFoldersEnabled(true);
      context.setCheckoutFilesEnabled(true);
      IPluginRegistry pluginRegistry = Platform.getPluginRegistry();
      IPluginDescriptor pluginDescriptor = pluginRegistry.getPluginDescriptor(AXIS_RUNTIME_PLUGIN_ID);
      Plugin axisrt_plugin = pluginDescriptor.getPlugin();
      IFile resource = ResourceUtils.getWorkspaceRoot().getFile(target);
      if (!resource.exists()) {
        IFile file = FileResourceUtils.createFile(context, target, axisrt_plugin.openStream(new Path(source)), env.getProgressMonitor(), env
            .getStatusHandler());
        if (projectRestartRequired_.booleanValue() == false && file.exists()) {
          projectRestartRequired_ = Boolean.TRUE;
        }

      }
    }
    catch (Exception e) {
      status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_FILECOPY"), Status.ERROR, e);
      env.getStatusHandler().reportError(status);

    }
  }

  public void setProject(IProject project) {
    this.project = project;
  }

  public boolean getProjectRestartRequired() {
    return projectRestartRequired_.booleanValue();
  }
}
