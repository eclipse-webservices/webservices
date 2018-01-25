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
 * 20100407   308401 ericdp@ca.ibm.com - Eric D. Peters, JAX-RS facet wizard page - Shared-library option should be disabled
 * 20100420   309846 ericdp@ca.ibm.com - Eric D. Peters, Remove dead code related to e.p. pluginProvidedJaxrsLibraries
 *******************************************************************************/
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;

/**
 * Model for the custom control <b>JAXRSLibraryConfigControl</b>. A
 * JAXRSLibraryConfigModel object is initialized from a source and updated with
 * selected implementation when selections are changed.
 * 
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */

public class JAXRSLibraryConfigModel {
	final private JAXRSLibraryConfiglModelSource data;
	final private JAXRSLibraryRegistryUtil jaxrsLibReg;
	@SuppressWarnings("unchecked")
	private List colJAXRSImplLib;

	/**
	 * Private constructor.
	 * 
	 * @param data
	 */
	private JAXRSLibraryConfigModel(JAXRSLibraryConfiglModelSource data) {
		this.data = data;
		this.jaxrsLibReg = JAXRSLibraryRegistryUtil.getInstance();
	}

	/**
	 * Return JAXRS implementation libraries.
	 * 
	 * This list is initialized from JAXRS Library registry and updated with
	 * persistent configuration data.
	 * 
	 * @return List
	 *  
	 * @see org.eclipse.jst.ws.jaxrs.ui.properties.IJAXRSLibraryDecoratorProvider#getProjectJAXRSImplementationLibraries()
	 */
	@SuppressWarnings("unchecked")
	public List getJAXRSImplementationLibraries() {
		if (colJAXRSImplLib == null) {
			/*
			 * To initialze an implementation library list from registry and
			 * then update the list with saved implementation library.
			 */
			colJAXRSImplLib = jaxrsLibReg.getJAXRSImplementationLibraries();
			JAXRSLibraryInternalReference targetLib = data
					.getJAXRSImplementationLibrary();
			if (targetLib == null) {
				// no saved implementation, get default implementation library
				targetLib = jaxrsLibReg.getDefaultJAXRSImplementationLibrary();
			}
			if (targetLib != null) {
				JAXRSLibraryInternalReference srcLib = jaxrsLibReg
						.getJAXRSLibraryReferencebyID(targetLib.getID());
				if (srcLib != null) {
					srcLib.setSelected(true);
					srcLib.setToBeDeployed(targetLib.isCheckedToBeDeployed());
					srcLib.setToBeSharedLibrary(targetLib.isSharedLibSupported() && targetLib.isCheckedToBeSharedLibrary());
					srcLib.setSharedLibSupported(targetLib.isSharedLibSupported());
				}
			}
		}
		return colJAXRSImplLib;
	}

	/**
	 * Return the selected JAXRS implementation library currently. A null is
	 * returned if none is selected.
	 * 
	 * @return JAXRSLibraryInternalReference
	 */
	@SuppressWarnings("unchecked")
	public JAXRSLibraryInternalReference getCurrentJAXRSImplementationLibrarySelection() {
		Iterator it = getJAXRSImplementationLibraries().iterator();
		JAXRSLibraryInternalReference crtItem = null;
		while (it.hasNext()) {
			crtItem = (JAXRSLibraryInternalReference) it.next();
			if (crtItem.isSelected()) {
				return crtItem;
			}
		}
		return null;
	}

	/**
	 * Returned a saved implementation library which was persisted as
	 * DialogSettings or as project properties.
	 * 
	 * @return JAXRSLibraryInternalReference
	 */
	public JAXRSLibraryInternalReference getSavedJAXRSImplementationLibrary() {
		return data.getJAXRSImplementationLibrary();
	}

	/**
	 * Update the selected JAXRS implementation library.
	 * 
	 * Note: The library parameter won't be not added into the collection if it
	 * does not exist already.
	 * 
	 * @param library
	 *            JAXRSLibraryInternalReference
	 */
	@SuppressWarnings("unchecked")
	public void setCurrentJAXRSImplementationLibrarySelection(
			final JAXRSLibraryInternalReference library) {
		if (library != null) {
			Iterator it = getJAXRSImplementationLibraries().iterator();
			JAXRSLibraryInternalReference crtjaxrslib = null;
			while (it.hasNext()) {
				crtjaxrslib = (JAXRSLibraryInternalReference) it.next();
				if (crtjaxrslib.getID().equals(library.getID())) {
					crtjaxrslib.setSelected(true);
					crtjaxrslib
							.setToBeDeployed(library.isCheckedToBeDeployed());
					crtjaxrslib.setToBeSharedLibrary(library.isSharedLibSupported() && library.isCheckedToBeSharedLibrary());
					crtjaxrslib.setSharedLibSupported(library.isSharedLibSupported());
				} else {
					crtjaxrslib.setSelected(false);
				}
			}
		}
	}

	/**
	 * To save current configuration of implementation libraries as project
	 * properties.
	 * 
	 * @param project
	 *            IProject
	 */
	public void saveData(final IProject project) {
		// Instantiate one to make sure it is for a project.
		JAXRSLibraryConfigProjectData data_ = new JAXRSLibraryConfigProjectData(
				project);
		List<JAXRSLibraryInternalReference> implLibs = new ArrayList<JAXRSLibraryInternalReference>();
		implLibs.add(getCurrentJAXRSImplementationLibrarySelection());
		data_.saveData(implLibs);
	}

	/**
	 * Factory class to create new JAXRSLibraryConfigModel instances
	 */
	public static final class JAXRSLibraryConfigModelFactory {
		/**
		 * To create a new instance of JAXRSLibraryConfigModel object. A
		 * NullPointerException is raised if source is null.
		 * 
		 * @param source
		 *            JAXRSLibraryConfiglModelSource
		 * @return JAXRSLibraryConfigModel
		 */
		public static JAXRSLibraryConfigModel createInstance(
				final JAXRSLibraryConfiglModelSource source) {
			if (source == null) {
				throw new NullPointerException(
						Messages.JAXRSLibraryConfigModel_Null_Data_Source);
			}
			return new JAXRSLibraryConfigModel(source);
		}
	}

}
