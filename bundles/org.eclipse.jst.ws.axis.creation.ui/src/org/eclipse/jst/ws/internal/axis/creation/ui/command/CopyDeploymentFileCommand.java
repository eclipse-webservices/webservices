/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060524   130755 kathy@ca.ibm.com - Kathy Chan
 * 20061221   168787 kathy@ca.ibm.com - Kathy Chan
 * 20070815   188999 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.command;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.command.internal.env.core.context.ResourceContext;
import org.eclipse.wst.command.internal.env.eclipse.BaseEclipseEnvironment;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.internal.IModulePublishHelper;

/**
 * 
 * This command copies the server-config.wsdd file to it's proper location in the module.
 * 
 */

public class CopyDeploymentFileCommand extends AbstractDataModelOperation
{
	private final String WEB_INF = "WEB-INF";
	private final String SERVER_CONFIG = "server-config.wsdd";
    private String projectName_;
    private String EARProjectName_;
	private String serverInstanceId_;
  
  /**
   * Constructor for CopyDeploymentFileCommand.
 * @param String description
 * @param String name
   * 
   */
  public CopyDeploymentFileCommand( String projectName, String earProjectName )
  { 
    projectName_   = projectName;
    EARProjectName_   = earProjectName;
  }

  public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
  {
	  IStatus status = Status.OK_STATUS;
	  IEnvironment environment = getEnvironment();
	  
	  FileInputStream finStream = null;
	  try
	  {
		  
		  IVirtualComponent component = J2EEUtils.getVirtualComponent( projectName_ );
		  IServer server = ServerCore.findServer( serverInstanceId_ );
		  IProject project = ProjectUtilities.getProject(projectName_);
		  IModule projectModule = ServerUtil.getModule(project);
		  
		  if (server != null && component != null && projectModule != null) {
			  IModulePublishHelper publishHelper = (IModulePublishHelper) 
			  	server.loadAdapter(IModulePublishHelper.class, monitor);
			  if (publishHelper != null) {
				  IModule[] serverModules;
				  if (EARProjectName_ == null) {
					  serverModules = new IModule [] {projectModule};
				  } else {
					  IProject EARProject = ProjectUtilities.getProject(EARProjectName_);
					  IModule EARProjectModule = ServerUtil.getModule(EARProject);
					  serverModules = new IModule [] {EARProjectModule, projectModule};
				  }
				  IPath publishDirPath = publishHelper.getPublishDirectory(serverModules);
				  
				  if (publishDirPath != null) {
					  IVirtualFolder rootFolder = component.getRootFolder();
					  IPath rootFolderPath = ResourceUtils.getWorkspaceRoot().getFile(rootFolder.getWorkspaceRelativePath()).getLocation();
					  if (rootFolderPath != null) {
						  File rootFolderFile = rootFolderPath.toFile();
						  File publishDirFile = publishDirPath.toFile();
						  if (!rootFolderFile.equals(publishDirFile)) {
							  // copy the server-config.wsdd if the publish directory is not in the same as the project root folder
							  IPath path = new Path( WEB_INF ).append( SERVER_CONFIG );
							  IPath serverConfigPath = publishDirPath.append(path);
							  if (serverConfigPath != null) {			  
								  IVirtualFile newLocation = rootFolder.getFile(path);
								  IPath targetPath = newLocation.getWorkspaceRelativePath();
								  if (targetPath != null) {
									  finStream = new FileInputStream(serverConfigPath.toString());
									  if (finStream != null) 
									  {
										  IStatusHandler  statusHandler = environment.getStatusHandler();
									    ResourceContext context       = ((BaseEclipseEnvironment)environment).getResourceContext();
									    
										  FileResourceUtils.createFile(context,  
												  targetPath,
												  finStream,
												  monitor,
												  statusHandler);
										  finStream.close();
									  }
								  }
							  }
						  }
					  }
				  }  
			  } 	  		  
		  }	  
	  }
	  catch( Throwable e )
	  {
		  status = StatusUtils.errorStatus(NLS.bind(AxisConsumptionCoreMessages.MSG_ERROR_MOVE_RESOURCE, new String[]{e.getLocalizedMessage()}), e);
		  environment.getStatusHandler().reportError(status);
	  } finally {
		  if (finStream != null) {
			  try {
				  finStream.close();
			  } catch (IOException e) {
			  }
		  }			
	  }
	  
	  return status;
	  
  }

	public void setServerInstanceId(String serverInstanceId) {
		this.serverInstanceId_ = serverInstanceId;
	}
}
