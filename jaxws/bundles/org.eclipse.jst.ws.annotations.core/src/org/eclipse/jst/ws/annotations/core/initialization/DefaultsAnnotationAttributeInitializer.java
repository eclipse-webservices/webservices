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
package org.eclipse.jst.ws.annotations.core.initialization;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.AnnotationsCorePlugin;
import org.eclipse.jst.ws.internal.annotations.core.utils.SignatureUtils;

/**
 * Constructs {@link MemberValuePair} from the defaults found in the given {@link java.lang.annotation.Annotation}.
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under
 * development and expected to change significantly before reaching stability. It is being made available at
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 */
public class DefaultsAnnotationAttributeInitializer extends AnnotationAttributeInitializer {

    public DefaultsAnnotationAttributeInitializer() {
    }

    /**
     * Constructs a list of {@link MemberValuePair} using the declared method names and default values in
     * the given {@link java.lang.annotation.Annotation}.
     *  @see org.eclipse.jst.ws.annotations.core.initialization.IAnnotationAttributeInitializer#getMemberValuePairs(org.eclipse.jdt.core.IJavaElement, org.eclipse.jdt.core.dom.AST, java.lang.Class)
     */
    @Override
    @Deprecated
    public List<MemberValuePair> getMemberValuePairs(IJavaElement javaElement, AST ast, Class<? extends Annotation> annotationClass) {
        return getMemberValuePairs(ast, annotationClass);
    }

    @Override
    public List<MemberValuePair> getMemberValuePairs(IJavaElement javaElement, AST ast, IType type) {
        return interalGetMemberValuePairs(javaElement, ast, type);
    }

    private List<MemberValuePair> interalGetMemberValuePairs(IJavaElement javaElement, AST ast, IType type) {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        try {
            IMethod[] methods = type.getMethods();
            for (IMethod method : methods) {
                String name = method.getElementName();
                String returnType = method.getReturnType();
                IMemberValuePair defaultValue = method.getDefaultValue();
                if (defaultValue != null) {
                    if (SignatureUtils.isString(returnType)) {
                        memberValuePairs.add(AnnotationsCore.createStringMemberValuePair(ast,
                                name, defaultValue.getValue().toString()));
                    }

                    if (SignatureUtils.isBoolean(returnType)) {
                        memberValuePairs.add(AnnotationsCore.createBooleanMemberValuePair(ast,
                                name, Boolean.parseBoolean(defaultValue.getValue().toString())));
                    }

                    int signatureKind = Signature.getTypeSignatureKind(returnType);
                    if (signatureKind == Signature.BASE_TYPE_SIGNATURE) {
                        if (returnType.charAt(0) == Signature.C_BYTE
                                || returnType.charAt(0) == Signature.C_SHORT
                                || returnType.charAt(0) == Signature.C_INT
                                || returnType.charAt(0) == Signature.C_LONG
                                || returnType.charAt(0) == Signature.C_FLOAT
                                || returnType.charAt(0) == Signature.C_DOUBLE) {
                            memberValuePairs.add(AnnotationsCore.createNumberMemberValuePair(ast, name, defaultValue.getValue().toString()));
                        }
                    }

                    if (SignatureUtils.isArray(returnType)) {
                        memberValuePairs.add(AnnotationsCore.createArrayMemberValuePair(ast, method.getElementName(),
                                (Object[]) defaultValue.getValue()));
                    }

                    if (SignatureUtils.isEnum(method)) {
                        if (defaultValue.getValueKind() == IMemberValuePair.K_QUALIFIED_NAME) {
                            String value = defaultValue.getValue().toString();
                            String enumName = value.substring(0, value.lastIndexOf("."));
                            String constant = value.substring(value.lastIndexOf(".") + 1, value.length());
                            IType enumType = javaElement.getJavaProject().findType(enumName);
                            if (enumType != null && enumType.isEnum()) {
                                IField[] fields = enumType.getFields();
                                for (IField field : fields) {
                                    if (field.isEnumConstant() && field.getElementName().equals(constant)) {
                                        memberValuePairs.add(AnnotationsCore.createEnumMemberValuePair(ast,
                                                method.getDeclaringType().getFullyQualifiedName(), name, field));
                                    }
                                }
                            }
                        }
                    }

                    if (SignatureUtils.isClass(returnType)) {
                        memberValuePairs.add(AnnotationsCore.createTypeMemberValuePair(ast, name,
                                defaultValue.getValue()));
                    }
                }
            }
        } catch (JavaModelException jme) {
            AnnotationsCorePlugin.log(jme.getStatus());
        }

        return memberValuePairs;
    }

    private List<MemberValuePair> getMemberValuePairs(AST ast, Class<? extends Annotation> annotationClass) {
        List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

        Method[] declaredMethods = annotationClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            String name = method.getName();
            Class<?> returnType = method.getReturnType();
            Object defaultValue = method.getDefaultValue();

            if (defaultValue != null) {
                if (returnType.equals(String.class)) {
                    memberValuePairs.add(AnnotationsCore.createStringMemberValuePair(ast,
                            name, defaultValue.toString()));
                }

                if (returnType.equals(Boolean.TYPE)) {
                    memberValuePairs.add(AnnotationsCore.createBooleanMemberValuePair(ast,
                            name, Boolean.parseBoolean(defaultValue.toString())));
                }

                if (returnType.isPrimitive() && (returnType.equals(Byte.TYPE) || returnType.equals(Short.TYPE)
                        || returnType.equals(Integer.TYPE) || returnType.equals(Long.TYPE)
                        || returnType.equals(Float.TYPE) || returnType.equals(Double.TYPE))) {
                    memberValuePairs.add(AnnotationsCore.createNumberMemberValuePair(ast, name, defaultValue.toString()));
                }

                if (returnType.isArray()) {
                    memberValuePairs.add(AnnotationsCore.createArrayMemberValuePair(ast, method.getName(),
                            (Object[]) defaultValue));
                }

                if (returnType.isEnum()) {
                    memberValuePairs.add(AnnotationsCore.createEnumMemberValuePair(ast,
                            method.getDeclaringClass().getCanonicalName(), name, defaultValue));
                }

                if (returnType.equals(Class.class)) {
                    memberValuePairs.add(AnnotationsCore.createTypeMemberValuePair(ast, name,
                            defaultValue));
                }
            }
        }
        return memberValuePairs;
    }
}
