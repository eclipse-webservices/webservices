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

import javax.xml.ws.Holder;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.type.TypeMirror;

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
                checkParameters(methodDeclaration, annotationDeclaration);
            }
        }
    }
    
    private void checkParameters(MethodDeclaration methodDeclaration,
            AnnotationTypeDeclaration annotationDeclaration) {
        Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
        for (ParameterDeclaration parameter : parameters) {
            TypeMirror typeMirror = environment.getTypeUtils().getErasure(parameter.getType());
            if (typeMirror.toString().equals(Holder.class.getCanonicalName())) {
                printError(annotationDeclaration, methodDeclaration,
                        JAXWSCoreMessages.ONEWAY_NO_HOLDER_PARAMETERS);
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
