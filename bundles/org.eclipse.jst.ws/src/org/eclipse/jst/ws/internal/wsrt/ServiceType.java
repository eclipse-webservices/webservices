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
package org.eclipse.jst.ws.internal.wsrt;

public class ServiceType {

	private String id;
	private WebServiceImpl webServiceImpl1;
	private String[] bottomUpModuleTypes;
	private String[] topDownModuleTypes;
	
	public String[] getBottomUpModuleTypes() {
		return bottomUpModuleTypes;
	}
	
	public void setBottomUpModuleTypes(String[] bottomUpModuleTypes) {
		this.bottomUpModuleTypes = bottomUpModuleTypes;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String[] getTopDownModuleTypes() {
		return topDownModuleTypes;
	}
	
	public void setTopDownModuleTypes(String[] topDownModuleTypes) {
		this.topDownModuleTypes = topDownModuleTypes;
	}
	
	public WebServiceImpl getWebServiceImpl1() {
		return webServiceImpl1;
	}
	
	public void setWebServiceImpl1(WebServiceImpl webServiceImpl1) {
		this.webServiceImpl1 = webServiceImpl1;
	}
	
	
	
}
