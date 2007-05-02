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
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.core.context;

public class BUServiceContext {
	
	private static BUServiceContext instance;
	private static String serviceName; 
	
	//singleton
	private BUServiceContext(){}
	
	public static BUServiceContext getInstance(){
		if (instance == null) {
			instance = new BUServiceContext();
		}
		return instance;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		BUServiceContext.serviceName = serviceName;
	}
	
	
}
