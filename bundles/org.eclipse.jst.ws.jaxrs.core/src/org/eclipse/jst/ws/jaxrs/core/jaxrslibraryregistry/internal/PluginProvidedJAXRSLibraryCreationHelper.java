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
package org.eclipse.jst.ws.jaxrs.core.jaxrslibraryregistry.internal;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryFactory;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;
import org.eclipse.osgi.util.NLS;

/**
 * Helper class used to create JAXRS Libraries from the
 * <code>org.eclipse.jst.ws.jaxrs.core.jaxrslibraries</code> extension-point.
 * 
 * <p>
 * <b>Provisional API - subject to change</b>
 * </p>
 * 
 * @deprecated - clients should not use
 */
public final class PluginProvidedJAXRSLibraryCreationHelper {
	private IConfigurationElement config_element;

	/**
	 * Key of the 'name' attribute of the extension point.
	 */
	public final static String NAME = "name"; //$NON-NLS-1$
	/**
	 * Key of the 'isImplementation' attribute of the extension point.
	 */
	public final static String IS_IMPL = "isImplementation"; //$NON-NLS-1$
	/**
	 * Key of the 'archiveFilesDelegate' attribute of the extension point.
	 */
	public final static String DELEGATE = "archiveFilesDelegate"; //$NON-NLS-1$

	/**
	 * Creates an instance with the specified IConfigurationElement instance.
	 * 
	 * @param JAXRSLibrary
	 *            IConfigurationElement instance
	 */
	public PluginProvidedJAXRSLibraryCreationHelper(
			IConfigurationElement JAXRSLibrary) {
		this.config_element = JAXRSLibrary;
	}

	/**
	 * Creates a new PluginProvidedJAXRSLibrary from the JAXRSLibrary extension
	 * point.
	 * 
	 * @return PluginProvidedJAXRSLibrary instance.
	 */
	public JAXRSLibrary create() {
		PluginProvidedJAXRSLibrary newLib = JAXRSLibraryRegistryFactory.eINSTANCE
				.createPluginProvidedJAXRSLibrary();
		newLib.setPluginID(getPluginID());
		newLib.setName(config_element.getAttribute(NAME));
		try {
			addArchives(newLib);
			return newLib;
		} catch (Exception e) {
			JAXRSCorePlugin
					.log(
							e,
							NLS
									.bind(
											Messages.PluginProvidedJAXRSLibraryCreationHelper_ErrorCreating,
											newLib.getName()));
		}
		return null;
	}

	/**
	 * Adds ArchiveFile instances to the specified JAXRSLibrary instance.
	 * 
	 * @param newLib
	 *            JAXRSLibrary instance
	 * @throws InvalidArchiveFilesCreationException
	 *             on attempt to create multiple instances at same location.
	 * @throws CoreException
	 *             on core failure.
	 */
	@SuppressWarnings("unchecked")
	private void addArchives(JAXRSLibrary newLib)
			throws InvalidArchiveFilesCreationException, CoreException {
		JAXRSLibraryArchiveFilesDelegate jarCol = null;
		ArchiveFile jar = null;

		jarCol = (JAXRSLibraryArchiveFilesDelegate) config_element
				.createExecutableExtension(DELEGATE);
		if (jarCol != null) {
			jarCol.setConfigurationElement(config_element);
			Collection jars = jarCol.getArchiveFiles();
			if (jars == null)// TODO: better validation and error handling
				return;
			Iterator it = jars.iterator();
			while (it.hasNext()) {
				Object aJar = it.next();
				if (aJar instanceof ArchiveFile) {// for now check to see
													// ArchiveFiles were being
													// returned
					jar = (ArchiveFile) aJar;
					if (!newLib.containsArchiveFile(jar.getSourceLocation()))
						newLib.getArchiveFiles().add(jar);
				} else {
					throw new InvalidArchiveFilesCreationException(
							NLS
									.bind(
											Messages.PluginProvidedJAXRSLibraryCreationHelper_ErrorMultipleDefinition,
											jar.getSourceLocation(),
											config_element.getName()));
				}

			}
		}
	}

	/**
	 * Returns the plugin's ID.
	 * 
	 * @return The plugin's ID
	 */
	private String getPluginID() {
		return config_element.getDeclaringExtension().getContributor()
				.getName();
	}
}
