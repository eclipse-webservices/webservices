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

import java.net.URL;
import javax.wsdl.Definition;

/**
 * 
 * @model abstract="true"
 */
public interface CXFDataModel extends CXFContext {
    /**
     * Returns the originating project that contains the java or wsdl resource.
     * 
     * @model
     */
    String getProjectName();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getProjectName <em>Project Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the originating project that contains the java or wsdl resource.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Project Name</em>' attribute.
     * @see #getProjectName()
     * @generated
     */
    void setProjectName(String value);

    /**
     * Returns the resource directory in which the output files are placed.
     *  
     * @model
     */
    String getResourceDirectory();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getResourceDirectory <em>Resource Directory</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the resource directory in which the output files are placed.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Resource Directory</em>' attribute.
     * @see #getResourceDirectory()
     * @generated
     */
    void setResourceDirectory(String value);

    /**
     * Returns the directory in which the generated sources are compiled into.
     * If not specified, the files are not compiled.
     * 
     * @model
     */
    String getClassDirectory();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getClassDirectory <em>Class Directory</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets directory in which the generated sources are compiled into. If not
     * specified, the files are not compiled.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Class Directory</em>' attribute.
     * @see #getClassDirectory()
     * @generated
     */
    void setClassDirectory(String value);

    /**
     * Returns the name of the WSDL File that is used as input to WSDL2Java or
     * is the output of Java2WS.
     * 
     * @model
     */
    String getWsdlFileName();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlFileName <em>Wsdl File Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Wsdl File Name</em>' attribute.
     * @see #getWsdlFileName()
     * @generated
     */
    void setWsdlFileName(String value);

    /**
     * Returns the WSDL URL.
     * 
     * @model
     */
    URL getWsdlURL();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlURL <em>Wsdl URL</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Wsdl URL</em>' attribute.
     * @see #getWsdlURL()
     * @generated
     */
    void setWsdlURL(URL value);

    /**
     * Returns the wsdl location relative to the WebContent directory.
     * Used in the spring configuration.
     * 
     * @model
     */
    String getConfigWsdlLocation();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getConfigWsdlLocation <em>Config Wsdl Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Config Wsdl Location</em>' attribute.
     * @see #getConfigWsdlLocation()
     * @generated
     */
    void setConfigWsdlLocation(String value);

    /**
     * Returns the Java Class that may be used as input to Java2WS or is the output
     * of WSDL2Java.
     * 
     * @model
     */
    String getFullyQualifiedJavaClassName();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getFullyQualifiedJavaClassName <em>Fully Qualified Java Class Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the Java Class that may be used as input to Java2WS or is the output of WSDL2Java.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Fully Qualified Java Class Name</em>' attribute.
     * @see #getFullyQualifiedJavaClassName()
     * @generated
     */
    void setFullyQualifiedJavaClassName(String value);

    /**
     * Returns the name of the Java Interface that may be used as input to Java2WS.
     * 
     * @model
     */
    String getFullyQualifiedJavaInterfaceName();
    
    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getFullyQualifiedJavaInterfaceName <em>Fully Qualified Java Interface Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the name of the Java Interface that may be used as input to Java2WS.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Fully Qualified Java Interface Name</em>' attribute.
     * @see #getFullyQualifiedJavaInterfaceName()
     * @generated
     */
    void setFullyQualifiedJavaInterfaceName(String value);

    /**
     * Used in the CXF Spring Configuration to uniquely identify elements.
     * 
     * @model
     */
    String getConfigId();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getConfigId <em>Config Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Config Id</em>' attribute.
     * @see #getConfigId()
     * @generated
     */
    void setConfigId(String value);

    /**
     * Returns the targetNamespace.
     * 
     * @model
     */
    String getTargetNamespace();
    
    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getTargetNamespace <em>Target Namespace</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the targetNamespace.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target Namespace</em>' attribute.
     * @see #getTargetNamespace()
     * @generated
     */
    void setTargetNamespace(String value);

    /**
     * Returns the endointName attribute used in the jaxws:endpoint element in the CXF Spring Configuration.
     * 
     * @model
     */
    String getEndpointName();
    
    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getEndpointName <em>Endpoint Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the endpointName attribute used in the jaxws:endpoint element in the CXF Spring Configuration.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Endpoint Name</em>' attribute.
     * @see #getEndpointName()
     * @generated
     */
    void setEndpointName(String value);

    /**
     * Returns the serviceName attribute used in the jaxws:endpoint element in the CXF Spring Configuration.
     * 
     * @model
     */
    String getServiceName();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getServiceName <em>Service Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * Sets the serviceName attribute used in the jaxws:endpoint element in the CXF Spring Configuration.
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Name</em>' attribute.
     * @see #getServiceName()
     * @generated
     */
    void setServiceName(String value);

    /**
     * @model
     */
    Definition getWsdlDefinition();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlDefinition <em>Wsdl Definition</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Wsdl Definition</em>' attribute.
     * @see #getWsdlDefinition()
     * @generated
     */
    void setWsdlDefinition(Definition value);

    /**
     * @model
     */
    String getWsdlLocation();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel#getWsdlLocation <em>Wsdl Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Wsdl Location</em>' attribute.
     * @see #getWsdlLocation()
     * @generated
     */
    void setWsdlLocation(String value);
        
}