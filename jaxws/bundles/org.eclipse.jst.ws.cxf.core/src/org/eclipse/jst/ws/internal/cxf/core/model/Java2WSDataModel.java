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

import org.eclipse.jdt.core.IMethod;

/**
 * @author sclarke
 * 
 * @model
 */
public interface Java2WSDataModel extends CXFDataModel, Java2WSContext {
    /**
     * Returns the classpath searched when processing.
     * 
     * @model
     */
    String getClasspath();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getClasspath <em>Classpath</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the classpath to search when processing.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Classpath</em>' attribute.
     * @see #getClasspath()
     * @generated
     */
    void setClasspath(String value);

    /**
     * Returns the Java starting point artifact. This can be either a Java Class or Interface.
     * 
     * @model
     */
    String getJavaStartingPoint();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getJavaStartingPoint <em>Java Starting Point</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the Java starting point artifact. It may be a Java Class or Interface.
     * @see #setFullyQualifiedJavaClassName()
     * @see #setFullyQualifiedJavaInterfaceName()
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Java Starting Point</em>' attribute.
     * @see #getJavaStartingPoint()
     * @generated
     */
    void setJavaStartingPoint(String value);

    /**
     * If the starting point is a Java Class this tracks whether to use an SEI has been selected.
     * 
     * @model
     */
    boolean isUseServiceEndpointInterface();


    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#isUseServiceEndpointInterface <em>Use Service Endpoint Interface</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Use Service Endpoint Interface</em>' attribute.
     * @see #isUseServiceEndpointInterface()
     * @generated
     */
    void setUseServiceEndpointInterface(boolean value);

    /**
     * If the starting point is a Java Class this tracks whether to option to
     * extract the SEI has been selected.
     * 
     * @model
     */
    boolean isExtractInterface();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#isExtractInterface <em>Extract Interface</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the option to extract the SEI from the Java class starting point.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extract Interface</em>' attribute.
     * @see #isExtractInterface()
     * @generated
     */
    void setExtractInterface(boolean value);

    /**
     * Gets the new SEI name.
     * 
     * @model
     */
    String getServiceEndpointInterfaceName();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getServiceEndpointInterfaceName <em>Service Endpoint Interface Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Endpoint Interface Name</em>' attribute.
     * @see #getServiceEndpointInterfaceName()
     * @generated
     */
    void setServiceEndpointInterfaceName(String value);

    /**
     * Returns the map of the methods to use and their annotations.
     * 
     * @model
     */
    Map<IMethod, Map<String, Boolean>> getMethodMap();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getMethodMap <em>Method Map</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the map of the methods to use and the method annotations.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Method Map</em>' attribute.
     * @see #getMethodMap()
     * @generated
     */
    void setMethodMap(Map<IMethod, Map<String, Boolean>> value);

    /**
     * Returns a map of enabled default method annotations
     * 
     * @model
     */
    Map<String, Boolean> getAnnotationMap();
    
    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getAnnotationMap <em>Annotation Map</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets a map of the enabled method annotations.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Annotation Map</em>' attribute.
     * @see #getAnnotationMap()
     * @generated
     */
    void setAnnotationMap(Map<String, Boolean> value);

    /**
     * Returns the directory in which the generated source files are placed.
     * 
     * @model
     */
    String getSourceDirectory();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getSourceDirectory <em>Source Directory</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the directory in which the generated source files are placed.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source Directory</em>' attribute.
     * @see #getSourceDirectory()
     * @generated
     */
    void setSourceDirectory(String value);

    /**
     * Returns the port name used in the generated wsdl.
     * 
     * @model
     */
    String getPortName();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel#getPortName <em>Port Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the port name to use in the generated wsdl.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Port Name</em>' attribute.
     * @see #getPortName()
     * @generated
     */
    void setPortName(String value);

}
