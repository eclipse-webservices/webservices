package org.eclipse.jst.ws.internal.cxf.core.utils;

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
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.AnnotationsManager;
import org.eclipse.jst.ws.annotations.core.initialization.IAnnotationAttributeInitializer;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.cxf.core.CXFCorePlugin;
import org.eclipse.jst.ws.internal.cxf.core.model.Java2WSDataModel;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.TextEdit;

/**
 * Interim class to aid in refactoring core utilities into jaxws core.
 * @author sclarke
 *
 */
public final class CXFModelUtils {
    public static final String WEB_SERVICE = "WebService"; //$NON-NLS-1$
    public static final String WEB_METHOD = "WebMethod"; //$NON-NLS-1$
    public static final String WEB_PARAM = "WebParam"; //$NON-NLS-1$
    public static final String REQUEST_WRAPPER = "RequestWrapper"; //$NON-NLS-1$
    public static final String RESPONSE_WRAPPER = "ResponseWrapper"; //$NON-NLS-1$

    private static Map<String, String> ANNOTATION_TYPENAME_MAP = new HashMap<String, String>();

    static {
        ANNOTATION_TYPENAME_MAP.put("ServiceMode", "javax.xml.ws.ServiceMode"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put("WebFault", "javax.xml.ws.WebFault"); //$NON-NLS-1$ //$NON-NLS-2$
        ANNOTATION_TYPENAME_MAP.put(REQUEST_WRAPPER, "javax.xml.ws.RequestWrapper"); //$NON-NLS-1$
        ANNOTATION_TYPENAME_MAP.put(RESPONSE_WRAPPER, "javax.xml.ws.ResponseWrapper"); //$NON-NLS-1$ //$NON-NLS-2$
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

        CompilationUnit compilationUnit = AnnotationUtils.getASTParser(source);
        
        AST ast = compilationUnit.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);
        
        IAnnotationAttributeInitializer annotationAttributeInitializer = 
            AnnotationsManager.getAnnotationDefinitionForClass(WebService.class).
                getAnnotationAttributeInitializer();
        
        List<MemberValuePair> memberValuePairs = annotationAttributeInitializer.getMemberValuePairs(type, ast,
                WebService.class);
        
        if (model.isUseServiceEndpointInterface() && type.isClass()) {
            MemberValuePair endpointInterfaceValuePair = AnnotationsCore.createStringMemberValuePair(ast, 
                    "endpointInterface", model.getFullyQualifiedJavaInterfaceName());
            memberValuePairs.add(1, endpointInterfaceValuePair);
        }
        
        Annotation annotation = AnnotationsCore.createAnnotation(ast, WebService.class,
                WebService.class.getSimpleName(), memberValuePairs);
        
        AnnotationUtils.createTypeAnnotationChange(source, compilationUnit, rewriter, annotation, 
                textFileChange);
    }

    public static void createMethodAnnotationChange(IType type, IMethod method, 
            Class<? extends java.lang.annotation.Annotation> annotationClass, TextFileChange textFileChange) 
                throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = AnnotationUtils.getASTParser(source);

        AST ast = compilationUnit.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);

        Annotation annotation = getAnnotation(method, ast, annotationClass);

        AnnotationUtils.createMethodAnnotationChange(source, compilationUnit, rewriter, method,
                annotation, textFileChange);
    }

    public static void getWebMethodAnnotationChange(IType type, IMethod method, 
            TextFileChange textFileChange) throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = AnnotationUtils.getASTParser(source);

        AST ast = compilationUnit.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);

        Annotation annotation = getAnnotation(method, ast, WebMethod.class);

        AnnotationUtils.createMethodAnnotationChange(source, compilationUnit, rewriter, method,
                annotation, textFileChange);
    }
    
    public static void getRequestWrapperAnnotationChange(IType type, IMethod method, 
            TextFileChange textFileChange) throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = AnnotationUtils.getASTParser(source);

        AST ast = compilationUnit.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);

        Annotation annotation = getAnnotation(method, ast, RequestWrapper.class);

        AnnotationUtils.createMethodAnnotationChange(source, compilationUnit, rewriter, method, 
                annotation, textFileChange);
    }

    public static void getResponseWrapperAnnotationChange(IType type, IMethod method,
            TextFileChange textFileChange) throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = AnnotationUtils.getASTParser(source);

        AST ast = compilationUnit.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);

        Annotation annotation = getAnnotation(method, ast, ResponseWrapper.class);

        AnnotationUtils.createMethodAnnotationChange(source, compilationUnit, rewriter, method, 
                annotation, textFileChange);
    }
    
    public static void getWebParamAnnotationChange(IType type, final IMethod method, 
            SingleVariableDeclaration parameter, TextFileChange textFileChange) 
            throws CoreException {
        ICompilationUnit source = type.getCompilationUnit();
        CompilationUnit compilationUnit = AnnotationUtils.getASTParser(source);

        AST ast = compilationUnit.getAST();
        ASTRewrite rewriter = ASTRewrite.create(ast);

        Annotation annotation = getAnnotation(parameter, ast, WebParam.class);

        AnnotationUtils.createMethodParameterAnnotationChange(source, compilationUnit, 
                rewriter, parameter, method, annotation, textFileChange);
    }
    
    private static Annotation getAnnotation(IJavaElement javaElement, AST ast, 
                Class<? extends java.lang.annotation.Annotation> annotationClass) {
        
        IAnnotationAttributeInitializer annotationAttributeInitializer = AnnotationsManager.
            getAnnotationDefinitionForClass(annotationClass).getAnnotationAttributeInitializer();
        
        List<MemberValuePair> memberValuePairs = 
                annotationAttributeInitializer.getMemberValuePairs(javaElement, ast, annotationClass);
        
        return AnnotationsCore.createAnnotation(ast, annotationClass, annotationClass.getSimpleName(),
                memberValuePairs);
    }

    private static Annotation getAnnotation(ASTNode astNode, AST ast, 
                Class<? extends java.lang.annotation.Annotation> annotationClass) {
        
        IAnnotationAttributeInitializer annotationAttributeInitializer = AnnotationsManager.
            getAnnotationDefinitionForClass(annotationClass).getAnnotationAttributeInitializer();
        
        List<MemberValuePair> memberValuePairs = annotationAttributeInitializer.getMemberValuePairs(astNode, 
                ast, annotationClass);
        
        return AnnotationsCore.createAnnotation(ast, annotationClass, annotationClass.getSimpleName(),
                memberValuePairs);
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
                if (JDTUtils.isPublicMethod(method)) {
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
}
