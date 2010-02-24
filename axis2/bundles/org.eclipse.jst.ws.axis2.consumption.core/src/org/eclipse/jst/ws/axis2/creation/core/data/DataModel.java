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
 * 20070518   187311 sandakith@wso2.com - Lahiru Sandakith, Fixing test resource addition
 * 20091207   193996 samindaw@wso2.com - Saminda Wijeratne, selecting a specific service/portname
 *******************************************************************************/
package org.eclipse.jst.ws.axis2.creation.core.data;

import org.eclipse.jst.ws.axis2.consumption.core.data.Model;

public class DataModel extends Model{
	
	//Client Side
	private String serviceClass;
	private boolean generateServicesXML;
	private boolean servicesXML;
	private boolean generateAAR;
	private String  pathToServicesXML;
	private String  pathToWebServicesTempDir;
	
	//Server Side 
	private boolean generateServerSideInterface;
    private boolean serverXMLCheck;
	private boolean generateAllCheck;
	private String databindingType;
	private String namespaseToPackageMapping;


	public boolean isGenerateServicesXML(){
		return generateServicesXML;
	}

	public void setGenerateServicesXML(boolean generateServicesXML){
		this.generateServicesXML = generateServicesXML;
	}

	public String getPathToServicesXML(){
		return pathToServicesXML;
	}

	public void setPathToServicesXML(String pathToServicesXML){
		this.pathToServicesXML = pathToServicesXML;
	}

	public boolean isGenerateAAR() {
		return generateAAR;
	}

	public void setGenerateAAR(boolean generateAAR) {
		this.generateAAR = generateAAR;
	}

	public boolean isServicesXML() {
		return servicesXML;
	}

	public void setServicesXML(boolean servicesXML) {
		this.servicesXML = servicesXML;
	}

	public String getPathToWebServicesTempDir() {
		return pathToWebServicesTempDir;
	}

	public void setPathToWebServicesTempDir(String pathToWebServicesTempDir) {
		this.pathToWebServicesTempDir = pathToWebServicesTempDir;
	}

	public String getWsdlURI() {
		return super.getWsdlURI();
	}

	public void setWsdlURI(String wsdlURI) {
		super.setWsdlURI(wsdlURI);
	}

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

	public boolean isGenerateServerSideInterface() {
		return generateServerSideInterface;
	}

	public void setGenerateServerSideInterface(boolean generateServerSideInterface) {
		this.generateServerSideInterface = generateServerSideInterface;
	}

	public String getPortName() {
		return super.getPortName();
	}

	public void setPortName(String portName) {
		super.setPortName(portName);
	}

	public boolean isServerXMLCheck() {
		return serverXMLCheck;
	}

	public void setServerXMLCheck(boolean serverXMLCheck) {
		this.serverXMLCheck = serverXMLCheck;
	}

	public String getServiceName() {
		return super.getServiceName();
	}

	public String getPackageText() {
		return super.getPackageText();
	}

	public void setPackageText(String packageText) {
		super.setPackageText(packageText);
	}

	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getNamespaseToPackageMapping() {
		return namespaseToPackageMapping;
	}

	public void setNamespaseToPackageMapping(String namespaseToPackageMapping) {
		this.namespaseToPackageMapping = namespaseToPackageMapping;
	}

}
