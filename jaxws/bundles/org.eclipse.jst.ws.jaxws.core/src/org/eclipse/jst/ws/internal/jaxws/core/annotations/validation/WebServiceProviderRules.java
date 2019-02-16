/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.core.annotations.validation;

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.VALUE;

import java.util.Collection;

import javax.activation.DataSource;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.ws.BindingType;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.soap.SOAPBinding;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.TypeMirror;

public class WebServiceProviderRules extends AbstractAnnotationProcessor {

	@Override
	public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(WebServiceProvider.class.getName());

        Collection<Declaration> annotatedTypes = environment.getDeclarationsAnnotatedWith(annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
        	if (declaration instanceof ClassDeclaration) {
        		checkForDefaultConstructor((ClassDeclaration)declaration);
        		checkForProviderInterface((ClassDeclaration)declaration);
        	}
        }
	}
	
	private void checkForProviderInterface(ClassDeclaration classDeclaration) {
        Collection<InterfaceType> interfaces = classDeclaration.getSuperinterfaces();
        boolean implementsTypedProvider = false;
        if (interfaces.size() > 0) {
            for (InterfaceType interfaceType : interfaces) {
                Collection<TypeMirror> typeParameters = interfaceType.getActualTypeArguments();
                if (typeParameters.size() > 0) {
                    for (TypeMirror typeMirror : typeParameters) {
                        if (typeMirror instanceof ClassDeclaration) {
                            ClassDeclaration classDecl = (ClassDeclaration) typeMirror;
                            if (classDecl.getQualifiedName().equals(SOAPMessage.class.getCanonicalName())) {
                                checkSOAPMessageRequirements(classDeclaration);
                                implementsTypedProvider = true;
                                break;
                            }
                            Collection<InterfaceType> implementedInterfaces = classDecl.getSuperinterfaces();
                            for (InterfaceType implementedInterface : implementedInterfaces) {
                                InterfaceDeclaration interfaceDecl = implementedInterface.getDeclaration();
                                if (interfaceDecl != null) {
                                    if (interfaceDecl.getQualifiedName().equals(Source.class.getCanonicalName())) {
                                        implementsTypedProvider = true;
                                        break;
                                    }
                                    if (interfaceDecl.getQualifiedName().equals(DataSource.class.getCanonicalName())) {
                                        checkDataSourceRequirements(classDeclaration);
                                        implementsTypedProvider = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (typeMirror instanceof InterfaceDeclaration) {
                            InterfaceDeclaration interfaceDecl = (InterfaceDeclaration) typeMirror;
                            if (interfaceDecl.getQualifiedName().equals(Source.class.getCanonicalName())) {
                                implementsTypedProvider = true;
                                break;
                            }
                            if (interfaceDecl.getQualifiedName().equals(DataSource.class.getCanonicalName())) {
                                checkDataSourceRequirements(classDeclaration);
                                implementsTypedProvider = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (!implementsTypedProvider) {
            AnnotationMirror webServiceProvider = AnnotationUtils.getAnnotation(classDeclaration,
                    WebServiceProvider.class);
                printFixableError(webServiceProvider.getPosition(),
                        JAXWSCoreMessages.WEBSERVICEPROVIDER_IMPLEMENT_TYPED_PROVIDER_INTERFACE);
        }
    }
	
	private void checkSOAPMessageRequirements(ClassDeclaration classDeclaration) {
        if (!isSOAPBinding(classDeclaration)) {
            printError(classDeclaration.getPosition(), JAXWSCoreMessages.WEBSERVICEPROVIDER_SOAPMESSAGE_SOAPBINDING);
        }
        if (!isMessageMode(classDeclaration)) {
            printError(classDeclaration.getPosition(), JAXWSCoreMessages.WEBSERVICEPROVIDER_SOAPMESSAGE_MESSAGE_MODE);
        }
	}
	
	private void checkDataSourceRequirements(ClassDeclaration classDeclaration) {
        if (!isHTTPBinding(classDeclaration)) {
            printError(classDeclaration.getPosition(), JAXWSCoreMessages.WEBSERVICEPROVIDER_DATASOURCE_HTTPBINDING);
        }

	    if (!isMessageMode(classDeclaration)) {
            printError(classDeclaration.getPosition(), JAXWSCoreMessages.WEBSERVICEPROVIDER_DATASOURCE_MESSAGE_MODE);

        }
    }
	
    private boolean isSOAPBinding(ClassDeclaration classDeclaration) {
        String bindingType = getBindingTypeValue(classDeclaration);
        if (bindingType.equals(SOAPBinding.SOAP11HTTP_BINDING) 
                || bindingType.equals(SOAPBinding.SOAP12HTTP_BINDING)
                || bindingType.equals(SOAPBinding.SOAP11HTTP_MTOM_BINDING)
                || bindingType.equals(SOAPBinding.SOAP12HTTP_MTOM_BINDING)) {
            return true;
        }
        return false;
    }

    private boolean isHTTPBinding(ClassDeclaration classDeclaration) {
        String bindingType = getBindingTypeValue(classDeclaration);
        if (bindingType.equals(HTTPBinding.HTTP_BINDING)) {
            return true;
        }
        return false;
    }

    private String getBindingTypeValue(ClassDeclaration classDeclaration) {
        AnnotationMirror bindingType = AnnotationUtils.getAnnotation(classDeclaration, BindingType.class);
        if (bindingType != null) {
            String value = AnnotationUtils.getStringValue(bindingType, VALUE);
            if (value != null) {
                return value;
            }
        }
        return SOAPBinding.SOAP11HTTP_BINDING;
    }

    private boolean isMessageMode(ClassDeclaration classDeclaration) {
        return getServiceMode(classDeclaration).equals(Mode.MESSAGE.name());
    }

    private boolean isPayloadMode(ClassDeclaration classDeclaration) {
        return getServiceMode(classDeclaration).equals(Mode.PAYLOAD.name());
    }

    private String getServiceMode(ClassDeclaration classDeclaration) {
        AnnotationMirror serviceMode = AnnotationUtils.getAnnotation(classDeclaration, ServiceMode.class);
        if (serviceMode != null) {
            String value = AnnotationUtils.getStringValue(serviceMode, VALUE);
            if (value != null) {
                return value;
            }
        }
        return Mode.PAYLOAD.toString();
    }

    private void checkForDefaultConstructor(ClassDeclaration classDeclaration) {
		boolean hasDefaultConstructor = false;
		Collection<ConstructorDeclaration> constructors = classDeclaration.getConstructors();
		if (constructors.size() == 0) {
			hasDefaultConstructor = true;
		} else {
			for (ConstructorDeclaration constructorDeclaration : constructors) {
				if (constructorDeclaration.getModifiers().contains(Modifier.PUBLIC) && 
						constructorDeclaration.getParameters().size() == 0) {
					hasDefaultConstructor = true;
					break;
				}
			}
		}
		if (!hasDefaultConstructor) {
			AnnotationMirror webServiceProvider = AnnotationUtils.getAnnotation(classDeclaration, WebServiceProvider.class);
			printFixableError(webServiceProvider.getPosition(),
			        JAXWSCoreMessages.WEBSERVICEPROVIDER_DEFAULT_PUBLIC_CONSTRUCTOR); 
		}                

	}
}
