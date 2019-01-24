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
 * 20100420   309846 ericdp@ca.ibm.com - Eric D. Peters, Remove dead code related to e.p. pluginProvidedJaxrsLibraries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.jaxrslibraryregistry.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryFactory;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;
import org.eclipse.osgi.util.NLS;

/**
 * Helper class used to create plugin-rovided JAXRS Libraries from the
 * <code>org.eclipse.jst.ws.jaxrs.core.pluginProvidedJaxrsLibraries</code>
 * extension-point. <br>
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public final class PluginProvidedJAXRSLibraryCreationHelper2 {
	private PluginProvidedJAXRSLibrary newLib;
	private IConfigurationElement config_element;
	private String relativeDestLocation = "WEB-INF/lib"; //$NON-NLS-1$

	/**
	 * Key of the 'name' attribute of the extension point.
	 */
	public final static String NAME = "name"; //$NON-NLS-1$
	/**
	 * Key of the 'archiveFilesDelegate' attribute of the extension point.
	 */
	public final static String DELEGATE = "archiveFilesDelegate"; //$NON-NLS-1$
	/**
	 * Key of the 'label' attribute of the extension point.
	 */
	public final static String LABEL = "label"; //$NON-NLS-1$

	/**
	 * Creates an instance with the specified IConfigurationElement instance.
	 * 
	 * @param JAXRSLibrary
	 *            IConfigurationElement instance
	 */
	public PluginProvidedJAXRSLibraryCreationHelper2(
			IConfigurationElement JAXRSLibrary) {
		this.config_element = JAXRSLibrary;
	}

	/**
	 * Add a jar file to the library
	 * 
	 * @param pluginRootRelativePath
	 */
	@SuppressWarnings("unchecked")
	public void addArchiveFile(String pluginRootRelativePath) {
		ArchiveFile jar = createArchiveFile(pluginRootRelativePath);
		if (!newLib.containsArchiveFile(jar.getSourceLocation()))
			newLib.getArchiveFiles().add(jar);
	}
	
	@SuppressWarnings("unchecked")
	public void addArchiveFileFromFullPath(String path) throws Exception {
		ArchiveFile jar = createArchiveFileFromFullPath(path);
		if (!newLib.containsArchiveFile(jar.getSourceLocation()))
			newLib.getArchiveFiles().add(jar);
	}
	
	private ArchiveFile createArchiveFileFromFullPath(String path) {
		ArchiveFile file = JAXRSLibraryRegistryFactory.eINSTANCE
				.createArchiveFile();
		file.setRelativeToWorkspace(false);
		file.setSourceLocation(path);
		file.setRelativeDestLocation(relativeDestLocation);
		return file;
	}

	/**
	 * Creates a new PluginProvidedJAXRSLibrary from the JAXRSLibrary extension
	 * point.
	 * 
	 * @return PluginProvidedJAXRSLibrary instance.
	 */
	public JAXRSLibrary create() {
		newLib = JAXRSLibraryRegistryFactory.eINSTANCE
				.createPluginProvidedJAXRSLibrary();
		newLib.setPluginID(getPluginID());
		newLib.setName(config_element.getAttribute(NAME));
		String label = config_element.getAttribute(LABEL);
		if (label != null && label.length() > 0) {
			newLib.setLabel(label);
		}

		try {
			addArchives();
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
	 * @throws CoreException
	 *             on core failure.
	 */
	private void addArchives() throws Exception {
		PluginProvidedJAXRSLibraryArchiveFilesDelegate jarCol = null;

		jarCol = (PluginProvidedJAXRSLibraryArchiveFilesDelegate) config_element
				.createExecutableExtension(DELEGATE);
		if (jarCol != null) {
			jarCol.setCreationHelper(this);
			jarCol.getArchiveFiles();
		}
	}

	/**
	 * Returns ArchiveFile where the location is set relative to the plugin. As
	 * long as the ArchiveFile is on the local machine somewhere, it should be
	 * locatable.
	 * 
	 * @param relativePathFileName
	 *            Relative location of the ArchiveFile
	 * @return ArchiveFile instance.
	 */
	private ArchiveFile createArchiveFile(String pluginRootRelativePath) {
		ArchiveFile file = JAXRSLibraryRegistryFactory.eINSTANCE
				.createArchiveFile();
		file.setRelativeToWorkspace(false);
		file.setSourceLocation(pluginRootRelativePath);
		file.setRelativeDestLocation(relativeDestLocation);
		return file;
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
