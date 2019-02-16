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

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.TARGET_NAMESPACE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

public class WebServiceTargetNamespaceRule extends AbstractAnnotationProcessor {

    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
        .getTypeDeclaration(WebService.class.getName());

        Collection<Declaration> annotatedTypes = environment
        .getDeclarationsAnnotatedWith(annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof TypeDeclaration) {
                AnnotationMirror webService = AnnotationUtils.getAnnotation(declaration, WebService.class);
                AnnotationValue targetNamespace = AnnotationUtils.getAnnotationValue(webService, TARGET_NAMESPACE);

                TypeDeclaration typeDeclaration = (TypeDeclaration) declaration;
                //
                PackageDeclaration packageDeclaration = typeDeclaration.getPackage();
                if (packageDeclaration.getQualifiedName().length() == 0) {
                    if (targetNamespace == null || targetNamespace.getValue().toString().trim().length() == 0) {
                        printError(webService.getPosition(), JAXWSCoreMessages.WEBSERVICE_DEFAULT_PACKAGE_TARGET_NAMESPACE);
                    }
                }

                validateURISyntax(targetNamespace);

                Collection<? extends MethodDeclaration> methods = ((TypeDeclaration) declaration).getMethods();
                for (MethodDeclaration methodDeclaration : methods) {
                    Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
                    for (ParameterDeclaration parameterDeclaration : parameters) {
                        AnnotationMirror webParam = AnnotationUtils.getAnnotation(parameterDeclaration, WebParam.class);
                        if (webParam != null) {
                            validateURISyntax(AnnotationUtils.getAnnotationValue(webParam, TARGET_NAMESPACE));
                        }
                    }
                }
            }
        }
    }


    private void validateURISyntax(AnnotationValue targetNamespace) {
        if (targetNamespace != null) {
            try {
                new URI(targetNamespace.getValue().toString());
            } catch (URISyntaxException urise) {
                printError(targetNamespace.getPosition(), JAXWSCoreMessages.bind(
                        JAXWSCoreMessages.TARGET_NAMESPACE_URI_SYNTAX_ERROR, new Object[] {
                                urise.getIndex(), urise.getInput(), urise.getReason() }));
            }
        }
    }

}
