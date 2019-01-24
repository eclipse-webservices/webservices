/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
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
 * 20100413   307552 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS and Java EE 6 setup is incorrect
 * 20100420   309846 ericdp@ca.ibm.com - Eric D. Peters, Remove dead code related to e.p. pluginProvidedJaxrsLibraries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryproviderconfig;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.osgi.util.NLS;

/**
 * Helper class used to create JAXRS Library Providers from the
 * <code>org.eclipse.jst.ws.jaxrs.core.jaxrsLibraryProvider</code>
 * extension-point.
 * 
 */

public final class JAXRSLibraryProviderCreationHelper {
	private JAXRSLibraryProviderImpl newLibraryProvider;
	private IConfigurationElement config_element;
	/**
	 * Key of the 'libraryProviderID' attribute of the extension point.
	 */
	public final static String LibraryProviderID = "libraryProviderID"; //$NON-NLS-1$


	/**
	 * Key of the 'showUpdateDD' attribute of the extension point.
	 */
	public final static String ShowUpdateDD = "showUpdateDD"; //$NON-NLS-1$
	/**
	 * Key of the 'updateDDSelected' attribute of the extension point.
	 */
	public final static String UpdateDDSelected = "updateDDSelected"; //$NON-NLS-1$
	/**
	 * Key of the 'isSelected' attribute of the extension point.
	 */
	public final static String SevletClassName = "sevletClassName"; //$NON-NLS-1$

	/**
	 * Creates an instance with the specified IConfigurationElement instance.
	 * 
	 * @param JAXRSLibraryProvider
	 *            IConfigurationElement instance
	 */
	public JAXRSLibraryProviderCreationHelper(
			IConfigurationElement JAXRSLibraryProvider) {
		this.config_element = JAXRSLibraryProvider;
	}


	/**
	 * Creates a new LibraryProvider from the <code>org.eclipse.jst.ws.jaxrs.core.jaxrsLibraryProvider</code>
	 * extension-point.
	 * 
	 * @return JAXRSLibraryProvider instance.
	 */
	public JAXRSLibraryProvider create() {
		try {
			newLibraryProvider = new JAXRSLibraryProviderImpl();
			newLibraryProvider.setLibraryProviderID(config_element.getAttribute(LibraryProviderID));
			newLibraryProvider.setServletClassName(config_element.getAttribute(SevletClassName));
			newLibraryProvider.setShowUpdateDDCheckBox(Boolean.parseBoolean(config_element
 					.getAttribute(ShowUpdateDD)));
			newLibraryProvider.setUpdateDDCheckBoxSelected(Boolean.parseBoolean(config_element
 					.getAttribute(UpdateDDSelected)));
			return newLibraryProvider;
		} catch (Exception e) {
			JAXRSCorePlugin
					.log(
							e,
							NLS
									.bind(
											Messages.JAXRSLibraryProviderCreationHelper_ErrorCreating,
											newLibraryProvider.getLibraryProviderID()));
		}
		return null;
	}

}
