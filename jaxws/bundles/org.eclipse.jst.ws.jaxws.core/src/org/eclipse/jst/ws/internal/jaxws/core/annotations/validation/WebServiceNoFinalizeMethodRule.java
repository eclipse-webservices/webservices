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

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.FINALIZE;

import java.util.Collection;

import javax.jws.WebService;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

public class WebServiceNoFinalizeMethodRule extends AbstractAnnotationProcessor {

    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(WebService.class.getName());

        Collection<Declaration> annotatedTypes = environment
                .getDeclarationsAnnotatedWith(annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
            MethodDeclaration finalizeMethod = getFinalizeMethod(declaration);
            if (finalizeMethod != null) {
                printFixableError(finalizeMethod.getPosition(), JAXWSCoreMessages.WEBSERVICE_OVERRIDE_FINALIZE);
            }
        }
    }

    private MethodDeclaration getFinalizeMethod(Declaration declaration) {
        if (declaration instanceof TypeDeclaration) {
            TypeDeclaration typeDeclaration = (TypeDeclaration)declaration;
            Collection<? extends MethodDeclaration> methodDeclarations = typeDeclaration.getMethods();
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                if (methodDeclaration.getSimpleName().equals(FINALIZE) 
                        && methodDeclaration.getParameters().size() == 0) {
                    return methodDeclaration;
                }
            }
        }
        return null;
    }
}
