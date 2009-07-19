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

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.TARGET_NAMESPACE;

import java.util.Collection;

import javax.jws.WebService;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.PackageDeclaration;
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
                PackageDeclaration packageDeclaration = typeDeclaration.getPackage();
                if (packageDeclaration.getQualifiedName().length() == 0 && targetNamespace == null) {
                    printError(webService.getPosition(), JAXWSCoreMessages.WEBSERVICE_DEFAULT_PACKAGE_TARGET_NAMESPACE);
                } else if (targetNamespace != null && targetNamespace.getValue().toString().trim().length() == 0) {
                    printError(targetNamespace.getPosition(), JAXWSCoreMessages.WEBSERVICE_DEFAULT_PACKAGE_TARGET_NAMESPACE);
                }
            }            
        }
    }

}
