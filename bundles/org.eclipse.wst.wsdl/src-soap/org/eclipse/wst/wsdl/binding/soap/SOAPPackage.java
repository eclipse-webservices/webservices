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
package org.eclipse.wst.wsdl.binding.soap;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.wst.wsdl.WSDLPackage;


/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.wsdl.binding.soap.SOAPFactory
 * @generated
 */
public interface SOAPPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "soap";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.ibm.com/wsdl/2003/SOAP";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "soap";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  SOAPPackage eINSTANCE = org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBindingImpl <em>Binding</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBindingImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPBinding()
   * @generated
   */
  int SOAP_BINDING = 0;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Transport URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__TRANSPORT_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Style</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING__STYLE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the the '<em>Binding</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BINDING_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl <em>Body</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPBody()
   * @generated
   */
  int SOAP_BODY = 1;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__USE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__NAMESPACE_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Encoding Styles</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__ENCODING_STYLES = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Parts</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY__PARTS = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the the '<em>Body</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_BODY_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl <em>Header Base</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPHeaderBase()
   * @generated
   */
  int SOAP_HEADER_BASE = 2;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__USE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__NAMESPACE_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Encoding Styles</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__ENCODING_STYLES = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Message</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__MESSAGE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Part</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE__PART = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the the '<em>Header Base</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_BASE_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl <em>Fault</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPFault()
   * @generated
   */
  int SOAP_FAULT = 3;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__USE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__NAMESPACE_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Encoding Styles</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT__ENCODING_STYLES = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the the '<em>Fault</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_FAULT_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPOperationImpl <em>Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPOperationImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPOperation()
   * @generated
   */
  int SOAP_OPERATION = 4;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Soap Action URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__SOAP_ACTION_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Style</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION__STYLE = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the the '<em>Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_OPERATION_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPAddressImpl <em>Address</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPAddressImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPAddress()
   * @generated
   */
  int SOAP_ADDRESS = 5;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS__DOCUMENTATION_ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS__ELEMENT = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS__REQUIRED = WSDLPackage.EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS__ELEMENT_TYPE = WSDLPackage.EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Location URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS__LOCATION_URI = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the the '<em>Address</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_ADDRESS_FEATURE_COUNT = WSDLPackage.EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderFaultImpl <em>Header Fault</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderFaultImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPHeaderFault()
   * @generated
   */
  int SOAP_HEADER_FAULT = 6;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__DOCUMENTATION_ELEMENT = SOAP_HEADER_BASE__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__ELEMENT = SOAP_HEADER_BASE__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__REQUIRED = SOAP_HEADER_BASE__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__ELEMENT_TYPE = SOAP_HEADER_BASE__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__USE = SOAP_HEADER_BASE__USE;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__NAMESPACE_URI = SOAP_HEADER_BASE__NAMESPACE_URI;

  /**
   * The feature id for the '<em><b>Encoding Styles</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__ENCODING_STYLES = SOAP_HEADER_BASE__ENCODING_STYLES;

  /**
   * The feature id for the '<em><b>Message</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__MESSAGE = SOAP_HEADER_BASE__MESSAGE;

  /**
   * The feature id for the '<em><b>Part</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT__PART = SOAP_HEADER_BASE__PART;

  /**
   * The number of structural features of the the '<em>Header Fault</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FAULT_FEATURE_COUNT = SOAP_HEADER_BASE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderImpl <em>Header</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderImpl
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getSOAPHeader()
   * @generated
   */
  int SOAP_HEADER = 7;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__DOCUMENTATION_ELEMENT = SOAP_HEADER_BASE__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__ELEMENT = SOAP_HEADER_BASE__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__REQUIRED = SOAP_HEADER_BASE__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__ELEMENT_TYPE = SOAP_HEADER_BASE__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__USE = SOAP_HEADER_BASE__USE;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__NAMESPACE_URI = SOAP_HEADER_BASE__NAMESPACE_URI;

  /**
   * The feature id for the '<em><b>Encoding Styles</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__ENCODING_STYLES = SOAP_HEADER_BASE__ENCODING_STYLES;

  /**
   * The feature id for the '<em><b>Message</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__MESSAGE = SOAP_HEADER_BASE__MESSAGE;

  /**
   * The feature id for the '<em><b>Part</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__PART = SOAP_HEADER_BASE__PART;

  /**
   * The feature id for the '<em><b>Header Faults</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER__HEADER_FAULTS = SOAP_HEADER_BASE_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the the '<em>Header</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SOAP_HEADER_FEATURE_COUNT = SOAP_HEADER_BASE_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '<em>IString</em>' data type.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see java.lang.String
   * @see org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPPackageImpl#getIString()
   * @generated
   */
  int ISTRING = 8;

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBinding <em>Binding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBinding
   * @generated
   */
  EClass getSOAPBinding();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBinding#getTransportURI <em>Transport URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Transport URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBinding#getTransportURI()
   * @see #getSOAPBinding()
   * @generated
   */
  EAttribute getSOAPBinding_TransportURI();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBinding#getStyle <em>Style</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Style</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBinding#getStyle()
   * @see #getSOAPBinding()
   * @generated
   */
  EAttribute getSOAPBinding_Style();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Body</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBody
   * @generated
   */
  EClass getSOAPBody();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBody#getUse <em>Use</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Use</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBody#getUse()
   * @see #getSOAPBody()
   * @generated
   */
  EAttribute getSOAPBody_Use();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBody#getNamespaceURI <em>Namespace URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Namespace URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBody#getNamespaceURI()
   * @see #getSOAPBody()
   * @generated
   */
  EAttribute getSOAPBody_NamespaceURI();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBody#getEncodingStyles <em>Encoding Styles</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Encoding Styles</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBody#getEncodingStyles()
   * @see #getSOAPBody()
   * @generated
   */
  EAttribute getSOAPBody_EncodingStyles();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.wst.wsdl.binding.soap.SOAPBody#getParts <em>Parts</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Parts</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPBody#getParts()
   * @see #getSOAPBody()
   * @generated
   */
  EReference getSOAPBody_Parts();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase <em>Header Base</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Header Base</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase
   * @generated
   */
  EClass getSOAPHeaderBase();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getUse <em>Use</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Use</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getUse()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EAttribute getSOAPHeaderBase_Use();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getNamespaceURI <em>Namespace URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Namespace URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getNamespaceURI()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EAttribute getSOAPHeaderBase_NamespaceURI();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEncodingStyles <em>Encoding Styles</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Encoding Styles</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEncodingStyles()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EAttribute getSOAPHeaderBase_EncodingStyles();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getMessage <em>Message</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Message</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getMessage()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EReference getSOAPHeaderBase_Message();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getPart <em>Part</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Part</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getPart()
   * @see #getSOAPHeaderBase()
   * @generated
   */
  EReference getSOAPHeaderBase_Part();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault <em>Fault</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Fault</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPFault
   * @generated
   */
  EClass getSOAPFault();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getUse <em>Use</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Use</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPFault#getUse()
   * @see #getSOAPFault()
   * @generated
   */
  EAttribute getSOAPFault_Use();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getNamespaceURI <em>Namespace URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Namespace URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPFault#getNamespaceURI()
   * @see #getSOAPFault()
   * @generated
   */
  EAttribute getSOAPFault_NamespaceURI();

  /**
   * Returns the meta object for the attribute list '{@link org.eclipse.wst.wsdl.binding.soap.SOAPFault#getEncodingStyles <em>Encoding Styles</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Encoding Styles</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPFault#getEncodingStyles()
   * @see #getSOAPFault()
   * @generated
   */
  EAttribute getSOAPFault_EncodingStyles();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPOperation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operation</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPOperation
   * @generated
   */
  EClass getSOAPOperation();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPOperation#getSoapActionURI <em>Soap Action URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Soap Action URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPOperation#getSoapActionURI()
   * @see #getSOAPOperation()
   * @generated
   */
  EAttribute getSOAPOperation_SoapActionURI();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPOperation#getStyle <em>Style</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Style</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPOperation#getStyle()
   * @see #getSOAPOperation()
   * @generated
   */
  EAttribute getSOAPOperation_Style();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPAddress <em>Address</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Address</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPAddress
   * @generated
   */
  EClass getSOAPAddress();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wst.wsdl.binding.soap.SOAPAddress#getLocationURI <em>Location URI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location URI</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPAddress#getLocationURI()
   * @see #getSOAPAddress()
   * @generated
   */
  EAttribute getSOAPAddress_LocationURI();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault <em>Header Fault</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Header Fault</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault
   * @generated
   */
  EClass getSOAPHeaderFault();

  /**
   * Returns the meta object for class '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeader <em>Header</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Header</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeader
   * @generated
   */
  EClass getSOAPHeader();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeader#getHeaderFaults <em>Header Faults</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Header Faults</em>'.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPHeader#getHeaderFaults()
   * @see #getSOAPHeader()
   * @generated
   */
  EReference getSOAPHeader_HeaderFaults();

  /**
   * Returns the meta object for data type '{@link java.lang.String <em>IString</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for data type '<em>IString</em>'.
   * @see java.lang.String
   * @model instanceClass="java.lang.String"
   * @generated
   */
  EDataType getIString();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  SOAPFactory getSOAPFactory();

} //SOAPPackage
