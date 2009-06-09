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

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.ParameterDeclaration;

/**
 * 
 * @author sclarke
 * 
 */
public class WebServiceSEIPresentRestrictionsApplyRule extends AbstractJAXWSAnnotationProcessor {

    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(WebService.class.getName());
        Collection<Declaration> annotatedTypes = environment
                .getDeclarationsAnnotatedWith(annotationDeclaration);
        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof ClassDeclaration) {
                Collection<AnnotationMirror> annotationMirrors = declaration.getAnnotationMirrors();
                for (AnnotationMirror mirror : annotationMirrors) {
                    AnnotationValue endpointInterface = AnnotationUtils.getAnnotationValue(mirror,
                            ENDPOINT_INTERFACE);
                    if (endpointInterface != null) {
//                        AnnotationValue name = AnnotationUtils.getAnnotationValue(mirror, "name");
//                        if (name != null) {
//                            printError(
//                                    name.getPosition(),
//                                    JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_NAME_ATTRIBUTE_ERROR_MESSAGE);
//                        }
                        checkRestrictions((ClassDeclaration) declaration, endpointInterface);
                        checkJSR181Annotations((ClassDeclaration) declaration);
                    }
                }
            }
        }
    }

    private void checkRestrictions(ClassDeclaration classDeclaration, AnnotationValue endpointInterface) {
        com.sun.mirror.declaration.TypeDeclaration typeDeclaration = environment
                .getTypeDeclaration(endpointInterface.getValue().toString());
        if (typeDeclaration != null) {
            if (typeDeclaration.getDeclaringType() != null) {
                printError(endpointInterface.getPosition(),
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NOT_OUTER_ERROR_MESSAGE);
            }
            AnnotationMirror annotationMirror = AnnotationUtils.getAnnotation(typeDeclaration,
                    WebService.class);
            if (annotationMirror == null) {
                printError(endpointInterface.getPosition(),
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NOT_ANNOTATED_ERROR_MESSAGE);
            }

            if (!typeDeclaration.getModifiers().contains(Modifier.PUBLIC)) {
                printError(endpointInterface.getPosition(),
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NOT_PUBLIC_ERROR_MESSAGE);
            }

//            if (!classDeclaration.getSuperinterfaces().contains(typeDeclaration)) {
//                Collection<? extends com.sun.mirror.declaration.MethodDeclaration> seiMethods = typeDeclaration
//                        .getMethods();
//                for (com.sun.mirror.declaration.MethodDeclaration methodDeclaration : seiMethods) {
//                }
//                Collection<? extends com.sun.mirror.declaration.MethodDeclaration> implMethods = classDeclaration
//                        .getMethods();
//                for (com.sun.mirror.declaration.MethodDeclaration methodDeclaration : implMethods) {
//                }
//            }
        } else {
//            printError(endpointInterface.getPosition(), JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NOT_FOUND_ERROR_MESSAGE);
        }
    }

    private void checkJSR181Annotations(ClassDeclaration classDeclaration) {
        AnnotationMirror soapBinding = AnnotationUtils.getAnnotation(classDeclaration, SOAPBinding.class);
        if (soapBinding != null) {
            printError(soapBinding.getPosition(),
                    JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_SOAPBINDING_MESSAGE);
        }

        Collection<? extends com.sun.mirror.declaration.MethodDeclaration> implMethods = classDeclaration
                .getMethods();
        for (com.sun.mirror.declaration.MethodDeclaration methodDeclaration : implMethods) {
            AnnotationMirror msoapBinding = AnnotationUtils.getAnnotation(methodDeclaration,
                    SOAPBinding.class);
            if (msoapBinding != null) {
                printError(msoapBinding.getPosition(),
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_SOAPBINDING_MESSAGE);
            }

            AnnotationMirror oneway = AnnotationUtils.getAnnotation(methodDeclaration, Oneway.class);
            if (oneway != null) {
                printError(oneway.getPosition(),
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_ONEWAY_ERROR_MESSAGE);
            }

            AnnotationMirror webMethod = AnnotationUtils.getAnnotation(methodDeclaration, WebMethod.class);
            if (webMethod != null) {
                printError(webMethod.getPosition(),
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_WEBMETHODS_ERROR_MESSAGE);
            }
            AnnotationMirror webResult = AnnotationUtils.getAnnotation(methodDeclaration, WebResult.class);
            if (webResult != null) {
                printError(webResult.getPosition(),
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_WEBRESULT_ERROR_MESSAGE);
            }

            Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
            for (ParameterDeclaration parameterDeclaration : parameters) {
                AnnotationMirror webParam = AnnotationUtils.getAnnotation(parameterDeclaration,
                        WebParam.class);
                if (webParam != null) {
                    printError(webParam.getPosition(),
                            JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_WEBPARAM_ERROR_MESSAGE);
                }
            }
        }
    }
}
