/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
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

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.EXCLUDE;

import java.util.Collection;

import javax.jws.WebMethod;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

public class WebMethodExcludeRules extends AbstractAnnotationProcessor {

    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(WebMethod.class.getName());

        Collection<Declaration> annotatedTypes = environment.getDeclarationsAnnotatedWith(
                annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof MethodDeclaration) {
                Collection<AnnotationMirror> annotationMirrors = declaration.getAnnotationMirrors();
                for (AnnotationMirror annotationMirror : annotationMirrors) {
                    AnnotationValue excludeAttribute = AnnotationUtils.getAnnotationValue(annotationMirror,
                            EXCLUDE);
                    if (excludeAttribute != null) {
                        MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
                        TypeDeclaration typeDeclaration = methodDeclaration.getDeclaringType();

                        if (typeDeclaration instanceof InterfaceDeclaration
                                && ((Boolean) excludeAttribute.getValue()).booleanValue()) {
                            printFixableError(excludeAttribute.getPosition(),
                                    JAXWSCoreMessages.WEBMETHOD_EXCLUDE_NOT_ALLOWED_ON_SEI);
                        }

                        if (typeDeclaration instanceof ClassDeclaration
                                && annotationMirror.getElementValues().size() > 1
                                && Boolean.valueOf(excludeAttribute.getValue().toString()).booleanValue()) {
                            printFixableError(excludeAttribute.getPosition(),
                                    JAXWSCoreMessages.WEBMETHOD_EXCLUDE_SPECIFIED_NO_OTHER_ATTRIBUTES_ALLOWED);
                        }
                    }
                }
            }
        }
    }
}
