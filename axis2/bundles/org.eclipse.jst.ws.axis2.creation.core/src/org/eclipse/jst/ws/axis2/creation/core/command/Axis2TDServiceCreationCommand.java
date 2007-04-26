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
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IWebService;

public class Axis2TDServiceCreationCommand extends
AbstractDataModelOperation {
	
  	private DataModel model;

  public Axis2TDServiceCreationCommand( DataModel model,IWebService ws, String project )
  {
    this.model = model;  
  }

public IStatus execute(IProgressMonitor monitor, IAdaptable info)
		throws ExecutionException {
	IStatus status = Status.OK_STATUS;  
	IEnvironment environment = getEnvironment();
	//The full Qulalified Service Class
//	String serviceDefinition = ws.getWebServiceInfo().getWsdlURL(); 
	try {
		
//		String workspaceDirectory = ResourceUtils.getWorkspaceRoot().getLocation().toOSString();
		String workspaceDirectory = ResourcesPlugin.getWorkspace()
														.getRoot().getLocation().toOSString();
		String currentDynamicWebProjectDir = FileUtils.addAnotherNodeToPath(workspaceDirectory, 
														model.getWebProjectName());
		String matadataDir = FileUtils.addAnotherNodeToPath(workspaceDirectory,
														Axis2CreationUIMessages.DIR_DOT_METADATA);
	    String matadataPluginsDir = FileUtils.addAnotherNodeToPath(matadataDir,
	    												Axis2CreationUIMessages.DIR_DOT_PLUGINS);
	    String matadataAxis2Dir = FileUtils.addAnotherNodeToPath(matadataPluginsDir, 
	    												Axis2CreationUIMessages.AXIS2_PROJECT);
	    String tempServicesDir = FileUtils.addAnotherNodeToPath(matadataAxis2Dir,
	    													   Axis2CreationUIMessages.DIR_SERVICES);
	    
	    model.setPathToWebServicesTempDir(tempServicesDir);
		
	    //Exploded temperory services directory
		String currentservicesDirectory = FileUtils.addAnotherNodeToPath(tempServicesDir, 
														model.getServiceName());
		String metaInfDirectory = FileUtils.addAnotherNodeToPath(currentservicesDirectory, 
														Axis2CreationUIMessages.DIR_META_INF);
		
		//Create the directories
		//Create the Webservices stuff on the workspace .matadata directory  
	    FileUtils.createDirectorys(currentservicesDirectory);
	    FileUtils.createDirectorys(metaInfDirectory);		    
	    
	    
	    //copy the generated resources (services.xml .wsdl ) files
	    //at resources/service.xml
	    String currentProjectResourcesDirString = FileUtils
	    		.addAnotherNodeToPath(currentDynamicWebProjectDir, 
	    							  Axis2CreationUIMessages.DIR_RESOURCES);
	    File currentProjectResourcesDir = new File(currentProjectResourcesDirString);
	    FileUtils.copyDirectory(currentProjectResourcesDir, new File(metaInfDirectory));
        
        // Copy the classes directory to the sevices directory
		String defaultClassesSubDirectory = Axis2CreationUIMessages.DIR_BUILD + File.separator + 
											Axis2CreationUIMessages.DIR_CLASSES;
		//TODO copy only the relevent .classes to the aar
		String classesDirectory = currentDynamicWebProjectDir + File.separator + 
											defaultClassesSubDirectory;
		
		FileUtils.copyDirectory(new File(classesDirectory), new File(currentservicesDirectory));
		
//		//Create the .aar file 
//		String aarDirString =  FileUtils.addAnotherNodeToPath(webservicesDir, 
// 		Axis2CreationUIMessages.DIR_AAR);
//		File aarDir = new File(aarDirString);
//		FileUtils.createDirectorys(aarDirString);
//		AARFileWriter aarFileWriter = new AARFileWriter();
//		File serviseDir = new File(servicesDirectory);
//		aarFileWriter.writeAARFile(aarDir, serviceName + 
//		Axis2CreationUIMessages.FILE_AAR, serviseDir);
		
		
		//Import all the stuff form the .matadata directory to inside the current web project
		} catch (IOException e) {
			status = StatusUtils.errorStatus(
					NLS.bind(Axis2CreationUIMessages.ERROR_INVALID_FILE_READ_WRITEL,
							new String[]{e.getLocalizedMessage()}), e);
			environment.getStatusHandler().reportError(status); 
		} catch (Exception e) {
			status = StatusUtils.errorStatus(
					NLS.bind(Axis2CreationUIMessages.ERROR_INVALID_SERVICE_CREATION,
							new String[]{e.getLocalizedMessage()}), e);
			environment.getStatusHandler().reportError(status); 
		}
	    
	
    
    return status;
}
}
