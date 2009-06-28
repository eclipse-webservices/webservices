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
package org.eclipse.jst.ws.jaxws.core.annotation.validation.tests;

import java.util.ArrayList;
import java.util.List;

import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;

/**
 * 
 * @author sclarke
 * 
 */
public abstract class AbstractDocumentBareValidationTest extends AbstractAnnotationValidationTest {

    @Override
    protected Annotation getAnnotation() {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
        
        MemberValuePair styleValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                SOAPBinding.class.getCanonicalName(), "style", Style.DOCUMENT);

        MemberValuePair useValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                SOAPBinding.class.getCanonicalName(), "use", Use.LITERAL);

        MemberValuePair parameterStyleValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                SOAPBinding.class.getCanonicalName(), "parameterStyle", ParameterStyle.BARE);

        memberValuePairs.add(styleValuePair);
        memberValuePairs.add(useValuePair);
        memberValuePairs.add(parameterStyleValuePair);

        return AnnotationsCore.createAnnotation(ast, javax.jws.soap.SOAPBinding.class,
                javax.jws.soap.SOAPBinding.class.getSimpleName(), memberValuePairs);

    }

    @Override
    protected String getClassContents() {
        StringBuilder classContents = new StringBuilder("package com.example;\n\n");
        classContents.append("public class MyClass {\n\n\t");
        classContents.append("public String oneIN(String inOne, String inTwo) {\n\t\treturn \"txt\";\n\t}");
        classContents.append("\n}");
        return classContents.toString();
    }

    @Override
    protected String getClassName() {
        return "MyClass.java";
    }

    @Override
    protected String getPackageName() {
        return "com.example";
    }

}
