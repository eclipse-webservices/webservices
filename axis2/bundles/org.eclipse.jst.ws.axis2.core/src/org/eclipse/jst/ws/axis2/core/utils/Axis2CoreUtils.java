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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUIMessages;

public class Axis2CoreUtils {
	
	public static String tempAxis2Directory() {
		String projectDirDotMetadata = addAnotherNodeToPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString(), 
				 Axis2CoreUIMessages.DIR_DOT_METADATA);
		String projectDirDotPlugins= addAnotherNodeToPath(projectDirDotMetadata,
												Axis2CoreUIMessages.DIR_DOT_PLUGINS);
		String tempAxis2Dir =addAnotherNodeToPath(projectDirDotPlugins, 
							 Axis2CoreUIMessages.TEMP_AXIS2_FACET_DIR);
		return tempAxis2Dir;
	}
	
	public static String tempAxis2WebappFileLocation() {
		return
		addAnotherNodeToPath(tempAxis2Directory(),
							 Axis2CoreUIMessages.WEBAPP_EXPLODED_SERVER_LOCATION_FILE);
	}
	
	
	public static String tempRuntimeStatusFileLocation() {
		return
		addAnotherNodeToPath(tempAxis2Directory(),
							 Axis2CoreUIMessages.SERVER_STATUS_LOCATION_FILE);
	}
	
	public static String addAnotherNodeToPath(String currentPath, String newNode) {
		return currentPath + File.separator + newNode;
	}
	
	public static void  writePropertyToFile(File file,String key, String value) throws IOException {
	       BufferedWriter out = new BufferedWriter(new FileWriter(file));
	       out.write(key+"="+value+"\n");
	       out.close();
	}

}
