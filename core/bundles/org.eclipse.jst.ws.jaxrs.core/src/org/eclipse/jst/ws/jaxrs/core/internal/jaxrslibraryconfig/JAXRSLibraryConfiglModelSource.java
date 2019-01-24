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
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig;

/**
 * Interface for data source to instanciate a <b>JAXRSLibraryConfigModel</b>
 * object.
 * 
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */

public interface JAXRSLibraryConfiglModelSource {
	/**
	 * Return a saved JAXRS implementation library. Depends on the model source,
	 * it could be sticky values from DialogSettings or project property values.
	 * 
	 * A null could be returned when creating first web project in a new
	 * workspace.
	 * 
	 * @return JAXRSLibraryInternalReference
	 */
	public JAXRSLibraryInternalReference getJAXRSImplementationLibrary();

}
