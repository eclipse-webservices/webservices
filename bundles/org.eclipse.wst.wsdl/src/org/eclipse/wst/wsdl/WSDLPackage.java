/**
 * <copyright>
 * </copyright>
 *
 * %W%
 * @version %I% %H%
 */
package org.eclipse.wst.wsdl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

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
 * <!-- begin-model-doc -->
 * The WSDL model contains classes for the Web Services Description Language (WSDL).
 * 
 * WSDL describes network services as sets of endpoints operating on messages. The operations and messages are described abstractly, and then bound to a concrete network protocol and message format to define an endpoint.
 * 
 * WSDL describes the formats of the messages exchanged by the services, and supports the XML Schemas specification as its canonical type system. This package uses an XML Schema Infoset model package (see the XSD package) to describe the abstract message formats.
 * 
 * The model contains the following diagrams, named after the corresponding chapters in the WSDL 1.1 specification (http://www.w3.org/TR/2001/NOTE-wsdl-20010315)
 * - 2.1 Definition, shows the WSDL definition element and the WSDL document structure
 * - 2.1.1 Naming and Linking, shows the namespace and import mechanism
 * - 2.1.3 Extensibility, shows the WSDL extensibility mechanism
 * - 2.2 Types, shows the use of XML Schema types in WSDL
 * - 2.3 Messages, 2.4 PortTypes, 2.5 Bindings and 2.7 Services, show the major WSDL elements and their relations.
 * 
 * The WSDL classes extend the javax.wsdl interfaces defined by JSR 110. Classes with interface and datatype stereotypes are used to represent these non-MOF interfaces.
 * <!-- end-model-doc -->
 * @see org.eclipse.wsdl.WSDLFactory
 * @generated
 */
public interface WSDLPackage extends EPackage{
  /**
   * The package name.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	String eNAME = "wsdl";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.ibm.com/wsdl/2003/WSDL";
  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	String eNS_PREFIX = "wsdl";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	WSDLPackage eINSTANCE = org.eclipse.wst.wsdl.internal.impl.WSDLPackageImpl.init();

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.WSDLElementImpl <em>Element</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.WSDLElementImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getWSDLElement()
   * @generated
   */
	int WSDL_ELEMENT = 0;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int WSDL_ELEMENT__DOCUMENTATION_ELEMENT = 0;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WSDL_ELEMENT__ELEMENT = 1;

  /**
   * The number of structural features of the the '<em>Element</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int WSDL_ELEMENT_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.PortTypeImpl <em>Port Type</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.PortTypeImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getPortType()
   * @generated
   */
	int PORT_TYPE = 1;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT_TYPE__DOCUMENTATION_ELEMENT = WSDL_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT_TYPE__ELEMENT = WSDL_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT_TYPE__QNAME = WSDL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT_TYPE__UNDEFINED = WSDL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Proxy</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT_TYPE__PROXY = WSDL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Resource URI</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT_TYPE__RESOURCE_URI = WSDL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>EOperations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT_TYPE__EOPERATIONS = WSDL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the the '<em>Port Type</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT_TYPE_FEATURE_COUNT = WSDL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.OperationImpl <em>Operation</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.OperationImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getOperation()
   * @generated
   */
	int OPERATION = 2;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OPERATION__DOCUMENTATION_ELEMENT = WSDL_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OPERATION__ELEMENT = WSDL_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Style</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OPERATION__STYLE = WSDL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OPERATION__NAME = WSDL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OPERATION__UNDEFINED = WSDL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Proxy</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OPERATION__PROXY = WSDL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Resource URI</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OPERATION__RESOURCE_URI = WSDL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>EInput</b></em>' containment reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OPERATION__EINPUT = WSDL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>EOutput</b></em>' containment reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OPERATION__EOUTPUT = WSDL_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>EFaults</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OPERATION__EFAULTS = WSDL_ELEMENT_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>EParameter Ordering</b></em>' reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OPERATION__EPARAMETER_ORDERING = WSDL_ELEMENT_FEATURE_COUNT + 8;

  /**
   * The number of structural features of the the '<em>Operation</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OPERATION_FEATURE_COUNT = WSDL_ELEMENT_FEATURE_COUNT + 9;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.MessageImpl <em>Message</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.MessageImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getMessage()
   * @generated
   */
	int MESSAGE = 3;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int MESSAGE__DOCUMENTATION_ELEMENT = WSDL_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE__ELEMENT = WSDL_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int MESSAGE__QNAME = WSDL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int MESSAGE__UNDEFINED = WSDL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Proxy</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int MESSAGE__PROXY = WSDL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Resource URI</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int MESSAGE__RESOURCE_URI = WSDL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>EParts</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int MESSAGE__EPARTS = WSDL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the the '<em>Message</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int MESSAGE_FEATURE_COUNT = WSDL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.PartImpl <em>Part</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.PartImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getPart()
   * @generated
   */
	int PART = 4;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PART__DOCUMENTATION_ELEMENT = WSDL_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__ELEMENT = WSDL_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PART__NAME = WSDL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Element Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PART__ELEMENT_NAME = WSDL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Type Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PART__TYPE_NAME = WSDL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Type Definition</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__TYPE_DEFINITION = WSDL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>Element Declaration</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PART__ELEMENT_DECLARATION = WSDL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PART__EMESSAGE = WSDL_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the the '<em>Part</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PART_FEATURE_COUNT = WSDL_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.ExtensibleElementImpl <em>Extensible Element</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.ExtensibleElementImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getExtensibleElement()
   * @generated
   */
	int EXTENSIBLE_ELEMENT = 12;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT = WSDL_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXTENSIBLE_ELEMENT__ELEMENT = WSDL_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS = WSDL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the the '<em>Extensible Element</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int EXTENSIBLE_ELEMENT_FEATURE_COUNT = WSDL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.BindingImpl <em>Binding</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.BindingImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getBinding()
   * @generated
   */
	int BINDING = 5;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING__QNAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING__UNDEFINED = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Proxy</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING__PROXY = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Resource URI</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING__RESOURCE_URI = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>EPort Type</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING__EPORT_TYPE = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>EBinding Operations</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING__EBINDING_OPERATIONS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The number of structural features of the the '<em>Binding</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.BindingOperationImpl <em>Binding Operation</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.BindingOperationImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getBindingOperation()
   * @generated
   */
	int BINDING_OPERATION = 6;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OPERATION__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OPERATION__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OPERATION__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OPERATION__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EOperation</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OPERATION__EOPERATION = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>EBinding Input</b></em>' containment reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OPERATION__EBINDING_INPUT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>EBinding Output</b></em>' containment reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OPERATION__EBINDING_OUTPUT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>EBinding Faults</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OPERATION__EBINDING_FAULTS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the the '<em>Binding Operation</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OPERATION_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.ServiceImpl <em>Service</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.ServiceImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getService()
   * @generated
   */
	int SERVICE = 7;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int SERVICE__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int SERVICE__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int SERVICE__QNAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Undefined</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int SERVICE__UNDEFINED = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Proxy</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int SERVICE__PROXY = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Resource URI</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int SERVICE__RESOURCE_URI = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>EPorts</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int SERVICE__EPORTS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The number of structural features of the the '<em>Service</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int SERVICE_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.PortImpl <em>Port</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.PortImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getPort()
   * @generated
   */
	int PORT = 8;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int PORT__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EBinding</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT__EBINDING = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the the '<em>Port</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int PORT_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.ExtensibilityElementImpl <em>Extensibility Element</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.ExtensibilityElementImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getExtensibilityElement()
   * @generated
   */
	int EXTENSIBILITY_ELEMENT = 9;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT = WSDL_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int EXTENSIBILITY_ELEMENT__ELEMENT = WSDL_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int EXTENSIBILITY_ELEMENT__REQUIRED = WSDL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int EXTENSIBILITY_ELEMENT__ELEMENT_TYPE = WSDL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the the '<em>Extensibility Element</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int EXTENSIBILITY_ELEMENT_FEATURE_COUNT = WSDL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.DefinitionImpl <em>Definition</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.DefinitionImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getDefinition()
   * @generated
   */
	int DEFINITION = 10;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Target Namespace</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__TARGET_NAMESPACE = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Location</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__LOCATION = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>QName</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__QNAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Encoding</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__ENCODING = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The feature id for the '<em><b>EMessages</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__EMESSAGES = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The feature id for the '<em><b>EPort Types</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__EPORT_TYPES = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 5;

  /**
   * The feature id for the '<em><b>EBindings</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__EBINDINGS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 6;

  /**
   * The feature id for the '<em><b>EServices</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__ESERVICES = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 7;

  /**
   * The feature id for the '<em><b>ENamespaces</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__ENAMESPACES = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 8;

  /**
   * The feature id for the '<em><b>ETypes</b></em>' containment reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__ETYPES = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 9;

  /**
   * The feature id for the '<em><b>EImports</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION__EIMPORTS = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 10;

  /**
   * The number of structural features of the the '<em>Definition</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int DEFINITION_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 11;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.ImportImpl <em>Import</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.ImportImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getImport()
   * @generated
   */
	int IMPORT = 11;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IMPORT__DOCUMENTATION_ELEMENT = WSDL_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMPORT__ELEMENT = WSDL_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IMPORT__NAMESPACE_URI = WSDL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Location URI</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IMPORT__LOCATION_URI = WSDL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>EDefinition</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IMPORT__EDEFINITION = WSDL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>ESchema</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IMPORT__ESCHEMA = WSDL_ELEMENT_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the the '<em>Import</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IMPORT_FEATURE_COUNT = WSDL_ELEMENT_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.MessageReferenceImpl <em>Message Reference</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.MessageReferenceImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getMessageReference()
   * @generated
   */
	int MESSAGE_REFERENCE = 46;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT = WSDL_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE_REFERENCE__ELEMENT = WSDL_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int MESSAGE_REFERENCE__NAME = WSDL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MESSAGE_REFERENCE__EMESSAGE = WSDL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the the '<em>Message Reference</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int MESSAGE_REFERENCE_FEATURE_COUNT = WSDL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.InputImpl <em>Input</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.InputImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getInput()
   * @generated
   */
	int INPUT = 13;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int INPUT__DOCUMENTATION_ELEMENT = MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INPUT__ELEMENT = MESSAGE_REFERENCE__ELEMENT;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int INPUT__NAME = MESSAGE_REFERENCE__NAME;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int INPUT__EMESSAGE = MESSAGE_REFERENCE__EMESSAGE;

  /**
   * The number of structural features of the the '<em>Input</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int INPUT_FEATURE_COUNT = MESSAGE_REFERENCE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.OutputImpl <em>Output</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.OutputImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getOutput()
   * @generated
   */
	int OUTPUT = 14;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OUTPUT__DOCUMENTATION_ELEMENT = MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int OUTPUT__ELEMENT = MESSAGE_REFERENCE__ELEMENT;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OUTPUT__NAME = MESSAGE_REFERENCE__NAME;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OUTPUT__EMESSAGE = MESSAGE_REFERENCE__EMESSAGE;

  /**
   * The number of structural features of the the '<em>Output</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int OUTPUT_FEATURE_COUNT = MESSAGE_REFERENCE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.FaultImpl <em>Fault</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.FaultImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getFault()
   * @generated
   */
	int FAULT = 15;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int FAULT__DOCUMENTATION_ELEMENT = MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FAULT__ELEMENT = MESSAGE_REFERENCE__ELEMENT;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int FAULT__NAME = MESSAGE_REFERENCE__NAME;

  /**
   * The feature id for the '<em><b>EMessage</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int FAULT__EMESSAGE = MESSAGE_REFERENCE__EMESSAGE;

  /**
   * The number of structural features of the the '<em>Fault</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int FAULT_FEATURE_COUNT = MESSAGE_REFERENCE_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.BindingInputImpl <em>Binding Input</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.BindingInputImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getBindingInput()
   * @generated
   */
	int BINDING_INPUT = 16;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_INPUT__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_INPUT__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_INPUT__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_INPUT__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EInput</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_INPUT__EINPUT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the the '<em>Binding Input</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_INPUT_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.BindingOutputImpl <em>Binding Output</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.BindingOutputImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getBindingOutput()
   * @generated
   */
	int BINDING_OUTPUT = 17;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OUTPUT__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_OUTPUT__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OUTPUT__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OUTPUT__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EOutput</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OUTPUT__EOUTPUT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the the '<em>Binding Output</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_OUTPUT_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.BindingFaultImpl <em>Binding Fault</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.BindingFaultImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getBindingFault()
   * @generated
   */
	int BINDING_FAULT = 18;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_FAULT__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_FAULT__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_FAULT__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_FAULT__NAME = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>EFault</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_FAULT__EFAULT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the the '<em>Binding Fault</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int BINDING_FAULT_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.NamespaceImpl <em>Namespace</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.NamespaceImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getNamespace()
   * @generated
   */
	int NAMESPACE = 19;

  /**
   * The feature id for the '<em><b>URI</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int NAMESPACE__URI = 0;

  /**
   * The feature id for the '<em><b>Prefix</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int NAMESPACE__PREFIX = 1;

  /**
   * The number of structural features of the the '<em>Namespace</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int NAMESPACE_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link javax.wsdl.PortType <em>IPort Type</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.PortType
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIPortType()
   * @generated
   */
	int IPORT_TYPE = 20;

  /**
   * The number of structural features of the the '<em>IPort Type</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IPORT_TYPE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Operation <em>IOperation</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Operation
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIOperation()
   * @generated
   */
	int IOPERATION = 21;

  /**
   * The number of structural features of the the '<em>IOperation</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IOPERATION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Input <em>IInput</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Input
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIInput()
   * @generated
   */
	int IINPUT = 22;

  /**
   * The number of structural features of the the '<em>IInput</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IINPUT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Output <em>IOutput</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Output
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIOutput()
   * @generated
   */
	int IOUTPUT = 23;

  /**
   * The number of structural features of the the '<em>IOutput</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IOUTPUT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Fault <em>IFault</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Fault
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIFault()
   * @generated
   */
	int IFAULT = 24;

  /**
   * The number of structural features of the the '<em>IFault</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IFAULT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Message <em>IMessage</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Message
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIMessage()
   * @generated
   */
	int IMESSAGE = 25;

  /**
   * The number of structural features of the the '<em>IMessage</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IMESSAGE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Part <em>IPart</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Part
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIPart()
   * @generated
   */
	int IPART = 26;

  /**
   * The number of structural features of the the '<em>IPart</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IPART_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Service <em>IService</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Service
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIService()
   * @generated
   */
	int ISERVICE = 27;

  /**
   * The number of structural features of the the '<em>IService</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int ISERVICE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Port <em>IPort</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Port
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIPort()
   * @generated
   */
	int IPORT = 28;

  /**
   * The number of structural features of the the '<em>IPort</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IPORT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Binding <em>IBinding</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Binding
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIBinding()
   * @generated
   */
	int IBINDING = 29;

  /**
   * The number of structural features of the the '<em>IBinding</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IBINDING_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.BindingOperation <em>IBinding Operation</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.BindingOperation
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIBindingOperation()
   * @generated
   */
	int IBINDING_OPERATION = 30;

  /**
   * The number of structural features of the the '<em>IBinding Operation</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IBINDING_OPERATION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.BindingInput <em>IBinding Input</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.BindingInput
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIBindingInput()
   * @generated
   */
	int IBINDING_INPUT = 31;

  /**
   * The number of structural features of the the '<em>IBinding Input</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IBINDING_INPUT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.BindingOutput <em>IBinding Output</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.BindingOutput
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIBindingOutput()
   * @generated
   */
	int IBINDING_OUTPUT = 32;

  /**
   * The number of structural features of the the '<em>IBinding Output</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IBINDING_OUTPUT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.BindingFault <em>IBinding Fault</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.BindingFault
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIBindingFault()
   * @generated
   */
	int IBINDING_FAULT = 33;

  /**
   * The number of structural features of the the '<em>IBinding Fault</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IBINDING_FAULT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.ExtensibilityElement <em>IExtensibility Element</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.ExtensibilityElement
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIExtensibilityElement()
   * @generated
   */
	int IEXTENSIBILITY_ELEMENT = 34;

  /**
   * The number of structural features of the the '<em>IExtensibility Element</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IEXTENSIBILITY_ELEMENT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Definition <em>IDefinition</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Definition
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIDefinition()
   * @generated
   */
	int IDEFINITION = 35;

  /**
   * The number of structural features of the the '<em>IDefinition</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IDEFINITION_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Import <em>IImport</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Import
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIImport()
   * @generated
   */
	int IIMPORT = 36;

  /**
   * The number of structural features of the the '<em>IImport</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IIMPORT_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link java.util.List <em>IList</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see java.util.List
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIList()
   * @generated
   */
	int ILIST = 37;

  /**
   * The number of structural features of the the '<em>IList</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int ILIST_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link java.util.Map <em>IMap</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see java.util.Map
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIMap()
   * @generated
   */
	int IMAP = 38;

  /**
   * The number of structural features of the the '<em>IMap</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IMAP_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link java.net.URL <em>IURL</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see java.net.URL
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIURL()
   * @generated
   */
	int IURL = 39;

  /**
   * The number of structural features of the the '<em>IURL</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IURL_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.extensions.ExtensionRegistry <em>IExtension Registry</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.extensions.ExtensionRegistry
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIExtensionRegistry()
   * @generated
   */
	int IEXTENSION_REGISTRY = 40;

  /**
   * The number of structural features of the the '<em>IExtension Registry</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IEXTENSION_REGISTRY_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.TypesImpl <em>Types</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.TypesImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getTypes()
   * @generated
   */
	int TYPES = 41;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int TYPES__DOCUMENTATION_ELEMENT = EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TYPES__ELEMENT = EXTENSIBLE_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int TYPES__EEXTENSIBILITY_ELEMENTS = EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS;

  /**
   * The number of structural features of the the '<em>Types</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int TYPES_FEATURE_COUNT = EXTENSIBLE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link java.util.Iterator <em>IIterator</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see java.util.Iterator
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getIIterator()
   * @generated
   */
	int IITERATOR = 42;

  /**
   * The number of structural features of the the '<em>IIterator</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int IITERATOR_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link javax.wsdl.Types <em>ITypes</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.Types
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getITypes()
   * @generated
   */
	int ITYPES = 43;

  /**
   * The number of structural features of the the '<em>ITypes</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int ITYPES_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.UnknownExtensibilityElementImpl <em>Unknown Extensibility Element</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.UnknownExtensibilityElementImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getUnknownExtensibilityElement()
   * @generated
   */
	int UNKNOWN_EXTENSIBILITY_ELEMENT = 44;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int UNKNOWN_EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT = EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT = EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int UNKNOWN_EXTENSIBILITY_ELEMENT__REQUIRED = EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int UNKNOWN_EXTENSIBILITY_ELEMENT__ELEMENT_TYPE = EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Parent</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNKNOWN_EXTENSIBILITY_ELEMENT__PARENT = EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Children</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int UNKNOWN_EXTENSIBILITY_ELEMENT__CHILDREN = EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the the '<em>Unknown Extensibility Element</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int UNKNOWN_EXTENSIBILITY_ELEMENT_FEATURE_COUNT = EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.eclipse.wsdl.impl.XSDSchemaExtensibilityElementImpl <em>XSD Schema Extensibility Element</em>}' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.eclipse.wst.wsdl.impl.XSDSchemaExtensibilityElementImpl
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getXSDSchemaExtensibilityElement()
   * @generated
   */
	int XSD_SCHEMA_EXTENSIBILITY_ELEMENT = 45;

  /**
   * The feature id for the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int XSD_SCHEMA_EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT = EXTENSIBILITY_ELEMENT__DOCUMENTATION_ELEMENT;

  /**
   * The feature id for the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XSD_SCHEMA_EXTENSIBILITY_ELEMENT__ELEMENT = EXTENSIBILITY_ELEMENT__ELEMENT;

  /**
   * The feature id for the '<em><b>Required</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int XSD_SCHEMA_EXTENSIBILITY_ELEMENT__REQUIRED = EXTENSIBILITY_ELEMENT__REQUIRED;

  /**
   * The feature id for the '<em><b>Element Type</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int XSD_SCHEMA_EXTENSIBILITY_ELEMENT__ELEMENT_TYPE = EXTENSIBILITY_ELEMENT__ELEMENT_TYPE;

  /**
   * The feature id for the '<em><b>Schema</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA = EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the the '<em>XSD Schema Extensibility Element</em>' class.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
	int XSD_SCHEMA_EXTENSIBILITY_ELEMENT_FEATURE_COUNT = EXTENSIBILITY_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '<em>QName</em>' data type.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.xml.namespace.QName
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getQName()
   * @generated
   */
	int QNAME = 47;

  /**
   * The meta object id for the '<em>Operation Type</em>' data type.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.OperationType
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getOperationType()
   * @generated
   */
	int OPERATION_TYPE = 48;

  /**
   * The meta object id for the '<em>DOM Element</em>' data type.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.w3c.dom.Element
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getDOMElement()
   * @generated
   */
	int DOM_ELEMENT = 49;

  /**
   * The meta object id for the '<em>Exception</em>' data type.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see javax.wsdl.WSDLException
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getWSDLException()
   * @generated
   */
	int WSDL_EXCEPTION = 50;

  /**
   * The meta object id for the '<em>DOM Document</em>' data type.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see org.w3c.dom.Document
   * @see org.eclipse.wsdl.impl.WSDLPackageImpl#getDOMDocument()
   * @generated
   */
	int DOM_DOCUMENT = 51;


  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.WSDLElement <em>Element</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Element</em>'.
   * @see org.eclipse.wst.wsdl.WSDLElement
   * @generated
   */
	EClass getWSDLElement();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.WSDLElement#getDocumentationElement <em>Documentation Element</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Documentation Element</em>'.
   * @see org.eclipse.wsdl.WSDLElement#getDocumentationElement()
   * @see #getWSDLElement()
   * @generated
   */
	EAttribute getWSDLElement_DocumentationElement();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.WSDLElement#getElement <em>Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Element</em>'.
   * @see org.eclipse.wsdl.WSDLElement#getElement()
   * @see #getWSDLElement()
   * @generated
   */
  EAttribute getWSDLElement_Element();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.PortType <em>Port Type</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Port Type</em>'.
   * @see org.eclipse.wst.wsdl.PortType
   * @generated
   */
	EClass getPortType();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.PortType#getQName <em>QName</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>QName</em>'.
   * @see org.eclipse.wsdl.PortType#getQName()
   * @see #getPortType()
   * @generated
   */
	EAttribute getPortType_QName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.PortType#isUndefined <em>Undefined</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Undefined</em>'.
   * @see org.eclipse.wsdl.PortType#isUndefined()
   * @see #getPortType()
   * @generated
   */
	EAttribute getPortType_Undefined();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.PortType#isProxy <em>Proxy</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Proxy</em>'.
   * @see org.eclipse.wsdl.PortType#isProxy()
   * @see #getPortType()
   * @generated
   */
	EAttribute getPortType_Proxy();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.PortType#getResourceURI <em>Resource URI</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Resource URI</em>'.
   * @see org.eclipse.wsdl.PortType#getResourceURI()
   * @see #getPortType()
   * @generated
   */
	EAttribute getPortType_ResourceURI();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.PortType#getEOperations <em>EOperations</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EOperations</em>'.
   * @see org.eclipse.wsdl.PortType#getEOperations()
   * @see #getPortType()
   * @generated
   */
	EReference getPortType_EOperations();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Operation <em>Operation</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Operation</em>'.
   * @see org.eclipse.wst.wsdl.Operation
   * @generated
   */
	EClass getOperation();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Operation#getStyle <em>Style</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Style</em>'.
   * @see org.eclipse.wsdl.Operation#getStyle()
   * @see #getOperation()
   * @generated
   */
	EAttribute getOperation_Style();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Operation#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wsdl.Operation#getName()
   * @see #getOperation()
   * @generated
   */
	EAttribute getOperation_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Operation#isUndefined <em>Undefined</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Undefined</em>'.
   * @see org.eclipse.wsdl.Operation#isUndefined()
   * @see #getOperation()
   * @generated
   */
	EAttribute getOperation_Undefined();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Operation#isProxy <em>Proxy</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Proxy</em>'.
   * @see org.eclipse.wsdl.Operation#isProxy()
   * @see #getOperation()
   * @generated
   */
	EAttribute getOperation_Proxy();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Operation#getResourceURI <em>Resource URI</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Resource URI</em>'.
   * @see org.eclipse.wsdl.Operation#getResourceURI()
   * @see #getOperation()
   * @generated
   */
	EAttribute getOperation_ResourceURI();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wsdl.Operation#getEInput <em>EInput</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>EInput</em>'.
   * @see org.eclipse.wsdl.Operation#getEInput()
   * @see #getOperation()
   * @generated
   */
	EReference getOperation_EInput();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wsdl.Operation#getEOutput <em>EOutput</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>EOutput</em>'.
   * @see org.eclipse.wsdl.Operation#getEOutput()
   * @see #getOperation()
   * @generated
   */
	EReference getOperation_EOutput();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.Operation#getEFaults <em>EFaults</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EFaults</em>'.
   * @see org.eclipse.wsdl.Operation#getEFaults()
   * @see #getOperation()
   * @generated
   */
	EReference getOperation_EFaults();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.wsdl.Operation#getEParameterOrdering <em>EParameter Ordering</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>EParameter Ordering</em>'.
   * @see org.eclipse.wsdl.Operation#getEParameterOrdering()
   * @see #getOperation()
   * @generated
   */
	EReference getOperation_EParameterOrdering();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Message <em>Message</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Message</em>'.
   * @see org.eclipse.wst.wsdl.Message
   * @generated
   */
	EClass getMessage();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Message#getQName <em>QName</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>QName</em>'.
   * @see org.eclipse.wsdl.Message#getQName()
   * @see #getMessage()
   * @generated
   */
	EAttribute getMessage_QName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Message#isUndefined <em>Undefined</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Undefined</em>'.
   * @see org.eclipse.wsdl.Message#isUndefined()
   * @see #getMessage()
   * @generated
   */
	EAttribute getMessage_Undefined();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Message#isProxy <em>Proxy</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Proxy</em>'.
   * @see org.eclipse.wsdl.Message#isProxy()
   * @see #getMessage()
   * @generated
   */
	EAttribute getMessage_Proxy();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Message#getResourceURI <em>Resource URI</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Resource URI</em>'.
   * @see org.eclipse.wsdl.Message#getResourceURI()
   * @see #getMessage()
   * @generated
   */
	EAttribute getMessage_ResourceURI();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.Message#getEParts <em>EParts</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EParts</em>'.
   * @see org.eclipse.wsdl.Message#getEParts()
   * @see #getMessage()
   * @generated
   */
	EReference getMessage_EParts();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Part <em>Part</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Part</em>'.
   * @see org.eclipse.wst.wsdl.Part
   * @generated
   */
	EClass getPart();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Part#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wsdl.Part#getName()
   * @see #getPart()
   * @generated
   */
	EAttribute getPart_Name();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Part#getElementName <em>Element Name</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Element Name</em>'.
   * @see org.eclipse.wsdl.Part#getElementName()
   * @see #getPart()
   * @generated
   */
	EAttribute getPart_ElementName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Part#getTypeName <em>Type Name</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type Name</em>'.
   * @see org.eclipse.wsdl.Part#getTypeName()
   * @see #getPart()
   * @generated
   */
	EAttribute getPart_TypeName();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.Part#getTypeDefinition <em>Type Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Type Definition</em>'.
   * @see org.eclipse.wsdl.Part#getTypeDefinition()
   * @see #getPart()
   * @generated
   */
  EReference getPart_TypeDefinition();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.Part#getElementDeclaration <em>Element Declaration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Element Declaration</em>'.
   * @see org.eclipse.wsdl.Part#getElementDeclaration()
   * @see #getPart()
   * @generated
   */
  EReference getPart_ElementDeclaration();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.Part#getEMessage <em>EMessage</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EMessage</em>'.
   * @see org.eclipse.wsdl.Part#getEMessage()
   * @see #getPart()
   * @generated
   */
	EReference getPart_EMessage();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Binding <em>Binding</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding</em>'.
   * @see org.eclipse.wst.wsdl.Binding
   * @generated
   */
	EClass getBinding();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Binding#getQName <em>QName</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>QName</em>'.
   * @see org.eclipse.wsdl.Binding#getQName()
   * @see #getBinding()
   * @generated
   */
	EAttribute getBinding_QName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Binding#isUndefined <em>Undefined</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Undefined</em>'.
   * @see org.eclipse.wsdl.Binding#isUndefined()
   * @see #getBinding()
   * @generated
   */
	EAttribute getBinding_Undefined();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Binding#isProxy <em>Proxy</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Proxy</em>'.
   * @see org.eclipse.wsdl.Binding#isProxy()
   * @see #getBinding()
   * @generated
   */
	EAttribute getBinding_Proxy();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Binding#getResourceURI <em>Resource URI</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Resource URI</em>'.
   * @see org.eclipse.wsdl.Binding#getResourceURI()
   * @see #getBinding()
   * @generated
   */
	EAttribute getBinding_ResourceURI();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.Binding#getEPortType <em>EPort Type</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EPort Type</em>'.
   * @see org.eclipse.wsdl.Binding#getEPortType()
   * @see #getBinding()
   * @generated
   */
	EReference getBinding_EPortType();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.Binding#getEBindingOperations <em>EBinding Operations</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EBinding Operations</em>'.
   * @see org.eclipse.wsdl.Binding#getEBindingOperations()
   * @see #getBinding()
   * @generated
   */
	EReference getBinding_EBindingOperations();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.BindingOperation <em>Binding Operation</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding Operation</em>'.
   * @see org.eclipse.wst.wsdl.BindingOperation
   * @generated
   */
	EClass getBindingOperation();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.BindingOperation#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wsdl.BindingOperation#getName()
   * @see #getBindingOperation()
   * @generated
   */
	EAttribute getBindingOperation_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.BindingOperation#getEOperation <em>EOperation</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EOperation</em>'.
   * @see org.eclipse.wsdl.BindingOperation#getEOperation()
   * @see #getBindingOperation()
   * @generated
   */
	EReference getBindingOperation_EOperation();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wsdl.BindingOperation#getEBindingInput <em>EBinding Input</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>EBinding Input</em>'.
   * @see org.eclipse.wsdl.BindingOperation#getEBindingInput()
   * @see #getBindingOperation()
   * @generated
   */
	EReference getBindingOperation_EBindingInput();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wsdl.BindingOperation#getEBindingOutput <em>EBinding Output</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>EBinding Output</em>'.
   * @see org.eclipse.wsdl.BindingOperation#getEBindingOutput()
   * @see #getBindingOperation()
   * @generated
   */
	EReference getBindingOperation_EBindingOutput();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.BindingOperation#getEBindingFaults <em>EBinding Faults</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EBinding Faults</em>'.
   * @see org.eclipse.wsdl.BindingOperation#getEBindingFaults()
   * @see #getBindingOperation()
   * @generated
   */
	EReference getBindingOperation_EBindingFaults();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Service <em>Service</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Service</em>'.
   * @see org.eclipse.wst.wsdl.Service
   * @generated
   */
	EClass getService();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Service#getQName <em>QName</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>QName</em>'.
   * @see org.eclipse.wsdl.Service#getQName()
   * @see #getService()
   * @generated
   */
	EAttribute getService_QName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Service#isUndefined <em>Undefined</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Undefined</em>'.
   * @see org.eclipse.wsdl.Service#isUndefined()
   * @see #getService()
   * @generated
   */
	EAttribute getService_Undefined();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Service#isProxy <em>Proxy</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Proxy</em>'.
   * @see org.eclipse.wsdl.Service#isProxy()
   * @see #getService()
   * @generated
   */
	EAttribute getService_Proxy();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Service#getResourceURI <em>Resource URI</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Resource URI</em>'.
   * @see org.eclipse.wsdl.Service#getResourceURI()
   * @see #getService()
   * @generated
   */
	EAttribute getService_ResourceURI();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.Service#getEPorts <em>EPorts</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EPorts</em>'.
   * @see org.eclipse.wsdl.Service#getEPorts()
   * @see #getService()
   * @generated
   */
	EReference getService_EPorts();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Port <em>Port</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Port</em>'.
   * @see org.eclipse.wst.wsdl.Port
   * @generated
   */
	EClass getPort();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Port#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wsdl.Port#getName()
   * @see #getPort()
   * @generated
   */
	EAttribute getPort_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.Port#getEBinding <em>EBinding</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EBinding</em>'.
   * @see org.eclipse.wsdl.Port#getEBinding()
   * @see #getPort()
   * @generated
   */
	EReference getPort_EBinding();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.ExtensibilityElement <em>Extensibility Element</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Extensibility Element</em>'.
   * @see org.eclipse.wst.wsdl.ExtensibilityElement
   * @generated
   */
	EClass getExtensibilityElement();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.ExtensibilityElement#isRequired <em>Required</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Required</em>'.
   * @see org.eclipse.wsdl.ExtensibilityElement#isRequired()
   * @see #getExtensibilityElement()
   * @generated
   */
	EAttribute getExtensibilityElement_Required();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.ExtensibilityElement#getElementType <em>Element Type</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Element Type</em>'.
   * @see org.eclipse.wsdl.ExtensibilityElement#getElementType()
   * @see #getExtensibilityElement()
   * @generated
   */
	EAttribute getExtensibilityElement_ElementType();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Definition <em>Definition</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Definition</em>'.
   * @see org.eclipse.wst.wsdl.Definition
   * @generated
   */
	EClass getDefinition();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Definition#getTargetNamespace <em>Target Namespace</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Target Namespace</em>'.
   * @see org.eclipse.wsdl.Definition#getTargetNamespace()
   * @see #getDefinition()
   * @generated
   */
	EAttribute getDefinition_TargetNamespace();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Definition#getLocation <em>Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location</em>'.
   * @see org.eclipse.wsdl.Definition#getLocation()
   * @see #getDefinition()
   * @generated
   */
  EAttribute getDefinition_Location();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Definition#getQName <em>QName</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>QName</em>'.
   * @see org.eclipse.wsdl.Definition#getQName()
   * @see #getDefinition()
   * @generated
   */
	EAttribute getDefinition_QName();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Definition#getEncoding <em>Encoding</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Encoding</em>'.
   * @see org.eclipse.wsdl.Definition#getEncoding()
   * @see #getDefinition()
   * @generated
   */
	EAttribute getDefinition_Encoding();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.Definition#getEMessages <em>EMessages</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EMessages</em>'.
   * @see org.eclipse.wsdl.Definition#getEMessages()
   * @see #getDefinition()
   * @generated
   */
	EReference getDefinition_EMessages();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.Definition#getEPortTypes <em>EPort Types</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EPort Types</em>'.
   * @see org.eclipse.wsdl.Definition#getEPortTypes()
   * @see #getDefinition()
   * @generated
   */
	EReference getDefinition_EPortTypes();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.Definition#getEBindings <em>EBindings</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EBindings</em>'.
   * @see org.eclipse.wsdl.Definition#getEBindings()
   * @see #getDefinition()
   * @generated
   */
	EReference getDefinition_EBindings();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.Definition#getEServices <em>EServices</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EServices</em>'.
   * @see org.eclipse.wsdl.Definition#getEServices()
   * @see #getDefinition()
   * @generated
   */
	EReference getDefinition_EServices();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.Definition#getENamespaces <em>ENamespaces</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>ENamespaces</em>'.
   * @see org.eclipse.wsdl.Definition#getENamespaces()
   * @see #getDefinition()
   * @generated
   */
	EReference getDefinition_ENamespaces();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wsdl.Definition#getETypes <em>ETypes</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>ETypes</em>'.
   * @see org.eclipse.wsdl.Definition#getETypes()
   * @see #getDefinition()
   * @generated
   */
	EReference getDefinition_ETypes();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.Definition#getEImports <em>EImports</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EImports</em>'.
   * @see org.eclipse.wsdl.Definition#getEImports()
   * @see #getDefinition()
   * @generated
   */
	EReference getDefinition_EImports();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Import <em>Import</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Import</em>'.
   * @see org.eclipse.wst.wsdl.Import
   * @generated
   */
	EClass getImport();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Import#getNamespaceURI <em>Namespace URI</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Namespace URI</em>'.
   * @see org.eclipse.wsdl.Import#getNamespaceURI()
   * @see #getImport()
   * @generated
   */
	EAttribute getImport_NamespaceURI();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Import#getLocationURI <em>Location URI</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Location URI</em>'.
   * @see org.eclipse.wsdl.Import#getLocationURI()
   * @see #getImport()
   * @generated
   */
	EAttribute getImport_LocationURI();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.Import#getEDefinition <em>EDefinition</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EDefinition</em>'.
   * @see org.eclipse.wsdl.Import#getEDefinition()
   * @see #getImport()
   * @generated
   */
	EReference getImport_EDefinition();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.Import#getESchema <em>ESchema</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>ESchema</em>'.
   * @see org.eclipse.wsdl.Import#getESchema()
   * @see #getImport()
   * @generated
   */
	EReference getImport_ESchema();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.ExtensibleElement <em>Extensible Element</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Extensible Element</em>'.
   * @see org.eclipse.wst.wsdl.ExtensibleElement
   * @generated
   */
	EClass getExtensibleElement();

  /**
   * Returns the meta object for the containment reference list '{@link org.eclipse.wsdl.ExtensibleElement#getEExtensibilityElements <em>EExtensibility Elements</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>EExtensibility Elements</em>'.
   * @see org.eclipse.wsdl.ExtensibleElement#getEExtensibilityElements()
   * @see #getExtensibleElement()
   * @generated
   */
	EReference getExtensibleElement_EExtensibilityElements();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Input <em>Input</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Input</em>'.
   * @see org.eclipse.wst.wsdl.Input
   * @generated
   */
	EClass getInput();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Output <em>Output</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Output</em>'.
   * @see org.eclipse.wst.wsdl.Output
   * @generated
   */
	EClass getOutput();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Fault <em>Fault</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Fault</em>'.
   * @see org.eclipse.wst.wsdl.Fault
   * @generated
   */
	EClass getFault();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.BindingInput <em>Binding Input</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding Input</em>'.
   * @see org.eclipse.wst.wsdl.BindingInput
   * @generated
   */
	EClass getBindingInput();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.BindingInput#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wsdl.BindingInput#getName()
   * @see #getBindingInput()
   * @generated
   */
	EAttribute getBindingInput_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.BindingInput#getEInput <em>EInput</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EInput</em>'.
   * @see org.eclipse.wsdl.BindingInput#getEInput()
   * @see #getBindingInput()
   * @generated
   */
	EReference getBindingInput_EInput();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.BindingOutput <em>Binding Output</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding Output</em>'.
   * @see org.eclipse.wst.wsdl.BindingOutput
   * @generated
   */
	EClass getBindingOutput();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.BindingOutput#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wsdl.BindingOutput#getName()
   * @see #getBindingOutput()
   * @generated
   */
	EAttribute getBindingOutput_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.BindingOutput#getEOutput <em>EOutput</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EOutput</em>'.
   * @see org.eclipse.wsdl.BindingOutput#getEOutput()
   * @see #getBindingOutput()
   * @generated
   */
	EReference getBindingOutput_EOutput();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.BindingFault <em>Binding Fault</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding Fault</em>'.
   * @see org.eclipse.wst.wsdl.BindingFault
   * @generated
   */
	EClass getBindingFault();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.BindingFault#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wsdl.BindingFault#getName()
   * @see #getBindingFault()
   * @generated
   */
	EAttribute getBindingFault_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.BindingFault#getEFault <em>EFault</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EFault</em>'.
   * @see org.eclipse.wsdl.BindingFault#getEFault()
   * @see #getBindingFault()
   * @generated
   */
	EReference getBindingFault_EFault();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Namespace <em>Namespace</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Namespace</em>'.
   * @see org.eclipse.wst.wsdl.Namespace
   * @generated
   */
	EClass getNamespace();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Namespace#getURI <em>URI</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>URI</em>'.
   * @see org.eclipse.wsdl.Namespace#getURI()
   * @see #getNamespace()
   * @generated
   */
	EAttribute getNamespace_URI();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.Namespace#getPrefix <em>Prefix</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Prefix</em>'.
   * @see org.eclipse.wsdl.Namespace#getPrefix()
   * @see #getNamespace()
   * @generated
   */
	EAttribute getNamespace_Prefix();

  /**
   * Returns the meta object for class '{@link javax.wsdl.PortType <em>IPort Type</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IPort Type</em>'.
   * @see javax.wsdl.PortType
   * @model instanceClass="javax.wsdl.PortType" 
   * @generated
   */
	EClass getIPortType();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Operation <em>IOperation</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IOperation</em>'.
   * @see javax.wsdl.Operation
   * @model instanceClass="javax.wsdl.Operation" 
   * @generated
   */
	EClass getIOperation();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Input <em>IInput</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IInput</em>'.
   * @see javax.wsdl.Input
   * @model instanceClass="javax.wsdl.Input" 
   * @generated
   */
	EClass getIInput();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Output <em>IOutput</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IOutput</em>'.
   * @see javax.wsdl.Output
   * @model instanceClass="javax.wsdl.Output" 
   * @generated
   */
	EClass getIOutput();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Fault <em>IFault</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IFault</em>'.
   * @see javax.wsdl.Fault
   * @model instanceClass="javax.wsdl.Fault" 
   * @generated
   */
	EClass getIFault();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Message <em>IMessage</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IMessage</em>'.
   * @see javax.wsdl.Message
   * @model instanceClass="javax.wsdl.Message" 
   * @generated
   */
	EClass getIMessage();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Part <em>IPart</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IPart</em>'.
   * @see javax.wsdl.Part
   * @model instanceClass="javax.wsdl.Part" 
   * @generated
   */
	EClass getIPart();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Service <em>IService</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IService</em>'.
   * @see javax.wsdl.Service
   * @model instanceClass="javax.wsdl.Service" 
   * @generated
   */
	EClass getIService();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Port <em>IPort</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IPort</em>'.
   * @see javax.wsdl.Port
   * @model instanceClass="javax.wsdl.Port" 
   * @generated
   */
	EClass getIPort();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Binding <em>IBinding</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IBinding</em>'.
   * @see javax.wsdl.Binding
   * @model instanceClass="javax.wsdl.Binding" 
   * @generated
   */
	EClass getIBinding();

  /**
   * Returns the meta object for class '{@link javax.wsdl.BindingOperation <em>IBinding Operation</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IBinding Operation</em>'.
   * @see javax.wsdl.BindingOperation
   * @model instanceClass="javax.wsdl.BindingOperation" 
   * @generated
   */
	EClass getIBindingOperation();

  /**
   * Returns the meta object for class '{@link javax.wsdl.BindingInput <em>IBinding Input</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IBinding Input</em>'.
   * @see javax.wsdl.BindingInput
   * @model instanceClass="javax.wsdl.BindingInput" 
   * @generated
   */
	EClass getIBindingInput();

  /**
   * Returns the meta object for class '{@link javax.wsdl.BindingOutput <em>IBinding Output</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IBinding Output</em>'.
   * @see javax.wsdl.BindingOutput
   * @model instanceClass="javax.wsdl.BindingOutput" 
   * @generated
   */
	EClass getIBindingOutput();

  /**
   * Returns the meta object for class '{@link javax.wsdl.BindingFault <em>IBinding Fault</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IBinding Fault</em>'.
   * @see javax.wsdl.BindingFault
   * @model instanceClass="javax.wsdl.BindingFault" 
   * @generated
   */
	EClass getIBindingFault();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.ExtensibilityElement <em>IExtensibility Element</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IExtensibility Element</em>'.
   * @see javax.wsdl.extensions.ExtensibilityElement
   * @model instanceClass="javax.wsdl.extensions.ExtensibilityElement" 
   * @generated
   */
	EClass getIExtensibilityElement();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Definition <em>IDefinition</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IDefinition</em>'.
   * @see javax.wsdl.Definition
   * @model instanceClass="javax.wsdl.Definition" 
   * @generated
   */
	EClass getIDefinition();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Import <em>IImport</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IImport</em>'.
   * @see javax.wsdl.Import
   * @model instanceClass="javax.wsdl.Import" 
   * @generated
   */
	EClass getIImport();

  /**
   * Returns the meta object for class '{@link java.util.List <em>IList</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IList</em>'.
   * @see java.util.List
   * @model instanceClass="java.util.List" 
   * @generated
   */
	EClass getIList();

  /**
   * Returns the meta object for class '{@link java.util.Map <em>IMap</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IMap</em>'.
   * @see java.util.Map
   * @model instanceClass="java.util.Map" 
   * @generated
   */
	EClass getIMap();

  /**
   * Returns the meta object for class '{@link java.net.URL <em>IURL</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IURL</em>'.
   * @see java.net.URL
   * @model instanceClass="java.net.URL" 
   * @generated
   */
	EClass getIURL();

  /**
   * Returns the meta object for class '{@link javax.wsdl.extensions.ExtensionRegistry <em>IExtension Registry</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IExtension Registry</em>'.
   * @see javax.wsdl.extensions.ExtensionRegistry
   * @model instanceClass="javax.wsdl.extensions.ExtensionRegistry" 
   * @generated
   */
	EClass getIExtensionRegistry();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.Types <em>Types</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Types</em>'.
   * @see org.eclipse.wst.wsdl.Types
   * @generated
   */
	EClass getTypes();

  /**
   * Returns the meta object for class '{@link java.util.Iterator <em>IIterator</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>IIterator</em>'.
   * @see java.util.Iterator
   * @model instanceClass="java.util.Iterator" 
   * @generated
   */
	EClass getIIterator();

  /**
   * Returns the meta object for class '{@link javax.wsdl.Types <em>ITypes</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>ITypes</em>'.
   * @see javax.wsdl.Types
   * @model instanceClass="javax.wsdl.Types" 
   * @generated
   */
	EClass getITypes();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.UnknownExtensibilityElement <em>Unknown Extensibility Element</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Unknown Extensibility Element</em>'.
   * @see org.eclipse.wst.wsdl.UnknownExtensibilityElement
   * @generated
   */
	EClass getUnknownExtensibilityElement();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.UnknownExtensibilityElement#getParent <em>Parent</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Parent</em>'.
   * @see org.eclipse.wsdl.UnknownExtensibilityElement#getParent()
   * @see #getUnknownExtensibilityElement()
   * @generated
   */
  EReference getUnknownExtensibilityElement_Parent();

  /**
   * Returns the meta object for the reference list '{@link org.eclipse.wsdl.UnknownExtensibilityElement#getChildren <em>Children</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Children</em>'.
   * @see org.eclipse.wsdl.UnknownExtensibilityElement#getChildren()
   * @see #getUnknownExtensibilityElement()
   * @generated
   */
  EReference getUnknownExtensibilityElement_Children();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.XSDSchemaExtensibilityElement <em>XSD Schema Extensibility Element</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>XSD Schema Extensibility Element</em>'.
   * @see org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement
   * @generated
   */
	EClass getXSDSchemaExtensibilityElement();

  /**
   * Returns the meta object for the containment reference '{@link org.eclipse.wsdl.XSDSchemaExtensibilityElement#getSchema <em>Schema</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Schema</em>'.
   * @see org.eclipse.wsdl.XSDSchemaExtensibilityElement#getSchema()
   * @see #getXSDSchemaExtensibilityElement()
   * @generated
   */
  EReference getXSDSchemaExtensibilityElement_Schema();

  /**
   * Returns the meta object for class '{@link org.eclipse.wsdl.MessageReference <em>Message Reference</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for class '<em>Message Reference</em>'.
   * @see org.eclipse.wst.wsdl.MessageReference
   * @generated
   */
	EClass getMessageReference();

  /**
   * Returns the meta object for the attribute '{@link org.eclipse.wsdl.MessageReference#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.eclipse.wsdl.MessageReference#getName()
   * @see #getMessageReference()
   * @generated
   */
	EAttribute getMessageReference_Name();

  /**
   * Returns the meta object for the reference '{@link org.eclipse.wsdl.MessageReference#getEMessage <em>EMessage</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>EMessage</em>'.
   * @see org.eclipse.wsdl.MessageReference#getEMessage()
   * @see #getMessageReference()
   * @generated
   */
  EReference getMessageReference_EMessage();

  /**
   * Returns the meta object for data type '{@link javax.xml.namespace.QName <em>QName</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for data type '<em>QName</em>'.
   * @see javax.xml.namespace.QName
   * @model instanceClass="javax.xml.namespace.QName"
   * @generated
   */
	EDataType getQName();

  /**
   * Returns the meta object for data type '{@link javax.wsdl.OperationType <em>Operation Type</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Operation Type</em>'.
   * @see javax.wsdl.OperationType
   * @model instanceClass="javax.wsdl.OperationType"
   * @generated
   */
	EDataType getOperationType();

  /**
   * Returns the meta object for data type '{@link org.w3c.dom.Element <em>DOM Element</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for data type '<em>DOM Element</em>'.
   * @see org.w3c.dom.Element
   * @model instanceClass="org.w3c.dom.Element"
   * @generated
   */
	EDataType getDOMElement();

  /**
   * Returns the meta object for data type '{@link javax.wsdl.WSDLException <em>Exception</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for data type '<em>Exception</em>'.
   * @see javax.wsdl.WSDLException
   * @model instanceClass="javax.wsdl.WSDLException"
   * @generated
   */
	EDataType getWSDLException();

  /**
   * Returns the meta object for data type '{@link org.w3c.dom.Document <em>DOM Document</em>}'.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the meta object for data type '<em>DOM Document</em>'.
   * @see org.w3c.dom.Document
   * @model instanceClass="org.w3c.dom.Document"
   * @generated
   */
	EDataType getDOMDocument();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
	WSDLFactory getWSDLFactory();

} //WSDLPackage
