/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060216   115144 pmoogk@ca.ibm.com - Peter Moogk
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.ui.command;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.internal.axis.consumption.core.AxisConsumptionCoreMessages;
import org.eclipse.jst.ws.internal.axis.consumption.core.common.JavaWSDLParameter;
import org.eclipse.jst.ws.internal.axis.consumption.ui.AxisConsumptionUIMessages;
import org.eclipse.jst.ws.internal.axis.consumption.ui.util.PlatformUtils;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class DefaultsForClientJavaWSDLCommand extends AbstractDataModelOperation
{
	private JavaWSDLParameter	javaWSDLParam_;
	private IProject					proxyProject_;
	private String						WSDLServiceURL_;
	private String						WSDLServicePathname_;
	private String						outputFolder_;

	public DefaultsForClientJavaWSDLCommand()
	{
	}

	public IStatus execute(IProgressMonitor monitor, IAdaptable adaptable)
	{
		IEnvironment environment = getEnvironment();
		IStatus status;
		
		if( outputFolder_ == null )
		{
			IPath[] paths = ResourceUtils.getAllJavaSourceLocations(proxyProject_);
			outputFolder_ = paths[0].toString();
		}
		
		if (javaWSDLParam_ == null)
		{
			status = StatusUtils
					.errorStatus(AxisConsumptionCoreMessages.MSG_ERROR_JAVA_WSDL_PARAM_NOT_SET);
			environment.getStatusHandler().reportError(status);
			return status;
		}
		javaWSDLParam_.setMetaInfOnly(false);
		javaWSDLParam_.setServerSide(JavaWSDLParameter.SERVER_SIDE_NONE);
		ModuleCoreNature mn = ModuleCoreNature.getModuleCoreNature(proxyProject_);
		if (mn != null)
		{
			IPath webModuleServerRoot = ResourceUtils.getJavaSourceLocation(proxyProject_);
			// String output = PlatformUtils.getPlatformURL(webModuleServerRoot);
			String output = ResourceUtils.findResource(webModuleServerRoot).getLocation().toString();
			// String output =
			// ResourceUtils.getWorkspaceRoot().getFolder(webModuleServerRoot).getLocation().toString();
			IPath javaOutput = ResourceUtils.findResource( new Path( outputFolder_ )).getLocation();
			javaWSDLParam_.setJavaOutput(javaOutput.toString());
			
			IFolder webModuleContainer = ResourceUtils.getWebComponentServerRoot(proxyProject_);
			if (webModuleContainer != null)
			{
				IPath webModulePath = webModuleContainer.getFullPath();
				// output = PlatformUtils.getPlatformURL(webModulePath);
				IResource res = ResourceUtils.findResource(webModulePath);
				if (res != null)
				{
					output = res.getLocation().toString();
				}
				javaWSDLParam_.setOutput(output);
			}
		}
		else
		{
			// Check if it's a plain old Java project
			IJavaProject javaProject = null;
			javaProject = JavaCore.create(proxyProject_);
			if (javaProject != null)
			{
				IPath output = ResourceUtils.findResource( new Path( outputFolder_ )).getLocation();
				javaWSDLParam_.setJavaOutput(output.toString());
				javaWSDLParam_.setOutput(output.toString());
			}
			else
			{
				// Not familiar with this kind of project
				status = StatusUtils.errorStatus(AxisConsumptionUIMessages.MSG_WARN_NO_JAVA_NATURE);
				environment.getStatusHandler().reportError(status);
				return status;
			}
		}
		if (WSDLServicePathname_ == null)
		{
			if (WSDLServiceURL_ == null)
			{
				status = StatusUtils.errorStatus(AxisConsumptionUIMessages.MSG_ERROR_WSDL_LOCATION_NOT_SET);
				environment.getStatusHandler().reportError(status);
				return status;
			}
		}
		else
		{
			WSDLServiceURL_ = PlatformUtils.getFileFromPlatform(WSDLServicePathname_);
		}
		javaWSDLParam_.setInputWsdlLocation(WSDLServiceURL_);
		return Status.OK_STATUS;
	}

	/**
	 * Returns the javaWSDLParam.
	 * 
	 * @return JavaWSDLParameter
	 */
	public JavaWSDLParameter getJavaWSDLParam()
	{
		return javaWSDLParam_;
	}

	/**
	 * Sets the javaWSDLParam.
	 * 
	 * @param javaWSDLParam
	 *          The javaWSDLParam to set
	 */
	public void setJavaWSDLParam(JavaWSDLParameter javaWSDLParam)
	{
		this.javaWSDLParam_ = javaWSDLParam;
	}

	/**
	 * @param proxyProject_
	 *          The proxyProject_ to set.
	 */
	public void setProxyProject(IProject proxyProject)
	{
		this.proxyProject_ = proxyProject;
	}

	/**
	 * @param serviceURL_
	 *          The wSDLServiceURL_ to set.
	 */
	public void setWSDLServiceURL(String serviceURL)
	{
		WSDLServiceURL_ = serviceURL;
	}

	/**
	 * @param servicePathname_
	 *          The wSDLServicePathname_ to set.
	 */
	public void setWSDLServicePathname(String servicePathname)
	{
		WSDLServicePathname_ = servicePathname;
	}

	public String getOutputFolder()
	{
	  return outputFolder_;	
	}
	
	public void setOutputFolder(String outputFolder)
	{
		outputFolder_ = outputFolder;
	}
}
