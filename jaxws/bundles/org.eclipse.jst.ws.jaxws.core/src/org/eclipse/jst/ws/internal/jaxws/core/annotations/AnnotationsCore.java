/*******************************************************************************
 * Copyright (c) 2008 IONA Technologies PLC
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IONA Technologies PLC - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.core.annotations;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;
/**
 * 
 * @author sclarke
 */
public final class AnnotationsCore {

    private AnnotationsCore() {
    }
    
    public static MemberValuePair createMemberValuePair(AST ast, String name, Expression expression) {
        MemberValuePair memberValuePair = ast.newMemberValuePair();
        memberValuePair.setName(ast.newSimpleName(name));
        memberValuePair.setValue(expression);
        return memberValuePair;
    }

    @SuppressWarnings("unchecked")
    public static Annotation getAnnotation(AST ast,
            Class<? extends java.lang.annotation.Annotation> annotationClass,
            List<MemberValuePair> memberValuePairs) {

        NormalAnnotation annotation = ast.newNormalAnnotation();
        Name annotationTypeName = ast.newName(annotationClass.getSimpleName());
        //Name annotationTypeName = ast.newName(annotationClass.getCanonicalName());
        annotation.setTypeName(annotationTypeName);

        if (memberValuePairs != null) {
            for (MemberValuePair memberValuePair : memberValuePairs) {
                List<MemberValuePair> annotationValues = annotation.values();
                annotationValues.add(memberValuePair);
            }
        }
        return annotation;
    }

    public static MemberValuePair getNameValuePair(AST ast, String name) {
        return AnnotationsCore.getStringMemberValuePair(ast, "name", name); //$NON-NLS-1$
    }

    public static MemberValuePair getEndpointInterfaceValuePair(AST ast, String endpointInterface) {
        return AnnotationsCore.getStringMemberValuePair(ast, "endpointInterface", //$NON-NLS-1$
                endpointInterface);
    }

//    public static MemberValuePair getTargetNamespaceValuePair(AST ast, String targetNamespace) {
//        return AnnotationsCore.getStringMemberValuePair(ast, "targetNamespace", targetNamespace); //$NON-NLS-1$
//    }

    public static MemberValuePair getPortNameValuePair(AST ast, String portName) {
        return AnnotationsCore.getStringMemberValuePair(ast, "portName", portName); //$NON-NLS-1$
    }

    public static MemberValuePair getServiceNameValuePair(AST ast, String serviceName) {
        return AnnotationsCore.getStringMemberValuePair(ast, "serviceName", serviceName); //$NON-NLS-1$
    }

//    public static MemberValuePair getOperationNameValuePair(AST ast, String operationName) {
//        return AnnotationsCore.getStringMemberValuePair(ast, "operationName", operationName); //$NON-NLS-1$
//    }

    public static MemberValuePair getExcludeValuePair(AST ast, boolean exclude) {
        return AnnotationsCore.getBooleanMemberValuePair(ast, "exclude", exclude); //$NON-NLS-1$
    }

    public static MemberValuePair getPartNameValuePair(AST ast, String partName) {
        return AnnotationsCore.getStringMemberValuePair(ast, "partName", partName); //$NON-NLS-1$
    }

//    public static MemberValuePair getClassNameValuePair(AST ast, String className) {
//        return AnnotationsCore.getStringMemberValuePair(ast, "className", className); //$NON-NLS-1$
//    }

//    public static MemberValuePair getLocalNameValuePair(AST ast, String localName) {
//        return AnnotationsCore.getStringMemberValuePair(ast, "localName", localName); //$NON-NLS-1$
//    }

    public static MemberValuePair getFaultBeanValuePair(AST ast, String faultBean) {
        return AnnotationsCore.getStringMemberValuePair(ast, "faultBean", faultBean); //$NON-NLS-1$
    }

    public static MemberValuePair getWSDLLocationValuePair(AST ast, String wsdlLocation) {
        return AnnotationsCore.getStringMemberValuePair(ast, "wsdlLocation", wsdlLocation); //$NON-NLS-1$
    }

    public static MemberValuePair getStringMemberValuePair(AST ast, String name, Object value) {
        MemberValuePair stringMemberValuePair = AnnotationsCore.createMemberValuePair(ast, name,
                AnnotationsCore.getStringLiteral(ast, value.toString()));

        return stringMemberValuePair;
    }

