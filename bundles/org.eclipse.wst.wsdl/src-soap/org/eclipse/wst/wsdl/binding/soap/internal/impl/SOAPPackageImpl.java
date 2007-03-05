/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap.internal.impl;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeader;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPOperation;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl;
import org.eclipse.xsd.impl.XSDPackageImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SOAPPackageImpl extends EPackageImpl implements SOAPPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapBindingEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapBodyEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapHeaderBaseEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapFaultEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapAddressEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapHeaderFaultEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass soapHeaderEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType iStringEDataType = null;

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
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private SOAPPackageImpl()
  {
    super(eNS_URI, SOAPFactory.eINSTANCE);
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
  public static SOAPPackage init()
  {
    if (isInited)
      return (SOAPPackage)EPackage.Registry.INSTANCE.get(SOAPPackage.eNS_URI);

    // Obtain or create and register package.
    SOAPPackageImpl theSOAPPackage = (SOAPPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof EPackage
      ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SOAPPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    XSDPackageImpl.init();

    // Obtain or create and register interdependencies
    WSDLPackageImpl theWSDLPackage = (WSDLPackageImpl)(EPackage.Registry.INSTANCE.get(WSDLPackage.eNS_URI) instanceof EPackage
      ? EPackage.Registry.INSTANCE.get(WSDLPackage.eNS_URI) : WSDLPackageImpl.eINSTANCE);

    // Step 1: create meta-model objects
    theSOAPPackage.createPackageContents();
    theWSDLPackage.createPackageContents();

    // Step 2: complete initialization
    theSOAPPackage.initializePackageContents();
    theWSDLPackage.initializePackageContents();

    return theSOAPPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPBinding()
  {
    return soapBindingEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPBinding_TransportURI()
  {
    return (EAttribute)soapBindingEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPBinding_Style()
  {
    return (EAttribute)soapBindingEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPBody()
  {
    return soapBodyEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPBody_Use()
  {
    return (EAttribute)soapBodyEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPBody_NamespaceURI()
  {
    return (EAttribute)soapBodyEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPBody_EncodingStyles()
  {
    return (EAttribute)soapBodyEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSOAPBody_Parts()
  {
    return (EReference)soapBodyEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPHeaderBase()
  {
    return soapHeaderBaseEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPHeaderBase_Use()
  {
    return (EAttribute)soapHeaderBaseEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPHeaderBase_NamespaceURI()
  {
    return (EAttribute)soapHeaderBaseEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPHeaderBase_EncodingStyles()
  {
    return (EAttribute)soapHeaderBaseEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSOAPHeaderBase_Message()
  {
    return (EReference)soapHeaderBaseEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSOAPHeaderBase_Part()
  {
    return (EReference)soapHeaderBaseEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPFault()
  {
    return soapFaultEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPFault_Use()
  {
    return (EAttribute)soapFaultEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPFault_NamespaceURI()
  {
    return (EAttribute)soapFaultEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPFault_EncodingStyles()
  {
    return (EAttribute)soapFaultEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPOperation()
  {
    return soapOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPOperation_SoapActionURI()
  {
    return (EAttribute)soapOperationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPOperation_Style()
  {
    return (EAttribute)soapOperationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPAddress()
  {
    return soapAddressEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSOAPAddress_LocationURI()
  {
    return (EAttribute)soapAddressEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPHeaderFault()
  {
    return soapHeaderFaultEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSOAPHeader()
  {
    return soapHeaderEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSOAPHeader_HeaderFaults()
  {
    return (EReference)soapHeaderEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getIString()
  {
    return iStringEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SOAPFactory getSOAPFactory()
  {
    return (SOAPFactory)getEFactoryInstance();
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
  public void createPackageContents()
  {
    if (isCreated)
      return;
    isCreated = true;

    // Create classes and their features
    soapBindingEClass = createEClass(SOAP_BINDING);
    createEAttribute(soapBindingEClass, SOAP_BINDING__TRANSPORT_URI);
    createEAttribute(soapBindingEClass, SOAP_BINDING__STYLE);

    soapBodyEClass = createEClass(SOAP_BODY);
    createEAttribute(soapBodyEClass, SOAP_BODY__USE);
    createEAttribute(soapBodyEClass, SOAP_BODY__NAMESPACE_URI);
    createEAttribute(soapBodyEClass, SOAP_BODY__ENCODING_STYLES);
    createEReference(soapBodyEClass, SOAP_BODY__PARTS);

    soapHeaderBaseEClass = createEClass(SOAP_HEADER_BASE);
    createEAttribute(soapHeaderBaseEClass, SOAP_HEADER_BASE__USE);
    createEAttribute(soapHeaderBaseEClass, SOAP_HEADER_BASE__NAMESPACE_URI);
    createEAttribute(soapHeaderBaseEClass, SOAP_HEADER_BASE__ENCODING_STYLES);
    createEReference(soapHeaderBaseEClass, SOAP_HEADER_BASE__MESSAGE);
    createEReference(soapHeaderBaseEClass, SOAP_HEADER_BASE__PART);

    soapFaultEClass = createEClass(SOAP_FAULT);
    createEAttribute(soapFaultEClass, SOAP_FAULT__USE);
    createEAttribute(soapFaultEClass, SOAP_FAULT__NAMESPACE_URI);
    createEAttribute(soapFaultEClass, SOAP_FAULT__ENCODING_STYLES);

    soapOperationEClass = createEClass(SOAP_OPERATION);
    createEAttribute(soapOperationEClass, SOAP_OPERATION__SOAP_ACTION_URI);
    createEAttribute(soapOperationEClass, SOAP_OPERATION__STYLE);

    soapAddressEClass = createEClass(SOAP_ADDRESS);
    createEAttribute(soapAddressEClass, SOAP_ADDRESS__LOCATION_URI);

    soapHeaderFaultEClass = createEClass(SOAP_HEADER_FAULT);

    soapHeaderEClass = createEClass(SOAP_HEADER);
    createEReference(soapHeaderEClass, SOAP_HEADER__HEADER_FAULTS);

    // Create data types
    iStringEDataType = createEDataType(ISTRING);
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
  public void initializePackageContents()
  {
    if (isInitialized)
      return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    WSDLPackageImpl theWSDLPackage = (WSDLPackageImpl)EPackage.Registry.INSTANCE.getEPackage(WSDLPackage.eNS_URI);

    // Add supertypes to classes
    soapBindingEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapBodyEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapHeaderBaseEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapFaultEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapOperationEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapAddressEClass.getESuperTypes().add(theWSDLPackage.getExtensibilityElement());
    soapHeaderFaultEClass.getESuperTypes().add(this.getSOAPHeaderBase());
    soapHeaderEClass.getESuperTypes().add(this.getSOAPHeaderBase());

    // Initialize classes and features; add operations and parameters
    initEClass(soapBindingEClass, SOAPBinding.class, "SOAPBinding", !IS_ABSTRACT, !IS_INTERFACE);
    initEAttribute(
      getSOAPBinding_TransportURI(),
      ecorePackage.getEString(),
      "transportURI",
      null,
      0,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);
    initEAttribute(
      getSOAPBinding_Style(),
      ecorePackage.getEString(),
      "style",
      null,
      0,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);

    initEClass(soapBodyEClass, SOAPBody.class, "SOAPBody", !IS_ABSTRACT, !IS_INTERFACE);
    initEAttribute(
      getSOAPBody_Use(),
      ecorePackage.getEString(),
      "use",
      null,
      0,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);
    initEAttribute(
      getSOAPBody_NamespaceURI(),
      ecorePackage.getEString(),
      "namespaceURI",
      null,
      0,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);
    initEAttribute(
      getSOAPBody_EncodingStyles(),
      this.getIString(),
      "encodingStyles",
      null,
      0,
      -1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);
    initEReference(
      getSOAPBody_Parts(),
      theWSDLPackage.getPart(),
      null,
      "parts",
      null,
      0,
      -1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_COMPOSITE,
      IS_RESOLVE_PROXIES,
      !IS_UNSETTABLE,
      IS_UNIQUE,
      !IS_DERIVED);

    initEClass(soapHeaderBaseEClass, SOAPHeaderBase.class, "SOAPHeaderBase", !IS_ABSTRACT, !IS_INTERFACE);
    initEAttribute(
      getSOAPHeaderBase_Use(),
      ecorePackage.getEString(),
      "use",
      null,
      0,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);
    initEAttribute(
      getSOAPHeaderBase_NamespaceURI(),
      ecorePackage.getEString(),
      "namespaceURI",
      null,
      0,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);
    initEAttribute(
      getSOAPHeaderBase_EncodingStyles(),
      this.getIString(),
      "encodingStyles",
      null,
      0,
      -1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);
    initEReference(
      getSOAPHeaderBase_Message(),
      theWSDLPackage.getMessage(),
      null,
      "message",
      null,
      1,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_COMPOSITE,
      IS_RESOLVE_PROXIES,
      !IS_UNSETTABLE,
      IS_UNIQUE,
      !IS_DERIVED);
    initEReference(
      getSOAPHeaderBase_Part(),
      theWSDLPackage.getPart(),
      null,
      "part",
      null,
      1,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_COMPOSITE,
      IS_RESOLVE_PROXIES,
      !IS_UNSETTABLE,
      IS_UNIQUE,
      !IS_DERIVED);

    initEClass(soapFaultEClass, SOAPFault.class, "SOAPFault", !IS_ABSTRACT, !IS_INTERFACE);
    initEAttribute(
      getSOAPFault_Use(),
      ecorePackage.getEString(),
      "use",
      null,
      0,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);
    initEAttribute(
      getSOAPFault_NamespaceURI(),
      ecorePackage.getEString(),
      "namespaceURI",
      null,
      0,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);
    initEAttribute(
      getSOAPFault_EncodingStyles(),
      this.getIString(),
      "encodingStyles",
      null,
      0,
      -1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);

    initEClass(soapOperationEClass, SOAPOperation.class, "SOAPOperation", !IS_ABSTRACT, !IS_INTERFACE);
    initEAttribute(
      getSOAPOperation_SoapActionURI(),
      ecorePackage.getEString(),
      "soapActionURI",
      null,
      0,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);
    initEAttribute(
      getSOAPOperation_Style(),
      ecorePackage.getEString(),
      "style",
      null,
      0,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);

    initEClass(soapAddressEClass, SOAPAddress.class, "SOAPAddress", !IS_ABSTRACT, !IS_INTERFACE);
    initEAttribute(
      getSOAPAddress_LocationURI(),
      ecorePackage.getEString(),
      "locationURI",
      null,
      0,
      1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      !IS_UNSETTABLE,
      !IS_ID,
      IS_UNIQUE,
      !IS_DERIVED);

    initEClass(soapHeaderFaultEClass, SOAPHeaderFault.class, "SOAPHeaderFault", !IS_ABSTRACT, !IS_INTERFACE);

    initEClass(soapHeaderEClass, SOAPHeader.class, "SOAPHeader", !IS_ABSTRACT, !IS_INTERFACE);
    initEReference(
      getSOAPHeader_HeaderFaults(),
      this.getSOAPHeaderFault(),
      null,
      "headerFaults",
      null,
      0,
      -1,
      !IS_TRANSIENT,
      !IS_VOLATILE,
      IS_CHANGEABLE,
      IS_COMPOSITE,
      !IS_RESOLVE_PROXIES,
      !IS_UNSETTABLE,
      IS_UNIQUE,
      !IS_DERIVED);

    // Initialize data types
    initEDataType(iStringEDataType, String.class, "IString", IS_SERIALIZABLE);

    // Create resource
    createResource(eNS_URI);
  }
} //SOAPPackageImpl
