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
 * 20070213   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse 
 * 										the Axis2 runtime to the framework for 168762
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.plugin.data;

public class ServerModel {
	
	private static String axis2ServerPath;

	public static String getAxis2ServerPath() {
		return axis2ServerPath;
	}

	public static void setAxis2ServerPath(String axis2ServerPathParam) {
		axis2ServerPath = axis2ServerPathParam;
	}

}
