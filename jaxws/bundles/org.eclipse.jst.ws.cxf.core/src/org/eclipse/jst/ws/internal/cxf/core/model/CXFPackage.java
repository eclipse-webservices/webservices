/**
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *
 * $Id: CXFPackage.java,v 1.5 2012/03/06 22:05:08 sclarke Exp $
 */
package org.eclipse.jst.ws.internal.cxf.core.model;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;

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
 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFFactory
 * @model kind="package"
 * @generated
 */
public interface CXFPackage extends EPackage {
    /**
	 * The package name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String eNAME = "model";

    /**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_URI = "http:///org/eclipse/jst/ws/internal/cxf/core/model.ecore";

    /**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    String eNS_PREFIX = "org.eclipse.jst.ws.internal.cxf.core.model";

    /**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    CXFPackage eINSTANCE = org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl.init();

    /**
	 * The meta object id for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext <em>Context</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getCXFContext()
	 * @generated
	 */
    int CXF_CONTEXT = 0;

    /**
	 * The feature id for the '<em><b>Default Runtime Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__DEFAULT_RUNTIME_LOCATION = 0;

    /**
	 * The feature id for the '<em><b>Default Runtime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__DEFAULT_RUNTIME_TYPE = 1;

    /**
	 * The feature id for the '<em><b>Default Runtime Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__DEFAULT_RUNTIME_VERSION = 2;

    /**
	 * The feature id for the '<em><b>Verbose</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__VERBOSE = 3;

    /**
	 * The feature id for the '<em><b>Generate Ant Build File</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__GENERATE_ANT_BUILD_FILE = 4;

    /**
	 * The feature id for the '<em><b>Generate Client</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__GENERATE_CLIENT = 5;

    /**
	 * The feature id for the '<em><b>Generate Server</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__GENERATE_SERVER = 6;

    /**
	 * The feature id for the '<em><b>Databinding</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__DATABINDING = 7;

    /**
	 * The feature id for the '<em><b>Frontend</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__FRONTEND = 8;

    /**
	 * The feature id for the '<em><b>Use Spring Application Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__USE_SPRING_APPLICATION_CONTEXT = 9;

    /**
	 * The feature id for the '<em><b>Export CXF Classpath Container</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__EXPORT_CXF_CLASSPATH_CONTAINER = 10;

    /**
	 * The feature id for the '<em><b>Installations</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT__INSTALLATIONS = 11;

    /**
	 * The number of structural features of the '<em>Context</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_CONTEXT_FEATURE_COUNT = 12;

    /**
	 * The meta object id for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl <em>Data Model</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getCXFDataModel()
	 * @generated
	 */
    int CXF_DATA_MODEL = 1;

    /**
	 * The feature id for the '<em><b>Default Runtime Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__DEFAULT_RUNTIME_LOCATION = CXF_CONTEXT__DEFAULT_RUNTIME_LOCATION;

    /**
	 * The feature id for the '<em><b>Default Runtime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__DEFAULT_RUNTIME_TYPE = CXF_CONTEXT__DEFAULT_RUNTIME_TYPE;

    /**
	 * The feature id for the '<em><b>Default Runtime Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__DEFAULT_RUNTIME_VERSION = CXF_CONTEXT__DEFAULT_RUNTIME_VERSION;

    /**
	 * The feature id for the '<em><b>Verbose</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__VERBOSE = CXF_CONTEXT__VERBOSE;

    /**
	 * The feature id for the '<em><b>Generate Ant Build File</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__GENERATE_ANT_BUILD_FILE = CXF_CONTEXT__GENERATE_ANT_BUILD_FILE;

    /**
	 * The feature id for the '<em><b>Generate Client</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__GENERATE_CLIENT = CXF_CONTEXT__GENERATE_CLIENT;

    /**
	 * The feature id for the '<em><b>Generate Server</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__GENERATE_SERVER = CXF_CONTEXT__GENERATE_SERVER;

    /**
	 * The feature id for the '<em><b>Databinding</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__DATABINDING = CXF_CONTEXT__DATABINDING;

    /**
	 * The feature id for the '<em><b>Frontend</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__FRONTEND = CXF_CONTEXT__FRONTEND;

    /**
	 * The feature id for the '<em><b>Use Spring Application Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__USE_SPRING_APPLICATION_CONTEXT = CXF_CONTEXT__USE_SPRING_APPLICATION_CONTEXT;

    /**
	 * The feature id for the '<em><b>Export CXF Classpath Container</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__EXPORT_CXF_CLASSPATH_CONTAINER = CXF_CONTEXT__EXPORT_CXF_CLASSPATH_CONTAINER;

    /**
	 * The feature id for the '<em><b>Installations</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__INSTALLATIONS = CXF_CONTEXT__INSTALLATIONS;

    /**
	 * The feature id for the '<em><b>Project Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__PROJECT_NAME = CXF_CONTEXT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Resource Directory</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__RESOURCE_DIRECTORY = CXF_CONTEXT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Class Directory</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__CLASS_DIRECTORY = CXF_CONTEXT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Wsdl File Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__WSDL_FILE_NAME = CXF_CONTEXT_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Wsdl URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__WSDL_URL = CXF_CONTEXT_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Config Wsdl Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__CONFIG_WSDL_LOCATION = CXF_CONTEXT_FEATURE_COUNT + 5;

    /**
	 * The feature id for the '<em><b>Fully Qualified Java Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME = CXF_CONTEXT_FEATURE_COUNT + 6;

    /**
	 * The feature id for the '<em><b>Fully Qualified Java Interface Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME = CXF_CONTEXT_FEATURE_COUNT + 7;

    /**
	 * The feature id for the '<em><b>Config Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__CONFIG_ID = CXF_CONTEXT_FEATURE_COUNT + 8;

    /**
	 * The feature id for the '<em><b>Target Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__TARGET_NAMESPACE = CXF_CONTEXT_FEATURE_COUNT + 9;

    /**
	 * The feature id for the '<em><b>Endpoint Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__ENDPOINT_NAME = CXF_CONTEXT_FEATURE_COUNT + 10;

    /**
	 * The feature id for the '<em><b>Service Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__SERVICE_NAME = CXF_CONTEXT_FEATURE_COUNT + 11;

    /**
	 * The feature id for the '<em><b>Wsdl Definition</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__WSDL_DEFINITION = CXF_CONTEXT_FEATURE_COUNT + 12;

    /**
	 * The feature id for the '<em><b>Wsdl Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL__WSDL_LOCATION = CXF_CONTEXT_FEATURE_COUNT + 13;

    /**
	 * The number of structural features of the '<em>Data Model</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_DATA_MODEL_FEATURE_COUNT = CXF_CONTEXT_FEATURE_COUNT + 14;

    /**
	 * The meta object id for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext <em>Java2 WS Context</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getJava2WSContext()
	 * @generated
	 */
    int JAVA2_WS_CONTEXT = 2;

