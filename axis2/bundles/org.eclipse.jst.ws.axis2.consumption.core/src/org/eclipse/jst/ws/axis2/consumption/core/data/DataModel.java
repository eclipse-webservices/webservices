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
 * 20070110   168762 sandakith@wso2.com - Lahiru Sandakith, Initial code to introduse the Axis2 
 * 										  runtime to the framework for 168762
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.consumption.core.data;

public class DataModel extends Model{
	
	//Consumption DataModel Parameters
	
	private String 	serviceClass;
	private String 	databindingType;
	private String 	namespaseToPackageMapping;
	private boolean	Sync;
	private boolean	ASync;
	private boolean generateAllCheck;
	private boolean testCaseCheck;

	
	public String getDatabindingType() {
		return databindingType;
	}
	public void setDatabindingType(String databindingType) {
		this.databindingType = databindingType;
	}
	public boolean isGenerateAllCheck() {
		return generateAllCheck;
	}
	public void setGenerateAllCheck(boolean generateAllCheck) {
		this.generateAllCheck = generateAllCheck;
	}
	public String getNamespaseToPackageMapping() {
		return namespaseToPackageMapping;
	}
	public void setNamespaseToPackageMapping(String namespaseToPackageMapping) {
		this.namespaseToPackageMapping = namespaseToPackageMapping;
	}

	public String getPackageText() {
		return super.getPackageText();
	}

	public void setPackageText(String packageText) {
		super.setPackageText(packageText);
	}
	
	public String getPortName() {
		return super.getPortName();
	}

	public void setPortName(String portName) {
		super.setPortName(portName);
	}
	
	public String getServiceClass() {
		return serviceClass;
	}
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getServiceName() {
		return super.getServiceName();
	}

	public void setServiceName(String serviceName) {
		super.setServiceName(serviceName);
	}
	
	public String getWsdlURI() {
		return super.getWsdlURI();
	}

	public void setWsdlURI(String wsdlURI) {
		super.setWsdlURI(wsdlURI);
	}
	
	public boolean isASync() {
		return ASync;
	}
	public void setASync(boolean sync) {
		ASync = sync;
	}
	public boolean isSync() {
		return Sync;
	}
	public void setSync(boolean sync) {
		Sync = sync;
	}
	public boolean isTestCaseCheck() {
		return testCaseCheck;
	}
	public void setTestCaseCheck(boolean testCaseCheck) {
		this.testCaseCheck = testCaseCheck;
	}

}