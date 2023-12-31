/*******************************************************************************
 * Copyright (c) 2009, 2013 IBM Corporation and others.
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
 * 20100413   307552 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS and Java EE 6 setup is incorrect
 * 20100519   313576 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS tools- validation problems
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal;
import org.eclipse.osgi.util.NLS;

/**
 * String resource handler.
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.jst.ws.jaxrs.core.internal.messages"; //$NON-NLS-1$

	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetInstallDataModelProvider_ClientImplValidationMsg;
	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetInstallDataModelProvider_DupeJarValidation;

	/**
	 * see messages.properties
	 */
	public static String JAXRSLibrariesContainerInitializer_missing_library;

	/**
	 * see messages.properties
	 */
	public static String JAXRSLibraryClasspathContainer_IMPL_LIBRARY;

	/**
	 * see messages.properties
	 */
	public static String JAXRSLibraryConfigModel_Null_Data_Source;

	/**
	 * see messages.properties
	 */
	public static String JAXRSLibraryConfigPersistData_SAVED_COMPLIB_NOT_FOUND;

	/**
	 * see messages.properties
	 */
	public static String JAXRSLibraryConfigPersistData_SAVED_IMPLLIB_NOT_FOUND;

	/**
	 * see messages.properties
	 */
	public static String JAXRSLibraryRegistry_ErrorCreatingURL;
	/**
	 * see messages.properties
	 */
	public static String JAXRSLibraryRegistry_ErrorSaving;
	/**
	 * see messages.properties
	 */
	public static String JAXRSLibraryRegistry_DEFAULT_IMPL_LABEL;
	/**
	 * see messages.properties
	 */
	public static String JAXRSLibraryRegistry_ErrorLoadingFromExtPt;

	/**
	 * see messages.properties
	 */
	public static String JAXRSSharedLibraryConfigurator_ErrorLoadingFromExtPt;
	/**
	 * see messages.properties
	 */
	public static String JAXRSLibraryProvider_ErrorLoadingFromExtPt;
	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetInstallDataModelProvider_ValidateServletName;
	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetInstallDataModelProvider_ValidateServletClassName;
	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetInstallDataModelProvider_ValidateJAXRSImpl;
	/**
	 * see messages.properties
	 */
	public static String ArchiveFileImpl_CannotCopyFile;
	/**
	 * see messages.properties
	 */
	public static String ArchiveFileImpl_CannotCloseFile;
	/**
	 * see messages.properties
	 */
	public static String ArchiveFileImpl_CannotLocatePluginRelativeFile;

	/**
	 * see messages.properties
	 */
	public static String PluginProvidedJAXRSLibraryCreationHelper_ErrorCreating;

	/**
	 * see messages.properties
	 */
	public static String PluginProvidedSharedLibraryConfiguratorCreationHelper_ErrorCreating;

	/**
	 * see messages.properties
	 */
	public static String JAXRSLibraryProviderCreationHelper_ErrorCreating;

	/**
	 * see messages.properties
	 */
	public static String PluginProvidedJAXRSLibraryCreationHelper_ErrorMultipleDefinition;

	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetInstallDelegate_InternalErr;
	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetInstallDelegate_ConfigErr;
	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetInstallDelegate_NonUpdateableWebXML;
	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetUninstallDelegate_ConfigErr;
	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetUninstallDelegate_NonUpdateableWebXML;
	
	/**
	 * see messages.properties
	 */
	public static String JAXRSFacetUninstallDelegate_ProjectErr;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
