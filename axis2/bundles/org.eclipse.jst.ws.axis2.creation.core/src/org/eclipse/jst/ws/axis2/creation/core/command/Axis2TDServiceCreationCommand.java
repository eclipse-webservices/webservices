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
 * 20070507   184740 sandakith@wso2.com - Lahiru Sandakith
 * 20070625   192522 sandakith@wso2.com - Lahiru Sandakith, fix the build path problem
 * 20070813   196173  sandakith@wso2.com - Lahiru Sandakith, Fix 196173, DWP custom location fix
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.utils.FacetContainerUtils;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IWebService;

public class Axis2TDServiceCreationCommand extends
AbstractDataModelOperation {
	
  	private DataModel model;
  	private String project;

  public Axis2TDServiceCreationCommand( DataModel model,IWebService ws, String project ) {
    this.model = model;  
    this.project = project;
  }

public IStatus execute(IProgressMonitor monitor, IAdaptable info)
		throws ExecutionException {
	IStatus status = Status.OK_STATUS;  
	IEnvironment environment = getEnvironment();

	try {
		
		String workspaceDirectory = ResourcesPlugin.getWorkspace()
														.getRoot().getLocation().toOSString();
		String currentDynamicWebProjectDir = FacetContainerUtils.getProjectRoot( 
														model.getWebProjectName()).toOSString();
		String matadataDir = FileUtils.addAnotherNodeToPath(workspaceDirectory,
				Axis2Constants.DIR_DOT_METADATA);
	    String matadataPluginsDir = FileUtils.addAnotherNodeToPath(matadataDir,
	    		Axis2Constants.DIR_DOT_PLUGINS);
	    String matadataAxis2Dir = FileUtils.addAnotherNodeToPath(matadataPluginsDir, 
	    		Axis2Constants.AXIS2_PROJECT);
	    String tempServicesDir = FileUtils.addAnotherNodeToPath(matadataAxis2Dir,
	    		Axis2Constants.DIR_SERVICES);
	    
	    model.setPathToWebServicesTempDir(tempServicesDir);
		
	    //Exploded temperory services directory
		String currentservicesDirectory = FileUtils.addAnotherNodeToPath(tempServicesDir, 
														model.getServiceName());
		String metaInfDirectory = FileUtils.addAnotherNodeToPath(currentservicesDirectory, 
				Axis2Constants.DIR_META_INF);
		
		//Create the directories
		//Create the Webservices stuff on the workspace .matadata directory  
	    FileUtils.createDirectorys(currentservicesDirectory);
	    FileUtils.createDirectorys(metaInfDirectory);		    
	    
	    
	    //copy the generated resources (services.xml .wsdl ) files
	    //at resources/service.xml
	    String currentProjectResourcesDirString = FileUtils
	    		.addAnotherNodeToPath(currentDynamicWebProjectDir, 
	    				Axis2Constants.DIR_RESOURCES);
	    File currentProjectResourcesDir = new File(currentProjectResourcesDirString);
	    if(!currentProjectResourcesDir.exists()){
	    	currentProjectResourcesDir.mkdir();
	    }
	    FileUtils.copyDirectory(currentProjectResourcesDir, new File(metaInfDirectory));
        
        //Copy the classes directory to the sevices directory
        String classesDirectory = null;
		IPath projectRoot = FacetContainerUtils.getProjectRoot(project);
		IPath defaultClassesSubDirectory = ResourceUtils.getJavaOutputLocation(
        		ResourcesPlugin.getWorkspace().getRoot().getProject(project));
		IPath defaultClassesSubDirectoryWithoutProjectRoot = ResourceUtils.getJavaOutputLocation(
        		ResourcesPlugin.getWorkspace().getRoot().getProject(project))
        		.removeFirstSegments(1).makeAbsolute();
		if(projectRoot.toOSString().contains(FacetContainerUtils.getWorkspace().toOSString())){
			classesDirectory = FacetContainerUtils.getWorkspace()
						.append(defaultClassesSubDirectory).toOSString();
		}else{
			classesDirectory = projectRoot
						.append(defaultClassesSubDirectoryWithoutProjectRoot).toOSString();
		}
		
        //TODO copy only the relevent .classes to the aar
		FileUtils.copyDirectory(new File(classesDirectory),
								new File(currentservicesDirectory));
		
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
