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
package org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration;

import java.util.Collection;

import org.eclipse.jdt.core.IClasspathEntry;

/**
 * Represents a reference to a JAXRS Library on a project
 * 
 * <p>
 * <b>Provisional API - subject to change</b>
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
