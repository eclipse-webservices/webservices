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
package org.eclipse.jst.ws.jaxrs.core.internal.jaxrslibraryregistry.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.ArchiveFile;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibrary;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistry;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryFactory;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage;
import org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.PluginProvidedJAXRSLibrary;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!--
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
public class JAXRSLibraryRegistryPackageImpl extends EPackageImpl implements
		JAXRSLibraryRegistryPackage {
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass JAXRSLibraryRegistryEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass JAXRSLibraryEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass archiveFileEClass = null;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private EClass pluginProvidedJAXRSLibraryEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the
	 * package package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static factory
	 * method {@link #init init()}, which also performs initialization of the
	 * package, or returns the registered package, if one already exists. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.jst.ws.jaxrs.core.internal.jaxrsibraryregistry.JAXRSLibraryRegistryPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private JAXRSLibraryRegistryPackageImpl() {
		super(eNS_URI, JAXRSLibraryRegistryFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model,
	 * and for any others upon which it depends. Simple dependencies are
	 * satisfied by calling this method on all dependent packages before doing
	 * anything else. This method drives initialization for interdependent
	 * packages directly, in parallel with this package, itself.
	 * <p>
	 * Of this package and its interdependencies, all packages which have not
	 * yet been registered by their URI values are first created and registered.
	 * The packages are then initialized in two steps: meta-model objects for
	 * all of the packages are created before any are initialized, since one
	 * package's meta-model objects may refer to those of another.
	 * <p>
	 * Invocation of this method will not affect any packages that have already
	 * been initialized. <!-- begin-user-doc -->
	 * 
	 * @return the JAXRS Library registry package <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static JAXRSLibraryRegistryPackage init() {
		if (isInited)
			return (JAXRSLibraryRegistryPackage) EPackage.Registry.INSTANCE
					.getEPackage(JAXRSLibraryRegistryPackage.eNS_URI);

		// Obtain or create and register package
		JAXRSLibraryRegistryPackageImpl theJAXRSLibraryRegistryPackage = (JAXRSLibraryRegistryPackageImpl) (EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI) instanceof JAXRSLibraryRegistryPackageImpl ? EPackage.Registry.INSTANCE
				.getEPackage(eNS_URI)
				: new JAXRSLibraryRegistryPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theJAXRSLibraryRegistryPackage.createPackageContents();

		// Initialize created meta-data
		theJAXRSLibraryRegistryPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theJAXRSLibraryRegistryPackage.freeze();

		return theJAXRSLibraryRegistryPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eclass <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJAXRSLibraryRegistry() {
		return JAXRSLibraryRegistryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eattribute <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJAXRSLibraryRegistry_DefaultImplementationID() {
		return (EAttribute) JAXRSLibraryRegistryEClass.getEStructuralFeatures()
				.get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the ereference <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJAXRSLibraryRegistry_JAXRSLibraries() {
		return (EReference) JAXRSLibraryRegistryEClass.getEStructuralFeatures()
				.get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the ereference <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJAXRSLibraryRegistry_PluginProvidedJAXRSLibraries() {
		return (EReference) JAXRSLibraryRegistryEClass.getEStructuralFeatures()
				.get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eclass <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJAXRSLibrary() {
		return JAXRSLibraryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eattribute <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJAXRSLibrary_ID() {
		return (EAttribute) JAXRSLibraryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eattribute <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJAXRSLibrary_Name() {
		return (EAttribute) JAXRSLibraryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eattribute <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJAXRSLibrary_Deployed() {
		return (EAttribute) JAXRSLibraryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the ereference <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJAXRSLibrary_ArchiveFiles() {
		return (EReference) JAXRSLibraryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eclass <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getArchiveFile() {
		return archiveFileEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eattribute <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getArchiveFile_SourceLocation() {
		return (EAttribute) archiveFileEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eattribute <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getArchiveFile_RelativeToWorkspace() {
		return (EAttribute) archiveFileEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eattribute <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getArchiveFile_RelativeDestLocation() {
		return (EAttribute) archiveFileEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the ereference <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getArchiveFile_JAXRSLibrary() {
		return (EReference) archiveFileEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eclass <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPluginProvidedJAXRSLibrary() {
		return pluginProvidedJAXRSLibraryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the eattribute <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPluginProvidedJAXRSLibrary_PluginID() {
		return (EAttribute) pluginProvidedJAXRSLibraryEClass
				.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EAttribute getPluginProvidedJAXRSLibrary_Label() {
		return (EAttribute) pluginProvidedJAXRSLibraryEClass
				.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * 
	 * @return the JAXRS Library registry factory <!-- end-user-doc -->
	 * @generated
	 */
	public JAXRSLibraryRegistryFactory getJAXRSLibraryRegistryFactory() {
		return (JAXRSLibraryRegistryFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is guarded to
	 * have no affect on any invocation but its first. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		JAXRSLibraryRegistryEClass = createEClass(JAXRS_LIBRARY_REGISTRY);
		createEAttribute(JAXRSLibraryRegistryEClass,
				JAXRS_LIBRARY_REGISTRY__DEFAULT_IMPLEMENTATION_ID);
		createEReference(JAXRSLibraryRegistryEClass,
				JAXRS_LIBRARY_REGISTRY__JAXRS_LIBRARIES);
		createEReference(JAXRSLibraryRegistryEClass,
				JAXRS_LIBRARY_REGISTRY__PLUGIN_PROVIDED_JAXRS_LIBRARIES);

		JAXRSLibraryEClass = createEClass(JAXRS_LIBRARY);
		createEAttribute(JAXRSLibraryEClass, JAXRS_LIBRARY__ID);
		createEAttribute(JAXRSLibraryEClass, JAXRS_LIBRARY__NAME);
		createEAttribute(JAXRSLibraryEClass, JAXRS_LIBRARY__DEPLOYED);
		createEReference(JAXRSLibraryEClass, JAXRS_LIBRARY__ARCHIVE_FILES);

		pluginProvidedJAXRSLibraryEClass = createEClass(PLUGIN_PROVIDED_JAXRS_LIBRARY);
		createEAttribute(pluginProvidedJAXRSLibraryEClass,
				PLUGIN_PROVIDED_JAXRS_LIBRARY__PLUGIN_ID);
		createEAttribute(pluginProvidedJAXRSLibraryEClass,
				PLUGIN_PROVIDED_JAXRS_LIBRARY__LABEL);

		archiveFileEClass = createEClass(ARCHIVE_FILE);
		createEAttribute(archiveFileEClass, ARCHIVE_FILE__RELATIVE_TO_WORKSPACE);
		createEAttribute(archiveFileEClass, ARCHIVE_FILE__SOURCE_LOCATION);
		createEAttribute(archiveFileEClass,
				ARCHIVE_FILE__RELATIVE_DEST_LOCATION);
		createEReference(archiveFileEClass, ARCHIVE_FILE__JAXRS_LIBRARY);

	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Add supertypes to classes
		pluginProvidedJAXRSLibraryEClass.getESuperTypes().add(
				this.getJAXRSLibrary());

		// Initialize classes and features; add operations and parameters
		initEClass(JAXRSLibraryRegistryEClass, JAXRSLibraryRegistry.class,
				"JAXRSLibraryRegistry", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getJAXRSLibraryRegistry_DefaultImplementationID(),
				ecorePackage.getEString(), "DefaultImplementationID", "", 0, 1,
				JAXRSLibraryRegistry.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEReference(getJAXRSLibraryRegistry_JAXRSLibraries(), this
				.getJAXRSLibrary(), null, "JAXRSLibraries", null, 0, -1,
				JAXRSLibraryRegistry.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJAXRSLibraryRegistry_PluginProvidedJAXRSLibraries(),
				this.getPluginProvidedJAXRSLibrary(), null,
				"PluginProvidedJAXRSLibraries", null, 0, -1,
				JAXRSLibraryRegistry.class, IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(JAXRSLibraryRegistryEClass, this
				.getJAXRSLibrary(), "getJAXRSLibraryByID", 1, 1);
		addEParameter(op, ecorePackage.getEString(), "ID", 1, 1);

		op = addEOperation(JAXRSLibraryRegistryEClass,
				ecorePackage.getEEList(), "getJAXRSLibrariesByName", 1, 1);
		addEParameter(op, ecorePackage.getEString(), "name", 1, 1);

		addEOperation(JAXRSLibraryRegistryEClass, ecorePackage.getEEList(),
				"getImplJAXRSLibraries", 1, 1);

		addEOperation(JAXRSLibraryRegistryEClass, ecorePackage.getEEList(),
				"getNonImplJAXRSLibraries", 1, 1);

		addEOperation(JAXRSLibraryRegistryEClass, ecorePackage.getEEList(),
				"getAllJAXRSLibraries", 1, 1);

		op = addEOperation(JAXRSLibraryRegistryEClass, ecorePackage
				.getEBoolean(), "addJAXRSLibrary", 1, 1);
		addEParameter(op, this.getJAXRSLibrary(), "library", 1, 1);

		op = addEOperation(JAXRSLibraryRegistryEClass, ecorePackage
				.getEBoolean(), "removeJAXRSLibrary", 1, 1);
		addEParameter(op, this.getJAXRSLibrary(), "library", 1, 1);

		addEOperation(JAXRSLibraryRegistryEClass, this.getJAXRSLibrary(),
				"getDefaultImplementation", 1, 1);

		op = addEOperation(JAXRSLibraryRegistryEClass, null,
				"setDefaultImplementation");
		addEParameter(op, this.getJAXRSLibrary(), "implementation", 1, 1);

		initEClass(JAXRSLibraryEClass, JAXRSLibrary.class, "JAXRSLibrary",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getJAXRSLibrary_ID(), ecorePackage.getEString(), "ID",
				"", 0, 1, JAXRSLibrary.class, IS_TRANSIENT, !IS_VOLATILE,
				!IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getJAXRSLibrary_Name(), ecorePackage.getEString(),
				"Name", null, 1, 1, JAXRSLibrary.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEAttribute(getJAXRSLibrary_Deployed(), ecorePackage.getEBoolean(),
				"Deployed", "true", 1, 1, JAXRSLibrary.class, !IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
		initEReference(getJAXRSLibrary_ArchiveFiles(), this.getArchiveFile(),
				this.getArchiveFile_JAXRSLibrary(), "ArchiveFiles", null, 0,
				-1, JAXRSLibrary.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(JAXRSLibraryEClass, ecorePackage.getEBoolean(),
				"containsArchiveFile", 1, 1);
		addEParameter(op, ecorePackage.getEString(), "fullPath", 1, 1);

		addEOperation(JAXRSLibraryEClass, this.getJAXRSLibrary(),
				"getWorkingCopy", 1, 1);

		op = addEOperation(JAXRSLibraryEClass, null, "updateValues");
		addEParameter(op, this.getJAXRSLibrary(), "otherLibrary", 1, 1);

		op = addEOperation(JAXRSLibraryEClass, ecorePackage.getEBoolean(),
				"copyTo", 1, 1);
		addEParameter(op, ecorePackage.getEString(), "baseDestLocation", 1, 1);

		addEOperation(JAXRSLibraryEClass, ecorePackage.getEString(),
				"getLabel", 1, 1);

		initEClass(pluginProvidedJAXRSLibraryEClass,
				PluginProvidedJAXRSLibrary.class, "PluginProvidedJAXRSLibrary",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPluginProvidedJAXRSLibrary_PluginID(), ecorePackage
				.getEString(), "pluginID", null, 1, 1,
				PluginProvidedJAXRSLibrary.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);
		initEAttribute(getPluginProvidedJAXRSLibrary_Label(), ecorePackage
				.getEString(), "Label", null, 1, 1,
				PluginProvidedJAXRSLibrary.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED,
				IS_ORDERED);

		initEClass(archiveFileEClass, ArchiveFile.class, "ArchiveFile",
				!IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getArchiveFile_RelativeToWorkspace(), ecorePackage
				.getEBoolean(), "RelativeToWorkspace", "true", 1, 1,
				ArchiveFile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getArchiveFile_SourceLocation(), ecorePackage
				.getEString(), "SourceLocation", null, 1, 1, ArchiveFile.class,
				!IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getArchiveFile_RelativeDestLocation(), ecorePackage
				.getEString(), "RelativeDestLocation", null, 1, 1,
				ArchiveFile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getArchiveFile_JAXRSLibrary(), this.getJAXRSLibrary(),
				this.getJAXRSLibrary_ArchiveFiles(), "JAXRSLibrary", null, 1,
				1, ArchiveFile.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES,
				!IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(archiveFileEClass, ecorePackage.getEString(), "getName",
				1, 1);

		addEOperation(archiveFileEClass, ecorePackage.getEString(), "getPath",
				1, 1);

		addEOperation(archiveFileEClass, ecorePackage.getEBoolean(), "exists",
				1, 1);

		op = addEOperation(archiveFileEClass, ecorePackage.getEBoolean(),
				"equals", 1, 1);
		addEParameter(op, ecorePackage.getEJavaObject(), "object", 1, 1);

		addEOperation(archiveFileEClass, ecorePackage.getEInt(), "hashCode", 1,
				1);

		op = addEOperation(archiveFileEClass, ecorePackage.getEBoolean(),
				"copyTo", 1, 1);
		addEParameter(op, ecorePackage.getEString(), "baseDestLocation", 1, 1);

		addEOperation(archiveFileEClass, ecorePackage.getEString(),
				"getResolvedSourceLocation", 1, 1);

		// Create resource
		createResource(eNS_URI);
	}

} // JAXRSLibraryRegistryPackageImpl
