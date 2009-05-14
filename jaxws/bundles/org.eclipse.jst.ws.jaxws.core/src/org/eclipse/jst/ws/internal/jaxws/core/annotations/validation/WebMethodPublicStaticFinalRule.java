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

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;

/**
 * 
 * @author sclarke
 * 
 */
public class WebMethodPublicStaticFinalRule extends AbstractAnnotationProcessor {
	
    public WebMethodPublicStaticFinalRule() {
    }
    
    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration("javax.jws.WebMethod"); //$NON-NLS-1$

        Collection<Declaration> annotatedTypes = environment.getDeclarationsAnnotatedWith(
                annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
        	if (declaration instanceof MethodDeclaration) {
        	    MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
        	    if (methodDeclaration.getDeclaringType() instanceof ClassDeclaration) {
                    Collection<Modifier> modifiers = methodDeclaration.getModifiers();
                    if (!modifiers.contains(Modifier.PUBLIC)) {
                        printError(annotationDeclaration, methodDeclaration,
                                JAXWSCoreMessages.WEBMETHOD_ONLY_ON_PUBLIC_METHODS_MESSAGE);
                    }
                    if (modifiers.contains(Modifier.FINAL)) {
                        printError(annotationDeclaration, methodDeclaration,
                                JAXWSCoreMessages.WEBMETHOD_NO_FINAL_MODIFIER_ALLOWED_MESSAGE);
                    }
                    if (modifiers.contains(Modifier.STATIC)) {
                        printError(annotationDeclaration, methodDeclaration,
                                JAXWSCoreMessages.WEBMETHOD_NO_STATIC_MODIFIER_ALLOWED_MESSAGE);
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
