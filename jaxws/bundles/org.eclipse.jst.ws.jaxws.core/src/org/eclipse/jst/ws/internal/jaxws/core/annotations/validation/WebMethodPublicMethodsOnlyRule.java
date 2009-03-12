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
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.Modifier;

/**
 * 
 * @author sclarke
 * 
 */
public class WebMethodPublicMethodsOnlyRule extends AbstractAnnotationProcessor {
	
    public WebMethodPublicMethodsOnlyRule() {
    }
    
    @Override
    public void process() {
        Messager messager = environment.getMessager();

        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration("javax.jws.WebMethod"); //$NON-NLS-1$

        Collection<Declaration> annotatedTypes = environment.getDeclarationsAnnotatedWith(annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
        	
        	Collection<Modifier> modifiers = declaration.getModifiers();
        	for (Modifier modifier : modifiers) {
			    if (modifier.equals(Modifier.PRIVATE) || modifier.equals(Modifier.PROTECTED)) {
		            Collection<AnnotationMirror> annotationMirrors = declaration.getAnnotationMirrors();
		            for (AnnotationMirror mirror : annotationMirrors) {
		            	if ( mirror.getAnnotationType().toString().equals(annotationDeclaration
		            			.getQualifiedName())) {
			            	messager.printError(mirror.getPosition(), JAXWSCoreMessages.
			            	        WEBMETHOD_ANNOTATION_PROCESSOR_ONLY_ON_PUBLIC_METHODS_MESSAGE);	
		            	}
		            }
			    }
			}
        }
	}
}
