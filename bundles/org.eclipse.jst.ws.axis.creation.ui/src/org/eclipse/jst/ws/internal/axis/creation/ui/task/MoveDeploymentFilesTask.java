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
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.FileUtil;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.wst.command.internal.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class MoveDeploymentFilesTask extends AbstractDataModelOperation {

	private MessageUtils msgUtils_;
	private MessageUtils coreMsgUtils_;
	private IProject serverProject;
    private String serviceServerTypeID_;    
	
    private JavaWSDLParameter javaWSDLParam_;
	
	public MoveDeploymentFilesTask( )
    {
      String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
      msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
      coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
    }

  /**
	* Execute DefaultsForJavaToWSDLTask
	*/
	public IStatus execute( IProgressMonitor monitor, IAdaptable adaptable ) 
	{
		IEnvironment environment = getEnvironment();
		IStatus status = Status.OK_STATUS;
		if (javaWSDLParam_ == null) {
		  status = StatusUtils.errorStatus(coreMsgUtils_.getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"));
		  environment.getStatusHandler().reportError(status);
		  return status;
		}

		IProject project = serverProject;
		//String projectURL = ResourceUtils.getEncodedWebProjectURL(project);
		String projectURL = ServerUtils.getEncodedWebComponentURL(project, serviceServerTypeID_);
		
		if (projectURL == null) {
		    status = StatusUtils.errorStatus(msgUtils_.getMessage("MSG_ERROR_PROJECT_URL",new String[] { project.toString()}));
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


		IPath webinfPath = J2EEUtils.getWebInfPath(project );

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
		  status = StatusUtils.errorStatus(msgUtils_.getMessage("MSG_ERROR_MOVE_RESOURCE",new String[] { e.getLocalizedMessage()}), e);
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
    
    public void setServiceServerTypeID(String id)
    {
      serviceServerTypeID_ = id;
    }    
}
