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
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class UpdateWebXMLForJ2EE extends UpdateWebXMLBase implements Runnable {
	private IProject project;
	private IDataModel config;

	public UpdateWebXMLForJ2EE(IProject project, final IDataModel config) {
		this.project = project;
		this.config = config;
	}

	public void run() {
		org.eclipse.jst.j2ee.webapplication.WebApp webApp = (org.eclipse.jst.j2ee.webapplication.WebApp) ModelProviderManager
				.getModelProvider(project).getModelObject();
		// create or update servlet ref
		org.eclipse.jst.j2ee.webapplication.Servlet servlet = JAXRSJ2EEUtils
				.findJAXRSServlet(webApp);// check to see
		// if already
		// present

		servlet = JAXRSJ2EEUtils.createOrUpdateServletRef(webApp, config,
				servlet);

		// init mappings
		List<String> listOfMappings = getServletMappings(config);
		JAXRSJ2EEUtils.setUpURLMappings(webApp, listOfMappings, servlet);

	}
}
