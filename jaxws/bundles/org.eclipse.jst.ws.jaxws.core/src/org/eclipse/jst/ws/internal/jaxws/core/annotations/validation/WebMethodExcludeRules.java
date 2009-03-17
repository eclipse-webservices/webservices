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
import java.util.Map;
import java.util.Set;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationTypeElementDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;

/**
 * 
 * @author sclarke
 *
 */
public class WebMethodExcludeRules extends AbstractAnnotationProcessor {

    public WebMethodExcludeRules() {
    }

    @Override
    public void process() {
        Messager messager = environment.getMessager();

        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration("javax.jws.WebMethod"); //$NON-NLS-1$

        Collection<Declaration> annotatedTypes = environment.getDeclarationsAnnotatedWith(
                annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
            Collection<AnnotationMirror> annotationMirrors = declaration.getAnnotationMirrors();

            for (AnnotationMirror mirror : annotationMirrors) {
                Map<AnnotationTypeElementDeclaration, AnnotationValue> valueMap = mirror.getElementValues();
                Set<Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue>> valueSet = valueMap
                        .entrySet();
                for (Map.Entry<AnnotationTypeElementDeclaration, AnnotationValue> annotationKeyValue : 
                        valueSet) {
                    
                    if (annotationKeyValue.getKey().getSimpleName().equals("exclude")) { //$NON-NLS-1$
                        if (declaration instanceof MethodDeclaration) {
                            MethodDeclaration methodDeclaration = (MethodDeclaration)declaration;
                            if (methodDeclaration.getDeclaringType() instanceof InterfaceDeclaration) {
                                messager.printError(mirror.getPosition(),
                                        JAXWSCoreMessages.WEBMETHOD_ANNOTATION_PROCESSOR_EXCLUDE_NOT_ALLOWED_ON_SEI);
                                break;
                            }
                        }

                        if (annotationKeyValue.getValue() != null) {
                            AnnotationValue annotationValue = annotationKeyValue.getValue();
                            if (annotationValue.getValue().toString().equals("true") && valueMap.size() > 1) {
                              messager.printError(mirror.getPosition(), JAXWSCoreMessages.
                        WEBMETHOD_ANNOTATION_PROCESSOR_EXCLUDE_SPECIFEID_NO_OTHER_ATTRIBUTES_ALLOWED_MESSAGE);
                            }
                        }
                    }
                }
            }
        }
    }

}
