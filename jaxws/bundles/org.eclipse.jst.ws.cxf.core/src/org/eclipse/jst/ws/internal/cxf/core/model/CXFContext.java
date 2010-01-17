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

import java.util.Map;

/**
 * 
 * @model abstract="true" interface="true"
 */
public interface CXFContext {

    /**
     * Returns the value of the '<em><b>Default Runtime Location</b></em>' attribute.
     * <!-- begin-user-doc -->
     * Returns the default CXF Home Directory location.
     * <!-- end-user-doc -->
     * @return the value of the '<em>Default Runtime Location</em>' attribute.
     * @see #setDefaultRuntimeLocation(String)
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage#getCXFContext_DefaultRuntimeLocation()
     * @model
     * @generated
     */
    String getDefaultRuntimeLocation();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDefaultRuntimeLocation <em>Default Runtime Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the default CXF runtime location.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Runtime Location</em>' attribute.
     * @see #getDefaultRuntimeLocation()
     * @generated
     */
    void setDefaultRuntimeLocation(String value);

    /**
     * Returns the value of the '<em><b>Default Runtime Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     *  Returns the default CXF runtime type.
     * <!-- end-user-doc -->
     * @return the value of the '<em>Default Runtime Type</em>' attribute.
     * @see #setDefaultRuntimeType(String)
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage#getCXFContext_DefaultRuntimeType()
     * @model
     * @generated
     */
    String getDefaultRuntimeType();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDefaultRuntimeType <em>Default Runtime Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the default CXF runtime type.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Runtime Type</em>' attribute.
     * @see #getDefaultRuntimeType()
     * @generated
     */
    void setDefaultRuntimeType(String value);

    /**
     * Returns the value of the '<em><b>Default Runtime Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     *  Returns the default CXF runtime version.
     * <!-- end-user-doc -->
     * @return the value of the '<em>Default Runtime Version</em>' attribute.
     * @see #setDefaultRuntimeVersion(String)
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage#getCXFContext_DefaultRuntimeVersion()
     * @model
     * @generated
     */
    String getDefaultRuntimeVersion();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDefaultRuntimeVersion <em>Default Runtime Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the default CXF runtime version.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default Runtime Version</em>' attribute.
     * @see #getDefaultRuntimeVersion()
     * @generated
     */
    void setDefaultRuntimeVersion(String value);

    /**
     * Returns whether comments are shown during the code generation process.
     * 
     * @model default="true"
     */
    boolean isVerbose();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isVerbose <em>Verbose</em>}' attribute.
     * <!-- begin-user-doc -->
     * Displays comments during the code generation process.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Verbose</em>' attribute.
     * @see #isVerbose()
     * @generated
     */
    void setVerbose(boolean value);

    /**
     * Returns whether to generate an Ant build.xml file.
     * 
     * @model default="false"
     */
    boolean isGenerateAntBuildFile();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isGenerateAntBuildFile <em>Generate Ant Build File</em>}' attribute.
     * <!-- begin-user-doc -->
     * Set to generate an Ant build.xml file.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Generate Ant Build File</em>' attribute.
     * @see #isGenerateAntBuildFile()
     * @generated
     */
    void setGenerateAntBuildFile(boolean value);

    /**
     * Returns whether to generate a client.
     * 
     * @model default="false"
     */
    boolean isGenerateClient();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isGenerateClient <em>Generate Client</em>}' attribute.
     * <!-- begin-user-doc -->
     * Specify to generate client side code.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Generate Client</em>' attribute.
     * @see #isGenerateClient()
     * @generated
     */
    void setGenerateClient(boolean value);

    /**
     * Returns whether to generate a server.
     * 
     * @model default="false"
     */
    boolean isGenerateServer();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isGenerateServer <em>Generate Server</em>}' attribute.
     * <!-- begin-user-doc -->
     * Specify to generate server side code.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Generate Server</em>' attribute.
     * @see #isGenerateServer()
     * @generated
     */
    void setGenerateServer(boolean value);

    /**
     * Returns the data binding used.
     * 
     * @model default="jaxb"
     */
    DataBinding getDatabinding();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getDatabinding <em>Databinding</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Databinding</em>' attribute.
     * @see org.eclipse.jst.ws.internal.cxf.core.model.DataBinding
     * @see #getDatabinding()
     * @generated
     */
    void setDatabinding(DataBinding value);

    /**
     * Returns the frontend used.
     * 
     * @model default="jaxws"
     */
    Frontend getFrontend();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getFrontend <em>Frontend</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Frontend</em>' attribute.
     * @see org.eclipse.jst.ws.internal.cxf.core.model.Frontend
     * @see #getFrontend()
     * @generated
     */
    void setFrontend(Frontend value);

    /**
     * Returns if the Spring Application Context is used instead of cxf-servlet.
     * 
     * @model default="true"
     */
    boolean isUseSpringApplicationContext();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isUseSpringApplicationContext <em>Use Spring Application Context</em>}' attribute.
     * <!-- begin-user-doc -->
     * If true the Spring Application Context is used otherwise cxf servlet.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Spring Application Context</em>' attribute.
     * @see #isUseSpringApplicationContext()
     * @generated
     */
    void setUseSpringApplicationContext(boolean value);

    /**
     * Returns the value of the '<em><b>Export CXF Classpath Container</b></em>' attribute.
     * The default value is <code>"true"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Export CXF Classpath Container</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Export CXF Classpath Container</em>' attribute.
     * @see #setExportCXFClasspathContainer(boolean)
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage#getCXFContext_ExportCXFClasspathContainer()
     * @model default="true"
     * @generated
     */
    boolean isExportCXFClasspathContainer();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#isExportCXFClasspathContainer <em>Export CXF Classpath Container</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Export CXF Classpath Container</em>' attribute.
     * @see #isExportCXFClasspathContainer()
     * @generated
     */
    void setExportCXFClasspathContainer(boolean value);

    /**
     * Returns the value of the '<em><b>Installations</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Installations</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Installations</em>' attribute.
     * @see #setInstallations(Map)
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage#getCXFContext_Installations()
     * @model transient="true"
     * @generated
     */
    Map<String, CXFInstall> getInstallations();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getInstallations <em>Installations</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Installations</em>' attribute.
     * @see #getInstallations()
     * @generated
     */
    void setInstallations(Map<String, CXFInstall> value);
}
