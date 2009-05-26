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
package org.eclipse.jst.ws.internal.jaxws.core.annotations.validation;

import java.util.Collection;

import javax.jws.WebParam;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;

/**
 * 
 * @author sclarke
 *
 */
public class OnewayRules extends AbstractAnnotationProcessor {
    
    public OnewayRules() {
    }
    
    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration("javax.jws.Oneway"); //$NON-NLS-1$

        Collection<Declaration> annotatedTypes = environment
                .getDeclarationsAnnotatedWith(annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {        
            if (declaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
                if (!methodDeclaration.getReturnType().equals(environment.getTypeUtils().getVoidType())) {
                   printError(annotationDeclaration, methodDeclaration,
                            JAXWSCoreMessages.ONEWAY_NO_RETURN_VALUE_ERROR_MESSAGE); 
                }
                if (methodDeclaration.getThrownTypes().size() > 0) {
                    printError(annotationDeclaration, methodDeclaration,
                            JAXWSCoreMessages.ONEWAY_NO_CHECKED_EXCEPTIONS_ERROR_MESSAGE); 
                }
                checkParameters(methodDeclaration);
            }
        }
    }
    
    private void checkParameters(MethodDeclaration methodDeclaration) {
        Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
        for (ParameterDeclaration parameter : parameters) {
            Collection<AnnotationMirror> annotatinMirrors = parameter.getAnnotationMirrors();
            for (AnnotationMirror mirror : annotatinMirrors) {
                AnnotationTypeDeclaration annotationTypeDeclaration = mirror.getAnnotationType()
                        .getDeclaration();
                if (annotationTypeDeclaration.getQualifiedName().equals(WebParam.class.getCanonicalName())) {
                    String mode = AnnotationUtils.findAnnotationValue(mirror, "mode");  //$NON-NLS-1$
                    if (mode.equals(WebParam.Mode.OUT.toString())) {
                        environment.getMessager().printError(mirror.getPosition(),
                                JAXWSCoreMessages.ONEWAY_NO_OUT_PARAMETERS);
                    }
                    if (mode.equals(WebParam.Mode.INOUT.toString())) {
                        environment.getMessager().printError(mirror.getPosition(),
                                JAXWSCoreMessages.ONEWAY_NO_INOUT_PARAMETERS);
                    }

                }
            }
        }
    }
    
    private void printError(AnnotationTypeDeclaration annotationDeclaration ,
            MethodDeclaration methodDeclaration, String errorMessage) {
        Messager messager = environment.getMessager();

        Collection<AnnotationMirror> annotationMirrors = methodDeclaration.getAnnotationMirrors();
        for (AnnotationMirror mirror : annotationMirrors) {
            if ( mirror.getAnnotationType().toString().equals(annotationDeclaration
                    .getQualifiedName())) {
                messager.printError(mirror.getPosition(), errorMessage); 
            }
        }
    }


}
