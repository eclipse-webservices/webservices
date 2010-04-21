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
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.adapter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryconfig.JAXRSLibraryRegistryUtil;

/**
 * EMF adapter that attempts to always maintain a default implementation
 * JAXRSLibrary upon addition and removal of JAXRSLibrary instances *
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public class MaintainDefaultImplementationAdapter extends AdapterImpl {

	private static MaintainDefaultImplementationAdapter INSTANCE = new MaintainDefaultImplementationAdapter();

	/**
	 * Gets the single instance of this adapter.
	 * 
	 * @return The single instance of this adapter.
	 */
	public static MaintainDefaultImplementationAdapter getInstance() {
		return INSTANCE;
	}

	/**
	 * Called to notify this adapter that a change has occured.
	 * 
	 * @param notification
	 *            EMF Notification instance
	 */
	public void notifyChanged(Notification notification) {
		Object objNotifier = notification.getNotifier();
		if (objNotifier instanceof JAXRSLibraryRegistry) {
			int eventType = notification.getEventType();
			switch (eventType) {
			case Notification.ADD:
				Object objNewValue = notification.getNewValue();
				if (objNewValue instanceof JAXRSLibrary) {
					libraryAdded((JAXRSLibrary) objNewValue);
				}
				break;
			case Notification.REMOVE:
				Object objOldValue = notification.getOldValue();
				if (objOldValue instanceof JAXRSLibrary) {
					libraryRemoved((JAXRSLibrary) objOldValue);
				}
				break;
			}
		} else if (objNotifier instanceof JAXRSLibrary) {

		}
	}

	/**
	 * Checks if the library added is an implementation and, if so, makes it the
	 * default implementation if it is the only implementation.
	 * 
	 * @param library
	 *            JAXRSLibrary instance
	 */
	@SuppressWarnings("unchecked")
	protected void libraryAdded(JAXRSLibrary library) {
		if (library != null) {
			JAXRSLibraryRegistry jaxrsLibReg = JAXRSLibraryRegistryUtil
					.getInstance().getJAXRSLibraryRegistry();
			EList impls = jaxrsLibReg.getImplJAXRSLibraries();
			if (impls.size() == 1) {
				jaxrsLibReg.setDefaultImplementation(library);
			}
		}
	}

	/**
	 * Checks if the library removed is the default implementation and, if so,
	 * makes the first remaining implementation the new default or nulls out the
	 * default implementation if no other implementation remains.
	 * 
	 * @param library
	 *            JAXRSLibrary instance
	 */
	protected void libraryRemoved(JAXRSLibrary library) {
		if (library != null) {
			JAXRSLibraryRegistry jaxrsLibReg = JAXRSLibraryRegistryUtil
					.getInstance().getJAXRSLibraryRegistry();
			JAXRSLibrary defaultImpl = jaxrsLibReg.getDefaultImplementation();
			if (defaultImpl == null
					|| library.getID().equals(defaultImpl.getID())) {
				setNewDefaultImplementation();
			}
		}
	}

	/**
	 * Sets the first available JAXRSLibrary marked as an implementation as the
	 * default implementation or sets the default implementation to null if no
	 * JAXRSLibrary is marked as an implementation.
	 */
	@SuppressWarnings("unchecked")
	protected void setNewDefaultImplementation() {
		JAXRSLibraryRegistry jaxrsLibReg = JAXRSLibraryRegistryUtil
				.getInstance().getJAXRSLibraryRegistry();
		EList impls = jaxrsLibReg.getImplJAXRSLibraries();
		if (impls.size() > 0) {
			jaxrsLibReg.setDefaultImplementation((JAXRSLibrary) impls.get(0));
		} else {
			jaxrsLibReg.setDefaultImplementation(null);
		}
	}
}
