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
package org.eclipse.jst.ws.jaxws.dom.runtime.api;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomFactory
 * @model kind="package"
 * @generated
 */
public interface DomPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "dom"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///org/eclipse/jst/ws/jaxws/dom/runtime/dom.ecore"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "org.eclipse.jst.ws.jaxws.dom.runtime"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DomPackage eINSTANCE = org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IDOMImpl <em>IDOM</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IDOMImpl
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIDOM()
	 * @generated
	 */
	int IDOM = 0;

	/**
	 * The feature id for the '<em><b>Web Service Projects</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDOM__WEB_SERVICE_PROJECTS = 0;

	/**
	 * The number of structural features of the '<em>IDOM</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IDOM_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IJavaWebServiceElementImpl <em>IJava Web Service Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IJavaWebServiceElementImpl
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIJavaWebServiceElement()
	 * @generated
	 */
	int IJAVA_WEB_SERVICE_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IJAVA_WEB_SERVICE_ELEMENT__NAME = 1;

	/**
	 * The number of structural features of the '<em>IJava Web Service Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IServiceEndpointInterfaceImpl <em>IService Endpoint Interface</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IServiceEndpointInterfaceImpl
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIServiceEndpointInterface()
	 * @generated
	 */
	int ISERVICE_ENDPOINT_INTERFACE = 2;

	/**
	 * The feature id for the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTATION = IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISERVICE_ENDPOINT_INTERFACE__NAME = IJAVA_WEB_SERVICE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISERVICE_ENDPOINT_INTERFACE__IMPLICIT = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Implementing Web Services</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Web Methods</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Target Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Soap Binding Style</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_STYLE = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Soap Binding Use</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_USE = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Soap Binding Parameter Style</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_PARAMETER_STYLE = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>IService Endpoint Interface</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ISERVICE_ENDPOINT_INTERFACE_FEATURE_COUNT = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebMethodImpl <em>IWeb Method</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebMethodImpl
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIWebMethod()
	 * @generated
	 */
	int IWEB_METHOD = 3;

	/**
	 * The feature id for the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_METHOD__IMPLEMENTATION = IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_METHOD__NAME = IJAVA_WEB_SERVICE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_METHOD__PARAMETERS = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Excluded</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_METHOD__EXCLUDED = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Soap Binding Style</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_METHOD__SOAP_BINDING_STYLE = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Soap Binding Use</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_METHOD__SOAP_BINDING_USE = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Soap Binding Parameter Style</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_METHOD__SOAP_BINDING_PARAMETER_STYLE = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>IWeb Method</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_METHOD_FEATURE_COUNT = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebParamImpl <em>IWeb Param</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebParamImpl
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIWebParam()
	 * @generated
	 */
	int IWEB_PARAM = 4;

	/**
	 * The feature id for the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_PARAM__IMPLEMENTATION = IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_PARAM__NAME = IJAVA_WEB_SERVICE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_PARAM__KIND = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_PARAM__TYPE_NAME = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Part Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_PARAM__PART_NAME = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Target Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_PARAM__TARGET_NAMESPACE = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Header</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_PARAM__HEADER = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>IWeb Param</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_PARAM_FEATURE_COUNT = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceImpl <em>IWeb Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceImpl
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIWebService()
	 * @generated
	 */
	int IWEB_SERVICE = 5;

	/**
	 * The feature id for the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_SERVICE__IMPLEMENTATION = IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_SERVICE__NAME = IJAVA_WEB_SERVICE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Service Endpoint</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_SERVICE__SERVICE_ENDPOINT = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Target Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_SERVICE__TARGET_NAMESPACE = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Port Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_SERVICE__PORT_NAME = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Wsdl Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_SERVICE__WSDL_LOCATION = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>IWeb Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_SERVICE_FEATURE_COUNT = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceProjectImpl <em>IWeb Service Project</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceProjectImpl
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIWebServiceProject()
	 * @generated
	 */
	int IWEB_SERVICE_PROJECT = 6;

	/**
	 * The feature id for the '<em><b>Web Services</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_SERVICE_PROJECT__WEB_SERVICES = 0;

	/**
	 * The feature id for the '<em><b>Service Endpoint Interfaces</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_SERVICE_PROJECT__SERVICE_ENDPOINT_INTERFACES = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_SERVICE_PROJECT__NAME = 2;

	/**
	 * The number of structural features of the '<em>IWeb Service Project</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_SERVICE_PROJECT_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebTypeImpl <em>IWeb Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebTypeImpl
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIWebType()
	 * @generated
	 */
	int IWEB_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_TYPE__IMPLEMENTATION = IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_TYPE__NAME = IJAVA_WEB_SERVICE_ELEMENT__NAME;

	/**
	 * The number of structural features of the '<em>IWeb Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IWEB_TYPE_FEATURE_COUNT = IJAVA_WEB_SERVICE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind <em>Web Param Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getWebParamKind()
	 * @generated
	 */
	int WEB_PARAM_KIND = 8;


	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle <em>SOAP Binding Style</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getSOAPBindingStyle()
	 * @generated
	 */
	int SOAP_BINDING_STYLE = 9;

	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse <em>SOAP Binding Use</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getSOAPBindingUse()
	 * @generated
	 */
	int SOAP_BINDING_USE = 10;

	/**
	 * The meta object id for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle <em>SOAP Binding Parameter Style</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getSOAPBindingParameterStyle()
	 * @generated
	 */
	int SOAP_BINDING_PARAMETER_STYLE = 11;


	/**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM <em>IDOM</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IDOM</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM
	 * @generated
	 */
	EClass getIDOM();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM#getWebServiceProjects <em>Web Service Projects</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Web Service Projects</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM#getWebServiceProjects()
	 * @see #getIDOM()
	 * @generated
	 */
	EReference getIDOM_WebServiceProjects();

	/**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement <em>IJava Web Service Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IJava Web Service Element</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement
	 * @generated
	 */
	EClass getIJavaWebServiceElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement#getImplementation <em>Implementation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Implementation</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement#getImplementation()
	 * @see #getIJavaWebServiceElement()
	 * @generated
	 */
	EAttribute getIJavaWebServiceElement_Implementation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement#getName()
	 * @see #getIJavaWebServiceElement()
	 * @generated
	 */
	EAttribute getIJavaWebServiceElement_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface <em>IService Endpoint Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IService Endpoint Interface</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface
	 * @generated
	 */
	EClass getIServiceEndpointInterface();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#isImplicit <em>Implicit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Implicit</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#isImplicit()
	 * @see #getIServiceEndpointInterface()
	 * @generated
	 */
	EAttribute getIServiceEndpointInterface_Implicit();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getImplementingWebServices <em>Implementing Web Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Implementing Web Services</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getImplementingWebServices()
	 * @see #getIServiceEndpointInterface()
	 * @generated
	 */
	EReference getIServiceEndpointInterface_ImplementingWebServices();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getWebMethods <em>Web Methods</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Web Methods</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getWebMethods()
	 * @see #getIServiceEndpointInterface()
	 * @generated
	 */
	EReference getIServiceEndpointInterface_WebMethods();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getTargetNamespace <em>Target Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target Namespace</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getTargetNamespace()
	 * @see #getIServiceEndpointInterface()
	 * @generated
	 */
	EAttribute getIServiceEndpointInterface_TargetNamespace();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getSoapBindingStyle <em>Soap Binding Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Soap Binding Style</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getSoapBindingStyle()
	 * @see #getIServiceEndpointInterface()
	 * @generated
	 */
	EAttribute getIServiceEndpointInterface_SoapBindingStyle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getSoapBindingUse <em>Soap Binding Use</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Soap Binding Use</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getSoapBindingUse()
	 * @see #getIServiceEndpointInterface()
	 * @generated
	 */
	EAttribute getIServiceEndpointInterface_SoapBindingUse();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getSoapBindingParameterStyle <em>Soap Binding Parameter Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Soap Binding Parameter Style</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getSoapBindingParameterStyle()
	 * @see #getIServiceEndpointInterface()
	 * @generated
	 */
	EAttribute getIServiceEndpointInterface_SoapBindingParameterStyle();

	/**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod <em>IWeb Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IWeb Method</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod
	 * @generated
	 */
	EClass getIWebMethod();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod#getParameters()
	 * @see #getIWebMethod()
	 * @generated
	 */
	EReference getIWebMethod_Parameters();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod#isExcluded <em>Excluded</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Excluded</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod#isExcluded()
	 * @see #getIWebMethod()
	 * @generated
	 */
	EAttribute getIWebMethod_Excluded();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod#getSoapBindingStyle <em>Soap Binding Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Soap Binding Style</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod#getSoapBindingStyle()
	 * @see #getIWebMethod()
	 * @generated
	 */
	EAttribute getIWebMethod_SoapBindingStyle();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod#getSoapBindingUse <em>Soap Binding Use</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Soap Binding Use</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod#getSoapBindingUse()
	 * @see #getIWebMethod()
	 * @generated
	 */
	EAttribute getIWebMethod_SoapBindingUse();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod#getSoapBindingParameterStyle <em>Soap Binding Parameter Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Soap Binding Parameter Style</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod#getSoapBindingParameterStyle()
	 * @see #getIWebMethod()
	 * @generated
	 */
	EAttribute getIWebMethod_SoapBindingParameterStyle();

	/**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam <em>IWeb Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IWeb Param</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam
	 * @generated
	 */
	EClass getIWebParam();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#getKind()
	 * @see #getIWebParam()
	 * @generated
	 */
	EAttribute getIWebParam_Kind();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#getTypeName()
	 * @see #getIWebParam()
	 * @generated
	 */
	EAttribute getIWebParam_TypeName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#getPartName <em>Part Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Part Name</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#getPartName()
	 * @see #getIWebParam()
	 * @generated
	 */
	EAttribute getIWebParam_PartName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#getTargetNamespace <em>Target Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target Namespace</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#getTargetNamespace()
	 * @see #getIWebParam()
	 * @generated
	 */
	EAttribute getIWebParam_TargetNamespace();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#isHeader <em>Header</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Header</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#isHeader()
	 * @see #getIWebParam()
	 * @generated
	 */
	EAttribute getIWebParam_Header();

	/**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService <em>IWeb Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IWeb Service</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService
	 * @generated
	 */
	EClass getIWebService();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService#getServiceEndpoint <em>Service Endpoint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Service Endpoint</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService#getServiceEndpoint()
	 * @see #getIWebService()
	 * @generated
	 */
	EReference getIWebService_ServiceEndpoint();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService#getTargetNamespace <em>Target Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target Namespace</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService#getTargetNamespace()
	 * @see #getIWebService()
	 * @generated
	 */
	EAttribute getIWebService_TargetNamespace();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService#getPortName <em>Port Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Port Name</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService#getPortName()
	 * @see #getIWebService()
	 * @generated
	 */
	EAttribute getIWebService_PortName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService#getWsdlLocation <em>Wsdl Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wsdl Location</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService#getWsdlLocation()
	 * @see #getIWebService()
	 * @generated
	 */
	EAttribute getIWebService_WsdlLocation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject <em>IWeb Service Project</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IWeb Service Project</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject
	 * @generated
	 */
	EClass getIWebServiceProject();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject#getWebServices <em>Web Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Web Services</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject#getWebServices()
	 * @see #getIWebServiceProject()
	 * @generated
	 */
	EReference getIWebServiceProject_WebServices();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject#getServiceEndpointInterfaces <em>Service Endpoint Interfaces</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Service Endpoint Interfaces</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject#getServiceEndpointInterfaces()
	 * @see #getIWebServiceProject()
	 * @generated
	 */
	EReference getIWebServiceProject_ServiceEndpointInterfaces();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject#getName()
	 * @see #getIWebServiceProject()
	 * @generated
	 */
	EAttribute getIWebServiceProject_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebType <em>IWeb Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>IWeb Type</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebType
	 * @generated
	 */
	EClass getIWebType();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind <em>Web Param Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Web Param Kind</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind
	 * @generated
	 */
	EEnum getWebParamKind();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle <em>SOAP Binding Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>SOAP Binding Style</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle
	 * @generated
	 */
	EEnum getSOAPBindingStyle();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse <em>SOAP Binding Use</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>SOAP Binding Use</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse
	 * @generated
	 */
	EEnum getSOAPBindingUse();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle <em>SOAP Binding Parameter Style</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>SOAP Binding Parameter Style</em>'.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle
	 * @generated
	 */
	EEnum getSOAPBindingParameterStyle();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DomFactory getDomFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IDOMImpl <em>IDOM</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IDOMImpl
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIDOM()
		 * @generated
		 */
		EClass IDOM = eINSTANCE.getIDOM();

		/**
		 * The meta object literal for the '<em><b>Web Service Projects</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IDOM__WEB_SERVICE_PROJECTS = eINSTANCE.getIDOM_WebServiceProjects();

		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IJavaWebServiceElementImpl <em>IJava Web Service Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IJavaWebServiceElementImpl
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIJavaWebServiceElement()
		 * @generated
		 */
		EClass IJAVA_WEB_SERVICE_ELEMENT = eINSTANCE.getIJavaWebServiceElement();

		/**
		 * The meta object literal for the '<em><b>Implementation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IJAVA_WEB_SERVICE_ELEMENT__IMPLEMENTATION = eINSTANCE.getIJavaWebServiceElement_Implementation();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IJAVA_WEB_SERVICE_ELEMENT__NAME = eINSTANCE.getIJavaWebServiceElement_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IServiceEndpointInterfaceImpl <em>IService Endpoint Interface</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IServiceEndpointInterfaceImpl
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIServiceEndpointInterface()
		 * @generated
		 */
		EClass ISERVICE_ENDPOINT_INTERFACE = eINSTANCE.getIServiceEndpointInterface();

		/**
		 * The meta object literal for the '<em><b>Implicit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ISERVICE_ENDPOINT_INTERFACE__IMPLICIT = eINSTANCE.getIServiceEndpointInterface_Implicit();

		/**
		 * The meta object literal for the '<em><b>Implementing Web Services</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES = eINSTANCE.getIServiceEndpointInterface_ImplementingWebServices();

		/**
		 * The meta object literal for the '<em><b>Web Methods</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS = eINSTANCE.getIServiceEndpointInterface_WebMethods();

		/**
		 * The meta object literal for the '<em><b>Target Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE = eINSTANCE.getIServiceEndpointInterface_TargetNamespace();

		/**
		 * The meta object literal for the '<em><b>Soap Binding Style</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_STYLE = eINSTANCE.getIServiceEndpointInterface_SoapBindingStyle();

		/**
		 * The meta object literal for the '<em><b>Soap Binding Use</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_USE = eINSTANCE.getIServiceEndpointInterface_SoapBindingUse();

		/**
		 * The meta object literal for the '<em><b>Soap Binding Parameter Style</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_PARAMETER_STYLE = eINSTANCE.getIServiceEndpointInterface_SoapBindingParameterStyle();

		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebMethodImpl <em>IWeb Method</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebMethodImpl
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIWebMethod()
		 * @generated
		 */
		EClass IWEB_METHOD = eINSTANCE.getIWebMethod();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IWEB_METHOD__PARAMETERS = eINSTANCE.getIWebMethod_Parameters();

		/**
		 * The meta object literal for the '<em><b>Excluded</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_METHOD__EXCLUDED = eINSTANCE.getIWebMethod_Excluded();

		/**
		 * The meta object literal for the '<em><b>Soap Binding Style</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_METHOD__SOAP_BINDING_STYLE = eINSTANCE.getIWebMethod_SoapBindingStyle();

		/**
		 * The meta object literal for the '<em><b>Soap Binding Use</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_METHOD__SOAP_BINDING_USE = eINSTANCE.getIWebMethod_SoapBindingUse();

		/**
		 * The meta object literal for the '<em><b>Soap Binding Parameter Style</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_METHOD__SOAP_BINDING_PARAMETER_STYLE = eINSTANCE.getIWebMethod_SoapBindingParameterStyle();

		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebParamImpl <em>IWeb Param</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebParamImpl
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIWebParam()
		 * @generated
		 */
		EClass IWEB_PARAM = eINSTANCE.getIWebParam();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_PARAM__KIND = eINSTANCE.getIWebParam_Kind();

		/**
		 * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_PARAM__TYPE_NAME = eINSTANCE.getIWebParam_TypeName();

		/**
		 * The meta object literal for the '<em><b>Part Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_PARAM__PART_NAME = eINSTANCE.getIWebParam_PartName();

		/**
		 * The meta object literal for the '<em><b>Target Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_PARAM__TARGET_NAMESPACE = eINSTANCE.getIWebParam_TargetNamespace();

		/**
		 * The meta object literal for the '<em><b>Header</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_PARAM__HEADER = eINSTANCE.getIWebParam_Header();

		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceImpl <em>IWeb Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceImpl
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIWebService()
		 * @generated
		 */
		EClass IWEB_SERVICE = eINSTANCE.getIWebService();

		/**
		 * The meta object literal for the '<em><b>Service Endpoint</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IWEB_SERVICE__SERVICE_ENDPOINT = eINSTANCE.getIWebService_ServiceEndpoint();

		/**
		 * The meta object literal for the '<em><b>Target Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_SERVICE__TARGET_NAMESPACE = eINSTANCE.getIWebService_TargetNamespace();

		/**
		 * The meta object literal for the '<em><b>Port Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_SERVICE__PORT_NAME = eINSTANCE.getIWebService_PortName();

		/**
		 * The meta object literal for the '<em><b>Wsdl Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_SERVICE__WSDL_LOCATION = eINSTANCE.getIWebService_WsdlLocation();

		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceProjectImpl <em>IWeb Service Project</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceProjectImpl
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIWebServiceProject()
		 * @generated
		 */
		EClass IWEB_SERVICE_PROJECT = eINSTANCE.getIWebServiceProject();

		/**
		 * The meta object literal for the '<em><b>Web Services</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IWEB_SERVICE_PROJECT__WEB_SERVICES = eINSTANCE.getIWebServiceProject_WebServices();

		/**
		 * The meta object literal for the '<em><b>Service Endpoint Interfaces</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IWEB_SERVICE_PROJECT__SERVICE_ENDPOINT_INTERFACES = eINSTANCE.getIWebServiceProject_ServiceEndpointInterfaces();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IWEB_SERVICE_PROJECT__NAME = eINSTANCE.getIWebServiceProject_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebTypeImpl <em>IWeb Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebTypeImpl
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getIWebType()
		 * @generated
		 */
		EClass IWEB_TYPE = eINSTANCE.getIWebType();

		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind <em>Web Param Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getWebParamKind()
		 * @generated
		 */
		EEnum WEB_PARAM_KIND = eINSTANCE.getWebParamKind();

		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle <em>SOAP Binding Style</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getSOAPBindingStyle()
		 * @generated
		 */
		EEnum SOAP_BINDING_STYLE = eINSTANCE.getSOAPBindingStyle();

		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse <em>SOAP Binding Use</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getSOAPBindingUse()
		 * @generated
		 */
		EEnum SOAP_BINDING_USE = eINSTANCE.getSOAPBindingUse();

		/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle <em>SOAP Binding Parameter Style</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle
		 * @see org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomPackageImpl#getSOAPBindingParameterStyle()
		 * @generated
		 */
		EEnum SOAP_BINDING_PARAMETER_STYLE = eINSTANCE.getSOAPBindingParameterStyle();

	}

} //DomPackage
