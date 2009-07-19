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
 * 
 * @model abstract="true" interface="true"
 */
public interface CXFContext {

    /**
     * Returns the CXF Home Directory location.
     * 
     * @model
     */
    String getCxfRuntimeLocation();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getCxfRuntimeLocation <em>Cxf Runtime Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the CXF Home Directory location.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cxf Runtime Location</em>' attribute.
     * @see #getCxfRuntimeLocation()
     * @generated
     */
    void setCxfRuntimeLocation(String value);

    /**
     * Returns the CXF Runtime Edition.
     * 
     * @model
     */
    String getCxfRuntimeEdition();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getCxfRuntimeEdition <em>Cxf Runtime Edition</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the CXF Runtime Edition.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cxf Runtime Edition</em>' attribute.
     * @see #getCxfRuntimeEdition()
     * @generated
     */
    void setCxfRuntimeEdition(String value);

    /**
     * Returns the CXF Tool version.
     * 
     * @model
     */
    String getCxfRuntimeVersion();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext#getCxfRuntimeVersion <em>Cxf Runtime Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the CXF Tool version
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cxf Runtime Version</em>' attribute.
     * @see #getCxfRuntimeVersion()
     * @generated
     */
    void setCxfRuntimeVersion(String value);

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
}
