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

import javax.xml.ws.BindingType;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;

public class UpdateSingleMemberAnnotationTest extends AbstractAnnotationTest {

    @Override
    protected Annotation getAnnotation() {
        return null;
    }

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("import javax.xml.ws.BindingType;\n\n");
        classContents.append("@BindingType(\"http://schemas.xmlsoap.org/wsdl/soap/http\")\n");
        classContents.append("public class Calculator {\n\n}");
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

    public void testUpdateSingleMemberAnnotation() {
        try {
            IType type = source.findPrimaryType();
            assertTrue(AnnotationUtils.isAnnotationPresent(source, "BindingType"));
            Annotation bindingType = AnnotationUtils.getAnnotation(type, BindingType.class);
            assertNotNull(bindingType);
            assertTrue(bindingType instanceof SingleMemberAnnotation);

            SingleMemberAnnotation bindingTypeAnnotation = (SingleMemberAnnotation) bindingType;
            StringLiteral bindingTypeValue = (StringLiteral) bindingTypeAnnotation.getValue();
            assertTrue(bindingTypeValue.getLiteralValue().equals("http://schemas.xmlsoap.org/wsdl/soap/http"));

            AnnotationUtils.updateSingleMemberAnnotation(bindingTypeAnnotation, AnnotationsCore.createStringLiteral(ast, "http://www.w3.org/2003/05/soap/bindings/HTTP/"));
            bindingTypeAnnotation = (SingleMemberAnnotation) AnnotationUtils.getAnnotation(type, BindingType.class);
            bindingTypeValue = (StringLiteral) bindingTypeAnnotation.getValue();
            assertTrue(bindingTypeValue.getLiteralValue().equals("http://www.w3.org/2003/05/soap/bindings/HTTP/"));
        } catch (CoreException ce) {
            fail(ce.getLocalizedMessage());
        }
    }

}
