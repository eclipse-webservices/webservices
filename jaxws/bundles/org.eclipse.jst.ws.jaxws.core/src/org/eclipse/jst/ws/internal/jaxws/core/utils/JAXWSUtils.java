/*******************************************************************************
 * Copyright (c) 2009, 2010 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.core.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;

import com.sun.mirror.declaration.AnnotationMirror;

public final class JAXWSUtils {

    public static final String ACTION = "action"; //$NON-NLS-1$
    public static final String ARG = "arg"; //$NON-NLS-1$
    public static final String CLASS_NAME = "className"; //$NON-NLS-1$
    public static final String DOT_CHARACTER = "."; //$NON-NLS-1$
    public static final String ENDPOINT_INTERFACE = "endpointInterface"; //$NON-NLS-1$
    public static final String EXCLUDE = "exclude"; //$NON-NLS-1$
    public static final String FAULT_BEAN = "faultBean"; //$NON-NLS-1$
    public static final String FINALIZE = "finalize"; //$NON-NLS-1$
    public static final String HEADER = "header"; //$NON-NLS-1$
    public static final String JAXWS_SUBPACKAGE = "jaxws"; //$NON-NLS-1$
    public static final String LOCAL_NAME = "localName"; //$NON-NLS-1$
    public static final String MODE = "mode"; //$NON-NLS-1$
    public static final String NAME = "name"; //$NON-NLS-1$    
    public static final String OPERATION_NAME = "operationName"; //$NON-NLS-1$
    public static final String PARAMETER_STYLE = "parameterStyle"; //$NON-NLS-1$
    public static final String PART_NAME = "partName"; //$NON-NLS-1$
    public static final String PORT_NAME = "portName"; //$NON-NLS-1$
    public static final String PORT_SUFFIX = "Port"; //$NON-NLS-1$
    public static final String RESPONSE = "Response"; //$NON-NLS-1$
    public static final String RESPONSE_SUFFIX = "Response"; //$NON-NLS-1$
    public static final String RETURN = "return"; //$NON-NLS-1$
    public static final String SERVICE_NAME = "serviceName"; //$NON-NLS-1$
    public static final String SERVICE_SUFFIX = "Service"; //$NON-NLS-1$
    public static final String STYLE = "style"; //$NON-NLS-1$
    public static final String TARGET_NAMESPACE = "targetNamespace"; //$NON-NLS-1$
    public static final String TRUE = "true"; //$NON-NLS-1$
    public static final String TYPE = "type"; //$NON-NLS-1$
    public static final String USE = "use"; //$NON-NLS-1$
    public static final String WSDL_LOCATION = "wsdlLocation"; //$NON-NLS-1$
    public static final String VALUE = "value"; //$NON-NLS-1$

    private JAXWSUtils() {
    }
    
    public static boolean isDocumentBare(AnnotationMirror mirror) {
        String style = AnnotationUtils.getStringValue(mirror, STYLE);
        String use = AnnotationUtils.getStringValue(mirror, USE);
        String parameterStyle = AnnotationUtils.getStringValue(mirror, PARAMETER_STYLE);

        return JAXWSUtils.isDocumentBare(style, use, parameterStyle);
    }

    public static boolean isDocumentBare(IAnnotation annotation) {
        try {
            String style = AnnotationUtils.getEnumValue(annotation, STYLE);
            String use = AnnotationUtils.getEnumValue(annotation, USE);
            String parameterStyle = AnnotationUtils.getEnumValue(annotation, PARAMETER_STYLE);
            return JAXWSUtils.isDocumentBare(style, use, parameterStyle);
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return false;
    }
    
    public static boolean isDocumentBare(org.eclipse.jdt.core.dom.Annotation annotation) {
        String style = AnnotationUtils.getEnumValue(annotation, STYLE);
        String use = AnnotationUtils.getEnumValue(annotation, USE);
        String parameterStyle = AnnotationUtils.getEnumValue(annotation, PARAMETER_STYLE);

        return JAXWSUtils.isDocumentBare(style, use, parameterStyle);
    }

    public static boolean isDocumentBare(String style, String use, String parameterStyle) {
        return (style == null || style.equals(Style.DOCUMENT.name()))
                && (use == null || use.equals(Use.LITERAL.name()))
                && (parameterStyle != null && parameterStyle.equals(ParameterStyle.BARE.name()));
    }

    public static boolean isDocumentWrapped(AnnotationMirror mirror) {
        String style = AnnotationUtils.getStringValue(mirror, STYLE);
        String use = AnnotationUtils.getStringValue(mirror, USE);
        String parameterStyle = AnnotationUtils.getStringValue(mirror, PARAMETER_STYLE);
        
        return JAXWSUtils.isDocumentWrapped(style, use, parameterStyle);
    }
    
    public static boolean isDocumentWrapped(IAnnotation annotation) {
        try {
            String style = AnnotationUtils.getEnumValue(annotation, STYLE);
            String use = AnnotationUtils.getEnumValue(annotation, USE);
            String parameterStyle = AnnotationUtils.getEnumValue(annotation, PARAMETER_STYLE);
            return JAXWSUtils.isDocumentWrapped(style, use, parameterStyle);
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }
        return true;
    }
    
    public static boolean isDocumentWrapped(org.eclipse.jdt.core.dom.Annotation annotation) {
        String style = AnnotationUtils.getEnumValue(annotation, STYLE);
        String use = AnnotationUtils.getEnumValue(annotation, USE);
        String parameterStyle = AnnotationUtils.getEnumValue(annotation, PARAMETER_STYLE);

        return JAXWSUtils.isDocumentWrapped(style, use, parameterStyle);
    }

    
    public static boolean isDocumentWrapped(String style, String use, String parameterStyle) {
        return (style == null || style.equals(Style.DOCUMENT.name()))
                && (use == null || use.equals(Use.LITERAL.name()))
                && (parameterStyle == null || parameterStyle.equals(ParameterStyle.WRAPPED.name()));
    }

    /**
     * Tests the given {@link IMethod} to see if it is an overloaded method. If it is it will return
     * a string representing the position of this method in it's parents {@link IType}.
     * <p>
     * E.g. Given three methods:
     * <pre>
     * public void myMethod(...);
     * public void myMethod(...);
     * public void myMethod(...);
     * </pre>
     * The first method would return a blank string, the second would return the string "1" and the third
     * would return the string "2".
     * </p>
     * @param method the method to test
     * @return a string value
     * @exception JavaModelException thrown if an exception occurs while accessing the underlying resource
     */
    public static String accountForOverloadedMethods(IMethod method) throws JavaModelException {
    	IType type = method.getDeclaringType();
    	if (type == null) {
    		return "";
    	}
        List<IMethod> methods =  Arrays.asList(type.getMethods());
        List<IMethod> similarMethods = new ArrayList<IMethod>();
        for (IMethod methodToTest : methods) {
            if (!method.equals(methodToTest) && method.getElementName().equalsIgnoreCase(
                    methodToTest.getElementName()) && methodToTest.getSourceRange().getOffset() <
                    method.getSourceRange().getOffset()) {
                similarMethods.add(methodToTest);
            }
        }
        return similarMethods.size() > 0 ? Integer.toString(similarMethods.size()) : ""; //$NON-NLS-1$
    }  
}