    /**
	 * The feature id for the '<em><b>Default Runtime Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__DEFAULT_RUNTIME_LOCATION = CXF_CONTEXT__DEFAULT_RUNTIME_LOCATION;

    /**
	 * The feature id for the '<em><b>Default Runtime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__DEFAULT_RUNTIME_TYPE = CXF_CONTEXT__DEFAULT_RUNTIME_TYPE;

    /**
	 * The feature id for the '<em><b>Default Runtime Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__DEFAULT_RUNTIME_VERSION = CXF_CONTEXT__DEFAULT_RUNTIME_VERSION;

    /**
	 * The feature id for the '<em><b>Verbose</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__VERBOSE = CXF_CONTEXT__VERBOSE;

    /**
	 * The feature id for the '<em><b>Generate Ant Build File</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__GENERATE_ANT_BUILD_FILE = CXF_CONTEXT__GENERATE_ANT_BUILD_FILE;

    /**
	 * The feature id for the '<em><b>Generate Client</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__GENERATE_CLIENT = CXF_CONTEXT__GENERATE_CLIENT;

    /**
	 * The feature id for the '<em><b>Generate Server</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__GENERATE_SERVER = CXF_CONTEXT__GENERATE_SERVER;

    /**
	 * The feature id for the '<em><b>Databinding</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__DATABINDING = CXF_CONTEXT__DATABINDING;

    /**
	 * The feature id for the '<em><b>Frontend</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__FRONTEND = CXF_CONTEXT__FRONTEND;

    /**
	 * The feature id for the '<em><b>Use Spring Application Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__USE_SPRING_APPLICATION_CONTEXT = CXF_CONTEXT__USE_SPRING_APPLICATION_CONTEXT;

    /**
	 * The feature id for the '<em><b>Export CXF Classpath Container</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__EXPORT_CXF_CLASSPATH_CONTAINER = CXF_CONTEXT__EXPORT_CXF_CLASSPATH_CONTAINER;

    /**
	 * The feature id for the '<em><b>Installations</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__INSTALLATIONS = CXF_CONTEXT__INSTALLATIONS;

    /**
	 * The feature id for the '<em><b>Soap12 Binding</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__SOAP12_BINDING = CXF_CONTEXT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Generate XSD Imports</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__GENERATE_XSD_IMPORTS = CXF_CONTEXT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Generate WSDL</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__GENERATE_WSDL = CXF_CONTEXT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Generate Wrapper Fault Beans</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__GENERATE_WRAPPER_FAULT_BEANS = CXF_CONTEXT_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Annotation Processing Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__ANNOTATION_PROCESSING_ENABLED = CXF_CONTEXT_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Generate Web Method Annotation</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__GENERATE_WEB_METHOD_ANNOTATION = CXF_CONTEXT_FEATURE_COUNT + 5;

    /**
	 * The feature id for the '<em><b>Generate Web Param Annotation</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__GENERATE_WEB_PARAM_ANNOTATION = CXF_CONTEXT_FEATURE_COUNT + 6;

    /**
	 * The feature id for the '<em><b>Generate Request Wrapper Annotation</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__GENERATE_REQUEST_WRAPPER_ANNOTATION = CXF_CONTEXT_FEATURE_COUNT + 7;

    /**
	 * The feature id for the '<em><b>Generate Response Wrapper Annotation</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT__GENERATE_RESPONSE_WRAPPER_ANNOTATION = CXF_CONTEXT_FEATURE_COUNT + 8;

    /**
	 * The feature id for the '<em><b>Generate Web Result Annotation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JAVA2_WS_CONTEXT__GENERATE_WEB_RESULT_ANNOTATION = CXF_CONTEXT_FEATURE_COUNT + 9;

				/**
	 * The number of structural features of the '<em>Java2 WS Context</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_CONTEXT_FEATURE_COUNT = CXF_CONTEXT_FEATURE_COUNT + 10;

    /**
	 * The meta object id for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl <em>Java2 WS Data Model</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getJava2WSDataModel()
	 * @generated
	 */
    int JAVA2_WS_DATA_MODEL = 3;

    /**
	 * The feature id for the '<em><b>Default Runtime Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__DEFAULT_RUNTIME_LOCATION = CXF_DATA_MODEL__DEFAULT_RUNTIME_LOCATION;

    /**
	 * The feature id for the '<em><b>Default Runtime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__DEFAULT_RUNTIME_TYPE = CXF_DATA_MODEL__DEFAULT_RUNTIME_TYPE;

    /**
	 * The feature id for the '<em><b>Default Runtime Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__DEFAULT_RUNTIME_VERSION = CXF_DATA_MODEL__DEFAULT_RUNTIME_VERSION;

    /**
	 * The feature id for the '<em><b>Verbose</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__VERBOSE = CXF_DATA_MODEL__VERBOSE;

    /**
	 * The feature id for the '<em><b>Generate Ant Build File</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__GENERATE_ANT_BUILD_FILE = CXF_DATA_MODEL__GENERATE_ANT_BUILD_FILE;

    /**
	 * The feature id for the '<em><b>Generate Client</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__GENERATE_CLIENT = CXF_DATA_MODEL__GENERATE_CLIENT;

    /**
	 * The feature id for the '<em><b>Generate Server</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__GENERATE_SERVER = CXF_DATA_MODEL__GENERATE_SERVER;

    /**
	 * The feature id for the '<em><b>Databinding</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__DATABINDING = CXF_DATA_MODEL__DATABINDING;

    /**
	 * The feature id for the '<em><b>Frontend</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__FRONTEND = CXF_DATA_MODEL__FRONTEND;

    /**
	 * The feature id for the '<em><b>Use Spring Application Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__USE_SPRING_APPLICATION_CONTEXT = CXF_DATA_MODEL__USE_SPRING_APPLICATION_CONTEXT;

    /**
	 * The feature id for the '<em><b>Export CXF Classpath Container</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__EXPORT_CXF_CLASSPATH_CONTAINER = CXF_DATA_MODEL__EXPORT_CXF_CLASSPATH_CONTAINER;

    /**
	 * The feature id for the '<em><b>Installations</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__INSTALLATIONS = CXF_DATA_MODEL__INSTALLATIONS;

    /**
	 * The feature id for the '<em><b>Project Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__PROJECT_NAME = CXF_DATA_MODEL__PROJECT_NAME;

    /**
	 * The feature id for the '<em><b>Resource Directory</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__RESOURCE_DIRECTORY = CXF_DATA_MODEL__RESOURCE_DIRECTORY;

    /**
	 * The feature id for the '<em><b>Class Directory</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__CLASS_DIRECTORY = CXF_DATA_MODEL__CLASS_DIRECTORY;

    /**
	 * The feature id for the '<em><b>Wsdl File Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__WSDL_FILE_NAME = CXF_DATA_MODEL__WSDL_FILE_NAME;

    /**
	 * The feature id for the '<em><b>Wsdl URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__WSDL_URL = CXF_DATA_MODEL__WSDL_URL;

    /**
	 * The feature id for the '<em><b>Config Wsdl Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__CONFIG_WSDL_LOCATION = CXF_DATA_MODEL__CONFIG_WSDL_LOCATION;

    /**
	 * The feature id for the '<em><b>Fully Qualified Java Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME = CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME;

    /**
	 * The feature id for the '<em><b>Fully Qualified Java Interface Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME = CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME;

    /**
	 * The feature id for the '<em><b>Config Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__CONFIG_ID = CXF_DATA_MODEL__CONFIG_ID;

    /**
	 * The feature id for the '<em><b>Target Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__TARGET_NAMESPACE = CXF_DATA_MODEL__TARGET_NAMESPACE;

    /**
	 * The feature id for the '<em><b>Endpoint Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__ENDPOINT_NAME = CXF_DATA_MODEL__ENDPOINT_NAME;

    /**
	 * The feature id for the '<em><b>Service Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__SERVICE_NAME = CXF_DATA_MODEL__SERVICE_NAME;

    /**
	 * The feature id for the '<em><b>Wsdl Definition</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__WSDL_DEFINITION = CXF_DATA_MODEL__WSDL_DEFINITION;

    /**
	 * The feature id for the '<em><b>Wsdl Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__WSDL_LOCATION = CXF_DATA_MODEL__WSDL_LOCATION;

    /**
	 * The feature id for the '<em><b>Soap12 Binding</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__SOAP12_BINDING = CXF_DATA_MODEL_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Generate XSD Imports</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__GENERATE_XSD_IMPORTS = CXF_DATA_MODEL_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Generate WSDL</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__GENERATE_WSDL = CXF_DATA_MODEL_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Generate Wrapper Fault Beans</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__GENERATE_WRAPPER_FAULT_BEANS = CXF_DATA_MODEL_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Annotation Processing Enabled</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__ANNOTATION_PROCESSING_ENABLED = CXF_DATA_MODEL_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Generate Web Method Annotation</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__GENERATE_WEB_METHOD_ANNOTATION = CXF_DATA_MODEL_FEATURE_COUNT + 5;

    /**
	 * The feature id for the '<em><b>Generate Web Param Annotation</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__GENERATE_WEB_PARAM_ANNOTATION = CXF_DATA_MODEL_FEATURE_COUNT + 6;

    /**
	 * The feature id for the '<em><b>Generate Request Wrapper Annotation</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__GENERATE_REQUEST_WRAPPER_ANNOTATION = CXF_DATA_MODEL_FEATURE_COUNT + 7;

    /**
	 * The feature id for the '<em><b>Generate Response Wrapper Annotation</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__GENERATE_RESPONSE_WRAPPER_ANNOTATION = CXF_DATA_MODEL_FEATURE_COUNT + 8;

    /**
	 * The feature id for the '<em><b>Generate Web Result Annotation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JAVA2_WS_DATA_MODEL__GENERATE_WEB_RESULT_ANNOTATION = CXF_DATA_MODEL_FEATURE_COUNT + 9;

				/**
	 * The feature id for the '<em><b>Classpath</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__CLASSPATH = CXF_DATA_MODEL_FEATURE_COUNT + 10;

    /**
	 * The feature id for the '<em><b>Java Starting Point</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__JAVA_STARTING_POINT = CXF_DATA_MODEL_FEATURE_COUNT + 11;

    /**
	 * The feature id for the '<em><b>Use Service Endpoint Interface</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__USE_SERVICE_ENDPOINT_INTERFACE = CXF_DATA_MODEL_FEATURE_COUNT + 12;

    /**
	 * The feature id for the '<em><b>Extract Interface</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__EXTRACT_INTERFACE = CXF_DATA_MODEL_FEATURE_COUNT + 13;

    /**
	 * The feature id for the '<em><b>Service Endpoint Interface Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__SERVICE_ENDPOINT_INTERFACE_NAME = CXF_DATA_MODEL_FEATURE_COUNT + 14;

    /**
	 * The feature id for the '<em><b>Method Map</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__METHOD_MAP = CXF_DATA_MODEL_FEATURE_COUNT + 15;

    /**
	 * The feature id for the '<em><b>Annotation Map</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__ANNOTATION_MAP = CXF_DATA_MODEL_FEATURE_COUNT + 16;

    /**
	 * The feature id for the '<em><b>Source Directory</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__SOURCE_DIRECTORY = CXF_DATA_MODEL_FEATURE_COUNT + 17;

    /**
	 * The feature id for the '<em><b>Port Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL__PORT_NAME = CXF_DATA_MODEL_FEATURE_COUNT + 18;

    /**
	 * The number of structural features of the '<em>Java2 WS Data Model</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int JAVA2_WS_DATA_MODEL_FEATURE_COUNT = CXF_DATA_MODEL_FEATURE_COUNT + 19;

    /**
	 * The meta object id for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext <em>WSDL2 Java Context</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getWSDL2JavaContext()
	 * @generated
	 */
    int WSDL2_JAVA_CONTEXT = 4;

