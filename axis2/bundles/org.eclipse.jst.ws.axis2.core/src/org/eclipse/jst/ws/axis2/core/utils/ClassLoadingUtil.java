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
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

public class ClassLoadingUtil {

	private static String[] axis2ClassPath = null;
	private static String[] classLoadPath = null;
	private static int libCount = 0;
	private static AntClassLoader antClassLoader;
	private static boolean libsLoaded = false;
	private static URL[] urls = null;
	private static boolean alreadyInit = false;
	private static boolean initByClient = false;
	
	public static void init(String project) {
		if (!alreadyInit) {

		//Obtain a ant class loader instance
			if(antClassLoader==null){
		antClassLoader =  new AntClassLoader();
			}
		
		// Set the class loader to child first
		antClassLoader.setParentFirst(false);
		
			if (!(axis2ClassPath ==null) || !libsLoaded){
				classLoadPath = getAxis2Libs(project);
			}
			
			if(urls == null){
				urls= new URL[classLoadPath.length];
			}

		Path classpath = new Path(new Project());
		
		try{
			for (int i = 0; i < classLoadPath.length; i++) {
				//Create a File object on the root of the directory containing the class file
				if(classLoadPath[i]!=null){
					File file = new File(classLoadPath[i]);
					// Convert File to a URL
					URL url = file.toURL();          
					urls[i]= url;
					classpath.setPath(classLoadPath[i]);
				}
			}
			
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
			antClassLoader.setClassPath(classpath);
			alreadyInit = true;
				}
			}

	
	
	public static Class loadClassFromAntClassLoader(String fillyQualifiedClassName){
		
		Class cls = null;
		try{	
			cls = antClassLoader.loadClass(fillyQualifiedClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return cls;
	}
	
	
	private static String[] getAxis2Libs(String project){
		File webContainerPath = new File(FacetContainerUtils.pathToWebProjectContainerLib(project));
		axis2ClassPath = new String[webContainerPath.list().length];
		libCount = 0;
		visitAllFiles(webContainerPath);
		return axis2ClassPath;
	}


	public static void visitAllFiles(File dir) {
		if(!dir.toString().endsWith(".txt")){
			if (dir.isDirectory()) {

				String[] children = dir.list();
				for (int i=0; i<children.length; i++) {
					visitAllFiles(new File(dir, children[i]));
				}

			} else {
				axis2ClassPath[libCount]=dir.getAbsolutePath();
				libCount+=1;
			}

		}
		libsLoaded = true;
	}

	public static void cleanAntClassLoader(){
		if(initByClient){
			antClassLoader.cleanup();
			alreadyInit = false;
		}
}
	public static void setInitByClient(boolean status){
		initByClient = status;
	}
}
