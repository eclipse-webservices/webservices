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
package org.eclipse.jst.ws.internal.axis.creation.ui.task;


import java.io.File;
import java.io.FileInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.common.EnvironmentUtils;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.command.env.core.context.ResourceContext;
import org.eclipse.wst.command.env.core.context.TransientResourceContext;
import org.eclipse.wst.command.internal.env.common.FileResourceUtils;
import org.eclipse.wst.server.core.IServer;

public class CopyAxisServerConfigTask extends SimpleCommand {


	private final String LABEL = "TASK_LABEL_COPY_SERVER_CONFIG_FILES";
	private final String DESCRIPTION ="TASK_DESC_COPY_SERVER_CONFIG_FILES";
	
	private final String DEPLOYEDSERVICES = "WEB-INF" + File.separator + "server-config.wsdd"; //$NON-NLS-1$
    private MessageUtils msgUtils_;
    private MessageUtils coreMsgUtils_;
    private MessageUtils baseConMsgUtils_;
	private JavaWSDLParameter javaWSDLParam_;
	private IProject serviceProject_;
	private Boolean projectRestartRequired_;
	private String serviceServerTypeID_;
	private IServer serviceExistingServer_;
	// rm private Model model_;

	public CopyAxisServerConfigTask() {
		String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
		msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
	    baseConMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.consumption.plugin", this );
		setDescription(msgUtils_.getMessage(DESCRIPTION));
		setName(msgUtils_.getMessage(LABEL));	 

	}
	
	public CopyAxisServerConfigTask(JavaWSDLParameter javaWSDLParam) {
		String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
		msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
	    baseConMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.consumption.plugin", this );
		setDescription(msgUtils_.getMessage(DESCRIPTION));
		setName(msgUtils_.getMessage(LABEL));	  
		javaWSDLParam_ = javaWSDLParam;

	}

	/**
	* Execute CopyAxisServerConfigTask
	*/
	public Status execute(Environment env) {
	  
	  // rm 
	  /*
	    //Begin setters
	  WebServiceElement webServiceElement =
		WebServiceElement.getWebServiceElement(model_);
	  setServiceProject(webServiceElement.getServiceProject());
	  setServiceServerTypeID(webServiceElement.getServiceServerTypeID());
	  setServiceExistingServer(webServiceElement.getServiceExistingServer());
	  
	    //End Setters
	     */
	  
	  
	    Status status = new SimpleStatus( "" );
	    
		if (javaWSDLParam_ == null) {
		    status = new SimpleStatus("", coreMsgUtils_.getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR);
		    env.getStatusHandler().reportError(status);
		    return status;
		}

		// rm 
		/*
		if (model_ == null) {
		    status = new SimpleStatus("", msgUtils_.getMessage("MSG_ERROR_MODEL_NOT_SET"), Status.ERROR);
		    return status;
		}
		*/
		
		try {

		    projectRestartRequired_ = Boolean.TRUE;
			// rm webServiceElement.setProjectRestartRequired(true);

			IProject project = serviceProject_;
			if (project == null) {
			    status = new SimpleStatus("", baseConMsgUtils_.getMessage("MSG_ERROR_PROJECT_NOT_FOUND"), Status.ERROR);
			    env.getStatusHandler().reportError(status);
			    return status;
			}

			IServer instance =
				ServerUtils.getServerForModule(
					ResourceUtils.getModule(project),
					serviceServerTypeID_,
					serviceExistingServer_,
					true,
					EnvironmentUtils.getIProgressMonitor(env));
			if (instance == null) {
			    status = new SimpleStatus("", baseConMsgUtils_.getMessage("MSG_ERROR_INSTANCE_NOT_FOUND"), Status.ERROR);
			    env.getStatusHandler().reportError(status);
			    return status;
			}

// M5 Port:  replacing instance.isLocal() with instance.getFactoryId().indexOf("test") > 0
			if (!(instance.getServerType().getId().indexOf(".te") > 0)) {
				IPath installPath = instance.getRuntime().getLocation();
				IPath sourcePath = installPath.append(
						"webapps").append(  //$NON-NLS-1$
						project.getName());

//				IPath webModulePath = ResourceUtils.getWebModuleServerRoot(project).getFullPath();
				IPath webModulePath = J2EEUtils.getFirstWebContentPath(project);
				ResourceContext context = new TransientResourceContext();
				context.setOverwriteFilesEnabled(true);
				context.setCreateFoldersEnabled(true);
				context.setCheckoutFilesEnabled(true);
				IFile file =
					FileResourceUtils.createFile(
						context,
						webModulePath.append(DEPLOYEDSERVICES),
						new FileInputStream(
							sourcePath.append(DEPLOYEDSERVICES).toOSString()),
						env.getProgressMonitor(),
						env.getStatusHandler());
			}
			return status;
			
		} catch (Exception e) {
		  return status;
		}

	}


	/**
	* Sets the javaWSDLParam.
	* @param javaWSDLParam The javaWSDLParam to set
	*/
	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
		javaWSDLParam_ = javaWSDLParam;
	}
	
	public void setServiceProject(IProject serviceProject)
	{
	  serviceProject_ = serviceProject;
	}
	
	public void setServiceServerTypeID(String serviceServerTypeID)
	{
	  serviceServerTypeID_ = serviceServerTypeID;
	}
	
	public void setServiceExistingServer(IServer serviceExistingServer)
	{
	  serviceExistingServer_ = serviceExistingServer;
	}	

	// rm 
	/*
	public void setModel(Model model)
	{
	  model_ = model;
	}
	*/
	
	public Boolean getProjectRestartRequired()
	{
	  return projectRestartRequired_;
	}
		
}
