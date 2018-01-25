/**
 * <copyright>
 * </copyright>
 *
 * $Id: UDDIRegistryPackageImpl.java,v 1.2 2005/12/03 04:06:50 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.eclipse.emf.ecore.xml.type.impl.XMLTypePackageImpl;

import org.eclipse.wst.ws.internal.model.v10.registry.RegistryPackage;

import org.eclipse.wst.ws.internal.model.v10.registry.impl.RegistryPackageImpl;

import org.eclipse.wst.ws.internal.model.v10.rtindex.RTIndexPackage;

import org.eclipse.wst.ws.internal.model.v10.rtindex.impl.RTIndexPackageImpl;

import org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage;

import org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.TaxonomyPackageImpl;

import org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryFactory;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UDDIRegistryPackageImpl extends EPackageImpl implements UDDIRegistryPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass documentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass taxonomiesEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass uddiRegistryEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private UDDIRegistryPackageImpl() {
		super(eNS_URI, UDDIRegistryFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static UDDIRegistryPackage init() {
		if (isInited) return (UDDIRegistryPackage)EPackage.Registry.INSTANCE.getEPackage(UDDIRegistryPackage.eNS_URI);

		// Obtain or create and register package
		UDDIRegistryPackageImpl theUDDIRegistryPackage = (UDDIRegistryPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof UDDIRegistryPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new UDDIRegistryPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackageImpl.init();

		// Obtain or create and register interdependencies
		RTIndexPackageImpl theRTIndexPackage = (RTIndexPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(RTIndexPackage.eNS_URI) instanceof RTIndexPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(RTIndexPackage.eNS_URI) : RTIndexPackageImpl.eINSTANCE);
		TaxonomyPackageImpl theTaxonomyPackage = (TaxonomyPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(TaxonomyPackage.eNS_URI) instanceof TaxonomyPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(TaxonomyPackage.eNS_URI) : TaxonomyPackageImpl.eINSTANCE);
		RegistryPackageImpl theRegistryPackage = (RegistryPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(RegistryPackage.eNS_URI) instanceof RegistryPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(RegistryPackage.eNS_URI) : RegistryPackageImpl.eINSTANCE);

		// Create package meta-data objects
		theUDDIRegistryPackage.createPackageContents();
		theRTIndexPackage.createPackageContents();
		theTaxonomyPackage.createPackageContents();
		theRegistryPackage.createPackageContents();

		// Initialize created meta-data
		theUDDIRegistryPackage.initializePackageContents();
		theRTIndexPackage.initializePackageContents();
		theTaxonomyPackage.initializePackageContents();
		theRegistryPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theUDDIRegistryPackage.freeze();

		return theUDDIRegistryPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDocumentRoot() {
		return documentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Mixed() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_XMLNSPrefixMap() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_XSISchemaLocation() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Taxonomies() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_UddiRegistry() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTaxonomies() {
		return taxonomiesEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTaxonomies_Taxonomy() {
		return (EReference)taxonomiesEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUDDIRegistry() {
		return uddiRegistryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUDDIRegistry_Version() {
		return (EAttribute)uddiRegistryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUDDIRegistry_DiscoveryURL() {
		return (EAttribute)uddiRegistryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUDDIRegistry_PublicationURL() {
		return (EAttribute)uddiRegistryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUDDIRegistry_SecuredDiscoveryURL() {
		return (EAttribute)uddiRegistryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUDDIRegistry_SecuredPublicationURL() {
		return (EAttribute)uddiRegistryEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUDDIRegistry_DefaultLogin() {
		return (EAttribute)uddiRegistryEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUDDIRegistry_DefaultPassword() {
		return (EAttribute)uddiRegistryEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUDDIRegistry_Taxonomies() {
		return (EReference)uddiRegistryEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UDDIRegistryFactory getUDDIRegistryFactory() {
		return (UDDIRegistryFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__TAXONOMIES);
		createEReference(documentRootEClass, DOCUMENT_ROOT__UDDI_REGISTRY);

		taxonomiesEClass = createEClass(TAXONOMIES);
		createEReference(taxonomiesEClass, TAXONOMIES__TAXONOMY);

		uddiRegistryEClass = createEClass(UDDI_REGISTRY);
		createEAttribute(uddiRegistryEClass, UDDI_REGISTRY__VERSION);
		createEAttribute(uddiRegistryEClass, UDDI_REGISTRY__DISCOVERY_URL);
		createEAttribute(uddiRegistryEClass, UDDI_REGISTRY__PUBLICATION_URL);
		createEAttribute(uddiRegistryEClass, UDDI_REGISTRY__SECURED_DISCOVERY_URL);
		createEAttribute(uddiRegistryEClass, UDDI_REGISTRY__SECURED_PUBLICATION_URL);
		createEAttribute(uddiRegistryEClass, UDDI_REGISTRY__DEFAULT_LOGIN);
		createEAttribute(uddiRegistryEClass, UDDI_REGISTRY__DEFAULT_PASSWORD);
		createEReference(uddiRegistryEClass, UDDI_REGISTRY__TAXONOMIES);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		TaxonomyPackageImpl theTaxonomyPackage = (TaxonomyPackageImpl)EPackage.Registry.INSTANCE.getEPackage(TaxonomyPackage.eNS_URI);
		RegistryPackageImpl theRegistryPackage = (RegistryPackageImpl)EPackage.Registry.INSTANCE.getEPackage(RegistryPackage.eNS_URI);
		XMLTypePackageImpl theXMLTypePackage = (XMLTypePackageImpl)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Add supertypes to classes
		uddiRegistryEClass.getESuperTypes().add(theRegistryPackage.getRegistry());

		// Initialize classes and features; add operations and parameters
		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Taxonomies(), this.getTaxonomies(), null, "taxonomies", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_UddiRegistry(), this.getUDDIRegistry(), null, "uddiRegistry", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(taxonomiesEClass, Taxonomies.class, "Taxonomies", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTaxonomies_Taxonomy(), theTaxonomyPackage.getTaxonomy(), null, "taxonomy", null, 0, -1, Taxonomies.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(uddiRegistryEClass, UDDIRegistry.class, "UDDIRegistry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUDDIRegistry_Version(), theXMLTypePackage.getString(), "version", null, 1, 1, UDDIRegistry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUDDIRegistry_DiscoveryURL(), theXMLTypePackage.getAnyURI(), "discoveryURL", null, 1, 1, UDDIRegistry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUDDIRegistry_PublicationURL(), theXMLTypePackage.getAnyURI(), "publicationURL", null, 1, 1, UDDIRegistry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUDDIRegistry_SecuredDiscoveryURL(), theXMLTypePackage.getAnyURI(), "securedDiscoveryURL", null, 1, 1, UDDIRegistry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUDDIRegistry_SecuredPublicationURL(), theXMLTypePackage.getAnyURI(), "securedPublicationURL", null, 1, 1, UDDIRegistry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUDDIRegistry_DefaultLogin(), theXMLTypePackage.getString(), "defaultLogin", null, 1, 1, UDDIRegistry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUDDIRegistry_DefaultPassword(), theXMLTypePackage.getString(), "defaultPassword", null, 1, 1, UDDIRegistry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUDDIRegistry_Taxonomies(), this.getTaxonomies(), null, "taxonomies", null, 0, 1, UDDIRegistry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";			
		addAnnotation
		  (documentRootEClass, 
		   source, 
		   new String[] {
			 "name", "",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_XMLNSPrefixMap(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xmlns:prefix"
		   });		
		addAnnotation
		  (getDocumentRoot_XSISchemaLocation(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xsi:schemaLocation"
		   });		
		addAnnotation
		  (getDocumentRoot_Taxonomies(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "taxonomies",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_UddiRegistry(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "uddiRegistry",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (taxonomiesEClass, 
		   source, 
		   new String[] {
			 "name", "Taxonomies",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getTaxonomies_Taxonomy(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "taxonomy",
			 "namespace", "http://eclipse.org/wst/ws/internal/model/v10/taxonomy"
		   });		
		addAnnotation
		  (uddiRegistryEClass, 
		   source, 
		   new String[] {
			 "name", "UDDIRegistry",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getUDDIRegistry_Version(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "version"
		   });		
		addAnnotation
		  (getUDDIRegistry_DiscoveryURL(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "discoveryURL"
		   });		
		addAnnotation
		  (getUDDIRegistry_PublicationURL(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "publicationURL"
		   });		
		addAnnotation
		  (getUDDIRegistry_SecuredDiscoveryURL(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "securedDiscoveryURL"
		   });		
		addAnnotation
		  (getUDDIRegistry_SecuredPublicationURL(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "securedPublicationURL"
		   });		
		addAnnotation
		  (getUDDIRegistry_DefaultLogin(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "defaultLogin"
		   });		
		addAnnotation
		  (getUDDIRegistry_DefaultPassword(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "defaultPassword"
		   });		
		addAnnotation
		  (getUDDIRegistry_Taxonomies(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "taxonomies"
		   });
	}

} //UDDIRegistryPackageImpl
