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
package org.eclipse.jst.ws.internal.jaxws.ui.views;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.annotations.core.utils.SignatureUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;
import org.eclipse.swt.graphics.Image;

public class AnnotationsValuesColumnLabelProvider extends ColumnLabelProvider {
    private TreeViewer annotationTreeViewer;
    private Image true_image;
    private Image false_image;

    public AnnotationsValuesColumnLabelProvider(TreeViewer annotationTreeViewer) {
        this.annotationTreeViewer = annotationTreeViewer;
        true_image = JAXWSUIPlugin.imageDescriptorFromPlugin(JAXWSUIPlugin.PLUGIN_ID, "icons/obj16/true.gif") //$NON-NLS-1$
        .createImage();
        false_image = JAXWSUIPlugin.imageDescriptorFromPlugin(JAXWSUIPlugin.PLUGIN_ID, "icons/obj16/false.gif") //$NON-NLS-1$
        .createImage();
    }

    @Override
    public String getText(Object element) {
        if (element instanceof IMethod) {
            try {
                return getTextForMethod((IMethod) element);
            } catch (JavaModelException jme) {
                JAXWSUIPlugin.log(jme.getStatus());
            }
        }
        return ""; //$NON-NLS-1$
    }

    private String getTextForMethod(IMethod method) throws JavaModelException {
        if (annotationTreeViewer.getInput() instanceof IJavaElement) {
            IJavaElement javaElement = (IJavaElement) annotationTreeViewer.getInput();
            if (javaElement.exists()) {
                return getTextForMethod(method, javaElement);
            }
        }
        return ""; //$NON-NLS-1$
    }

    private String getTextForMethod(IMethod method, IJavaElement annotatedElement) throws JavaModelException {
        List<Annotation> annotations = AnnotationUtils.getAnnotations(annotatedElement);
        for (Annotation annotation : annotations) {
            String annotationName = AnnotationUtils.getAnnotationName(annotation);
            IType type = method.getDeclaringType();
            if (type != null && annotationName.equals(type.getElementName())
                    || annotationName.equals(type.getFullyQualifiedName())) {
                if (annotation.isNormalAnnotation()) {
                    NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;
                    @SuppressWarnings("unchecked")
                    List<MemberValuePair> memberValuePairs = normalAnnotation.values();
                    for (MemberValuePair memberValuePair : memberValuePairs) {
                        if (memberValuePair.getName().getIdentifier().equals(method.getElementName())) {
                            return getTextForMethod(method, memberValuePair.getValue());
                        }
                    }
                } else if (annotation.isSingleMemberAnnotation()) {
                    SingleMemberAnnotation singleMemberAnnotation = (SingleMemberAnnotation) annotation;
                    return getTextForMethod(method, singleMemberAnnotation.getValue());
                }
            }
        }
        return ""; //$NON-NLS-1$
    }

    private String getTextForMethod(IMethod method, Expression expression) throws JavaModelException {
        String returnType = method.getReturnType();

        if (SignatureUtils.isString(returnType) || SignatureUtils.isClass(returnType)) {
            return expression.toString();
        }

        if (SignatureUtils.isEnum(method)) {
            String enumValue = expression.toString();
            return enumValue.substring(enumValue.lastIndexOf(".") + 1); //$NON-NLS-1$
        }

        if (SignatureUtils.isPrimitive(returnType) && (returnType.charAt(0) == Signature.C_BYTE
                || returnType.charAt(0) == Signature.C_SHORT || returnType.charAt(0) == Signature.C_INT
                || returnType.charAt(0) == Signature.C_LONG  || returnType.charAt(0) == Signature.C_FLOAT
                || returnType.charAt(0) == Signature.C_DOUBLE)) {
            return expression.toString();
        }

        if (SignatureUtils.isArray(returnType) && expression instanceof ArrayInitializer) {
            ArrayInitializer arrayInitializer = (ArrayInitializer) expression;
            if (arrayInitializer.expressions().size() > 0) {
                return "[]{...}"; //$NON-NLS-1$
            } else {
                return "[]{}"; //$NON-NLS-1$
            }
        }
        return ""; //$NON-NLS-1$
    }

    @Override
    public Image getImage(Object element) {
        try {
            if (element instanceof IType) {
                return getImageForClass((IType) element);
            }
            if (element instanceof IMethod) {
                return getImageForMethod((IMethod) element);
            }
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
        }
        return null;
    }

    private Image getImageForClass(IType type) throws JavaModelException {
        if (annotationTreeViewer.getInput() instanceof IJavaElement) {
            IJavaElement javaElement = (IJavaElement) annotationTreeViewer.getInput();
            if (javaElement.exists()) {
                return getImageForClass(type, javaElement);
            }
        }
        return null;
    }

    private Image getImageForClass(IType type, IJavaElement javaElement) throws JavaModelException {
        List<Annotation> annotations = AnnotationUtils.getAnnotations(javaElement);
        for (Annotation annotation : annotations) {
            String annotationName = AnnotationUtils.getAnnotationName(annotation);
            if (annotationName.equals(type.getElementName()) ||
                    annotationName.equals(type.getFullyQualifiedName())) {
                return true_image;
            }
        }
        return false_image;
    }

    private Image getImageForMethod(IMethod method) throws JavaModelException {
        if (SignatureUtils.isBoolean(method.getReturnType()) && annotationTreeViewer.getInput() instanceof IJavaElement) {
            IJavaElement javaElement = (IJavaElement) annotationTreeViewer.getInput();
            if (javaElement.exists()) {
                return getImageForMethod(method, javaElement);
            }
        }
        return null;
    }

    private Image getImageForMethod(IMethod method, IJavaElement javaElement) throws JavaModelException {
        List<Annotation> annotations = AnnotationUtils.getAnnotations(javaElement);
        for (Annotation annotation : annotations) {
            String annotationName = AnnotationUtils.getAnnotationName(annotation);
            IType declaringType = method.getDeclaringType();
            if (declaringType != null && annotationName.equals(declaringType.getElementName())
                    || annotationName.equals(declaringType.getFullyQualifiedName())) {
                if (annotation.isNormalAnnotation()) {
                    NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;
                    @SuppressWarnings("unchecked")
                    List<MemberValuePair> memberValuePairs = normalAnnotation.values();
                    for (MemberValuePair memberValuePair : memberValuePairs) {
                        if (memberValuePair.getName().getIdentifier().equals(method.getElementName())
                                && memberValuePair.getValue() instanceof BooleanLiteral) {
                            if (((BooleanLiteral) memberValuePair.getValue()).booleanValue()) {
                                return true_image;
                            }
                        }
                    }
                } else if (annotation.isSingleMemberAnnotation()) {
                    SingleMemberAnnotation singleMemberAnnotation = (SingleMemberAnnotation) annotation;
                    if (singleMemberAnnotation.getValue() instanceof BooleanLiteral) {
                        if (((BooleanLiteral) singleMemberAnnotation.getValue()).booleanValue()) {
                            return true_image;
                        }
                    }
                }
            }
        }
        return false_image;
    }

    @Override
    public void dispose() {
        super.dispose();
        true_image.dispose();
        false_image.dispose();
    }
}
