/*
/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.ws.internal.wsrt;


public class WebServiceRuntimeInfo {

	private java.lang.String state;
	private ServiceType[] serviceTypes;
	private ClientType[] clientTypes;
	private java.lang.String[] servletLevels;
	private java.lang.String[] j2eeLevels;
	private java.lang.String[] runtimeIds;
	private java.lang.String[] serverFactoryIds;
	private String className;
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public ClientType[] getClientTypes() {
		return clientTypes;
	}
	
	public void setClientTypes(ClientType[] clientTypes) {
		this.clientTypes = clientTypes;
	}
	
	public java.lang.String[] getJ2eeLevels() {
		return j2eeLevels;
	}
	
	public void setJ2eeLevels(java.lang.String[] levels) {
		j2eeLevels = levels;
	}
	
	public java.lang.String[] getRuntimeIds() {
		return runtimeIds;
	}
	
	public void setRuntimeIds(java.lang.String[] runtimeIds) {
		this.runtimeIds = runtimeIds;
	}
	
	public java.lang.String[] getServerFactoryIds() {
		return serverFactoryIds;
	}
	
	public void setServerFactoryIds(java.lang.String[] serverFactoryIds) {
		this.serverFactoryIds = serverFactoryIds;
	}
	
	public ServiceType[] getServiceTypes() {
		return serviceTypes;
	}
	
	public void setServiceTypes(ServiceType[] serviceTypes) {
		this.serviceTypes = serviceTypes;
	}
	
	public java.lang.String[] getServletLevels() {
		return servletLevels;
	}
	
	public void setServletLevels(java.lang.String[] servletLevels) {
		this.servletLevels = servletLevels;
	}
	
	public java.lang.String getState() {
		return state;
	}
	
	public void setState(java.lang.String state) {
		this.state = state;
	}
	
	
	
}
