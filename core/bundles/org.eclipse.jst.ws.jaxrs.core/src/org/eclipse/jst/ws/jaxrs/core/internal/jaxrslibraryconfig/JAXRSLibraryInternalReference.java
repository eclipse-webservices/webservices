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

import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;

/**
 * Attach additional attributes such as selection and deployment to a JAXRS
 * Library when it is referenced by a project.
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public class JAXRSLibraryInternalReference {
	final private JAXRSLibrary jaxrsLib;
	private boolean check4Deploy; // Initialized from default in workspace
	private boolean check4SharedLib; // Initialized from default in workspace
	private boolean selected; // selected for project
	private boolean sharedLibSupported = false;

	public boolean isSharedLibSupported() {
		return sharedLibSupported;
	}

	public void setSharedLibSupported(boolean sharedLibSupported) {
		this.sharedLibSupported = sharedLibSupported;
	}

	/**
	 * Constructor
	 * 
	 * @param jaxrsLib
	 *            JAXRSLibrary instance embedded inside.
	 * @param selected
	 *            boolean true if selected, otherwise, not selected.
	 * @param deploy
	 *            boolean true if needs to be deployed, otherwise, won't be
	 *            deployed.
	 */
	public JAXRSLibraryInternalReference(JAXRSLibrary jaxrsLib,
			boolean selected, boolean deploy, boolean sharedLib) {
		this.jaxrsLib = jaxrsLib;
		this.selected = selected;
		this.check4Deploy = deploy;
		this.check4SharedLib = sharedLib;
	}

	/**
	 * Return the embedded JAXRSLibrary instance.
	 * 
	 * @return jaxrsLib JAXRSLibrary
	 */
	public JAXRSLibrary getLibrary() {
		return jaxrsLib;
	}

	/**
	 * Set the to be deployed flag.
	 * 
	 * @param deploy
	 *            boolean
	 */
	public void setToBeDeployed(final boolean deploy) {
		check4Deploy = deploy;
	}

	/**
	 * Set the to be shared library flag.
	 * 
	 * @param deploy
	 *            boolean
	 */
	public void setToBeSharedLibrary(final boolean sharedLib) {
		check4SharedLib = sharedLib;
	}

	/**
	 * Return true if the JAXRS Library needs to be deployed. Otherwise, return
	 * false.
	 * 
	 * @return boolean
	 */
	public boolean isCheckedToBeDeployed() {
		return check4Deploy;
	}
	/**
	 * Return true if the JAXRS Library needs to be added to the project as a shared library.
	 * Otherwise, return false.
	 * 
	 * @return boolean
	 */
	public boolean isCheckedToBeSharedLibrary() {
		return check4SharedLib;
	}

	/**
	 * Set the selected attribute to a JAXRSLibraryLibraryReference object.
	 * 
	 * @param selected
	 *            boolean
	 */
	public void setSelected(final boolean selected) {
		this.selected = selected;
	}

	/**
	 * Return true if the JAXRS Library is referenced by a project. Otherwise,
	 * return false.
	 * 
	 * @return selected boolean
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * To generate a string that represents the JAXRSLibraryLibraryReference
	 * object for persistence.
	 * 
	 * @return String
	 */
	protected String generatePersistString() {
		return (getID() + JAXRSLibraryConfigProjectData.SPTR_TUPLE
				+ String.valueOf(isSelected())
				+ JAXRSLibraryConfigProjectData.SPTR_TUPLE + String
				.valueOf(isCheckedToBeDeployed()));
	}

	/**
	 * Helper method to return the library ID from the embedded JAXRSLibrary
	 * instance.
	 * 
	 * @return id String
	 */
	public String getID() {
		return jaxrsLib.getID();
	}

	/**
	 * Helper method to return the library name from the embedded JAXRSLibrary
	 * instance.
	 * 
	 * @return name String
	 */
	public String getName() {
		return jaxrsLib.getName();
	}

	/**
	 * Helper method to return the label for the library from the embedded
	 * JAXRSLibrary instance.
	 * 
	 * @return name String
	 */
	public String getLabel() {
		return jaxrsLib.getLabel();
	}

	/**
	 * Help method to return a list of Archive files from the embedded
	 * JAXRSLibrary instance.
	 * 
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public EList getArchiveFiles() {
		return jaxrsLib.getArchiveFiles();
	}

}
