/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.creation.ui.task;


import javax.wsdl.Definition;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.j2ee.internal.project.IWebNatureConstants;
import org.eclipse.jst.j2ee.internal.web.operations.J2EEWebNatureRuntime;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.util.ClasspathUtils;
import org.eclipse.jst.ws.internal.axis.consumption.util.FileUtil;
import org.eclipse.jst.ws.internal.axis.consumption.util.PlatformUtils;
import org.eclipse.jst.ws.internal.axis.consumption.util.WSDLUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.jst.ws.internal.consumption.ui.wsil.Utils;
import org.eclipse.wst.command.env.core.SimpleCommand;
import org.eclipse.wst.command.env.core.common.Environment;
import org.eclipse.wst.command.env.core.common.MessageUtils;
import org.eclipse.wst.command.env.core.common.SimpleStatus;
import org.eclipse.wst.command.env.core.common.Status;
import org.eclipse.wst.ws.internal.datamodel.Model;
import org.eclipse.wst.ws.parser.discovery.WebServicesParserExt;
import org.eclipse.wst.ws.parser.wsil.WebServicesParser;

public class DefaultsForServerJavaWSDLCommand extends SimpleCommand {

	private MessageUtils msgUtils_;
	private MessageUtils coreMsgUtils_;
	private MessageUtils conMsgUtils_;
	
	// rm private Model model_;
	private JavaWSDLParameter javaWSDLParam_ = null;
	private IProject serviceProject_;
	private String javaBeanName_; // this needs to be set by the extension with initial selection
	private String WSDLServiceURL_;
	private String WSDLServicePathname_;
	private WebServicesParser WSParser_;
	// rm private WebServiceElement wse_; // temporary
	
	private String LABEL = "TASK_LABEL_SERVER_JAVA_WSDL_DEFAULTS";
	private String DESCRIPTION = "TASK_DESC_SERVER_JAVA_WSDL_DEFAULTS";
	private final String WSDL_FOLDER = "wsdl"; //$NON-NLS-1$
	public final String SERVICE_EXT = "/services/"; //$NON-NLS-1$
	private final String WSDL_EXT = "wsdl"; //$NON-NLS-1$
	public final byte MODE_BEAN = (byte) 0;
	public final String SERVICE_NAME_EXT = "Service"; //$NON-NLS-1$

	public DefaultsForServerJavaWSDLCommand() {

		String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
	    conMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.ui.plugin", this );

		setName (msgUtils_.getMessage(LABEL));
		setDescription( msgUtils_.getMessage(DESCRIPTION));
	}
	

	public DefaultsForServerJavaWSDLCommand(
		JavaWSDLParameter javaWSDLParam,
		Model model) {

		String       pluginId = "org.eclipse.jst.ws.axis.creation.ui";
	    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );

