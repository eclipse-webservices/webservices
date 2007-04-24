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
 * 20070130   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;

public class RuntimePropertyUtils {
	private static File PropertiesFileDir,serverPropertiesFile,statusPropertyFile;
	private static IStatus status;
	private static Properties properties;
	
	private static void init(){
		PropertiesFileDir = new File(Axis2CoreUtils.tempAxis2Directory());
		if (!PropertiesFileDir.exists()){
			PropertiesFileDir.mkdirs();
		}
		properties = new Properties();
	}
	
	private static void serverPropertiesFileInit() throws IOException{
		serverPropertiesFile = new File(Axis2CoreUtils.tempAxis2WebappFileLocation());
		if (!serverPropertiesFile.exists()){
			serverPropertiesFile.createNewFile();
		}
		properties.load(new FileInputStream(serverPropertiesFile));
	}
	
	private static void statusPropertiesFileInit() throws IOException{
		statusPropertyFile = new File(Axis2CoreUtils.tempRuntimeStatusFileLocation());
		if (!statusPropertyFile.exists()){
			statusPropertyFile.createNewFile();
		}
		properties.load(new FileInputStream(statusPropertyFile));
	}
	
	public static IStatus writeServerPathToPropertiesFile(String axis2Path) {
		//Fix for properties file skipping the File seperator charactor on windows when loading again
		String axis2PathNew = null;
		if ((axis2Path.indexOf("\\")) != -1) {
			axis2PathNew = axis2Path.replace(File.separator, File.separator+File.separator);;
		}else{
			axis2PathNew=axis2Path;
		}
		try {
			init();
			serverPropertiesFileInit();
			if(! (properties.size()== 0)){
				if(properties.containsKey(Axis2CoreUIMessages.PROPERTY_KEY_PATH)){
					properties.remove(Axis2CoreUIMessages.PROPERTY_KEY_PATH);
				}
			}
				Axis2CoreUtils.writePropertyToFile(serverPropertiesFile, 
												   Axis2CoreUIMessages.PROPERTY_KEY_PATH,
												   axis2PathNew);
		} catch (FileNotFoundException e) {
			updateStatusError();
		} catch (IOException e) {
			updateStatusError();
		}
		
		return status;
	}
	
	public static String getServerPathFromPropertiesFile(){
		init();
		String serverPath = null;
		serverPropertiesFile = new File(Axis2CoreUtils.tempAxis2WebappFileLocation());
		if (!serverPropertiesFile.exists()){
			updateStatusError();			
		}
		try {
			properties.load(new FileInputStream(serverPropertiesFile));
			if(properties.containsKey(Axis2CoreUIMessages.PROPERTY_KEY_PATH)){
				serverPath = properties.getProperty(Axis2CoreUIMessages.PROPERTY_KEY_PATH);
			}
		} catch (FileNotFoundException e) {
			updateStatusError();
		} catch (IOException e) {
			updateStatusError();
		}
		return serverPath;
	}
	
	
	public static IStatus writeServerStausToPropertiesFile(String runtimeStatus) {
		try {
			init();
			statusPropertiesFileInit();
			if(! (properties.size()== 0)){
				if(properties.containsKey(Axis2CoreUIMessages.PROPERTY_KEY_STATUS)){
					properties.remove(Axis2CoreUIMessages.PROPERTY_KEY_STATUS);
				}
			}
			Axis2CoreUtils.writePropertyToFile(statusPropertyFile, 
											   Axis2CoreUIMessages.PROPERTY_KEY_STATUS, 
											   runtimeStatus);
		} catch (FileNotFoundException e) {
			updateStatusError(); 
		} catch (IOException e) {
			updateStatusError();
		}
		
		return status;
	}

	private static void updateStatusError(){
			status = new Status( IStatus.ERROR, 
					"id", 
					0, 
					Axis2CoreUIMessages.ERROR_INVALID_AXIS2_SERVER_LOCATION, 
					null ); 
		}
		
	}
