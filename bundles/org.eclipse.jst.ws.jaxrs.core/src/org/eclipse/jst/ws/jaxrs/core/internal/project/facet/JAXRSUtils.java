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
 * 20091021   291954 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS: Implement JAX-RS Facet
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.project.facet;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
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
	public static final String JAXRS_SERVLET_CLASS = "com.mycompany.web.rest.RestServlet"; //$NON-NLS-1$

	/**
	 * Default URL mapping to jaxrs servlet
	 */
	public static final String JAXRS_DEFAULT_URL_MAPPING = "/jaxrs/*"; //$NON-NLS-1$

	/**
	 * the key for implementation libraries in persistent properties 
	 */
	public static final String PP_JAXRS_IMPLEMENTATION_LIBRARIES = "jaxrs.implementation.libraries"; //$NON-NLS-1$

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
		if (className == null || className.trim().equals("")) //$NON-NLS-1$
			className = JAXRS_SERVLET_CLASS;
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
}
