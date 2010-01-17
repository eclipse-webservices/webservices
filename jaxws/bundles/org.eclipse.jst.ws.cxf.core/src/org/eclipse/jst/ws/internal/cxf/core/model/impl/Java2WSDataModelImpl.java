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
 * $Id: Java2WSDataModelImpl.java,v 1.3 2010/01/17 19:56:56 sclarke Exp $
 */
package org.eclipse.jst.ws.internal.cxf.core.model.impl;

import java.util.Map;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Java2 WS Data Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#isSoap12Binding <em>Soap12 Binding</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#isGenerateXSDImports <em>Generate XSD Imports</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#isGenerateWSDL <em>Generate WSDL</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#isGenerateWrapperFaultBeans <em>Generate Wrapper Fault Beans</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#isAnnotationProcessingEnabled <em>Annotation Processing Enabled</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#isGenerateWebMethodAnnotation <em>Generate Web Method Annotation</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#isGenerateWebParamAnnotation <em>Generate Web Param Annotation</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#isGenerateRequestWrapperAnnotation <em>Generate Request Wrapper Annotation</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#isGenerateResponseWrapperAnnotation <em>Generate Response Wrapper Annotation</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#getClasspath <em>Classpath</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#getJavaStartingPoint <em>Java Starting Point</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#isUseServiceEndpointInterface <em>Use Service Endpoint Interface</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#isExtractInterface <em>Extract Interface</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#getServiceEndpointInterfaceName <em>Service Endpoint Interface Name</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#getMethodMap <em>Method Map</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#getAnnotationMap <em>Annotation Map</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#getSourceDirectory <em>Source Directory</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.Java2WSDataModelImpl#getPortName <em>Port Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class Java2WSDataModelImpl extends CXFDataModelImpl implements Java2WSDataModel {
    /**
     * The default value of the '{@link #isSoap12Binding() <em>Soap12 Binding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSoap12Binding()
     * @generated
     * @ordered
     */
    protected static final boolean SOAP12_BINDING_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSoap12Binding() <em>Soap12 Binding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSoap12Binding()
     * @generated
     * @ordered
     */
    protected boolean soap12Binding = SOAP12_BINDING_EDEFAULT;

    /**
     * The default value of the '{@link #isGenerateXSDImports() <em>Generate XSD Imports</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateXSDImports()
     * @generated
     * @ordered
     */
    protected static final boolean GENERATE_XSD_IMPORTS_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isGenerateXSDImports() <em>Generate XSD Imports</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateXSDImports()
     * @generated
     * @ordered
     */
    protected boolean generateXSDImports = GENERATE_XSD_IMPORTS_EDEFAULT;

    /**
     * The default value of the '{@link #isGenerateWSDL() <em>Generate WSDL</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateWSDL()
     * @generated
     * @ordered
     */
    protected static final boolean GENERATE_WSDL_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isGenerateWSDL() <em>Generate WSDL</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateWSDL()
     * @generated
     * @ordered
     */
    protected boolean generateWSDL = GENERATE_WSDL_EDEFAULT;

    /**
     * The default value of the '{@link #isGenerateWrapperFaultBeans() <em>Generate Wrapper Fault Beans</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateWrapperFaultBeans()
     * @generated
     * @ordered
     */
    protected static final boolean GENERATE_WRAPPER_FAULT_BEANS_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isGenerateWrapperFaultBeans() <em>Generate Wrapper Fault Beans</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateWrapperFaultBeans()
     * @generated
     * @ordered
     */
    protected boolean generateWrapperFaultBeans = GENERATE_WRAPPER_FAULT_BEANS_EDEFAULT;

    /**
     * The default value of the '{@link #isAnnotationProcessingEnabled() <em>Annotation Processing Enabled</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAnnotationProcessingEnabled()
     * @generated
     * @ordered
     */
    protected static final boolean ANNOTATION_PROCESSING_ENABLED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isAnnotationProcessingEnabled() <em>Annotation Processing Enabled</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAnnotationProcessingEnabled()
     * @generated
     * @ordered
     */
    protected boolean annotationProcessingEnabled = ANNOTATION_PROCESSING_ENABLED_EDEFAULT;

    /**
     * The default value of the '{@link #isGenerateWebMethodAnnotation() <em>Generate Web Method Annotation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateWebMethodAnnotation()
     * @generated
     * @ordered
     */
    protected static final boolean GENERATE_WEB_METHOD_ANNOTATION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isGenerateWebMethodAnnotation() <em>Generate Web Method Annotation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateWebMethodAnnotation()
     * @generated
     * @ordered
     */
    protected boolean generateWebMethodAnnotation = GENERATE_WEB_METHOD_ANNOTATION_EDEFAULT;

    /**
     * The default value of the '{@link #isGenerateWebParamAnnotation() <em>Generate Web Param Annotation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateWebParamAnnotation()
     * @generated
     * @ordered
     */
    protected static final boolean GENERATE_WEB_PARAM_ANNOTATION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isGenerateWebParamAnnotation() <em>Generate Web Param Annotation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateWebParamAnnotation()
     * @generated
     * @ordered
     */
    protected boolean generateWebParamAnnotation = GENERATE_WEB_PARAM_ANNOTATION_EDEFAULT;

    /**
     * The default value of the '{@link #isGenerateRequestWrapperAnnotation() <em>Generate Request Wrapper Annotation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateRequestWrapperAnnotation()
     * @generated
     * @ordered
     */
    protected static final boolean GENERATE_REQUEST_WRAPPER_ANNOTATION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isGenerateRequestWrapperAnnotation() <em>Generate Request Wrapper Annotation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateRequestWrapperAnnotation()
     * @generated
     * @ordered
     */
    protected boolean generateRequestWrapperAnnotation = GENERATE_REQUEST_WRAPPER_ANNOTATION_EDEFAULT;

    /**
     * The default value of the '{@link #isGenerateResponseWrapperAnnotation() <em>Generate Response Wrapper Annotation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateResponseWrapperAnnotation()
     * @generated
     * @ordered
     */
    protected static final boolean GENERATE_RESPONSE_WRAPPER_ANNOTATION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isGenerateResponseWrapperAnnotation() <em>Generate Response Wrapper Annotation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateResponseWrapperAnnotation()
     * @generated
     * @ordered
     */
    protected boolean generateResponseWrapperAnnotation = GENERATE_RESPONSE_WRAPPER_ANNOTATION_EDEFAULT;

    /**
     * The default value of the '{@link #getClasspath() <em>Classpath</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getClasspath()
     * @generated
     * @ordered
     */
    protected static final String CLASSPATH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getClasspath() <em>Classpath</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getClasspath()
     * @generated
     * @ordered
     */
    protected String classpath = CLASSPATH_EDEFAULT;

    /**
     * The default value of the '{@link #getJavaStartingPoint() <em>Java Starting Point</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJavaStartingPoint()
     * @generated
     * @ordered
     */
    protected static final String JAVA_STARTING_POINT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getJavaStartingPoint() <em>Java Starting Point</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJavaStartingPoint()
     * @generated
     * @ordered
     */
    protected String javaStartingPoint = JAVA_STARTING_POINT_EDEFAULT;

    /**
     * The default value of the '{@link #isUseServiceEndpointInterface() <em>Use Service Endpoint Interface</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseServiceEndpointInterface()
     * @generated
     * @ordered
     */
    protected static final boolean USE_SERVICE_ENDPOINT_INTERFACE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseServiceEndpointInterface() <em>Use Service Endpoint Interface</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseServiceEndpointInterface()
     * @generated
     * @ordered
     */
    protected boolean useServiceEndpointInterface = USE_SERVICE_ENDPOINT_INTERFACE_EDEFAULT;

    /**
     * The default value of the '{@link #isExtractInterface() <em>Extract Interface</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isExtractInterface()
     * @generated
     * @ordered
     */
    protected static final boolean EXTRACT_INTERFACE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isExtractInterface() <em>Extract Interface</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isExtractInterface()
     * @generated
     * @ordered
     */
    protected boolean extractInterface = EXTRACT_INTERFACE_EDEFAULT;

    /**
     * The default value of the '{@link #getServiceEndpointInterfaceName() <em>Service Endpoint Interface Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceEndpointInterfaceName()
     * @generated
     * @ordered
     */
    protected static final String SERVICE_ENDPOINT_INTERFACE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getServiceEndpointInterfaceName() <em>Service Endpoint Interface Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceEndpointInterfaceName()
     * @generated
     * @ordered
     */
    protected String serviceEndpointInterfaceName = SERVICE_ENDPOINT_INTERFACE_NAME_EDEFAULT;

    /**
     * The cached value of the '{@link #getMethodMap() <em>Method Map</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getMethodMap()
     * @generated
     * @ordered
     */
    protected Map<IMethod, Map<String, Boolean>> methodMap;

    /**
     * The cached value of the '{@link #getAnnotationMap() <em>Annotation Map</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getAnnotationMap()
     * @generated
     * @ordered
     */
    protected Map<String, Boolean> annotationMap;

    /**
     * The default value of the '{@link #getSourceDirectory() <em>Source Directory</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceDirectory()
     * @generated
     * @ordered
     */
    protected static final String SOURCE_DIRECTORY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSourceDirectory() <em>Source Directory</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSourceDirectory()
     * @generated
     * @ordered
     */
    protected String sourceDirectory = SOURCE_DIRECTORY_EDEFAULT;

    /**
     * The default value of the '{@link #getPortName() <em>Port Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPortName()
     * @generated
     * @ordered
     */
    protected static final String PORT_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPortName() <em>Port Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPortName()
     * @generated
     * @ordered
     */
    protected String portName = PORT_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Java2WSDataModelImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return CXFPackage.Literals.JAVA2_WS_DATA_MODEL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isSoap12Binding() {
        return soap12Binding;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSoap12Binding(boolean newSoap12Binding) {
        boolean oldSoap12Binding = soap12Binding;
        soap12Binding = newSoap12Binding;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__SOAP12_BINDING, oldSoap12Binding, soap12Binding));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGenerateXSDImports() {
        return generateXSDImports;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerateXSDImports(boolean newGenerateXSDImports) {
        boolean oldGenerateXSDImports = generateXSDImports;
        generateXSDImports = newGenerateXSDImports;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_XSD_IMPORTS, oldGenerateXSDImports, generateXSDImports));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGenerateWSDL() {
        return generateWSDL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerateWSDL(boolean newGenerateWSDL) {
        boolean oldGenerateWSDL = generateWSDL;
        generateWSDL = newGenerateWSDL;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WSDL, oldGenerateWSDL, generateWSDL));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGenerateWrapperFaultBeans() {
        return generateWrapperFaultBeans;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerateWrapperFaultBeans(boolean newGenerateWrapperFaultBeans) {
        boolean oldGenerateWrapperFaultBeans = generateWrapperFaultBeans;
        generateWrapperFaultBeans = newGenerateWrapperFaultBeans;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WRAPPER_FAULT_BEANS, oldGenerateWrapperFaultBeans, generateWrapperFaultBeans));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isAnnotationProcessingEnabled() {
        return annotationProcessingEnabled;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAnnotationProcessingEnabled(boolean newAnnotationProcessingEnabled) {
        boolean oldAnnotationProcessingEnabled = annotationProcessingEnabled;
        annotationProcessingEnabled = newAnnotationProcessingEnabled;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_PROCESSING_ENABLED, oldAnnotationProcessingEnabled, annotationProcessingEnabled));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGenerateWebMethodAnnotation() {
        return generateWebMethodAnnotation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerateWebMethodAnnotation(boolean newGenerateWebMethodAnnotation) {
        boolean oldGenerateWebMethodAnnotation = generateWebMethodAnnotation;
        generateWebMethodAnnotation = newGenerateWebMethodAnnotation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_METHOD_ANNOTATION, oldGenerateWebMethodAnnotation, generateWebMethodAnnotation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGenerateWebParamAnnotation() {
        return generateWebParamAnnotation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerateWebParamAnnotation(boolean newGenerateWebParamAnnotation) {
        boolean oldGenerateWebParamAnnotation = generateWebParamAnnotation;
        generateWebParamAnnotation = newGenerateWebParamAnnotation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_PARAM_ANNOTATION, oldGenerateWebParamAnnotation, generateWebParamAnnotation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGenerateRequestWrapperAnnotation() {
        return generateRequestWrapperAnnotation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerateRequestWrapperAnnotation(boolean newGenerateRequestWrapperAnnotation) {
        boolean oldGenerateRequestWrapperAnnotation = generateRequestWrapperAnnotation;
        generateRequestWrapperAnnotation = newGenerateRequestWrapperAnnotation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_REQUEST_WRAPPER_ANNOTATION, oldGenerateRequestWrapperAnnotation, generateRequestWrapperAnnotation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGenerateResponseWrapperAnnotation() {
        return generateResponseWrapperAnnotation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerateResponseWrapperAnnotation(boolean newGenerateResponseWrapperAnnotation) {
        boolean oldGenerateResponseWrapperAnnotation = generateResponseWrapperAnnotation;
        generateResponseWrapperAnnotation = newGenerateResponseWrapperAnnotation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_RESPONSE_WRAPPER_ANNOTATION, oldGenerateResponseWrapperAnnotation, generateResponseWrapperAnnotation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getClasspath() {
        return classpath;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setClasspath(String newClasspath) {
        String oldClasspath = classpath;
        classpath = newClasspath;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__CLASSPATH, oldClasspath, classpath));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getJavaStartingPoint() {
        return javaStartingPoint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setJavaStartingPoint(String newJavaStartingPoint) {
        String oldJavaStartingPoint = javaStartingPoint;
        javaStartingPoint = newJavaStartingPoint;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__JAVA_STARTING_POINT, oldJavaStartingPoint, javaStartingPoint));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseServiceEndpointInterface() {
        return useServiceEndpointInterface;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseServiceEndpointInterface(boolean newUseServiceEndpointInterface) {
        boolean oldUseServiceEndpointInterface = useServiceEndpointInterface;
        useServiceEndpointInterface = newUseServiceEndpointInterface;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__USE_SERVICE_ENDPOINT_INTERFACE, oldUseServiceEndpointInterface, useServiceEndpointInterface));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isExtractInterface() {
        return extractInterface;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExtractInterface(boolean newExtractInterface) {
        boolean oldExtractInterface = extractInterface;
        extractInterface = newExtractInterface;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__EXTRACT_INTERFACE, oldExtractInterface, extractInterface));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getServiceEndpointInterfaceName() {
        return serviceEndpointInterfaceName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceEndpointInterfaceName(String newServiceEndpointInterfaceName) {
        String oldServiceEndpointInterfaceName = serviceEndpointInterfaceName;
        serviceEndpointInterfaceName = newServiceEndpointInterfaceName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__SERVICE_ENDPOINT_INTERFACE_NAME, oldServiceEndpointInterfaceName, serviceEndpointInterfaceName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Map<IMethod, Map<String, Boolean>> getMethodMap() {
        return methodMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setMethodMap(Map<IMethod, Map<String, Boolean>> newMethodMap) {
        Map<IMethod, Map<String, Boolean>> oldMethodMap = methodMap;
        methodMap = newMethodMap;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__METHOD_MAP, oldMethodMap, methodMap));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Map<String, Boolean> getAnnotationMap() {
        return annotationMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAnnotationMap(Map<String, Boolean> newAnnotationMap) {
        Map<String, Boolean> oldAnnotationMap = annotationMap;
        annotationMap = newAnnotationMap;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_MAP, oldAnnotationMap, annotationMap));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getSourceDirectory() {
        return sourceDirectory;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setSourceDirectory(String newSourceDirectory) {
        String oldSourceDirectory = sourceDirectory;
        sourceDirectory = newSourceDirectory;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__SOURCE_DIRECTORY, oldSourceDirectory, sourceDirectory));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getPortName() {
        return portName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setPortName(String newPortName) {
        String oldPortName = portName;
        portName = newPortName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.JAVA2_WS_DATA_MODEL__PORT_NAME, oldPortName, portName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case CXFPackage.JAVA2_WS_DATA_MODEL__SOAP12_BINDING:
                return isSoap12Binding();
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_XSD_IMPORTS:
                return isGenerateXSDImports();
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WSDL:
                return isGenerateWSDL();
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WRAPPER_FAULT_BEANS:
                return isGenerateWrapperFaultBeans();
            case CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_PROCESSING_ENABLED:
                return isAnnotationProcessingEnabled();
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_METHOD_ANNOTATION:
                return isGenerateWebMethodAnnotation();
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_PARAM_ANNOTATION:
                return isGenerateWebParamAnnotation();
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_REQUEST_WRAPPER_ANNOTATION:
                return isGenerateRequestWrapperAnnotation();
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_RESPONSE_WRAPPER_ANNOTATION:
                return isGenerateResponseWrapperAnnotation();
            case CXFPackage.JAVA2_WS_DATA_MODEL__CLASSPATH:
                return getClasspath();
            case CXFPackage.JAVA2_WS_DATA_MODEL__JAVA_STARTING_POINT:
                return getJavaStartingPoint();
            case CXFPackage.JAVA2_WS_DATA_MODEL__USE_SERVICE_ENDPOINT_INTERFACE:
                return isUseServiceEndpointInterface();
            case CXFPackage.JAVA2_WS_DATA_MODEL__EXTRACT_INTERFACE:
                return isExtractInterface();
            case CXFPackage.JAVA2_WS_DATA_MODEL__SERVICE_ENDPOINT_INTERFACE_NAME:
                return getServiceEndpointInterfaceName();
            case CXFPackage.JAVA2_WS_DATA_MODEL__METHOD_MAP:
                return getMethodMap();
            case CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_MAP:
                return getAnnotationMap();
            case CXFPackage.JAVA2_WS_DATA_MODEL__SOURCE_DIRECTORY:
                return getSourceDirectory();
            case CXFPackage.JAVA2_WS_DATA_MODEL__PORT_NAME:
                return getPortName();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case CXFPackage.JAVA2_WS_DATA_MODEL__SOAP12_BINDING:
                setSoap12Binding((Boolean)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_XSD_IMPORTS:
                setGenerateXSDImports((Boolean)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WSDL:
                setGenerateWSDL((Boolean)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WRAPPER_FAULT_BEANS:
                setGenerateWrapperFaultBeans((Boolean)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_PROCESSING_ENABLED:
                setAnnotationProcessingEnabled((Boolean)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_METHOD_ANNOTATION:
                setGenerateWebMethodAnnotation((Boolean)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_PARAM_ANNOTATION:
                setGenerateWebParamAnnotation((Boolean)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_REQUEST_WRAPPER_ANNOTATION:
                setGenerateRequestWrapperAnnotation((Boolean)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_RESPONSE_WRAPPER_ANNOTATION:
                setGenerateResponseWrapperAnnotation((Boolean)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__CLASSPATH:
                setClasspath((String)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__JAVA_STARTING_POINT:
                setJavaStartingPoint((String)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__USE_SERVICE_ENDPOINT_INTERFACE:
                setUseServiceEndpointInterface((Boolean)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__EXTRACT_INTERFACE:
                setExtractInterface((Boolean)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__SERVICE_ENDPOINT_INTERFACE_NAME:
                setServiceEndpointInterfaceName((String)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__METHOD_MAP:
                setMethodMap((Map<IMethod, Map<String, Boolean>>)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_MAP:
                setAnnotationMap((Map<String, Boolean>)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__SOURCE_DIRECTORY:
                setSourceDirectory((String)newValue);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__PORT_NAME:
                setPortName((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case CXFPackage.JAVA2_WS_DATA_MODEL__SOAP12_BINDING:
                setSoap12Binding(SOAP12_BINDING_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_XSD_IMPORTS:
                setGenerateXSDImports(GENERATE_XSD_IMPORTS_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WSDL:
                setGenerateWSDL(GENERATE_WSDL_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WRAPPER_FAULT_BEANS:
                setGenerateWrapperFaultBeans(GENERATE_WRAPPER_FAULT_BEANS_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_PROCESSING_ENABLED:
                setAnnotationProcessingEnabled(ANNOTATION_PROCESSING_ENABLED_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_METHOD_ANNOTATION:
                setGenerateWebMethodAnnotation(GENERATE_WEB_METHOD_ANNOTATION_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_PARAM_ANNOTATION:
                setGenerateWebParamAnnotation(GENERATE_WEB_PARAM_ANNOTATION_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_REQUEST_WRAPPER_ANNOTATION:
                setGenerateRequestWrapperAnnotation(GENERATE_REQUEST_WRAPPER_ANNOTATION_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_RESPONSE_WRAPPER_ANNOTATION:
                setGenerateResponseWrapperAnnotation(GENERATE_RESPONSE_WRAPPER_ANNOTATION_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__CLASSPATH:
                setClasspath(CLASSPATH_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__JAVA_STARTING_POINT:
                setJavaStartingPoint(JAVA_STARTING_POINT_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__USE_SERVICE_ENDPOINT_INTERFACE:
                setUseServiceEndpointInterface(USE_SERVICE_ENDPOINT_INTERFACE_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__EXTRACT_INTERFACE:
                setExtractInterface(EXTRACT_INTERFACE_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__SERVICE_ENDPOINT_INTERFACE_NAME:
                setServiceEndpointInterfaceName(SERVICE_ENDPOINT_INTERFACE_NAME_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__METHOD_MAP:
                setMethodMap((Map<IMethod, Map<String, Boolean>>)null);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_MAP:
                setAnnotationMap((Map<String, Boolean>)null);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__SOURCE_DIRECTORY:
                setSourceDirectory(SOURCE_DIRECTORY_EDEFAULT);
                return;
            case CXFPackage.JAVA2_WS_DATA_MODEL__PORT_NAME:
                setPortName(PORT_NAME_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case CXFPackage.JAVA2_WS_DATA_MODEL__SOAP12_BINDING:
                return soap12Binding != SOAP12_BINDING_EDEFAULT;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_XSD_IMPORTS:
                return generateXSDImports != GENERATE_XSD_IMPORTS_EDEFAULT;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WSDL:
                return generateWSDL != GENERATE_WSDL_EDEFAULT;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WRAPPER_FAULT_BEANS:
                return generateWrapperFaultBeans != GENERATE_WRAPPER_FAULT_BEANS_EDEFAULT;
            case CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_PROCESSING_ENABLED:
                return annotationProcessingEnabled != ANNOTATION_PROCESSING_ENABLED_EDEFAULT;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_METHOD_ANNOTATION:
                return generateWebMethodAnnotation != GENERATE_WEB_METHOD_ANNOTATION_EDEFAULT;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_PARAM_ANNOTATION:
                return generateWebParamAnnotation != GENERATE_WEB_PARAM_ANNOTATION_EDEFAULT;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_REQUEST_WRAPPER_ANNOTATION:
                return generateRequestWrapperAnnotation != GENERATE_REQUEST_WRAPPER_ANNOTATION_EDEFAULT;
            case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_RESPONSE_WRAPPER_ANNOTATION:
                return generateResponseWrapperAnnotation != GENERATE_RESPONSE_WRAPPER_ANNOTATION_EDEFAULT;
            case CXFPackage.JAVA2_WS_DATA_MODEL__CLASSPATH:
                return CLASSPATH_EDEFAULT == null ? classpath != null : !CLASSPATH_EDEFAULT.equals(classpath);
            case CXFPackage.JAVA2_WS_DATA_MODEL__JAVA_STARTING_POINT:
                return JAVA_STARTING_POINT_EDEFAULT == null ? javaStartingPoint != null : !JAVA_STARTING_POINT_EDEFAULT.equals(javaStartingPoint);
            case CXFPackage.JAVA2_WS_DATA_MODEL__USE_SERVICE_ENDPOINT_INTERFACE:
                return useServiceEndpointInterface != USE_SERVICE_ENDPOINT_INTERFACE_EDEFAULT;
            case CXFPackage.JAVA2_WS_DATA_MODEL__EXTRACT_INTERFACE:
                return extractInterface != EXTRACT_INTERFACE_EDEFAULT;
            case CXFPackage.JAVA2_WS_DATA_MODEL__SERVICE_ENDPOINT_INTERFACE_NAME:
                return SERVICE_ENDPOINT_INTERFACE_NAME_EDEFAULT == null ? serviceEndpointInterfaceName != null : !SERVICE_ENDPOINT_INTERFACE_NAME_EDEFAULT.equals(serviceEndpointInterfaceName);
            case CXFPackage.JAVA2_WS_DATA_MODEL__METHOD_MAP:
                return methodMap != null;
            case CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_MAP:
                return annotationMap != null;
            case CXFPackage.JAVA2_WS_DATA_MODEL__SOURCE_DIRECTORY:
                return SOURCE_DIRECTORY_EDEFAULT == null ? sourceDirectory != null : !SOURCE_DIRECTORY_EDEFAULT.equals(sourceDirectory);
            case CXFPackage.JAVA2_WS_DATA_MODEL__PORT_NAME:
                return PORT_NAME_EDEFAULT == null ? portName != null : !PORT_NAME_EDEFAULT.equals(portName);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == Java2WSContext.class) {
            switch (derivedFeatureID) {
                case CXFPackage.JAVA2_WS_DATA_MODEL__SOAP12_BINDING: return CXFPackage.JAVA2_WS_CONTEXT__SOAP12_BINDING;
                case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_XSD_IMPORTS: return CXFPackage.JAVA2_WS_CONTEXT__GENERATE_XSD_IMPORTS;
                case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WSDL: return CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WSDL;
                case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WRAPPER_FAULT_BEANS: return CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WRAPPER_FAULT_BEANS;
                case CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_PROCESSING_ENABLED: return CXFPackage.JAVA2_WS_CONTEXT__ANNOTATION_PROCESSING_ENABLED;
                case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_METHOD_ANNOTATION: return CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WEB_METHOD_ANNOTATION;
                case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_PARAM_ANNOTATION: return CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WEB_PARAM_ANNOTATION;
                case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_REQUEST_WRAPPER_ANNOTATION: return CXFPackage.JAVA2_WS_CONTEXT__GENERATE_REQUEST_WRAPPER_ANNOTATION;
                case CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_RESPONSE_WRAPPER_ANNOTATION: return CXFPackage.JAVA2_WS_CONTEXT__GENERATE_RESPONSE_WRAPPER_ANNOTATION;
                default: return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == Java2WSContext.class) {
            switch (baseFeatureID) {
                case CXFPackage.JAVA2_WS_CONTEXT__SOAP12_BINDING: return CXFPackage.JAVA2_WS_DATA_MODEL__SOAP12_BINDING;
                case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_XSD_IMPORTS: return CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_XSD_IMPORTS;
                case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WSDL: return CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WSDL;
                case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WRAPPER_FAULT_BEANS: return CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WRAPPER_FAULT_BEANS;
                case CXFPackage.JAVA2_WS_CONTEXT__ANNOTATION_PROCESSING_ENABLED: return CXFPackage.JAVA2_WS_DATA_MODEL__ANNOTATION_PROCESSING_ENABLED;
                case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WEB_METHOD_ANNOTATION: return CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_METHOD_ANNOTATION;
                case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WEB_PARAM_ANNOTATION: return CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_WEB_PARAM_ANNOTATION;
                case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_REQUEST_WRAPPER_ANNOTATION: return CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_REQUEST_WRAPPER_ANNOTATION;
                case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_RESPONSE_WRAPPER_ANNOTATION: return CXFPackage.JAVA2_WS_DATA_MODEL__GENERATE_RESPONSE_WRAPPER_ANNOTATION;
                default: return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (soap12Binding: ");
        result.append(soap12Binding);
        result.append(", generateXSDImports: ");
        result.append(generateXSDImports);
        result.append(", generateWSDL: ");
        result.append(generateWSDL);
        result.append(", generateWrapperFaultBeans: ");
        result.append(generateWrapperFaultBeans);
        result.append(", annotationProcessingEnabled: ");
        result.append(annotationProcessingEnabled);
        result.append(", generateWebMethodAnnotation: ");
        result.append(generateWebMethodAnnotation);
        result.append(", generateWebParamAnnotation: ");
        result.append(generateWebParamAnnotation);
        result.append(", generateRequestWrapperAnnotation: ");
        result.append(generateRequestWrapperAnnotation);
        result.append(", generateResponseWrapperAnnotation: ");
        result.append(generateResponseWrapperAnnotation);
        result.append(", classpath: ");
        result.append(classpath);
        result.append(", javaStartingPoint: ");
        result.append(javaStartingPoint);
        result.append(", useServiceEndpointInterface: ");
        result.append(useServiceEndpointInterface);
        result.append(", extractInterface: ");
        result.append(extractInterface);
        result.append(", serviceEndpointInterfaceName: ");
        result.append(serviceEndpointInterfaceName);
        result.append(", methodMap: ");
        result.append(methodMap);
        result.append(", annotationMap: ");
        result.append(annotationMap);
        result.append(", sourceDirectory: ");
        result.append(sourceDirectory);
        result.append(", portName: ");
        result.append(portName);
        result.append(')');
        return result.toString();
    }

} //Java2WSDataModelImpl
