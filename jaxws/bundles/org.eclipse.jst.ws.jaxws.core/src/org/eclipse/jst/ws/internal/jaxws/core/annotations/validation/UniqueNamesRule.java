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

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.CLASS_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.FAULT_BEAN;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.OPERATION_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.TARGET_NAMESPACE;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import javax.xml.ws.WebFault;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils;
import org.eclipse.jst.ws.jaxws.core.utils.JDTUtils;

import com.sun.mirror.declaration.AnnotationMirror;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.AnnotationValue;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.ReferenceType;

/**
 * 
 * @author sclarke
 *
 */
public class UniqueNamesRule extends AbstractAnnotationProcessor {

    @Override
    public void process() {
        AnnotationTypeDeclaration webServiceDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(WebService.class.getName());

        Collection<Declaration> annotatedTypes = environment
                .getDeclarationsAnnotatedWith(webServiceDeclaration);

        for (Declaration declaration : annotatedTypes) {
            if (declaration instanceof TypeDeclaration) {
                TypeDeclaration typeDeclaration = (TypeDeclaration) declaration;
                checkOperationNames(typeDeclaration.getMethods());
                checkWrapperAndFaultBeanNames(typeDeclaration.getMethods());
                //checkDocumentBareMethods(typeDeclaration.getMethods());
            }
        }
    }
    
    private void checkOperationNames(Collection<? extends MethodDeclaration> methods) {
        Map<MethodDeclaration, QName> methodNameMap = new HashMap<MethodDeclaration, QName>();
        for (MethodDeclaration methodDeclaration : methods) {
            methodNameMap.put(methodDeclaration, new QName(getTargetNamespace(methodDeclaration.getDeclaringType()), 
                    getOperationName(methodDeclaration)));
        }
        
        MethodDeclaration[] keys = methodNameMap.keySet().toArray(new MethodDeclaration[methodNameMap.size()]);
        QName[] values = methodNameMap.values().toArray(new QName[methodNameMap.size()]);

        for (int i = 0; i < values.length; i++) {
            QName name = values[i];
            for (int j = i + 1; j < values.length; j++) {
                QName otherName = values[j];
                if (name.equals(otherName)) {
                    printError(keys[i].getPosition(), JAXWSCoreMessages.bind(
                            JAXWSCoreMessages.OPERATION_NAMES_MUST_BE_UNIQUE_ERROR, name));
                    printError(keys[j].getPosition(), JAXWSCoreMessages.bind(
                            JAXWSCoreMessages.OPERATION_NAMES_MUST_BE_UNIQUE_ERROR, otherName));
                }
            }
        }
    }
        
    private String getAttributeValue(Declaration declaration, Class<? extends Annotation> annotation, String attributeName) {
        AnnotationMirror annotationMirror = AnnotationUtils.getAnnotation(declaration, annotation);
        if (annotationMirror != null) {
            return AnnotationUtils.getStringValue(annotationMirror, attributeName);
        }
        return null;
    }

    private void checkWrapperAndFaultBeanNames(Collection<? extends MethodDeclaration> methodDeclarations) {
        AnnotationTypeDeclaration requestWrapperDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(RequestWrapper.class.getName());

        AnnotationTypeDeclaration resposeWrapperDeclaration = (AnnotationTypeDeclaration) environment
                .getTypeDeclaration(ResponseWrapper.class.getName());

        Set<Declaration> methods = new HashSet<Declaration>();
        methods.addAll(environment.getDeclarationsAnnotatedWith(requestWrapperDeclaration));
        methods.addAll(environment.getDeclarationsAnnotatedWith(resposeWrapperDeclaration));

        List<AnnotationValue> classNames = new ArrayList<AnnotationValue>();

        for (Declaration declaration : methods) {
            AnnotationMirror requestWrapper = AnnotationUtils
                    .getAnnotation(declaration, RequestWrapper.class);
            if (requestWrapper != null) {
                AnnotationValue className = AnnotationUtils.getAnnotationValue(requestWrapper, CLASS_NAME);
                if (className != null) {
                    classNames.add(className);
                }

            }
            AnnotationMirror responseWrapper = AnnotationUtils.getAnnotation(declaration,
                    ResponseWrapper.class);
            if (responseWrapper != null) {
                AnnotationValue className = AnnotationUtils.getAnnotationValue(responseWrapper, CLASS_NAME);
                if (className != null) {
                    classNames.add(className);
                }
            }

        }
        
        Set<ReferenceType> thrownTypes = new HashSet<ReferenceType>();
        
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            thrownTypes.addAll(methodDeclaration.getThrownTypes());
        }

        for (ReferenceType referenceType : thrownTypes) {
            if (referenceType instanceof ClassDeclaration) {
                ClassDeclaration classDeclaration = (ClassDeclaration) referenceType;
                AnnotationMirror webFault = AnnotationUtils.getAnnotation(classDeclaration, WebFault.class);
                if (webFault != null) {
                    AnnotationValue annotationValue = AnnotationUtils.getAnnotationValue(webFault, FAULT_BEAN);
                    if (annotationValue != null) {
                        classNames.add(annotationValue);
                    }
                }
            }
        }

