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
 * 20070410 168766 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  facet to the framework for 168766
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070501   180284 sandakith@wso2.com - Lahiru Sandakith
 * 20070507   185686 sandakith@wso2.com - Lahiru Sandakith
 * 20070516   183147 sandakith@wso2.com - Lahiru Sandakith Fix for the persisting DBCS paths
 * 20070523   174876 sandakith@wso2.com - Lahiru Sandakith, Persist Preferences inside Framework
 * 20070730   194786 sandakith@wso2.com - Lahiru Sandakith, adding servletapi jar filter
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.facet.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;
import org.eclipse.jst.ws.axis2.core.context.Axis2EmitterContext;
import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.jst.ws.axis2.core.utils.Axis2CoreUtils;
import org.eclipse.jst.ws.axis2.core.utils.FileUtils;

public class Axis2WebappUtils {

	private static String tempWarLocation = null;
	private static boolean alreadyWarExist = false;
	private static Axis2EmitterContext context;

	public static String  copyAxis2War(IProgressMonitor monitor, String Axis2Home)
										throws FileNotFoundException, IOException{
		context = WebServiceAxis2CorePlugin.getDefault().getAxisEmitterContext();
		if(!alreadyWarExist){
			File tempAxis2Directory = new File (Axis2CoreUtils.tempAxis2Directory());
			if(!tempAxis2Directory.exists()){
				tempAxis2Directory.mkdirs();
			}
			if (tempAxis2Directory.isDirectory()) {
				tempWarLocation = Axis2CoreUtils.addAnotherNodeToPath(
						Axis2CoreUtils.tempAxis2Directory(),
						Axis2Constants.DIR_EXPLOADED_TEMPWAR);
				File tempWarLocationFile= new File(tempWarLocation);
				if (tempWarLocationFile.exists()) {
					FileUtils.deleteDirectories(tempWarLocationFile);
				}
				tempWarLocationFile.mkdirs();
		
				String axis2HomeLocation = null;
				if(context.getAxis2RuntimeLocation()!=null){
					axis2HomeLocation = context.getAxis2RuntimeLocation();
				}else{
				    Axis2EmitterContext context = WebServiceAxis2CorePlugin
													.getDefault().getAxisEmitterContext();
				    axis2HomeLocation =  context.getAxis2RuntimeLocation();

				}

					String axis2WebappLocation = Axis2CoreUtils.addAnotherNodeToPath(
							axis2HomeLocation,
							"webapp");
					String axis2LibFile = Axis2CoreUtils.addAnotherNodeToPath(
							axis2HomeLocation,
					"lib");
					String axis2ConfFile = Axis2CoreUtils.addAnotherNodeToPath(
							axis2HomeLocation,
					"conf");
					String axis2RepositoryFile = Axis2CoreUtils.addAnotherNodeToPath(
							axis2HomeLocation,
					"repository");
					String axis2TempWebInfFile = Axis2CoreUtils.addAnotherNodeToPath(
							tempWarLocation,
					"WEB-INF");
					String axis2TempWebInfLibFile = Axis2CoreUtils.addAnotherNodeToPath(
							axis2TempWebInfFile,
					"lib");
					String axis2TempWebInfConfFile = Axis2CoreUtils.addAnotherNodeToPath(
							axis2TempWebInfFile,
					"conf");
					String axis2TempWebBuildFile = Axis2CoreUtils.addAnotherNodeToPath(
							tempWarLocation,
					"build.xml");
					String axis2TempWebXMLFile = Axis2CoreUtils.addAnotherNodeToPath(
							axis2TempWebInfFile,
							Axis2Constants.FILE_WEB_XML);

					//Copy the webapp content 
					FileUtils.copyDirectory(new File(axis2WebappLocation), tempWarLocationFile);
					////delete the axis2 web.xml File(DWP already have)
					new File(axis2TempWebBuildFile).delete();
					//delete the web.xml File
					new File(axis2TempWebXMLFile).delete();
					//Copy libs 
					FileUtils.copyDirectory(new File(axis2LibFile),
											new File(axis2TempWebInfLibFile));
					//copy conf/axis2.xml
					FileUtils.copyDirectory(new File(axis2ConfFile), 
											new File(axis2TempWebInfConfFile));
					//Copy modules and services 					
					FileUtils.copyDirectory(new File(axis2RepositoryFile), 
											new File(axis2TempWebInfFile));
					//Filter out the servletapi jars
					FileUtils.filterOutRestrictedFiles(axis2TempWebInfLibFile,
							Axis2Constants.FILE_SERVLET_API,"jar");
					
					alreadyWarExist= true;
				}

		}	
		return tempWarLocation;
	}

}
