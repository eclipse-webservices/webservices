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

public class ClientType {

	private String id;
	private WebServiceClientImpl webServiceClientImpl1;
	private String[] moduleTypes;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String[] getModuleTypes() {
		return moduleTypes;
	}
	
	public void setModuleTypes(String[] moduleTypes) {
		this.moduleTypes = moduleTypes;
	}
	
	public WebServiceClientImpl getWebServiceClientImpl1() {
		return webServiceClientImpl1;
	}
	
	public void setWebServiceClientImpl1(WebServiceClientImpl webServiceClientImpl1) {
		this.webServiceClientImpl1 = webServiceClientImpl1;
	}
	
	
	
}
