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
package org.eclipse.jst.ws.internal.jaxws.core.annotations.initialization;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.initialization.AnnotationAttributeInitializer;

/**
 * 
 * @author sclarke
 *
 */
public class SOAPBindingAttributeInitializer extends AnnotationAttributeInitializer {
    private static final String STYLE = "style";
    private static final String USE = "use";
    private static final String PARAMETER_STYLE = "parameterStyle";

    public SOAPBindingAttributeInitializer() {
    }
    
    @Override
    public List<MemberValuePair> getMemberValuePairs(IJavaElement javaElement, AST ast,
            Class<? extends Annotation> annotationClass) {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        MemberValuePair styleValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                "javax.jws.soap.SOAPBinding", STYLE, Style.DOCUMENT);

        MemberValuePair useValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                "javax.jws.soap.SOAPBinding", USE, Use.LITERAL);

        MemberValuePair parameterStyleValuePair = AnnotationsCore.createEnumMemberValuePair(ast,
                "javax.jws.soap.SOAPBinding", PARAMETER_STYLE, ParameterStyle.WRAPPED);
        
        memberValuePairs.add(styleValuePair);
        memberValuePairs.add(useValuePair);
        memberValuePairs.add(parameterStyleValuePair);
        return memberValuePairs;
    }
}
