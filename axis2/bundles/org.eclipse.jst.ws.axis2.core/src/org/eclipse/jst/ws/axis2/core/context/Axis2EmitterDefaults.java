/*******************************************************************************
 * Copyright (c) 2007 WSO2 Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * WSO2 Inc - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20070516   183147 sandakith@wso2.com - Lahiru Sandakith Fix for the persisting DBCS paths
 * 20070523   174876 sandakith@wso2.com - Lahiru Sandakith, Persist Preferences inside Framework
 * 20070824   200515 sandakith@wso2.com - Lahiru Sandakith, NON-NLS move to seperate file
 *******************************************************************************/ 
package org.eclipse.jst.ws.axis2.core.context;

import org.eclipse.jst.ws.axis2.core.constant.Axis2Constants;

public class Axis2EmitterDefaults {

	public static final String PREFERENCE_AXIS2_RUNTIME_LOCATION_DEFAULT = "";
	public static final boolean PREFERENCE_AXIS2_SERVER_IS_WAR = false;

	//Axis2 Preferences
	//Service Code generation Options
	public static final String PREFERENCE_SERVICE_DATABINDING_DEFAULT = Axis2Constants.ADB;
	public static final boolean PREFERENCE_SERVICE_INTERFACE_SKELETON_DEFAULT = false;
	public static final boolean PREFERENCE_SERVICE_GENERATE_ALL_DEFAULT = false;

	//Client Code generation Options
	public static final boolean PREFERENCE_CLIENT_SYNC_DEFAULT =  false;
	public static final boolean PREFERENCE_CLIENT_ASYNC_DEFAULT =  false;
	public static final String PREFERENCE_CLIENT_DATABINDING_DEFAULT  = Axis2Constants.ADB;
	public static final boolean PREFERENCE_CLIENT_TESTCASE_DEFAULT = false;
	public static final boolean PREFERENCE_CLIENT_GENERATE_ALL_DEFAULT = false;

	//Service Archive Options
	private static String PREFERENCE_AAR_EXTENTION_DEFAULT  = Axis2Constants.AAR;	//aarExtention


	/**
	 * @return returns the default setting for runtime location.
	 */
	public static String getAxis2RuntimeLocation() {
		return PREFERENCE_AXIS2_RUNTIME_LOCATION_DEFAULT;
	}
	
	public static boolean isAxis2ServerPathRepresentsWar() {
		return PREFERENCE_AXIS2_SERVER_IS_WAR;
	}


	//Service Code generation Options

	public static String getServiceDatabinding(){
		return PREFERENCE_SERVICE_DATABINDING_DEFAULT;
	}

	public static boolean isServiceInterfaceSkeleton(){
		return PREFERENCE_SERVICE_INTERFACE_SKELETON_DEFAULT;
	}

	public static boolean isServiceGenerateAll(){
		return PREFERENCE_SERVICE_GENERATE_ALL_DEFAULT;
	}


	//Client Code generation Options

	public static boolean isClientSync(){
		return PREFERENCE_CLIENT_SYNC_DEFAULT;
	}

	public static boolean isClientAsync(){
		return PREFERENCE_CLIENT_ASYNC_DEFAULT;
	}

	public static String getClientDatabinding(){
		return PREFERENCE_CLIENT_DATABINDING_DEFAULT;
	}

	public static boolean isClientTestCase(){
		return PREFERENCE_CLIENT_TESTCASE_DEFAULT;
	}

	public static boolean isClientGenerateAll(){
		return PREFERENCE_CLIENT_GENERATE_ALL_DEFAULT;
	}

	//Service AAR Options
	public static String getAarExtention(){
		return PREFERENCE_AAR_EXTENTION_DEFAULT;
	}

}
