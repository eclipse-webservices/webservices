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
import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationAttributeInitializerAdapter;
import org.eclipse.jst.ws.internal.jaxws.core.annotations.AnnotationsCore;
import org.eclipse.jst.ws.internal.jaxws.core.utils.AnnotationUtils;

/**
 * 
 * @author sclarke
 *
 */
public class WebMethodAttributeInitializer extends AnnotationAttributeInitializerAdapter {
    private static final String OPERATION_NAME = "operationName";
    
    @Override
    public List<MemberValuePair> getMemberValuePairs(IMember member, AST ast,
            Class<? extends Annotation> annotationClass) {
        
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
        
        try {
            IType type = member.getCompilationUnit().findPrimaryType();
            IMethod method = (IMethod)member;
            MemberValuePair operationValuePair = AnnotationsCore.getStringMemberValuePair(ast, OPERATION_NAME,
                    method.getElementName() + AnnotationUtils.accountForOverloadedMethods(type, method));
    
            memberValuePairs.add(operationValuePair);
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return memberValuePairs;
    }

    @Override
    public List<MemberValuePair> getMemberValuePairs(ASTNode astNode, AST ast,
            Class<? extends Annotation> annotationClass) {
        return Collections.emptyList();
    }
}
