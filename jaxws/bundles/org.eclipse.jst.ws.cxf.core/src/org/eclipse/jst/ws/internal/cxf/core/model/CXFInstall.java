/**
 * Copyright (c) 2010 Shane Clarke
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 * Shane Clarke - initial API and implementation
 *
 * $Id: CXFInstall.java,v 1.1 2010/01/17 19:56:56 sclarke Exp $
 */
package org.eclipse.jst.ws.internal.cxf.core.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Install</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage#getCXFInstall()
 * @model
 * @generated
 */
public interface CXFInstall {
    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Version</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage#getCXFInstall_Version()
     * @model
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

    /**
     * Returns the value of the '<em><b>Location</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Location</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Location</em>' attribute.
     * @see #setLocation(String)
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage#getCXFInstall_Location()
     * @model
     * @generated
     */
    String getLocation();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getLocation <em>Location</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Location</em>' attribute.
     * @see #getLocation()
     * @generated
     */
    void setLocation(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(String)
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage#getCXFInstall_Type()
     * @model
     * @generated
     */
    String getType();

    /**
     * Sets the value of the '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(String value);

} // CXFInstall
