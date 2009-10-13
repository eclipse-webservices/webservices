/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091013   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.jaxrs.core.internal.IJAXRSCoreConstants;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;

/**
 * Provides a data model used by the JAXRS facet install.
 */
public class JAXRSFacetInstallDataModelProvider extends
		FacetInstallDataModelProvider implements
		IJAXRSFacetInstallDataModelProperties {


	@SuppressWarnings("unchecked")
	public Set<String> getPropertyNames() {
		Set<String> names = super.getPropertyNames();
		return names;
	}

	public Object getDefaultProperty(String propertyName) {
		if (propertyName.equals(FACET_ID)) {
			return IJAXRSCoreConstants.JAXRS_FACET_ID;
		}
		return super.getDefaultProperty(propertyName);

	}

	public IStatus validate(String name) {

		return super.validate(name);
	}

}
