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
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 * 20071011   205972 sandakith@wso2.com - Lahiru Sandakith, Axis2 Libraries License files fix
 * 20071029   206967 sandakith@wso2.com - Lahiru Sandakith
 * 20080521   231098 sandakith@wso2.com - Lahiru Sandakith, Fix for Axis2 1.4 dependency chenge
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.constant;

public class Axis2Constants {
	
	//Axis2 Core Plugin Constants  	
	public static final String DIR_TEMPWAR="tempwar";							//$NON-NLS-1$
	public static final String DIR_EXPLOADED_TEMPWAR="exploadedwar";			//$NON-NLS-1$
	public static final String DIR_LIB="lib";									//$NON-NLS-1$
	public static final String DIR_TEST="test";									//$NON-NLS-1$
	public static final String FILE_AXIS2_WAR="axis2.war";						//$NON-NLS-1$
	public static final String  NULL="";										//$NON-NLS-1$
	public static final String ADB="adb";										//$NON-NLS-1$
	public static final String AAR="aar";										//$NON-NLS-1$
	public static final String JAR="jar";										//$NON-NLS-1$
	public static final String TXT="txt";										//$NON-NLS-1$
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
	
	//Axis2 Creation Plugin Constants 
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
	public static final String JAR_TASK="jar";									//$NON-NLS-1$
	public static final String AXIS2_PROJECT="org.eclipse.jst.ws.axis2";		//$NON-NLS-1$
	public static final String CODEGEN_RESULTS="codegen";						//$NON-NLS-1$
	public static final String DATA_BINDING_ADB="ADB";							//$NON-NLS-1$
	public static final String DATA_BINDING_NONE="NONE";						//$NON-NLS-1$
	public static final String SKELETON_SUFFIX="Skeleton.java";					//$NON-NLS-1$
	
	
	//Axis2 Consumption Plugin Constants  
	public static final String JUNIT_JAR="junit.jar";							//$NON-NLS-1$
	public static final String JUNIT_BUNDLE="org.junit";						//$NON-NLS-1$
	public static final String DIR_AXIS2_WEB="axis2-web";						//$NON-NLS-1$
	public static final String FILE_WEB_XML="web.xml";							//$NON-NLS-1$
	public static final String FILE_SERVLET_API="servletapi";					//$NON-NLS-1$
	public static final String[] AXIS2_LIB_PREFIXES = {							//$NON-NLS-1$
        "ant",
        "axiom",
        "axis2",
        "commons",
        "log4j",
        "neethi",
        "stax",
        "geronimo-stax-api_1.0_spec",
        "wsdl4j",
        "wstx",
        "xbean",
        "XmlSchema",
        "woden",
        "activation",
        "annogen",
        "backport-util",
        "mex-impl",
        "mail"
	};
}	
	
