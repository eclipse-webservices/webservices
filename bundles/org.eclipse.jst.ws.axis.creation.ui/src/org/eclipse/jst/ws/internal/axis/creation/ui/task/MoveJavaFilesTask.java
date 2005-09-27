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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.wst.command.internal.provisional.env.core.EnvironmentalOperation;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;


public class MoveJavaFilesTask extends EnvironmentalOperation {

	private JavaWSDLParameter javaWSDLParam_;
	private MessageUtils msgUtils_;
	private MessageUtils coreMsgUtils_;
	private IProject serviceProject_;
	
	private String moduleName_;
	// rm private Model model_;

	public MoveJavaFilesTask(String moduleName) {
	    String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
		this.moduleName_ = moduleName;
	}
	
	public MoveJavaFilesTask() {
	    String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
	}	

	public MoveJavaFilesTask(JavaWSDLParameter javaWSDLParam) {
	    String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
		javaWSDLParam_ = javaWSDLParam;

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

		// rm 
		/*
		if (model_ == null) {
		  status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_MODEL_NOT_SET"), Status.ERROR);
		  return status;		  
		}
		*/

		IProject project = serviceProject_;
		//String projectURL = ResourceUtils.getEncodedWebProjectURL(project);
		String projectURL = ServerUtils.getEncodedWebComponentURL(project, moduleName_);
		if (projectURL == null) {
		    status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_PROJECT_URL",new String[] {project.toString()}), Status.ERROR);
		    environment.getStatusHandler().reportError(status);
		    return status;		  

		} else {
			javaWSDLParam_.setProjectURL(projectURL);
		}

		String[] javaFiles = javaWSDLParam_.getJavaFiles();
		String javaoutput = javaWSDLParam_.getJavaOutput();  // <webproject>/JavaSource
		String output = javaWSDLParam_.getOutput();  		 // <webproject>/WebContent/META_INF

		if(javaFiles == null || javaoutput == null && output == null ){
			return status;
		}
		try {
			for (int i = 0; i < javaFiles.length; i++) {
//				String resourceToMove = javaFiles[i].substring(output.length());
//				String targetFileName = javaoutput + resourceToMove;
//				File resultFile = new File(targetFileName);
				// copy java files that without overwtriting existing ones
//				if (!resultFile.exists()) {
//					FileUtil.createTargetFile(
//						javaFiles[i],
//						javaoutput + resourceToMove);
//				}
				// delete java files from the output directory
				File source = new File(javaFiles[i]);
				source.delete();
			}
		} catch (Exception e) {
		  status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_MOVE_RESOURCE",new String[]{e.getLocalizedMessage()}), Status.ERROR, e);
		  environment.getStatusHandler().reportError(status);
		  return status;		  
		}
		
		return status;
	}

	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam)
	{
		javaWSDLParam_ = javaWSDLParam;
	}
	
	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam_;
	}
	
	public void setServiceProject(IProject serviceProject)
	{
	  serviceProject_ = serviceProject;
	}
	
	// rm
	/*
	public void setModel(Model model)
	{
	  model_ = model;
	}
	*/
}
