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
package org.eclipse.jst.ws.internal.cxf.core.annotations;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.StringLiteral;

/**
 * 
 * @author sclarke
 */
public final class JAXWSAnnotations {

    private JAXWSAnnotations() {
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
        annotation.setTypeName(annotationTypeName);

        for (MemberValuePair memberValuePair : memberValuePairs) {
            List<MemberValuePair> annotationValues = annotation.values();
            annotationValues.add(memberValuePair);
        }

        return annotation;
    }

    public static MemberValuePair getNameValuePair(CompilationUnit compilationUnit, String name) {
        return JAXWSAnnotations.getStringMemberValuePair(compilationUnit, "name", name); //$NON-NLS-1$
    }

    public static MemberValuePair getEndpointInterfaceValuePair(CompilationUnit compilationUnit,
            String endpointInterface) {
        return JAXWSAnnotations.getStringMemberValuePair(compilationUnit, "endpointInterface", //$NON-NLS-1$
                endpointInterface);
    }

    public static MemberValuePair getTargetNamespaceValuePair(CompilationUnit compilationUnit,
            String targetNamespace) {
        return JAXWSAnnotations.getStringMemberValuePair(compilationUnit, "targetNamespace", targetNamespace); //$NON-NLS-1$
    }

    public static MemberValuePair getPortNameValuePair(CompilationUnit compilationUnit, String portName) {
        return JAXWSAnnotations.getStringMemberValuePair(compilationUnit, "portName", portName); //$NON-NLS-1$
    }

    public static MemberValuePair getServiceNameValuePair(CompilationUnit compilationUnit, 
            String serviceName) {
        return JAXWSAnnotations.getStringMemberValuePair(compilationUnit, "serviceName", serviceName); //$NON-NLS-1$
    }

    public static MemberValuePair getOperationNameValuePair(CompilationUnit compilationUnit,
            String operationName) {
        return JAXWSAnnotations.getStringMemberValuePair(compilationUnit, "operationName", operationName); //$NON-NLS-1$
    }

    public static MemberValuePair getExcludeValuePair(CompilationUnit compilationUnit, boolean exclude) {
        return JAXWSAnnotations.getBooleanMemberValuePair(compilationUnit, "exclude", exclude); //$NON-NLS-1$
    }

    public static MemberValuePair getPartNameValuePair(CompilationUnit compilationUnit, String partName) {
        return JAXWSAnnotations.getStringMemberValuePair(compilationUnit, "partName", partName); //$NON-NLS-1$
    }

    public static MemberValuePair getClassNameValuePair(CompilationUnit compilationUnit, String className) {
        return JAXWSAnnotations.getStringMemberValuePair(compilationUnit, "className", className); //$NON-NLS-1$
    }

    public static MemberValuePair getLocalNameValuePair(CompilationUnit compilationUnit, String localName) {
        return JAXWSAnnotations.getStringMemberValuePair(compilationUnit, "localName", localName); //$NON-NLS-1$
    }

    public static MemberValuePair getFaultBeanValuePair(CompilationUnit compilationUnit, String faultBean) {
        return JAXWSAnnotations.getStringMemberValuePair(compilationUnit, "faultBean", faultBean); //$NON-NLS-1$
    }

    public static MemberValuePair getWSDLLocationValuePair(CompilationUnit compilationUnit,
            String wsdlLocation) {
        return JAXWSAnnotations.getStringMemberValuePair(compilationUnit, "wsdlLocation", wsdlLocation); //$NON-NLS-1$
    }

    public static MemberValuePair getStringMemberValuePair(CompilationUnit compilationUnit, String name,
            String value) {
        AST ast = compilationUnit.getAST();

        MemberValuePair stringMemberValuePair = JAXWSAnnotations.createMemberValuePair(ast, name,
                JAXWSAnnotations.getStringLiteral(ast, value));

        return stringMemberValuePair;
    }

    public static MemberValuePair getBooleanMemberValuePair(CompilationUnit compilationUnit, String name,
            boolean value) {
        AST ast = compilationUnit.getAST();

        MemberValuePair excludeValuePair = JAXWSAnnotations.createMemberValuePair(ast, name, JAXWSAnnotations
                .getBooleanLiteral(ast, value));

        return excludeValuePair;
    }

    private static StringLiteral getStringLiteral(AST ast, String literalValue) {
        StringLiteral stringLiteral = ast.newStringLiteral();
        stringLiteral.setLiteralValue(literalValue);
        return stringLiteral;
    }

    private static BooleanLiteral getBooleanLiteral(AST ast, boolean value) {
        BooleanLiteral booleanLiteral = ast.newBooleanLiteral(value);
        return booleanLiteral;
    }

}
