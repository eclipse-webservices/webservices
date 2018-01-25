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
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070625   192522 sandakith@wso2.com - Lahiru Sandakith, fix the build path problem
 * 20070813   196173  sandakith@wso2.com - Lahiru Sandakith, Fix 196173, DWP custom location fix
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
import org.eclipse.jst.ws.axis2.creation.core.utils.CommonUtils;
import org.eclipse.jst.ws.axis2.creation.core.utils.ServiceXMLCreator;
import org.eclipse.jst.ws.internal.common.ResourceUtils;
import org.eclipse.osgi.util.NLS;
import org.eclipse.wst.command.internal.env.core.common.StatusUtils;
import org.eclipse.wst.common.environment.IEnvironment;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;
import org.eclipse.wst.ws.internal.wsrt.IWebService;

public class Axis2BUServiceCreationCommand extends
		AbstractDataModelOperation {
	
	  	private DataModel model;
		private IWebService ws;
		private String project;

	  public Axis2BUServiceCreationCommand( DataModel model,IWebService ws, String project )
	  {
	    this.model = model;  
	    this.ws=ws;
	    this.project= project;
	  }

	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		IStatus status = Status.OK_STATUS;  
		IEnvironment environment = getEnvironment();
		//The full Qulalified Service Class
		String serviceClass = ws.getWebServiceInfo().getImplURL(); 
		try {
			
			String workspaceDirectory = ResourcesPlugin.getWorkspace()
														.getRoot().getLocation().toOSString();
			String matadataDir = FileUtils.addAnotherNodeToPath(workspaceDirectory,
					Axis2Constants.DIR_DOT_METADATA);
		    String matadataPluginsDir = FileUtils.addAnotherNodeToPath(matadataDir,
		    		Axis2Constants.DIR_DOT_PLUGINS);
		    String matadataAxis2Dir = FileUtils.addAnotherNodeToPath(matadataPluginsDir, 
		    		Axis2Constants.AXIS2_PROJECT);
		    String webservicesDir = FileUtils.addAnotherNodeToPath(matadataAxis2Dir,
		    		Axis2Constants.DIR_WEBSERVICES);
		    model.setPathToWebServicesTempDir(webservicesDir);
		    
			//Get the Service name from the class name
		    String serviceName = CommonUtils.classNameFromQualifiedName(serviceClass); 
		    //String servicePackage = CommonUtils.packageNameFromQualifiedName(serviceClass); 
			
			String servicesDirectory = FileUtils.addAnotherNodeToPath(webservicesDir, serviceName);
			String serviceXMLDirectory = FileUtils.addAnotherNodeToPath(servicesDirectory, 
					Axis2Constants.DIR_META_INF);
			
			//Create the directories
			//Create the Webservices stuff on the workspace .matadata directory  
		    FileUtils.createDirectorys(servicesDirectory);
		    FileUtils.createDirectorys(serviceXMLDirectory);		    
		    
		    //create the services.xml file
		    File serviceXMLFile;
            if (model.isGenerateServicesXML()){
			    ServiceXMLCreator serviceXMLCreator = new ServiceXMLCreator(serviceName, 
			    															serviceClass, 	
			    															null);
			    serviceXMLFile = new File(serviceXMLDirectory + File.separator + 
			    		Axis2Constants.FILE_SERVICES_XML);
			    FileWriter serviceXMLFileWriter;
	
				serviceXMLFileWriter = new FileWriter(serviceXMLFile, false);
	            BufferedWriter writer = new BufferedWriter(serviceXMLFileWriter) ;
	            writer.write(serviceXMLCreator.toString()) ;
	            writer.close() ;
            }else {
            	String pathToServicesXML = model.getPathToServicesXML();
            	if (pathToServicesXML == null){
    				status = StatusUtils
    					.errorStatus(Axis2CreationUIMessages.ERROR_INVALID_SERVICES_XML);
    				environment.getStatusHandler().reportError(status); 
            	}else{
            		serviceXMLFile = new File(pathToServicesXML);
            		File targetServicesXMLFile = new File(serviceXMLDirectory + File.separator + 
            				Axis2Constants.FILE_SERVICES_XML);
            		FileUtils.copy(serviceXMLFile, targetServicesXMLFile);
            	}
            	
            }
	        
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
									new File(servicesDirectory));
			
//			//Create the .aar file 
//			String aarDirString =  FileUtils.addAnotherNodeToPath(webservicesDir, 
//			Axis2CreationUIMessages.DIR_AAR);
//			File aarDir = new File(aarDirString);
//			FileUtils.createDirectorys(aarDirString);
//			AARFileWriter aarFileWriter = new AARFileWriter();
//			File serviseDir = new File(servicesDirectory);
//			aarFileWriter.writeAARFile(aarDir, serviceName + 
//			Axis2CreationUIMessages.FILE_AAR, serviseDir);
			
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
