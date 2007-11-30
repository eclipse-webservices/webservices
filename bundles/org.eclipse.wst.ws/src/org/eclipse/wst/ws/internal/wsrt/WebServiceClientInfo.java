/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060728   145426 kathy@ca.ibm.com - Kathy Chan
 * 20070815   199626 kathy@ca.ibm.com - Kathy Chan
 * 20071130   203826 kathy@ca.ibm.com - Kathy Chan
 *******************************************************************************/

package org.eclipse.wst.ws.internal.wsrt;

public class WebServiceClientInfo {

	private WebServiceState state;
	private String serverFactoryId;
	private String serverInstanceId;
	private String serverRuntimeId;
	private String webServiceRuntimeId;
	private String wsdlURL;
	private String implURL;
	private String proxyEndpoint = null;
	private boolean serverCreated = false;
	
	public String getImplURL() {
		return implURL;
	}
	public void setImplURL(String implURL) {
		this.implURL = implURL;
	}
	public String getServerFactoryId() {
		return serverFactoryId;
	}
	public void setServerFactoryId(String serverFactoryId) {
		this.serverFactoryId = serverFactoryId;
	}
	public String getServerInstanceId() {
		return serverInstanceId;
	}
	public void setServerInstanceId(String serverInstanceId) {
		this.serverInstanceId = serverInstanceId;
	}
	public WebServiceState getState() {
		return state;
	}
	public void setState(WebServiceState state) {
		this.state = state;
	}
	public String getWebServiceRuntimeId() {
		return webServiceRuntimeId;
	}
	public void setWebServiceRuntimeId(String webServiceRuntimeId) {
		this.webServiceRuntimeId = webServiceRuntimeId;
	}
	public String getWsdlURL() {
		return wsdlURL;
	}
	public void setWsdlURL(String wsdlURL) {
		this.wsdlURL = wsdlURL;
	}
	public String getProxyEndpoint() {
		return proxyEndpoint;
	}
	public void setProxyEndpoint(String proxyEndpoint) {
		this.proxyEndpoint = proxyEndpoint;
	}
	
	/**
	 * @return Indicated whether the framework had created 
	 * the server instance referenced by getServerInstanceId
	 */
	public boolean isServerCreated() {
		return serverCreated;
	}
	
	/**
	 * @param serverCreated True if the framework had created 
	 * the server instance referenced by getServerInstanceId
	 */
	public void setServerCreated(boolean serverCreated) {
		this.serverCreated = serverCreated;
	}
	public String getServerRuntimeId() {
		return serverRuntimeId;
	}
	public void setServerRuntimeId(String serverRuntimeId) {
		this.serverRuntimeId = serverRuntimeId;
	}
}
