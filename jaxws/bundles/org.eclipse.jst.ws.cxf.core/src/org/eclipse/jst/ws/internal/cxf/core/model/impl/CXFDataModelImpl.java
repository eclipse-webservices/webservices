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
 * $Id: CXFDataModelImpl.java,v 1.4 2010/01/17 19:56:56 sclarke Exp $
 */
package org.eclipse.jst.ws.internal.cxf.core.model.impl;

import java.net.URL;
import java.util.Map;

import javax.wsdl.Definition;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage;
import org.eclipse.jst.ws.internal.cxf.core.model.DataBinding;
import org.eclipse.jst.ws.internal.cxf.core.model.Frontend;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getDefaultRuntimeLocation <em>Default Runtime Location</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getDefaultRuntimeType <em>Default Runtime Type</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getDefaultRuntimeVersion <em>Default Runtime Version</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#isVerbose <em>Verbose</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#isGenerateAntBuildFile <em>Generate Ant Build File</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#isGenerateClient <em>Generate Client</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#isGenerateServer <em>Generate Server</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getDatabinding <em>Databinding</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getFrontend <em>Frontend</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#isUseSpringApplicationContext <em>Use Spring Application Context</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#isExportCXFClasspathContainer <em>Export CXF Classpath Container</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getInstallations <em>Installations</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getProjectName <em>Project Name</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getResourceDirectory <em>Resource Directory</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getClassDirectory <em>Class Directory</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getWsdlFileName <em>Wsdl File Name</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getWsdlURL <em>Wsdl URL</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getConfigWsdlLocation <em>Config Wsdl Location</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getFullyQualifiedJavaClassName <em>Fully Qualified Java Class Name</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getFullyQualifiedJavaInterfaceName <em>Fully Qualified Java Interface Name</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getConfigId <em>Config Id</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getTargetNamespace <em>Target Namespace</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getEndpointName <em>Endpoint Name</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getServiceName <em>Service Name</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getWsdlDefinition <em>Wsdl Definition</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFDataModelImpl#getWsdlLocation <em>Wsdl Location</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class CXFDataModelImpl extends EObjectImpl implements CXFDataModel {
    /**
     * The default value of the '{@link #getDefaultRuntimeLocation() <em>Default Runtime Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultRuntimeLocation()
     * @generated
     * @ordered
     */
    protected static final String DEFAULT_RUNTIME_LOCATION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDefaultRuntimeLocation() <em>Default Runtime Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultRuntimeLocation()
     * @generated
     * @ordered
     */
    protected String defaultRuntimeLocation = DEFAULT_RUNTIME_LOCATION_EDEFAULT;

    /**
     * The default value of the '{@link #getDefaultRuntimeType() <em>Default Runtime Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultRuntimeType()
     * @generated
     * @ordered
     */
    protected static final String DEFAULT_RUNTIME_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDefaultRuntimeType() <em>Default Runtime Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultRuntimeType()
     * @generated
     * @ordered
     */
    protected String defaultRuntimeType = DEFAULT_RUNTIME_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getDefaultRuntimeVersion() <em>Default Runtime Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultRuntimeVersion()
     * @generated
     * @ordered
     */
    protected static final String DEFAULT_RUNTIME_VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDefaultRuntimeVersion() <em>Default Runtime Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDefaultRuntimeVersion()
     * @generated
     * @ordered
     */
    protected String defaultRuntimeVersion = DEFAULT_RUNTIME_VERSION_EDEFAULT;

    /**
     * The default value of the '{@link #isVerbose() <em>Verbose</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isVerbose()
     * @generated
     * @ordered
     */
    protected static final boolean VERBOSE_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isVerbose() <em>Verbose</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isVerbose()
     * @generated
     * @ordered
     */
    protected boolean verbose = VERBOSE_EDEFAULT;

    /**
     * The default value of the '{@link #isGenerateAntBuildFile() <em>Generate Ant Build File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateAntBuildFile()
     * @generated
     * @ordered
     */
    protected static final boolean GENERATE_ANT_BUILD_FILE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isGenerateAntBuildFile() <em>Generate Ant Build File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateAntBuildFile()
     * @generated
     * @ordered
     */
    protected boolean generateAntBuildFile = GENERATE_ANT_BUILD_FILE_EDEFAULT;

    /**
     * The default value of the '{@link #isGenerateClient() <em>Generate Client</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateClient()
     * @generated
     * @ordered
     */
    protected static final boolean GENERATE_CLIENT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isGenerateClient() <em>Generate Client</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateClient()
     * @generated
     * @ordered
     */
    protected boolean generateClient = GENERATE_CLIENT_EDEFAULT;

    /**
     * The default value of the '{@link #isGenerateServer() <em>Generate Server</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateServer()
     * @generated
     * @ordered
     */
    protected static final boolean GENERATE_SERVER_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isGenerateServer() <em>Generate Server</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateServer()
     * @generated
     * @ordered
     */
    protected boolean generateServer = GENERATE_SERVER_EDEFAULT;

    /**
     * The default value of the '{@link #getDatabinding() <em>Databinding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDatabinding()
     * @generated
     * @ordered
     */
    protected static final DataBinding DATABINDING_EDEFAULT = DataBinding.JAXB;

    /**
     * The cached value of the '{@link #getDatabinding() <em>Databinding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getDatabinding()
     * @generated
     * @ordered
     */
    protected DataBinding databinding = DATABINDING_EDEFAULT;

    /**
     * The default value of the '{@link #getFrontend() <em>Frontend</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFrontend()
     * @generated
     * @ordered
     */
    protected static final Frontend FRONTEND_EDEFAULT = Frontend.JAXWS;

    /**
     * The cached value of the '{@link #getFrontend() <em>Frontend</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFrontend()
     * @generated
     * @ordered
     */
    protected Frontend frontend = FRONTEND_EDEFAULT;

    /**
     * The default value of the '{@link #isUseSpringApplicationContext() <em>Use Spring Application Context</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseSpringApplicationContext()
     * @generated
     * @ordered
     */
    protected static final boolean USE_SPRING_APPLICATION_CONTEXT_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isUseSpringApplicationContext() <em>Use Spring Application Context</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseSpringApplicationContext()
     * @generated
     * @ordered
     */
    protected boolean useSpringApplicationContext = USE_SPRING_APPLICATION_CONTEXT_EDEFAULT;

    /**
     * The default value of the '{@link #isExportCXFClasspathContainer() <em>Export CXF Classpath Container</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isExportCXFClasspathContainer()
     * @generated
     * @ordered
     */
    protected static final boolean EXPORT_CXF_CLASSPATH_CONTAINER_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isExportCXFClasspathContainer() <em>Export CXF Classpath Container</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isExportCXFClasspathContainer()
     * @generated
     * @ordered
     */
    protected boolean exportCXFClasspathContainer = EXPORT_CXF_CLASSPATH_CONTAINER_EDEFAULT;

    /**
     * The cached value of the '{@link #getInstallations() <em>Installations</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getInstallations()
     * @generated
     * @ordered
     */
    protected Map<String, CXFInstall> installations;

    /**
     * The default value of the '{@link #getProjectName() <em>Project Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProjectName()
     * @generated
     * @ordered
     */
    protected static final String PROJECT_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProjectName() <em>Project Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProjectName()
     * @generated
     * @ordered
     */
    protected String projectName = PROJECT_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getResourceDirectory() <em>Resource Directory</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceDirectory()
     * @generated
     * @ordered
     */
    protected static final String RESOURCE_DIRECTORY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getResourceDirectory() <em>Resource Directory</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResourceDirectory()
     * @generated
     * @ordered
     */
    protected String resourceDirectory = RESOURCE_DIRECTORY_EDEFAULT;

    /**
     * The default value of the '{@link #getClassDirectory() <em>Class Directory</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getClassDirectory()
     * @generated
     * @ordered
     */
    protected static final String CLASS_DIRECTORY_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getClassDirectory() <em>Class Directory</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getClassDirectory()
     * @generated
     * @ordered
     */
    protected String classDirectory = CLASS_DIRECTORY_EDEFAULT;

    /**
     * The default value of the '{@link #getWsdlFileName() <em>Wsdl File Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWsdlFileName()
     * @generated
     * @ordered
     */
    protected static final String WSDL_FILE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getWsdlFileName() <em>Wsdl File Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWsdlFileName()
     * @generated
     * @ordered
     */
    protected String wsdlFileName = WSDL_FILE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getWsdlURL() <em>Wsdl URL</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWsdlURL()
     * @generated
     * @ordered
     */
    protected static final URL WSDL_URL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getWsdlURL() <em>Wsdl URL</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWsdlURL()
     * @generated
     * @ordered
     */
    protected URL wsdlURL = WSDL_URL_EDEFAULT;

    /**
     * The default value of the '{@link #getConfigWsdlLocation() <em>Config Wsdl Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConfigWsdlLocation()
     * @generated
     * @ordered
     */
    protected static final String CONFIG_WSDL_LOCATION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getConfigWsdlLocation() <em>Config Wsdl Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConfigWsdlLocation()
     * @generated
     * @ordered
     */
    protected String configWsdlLocation = CONFIG_WSDL_LOCATION_EDEFAULT;

    /**
     * The default value of the '{@link #getFullyQualifiedJavaClassName() <em>Fully Qualified Java Class Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFullyQualifiedJavaClassName()
     * @generated
     * @ordered
     */
    protected static final String FULLY_QUALIFIED_JAVA_CLASS_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFullyQualifiedJavaClassName() <em>Fully Qualified Java Class Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFullyQualifiedJavaClassName()
     * @generated
     * @ordered
     */
    protected String fullyQualifiedJavaClassName = FULLY_QUALIFIED_JAVA_CLASS_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getFullyQualifiedJavaInterfaceName() <em>Fully Qualified Java Interface Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFullyQualifiedJavaInterfaceName()
     * @generated
     * @ordered
     */
    protected static final String FULLY_QUALIFIED_JAVA_INTERFACE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFullyQualifiedJavaInterfaceName() <em>Fully Qualified Java Interface Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getFullyQualifiedJavaInterfaceName()
     * @generated
     * @ordered
     */
    protected String fullyQualifiedJavaInterfaceName = FULLY_QUALIFIED_JAVA_INTERFACE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getConfigId() <em>Config Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConfigId()
     * @generated
     * @ordered
     */
    protected static final String CONFIG_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getConfigId() <em>Config Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConfigId()
     * @generated
     * @ordered
     */
    protected String configId = CONFIG_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getTargetNamespace() <em>Target Namespace</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetNamespace()
     * @generated
     * @ordered
     */
    protected static final String TARGET_NAMESPACE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTargetNamespace() <em>Target Namespace</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTargetNamespace()
     * @generated
     * @ordered
     */
    protected String targetNamespace = TARGET_NAMESPACE_EDEFAULT;

    /**
     * The default value of the '{@link #getEndpointName() <em>Endpoint Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndpointName()
     * @generated
     * @ordered
     */
    protected static final String ENDPOINT_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEndpointName() <em>Endpoint Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getEndpointName()
     * @generated
     * @ordered
     */
    protected String endpointName = ENDPOINT_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getServiceName() <em>Service Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceName()
     * @generated
     * @ordered
     */
    protected static final String SERVICE_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getServiceName() <em>Service Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getServiceName()
     * @generated
     * @ordered
     */
    protected String serviceName = SERVICE_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getWsdlDefinition() <em>Wsdl Definition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWsdlDefinition()
     * @generated
     * @ordered
     */
    protected static final Definition WSDL_DEFINITION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getWsdlDefinition() <em>Wsdl Definition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWsdlDefinition()
     * @generated
     * @ordered
     */
    protected Definition wsdlDefinition = WSDL_DEFINITION_EDEFAULT;

    /**
     * The default value of the '{@link #getWsdlLocation() <em>Wsdl Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWsdlLocation()
     * @generated
     * @ordered
     */
    protected static final String WSDL_LOCATION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getWsdlLocation() <em>Wsdl Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWsdlLocation()
     * @generated
     * @ordered
     */
    protected String wsdlLocation = WSDL_LOCATION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CXFDataModelImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return CXFPackage.Literals.CXF_DATA_MODEL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDefaultRuntimeLocation() {
        return defaultRuntimeLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultRuntimeLocation(String newDefaultRuntimeLocation) {
        String oldDefaultRuntimeLocation = defaultRuntimeLocation;
        defaultRuntimeLocation = newDefaultRuntimeLocation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_LOCATION, oldDefaultRuntimeLocation, defaultRuntimeLocation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDefaultRuntimeType() {
        return defaultRuntimeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultRuntimeType(String newDefaultRuntimeType) {
        String oldDefaultRuntimeType = defaultRuntimeType;
        defaultRuntimeType = newDefaultRuntimeType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_TYPE, oldDefaultRuntimeType, defaultRuntimeType));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getDefaultRuntimeVersion() {
        return defaultRuntimeVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDefaultRuntimeVersion(String newDefaultRuntimeVersion) {
        String oldDefaultRuntimeVersion = defaultRuntimeVersion;
        defaultRuntimeVersion = newDefaultRuntimeVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_VERSION, oldDefaultRuntimeVersion, defaultRuntimeVersion));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVerbose(boolean newVerbose) {
        boolean oldVerbose = verbose;
        verbose = newVerbose;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__VERBOSE, oldVerbose, verbose));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGenerateAntBuildFile() {
        return generateAntBuildFile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerateAntBuildFile(boolean newGenerateAntBuildFile) {
        boolean oldGenerateAntBuildFile = generateAntBuildFile;
        generateAntBuildFile = newGenerateAntBuildFile;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__GENERATE_ANT_BUILD_FILE, oldGenerateAntBuildFile, generateAntBuildFile));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGenerateClient() {
        return generateClient;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerateClient(boolean newGenerateClient) {
        boolean oldGenerateClient = generateClient;
        generateClient = newGenerateClient;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__GENERATE_CLIENT, oldGenerateClient, generateClient));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGenerateServer() {
        return generateServer;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerateServer(boolean newGenerateServer) {
        boolean oldGenerateServer = generateServer;
        generateServer = newGenerateServer;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__GENERATE_SERVER, oldGenerateServer, generateServer));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataBinding getDatabinding() {
        return databinding;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDatabinding(DataBinding newDatabinding) {
        DataBinding oldDatabinding = databinding;
        databinding = newDatabinding == null ? DATABINDING_EDEFAULT : newDatabinding;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__DATABINDING, oldDatabinding, databinding));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Frontend getFrontend() {
        return frontend;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFrontend(Frontend newFrontend) {
        Frontend oldFrontend = frontend;
        frontend = newFrontend == null ? FRONTEND_EDEFAULT : newFrontend;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__FRONTEND, oldFrontend, frontend));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseSpringApplicationContext() {
        return useSpringApplicationContext;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseSpringApplicationContext(boolean newUseSpringApplicationContext) {
        boolean oldUseSpringApplicationContext = useSpringApplicationContext;
        useSpringApplicationContext = newUseSpringApplicationContext;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__USE_SPRING_APPLICATION_CONTEXT, oldUseSpringApplicationContext, useSpringApplicationContext));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isExportCXFClasspathContainer() {
        return exportCXFClasspathContainer;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExportCXFClasspathContainer(boolean newExportCXFClasspathContainer) {
        boolean oldExportCXFClasspathContainer = exportCXFClasspathContainer;
        exportCXFClasspathContainer = newExportCXFClasspathContainer;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__EXPORT_CXF_CLASSPATH_CONTAINER, oldExportCXFClasspathContainer, exportCXFClasspathContainer));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Map<String, CXFInstall> getInstallations() {
        return installations;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setInstallations(Map<String, CXFInstall> newInstallations) {
        Map<String, CXFInstall> oldInstallations = installations;
        installations = newInstallations;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__INSTALLATIONS, oldInstallations, installations));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProjectName(String newProjectName) {
        String oldProjectName = projectName;
        projectName = newProjectName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__PROJECT_NAME, oldProjectName, projectName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getResourceDirectory() {
        return resourceDirectory;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResourceDirectory(String newResourceDirectory) {
        String oldResourceDirectory = resourceDirectory;
        resourceDirectory = newResourceDirectory;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__RESOURCE_DIRECTORY, oldResourceDirectory, resourceDirectory));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getClassDirectory() {
        return classDirectory;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setClassDirectory(String newClassDirectory) {
        String oldClassDirectory = classDirectory;
        classDirectory = newClassDirectory;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__CLASS_DIRECTORY, oldClassDirectory, classDirectory));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getWsdlFileName() {
        return wsdlFileName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWsdlFileName(String newWsdlFileName) {
        String oldWsdlFileName = wsdlFileName;
        wsdlFileName = newWsdlFileName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__WSDL_FILE_NAME, oldWsdlFileName, wsdlFileName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public URL getWsdlURL() {
        return wsdlURL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWsdlURL(URL newWsdlURL) {
        URL oldWsdlURL = wsdlURL;
        wsdlURL = newWsdlURL;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__WSDL_URL, oldWsdlURL, wsdlURL));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getConfigWsdlLocation() {
        return configWsdlLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConfigWsdlLocation(String newConfigWsdlLocation) {
        String oldConfigWsdlLocation = configWsdlLocation;
        configWsdlLocation = newConfigWsdlLocation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__CONFIG_WSDL_LOCATION, oldConfigWsdlLocation, configWsdlLocation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFullyQualifiedJavaClassName() {
        return fullyQualifiedJavaClassName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFullyQualifiedJavaClassName(String newFullyQualifiedJavaClassName) {
        String oldFullyQualifiedJavaClassName = fullyQualifiedJavaClassName;
        fullyQualifiedJavaClassName = newFullyQualifiedJavaClassName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME, oldFullyQualifiedJavaClassName, fullyQualifiedJavaClassName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getFullyQualifiedJavaInterfaceName() {
        return fullyQualifiedJavaInterfaceName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setFullyQualifiedJavaInterfaceName(String newFullyQualifiedJavaInterfaceName) {
        String oldFullyQualifiedJavaInterfaceName = fullyQualifiedJavaInterfaceName;
        fullyQualifiedJavaInterfaceName = newFullyQualifiedJavaInterfaceName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME, oldFullyQualifiedJavaInterfaceName, fullyQualifiedJavaInterfaceName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getConfigId() {
        return configId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setConfigId(String newConfigId) {
        String oldConfigId = configId;
        configId = newConfigId;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__CONFIG_ID, oldConfigId, configId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getTargetNamespace() {
        return targetNamespace;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setTargetNamespace(String newTargetNamespace) {
        String oldTargetNamespace = targetNamespace;
        targetNamespace = newTargetNamespace;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__TARGET_NAMESPACE, oldTargetNamespace, targetNamespace));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEndpointName() {
        return endpointName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setEndpointName(String newEndpointName) {
        String oldEndpointName = endpointName;
        endpointName = newEndpointName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__ENDPOINT_NAME, oldEndpointName, endpointName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setServiceName(String newServiceName) {
        String oldServiceName = serviceName;
        serviceName = newServiceName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__SERVICE_NAME, oldServiceName, serviceName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Definition getWsdlDefinition() {
        return wsdlDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWsdlDefinition(Definition newWsdlDefinition) {
        Definition oldWsdlDefinition = wsdlDefinition;
        wsdlDefinition = newWsdlDefinition;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__WSDL_DEFINITION, oldWsdlDefinition, wsdlDefinition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getWsdlLocation() {
        return wsdlLocation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWsdlLocation(String newWsdlLocation) {
        String oldWsdlLocation = wsdlLocation;
        wsdlLocation = newWsdlLocation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.CXF_DATA_MODEL__WSDL_LOCATION, oldWsdlLocation, wsdlLocation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_LOCATION:
                return getDefaultRuntimeLocation();
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_TYPE:
                return getDefaultRuntimeType();
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_VERSION:
                return getDefaultRuntimeVersion();
            case CXFPackage.CXF_DATA_MODEL__VERBOSE:
                return isVerbose();
            case CXFPackage.CXF_DATA_MODEL__GENERATE_ANT_BUILD_FILE:
                return isGenerateAntBuildFile();
            case CXFPackage.CXF_DATA_MODEL__GENERATE_CLIENT:
                return isGenerateClient();
            case CXFPackage.CXF_DATA_MODEL__GENERATE_SERVER:
                return isGenerateServer();
            case CXFPackage.CXF_DATA_MODEL__DATABINDING:
                return getDatabinding();
            case CXFPackage.CXF_DATA_MODEL__FRONTEND:
                return getFrontend();
            case CXFPackage.CXF_DATA_MODEL__USE_SPRING_APPLICATION_CONTEXT:
                return isUseSpringApplicationContext();
            case CXFPackage.CXF_DATA_MODEL__EXPORT_CXF_CLASSPATH_CONTAINER:
                return isExportCXFClasspathContainer();
            case CXFPackage.CXF_DATA_MODEL__INSTALLATIONS:
                return getInstallations();
            case CXFPackage.CXF_DATA_MODEL__PROJECT_NAME:
                return getProjectName();
            case CXFPackage.CXF_DATA_MODEL__RESOURCE_DIRECTORY:
                return getResourceDirectory();
            case CXFPackage.CXF_DATA_MODEL__CLASS_DIRECTORY:
                return getClassDirectory();
            case CXFPackage.CXF_DATA_MODEL__WSDL_FILE_NAME:
                return getWsdlFileName();
            case CXFPackage.CXF_DATA_MODEL__WSDL_URL:
                return getWsdlURL();
            case CXFPackage.CXF_DATA_MODEL__CONFIG_WSDL_LOCATION:
                return getConfigWsdlLocation();
            case CXFPackage.CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME:
                return getFullyQualifiedJavaClassName();
            case CXFPackage.CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME:
                return getFullyQualifiedJavaInterfaceName();
            case CXFPackage.CXF_DATA_MODEL__CONFIG_ID:
                return getConfigId();
            case CXFPackage.CXF_DATA_MODEL__TARGET_NAMESPACE:
                return getTargetNamespace();
            case CXFPackage.CXF_DATA_MODEL__ENDPOINT_NAME:
                return getEndpointName();
            case CXFPackage.CXF_DATA_MODEL__SERVICE_NAME:
                return getServiceName();
            case CXFPackage.CXF_DATA_MODEL__WSDL_DEFINITION:
                return getWsdlDefinition();
            case CXFPackage.CXF_DATA_MODEL__WSDL_LOCATION:
                return getWsdlLocation();
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
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_LOCATION:
                setDefaultRuntimeLocation((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_TYPE:
                setDefaultRuntimeType((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_VERSION:
                setDefaultRuntimeVersion((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__VERBOSE:
                setVerbose((Boolean)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__GENERATE_ANT_BUILD_FILE:
                setGenerateAntBuildFile((Boolean)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__GENERATE_CLIENT:
                setGenerateClient((Boolean)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__GENERATE_SERVER:
                setGenerateServer((Boolean)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__DATABINDING:
                setDatabinding((DataBinding)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__FRONTEND:
                setFrontend((Frontend)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__USE_SPRING_APPLICATION_CONTEXT:
                setUseSpringApplicationContext((Boolean)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__EXPORT_CXF_CLASSPATH_CONTAINER:
                setExportCXFClasspathContainer((Boolean)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__INSTALLATIONS:
                setInstallations((Map<String, CXFInstall>)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__PROJECT_NAME:
                setProjectName((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__RESOURCE_DIRECTORY:
                setResourceDirectory((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__CLASS_DIRECTORY:
                setClassDirectory((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__WSDL_FILE_NAME:
                setWsdlFileName((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__WSDL_URL:
                setWsdlURL((URL)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__CONFIG_WSDL_LOCATION:
                setConfigWsdlLocation((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME:
                setFullyQualifiedJavaClassName((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME:
                setFullyQualifiedJavaInterfaceName((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__CONFIG_ID:
                setConfigId((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__TARGET_NAMESPACE:
                setTargetNamespace((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__ENDPOINT_NAME:
                setEndpointName((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__SERVICE_NAME:
                setServiceName((String)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__WSDL_DEFINITION:
                setWsdlDefinition((Definition)newValue);
                return;
            case CXFPackage.CXF_DATA_MODEL__WSDL_LOCATION:
                setWsdlLocation((String)newValue);
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
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_LOCATION:
                setDefaultRuntimeLocation(DEFAULT_RUNTIME_LOCATION_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_TYPE:
                setDefaultRuntimeType(DEFAULT_RUNTIME_TYPE_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_VERSION:
                setDefaultRuntimeVersion(DEFAULT_RUNTIME_VERSION_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__VERBOSE:
                setVerbose(VERBOSE_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__GENERATE_ANT_BUILD_FILE:
                setGenerateAntBuildFile(GENERATE_ANT_BUILD_FILE_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__GENERATE_CLIENT:
                setGenerateClient(GENERATE_CLIENT_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__GENERATE_SERVER:
                setGenerateServer(GENERATE_SERVER_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__DATABINDING:
                setDatabinding(DATABINDING_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__FRONTEND:
                setFrontend(FRONTEND_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__USE_SPRING_APPLICATION_CONTEXT:
                setUseSpringApplicationContext(USE_SPRING_APPLICATION_CONTEXT_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__EXPORT_CXF_CLASSPATH_CONTAINER:
                setExportCXFClasspathContainer(EXPORT_CXF_CLASSPATH_CONTAINER_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__INSTALLATIONS:
                setInstallations((Map<String, CXFInstall>)null);
                return;
            case CXFPackage.CXF_DATA_MODEL__PROJECT_NAME:
                setProjectName(PROJECT_NAME_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__RESOURCE_DIRECTORY:
                setResourceDirectory(RESOURCE_DIRECTORY_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__CLASS_DIRECTORY:
                setClassDirectory(CLASS_DIRECTORY_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__WSDL_FILE_NAME:
                setWsdlFileName(WSDL_FILE_NAME_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__WSDL_URL:
                setWsdlURL(WSDL_URL_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__CONFIG_WSDL_LOCATION:
                setConfigWsdlLocation(CONFIG_WSDL_LOCATION_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME:
                setFullyQualifiedJavaClassName(FULLY_QUALIFIED_JAVA_CLASS_NAME_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME:
                setFullyQualifiedJavaInterfaceName(FULLY_QUALIFIED_JAVA_INTERFACE_NAME_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__CONFIG_ID:
                setConfigId(CONFIG_ID_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__TARGET_NAMESPACE:
                setTargetNamespace(TARGET_NAMESPACE_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__ENDPOINT_NAME:
                setEndpointName(ENDPOINT_NAME_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__SERVICE_NAME:
                setServiceName(SERVICE_NAME_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__WSDL_DEFINITION:
                setWsdlDefinition(WSDL_DEFINITION_EDEFAULT);
                return;
            case CXFPackage.CXF_DATA_MODEL__WSDL_LOCATION:
                setWsdlLocation(WSDL_LOCATION_EDEFAULT);
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
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_LOCATION:
                return DEFAULT_RUNTIME_LOCATION_EDEFAULT == null ? defaultRuntimeLocation != null : !DEFAULT_RUNTIME_LOCATION_EDEFAULT.equals(defaultRuntimeLocation);
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_TYPE:
                return DEFAULT_RUNTIME_TYPE_EDEFAULT == null ? defaultRuntimeType != null : !DEFAULT_RUNTIME_TYPE_EDEFAULT.equals(defaultRuntimeType);
            case CXFPackage.CXF_DATA_MODEL__DEFAULT_RUNTIME_VERSION:
                return DEFAULT_RUNTIME_VERSION_EDEFAULT == null ? defaultRuntimeVersion != null : !DEFAULT_RUNTIME_VERSION_EDEFAULT.equals(defaultRuntimeVersion);
            case CXFPackage.CXF_DATA_MODEL__VERBOSE:
                return verbose != VERBOSE_EDEFAULT;
            case CXFPackage.CXF_DATA_MODEL__GENERATE_ANT_BUILD_FILE:
                return generateAntBuildFile != GENERATE_ANT_BUILD_FILE_EDEFAULT;
            case CXFPackage.CXF_DATA_MODEL__GENERATE_CLIENT:
                return generateClient != GENERATE_CLIENT_EDEFAULT;
            case CXFPackage.CXF_DATA_MODEL__GENERATE_SERVER:
                return generateServer != GENERATE_SERVER_EDEFAULT;
            case CXFPackage.CXF_DATA_MODEL__DATABINDING:
                return databinding != DATABINDING_EDEFAULT;
            case CXFPackage.CXF_DATA_MODEL__FRONTEND:
                return frontend != FRONTEND_EDEFAULT;
            case CXFPackage.CXF_DATA_MODEL__USE_SPRING_APPLICATION_CONTEXT:
                return useSpringApplicationContext != USE_SPRING_APPLICATION_CONTEXT_EDEFAULT;
            case CXFPackage.CXF_DATA_MODEL__EXPORT_CXF_CLASSPATH_CONTAINER:
                return exportCXFClasspathContainer != EXPORT_CXF_CLASSPATH_CONTAINER_EDEFAULT;
            case CXFPackage.CXF_DATA_MODEL__INSTALLATIONS:
                return installations != null;
            case CXFPackage.CXF_DATA_MODEL__PROJECT_NAME:
                return PROJECT_NAME_EDEFAULT == null ? projectName != null : !PROJECT_NAME_EDEFAULT.equals(projectName);
            case CXFPackage.CXF_DATA_MODEL__RESOURCE_DIRECTORY:
                return RESOURCE_DIRECTORY_EDEFAULT == null ? resourceDirectory != null : !RESOURCE_DIRECTORY_EDEFAULT.equals(resourceDirectory);
            case CXFPackage.CXF_DATA_MODEL__CLASS_DIRECTORY:
                return CLASS_DIRECTORY_EDEFAULT == null ? classDirectory != null : !CLASS_DIRECTORY_EDEFAULT.equals(classDirectory);
            case CXFPackage.CXF_DATA_MODEL__WSDL_FILE_NAME:
                return WSDL_FILE_NAME_EDEFAULT == null ? wsdlFileName != null : !WSDL_FILE_NAME_EDEFAULT.equals(wsdlFileName);
            case CXFPackage.CXF_DATA_MODEL__WSDL_URL:
                return WSDL_URL_EDEFAULT == null ? wsdlURL != null : !WSDL_URL_EDEFAULT.equals(wsdlURL);
            case CXFPackage.CXF_DATA_MODEL__CONFIG_WSDL_LOCATION:
                return CONFIG_WSDL_LOCATION_EDEFAULT == null ? configWsdlLocation != null : !CONFIG_WSDL_LOCATION_EDEFAULT.equals(configWsdlLocation);
            case CXFPackage.CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME:
                return FULLY_QUALIFIED_JAVA_CLASS_NAME_EDEFAULT == null ? fullyQualifiedJavaClassName != null : !FULLY_QUALIFIED_JAVA_CLASS_NAME_EDEFAULT.equals(fullyQualifiedJavaClassName);
            case CXFPackage.CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME:
                return FULLY_QUALIFIED_JAVA_INTERFACE_NAME_EDEFAULT == null ? fullyQualifiedJavaInterfaceName != null : !FULLY_QUALIFIED_JAVA_INTERFACE_NAME_EDEFAULT.equals(fullyQualifiedJavaInterfaceName);
            case CXFPackage.CXF_DATA_MODEL__CONFIG_ID:
                return CONFIG_ID_EDEFAULT == null ? configId != null : !CONFIG_ID_EDEFAULT.equals(configId);
            case CXFPackage.CXF_DATA_MODEL__TARGET_NAMESPACE:
                return TARGET_NAMESPACE_EDEFAULT == null ? targetNamespace != null : !TARGET_NAMESPACE_EDEFAULT.equals(targetNamespace);
            case CXFPackage.CXF_DATA_MODEL__ENDPOINT_NAME:
                return ENDPOINT_NAME_EDEFAULT == null ? endpointName != null : !ENDPOINT_NAME_EDEFAULT.equals(endpointName);
            case CXFPackage.CXF_DATA_MODEL__SERVICE_NAME:
                return SERVICE_NAME_EDEFAULT == null ? serviceName != null : !SERVICE_NAME_EDEFAULT.equals(serviceName);
            case CXFPackage.CXF_DATA_MODEL__WSDL_DEFINITION:
                return WSDL_DEFINITION_EDEFAULT == null ? wsdlDefinition != null : !WSDL_DEFINITION_EDEFAULT.equals(wsdlDefinition);
            case CXFPackage.CXF_DATA_MODEL__WSDL_LOCATION:
                return WSDL_LOCATION_EDEFAULT == null ? wsdlLocation != null : !WSDL_LOCATION_EDEFAULT.equals(wsdlLocation);
        }
        return super.eIsSet(featureID);
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
        result.append(" (defaultRuntimeLocation: ");
        result.append(defaultRuntimeLocation);
        result.append(", defaultRuntimeType: ");
        result.append(defaultRuntimeType);
        result.append(", defaultRuntimeVersion: ");
        result.append(defaultRuntimeVersion);
        result.append(", verbose: ");
        result.append(verbose);
        result.append(", generateAntBuildFile: ");
        result.append(generateAntBuildFile);
        result.append(", generateClient: ");
        result.append(generateClient);
        result.append(", generateServer: ");
        result.append(generateServer);
        result.append(", databinding: ");
        result.append(databinding);
        result.append(", frontend: ");
        result.append(frontend);
        result.append(", useSpringApplicationContext: ");
        result.append(useSpringApplicationContext);
        result.append(", exportCXFClasspathContainer: ");
        result.append(exportCXFClasspathContainer);
        result.append(", installations: ");
        result.append(installations);
        result.append(", projectName: ");
        result.append(projectName);
        result.append(", resourceDirectory: ");
        result.append(resourceDirectory);
        result.append(", classDirectory: ");
        result.append(classDirectory);
        result.append(", wsdlFileName: ");
        result.append(wsdlFileName);
        result.append(", wsdlURL: ");
        result.append(wsdlURL);
        result.append(", configWsdlLocation: ");
        result.append(configWsdlLocation);
        result.append(", fullyQualifiedJavaClassName: ");
        result.append(fullyQualifiedJavaClassName);
        result.append(", fullyQualifiedJavaInterfaceName: ");
        result.append(fullyQualifiedJavaInterfaceName);
        result.append(", configId: ");
        result.append(configId);
        result.append(", targetNamespace: ");
        result.append(targetNamespace);
        result.append(", endpointName: ");
        result.append(endpointName);
        result.append(", serviceName: ");
        result.append(serviceName);
        result.append(", wsdlDefinition: ");
        result.append(wsdlDefinition);
        result.append(", wsdlLocation: ");
        result.append(wsdlLocation);
        result.append(')');
        return result.toString();
    }

} //CXFDataModelImpl
