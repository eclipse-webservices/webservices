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
 * 20100407   308401 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet wizard page - Shared-library option should be disabled
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrssharedlibraryconfig;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.osgi.util.NLS;

/**
 * Helper class used to create JAXRS Shared Library Configurators from the
 * <code>org.eclipse.jst.ws.jaxrs.core.jaxrsSharedLibrarySupport</code>
 * extension-point. <br>
 * 
 */
public final class SharedLibraryConfiguratorCreationHelper {
	private SharedLibraryConfiguratorImpl newSharedLibConfigurator;
	private IConfigurationElement config_element;
	/**
	 * Key of the 'name' attribute of the extension point.
	 */
	public final static String NAME = "name"; //$NON-NLS-1$


	/**
	 * Key of the 'runtimeID' attribute of the extension point.
	 */
	public final static String SERVERRUNTIME = "runtimeID"; //$NON-NLS-1$
	/**
	 * Key of the 'jaxrsSharedLibraryConfiguratorDelegate' attribute of the extension point.
	 */
	public final static String DELEGATE = "jaxrsSharedLibraryConfiguratorDelegate"; //$NON-NLS-1$
	/**
	 * Key of the 'isSelected' attribute of the extension point.
	 */
	public final static String SELECTED = "isSelected"; //$NON-NLS-1$

	/**
	 * Creates an instance with the specified IConfigurationElement instance.
	 * 
	 * @param SharedLibraryConfigurator
	 *            IConfigurationElement instance
	 */
	public SharedLibraryConfiguratorCreationHelper(
			IConfigurationElement SharedLibraryConfigurator) {
		this.config_element = SharedLibraryConfigurator;
	}


	/**
	 * Creates a new SharedLibraryConfigurator from the JAXRSSharedLibrarySupport extension
	 * point.
	 * 
	 * @return SharedLibraryConfigurator instance.
	 */
	public SharedLibraryConfigurator create() {
		try {
			newSharedLibConfigurator = new SharedLibraryConfiguratorImpl(config_element);
			newSharedLibConfigurator.setName(config_element.getAttribute(NAME));
			newSharedLibConfigurator.setRuntimeID(config_element.getAttribute(SERVERRUNTIME));
			newSharedLibConfigurator.setSelected(Boolean.parseBoolean(config_element
 					.getAttribute(SELECTED)));
			return newSharedLibConfigurator;
		} catch (Exception e) {
			JAXRSCorePlugin
					.log(
							e,
							NLS
									.bind(
											Messages.PluginProvidedSharedLibraryConfiguratorCreationHelper_ErrorCreating,
											newSharedLibConfigurator.getName()));
		}
		return null;
	}

}