    public static MemberValuePair getBooleanMemberValuePair(AST ast, String name, Object value) {
        MemberValuePair booleanValuePair = AnnotationsCore.createMemberValuePair(ast, name, AnnotationsCore
                .getBooleanLiteral(ast, ((Boolean)value).booleanValue()));

        return booleanValuePair;
    }
    
    public static MemberValuePair getNumberMemberValuePair(AST ast, String name, Object value) {
        MemberValuePair primitiveValuePair = AnnotationsCore.createMemberValuePair(ast, name, 
                AnnotationsCore.getNumberLiteral(ast, value.toString()));
        return primitiveValuePair;
    }
    
    public static MemberValuePair getEnumMemberValuePair(AST ast, String className, String name, Object value) {
         return AnnotationsCore.createMemberValuePair(ast, name, getEnumLiteral(ast, className, value));        
    }
    
    public static MemberValuePair getTypeMemberVaulePair(AST ast, String name, Object value) {
        return AnnotationsCore.createMemberValuePair(ast, name,
                getTypeLiteral(ast, value));
    }
    
    public static MemberValuePair getArrayMemberValuePair(AST ast, Method method, Object[] values) {
        return AnnotationsCore.createMemberValuePair(ast, method.getName(), getArrayValueLiteral(ast, method, values));
     }
    
