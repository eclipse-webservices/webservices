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
 * $Id: CXFFactory.java,v 1.2 2010/01/17 19:56:56 sclarke Exp $
 */
package org.eclipse.jst.ws.internal.cxf.core.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage
 * @generated
 */
public interface CXFFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    CXFFactory eINSTANCE = org.eclipse.jst.ws.internal.cxf.core.model.impl.CXFFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Java2 WS Data Model</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Java2 WS Data Model</em>'.
     * @generated
     */
    Java2WSDataModel createJava2WSDataModel();

    /**
     * Returns a new object of class '<em>WSDL2 Java Data Model</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>WSDL2 Java Data Model</em>'.
     * @generated
     */
    WSDL2JavaDataModel createWSDL2JavaDataModel();

    /**
     * Returns a new object of class '<em>Install</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Install</em>'.
     * @generated
     */
    CXFInstall createCXFInstall();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    CXFPackage getCXFPackage();

} //CXFFactory
