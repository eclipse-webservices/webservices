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
package org.eclipse.jst.ws.internal.axis.creation.ui.task;


import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.FileUtil;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

public class MoveDeploymentFilesTask extends EnvironmentalOperation {

	private MessageUtils msgUtils_;
	private MessageUtils coreMsgUtils_;
	private IProject serverProject;
	private String   moduleName_;
	
    private JavaWSDLParameter javaWSDLParam_;
	
	public MoveDeploymentFilesTask( String moduleName )
    {
      String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
      msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
      coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
	  moduleName_ = moduleName;
    }

  /**
	* Execute DefaultsForJavaToWSDLTask
	*/
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		Environment environment = getEnvironment();
		Status status = new SimpleStatus("");
		if (javaWSDLParam_ == null) {
		  status = new SimpleStatus("",coreMsgUtils_.getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR);
		  environment.getStatusHandler().reportError(status);
		  return status;
		}

		IProject project = serverProject;
		//String projectURL = ResourceUtils.getEncodedWebProjectURL(project);
		String projectURL = ServerUtils.getEncodedWebComponentURL(project, moduleName_);
		
		if (projectURL == null) {
		    status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_PROJECT_URL",new String[] { project.toString()}), Status.ERROR);
		    environment.getStatusHandler().reportError(status);
		    return status;		  
		} else {
			javaWSDLParam_.setProjectURL(projectURL);
		}

		try {
		String[] deployFiles = javaWSDLParam_.getDeploymentFiles();
		String javaOutput = javaWSDLParam_.getJavaOutput();

		if (deployFiles == null || javaOutput == null) {
			return status;
		}


		IPath webinfPath = J2EEUtils.getWebInfPath(project, moduleName_ );

			for (int i = 0; i < deployFiles.length; i++) {
        File f = new File(deployFiles[i]);
        String resourceToMove = f.getName();
				String targetFileName = ResourceUtils.getWorkspaceRoot().getFile(webinfPath.addTrailingSeparator().append(resourceToMove)).getLocation().toString();
				FileUtil.createTargetFile(deployFiles[i], targetFileName, true);
				File deploymentFile = new File(deployFiles[i]);
				deploymentFile.delete();
				deployFiles[i] = targetFileName;
			}

		} catch (Exception e) {
		  status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_MOVE_RESOURCE",new String[] { e.getLocalizedMessage()}), Status.ERROR, e);
		  environment.getStatusHandler().reportError(status);
		  return status;		  
		}
		
		return status;
	}

	public JavaWSDLParameter getJavaWSDLParam()
	{
		return javaWSDLParam_;
	}
	
	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam)
	{
		javaWSDLParam_ = javaWSDLParam;
	}
	
	public void setServerProject(IProject serverProject)
	{
	  this.serverProject = serverProject;
	}
}
