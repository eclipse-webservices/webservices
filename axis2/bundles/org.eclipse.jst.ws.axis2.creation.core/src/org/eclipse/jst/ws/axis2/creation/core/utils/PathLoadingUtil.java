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
 * 20070330   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070507   184740 sandakith@wso2.com - Lahiru Sandakith
 * 20070507   185686 sandakith@wso2.com - Lahiru Sandakith
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.utils;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;
import org.eclipse.jst.ws.axis2.creation.core.data.DataModel;
import org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUIMessages;

public class PathLoadingUtil {
	
	//Model
	private static  DataModel model = null;

	//Paths
	private static String tempCodegenOutputLocation = null;
	private static String currentDynamicWebProjectDir = null;
	private static String workspaceDirectory = null;
	private static String currentProjectWebProjectName = null;
	
	//Already Computed
	private static boolean alreadyInit = false;
	private static boolean alreadyComputeTempCodegenOutputLocation = false;
	private static boolean alreadyComputeCurrentDynamicWebProjectDir = false;
	private static boolean alreadyComputeWorkspaceDirectory = false;
	private static boolean requireToupdateModel = false;
	
	public static void init(DataModel inputModel){
		requireToupdateModel = !alreadyInit || 
		!currentProjectWebProjectName.equals(inputModel.getWebProjectName());
		if(requireToupdateModel){
		model = inputModel;
		currentProjectWebProjectName = inputModel.getWebProjectName();
		}
		}

	public	static String getWorkspaceDirectory() {
		if (!alreadyComputeWorkspaceDirectory){
			workspaceDirectory = ResourcesPlugin.getWorkspace().getRoot()
			.getLocation().toOSString();
			alreadyComputeWorkspaceDirectory = true;
		}
		return workspaceDirectory;
	}
	
	public static String getCurrentDynamicWebProjectDir(){
		if (!alreadyComputeCurrentDynamicWebProjectDir || requireToupdateModel){
			currentDynamicWebProjectDir = FileUtils.addAnotherNodeToPath(
			getWorkspaceDirectory(), model.getWebProjectName());
			alreadyComputeCurrentDynamicWebProjectDir = true;
		}
		return currentDynamicWebProjectDir;
	}
	
	
	public static String getTempCodegenOutputLocation() {
		if (!alreadyComputeTempCodegenOutputLocation){
			String[] nodes = {	Axis2CreationUIMessages.DIR_DOT_METADATA,
								Axis2CreationUIMessages.DIR_DOT_PLUGINS,
								Axis2CreationUIMessages.AXIS2_PROJECT,
								Axis2CreationUIMessages.CODEGEN_RESULTS};
			tempCodegenOutputLocation = FileUtils.addNodesToPath(getWorkspaceDirectory(), nodes);
			alreadyComputeTempCodegenOutputLocation = true;
		}
		return tempCodegenOutputLocation;
	}

}
