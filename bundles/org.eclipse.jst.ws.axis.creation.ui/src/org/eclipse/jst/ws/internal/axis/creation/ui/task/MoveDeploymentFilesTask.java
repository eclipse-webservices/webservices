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
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.j2ee.internal.project.IWebNatureConstants;
import org.eclipse.jst.j2ee.internal.web.operations.J2EEWebNatureRuntime;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.util.FileUtil;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;

public class MoveDeploymentFilesTask extends SimpleCommand {

	private final String LABEL = "TASK_LABEL_MOVE_DEPLOYMENT_FILES";
	private final String DESCRIPTION = "TASK_DESC_MOVE_DEPLOYMENT_FILES";
	private final String WEB_INF = "WEB-INF";	//$NON-NLS-1$
	private final String META_INF = "META-INF";	//$NON-NLS-1$

	private MessageUtils msgUtils_;
	private MessageUtils coreMsgUtils_;
	private IProject serverProject;
  private JavaWSDLParameter javaWSDLParam_;
	
	public MoveDeploymentFilesTask()
  {
    String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
    setDescription(msgUtils_.getMessage(DESCRIPTION));
    setName(msgUtils_.getMessage(LABEL));
  }

  /**
	* Execute DefaultsForJavaToWSDLTask
	*/
	public Status execute(Environment env) {
		Status status = new SimpleStatus("");
		if (javaWSDLParam_ == null) {
		  status = new SimpleStatus("",coreMsgUtils_.getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR);
		  env.getStatusHandler().reportError(status);
		  return status;
		}

		IProject project = serverProject;
		String projectURL = ResourceUtils.getEncodedWebProjectURL(project);
		if (projectURL == null) {
		    status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_PROJECT_URL",new String[] { project.toString()}), Status.ERROR);
		    env.getStatusHandler().reportError(status);
		    return status;		  
		} else {
			javaWSDLParam_.setProjectURL(projectURL);
		}

//		try {
//			if (!project.hasNature(IWebNatureConstants.J2EE_NATURE_ID))
//				return status;
//		} catch (Exception ex) {
//		  status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_INTERAL"), Status.ERROR, ex);
//		  env.getStatusHandler().reportError(status);
//		  return status;		  
//		}
		String[] deployFiles = javaWSDLParam_.getDeploymentFiles();
		String javaOutput = javaWSDLParam_.getJavaOutput();

		if (deployFiles == null || javaOutput == null) {
			return status;
		}


		IPath webinfPath;
		webinfPath = J2EEUtils.getFirstWebInfPath(project);

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
		  env.getStatusHandler().reportError(status);
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
