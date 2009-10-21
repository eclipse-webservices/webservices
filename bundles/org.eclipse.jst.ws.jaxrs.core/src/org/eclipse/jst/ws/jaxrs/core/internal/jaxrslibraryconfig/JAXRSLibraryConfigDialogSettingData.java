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
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig;

/**
 * To construct implementation library from sticky values in DialogSettings as
 * saved libraries.
 * 
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
