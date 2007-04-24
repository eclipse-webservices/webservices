/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * WSO2 Inc. - initial API and implementation
 * -------- -------- -----------------------------------------------------------
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.utils;


public class CommonUtils {
	
	 public static String classNameFromQualifiedName(String qualifiedCalssName){
		 //This was done due to not splitting with . Strange
		 qualifiedCalssName = qualifiedCalssName.replace('.', ':');
		 String[] parts = qualifiedCalssName.split(":");
		 if (parts.length == 0){
			 return "";
		 }
		 return parts[parts.length-1];
	 }
	  
	 public static String packageNameFromQualifiedName(String qualifiedCalssName){
		 //This was done due to not splitting with . Strange
		 qualifiedCalssName = qualifiedCalssName.replace('.', ':');
		 String[] parts = qualifiedCalssName.split(":");
		 StringBuffer packageName = new StringBuffer();
		 for (int i = 0; i < parts.length-1; i++) {
			 packageName.append(parts[i]);
			 if (! (i == parts.length-2)){
				 packageName.append(".");
			 }
		 }
		 return packageName.toString();
	 }
	 
	 
	 public static String packgeName2PathName(String packageName){
		 packageName = packageName.replace('.', '/');
		 return packageName;
	 }
	 

}