    @SuppressWarnings("unchecked")
    public static ArrayInitializer getArrayValueLiteral(AST ast, Method method, Object[] values) {
        ArrayInitializer arrayInitializer = getArrayInitializer(ast);
        for (Object value : values) {
            if (value instanceof java.lang.annotation.Annotation) {
                //TODO Handle this situation. Arises when annotations are specified as defaults in array initializers
            }
            if (value instanceof List) {
                Class<? extends java.lang.annotation.Annotation> annotationClass = 
                    (Class<? extends java.lang.annotation.Annotation>) method.getReturnType().getComponentType();

                List<MemberValuePair> memberValuePairs = new ArrayList<MemberValuePair>();

                List<Map<String, Object>> valuesList = (List<Map<String, Object>>) value;
                Iterator<Map<String, Object>> valuesIterator = valuesList.iterator();
                while (valuesIterator.hasNext()) {
                    Map<String, Object> annotationMap = (Map<String, Object>) valuesIterator.next();
                    Set<Entry<String, Object>> entrySet = annotationMap.entrySet();
                    Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<java.lang.String, Object> entry = iterator.next();
                        String memberName = entry.getKey();
                        try {
                            Method annotationMethod = annotationClass.getMethod(memberName, null);
                            if (annotationMethod != null) {
                                Object memberValue = entry.getValue();
                                Class<?> returnType = annotationMethod.getReturnType();
                                if (returnType.equals(String.class)) {
                                    memberValuePairs.add(getStringMemberValuePair(ast, memberName, memberValue));
                                }
                                if (returnType.equals(Boolean.TYPE)) {
                                    memberValuePairs.add(getBooleanMemberValuePair(ast, memberName, memberValue));                                    
                                }
                                if (returnType.equals(Class.class)) {
                                    String className = memberValue.toString();
                                    if (className.endsWith(".class")) {
                                        className = className.substring(0, className.lastIndexOf("."));
                                    }
                                    memberValuePairs.add(AnnotationsCore.createMemberValuePair(ast, memberName,
                                            getTypeLiteral(ast, className)));                                    
                                }
//                                if (returnType.isPrimitive()) {
//                                    memberValuePairs.add(getNumberMemberValuePair(ast, memberName, memberValue));
//                                }
                            }
                            
                        } catch (SecurityException se) {
                            JAXWSCorePlugin.log(se);
                        } catch (NoSuchMethodException nsme) {
                            JAXWSCorePlugin.log(nsme);
                        }
                    }
                }
                arrayInitializer.expressions().add(getAnnotation(ast, annotationClass, memberValuePairs));
            }
            if (value.equals(Class.class)) {
                arrayInitializer.expressions().add(getTypeLiteral(ast, value.toString()));
            }
            if (value instanceof String) {
                String stringValue = value.toString();
                if (stringValue.endsWith(".class")) {
                    arrayInitializer.expressions().add(getTypeLiteral(ast, stringValue.substring(0,
                            stringValue.lastIndexOf("."))));
                } else {
                    arrayInitializer.expressions().add(getStringLiteral(ast, stringValue));
                }
            }
        }
        return arrayInitializer;
    }
    
    public static Name getEnumLiteral(AST ast, String className, Object value) {
        QualifiedName enumName = null;
        SimpleName enumClassName = ast.newSimpleName(value.getClass().getSimpleName());
        SimpleName enumLiteral = ast.newSimpleName(value.toString());
        if (value.getClass().isMemberClass()) {
            Name enumEnclosingClassName = null;
            String enclosingClassName = value.getClass().getEnclosingClass().getCanonicalName();
            if (enclosingClassName.equals(className)) {
                enumEnclosingClassName = ast.newSimpleName(value.getClass().getEnclosingClass()
                        .getSimpleName());
            } else {
                enumEnclosingClassName = ast.newName(enclosingClassName);
            }
            QualifiedName qualifiedName = ast.newQualifiedName(enumEnclosingClassName, enumClassName);
            enumName = ast.newQualifiedName(qualifiedName, enumLiteral);
        } else {
            Name qualifiedName = ast.newName(value.getClass().getCanonicalName());
            enumName = ast.newQualifiedName(qualifiedName, enumLiteral);
        }
        return enumName;
    }

    public static TypeLiteral getTypeLiteral(AST ast, Object value) {
        TypeLiteral typeLiteral = null;
        if (value instanceof Class) {
            typeLiteral = getTypeLiteral(ast, (Class<?>) value);
        }
        if (value instanceof String) {
            typeLiteral = getTypeLiteral(ast, (String) value);
        }
        return typeLiteral;
    }

    public static TypeLiteral getTypeLiteral(AST ast, Class<?> value) {
        TypeLiteral typeLiteral = ast.newTypeLiteral();

        Class<?> aClass = (Class<?>)value;
        SimpleName className = ast.newSimpleName(aClass.getSimpleName());

        if (aClass.isMemberClass()) {
            Name enclosingClassName = ast.newName(aClass.getEnclosingClass().getCanonicalName());
            QualifiedType qualifiedType = ast.newQualifiedType(ast.newSimpleType(enclosingClassName), className);
            typeLiteral.setType(qualifiedType);
            return typeLiteral;
        }
        return getTypeLiteral(ast, value.getCanonicalName());
    }

    public static TypeLiteral getTypeLiteral(AST ast, String value) {
        TypeLiteral typeLiteral = ast.newTypeLiteral();

        if (value.toString().indexOf(".") == -1) { //$NON-NLS-1$
            typeLiteral.setType(ast.newSimpleType(ast.newSimpleName(value.toString())));
        } else {
            String qualifier = value.toString().substring(0, value.toString().lastIndexOf(".")); //$NON-NLS-1$
            String identifier = value.toString().substring(value.toString().lastIndexOf(".") + 1); //$NON-NLS-1$
            if (qualifier.equals("java.lang")) { //$NON-NLS-1$
                typeLiteral.setType(ast.newSimpleType(ast.newSimpleName(identifier)));
            } else {
                typeLiteral.setType(ast.newQualifiedType(ast.newSimpleType(ast.newName(qualifier)), ast
                        .newSimpleName(identifier)));
            }
        }
        return typeLiteral;
    }

    public static StringLiteral getStringLiteral(AST ast, String literalValue) {
        StringLiteral stringLiteral = ast.newStringLiteral();
        stringLiteral.setLiteralValue(literalValue);
        return stringLiteral;
    }

    public static BooleanLiteral getBooleanLiteral(AST ast, boolean value) {
        BooleanLiteral booleanLiteral = ast.newBooleanLiteral(value);
        return booleanLiteral;
    }
    
    public static NumberLiteral getNumberLiteral(AST ast, String value) {
        NumberLiteral primitiveLiteral = ast.newNumberLiteral();
        primitiveLiteral.setToken(value);
        return primitiveLiteral;
    }

    public static ArrayInitializer getArrayInitializer(AST ast) {
        ArrayInitializer arrayInitializer = ast.newArrayInitializer();
        return arrayInitializer;
    }
    
}
