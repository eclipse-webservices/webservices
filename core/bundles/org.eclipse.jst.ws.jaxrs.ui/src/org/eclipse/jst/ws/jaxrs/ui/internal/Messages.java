/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
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
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20091106   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 * 20100303   291954 kchong@ca.ibm.com - Keith Chong, JAX-RS: Implement JAX-RS Facet
 * 20100413   307552 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS and Java EE 6 setup is incorrect
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.ui.internal;

import org.eclipse.osgi.util.NLS;

/**
 * String resource handler.
 * 
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.jaxrs.ui.internal.messages"; //$NON-NLS-1$

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String JAXRSFacetInstallPage_title;
	public static String JAXRSFacetInstallPage_description;
	public static String JAXRSFacetInstallPage_JAXRSServletLabel;
	public static String JAXRSFacetInstallPage_JAXRSImplementationLibrariesFrame;
	public static String JAXRSFacetInstallPage_Add2;
	public static String JAXRSFacetInstallPage_JAXRSServletNameLabel;
	public static String JAXRSFacetInstallPage_JAXRSServletClassNameLabel;
	public static String JAXRSFacetInstallPage_JAXRSURLMappingLabel;
	public static String JAXRSFacetInstallPage_PatternDialogTitle;
	public static String JAXRSFacetInstallPage_PatternDialogDesc;
	public static String JAXRSFacetInstallPage_Remove;
	public static String JAXRSFacetInstallPage_PatternEmptyMsg;
	public static String JAXRSFacetInstallPage_PatternSpecifiedMsg;
	public static String JAXRSFacetInstallPage_ErrorNoWebAppDataModel;
	public static String JAXRSFacetInstallPage_UpdateDD;
	
	public static String JAXRSLibraryConfigControl_IncludeGroupLabel;
	public static String JAXRSLibraryConfigControl_DeployButtonLabel;
	public static String JAXRSLibraryConfigControl_DeployJAR;
	public static String JAXRSLibraryConfigControl_SharedLibButtonLabel;
	public static String JAXRSLibraryConfigControl_TooltipIncludeAsSharedLib;

}
