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
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>JAXRS Library Registry</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryImpl#getDefaultImplementationID
 * <em>Default Implementation ID</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryImpl#getJAXRSLibraries
 * <em>JAXRS Libraries</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryImpl#getPluginProvidedJAXRSLibraries
 * <em>Plugin Provided JAXRS Libraries</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public class JAXRSLibraryRegistryImpl extends EObjectImpl implements
		JAXRSLibraryRegistry {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */

	/**
	 * The default value of the '{@link #getDefaultImplementationID()
	 * <em>Default Implementation ID</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getDefaultImplementationID()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_IMPLEMENTATION_ID_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getDefaultImplementationID()
	 * <em>Default Implementation ID</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getDefaultImplementationID()
	 * @generated
	 * @ordered
	 */
	protected String defaultImplementationID = DEFAULT_IMPLEMENTATION_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getJAXRSLibraries()
	 * <em>JAXRS Libraries</em>}' containment reference list. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getJAXRSLibraries()
	 * @generated
	 * @ordered
	 */
	protected EList<JAXRSLibrary> jaxrsLibraries;

	/**
	 * The cached value of the '{@link #getPluginProvidedJAXRSLibraries()
	 * <em>Plugin Provided JAXRS Libraries</em>}' containment reference list.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPluginProvidedJAXRSLibraries()
	 * @generated
	 * @ordered
	 */
	protected EList<JAXRSLibrary> pluginProvidedJAXRSLibraries;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected JAXRSLibraryRegistryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the static eClass <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JAXRSLibraryRegistryPackage.Literals.JAXRS_LIBRARY_REGISTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the default implementation id <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultImplementationID() {
		return defaultImplementationID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param newDefaultImplementationID
	 *            <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultImplementationID(String newDefaultImplementationID) {
		String oldDefaultImplementationID = defaultImplementationID;
		defaultImplementationID = newDefaultImplementationID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(
					this,
					Notification.SET,
					JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__DEFAULT_IMPLEMENTATION_ID,
					oldDefaultImplementationID, defaultImplementationID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the list of jaxrs libraries <!-- end-user-doc -->
	 * @generated
	 */
	public EList<JAXRSLibrary> getJAXRSLibraries() {
		if (jaxrsLibraries == null) {
			jaxrsLibraries = new EObjectContainmentEList<JAXRSLibrary>(
					JAXRSLibrary.class,
					this,
					JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__JAXRS_LIBRARIES);
		}
		return jaxrsLibraries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the list of plugin provided JAXRS libraries <!-- end-user-doc -->
	 * @generated
	 */
	public EList<JAXRSLibrary> getPluginProvidedJAXRSLibraries() {
		if (pluginProvidedJAXRSLibraries == null) {
			pluginProvidedJAXRSLibraries = new EObjectContainmentEList<JAXRSLibrary>(
					PluginProvidedJAXRSLibrary.class,
					this,
					JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__PLUGIN_PROVIDED_JAXRS_LIBRARIES);
		}
		return pluginProvidedJAXRSLibraries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the default implemention JAXRS Library <!-- end-user-doc -->
	 * @generated NOT
	 */
	public JAXRSLibrary getDefaultImplementation() {
		return getJAXRSLibraryByID(getDefaultImplementationID());
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param implementation
	 *            <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setDefaultImplementation(JAXRSLibrary implementation) {
		if (implementation != null) {
			setDefaultImplementationID(implementation.getID());
		} else {
			setDefaultImplementationID(null);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__JAXRS_LIBRARIES:
			return ((InternalEList<JAXRSLibrary>) getJAXRSLibraries())
					.basicRemove(otherEnd, msgs);
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__PLUGIN_PROVIDED_JAXRS_LIBRARIES:
			return ((InternalEList<JAXRSLibrary>) getPluginProvidedJAXRSLibraries())
					.basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__DEFAULT_IMPLEMENTATION_ID:
			return getDefaultImplementationID();
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__JAXRS_LIBRARIES:
			return getJAXRSLibraries();
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__PLUGIN_PROVIDED_JAXRS_LIBRARIES:
			return getPluginProvidedJAXRSLibraries();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__DEFAULT_IMPLEMENTATION_ID:
			setDefaultImplementationID((String) newValue);
			return;
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__JAXRS_LIBRARIES:
			getJAXRSLibraries().clear();
			getJAXRSLibraries().addAll((Collection) newValue);
			return;
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__PLUGIN_PROVIDED_JAXRS_LIBRARIES:
			getPluginProvidedJAXRSLibraries().clear();
			getPluginProvidedJAXRSLibraries().addAll((Collection) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__DEFAULT_IMPLEMENTATION_ID:
			setDefaultImplementationID(DEFAULT_IMPLEMENTATION_ID_EDEFAULT);
			return;
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__JAXRS_LIBRARIES:
			getJAXRSLibraries().clear();
			return;
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__PLUGIN_PROVIDED_JAXRS_LIBRARIES:
			getPluginProvidedJAXRSLibraries().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__DEFAULT_IMPLEMENTATION_ID:
			return DEFAULT_IMPLEMENTATION_ID_EDEFAULT == null ? defaultImplementationID != null
					: !DEFAULT_IMPLEMENTATION_ID_EDEFAULT
							.equals(defaultImplementationID);
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__JAXRS_LIBRARIES:
			return jaxrsLibraries != null && !jaxrsLibraries.isEmpty();
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY__PLUGIN_PROVIDED_JAXRS_LIBRARIES:
			return pluginProvidedJAXRSLibraries != null
					&& !pluginProvidedJAXRSLibraries.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param ID
	 * @return the JAXRS Library of ID or null if none <!-- end-user-doc -->
	 * @generated NOT
	 */
	public JAXRSLibrary getJAXRSLibraryByID(String ID) {
		JAXRSLibrary library = null;
		if (ID != null) {
			Iterator<JAXRSLibrary> itLibs = getAllJAXRSLibraries().iterator();
			while (itLibs.hasNext()) {
				JAXRSLibrary curLib = itLibs.next();
				if (ID.equals(curLib.getID())) {
					library = curLib;
					break;
				}
			}
		}
		return library;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param name
	 * @return the list of libraries named 'name' <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<JAXRSLibrary> getJAXRSLibrariesByName(String name) {
		EList<JAXRSLibrary> libraries = new BasicEList<JAXRSLibrary>();
		if (name != null) {
			Iterator<JAXRSLibrary> itLibs = getAllJAXRSLibraries().iterator();
			while (itLibs.hasNext()) {
				JAXRSLibrary curLib = itLibs.next();
				if (name.equals(curLib.getName())) {
					libraries.add(curLib);
				}
			}
		}
		return libraries;
	}

	/**
	 * <!-- begin-user-doc --> This is a convenience method to return an EList
	 * of JAXRSLibrary instances that are marked as JAXRS implementations; while
	 * all instances are valid references, the returned EList should not be used
	 * for additions and/or removals of instances (use the EList returned by
	 * getJAXRSLibraries()).
	 * 
	 * @return the list of implemention jaxrs libraries <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<JAXRSLibrary> getImplJAXRSLibraries() {
		EList<JAXRSLibrary> implementations = new BasicEList<JAXRSLibrary>();
		Iterator<JAXRSLibrary> itLibs = getAllJAXRSLibraries().iterator();
		while (itLibs.hasNext()) {
			JAXRSLibrary lib = itLibs.next();
			implementations.add(lib);
		}
		return implementations;
	}

	/**
	 * <!-- begin-user-doc --> This is a convenience method to return an EList
	 * of JAXRSLibrary instances and PluginProvidedJAXRSLibrary instances; while
	 * all instances are valid references, the returned EList should not be used
	 * for additions and/or removals of instances (use the EList returned by
	 * getJAXRSLibraries()).
	 * 
	 * @return all JAXRS libraries <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<JAXRSLibrary> getAllJAXRSLibraries() {
		EList<JAXRSLibrary> allLibs = new BasicEList<JAXRSLibrary>();
		allLibs.addAll(getJAXRSLibraries());
		allLibs.addAll(getPluginProvidedJAXRSLibraries());
		return allLibs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param library
	 * @return true if library is successfully added <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean addJAXRSLibrary(JAXRSLibrary library) {
		boolean added = false;
		if (library instanceof PluginProvidedJAXRSLibrary) {
			added = getPluginProvidedJAXRSLibraries().add(library);
		} else {
			added = getJAXRSLibraries().add(library);
		}
		return added;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param library
	 * @return true if library is successfully removed <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean removeJAXRSLibrary(JAXRSLibrary library) {
		boolean removed = false;
		if (library instanceof PluginProvidedJAXRSLibrary) {
			removed = getPluginProvidedJAXRSLibraries().remove(library);
		} else {
			removed = getJAXRSLibraries().remove(library);
		}
		return removed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the string representation <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (DefaultImplementationID: ");
		result.append(defaultImplementationID);
		result.append(')');
		return result.toString();
	}

} // JAXRSLibraryRegistryImpl
