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
 * 20070606   177421 sandakith@wso2.com - fix web.xml wiped out when Axis2 facet
 * 20070612   192047 sandakith@wso2.com - Lahiru Sandakith, 192047
 * 20070612   192047 kathy@ca.ibm.com   - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.utils;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;
import org.eclipse.jst.ws.axis2.facet.messages.Axis2FacetUIMessages;
import org.eclipse.jst.ws.internal.common.J2EEUtils;
import org.eclipse.jst.ws.internal.common.ServerUtils;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;

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
	
	public static String  pathToWebProjectContainerWEBINF(String project) {
		IPath workspaceDirectory = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		String projectString = replaceEscapecharactors(project.toString());
		IPath webContainerWEBINF = J2EEUtils.getWebInfPath(
					ResourcesPlugin.getWorkspace().getRoot().getProject(
						getProjectNameFromFramewokNameString(projectString)));
		return workspaceDirectory.append(webContainerWEBINF).toOSString();
	}
	
	public static String  pathToWebProjectContainerMETAINF(String project) {
		String containerDirectory = pathToWebProjectContainer(project);
		String webContainerMETAINF = Axis2FacetUIMessages.DIR_META_INF;
		String webContainerMETAINFString = Axis2CoreUtils.addAnotherNodeToPath(
												containerDirectory,
												webContainerMETAINF);
		return webContainerMETAINFString;
	}
	
	public static String  pathToWebProjectContainerAxis2Web(String project) {
		String containerDirectory = pathToWebProjectContainer(project);
		String webContainerAxis2Web = Axis2FacetUIMessages.DIR_AXIS2_WEB;
		String webContainerAxis2WebString = Axis2CoreUtils.addAnotherNodeToPath(
				containerDirectory,
												webContainerAxis2Web);
		return webContainerAxis2WebString;
	}
	
	public static String pathToWebProjectContainerLib(String project){
		String webContainerWEBINFString = pathToWebProjectContainerWEBINF(project);
		return  Axis2CoreUtils.addAnotherNodeToPath(
				webContainerWEBINFString,
				Axis2CoreUIMessages.DIR_LIB);
		
	}
	
	public static String pathToWebProjectContainerWebXML(String project){
		String webContainerWEBINFString = pathToWebProjectContainerWEBINF(project);
		return  Axis2CoreUtils.addAnotherNodeToPath(
				webContainerWEBINFString,
				"web.xml");
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
	
	public static String pathToAxis2CoreFacetTempDirectory(String project){
	//
		String pluginAxis2CoreTempDir = WebServiceAxis2CorePlugin
        .getInstance().getStateLocation().toOSString();
		String projectString = replaceEscapecharactors(project);
		return FileUtils.addAnotherNodeToPath(pluginAxis2CoreTempDir, projectString);
		
		
	}
	
	private static String getSplitCharactors(){
		//Windows check (because from inside wtp in return I received a hard coded path)
		if (File.separatorChar == '\\'){
			return "\\" + File.separator;
		}else{
			return File.separator;
		}
	}
	
	
	public static String getDeployedWSDLURL(IProject serverProject, 
											String ServerFactoryId, 
											String ServerInstanceId,
											String serviceName){ 
		// Note that ServerCore.findServer() might return null if the server cannot be found and
		// ServerUtils.getEncodedWebComponentURL() can handle null server properly (by using ServerFactoryId)
		String deployedWSDLURLpath = null;
		IServer server = null;
		if (ServerInstanceId != null) {
			server = ServerCore.findServer(ServerInstanceId);
		}
		deployedWSDLURLpath = ServerUtils.getEncodedWebComponentURL(serverProject, 
				ServerFactoryId, server);
		if (deployedWSDLURLpath == null) {
			deployedWSDLURLpath = Axis2CoreUIMessages.LOCAL_SERVER_PORT;
		}
		String[] deployedWSDLURLParts = {Axis2CoreUIMessages.SERVICES,serviceName};
		return FileUtils.addNodesToURL(deployedWSDLURLpath, deployedWSDLURLParts)+"?wsdl";
	}
	
	
}
