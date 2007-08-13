/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070510   172926 sandakith@wso2.com - Lahiru Sandakith, Fix 172926 Use Util Classes 
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;

import java.io.File;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.utils.ContentCopyUtils;
import org.eclipse.jst.ws.axis2.core.utils.FacetContainerUtils;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.environment.IStatusHandler;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.WebServiceScenario;

public class Axis2WebservicesServerCommand extends AbstractDataModelOperation {

	private DataModel model;
	private int scenario;

	public Axis2WebservicesServerCommand( DataModel model, int scenario ){
		this.model = model;  
		this.scenario = scenario;
	}

	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
	throws ExecutionException {

		IStatus status = Status.OK_STATUS;
		IEnvironment environment = getEnvironment();
		IStatusHandler statusHandler = environment.getStatusHandler();	

//		 String currentDynamicWebProjectDir = J2EEUtils.getWebContentPath(
//				 				ResourcesPlugin.getWorkspace().getRoot().getProject(
//				 				model.getWebProjectName())
//						).toOSString();
//
//		String webContainerDirString = FileUtils.addAnotherNodeToPath(
//				ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString(),
//				currentDynamicWebProjectDir);
		
		String webContainerDirString = FacetContainerUtils.pathToWebProjectContainer(
				model.getWebProjectName());

		ContentCopyUtils contentCopyUtils = new ContentCopyUtils();
		
		//Check for the server status
		if (model.getServerStatus()){
			if (scenario == WebServiceScenario.BOTTOMUP){
				//Import the tempory webservices directory according to the Resources API of eclipse 
				String webserviceTempDirString = model.getPathToWebServicesTempDir();
				String repositoryString = webContainerDirString 
				+ File.separator + Axis2CreationUIMessages.DIR_WEB_INF 
				+ File.separator + Axis2CreationUIMessages.DIR_SERVICES;
				//Copy the existing services to the repository
				status = contentCopyUtils.copyDirectoryRecursivelyIntoWorkspace(
											webserviceTempDirString, 
											repositoryString, 
											monitor, 
											statusHandler);
				FileUtils.deleteDirectories(webserviceTempDirString);


			}else if (scenario == WebServiceScenario.TOPDOWN){
				//Do topdown
				String webserviceTempDirString = model.getPathToWebServicesTempDir();
				String repositoryString = webContainerDirString 
				+ File.separator + Axis2CreationUIMessages.DIR_WEB_INF 
				+ File.separator + Axis2CreationUIMessages.DIR_SERVICES;
				//Copy the existing services to the repository
				status = contentCopyUtils.copyDirectoryRecursivelyIntoWorkspace(
											webserviceTempDirString, 
											repositoryString, 
											monitor, 
											statusHandler);
				FileUtils.deleteDirectories(webserviceTempDirString);
			}
			status = Status.OK_STATUS;
		}else{
			status = Status.CANCEL_STATUS;
		}

		return status;
	}

}
