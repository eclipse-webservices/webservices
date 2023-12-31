/*******************************************************************************
 * Copyright (c) 2007, 2010 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * WSO2 Inc. - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070510   172926 sandakith@wso2.com - Lahiru Sandakith, Fix 172926 Use Util Classes
 * 20070813   196173  sandakith@wso2.com - Lahiru Sandakith, Fix 196173, DWP custom location fix
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 * 20080616   237363 samindaw@wso2.com - Saminda Wijeratne, get ResourceContext from environment instead of preference
 * 20100308	  282466 samindaw@wso2.com - Saminda Wijeratne, support for axis2 1.5
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.consumption.core.utils.ContentCopyUtils;
import org.eclipse.jst.ws.axis2.core.utils.FacetContainerUtils;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
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



		ContentCopyUtils contentCopyUtils = new ContentCopyUtils(getEnvironment());
		
		//Check for the server status
		if (model.getServerStatus()){
			if (scenario == WebServiceScenario.BOTTOMUP || scenario == WebServiceScenario.TOPDOWN){
				String webContainerDirString = FacetContainerUtils.pathToWebProjectContainer(
						model.getWebProjectName());
				String repositoryString = FacetContainerUtils.getAxis2WebContainerRepositoryPath(
						webContainerDirString); 
				String webserviceTempDirString = model.getPathToWebServicesTempDir();
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
