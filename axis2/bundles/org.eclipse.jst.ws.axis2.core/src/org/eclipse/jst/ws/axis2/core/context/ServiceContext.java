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
 * 20070130   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse 
 * 											the Axis2 runtime to the framework for 168762
 * 20070502   184302 sandakith@wso2.com - Lahiru Sandakith, Fix copyright for Axis2 plugins
 * 20070508   175030 sandakith@wso2.com - Lahiru Sandakith, Refactor BuServiceContext 
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.context;

public class ServiceContext {
	
	private static ServiceContext instance;
	private static String serviceName; 
	
	//singleton
	private ServiceContext(){}
	
	public static ServiceContext getInstance(){
		if (instance == null) {
			instance = new ServiceContext();
		}
		return instance;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		ServiceContext.serviceName = serviceName;
	}
	
	
}
