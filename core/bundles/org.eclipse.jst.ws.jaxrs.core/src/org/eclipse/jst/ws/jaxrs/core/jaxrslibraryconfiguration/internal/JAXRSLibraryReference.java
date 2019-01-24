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

import java.util.Collection;

import org.eclipse.jdt.core.IClasspathEntry;

/**
 * Represents a reference to a JAXRS Library on a project
 * 
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */ 
public abstract interface JAXRSLibraryReference {
	/**
	 * @return id for the library
	 */
	public String getId();

	/**
	 * @return name
	 */
	public String getName();

	/**
	 * @return collection of jars as {@link IClasspathEntry}s
	 */
	public Collection<IClasspathEntry> getJars();

	/**
	 * @return label user sees for this library
	 */
	public String getLabel();

	/**
	 * @return is deployed (marked as J2EE Module Dependency)
	 */
	public boolean isDeployed();
}
