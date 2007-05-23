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
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse 
 * 										the Axis2 runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070501   180284 sandakith@wso2.com - Lahiru Sandakith
 * 20070511   186440 sandakith@wso2.com - Lahiru Sandakith fix 186440
 * 20070510   172926 sandakith@wso2.com - Lahiru Sandakith, Fix 172926 Use Util Classes
 * 20070517   187307 sandakith@wso2.com - Lahiru Sandakith, Fix 187307 Property File Fix
 * 20070518        187311 sandakith@wso2.com - Lahiru Sandakith, Fixing test resource addition
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.plugin.messages;

import org.eclipse.osgi.util.NLS;

public final class Axis2CoreUIMessages extends NLS {

	private static final String BUNDLE_NAME = 
			"org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUI";		//$NON-NLS-1$
	public static final String DIR_TEMPWAR="tempwar";							//$NON-NLS-1$
	public static final String DIR_EXPLOADED_TEMPWAR="exploadedwar";			//$NON-NLS-1$
	public static final String DIR_LIB="lib";									//$NON-NLS-1$
	public static final String DIR_TEST="test";									//$NON-NLS-1$
	public static final String FILE_AXIS2_WAR="axis2.war";						//$NON-NLS-1$
	public static final String  NULL="";										//$NON-NLS-1$
	public static final String ADB="adb";										//$NON-NLS-1$
	public static final String AAR="aar";										//$NON-NLS-1$
	public static final String TEMP_AXIS2_FACET_DIR="org.apache.axis2.facet";	//$NON-NLS-1$
	public static final String DIR_DOT_METADATA=".metadata";					//$NON-NLS-1$
	public static final String DIR_DOT_PLUGINS=".plugins";						//$NON-NLS-1$
	public static final String DIR_UNZIP="nzip";								//$NON-NLS-1$
	public static final String LOCAL_SERVER_PORT="http://localhost:8080";		//$NON-NLS-1$
	public static final String SERVICES="services";								//$NON-NLS-1$
	public static final String WEBAPP_EXPLODED_SERVER_LOCATION_FILE=
			"server.properties";												//$NON-NLS-1$
	public static final String SERVER_STATUS_LOCATION_FILE="status.properties";	//$NON-NLS-1$
	public static final String WAR_STATUS_LOCATION_FILE="war.properties";		//$NON-NLS-1$
	public static final String PROPERTY_KEY_PATH="path";						//$NON-NLS-1$
	public static final String PROPERTY_KEY_STATUS="status";					//$NON-NLS-1$
	public static final String SERVER_STATUS_PASS="pass";						//$NON-NLS-1$
	public static final String SERVER_STATUS_FAIL="fail";						//$NON-NLS-1$

	private Axis2CoreUIMessages() {
		// Do not instantiate
	}

	public static String LABEL_BROUSE;
	public static String LABEL_WEB_SERVICE_CODEGEN;
	public static String LABEL_WEB_SERVICE_CLIENT_CODEGEN;
	public static String LABEL_WEB_SERVICE_AAR;
	public static String LABEL_AAR_EXTENTION;
	public static String LABEL_DATABINDING;
	public static String LABEL_AXIS2_RUNTIME_LOAD;
	public static String LABEL_AXIS2_RUNTIME_LOAD_ERROR;
	public static String LABEL_AXIS2_RUNTIME_NOT_EXIT;
	public static String LABEL_GENERATE_TESTCASE_CAPTION;
	public static String LABEL_GENERATE_SERVERSIDE_INTERFACE;
	public static String LABEL_GENERATE_ALL;
	public static String LABEL_CLIENT_SIDE;
	public static String LABEL_SYNC_AND_ASYNC;
	public static String LABEL_SYNC;
	public static String LABEL_ASYNC;
	
	public static String AXIS2_LOCATION;
	public static String AXIS2_RUNTIME;
	public static String AXIS2_PREFERENCES;
	public static String AXIS2_RUNTIME_TOOLTIP;
	public static String AXIS2_PREFERENCES_TOOLTIP;
	public static String AXIS2_RUNTIME_LOCATION;
	public static String AXIS2_RUNTIME_PREFERENCES;

	public static String ERROR_INVALID_AXIS2_SERVER_LOCATION;
	public static String ERROR_INVALID_FILE_READ_WRITEL;
	public static String ERROR_SERVER_IS_NOT_SET;

	public static String PROGRESS_INSTALL_AXIS2_RUNTIME;
	public static String PROGRESS_UNINSTALL_AXIS2_RUNTIME;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Axis2CoreUIMessages.class);
	}
}
