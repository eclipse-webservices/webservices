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
package org.eclipse.jst.ws.jaxws.core.tests;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceRef;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;

public class UpdateMemberValuePairTest extends AbstractAnnotationTest {

    @Override
    protected Annotation getAnnotation() {
        return null;
    }

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("import javax.jws.WebService;\n");
        classContents.append("import javax.jws.WebMethod;\n");
        classContents.append("import javax.jws.WebParam;\n");
        classContents.append("import javax.xml.ws.WebServiceRef;\n\n");
        classContents.append("@WebService(name=\"Calculator\")\n");
        classContents.append("public class Calculator {\n\n");
        classContents.append("\t@WebServiceRef(mappedName=\"myField\")\n");
        classContents.append("\tpublic String myField;\n\n");
        classContents.append("\t@WebMethod(operationName=\"add\")\n");
        classContents.append("\tpublic int add(@WebParam(partName=\"i\") int i, int k) {");
        classContents.append("\n\t\treturn i + k;\n\t}\n}");
        return classContents.toString();
    }

    @Override
    protected String getClassName() {
        return "Calculator.java";
    }

    @Override
    protected String getPackageName() {
        return "com.example";
    }

    public void testUpdateMemberValuePair() {
        try {
            //@WebService
            IType type = source.findPrimaryType();
            assertTrue(AnnotationUtils.isAnnotationPresent(source, "WebService"));
            Annotation webService = AnnotationUtils.getAnnotation(type, WebService.class);
            assertNotNull(webService);
            assertTrue(webService instanceof NormalAnnotation);

            NormalAnnotation webServiceAnnotation = (NormalAnnotation) webService;
            assertTrue(webServiceAnnotation.values().size() == 1);

            MemberValuePair nameValuePair = AnnotationUtils.getMemberValuePair(webServiceAnnotation, "name");
            assertNotNull(nameValuePair);
            StringLiteral nameValue =  (StringLiteral) nameValuePair.getValue();
            assertTrue(nameValue.getLiteralValue().equals("Calculator"));

            AnnotationUtils.updateMemberValuePair(nameValuePair, AnnotationsCore.createStringLiteral(ast, "ScientificCalculator"));
            webServiceAnnotation = (NormalAnnotation) AnnotationUtils.getAnnotation(type, WebService.class);
            nameValuePair = AnnotationUtils.getMemberValuePair(webServiceAnnotation, "name");
            nameValue =  (StringLiteral) nameValuePair.getValue();
            assertTrue(nameValue.getLiteralValue().equals("ScientificCalculator"));

            //@WebServiceRef
            IField field = type.getField("myField");
            assertTrue(AnnotationUtils.isAnnotationPresent(field, "WebServiceRef"));
            Annotation webServiceRef = AnnotationUtils.getAnnotation(field, WebServiceRef.class);
            assertNotNull(webServiceRef);
            assertTrue(webServiceRef instanceof NormalAnnotation);

            NormalAnnotation webServiceRefAnnotation = (NormalAnnotation) webServiceRef;
            assertTrue(webServiceRefAnnotation.values().size() == 1);

            MemberValuePair mappedNameValuePair = AnnotationUtils.getMemberValuePair(webServiceRefAnnotation, "mappedName");
            assertNotNull(mappedNameValuePair);
            StringLiteral mappedNameValue =  (StringLiteral) mappedNameValuePair.getValue();
            assertTrue(mappedNameValue.getLiteralValue().equals("myField"));

            AnnotationUtils.updateMemberValuePair(mappedNameValuePair, AnnotationsCore.createStringLiteral(ast, "anotherField"));
            webServiceRefAnnotation = (NormalAnnotation) AnnotationUtils.getAnnotation(field, WebServiceRef.class);
            mappedNameValuePair = AnnotationUtils.getMemberValuePair(webServiceRefAnnotation, "mappedName");
            mappedNameValue =  (StringLiteral) mappedNameValuePair.getValue();
            assertTrue(mappedNameValue.getLiteralValue().equals("anotherField"));

            //@WebMethod
            IMethod method = type.getMethod("add", new String[] { "I", "I" });
            assertTrue(AnnotationUtils.isAnnotationPresent(method, "WebMethod"));
            Annotation webMethod = AnnotationUtils.getAnnotation(method, WebMethod.class);
            assertNotNull(webMethod);
            assertTrue(webMethod instanceof NormalAnnotation);

            NormalAnnotation webMethodAnnotation = (NormalAnnotation) webMethod;
            assertTrue(webMethodAnnotation.values().size() == 1);

            MemberValuePair operationNameValuePair = AnnotationUtils.getMemberValuePair(webMethodAnnotation, "operationName");
            assertNotNull(operationNameValuePair);
            StringLiteral operationNameValue =  (StringLiteral) operationNameValuePair.getValue();
            assertTrue(operationNameValue.getLiteralValue().equals("add"));

            AnnotationUtils.updateMemberValuePair(operationNameValuePair, AnnotationsCore.createStringLiteral(ast, "multiply"));
            webMethodAnnotation = (NormalAnnotation) AnnotationUtils.getAnnotation(method, WebMethod.class);
            operationNameValuePair = AnnotationUtils.getMemberValuePair(webMethodAnnotation, "operationName");
            operationNameValue =  (StringLiteral) operationNameValuePair.getValue();
            assertTrue(operationNameValue.getLiteralValue().equals("multiply"));

            //@WebParam
            ILocalVariable localVariable = AnnotationUtils.getLocalVariable(method, "i");
            assertTrue(AnnotationUtils.isAnnotationPresent(localVariable, "WebParam"));
            Annotation webParam = AnnotationUtils.getAnnotation(localVariable, WebParam.class);
            assertNotNull(webParam);
            assertTrue(webParam instanceof NormalAnnotation);

            NormalAnnotation webParamAnnotation = (NormalAnnotation) webParam;
            assertTrue(webParamAnnotation.values().size() == 1);

            MemberValuePair partNameValuePair = AnnotationUtils.getMemberValuePair(webParamAnnotation, "partName");
            assertNotNull(partNameValuePair);
            StringLiteral partNameValue =  (StringLiteral) partNameValuePair.getValue();
            assertTrue(partNameValue.getLiteralValue().equals("i"));

            AnnotationUtils.updateMemberValuePair(partNameValuePair, AnnotationsCore.createStringLiteral(ast, "iii"));
            webParamAnnotation = (NormalAnnotation) AnnotationUtils.getAnnotation(localVariable, WebParam.class);
            partNameValuePair = AnnotationUtils.getMemberValuePair(webParamAnnotation, "partName");
            partNameValue =  (StringLiteral) partNameValuePair.getValue();
            assertTrue(partNameValue.getLiteralValue().equals("iii"));
        } catch (CoreException ce) {
            fail(ce.getLocalizedMessage());
        }
    }

}
