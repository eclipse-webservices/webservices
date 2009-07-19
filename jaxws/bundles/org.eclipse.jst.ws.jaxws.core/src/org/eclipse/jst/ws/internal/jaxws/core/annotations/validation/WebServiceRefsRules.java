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

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.TYPE;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.VALUE;

import java.util.Collection;
import java.util.List;

import javax.xml.ws.WebServiceRefs;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;

public class WebServiceRefsRules extends AbstractAnnotationProcessor {

    @Override
    public void process() {
        AnnotationTypeDeclaration annotationTypeDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(WebServiceRefs.class.getName());

        Collection<Declaration> annotatedTypes = environment
                .getDeclarationsAnnotatedWith(annotationTypeDeclaration);

        for (Declaration declaration : annotatedTypes) {
            AnnotationMirror webServiceRefs = AnnotationUtils
                    .getAnnotation(declaration, WebServiceRefs.class);

            AnnotationValue webServiceRefsValue = AnnotationUtils.getAnnotationValue(webServiceRefs, VALUE);

            if (webServiceRefsValue != null && webServiceRefsValue.getValue() instanceof List<?>) {
                List<AnnotationValue> value = (List<AnnotationValue>) webServiceRefsValue.getValue();

                for (AnnotationValue annotationValue : value) {
                    if (annotationValue.getValue() instanceof AnnotationMirror) {
                        AnnotationMirror webServiceRef = (AnnotationMirror) annotationValue.getValue();

                        String webServiceRefName = AnnotationUtils.getStringValue(webServiceRef, NAME);
                        if (webServiceRefName == null || webServiceRefName.trim().length() == 0) {
                            printError(webServiceRef.getPosition(),
                                    JAXWSCoreMessages.WEBSERVICEREFS_NAME_REQUIRED);
                        }

                        AnnotationValue webServiceRefNameType = AnnotationUtils.getAnnotationValue(
                                webServiceRef, TYPE);
                        if (webServiceRefNameType != null && webServiceRefNameType instanceof AnnotationValue) {
                            AnnotationValue type = (AnnotationValue) webServiceRefNameType;
                            if (type.getValue() instanceof ClassDeclaration) {
                                ClassDeclaration classDeclaration = (ClassDeclaration) type.getValue();
                                if (classDeclaration.getQualifiedName().equals(
                                        java.lang.Object.class.getCanonicalName())) {
                                    printError(webServiceRef.getPosition(),
                                            JAXWSCoreMessages.WEBSERVICEREFS_TYPE_REQUIRED);
                                }
                            }
                        } else {
                            printError(webServiceRef.getPosition(),
                                    JAXWSCoreMessages.WEBSERVICEREFS_TYPE_REQUIRED);
                        }
                    }
                }
            }
        }
    }

}
