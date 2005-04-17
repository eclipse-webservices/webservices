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


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.j2ee.internal.project.IWebNatureConstants;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.FileUtil;
import org.eclipse.jst.ws.internal.axis.creation.ui.plugin.WebServiceAxisCreationUIPlugin;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;

public class UpdateAxisWSDDFileTask extends SimpleCommand {
	
	
	private final String LABEL = "TASK_LABEL_UPDATE_AXIS_WSDD";	
	private final String DESCRIPTION = "TASK_DESC_UPDATE_AXIS_WSDD";
	private final String DEPLOY_XSL = "deploy.xsl";	//$NON-NLS-1$
	private final String DEPLOY_BAK = "deploy.wsdd.bak";		//$NON-NLS-1$
	private final String CLASSNAME_PARAM = "newClassName";		//$NON-NLS-1$
    private MessageUtils msgUtils_;
    private MessageUtils coreMsgUtils_;
	private JavaWSDLParameter javaWSDLParam_;
	private IProject serviceProject_;
	// rm private Model model_;

	public UpdateAxisWSDDFileTask() {
	    String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
	    setDescription(msgUtils_.getMessage(DESCRIPTION));
	    setName(msgUtils_.getMessage(LABEL));	    
	}
	
	public UpdateAxisWSDDFileTask(JavaWSDLParameter javaWSDLParam) {
	    String pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils(pluginId + ".plugin", this);
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
	    setDescription(msgUtils_.getMessage(DESCRIPTION));
	    setName(msgUtils_.getMessage(LABEL));	    
		javaWSDLParam_ = javaWSDLParam;
	}

	/**
	* Execute UpdateAxisWSDDFileTask
	*/
	public Status execute(Environment env) {
	  
	  // rm
	  /*
	  //Begin Setters
	  WebServiceElement wse =
		WebServiceElement.getWebServiceElement(model_);
	  setServiceProject(wse.getServiceProject());
	  //End Setters
	  */
	    Status status = new SimpleStatus("");		
		if (javaWSDLParam_ == null) {
		  status = new SimpleStatus("",coreMsgUtils_.getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR);
		  env.getStatusHandler().reportError(status);
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
		String projectURL = ResourceUtils.getEncodedWebProjectURL(project);
		if (projectURL == null) {
		    status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_PROJECT_URL",new String[] {project.toString()}), Status.ERROR);
		    env.getStatusHandler().reportError(status);
		    return status;		  
		} else {
			javaWSDLParam_.setProjectURL(projectURL);
		}
		String outputLocation = javaWSDLParam_.getJavaOutput();
		if (outputLocation == null) {
			return status;
		}
//		try {
//			if (!project.hasNature(IWebNatureConstants.J2EE_NATURE_ID))
//				return status;
//		} catch (Exception ex) {
//		    status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_INTERAL"), Status.ERROR, ex);
//		    env.getStatusHandler().reportError(status);
//		    return status;		  
//		}
		String[] javaFiles = javaWSDLParam_.getJavaFiles();
		String javaoutput = javaWSDLParam_.getJavaOutput();
		String output = javaWSDLParam_.getOutput();

//		String webContentPath =	ResourceUtils.getWebModuleServerRoot(project).getLocation().toString();
		String webContentPath =	J2EEUtils.getFirstWebContentContainer(project).getLocation().toString();
		try {

			if (javaWSDLParam_.getDeploymentFiles() != null
				&& javaWSDLParam_.getDeploymentFiles().length > 0) {

				String wsdd_deploy = javaWSDLParam_.getDeploymentFiles()[0];
				Path deployPath = new Path(wsdd_deploy);
				String deployBackup =
					deployPath
						.removeLastSegments(1)
						.append(DEPLOY_BAK)
						.toString();
				FileUtil.createTargetFile(wsdd_deploy, deployBackup, true);
				String deployXSL = getPluginFilePath(DEPLOY_XSL).toString();

				TransformerFactory tFactory = TransformerFactory.newInstance();

				// Use the TransformerFactory to instantiate a Transformer that will work with  
				// the stylesheet you specify. This method call also processes the stylesheet
				// into a compiled Templates object.
				Transformer transformer =
					tFactory.newTransformer(new StreamSource(deployXSL));
				transformer.setParameter(CLASSNAME_PARAM, javaWSDLParam_.getBeanName());

				// Use the Transformer to apply the associated Templates object to an XML document
				// (foo.xml) and write the output to a file (foo.out).
				transformer.transform(
					new StreamSource(deployBackup),
					new StreamResult(new FileOutputStream(wsdd_deploy)));

			}

		} catch (Exception e) {
		    String[] errorMsgStrings = new String[]{project.toString(), outputLocation.toString(), webContentPath.toString()}; 
		    status = new SimpleStatus("",msgUtils_.getMessage("MSG_ERROR_UPDATE_AXIS_WSDD", errorMsgStrings), Status.ERROR, e);
		    env.getStatusHandler().reportError(status);
		    return status;		  		  
		}
		
		return status;
	}

	private IPath getPluginFilePath(String pluginfileName)
		throws CoreException {
		try {
			IPluginRegistry pluginRegistry = Platform.getPluginRegistry();
			IPluginDescriptor pluginDescriptor =
				pluginRegistry.getPluginDescriptor(WebServiceAxisCreationUIPlugin.ID);

			URL localURL =
				Platform.asLocalURL(
					new URL(pluginDescriptor.getInstallURL(), pluginfileName));
			return new Path(localURL.getFile());
		} catch (MalformedURLException e) {
			throw new CoreException(
				new org.eclipse.core.runtime.Status(
					IStatus.WARNING,
					WebServiceAxisCreationUIPlugin.ID,
					0,
					msgUtils_.getMessage(
						"MSG_PLUGIN_FILE_URL"),
					e));
		} catch (IOException e) {
			throw new CoreException(
				new org.eclipse.core.runtime.Status(
					IStatus.WARNING,
					WebServiceAxisCreationUIPlugin.ID,
					0,
					msgUtils_.getMessage(
						"MSG_PLUGIN_FILE_URL"),
					e));
		}
	}


	/**
	* Sets the javaWSDLParam.
	* @param javaWSDLParam The javaWSDLParam to set
	*/
	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
		javaWSDLParam_ = javaWSDLParam;
	}
	
	public JavaWSDLParameter getJavaWSDLParam()
	{
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
