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
 *  20100420   309846 ericdp@ca.ibm.com - Eric D. Peters, Remove dead code related to e.p. pluginProvidedJaxrsLibraries
 *******************************************************************************/package org.eclipse.jst.ws.jaxrs.core.internal;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
import org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration.internal.JAXRSLibraryConfigurationHelper;

/**
 * JAXRS Library classpath container
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public class JAXRSLibraryClasspathContainer implements IClasspathContainer {
	private static final String IMPL_DESC = Messages.JAXRSLibraryClasspathContainer_IMPL_LIBRARY;

	private JAXRSLibrary lib;

	/**
	 * @param lib
	 */
	public JAXRSLibraryClasspathContainer(JAXRSLibrary lib) {
		this.lib = lib;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IClasspathContainer#getClasspathEntries()
	 */
	public IClasspathEntry[] getClasspathEntries() {
		return JAXRSLibraryRegistryUtil.getInstance().getClasspathEntries(lib);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IClasspathContainer#getDescription()
	 */
	public String getDescription() {
		StringBuffer buf = new StringBuffer(lib.getLabel());
		buf.append(" "); //$NON-NLS-1$
		buf.append(IMPL_DESC);

		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IClasspathContainer#getKind()
	 */
	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.IClasspathContainer#getPath()
	 */
	public IPath getPath() {
		return new Path(
				JAXRSLibraryConfigurationHelper.JAXRS_LIBRARY_CP_CONTAINER_ID)
				.append(this.lib.getID());
	}

}
