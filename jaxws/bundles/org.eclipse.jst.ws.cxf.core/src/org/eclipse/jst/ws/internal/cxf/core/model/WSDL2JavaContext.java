/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.cxf.core.model;

/**
 * @author sclarke
 * 
 * @model abstract="true" interface="true"
 */
public interface WSDL2JavaContext extends CXFContext {

    /**
     * Returns whether to generate starting point code for an implementation object.
     * 
     * @model default="true"
     */
    boolean isGenerateImplementation();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isGenerateImplementation <em>Generate Implementation</em>}' attribute.
     * <!-- begin-user-doc -->
     * Specifies whether to generate starting point code for an implementation object.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Generate Implementation</em>' attribute.
     * @see #isGenerateImplementation()
     * @generated
     */
    void setGenerateImplementation(boolean value);

    /**
     * Returns whether processing of implicit SOAP headers (i.e. SOAP headers
     * defined in the wsdl:binding but not wsdl:portType section.) is enabled.
     * 
     * @model default="false"
     */
    boolean isProcessSOAPHeaders();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isProcessSOAPHeaders <em>Process SOAP Headers</em>}' attribute.
     * <!-- begin-user-doc -->
     * Enables or disables processing of implicit SOAP headers (i.e. SOAP
     * headers defined in the wsdl:binding but not wsdl:portType section.)
     * Default is false.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Process SOAP Headers</em>' attribute.
     * @see #isProcessSOAPHeaders()
     * @generated
     */
    void setProcessSOAPHeaders(boolean value);

    /**
     * Returns whether the loading of the default namespace package name mapping is enabled. 
     * Default is true.
     * 
     * @model default="true"
     */
    boolean isLoadDefaultNamespacePackageNameMapping();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isLoadDefaultNamespacePackageNameMapping <em>Load Default Namespace Package Name Mapping</em>}' attribute.
     * <!-- begin-user-doc -->
     * Enables the use of the default namespace package name mapping.
     * Default is true.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Load Default Namespace Package Name Mapping</em>' attribute.
     * @see #isLoadDefaultNamespacePackageNameMapping()
     * @generated
     */
    void setLoadDefaultNamespacePackageNameMapping(boolean value);

    /**
     * Returns whether the loading of the default excludes namespace mapping is enabled.
     * Default is true.
     * 
     * @model default="true"
     */
    boolean isLoadDefaultExcludesNamepsaceMapping();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isLoadDefaultExcludesNamepsaceMapping <em>Load Default Excludes Namepsace Mapping</em>}' attribute.
     * <!-- begin-user-doc -->
     * Enables the use of the default excludes namespace mapping.
     * Default is true.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Load Default Excludes Namepsace Mapping</em>' attribute.
     * @see #isLoadDefaultExcludesNamepsaceMapping()
     * @generated
     */
    void setLoadDefaultExcludesNamepsaceMapping(boolean value);

    /**
     * Returns whether to validate the WSDL before generating the code.
     * 
     * @model default="true"
     */
    boolean isValidate();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isValidate <em>Validate</em>}' attribute.
     * <!-- begin-user-doc -->
     * Enables WSDL validation before code generation.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Validate</em>' attribute.
     * @see #isValidate()
     * @generated
     */
    void setValidate(boolean value);

    /**
     * Returns the wsdl version .Default is WSDL1.1. Currently supports only WSDL1.1 version.
     * 
     * @model default="1.1"
     */
    String getWsdlVersion();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#getWsdlVersion <em>Wsdl Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Wsdl Version</em>' attribute.
     * @see #getWsdlVersion()
     * @generated
     */
    void setWsdlVersion(String value);

    /**
     * Returns whether the tool is set to generate default vales for the
     * generated client and the generated implementation.
     *
     * @model default="true"
     */
    boolean isUseDefaultValues();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isUseDefaultValues <em>Use Default Values</em>}' attribute.
     * <!-- begin-user-doc -->
     * Generate default vales for the generated client and the generated implementation.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Default Values</em>' attribute.
     * @see #isUseDefaultValues()
     * @generated
     */
    void setUseDefaultValues(boolean value);

    /**
     * Returns the comma separated list of arguments to be passed directly to
     * the XJC when the JAXB binding is being used.
     * 
     * @model
     */
    String getXjcArgs();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#getXjcArgs <em>Xjc Args</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xjc Args</em>' attribute.
     * @see #getXjcArgs()
     * @generated
     */
    void setXjcArgs(String value);

    /**
     * Returns whether the tool is set to use the CXF proprietary WS-Addressing 
     * type instead of the JAX-WS 2.1 compliant mapping.
     * 
     * @model
     */
    boolean isNoAddressBinding();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isNoAddressBinding <em>No Address Binding</em>}' attribute.
     * <!-- begin-user-doc -->
     * Instructs the tool to use the CXF proprietary WS-Addressing type 
     * instead of the JAX-WS 2.1 compliant mapping.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>No Address Binding</em>' attribute.
     * @see #isNoAddressBinding()
     * @generated
     */
    void setNoAddressBinding(boolean value);

    /**
     * Returns whether XJC will initialize fields mapped from elements with their default values.
     * 
     * @model
     */
    boolean isXjcUseDefaultValues();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcUseDefaultValues <em>Xjc Use Default Values</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xjc Use Default Values</em>' attribute.
     * @see #isXjcUseDefaultValues()
     * @generated
     */
    void setXjcUseDefaultValues(boolean value);

    /**
     * Returns whether XJC will generate a <code>toString()</code> method.
     * 
     * @model
     */
    boolean isXjcToString();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcToString <em>Xjc To String</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xjc To String</em>' attribute.
     * @see #isXjcToString()
     * @generated
     */
    void setXjcToString(boolean value);

    /**
     * Returns whether multi line output is enabled for <code>toString()</code>.
     * 
     * @model
     */
    boolean isXjcToStringMultiLine();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcToStringMultiLine <em>Xjc To String Multi Line</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xjc To String Multi Line</em>' attribute.
     * @see #isXjcToStringMultiLine()
     * @generated
     */
    void setXjcToStringMultiLine(boolean value);

    /**
     * Returns whether terse output is enabled for <code>toString()</code>.
     * 
     * @model
     */
    boolean isXjcToStringSimple();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcToStringSimple <em>Xjc To String Simple</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xjc To String Simple</em>' attribute.
     * @see #isXjcToStringSimple()
     * @generated
     */
    void setXjcToStringSimple(boolean value);

    /**
     * Returns whether source location support is enabled.
     * @model
     */
    boolean isXjcLocator();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcLocator <em>Xjc Locator</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xjc Locator</em>' attribute.
     * @see #isXjcLocator()
     * @generated
     */
    void setXjcLocator(boolean value);

    /**
     * Returns whether the XJC will generate accessor methods with 'synchronized' keyword.
     * 
     * @model
     */
    boolean isXjcSyncMethods();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcSyncMethods <em>Xjc Sync Methods</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xjc Sync Methods</em>' attribute.
     * @see #isXjcSyncMethods()
     * @generated
     */
    void setXjcSyncMethods(boolean value);

    /**
     * Returns whether the XJC will mark the code as generated using the annotations.
     * 
     * @model
     */
    boolean isXjcMarkGenerated();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#isXjcMarkGenerated <em>Xjc Mark Generated</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xjc Mark Generated</em>' attribute.
     * @see #isXjcMarkGenerated()
     * @generated
     */
    void setXjcMarkGenerated(boolean value);

    /**
     * The episode file location.
     * 
     * @model
     */
    String getXjcEpisodeFile();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext#getXjcEpisodeFile <em>Xjc Episode File</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Xjc Episode File</em>' attribute.
     * @see #getXjcEpisodeFile()
     * @generated
     */
    void setXjcEpisodeFile(String value);
}
