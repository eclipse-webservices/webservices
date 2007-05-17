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
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 runtime to the framework for 168762
 * 20070426   183046 sandakith@wso2.com - Lahiru Sandakith
 * 20070511   186440 sandakith@wso2.com - Lahiru Sandakith fix 186440
 * 20070517   187307 sandakith@wso2.com - Lahiru Sandakith, Fix 187307 Property File Fix
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.messages;

import org.eclipse.osgi.util.NLS;

public class Axis2CreationUIMessages extends NLS {

	//get the lables from this massages to the properties file
	private static final String BUNDLE_NAME = 
			"org.eclipse.jst.ws.axis2.creation.core.messages.Axis2CreationUI";	//$NON-NLS-1$
	public static final String FILE_SERVICES_XML="services.xml";				//$NON-NLS-1$
	public static final String FILE_XML="*.xml";								//$NON-NLS-1$
	public static final String DIR_META_INF="META-INF";							//$NON-NLS-1$
	public static final String DIR_BUILD="build";								//$NON-NLS-1$
	public static final String DIR_SRC="src";									//$NON-NLS-1$
	public static final String DIR_CLASSES="classes";							//$NON-NLS-1$
	public static final String DIR_RESOURCES="resources";						//$NON-NLS-1$
	public static final String DIR_WEB_INF="WEB-INF";							//$NON-NLS-1$
	public static final String DIR_SERVICES="services";							//$NON-NLS-1$
	public static final String DIR_WEBSERVICES="webservices";					//$NON-NLS-1$
	public static final String DIR_WEBCONTENT="WebContent";						//$NON-NLS-1$
	public static final String DIR_DOT_METADATA=".metadata";					//$NON-NLS-1$
	public static final String DIR_DOT_PLUGINS=".plugins";						//$NON-NLS-1$
	public static final String JAR_TASK="jar";									//$NON-NLS-1$
	public static final String AXIS2_PROJECT="org.eclipse.jst.ws.axis2";		//$NON-NLS-1$
	public static final String CODEGEN_RESULTS="codegen";						//$NON-NLS-1$
	public static final String DATA_BINDING_ADB="ADB";							//$NON-NLS-1$
	public static final String DATA_BINDING_NONE="NONE";						//$NON-NLS-1$
	public static final String SKELETON_SUFFIX="Skeleton.java";					//$NON-NLS-1$

	private Axis2CreationUIMessages() {
		// Do not instantiate
	}
	//labels
	public static String LABEL_HAVE_SERVICES_XML_FILE;
	public static String LABEL_DEFAULT_SERVICES_XML_FILE;
	public static String LABEL_AXIS2_PREFERENCE_PAGE;
	public static String LABEL_AXIS2_PREFERENCE_PAGE_HEADING;
	public static String LABEL_AXIS2_PREFERENCE_PAGE_SUB_HEADING;
	public static String LABEL_BROWSE;
	public static String LABEL_DATA_MODEL;
	public static String LABEL_JAVA_2_WSDL_PAGE_HEADING;
	public static String LABEL_JAVA_2_WSDL_PAGE_SUB_HEADING;
	public static String LABEL_SERVICE_NAME_CAPTION;
	public static String LABEL_GENERATE_TESTCASE_CAPTION;
	public static String LABEL_DATABINDING_CAPTION;
	public static String LABEL_PORTNAME;
	public static String LABEL_GENERATE_ALL;
	public static String LABEL_GENERATE_SERVERSIDE_INTERFACE;
	public static String LABEL_PACKEGE_NAME;
	public static String LABEL_NAMESPACE;
	public static String LABEL_PACKAGE;
	public static String LABEL_NAMESPACE_TO_PACKAGE;

	//error
	public static String ERROR_INVALID_SERVICES_XML;
	public static String ERROR_INVALID_FILE_READ_WRITEL;
	public static String ERROR_INVALID_WSDL_FILE_READ_WRITEL;
	public static String ERROR_INVALID_SERVICE_CREATION;
	public static String ERROR_CODEGEN_EXCEPTION;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Axis2CreationUIMessages.class);
	}

}
