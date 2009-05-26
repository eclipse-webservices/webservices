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
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;

/**
 * 
 * @author sclarke
 *
 */
public class SOAPBindingMethodStyleDocumentRule extends AbstractAnnotationProcessor {
    private static final String STYLE = "style";
    
    public SOAPBindingMethodStyleDocumentRule() {
    }

    @Override
    public void process() {
        Messager messager = environment.getMessager();

        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration("javax.jws.soap.SOAPBinding"); //$NON-NLS-1$

        Collection<Declaration> annotatedTypes = environment.getDeclarationsAnnotatedWith(
                annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof MethodDeclaration) {

                Collection<AnnotationMirror> annotationMirrors = declaration.getAnnotationMirrors();
    
                for (AnnotationMirror mirror : annotationMirrors) {
                    if (mirror.getAnnotationType().getDeclaration().equals(annotationDeclaration)) {
                        String style = AnnotationUtils.findAnnotationValue(mirror, STYLE);
                        if (!style.equals(javax.jws.soap.SOAPBinding.Style.DOCUMENT.toString())) {
                            messager.printError(mirror.getPosition(), 
                              JAXWSCoreMessages.SOAPBINDING_ON_METHOD_STYLE_DOCUMENT_ONLY_MESSAGE); 
                        }
                    }
                }
            }
        }
    }
}
