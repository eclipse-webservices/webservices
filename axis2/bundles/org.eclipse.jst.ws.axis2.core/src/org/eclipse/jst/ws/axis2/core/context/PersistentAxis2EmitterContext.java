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

import org.eclipse.jst.ws.axis2.core.plugin.WebServiceAxis2CorePlugin;
import org.eclipse.wst.command.internal.env.context.PersistentContext;

public class PersistentAxis2EmitterContext extends PersistentContext implements
		Axis2EmitterContext {

	private static PersistentAxis2EmitterContext context_ = null;

	public static PersistentAxis2EmitterContext getInstance() 
	{
		if (context_ == null) {
			context_ = new PersistentAxis2EmitterContext();
			context_.load();
		}
		return context_;
	}
	
	public void load(){
		setDefault(PREFERENCE_AXIS2_RUNTIME_LOCATION, Axis2EmitterDefaults.getAxis2RuntimeLocation());
		setDefault(PREFERENCE_AXIS2_SERVER_IS_WAR, Axis2EmitterDefaults.isAxis2ServerPathRepresentsWar());
		//service defaults
		setDefault(PREFERENCE_SERVICE_DATABINDING, Axis2EmitterDefaults.getServiceDatabinding());
		setDefault(PREFERENCE_SERVICE_INTERFACE_SKELETON, Axis2EmitterDefaults.isServiceInterfaceSkeleton());
		setDefault(PREFERENCE_SERVICE_GENERATE_ALL, Axis2EmitterDefaults.isServiceGenerateAll());
		//client defaults
		setDefault(PREFERENCE_CLIENT_SYNC, Axis2EmitterDefaults.isClientSync());
		setDefault(PREFERENCE_CLIENT_ASYNC, Axis2EmitterDefaults.isClientAsync());
		setDefault(PREFERENCE_CLIENT_DATABINDING, Axis2EmitterDefaults.getClientDatabinding());
		setDefault(PREFERENCE_CLIENT_TESTCASE, Axis2EmitterDefaults.isClientTestCase());
		setDefault(PREFERENCE_CLIENT_GENERATE_ALL, Axis2EmitterDefaults.isClientGenerateAll());
		//AAR Defaults
		setDefault(PREFERENCE_AAR_EXTENTION, Axis2EmitterDefaults.getAarExtention());

	}
	
	private PersistentAxis2EmitterContext() 	{
		super(WebServiceAxis2CorePlugin.getInstance());
	}

	public String getAxis2RuntimeLocation() {
		return getValueAsString(PREFERENCE_AXIS2_RUNTIME_LOCATION);
	}

	public void setAxis2RuntimeLocation(String runtimeLocation) {
		setValue(PREFERENCE_AXIS2_RUNTIME_LOCATION, runtimeLocation);
	}
	
	public boolean isAxis2ServerPathRepresentsWar() {
		return getValueAsBoolean(PREFERENCE_AXIS2_SERVER_IS_WAR);
	}

	public void setAxis2ServerPathRepresentsWar(String isWar) {
		setValue(PREFERENCE_AXIS2_SERVER_IS_WAR, isWar);
	}
	
	//Service 
	
	public String getServiceDatabinding() {
		return getValueAsString(PREFERENCE_SERVICE_DATABINDING);
	}
	
	public void setServiceDatabinding(String serviceDatabinding) {
		setValue(PREFERENCE_SERVICE_DATABINDING, serviceDatabinding);
	}
	
	public boolean isServiceInterfaceSkeleton() {
		return getValueAsBoolean(PREFERENCE_SERVICE_INTERFACE_SKELETON);
	}
	
	public void setServiceInterfaceSkeleton(boolean serviceInterfaceSkeleton) {
		setValue(PREFERENCE_SERVICE_INTERFACE_SKELETON, serviceInterfaceSkeleton);	
	}	
	
	public boolean isServiceGenerateAll() {
		return getValueAsBoolean(PREFERENCE_SERVICE_GENERATE_ALL);
	}
	
	public void setServiceGenerateAll(boolean serviceGenerateAll) {
		setValue(PREFERENCE_SERVICE_GENERATE_ALL, serviceGenerateAll);
	}
	
	
	//Client
	
	public boolean isSync() {
		return getValueAsBoolean(PREFERENCE_CLIENT_SYNC);
	}
	
	public void setSync(boolean clientSync) {
		setValue(PREFERENCE_CLIENT_SYNC, clientSync);
	}

	public boolean isAsync() {
		return getValueAsBoolean(PREFERENCE_CLIENT_ASYNC);
	}
	
	public void setAsync(boolean clientAsync) {
		setValue(PREFERENCE_CLIENT_ASYNC, clientAsync);
	}
	
	public String getClientDatabinding() {
		return getValueAsString(PREFERENCE_CLIENT_DATABINDING);
	}
	
	public void setClientDatabinding(String clientDatabinding) {
		setValue(PREFERENCE_CLIENT_DATABINDING, clientDatabinding);
	}
	
	public boolean isClientTestCase() {
		return getValueAsBoolean(PREFERENCE_CLIENT_TESTCASE);
	}
	
	public void setClientTestCase(boolean clientTestCase) {
		setValue(PREFERENCE_CLIENT_TESTCASE, clientTestCase);
	}

	public boolean isClientGenerateAll() {
		return getValueAsBoolean(PREFERENCE_CLIENT_GENERATE_ALL);
	}

	public void setClientGenerateAll(boolean clientGenrateAll) {
		setValue(PREFERENCE_CLIENT_GENERATE_ALL, clientGenrateAll);
	}

	
	//AAR Options
	
	public String getAarExtention(){
		return getValueAsString(PREFERENCE_AAR_EXTENTION);
	}
	
	public void setAarExtention(String aarExtention){
		setValue(PREFERENCE_AAR_EXTENTION, aarExtention);
	}

}
