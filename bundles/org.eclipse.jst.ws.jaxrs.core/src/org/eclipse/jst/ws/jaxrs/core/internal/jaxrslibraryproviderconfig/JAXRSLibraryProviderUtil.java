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
 * 20100413   307552 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS and Java EE 6 setup is incorrect
 * 20100618   307059 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS properties page- fields empty or incorrect
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryproviderconfig;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.jst.ws.jaxrs.core.internal.project.facet.IJAXRSFacetInstallDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * A singleton maintains lists of Library Providers
 * 
 */
public class JAXRSLibraryProviderUtil {
	private static JAXRSLibraryProviderUtil instance = null;

	private static List<JAXRSLibraryProvider> libraryProviders = new Vector<JAXRSLibraryProvider>();

	private static final String JAXRS_LIBRARY_PROVIDER_EXT_PT = "jaxrsLibraryProvider"; 


	/**
	 * Private constructor
	 */
	private JAXRSLibraryProviderUtil() {
		// nothing to do
	}
	
	public static boolean isUpdateDDCheckBoxSelectedByDefault(String libraryID) {
		if (libraryID == null)
			return false;

		JAXRSLibraryProviderUtil.getInstance();
		java.util.List<JAXRSLibraryProvider> libraryProviders = getLibraryProviders();

		Iterator<JAXRSLibraryProvider> libraryProvidersIterator = libraryProviders
				.iterator();
		while (libraryProvidersIterator.hasNext()) {
			JAXRSLibraryProvider thisLibraryProvider = libraryProvidersIterator
					.next();
			if (libraryID.equals(thisLibraryProvider.getLibraryProviderID())) {
				return thisLibraryProvider.getUpdateDDCheckBoxSelected();
			}

		}

		return false;
	}

	public static boolean isUpdateDDCheckBoxSupportAvailable(String libraryID) {
		if (libraryID == null || libraryID.length() == 0)
			return false;

		JAXRSLibraryProviderUtil.getInstance();
		java.util.List<JAXRSLibraryProvider> libraryProviders = getLibraryProviders();

		Iterator<JAXRSLibraryProvider> libraryProvidersIterator = libraryProviders
				.iterator();
		while (libraryProvidersIterator.hasNext()) {
			JAXRSLibraryProvider thisLibraryProvider = libraryProvidersIterator
					.next();
			if (libraryID.equals(thisLibraryProvider.getLibraryProviderID())) {
				if (thisLibraryProvider.getShowUpdateDDCheckBox()) {
					return true;
				}
			}

		}

		return false;
	}
	public static boolean servletClassNameHasLibraryProvider(String servletClassName) {
		if (servletClassName == null)
			return false;

		JAXRSLibraryProviderUtil.getInstance();
		java.util.List<JAXRSLibraryProvider> libraryProviders = getLibraryProviders();

		Iterator<JAXRSLibraryProvider> libraryProvidersIterator = libraryProviders
				.iterator();
		while (libraryProvidersIterator.hasNext()) {
			JAXRSLibraryProvider thisLibraryProvider = libraryProvidersIterator
					.next();
			if (servletClassName.equals(thisLibraryProvider.getServletClassName())) {
					return true;
			}

		}

		return false;
	}	
	public static String getServletClassName(String libraryID) {
		String toReturn = "";
		if (libraryID == null || libraryID.length() == 0)
			return toReturn;

		JAXRSLibraryProviderUtil.getInstance();
		java.util.List<JAXRSLibraryProvider> libraryProviders = getLibraryProviders();

		Iterator<JAXRSLibraryProvider> libraryProvidersIterator = libraryProviders
				.iterator();
		while (libraryProvidersIterator.hasNext()) {
			JAXRSLibraryProvider thisLibraryProvider = libraryProvidersIterator
					.next();
			if (libraryID.equals(thisLibraryProvider.getLibraryProviderID())) {
					return thisLibraryProvider.getServletClassName() != null ? thisLibraryProvider.getServletClassName() : toReturn;
			}

		}

		return toReturn;
	}

	/**
	 * Return the singleton instance of JAXRSLibraryProviderUtil.
	 * 
	 * @return JAXRSLibraryProviderUtil
	 */
	public synchronized static JAXRSLibraryProviderUtil getInstance() {
		if (instance == null) {
			instance = new JAXRSLibraryProviderUtil();
			instance.loadLibraryProvidersExtensions();
		}
		return instance;
	}

	/**
	 * Creates jax-rs library provider items from extension points.
	 */
	private void loadLibraryProvidersExtensions() {
		try {
			IExtensionPoint point = Platform.getExtensionRegistry()
					.getExtensionPoint(JAXRSCorePlugin.PLUGIN_ID, JAXRS_LIBRARY_PROVIDER_EXT_PT);
			IExtension[] extensions = point.getExtensions();
			for (int i = 0; i < extensions.length; i++) {
				IExtension ext = extensions[i];
				for (int j = 0; j < ext.getConfigurationElements().length; j++) {
					JAXRSLibraryProviderCreationHelper newLibCreator = new JAXRSLibraryProviderCreationHelper(
							ext.getConfigurationElements()[j]);
					JAXRSLibraryProvider newLibraryProvider = newLibCreator.create();

					if (newLibraryProvider != null) 
						libraryProviders.add(newLibraryProvider);
				}
			}
		} catch (InvalidRegistryObjectException e) {
			JAXRSCorePlugin.log(IStatus.ERROR,
					Messages.JAXRSLibraryProvider_ErrorLoadingFromExtPt, e);
		}
	}

	public static List<JAXRSLibraryProvider> getLibraryProviders() {
		return libraryProviders;
	}

}