        for (int i = 0; i < classNames.size(); i++) {
            AnnotationValue className = classNames.get(i);
            for (int j = i + 1; j < classNames.size(); j++) {
                AnnotationValue otherClassName = classNames.get(j);
                if (className.getValue().toString().equalsIgnoreCase(otherClassName.getValue().toString())) {
                    printError(className.getPosition(), JAXWSCoreMessages.bind(
                            JAXWSCoreMessages.WRAPPER_FAULT_BEAN_NAMES_MUST_BE_UNIQUE, className));
                    printError(otherClassName.getPosition(), JAXWSCoreMessages.bind(
                            JAXWSCoreMessages.WRAPPER_FAULT_BEAN_NAMES_MUST_BE_UNIQUE, otherClassName));
                }
            }
        }
    }
    
    private void checkDocumentBareMethods(Collection<? extends MethodDeclaration> methods) {
        List<MethodDeclaration> docBareMethods = new ArrayList<MethodDeclaration>();
        for (MethodDeclaration methodDeclaration : methods) {
            if (hasDocumentBareSOAPBinding(methodDeclaration)) {
                docBareMethods.add(methodDeclaration);
            }
        }
        
        Map<AnnotationValue, QName> qNames = new HashMap<AnnotationValue, QName>();
        for (MethodDeclaration methodDeclaration : docBareMethods) {
            AnnotationMirror webResult = AnnotationUtils.getAnnotation(methodDeclaration, WebResult.class);
            if (webResult != null) {
                AnnotationValue name = AnnotationUtils.getAnnotationValue(webResult, NAME);
                if (name != null) {
                    QName qName = new QName(getTargetNamespace(webResult, methodDeclaration), name.getValue().toString());
                    qNames.put(name, qName);
                }
            }
            Collection<ParameterDeclaration> parameters = methodDeclaration.getParameters();
            for (ParameterDeclaration parameterDeclaration : parameters) {
                AnnotationMirror webParam = AnnotationUtils.getAnnotation(parameterDeclaration, WebParam.class);
                if (webParam != null) {
                    AnnotationValue name = AnnotationUtils.getAnnotationValue(webParam, NAME);
                    if (name != null) {
                        QName qName = new QName(getTargetNamespace(webParam, methodDeclaration), name.getValue().toString());
                        qNames.put(name, qName);
                    }
                }
            }
        }
        
        AnnotationValue[] keys =  qNames.keySet().toArray(new AnnotationValue[qNames.size()]);
        QName[] values = qNames.values().toArray(new QName[qNames.size()]);
       
        for(int i = 0; i < values.length; i++) {
            QName name = values[i];
            for(int j = i + 1; j < values.length; j++) {
                QName otherName = values[j];
                if (name.equals(otherName)) {
                    printError(keys[i].getPosition(), JAXWSCoreMessages.bind( 
                            JAXWSCoreMessages.DOC_BARE_METHODS_UNIQUE_XML_ELEMENTS, name));
                    printError(keys[j].getPosition(), JAXWSCoreMessages.bind( 
                            JAXWSCoreMessages.DOC_BARE_METHODS_UNIQUE_XML_ELEMENTS, otherName));
                }
            }
        }
    }

    private String getTargetNamespace(TypeDeclaration typeDeclaration) {
        String targetNamespace = getAttributeValue(typeDeclaration, WebService.class, TARGET_NAMESPACE);
        if (targetNamespace != null) {
            return targetNamespace;
        }
        
        return JDTUtils.getTargetNamespaceFromPackageName(typeDeclaration.getPackage().getQualifiedName());
    }
    
    private String getTargetNamespace(AnnotationMirror annotationMirror, MethodDeclaration methodDeclaration) {
        String targetNamespace = AnnotationUtils.getStringValue(annotationMirror, TARGET_NAMESPACE);
        if (targetNamespace == null) {
            targetNamespace = getTargetNamespace(methodDeclaration.getDeclaringType());
        }
        return targetNamespace;
    }

    private String getOperationName(MethodDeclaration methodDeclaration) {
        String operationName = getAttributeValue(methodDeclaration, WebMethod.class, OPERATION_NAME);
        if (operationName != null) {
            return operationName;
        }
        return methodDeclaration.getSimpleName();
    }

    private boolean hasDocumentBareSOAPBinding(Declaration declaration) {
        AnnotationMirror soapBinding = AnnotationUtils.getAnnotation(declaration, SOAPBinding.class);
        if (soapBinding != null) {
            return JAXWSUtils.isDocumentBare(soapBinding);
        }
        if (declaration instanceof MethodDeclaration) {
            MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
            return hasDocumentBareSOAPBinding(methodDeclaration.getDeclaringType());
        }
        
        return false;
    }
}
