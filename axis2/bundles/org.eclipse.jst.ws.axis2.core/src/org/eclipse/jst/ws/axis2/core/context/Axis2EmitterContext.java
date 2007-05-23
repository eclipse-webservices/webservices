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
 *******************************************************************************/ 
package org.eclipse.jst.ws.axis2.core.context;

public interface Axis2EmitterContext {

	/**
	 * This constant string is used to lookup the runtime location general
	 * preference from the plugins local preferences store.
	 */
	public static final String PREFERENCE_AXIS2_RUNTIME_LOCATION = "axis2RuntimeLocation";
	public static final String PREFERENCE_AXIS2_SERVER_IS_WAR = "axis2ServerPathRepresentsWar";
	//Axis2 Preferences
	//Service Code generation Options
	public static final String PREFERENCE_SERVICE_DATABINDING = "serviceDatabinding"; 
	public static final String PREFERENCE_SERVICE_INTERFACE_SKELETON = "serviceInterfaceSkeleton"; 
	public static final String PREFERENCE_SERVICE_GENERATE_ALL = "serviceGenerateAll"; 
	
	//Client Code generation Options
	public static final String PREFERENCE_CLIENT_SYNC = "sync"; 
	public static final String PREFERENCE_CLIENT_ASYNC = "async"; 
	public static final String PREFERENCE_CLIENT_DATABINDING = "cleintDatabinding"; 
	public static final String PREFERENCE_CLIENT_TESTCASE = "clientTestcase"; 
	public static final String PREFERENCE_CLIENT_GENERATE_ALL = "clientGenerateAll"; 
	
	//Service Archive Options
	public static final String PREFERENCE_AAR_EXTENTION  = "aarExtention";

	/**
	 * @param selection set the axis2 runtime location.
	 */
	public void setAxis2RuntimeLocation(String runtimeLocation);

	/**
	 * @param returns the axis2 runtime location.
	 */
	public String getAxis2RuntimeLocation();
	
	public boolean isAxis2ServerPathRepresentsWar();

	public void setAxis2ServerPathRepresentsWar(String isWar);

	//Service Code generation Options

	public void setServiceDatabinding(String serviceDatabinding);

	public String getServiceDatabinding();

	public void setServiceInterfaceSkeleton(boolean serviceInterfaceSkeleton);

	public boolean isServiceInterfaceSkeleton();

	public void setServiceGenerateAll(boolean serviceGenerateAll);

	public boolean isServiceGenerateAll();


	//Client Code generation Options
	public void setSync(boolean clientSync);

	public boolean isSync();

	public void setAsync(boolean clientAsync);

	public boolean isAsync();

	public void setClientDatabinding(String clientDatabinding);

	public String getClientDatabinding();

	public void setClientTestCase(boolean clientTestCase);

	public boolean isClientTestCase();

	public void setClientGenerateAll(boolean clientGenerateAll);

	public boolean isClientGenerateAll();

	//AAR Options
	public void setAarExtention(String aarExtention);

	public String getAarExtention();

}
