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

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Holder;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.TypeMirror;

/**
 * 
 * @author sclarke
 *
 */
public class HolderTypeParametersRule extends AbstractAnnotationProcessor {
    
    private static final String MODE = "mode"; //$NON-NLS-1$
    
    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(WebService.class.getName());

        Collection<Declaration> annotatedTypes = environment
                .getDeclarationsAnnotatedWith(annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
             if (declaration instanceof TypeDeclaration) {
                 validateParameters((TypeDeclaration) declaration);
             }
        }
    }
    
    private void validateParameters(TypeDeclaration typeDeclaration) {
        Messager messager = environment.getMessager();
        Collection<? extends MethodDeclaration> methods = typeDeclaration.getMethods();
        for (MethodDeclaration methodDeclaration : methods) {
            Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
            for (ParameterDeclaration parameter : parameters) {
                TypeMirror typeMirror = environment.getTypeUtils().getErasure(parameter.getType());
                boolean isHolderParameter = typeMirror.toString().equals(Holder.class.getCanonicalName());
                if (isWebParamOutInoutMode(parameter) && !isHolderParameter) {
                    messager.printError(parameter.getPosition(), 
                                JAXWSCoreMessages.WEBPARAM_MODE_OUT_INOUT_HOLDER_TYPE_ERROR_MESSAGE);
                } else if (isHolderParameter && isWebParamInMode(parameter)){
                    messager.printError(parameter.getPosition(),
                            JAXWSCoreMessages.HOLDER_TYPE_MUST_BE_OUT_INOUT_ERROR_MESSAGE);
                }
            }
        }
    }
    
    private boolean isWebParamInMode(ParameterDeclaration parameter) {
        Collection<AnnotationMirror> annotatinMirrors = parameter.getAnnotationMirrors();
        for (AnnotationMirror mirror : annotatinMirrors) {
            AnnotationTypeDeclaration annotationTypeDeclaration = mirror.getAnnotationType().getDeclaration();
            if (annotationTypeDeclaration.getQualifiedName().equals(WebParam.class.getCanonicalName())) {
                String mode = AnnotationUtils.findAnnotationValue(mirror, MODE); //$NON-NLS-1$
                if (mode.equals(WebParam.Mode.IN.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isWebParamOutInoutMode(ParameterDeclaration parameter) {
        Collection<AnnotationMirror> annotatinMirrors = parameter.getAnnotationMirrors();
        for (AnnotationMirror mirror : annotatinMirrors) {
            AnnotationTypeDeclaration annotationTypeDeclaration = mirror.getAnnotationType().getDeclaration();
            if (annotationTypeDeclaration.getQualifiedName().equals(WebParam.class.getCanonicalName())) {
                String mode = AnnotationUtils.findAnnotationValue(mirror, MODE); //$NON-NLS-1$
                if (mode.equals(WebParam.Mode.OUT.toString())) {
                    return true;
                }
                if (mode.equals(WebParam.Mode.INOUT.toString())) {
                    return true;
                }
            }
        }
        return false;
    }    
}
