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
 * 20070213  168766 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  facet to the framework for 168766
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070501   180284 sandakith@wso2.com - Lahiru Sandakith
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.facet.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.core.plugin.data.ServerModel;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.axis2.core.utils.FacetContainerUtils;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;
import org.eclipse.jst.ws.axis2.core.utils.RuntimePropertyUtils;
import org.eclipse.jst.ws.axis2.facet.utils.Axis2RuntimeUtils;
import org.eclipse.jst.ws.axis2.facet.utils.Axis2WebappUtils;
import org.eclipse.jst.ws.axis2.facet.utils.ContentCopyUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class Axis2WebservicesServerCommand extends
AbstractDataModelOperation {

	String project;
	public Axis2WebservicesServerCommand(String project){
		this.project = project;
	}

	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
	throws ExecutionException {
		return Status.OK_STATUS;
	}
	
	public IStatus exexuteOverrride(IProgressMonitor monitor)  {
		//Copy the axis2 libs in to this client project
		IStatus status =null;
		String runtimeLocation = null;
		ContentCopyUtils contentCopyUtils = new ContentCopyUtils();
		try {
			if(ServerModel.isAxis2ServerPathRepresentsWar() 
					|| RuntimePropertyUtils.getWarStatusFromPropertiesFile()){
				runtimeLocation = Axis2RuntimeUtils.copyAxis2War(
														monitor,
														Axis2CoreUIMessages.PROPERTY_KEY_PATH);
			}else{

				runtimeLocation = Axis2WebappUtils.copyAxis2War(
														monitor,
														Axis2CoreUIMessages.PROPERTY_KEY_PATH);
			}
		} catch (FileNotFoundException e) {
			return handleExceptionStatus(e);
		} catch (IOException e) {
			return handleExceptionStatus(e);
		} catch (Exception e) {
			return handleExceptionStatus(e);
		}
		status = contentCopyUtils.copyDirectoryRecursivelyIntoWorkspace(
				runtimeLocation, 
				FacetContainerUtils.pathToWebProjectContainer(project), 
				monitor 
		);
		status = Status.OK_STATUS;
		//clean up tempory files
		File tempFacetDirectory = new File(runtimeLocation);
		if (tempFacetDirectory.exists() && ServerModel.isAxis2ServerPathRepresentsWar()) {
			FileUtils.deleteDir(tempFacetDirectory);
		}
		return status;
	}
	
	private void cleanupIfFacetStatusFailed(String runtimeLocation){
		File tempFacetDirectory = new File(runtimeLocation);
		if (tempFacetDirectory.exists()) {
			FileUtils.deleteDir(tempFacetDirectory);
		}
	}
	
	private IStatus handleExceptionStatus(Exception e){
		IStatus status = null;
		status = new Status(1,project,1,Axis2CoreUIMessages.ERROR_SERVER_IS_NOT_SET,e);
		cleanupIfFacetStatusFailed(Axis2CoreUtils.tempAxis2Directory());
		return status;
	}
}
