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
import java.util.HashSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxrs.core.internal.JAXRSCorePlugin;

/**
 * Helper class for adopters needing to deal with JAXRS Library References
 * 
 * <p>
 * <b>Provisional API - subject to change</b>
 * </p>
 * 
 * @deprecated
 * 
 */
public final class JAXRSLibraryConfigurationHelper {
	/**
	 * container id for JAXRS Library Classpath Containers
	 */
	public static final String JAXRS_LIBRARY_CP_CONTAINER_ID = "org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibrarycontainer";

	/**
	 * @param project
	 * @return collection of references
	 */
	public static Collection<JAXRSLibraryReference> getJAXRSLibraryReferences(
			IProject project) {
		Collection<JAXRSLibraryReference> results = new HashSet<JAXRSLibraryReference>();
		IJavaProject jproj = JavaCore.create(project);
		try {
			IClasspathEntry[] entries = jproj.getRawClasspath();
			for (int i = 0; i < entries.length; i++) {
				JAXRSLibraryReference ref = JAXRSLibraryReferenceFacadeFactory
						.create(entries[i]);
				if (ref != null) {
					results.add(ref);
				}
			}
		} catch (JavaModelException e) {
			JAXRSCorePlugin.log(e,
					"Exception occurred calling getJAXRSLibraryReferences for "
							+ project.getName());
		}
		return results;
	}

	/**
	 * @param cpEntry
	 * @return boolean indicating that the classpath entry is a JAXRS Libary
	 *         Classpath Container
	 */
	public static boolean isJAXRSLibraryContainer(IClasspathEntry cpEntry) {
		if (cpEntry.getEntryKind() != IClasspathEntry.CPE_CONTAINER)
			return false;

		IPath path = cpEntry.getPath();
		return path != null && path.segmentCount() == 2
				&& JAXRS_LIBRARY_CP_CONTAINER_ID.equals(path.segment(0));
	}

}
