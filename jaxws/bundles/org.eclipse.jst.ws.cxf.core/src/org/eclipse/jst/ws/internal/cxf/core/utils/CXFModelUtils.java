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
package org.eclipse.jst.ws.internal.cxf.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jdt.ui.SharedASTProvider;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.AnnotationsManager;
import org.eclipse.jst.ws.annotations.core.initialization.IAnnotationAttributeInitializer;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.CXFPackage;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.TextEdit;
import org.osgi.framework.Version;

/**
 * Provides utility methods for working with the CXF model.
 * Also serving as an interim class to aid in refactoring core utilities into jaxws core.
 *
 */
public final class CXFModelUtils {
    private static final Version v_0_0_0 = new Version("0.0.0");
    private static final Version v_2_1 = new Version(CXFCorePlugin.CXF_VERSION_2_1);
    private static final Version v_2_0_7 = new Version("2.0.7");
    private static final Version v_2_1_1 = new Version("2.1.1");

    public static final String WEB_SERVICE = "WebService"; //$NON-NLS-1$
    public static final String WEB_METHOD = "WebMethod"; //$NON-NLS-1$
    public static final String WEB_PARAM = "WebParam"; //$NON-NLS-1$
    public static final String REQUEST_WRAPPER = "RequestWrapper"; //$NON-NLS-1$
    public static final String RESPONSE_WRAPPER = "ResponseWrapper"; //$NON-NLS-1$

    private static final String ENDPOINT_INTERFACE = "endpointInterface"; //$NON-NLS-1$

    private static Map<String, String> ANNOTATION_TYPENAME_MAP = new HashMap<String, String>();

