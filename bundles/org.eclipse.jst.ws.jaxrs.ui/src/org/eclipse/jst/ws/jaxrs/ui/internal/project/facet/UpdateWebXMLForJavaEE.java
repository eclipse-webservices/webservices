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
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.javaee.web.Servlet;
import org.eclipse.jst.javaee.web.ServletMapping;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSJEEUtils;

public class UpdateWebXMLForJavaEE extends UpdateWebXMLBase implements Runnable {
	private IProject project;
	private Servlet servlet;
	private ServletMapping servletMapping;
	private String newServletName, newServletClass;
	private List<String> listOfMappings;

	public UpdateWebXMLForJavaEE(final IProject project, Servlet servlet, ServletMapping servletMapping, String newServletName, String newServletClass, List<String> listOfMappings) {
		this.project = project;
		this.servlet = servlet;
		this.servletMapping = servletMapping;
		this.newServletName = newServletName;
		this.newServletClass = newServletClass;
		this.listOfMappings = listOfMappings;
	}

	public void run() {
		WebApp webApp = (WebApp) ModelProviderManager.getModelProvider(project).getModelObject();
  	    JAXRSJEEUtils.createOrUpdateServletRef(webApp, this.newServletName, this.newServletClass, this.servlet);
		
		JAXRSJEEUtils.updateURLMappings(webApp, listOfMappings, servlet);
	}
}