		setName (msgUtils_.getMessage(LABEL));
		setDescription( msgUtils_.getMessage(DESCRIPTION));
		//rm setModel(model);
		setJavaWSDLParam(javaWSDLParam);
	
	}

	/**
	* Execute DefaultsForJavaToWSDLTask
	*/
	public Status execute( Environment env ) {

		Status status;
		if (javaWSDLParam_ == null) {
			status = new SimpleStatus( "DefaultsForServerJavaWSDLTask", coreMsgUtils_.getMessage("MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"), Status.ERROR );
			env.getStatusHandler().reportError(status);
			return status;
		}
		
		if (javaBeanName_ == null) {  // either set by extension point or bean class page
			javaBeanName_ = javaWSDLParam_.getBeanName();
			if (javaBeanName_ == null) {
				//rm javaBeanName_ = isdElement.getJavaBeanName();
				javaWSDLParam_.setBeanName(javaBeanName_);
			}
		}
		// rm WSParser_ =	(WebServicesParser) wse_.getWSParser();
		
		javaWSDLParam_.setServerSide(JavaWSDLParameter.SERVER_SIDE_BEAN);
		javaWSDLParam_.setSkeletonDeploy(false);

		javaWSDLParam_.setBeanName(javaBeanName_);
		String classpath = ClasspathUtils.getInstance().getClasspathString(serviceProject_);
		javaWSDLParam_.setClasspath(classpath);

		String simpleBeanName = javaBeanName_;
		String beanPackageName = null;
		if (javaBeanName_ != null) {
			int index = javaBeanName_.lastIndexOf('.');
			if (index != -1) {
				simpleBeanName = javaBeanName_.substring(index + 1);
				beanPackageName = javaBeanName_.substring(0, index);
			}
		}
		String namespace = WSDLUtils.makeNamespace(javaWSDLParam_.getBeanName());
		javaWSDLParam_.setNamespace(namespace);

		javaWSDLParam_.setPortTypeName(simpleBeanName);
		javaWSDLParam_.setServiceName(simpleBeanName + SERVICE_NAME_EXT);

		IPath moduleServerRoot = null;

		IPath modulePath = serviceProject_.getFullPath();
		IPath webinfPath = serviceProject_.getFullPath();
		try {
			if (serviceProject_.hasNature(IWebNatureConstants.J2EE_NATURE_ID)) {
				moduleServerRoot = ResourceUtils.getJavaSourceLocation(serviceProject_);

				J2EEWebNatureRuntime webProject =
					(J2EEWebNatureRuntime) serviceProject_.getNature(
						IWebNatureConstants.J2EE_NATURE_ID);
				modulePath = webProject.getWebModulePath();
				webinfPath = webinfPath.append(webProject.getWEBINFPath());
			}

		} catch (CoreException e) {
			status =  new SimpleStatus( "DefaultsForServerJavaWSDLTask", conMsgUtils_.getMessage("MSG_ERROR_DEFAULT_BEAN"), Status.ERROR, e );
			env.getStatusHandler().reportError(status);
			return status;
		}

		IPath wsdlPath =
			modulePath.append(WSDL_FOLDER).append(simpleBeanName).addFileExtension(WSDL_EXT);

		try{
			IFolder folder = ResourceUtils
									.getWorkspaceRoot()
									.getFolder(modulePath.append(WSDL_FOLDER));
			FileUtil.createFolder(folder, true, true);
		
		}
		catch(CoreException e){
			status = new SimpleStatus( "DefaultsForServerJavaWSDLTask", conMsgUtils_.getMessage("MSG_ERROR_WRITE_WSDL"), Status.ERROR, e );
			env.getStatusHandler().reportError(status);
			return status;
		}
		
		String wsdlLocation =
			ResourceUtils
				.getWorkspaceRoot()
				.getFile(wsdlPath)
				.getLocation()
				.toString();

		javaWSDLParam_.setOutputWsdlLocation(wsdlLocation);
		javaWSDLParam_.setInputWsdlLocation(wsdlLocation);
		WSDLServicePathname_ = wsdlPath.toString();

		if (wsdlPath != null) {
			String wsdlURL = PlatformUtils.getFileURLFromPath(new Path(wsdlLocation));
			WSDLServiceURL_ = wsdlURL;
			// set parser 
			if (wsdlURL == null || wsdlURL.length() <= 0) {
				IResource res =
					ResourceUtils.findResource(WSDLServicePathname_);
				if (res != null)
					wsdlURL = (new Utils()).toFileSystemURI(res);
			}
			Definition definition = null;
			if (wsdlURL != null && wsdlURL.length() > 0) {
				if (WSParser_ == null) {
					WSParser_ = new WebServicesParserExt();
				}
				definition = WSParser_.getWSDLDefinition(wsdlURL);
			}
		}

		javaWSDLParam_.setStyle(JavaWSDLParameter.STYLE_RPC);
		javaWSDLParam_.setUse(JavaWSDLParameter.USE_ENCODED);

		String projectURL =
			ResourceUtils.getEncodedWebProjectURL(serviceProject_);
		String serviceURL = null;
		if (projectURL == null) {
			status = new SimpleStatus( "DefaultsForServerJavaWSDLTask", msgUtils_.getMessage("MSG_ERROR_PROJECT_URL"), Status.ERROR);
			env.getStatusHandler().reportError(status);
			return status;
		}
		serviceURL = projectURL + SERVICE_EXT + simpleBeanName;

		javaWSDLParam_.setUrlLocation(serviceURL);

		javaWSDLParam_.setMetaInfOnly(true);

		//		String javaOutput = PlatformUtils.getPlatformURL(moduleServerRoot);
		//		String output = PlatformUtils.getPlatformURL(modulePath);

		String javaOutput =
			ResourceUtils
				.findResource(moduleServerRoot)
				.getLocation()
				.toString();
	
		String serviceName = javaWSDLParam_.getServiceName();
		String output =
			ResourceUtils.findResource(webinfPath).getLocation().append(serviceName).toString();

		javaWSDLParam_.setJavaOutput(javaOutput);
		javaWSDLParam_.setOutput(output);
		
		return new SimpleStatus( "" );
	}

	/**
	 * Returns the javaWSDLParam.
	 * @return JavaWSDLParameter
	 */
	public JavaWSDLParameter getJavaWSDLParam() {
		return javaWSDLParam_;
	}

	/**
	 * Sets the javaWSDLParam.
	 * @param javaWSDLParam The javaWSDLParam to set
	 */
	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam) {
		this.javaWSDLParam_ = javaWSDLParam;
	}

	// rm
	/*
	public void setModel(Model model) {
		this.model_ = model;
	}
	*/
	
	public void setServiceProject(IProject serviceProject) {
		this.serviceProject_ = serviceProject;
	}
	
	public void setJavaBeanName(String javaBeanName) {
		this.javaBeanName_ = javaBeanName;
	}
	
	public String getWSDLServiceURL() {
		return WSDLServiceURL_;
	}
	
	public String getWSDLServicePathname() {
		return WSDLServicePathname_;
	}
	
	public void setParser(WebServicesParser wsParser) {
		this.WSParser_ = wsParser;
	}
	
	public WebServicesParser getParser() {
		return WSParser_;
	}

  public void setObjectSelection(IStructuredSelection objectSelection)
  {
    if (objectSelection != null && !objectSelection.isEmpty())
    {
      Object object = objectSelection.getFirstElement();
      if (object instanceof String)
        setJavaBeanName((String)object);
    }
  }	
}
