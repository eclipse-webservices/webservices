/*******************************************************************************
 * Copyright (c) 2009, 2010 Shane Clarke.
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

import java.util.Collection;

import javax.jws.WebService;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.EnumDeclaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.TypeDeclaration;

public class WebServicePublicAbstractFinalRule extends AbstractAnnotationProcessor {

    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
        .getTypeDeclaration(WebService.class.getName());

        Collection<Declaration> annotatedTypes = environment
        .getDeclarationsAnnotatedWith(annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof ClassDeclaration) {
                if (declaration instanceof EnumDeclaration) {
                    printError(declaration.getPosition(), JAXWSCoreMessages.WEBSERVICE_CLASS_OR_INTERFACE_ONLY);
                } else {
                    testDeclaration(declaration, annotationDeclaration);
                }
            }

            if (declaration instanceof InterfaceDeclaration) {
                if (declaration instanceof AnnotationTypeDeclaration) {
                    printError(declaration.getPosition(), JAXWSCoreMessages.WEBSERVICE_CLASS_OR_INTERFACE_ONLY);
                } else {
                    testDeclaration(declaration, annotationDeclaration);
                }
            }
        }
    }

    private void testDeclaration(Declaration declaration, AnnotationTypeDeclaration annotationDeclaration) {
        Collection<Modifier> modifiers = declaration.getModifiers();
        if (modifiers.contains(Modifier.FINAL) || modifiers.contains(Modifier.ABSTRACT)
                || !modifiers.contains(Modifier.PUBLIC)) {
            Collection<AnnotationMirror> annotationMirrors = declaration.getAnnotationMirrors();
            for (AnnotationMirror mirror : annotationMirrors) {
                if (mirror.getAnnotationType().toString().equals(annotationDeclaration.getQualifiedName())) {
                    printFixableError(declaration.getPosition(),
                            JAXWSCoreMessages.WEBSERVICE_PUBLIC_ABSTRACT_FINAL);
                }
            }
        }
        TypeDeclaration typeDeclaration = (TypeDeclaration)declaration;
        if (typeDeclaration.getNestedTypes().size() > 0) {
            testNestedTypes(typeDeclaration.getNestedTypes(), annotationDeclaration);
        }
    }

    private void testNestedTypes(Collection<TypeDeclaration> nestedTypes,AnnotationTypeDeclaration annotationDeclaration) {
        for (TypeDeclaration nestedDeclaration : nestedTypes) {
            Collection<AnnotationMirror> annotationMirrors = nestedDeclaration.getAnnotationMirrors();
            for (AnnotationMirror mirror : annotationMirrors) {
                if (mirror.getAnnotationType().toString().equals(annotationDeclaration.getQualifiedName())) {
                    printError(mirror.getPosition(),
                            JAXWSCoreMessages.WEBSERVICE_PUBLIC_ABSTRACT_FINAL);
                }
            }
        }
    }
}
