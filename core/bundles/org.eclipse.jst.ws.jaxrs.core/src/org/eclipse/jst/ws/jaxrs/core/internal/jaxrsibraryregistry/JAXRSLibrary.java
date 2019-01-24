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
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>JAXRS Library</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.JAXRSLibrary#getID
 * <em>ID</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.JAXRSLibrary#getName
 * <em>Name</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.JAXRSLibrary#isDeployed
 * <em>Deployed</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.JAXRSLibrary#getArchiveFiles
 * <em>Archive Files</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.jst.ws.jaxrs.core.internal.JAXRSLibraryRegistryPackage#getJAXRSLibrary()
 * @model
 * @generated
 * 
 *  @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public interface JAXRSLibrary extends EObject {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */

	/**
	 * Returns the value of the '<em><b>ID</b></em>' attribute. The default
	 * value is <code>""</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ID</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>ID</em>' attribute.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getJAXRSLibrary_ID()
	 * @model default="" transient="true" changeable="false" derived="true"
	 * @generated
	 */
	String getID();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getJAXRSLibrary_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#getName
	 * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Deployed</b></em>' attribute. The
	 * default value is <code>"true"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Deployed</em>' attribute isn't clear, there
	 * really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Deployed</em>' attribute.
	 * @see #setDeployed(boolean)
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getJAXRSLibrary_Deployed()
	 * @model default="true" required="true"
	 * @generated
	 */
	boolean isDeployed();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#isDeployed
	 * <em>Deployed</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Deployed</em>' attribute.
	 * @see #isDeployed()
	 * @generated
	 */
	void setDeployed(boolean value);

	/**
	 * Returns the value of the '<em><b>Archive Files</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile}
	 * . It is bidirectional and its opposite is '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getJAXRSLibrary
	 * <em>JAXRS Library</em>}'. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Archive Files</em>' containment reference list
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Archive Files</em>' containment reference
	 *         list.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getJAXRSLibrary_ArchiveFiles()
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getJAXRSLibrary
	 * @model type=
	 *        "org.eclipse.jst.ws.jaxrs.core.internal.JAXRSLibraryregistry.ArchiveFile"
	 *        opposite="JAXRSLibrary" containment="true"
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	EList getArchiveFiles();

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param fullPath
	 * @return true if fullPath contains archive file <!-- end-user-doc -->
	 * @model required="true" fullPathRequired="true"
	 * @generated
	 */
	boolean containsArchiveFile(String fullPath);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the working copy <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	JAXRSLibrary getWorkingCopy();

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param otherLibrary
	 *            <!-- end-user-doc -->
	 * @model otherLibraryRequired="true"
	 * @generated
	 */
	void updateValues(JAXRSLibrary otherLibrary);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param baseDestLocation
	 * @return true if copy succeeds <!-- end-user-doc -->
	 * @model required="true" baseDestLocationRequired="true"
	 * @generated
	 */
	boolean copyTo(String baseDestLocation);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return label <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getLabel();

} // JAXRSLibrary
