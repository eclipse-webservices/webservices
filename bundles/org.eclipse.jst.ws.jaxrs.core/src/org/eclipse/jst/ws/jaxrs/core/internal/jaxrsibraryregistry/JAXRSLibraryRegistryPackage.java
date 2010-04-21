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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains
 * accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryFactory
 * @model kind="package"
 * @generated
 * 
 * 
 * @deprecated
 * 
 * <p>
 * <b>Provisional API - subject to change - do not use</b>
 * </p>
 */
 public interface JAXRSLibraryRegistryPackage extends EPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	/**
	 * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "jaxrslibraryregistry";

	/**
	 * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/webtools/jaxrs/schema/jaxrslibraryregistry.xsd";

	/**
	 * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "jaxrslibreg";

	/**
	 * The singleton instance of the package. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	JAXRSLibraryRegistryPackage eINSTANCE = org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryPackageImpl
			.init();

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryImpl
	 * <em>JAXRS Library Registry</em>}' class. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryImpl
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryPackageImpl#getJAXRSLibraryRegistry()
	 * @generated
	 */
	int JAXRS_LIBRARY_REGISTRY = 0;

	/**
	 * The feature id for the '<em><b>Default Implementation ID</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAXRS_LIBRARY_REGISTRY__DEFAULT_IMPLEMENTATION_ID = 0;

	/**
	 * The feature id for the '<em><b>JAXRS Libraries</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAXRS_LIBRARY_REGISTRY__JAXRS_LIBRARIES = 1;

	/**
	 * The feature id for the '<em><b>Plugin Provided JAXRS Libraries</b></em>'
	 * containment reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAXRS_LIBRARY_REGISTRY__PLUGIN_PROVIDED_JAXRS_LIBRARIES = 2;

	/**
	 * The number of structural features of the '<em>JAXRS Library Registry</em>
	 * ' class. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAXRS_LIBRARY_REGISTRY_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryImpl
	 * <em>JAXRS Library</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryImpl
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryPackageImpl#getJAXRSLibrary()
	 * @generated
	 */
	int JAXRS_LIBRARY = 1;

	/**
	 * The feature id for the '<em><b>ID</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAXRS_LIBRARY__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAXRS_LIBRARY__NAME = 1;

	/**
	 * The feature id for the '<em><b>Deployed</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAXRS_LIBRARY__DEPLOYED = 2;

	/**
	 * The feature id for the '<em><b>Archive Files</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAXRS_LIBRARY__ARCHIVE_FILES = 3;

	/**
	 * The number of structural features of the '<em>JAXRS Library</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int JAXRS_LIBRARY_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.ArchiveFileImpl
	 * <em>Archive File</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.ArchiveFileImpl
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryPackageImpl#getArchiveFile()
	 * @generated
	 */
	int ARCHIVE_FILE = 3;

	/**
	 * The meta object id for the '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.PluginProvidedJAXRSLibraryImpl
	 * <em>Plugin Provided JAXRS Library</em>}' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.PluginProvidedJAXRSLibraryImpl
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryPackageImpl#getPluginProvidedJAXRSLibrary()
	 * @generated
	 */
	int PLUGIN_PROVIDED_JAXRS_LIBRARY = 2;

	/**
	 * The feature id for the '<em><b>ID</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLUGIN_PROVIDED_JAXRS_LIBRARY__ID = JAXRS_LIBRARY__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLUGIN_PROVIDED_JAXRS_LIBRARY__NAME = JAXRS_LIBRARY__NAME;

	/**
	 * The feature id for the '<em><b>Deployed</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLUGIN_PROVIDED_JAXRS_LIBRARY__DEPLOYED = JAXRS_LIBRARY__DEPLOYED;

	/**
	 * The feature id for the '<em><b>Archive Files</b></em>' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLUGIN_PROVIDED_JAXRS_LIBRARY__ARCHIVE_FILES = JAXRS_LIBRARY__ARCHIVE_FILES;

	/**
	 * The feature id for the '<em><b>Plugin ID</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLUGIN_PROVIDED_JAXRS_LIBRARY__PLUGIN_ID = JAXRS_LIBRARY_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Label</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLUGIN_PROVIDED_JAXRS_LIBRARY__LABEL = JAXRS_LIBRARY_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '
	 * <em>Plugin Provided JAXRS Library</em>' class. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int PLUGIN_PROVIDED_JAXRS_LIBRARY_FEATURE_COUNT = JAXRS_LIBRARY_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Relative To Workspace</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARCHIVE_FILE__RELATIVE_TO_WORKSPACE = 0;

	/**
	 * The feature id for the '<em><b>Source Location</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARCHIVE_FILE__SOURCE_LOCATION = 1;

	/**
	 * The feature id for the '<em><b>Relative Dest Location</b></em>'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARCHIVE_FILE__RELATIVE_DEST_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>JAXRS Library</b></em>' container
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARCHIVE_FILE__JAXRS_LIBRARY = 3;

	/**
	 * The number of structural features of the '<em>Archive File</em>' class.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int ARCHIVE_FILE_FEATURE_COUNT = 4;

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry
	 * <em>JAXRS Library Registry</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for class '<em>JAXRS Library Registry</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry
	 * @generated
	 */
	EClass getJAXRSLibraryRegistry();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry#getDefaultImplementationID
	 * <em>Default Implementation ID</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the attribute '
	 *         <em>Default Implementation ID</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry#getDefaultImplementationID()
	 * @see #getJAXRSLibraryRegistry()
	 * @generated
	 */
	EAttribute getJAXRSLibraryRegistry_DefaultImplementationID();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry#getJAXRSLibraries
	 * <em>JAXRS Libraries</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>JAXRS Libraries</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry#getJAXRSLibraries()
	 * @see #getJAXRSLibraryRegistry()
	 * @generated
	 */
	EReference getJAXRSLibraryRegistry_JAXRSLibraries();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry#getPluginProvidedJAXRSLibraries
	 * <em>Plugin Provided JAXRS Libraries</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Plugin Provided JAXRS Libraries</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry#getPluginProvidedJAXRSLibraries()
	 * @see #getJAXRSLibraryRegistry()
	 * @generated
	 */
	EReference getJAXRSLibraryRegistry_PluginProvidedJAXRSLibraries();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary
	 * <em>JAXRS Library</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>JAXRS Library</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary
	 * @generated
	 */
	EClass getJAXRSLibrary();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#getID
	 * <em>ID</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>ID</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#getID()
	 * @see #getJAXRSLibrary()
	 * @generated
	 */
	EAttribute getJAXRSLibrary_ID();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#getName
	 * <em>Name</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#getName()
	 * @see #getJAXRSLibrary()
	 * @generated
	 */
	EAttribute getJAXRSLibrary_Name();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#isDeployed
	 * <em>Deployed</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Deployed</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#isDeployed()
	 * @see #getJAXRSLibrary()
	 * @generated
	 */
	EAttribute getJAXRSLibrary_Deployed();

	/**
	 * Returns the meta object for the containment reference list '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#getArchiveFiles
	 * <em>Archive Files</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the containment reference list '
	 *         <em>Archive Files</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary#getArchiveFiles()
	 * @see #getJAXRSLibrary()
	 * @generated
	 */
	EReference getJAXRSLibrary_ArchiveFiles();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile
	 * <em>Archive File</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Archive File</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile
	 * @generated
	 */
	EClass getArchiveFile();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getSourceLocation
	 * <em>Source Location</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Source Location</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getSourceLocation()
	 * @see #getArchiveFile()
	 * @generated
	 */
	EAttribute getArchiveFile_SourceLocation();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#isRelativeToWorkspace
	 * <em>Relative To Workspace</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Relative To Workspace</em>
	 *         '.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#isRelativeToWorkspace()
	 * @see #getArchiveFile()
	 * @generated
	 */
	EAttribute getArchiveFile_RelativeToWorkspace();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getRelativeDestLocation
	 * <em>Relative Dest Location</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for the attribute '
	 *         <em>Relative Dest Location</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getRelativeDestLocation()
	 * @see #getArchiveFile()
	 * @generated
	 */
	EAttribute getArchiveFile_RelativeDestLocation();

	/**
	 * Returns the meta object for the container reference '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getJAXRSLibrary
	 * <em>JAXRS Library</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the container reference '
	 *         <em>JAXRS Library</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile#getJAXRSLibrary()
	 * @see #getArchiveFile()
	 * @generated
	 */
	EReference getArchiveFile_JAXRSLibrary();

	/**
	 * Returns the meta object for class '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary
	 * <em>Plugin Provided JAXRS Library</em>}'. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Plugin Provided JAXRS Library</em>
	 *         '.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary
	 * @generated
	 */
	EClass getPluginProvidedJAXRSLibrary();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary#getPluginID
	 * <em>Plugin ID</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Plugin ID</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary#getPluginID()
	 * @see #getPluginProvidedJAXRSLibrary()
	 * @generated
	 */
	EAttribute getPluginProvidedJAXRSLibrary_PluginID();

	/**
	 * Returns the meta object for the attribute '
	 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary#getLabel
	 * <em>Label</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Label</em>'.
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary#getLabel()
	 * @see #getPluginProvidedJAXRSLibrary()
	 * @generated
	 */
	EAttribute getPluginProvidedJAXRSLibrary_Label();

	/**
	 * Returns the factory that creates the instances of the model. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	JAXRSLibraryRegistryFactory getJAXRSLibraryRegistryFactory();

	/**
	 * <!-- begin-user-doc --> Defines literals for the meta objects that
	 * represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryImpl
		 * <em>JAXRS Library Registry</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryImpl
		 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryPackageImpl#getJAXRSLibraryRegistry()
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EClass JAXRS_LIBRARY_REGISTRY = eINSTANCE.getJAXRSLibraryRegistry();

		/**
		 * The meta object literal for the '
		 * <em><b>Default Implementation ID</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EAttribute JAXRS_LIBRARY_REGISTRY__DEFAULT_IMPLEMENTATION_ID = eINSTANCE
				.getJAXRSLibraryRegistry_DefaultImplementationID();

		/**
		 * The meta object literal for the '<em><b>JAXRS Libraries</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EReference JAXRS_LIBRARY_REGISTRY__JAXRS_LIBRARIES = eINSTANCE
				.getJAXRSLibraryRegistry_JAXRSLibraries();

		/**
		 * The meta object literal for the '
		 * <em><b>Plugin Provided JAXRS Libraries</b></em>' containment
		 * reference list feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EReference JAXRS_LIBRARY_REGISTRY__PLUGIN_PROVIDED_JAXRS_LIBRARIES = eINSTANCE
				.getJAXRSLibraryRegistry_PluginProvidedJAXRSLibraries();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryImpl
		 * <em>JAXRS Library</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryImpl
		 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryPackageImpl#getJAXRSLibrary()
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EClass JAXRS_LIBRARY = eINSTANCE.getJAXRSLibrary();

		/**
		 * The meta object literal for the '<em><b>ID</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EAttribute JAXRS_LIBRARY__ID = eINSTANCE.getJAXRSLibrary_ID();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EAttribute JAXRS_LIBRARY__NAME = eINSTANCE.getJAXRSLibrary_Name();

		/**
		 * The meta object literal for the '<em><b>Deployed</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EAttribute JAXRS_LIBRARY__DEPLOYED = eINSTANCE
				.getJAXRSLibrary_Deployed();

		/**
		 * The meta object literal for the '<em><b>Archive Files</b></em>'
		 * containment reference list feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EReference JAXRS_LIBRARY__ARCHIVE_FILES = eINSTANCE
				.getJAXRSLibrary_ArchiveFiles();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.ArchiveFileImpl
		 * <em>Archive File</em>}' class. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.ArchiveFileImpl
		 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryPackageImpl#getArchiveFile()
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EClass ARCHIVE_FILE = eINSTANCE.getArchiveFile();

		/**
		 * The meta object literal for the '
		 * <em><b>Relative To Workspace</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EAttribute ARCHIVE_FILE__RELATIVE_TO_WORKSPACE = eINSTANCE
				.getArchiveFile_RelativeToWorkspace();

		/**
		 * The meta object literal for the '<em><b>Source Location</b></em>'
		 * attribute feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EAttribute ARCHIVE_FILE__SOURCE_LOCATION = eINSTANCE
				.getArchiveFile_SourceLocation();

		/**
		 * The meta object literal for the '
		 * <em><b>Relative Dest Location</b></em>' attribute feature. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EAttribute ARCHIVE_FILE__RELATIVE_DEST_LOCATION = eINSTANCE
				.getArchiveFile_RelativeDestLocation();

		/**
		 * The meta object literal for the '<em><b>JAXRS Library</b></em>'
		 * container reference feature. <!-- begin-user-doc --> <!--
		 * end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EReference ARCHIVE_FILE__JAXRS_LIBRARY = eINSTANCE
				.getArchiveFile_JAXRSLibrary();

		/**
		 * The meta object literal for the '
		 * {@link org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.PluginProvidedJAXRSLibraryImpl
		 * <em>Plugin Provided JAXRS Library</em>}' class. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.PluginProvidedJAXRSLibraryImpl
		 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl.JAXRSLibraryRegistryPackageImpl#getPluginProvidedJAXRSLibrary()
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EClass PLUGIN_PROVIDED_JAXRS_LIBRARY = eINSTANCE
				.getPluginProvidedJAXRSLibrary();

		/**
		 * The meta object literal for the '<em><b>Plugin ID</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EAttribute PLUGIN_PROVIDED_JAXRS_LIBRARY__PLUGIN_ID = eINSTANCE
				.getPluginProvidedJAXRSLibrary_PluginID();

		/**
		 * The meta object literal for the '<em><b>Label</b></em>' attribute
		 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		// @SuppressWarnings("hiding")
		EAttribute PLUGIN_PROVIDED_JAXRS_LIBRARY__LABEL = eINSTANCE
				.getPluginProvidedJAXRSLibrary_Label();

	}

} // JAXRSLibraryRegistryPackage
