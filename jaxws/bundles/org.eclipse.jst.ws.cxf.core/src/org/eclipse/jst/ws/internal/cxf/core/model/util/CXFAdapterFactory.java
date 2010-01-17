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
 * $Id: CXFAdapterFactory.java,v 1.3 2010/01/17 19:56:56 sclarke Exp $
 */
package org.eclipse.jst.ws.internal.cxf.core.model.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jst.ws.internal.cxf.core.model.*;


/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage
 * @generated
 */
public class CXFAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static CXFPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CXFAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = CXFPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    @Override
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected CXFSwitch<Adapter> modelSwitch =
        new CXFSwitch<Adapter>() {
            @Override
            public Adapter caseCXFContext(CXFContext object) {
                return createCXFContextAdapter();
            }
            @Override
            public Adapter caseCXFDataModel(CXFDataModel object) {
                return createCXFDataModelAdapter();
            }
            @Override
            public Adapter caseJava2WSContext(Java2WSContext object) {
                return createJava2WSContextAdapter();
            }
            @Override
            public Adapter caseJava2WSDataModel(Java2WSDataModel object) {
                return createJava2WSDataModelAdapter();
            }
            @Override
            public Adapter caseWSDL2JavaContext(WSDL2JavaContext object) {
                return createWSDL2JavaContextAdapter();
            }
            @Override
            public Adapter caseWSDL2JavaDataModel(WSDL2JavaDataModel object) {
                return createWSDL2JavaDataModelAdapter();
            }
            @Override
            public Adapter caseCXFInstall(CXFInstall object) {
                return createCXFInstallAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFContext <em>Context</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFContext
     * @generated
     */
    public Adapter createCXFContextAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel <em>Data Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel
     * @generated
     */
    public Adapter createCXFDataModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext <em>Java2 WS Context</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext
     * @generated
     */
    public Adapter createJava2WSContextAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel <em>Java2 WS Data Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel
     * @generated
     */
    public Adapter createJava2WSDataModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext <em>WSDL2 Java Context</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext
     * @generated
     */
    public Adapter createWSDL2JavaContextAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel <em>WSDL2 Java Data Model</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel
     * @generated
     */
    public Adapter createWSDL2JavaDataModelAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall <em>Install</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFInstall
     * @generated
     */
    public Adapter createCXFInstallAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //CXFAdapterFactory
