/*******************************************************************************
 * Copyright (c) 2011 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.annotations.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

public final class SignatureUtils {

    private SignatureUtils() {
    }

    public static boolean isString(String returnType) {
        int signatureKind = Signature.getTypeSignatureKind(returnType);
        if (signatureKind == Signature.CLASS_TYPE_SIGNATURE) {
            if (returnType.charAt(0) == Signature.C_RESOLVED) {
                return Signature.toString(returnType).equals(java.lang.String.class.getCanonicalName());
            }
            if (returnType.charAt(0) == Signature.C_UNRESOLVED) {
                return Signature.toString(returnType).equals(java.lang.String.class.getSimpleName());
            }
        }
        return false;
    }

    public static boolean isClass(String returnType) {
        int signatureKind = Signature.getTypeSignatureKind(returnType);
        if (signatureKind == Signature.CLASS_TYPE_SIGNATURE) {
            if (returnType.charAt(0) == Signature.C_RESOLVED) {
                returnType = Signature.getTypeErasure(returnType);
                return Signature.toString(returnType).equals(java.lang.Class.class.getCanonicalName());
            }
            if (returnType.charAt(0) == Signature.C_UNRESOLVED) {
                returnType = Signature.getTypeErasure(returnType);
                return Signature.toString(returnType).equals(java.lang.Class.class.getSimpleName());
            }
        }
        return false;
    }

    public static boolean isBoolean(String returnType) {
        int signatureKind = Signature.getTypeSignatureKind(returnType);
        if (signatureKind == Signature.CLASS_TYPE_SIGNATURE) {
            if (returnType.charAt(0) == Signature.C_RESOLVED) {
                return Signature.toString(returnType).equals(java.lang.Boolean.class.getCanonicalName());
            }
            if (returnType.charAt(0) == Signature.C_UNRESOLVED) {
                return Signature.toString(returnType).equals(java.lang.Boolean.class.getSimpleName());
            }
        }
        if (signatureKind == Signature.BASE_TYPE_SIGNATURE) {
            return returnType.charAt(0) == Signature.C_BOOLEAN;
        }
        return false;
    }

    public static boolean isArray(String returnType) {
        return Signature.getTypeSignatureKind(returnType) == Signature.ARRAY_TYPE_SIGNATURE;
    }

    public static boolean isPrimitive(String returnType) {
        return Signature.getTypeSignatureKind(returnType) == Signature.BASE_TYPE_SIGNATURE;
    }

    public static boolean isEnum(IMethod method) throws JavaModelException {
        return getEnumReturnType(method) != null;
    }

    public static IType getEnumReturnType(IMethod method) throws JavaModelException {
        String returnType = method.getReturnType();
        int signatureKind = Signature.getTypeSignatureKind(returnType);
        if (signatureKind == Signature.CLASS_TYPE_SIGNATURE) {
            if (returnType.charAt(0) == Signature.C_RESOLVED) {
                IType type = method.getJavaProject().findType(Signature.toString(returnType));
                if (type != null && type.isEnum()) {
                    return type;
                }
            }
        }
        return null;
    }

    public static String[] getEnumConstantsNames(IType enumType) throws JavaModelException {
        if (enumType.isEnum()) {
            List<String> enumConstants = new ArrayList<String>();
            IField[] fields = getEnumConstants(enumType);
            for (IField field : fields) {
                enumConstants.add(field.getElementName());
            }
            return enumConstants.toArray(new String[enumConstants.size()]);
        }
        return new String[] {};
    }

    public static IField[] getEnumConstants(IType enumType) throws JavaModelException {
        if (enumType.isEnum()) {
            List<IField> enumConstants = new ArrayList<IField>();
            IField[] fields = enumType.getFields();
            for (IField field : fields) {
                if (field.isEnumConstant()) {
                    enumConstants.add(field);
                }
            }
            return enumConstants.toArray(new IField[enumConstants.size()]);
        }
        return new IField[] {};
    }


}
