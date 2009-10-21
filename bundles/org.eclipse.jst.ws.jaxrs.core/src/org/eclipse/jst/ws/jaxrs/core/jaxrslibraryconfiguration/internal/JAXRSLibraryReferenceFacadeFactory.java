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

import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jst.j2ee.classpathdep.IClasspathDependencyConstants;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;
import org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration.JAXRSLibraryConfigurationHelper;
import org.eclipse.jst.ws.jaxrs.core.jaxrslibraryconfiguration.JAXRSLibraryReference;

/**
 * Factory for producing facade objects for references to the internal EMF JAXRS
 * Library classes
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
