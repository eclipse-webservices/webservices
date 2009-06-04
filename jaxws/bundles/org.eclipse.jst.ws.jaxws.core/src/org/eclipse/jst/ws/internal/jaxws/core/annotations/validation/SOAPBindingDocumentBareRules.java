/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.core.annotations.validation;

import java.util.Collection;

import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.xml.ws.Holder;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.TypeMirror;

/**
 * 
 * @author sclarke
 *
 */
public class SOAPBindingDocumentBareRules extends AbstractAnnotationProcessor {
    
    private static final String SOAP_BINDING_STYLE = "style"; //$NON-NLS-1$
    private static final String SOAP_BINDING_USE = "use"; //$NON-NLS-1$
    private static final String SOAP_BINDING_PARAMETER_STYLE = "parameterStyle"; //$NON-NLS-1$
    
    private static final String WEB_PARAM_MODE = "mode"; //$NON-NLS-1$
    private static final String WEB_PARAM_MODE_IN = "IN"; //$NON-NLS-1$
    private static final String WEB_PARAM_MODE_OUT = "OUT"; //$NON-NLS-1$
    private static final String WEB_PARAM_MODE_INOUT = "INOUT"; //$NON-NLS-1$
    
    private static final String WEB_PARAM_HEADER = "header"; //$NON-NLS-1$
    
    private static final String WEBPARAM = "javax.jws.WebParam"; //$NON-NLS-1$
    private static final String ONEWAY = "javax.jws.Oneway"; //$NON-NLS-1$
    
    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
        .getTypeDeclaration(SOAPBinding.class.getName());

