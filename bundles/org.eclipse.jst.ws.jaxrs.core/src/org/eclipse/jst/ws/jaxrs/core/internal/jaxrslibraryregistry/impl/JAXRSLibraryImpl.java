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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryFactory;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.adapter.MaintainDefaultImplementationAdapter;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>JAXRS Library</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryImpl#getID
 * <em>ID</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryImpl#getName
 * <em>Name</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryImpl#getJAXRSVersion
 * <em>JAXRS Version</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryImpl#isDeployed
 * <em>Deployed</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryImpl#isImplementation
 * <em>Implementation</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryImpl#getArchiveFiles
 * <em>Archive Files</em>}</li>
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
public class JAXRSLibraryImpl extends EObjectImpl implements JAXRSLibrary {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */

	/**
	 * The default value of the '{@link #getID() <em>ID</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getID()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getID() <em>ID</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getID()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #isDeployed() <em>Deployed</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isDeployed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DEPLOYED_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isDeployed() <em>Deployed</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isDeployed()
	 * @generated
	 * @ordered
	 */
	protected boolean deployed = DEPLOYED_EDEFAULT;

	/**
	 * The default value of the '{@link #isImplementation()
	 * <em>Implementation</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isImplementation()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IMPLEMENTATION_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #getArchiveFiles()
	 * <em>Archive Files</em>}' containment reference list. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @see #getArchiveFiles()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> archiveFiles;

	/**
	 * <!-- begin-user-doc --> Enhanced to not only create an instance but also
	 * to set an initial ID (which can be reset later) and to add the
	 * MaintainDefaultImplementationAdapter to the list of adapters. <!--
	 * end-user-doc -->
	 * 
	 * @generated NOT
	 */
	protected JAXRSLibraryImpl() {
		super();
		// //set initial ID; will be overwritten from XML if already persisted
		// setID(String.valueOf(System.currentTimeMillis()));
		// add adapter to maintain a default implementation
		eAdapters().add(MaintainDefaultImplementationAdapter.getInstance());
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the static eClass <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return JAXRSLibraryRegistryPackage.Literals.JAXRS_LIBRARY;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getID() {
		return getName();
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the name <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param newName
	 *            <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__NAME, oldName,
					name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return true if library is to be deployed <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDeployed() {
		return deployed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param newDeployed
	 *            <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeployed(boolean newDeployed) {
		boolean oldDeployed = deployed;
		deployed = newDeployed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET,
					JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__DEPLOYED,
					oldDeployed, deployed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the list of archive files <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getArchiveFiles() {
		if (archiveFiles == null) {
			archiveFiles = new EObjectContainmentWithInverseEList<EObject>(
					ArchiveFile.class, this,
					JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__ARCHIVE_FILES,
					JAXRSLibraryRegistryPackage.ARCHIVE_FILE__JAXRS_LIBRARY);
		}
		return archiveFiles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param fullPath
	 * @return the true if the fullPath contains an archive file <!--
	 *         end-user-doc -->
	 * @generated NOT
	 */
	public boolean containsArchiveFile(String fullPath) {
		boolean contains = false;
		if (fullPath != null) {
			Iterator<EObject> itArchiveFiles = getArchiveFiles().iterator();
			while (itArchiveFiles.hasNext()) {
				ArchiveFile archiveFile = (ArchiveFile) itArchiveFiles.next();
				if (fullPath.equals(archiveFile.getResolvedSourceLocation())) {
					contains = true;
					break;
				}
			}
		}
		return contains;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the working copy <!-- end-user-doc -->
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public JAXRSLibrary getWorkingCopy() {
		JAXRSLibrary workingCopyLib = JAXRSLibraryRegistryFactory.eINSTANCE
				.createJAXRSLibrary();
		// workingCopyLib.setID(getID());
		workingCopyLib.setName(getName());
		workingCopyLib.setDeployed(isDeployed());
		Iterator<EObject> itArchiveFiles = getArchiveFiles().iterator();
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
	 * @param otherLibrary
	 *            <!-- end-user-doc -->
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	public void updateValues(JAXRSLibrary otherLibrary) {
		if (otherLibrary != null) {
			// setID(otherLibrary.getID());
			setName(otherLibrary.getName());
			setDeployed(otherLibrary.isDeployed());
			Iterator<EObject> itArchiveFiles = otherLibrary.getArchiveFiles()
					.iterator();
			getArchiveFiles().clear();
			while (itArchiveFiles.hasNext()) {
				ArchiveFile srcArchiveFile = (ArchiveFile) itArchiveFiles
						.next();
				ArchiveFile destArchiveFile = JAXRSLibraryRegistryFactory.eINSTANCE
						.createArchiveFile();
				destArchiveFile.setRelativeToWorkspace(srcArchiveFile
						.isRelativeToWorkspace());
				destArchiveFile.setSourceLocation(srcArchiveFile
						.getSourceLocation());
				destArchiveFile.setRelativeDestLocation(srcArchiveFile
						.getRelativeDestLocation());
				getArchiveFiles().add(destArchiveFile);
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param baseDestLocation
	 * @return the base destination location <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean copyTo(String baseDestLocation) {
		boolean allCopied = true;
		Iterator<EObject> itFiles = getArchiveFiles().iterator();
		while (itFiles.hasNext()) {
			ArchiveFile archiveFile = (ArchiveFile) itFiles.next();
			boolean copied = archiveFile.copyTo(baseDestLocation);
			allCopied = allCopied && copied;
		}
		return allCopied;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public String getLabel() {
		return getName();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain eInverseAdd(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__ARCHIVE_FILES:
			return ((InternalEList<EObject>) getArchiveFiles()).basicAdd(
					otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
		switch (featureID) {
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__ARCHIVE_FILES:
			return ((InternalEList<EObject>) getArchiveFiles()).basicRemove(
					otherEnd, msgs);
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
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__ID:
			return getID();
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__NAME:
			return getName();
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__DEPLOYED:
			return isDeployed() ? Boolean.TRUE : Boolean.FALSE;
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__ARCHIVE_FILES:
			return getArchiveFiles();
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
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__NAME:
			setName((String) newValue);
			return;
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__DEPLOYED:
			setDeployed(((Boolean) newValue).booleanValue());
			return;
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__ARCHIVE_FILES:
			getArchiveFiles().clear();
			getArchiveFiles().addAll((Collection) newValue);
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
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__NAME:
			setName(NAME_EDEFAULT);
			return;
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__DEPLOYED:
			setDeployed(DEPLOYED_EDEFAULT);
			return;
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__ARCHIVE_FILES:
			getArchiveFiles().clear();
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
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__ID:
			return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__NAME:
			return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT
					.equals(name);
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__DEPLOYED:
			return deployed != DEPLOYED_EDEFAULT;
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY__ARCHIVE_FILES:
			return archiveFiles != null && !archiveFiles.isEmpty();
		}
		return super.eIsSet(featureID);
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
		result.append(" (ID: ");
		result.append(id);
		result.append(", Name: ");
		result.append(name);
		result.append(", Deployed: ");
		result.append(deployed);
		result.append(')');
		return result.toString();
	}

} // JAXRSLibraryImpl
