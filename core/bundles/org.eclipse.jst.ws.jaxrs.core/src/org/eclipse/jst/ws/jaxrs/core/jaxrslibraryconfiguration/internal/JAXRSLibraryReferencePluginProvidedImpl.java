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
package org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration.internal;

import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryInternalReference;

/**
 *
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public class JAXRSLibraryReferencePluginProvidedImpl extends
		AbstractJAXRSLibraryReferenceImpl implements
		JAXRSLibraryReferencePluginProvided {

	/**
	 * Constructor
	 * 
	 * @param libRef
	 * @param isDeployed
	 */
	public JAXRSLibraryReferencePluginProvidedImpl(
			JAXRSLibraryInternalReference libRef, boolean isDeployed) {
		super(libRef, isDeployed);
	}

	public String getPluginId() {
		if (getLibrary() != null)
			return ((PluginProvidedJAXRSLibrary) getLibrary()).getPluginID();

		return null;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("PluginProvided: (");
		buf.append(super.toString());

		return buf.toString();
	}
}
