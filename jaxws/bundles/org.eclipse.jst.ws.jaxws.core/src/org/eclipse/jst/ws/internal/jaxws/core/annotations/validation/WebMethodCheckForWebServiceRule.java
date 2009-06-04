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

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

/**
 * 
 * @author sclarke
 *
 */
public class WebMethodCheckForWebServiceRule extends AbstractAnnotationProcessor {

    public WebMethodCheckForWebServiceRule() {
    }

    @Override
    public void process() {
         AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(WebMethod.class.getName());

        Collection<Declaration> annotatedTypes = environment
                .getDeclarationsAnnotatedWith(annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
            Collection<AnnotationMirror> annotationMirrors = declaration.getAnnotationMirrors();
            for (AnnotationMirror mirror : annotationMirrors) {
                if (mirror.getAnnotationType().getDeclaration().equals(annotationDeclaration)
                 && !checkForWebServiceAnnotation(((MethodDeclaration)declaration).getDeclaringType())) {
                    printError(mirror.getPosition(), JAXWSCoreMessages
                           .WEBMETHOD_ONLY_SUPPORTED_ON_CLASSES_WITH_WEBSERVICE_MESSAGE);
                }
            }
        }
    }
    
    private boolean checkForWebServiceAnnotation(TypeDeclaration typeDeclaration) {
        Collection<AnnotationMirror> annotationMirrors = typeDeclaration.getAnnotationMirrors();
        for (AnnotationMirror mirror : annotationMirrors) {
            if (mirror.getAnnotationType().toString().equals(WebService.class.getName())) {
                return true;
            }
        }
        return false;
    }
}
