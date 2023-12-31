/*******************************************************************************
 * Copyright (c) 2008, 2010 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.core.annotations.validation;

import java.util.Collection;

import javax.jws.Oneway;
import javax.xml.ws.Holder;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.type.TypeMirror;

public class OnewayRules extends AbstractAnnotationProcessor {

    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
        .getTypeDeclaration(Oneway.class.getName());

        Collection<Declaration> annotatedTypes = environment
        .getDeclarationsAnnotatedWith(annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
                if (!methodDeclaration.getReturnType().equals(environment.getTypeUtils().getVoidType())) {
                    AnnotationMirror oneway = AnnotationUtils.getAnnotation(methodDeclaration, Oneway.class);
                    printError(oneway.getPosition(), JAXWSCoreMessages.ONEWAY_NO_RETURN_VALUE);
                }
                if (methodDeclaration.getThrownTypes().size() > 0) {
                    AnnotationMirror oneway = AnnotationUtils.getAnnotation(methodDeclaration, Oneway.class);
                    printError(oneway.getPosition(), JAXWSCoreMessages.ONEWAY_NO_CHECKED_EXCEPTIONS);
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
                AnnotationMirror oneway = AnnotationUtils.getAnnotation(methodDeclaration, Oneway.class);
                printError(oneway.getPosition(), JAXWSCoreMessages.ONEWAY_NO_HOLDER_PARAMETERS);
            }
        }
    }
}
