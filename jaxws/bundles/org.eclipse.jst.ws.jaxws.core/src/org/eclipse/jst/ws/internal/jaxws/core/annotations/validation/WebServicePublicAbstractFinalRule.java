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

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.declaration.TypeDeclaration;

/**
 * 
 * @author sclarke
 *
 */
public class WebServicePublicAbstractFinalRule extends AbstractAnnotationProcessor {

    public WebServicePublicAbstractFinalRule() {
    }

    @Override
    public void process() {
        Messager messager = environment.getMessager();

        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration("javax.jws.WebService"); //$NON-NLS-1$

        Collection<Declaration> annotatedTypes = environment
                .getDeclarationsAnnotatedWith(annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof TypeDeclaration) {
                Collection<Modifier> modifiers = declaration.getModifiers();
                if (modifiers.contains(Modifier.FINAL) || modifiers.contains(Modifier.ABSTRACT) 
                     || !modifiers.contains(Modifier.PUBLIC)) {
                    Collection<AnnotationMirror> annotationMirrors = declaration.getAnnotationMirrors();
                    for (AnnotationMirror mirror : annotationMirrors) {
                        if (mirror.getAnnotationType().toString()
                            .equals(annotationDeclaration.getQualifiedName())) {
                            messager.printError(mirror.getPosition(), 
                            JAXWSCoreMessages.WEBSERVICE_PUBLIC_ABSTRACT_FINAL_MESSAGE);
                        }
                    }
                }
                TypeDeclaration typeDeclaration = (TypeDeclaration)declaration;
                if (typeDeclaration.getNestedTypes().size() > 0) {
                    testNestedTypes(typeDeclaration.getNestedTypes(), annotationDeclaration, messager);
                }
            }
        }
    }
    
    private void testNestedTypes(Collection<TypeDeclaration> nestedTypes, 
            AnnotationTypeDeclaration annotationDeclaration, Messager messager) {
        for (TypeDeclaration nestedDeclaration : nestedTypes) {
            Collection<AnnotationMirror> annotationMirrors = nestedDeclaration.getAnnotationMirrors();
            for (AnnotationMirror mirror : annotationMirrors) {
                if (mirror.getAnnotationType().toString().equals(annotationDeclaration.getQualifiedName())) {
                    messager.printError(mirror.getPosition(),
                            JAXWSCoreMessages.WEBSERVICE_PUBLIC_ABSTRACT_FINAL_MESSAGE);
                }
            }
        }
    }
}
