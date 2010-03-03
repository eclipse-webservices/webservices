/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20091106   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100302   304405 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS Facet : support JAX-RS 1.1 (JSR 311)
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;


import org.eclipse.jst.ws.jaxrs.core.internal.IJAXRSCoreConstants;
import org.eclipse.wst.common.project.facet.core.IDefaultVersionProvider;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

/**
 * Provides the default JAX-RS project facet version.
 * Currently always returns "1.1" facet version.   Eventually should be computed from the project's facet context.
 *    
 */
public final class JAXRSFacetDefaultVersionProvider implements IDefaultVersionProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.wst.common.project.facet.core.IDefaultVersionProvider#getDefaultVersion()
	 */
	public IProjectFacetVersion getDefaultVersion() {		
		return ProjectFacetsManager.getProjectFacet(IJAXRSCoreConstants.JAXRS_FACET_ID).getVersion(IJAXRSCoreConstants.FACET_VERSION_1_1);
	}

}
