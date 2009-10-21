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
package org.eclipse.jst.ws.jaxrs.core.jaxrslibraryregistry;

import java.util.Collection;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryFactory;
import org.osgi.framework.Bundle;

/**
 * Abstract delegate class used by jaxrsLibraries ext-pt to return collection of
 * ArchiveFiles to PluginProvidedJAXRSLibraryCreationHelper.
 * 
 * Caller must first setConfigurationElement(IConfigurationElement element)
 * before a call to getArchiveFiles().
 * 
 * <p>
 * <b>Provisional API - subject to change</b>
 * </p>
 * 
 * @deprecated along with the jaxrslibraries extension-point
 */
public abstract class JAXRSLibraryArchiveFilesDelegate {
	private IConfigurationElement extPtElement;
	private String libID = null;
	private IPath pluginPath = null;
	private String pluginID;
	private String relativeDestLocation = "WEB-INF/lib"; //$NON-NLS-1$

	/**
	 * Returns a Collection of
	 * <code>org.eclipse.jst.ws.jaxrs.core.internal.JAXRSLibraryregistry.ArchiveFile</code>
	 * instances.
	 * 
	 * @return A Collection of
	 *         <code>org.eclipse.jst.ws.jaxrs.core.internal.JAXRSLibraryregistry.ArchiveFile</code>
	 *         instances
	 */
	@SuppressWarnings("unchecked")
	public abstract Collection getArchiveFiles();

	/**
	 * Sets the IConfigurationElement instance to be subsequently used to get
	 * the plugin ID and the name defined for the JAXRS Library.
	 * 
	 * @param extPtElement
	 *            IConfigurationElement instance
	 */
	public final void setConfigurationElement(IConfigurationElement extPtElement) {
		this.extPtElement = extPtElement;
	}

	/**
	 * Constructs an instance.
	 */
	public JAXRSLibraryArchiveFilesDelegate() {
		super();
	}

	/**
	 * Sets the relative destination location subsequently used to set the
	 * corresponding property on each created ArchiveFile.
	 * 
	 * @param relPath
	 *            Relative destination location for ArchiveFile instances
	 */
	protected void setRelativeDestinationLocation(String relPath) {
		relativeDestLocation = relPath;
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
	protected ArchiveFile createArchiveFile(String relativePathFileName) {
		ArchiveFile file = JAXRSLibraryRegistryFactory.eINSTANCE
				.createArchiveFile();
		file.setRelativeToWorkspace(false);
		file.setSourceLocation(relativePathFileName);
		file.setRelativeDestLocation(relativeDestLocation);
		return file;
	}

	/**
	 * Returns the JAXRSLibrary ID as set on the extension point.
	 * 
	 * @return JAXRSLibrary ID
	 */
	protected String getLibID() {
		if (libID == null) {
			StringBuffer buf = new StringBuffer(getPluginID());
			buf
					.append("/").append(extPtElement.getAttribute(PluginProvidedJAXRSLibraryCreationHelper.NAME)); //$NON-NLS-1$
			libID = buf.toString();
		}
		return libID;
	}

	/**
	 * Returns the plugin's path.
	 * 
	 * @return The plugin's path.
	 */
	protected IPath getPluginPath() {
		if (pluginPath == null) {
			Bundle bundle = Platform.getBundle(getPluginID());
			pluginPath = new Path(bundle.getLocation());
		}
		return pluginPath;
	}

	/**
	 * Returns the plugin's ID.
	 * 
	 * @return The plugin's ID.
	 */
	private String getPluginID() {
		if (pluginID == null) {
			pluginID = extPtElement.getDeclaringExtension().getContributor()
					.getName();
		}
		return pluginID;
	}

}
