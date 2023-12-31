/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 * 20100618   307059 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS properties page- fields empty or incorrect
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal.project.facet;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.j2ee.webapplication.Servlet;
import org.eclipse.jst.j2ee.webapplication.ServletMapping;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSJ2EEUtils;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class UpdateWebXMLForJ2EE extends UpdateWebXMLBase implements Runnable {
	private IProject project;
	private Servlet servlet;
	private ServletMapping servletMapping;
	private String newServletName, newServletClass;
	private List<String> listOfMappings;

	public UpdateWebXMLForJ2EE(IProject project, Servlet servlet, ServletMapping servletMapping, String newServletName, String newServletClass, List listOfMappings) {
		this.project = project;
		this.project = project;
		this.servlet = servlet;
		this.servletMapping = servletMapping;
		this.newServletName = newServletName;
		this.newServletClass = newServletClass;
		this.listOfMappings = listOfMappings;
	}

	public void run() {
		org.eclipse.jst.j2ee.webapplication.WebApp webApp = (org.eclipse.jst.j2ee.webapplication.WebApp) ModelProviderManager
				.getModelProvider(project).getModelObject();
		//servlet may not exist yet, but will after call below
		servlet = JAXRSJ2EEUtils.createOrUpdateServletRef(webApp, this.newServletName, this.newServletClass, this.servlet);
		JAXRSJ2EEUtils.updateURLMappings(webApp, listOfMappings, servlet);

	}
}
