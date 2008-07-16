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
 * 20070213   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 * 20080621   200069 samindaw@wso2.com - Saminda Wijeratne, saving the retrieved WSDL so no need to retrieve it again
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.consumption.core.data;

public class Model {
	
	private String  webProjectName;
	
	private boolean serverStatus;
	private String  wsdlURI;
	private String portName;
	private String serviceName;
	private String packageText;
	private Object wsdlDefinitionInstance=null;
	private String wsdlLocation="";
	
	public String getPackageText() {
		return packageText;
	}
	public void setPackageText(String packageText) {
		this.packageText = packageText;
	}
	public String getPortName() {
		return portName;
	}
	public void setPortName(String portName) {
		this.portName = portName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getWsdlURI() {
		return wsdlURI;
	}
	public void setWsdlURI(String wsdlURI) {
		this.wsdlURI = wsdlURI;
	}
	public boolean getServerStatus() {
		return serverStatus;
	}
	public void setServerStatus(boolean b) {
		this.serverStatus = b;
	}
	
	public String getWebProjectName() {
		return webProjectName;
	}
	public void setWebProjectName(String webProjectName) {
		this.webProjectName = webProjectName;
	}

	public void setWsdlDefinitionInstance(Object wsdlDefinitionInstance, String wsdlLocation) {
		if (wsdlDefinitionInstance!=null){
			this.wsdlDefinitionInstance = wsdlDefinitionInstance;
			this.wsdlLocation = wsdlLocation;
		}
	}

	public Object getWsdlDefinitionInstance() {
		return wsdlDefinitionInstance;
	}

	public boolean isWsdlAlreadyLoaded(String filepath) {
		return wsdlLocation.equalsIgnoreCase(filepath);
	}
}