    /**
	 * The feature id for the '<em><b>Default Runtime Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__DEFAULT_RUNTIME_LOCATION = CXF_CONTEXT__DEFAULT_RUNTIME_LOCATION;

    /**
	 * The feature id for the '<em><b>Default Runtime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__DEFAULT_RUNTIME_TYPE = CXF_CONTEXT__DEFAULT_RUNTIME_TYPE;

    /**
	 * The feature id for the '<em><b>Default Runtime Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__DEFAULT_RUNTIME_VERSION = CXF_CONTEXT__DEFAULT_RUNTIME_VERSION;

    /**
	 * The feature id for the '<em><b>Verbose</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__VERBOSE = CXF_CONTEXT__VERBOSE;

    /**
	 * The feature id for the '<em><b>Generate Ant Build File</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__GENERATE_ANT_BUILD_FILE = CXF_CONTEXT__GENERATE_ANT_BUILD_FILE;

    /**
	 * The feature id for the '<em><b>Generate Client</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__GENERATE_CLIENT = CXF_CONTEXT__GENERATE_CLIENT;

    /**
	 * The feature id for the '<em><b>Generate Server</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__GENERATE_SERVER = CXF_CONTEXT__GENERATE_SERVER;

    /**
	 * The feature id for the '<em><b>Databinding</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__DATABINDING = CXF_CONTEXT__DATABINDING;

    /**
	 * The feature id for the '<em><b>Frontend</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__FRONTEND = CXF_CONTEXT__FRONTEND;

    /**
	 * The feature id for the '<em><b>Use Spring Application Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__USE_SPRING_APPLICATION_CONTEXT = CXF_CONTEXT__USE_SPRING_APPLICATION_CONTEXT;

    /**
	 * The feature id for the '<em><b>Export CXF Classpath Container</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__EXPORT_CXF_CLASSPATH_CONTAINER = CXF_CONTEXT__EXPORT_CXF_CLASSPATH_CONTAINER;

    /**
	 * The feature id for the '<em><b>Installations</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__INSTALLATIONS = CXF_CONTEXT__INSTALLATIONS;

    /**
	 * The feature id for the '<em><b>Generate Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__GENERATE_IMPLEMENTATION = CXF_CONTEXT_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Process SOAP Headers</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__PROCESS_SOAP_HEADERS = CXF_CONTEXT_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Load Default Namespace Package Name Mapping</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING = CXF_CONTEXT_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Load Default Excludes Namepsace Mapping</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING = CXF_CONTEXT_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Validate</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__VALIDATE = CXF_CONTEXT_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Wsdl Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__WSDL_VERSION = CXF_CONTEXT_FEATURE_COUNT + 5;

    /**
	 * The feature id for the '<em><b>Use Default Values</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__USE_DEFAULT_VALUES = CXF_CONTEXT_FEATURE_COUNT + 6;

    /**
	 * The feature id for the '<em><b>Xjc Args</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__XJC_ARGS = CXF_CONTEXT_FEATURE_COUNT + 7;

    /**
	 * The feature id for the '<em><b>No Address Binding</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__NO_ADDRESS_BINDING = CXF_CONTEXT_FEATURE_COUNT + 8;

    /**
	 * The feature id for the '<em><b>Xjc Use Default Values</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__XJC_USE_DEFAULT_VALUES = CXF_CONTEXT_FEATURE_COUNT + 9;

    /**
	 * The feature id for the '<em><b>Xjc To String</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__XJC_TO_STRING = CXF_CONTEXT_FEATURE_COUNT + 10;

    /**
	 * The feature id for the '<em><b>Xjc To String Multi Line</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__XJC_TO_STRING_MULTI_LINE = CXF_CONTEXT_FEATURE_COUNT + 11;

    /**
	 * The feature id for the '<em><b>Xjc To String Simple</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__XJC_TO_STRING_SIMPLE = CXF_CONTEXT_FEATURE_COUNT + 12;

    /**
	 * The feature id for the '<em><b>Xjc Locator</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__XJC_LOCATOR = CXF_CONTEXT_FEATURE_COUNT + 13;

    /**
	 * The feature id for the '<em><b>Xjc Sync Methods</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__XJC_SYNC_METHODS = CXF_CONTEXT_FEATURE_COUNT + 14;

    /**
	 * The feature id for the '<em><b>Xjc Mark Generated</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__XJC_MARK_GENERATED = CXF_CONTEXT_FEATURE_COUNT + 15;

    /**
	 * The feature id for the '<em><b>Xjc Episode File</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__XJC_EPISODE_FILE = CXF_CONTEXT_FEATURE_COUNT + 16;

    /**
	 * The feature id for the '<em><b>Auto Name Resolution</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT__AUTO_NAME_RESOLUTION = CXF_CONTEXT_FEATURE_COUNT + 17;

    /**
	 * The number of structural features of the '<em>WSDL2 Java Context</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_CONTEXT_FEATURE_COUNT = CXF_CONTEXT_FEATURE_COUNT + 18;

    /**
	 * The meta object id for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl <em>WSDL2 Java Data Model</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getWSDL2JavaDataModel()
	 * @generated
	 */
    int WSDL2_JAVA_DATA_MODEL = 5;

