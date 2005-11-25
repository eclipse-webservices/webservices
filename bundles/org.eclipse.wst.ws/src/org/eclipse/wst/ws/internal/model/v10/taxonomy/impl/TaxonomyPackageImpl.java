/**
 * <copyright>
 * </copyright>
 *
 * $Id: TaxonomyPackageImpl.java,v 1.1 2005/11/25 21:54:35 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.taxonomy.impl;

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

import org.eclipse.wst.ws.internal.model.v10.taxonomy.Category;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.DocumentRoot;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Name;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyFactory;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage;

import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage;

import org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TaxonomyPackageImpl extends EPackageImpl implements TaxonomyPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass categoryEClass = null;

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
	private EClass nameEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass taxonomyEClass = null;

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
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TaxonomyPackageImpl() {
		super(eNS_URI, TaxonomyFactory.eINSTANCE);
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
	public static TaxonomyPackage init() {
		if (isInited) return (TaxonomyPackage)EPackage.Registry.INSTANCE.getEPackage(TaxonomyPackage.eNS_URI);

		// Obtain or create and register package
		TaxonomyPackageImpl theTaxonomyPackage = (TaxonomyPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof TaxonomyPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new TaxonomyPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackageImpl.init();

		// Obtain or create and register interdependencies
		RTIndexPackageImpl theRTIndexPackage = (RTIndexPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(RTIndexPackage.eNS_URI) instanceof RTIndexPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(RTIndexPackage.eNS_URI) : RTIndexPackageImpl.eINSTANCE);
		RegistryPackageImpl theRegistryPackage = (RegistryPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(RegistryPackage.eNS_URI) instanceof RegistryPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(RegistryPackage.eNS_URI) : RegistryPackageImpl.eINSTANCE);
		UDDIRegistryPackageImpl theUDDIRegistryPackage = (UDDIRegistryPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(UDDIRegistryPackage.eNS_URI) instanceof UDDIRegistryPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(UDDIRegistryPackage.eNS_URI) : UDDIRegistryPackageImpl.eINSTANCE);

		// Create package meta-data objects
		theTaxonomyPackage.createPackageContents();
		theRTIndexPackage.createPackageContents();
		theRegistryPackage.createPackageContents();
		theUDDIRegistryPackage.createPackageContents();

		// Initialize created meta-data
		theTaxonomyPackage.initializePackageContents();
		theRTIndexPackage.initializePackageContents();
		theRegistryPackage.initializePackageContents();
		theUDDIRegistryPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTaxonomyPackage.freeze();

		return theTaxonomyPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCategory() {
		return categoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCategory_Nlname() {
		return (EReference)categoryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCategory_Category() {
		return (EReference)categoryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCategory_Code() {
		return (EAttribute)categoryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCategory_Name() {
		return (EAttribute)categoryEClass.getEStructuralFeatures().get(3);
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
	public EReference getDocumentRoot_Category() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Name() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Taxonomy() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getName_() {
		return nameEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getName_Value() {
		return (EAttribute)nameEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getName_Lang() {
		return (EAttribute)nameEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTaxonomy() {
		return taxonomyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTaxonomy_Nlname() {
		return (EReference)taxonomyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTaxonomy_Category() {
		return (EReference)taxonomyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaxonomy_Id() {
		return (EAttribute)taxonomyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaxonomy_Location() {
		return (EAttribute)taxonomyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaxonomy_Name() {
		return (EAttribute)taxonomyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTaxonomy_Ref() {
		return (EAttribute)taxonomyEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TaxonomyFactory getTaxonomyFactory() {
		return (TaxonomyFactory)getEFactoryInstance();
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
		categoryEClass = createEClass(CATEGORY);
		createEReference(categoryEClass, CATEGORY__NLNAME);
		createEReference(categoryEClass, CATEGORY__CATEGORY);
		createEAttribute(categoryEClass, CATEGORY__CODE);
		createEAttribute(categoryEClass, CATEGORY__NAME);

		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__CATEGORY);
		createEReference(documentRootEClass, DOCUMENT_ROOT__NAME);
		createEReference(documentRootEClass, DOCUMENT_ROOT__TAXONOMY);

		nameEClass = createEClass(NAME);
		createEAttribute(nameEClass, NAME__VALUE);
		createEAttribute(nameEClass, NAME__LANG);

		taxonomyEClass = createEClass(TAXONOMY);
		createEReference(taxonomyEClass, TAXONOMY__NLNAME);
		createEReference(taxonomyEClass, TAXONOMY__CATEGORY);
		createEAttribute(taxonomyEClass, TAXONOMY__ID);
		createEAttribute(taxonomyEClass, TAXONOMY__LOCATION);
		createEAttribute(taxonomyEClass, TAXONOMY__NAME);
		createEAttribute(taxonomyEClass, TAXONOMY__REF);
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
		XMLTypePackageImpl theXMLTypePackage = (XMLTypePackageImpl)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(categoryEClass, Category.class, "Category", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getCategory_Nlname(), this.getName_(), null, "nlname", null, 0, -1, Category.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCategory_Category(), this.getCategory(), null, "category", null, 0, -1, Category.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCategory_Code(), theXMLTypePackage.getString(), "code", null, 0, 1, Category.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCategory_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, Category.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Category(), this.getCategory(), null, "category", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Name(), this.getName_(), null, "name", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Taxonomy(), this.getTaxonomy(), null, "taxonomy", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(nameEClass, Name.class, "Name", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getName_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, Name.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getName_Lang(), theXMLTypePackage.getLanguage(), "lang", null, 0, 1, Name.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(taxonomyEClass, Taxonomy.class, "Taxonomy", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTaxonomy_Nlname(), this.getName_(), null, "nlname", null, 0, -1, Taxonomy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTaxonomy_Category(), this.getCategory(), null, "category", null, 0, -1, Taxonomy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaxonomy_Id(), theXMLTypePackage.getAnyURI(), "id", null, 1, 1, Taxonomy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaxonomy_Location(), theXMLTypePackage.getAnyURI(), "location", null, 0, 1, Taxonomy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaxonomy_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, Taxonomy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTaxonomy_Ref(), theXMLTypePackage.getAnyURI(), "ref", null, 0, 1, Taxonomy.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
		  (categoryEClass, 
		   source, 
		   new String[] {
			 "name", "Category",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getCategory_Nlname(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "nlname"
		   });		
		addAnnotation
		  (getCategory_Category(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "category"
		   });		
		addAnnotation
		  (getCategory_Code(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "code"
		   });		
		addAnnotation
		  (getCategory_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
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
		  (getDocumentRoot_Category(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "category",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Name(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "name",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Taxonomy(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "taxonomy",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (nameEClass, 
		   source, 
		   new String[] {
			 "name", "Name",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getName_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getName_Lang(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "lang",
			 "namespace", "http://www.w3.org/XML/1998/namespace"
		   });		
		addAnnotation
		  (taxonomyEClass, 
		   source, 
		   new String[] {
			 "name", "Taxonomy",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getTaxonomy_Nlname(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "nlname"
		   });		
		addAnnotation
		  (getTaxonomy_Category(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "category"
		   });		
		addAnnotation
		  (getTaxonomy_Id(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "id"
		   });		
		addAnnotation
		  (getTaxonomy_Location(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "location"
		   });		
		addAnnotation
		  (getTaxonomy_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (getTaxonomy_Ref(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "ref"
		   });
	}

} //TaxonomyPackageImpl
