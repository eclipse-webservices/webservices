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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.Path;

public class ClassLoadingUtil {

	private static String[] axis2ClassPath = null;
	private static int libCount = 0;
	private static AntClassLoader antClassLoader;
	
	public static void init(String project) {

		//Obtain a ant class loader instance
		antClassLoader =  new AntClassLoader();
		
		// Set the class loader to child first
		antClassLoader.setParentFirst(false);
		
		String[] classLoadPath = getAxis2Libs(project);
		URL[] urls = new URL[classLoadPath.length];

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
	}
	
	
	

	/**
	 * Load the class from the class loader 
	 * @param project
	 * @param fillyQualifiedClassName
	 * @return Class loaded through class loader
	 */
	public static Class loadClassFromAxis2LibPath(String project, String fillyQualifiedClassName){

		String[] classLoadPath = getAxis2Libs(project);
		Class cls = null;
		URL[] urls = new URL[classLoadPath.length];


		try {	
			for (int i = 0; i < classLoadPath.length; i++) {
				//Create a File object on the root of the directory containing the class file
				if(classLoadPath[i]!=null){
					File file = new File(classLoadPath[i]);
					// Convert File to a URL
					URL url = file.toURL();          
					urls[i]= url;
				}
			}

			// Create a new class loader with the directory
			ClassLoader cl = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());
//			ClassLoader cl = new URLClassLoader(urls,null); //Set no parent class loader and give me from local jars only

			// Load in the class
			cls = cl.loadClass(fillyQualifiedClassName);
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return cls;
	}

	/**
	 * load the instance from the class loader
	 * @param project
	 * @param fillyQualifiedClassName
	 * @param parameterTypes
	 * @param initargs
	 * @deprecated 
	 * @return instance from the class loader
	 */
	public static Object getInstanceFromAxis2LibPath(String project, String fillyQualifiedClassName,Class[] parameterTypes,Object[] initargs){

		String[] classLoadPath = getAxis2Libs(project);
		Class cls = null;
		Object instance = null;
		URL[] urls = new URL[classLoadPath.length];


		try {	
			for (int i = 0; i < classLoadPath.length; i++) {
				//Create a File object on the root of the directory containing the class file
				if(classLoadPath[i]!=null){
					File file = new File(classLoadPath[i]);
					// Convert File to a URL
					URL url = file.toURL();          
					urls[i]= url;
				}
			}

			// Create a new class loader with the directory
			URLClassLoader cl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
			//cl.

			// Load in the class
			cls = cl.loadClass(fillyQualifiedClassName);
			
			Constructor constructor = cls.getConstructor(parameterTypes);
			instance = constructor.newInstance(initargs);
			
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return instance;
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

	}


}
