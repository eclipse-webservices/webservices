/*******************************************************************************
 * Copyright (c) 2008, 2009 IONA Technologies PLC
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

import javax.jws.WebMethod;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;

public class WebMethodPublicStaticFinalRule extends AbstractAnnotationProcessor {
	
    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(WebMethod.class.getName());

        Collection<Declaration> annotatedTypes = environment.getDeclarationsAnnotatedWith(
                annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof MethodDeclaration) {
                MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
                if (methodDeclaration.getDeclaringType() instanceof ClassDeclaration) {
                    Collection<Modifier> modifiers = methodDeclaration.getModifiers();
                    if (!modifiers.contains(Modifier.PUBLIC)) {
                        printFixableError(methodDeclaration.getPosition(), 
                                JAXWSCoreMessages.WEBMETHOD_ONLY_ON_PUBLIC_METHODS);
                    }

                    if (modifiers.contains(Modifier.FINAL)) {
                        printFixableError(methodDeclaration.getPosition(),
                                JAXWSCoreMessages.WEBMETHOD_NO_FINAL_MODIFIER_ALLOWED);
                    }
                    
                    if (modifiers.contains(Modifier.STATIC)) {
                        printFixableError(methodDeclaration.getPosition(),
                                JAXWSCoreMessages.WEBMETHOD_NO_STATIC_MODIFIER_ALLOWED);
                    }
                }
            }
        }
    }

}
