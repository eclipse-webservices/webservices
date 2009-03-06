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
package org.eclipse.jst.ws.internal.jaxws.core.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MemberValuePair;

/**
 * 
 * @author sclarke
 *
 */
public class AnnotationAttributeInitializerAdapter implements IAnnotationAttributeInitializer {

    public AnnotationAttributeInitializerAdapter() {    
    }
    
    public List<MemberValuePair> getMemberValuePairs(IMember member, AST ast,
            Class<? extends Annotation> annotationClass) {
        return getMemberValuePairs(ast, annotationClass);
    }

    public List<MemberValuePair> getMemberValuePairs(ASTNode astNode, AST ast,
            Class<? extends Annotation> annotationClass) {
        return getMemberValuePairs(ast, annotationClass);
    }
    
    private List<MemberValuePair> getMemberValuePairs(AST ast,
            Class<? extends Annotation> annotationClass) {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();
        
        Method[] declaredMethods = annotationClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String name = method.getName();
            Class<?> returnType = method.getReturnType();
            Object defaultValue = method.getDefaultValue();

            if (defaultValue != null) {
                if (returnType.equals(String.class)) {
                    memberValuePairs.add(AnnotationsCore.getStringMemberValuePair(ast,
                            name, defaultValue.toString()));
                }
                
                if (returnType.equals(Boolean.TYPE)) {
                    memberValuePairs.add(AnnotationsCore.getBooleanMemberValuePair(ast,
                            name, Boolean.parseBoolean(defaultValue.toString())));
                }
                
                if (returnType.isPrimitive() && (returnType.equals(Byte.TYPE) || returnType.equals(Short.TYPE) 
                        || returnType.equals(Integer.TYPE) || returnType.equals(Long.TYPE)
                        || returnType.equals(Float.TYPE) || returnType.equals(Double.TYPE))) {
                    memberValuePairs.add(AnnotationsCore.getNumberMemberValuePair(ast, name, defaultValue));
                }
                
                if (returnType.isArray()) {
                    memberValuePairs.add(AnnotationsCore.getArrayMemberValuePair(ast, method, 
                            (Object[])defaultValue));
                }
                
                if (returnType.isEnum()) {
                    memberValuePairs.add(AnnotationsCore.getEnumMemberValuePair(ast, 
                            method.getDeclaringClass().getCanonicalName(), name, defaultValue));
                }
                
                if (returnType.equals(Class.class)) {
                    memberValuePairs.add(AnnotationsCore.getTypeMemberVaulePair(ast, name, 
                            defaultValue));
                }
            }
        }      
        return memberValuePairs;
    }
}
