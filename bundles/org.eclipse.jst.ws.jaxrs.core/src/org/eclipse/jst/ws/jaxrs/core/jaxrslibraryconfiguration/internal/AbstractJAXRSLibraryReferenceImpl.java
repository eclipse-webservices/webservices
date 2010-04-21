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
 * 20100420   309846 ericdp@ca.ibm.com - Eric D. Peters, Remove dead code related to e.p. pluginProvidedJaxrsLibraries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration.internal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryInternalReference;
/* 
* @deprecated
* 
* <p>
* <b>Provisional API - subject to change - do not use</b>
* </p>
*/
public abstract class AbstractJAXRSLibraryReferenceImpl implements
		JAXRSLibraryReference {

	/**
	 * The
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryInternalReference}
	 * being wrapped
	 */
	protected JAXRSLibraryInternalReference libRef;
	private String _id;
	private String _label;
	private boolean _isDeloyed;

	/**
	 * Constructor for "virtual" JAXRS Library References like "ServerSupplied"
	 * 
	 * @param id
	 * @param label
	 * @param isImplementation
	 */
	public AbstractJAXRSLibraryReferenceImpl(String id, String label) {
		_id = id;
		_label = label;
	}

	/**
	 * Constructor non-virtual library references
	 * 
	 * @param libRef
	 * @param isDeployed
	 */
	public AbstractJAXRSLibraryReferenceImpl(
			JAXRSLibraryInternalReference libRef, boolean isDeployed) {
		this.libRef = libRef;
		_isDeloyed = isDeployed;
	}

	public String getId() {
		if (libRef != null)
			return libRef.getID();

		return _id;
	}

	public String getLabel() {
		if (libRef != null)
			return libRef.getLabel();

		return _label;
	}

	public boolean isDeployed() {
		return _isDeloyed;
	}

	/**
	 * @return the JAXRSLibrary underpinning the reference. May be null if the
	 *         library is missing or cannot be resolved from the registry.
	 */
	protected JAXRSLibrary getLibrary() {
		return libRef.getLibrary();
	}

	@SuppressWarnings("unchecked")
	public Collection<IClasspathEntry> getJars() {
		Set<IClasspathEntry> results = new HashSet<IClasspathEntry>();
		if (getLibrary() != null) {
			List jars = getLibrary().getArchiveFiles();
			for (Iterator it = jars.iterator(); it.hasNext();) {
				ArchiveFile jar = (ArchiveFile) it.next();
				String path = jar.getResolvedSourceLocation();
				results.add(JavaCore
						.newLibraryEntry(new Path(path), null, null));
			}
		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jst.ws.jaxrs.core.JAXRSLibraryconfiguration.JAXRSLibraryReference
	 * #getName()
	 */
	public String getName() {
		if (getLibrary() != null) {
			return getLibrary().getName();
		}
		return getId();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("id: ");
		buf.append(getId());
		buf.append(", label: ");
		buf.append(getLabel());
		buf.append(", isDeployed: ");
		buf.append(isDeployed());

		return buf.toString();
	}
}
