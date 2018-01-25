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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryFactory;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * 
 * @generated
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
public class JAXRSLibraryRegistryFactoryImpl extends EFactoryImpl implements
		JAXRSLibraryRegistryFactory {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */

	/**
	 * Creates the default factory implementation. <!-- begin-user-doc -->
	 * 
	 * @return the initialized factory <!-- end-user-doc -->
	 * @generated
	 */
	public static JAXRSLibraryRegistryFactory init() {
		try {
			JAXRSLibraryRegistryFactory theJAXRSLibraryRegistryFactory = (JAXRSLibraryRegistryFactory) EPackage.Registry.INSTANCE
					.getEFactory("http://www.eclipse.org/webtools/jaxrs/schema/JAXRSLibraryregistry.xsd");
			if (theJAXRSLibraryRegistryFactory != null) {
				return theJAXRSLibraryRegistryFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new JAXRSLibraryRegistryFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public JAXRSLibraryRegistryFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @param eClass
	 * @return the static eclass <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY_REGISTRY:
			return createJAXRSLibraryRegistry();
		case JAXRSLibraryRegistryPackage.JAXRS_LIBRARY:
			return createJAXRSLibrary();
		case JAXRSLibraryRegistryPackage.PLUGIN_PROVIDED_JAXRS_LIBRARY:
			return createPluginProvidedJAXRSLibrary();
		case JAXRSLibraryRegistryPackage.ARCHIVE_FILE:
			return createArchiveFile();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName()
					+ "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the JAXRS Library registry <!-- end-user-doc -->
	 * @generated
	 */
	public JAXRSLibraryRegistry createJAXRSLibraryRegistry() {
		JAXRSLibraryRegistryImpl JAXRSLibraryRegistry = new JAXRSLibraryRegistryImpl();
		return JAXRSLibraryRegistry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the JAXRS Library <!-- end-user-doc -->
	 * @generated
	 */
	public JAXRSLibrary createJAXRSLibrary() {
		JAXRSLibraryImpl JAXRSLibrary = new JAXRSLibraryImpl();
		return JAXRSLibrary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the archive file <!-- end-user-doc -->
	 * @generated
	 */
	public ArchiveFile createArchiveFile() {
		ArchiveFileImpl archiveFile = new ArchiveFileImpl();
		return archiveFile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the plugin provided JAXRS Library <!-- end-user-doc -->
	 * @generated
	 */
	public PluginProvidedJAXRSLibrary createPluginProvidedJAXRSLibrary() {
		PluginProvidedJAXRSLibraryImpl pluginProvidedJAXRSLibrary = new PluginProvidedJAXRSLibraryImpl();
		return pluginProvidedJAXRSLibrary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the registry package <!-- end-user-doc -->
	 * @generated
	 */
	public JAXRSLibraryRegistryPackage getJAXRSLibraryRegistryPackage() {
		return (JAXRSLibraryRegistryPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the package <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static JAXRSLibraryRegistryPackage getPackage() {
		return JAXRSLibraryRegistryPackage.eINSTANCE;
	}

} // JAXRSLibraryRegistryFactoryImpl
