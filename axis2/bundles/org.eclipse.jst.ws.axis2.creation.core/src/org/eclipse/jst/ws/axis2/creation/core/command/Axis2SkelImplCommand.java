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
 * 20070123   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 * 20080924   247929 samindaw@wso2.com - Saminda Wijeratne, source folder not correctly set
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;

import java.net.MalformedURLException;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.jst.ws.axis2.creation.core.utils.CommonUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.WebServiceInfo;

public class Axis2SkelImplCommand extends AbstractDataModelOperation {

	private WebServiceInfo webServiceInfo;
	private DataModel model;

	public Axis2SkelImplCommand(WebServiceInfo webServiceInfo, DataModel model){
		this.webServiceInfo = webServiceInfo;
		this.model=model;
	}

	public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		
		IStatus status = Status.OK_STATUS;
		IEnvironment environment = getEnvironment();

		String workspaceDirectory = ResourcesPlugin.getWorkspace().getRoot().
													getLocation().toOSString();
		String currentDynamicWebProjectDir = FileUtils.addAnotherNodeToPath(
													   workspaceDirectory, 
													   model.getWebProjectName());
		String projectSrcDir =  FileUtils.addAnotherNodeToPath(
											currentDynamicWebProjectDir, 
											Axis2CoreUtils.getSourceFolder(model.getWebProjectName()));

		String packagePath = CommonUtils.packgeName2PathName(model.getPackageText());
		String packageDir = FileUtils.addAnotherNodeToPath(projectSrcDir, packagePath);
		String serviceSkeletonImpl = FileUtils.addAnotherNodeToPath(packageDir, model.
																	getServiceName());

		String serviceSkeletonImplURL =  serviceSkeletonImpl + Axis2Constants.SKELETON_SUFFIX;
		IPath serviceSkeletonImplURLPath = new Path(serviceSkeletonImplURL); 
		try {
			//webServiceInfo.setImplURL(serviceSkeletonImplURLPath.toFile().toURL().toString());
			//String[] implURLArray = {serviceSkeletonImplURL};
			//webServiceInfo.setImplURLs( implURLArray);
			
			String serviceSkeletonImplURLString = 
				serviceSkeletonImplURLPath.toFile().toURL().toString();

			webServiceInfo.setImplURL(serviceSkeletonImplURLString);
			String[] implURLArray = {serviceSkeletonImplURLString};
			webServiceInfo.setImplURLs( implURLArray);

			
		} catch (MalformedURLException e) {
			status = StatusUtils.errorStatus(
					NLS.bind(Axis2CreationUIMessages.ERROR_INVALID_FILE_READ_WRITEL,
							new String[]{e.getLocalizedMessage()}), e);
			environment.getStatusHandler().reportError(status); 
		}

		return status;
		
	}
	
}
