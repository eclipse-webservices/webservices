/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.mime.internal.impl;

import org.eclipse.wst.wsdl.WSDLPackage;


import org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.wst.wsdl.binding.mime.MIMEContent;
import org.eclipse.wst.wsdl.binding.mime.MIMEFactory;
import org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml;
import org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated;
import org.eclipse.wst.wsdl.binding.mime.MIMEPackage;
import org.eclipse.wst.wsdl.binding.mime.MIMEPart;
import org.eclipse.xsd.impl.XSDPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MIMEPackageImpl extends EPackageImpl implements MIMEPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mimeContentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mimePartEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mimeMultipartRelatedEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mimeMimeXmlEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass imimePartEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass imimeMultipartRelatedEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass imimeContentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass imimeMimeXmlEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType listEDataType = null;

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
	 * @see org.eclipse.wst.wsdl.binding.mime.MIMEPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MIMEPackageImpl() {
		super(eNS_URI, MIMEFactory.eINSTANCE);
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
	public static MIMEPackage init() {
		if (isInited) return (MIMEPackage)EPackage.Registry.INSTANCE.get(MIMEPackage.eNS_URI);

		// Obtain or create and register package.
		MIMEPackageImpl theMIMEPackage = (MIMEPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EPackage ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MIMEPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XSDPackageImpl.init();

		// Obtain or create and register interdependencies
		WSDLPackageImpl theWSDLPackage = (WSDLPackageImpl)(EPackage.Registry.INSTANCE.get(WSDLPackage.eNS_URI) instanceof EPackage ? EPackage.Registry.INSTANCE.get(WSDLPackage.eNS_URI) : WSDLPackageImpl.eINSTANCE);

		// Step 1: create meta-model objects
		theMIMEPackage.createPackageContents();
		theWSDLPackage.createPackageContents();

		// Step 2: complete initialization
		theMIMEPackage.initializePackageContents();
		theWSDLPackage.initializePackageContents();

		return theMIMEPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMIMEContent() {
		return mimeContentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMIMEContent_Type() {
		return (EAttribute)mimeContentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMIMEContent_EPart() {
		return (EReference)mimeContentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMIMEPart() {
		return mimePartEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMIMEMultipartRelated() {
		return mimeMultipartRelatedEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMIMEMultipartRelated_EMIMEPart() {
		return (EReference)mimeMultipartRelatedEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMIMEMimeXml() {
		return mimeMimeXmlEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMIMEMimeXml_EPart() {
		return (EReference)mimeMimeXmlEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIMIMEPart() {
		return imimePartEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIMIMEMultipartRelated() {
		return imimeMultipartRelatedEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIMIMEContent() {
		return imimeContentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIMIMEMimeXml() {
		return imimeMimeXmlEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getList() {
		return listEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MIMEFactory getMIMEFactory() {
		return (MIMEFactory)getEFactoryInstance();
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
		mimeContentEClass = createEClass(MIME_CONTENT);
		createEAttribute(mimeContentEClass, MIME_CONTENT__TYPE);
		createEReference(mimeContentEClass, MIME_CONTENT__EPART);

		mimePartEClass = createEClass(MIME_PART);

		mimeMultipartRelatedEClass = createEClass(MIME_MULTIPART_RELATED);
		createEReference(mimeMultipartRelatedEClass, MIME_MULTIPART_RELATED__EMIME_PART);

		mimeMimeXmlEClass = createEClass(MIME_MIME_XML);
		createEReference(mimeMimeXmlEClass, MIME_MIME_XML__EPART);

		imimePartEClass = createEClass(IMIME_PART);

		imimeMultipartRelatedEClass = createEClass(IMIME_MULTIPART_RELATED);

		imimeContentEClass = createEClass(IMIME_CONTENT);

		imimeMimeXmlEClass = createEClass(IMIME_MIME_XML);

		// Create data types
		listEDataType = createEDataType(LIST);
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
		WSDLPackageImpl theWSDLPackage = (WSDLPackageImpl)EPackage.Registry.INSTANCE.getEPackage(WSDLPackage.eNS_URI);

		// Add supertypes to classes
		mimeContentEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
		mimeContentEClass.getESuperTypes().add(this.getIMIMEContent());
		mimePartEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
		mimePartEClass.getESuperTypes().add(this.getIMIMEPart());
		mimeMultipartRelatedEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
		mimeMultipartRelatedEClass.getESuperTypes().add(this.getIMIMEMultipartRelated());
		mimeMimeXmlEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
		mimeMimeXmlEClass.getESuperTypes().add(this.getIMIMEMimeXml());

		// Initialize classes and features; add operations and parameters
		initEClass(mimeContentEClass, MIMEContent.class, "MIMEContent", !IS_ABSTRACT, !IS_INTERFACE);
		initEAttribute(getMIMEContent_Type(), ecorePackage.getEString(), "type", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED);
		initEReference(getMIMEContent_EPart(), theWSDLPackage.getPart(), null, "ePart", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED);

		EOperation op = addEOperation(mimeContentEClass, null, "setPart");
		addEParameter(op, ecorePackage.getEString(), "part");

		addEOperation(mimeContentEClass, ecorePackage.getEString(), "getPart");

		initEClass(mimePartEClass, MIMEPart.class, "MIMEPart", !IS_ABSTRACT, !IS_INTERFACE);

		op = addEOperation(mimePartEClass, null, "addExtensibilityElement");
		addEParameter(op, theWSDLPackage.getIExtensibilityElement(), "extensibilityElement");

		addEOperation(mimePartEClass, this.getList(), "getExtensibilityElements");

		initEClass(mimeMultipartRelatedEClass, MIMEMultipartRelated.class, "MIMEMultipartRelated", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getMIMEMultipartRelated_EMIMEPart(), this.getMIMEPart(), null, "eMIMEPart", null, 0, -1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED);

		op = addEOperation(mimeMultipartRelatedEClass, null, "addMIMEPart");
		addEParameter(op, this.getIMIMEPart(), "mimePart");

		addEOperation(mimeMultipartRelatedEClass, this.getList(), "getMIMEParts");

		initEClass(mimeMimeXmlEClass, MIMEMimeXml.class, "MIMEMimeXml", !IS_ABSTRACT, !IS_INTERFACE);
		initEReference(getMIMEMimeXml_EPart(), theWSDLPackage.getPart(), null, "ePart", null, 0, 1, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED);

		op = addEOperation(mimeMimeXmlEClass, null, "setPart");
		addEParameter(op, ecorePackage.getEString(), "part");

		addEOperation(mimeMimeXmlEClass, ecorePackage.getEString(), "getPart");

		initEClass(imimePartEClass, javax.wsdl.extensions.mime.MIMEPart.class, "IMIMEPart", IS_ABSTRACT, IS_INTERFACE);

		initEClass(imimeMultipartRelatedEClass, javax.wsdl.extensions.mime.MIMEMultipartRelated.class, "IMIMEMultipartRelated", IS_ABSTRACT, IS_INTERFACE);

		initEClass(imimeContentEClass, javax.wsdl.extensions.mime.MIMEContent.class, "IMIMEContent", IS_ABSTRACT, IS_INTERFACE);

		initEClass(imimeMimeXmlEClass, javax.wsdl.extensions.mime.MIMEMimeXml.class, "IMIMEMimeXml", IS_ABSTRACT, IS_INTERFACE);

		// Initialize data types
		initEDataType(listEDataType, List.class, "List", IS_SERIALIZABLE);

		// Create resource
		createResource(eNS_URI);
	}
} //MIMEPackageImpl
