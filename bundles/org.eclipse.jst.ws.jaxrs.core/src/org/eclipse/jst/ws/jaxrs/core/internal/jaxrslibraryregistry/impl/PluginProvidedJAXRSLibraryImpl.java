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
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl;

import java.util.Iterator;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryFactory;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Plugin Provided JAXRS Library</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.PluginProvidedJAXRSLibraryImpl#getPluginID
 * <em>Plugin ID</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.PluginProvidedJAXRSLibraryImpl#getLabel
 * <em>Label</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class PluginProvidedJAXRSLibraryImpl extends JAXRSLibraryImpl implements
		PluginProvidedJAXRSLibrary {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */

	/**
	 * The default value of the '{@link #getPluginID() <em>Plugin ID</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPluginID()
	 * @generated
	 * @ordered
	 */
	protected static final String PLUGIN_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPluginID() <em>Plugin ID</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPluginID()
	 * @generated
	 * @ordered
	 */
	protected String pluginID = PLUGIN_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected String label = LABEL_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PluginProvidedJAXRSLibraryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the static eclass <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JAXRSLibraryRegistryPackage.Literals.PLUGIN_PROVIDED_JAXRS_LIBRARY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the plugin id <!-- end-user-doc -->
	 * @generated
	 */
	public String getPluginID() {
		return pluginID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param newPluginID
	 *            <!-- end-user-doc -->
	 * @generated
	 */
	public void setPluginID(String newPluginID) {
		String oldPluginID = pluginID;
		pluginID = newPluginID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(
					this,
					Notification.SET,
					JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY__PLUGIN_ID,
					oldPluginID, pluginID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return translatable label <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getLabel() {
		if (label == null)
			return super.getLabel();
		return label;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLabel(String newLabel) {
		String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(
					this,
					Notification.SET,
					JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY__LABEL,
					oldLabel, label));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY__PLUGIN_ID:
			return getPluginID();
		case JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY__LABEL:
			return getLabel();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY__PLUGIN_ID:
			setPluginID((String) newValue);
			return;
		case JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY__LABEL:
			setLabel((String) newValue);
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
		case JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY__PLUGIN_ID:
			setPluginID(PLUGIN_ID_EDEFAULT);
			return;
		case JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY__LABEL:
			setLabel(LABEL_EDEFAULT);
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
		case JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY__PLUGIN_ID:
			return PLUGIN_ID_EDEFAULT == null ? pluginID != null
					: !PLUGIN_ID_EDEFAULT.equals(pluginID);
		case JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY__LABEL:
			return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT
					.equals(label);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getID() {
		return getPluginID() + ID_SEPARATOR + getName();
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the working copy <!-- end-user-doc -->
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public JAXRSLibrary getWorkingCopy() {
		PluginProvidedJAXRSLibrary workingCopyLib = JAXRSLibraryRegistryFactory.eINSTANCE
				.createPluginProvidedJAXRSLibrary();
		workingCopyLib.setName(getName());
		if (label != null)
			workingCopyLib.setLabel(getLabel());
		workingCopyLib.setDeployed(isDeployed());
		workingCopyLib.setPluginID(getPluginID());
		Iterator itArchiveFiles = getArchiveFiles().iterator();
		while (itArchiveFiles.hasNext()) {
			ArchiveFile srcArchiveFile = (ArchiveFile) itArchiveFiles.next();
			ArchiveFile destArchiveFile = JAXRSLibraryRegistryFactory.eINSTANCE
					.createArchiveFile();
			destArchiveFile.setRelativeToWorkspace(srcArchiveFile
					.isRelativeToWorkspace());
			destArchiveFile.setSourceLocation(srcArchiveFile
					.getSourceLocation());
			destArchiveFile.setRelativeDestLocation(srcArchiveFile
					.getRelativeDestLocation());
			workingCopyLib.getArchiveFiles().add(destArchiveFile);
		}
		return workingCopyLib;
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
		result.append(" (pluginID: ");
		result.append(pluginID);
		result.append(", Label: ");
		result.append(label);
		result.append(')');
		return result.toString();
	}

} // PluginProvidedJAXRSLibraryImpl
