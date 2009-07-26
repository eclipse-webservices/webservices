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

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.ENDPOINT_INTERFACE;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.PORT_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.SERVICE_NAME;

import java.util.Collection;

import javax.jws.WebService;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;

public class WebServiceSEIAllowedAttributesRule extends AbstractAnnotationProcessor {

    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(WebService.class.getName());

        Collection<Declaration> annotatedTypes = environment
                .getDeclarationsAnnotatedWith(annotationDeclaration);
        
        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof InterfaceDeclaration) {
                Collection<AnnotationMirror> annotationMirrors = declaration.getAnnotationMirrors();
        
                for (AnnotationMirror mirror : annotationMirrors) {
                    AnnotationValue serviceName = AnnotationUtils.getAnnotationValue(mirror, SERVICE_NAME);
                    if (serviceName != null) {
                        printFixableError(serviceName.getPosition(),
                                JAXWSCoreMessages.WEBSERVICE_SERVICENAME_SEI);
                    }
                
                    AnnotationValue endpointInterface = AnnotationUtils.getAnnotationValue(mirror, ENDPOINT_INTERFACE);
                    if (endpointInterface != null) {
                        printFixableError(endpointInterface.getPosition(),
                                JAXWSCoreMessages.WEBSERVICE_ENDPOINTINTERFACE_SEI);
                    }
                    
                    AnnotationValue portName = AnnotationUtils.getAnnotationValue(mirror, PORT_NAME);
                    if (portName != null) {
                        printFixableError(portName.getPosition(),
                                JAXWSCoreMessages.WEBSERVICE_PORTNAME_SEI);
                    }
                }
            }
        }
    }
   
}