        Collection<Declaration> annotatedTypes = environment
                .getDeclarationsAnnotatedWith(annotationDeclaration);
        
        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof TypeDeclaration) {
                TypeDeclaration typeDeclaration = (TypeDeclaration) declaration;
                Collection<AnnotationMirror> annotationMirrors = typeDeclaration.getAnnotationMirrors();
                for (AnnotationMirror mirror : annotationMirrors) {
                    if (isDocumentBare(mirror, annotationDeclaration)) {
                        Collection<? extends MethodDeclaration> methodDeclarations = typeDeclaration.getMethods();
                        for (MethodDeclaration methodDeclaration : methodDeclarations) {
                            processMethod(methodDeclaration);    
                        }
                    }
                }
            }
            
            if (declaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
                Collection<AnnotationMirror> annotationMirrors = methodDeclaration.getAnnotationMirrors();
                for (AnnotationMirror mirror : annotationMirrors) {
                    if (isDocumentBare(mirror, annotationDeclaration)) {
                        processMethod((MethodDeclaration) declaration);
                    }
                }
            }
        }
    }

    public void processMethod(MethodDeclaration methodDeclaration) {
        Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
        
        //@Oneway operations
        if (isOneway(methodDeclaration) && !isSingleNonHeaderINParameter(parameters)) {
            printError(methodDeclaration.getPosition(), 
                JAXWSCoreMessages.DOC_BARE_ONLY_ONE_NON_HEADER_IN_PARAMETER_ERROR_MESSAGE);                                
        } else {
            if (isVoidReturnType(methodDeclaration)) {
                if (countINParameters(parameters) > 1) {
                    printError(methodDeclaration.getPosition(), 
                            JAXWSCoreMessages.DOC_BARE_VOID_RETURN_ONE_IN_PARAMETER);                                                                        
                }
                if (countOUTParameters(parameters) > 1) {
                    printError(methodDeclaration.getPosition(), 
                            JAXWSCoreMessages.DOC_BARE_VOID_RETURN_ONE_OUT_PARAMETER);                                                                                            
                }
            } else {
                if (countINParameters(parameters) > 1) {
                    printError(methodDeclaration.getPosition(), 
                        JAXWSCoreMessages.DOC_BARE_ONLY_ONE_NON_HEADER_IN_PARAMETER_ERROR_MESSAGE);                                                                        
                } 
                if (countOUTParameters(parameters) > 0) {
                    printError(methodDeclaration.getPosition(), 
                        JAXWSCoreMessages.DOC_BARE_NON_VOID_RETURN_NO_INOUT_OUT_PARAMETER);
                }
            }
        }
                    
        //check for @WebParam.name attribute when @WebParam.Mode = OUT or INOUT
        for(ParameterDeclaration parameterDeclaration : parameters) {
            Collection<AnnotationMirror> aannotationMirrors = parameterDeclaration.getAnnotationMirrors();
            for (AnnotationMirror annotationMirror : aannotationMirrors) {
                if (annotationMirror.getAnnotationType().getDeclaration().getQualifiedName().equals(WEBPARAM)) {
                    String mode = getWebParamMode(annotationMirror, parameterDeclaration);
                    String name = AnnotationUtils.findAnnotationValue(annotationMirror, "name");
                    if (name.length() == 0 && (mode.equals(WEB_PARAM_MODE_OUT) || mode.equals(WEB_PARAM_MODE_INOUT))) {
                        printError(annotationMirror.getPosition(), 
                           JAXWSCoreMessages.WEBPARAM_NAME_REQUIRED_WHEN_DOC_BARE_OUT_INOUT);
                    }
                }
            }
        }
    }
    
    private boolean isDocumentBare(AnnotationMirror mirror, AnnotationTypeDeclaration annotationDeclaration) {
        if (mirror.getAnnotationType().getDeclaration().equals(annotationDeclaration)) {
            String document = AnnotationUtils.findAnnotationValue(mirror, SOAP_BINDING_STYLE);
            String use = AnnotationUtils.findAnnotationValue(mirror, SOAP_BINDING_USE);
            String parameterStyle = AnnotationUtils.findAnnotationValue(mirror, SOAP_BINDING_PARAMETER_STYLE);
            
            return (document.length() == 0 || document.equals(Style.DOCUMENT.name()))
                    && (use.length() == 0 || use.equals(Use.LITERAL.name()))
                    && (parameterStyle.length() == 0 || parameterStyle.equals(ParameterStyle.BARE.name()));
        }
        return false;
    }
        
    private boolean isOneway(MethodDeclaration methodDeclaration) {
        Collection<AnnotationMirror> annotationMirrors = methodDeclaration.getAnnotationMirrors();
        for (AnnotationMirror mirror : annotationMirrors) {
            if (mirror.getAnnotationType().getDeclaration().getQualifiedName().equals(ONEWAY)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isVoidReturnType(MethodDeclaration methodDeclaration) {
        return methodDeclaration.getReturnType().equals(environment.getTypeUtils().getVoidType());
    }
    
    private boolean isSingleNonHeaderINParameter(Collection<ParameterDeclaration> parameters) {
        return countNonHeaderINParameters(parameters) <= 1;
    }
    
    private int countNonHeaderINParameters(Collection<ParameterDeclaration> parameters) {
        int inNonHeaderParameters = 0;
        for (ParameterDeclaration parameterDeclaration : parameters) {
            if (isNonHeaderINParameter(parameterDeclaration)) {
                inNonHeaderParameters++;
            }
        }
        return inNonHeaderParameters;
    }
    
    private boolean isNonHeaderINParameter(ParameterDeclaration parameterDeclaration) {
        Collection<AnnotationMirror> annotationMirrors = parameterDeclaration.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            if (annotationMirror.getAnnotationType().getDeclaration().getQualifiedName().equals(WEBPARAM)) {
                return getWebParamMode(annotationMirror, parameterDeclaration).equals(WEB_PARAM_MODE_IN)
                        && !isHeader(annotationMirror);
            }
        }
        
        if (getDefaultWebParamMode(parameterDeclaration).equals(WEB_PARAM_MODE_IN)) {
            return true;
        }
        
        return false;
    }
    
    private int countINParameters(Collection<ParameterDeclaration> parameters) {
        int inNonHeaderParameters = 0;
        for (ParameterDeclaration parameterDeclaration : parameters) {
            if (isINParameter(parameterDeclaration)) {
                inNonHeaderParameters++;
            }
        }
        return inNonHeaderParameters;
    }
 
    private boolean isINParameter(ParameterDeclaration parameterDeclaration) {
        Collection<AnnotationMirror> annotationMirrors = parameterDeclaration.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            if (annotationMirror.getAnnotationType().getDeclaration().getQualifiedName().equals(WEBPARAM)) {              
                String mode = getWebParamMode(annotationMirror, parameterDeclaration);
                return (mode.equals(WEB_PARAM_MODE_IN) || mode.equals(WEB_PARAM_MODE_INOUT)) 
                        && !isHeader(annotationMirror);
            }
        }
        
        String defaultMode = getDefaultWebParamMode(parameterDeclaration);
        if (defaultMode.equals(WEB_PARAM_MODE_IN) || defaultMode.equals(WEB_PARAM_MODE_INOUT)) {
            return true;
        }

        return false;
    }

    private int countOUTParameters(Collection<ParameterDeclaration> parameters) {
        int outNonHeaderParameters = 0;
        for (ParameterDeclaration parameterDeclaration : parameters) {
            if (isOUTParameter(parameterDeclaration)) {
                outNonHeaderParameters++;
            }
        }
        return outNonHeaderParameters;
    }

    private boolean isOUTParameter(ParameterDeclaration parameterDeclaration) {
        Collection<AnnotationMirror> annotationMirrors = parameterDeclaration.getAnnotationMirrors();
        for (AnnotationMirror annotationMirror : annotationMirrors) {
            if (annotationMirror.getAnnotationType().getDeclaration().getQualifiedName().equals(WEBPARAM)) {              
                String mode = getWebParamMode(annotationMirror, parameterDeclaration);
                return (mode.equals(WEB_PARAM_MODE_OUT) || mode.equals(WEB_PARAM_MODE_INOUT)) 
                        && !isHeader(annotationMirror);
            }
        }
        
        if (getDefaultWebParamMode(parameterDeclaration).equals(WEB_PARAM_MODE_INOUT)) {
            return true;
        }

        return false;
    }

    private boolean isHeader(AnnotationMirror annotationMirror) {
        String header = AnnotationUtils.findAnnotationValue(annotationMirror, WEB_PARAM_HEADER);
        if (header.length() == 0) {
           header = "false"; //$NON-NLS-1$
        }
        return Boolean.valueOf(header);
    }
    

    private String getWebParamMode(AnnotationMirror annotationMirror, ParameterDeclaration parameterDeclaration) {
        String mode = AnnotationUtils.findAnnotationValue(annotationMirror, WEB_PARAM_MODE);
        if (mode.length() == 0) {
            mode = getDefaultWebParamMode(parameterDeclaration);
        }
        return mode;
    }
    
    private String getDefaultWebParamMode(ParameterDeclaration parameterDeclaration) {
        TypeMirror typeMirror = environment.getTypeUtils().getErasure(parameterDeclaration.getType());
        if (typeMirror.toString().equals(Holder.class.getCanonicalName())) {
            return WEB_PARAM_MODE_INOUT;
        }
        return WEB_PARAM_MODE_IN;
    }
    
}
