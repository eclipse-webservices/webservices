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
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage
 * @generated
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
