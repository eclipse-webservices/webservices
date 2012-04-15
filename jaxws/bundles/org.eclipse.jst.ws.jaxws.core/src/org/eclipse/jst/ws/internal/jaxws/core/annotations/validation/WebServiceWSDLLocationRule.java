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
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.EXCLUDE;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.OPERATION_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.PORT_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.PORT_SUFFIX;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.SERVICE_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.SERVICE_SUFFIX;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.STYLE;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.TARGET_NAMESPACE;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.WSDL_LOCATION;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap12.SOAP12Binding;
import javax.xml.namespace.QName;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.apt.core.env.EclipseAnnotationProcessorEnvironment;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;
import org.eclipse.jst.ws.jaxws.core.utils.WSDLUtils;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

public class WebServiceWSDLLocationRule extends AbstractAnnotationProcessor {

    @Override
    public void process() {
        AnnotationTypeDeclaration annotationDeclaration = (AnnotationTypeDeclaration) environment
        .getTypeDeclaration(WebService.class.getName());

        Collection<Declaration> annotatedTypes = environment
        .getDeclarationsAnnotatedWith(annotationDeclaration);

        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof ClassDeclaration) {
                ClassDeclaration classDeclaration = (ClassDeclaration) declaration;
                checkWSDLocation(classDeclaration);
            }
        }
    }

    private void checkWSDLocation(ClassDeclaration classDeclaration) {
        AnnotationMirror webService = AnnotationUtils.getAnnotation(classDeclaration, WebService.class);
        AnnotationValue wsdlLocationValue = AnnotationUtils.getAnnotationValue(webService, WSDL_LOCATION);
        if (wsdlLocationValue != null) {
            String wsdlLocation = wsdlLocationValue.getValue().toString().trim();
            if (wsdlLocation.length() > 0) {
                if (wsdlLocation.endsWith(".wsdl")) { //$NON-NLS-1$
                    URL relativeURL = getRelativeURL(classDeclaration, wsdlLocation);
                    if (relativeURL != null) {
                        validateWSDL(relativeURL.toString(), webService, classDeclaration, wsdlLocationValue);
                    } else {
                        printWarning(wsdlLocationValue.getPosition(), JAXWSCoreMessages.bind(
                                JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_UNABLE_TO_LOCATE, wsdlLocation));
                    }
                } else {
                    validateWSDL(wsdlLocation, webService, classDeclaration, wsdlLocationValue);
                }

            } else {
                printError(wsdlLocationValue.getPosition(), JAXWSCoreMessages.bind(
                        JAXWSCoreMessages.EMPTY_ATTRIBUTE_VALUE, new Object[] {WebService.class.getSimpleName(),
                                WSDL_LOCATION}));
            }
        }
    }

    private URL getRelativeURL(ClassDeclaration classDeclaration, String wsdlLocation) {
        if (environment instanceof EclipseAnnotationProcessorEnvironment) {
            EclipseAnnotationProcessorEnvironment eclipseEnvironment = (EclipseAnnotationProcessorEnvironment) environment;
            IJavaProject javaProject = eclipseEnvironment.getJavaProject();
            IFolder webContentFolder = WSDLUtils.getWebContentFolder(javaProject.getProject());
            if (webContentFolder != null) {
                IResource wsdlResource = webContentFolder.findMember(wsdlLocation);
                if (wsdlResource != null) {
                    try {
                        return wsdlResource.getLocationURI().toURL();
                    } catch (MalformedURLException murle) {
                        JAXWSCorePlugin.log(murle);
                    }
                }
            }
        }
        return null;
    }

    private void validateWSDL(String wsdlLocation, AnnotationMirror webService, ClassDeclaration classDeclaration, AnnotationValue wsdlLocationValue) {
        try {
            URL wsdlURL = new URL(wsdlLocation);
            Definition definition = WSDLUtils.readWSDL(wsdlURL);
            if (definition != null) {
                QName serviceQName =  getServiceQName(webService, classDeclaration);
                Service service = getService(definition, serviceQName);
                if (service != null) {
                    String portName =  getPortName(webService, classDeclaration);
                    Port port = getPort(service, portName);
                    if (port != null) {
                        Binding binding = port.getBinding();
                        validateBinding(binding, classDeclaration, webService, wsdlLocation);
                        Map<String, MethodDeclaration> methodMap = getWebMethods(classDeclaration,
                                webService);
                        @SuppressWarnings("unchecked")
                        List<Operation> operations = binding.getPortType().getOperations();
                        for (Operation operation : operations) {
                            MethodDeclaration methodDeclaration = methodMap.get(operation.getName());
                            if (methodDeclaration == null) {
                                printError(webService.getPosition(), JAXWSCoreMessages.bind(
                                        JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_NO_OPERTATION_NAME,
                                        operation.getName()));
                            } else {
                                AnnotationMirror oneway = AnnotationUtils.getAnnotation(
                                        methodDeclaration, Oneway.class);
                                if (oneway != null && operation.getOutput() != null) {
                                    printError(methodDeclaration.getPosition(), JAXWSCoreMessages.bind(
                                            JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_WSDL_OPERATION_OUTPUT_METHOD_ONEWAY,
                                            new Object[] {methodDeclaration.getSimpleName(), operation.getName()}));
                                }
                            }
                        }
                    } else {
                        printError(webService.getPosition(), JAXWSCoreMessages.bind(
                                JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_NO_PORT_NAME,
                                new Object[] {portName, wsdlLocation}));
                    }
                } else {
                    printError(webService.getPosition(), JAXWSCoreMessages.bind(
                            JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_NO_SERVICE_NAME,
                            new Object[] {serviceQName.getLocalPart(), wsdlLocation}));
                }
            } else {
                printWarning(wsdlLocationValue.getPosition(), JAXWSCoreMessages.bind(
                        JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_UNABLE_TO_LOCATE, wsdlLocation));
            }
        } catch (MalformedURLException murle) {
            printError(wsdlLocationValue.getPosition(), murle.getLocalizedMessage());
        } catch (IOException ioe) {
            printWarning(wsdlLocationValue.getPosition(), JAXWSCoreMessages.bind(
                    JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_UNABLE_TO_LOCATE, wsdlLocation));
        }
    }

    private void validateBinding(Binding binding, ClassDeclaration classDeclaration,
            AnnotationMirror webService, String wsdlLocation) {
        String style = javax.jws.soap.SOAPBinding.Style.DOCUMENT.name();

        AnnotationMirror soapBindingAnnotation = getSOAPBinding(classDeclaration, webService);
        if (soapBindingAnnotation != null) {
            String styleValue = AnnotationUtils.getStringValue(soapBindingAnnotation, STYLE);
            if (styleValue != null) {
                style = styleValue;
            }
        }

        List<?> extensibilityElements =  binding.getExtensibilityElements();
        for (Object elementExtensible : extensibilityElements) {
            if (elementExtensible instanceof SOAPBinding) {
                SOAPBinding soapBinding = (SOAPBinding) elementExtensible;
                if (soapBinding.getStyle() != null && style.compareToIgnoreCase(soapBinding.getStyle()) != 0) {
                    printError(soapBindingAnnotation == null ? webService.getPosition()
                            : soapBindingAnnotation.getPosition(),
                            JAXWSCoreMessages.bind(JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_SOAP_BINDING_STYLE,
                                    new Object[] {wsdlLocation, soapBinding.getStyle(), style}));
                }
            }
            if (elementExtensible instanceof SOAP12Binding) {
                SOAP12Binding soap12Binding = (SOAP12Binding) elementExtensible;
                if (soap12Binding.getStyle() != null && style.compareToIgnoreCase(soap12Binding.getStyle()) != 0) {
                    printError(soapBindingAnnotation == null ? webService.getPosition()
                            : soapBindingAnnotation.getPosition(),
                            JAXWSCoreMessages.bind(JAXWSCoreMessages.WEBSERVICE_WSDL_LOCATION_SOAP_BINDING_STYLE,
                                    new Object[] {wsdlLocation, soap12Binding.getStyle(), style}));
                }
            }
        }
    }

    private AnnotationMirror getSOAPBinding(TypeDeclaration typeDeclaration, AnnotationMirror webService) {
        String endpointInterface = AnnotationUtils.getStringValue(webService, ENDPOINT_INTERFACE);
        if (endpointInterface != null) {
            TypeDeclaration seiDeclaration = environment.getTypeDeclaration(endpointInterface);
            if (seiDeclaration != null) {
                typeDeclaration = seiDeclaration;
            }
        }
        return AnnotationUtils.getAnnotation(typeDeclaration, javax.jws.soap.SOAPBinding.class);
    }

    private Map<String, MethodDeclaration> getWebMethods(TypeDeclaration typeDeclaration, AnnotationMirror webService) {
        Map<String, MethodDeclaration> methodMap = new HashMap<String, MethodDeclaration>();

        String endpointInterface = AnnotationUtils.getStringValue(webService, ENDPOINT_INTERFACE);
        if (endpointInterface != null) {
            TypeDeclaration seiDeclaration = environment.getTypeDeclaration(endpointInterface);
            if (seiDeclaration != null) {
                typeDeclaration = seiDeclaration;
            }
        }

        Collection<? extends MethodDeclaration> methods = typeDeclaration.getMethods();
        for (MethodDeclaration methodDeclaration : methods) {
            AnnotationMirror webMethod = AnnotationUtils.getAnnotation(methodDeclaration, WebMethod.class);
            if (webMethod != null && !isExluded(webMethod)) {
                methodMap.put(getOperationName(webMethod, methodDeclaration), methodDeclaration);
            } else {
                methodMap.put(methodDeclaration.getSimpleName(), methodDeclaration);
            }
        }
        return methodMap;
    }

    private boolean isExluded(AnnotationMirror webMethod) {
        Boolean exclude = AnnotationUtils.getBooleanValue(webMethod, EXCLUDE);
        if (exclude != null) {
            return exclude.booleanValue();
        }
        return false;
    }

    private String getOperationName(AnnotationMirror webMethod, MethodDeclaration methodDeclaration) {
        String operationName = AnnotationUtils.getStringValue(webMethod, OPERATION_NAME);
        if (operationName != null) {
            return operationName;
        }
        return methodDeclaration.getSimpleName();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Service getService(Definition definition, QName serviceQName) {
        Map servicesMap = definition.getServices();
        Set<Map.Entry> servicesSet = servicesMap.entrySet();
        for (Map.Entry<QName, Service> serviceEntry : servicesSet) {
            QName serviceEntryKey = serviceEntry.getKey();
            if (serviceEntryKey.equals(serviceQName)) {
                return serviceEntry.getValue();
            }
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Port getPort(Service service, String portName) {
        Map portsMap = service.getPorts();
        Set<Map.Entry> portsSet = portsMap.entrySet();
        for (Map.Entry<String, Port> portEntry : portsSet) {
            String portEntryKey = portEntry.getKey();
            if (portEntryKey.equals(portName)) {
                return portEntry.getValue();
            }
        }
        return null;
    }

    private QName getServiceQName(AnnotationMirror webService, ClassDeclaration classDeclaration) {
        String serviceName = AnnotationUtils.getStringValue(webService, SERVICE_NAME);
        if (serviceName == null) {
            serviceName = classDeclaration.getSimpleName() + SERVICE_SUFFIX;
        }
        return new QName(getTargetNamespace(webService, classDeclaration), serviceName);
    }

    private String getPortName(AnnotationMirror webService, ClassDeclaration classDeclaration) {
        String portName = AnnotationUtils.getStringValue(webService, PORT_NAME);
        if (portName == null) {
            portName = classDeclaration.getSimpleName() + PORT_SUFFIX;
        }
        return portName;
    }

    private String getTargetNamespace(AnnotationMirror webService, ClassDeclaration classDeclaration) {
        String targetNamespace = AnnotationUtils.getStringValue(webService, TARGET_NAMESPACE);
        if (targetNamespace == null) {
            targetNamespace = JDTUtils.getTargetNamespaceFromPackageName(
                    classDeclaration.getPackage().getQualifiedName());
        }
        return targetNamespace;
    }

}
