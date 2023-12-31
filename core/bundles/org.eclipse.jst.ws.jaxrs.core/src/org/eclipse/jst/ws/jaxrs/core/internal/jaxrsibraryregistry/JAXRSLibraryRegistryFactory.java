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

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage
 * @generated
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public interface JAXRSLibraryRegistryFactory extends EFactory {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */

	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	JAXRSLibraryRegistryFactory eINSTANCE = org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryFactoryImpl
			.init();

	/**
	 * Returns a new object of class '<em>JAXRS Library Registry</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>JAXRS Library Registry</em>'.
	 * @generated
	 */
	JAXRSLibraryRegistry createJAXRSLibraryRegistry();

	/**
	 * Returns a new object of class '<em>JAXRS Library</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>JAXRS Library</em>'.
	 * @generated
	 */
	JAXRSLibrary createJAXRSLibrary();

	/**
	 * Returns a new object of class '<em>Archive File</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Archive File</em>'.
	 * @generated
	 */
	ArchiveFile createArchiveFile();

	/**
	 * Returns a new object of class '<em>Plugin Provided JAXRS Library</em>'.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Plugin Provided JAXRS Library</em>'.
	 * @generated
	 */
	PluginProvidedJAXRSLibrary createPluginProvidedJAXRSLibrary();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	JAXRSLibraryRegistryPackage getJAXRSLibraryRegistryPackage();

} // JAXRSLibraryRegistryFactory
