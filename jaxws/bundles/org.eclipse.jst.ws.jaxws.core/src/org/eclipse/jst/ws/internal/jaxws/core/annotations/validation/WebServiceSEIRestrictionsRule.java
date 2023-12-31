/*******************************************************************************
 * Copyright (c) 2009, 2013 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.core.annotations.validation;

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.ENDPOINT_INTERFACE;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.NAME;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ClassType;

public class WebServiceSEIRestrictionsRule extends AbstractAnnotationProcessor {

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
                        AnnotationValue name = AnnotationUtils.getAnnotationValue(mirror, NAME);
                        if (name != null) {
                            printFixableError(name.getPosition(),
                                    JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_NAME_ATTRIBUTE);
                        }
                        checkRestrictions((ClassDeclaration) declaration, endpointInterface);
                        checkJSR181Annotations((ClassDeclaration) declaration);
                    }
                }
            }
        }
    }

    private void checkRestrictions(ClassDeclaration classDeclaration, AnnotationValue endpointInterface) {
        String sei = endpointInterface.getValue().toString();
        if (JDTUtils.validateJavaTypeName(sei).getSeverity() == IStatus.ERROR) {
            return;
        }
        com.sun.mirror.declaration.TypeDeclaration typeDeclaration = environment.getTypeDeclaration(sei);
        if (typeDeclaration != null) {
            if (!(typeDeclaration instanceof InterfaceDeclaration) || typeDeclaration instanceof AnnotationTypeDeclaration) {
                printError(endpointInterface.getPosition(), JAXWSCoreMessages.bind(
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NOT_INTERFACE, sei));
                return;
            }
            if (typeDeclaration.getDeclaringType() != null) {
                printError(endpointInterface.getPosition(), JAXWSCoreMessages.bind(
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NOT_OUTER, sei));
            }
            AnnotationMirror annotationMirror = AnnotationUtils.getAnnotation(typeDeclaration,
                    WebService.class);
            if (annotationMirror == null) {
                printError(endpointInterface.getPosition(), JAXWSCoreMessages.bind(
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NOT_ANNOTATED, sei));
            }

            if (!typeDeclaration.getModifiers().contains(Modifier.PUBLIC)) {
                printError(endpointInterface.getPosition(), JAXWSCoreMessages.bind(
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NOT_PUBLIC, sei));
            }

            if (!classDeclaration.getSuperinterfaces().contains(typeDeclaration)) {
                Collection<? extends MethodDeclaration> seiMethods = typeDeclaration.getMethods();
                Collection<? extends MethodDeclaration> implMethods = getAllMethods(classDeclaration);

                for (MethodDeclaration seiMethod : seiMethods) {
                    boolean implemented = false;
                    for (MethodDeclaration implMethod : implMethods) {
                        if (AnnotationUtils.compareMethods(seiMethod, implMethod)) {
                            if (!implMethod.getModifiers().contains(Modifier.PUBLIC)) {
                                printFixableError(implMethod.getPosition(),
                                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_REDUCED_VISIBILITY);
                            }
                            if (!implMethod.getReturnType().equals(seiMethod.getReturnType())) {
                                printFixableError(implMethod.getPosition(), JAXWSCoreMessages.bind(
                                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_INCOMPATIBLE_RETURN_TYPE,
                                        getImplementsMessage(typeDeclaration, seiMethod)),
                                        "", JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_INCOMPATIBLE_RETURN_TYPE);
                            }
                            if (implMethod.getThrownTypes().size() != seiMethod.getThrownTypes().size() ||
                                    !implMethod.getThrownTypes().containsAll(seiMethod.getThrownTypes())) {
                                printError(implMethod.getPosition(), JAXWSCoreMessages.bind(
                                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_INCOMPATIBLE_EXCEPTIONS,
                                        implMethod.getSimpleName(), sei));
                            }
                            implemented = true;
                            break;
                        }
                    }
                    if (!implemented) {
                        printFixableError(endpointInterface.getPosition(), JAXWSCoreMessages.bind(
                                JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_MUST_IMPLEMENT,
                                getImplementsMessage(typeDeclaration, seiMethod)), "", //$NON-NLS-1$
                                JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_MUST_IMPLEMENT);
                    }
                }
            }
        } else {
            printError(endpointInterface.getPosition(), JAXWSCoreMessages.bind(
                    JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NOT_FOUND, sei));
        }
    }

    private Collection<MethodDeclaration> getAllMethods(ClassDeclaration classDeclaration) {
        List<MethodDeclaration> allMethods = new ArrayList<MethodDeclaration>();
        collectAllMethodDeclarations(classDeclaration, allMethods);
    	return allMethods;
    }
    
    private void collectAllMethodDeclarations(ClassDeclaration classDeclaration, Collection<MethodDeclaration> methods) {
    	if (classDeclaration != null) {
    		methods.addAll(classDeclaration.getMethods());
    		
    		ClassType classType = classDeclaration.getSuperclass();
    		if (classType != null && classType.getDeclaration() != null) {
    			collectAllMethodDeclarations(classType.getDeclaration(), methods);
    		}
    	}
    }
    
    private String getImplementsMessage(TypeDeclaration typeDeclaration, MethodDeclaration seiMethod) {
        StringBuilder message = new StringBuilder(typeDeclaration.getSimpleName());
        message.append("."); //$NON-NLS-1$
        message.append(seiMethod.getSimpleName());
        message.append("("); //$NON-NLS-1$
        Collection<ParameterDeclaration> parameters = seiMethod.getParameters();
        Iterator<ParameterDeclaration> iter = parameters.iterator();
        while (iter.hasNext()) {
            ParameterDeclaration parameterDeclaration = iter.next();
            String typeMirror = environment.getTypeUtils().getErasure(parameterDeclaration.getType()).toString();
            message.append(typeMirror.substring(typeMirror.lastIndexOf(".") + 1)); //$NON-NLS-1$
            if (iter.hasNext()) {
                message.append(", "); //$NON-NLS-1$
            }

        }
        message.append(")"); //$NON-NLS-1$
        return message.toString();
    }

    private void checkJSR181Annotations(ClassDeclaration classDeclaration) {
        AnnotationMirror soapBinding = AnnotationUtils.getAnnotation(classDeclaration, SOAPBinding.class);
        if (soapBinding != null) {
            printFixableError(soapBinding.getPosition(),
                    JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_SOAPBINDING);
        }

        Collection<? extends com.sun.mirror.declaration.MethodDeclaration> implMethods = classDeclaration
        .getMethods();
        for (com.sun.mirror.declaration.MethodDeclaration methodDeclaration : implMethods) {
            AnnotationMirror msoapBinding = AnnotationUtils.getAnnotation(methodDeclaration,
                    SOAPBinding.class);
            if (msoapBinding != null) {
                printFixableError(msoapBinding.getPosition(),
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_SOAPBINDING);
            }

            AnnotationMirror oneway = AnnotationUtils.getAnnotation(methodDeclaration, Oneway.class);
            if (oneway != null) {
                printFixableError(oneway.getPosition(),
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_ONEWAY);
            }

            AnnotationMirror webMethod = AnnotationUtils.getAnnotation(methodDeclaration, WebMethod.class);
            if (webMethod != null) {
                printFixableError(webMethod.getPosition(),
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_WEBMETHODS);
            }
            AnnotationMirror webResult = AnnotationUtils.getAnnotation(methodDeclaration, WebResult.class);
            if (webResult != null) {
                printFixableError(webResult.getPosition(),
                        JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_WEBRESULT);
            }

            Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
            for (ParameterDeclaration parameterDeclaration : parameters) {
                AnnotationMirror webParam = AnnotationUtils.getAnnotation(parameterDeclaration,
                        WebParam.class);
                if (webParam != null) {
                    printFixableError(webParam.getPosition(),
                            JAXWSCoreMessages.WEBSERVICE_ENPOINTINTERFACE_NO_WEBPARAM);
                }
            }
        }
    }
}
