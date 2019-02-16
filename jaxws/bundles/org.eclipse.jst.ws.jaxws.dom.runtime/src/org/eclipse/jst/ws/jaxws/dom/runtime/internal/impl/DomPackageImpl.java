/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebType;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DomPackageImpl extends EPackageImpl implements DomPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass idomEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iJavaWebServiceElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iServiceEndpointInterfaceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iWebMethodEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iWebParamEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iWebServiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iWebServiceProjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass iWebTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum webParamKindEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum soapBindingStyleEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum soapBindingUseEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum soapBindingParameterStyleEEnum = null;

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
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DomPackageImpl() {
		super(eNS_URI, DomFactory.eINSTANCE);
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
	public static DomPackage init() {
		if (isInited) return (DomPackage)EPackage.Registry.INSTANCE.getEPackage(DomPackage.eNS_URI);

		// Obtain or create and register package
		DomPackageImpl theDomPackage = (DomPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof DomPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new DomPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theDomPackage.createPackageContents();

		// Initialize created meta-data
		theDomPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDomPackage.freeze();

		return theDomPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIDOM() {
		return idomEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIDOM_WebServiceProjects() {
		return (EReference)idomEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIJavaWebServiceElement() {
		return iJavaWebServiceElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIJavaWebServiceElement_Implementation() {
		return (EAttribute)iJavaWebServiceElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIJavaWebServiceElement_Name() {
		return (EAttribute)iJavaWebServiceElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIServiceEndpointInterface() {
		return iServiceEndpointInterfaceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIServiceEndpointInterface_Implicit() {
		return (EAttribute)iServiceEndpointInterfaceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIServiceEndpointInterface_ImplementingWebServices() {
		return (EReference)iServiceEndpointInterfaceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIServiceEndpointInterface_WebMethods() {
		return (EReference)iServiceEndpointInterfaceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIServiceEndpointInterface_TargetNamespace() {
		return (EAttribute)iServiceEndpointInterfaceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIServiceEndpointInterface_SoapBindingStyle() {
		return (EAttribute)iServiceEndpointInterfaceEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIServiceEndpointInterface_SoapBindingUse() {
		return (EAttribute)iServiceEndpointInterfaceEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIServiceEndpointInterface_SoapBindingParameterStyle() {
		return (EAttribute)iServiceEndpointInterfaceEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIWebMethod() {
		return iWebMethodEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIWebMethod_Parameters() {
		return (EReference)iWebMethodEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebMethod_Excluded() {
		return (EAttribute)iWebMethodEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebMethod_SoapBindingStyle() {
		return (EAttribute)iWebMethodEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebMethod_SoapBindingUse() {
		return (EAttribute)iWebMethodEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebMethod_SoapBindingParameterStyle() {
		return (EAttribute)iWebMethodEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIWebParam() {
		return iWebParamEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebParam_Kind() {
		return (EAttribute)iWebParamEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebParam_TypeName() {
		return (EAttribute)iWebParamEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebParam_PartName() {
		return (EAttribute)iWebParamEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebParam_TargetNamespace() {
		return (EAttribute)iWebParamEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebParam_Header() {
		return (EAttribute)iWebParamEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIWebService() {
		return iWebServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIWebService_ServiceEndpoint() {
		return (EReference)iWebServiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebService_TargetNamespace() {
		return (EAttribute)iWebServiceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebService_PortName() {
		return (EAttribute)iWebServiceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebService_WsdlLocation() {
		return (EAttribute)iWebServiceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIWebServiceProject() {
		return iWebServiceProjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIWebServiceProject_WebServices() {
		return (EReference)iWebServiceProjectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getIWebServiceProject_ServiceEndpointInterfaces() {
		return (EReference)iWebServiceProjectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getIWebServiceProject_Name() {
		return (EAttribute)iWebServiceProjectEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getIWebType() {
		return iWebTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getWebParamKind() {
		return webParamKindEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSOAPBindingStyle() {
		return soapBindingStyleEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSOAPBindingUse() {
		return soapBindingUseEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getSOAPBindingParameterStyle() {
		return soapBindingParameterStyleEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DomFactory getDomFactory() {
		return (DomFactory)getEFactoryInstance();
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
		idomEClass = createEClass(IDOM);
		createEReference(idomEClass, IDOM__WEB_SERVICE_PROJECTS);

		iJavaWebServiceElementEClass = createEClass(IJAVA_WEB_SERVICE_ELEMENT);
		createEAttribute(iJavaWebServiceElementEClass, IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION);
		createEAttribute(iJavaWebServiceElementEClass, IJAVA_WEB_SERVICE_ELEMENT__NAME);

		iServiceEndpointInterfaceEClass = createEClass(ISERVICE_ENDPOINT_INTERFACE);
		createEAttribute(iServiceEndpointInterfaceEClass, ISERVICE_ENDPOINT_INTERFACE__IMPLICIT);
		createEReference(iServiceEndpointInterfaceEClass, ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES);
		createEReference(iServiceEndpointInterfaceEClass, ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS);
		createEAttribute(iServiceEndpointInterfaceEClass, ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE);
		createEAttribute(iServiceEndpointInterfaceEClass, ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_STYLE);
		createEAttribute(iServiceEndpointInterfaceEClass, ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_USE);
		createEAttribute(iServiceEndpointInterfaceEClass, ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_PARAMETER_STYLE);

		iWebMethodEClass = createEClass(IWEB_METHOD);
		createEReference(iWebMethodEClass, IWEB_METHOD__PARAMETERS);
		createEAttribute(iWebMethodEClass, IWEB_METHOD__EXCLUDED);
		createEAttribute(iWebMethodEClass, IWEB_METHOD__SOAP_BINDING_STYLE);
		createEAttribute(iWebMethodEClass, IWEB_METHOD__SOAP_BINDING_USE);
		createEAttribute(iWebMethodEClass, IWEB_METHOD__SOAP_BINDING_PARAMETER_STYLE);

		iWebParamEClass = createEClass(IWEB_PARAM);
		createEAttribute(iWebParamEClass, IWEB_PARAM__KIND);
		createEAttribute(iWebParamEClass, IWEB_PARAM__TYPE_NAME);
		createEAttribute(iWebParamEClass, IWEB_PARAM__PART_NAME);
		createEAttribute(iWebParamEClass, IWEB_PARAM__TARGET_NAMESPACE);
		createEAttribute(iWebParamEClass, IWEB_PARAM__HEADER);

		iWebServiceEClass = createEClass(IWEB_SERVICE);
		createEReference(iWebServiceEClass, IWEB_SERVICE__SERVICE_ENDPOINT);
		createEAttribute(iWebServiceEClass, IWEB_SERVICE__TARGET_NAMESPACE);
		createEAttribute(iWebServiceEClass, IWEB_SERVICE__PORT_NAME);
		createEAttribute(iWebServiceEClass, IWEB_SERVICE__WSDL_LOCATION);

		iWebServiceProjectEClass = createEClass(IWEB_SERVICE_PROJECT);
		createEReference(iWebServiceProjectEClass, IWEB_SERVICE_PROJECT__WEB_SERVICES);
		createEReference(iWebServiceProjectEClass, IWEB_SERVICE_PROJECT__SERVICE_ENDPOINT_INTERFACES);
		createEAttribute(iWebServiceProjectEClass, IWEB_SERVICE_PROJECT__NAME);

		iWebTypeEClass = createEClass(IWEB_TYPE);

		// Create enums
		webParamKindEEnum = createEEnum(WEB_PARAM_KIND);
		soapBindingStyleEEnum = createEEnum(SOAP_BINDING_STYLE);
		soapBindingUseEEnum = createEEnum(SOAP_BINDING_USE);
		soapBindingParameterStyleEEnum = createEEnum(SOAP_BINDING_PARAMETER_STYLE);
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

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		iServiceEndpointInterfaceEClass.getESuperTypes().add(this.getIJavaWebServiceElement());
		iWebMethodEClass.getESuperTypes().add(this.getIJavaWebServiceElement());
		iWebParamEClass.getESuperTypes().add(this.getIJavaWebServiceElement());
		iWebServiceEClass.getESuperTypes().add(this.getIJavaWebServiceElement());
		iWebTypeEClass.getESuperTypes().add(this.getIJavaWebServiceElement());

		// Initialize classes and features; add operations and parameters
		initEClass(idomEClass, org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM.class, "IDOM", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getIDOM_WebServiceProjects(), this.getIWebServiceProject(), null, "webServiceProjects", null, 0, -1, org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(iJavaWebServiceElementEClass, IJavaWebServiceElement.class, "IJavaWebServiceElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getIJavaWebServiceElement_Implementation(), ecorePackage.getEString(), "implementation", null, 1, 1, IJavaWebServiceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIJavaWebServiceElement_Name(), ecorePackage.getEString(), "name", null, 1, 1, IJavaWebServiceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(iServiceEndpointInterfaceEClass, IServiceEndpointInterface.class, "IServiceEndpointInterface", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getIServiceEndpointInterface_Implicit(), ecorePackage.getEBoolean(), "implicit", null, 1, 1, IServiceEndpointInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getIServiceEndpointInterface_ImplementingWebServices(), this.getIWebService(), this.getIWebService_ServiceEndpoint(), "implementingWebServices", null, 1, -1, IServiceEndpointInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getIServiceEndpointInterface_WebMethods(), this.getIWebMethod(), null, "webMethods", null, 1, -1, IServiceEndpointInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIServiceEndpointInterface_TargetNamespace(), ecorePackage.getEString(), "targetNamespace", null, 1, 1, IServiceEndpointInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIServiceEndpointInterface_SoapBindingStyle(), this.getSOAPBindingStyle(), "soapBindingStyle", "DOCUMENT", 1, 1, IServiceEndpointInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute(getIServiceEndpointInterface_SoapBindingUse(), this.getSOAPBindingUse(), "soapBindingUse", "LITERAL", 1, 1, IServiceEndpointInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute(getIServiceEndpointInterface_SoapBindingParameterStyle(), this.getSOAPBindingParameterStyle(), "soapBindingParameterStyle", "WRAPPED", 1, 1, IServiceEndpointInterface.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass(iWebMethodEClass, IWebMethod.class, "IWebMethod", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getIWebMethod_Parameters(), this.getIWebParam(), null, "parameters", null, 1, -1, IWebMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIWebMethod_Excluded(), ecorePackage.getEBoolean(), "excluded", null, 1, 1, IWebMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIWebMethod_SoapBindingStyle(), this.getSOAPBindingStyle(), "soapBindingStyle", "DOCUMENT", 1, 1, IWebMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute(getIWebMethod_SoapBindingUse(), this.getSOAPBindingUse(), "soapBindingUse", "LITERAL", 1, 1, IWebMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$
		initEAttribute(getIWebMethod_SoapBindingParameterStyle(), this.getSOAPBindingParameterStyle(), "soapBindingParameterStyle", "WRAPPED", 1, 1, IWebMethod.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass(iWebParamEClass, IWebParam.class, "IWebParam", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getIWebParam_Kind(), this.getWebParamKind(), "kind", null, 1, 1, IWebParam.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIWebParam_TypeName(), ecorePackage.getEString(), "typeName", null, 1, 1, IWebParam.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIWebParam_PartName(), ecorePackage.getEString(), "partName", null, 1, 1, IWebParam.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIWebParam_TargetNamespace(), ecorePackage.getEString(), "targetNamespace", null, 1, 1, IWebParam.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIWebParam_Header(), ecorePackage.getEBoolean(), "header", "false", 1, 1, IWebParam.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$ //$NON-NLS-2$

		initEClass(iWebServiceEClass, IWebService.class, "IWebService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getIWebService_ServiceEndpoint(), this.getIServiceEndpointInterface(), this.getIServiceEndpointInterface_ImplementingWebServices(), "serviceEndpoint", null, 0, 1, IWebService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIWebService_TargetNamespace(), ecorePackage.getEString(), "targetNamespace", null, 1, 1, IWebService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIWebService_PortName(), ecorePackage.getEString(), "portName", null, 1, 1, IWebService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIWebService_WsdlLocation(), ecorePackage.getEString(), "wsdlLocation", null, 0, 1, IWebService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(iWebServiceProjectEClass, IWebServiceProject.class, "IWebServiceProject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEReference(getIWebServiceProject_WebServices(), this.getIWebService(), null, "webServices", null, 0, -1, IWebServiceProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getIWebServiceProject_ServiceEndpointInterfaces(), this.getIServiceEndpointInterface(), null, "serviceEndpointInterfaces", null, 0, -1, IWebServiceProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getIWebServiceProject_Name(), ecorePackage.getEString(), "name", null, 1, 1, IWebServiceProject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(iWebTypeEClass, IWebType.class, "IWebType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

		// Initialize enums and add enum literals
		initEEnum(webParamKindEEnum, WebParamKind.class, "WebParamKind"); //$NON-NLS-1$
		addEEnumLiteral(webParamKindEEnum, WebParamKind.IN);
		addEEnumLiteral(webParamKindEEnum, WebParamKind.INOUT);
		addEEnumLiteral(webParamKindEEnum, WebParamKind.OUT);

		initEEnum(soapBindingStyleEEnum, SOAPBindingStyle.class, "SOAPBindingStyle"); //$NON-NLS-1$
		addEEnumLiteral(soapBindingStyleEEnum, SOAPBindingStyle.DOCUMENT);
		addEEnumLiteral(soapBindingStyleEEnum, SOAPBindingStyle.RPC);

		initEEnum(soapBindingUseEEnum, SOAPBindingUse.class, "SOAPBindingUse"); //$NON-NLS-1$
		addEEnumLiteral(soapBindingUseEEnum, SOAPBindingUse.LITERAL);
		addEEnumLiteral(soapBindingUseEEnum, SOAPBindingUse.ENCODED);

		initEEnum(soapBindingParameterStyleEEnum, SOAPBindingParameterStyle.class, "SOAPBindingParameterStyle"); //$NON-NLS-1$
		addEEnumLiteral(soapBindingParameterStyleEEnum, SOAPBindingParameterStyle.WRAPPED);
		addEEnumLiteral(soapBindingParameterStyleEEnum, SOAPBindingParameterStyle.BARE);

		// Create resource
		createResource(eNS_URI);
	}

} //DomPackageImpl
