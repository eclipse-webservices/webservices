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
 * 20070222  168766 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  facet to the framework for 168766
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070510   172926 sandakith@wso2.com - Lahiru Sandakith, Fix 172926 Use Util Classes
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.utils;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.internal.common.J2EEUtils;

public class FacetContainerUtils {

	public static String  pathToWebProjectContainer(String project) {

		String workspaceDirectory = ResourcesPlugin.getWorkspace().getRoot()
												   .getLocation().toOSString();
		String projectString = replaceEscapecharactors(project.toString());

		String currentDynamicWebProjectDir = J2EEUtils.getWebContentPath(
				ResourcesPlugin.getWorkspace().getRoot().getProject(
						getProjectNameFromFramewokNameString(projectString)
						)).toOSString();

		String webContainerDirString = Axis2CoreUtils.addAnotherNodeToPath(
				workspaceDirectory,
				currentDynamicWebProjectDir);
		
		return webContainerDirString;
	}
	
	
	public static String pathToWebProjectContainerLib(String project){

		String workspaceDirectory = ResourcesPlugin.getWorkspace().getRoot()
		   								.getLocation().toOSString();
		
		String webContainerWEBINF = J2EEUtils.getWebInfPath(
						ResourcesPlugin.getWorkspace().getRoot().getProject(
						getProjectNameFromFramewokNameString(project))).toOSString();
		
		String webContainerWEBINFString = Axis2CoreUtils.addAnotherNodeToPath(
															workspaceDirectory,
															webContainerWEBINF);
		return  Axis2CoreUtils.addAnotherNodeToPath(
				webContainerWEBINFString,
				Axis2CoreUIMessages.DIR_LIB);
		
	}
	
	
	
	//Fix for the windows build not working
	private static String replaceEscapecharactors(String vulnarableString){
		if (vulnarableString.indexOf("/")!=-1){
			vulnarableString = vulnarableString.replace('/', File.separator.charAt(0));
		}
		return vulnarableString;
	}
	
	
	private static String getProjectNameFromFramewokNameString(String frameworkProjectString){
		if (frameworkProjectString.indexOf(getSplitCharactor())== -1){
			return frameworkProjectString;
		}else{
			return frameworkProjectString.split(getSplitCharactors())[1];
		}
		
	}
	
	
	private static String getSplitCharactor(){
		//Windows check (because from inside wtp in return I received a hard coded path)
		if (File.separatorChar == '\\'){
			return "\\" ;
		}else{
			return File.separator;
		}
	}
	
	private static String getSplitCharactors(){
		//Windows check (because from inside wtp in return I received a hard coded path)
		if (File.separatorChar == '\\'){
			return "\\" + File.separator;
		}else{
			return File.separator;
		}
	}
	
	
	public static String getDeployedWSDLURL(String projectName, String serviceName){
		//TODO get the hostname and port from eclispe runtime  
		String[] deployedWSDLURLParts = {projectName.split("Client")[0],
										 Axis2CoreUIMessages.SERVICES,
										 serviceName
										 };
		return FileUtils.addNodesToURL(Axis2CoreUIMessages.LOCAL_SERVER_PORT, 
										deployedWSDLURLParts)+"?wsdl";
	}
}
