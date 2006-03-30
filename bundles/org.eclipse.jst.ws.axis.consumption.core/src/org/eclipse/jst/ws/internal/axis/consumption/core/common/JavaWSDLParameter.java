/*******************************************************************************
 * Copyright (c) 2003, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060330   124667 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/
package org.eclipse.jst.ws.internal.axis.consumption.core.common;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;


public class JavaWSDLParameter {

	public static final byte SERVER_SIDE_NONE = 0;
	public static final byte SERVER_SIDE_BEAN = 1;
	public static final byte SERVER_SIDE_EJB = 2;

	public static final String STYLE_RPC = "RPC"; //$NON-NLS-1$
	public static final String STYLE_DOCUMENT = "DOCUMENT"; //$NON-NLS-1$
	public static final String STYLE_WRAPPED = "WRAPPED"; //$NON-NLS-1$

	public static final String USE_LITERAL = "LITERAL"; //$NON-NLS-1$
	public static final String USE_ENCODED = "ENCODED"; //$NON-NLS-1$

	private String beanName = null; // The name of the Java bean
	private String classpath = null;
	// The class path for loading the bean and command execution
	private String beanPackage = null; // The package location of the bean
	private String portTypeName = null; // The name of the port type element
	private String serviceName = null; // The name of the service element
	private String outputWsdlLocation = null;
	// The output location of the WSDL file
	private String inputWsdlLocation = null;
	// The input location of the WSDL file
	private String urlLocation = null; // The URL location of the web service
	private Hashtable methods = null; // The array of method names
	private String methodString_ = ""; //$NON-NLS-1$
	 //The string representation of the method array
	private String style = null;
	// The style (RPC | DOCUMENT | WRAPPED) attribute for the generated WSDL
	private String use = null;
	// The use (LITERAL | ENCODED) attribtue for the generated WSDL
	private String output = null;
	// The output location for deployment descriptors & Java code (output from WSDL2Java command)
	private String javaOutput = null;
	// The output location for Java code (output from WSDL2Java command)
	private boolean metaInfOnly = false; // META-INF-ONLY flag
	private byte serverSide = SERVER_SIDE_NONE;
	// server-side flag (SERVER_SIDE_NONE | SERVER_SIDE_BEAN | SERVER_SIDE_EJB)
	private String namespace = null;
	private boolean skeletonDeploy = true;
	private String container = null;
	private String[] deploymentFiles = null;
	private String[] javaFiles = null;
	private String projectURL = null;
	private boolean guessProjectURL = false;
	private HashMap mappingPairs;
	private String httpUsername_ = null;
	private String httpPassword_ = null;

	public void setContainer(String container) {
		this.container = container;
	}

	public String getContainer() {
		return container;
	}

	/**
	 * Constructor for JavaWSDLParameter.
	 */
	public JavaWSDLParameter() {
	}

	/**
	 * Returns the beanName.
	 * @return String
	 */
	public String getBeanName() {
		return beanName;
	}

	public String getBeanPackage() {
		return beanPackage;
	}

	/**
	 * Returns the portTypeName.
	 * @return String
	 */
	public String getPortTypeName() {
		return portTypeName;
	}

	/**
	 * Returns the serviceName.
	 * @return String
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Returns the output.
	 * @return String
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * Returns the methods.
	 * @return String[]
	 */
	public Hashtable getMethods() {
		return methods;
	}

	/**
	 * Returns the comma seperated string represetation of the methods.
	 * @return String
	 */
	public String getMethodString() {
		if (methodString_.equals(""))
		{		
			Enumeration e = methods.keys();
	 		for (int i=0; e.hasMoreElements(); i++)
	    	{
	
				String signature = (String) e.nextElement();
				if (((Boolean) methods.get(signature)).booleanValue()){
								int index = signature.indexOf('(');
								String name = signature.substring(0, index);
								methodString_ += name;
				}
		    	if (i != methods.size() -1) // this is not the last array item	
				{
					methodString_ += ",";
				}
			}
		}
		return methodString_;
	}
	
	/**
	 * Sets a comma separated string of methods	 *
	 */
	public void setMethodString(String methods)
	{
		methodString_ = methods;
	}

	/**
	 * Returns the style attribute.
	 * @return String
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * Returns the urlLocation.
	 * @return String
	 */
	public String getUrlLocation() {
		return urlLocation;
	}

	/**
	 * Returns the use attribute.
	 * @return String
	 */
	public String getUse() {
		return use;
	}

	/**
	 * Returns the outputWsdlLocation.
	 * @return String
	 */
	public String getOutputWsdlLocation() {
		return outputWsdlLocation;
	}

	/**
	 * Sets the beanName.
	 * @param beanName The beanName to set
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public void setBeanPackage(String beanPackage) {
		this.beanPackage = beanPackage;
	}

	/**
	 * Sets the portTypeName.
	 * @param portTypeName The portTypeName to set
	 */
	public void setPortTypeName(String portTypeName) {
		this.portTypeName = portTypeName;
	}

	/**
	 * Sets the serviceName.
	 * @param serviceName The serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Sets the output.
	 * @param output The output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * Sets the methods.
	 * @param methods The methods to set
	 */
	public void setMethods(Hashtable methods) {
		this.methods = methods;
	}

	/**
	 * Sets the style.
	 * @param style The style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * Sets the urlLocation.
	 * @param urlLocation The urlLocation to set
	 */
	public void setUrlLocation(String urlLocation) {
		this.urlLocation = urlLocation;
	}

	/**
	 * Sets the use.
	 * @param use The use to set
	 */
	public void setUse(String use) {
		this.use = use;
	}

	/**
	 * Sets the outputWsdlLocation.
	 * @param outputWsdlLocation The outputWsdlLocation to set
	 */
	public void setOutputWsdlLocation(String wsdlLocation) {
		this.outputWsdlLocation = wsdlLocation;
	}

	/**
	 * Returns the metaInfOnly.
	 * @return boolean
	 */
	public boolean isMetaInfOnly() {
		return metaInfOnly;
	}

	/**
	 * Sets the metaInfOnly.
	 * @param metaInfOnly The metaInfOnly to set
	 */
	public void setMetaInfOnly(boolean metaInfOnly) {
		this.metaInfOnly = metaInfOnly;
	}

	/**
	 * Returns the serverSide.
	 * @return byte
	 */
	public byte getServerSide() {
		return serverSide;
	}

	/**
	 * Sets the serverSide.
	 * @param serverSide The serverSide to set
	 */
	public void setServerSide(byte serverSide) {
		this.serverSide = serverSide;
	}

	/**
	 * Returns the inputWsdlLocation.
	 * @return String
	 */
	public String getInputWsdlLocation() {
		return inputWsdlLocation;
	}

	/**
	 * Sets the inputWsdlLocation.
	 * @param inputWsdlLocation The inputWsdlLocation to set
	 */
	public void setInputWsdlLocation(String inputWsdlLocation) {
		this.inputWsdlLocation = inputWsdlLocation;
	}

	/**
	 * Returns the namespace.
	 * @return String
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Sets the namespace.
	 * @param namespace The namespace to set
	 */
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * Returns the Java output.
	 * @return String
	 */
	public String getJavaOutput() {
		return javaOutput;
	}

	/**
	 * Sets the javaOutput.
	 * @param javaOutput The Java output to set
	 */
	public void setJavaOutput(String javaOutput) {
		this.javaOutput = javaOutput;
	}

	/**
	 * @return
	 */
	public String getClasspath() {
		return classpath;
	}

	/**
	 * @param string
	 */
	public void setClasspath(String string) {
		classpath = string;
	}

	/**
	 * @return
	 */
	public boolean isSkeletonDeploy() {
		return skeletonDeploy;
	}

	/**
	 * @param b
	 */
	public void setSkeletonDeploy(boolean b) {
		skeletonDeploy = b;
	}

	/**
	 * @return
	 */
	public String[] getDeploymentFiles() {
		return deploymentFiles;
	}

	/**
	 * @param strings
	 */
	public void setDeploymentFiles(String[] strings) {
		deploymentFiles = strings;
	}

	/**
	 * @return
	 */
	public String[] getJavaFiles() {
		return javaFiles;
	}

	/**
	 * @param strings
	 */
	public void setJavaFiles(String[] strings) {
		javaFiles = strings;
	}

	/**
	 * @return
	 */
	public String getProjectURL() {
		return projectURL;
	}

	/**
	 * @param string
	 */
	public void setProjectURL(String string) {
		projectURL = string;
	}
	
	public HashMap getMappings()
	{
			return mappingPairs;
	}

	public void setMappings(HashMap map)
	{
			mappingPairs = map;
	}
	
	/**
	 * Returns the user name for HTTP basic authentication
	 * @return String
	 */
	public String getHTTPUsername()
	{
	  return httpUsername_;
	}

	/**
	 * Sets the user name for HTTP basic authentication
	 * @param String
	 */
	public void setHTTPUsername(String httpUsername)
	{
	  httpUsername_ = httpUsername;
	}

	/**
	 * Returns the password for HTTP basic authentication
	 * @return String
	 */
	public String getHTTPPassword()
	{
	  return httpPassword_;
	}

	/**
	 * @param string
	 */
	public void setHTTPPassword(String httpPassword) {
		httpPassword_ = httpPassword;
	}

	public boolean isGuessProjectURL() {
		return guessProjectURL;
	}

	public void setGuessProjectURL(boolean guessProjectURL) {
		this.guessProjectURL = guessProjectURL;
	}

}
