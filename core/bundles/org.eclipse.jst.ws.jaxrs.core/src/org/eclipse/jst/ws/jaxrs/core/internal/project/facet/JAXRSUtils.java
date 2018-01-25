/*******************************************************************************
 * Copyright (c) 2009, 2015 IBM Corporation and others.
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
 * 20100325   307059 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS properties page- fields empty or incorrect
 * 20100408   308565 kchong@ca.ibm.com - Keith Chong, JAX-RS: Servlet name and class not updated
 * 20100618   307059 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS properties page- fields empty or incorrect
 * 20150325   463126 jgwest@ca.ibm.com - Jonathan West,  JAX-RS Facet Install Page servlet-class field validation is too strict 
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryproviderconfig.JAXRSLibraryProviderUtil;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * 
 */
public abstract class JAXRSUtils {
	/**
	 * The default name for the JAX-RS servlet
	 */
	public static final String JAXRS_DEFAULT_SERVLET_NAME = "JAX-RS Servlet"; //$NON-NLS-1$
	/**
	 * The default name of the JAX-RS servlet class
	 */
	public static final String JAXRS_SERVLET_CLASS = ""; //$NON-NLS-1$

	/**
	 * Default URL mapping to jaxrs servlet
	 */
	public static final String JAXRS_DEFAULT_URL_MAPPING = "/jaxrs/*"; //$NON-NLS-1$

	/**
	 * the key for implementation libraries in persistent properties 
	 */
	public static final String PP_JAXRS_IMPLEMENTATION_LIBRARIES = "jaxrs.implementation.libraries"; //$NON-NLS-1$
	
	public static final String JAXRS_SERVLET_IDENTIFIER = "JAX-RS Tools Generated"; //$NON-NLS-1$
	public static final String JAXRS_SERVLET_IDENTIFIER_DESCRIPTION = "JAX-RS Tools Generated - Do not modify"; //$NON-NLS-1$

	/**
	 * @param config
	 * @return servlet display name to use from wizard data model
	 */
	protected static String getDisplayName(IDataModel config) {
		String displayName = config
				.getStringProperty(IJAXRSFacetInstallDataModelProperties.SERVLET_NAME);
		if (displayName == null || displayName.trim().equals("")) //$NON-NLS-1$
			displayName = JAXRS_DEFAULT_SERVLET_NAME;
		return displayName.trim();
	}

	/**
	 * @param config
	 * @return servlet display name to use from wizard data model
	 */
	protected static String getServletClassname(IDataModel config) {
		String className = config
				.getStringProperty(IJAXRSFacetInstallDataModelProperties.SERVLET_CLASSNAME);

		if (className == null || className.trim().equals("")) { //$NON-NLS-1$
			className = JAXRS_SERVLET_CLASS;
		}
		
		return className.trim();
	}

	/**
	 * @param webProject
	 * @return IModelProvider
	 */
	public static IModelProvider getModelProvider(IProject webProject) {
		IModelProvider provider = ModelProviderManager
				.getModelProvider(webProject);
		Object webAppObj = provider.getModelObject();
		if (webAppObj == null) {
			return null;
		}
		return provider;
	}

	/**
	 * The result of a servlet mapping search
	 * 
	 */
	public static class MappingSearchResult {
		private final String _extensionMapping; // may be null;
		private final String _prefixMapping; // may be null

		MappingSearchResult(final String extensionMapping,
				final String prefixMapping) {
			_extensionMapping = extensionMapping;
			_prefixMapping = prefixMapping;
		}

		/**
		 * @return true if the search yielded a valid result
		 */
		public boolean isResult() {
			return _extensionMapping != null || _prefixMapping != null;
		}

		/**
		 * @return the first extension mapping matching search criteria or null
		 *         if none
		 */
		public final String getExtensionMapping() {
			return _extensionMapping;
		}

		/**
		 * @return the first prefix mapping matching search criteria or null if
		 *         none
		 */
		public final String getPrefixMapping() {
			return _prefixMapping;
		}
	}

	static String getSavedServletClassName(String libraryProviderID) {
		IDialogSettings jaxrsUISettings = JAXRSCorePlugin.getDefault()
				.getJaxrsUISettings();
		if (jaxrsUISettings != null) {
			
			String JAXRSUISettingsRoot = "org.eclipse.jst.ws.jaxrs.ui" + ".jaxrsFacetInstall"; //$NON-NLS-1$
			IDialogSettings root = jaxrsUISettings
					.getSection(JAXRSUISettingsRoot);

			if (root != null) {
				String toReturn = root.get(libraryProviderID + "servletClassname"); //$NON-NLS-1$
				if (toReturn == null)
					toReturn = root.get("servletClassname"); //$NON-NLS-1$
				return toReturn;
			}
		}
		return null;
	}
	static boolean facetKnowsServletClassName(String servletClassName) {
		return JAXRSLibraryProviderUtil.servletClassNameHasLibraryProvider(servletClassName);
	}
	/** 
	* @deprecated
	* use org.eclipse.jst.ws.jaxrs.core.internal.project.facet.JAXRSUtils.getSavedservletName()
	*/
	static String getSavedServletName() {
		IDialogSettings jaxrsUISettings = JAXRSCorePlugin.getDefault()
				.getJaxrsUISettings();
		if (jaxrsUISettings != null) {
			String JAXRSUISettingsRoot = "org.eclipse.jst.ws.jaxrs.ui" + ".jaxrsFacetInstall"; //$NON-NLS-1$
			IDialogSettings root = jaxrsUISettings
					.getSection(JAXRSUISettingsRoot);
	
			if (root != null)
				return root.get("servletClassname");
		}
		return null;
	}
	
	static String getSavedservletName() {
		IDialogSettings jaxrsUISettings = JAXRSCorePlugin.getDefault()
				.getJaxrsUISettings();
		if (jaxrsUISettings != null) {
			String JAXRSUISettingsRoot = "org.eclipse.jst.ws.jaxrs.ui" + ".jaxrsFacetInstall"; //$NON-NLS-1$
			IDialogSettings root = jaxrsUISettings
					.getSection(JAXRSUISettingsRoot);
	
			if (root != null)
				return root.get("servletName"); //$NON-NLS-1$
		}
		return null;
	}
}
