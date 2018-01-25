/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20100413   307552 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS and Java EE 6 setup is incorrect
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryproviderconfig;


public class JAXRSLibraryProviderImpl implements JAXRSLibraryProvider {
	boolean showUpdateDD = false;
	boolean updateDDSelected = false;
	String libraryProviderID;
	String servletClassName;
	public JAXRSLibraryProviderImpl() {
	}
	public String getLibraryProviderID() {
		return libraryProviderID;
	}

	public void setLibraryProviderID(String id) {
		this.libraryProviderID = id;

	}
	public boolean getShowUpdateDDCheckBox() {
		return showUpdateDD;
	}
	public void setShowUpdateDDCheckBox(boolean show) {
		this.showUpdateDD = show;
	}
	public boolean getUpdateDDCheckBoxSelected() {
		return this.updateDDSelected;
	}
	public void setUpdateDDCheckBoxSelected(boolean selected) {
		this.updateDDSelected = selected;
	}
	public String getServletClassName() {
		return this.servletClassName;
	}
	public void setServletClassName(String name) {
		this.servletClassName = name;
		
	}
	

}