    /**
	 * The feature id for the '<em><b>Default Runtime Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__DEFAULT_RUNTIME_LOCATION = CXF_DATA_MODEL__DEFAULT_RUNTIME_LOCATION;

    /**
	 * The feature id for the '<em><b>Default Runtime Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__DEFAULT_RUNTIME_TYPE = CXF_DATA_MODEL__DEFAULT_RUNTIME_TYPE;

    /**
	 * The feature id for the '<em><b>Default Runtime Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__DEFAULT_RUNTIME_VERSION = CXF_DATA_MODEL__DEFAULT_RUNTIME_VERSION;

    /**
	 * The feature id for the '<em><b>Verbose</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__VERBOSE = CXF_DATA_MODEL__VERBOSE;

    /**
	 * The feature id for the '<em><b>Generate Ant Build File</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__GENERATE_ANT_BUILD_FILE = CXF_DATA_MODEL__GENERATE_ANT_BUILD_FILE;

    /**
	 * The feature id for the '<em><b>Generate Client</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__GENERATE_CLIENT = CXF_DATA_MODEL__GENERATE_CLIENT;

    /**
	 * The feature id for the '<em><b>Generate Server</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__GENERATE_SERVER = CXF_DATA_MODEL__GENERATE_SERVER;

    /**
	 * The feature id for the '<em><b>Databinding</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__DATABINDING = CXF_DATA_MODEL__DATABINDING;

    /**
	 * The feature id for the '<em><b>Frontend</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__FRONTEND = CXF_DATA_MODEL__FRONTEND;

    /**
	 * The feature id for the '<em><b>Use Spring Application Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__USE_SPRING_APPLICATION_CONTEXT = CXF_DATA_MODEL__USE_SPRING_APPLICATION_CONTEXT;

    /**
	 * The feature id for the '<em><b>Export CXF Classpath Container</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__EXPORT_CXF_CLASSPATH_CONTAINER = CXF_DATA_MODEL__EXPORT_CXF_CLASSPATH_CONTAINER;

    /**
	 * The feature id for the '<em><b>Installations</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__INSTALLATIONS = CXF_DATA_MODEL__INSTALLATIONS;

    /**
	 * The feature id for the '<em><b>Project Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__PROJECT_NAME = CXF_DATA_MODEL__PROJECT_NAME;

    /**
	 * The feature id for the '<em><b>Resource Directory</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__RESOURCE_DIRECTORY = CXF_DATA_MODEL__RESOURCE_DIRECTORY;

    /**
	 * The feature id for the '<em><b>Class Directory</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__CLASS_DIRECTORY = CXF_DATA_MODEL__CLASS_DIRECTORY;

    /**
	 * The feature id for the '<em><b>Wsdl File Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__WSDL_FILE_NAME = CXF_DATA_MODEL__WSDL_FILE_NAME;

    /**
	 * The feature id for the '<em><b>Wsdl URL</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__WSDL_URL = CXF_DATA_MODEL__WSDL_URL;

    /**
	 * The feature id for the '<em><b>Config Wsdl Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__CONFIG_WSDL_LOCATION = CXF_DATA_MODEL__CONFIG_WSDL_LOCATION;

    /**
	 * The feature id for the '<em><b>Fully Qualified Java Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME = CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME;

    /**
	 * The feature id for the '<em><b>Fully Qualified Java Interface Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME = CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME;

    /**
	 * The feature id for the '<em><b>Config Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__CONFIG_ID = CXF_DATA_MODEL__CONFIG_ID;

    /**
	 * The feature id for the '<em><b>Target Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__TARGET_NAMESPACE = CXF_DATA_MODEL__TARGET_NAMESPACE;

    /**
	 * The feature id for the '<em><b>Endpoint Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__ENDPOINT_NAME = CXF_DATA_MODEL__ENDPOINT_NAME;

    /**
	 * The feature id for the '<em><b>Service Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__SERVICE_NAME = CXF_DATA_MODEL__SERVICE_NAME;

    /**
	 * The feature id for the '<em><b>Wsdl Definition</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__WSDL_DEFINITION = CXF_DATA_MODEL__WSDL_DEFINITION;

    /**
	 * The feature id for the '<em><b>Wsdl Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__WSDL_LOCATION = CXF_DATA_MODEL__WSDL_LOCATION;

    /**
	 * The feature id for the '<em><b>Generate Implementation</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__GENERATE_IMPLEMENTATION = CXF_DATA_MODEL_FEATURE_COUNT + 0;

    /**
	 * The feature id for the '<em><b>Process SOAP Headers</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__PROCESS_SOAP_HEADERS = CXF_DATA_MODEL_FEATURE_COUNT + 1;

    /**
	 * The feature id for the '<em><b>Load Default Namespace Package Name Mapping</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING = CXF_DATA_MODEL_FEATURE_COUNT + 2;

    /**
	 * The feature id for the '<em><b>Load Default Excludes Namepsace Mapping</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING = CXF_DATA_MODEL_FEATURE_COUNT + 3;

    /**
	 * The feature id for the '<em><b>Validate</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__VALIDATE = CXF_DATA_MODEL_FEATURE_COUNT + 4;

    /**
	 * The feature id for the '<em><b>Wsdl Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__WSDL_VERSION = CXF_DATA_MODEL_FEATURE_COUNT + 5;

    /**
	 * The feature id for the '<em><b>Use Default Values</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__USE_DEFAULT_VALUES = CXF_DATA_MODEL_FEATURE_COUNT + 6;

    /**
	 * The feature id for the '<em><b>Xjc Args</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__XJC_ARGS = CXF_DATA_MODEL_FEATURE_COUNT + 7;

    /**
	 * The feature id for the '<em><b>No Address Binding</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__NO_ADDRESS_BINDING = CXF_DATA_MODEL_FEATURE_COUNT + 8;

    /**
	 * The feature id for the '<em><b>Xjc Use Default Values</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__XJC_USE_DEFAULT_VALUES = CXF_DATA_MODEL_FEATURE_COUNT + 9;

    /**
	 * The feature id for the '<em><b>Xjc To String</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING = CXF_DATA_MODEL_FEATURE_COUNT + 10;

    /**
	 * The feature id for the '<em><b>Xjc To String Multi Line</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_MULTI_LINE = CXF_DATA_MODEL_FEATURE_COUNT + 11;

    /**
	 * The feature id for the '<em><b>Xjc To String Simple</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_SIMPLE = CXF_DATA_MODEL_FEATURE_COUNT + 12;

    /**
	 * The feature id for the '<em><b>Xjc Locator</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__XJC_LOCATOR = CXF_DATA_MODEL_FEATURE_COUNT + 13;

    /**
	 * The feature id for the '<em><b>Xjc Sync Methods</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__XJC_SYNC_METHODS = CXF_DATA_MODEL_FEATURE_COUNT + 14;

    /**
	 * The feature id for the '<em><b>Xjc Mark Generated</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__XJC_MARK_GENERATED = CXF_DATA_MODEL_FEATURE_COUNT + 15;

    /**
	 * The feature id for the '<em><b>Xjc Episode File</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__XJC_EPISODE_FILE = CXF_DATA_MODEL_FEATURE_COUNT + 16;

    /**
	 * The feature id for the '<em><b>Auto Name Resolution</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__AUTO_NAME_RESOLUTION = CXF_DATA_MODEL_FEATURE_COUNT + 17;

    /**
	 * The feature id for the '<em><b>Included Namespaces</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__INCLUDED_NAMESPACES = CXF_DATA_MODEL_FEATURE_COUNT + 18;

    /**
	 * The feature id for the '<em><b>Binding Files</b></em>' attribute list.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__BINDING_FILES = CXF_DATA_MODEL_FEATURE_COUNT + 19;

    /**
	 * The feature id for the '<em><b>Excluded Namespaces</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__EXCLUDED_NAMESPACES = CXF_DATA_MODEL_FEATURE_COUNT + 20;

    /**
	 * The feature id for the '<em><b>Catalog File</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__CATALOG_FILE = CXF_DATA_MODEL_FEATURE_COUNT + 21;

    /**
	 * The feature id for the '<em><b>Java Source Folder</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL__JAVA_SOURCE_FOLDER = CXF_DATA_MODEL_FEATURE_COUNT + 22;

    /**
	 * The number of structural features of the '<em>WSDL2 Java Data Model</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int WSDL2_JAVA_DATA_MODEL_FEATURE_COUNT = CXF_DATA_MODEL_FEATURE_COUNT + 23;

    /**
	 * The meta object id for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFInstallImpl <em>Install</em>}' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFInstallImpl
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getCXFInstall()
	 * @generated
	 */
    int CXF_INSTALL = 6;

