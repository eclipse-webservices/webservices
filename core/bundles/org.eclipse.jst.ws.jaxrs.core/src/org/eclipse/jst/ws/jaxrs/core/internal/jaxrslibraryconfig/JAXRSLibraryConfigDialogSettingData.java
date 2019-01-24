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
 * To construct implementation library from sticky values in DialogSettings as
 * saved libraries.
 * 
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */

public class JAXRSLibraryConfigDialogSettingData implements
		JAXRSLibraryConfiglModelSource {
	/**
	 * Delimintor for parsing a persistent property string.
	 */
	final protected static String SEPARATOR = ":"; //$NON-NLS-1$

	final private JAXRSLibraryRegistryUtil jaxrsLibReg;
	final private boolean dftImplLibDeployFlag;
	final private boolean dftImplLibSharedLibFlag;
	final private boolean sharedLibSupported;
	private JAXRSLibraryInternalReference selJAXRSLibImpl; // lazy initialized

	public JAXRSLibraryConfigDialogSettingData(boolean implLibDeployFlag, boolean implLibSharedLibFlag, boolean sharedLibSupported) {
		this.jaxrsLibReg = JAXRSLibraryRegistryUtil.getInstance();
		this.dftImplLibDeployFlag = implLibDeployFlag;
		this.dftImplLibSharedLibFlag = implLibSharedLibFlag;
		this.sharedLibSupported = sharedLibSupported;
	}

	/**
	 * There is no saved JAXRSImplLibrary per se if initializing from
	 * DialogSettings since default implementation library is always selected
	 * 
	 * A null is returned when there is no default implementation library in
	 * registry.
	 * 
	 * @return selJAXRSLibImpl JAXRSLibraryInternalReference return default
	 *         implementation library with updated deployment flag
	 */
	public JAXRSLibraryInternalReference getJAXRSImplementationLibrary() {
		if (selJAXRSLibImpl == null) {
			// To instansiate a JAXRSLibraryReferenceUserSpecified object from
			// default impl lib as the saved library.
			JAXRSLibraryInternalReference dftImplLib = jaxrsLibReg
					.getDefaultJAXRSImplementationLibrary();
			if (dftImplLib != null) {
				selJAXRSLibImpl = new JAXRSLibraryInternalReference(dftImplLib
						.getLibrary(), true, // selected
						dftImplLibDeployFlag, dftImplLibSharedLibFlag);
				selJAXRSLibImpl.setSharedLibSupported(sharedLibSupported);
			}
			
		}
		return selJAXRSLibImpl;
	}

}
