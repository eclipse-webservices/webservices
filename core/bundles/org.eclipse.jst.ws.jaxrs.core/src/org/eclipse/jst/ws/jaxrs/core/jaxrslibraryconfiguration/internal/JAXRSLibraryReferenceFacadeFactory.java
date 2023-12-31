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

import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jst.j2ee.classpathdep.IClasspathDependencyConstants;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;

/**
 * Factory for producing facade objects for references to the internal EMF JAXRS
 * Library classes
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public class JAXRSLibraryReferenceFacadeFactory {
	/**
	 * Returns a JAXRSLibraryReferenceUserSpecified (or
	 * JAXRSLibraryReferenceUserDefined}) or JAXRSLibraryReferencePluginProvided
	 * instance. Will not create a JAXRSLibraryReferenceServerSupplied as there
	 * is no cp entry. Use createServerSuppliedJAXRSLibRef instead.
	 * 
	 * @param classpathEntry
	 * @return an instance of JAXRSLibraryInternalReference. Null will be
	 *         returned if the cpEntry is not a JAXRS Library reference.
	 */
	public static JAXRSLibraryReference create(
			final IClasspathEntry classpathEntry) {
		if (JAXRSLibraryConfigurationHelper
				.isJAXRSLibraryContainer(classpathEntry)) {
			return createReference(classpathEntry);
		}
		return null;
	}

	/**
	 * @param classpathEntry
	 * @return {@link JAXRSLibraryReference}
	 */
	private static JAXRSLibraryReference createReference(
			final IClasspathEntry classpathEntry) {

		String libID = classpathEntry.getPath().segment(1);
		org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryInternalReference libRef = JAXRSLibraryRegistryUtil
				.getInstance().getJAXRSLibraryReferencebyID(libID);
		if (libRef != null) {
			boolean isDeployed = getJ2EEModuleDependency(classpathEntry);
			if (libRef.getLibrary() instanceof PluginProvidedJAXRSLibrary)
				return new JAXRSLibraryReferencePluginProvidedImpl(libRef,
						isDeployed);

			return new JAXRSLibraryReferenceUserSpecifiedImpl(libRef,
					isDeployed);
		}
		return null;
	}

	private static boolean getJ2EEModuleDependency(
			IClasspathEntry classpathEntry) {
		IClasspathAttribute[] attrs = classpathEntry.getExtraAttributes();
		for (int i = 0; i < attrs.length; i++) {
			IClasspathAttribute attr = attrs[i];
			if (attr
					.getName()
					.equals(
							IClasspathDependencyConstants.CLASSPATH_COMPONENT_DEPENDENCY)) {
				return true;
			}
		}
		return false;
	}
}