    /**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_INSTALL__VERSION = 0;

    /**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_INSTALL__LOCATION = 1;

    /**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_INSTALL__TYPE = 2;

    /**
	 * The number of structural features of the '<em>Install</em>' class.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
    int CXF_INSTALL_FEATURE_COUNT = 3;

    /**
	 * The meta object id for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Frontend <em>Frontend</em>}' enum.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Frontend
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getFrontend()
	 * @generated
	 */
    int FRONTEND = 7;

    /**
	 * The meta object id for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.DataBinding <em>Data Binding</em>}' enum.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.DataBinding
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getDataBinding()
	 * @generated
	 */
    int DATA_BINDING = 8;

    /**
	 * The meta object id for the '<em>URL</em>' data type.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see java.net.URL
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getURL()
	 * @generated
	 */
    int URL = 9;

    /**
	 * The meta object id for the '<em>Definition</em>' data type.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see javax.wsdl.Definition
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getDefinition()
	 * @generated
	 */
    int DEFINITION = 10;

    /**
	 * The meta object id for the '<em>Map</em>' data type.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see java.util.Map
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getMap()
	 * @generated
	 */
    int MAP = 11;

    /**
	 * The meta object id for the '<em>IMethod</em>' data type.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see org.eclipse.jdt.core.IMethod
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getIMethod()
	 * @generated
	 */
    int IMETHOD = 12;


    /**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext <em>Context</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Context</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext
	 * @generated
	 */
    EClass getCXFContext();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDefaultRuntimeLocation <em>Default Runtime Location</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Runtime Location</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDefaultRuntimeLocation()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_DefaultRuntimeLocation();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDefaultRuntimeType <em>Default Runtime Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Runtime Type</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDefaultRuntimeType()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_DefaultRuntimeType();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDefaultRuntimeVersion <em>Default Runtime Version</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Runtime Version</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDefaultRuntimeVersion()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_DefaultRuntimeVersion();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isVerbose <em>Verbose</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Verbose</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isVerbose()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_Verbose();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isGenerateAntBuildFile <em>Generate Ant Build File</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Ant Build File</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isGenerateAntBuildFile()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_GenerateAntBuildFile();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isGenerateClient <em>Generate Client</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Client</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isGenerateClient()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_GenerateClient();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isGenerateServer <em>Generate Server</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Server</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isGenerateServer()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_GenerateServer();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDatabinding <em>Databinding</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Databinding</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDatabinding()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_Databinding();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getFrontend <em>Frontend</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Frontend</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getFrontend()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_Frontend();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isUseSpringApplicationContext <em>Use Spring Application Context</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Spring Application Context</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isUseSpringApplicationContext()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_UseSpringApplicationContext();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isExportCXFClasspathContainer <em>Export CXF Classpath Container</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Export CXF Classpath Container</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isExportCXFClasspathContainer()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_ExportCXFClasspathContainer();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getInstallations <em>Installations</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Installations</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getInstallations()
	 * @see #getCXFContext()
	 * @generated
	 */
    EAttribute getCXFContext_Installations();

    /**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel <em>Data Model</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Model</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel
	 * @generated
	 */
    EClass getCXFDataModel();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getProjectName <em>Project Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Project Name</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getProjectName()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_ProjectName();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getResourceDirectory <em>Resource Directory</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Resource Directory</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getResourceDirectory()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_ResourceDirectory();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getClassDirectory <em>Class Directory</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Class Directory</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getClassDirectory()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_ClassDirectory();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlFileName <em>Wsdl File Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wsdl File Name</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlFileName()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_WsdlFileName();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlURL <em>Wsdl URL</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wsdl URL</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlURL()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_WsdlURL();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getConfigWsdlLocation <em>Config Wsdl Location</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Config Wsdl Location</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getConfigWsdlLocation()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_ConfigWsdlLocation();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getFullyQualifiedJavaClassName <em>Fully Qualified Java Class Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fully Qualified Java Class Name</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getFullyQualifiedJavaClassName()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_FullyQualifiedJavaClassName();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getFullyQualifiedJavaInterfaceName <em>Fully Qualified Java Interface Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fully Qualified Java Interface Name</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getFullyQualifiedJavaInterfaceName()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_FullyQualifiedJavaInterfaceName();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getConfigId <em>Config Id</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Config Id</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getConfigId()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_ConfigId();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getTargetNamespace <em>Target Namespace</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target Namespace</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getTargetNamespace()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_TargetNamespace();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getEndpointName <em>Endpoint Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Endpoint Name</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getEndpointName()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_EndpointName();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getServiceName <em>Service Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service Name</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getServiceName()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_ServiceName();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlDefinition <em>Wsdl Definition</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wsdl Definition</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlDefinition()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_WsdlDefinition();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlLocation <em>Wsdl Location</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wsdl Location</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlLocation()
	 * @see #getCXFDataModel()
	 * @generated
	 */
    EAttribute getCXFDataModel_WsdlLocation();

    /**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext <em>Java2 WS Context</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Java2 WS Context</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext
	 * @generated
	 */
    EClass getJava2WSContext();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isSoap12Binding <em>Soap12 Binding</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Soap12 Binding</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isSoap12Binding()
	 * @see #getJava2WSContext()
	 * @generated
	 */
    EAttribute getJava2WSContext_Soap12Binding();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateXSDImports <em>Generate XSD Imports</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate XSD Imports</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateXSDImports()
	 * @see #getJava2WSContext()
	 * @generated
	 */
    EAttribute getJava2WSContext_GenerateXSDImports();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWSDL <em>Generate WSDL</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate WSDL</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWSDL()
	 * @see #getJava2WSContext()
	 * @generated
	 */
    EAttribute getJava2WSContext_GenerateWSDL();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWrapperFaultBeans <em>Generate Wrapper Fault Beans</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Wrapper Fault Beans</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWrapperFaultBeans()
	 * @see #getJava2WSContext()
	 * @generated
	 */
    EAttribute getJava2WSContext_GenerateWrapperFaultBeans();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isAnnotationProcessingEnabled <em>Annotation Processing Enabled</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Annotation Processing Enabled</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isAnnotationProcessingEnabled()
	 * @see #getJava2WSContext()
	 * @generated
	 */
    EAttribute getJava2WSContext_AnnotationProcessingEnabled();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWebMethodAnnotation <em>Generate Web Method Annotation</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Web Method Annotation</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWebMethodAnnotation()
	 * @see #getJava2WSContext()
	 * @generated
	 */
    EAttribute getJava2WSContext_GenerateWebMethodAnnotation();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWebParamAnnotation <em>Generate Web Param Annotation</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Web Param Annotation</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWebParamAnnotation()
	 * @see #getJava2WSContext()
	 * @generated
	 */
    EAttribute getJava2WSContext_GenerateWebParamAnnotation();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateRequestWrapperAnnotation <em>Generate Request Wrapper Annotation</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Request Wrapper Annotation</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateRequestWrapperAnnotation()
	 * @see #getJava2WSContext()
	 * @generated
	 */
    EAttribute getJava2WSContext_GenerateRequestWrapperAnnotation();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateResponseWrapperAnnotation <em>Generate Response Wrapper Annotation</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Response Wrapper Annotation</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateResponseWrapperAnnotation()
	 * @see #getJava2WSContext()
	 * @generated
	 */
    EAttribute getJava2WSContext_GenerateResponseWrapperAnnotation();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWebResultAnnotation <em>Generate Web Result Annotation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Web Result Annotation</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWebResultAnnotation()
	 * @see #getJava2WSContext()
	 * @generated
	 */
	EAttribute getJava2WSContext_GenerateWebResultAnnotation();

