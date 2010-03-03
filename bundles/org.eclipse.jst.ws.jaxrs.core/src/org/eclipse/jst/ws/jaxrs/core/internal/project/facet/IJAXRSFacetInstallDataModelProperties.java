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
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20091106   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetInstallDataModelProperties;

/**
 * Defines properties used by the JAXRS facet install data model.
 * 
 */
public interface IJAXRSFacetInstallDataModelProperties extends
		IFacetInstallDataModelProperties {

	public static final String ADD_TO_EAR = "IJAXRSFacetInstallDataModelProperties.ADD_TO_EAR"; //$NON-NLS-1$

	public static final String SHAREDLIBRARY = "IJAXRSFacetInstallDataModelProperties.SHAREDLIBRARY"; //$NON-NLS-1$

	public static final String EARPROJECT_NAME = "IJAXRSFacetInstallDataModelProperties.EARPROJECT_NAME"; //$NON-NLS-1$

	public static final String WEBPROJECT_NAME = "IJAXRSFacetInstallDataModelProperties.WEBPROJECT_NAME"; //$NON-NLS-1$

	public static final String TARGETRUNTIME = "IJAXRSFacetInstallDataModelProperties.TARGETRUNTIME"; //$NON-NLS-1$

	public static final String SERVLET_NAME = "IJAXRSFacetInstallDataModelProperties.SERVLET_NAME"; //$NON-NLS-1$

	public static final String SERVLET_CLASSNAME = "IJAXRSFacetInstallDataModelProperties.SERVLET_CLASSNAME"; //$NON-NLS-1$

	public static final String SERVLET_URL_PATTERNS = "IJAXRSFacetInstallDataModelProperties.SERVLET_URL_PATTERNS"; //$NON-NLS-1$

	public static final String WEBCONTENT_DIR = "IJAXRSFacetInstallDataModelProperties.WEBCONTENT_DIR"; //$NON-NLS-1$

	public static final String LIBRARY_PROVIDER_DELEGATE = "IJAXRSFacetInstallDataModelProperties.LIBRARY_PROVIDER_DELEGATE"; //$NON-NLS-1$
	
	public static final String DEPLOY_IMPLEMENTATION = "IJAXRSFacetInstallDataModelProperties.DEPLOY_IMPLEMENTATION"; //$NON-NLS-1$

}
