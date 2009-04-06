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
 * $Id: WSDL2JavaDataModelImpl.java,v 1.2 2009/04/06 21:33:14 sclarke Exp $
 */
package org.eclipse.jst.ws.internal.cxf.core.model.impl;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>WSDL2 Java Data Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isGenerateImplementation <em>Generate Implementation</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isProcessSOAPHeaders <em>Process SOAP Headers</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isLoadDefaultNamespacePackageNameMapping <em>Load Default Namespace Package Name Mapping</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isLoadDefaultExcludesNamepsaceMapping <em>Load Default Excludes Namepsace Mapping</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isValidate <em>Validate</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#getWsdlVersion <em>Wsdl Version</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isUseDefaultValues <em>Use Default Values</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#getXjcArgs <em>Xjc Args</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isNoAddressBinding <em>No Address Binding</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isXjcUseDefaultValues <em>Xjc Use Default Values</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isXjcToString <em>Xjc To String</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isXjcToStringMultiLine <em>Xjc To String Multi Line</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isXjcToStringSimple <em>Xjc To String Simple</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isXjcLocator <em>Xjc Locator</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isXjcSyncMethods <em>Xjc Sync Methods</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isXjcMarkGenerated <em>Xjc Mark Generated</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#getXjcEpisodeFile <em>Xjc Episode File</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#isAutoNameResolution <em>Auto Name Resolution</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#getIncludedNamespaces <em>Included Namespaces</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#getBindingFiles <em>Binding Files</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#getExcludedNamespaces <em>Excluded Namespaces</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#getCatalogFile <em>Catalog File</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.impl.WSDL2JavaDataModelImpl#getJavaSourceFolder <em>Java Source Folder</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WSDL2JavaDataModelImpl extends CXFDataModelImpl implements WSDL2JavaDataModel {
    /**
     * The default value of the '{@link #isGenerateImplementation() <em>Generate Implementation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateImplementation()
     * @generated
     * @ordered
     */
    protected static final boolean GENERATE_IMPLEMENTATION_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isGenerateImplementation() <em>Generate Implementation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isGenerateImplementation()
     * @generated
     * @ordered
     */
    protected boolean generateImplementation = GENERATE_IMPLEMENTATION_EDEFAULT;

    /**
     * The default value of the '{@link #isProcessSOAPHeaders() <em>Process SOAP Headers</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isProcessSOAPHeaders()
     * @generated
     * @ordered
     */
    protected static final boolean PROCESS_SOAP_HEADERS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isProcessSOAPHeaders() <em>Process SOAP Headers</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isProcessSOAPHeaders()
     * @generated
     * @ordered
     */
    protected boolean processSOAPHeaders = PROCESS_SOAP_HEADERS_EDEFAULT;

    /**
     * The default value of the '{@link #isLoadDefaultNamespacePackageNameMapping() <em>Load Default Namespace Package Name Mapping</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLoadDefaultNamespacePackageNameMapping()
     * @generated
     * @ordered
     */
    protected static final boolean LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isLoadDefaultNamespacePackageNameMapping() <em>Load Default Namespace Package Name Mapping</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLoadDefaultNamespacePackageNameMapping()
     * @generated
     * @ordered
     */
    protected boolean loadDefaultNamespacePackageNameMapping = LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING_EDEFAULT;

    /**
     * The default value of the '{@link #isLoadDefaultExcludesNamepsaceMapping() <em>Load Default Excludes Namepsace Mapping</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLoadDefaultExcludesNamepsaceMapping()
     * @generated
     * @ordered
     */
    protected static final boolean LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isLoadDefaultExcludesNamepsaceMapping() <em>Load Default Excludes Namepsace Mapping</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isLoadDefaultExcludesNamepsaceMapping()
     * @generated
     * @ordered
     */
    protected boolean loadDefaultExcludesNamepsaceMapping = LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING_EDEFAULT;

    /**
     * The default value of the '{@link #isValidate() <em>Validate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isValidate()
     * @generated
     * @ordered
     */
    protected static final boolean VALIDATE_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isValidate() <em>Validate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isValidate()
     * @generated
     * @ordered
     */
    protected boolean validate = VALIDATE_EDEFAULT;

    /**
     * The default value of the '{@link #getWsdlVersion() <em>Wsdl Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWsdlVersion()
     * @generated
     * @ordered
     */
    protected static final String WSDL_VERSION_EDEFAULT = "1.1";

    /**
     * The cached value of the '{@link #getWsdlVersion() <em>Wsdl Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getWsdlVersion()
     * @generated
     * @ordered
     */
    protected String wsdlVersion = WSDL_VERSION_EDEFAULT;

    /**
     * The default value of the '{@link #isUseDefaultValues() <em>Use Default Values</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseDefaultValues()
     * @generated
     * @ordered
     */
    protected static final boolean USE_DEFAULT_VALUES_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isUseDefaultValues() <em>Use Default Values</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isUseDefaultValues()
     * @generated
     * @ordered
     */
    protected boolean useDefaultValues = USE_DEFAULT_VALUES_EDEFAULT;

    /**
     * The default value of the '{@link #getXjcArgs() <em>Xjc Args</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXjcArgs()
     * @generated
     * @ordered
     */
    protected static final String XJC_ARGS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getXjcArgs() <em>Xjc Args</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXjcArgs()
     * @generated
     * @ordered
     */
    protected String xjcArgs = XJC_ARGS_EDEFAULT;

    /**
     * The default value of the '{@link #isNoAddressBinding() <em>No Address Binding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isNoAddressBinding()
     * @generated
     * @ordered
     */
    protected static final boolean NO_ADDRESS_BINDING_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isNoAddressBinding() <em>No Address Binding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isNoAddressBinding()
     * @generated
     * @ordered
     */
    protected boolean noAddressBinding = NO_ADDRESS_BINDING_EDEFAULT;

    /**
     * The default value of the '{@link #isXjcUseDefaultValues() <em>Xjc Use Default Values</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcUseDefaultValues()
     * @generated
     * @ordered
     */
    protected static final boolean XJC_USE_DEFAULT_VALUES_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isXjcUseDefaultValues() <em>Xjc Use Default Values</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcUseDefaultValues()
     * @generated
     * @ordered
     */
    protected boolean xjcUseDefaultValues = XJC_USE_DEFAULT_VALUES_EDEFAULT;

    /**
     * The default value of the '{@link #isXjcToString() <em>Xjc To String</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcToString()
     * @generated
     * @ordered
     */
    protected static final boolean XJC_TO_STRING_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isXjcToString() <em>Xjc To String</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcToString()
     * @generated
     * @ordered
     */
    protected boolean xjcToString = XJC_TO_STRING_EDEFAULT;

    /**
     * The default value of the '{@link #isXjcToStringMultiLine() <em>Xjc To String Multi Line</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcToStringMultiLine()
     * @generated
     * @ordered
     */
    protected static final boolean XJC_TO_STRING_MULTI_LINE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isXjcToStringMultiLine() <em>Xjc To String Multi Line</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcToStringMultiLine()
     * @generated
     * @ordered
     */
    protected boolean xjcToStringMultiLine = XJC_TO_STRING_MULTI_LINE_EDEFAULT;

    /**
     * The default value of the '{@link #isXjcToStringSimple() <em>Xjc To String Simple</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcToStringSimple()
     * @generated
     * @ordered
     */
    protected static final boolean XJC_TO_STRING_SIMPLE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isXjcToStringSimple() <em>Xjc To String Simple</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcToStringSimple()
     * @generated
     * @ordered
     */
    protected boolean xjcToStringSimple = XJC_TO_STRING_SIMPLE_EDEFAULT;

    /**
     * The default value of the '{@link #isXjcLocator() <em>Xjc Locator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcLocator()
     * @generated
     * @ordered
     */
    protected static final boolean XJC_LOCATOR_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isXjcLocator() <em>Xjc Locator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcLocator()
     * @generated
     * @ordered
     */
    protected boolean xjcLocator = XJC_LOCATOR_EDEFAULT;

    /**
     * The default value of the '{@link #isXjcSyncMethods() <em>Xjc Sync Methods</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcSyncMethods()
     * @generated
     * @ordered
     */
    protected static final boolean XJC_SYNC_METHODS_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isXjcSyncMethods() <em>Xjc Sync Methods</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcSyncMethods()
     * @generated
     * @ordered
     */
    protected boolean xjcSyncMethods = XJC_SYNC_METHODS_EDEFAULT;

    /**
     * The default value of the '{@link #isXjcMarkGenerated() <em>Xjc Mark Generated</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcMarkGenerated()
     * @generated
     * @ordered
     */
    protected static final boolean XJC_MARK_GENERATED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isXjcMarkGenerated() <em>Xjc Mark Generated</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isXjcMarkGenerated()
     * @generated
     * @ordered
     */
    protected boolean xjcMarkGenerated = XJC_MARK_GENERATED_EDEFAULT;

    /**
     * The default value of the '{@link #getXjcEpisodeFile() <em>Xjc Episode File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXjcEpisodeFile()
     * @generated
     * @ordered
     */
    protected static final String XJC_EPISODE_FILE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getXjcEpisodeFile() <em>Xjc Episode File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getXjcEpisodeFile()
     * @generated
     * @ordered
     */
    protected String xjcEpisodeFile = XJC_EPISODE_FILE_EDEFAULT;

    /**
     * The default value of the '{@link #isAutoNameResolution() <em>Auto Name Resolution</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAutoNameResolution()
     * @generated
     * @ordered
     */
    protected static final boolean AUTO_NAME_RESOLUTION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isAutoNameResolution() <em>Auto Name Resolution</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isAutoNameResolution()
     * @generated
     * @ordered
     */
    protected boolean autoNameResolution = AUTO_NAME_RESOLUTION_EDEFAULT;

    /**
     * The cached value of the '{@link #getIncludedNamespaces() <em>Included Namespaces</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getIncludedNamespaces()
     * @generated
     * @ordered
     */
    protected Map<String, String> includedNamespaces;

    /**
     * The cached value of the '{@link #getBindingFiles() <em>Binding Files</em>}' attribute list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getBindingFiles()
     * @generated
     * @ordered
     */
    protected EList<String> bindingFiles;

    /**
     * The cached value of the '{@link #getExcludedNamespaces() <em>Excluded Namespaces</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getExcludedNamespaces()
     * @generated
     * @ordered
     */
    protected Map<String, String> excludedNamespaces;

    /**
     * The default value of the '{@link #getCatalogFile() <em>Catalog File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCatalogFile()
     * @generated
     * @ordered
     */
    protected static final String CATALOG_FILE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getCatalogFile() <em>Catalog File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getCatalogFile()
     * @generated
     * @ordered
     */
    protected String catalogFile = CATALOG_FILE_EDEFAULT;

    /**
     * The default value of the '{@link #getJavaSourceFolder() <em>Java Source Folder</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJavaSourceFolder()
     * @generated
     * @ordered
     */
    protected static final String JAVA_SOURCE_FOLDER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getJavaSourceFolder() <em>Java Source Folder</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getJavaSourceFolder()
     * @generated
     * @ordered
     */
    protected String javaSourceFolder = JAVA_SOURCE_FOLDER_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected WSDL2JavaDataModelImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return CXFPackage.Literals.WSDL2_JAVA_DATA_MODEL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isGenerateImplementation() {
        return generateImplementation;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setGenerateImplementation(boolean newGenerateImplementation) {
        boolean oldGenerateImplementation = generateImplementation;
        generateImplementation = newGenerateImplementation;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__GENERATE_IMPLEMENTATION, oldGenerateImplementation, generateImplementation));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isProcessSOAPHeaders() {
        return processSOAPHeaders;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProcessSOAPHeaders(boolean newProcessSOAPHeaders) {
        boolean oldProcessSOAPHeaders = processSOAPHeaders;
        processSOAPHeaders = newProcessSOAPHeaders;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__PROCESS_SOAP_HEADERS, oldProcessSOAPHeaders, processSOAPHeaders));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isLoadDefaultNamespacePackageNameMapping() {
        return loadDefaultNamespacePackageNameMapping;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLoadDefaultNamespacePackageNameMapping(boolean newLoadDefaultNamespacePackageNameMapping) {
        boolean oldLoadDefaultNamespacePackageNameMapping = loadDefaultNamespacePackageNameMapping;
        loadDefaultNamespacePackageNameMapping = newLoadDefaultNamespacePackageNameMapping;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING, oldLoadDefaultNamespacePackageNameMapping, loadDefaultNamespacePackageNameMapping));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isLoadDefaultExcludesNamepsaceMapping() {
        return loadDefaultExcludesNamepsaceMapping;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLoadDefaultExcludesNamepsaceMapping(boolean newLoadDefaultExcludesNamepsaceMapping) {
        boolean oldLoadDefaultExcludesNamepsaceMapping = loadDefaultExcludesNamepsaceMapping;
        loadDefaultExcludesNamepsaceMapping = newLoadDefaultExcludesNamepsaceMapping;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING, oldLoadDefaultExcludesNamepsaceMapping, loadDefaultExcludesNamepsaceMapping));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isValidate() {
        return validate;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setValidate(boolean newValidate) {
        boolean oldValidate = validate;
        validate = newValidate;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__VALIDATE, oldValidate, validate));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getWsdlVersion() {
        return wsdlVersion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setWsdlVersion(String newWsdlVersion) {
        String oldWsdlVersion = wsdlVersion;
        wsdlVersion = newWsdlVersion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__WSDL_VERSION, oldWsdlVersion, wsdlVersion));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isUseDefaultValues() {
        return useDefaultValues;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUseDefaultValues(boolean newUseDefaultValues) {
        boolean oldUseDefaultValues = useDefaultValues;
        useDefaultValues = newUseDefaultValues;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__USE_DEFAULT_VALUES, oldUseDefaultValues, useDefaultValues));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getXjcArgs() {
        return xjcArgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXjcArgs(String newXjcArgs) {
        String oldXjcArgs = xjcArgs;
        xjcArgs = newXjcArgs;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_ARGS, oldXjcArgs, xjcArgs));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isNoAddressBinding() {
        return noAddressBinding;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNoAddressBinding(boolean newNoAddressBinding) {
        boolean oldNoAddressBinding = noAddressBinding;
        noAddressBinding = newNoAddressBinding;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__NO_ADDRESS_BINDING, oldNoAddressBinding, noAddressBinding));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isXjcUseDefaultValues() {
        return xjcUseDefaultValues;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXjcUseDefaultValues(boolean newXjcUseDefaultValues) {
        boolean oldXjcUseDefaultValues = xjcUseDefaultValues;
        xjcUseDefaultValues = newXjcUseDefaultValues;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_USE_DEFAULT_VALUES, oldXjcUseDefaultValues, xjcUseDefaultValues));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isXjcToString() {
        return xjcToString;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXjcToString(boolean newXjcToString) {
        boolean oldXjcToString = xjcToString;
        xjcToString = newXjcToString;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING, oldXjcToString, xjcToString));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isXjcToStringMultiLine() {
        return xjcToStringMultiLine;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXjcToStringMultiLine(boolean newXjcToStringMultiLine) {
        boolean oldXjcToStringMultiLine = xjcToStringMultiLine;
        xjcToStringMultiLine = newXjcToStringMultiLine;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_MULTI_LINE, oldXjcToStringMultiLine, xjcToStringMultiLine));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isXjcToStringSimple() {
        return xjcToStringSimple;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXjcToStringSimple(boolean newXjcToStringSimple) {
        boolean oldXjcToStringSimple = xjcToStringSimple;
        xjcToStringSimple = newXjcToStringSimple;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_SIMPLE, oldXjcToStringSimple, xjcToStringSimple));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isXjcLocator() {
        return xjcLocator;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXjcLocator(boolean newXjcLocator) {
        boolean oldXjcLocator = xjcLocator;
        xjcLocator = newXjcLocator;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_LOCATOR, oldXjcLocator, xjcLocator));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isXjcSyncMethods() {
        return xjcSyncMethods;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXjcSyncMethods(boolean newXjcSyncMethods) {
        boolean oldXjcSyncMethods = xjcSyncMethods;
        xjcSyncMethods = newXjcSyncMethods;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_SYNC_METHODS, oldXjcSyncMethods, xjcSyncMethods));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isXjcMarkGenerated() {
        return xjcMarkGenerated;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXjcMarkGenerated(boolean newXjcMarkGenerated) {
        boolean oldXjcMarkGenerated = xjcMarkGenerated;
        xjcMarkGenerated = newXjcMarkGenerated;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_MARK_GENERATED, oldXjcMarkGenerated, xjcMarkGenerated));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getXjcEpisodeFile() {
        return xjcEpisodeFile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setXjcEpisodeFile(String newXjcEpisodeFile) {
        String oldXjcEpisodeFile = xjcEpisodeFile;
        xjcEpisodeFile = newXjcEpisodeFile;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_EPISODE_FILE, oldXjcEpisodeFile, xjcEpisodeFile));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isAutoNameResolution() {
        return autoNameResolution;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setAutoNameResolution(boolean newAutoNameResolution) {
        boolean oldAutoNameResolution = autoNameResolution;
        autoNameResolution = newAutoNameResolution;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__AUTO_NAME_RESOLUTION, oldAutoNameResolution, autoNameResolution));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Map<String, String> getIncludedNamespaces() {
        return includedNamespaces;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setIncludedNamespaces(Map<String, String> newIncludedNamespaces) {
        Map<String, String> oldIncludedNamespaces = includedNamespaces;
        includedNamespaces = newIncludedNamespaces;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__INCLUDED_NAMESPACES, oldIncludedNamespaces, includedNamespaces));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<String> getBindingFiles() {
        if (bindingFiles == null) {
            bindingFiles = new EDataTypeUniqueEList<String>(String.class, this, CXFPackage.WSDL2_JAVA_DATA_MODEL__BINDING_FILES);
        }
        return bindingFiles;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Map<String, String> getExcludedNamespaces() {
        return excludedNamespaces;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setExcludedNamespaces(Map<String, String> newExcludedNamespaces) {
        Map<String, String> oldExcludedNamespaces = excludedNamespaces;
        excludedNamespaces = newExcludedNamespaces;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__EXCLUDED_NAMESPACES, oldExcludedNamespaces, excludedNamespaces));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getCatalogFile() {
        return catalogFile;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setCatalogFile(String newCatalogFile) {
        String oldCatalogFile = catalogFile;
        catalogFile = newCatalogFile;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__CATALOG_FILE, oldCatalogFile, catalogFile));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getJavaSourceFolder() {
        return javaSourceFolder;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setJavaSourceFolder(String newJavaSourceFolder) {
        String oldJavaSourceFolder = javaSourceFolder;
        javaSourceFolder = newJavaSourceFolder;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, CXFPackage.WSDL2_JAVA_DATA_MODEL__JAVA_SOURCE_FOLDER, oldJavaSourceFolder, javaSourceFolder));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__GENERATE_IMPLEMENTATION:
                return isGenerateImplementation() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__PROCESS_SOAP_HEADERS:
                return isProcessSOAPHeaders() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING:
                return isLoadDefaultNamespacePackageNameMapping() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING:
                return isLoadDefaultExcludesNamepsaceMapping() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__VALIDATE:
                return isValidate() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__WSDL_VERSION:
                return getWsdlVersion();
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__USE_DEFAULT_VALUES:
                return isUseDefaultValues() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_ARGS:
                return getXjcArgs();
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__NO_ADDRESS_BINDING:
                return isNoAddressBinding() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_USE_DEFAULT_VALUES:
                return isXjcUseDefaultValues() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING:
                return isXjcToString() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_MULTI_LINE:
                return isXjcToStringMultiLine() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_SIMPLE:
                return isXjcToStringSimple() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_LOCATOR:
                return isXjcLocator() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_SYNC_METHODS:
                return isXjcSyncMethods() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_MARK_GENERATED:
                return isXjcMarkGenerated() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_EPISODE_FILE:
                return getXjcEpisodeFile();
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__AUTO_NAME_RESOLUTION:
                return isAutoNameResolution() ? Boolean.TRUE : Boolean.FALSE;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__INCLUDED_NAMESPACES:
                return getIncludedNamespaces();
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__BINDING_FILES:
                return getBindingFiles();
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__EXCLUDED_NAMESPACES:
                return getExcludedNamespaces();
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__CATALOG_FILE:
                return getCatalogFile();
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__JAVA_SOURCE_FOLDER:
                return getJavaSourceFolder();
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
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__GENERATE_IMPLEMENTATION:
                setGenerateImplementation(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__PROCESS_SOAP_HEADERS:
                setProcessSOAPHeaders(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING:
                setLoadDefaultNamespacePackageNameMapping(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING:
                setLoadDefaultExcludesNamepsaceMapping(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__VALIDATE:
                setValidate(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__WSDL_VERSION:
                setWsdlVersion((String)newValue);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__USE_DEFAULT_VALUES:
                setUseDefaultValues(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_ARGS:
                setXjcArgs((String)newValue);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__NO_ADDRESS_BINDING:
                setNoAddressBinding(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_USE_DEFAULT_VALUES:
                setXjcUseDefaultValues(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING:
                setXjcToString(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_MULTI_LINE:
                setXjcToStringMultiLine(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_SIMPLE:
                setXjcToStringSimple(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_LOCATOR:
                setXjcLocator(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_SYNC_METHODS:
                setXjcSyncMethods(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_MARK_GENERATED:
                setXjcMarkGenerated(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_EPISODE_FILE:
                setXjcEpisodeFile((String)newValue);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__AUTO_NAME_RESOLUTION:
                setAutoNameResolution(((Boolean)newValue).booleanValue());
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__INCLUDED_NAMESPACES:
                setIncludedNamespaces((Map<String, String>)newValue);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__BINDING_FILES:
                getBindingFiles().clear();
                getBindingFiles().addAll((Collection<? extends String>)newValue);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__EXCLUDED_NAMESPACES:
                setExcludedNamespaces((Map<String, String>)newValue);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__CATALOG_FILE:
                setCatalogFile((String)newValue);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__JAVA_SOURCE_FOLDER:
                setJavaSourceFolder((String)newValue);
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
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__GENERATE_IMPLEMENTATION:
                setGenerateImplementation(GENERATE_IMPLEMENTATION_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__PROCESS_SOAP_HEADERS:
                setProcessSOAPHeaders(PROCESS_SOAP_HEADERS_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING:
                setLoadDefaultNamespacePackageNameMapping(LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING:
                setLoadDefaultExcludesNamepsaceMapping(LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__VALIDATE:
                setValidate(VALIDATE_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__WSDL_VERSION:
                setWsdlVersion(WSDL_VERSION_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__USE_DEFAULT_VALUES:
                setUseDefaultValues(USE_DEFAULT_VALUES_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_ARGS:
                setXjcArgs(XJC_ARGS_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__NO_ADDRESS_BINDING:
                setNoAddressBinding(NO_ADDRESS_BINDING_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_USE_DEFAULT_VALUES:
                setXjcUseDefaultValues(XJC_USE_DEFAULT_VALUES_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING:
                setXjcToString(XJC_TO_STRING_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_MULTI_LINE:
                setXjcToStringMultiLine(XJC_TO_STRING_MULTI_LINE_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_SIMPLE:
                setXjcToStringSimple(XJC_TO_STRING_SIMPLE_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_LOCATOR:
                setXjcLocator(XJC_LOCATOR_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_SYNC_METHODS:
                setXjcSyncMethods(XJC_SYNC_METHODS_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_MARK_GENERATED:
                setXjcMarkGenerated(XJC_MARK_GENERATED_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_EPISODE_FILE:
                setXjcEpisodeFile(XJC_EPISODE_FILE_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__AUTO_NAME_RESOLUTION:
                setAutoNameResolution(AUTO_NAME_RESOLUTION_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__INCLUDED_NAMESPACES:
                setIncludedNamespaces((Map<String, String>)null);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__BINDING_FILES:
                getBindingFiles().clear();
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__EXCLUDED_NAMESPACES:
                setExcludedNamespaces((Map<String, String>)null);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__CATALOG_FILE:
                setCatalogFile(CATALOG_FILE_EDEFAULT);
                return;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__JAVA_SOURCE_FOLDER:
                setJavaSourceFolder(JAVA_SOURCE_FOLDER_EDEFAULT);
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
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__GENERATE_IMPLEMENTATION:
                return generateImplementation != GENERATE_IMPLEMENTATION_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__PROCESS_SOAP_HEADERS:
                return processSOAPHeaders != PROCESS_SOAP_HEADERS_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING:
                return loadDefaultNamespacePackageNameMapping != LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING:
                return loadDefaultExcludesNamepsaceMapping != LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__VALIDATE:
                return validate != VALIDATE_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__WSDL_VERSION:
                return WSDL_VERSION_EDEFAULT == null ? wsdlVersion != null : !WSDL_VERSION_EDEFAULT.equals(wsdlVersion);
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__USE_DEFAULT_VALUES:
                return useDefaultValues != USE_DEFAULT_VALUES_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_ARGS:
                return XJC_ARGS_EDEFAULT == null ? xjcArgs != null : !XJC_ARGS_EDEFAULT.equals(xjcArgs);
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__NO_ADDRESS_BINDING:
                return noAddressBinding != NO_ADDRESS_BINDING_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_USE_DEFAULT_VALUES:
                return xjcUseDefaultValues != XJC_USE_DEFAULT_VALUES_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING:
                return xjcToString != XJC_TO_STRING_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_MULTI_LINE:
                return xjcToStringMultiLine != XJC_TO_STRING_MULTI_LINE_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_SIMPLE:
                return xjcToStringSimple != XJC_TO_STRING_SIMPLE_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_LOCATOR:
                return xjcLocator != XJC_LOCATOR_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_SYNC_METHODS:
                return xjcSyncMethods != XJC_SYNC_METHODS_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_MARK_GENERATED:
                return xjcMarkGenerated != XJC_MARK_GENERATED_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_EPISODE_FILE:
                return XJC_EPISODE_FILE_EDEFAULT == null ? xjcEpisodeFile != null : !XJC_EPISODE_FILE_EDEFAULT.equals(xjcEpisodeFile);
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__AUTO_NAME_RESOLUTION:
                return autoNameResolution != AUTO_NAME_RESOLUTION_EDEFAULT;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__INCLUDED_NAMESPACES:
                return includedNamespaces != null;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__BINDING_FILES:
                return bindingFiles != null && !bindingFiles.isEmpty();
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__EXCLUDED_NAMESPACES:
                return excludedNamespaces != null;
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__CATALOG_FILE:
                return CATALOG_FILE_EDEFAULT == null ? catalogFile != null : !CATALOG_FILE_EDEFAULT.equals(catalogFile);
            case CXFPackage.WSDL2_JAVA_DATA_MODEL__JAVA_SOURCE_FOLDER:
                return JAVA_SOURCE_FOLDER_EDEFAULT == null ? javaSourceFolder != null : !JAVA_SOURCE_FOLDER_EDEFAULT.equals(javaSourceFolder);
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
        if (baseClass == WSDL2JavaContext.class) {
            switch (derivedFeatureID) {
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__GENERATE_IMPLEMENTATION: return CXFPackage.WSDL2_JAVA_CONTEXT__GENERATE_IMPLEMENTATION;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__PROCESS_SOAP_HEADERS: return CXFPackage.WSDL2_JAVA_CONTEXT__PROCESS_SOAP_HEADERS;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING: return CXFPackage.WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING: return CXFPackage.WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__VALIDATE: return CXFPackage.WSDL2_JAVA_CONTEXT__VALIDATE;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__WSDL_VERSION: return CXFPackage.WSDL2_JAVA_CONTEXT__WSDL_VERSION;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__USE_DEFAULT_VALUES: return CXFPackage.WSDL2_JAVA_CONTEXT__USE_DEFAULT_VALUES;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_ARGS: return CXFPackage.WSDL2_JAVA_CONTEXT__XJC_ARGS;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__NO_ADDRESS_BINDING: return CXFPackage.WSDL2_JAVA_CONTEXT__NO_ADDRESS_BINDING;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_USE_DEFAULT_VALUES: return CXFPackage.WSDL2_JAVA_CONTEXT__XJC_USE_DEFAULT_VALUES;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING: return CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_MULTI_LINE: return CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING_MULTI_LINE;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_SIMPLE: return CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING_SIMPLE;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_LOCATOR: return CXFPackage.WSDL2_JAVA_CONTEXT__XJC_LOCATOR;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_SYNC_METHODS: return CXFPackage.WSDL2_JAVA_CONTEXT__XJC_SYNC_METHODS;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_MARK_GENERATED: return CXFPackage.WSDL2_JAVA_CONTEXT__XJC_MARK_GENERATED;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_EPISODE_FILE: return CXFPackage.WSDL2_JAVA_CONTEXT__XJC_EPISODE_FILE;
                case CXFPackage.WSDL2_JAVA_DATA_MODEL__AUTO_NAME_RESOLUTION: return CXFPackage.WSDL2_JAVA_CONTEXT__AUTO_NAME_RESOLUTION;
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
        if (baseClass == WSDL2JavaContext.class) {
            switch (baseFeatureID) {
                case CXFPackage.WSDL2_JAVA_CONTEXT__GENERATE_IMPLEMENTATION: return CXFPackage.WSDL2_JAVA_DATA_MODEL__GENERATE_IMPLEMENTATION;
                case CXFPackage.WSDL2_JAVA_CONTEXT__PROCESS_SOAP_HEADERS: return CXFPackage.WSDL2_JAVA_DATA_MODEL__PROCESS_SOAP_HEADERS;
                case CXFPackage.WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING: return CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING;
                case CXFPackage.WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING: return CXFPackage.WSDL2_JAVA_DATA_MODEL__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING;
                case CXFPackage.WSDL2_JAVA_CONTEXT__VALIDATE: return CXFPackage.WSDL2_JAVA_DATA_MODEL__VALIDATE;
                case CXFPackage.WSDL2_JAVA_CONTEXT__WSDL_VERSION: return CXFPackage.WSDL2_JAVA_DATA_MODEL__WSDL_VERSION;
                case CXFPackage.WSDL2_JAVA_CONTEXT__USE_DEFAULT_VALUES: return CXFPackage.WSDL2_JAVA_DATA_MODEL__USE_DEFAULT_VALUES;
                case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_ARGS: return CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_ARGS;
                case CXFPackage.WSDL2_JAVA_CONTEXT__NO_ADDRESS_BINDING: return CXFPackage.WSDL2_JAVA_DATA_MODEL__NO_ADDRESS_BINDING;
                case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_USE_DEFAULT_VALUES: return CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_USE_DEFAULT_VALUES;
                case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING: return CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING;
                case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING_MULTI_LINE: return CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_MULTI_LINE;
                case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING_SIMPLE: return CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_TO_STRING_SIMPLE;
                case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_LOCATOR: return CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_LOCATOR;
                case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_SYNC_METHODS: return CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_SYNC_METHODS;
                case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_MARK_GENERATED: return CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_MARK_GENERATED;
                case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_EPISODE_FILE: return CXFPackage.WSDL2_JAVA_DATA_MODEL__XJC_EPISODE_FILE;
                case CXFPackage.WSDL2_JAVA_CONTEXT__AUTO_NAME_RESOLUTION: return CXFPackage.WSDL2_JAVA_DATA_MODEL__AUTO_NAME_RESOLUTION;
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
        result.append(" (generateImplementation: ");
        result.append(generateImplementation);
        result.append(", processSOAPHeaders: ");
        result.append(processSOAPHeaders);
        result.append(", loadDefaultNamespacePackageNameMapping: ");
        result.append(loadDefaultNamespacePackageNameMapping);
        result.append(", loadDefaultExcludesNamepsaceMapping: ");
        result.append(loadDefaultExcludesNamepsaceMapping);
        result.append(", validate: ");
        result.append(validate);
        result.append(", wsdlVersion: ");
        result.append(wsdlVersion);
        result.append(", useDefaultValues: ");
        result.append(useDefaultValues);
        result.append(", xjcArgs: ");
        result.append(xjcArgs);
        result.append(", noAddressBinding: ");
        result.append(noAddressBinding);
        result.append(", xjcUseDefaultValues: ");
        result.append(xjcUseDefaultValues);
        result.append(", xjcToString: ");
        result.append(xjcToString);
        result.append(", xjcToStringMultiLine: ");
        result.append(xjcToStringMultiLine);
        result.append(", xjcToStringSimple: ");
        result.append(xjcToStringSimple);
        result.append(", xjcLocator: ");
        result.append(xjcLocator);
        result.append(", xjcSyncMethods: ");
        result.append(xjcSyncMethods);
        result.append(", xjcMarkGenerated: ");
        result.append(xjcMarkGenerated);
        result.append(", xjcEpisodeFile: ");
        result.append(xjcEpisodeFile);
        result.append(", autoNameResolution: ");
        result.append(autoNameResolution);
        result.append(", includedNamespaces: ");
        result.append(includedNamespaces);
        result.append(", bindingFiles: ");
        result.append(bindingFiles);
        result.append(", excludedNamespaces: ");
        result.append(excludedNamespaces);
        result.append(", catalogFile: ");
        result.append(catalogFile);
        result.append(", javaSourceFolder: ");
        result.append(javaSourceFolder);
        result.append(')');
        return result.toString();
    }

} //WSDL2JavaDataModelImpl