				/**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel <em>Java2 WS Data Model</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Java2 WS Data Model</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel
	 * @generated
	 */
    EClass getJava2WSDataModel();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getClasspath <em>Classpath</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Classpath</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getClasspath()
	 * @see #getJava2WSDataModel()
	 * @generated
	 */
    EAttribute getJava2WSDataModel_Classpath();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getJavaStartingPoint <em>Java Starting Point</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Java Starting Point</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getJavaStartingPoint()
	 * @see #getJava2WSDataModel()
	 * @generated
	 */
    EAttribute getJava2WSDataModel_JavaStartingPoint();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#isUseServiceEndpointInterface <em>Use Service Endpoint Interface</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Service Endpoint Interface</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#isUseServiceEndpointInterface()
	 * @see #getJava2WSDataModel()
	 * @generated
	 */
    EAttribute getJava2WSDataModel_UseServiceEndpointInterface();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#isExtractInterface <em>Extract Interface</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extract Interface</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#isExtractInterface()
	 * @see #getJava2WSDataModel()
	 * @generated
	 */
    EAttribute getJava2WSDataModel_ExtractInterface();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getServiceEndpointInterfaceName <em>Service Endpoint Interface Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service Endpoint Interface Name</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getServiceEndpointInterfaceName()
	 * @see #getJava2WSDataModel()
	 * @generated
	 */
    EAttribute getJava2WSDataModel_ServiceEndpointInterfaceName();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getMethodMap <em>Method Map</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Method Map</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getMethodMap()
	 * @see #getJava2WSDataModel()
	 * @generated
	 */
    EAttribute getJava2WSDataModel_MethodMap();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getAnnotationMap <em>Annotation Map</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Annotation Map</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getAnnotationMap()
	 * @see #getJava2WSDataModel()
	 * @generated
	 */
    EAttribute getJava2WSDataModel_AnnotationMap();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getSourceDirectory <em>Source Directory</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Directory</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getSourceDirectory()
	 * @see #getJava2WSDataModel()
	 * @generated
	 */
    EAttribute getJava2WSDataModel_SourceDirectory();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getPortName <em>Port Name</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Port Name</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getPortName()
	 * @see #getJava2WSDataModel()
	 * @generated
	 */
    EAttribute getJava2WSDataModel_PortName();

    /**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext <em>WSDL2 Java Context</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>WSDL2 Java Context</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext
	 * @generated
	 */
    EClass getWSDL2JavaContext();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isGenerateImplementation <em>Generate Implementation</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Generate Implementation</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isGenerateImplementation()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_GenerateImplementation();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isProcessSOAPHeaders <em>Process SOAP Headers</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Process SOAP Headers</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isProcessSOAPHeaders()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_ProcessSOAPHeaders();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isLoadDefaultNamespacePackageNameMapping <em>Load Default Namespace Package Name Mapping</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Default Namespace Package Name Mapping</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isLoadDefaultNamespacePackageNameMapping()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_LoadDefaultNamespacePackageNameMapping();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isLoadDefaultExcludesNamepsaceMapping <em>Load Default Excludes Namepsace Mapping</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Load Default Excludes Namepsace Mapping</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isLoadDefaultExcludesNamepsaceMapping()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_LoadDefaultExcludesNamepsaceMapping();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isValidate <em>Validate</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Validate</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isValidate()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_Validate();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#getWsdlVersion <em>Wsdl Version</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wsdl Version</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#getWsdlVersion()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_WsdlVersion();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isUseDefaultValues <em>Use Default Values</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Default Values</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isUseDefaultValues()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_UseDefaultValues();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#getXjcArgs <em>Xjc Args</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Xjc Args</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#getXjcArgs()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_XjcArgs();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isNoAddressBinding <em>No Address Binding</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>No Address Binding</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isNoAddressBinding()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_NoAddressBinding();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcUseDefaultValues <em>Xjc Use Default Values</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Xjc Use Default Values</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcUseDefaultValues()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_XjcUseDefaultValues();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcToString <em>Xjc To String</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Xjc To String</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcToString()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_XjcToString();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcToStringMultiLine <em>Xjc To String Multi Line</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Xjc To String Multi Line</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcToStringMultiLine()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_XjcToStringMultiLine();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcToStringSimple <em>Xjc To String Simple</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Xjc To String Simple</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcToStringSimple()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_XjcToStringSimple();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcLocator <em>Xjc Locator</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Xjc Locator</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcLocator()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_XjcLocator();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcSyncMethods <em>Xjc Sync Methods</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Xjc Sync Methods</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcSyncMethods()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_XjcSyncMethods();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcMarkGenerated <em>Xjc Mark Generated</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Xjc Mark Generated</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcMarkGenerated()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_XjcMarkGenerated();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#getXjcEpisodeFile <em>Xjc Episode File</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Xjc Episode File</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#getXjcEpisodeFile()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_XjcEpisodeFile();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isAutoNameResolution <em>Auto Name Resolution</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Auto Name Resolution</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isAutoNameResolution()
	 * @see #getWSDL2JavaContext()
	 * @generated
	 */
    EAttribute getWSDL2JavaContext_AutoNameResolution();

    /**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel <em>WSDL2 Java Data Model</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>WSDL2 Java Data Model</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel
	 * @generated
	 */
    EClass getWSDL2JavaDataModel();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getIncludedNamespaces <em>Included Namespaces</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Included Namespaces</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getIncludedNamespaces()
	 * @see #getWSDL2JavaDataModel()
	 * @generated
	 */
    EAttribute getWSDL2JavaDataModel_IncludedNamespaces();

    /**
	 * Returns the meta object for the attribute list '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getBindingFiles <em>Binding Files</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Binding Files</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getBindingFiles()
	 * @see #getWSDL2JavaDataModel()
	 * @generated
	 */
    EAttribute getWSDL2JavaDataModel_BindingFiles();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getExcludedNamespaces <em>Excluded Namespaces</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Excluded Namespaces</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getExcludedNamespaces()
	 * @see #getWSDL2JavaDataModel()
	 * @generated
	 */
    EAttribute getWSDL2JavaDataModel_ExcludedNamespaces();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getCatalogFile <em>Catalog File</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Catalog File</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getCatalogFile()
	 * @see #getWSDL2JavaDataModel()
	 * @generated
	 */
    EAttribute getWSDL2JavaDataModel_CatalogFile();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getJavaSourceFolder <em>Java Source Folder</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Java Source Folder</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel#getJavaSourceFolder()
	 * @see #getWSDL2JavaDataModel()
	 * @generated
	 */
    EAttribute getWSDL2JavaDataModel_JavaSourceFolder();

    /**
	 * Returns the meta object for class '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall <em>Install</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Install</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall
	 * @generated
	 */
    EClass getCXFInstall();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getVersion()
	 * @see #getCXFInstall()
	 * @generated
	 */
    EAttribute getCXFInstall_Version();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getLocation()
	 * @see #getCXFInstall()
	 * @generated
	 */
    EAttribute getCXFInstall_Location();

    /**
	 * Returns the meta object for the attribute '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getType()
	 * @see #getCXFInstall()
	 * @generated
	 */
    EAttribute getCXFInstall_Type();

    /**
	 * Returns the meta object for enum '{@link org.eclipse.jst.ws.internal.cxf.core.model.Frontend <em>Frontend</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Frontend</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.Frontend
	 * @generated
	 */
    EEnum getFrontend();

    /**
	 * Returns the meta object for enum '{@link org.eclipse.jst.ws.internal.cxf.core.model.DataBinding <em>Data Binding</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Data Binding</em>'.
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.DataBinding
	 * @generated
	 */
    EEnum getDataBinding();

    /**
	 * Returns the meta object for data type '{@link java.net.URL <em>URL</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>URL</em>'.
	 * @see java.net.URL
	 * @model instanceClass="java.net.URL"
	 * @generated
	 */
    EDataType getURL();

    /**
	 * Returns the meta object for data type '{@link javax.wsdl.Definition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Definition</em>'.
	 * @see javax.wsdl.Definition
	 * @model instanceClass="javax.wsdl.Definition"
	 * @generated
	 */
    EDataType getDefinition();

    /**
	 * Returns the meta object for data type '{@link java.util.Map <em>Map</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Map</em>'.
	 * @see java.util.Map
	 * @model instanceClass="java.util.Map" typeParameters="T T1"
	 * @generated
	 */
    EDataType getMap();

    /**
	 * Returns the meta object for data type '{@link org.eclipse.jdt.core.IMethod <em>IMethod</em>}'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>IMethod</em>'.
	 * @see org.eclipse.jdt.core.IMethod
	 * @model instanceClass="org.eclipse.jdt.core.IMethod"
	 * @generated
	 */
    EDataType getIMethod();

