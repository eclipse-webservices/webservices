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
 * $Id: CXFPackageImpl.java,v 1.1 2008/11/10 20:37:42 david_williams Exp $
 */
package org.eclipse.jst.ws.internal.cxf.core.model.impl;

import java.util.Map;

import javax.wsdl.Definition;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFContext;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFFactory;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage;
import org.eclipse.jst.ws.internal.cxf.core.model.DataBinding;
import org.eclipse.jst.ws.internal.cxf.core.model.Frontend;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSContext;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaContext;
import org.eclipse.jst.ws.internal.cxf.core.model.WSDL2JavaDataModel;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CXFPackageImpl extends EPackageImpl implements CXFPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass cxfContextEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass cxfDataModelEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass java2WSContextEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass java2WSDataModelEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass wsdl2JavaContextEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass wsdl2JavaDataModelEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum frontendEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum dataBindingEEnum = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType urlEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType definitionEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType mapEDataType = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EDataType iMethodEDataType = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private CXFPackageImpl() {
        super(eNS_URI, CXFFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this
     * model, and for any others upon which it depends.  Simple
     * dependencies are satisfied by calling this method on all
     * dependent packages before doing anything else.  This method drives
     * initialization for interdependent packages directly, in parallel
     * with this package, itself.
     * <p>Of this package and its interdependencies, all packages which
     * have not yet been registered by their URI values are first created
     * and registered.  The packages are then initialized in two steps:
     * meta-model objects for all of the packages are created before any
     * are initialized, since one package's meta-model objects may refer to
     * those of another.
     * <p>Invocation of this method will not affect any packages that have
     * already been initialized.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static CXFPackage init() {
        if (isInited) return (CXFPackage)EPackage.Registry.INSTANCE.getEPackage(CXFPackage.eNS_URI);

        // Obtain or create and register package
        CXFPackageImpl theCXFPackage = (CXFPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof CXFPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new CXFPackageImpl());

        isInited = true;

        // Create package meta-data objects
        theCXFPackage.createPackageContents();

        // Initialize created meta-data
        theCXFPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theCXFPackage.freeze();

        return theCXFPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCXFContext() {
        return cxfContextEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFContext_CxfRuntimeLocation() {
        return (EAttribute)cxfContextEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFContext_CxfRuntimeEdition() {
        return (EAttribute)cxfContextEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFContext_CxfRuntimeVersion() {
        return (EAttribute)cxfContextEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFContext_Verbose() {
        return (EAttribute)cxfContextEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFContext_GenerateAntBuildFile() {
        return (EAttribute)cxfContextEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFContext_GenerateClient() {
        return (EAttribute)cxfContextEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFContext_GenerateServer() {
        return (EAttribute)cxfContextEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFContext_Databinding() {
        return (EAttribute)cxfContextEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFContext_Frontend() {
        return (EAttribute)cxfContextEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFContext_UseSpringApplicationContext() {
        return (EAttribute)cxfContextEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCXFDataModel() {
        return cxfDataModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_ProjectName() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_ResourceDirectory() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_ClassDirectory() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_WsdlFileName() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_WsdlURL() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_ConfigWsdlLocation() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_FullyQualifiedJavaClassName() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_FullyQualifiedJavaInterfaceName() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_ConfigId() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_TargetNamespace() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_EndpointName() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_ServiceName() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_WsdlDefinition() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCXFDataModel_WsdlLocation() {
        return (EAttribute)cxfDataModelEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getJava2WSContext() {
        return java2WSContextEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSContext_Soap12Binding() {
        return (EAttribute)java2WSContextEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSContext_GenerateXSDImports() {
        return (EAttribute)java2WSContextEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSContext_GenerateWSDL() {
        return (EAttribute)java2WSContextEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSContext_GenerateWrapperFaultBeans() {
        return (EAttribute)java2WSContextEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSContext_AnnotationProcessingEnabled() {
        return (EAttribute)java2WSContextEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSContext_GenerateWebMethodAnnotation() {
        return (EAttribute)java2WSContextEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSContext_GenerateWebParamAnnotation() {
        return (EAttribute)java2WSContextEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSContext_GenerateRequestWrapperAnnotation() {
        return (EAttribute)java2WSContextEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSContext_GenerateResponseWrapperAnnotation() {
        return (EAttribute)java2WSContextEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getJava2WSDataModel() {
        return java2WSDataModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSDataModel_Classpath() {
        return (EAttribute)java2WSDataModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSDataModel_JavaStartingPoint() {
        return (EAttribute)java2WSDataModelEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSDataModel_UseServiceEndpointInterface() {
        return (EAttribute)java2WSDataModelEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSDataModel_ExtractInterface() {
        return (EAttribute)java2WSDataModelEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSDataModel_ServiceEndpointInterfaceName() {
        return (EAttribute)java2WSDataModelEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSDataModel_MethodMap() {
        return (EAttribute)java2WSDataModelEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSDataModel_AnnotationMap() {
        return (EAttribute)java2WSDataModelEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSDataModel_SourceDirectory() {
        return (EAttribute)java2WSDataModelEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getJava2WSDataModel_PortName() {
        return (EAttribute)java2WSDataModelEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getWSDL2JavaContext() {
        return wsdl2JavaContextEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_GenerateImplementation() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_ProcessSOAPHeaders() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_LoadDefaultNamespacePackageNameMapping() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_LoadDefaultExcludesNamepsaceMapping() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_Validate() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_WsdlVersion() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_UseDefaultValues() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_XjcArgs() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_NoAddressBinding() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_XjcUseDefaultValues() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_XjcToString() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_XjcToStringMultiLine() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_XjcToStringSimple() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_XjcLocator() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_XjcSyncMethods() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_XjcMarkGenerated() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaContext_XjcEpisodeFile() {
        return (EAttribute)wsdl2JavaContextEClass.getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getWSDL2JavaDataModel() {
        return wsdl2JavaDataModelEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaDataModel_IncludedNamespaces() {
        return (EAttribute)wsdl2JavaDataModelEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaDataModel_BindingFiles() {
        return (EAttribute)wsdl2JavaDataModelEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaDataModel_ExcludedNamespaces() {
        return (EAttribute)wsdl2JavaDataModelEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaDataModel_CatalogFile() {
        return (EAttribute)wsdl2JavaDataModelEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getWSDL2JavaDataModel_JavaSourceFolder() {
        return (EAttribute)wsdl2JavaDataModelEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getFrontend() {
        return frontendEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getDataBinding() {
        return dataBindingEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getURL() {
        return urlEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getDefinition() {
        return definitionEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getMap() {
        return mapEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EDataType getIMethod() {
        return iMethodEDataType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public CXFFactory getCXFFactory() {
        return (CXFFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        cxfContextEClass = createEClass(CXF_CONTEXT);
        createEAttribute(cxfContextEClass, CXF_CONTEXT__CXF_RUNTIME_LOCATION);
        createEAttribute(cxfContextEClass, CXF_CONTEXT__CXF_RUNTIME_EDITION);
        createEAttribute(cxfContextEClass, CXF_CONTEXT__CXF_RUNTIME_VERSION);
        createEAttribute(cxfContextEClass, CXF_CONTEXT__VERBOSE);
        createEAttribute(cxfContextEClass, CXF_CONTEXT__GENERATE_ANT_BUILD_FILE);
        createEAttribute(cxfContextEClass, CXF_CONTEXT__GENERATE_CLIENT);
        createEAttribute(cxfContextEClass, CXF_CONTEXT__GENERATE_SERVER);
        createEAttribute(cxfContextEClass, CXF_CONTEXT__DATABINDING);
        createEAttribute(cxfContextEClass, CXF_CONTEXT__FRONTEND);
        createEAttribute(cxfContextEClass, CXF_CONTEXT__USE_SPRING_APPLICATION_CONTEXT);

        cxfDataModelEClass = createEClass(CXF_DATA_MODEL);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__PROJECT_NAME);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__RESOURCE_DIRECTORY);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__CLASS_DIRECTORY);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__WSDL_FILE_NAME);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__WSDL_URL);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__CONFIG_WSDL_LOCATION);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_CLASS_NAME);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__FULLY_QUALIFIED_JAVA_INTERFACE_NAME);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__CONFIG_ID);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__TARGET_NAMESPACE);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__ENDPOINT_NAME);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__SERVICE_NAME);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__WSDL_DEFINITION);
        createEAttribute(cxfDataModelEClass, CXF_DATA_MODEL__WSDL_LOCATION);

        java2WSContextEClass = createEClass(JAVA2_WS_CONTEXT);
        createEAttribute(java2WSContextEClass, JAVA2_WS_CONTEXT__SOAP12_BINDING);
        createEAttribute(java2WSContextEClass, JAVA2_WS_CONTEXT__GENERATE_XSD_IMPORTS);
        createEAttribute(java2WSContextEClass, JAVA2_WS_CONTEXT__GENERATE_WSDL);
        createEAttribute(java2WSContextEClass, JAVA2_WS_CONTEXT__GENERATE_WRAPPER_FAULT_BEANS);
        createEAttribute(java2WSContextEClass, JAVA2_WS_CONTEXT__ANNOTATION_PROCESSING_ENABLED);
        createEAttribute(java2WSContextEClass, JAVA2_WS_CONTEXT__GENERATE_WEB_METHOD_ANNOTATION);
        createEAttribute(java2WSContextEClass, JAVA2_WS_CONTEXT__GENERATE_WEB_PARAM_ANNOTATION);
        createEAttribute(java2WSContextEClass, JAVA2_WS_CONTEXT__GENERATE_REQUEST_WRAPPER_ANNOTATION);
        createEAttribute(java2WSContextEClass, JAVA2_WS_CONTEXT__GENERATE_RESPONSE_WRAPPER_ANNOTATION);

        java2WSDataModelEClass = createEClass(JAVA2_WS_DATA_MODEL);
        createEAttribute(java2WSDataModelEClass, JAVA2_WS_DATA_MODEL__CLASSPATH);
        createEAttribute(java2WSDataModelEClass, JAVA2_WS_DATA_MODEL__JAVA_STARTING_POINT);
        createEAttribute(java2WSDataModelEClass, JAVA2_WS_DATA_MODEL__USE_SERVICE_ENDPOINT_INTERFACE);
        createEAttribute(java2WSDataModelEClass, JAVA2_WS_DATA_MODEL__EXTRACT_INTERFACE);
        createEAttribute(java2WSDataModelEClass, JAVA2_WS_DATA_MODEL__SERVICE_ENDPOINT_INTERFACE_NAME);
        createEAttribute(java2WSDataModelEClass, JAVA2_WS_DATA_MODEL__METHOD_MAP);
        createEAttribute(java2WSDataModelEClass, JAVA2_WS_DATA_MODEL__ANNOTATION_MAP);
        createEAttribute(java2WSDataModelEClass, JAVA2_WS_DATA_MODEL__SOURCE_DIRECTORY);
        createEAttribute(java2WSDataModelEClass, JAVA2_WS_DATA_MODEL__PORT_NAME);

        wsdl2JavaContextEClass = createEClass(WSDL2_JAVA_CONTEXT);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__GENERATE_IMPLEMENTATION);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__PROCESS_SOAP_HEADERS);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__VALIDATE);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__WSDL_VERSION);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__USE_DEFAULT_VALUES);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__XJC_ARGS);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__NO_ADDRESS_BINDING);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__XJC_USE_DEFAULT_VALUES);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__XJC_TO_STRING);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__XJC_TO_STRING_MULTI_LINE);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__XJC_TO_STRING_SIMPLE);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__XJC_LOCATOR);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__XJC_SYNC_METHODS);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__XJC_MARK_GENERATED);
        createEAttribute(wsdl2JavaContextEClass, WSDL2_JAVA_CONTEXT__XJC_EPISODE_FILE);

        wsdl2JavaDataModelEClass = createEClass(WSDL2_JAVA_DATA_MODEL);
        createEAttribute(wsdl2JavaDataModelEClass, WSDL2_JAVA_DATA_MODEL__INCLUDED_NAMESPACES);
        createEAttribute(wsdl2JavaDataModelEClass, WSDL2_JAVA_DATA_MODEL__BINDING_FILES);
        createEAttribute(wsdl2JavaDataModelEClass, WSDL2_JAVA_DATA_MODEL__EXCLUDED_NAMESPACES);
        createEAttribute(wsdl2JavaDataModelEClass, WSDL2_JAVA_DATA_MODEL__CATALOG_FILE);
        createEAttribute(wsdl2JavaDataModelEClass, WSDL2_JAVA_DATA_MODEL__JAVA_SOURCE_FOLDER);

        // Create enums
        frontendEEnum = createEEnum(FRONTEND);
        dataBindingEEnum = createEEnum(DATA_BINDING);

        // Create data types
        urlEDataType = createEDataType(URL);
        definitionEDataType = createEDataType(DEFINITION);
        mapEDataType = createEDataType(MAP);
        iMethodEDataType = createEDataType(IMETHOD);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Create type parameters
        addETypeParameter(mapEDataType, "T");
        addETypeParameter(mapEDataType, "T1");

        // Set bounds for type parameters

        // Add supertypes to classes
        cxfDataModelEClass.getESuperTypes().add(this.getCXFContext());
        java2WSContextEClass.getESuperTypes().add(this.getCXFContext());
        java2WSDataModelEClass.getESuperTypes().add(this.getCXFDataModel());
        java2WSDataModelEClass.getESuperTypes().add(this.getJava2WSContext());
        wsdl2JavaContextEClass.getESuperTypes().add(this.getCXFContext());
        wsdl2JavaDataModelEClass.getESuperTypes().add(this.getCXFDataModel());
        wsdl2JavaDataModelEClass.getESuperTypes().add(this.getWSDL2JavaContext());

        // Initialize classes and features; add operations and parameters
        initEClass(cxfContextEClass, CXFContext.class, "CXFContext", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCXFContext_CxfRuntimeLocation(), ecorePackage.getEString(), "cxfRuntimeLocation", null, 0, 1, CXFContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFContext_CxfRuntimeEdition(), ecorePackage.getEString(), "cxfRuntimeEdition", null, 0, 1, CXFContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFContext_CxfRuntimeVersion(), ecorePackage.getEString(), "cxfRuntimeVersion", null, 0, 1, CXFContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFContext_Verbose(), ecorePackage.getEBoolean(), "verbose", "true", 0, 1, CXFContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFContext_GenerateAntBuildFile(), ecorePackage.getEBoolean(), "generateAntBuildFile", "false", 0, 1, CXFContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFContext_GenerateClient(), ecorePackage.getEBoolean(), "generateClient", "false", 0, 1, CXFContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFContext_GenerateServer(), ecorePackage.getEBoolean(), "generateServer", "false", 0, 1, CXFContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFContext_Databinding(), this.getDataBinding(), "databinding", "jaxb", 0, 1, CXFContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFContext_Frontend(), this.getFrontend(), "frontend", "jaxws", 0, 1, CXFContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFContext_UseSpringApplicationContext(), ecorePackage.getEBoolean(), "useSpringApplicationContext", "true", 0, 1, CXFContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(cxfDataModelEClass, CXFDataModel.class, "CXFDataModel", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCXFDataModel_ProjectName(), ecorePackage.getEString(), "projectName", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_ResourceDirectory(), ecorePackage.getEString(), "resourceDirectory", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_ClassDirectory(), ecorePackage.getEString(), "classDirectory", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_WsdlFileName(), ecorePackage.getEString(), "wsdlFileName", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_WsdlURL(), this.getURL(), "wsdlURL", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_ConfigWsdlLocation(), ecorePackage.getEString(), "configWsdlLocation", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_FullyQualifiedJavaClassName(), ecorePackage.getEString(), "fullyQualifiedJavaClassName", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_FullyQualifiedJavaInterfaceName(), ecorePackage.getEString(), "fullyQualifiedJavaInterfaceName", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_ConfigId(), ecorePackage.getEString(), "configId", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_TargetNamespace(), ecorePackage.getEString(), "targetNamespace", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_EndpointName(), ecorePackage.getEString(), "endpointName", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_ServiceName(), ecorePackage.getEString(), "serviceName", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_WsdlDefinition(), this.getDefinition(), "wsdlDefinition", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getCXFDataModel_WsdlLocation(), ecorePackage.getEString(), "wsdlLocation", null, 0, 1, CXFDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(java2WSContextEClass, Java2WSContext.class, "Java2WSContext", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getJava2WSContext_Soap12Binding(), ecorePackage.getEBoolean(), "soap12Binding", "false", 0, 1, Java2WSContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSContext_GenerateXSDImports(), ecorePackage.getEBoolean(), "generateXSDImports", "true", 0, 1, Java2WSContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSContext_GenerateWSDL(), ecorePackage.getEBoolean(), "generateWSDL", "true", 0, 1, Java2WSContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSContext_GenerateWrapperFaultBeans(), ecorePackage.getEBoolean(), "generateWrapperFaultBeans", "true", 0, 1, Java2WSContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSContext_AnnotationProcessingEnabled(), ecorePackage.getEBoolean(), "annotationProcessingEnabled", "false", 0, 1, Java2WSContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSContext_GenerateWebMethodAnnotation(), ecorePackage.getEBoolean(), "generateWebMethodAnnotation", "true", 0, 1, Java2WSContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSContext_GenerateWebParamAnnotation(), ecorePackage.getEBoolean(), "generateWebParamAnnotation", "true", 0, 1, Java2WSContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSContext_GenerateRequestWrapperAnnotation(), ecorePackage.getEBoolean(), "generateRequestWrapperAnnotation", "true", 0, 1, Java2WSContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSContext_GenerateResponseWrapperAnnotation(), ecorePackage.getEBoolean(), "generateResponseWrapperAnnotation", "true", 0, 1, Java2WSContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(java2WSDataModelEClass, Java2WSDataModel.class, "Java2WSDataModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getJava2WSDataModel_Classpath(), ecorePackage.getEString(), "classpath", null, 0, 1, Java2WSDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSDataModel_JavaStartingPoint(), ecorePackage.getEString(), "javaStartingPoint", null, 0, 1, Java2WSDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSDataModel_UseServiceEndpointInterface(), ecorePackage.getEBoolean(), "useServiceEndpointInterface", null, 0, 1, Java2WSDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSDataModel_ExtractInterface(), ecorePackage.getEBoolean(), "extractInterface", null, 0, 1, Java2WSDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSDataModel_ServiceEndpointInterfaceName(), ecorePackage.getEString(), "serviceEndpointInterfaceName", null, 0, 1, Java2WSDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        EGenericType g1 = createEGenericType(this.getMap());
        EGenericType g2 = createEGenericType(this.getIMethod());
        g1.getETypeArguments().add(g2);
        g2 = createEGenericType(this.getMap());
        g1.getETypeArguments().add(g2);
        EGenericType g3 = createEGenericType(ecorePackage.getEString());
        g2.getETypeArguments().add(g3);
        g3 = createEGenericType(ecorePackage.getEBooleanObject());
        g2.getETypeArguments().add(g3);
        initEAttribute(getJava2WSDataModel_MethodMap(), g1, "methodMap", null, 0, 1, Java2WSDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        g1 = createEGenericType(this.getMap());
        g2 = createEGenericType(ecorePackage.getEString());
        g1.getETypeArguments().add(g2);
        g2 = createEGenericType(ecorePackage.getEBooleanObject());
        g1.getETypeArguments().add(g2);
        initEAttribute(getJava2WSDataModel_AnnotationMap(), g1, "annotationMap", null, 0, 1, Java2WSDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSDataModel_SourceDirectory(), ecorePackage.getEString(), "sourceDirectory", null, 0, 1, Java2WSDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getJava2WSDataModel_PortName(), ecorePackage.getEString(), "portName", null, 0, 1, Java2WSDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(wsdl2JavaContextEClass, WSDL2JavaContext.class, "WSDL2JavaContext", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getWSDL2JavaContext_GenerateImplementation(), ecorePackage.getEBoolean(), "generateImplementation", "true", 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_ProcessSOAPHeaders(), ecorePackage.getEBoolean(), "processSOAPHeaders", "false", 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_LoadDefaultNamespacePackageNameMapping(), ecorePackage.getEBoolean(), "loadDefaultNamespacePackageNameMapping", "true", 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_LoadDefaultExcludesNamepsaceMapping(), ecorePackage.getEBoolean(), "loadDefaultExcludesNamepsaceMapping", "true", 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_Validate(), ecorePackage.getEBoolean(), "validate", "true", 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_WsdlVersion(), ecorePackage.getEString(), "wsdlVersion", "1.1", 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_UseDefaultValues(), ecorePackage.getEBoolean(), "useDefaultValues", "true", 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_XjcArgs(), ecorePackage.getEString(), "xjcArgs", null, 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_NoAddressBinding(), ecorePackage.getEBoolean(), "noAddressBinding", null, 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_XjcUseDefaultValues(), ecorePackage.getEBoolean(), "xjcUseDefaultValues", null, 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_XjcToString(), ecorePackage.getEBoolean(), "xjcToString", null, 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_XjcToStringMultiLine(), ecorePackage.getEBoolean(), "xjcToStringMultiLine", null, 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_XjcToStringSimple(), ecorePackage.getEBoolean(), "xjcToStringSimple", null, 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_XjcLocator(), ecorePackage.getEBoolean(), "xjcLocator", null, 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_XjcSyncMethods(), ecorePackage.getEBoolean(), "xjcSyncMethods", null, 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_XjcMarkGenerated(), ecorePackage.getEBoolean(), "xjcMarkGenerated", null, 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaContext_XjcEpisodeFile(), ecorePackage.getEString(), "xjcEpisodeFile", null, 0, 1, WSDL2JavaContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(wsdl2JavaDataModelEClass, WSDL2JavaDataModel.class, "WSDL2JavaDataModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        g1 = createEGenericType(this.getMap());
        g2 = createEGenericType(ecorePackage.getEString());
        g1.getETypeArguments().add(g2);
        g2 = createEGenericType(ecorePackage.getEString());
        g1.getETypeArguments().add(g2);
        initEAttribute(getWSDL2JavaDataModel_IncludedNamespaces(), g1, "includedNamespaces", null, 0, 1, WSDL2JavaDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaDataModel_BindingFiles(), ecorePackage.getEString(), "bindingFiles", null, 0, -1, WSDL2JavaDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        g1 = createEGenericType(this.getMap());
        g2 = createEGenericType(ecorePackage.getEString());
        g1.getETypeArguments().add(g2);
        g2 = createEGenericType(ecorePackage.getEString());
        g1.getETypeArguments().add(g2);
        initEAttribute(getWSDL2JavaDataModel_ExcludedNamespaces(), g1, "excludedNamespaces", null, 0, 1, WSDL2JavaDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaDataModel_CatalogFile(), ecorePackage.getEString(), "catalogFile", null, 0, 1, WSDL2JavaDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getWSDL2JavaDataModel_JavaSourceFolder(), ecorePackage.getEString(), "javaSourceFolder", null, 0, 1, WSDL2JavaDataModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        // Initialize enums and add enum literals
        initEEnum(frontendEEnum, Frontend.class, "Frontend");
        addEEnumLiteral(frontendEEnum, Frontend.JAXWS);

        initEEnum(dataBindingEEnum, DataBinding.class, "DataBinding");
        addEEnumLiteral(dataBindingEEnum, DataBinding.JAXB);

        // Initialize data types
        initEDataType(urlEDataType, java.net.URL.class, "URL", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(definitionEDataType, Definition.class, "Definition", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(mapEDataType, Map.class, "Map", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
        initEDataType(iMethodEDataType, IMethod.class, "IMethod", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);
    }

} //CXFPackageImpl
