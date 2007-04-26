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
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.plugin.messages;

import org.eclipse.osgi.util.NLS;

public final class Axis2CoreUIMessages extends NLS {

	private static final String BUNDLE_NAME = 
			"org.eclipse.jst.ws.axis2.core.plugin.messages.Axis2CoreUI";//$NON-NLS-1$

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
	
	public static String DIR_WEBCONTENT;
	public static String DIR_TEMPWAR;
	public static String DIR_EXPLOADED_TEMPWAR;
	public static String DIR_DIST;
	public static String DIR_DOT_METADATA;
	public static String DIR_WEB_INF;
	public static String DIR_LIB;
	public static String DIR_DOT_PLUGINS;
	public static String DIR_UNZIP;

	public static String FILE_AXIS2_WAR;
	
	public static String AXIS2_LOCATION;
	public static String AXIS2_RUNTIME;
	public static String AXIS2_PREFERENCES;
	public static String AXIS2_RUNTIME_TOOLTIP;
	public static String AXIS2_PREFERENCES_TOOLTIP;
	public static String TEMP_AXIS2_FACET_DIR;
	public static String WEBAPP_EXPLODED_SERVER_LOCATION_FILE;
	public static String SERVER_STATUS_LOCATION_FILE;
	public static String LABEL_WEBAPP_LOCATION;
	public static String NULL;
	public static String ADB;
	public static String AAR;
	public static String PROPERTY_KEY_PATH;
	public static String PROPERTY_KEY_STATUS;
	public static String LOCAL_SERVER_PORT;
	public static String SERVICES;

	public static String SERVER_STATUS;
	public static String SERVER_STATUS_PASS;
	public static String SERVER_STATUS_FAIL;

	public static String ERROR_INVALID_AXIS2_SERVER_LOCATION;
	public static String ERROR_INVALID_FILE_READ_WRITEL;
	public static String ERROR_SERVER_IS_NOT_SET;

	
	public static String PROGRESS_INSTALL_AXIS2_RUNTIME;
	public static String PROGRESS_UNINSTALL_AXIS2_RUNTIME;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Axis2CoreUIMessages.class);
	}
}