    /**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
    CXFFactory getCXFFactory();

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
		 * The meta object literal for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext <em>Context</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getCXFContext()
		 * @generated
		 */
        EClass CXF_CONTEXT = eINSTANCE.getCXFContext();

        /**
		 * The meta object literal for the '<em><b>Default Runtime Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__DEFAULT_RUNTIME_LOCATION = eINSTANCE.getCXFContext_DefaultRuntimeLocation();

        /**
		 * The meta object literal for the '<em><b>Default Runtime Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__DEFAULT_RUNTIME_TYPE = eINSTANCE.getCXFContext_DefaultRuntimeType();

        /**
		 * The meta object literal for the '<em><b>Default Runtime Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__DEFAULT_RUNTIME_VERSION = eINSTANCE.getCXFContext_DefaultRuntimeVersion();

        /**
		 * The meta object literal for the '<em><b>Verbose</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__VERBOSE = eINSTANCE.getCXFContext_Verbose();

        /**
		 * The meta object literal for the '<em><b>Generate Ant Build File</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__GENERATE_ANT_BUILD_FILE = eINSTANCE.getCXFContext_GenerateAntBuildFile();

        /**
		 * The meta object literal for the '<em><b>Generate Client</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__GENERATE_CLIENT = eINSTANCE.getCXFContext_GenerateClient();

        /**
		 * The meta object literal for the '<em><b>Generate Server</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__GENERATE_SERVER = eINSTANCE.getCXFContext_GenerateServer();

        /**
		 * The meta object literal for the '<em><b>Databinding</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__DATABINDING = eINSTANCE.getCXFContext_Databinding();

        /**
		 * The meta object literal for the '<em><b>Frontend</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__FRONTEND = eINSTANCE.getCXFContext_Frontend();

        /**
		 * The meta object literal for the '<em><b>Use Spring Application Context</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__USE_SPRING_APPLICATION_CONTEXT = eINSTANCE.getCXFContext_UseSpringApplicationContext();

        /**
		 * The meta object literal for the '<em><b>Export CXF Classpath Container</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__EXPORT_CXF_CLASSPATH_CONTAINER = eINSTANCE.getCXFContext_ExportCXFClasspathContainer();

        /**
		 * The meta object literal for the '<em><b>Installations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_CONTEXT__INSTALLATIONS = eINSTANCE.getCXFContext_Installations();

        /**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl <em>Data Model</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getCXFDataModel()
		 * @generated
		 */
        EClass CXF_DATA_MODEL = eINSTANCE.getCXFDataModel();

        /**
		 * The meta object literal for the '<em><b>Project Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__PROJECT_NAME = eINSTANCE.getCXFDataModel_ProjectName();

        /**
		 * The meta object literal for the '<em><b>Resource Directory</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__RESOURCE_DIRECTORY = eINSTANCE.getCXFDataModel_ResourceDirectory();

        /**
		 * The meta object literal for the '<em><b>Class Directory</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__CLASS_DIRECTORY = eINSTANCE.getCXFDataModel_ClassDirectory();

        /**
		 * The meta object literal for the '<em><b>Wsdl File Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__WSDL_FILE_NAME = eINSTANCE.getCXFDataModel_WsdlFileName();

        /**
		 * The meta object literal for the '<em><b>Wsdl URL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__WSDL_URL = eINSTANCE.getCXFDataModel_WsdlURL();

        /**
		 * The meta object literal for the '<em><b>Config Wsdl Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__CONFIG_WSDL_LOCATION = eINSTANCE.getCXFDataModel_ConfigWsdlLocation();

        /**
		 * The meta object literal for the '<em><b>Fully Qualified Java Class Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME = eINSTANCE.getCXFDataModel_FullyQualifiedJavaClassName();

        /**
		 * The meta object literal for the '<em><b>Fully Qualified Java Interface Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME = eINSTANCE.getCXFDataModel_FullyQualifiedJavaInterfaceName();

        /**
		 * The meta object literal for the '<em><b>Config Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__CONFIG_ID = eINSTANCE.getCXFDataModel_ConfigId();

        /**
		 * The meta object literal for the '<em><b>Target Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__TARGET_NAMESPACE = eINSTANCE.getCXFDataModel_TargetNamespace();

        /**
		 * The meta object literal for the '<em><b>Endpoint Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__ENDPOINT_NAME = eINSTANCE.getCXFDataModel_EndpointName();

        /**
		 * The meta object literal for the '<em><b>Service Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__SERVICE_NAME = eINSTANCE.getCXFDataModel_ServiceName();

        /**
		 * The meta object literal for the '<em><b>Wsdl Definition</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__WSDL_DEFINITION = eINSTANCE.getCXFDataModel_WsdlDefinition();

        /**
		 * The meta object literal for the '<em><b>Wsdl Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_DATA_MODEL__WSDL_LOCATION = eINSTANCE.getCXFDataModel_WsdlLocation();

        /**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext <em>Java2 WS Context</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getJava2WSContext()
		 * @generated
		 */
        EClass JAVA2_WS_CONTEXT = eINSTANCE.getJava2WSContext();

        /**
		 * The meta object literal for the '<em><b>Soap12 Binding</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_CONTEXT__SOAP12_BINDING = eINSTANCE.getJava2WSContext_Soap12Binding();

        /**
		 * The meta object literal for the '<em><b>Generate XSD Imports</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_CONTEXT__GENERATE_XSD_IMPORTS = eINSTANCE.getJava2WSContext_GenerateXSDImports();

        /**
		 * The meta object literal for the '<em><b>Generate WSDL</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_CONTEXT__GENERATE_WSDL = eINSTANCE.getJava2WSContext_GenerateWSDL();

        /**
		 * The meta object literal for the '<em><b>Generate Wrapper Fault Beans</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_CONTEXT__GENERATE_WRAPPER_FAULT_BEANS = eINSTANCE.getJava2WSContext_GenerateWrapperFaultBeans();

        /**
		 * The meta object literal for the '<em><b>Annotation Processing Enabled</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_CONTEXT__ANNOTATION_PROCESSING_ENABLED = eINSTANCE.getJava2WSContext_AnnotationProcessingEnabled();

        /**
		 * The meta object literal for the '<em><b>Generate Web Method Annotation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_CONTEXT__GENERATE_WEB_METHOD_ANNOTATION = eINSTANCE.getJava2WSContext_GenerateWebMethodAnnotation();

        /**
		 * The meta object literal for the '<em><b>Generate Web Param Annotation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_CONTEXT__GENERATE_WEB_PARAM_ANNOTATION = eINSTANCE.getJava2WSContext_GenerateWebParamAnnotation();

        /**
		 * The meta object literal for the '<em><b>Generate Request Wrapper Annotation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_CONTEXT__GENERATE_REQUEST_WRAPPER_ANNOTATION = eINSTANCE.getJava2WSContext_GenerateRequestWrapperAnnotation();

        /**
		 * The meta object literal for the '<em><b>Generate Response Wrapper Annotation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_CONTEXT__GENERATE_RESPONSE_WRAPPER_ANNOTATION = eINSTANCE.getJava2WSContext_GenerateResponseWrapperAnnotation();

        /**
		 * The meta object literal for the '<em><b>Generate Web Result Annotation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JAVA2_WS_CONTEXT__GENERATE_WEB_RESULT_ANNOTATION = eINSTANCE.getJava2WSContext_GenerateWebResultAnnotation();

								/**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl <em>Java2 WS Data Model</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getJava2WSDataModel()
		 * @generated
		 */
        EClass JAVA2_WS_DATA_MODEL = eINSTANCE.getJava2WSDataModel();

