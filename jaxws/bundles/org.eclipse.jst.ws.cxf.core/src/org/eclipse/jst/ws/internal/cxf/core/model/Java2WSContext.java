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
public interface Java2WSContext extends CXFContext {
    /**
     * Returns whether the generated WSDL is to include a SOAP 1.2 binding.
     * 
     * @model default="false"
     */
    boolean isSoap12Binding();

    /**
	 * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isSoap12Binding <em>Soap12 Binding</em>}' attribute.
	 * <!-- begin-user-doc -->
     * Specifies that the generated WSDL is to include a SOAP 1.2 binding.
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Soap12 Binding</em>' attribute.
	 * @see #isSoap12Binding()
	 * @generated
	 */
    void setSoap12Binding(boolean value);

    /**
     * Returns whether to output schemas to separate files and use imports to
     * load them instead of inlining them into the wsdl.
     * 
     * @model default="true"
     */
    boolean isGenerateXSDImports();

    /**
	 * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateXSDImports <em>Generate XSD Imports</em>}' attribute.
	 * <!-- begin-user-doc -->
     * Output schemas to separate files and use imports to load them instead of
     * inlining them into the wsdl.
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate XSD Imports</em>' attribute.
	 * @see #isGenerateXSDImports()
	 * @generated
	 */
    void setGenerateXSDImports(boolean value);

    /**
     * Returns whether to generate wsdl.
     * 
     * @model default="true"
     */
    boolean isGenerateWSDL();

    /**
	 * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWSDL <em>Generate WSDL</em>}' attribute.
	 * <!-- begin-user-doc -->
     * Specify to generate the WSDL file.
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate WSDL</em>' attribute.
	 * @see #isGenerateWSDL()
	 * @generated
	 */
    void setGenerateWSDL(boolean value);

    /**
     * Returns whether to generate wrapper beans.
     * 
     * @model default="true"
     */
    boolean isGenerateWrapperFaultBeans();

    /**
	 * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWrapperFaultBeans <em>Generate Wrapper Fault Beans</em>}' attribute.
	 * <!-- begin-user-doc -->
     * Specify to generate the wrapper and fault bean.
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate Wrapper Fault Beans</em>' attribute.
	 * @see #isGenerateWrapperFaultBeans()
	 * @generated
	 */
    void setGenerateWrapperFaultBeans(boolean value);

    /**
     * Returns whether the Annotation Processing Tool (APT) is enabled.
     * 
     * @model default="false"
     */
    boolean isAnnotationProcessingEnabled();

    /**
	 * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isAnnotationProcessingEnabled <em>Annotation Processing Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
     * Set to enable the Annotation Processing Tool (APT) extension.
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Annotation Processing Enabled</em>' attribute.
	 * @see #isAnnotationProcessingEnabled()
	 * @generated
	 */
    void setAnnotationProcessingEnabled(boolean value);

    /**
     * Returns whether the <code>@WebMethod</code> annotation generation is enabled by default.
     * 
     * @model default="true"
     */
    boolean isGenerateWebMethodAnnotation();
    
    /**
	 * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWebMethodAnnotation <em>Generate Web Method Annotation</em>}' attribute.
	 * <!-- begin-user-doc -->
     * Sets the enablement of the <code>@WebMethod</code> annotation generation.
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate Web Method Annotation</em>' attribute.
	 * @see #isGenerateWebMethodAnnotation()
	 * @generated
	 */
    void setGenerateWebMethodAnnotation(boolean value);

    /**
     * Returns whether the <code>@WebParam</code> annotation generation is enabled by default.
     * 
     * @model default="true"
     */
    boolean isGenerateWebParamAnnotation();
    
    /**
	 * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWebParamAnnotation <em>Generate Web Param Annotation</em>}' attribute.
	 * <!-- begin-user-doc -->
     * Sets the enablement of the <code>@WebParam</code> annotation generation.
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate Web Param Annotation</em>' attribute.
	 * @see #isGenerateWebParamAnnotation()
	 * @generated
	 */
    void setGenerateWebParamAnnotation(boolean value);

    /**
     * Returns whether the <code>@RequestWrapper</code> annotation generation is enabled by default.
     * 
     * @model default="true"
     */
    boolean isGenerateRequestWrapperAnnotation();
    
    /**
	 * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateRequestWrapperAnnotation <em>Generate Request Wrapper Annotation</em>}' attribute.
	 * <!-- begin-user-doc -->
     * Sets the enablement of the <code>@RequestWrapper</code> annotation generation.
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate Request Wrapper Annotation</em>' attribute.
	 * @see #isGenerateRequestWrapperAnnotation()
	 * @generated
	 */
    void setGenerateRequestWrapperAnnotation(boolean value);

    /**
     * Returns whether the <code>@ResponseWrapper</code> annotation generation is enabled by default.
     * 
     * @model default="true"
     */
    boolean isGenerateResponseWrapperAnnotation();

    /**
	 * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateResponseWrapperAnnotation <em>Generate Response Wrapper Annotation</em>}' attribute.
	 * <!-- begin-user-doc -->
     * Sets the enablement of the <code>@ResponseWrapper</code> annotation generation.
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate Response Wrapper Annotation</em>' attribute.
	 * @see #isGenerateResponseWrapperAnnotation()
	 * @generated
	 */
    void setGenerateResponseWrapperAnnotation(boolean value);

				/**
	 * Returns the value of the '<em><b>Generate Web Result Annotation</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Generate Web Result Annotation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Generate Web Result Annotation</em>' attribute.
	 * @see #setGenerateWebResultAnnotation(boolean)
	 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage#getJava2WSContext_GenerateWebResultAnnotation()
	 * @model default="false"
	 * @generated
	 */
	boolean isGenerateWebResultAnnotation();

				/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext#isGenerateWebResultAnnotation <em>Generate Web Result Annotation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Generate Web Result Annotation</em>' attribute.
	 * @see #isGenerateWebResultAnnotation()
	 * @generated
	 */
	void setGenerateWebResultAnnotation(boolean value);
}