    static {
        ANNOTATION_TYPENAME_MAP.put("ServiceMode", "javax.xml.ws.ServiceMode"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put("WebFault", "javax.xml.ws.WebFault"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put(REQUEST_WRAPPER, "javax.xml.ws.RequestWrapper"); //$NON-NLS-1$
        ANNOTATION_TYPENAME_MAP.put(RESPONSE_WRAPPER, "javax.xml.ws.ResponseWrapper"); //$NON-NLS-1$
        ANNOTATION_TYPENAME_MAP.put("WebServiceClient", "javax.xml.ws.WebServiceClient"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put("WebEndpoint", "javax.xml.ws.WebEndpoint"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put("WebServiceProvider", "javax.xml.ws.WebServiceProvider"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put("BindingType", "javax.xml.ws.BindingType"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put("WebServiceRef", "javax.xml.ws.WebServiceRef"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put("WebServiceRefs", "javax.xml.ws.WebServiceRefs"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put(WEB_SERVICE, "javax.jws.WebService"); //$NON-NLS-1$
        ANNOTATION_TYPENAME_MAP.put(WEB_METHOD, "javax.jws.WebMethod"); //$NON-NLS-1$
        ANNOTATION_TYPENAME_MAP.put("Oneway", "javax.jws.OneWay"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put(WEB_PARAM, "javax.jws.WebParam"); //$NON-NLS-1$
        ANNOTATION_TYPENAME_MAP.put("WebResult", "javax.jws.WebResult"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put("SOAPBinding", "javax.jws.SOAPBinding"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put("HandlerChain", "javax.jws.HandlerChain"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private CXFModelUtils() {
    }

    public static void getWebServiceAnnotationChange(IType type, Java2WSDataModel model,
            TextFileChange textFileChange) throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        AST ast = compilationUnit.getAST();

        NormalAnnotation webServiceAnnotation = getAnnotation(type, WebService.class);
        if (webServiceAnnotation != null && model.isUseServiceEndpointInterface() && type.isClass()) {
            MemberValuePair endpointInterface = getMemberValuePair(webServiceAnnotation, ENDPOINT_INTERFACE);
            if (endpointInterface != null && endpointInterface.getValue() instanceof StringLiteral) {
                StringLiteral stringLiteral = (StringLiteral) endpointInterface.getValue();
                if (!stringLiteral.getLiteralValue().equals(model.getServiceEndpointInterfaceName())) {
                    ASTNode newSEIValue = AnnotationsCore.createStringLiteral(ast, model
                            .getServiceEndpointInterfaceName());

                    textFileChange.addEdit(AnnotationUtils.createUpdateMemberValuePairTextEdit(endpointInterface, newSEIValue));
                }
            } else {
                MemberValuePair endpointInterfacePair = AnnotationsCore.createMemberValuePair(ast,
                        ENDPOINT_INTERFACE, AnnotationsCore.createStringLiteral(ast, model
                                .getServiceEndpointInterfaceName()));

                textFileChange.addEdit(AnnotationUtils.createAddMemberValuePairTextEdit(webServiceAnnotation, endpointInterfacePair));
            }
        } else {
            List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

            IAnnotationAttributeInitializer annotationAttributeInitializer =
                AnnotationsManager.getAnnotationDefinitionForClass(WebService.class).
                getAnnotationAttributeInitializer();

            if (annotationAttributeInitializer != null) {
                memberValuePairs = annotationAttributeInitializer.getMemberValuePairs(type, ast,
                        WebService.class);
            }

            if (model.isUseServiceEndpointInterface() && type.isClass()) {
                MemberValuePair endpointInterfaceValuePair = AnnotationsCore.createStringMemberValuePair(ast,
                        ENDPOINT_INTERFACE, model.getServiceEndpointInterfaceName());
                memberValuePairs.add(1, endpointInterfaceValuePair);
            }

            Annotation annotation = AnnotationsCore.createNormalAnnotation(ast, WebService.class.getSimpleName(), memberValuePairs);

            textFileChange.addEdit(AnnotationUtils.createAddAnnotationTextEdit(source.findPrimaryType(), annotation));
        }
    }

    private static NormalAnnotation getAnnotation(IType type, Class<? extends java.lang.annotation.Annotation> annotation) {
        Annotation jdtAnnotation = AnnotationUtils.getAnnotation(type, annotation);
        if (jdtAnnotation != null && jdtAnnotation instanceof NormalAnnotation) {
            return (NormalAnnotation) jdtAnnotation;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static MemberValuePair getMemberValuePair(NormalAnnotation annotation, String memberName) {
        List<MemberValuePair> memberValuePairs = annotation.values();
        for (MemberValuePair memberValuePair : memberValuePairs) {
            if (memberValuePair.getName().getIdentifier().equals(memberName)) {
                return memberValuePair;
            }
        }
        return null;
    }

    public static void getWebMethodAnnotationChange(IType type, IMethod method,
            TextFileChange textFileChange) throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        AST ast = compilationUnit.getAST();

        Annotation annotation = getAnnotation(method, ast, WebMethod.class);

        textFileChange.addEdit(AnnotationUtils.createAddAnnotationTextEdit(method, annotation));
    }

    public static void getRequestWrapperAnnotationChange(IType type, IMethod method,
            TextFileChange textFileChange) throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        AST ast = compilationUnit.getAST();

        Annotation annotation = getAnnotation(method, ast, RequestWrapper.class);

        textFileChange.addEdit(AnnotationUtils.createAddAnnotationTextEdit(method, annotation));
    }

    public static void getResponseWrapperAnnotationChange(IType type, IMethod method,
            TextFileChange textFileChange) throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        AST ast = compilationUnit.getAST();

        Annotation annotation = getAnnotation(method, ast, ResponseWrapper.class);

        textFileChange.addEdit(AnnotationUtils.createAddAnnotationTextEdit(method, annotation));
    }

    public static void getWebParamAnnotationChange(IType type, final IMethod method,
            ILocalVariable parameter, TextFileChange textFileChange)
    throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = SharedASTProvider.getAST(source, SharedASTProvider.WAIT_YES, null);

        AST ast = compilationUnit.getAST();

        Annotation annotation = getAnnotation(parameter, ast, WebParam.class);

        textFileChange.addEdit(AnnotationUtils.createAddAnnotationTextEdit(parameter, annotation));
    }

    private static Annotation getAnnotation(IJavaElement javaElement, AST ast,
            Class<? extends java.lang.annotation.Annotation> annotationClass) {

        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        IAnnotationAttributeInitializer annotationAttributeInitializer = AnnotationsManager
        .getAnnotationDefinitionForClass(annotationClass).getAnnotationAttributeInitializer();

        if (annotationAttributeInitializer != null) {
            memberValuePairs = annotationAttributeInitializer.getMemberValuePairs(javaElement, ast, annotationClass);
        }

        return AnnotationsCore.createNormalAnnotation(ast, annotationClass.getSimpleName(), memberValuePairs);
    }

    public static void getImportsChange(ICompilationUnit compilationUnit, Java2WSDataModel model,
            TextFileChange textFileChange, boolean classOnly) {
        try {

            ImportRewrite importRewrite = CodeStyleConfiguration.createImportRewrite(compilationUnit, true);

            importRewrite.addImport(ANNOTATION_TYPENAME_MAP.get(WEB_SERVICE));

            if (!classOnly) {
                Map<IMethod, Map<String, Boolean>> methodAnnotationMap = model.getMethodMap();
                Set<Entry<IMethod, Map<String, Boolean>>> methodAnnotationSet = methodAnnotationMap.entrySet();
                for (Map.Entry<IMethod, Map<String, Boolean>> methodAnnotation : methodAnnotationSet) {
                    Map<String, Boolean> methodMap = methodAnnotation.getValue();
                    Set<Entry<String, Boolean>> methodSet = methodMap.entrySet();
                    for (Map.Entry<String, Boolean> method : methodSet) {
                        if (ANNOTATION_TYPENAME_MAP.containsKey(method.getKey()) && method.getValue()) {
                            importRewrite.addImport(ANNOTATION_TYPENAME_MAP.get(method.getKey()));
                        }
                    }
                }
            }
            if (importRewrite.hasRecordedChanges()) {
                TextEdit importTextEdit = importRewrite.rewriteImports(null);
                textFileChange.addEdit(importTextEdit);
            }
        } catch (CoreException ce) {
            CXFCorePlugin.log(ce.getStatus());
        }
    }

    /**
     * Loads all public methods with the default annotation maps
     * @return
     */
    public static Map<IMethod, Map<String, Boolean>> getMethodMap(IType type, Java2WSDataModel model) {
        Map<IMethod, Map<String, Boolean>> methodMap = new HashMap<IMethod, Map<String, Boolean>>();

        try {
            IMethod[] methods = type.getMethods();
            for (IMethod method : methods) {
                if (type.isInterface()) {
                    methodMap.put(method, getAnnotationMap(model));
                } else if (type.isClass() && JDTUtils.isPublicMethod(method)) {
                    methodMap.put(method, getAnnotationMap(model));
                }
            }
        } catch (JavaModelException jme) {
            CXFCorePlugin.log(jme.getStatus());
        }
        return methodMap;
    }

    public static Map<String, Boolean> getAnnotationMap(Java2WSDataModel model) {
        Map<String, Boolean> annotationdMap = new HashMap<String, Boolean>();
        annotationdMap.put(CXFModelUtils.WEB_METHOD, model.isGenerateWebMethodAnnotation());
        annotationdMap.put(CXFModelUtils.WEB_PARAM, model.isGenerateWebParamAnnotation());
        annotationdMap.put(CXFModelUtils.REQUEST_WRAPPER, model.isGenerateRequestWrapperAnnotation());
        annotationdMap.put(CXFModelUtils.RESPONSE_WRAPPER, model.isGenerateResponseWrapperAnnotation());
        return annotationdMap;
    }

    /**
     * CXF wsdl2java -autoNameResolution is supported in the CXF 2.0 stream from 2.0.7
     * and in the CXF 2.1 stream from 2.1.1 up.
     *
     * @param cxfRuntimeVersion
     * @return
     */
    public static boolean isAutoNameResolutionPermitted() {
        Version currentVersion = CXFCorePlugin.getDefault().getCurrentRuntimeVersion();
        //On startup with clean workspace show by default.
        if (currentVersion.compareTo(v_0_0_0) == 0) {
            return true;
        }

        if (currentVersion.compareTo(CXFModelUtils.v_2_1_1) >= 0) {
            return true;
        }

        if (currentVersion.compareTo(CXFModelUtils.v_2_0_7) >= 0
                && currentVersion.compareTo(CXFModelUtils.v_2_1) < 0) {
            return true;
        }
        return false;
    }

    public static boolean getDefaultBooleanValue(int classifierID, int featureID) {
        Object defaultValue = null;

        if (classifierID == CXFPackage.CXF_CONTEXT) {
            switch (featureID) {
            case CXFPackage.CXF_CONTEXT__EXPORT_CXF_CLASSPATH_CONTAINER:
                defaultValue = CXFPackage.eINSTANCE.getCXFContext_ExportCXFClasspathContainer()
                .getDefaultValue();
                break;
            case CXFPackage.CXF_CONTEXT__VERBOSE:
                defaultValue = CXFPackage.eINSTANCE.getCXFContext_Verbose().getDefaultValue();
                break;
            case CXFPackage.CXF_CONTEXT__GENERATE_ANT_BUILD_FILE:
                defaultValue = CXFPackage.eINSTANCE.getCXFContext_GenerateAntBuildFile().getDefaultValue();
                break;
            case CXFPackage.CXF_CONTEXT__USE_SPRING_APPLICATION_CONTEXT:
                defaultValue = CXFPackage.eINSTANCE.getCXFContext_UseSpringApplicationContext()
                .getDefaultValue();
                break;
            case CXFPackage.CXF_CONTEXT__GENERATE_CLIENT:
                defaultValue = CXFPackage.eINSTANCE.getCXFContext_GenerateClient().getDefaultValue();
                break;
            case CXFPackage.CXF_CONTEXT__GENERATE_SERVER:
                defaultValue = CXFPackage.eINSTANCE.getCXFContext_GenerateServer().getDefaultValue();
                break;
            }
        }

        if (classifierID == CXFPackage.JAVA2_WS_CONTEXT) {
            switch (featureID) {
            case CXFPackage.JAVA2_WS_CONTEXT__ANNOTATION_PROCESSING_ENABLED:
                defaultValue = CXFPackage.eINSTANCE.getJava2WSContext_AnnotationProcessingEnabled()
                .getDefaultValue();
                break;
            case CXFPackage.JAVA2_WS_CONTEXT__SOAP12_BINDING:
                defaultValue = CXFPackage.eINSTANCE.getJava2WSContext_Soap12Binding().getDefaultValue();
                break;
            case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_XSD_IMPORTS:
                defaultValue = CXFPackage.eINSTANCE.getJava2WSContext_GenerateXSDImports().getDefaultValue();
                break;
            case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WRAPPER_FAULT_BEANS:
                defaultValue = CXFPackage.eINSTANCE.getJava2WSContext_GenerateWrapperFaultBeans()
                .getDefaultValue();
                break;
            case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WSDL:
                defaultValue = CXFPackage.eINSTANCE.getJava2WSContext_GenerateWSDL().getDefaultValue();
                break;
            case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WEB_METHOD_ANNOTATION:
                defaultValue = CXFPackage.eINSTANCE.getJava2WSContext_GenerateWebMethodAnnotation()
                .getDefaultValue();
                break;
            case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_WEB_PARAM_ANNOTATION:
                defaultValue = CXFPackage.eINSTANCE.getJava2WSContext_GenerateWebParamAnnotation()
                .getDefaultValue();
                break;
            case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_REQUEST_WRAPPER_ANNOTATION:
                defaultValue = CXFPackage.eINSTANCE.getJava2WSContext_GenerateRequestWrapperAnnotation()
                .getDefaultValue();
                break;
            case CXFPackage.JAVA2_WS_CONTEXT__GENERATE_RESPONSE_WRAPPER_ANNOTATION:
                defaultValue = CXFPackage.eINSTANCE.getJava2WSContext_GenerateResponseWrapperAnnotation()
                .getDefaultValue();
                break;
            }
        }

        if (classifierID == CXFPackage.WSDL2_JAVA_CONTEXT) {
            switch (featureID) {
            case CXFPackage.WSDL2_JAVA_CONTEXT__NO_ADDRESS_BINDING:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_NoAddressBinding().getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__USE_DEFAULT_VALUES:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_UseDefaultValues().getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__AUTO_NAME_RESOLUTION:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_AutoNameResolution()
                .getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__GENERATE_IMPLEMENTATION:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_GenerateImplementation()
                .getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__PROCESS_SOAP_HEADERS:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_ProcessSOAPHeaders()
                .getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__GENERATE_ANT_BUILD_FILE:
                defaultValue = CXFPackage.eINSTANCE.getCXFContext_GenerateAntBuildFile().getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__VALIDATE:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_Validate().getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_NAMESPACE_PACKAGE_NAME_MAPPING:
                defaultValue = CXFPackage.eINSTANCE
                .getWSDL2JavaContext_LoadDefaultNamespacePackageNameMapping().getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__LOAD_DEFAULT_EXCLUDES_NAMEPSACE_MAPPING:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_LoadDefaultExcludesNamepsaceMapping()
                .getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_USE_DEFAULT_VALUES:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcUseDefaultValues()
                .getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcToString().getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING_MULTI_LINE:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcToStringMultiLine()
                .getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_TO_STRING_SIMPLE:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcToStringSimple().getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_LOCATOR:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcLocator().getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_SYNC_METHODS:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcSyncMethods().getDefaultValue();
                break;
            case CXFPackage.WSDL2_JAVA_CONTEXT__XJC_MARK_GENERATED:
                defaultValue = CXFPackage.eINSTANCE.getWSDL2JavaContext_XjcMarkGenerated().getDefaultValue();
                break;
            }
        }

        if (defaultValue != null && defaultValue instanceof Boolean) {
            return ((Boolean) defaultValue).booleanValue();
        }

        return false;
    }

}