        /**
		 * The meta object literal for the '<em><b>Classpath</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_DATA_MODEL__CLASSPATH = eINSTANCE.getJava2WSDataModel_Classpath();

        /**
		 * The meta object literal for the '<em><b>Java Starting Point</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_DATA_MODEL__JAVA_STARTING_POINT = eINSTANCE.getJava2WSDataModel_JavaStartingPoint();

        /**
		 * The meta object literal for the '<em><b>Use Service Endpoint Interface</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_DATA_MODEL__USE_SERVICE_ENDPOINT_INTERFACE = eINSTANCE.getJava2WSDataModel_UseServiceEndpointInterface();

        /**
		 * The meta object literal for the '<em><b>Extract Interface</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_DATA_MODEL__EXTRACT_INTERFACE = eINSTANCE.getJava2WSDataModel_ExtractInterface();

        /**
		 * The meta object literal for the '<em><b>Service Endpoint Interface Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_DATA_MODEL__SERVICE_ENDPOINT_INTERFACE_NAME = eINSTANCE.getJava2WSDataModel_ServiceEndpointInterfaceName();

        /**
		 * The meta object literal for the '<em><b>Method Map</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_DATA_MODEL__METHOD_MAP = eINSTANCE.getJava2WSDataModel_MethodMap();

        /**
		 * The meta object literal for the '<em><b>Annotation Map</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_DATA_MODEL__ANNOTATION_MAP = eINSTANCE.getJava2WSDataModel_AnnotationMap();

        /**
		 * The meta object literal for the '<em><b>Source Directory</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_DATA_MODEL__SOURCE_DIRECTORY = eINSTANCE.getJava2WSDataModel_SourceDirectory();

        /**
		 * The meta object literal for the '<em><b>Port Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute JAVA2_WS_DATA_MODEL__PORT_NAME = eINSTANCE.getJava2WSDataModel_PortName();

        /**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext <em>WSDL2 Java Context</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getWSDL2JavaContext()
		 * @generated
		 */
        EClass WSDL2_JAVA_CONTEXT = eINSTANCE.getWSDL2JavaContext();

        /**
		 * The meta object literal for the '<em><b>Generate Implementation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__GENERATE_IMPLEMENTATION = eINSTANCE.getWSDL2JavaContext_GenerateImplementation();

        /**
		 * The meta object literal for the '<em><b>Process SOAP Headers</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__PROCESS_SOAP_HEADERS = eINSTANCE.getWSDL2JavaContext_ProcessSOAPHeaders();

        /**
		 * The meta object literal for the '<em><b>Load Default Namespace Package Name Mapping</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING = eINSTANCE.getWSDL2JavaContext_LoadDefaultNamespacePackageNameMapping();

        /**
		 * The meta object literal for the '<em><b>Load Default Excludes Namepsace Mapping</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING = eINSTANCE.getWSDL2JavaContext_LoadDefaultExcludesNamepsaceMapping();

        /**
		 * The meta object literal for the '<em><b>Validate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__VALIDATE = eINSTANCE.getWSDL2JavaContext_Validate();

        /**
		 * The meta object literal for the '<em><b>Wsdl Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__WSDL_VERSION = eINSTANCE.getWSDL2JavaContext_WsdlVersion();

        /**
		 * The meta object literal for the '<em><b>Use Default Values</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__USE_DEFAULT_VALUES = eINSTANCE.getWSDL2JavaContext_UseDefaultValues();

        /**
		 * The meta object literal for the '<em><b>Xjc Args</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__XJC_ARGS = eINSTANCE.getWSDL2JavaContext_XjcArgs();

        /**
		 * The meta object literal for the '<em><b>No Address Binding</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__NO_ADDRESS_BINDING = eINSTANCE.getWSDL2JavaContext_NoAddressBinding();

        /**
		 * The meta object literal for the '<em><b>Xjc Use Default Values</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__XJC_USE_DEFAULT_VALUES = eINSTANCE.getWSDL2JavaContext_XjcUseDefaultValues();

        /**
		 * The meta object literal for the '<em><b>Xjc To String</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__XJC_TO_STRING = eINSTANCE.getWSDL2JavaContext_XjcToString();

        /**
		 * The meta object literal for the '<em><b>Xjc To String Multi Line</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__XJC_TO_STRING_MULTI_LINE = eINSTANCE.getWSDL2JavaContext_XjcToStringMultiLine();

        /**
		 * The meta object literal for the '<em><b>Xjc To String Simple</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__XJC_TO_STRING_SIMPLE = eINSTANCE.getWSDL2JavaContext_XjcToStringSimple();

        /**
		 * The meta object literal for the '<em><b>Xjc Locator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__XJC_LOCATOR = eINSTANCE.getWSDL2JavaContext_XjcLocator();

        /**
		 * The meta object literal for the '<em><b>Xjc Sync Methods</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__XJC_SYNC_METHODS = eINSTANCE.getWSDL2JavaContext_XjcSyncMethods();

        /**
		 * The meta object literal for the '<em><b>Xjc Mark Generated</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__XJC_MARK_GENERATED = eINSTANCE.getWSDL2JavaContext_XjcMarkGenerated();

        /**
		 * The meta object literal for the '<em><b>Xjc Episode File</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__XJC_EPISODE_FILE = eINSTANCE.getWSDL2JavaContext_XjcEpisodeFile();

        /**
		 * The meta object literal for the '<em><b>Auto Name Resolution</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_CONTEXT__AUTO_NAME_RESOLUTION = eINSTANCE.getWSDL2JavaContext_AutoNameResolution();

        /**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl <em>WSDL2 Java Data Model</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getWSDL2JavaDataModel()
		 * @generated
		 */
        EClass WSDL2_JAVA_DATA_MODEL = eINSTANCE.getWSDL2JavaDataModel();

        /**
		 * The meta object literal for the '<em><b>Included Namespaces</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_DATA_MODEL__INCLUDED_NAMESPACES = eINSTANCE.getWSDL2JavaDataModel_IncludedNamespaces();

        /**
		 * The meta object literal for the '<em><b>Binding Files</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_DATA_MODEL__BINDING_FILES = eINSTANCE.getWSDL2JavaDataModel_BindingFiles();

        /**
		 * The meta object literal for the '<em><b>Excluded Namespaces</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_DATA_MODEL__EXCLUDED_NAMESPACES = eINSTANCE.getWSDL2JavaDataModel_ExcludedNamespaces();

        /**
		 * The meta object literal for the '<em><b>Catalog File</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_DATA_MODEL__CATALOG_FILE = eINSTANCE.getWSDL2JavaDataModel_CatalogFile();

        /**
		 * The meta object literal for the '<em><b>Java Source Folder</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute WSDL2_JAVA_DATA_MODEL__JAVA_SOURCE_FOLDER = eINSTANCE.getWSDL2JavaDataModel_JavaSourceFolder();

        /**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFInstallImpl <em>Install</em>}' class.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFInstallImpl
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getCXFInstall()
		 * @generated
		 */
        EClass CXF_INSTALL = eINSTANCE.getCXFInstall();

        /**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_INSTALL__VERSION = eINSTANCE.getCXFInstall_Version();

        /**
		 * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_INSTALL__LOCATION = eINSTANCE.getCXFInstall_Location();

        /**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @generated
		 */
        EAttribute CXF_INSTALL__TYPE = eINSTANCE.getCXFInstall_Type();

        /**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Frontend <em>Frontend</em>}' enum.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.Frontend
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getFrontend()
		 * @generated
		 */
        EEnum FRONTEND = eINSTANCE.getFrontend();

        /**
		 * The meta object literal for the '{@link org.eclipse.jst.ws.internal.cxf.core.model.DataBinding <em>Data Binding</em>}' enum.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.DataBinding
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getDataBinding()
		 * @generated
		 */
        EEnum DATA_BINDING = eINSTANCE.getDataBinding();

        /**
		 * The meta object literal for the '<em>URL</em>' data type.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see java.net.URL
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getURL()
		 * @generated
		 */
        EDataType URL = eINSTANCE.getURL();

        /**
		 * The meta object literal for the '<em>Definition</em>' data type.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see javax.wsdl.Definition
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getDefinition()
		 * @generated
		 */
        EDataType DEFINITION = eINSTANCE.getDefinition();

        /**
		 * The meta object literal for the '<em>Map</em>' data type.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see java.util.Map
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getMap()
		 * @generated
		 */
        EDataType MAP = eINSTANCE.getMap();

        /**
		 * The meta object literal for the '<em>IMethod</em>' data type.
		 * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
		 * @see org.eclipse.jdt.core.IMethod
		 * @see org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFPackageImpl#getIMethod()
		 * @generated
		 */
        EDataType IMETHOD = eINSTANCE.getIMethod();

    }

} //CXFPackage
