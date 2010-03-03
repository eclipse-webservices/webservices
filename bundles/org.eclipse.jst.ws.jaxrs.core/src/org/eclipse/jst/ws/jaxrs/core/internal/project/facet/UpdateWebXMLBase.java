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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public abstract class UpdateWebXMLBase implements Runnable {

	public abstract void run();
	
	/**
	 * @param config
	 * @return list of URL patterns from the datamodel
	 */
	protected List<String> getServletMappings(final IDataModel config) {
		List<String> mappings = new ArrayList<String>();
		String[] patterns = (String[]) config
				.getProperty(IJAXRSFacetInstallDataModelProperties.SERVLET_URL_PATTERNS);
		for (int i = 0; i < patterns.length; i++) {
			String pattern = patterns[i];
			mappings.add(pattern);
		}

		return mappings;
	}
}
