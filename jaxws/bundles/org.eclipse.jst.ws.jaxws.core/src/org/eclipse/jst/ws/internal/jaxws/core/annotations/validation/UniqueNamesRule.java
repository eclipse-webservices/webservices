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

import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCoreMessages;
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
public class UniqueNamesRule extends AbstractJAXWSAnnotationProcessor {
    
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
                checkWrapperBeanNames(typeDeclaration.getMethods());
                checkFaultBeanNames(typeDeclaration.getMethods());
                //checkDocBareMethods(typeDeclaration.getMethods());
            }
        }
    }
    
    private void checkOperationNames(Collection<? extends MethodDeclaration> methods) {
        for (MethodDeclaration methodDeclaration : methods) {
            List<MethodDeclaration> overloadedMethods = findOverloadedMethod(methodDeclaration, methods);
            for (MethodDeclaration overloadedMethod : overloadedMethods) {
                if (compareOperationNames(methodDeclaration, overloadedMethod)) {
                    printError(methodDeclaration.getPosition(),
                            JAXWSCoreMessages.OPERATION_NAMES_MUST_BE_UNIQUE_ERROR_MESSAGE);
                    printError(overloadedMethod.getPosition(),
                            JAXWSCoreMessages.OPERATION_NAMES_MUST_BE_UNIQUE_ERROR_MESSAGE);
                }
            }
        }
    }

    private void checkWrapperBeanNames(Collection<? extends MethodDeclaration> methodDeclarations) {
        AnnotationTypeDeclaration requestWrapperDeclaration = (AnnotationTypeDeclaration) environment
        .getTypeDeclaration(RequestWrapper.class.getName());

        AnnotationTypeDeclaration resposeWrapperDeclaration = (AnnotationTypeDeclaration) environment
        .getTypeDeclaration(ResponseWrapper.class.getName());

        Set<Declaration> methods = new HashSet<Declaration>();
        methods.addAll(environment.getDeclarationsAnnotatedWith(requestWrapperDeclaration));
        methods.addAll(environment.getDeclarationsAnnotatedWith(resposeWrapperDeclaration));
        
        List<AnnotationValue> classNames = new ArrayList<AnnotationValue>();
        
        for (Declaration declaration : methods) {
            AnnotationMirror requestWrapper = AnnotationUtils.getAnnotation(declaration, RequestWrapper.class);
            if (requestWrapper != null) {
                AnnotationValue className = AnnotationUtils.getAnnotationValue(requestWrapper, CLASS_NAME);
                if (className != null) {
                    classNames.add(className);
                }
            }
            AnnotationMirror responseWrapper = AnnotationUtils.getAnnotation(declaration, ResponseWrapper.class);
            if (responseWrapper != null) {
                AnnotationValue className = AnnotationUtils.getAnnotationValue(responseWrapper, CLASS_NAME);
                if (className != null) {
                    classNames.add(className);
                }
            }
        }
        
        for (int i = 0; i < classNames.size(); i++) {
            AnnotationValue className = classNames.get(i);
            for (int j = i + 1; j < classNames.size(); j++) {
                AnnotationValue otherClassName = classNames.get(j);
                if (className.getValue().equals(otherClassName.getValue())) {
                    printError(className.getPosition(), JAXWSCoreMessages.WRAPPER_BEAN_NAMES_MUST_BE_UNIQUE_ERROR_MESSAGE);
                    printError(otherClassName.getPosition(), JAXWSCoreMessages.WRAPPER_BEAN_NAMES_MUST_BE_UNIQUE_ERROR_MESSAGE);
                }
            }
        }        
    }
    
    private void checkFaultBeanNames(Collection<? extends MethodDeclaration> methodDeclarations) {
        Set<ReferenceType> thrownTypes = new HashSet<ReferenceType>();
        
        for (MethodDeclaration methodDeclaration : methodDeclarations) {
            thrownTypes.addAll(methodDeclaration.getThrownTypes());
        }
        
        List<AnnotationValue> faultBeans = new ArrayList<AnnotationValue>();

        for (ReferenceType referenceType : thrownTypes) {
            if (referenceType instanceof ClassDeclaration) {
                ClassDeclaration classDeclaration = (ClassDeclaration) referenceType;
                AnnotationMirror webFault = AnnotationUtils.getAnnotation(classDeclaration, WebFault.class);
                if (webFault != null) {
                    AnnotationValue annotationValue = AnnotationUtils.getAnnotationValue(webFault, FAULT_BEAN);
                    if (annotationValue != null) {
                        faultBeans.add(annotationValue);
                    }
                }
            }
        }
       
       for (int i = 0; i < faultBeans.size(); i++) {
            AnnotationValue faultBean = faultBeans.get(i);
            for (int j = i + 1; j < faultBeans.size(); j++) {
                AnnotationValue otherFaultBean = faultBeans.get(j);
                if (faultBean.getValue().equals(otherFaultBean.getValue())) {
                    printError(JAXWSCoreMessages.FAULT_BEAN_NAMES_MUST_BE_UNIQUE_ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void checkDocBareMethods(Collection<? extends MethodDeclaration> methods) {
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
                    printError(keys[i].getPosition(), JAXWSCoreMessages.DOC_BARE_METHODS_UNIQUE_XML_ELEMENTS_ERROR_MESSAGE);
                    printError(keys[j].getPosition(), JAXWSCoreMessages.DOC_BARE_METHODS_UNIQUE_XML_ELEMENTS_ERROR_MESSAGE);
                }
            }
        }
    }

    private String getTargetNamespace(AnnotationMirror webResult, MethodDeclaration methodDeclaration) {
        String targetNamespace = AnnotationUtils.getStringValue(webResult, TARGET_NAMESPACE);
        if (targetNamespace == null) {
            targetNamespace = getTargetNamespace(methodDeclaration);
        }
        return targetNamespace;
    }
    
    private String getTargetNamespace(MethodDeclaration methodDeclaration) {
        TypeDeclaration typeDeclaration = methodDeclaration.getDeclaringType();
        AnnotationMirror webService = AnnotationUtils.getAnnotation(typeDeclaration, WebService.class);
        String targetNamespace = AnnotationUtils.getStringValue(webService, TARGET_NAMESPACE);
        if (targetNamespace != null) {
            return targetNamespace;
        }
        return JDTUtils.getTargetNamespaceFromPackageName(typeDeclaration.getPackage().getQualifiedName());
    }

    private boolean hasDocumentBareSOAPBinding(Declaration declaration) {
        AnnotationMirror soapBinding = AnnotationUtils.getAnnotation(declaration, SOAPBinding.class);
        if (soapBinding != null) {
            return isDocumentBare(soapBinding);
        }
        if (declaration instanceof MethodDeclaration) {
            MethodDeclaration methodDeclaration = (MethodDeclaration) declaration;
            return hasDocumentBareSOAPBinding(methodDeclaration.getDeclaringType());
        }
        
        return false;
    }

    private List<MethodDeclaration> findOverloadedMethod(MethodDeclaration methodToTest, Collection<? extends MethodDeclaration> methods) {
        List<MethodDeclaration> overloadedMethods = new ArrayList<MethodDeclaration>();
        for (MethodDeclaration method : methods) {
            if (!methodToTest.equals(method) && methodToTest.getSimpleName().equals(method.getSimpleName())) {
                overloadedMethods.add(method);
            }
        }
        return overloadedMethods;
    }
    
    private boolean compareOperationNames(MethodDeclaration methodOne, MethodDeclaration methodTwo) {
        return getOperationName(methodOne).equals(getOperationName(methodTwo));
    }
    
    private String getOperationName(MethodDeclaration methodDeclaration) {
        String operationName = methodDeclaration.getSimpleName();
        AnnotationMirror webMethod = AnnotationUtils.getAnnotation(methodDeclaration, WebMethod.class);
        if (webMethod != null) {
            operationName = AnnotationUtils.getStringValue(webMethod, OPERATION_NAME);
        }
        return operationName;
    }
    
}
