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

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.HEADER;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.PARAMETER_STYLE;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.PART_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.STYLE;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.USE;

import java.util.Collection;

import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

public class SOAPBindingRules extends AbstractAnnotationProcessor {

    @Override
    public void process() {
        AnnotationTypeDeclaration soapBindingDeclaration = (AnnotationTypeDeclaration) environment
        .getTypeDeclaration(SOAPBinding.class.getName());

        Collection<Declaration> annotatedTypes = environment
        .getDeclarationsAnnotatedWith(soapBindingDeclaration);

        for (Declaration declaration : annotatedTypes) {
            checkForEncodedUse(declaration);
            if (declaration instanceof TypeDeclaration) {
                TypeDeclaration typeDeclaration = (TypeDeclaration) declaration;
                checkForRPCBareOnTypeDeclaration(typeDeclaration);
                checkRedundantWebParamNameAttribute(typeDeclaration);
            }

            if (declaration instanceof MethodDeclaration) {
                checkForRPCStyleOnMethodDeclaration((MethodDeclaration) declaration);
            }
        }
    }

    private void checkForRPCBareOnTypeDeclaration(TypeDeclaration typeDeclaration) {
        AnnotationMirror soapBinding = AnnotationUtils.getAnnotation(typeDeclaration, SOAPBinding.class);
        String style = AnnotationUtils.getStringValue(soapBinding, STYLE);
        if (style != null && style.equals(Style.RPC.toString())) {
            String parameterStyle = AnnotationUtils.getStringValue(soapBinding, PARAMETER_STYLE);
            if (parameterStyle != null && parameterStyle.equals(ParameterStyle.BARE.toString())) {
                printError(soapBinding.getPosition(), JAXWSCoreMessages.SOAPBINDING_RPC_NO_BARE_PARAMETER_STYLE);
            }
            // check for Document style @SOAPBinding on methods
            Collection<? extends MethodDeclaration> methodDeclarations = typeDeclaration.getMethods();
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                AnnotationMirror soapBinding2 = AnnotationUtils.getAnnotation(methodDeclaration, SOAPBinding.class);
                if (soapBinding2 != null) {
                    String style2 = AnnotationUtils.getStringValue(soapBinding2, STYLE);
                    if (style2 == null || style2.equals(Style.DOCUMENT.toString())) {
                        printError(soapBinding2.getPosition(), JAXWSCoreMessages.SOAPBINDING_NO_MIXED_BINDINGS);
                    }
                }
            }
        }
    }

    private void checkForEncodedUse(Declaration declaration) {
        AnnotationMirror soapBinding = AnnotationUtils.getAnnotation(declaration, SOAPBinding.class);
        String use = AnnotationUtils.getStringValue(soapBinding, USE);
        if (use != null && use.equals(Use.ENCODED.toString())) {
            String style = AnnotationUtils.getStringValue(soapBinding, STYLE);
            if (style == null || style.equals(Style.DOCUMENT.toString())) {
                printError(soapBinding.getPosition(), JAXWSCoreMessages.SOAPBINDING_DOCUMENT_ENCODED_NOT_SUPPORTED);
            } else {
                printError(soapBinding.getPosition(), JAXWSCoreMessages.SOAPBINDING_RPC_ENCODED_NOT_SUPPORTED);
            }
        }
    }

    private void checkForRPCStyleOnMethodDeclaration(MethodDeclaration methodDeclaration) {
        AnnotationMirror soapBinding = AnnotationUtils.getAnnotation(methodDeclaration, SOAPBinding.class);
        String style = AnnotationUtils.getStringValue(soapBinding, STYLE);
        if (style != null && style.equals(Style.RPC.toString())) {
            printFixableError(soapBinding.getPosition(),
                    JAXWSCoreMessages.SOAPBINDING_NO_RPC_STYLE_ON_METHODS);
        }
    }

    private void checkRedundantWebParamNameAttribute(TypeDeclaration typeDeclaration) {
        AnnotationMirror soapBinding = AnnotationUtils.getAnnotation(typeDeclaration, SOAPBinding.class);
        String style = AnnotationUtils.getStringValue(soapBinding, STYLE);
        if (style != null && style.equals(Style.RPC.toString())) {
            Collection<? extends MethodDeclaration> methods = typeDeclaration.getMethods();
            for (MethodDeclaration methodDeclaration : methods) {
                Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
                for (ParameterDeclaration parameterDeclaration : parameters) {
                    AnnotationMirror webParam = AnnotationUtils.getAnnotation(parameterDeclaration, WebParam.class);
                    if (webParam != null && !isHeader(webParam)) {
                        AnnotationValue partName = AnnotationUtils.getAnnotationValue(webParam, PART_NAME);
                        if (partName != null) {
                            AnnotationValue name = AnnotationUtils.getAnnotationValue(webParam, NAME);
                            if (name != null) {
                                printWarning(name.getPosition(), JAXWSCoreMessages.WEBPARAM_NAME_REDUNDANT);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isHeader(AnnotationMirror annotationMirror) {
        Boolean header = AnnotationUtils.getBooleanValue(annotationMirror, HEADER);
        if (header != null) {
            return header.booleanValue();
        }
        return false;
    }

}
