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
 * 20070523   174876 sandakith@wso2.com - Lahiru Sandakith, Persist Preferences inside Framework
 * 20070606   177421 sandakith@wso2.com - fix web.xml wiped out when Axis2 facet
 * 20070808   194906 sandakith@wso2.com - Lahiru Sandakith, Fixing 194906 Runtime lib issue
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.facet.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterContext;
import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.axis2.core.utils.FacetContainerUtils;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;
import org.eclipse.jst.ws.axis2.core.utils.RuntimePropertyUtils;
import org.eclipse.jst.ws.axis2.facet.messages.Axis2FacetUIMessages;
import org.eclipse.jst.ws.axis2.facet.utils.Axis2RuntimeUtils;
import org.eclipse.jst.ws.axis2.facet.utils.Axis2WebappUtils;
import org.eclipse.jst.ws.axis2.facet.utils.ContentCopyUtils;
import org.eclipse.wst.common.frameworks.datamodel.AbstractDataModelOperation;

public class Axis2WebservicesServerCommand extends
AbstractDataModelOperation {

	IProject project;
	Axis2EmitterContext context;
	
	public Axis2WebservicesServerCommand(IProject project){
		context = WebServiceAxis2CorePlugin.getDefault().getAxisEmitterContext();
		this.project = project;
	}

	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
	throws ExecutionException {
		return Status.OK_STATUS;
	}
	
	public IStatus executeOverride(IProgressMonitor monitor)  {
		//Copy the axis2 facet in to this project
		IStatus status =null;
		String runtimeLocation = null;
		ContentCopyUtils contentCopyUtils = new ContentCopyUtils();
		try {
			if(context.isAxis2ServerPathRepresentsWar() 
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
		
		//First Setting the libs folder as ignored and then copy the content of the runtime
		IPath libPath = new Path(runtimeLocation);
		libPath = libPath.append(Axis2FacetUIMessages.DIR_WEB_INF);
		libPath = libPath.append(Axis2CoreUIMessages.DIR_LIB);
		List<String> ignoreList = new ArrayList<String>();
		ignoreList.add(libPath.toOSString());
		contentCopyUtils.updateCheckList(ignoreList);
		
		status = contentCopyUtils.copyDirectoryRecursivelyIntoWorkspace(
				runtimeLocation, 
				FacetContainerUtils.pathToWebProjectContainer(project.toString()), 
				monitor,
				false
		);
		
		// After that copy the nesessery set of libraries to the project again
		List<String> includeList = new ArrayList<String>();
		contentCopyUtils.updateCheckList(loadIncludeListWithAxis2Libs(libPath.toOSString(),
				includeList));
		String[] nodes = {Axis2FacetUIMessages.DIR_WEB_INF,Axis2CoreUIMessages.DIR_LIB};
		status = contentCopyUtils.copyDirectoryRecursivelyIntoWorkspace(
				libPath.toOSString(), 
				FileUtils.addNodesToPath(
						FacetContainerUtils.pathToWebProjectContainer(project.toString()), 
						nodes), 
				monitor,
				true
		);

		//Merge web.xml Files
		MergeWEBXMLCommand mergeWebXMLCommand = new MergeWEBXMLCommand();
		mergeWebXMLCommand.setExtraAxis2TagsAdded(false);
		mergeWebXMLCommand.setServerProject(project);
		mergeWebXMLCommand.exexuteOverride(monitor);

		//clean up tempery files
		File tempFacetDirectory = new File(runtimeLocation);
		if (tempFacetDirectory.exists() && context.isAxis2ServerPathRepresentsWar()) {
			FileUtils.deleteDir(tempFacetDirectory);
		}
		status = Status.OK_STATUS;
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
		status = new Status(1,project.toString(),1,Axis2CoreUIMessages.ERROR_SERVER_IS_NOT_SET,e);
		cleanupIfFacetStatusFailed(Axis2CoreUtils.tempAxis2Directory());
		return status;
	}
	
	/**
	 * Load the exact libs from the axis2 jars with the correct versions to the 
	 * <code>path</code>. This way we can 
	 * @param runtimeLocation
	 * @param includeList
	 * @return loaded list
	 */
	private List loadIncludeListWithAxis2Libs(String path, List includeList){
		for (int i = 0; i < Axis2FacetUIMessages.AXIS2_LIB_PREFIXES.length; i++) {
			File[] fileList = FileUtils.getMatchingFiles(path,
					Axis2FacetUIMessages.AXIS2_LIB_PREFIXES[i], 
					Axis2CoreUIMessages.JAR);
			for (int j = 0; j < fileList.length; j++) {
				includeList.add(fileList[j].getAbsolutePath());
			}
		}
		return includeList;
	}
}
