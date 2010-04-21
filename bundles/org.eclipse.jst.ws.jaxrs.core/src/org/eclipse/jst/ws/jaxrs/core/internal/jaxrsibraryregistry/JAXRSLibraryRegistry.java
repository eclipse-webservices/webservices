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
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jst.ws.jaxrs.core.internal.Messages;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>JAXRS Library Registry</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry#getDefaultImplementationID
 * <em>Default Implementation ID</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry#getJAXRSLibraries
 * <em>JAXRS Libraries</em>}</li>
 * <li>
 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry#getPluginProvidedJAXRSLibraries
 * <em>Plugin Provided JAXRS Libraries</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getJAXRSLibraryRegistry()
 * @model
 * @generated
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
 public interface JAXRSLibraryRegistry extends EObject {
	/**
	 * Returns the value of the '<em><b>Default Implementation ID</b></em>'
	 * attribute. The default value is <code>""</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Implementation ID</em>' attribute
	 * isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Default Implementation ID</em>' attribute.
	 * @see #setDefaultImplementationID(String)
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getJAXRSLibraryRegistry_DefaultImplementationID()
	 * @model default=""
	 * @generated
	 */
	String getDefaultImplementationID();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry#getDefaultImplementationID
	 * <em>Default Implementation ID</em>}' attribute. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Default Implementation ID</em>'
	 *            attribute.
	 * @see #getDefaultImplementationID()
	 * @generated
	 */
	void setDefaultImplementationID(String value);

	/**
	 *The default implementation message string
	 */
	public static final String DEFAULT_IMPL_LABEL = Messages.JAXRSLibraryRegistry_DEFAULT_IMPL_LABEL;

	/**
	 * Returns the value of the '<em><b>JAXRS Libraries</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary}
	 * . <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>JAXRS Libraries</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>JAXRS Libraries</em>' containment reference
	 *         list.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getJAXRSLibraryRegistry_JAXRSLibraries()
	 * @model type=
	 *        "org.eclipse.jst.ws.jaxrs.core.internal.JAXRSLibraryregistry.JAXRSLibrary"
	 *        containment="true"
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	EList getJAXRSLibraries();

	/**
	 * Returns the value of the '<em><b>Plugin Provided JAXRS Libraries</b></em>
	 * ' containment reference list. The list contents are of type
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary}
	 * . <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Plugin Provided JAXRS Libraries</em>'
	 * containment reference list isn't clear, there really should be more of a
	 * description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Plugin Provided JAXRS Libraries</em>'
	 *         containment reference list.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#getJAXRSLibraryRegistry_PluginProvidedJAXRSLibraries()
	 * @model type="org.eclipse.jst.ws.jaxrs.core.internal.JAXRSLibraryregistry.PluginProvidedJAXRSLibrary"
	 *        containment="true" transient="true"
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	EList getPluginProvidedJAXRSLibraries();

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Implementation</em>' reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * 
	 * @return the default implementation <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	JAXRSLibrary getDefaultImplementation();

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param implementation
	 *            <!-- end-user-doc -->
	 * @model implementationRequired="true"
	 * @generated
	 */
	void setDefaultImplementation(JAXRSLibrary implementation);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param ID
	 * @return the JAXRS Library <!-- end-user-doc -->
	 * @model required="true" IDRequired="true"
	 * @generated
	 */
	JAXRSLibrary getJAXRSLibraryByID(String ID);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param name
	 * @return the jaxrs libraries matching name <!-- end-user-doc -->
	 * @model required="true" many="false" nameRequired="true"
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	EList getJAXRSLibrariesByName(String name);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the implementation JAXRS libraries <!-- end-user-doc -->
	 * @model kind="operation" required="true" many="false"
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	EList getImplJAXRSLibraries();

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return all the JAXRS libraries <!-- end-user-doc -->
	 * @model kind="operation" required="true" many="false"
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	EList getAllJAXRSLibraries();

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param library
	 * @return true if add succeeds <!-- end-user-doc -->
	 * @model required="true" libraryRequired="true"
	 * @generated
	 */
	boolean addJAXRSLibrary(JAXRSLibrary library);

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param library
	 * @return true if remove succeeds <!-- end-user-doc -->
	 * @model required="true" libraryRequired="true"
	 * @generated
	 */
	boolean removeJAXRSLibrary(JAXRSLibrary library);

} // JAXRSLibraryRegistry
