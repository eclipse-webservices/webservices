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
 * $Id: CXFFactoryImpl.java,v 1.2 2010/01/17 19:56:56 sclarke Exp $
 */
package org.eclipse.jst.ws.internal.cxf.core.model.impl;

import java.net.URL;

import java.util.Map;

import javax.wsdl.Definition;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jst.ws.internal.cxf.core.model.*;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CXFFactoryImpl extends EFactoryImpl implements CXFFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static CXFFactory init() {
        try {
            CXFFactory theCXFFactory = (CXFFactory)EPackage.Registry.INSTANCE.getEFactory("http:///org/eclipse/jst/ws/internal/cxf/core/model.ecore"); 
            if (theCXFFactory != null) {
                return theCXFFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new CXFFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CXFFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case CXFPackage.JAVA2_WS_DATA_MODEL: return (EObject)createJava2WSDataModel();
            case CXFPackage.WSDL2_JAVA_DATA_MODEL: return (EObject)createWSDL2JavaDataModel();
            case CXFPackage.CXF_INSTALL: return (EObject)createCXFInstall();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case CXFPackage.FRONTEND:
                return createFrontendFromString(eDataType, initialValue);
            case CXFPackage.DATA_BINDING:
                return createDataBindingFromString(eDataType, initialValue);
            case CXFPackage.URL:
                return createURLFromString(eDataType, initialValue);
            case CXFPackage.DEFINITION:
                return createDefinitionFromString(eDataType, initialValue);
            case CXFPackage.MAP:
                return createMapFromString(eDataType, initialValue);
            case CXFPackage.IMETHOD:
                return createIMethodFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case CXFPackage.FRONTEND:
                return convertFrontendToString(eDataType, instanceValue);
            case CXFPackage.DATA_BINDING:
                return convertDataBindingToString(eDataType, instanceValue);
            case CXFPackage.URL:
                return convertURLToString(eDataType, instanceValue);
            case CXFPackage.DEFINITION:
                return convertDefinitionToString(eDataType, instanceValue);
            case CXFPackage.MAP:
                return convertMapToString(eDataType, instanceValue);
            case CXFPackage.IMETHOD:
                return convertIMethodToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Java2WSDataModel createJava2WSDataModel() {
        Java2WSDataModelImpl java2WSDataModel = new Java2WSDataModelImpl();
        return java2WSDataModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WSDL2JavaDataModel createWSDL2JavaDataModel() {
        WSDL2JavaDataModelImpl wsdl2JavaDataModel = new WSDL2JavaDataModelImpl();
        return wsdl2JavaDataModel;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CXFInstall createCXFInstall() {
        CXFInstallImpl cxfInstall = new CXFInstallImpl();
        return cxfInstall;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Frontend createFrontendFromString(EDataType eDataType, String initialValue) {
        Frontend result = Frontend.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertFrontendToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DataBinding createDataBindingFromString(EDataType eDataType, String initialValue) {
        DataBinding result = DataBinding.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDataBindingToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public URL createURLFromString(EDataType eDataType, String initialValue) {
        return (URL)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertURLToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Definition createDefinitionFromString(EDataType eDataType, String initialValue) {
        return (Definition)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertDefinitionToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Map<?, ?> createMapFromString(EDataType eDataType, String initialValue) {
        return (Map<?, ?>)super.createFromString(initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertMapToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IMethod createIMethodFromString(EDataType eDataType, String initialValue) {
        return (IMethod)super.createFromString(eDataType, initialValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertIMethodToString(EDataType eDataType, Object instanceValue) {
        return super.convertToString(eDataType, instanceValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CXFPackage getCXFPackage() {
        return (CXFPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static CXFPackage getPackage() {
        return CXFPackage.eINSTANCE;
    }

} //CXFFactoryImpl
