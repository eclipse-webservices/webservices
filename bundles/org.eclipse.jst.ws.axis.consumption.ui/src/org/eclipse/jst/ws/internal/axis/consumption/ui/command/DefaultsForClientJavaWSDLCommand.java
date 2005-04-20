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
package org.eclipse.jst.ws.internal.axis.consumption.ui.command;


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.PlatformUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.internal.provisional.env.core.SimpleCommand;
import org.eclipse.wst.command.internal.provisional.env.core.common.Environment;
import org.eclipse.wst.command.internal.provisional.env.core.common.MessageUtils;
import org.eclipse.wst.command.internal.provisional.env.core.common.SimpleStatus;
import org.eclipse.wst.command.internal.provisional.env.core.common.Status;
import org.eclipse.wst.ws.internal.datamodel.Model;


public class DefaultsForClientJavaWSDLCommand extends SimpleCommand {

	private JavaWSDLParameter javaWSDLParam_;
	private IProject proxyProject_;
	private String WSDLServiceURL_;
	private String WSDLServicePathname_;
	private MessageUtils msgUtils_;
	private MessageUtils coreMsgUtils_;
	private String moduleName_;
	
	private String LABEL = "TASK_LABEL_CLIENT_JAVA_WSDL_DEFAULTS";
	private String DESCRIPTION = "TASK_DESC_CLIENT_JAVA_WSDL_DEFAULTS";

	public DefaultsForClientJavaWSDLCommand( String moduleName ) {
		String       pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
	    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
	    setName (msgUtils_.getMessage(LABEL));
		setDescription( msgUtils_.getMessage(DESCRIPTION));		
		
		moduleName_ = moduleName;
	}
	
	/**
	 * Constructor for DefaultsForClientJavaWSDLCommand.
	 */
	public DefaultsForClientJavaWSDLCommand(
		JavaWSDLParameter javaWSDLParam,
		Model model) {
		//super();
		String       pluginId = "org.eclipse.jst.ws.axis.consumption.ui";
	    msgUtils_ = new MessageUtils( pluginId + ".plugin", this );
	    coreMsgUtils_ = new MessageUtils( "org.eclipse.jst.ws.axis.consumption.core.consumption", this );
	    setName (msgUtils_.getMessage(LABEL));
		setDescription( msgUtils_.getMessage(DESCRIPTION));
		this.javaWSDLParam_ = javaWSDLParam;
	}

	public Status execute(Environment environment) {
		Status status;
		if (javaWSDLParam_ == null) {
			status = new SimpleStatus("DefaultsForClientJavaWSDLCommand", //$NON-NLS-1$
					coreMsgUtils_.getMessage(
				"MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET"),
				Status.ERROR);
			environment.getStatusHandler().reportError(status);
			return status;
		}

		javaWSDLParam_.setMetaInfOnly(false);
		javaWSDLParam_.setServerSide(JavaWSDLParameter.SERVER_SIDE_NONE);
		
		IPath webModuleServerRoot = ResourceUtils.getJavaSourceLocation(proxyProject_, moduleName_ );
		//String output = PlatformUtils.getPlatformURL(webModuleServerRoot);
		String output =	ResourceUtils.findResource(webModuleServerRoot).getLocation().toString();
//		String output = ResourceUtils.getWorkspaceRoot().getFolder(webModuleServerRoot).getLocation().toString();
		javaWSDLParam_.setJavaOutput(output);


		IContainer webModuleContainer = ResourceUtils.getWebComponentServerRoot(proxyProject_, moduleName_);
		if (webModuleContainer !=null)
		{
		  IPath webModulePath = webModuleContainer.getFullPath();
		  //output =  PlatformUtils.getPlatformURL(webModulePath);
		  output = ResourceUtils.findResource(webModulePath).getLocation().toString();
		  javaWSDLParam_.setOutput(output);
		}


		if (WSDLServicePathname_ == null) {
			
			if (WSDLServiceURL_ == null) {
				status = new SimpleStatus("DefaultsForClientJavaWSDLCommand", //$NON-NLS-1$
						msgUtils_.getMessage(
					"MSG_ERROR_WSDL_LOCATION_NOT_SET"),
					Status.ERROR);
				environment.getStatusHandler().reportError(status);
				return status;
			}
		} else {
			WSDLServiceURL_ = PlatformUtils.getFileFromPlatform(WSDLServicePathname_);
		}

		javaWSDLParam_.setInputWsdlLocation(WSDLServiceURL_);

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

	/**
	 * @param proxyProject_ The proxyProject_ to set.
	 */
	public void setProxyProject(IProject proxyProject) {
		this.proxyProject_ = proxyProject;
	}
	/**
	 * @param serviceURL_ The wSDLServiceURL_ to set.
	 */
	public void setWSDLServiceURL(String serviceURL) {
		WSDLServiceURL_ = serviceURL;
	}
	/**
	 * @param servicePathname_ The wSDLServicePathname_ to set.
	 */
	public void setWSDLServicePathname(String servicePathname) {
		WSDLServicePathname_ = servicePathname;
	}
}
