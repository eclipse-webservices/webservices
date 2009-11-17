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

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
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
        if (element instanceof Method) {
            return getTextForMethod((Method)element);
        }
        return ""; //$NON-NLS-1$
    }

    private String getTextForMethod(Method method) {
        if (annotationTreeViewer.getInput() instanceof IJavaElement) {
            IJavaElement javaElement = (IJavaElement) annotationTreeViewer.getInput();
            if (javaElement.exists()) {
                return getTextForMethod(method, javaElement);
            }
        }
        return ""; //$NON-NLS-1$
    }

    private String getTextForMethod(Method method, IJavaElement annotatedElement) {
        List<Annotation> annotations = AnnotationUtils.getAnnotations(annotatedElement);
        for (Annotation annotation : annotations) {
            String annotationName = AnnotationUtils.getAnnotationName(annotation);
            Class<?> declaringClass = method.getDeclaringClass();
            if (annotationName.equals(declaringClass.getSimpleName())
                    || annotationName.equals(declaringClass.getCanonicalName())) {
                if (annotation.isNormalAnnotation()) {
                    NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;
                    @SuppressWarnings("unchecked")
                    List<MemberValuePair> memberValuePairs = normalAnnotation.values();
                    for (MemberValuePair memberValuePair : memberValuePairs) {
                        if (memberValuePair.getName().getIdentifier().equals(method.getName())) {
                            return getTextForMethod(method.getReturnType(), memberValuePair.getValue());
                        }
                    }
                } else if (annotation.isSingleMemberAnnotation()) {
                    SingleMemberAnnotation singleMemberAnnotation = (SingleMemberAnnotation) annotation;
                    return getTextForMethod(method.getReturnType(), singleMemberAnnotation.getValue());
                }
            }
        }
        return ""; //$NON-NLS-1$
    }

    private String getTextForMethod(Class<?> returnType, Expression expression) {
        if (returnType.equals(String.class)) {
            return expression.toString();
        }

        if (returnType.equals(Class.class)) {
            return expression.toString() + ".class"; //$NON-NLS-1$
        }
        if (returnType.isPrimitive() && (returnType.equals(Byte.TYPE)
                || returnType.equals(Short.TYPE) || returnType.equals(Integer.TYPE)
                || returnType.equals(Long.TYPE)  || returnType.equals(Float.TYPE)
                || returnType.equals(Double.TYPE))) {
            return expression.toString();
        }

        if (returnType.isArray() && expression instanceof ArrayInitializer) {
            ArrayInitializer arrayInitializer = (ArrayInitializer) expression;
            if (arrayInitializer.expressions().size() > 0) {
                return "[]{...}"; //$NON-NLS-1$
            } else {
                return "[]{}"; //$NON-NLS-1$
            }
        }
        if (returnType.isEnum()) {
            String enumValue = expression.toString();
            return enumValue.substring(enumValue.lastIndexOf(".") + 1); //$NON-NLS-1$
        }
        return ""; //$NON-NLS-1$
    }

    @Override
    public Image getImage(Object element) {
        try {
            if (element instanceof Class) {
                return getImageForClass((Class<?>) element);
            }
            if (element instanceof Method) {
                return getImageForMethod((Method) element);
            }
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
        }
        return null;
    }

    private Image getImageForClass(Class<?> aClass) throws JavaModelException {
        if (annotationTreeViewer.getInput() instanceof IJavaElement) {
            IJavaElement javaElement = (IJavaElement) annotationTreeViewer.getInput();
            if (javaElement.exists()) {
                return getImageForClass(aClass, javaElement);
            }
        }
        return false_image;
    }

    private Image getImageForClass(Class<?> aClass, IJavaElement javaElement) throws JavaModelException {
        List<Annotation> annotations = AnnotationUtils.getAnnotations(javaElement);
        for (Annotation annotation : annotations) {
            String annotationName = AnnotationUtils.getAnnotationName(annotation);
            if (annotationName.equals(aClass.getSimpleName()) ||
                    annotationName.equals(aClass.getCanonicalName())) {
                return true_image;
            }
        }
        return false_image;
    }

    private Image getImageForMethod(Method method) throws JavaModelException {
        if (method.getReturnType().equals(Boolean.TYPE) && annotationTreeViewer.getInput() instanceof IJavaElement) {
            IJavaElement javaElement = (IJavaElement) annotationTreeViewer.getInput();
            if (javaElement.exists()) {
                return getImageForMethod(method, javaElement);
            }
        }
        return null;
    }

    private Image getImageForMethod(Method method, IJavaElement javaElement) throws JavaModelException {
        List<Annotation> annotations = AnnotationUtils.getAnnotations(javaElement);
        for (Annotation annotation : annotations) {
            String annotationName = AnnotationUtils.getAnnotationName(annotation);
            Class<?> declaringClass = method.getDeclaringClass();
            if (annotationName.equals(declaringClass.getSimpleName())
                    || annotationName.equals(declaringClass.getCanonicalName())) {
                if (annotation.isNormalAnnotation()) {
                    NormalAnnotation normalAnnotation = (NormalAnnotation) annotation;
                    @SuppressWarnings("unchecked")
                    List<MemberValuePair> memberValuePairs = normalAnnotation.values();
                    for (MemberValuePair memberValuePair : memberValuePairs) {
                        if (memberValuePair.getName().getIdentifier().equals(method.getName())
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
