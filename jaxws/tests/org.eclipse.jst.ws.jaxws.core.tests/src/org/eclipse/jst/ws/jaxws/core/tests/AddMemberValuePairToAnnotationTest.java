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
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;

public class AddMemberValuePairToAnnotationTest extends AbstractAnnotationTest {

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
        classContents.append("@WebService()\n");
        classContents.append("public class Calculator {\n\n");
        classContents.append("\t@WebServiceRef()\n");
        classContents.append("\tpublic String myField;\n\n");
        classContents.append("\t@WebMethod()\n");
        classContents.append("\tpublic int add(@WebParam() int i, int k) {");
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
	
	public void testAddMemberValuePairToAnnotation() {
        try {
            //@WebService
            IType type = source.findPrimaryType();
            assertTrue(AnnotationUtils.isAnnotationPresent(source, "WebService"));
            Annotation webService = AnnotationUtils.getAnnotation(type, WebService.class);
            assertNotNull(webService);
            assertTrue(webService instanceof NormalAnnotation);

            NormalAnnotation webServiceAnnotation = (NormalAnnotation) webService;
            assertTrue(webServiceAnnotation.values().size() == 0);
                    
            MemberValuePair nameValuePair = AnnotationsCore.createStringMemberValuePair(ast, "name", "Calculator");
                                    
            AnnotationUtils.addMemberValuePair(webServiceAnnotation, nameValuePair);
            webServiceAnnotation = (NormalAnnotation) AnnotationUtils.getAnnotation(type, WebService.class);
            assertTrue(webServiceAnnotation.values().size() == 1);

            //@WebServiceRef
            IField field = type.getField("myField");
            assertTrue(AnnotationUtils.isAnnotationPresent(field, "WebServiceRef"));
            Annotation webServiceRef = AnnotationUtils.getAnnotation(field, WebServiceRef.class);
            assertNotNull(webServiceRef);
            assertTrue(webServiceRef instanceof NormalAnnotation);

            NormalAnnotation webServiceRefAnnotation = (NormalAnnotation) webServiceRef;
            assertTrue(webServiceRefAnnotation.values().size() == 0);
                    
            MemberValuePair mappedNameValuePair = AnnotationsCore.createStringMemberValuePair(ast, "mappedName", "myField");
                                    
            AnnotationUtils.addMemberValuePair(webServiceRefAnnotation, mappedNameValuePair);
            webServiceRefAnnotation = (NormalAnnotation) AnnotationUtils.getAnnotation(field, WebServiceRef.class);
            assertTrue(webServiceRefAnnotation.values().size() == 1);

            //@WebMethod
            IMethod method = type.getMethod("add", new String[] { "I", "I" });
            assertTrue(AnnotationUtils.isAnnotationPresent(method, "WebMethod"));
            Annotation webMethod = AnnotationUtils.getAnnotation(method, WebMethod.class);
            assertNotNull(webMethod);
            assertTrue(webMethod instanceof NormalAnnotation);

            NormalAnnotation webMethodAnnotation = (NormalAnnotation) webMethod;
            assertTrue(webMethodAnnotation.values().size() == 0);
                    
            MemberValuePair operationNameValuePair = AnnotationsCore.createStringMemberValuePair(ast, "operationName", "add");
                                    
            AnnotationUtils.addMemberValuePair(webMethodAnnotation, operationNameValuePair);
            webMethodAnnotation = (NormalAnnotation) AnnotationUtils.getAnnotation(method, WebMethod.class);
            assertTrue(webMethodAnnotation.values().size() == 1);

            //@WebParam
            ILocalVariable localVariable = AnnotationUtils.getLocalVariable(method, "i");
            assertTrue(AnnotationUtils.isAnnotationPresent(localVariable, "WebParam"));
            Annotation webParam = AnnotationUtils.getAnnotation(localVariable, WebParam.class);
            assertNotNull(webParam);
            assertTrue(webParam instanceof NormalAnnotation);

            NormalAnnotation webParamAnnotation = (NormalAnnotation) webParam;
            assertTrue(webParamAnnotation.values().size() == 0);
                    
            MemberValuePair partNameValuePair = AnnotationsCore.createStringMemberValuePair(ast, "partName", "i");
                                    
            AnnotationUtils.addMemberValuePair(webParamAnnotation, partNameValuePair);
            webParamAnnotation = (NormalAnnotation) AnnotationUtils.getAnnotation(localVariable, WebParam.class);
            assertTrue(webParamAnnotation.values().size() == 1);
        } catch (CoreException ce) {
            fail(ce.getLocalizedMessage());
        }
	}
}
