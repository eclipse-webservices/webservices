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

import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.JavaModelException;
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
        String text = ""; //$NON-NLS-1$
        if (element instanceof Method) {
            text =  getTextForMethod((Method)element);
        }
        return text;
    }
    
    private String getTextForMethod(Method method) {
        String text = ""; //$NON-NLS-1$
        if (annotationTreeViewer.getInput() instanceof IAnnotatable) {
            text = getTextForMethod(method, (IAnnotatable) annotationTreeViewer.getInput());
        }
        return text;
    }
    
    private String getTextForMethod(Method method, IAnnotatable annotatedElement) {
        Class<?> returnType = method.getReturnType();
        try {
            IAnnotation[] annotations = annotatedElement.getAnnotations();
            for (IAnnotation annotation : annotations) {
                Class<?> declaringClass = method.getDeclaringClass();
                if (annotation.getElementName().equals(declaringClass.getSimpleName())
                        || annotation.getElementName().equals(declaringClass.getCanonicalName())) {
                    IMemberValuePair[] memberValuePairs = annotation.getMemberValuePairs();
                    for (IMemberValuePair memberValuePair : memberValuePairs) {
                        if (memberValuePair.getValue() == null) {
                            break;
                        }
                        if (memberValuePair.getMemberName().equals(method.getName())) {
                            if (returnType.equals(String.class)) {
                                return memberValuePair.getValue().toString();
                            }
                            
                            if (returnType.equals(Class.class)) {
                                return memberValuePair.getValue().toString() + ".class";  //$NON-NLS-1$
                            }
                            if (returnType.isPrimitive() && (returnType.equals(Byte.TYPE) 
                                    || returnType.equals(Short.TYPE) 
                                    || returnType.equals(Integer.TYPE) || returnType.equals(Long.TYPE)
                                    || returnType.equals(Float.TYPE) 
                                    || returnType.equals(Double.TYPE))) {
                                return memberValuePair.getValue().toString();
                            }
                            
                            if (returnType.isArray()) {
                                Object[] values = (Object[])memberValuePair.getValue();
                                if (values != null && values.length > 0) {
                                    return "[]{...}"; //$NON-NLS-1$
                                } else {
                                    return "[]{}"; //$NON-NLS-1$
                                }
                            }
                            
                            if (returnType.isEnum()) {
                                String enumValue = memberValuePair.getValue().toString();
                                return enumValue.substring(enumValue.lastIndexOf(".") + 1); //$NON-NLS-1$
                            }
                        }
                    }
                }
            }
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
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
        if (annotationTreeViewer.getInput() instanceof IAnnotatable) {
            return getImageForClass(aClass, (IAnnotatable) annotationTreeViewer.getInput());
        }

        return false_image;        
    }
    
    private Image getImageForClass(Class<?> aClass, IAnnotatable annotatedElement) throws JavaModelException {
        IAnnotation[] annotations = annotatedElement.getAnnotations();
        for (IAnnotation annotation : annotations) {
            String annotationName = annotation.getElementName();
            if (AnnotationUtils.isAnnotationPresent((IJavaElement)annotatedElement, annotationName) && 
                    (annotationName.equals(aClass.getSimpleName()) || 
                    annotationName.equals(aClass.getCanonicalName()))) {
                return true_image;
            }
        }
        return false_image;
    }
    
    private Image getImageForMethod(Method method) throws JavaModelException {
        Class<?> returnType = method.getReturnType();
        if (returnType.equals(Boolean.TYPE)) {
            if (annotationTreeViewer.getInput() instanceof IAnnotatable) {
                return getImageForMethod(method, (IAnnotatable) annotationTreeViewer.getInput());
            }
        }
        return null;
    }

    private Image getImageForMethod(Method method, IAnnotatable annotatedElement) throws JavaModelException {
        IAnnotation[] annotations = annotatedElement.getAnnotations();
        for (IAnnotation annotation : annotations) {
            String annotationName = annotation.getElementName();
            Class<?> declaringClass = method.getDeclaringClass();
            if (annotationName.equals(declaringClass.getSimpleName())
                    || annotationName.equals(declaringClass.getCanonicalName())) {
                IMemberValuePair[] memberValuePairs = annotation.getMemberValuePairs();
                for (IMemberValuePair memberValuePair : memberValuePairs) {
                    if (memberValuePair.getMemberName().equals(method.getName())) {
                        if (Boolean.parseBoolean(memberValuePair.getValue().toString())) {
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
