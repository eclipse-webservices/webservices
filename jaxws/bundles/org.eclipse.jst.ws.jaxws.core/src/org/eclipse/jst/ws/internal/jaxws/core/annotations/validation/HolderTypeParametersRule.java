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

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.MODE;

import java.util.Collection;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.Holder;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.TypeMirror;

public class HolderTypeParametersRule extends AbstractAnnotationProcessor {
    
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
        Collection<? extends MethodDeclaration> methods = typeDeclaration.getMethods();
        for (MethodDeclaration methodDeclaration : methods) {
            Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
            for (ParameterDeclaration parameter : parameters) {
                TypeMirror typeMirror = environment.getTypeUtils().getErasure(parameter.getType());
                boolean isHolderParameter = typeMirror.toString().equals(Holder.class.getCanonicalName());
                if (isWebParamOutInoutMode(parameter) && !isHolderParameter) {
                    printError(parameter.getPosition(), 
                                JAXWSCoreMessages.WEBPARAM_MODE_OUT_INOUT_HOLDER_TYPE);
                } else if (isHolderParameter && isWebParamInMode(parameter)){
                    printError(parameter.getPosition(),
                            JAXWSCoreMessages.HOLDER_TYPE_MUST_BE_OUT_INOUT);
                }
            }
        }
    }
    
    private boolean isWebParamInMode(ParameterDeclaration parameter) {
        String mode = getMode(parameter);
        if (mode != null) {
            return mode.equals(WebParam.Mode.IN.toString());
        }
        return false;
    }

    private boolean isWebParamOutInoutMode(ParameterDeclaration parameter) {
        String mode = getMode(parameter);
        if (mode != null) {
            return mode.equals(WebParam.Mode.OUT.name()) || mode.equals(WebParam.Mode.INOUT.name());
        }
        return false;
    }

    private String getMode(ParameterDeclaration parameter) {
        AnnotationMirror webParam = AnnotationUtils.getAnnotation(parameter, WebParam.class);
        if (webParam != null) {
            return AnnotationUtils.getStringValue(webParam, MODE);
        }
        return null;
    }

}
