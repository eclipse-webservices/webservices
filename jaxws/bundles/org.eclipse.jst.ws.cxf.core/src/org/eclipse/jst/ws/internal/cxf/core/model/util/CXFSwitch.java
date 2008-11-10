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
 * $Id: CXFSwitch.java,v 1.1 2008/11/10 20:37:45 david_williams Exp $
 */
package org.eclipse.jst.ws.internal.cxf.core.model.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jst.ws.internal.cxf.core.model.*;


/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage
 * @generated
 */
public class CXFSwitch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static CXFPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CXFSwitch() {
        if (modelPackage == null) {
            modelPackage = CXFPackage.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public T doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        }
        else {
            List<EClass> eSuperTypes = theEClass.getESuperTypes();
            return
                eSuperTypes.isEmpty() ?
                    defaultCase(theEObject) :
                    doSwitch(eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case CXFPackage.CXF_CONTEXT: {
                CXFContext cxfContext = (CXFContext)theEObject;
                T result = caseCXFContext(cxfContext);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CXFPackage.CXF_DATA_MODEL: {
                CXFDataModel cxfDataModel = (CXFDataModel)theEObject;
                T result = caseCXFDataModel(cxfDataModel);
                if (result == null) result = caseCXFContext(cxfDataModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CXFPackage.JAVA2_WS_CONTEXT: {
                Java2WSContext java2WSContext = (Java2WSContext)theEObject;
                T result = caseJava2WSContext(java2WSContext);
                if (result == null) result = caseCXFContext(java2WSContext);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CXFPackage.JAVA2_WS_DATA_MODEL: {
                Java2WSDataModel java2WSDataModel = (Java2WSDataModel)theEObject;
                T result = caseJava2WSDataModel(java2WSDataModel);
                if (result == null) result = caseCXFDataModel(java2WSDataModel);
                if (result == null) result = caseJava2WSContext(java2WSDataModel);
                if (result == null) result = caseCXFContext(java2WSDataModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CXFPackage.WSDL2_JAVA_CONTEXT: {
                WSDL2JavaContext wsdl2JavaContext = (WSDL2JavaContext)theEObject;
                T result = caseWSDL2JavaContext(wsdl2JavaContext);
                if (result == null) result = caseCXFContext(wsdl2JavaContext);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case CXFPackage.WSDL2_JAVA_DATA_MODEL: {
                WSDL2JavaDataModel wsdl2JavaDataModel = (WSDL2JavaDataModel)theEObject;
                T result = caseWSDL2JavaDataModel(wsdl2JavaDataModel);
                if (result == null) result = caseCXFDataModel(wsdl2JavaDataModel);
                if (result == null) result = caseWSDL2JavaContext(wsdl2JavaDataModel);
                if (result == null) result = caseCXFContext(wsdl2JavaDataModel);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Context</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Context</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCXFContext(CXFContext object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Data Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Data Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCXFDataModel(CXFDataModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Java2 WS Context</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Java2 WS Context</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseJava2WSContext(Java2WSContext object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Java2 WS Data Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Java2 WS Data Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseJava2WSDataModel(Java2WSDataModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>WSDL2 Java Context</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>WSDL2 Java Context</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWSDL2JavaContext(WSDL2JavaContext object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>WSDL2 Java Data Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>WSDL2 Java Data Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseWSDL2JavaDataModel(WSDL2JavaDataModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    public T defaultCase(EObject object) {
        return null;
    }

} //CXFSwitch
