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
 * 20070501   180284 sandakith@wso2.com - Lahiru Sandakith
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;

public class Axis2CoreUtils {
	
	private static boolean alreadyComputedTempAxis2Directory = false;
	private static String tempAxis2Dir = null;
	
	public static String tempAxis2Directory() {
		if (!alreadyComputedTempAxis2Directory){
			String[] nodes = {Axis2Constants.DIR_DOT_METADATA,
					Axis2Constants.DIR_DOT_PLUGINS,
					Axis2Constants.TEMP_AXIS2_FACET_DIR};
			tempAxis2Dir =FileUtils.addNodesToPath(
					ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString(), nodes); 
			alreadyComputedTempAxis2Directory= true;
		}
		return tempAxis2Dir;
	}
	
	public static String tempAxis2WebappFileLocation() {
		return
		addAnotherNodeToPath(tempAxis2Directory(),
				Axis2Constants.WEBAPP_EXPLODED_SERVER_LOCATION_FILE);
	}
	
	
	public static String tempRuntimeStatusFileLocation() {
		return
		addAnotherNodeToPath(tempAxis2Directory(),
				Axis2Constants.SERVER_STATUS_LOCATION_FILE);
	}
	
	public static String tempWarStatusFileLocation() {
		return
		addAnotherNodeToPath(tempAxis2Directory(),
				Axis2Constants.WAR_STATUS_LOCATION_FILE);
	}
	
	public static String addAnotherNodeToPath(String currentPath, String newNode) {
		return currentPath + File.separator + newNode;
	}
	
	public static void  writePropertyToFile(File file,String key, String value) throws IOException {
		Writer out = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(file), "8859_1"));
	       out.write(key+"="+value+"\n");
	       out.close();
	}

}
