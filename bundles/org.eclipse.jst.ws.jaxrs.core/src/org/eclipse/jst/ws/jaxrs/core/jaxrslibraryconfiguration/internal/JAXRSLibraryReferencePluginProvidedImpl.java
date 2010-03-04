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
package org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration.internal;

import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryInternalReference;

/**
 *
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
