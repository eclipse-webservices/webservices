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

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jst.ws.internal.jaxws.core.JAXWSCorePlugin;

/**
 * 
 * @author sclarke
 *
 */
public final class AnnotationsManager {
    public static final String ANNOTATION_DEFINITION = "annotationDefinition"; //$NON-NLS-1$
    public static final String ANNOTATION_CATEGORY = "annotationCategory"; //$NON-NLS-1$
    public static final String ANNOTATION = "ANNOTATION"; //$NON-NLS-1$

    private static Map<String, String> annotationCategoryCache = null;
    private static Map<String, List<AnnotationDefinition>> annotationsByCategoryMap = null;
    private static List<AnnotationDefinition> annotationCache = null;
    private static Map<Class<? extends java.lang.annotation.Annotation>, AnnotationDefinition> 
            annotationClassToDefinitionMap;

    private static final String ATT_ID = "id"; //$NON-NLS-1$
    private static final String ATT_NAME = "name"; //$NON-NLS-1$
    private static final String ATT_CATEGORY = "category"; //$NON-NLS-1$

    private AnnotationsManager() {
    }
    
    public static Object[] getAnnotations(Object element) {
        List<Class<? extends java.lang.annotation.Annotation>> annotations = 
            new ArrayList<Class<? extends java.lang.annotation.Annotation>>();
        
        try {
            List<AnnotationDefinition> annotationDefinitions = getAllAnnotationsForElement(element);
        
            filterAnnotationsList(element, annotationDefinitions);
        
            for (AnnotationDefinition annotationDefinition : annotationDefinitions) {
                annotations.add(annotationDefinition.getAnnotationClass());
                getAnnotationToClassDefinitionMap().put(annotationDefinition.getAnnotationClass(),
                        annotationDefinition);
            }
        } catch (JavaModelException jme) {
            JAXWSCorePlugin.log(jme.getStatus());
        }  
        return annotations.toArray();     
    }
    
    private static synchronized Map<Class<? extends java.lang.annotation.Annotation>, AnnotationDefinition> 
            getAnnotationToClassDefinitionMap() {
        if (annotationClassToDefinitionMap == null) {
            annotationClassToDefinitionMap = new HashMap<Class<? extends java.lang.annotation.Annotation>, 
                AnnotationDefinition>();
        }
        return annotationClassToDefinitionMap;
    }
    
    @SuppressWarnings("unchecked")
    public static AnnotationDefinition getAnnotationDefinitionForClass(Object element) {
        if (element instanceof Class && ((Class<?>)element).isAnnotation()) {
            return getAnnotationToClassDefinitionMap().get(
                (Class<? extends java.lang.annotation.Annotation>)element);
        }
        return null;
    }
    
    public static AnnotationDefinition getAnnotationDefinitionForClass(Class<? 
            extends java.lang.annotation.Annotation> annotationClass) {
        return getAnnotationToClassDefinitionMap().get(annotationClass);
    }
    
    public static synchronized List<AnnotationDefinition> getAnnotationsByCategory(String categoryName) {
        if (annotationsByCategoryMap == null) {
            annotationsByCategoryMap = new HashMap<String, List<AnnotationDefinition>>();
            for (AnnotationDefinition annotationDefinition : getAnnotations()) {
                if (annotationsByCategoryMap.get(annotationDefinition.getCategory()) == null) {
                    List<AnnotationDefinition> annotationDefinitionList = new ArrayList<AnnotationDefinition>();
                    annotationDefinitionList.add(annotationDefinition);
                    annotationsByCategoryMap.put(annotationDefinition.getCategory(), annotationDefinitionList);
                    continue;
                }
                annotationsByCategoryMap.get(annotationDefinition.getCategory()).add(annotationDefinition);
            }
        }
        return annotationsByCategoryMap.get(categoryName);
    }

    public static List<String> getAnnotationCategories() {
        return Arrays.asList(getAnnotationCategoryCache().values().toArray(
                new String[getAnnotationCategoryCache().size()]));
    }

    public static String getAnnotationCategory(String categoryId) {
        return getAnnotationCategoryCache().get(categoryId);        
    }

    private static synchronized Map<String, String> getAnnotationCategoryCache() {
        if (annotationCategoryCache != null) {
            return annotationCategoryCache;
        }

        annotationCategoryCache = new HashMap<String, String>();
        
        IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
                JAXWSCorePlugin.PLUGIN_ID, ANNOTATION_CATEGORY);
        if (extensionPoint != null) {
            IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
            for (int i = 0; i < elements.length; i++) {
                IConfigurationElement element = elements[i];
                annotationCategoryCache.put(getAttributeValue(element, ATT_ID), 
                        getAttributeValue(element, ATT_NAME));
            }
        }
        return annotationCategoryCache;
    }
    
    public static String getAttributeValue(IConfigurationElement configurationElement, String attributeName) {
        String attribute = configurationElement.getAttribute(attributeName);
        if (attribute != null) {
            return attribute;
        }
        return ""; //$NON-NLS-1$
    }

    private static List<AnnotationDefinition> getAllAnnotationsForElement(Object element) 
            throws JavaModelException {
        
        if (element instanceof IPackageDeclaration) {
            return getAnnotationsForElementType(ElementType.PACKAGE);
        }
        
        if (element instanceof IType) {
            IType type = (IType) element;
            if (type.isAnnotation()) {
                return getAnnotationsForElementType(ElementType.ANNOTATION_TYPE);
            }
            return getAnnotationsForElementType(ElementType.TYPE);
        }
        
        if (element instanceof IField) {
            return getAnnotationsForElementType(ElementType.FIELD);
        }
        
        if (element instanceof IMethod) {
            return getAnnotationsForElementType(ElementType.METHOD);
        }
        
        if (element instanceof SingleVariableDeclaration) {
            return getAnnotationsForElementType(ElementType.PARAMETER);
        }
        
        if (element instanceof ILocalVariable) {
            return getAnnotationsForElementType(ElementType.LOCAL_VARIABLE);
        }
        
        if (element instanceof IAnnotation) {
            return getAnnotationsForElementType(ElementType.ANNOTATION_TYPE);
        }
        
        return Collections.emptyList();
    }
    
    private static List<AnnotationDefinition> getAnnotationsForElementType(ElementType elementType) {
        List<AnnotationDefinition> annotationDefinitions = new ArrayList<AnnotationDefinition>();
        
        if (annotationCache == null) {
            annotationCache = getAnnotations();
        }
        
        for (AnnotationDefinition annotationDefinition : annotationCache) {
            if (annotationDefinition.getTargets().contains(elementType) && 
                    !isDeprecated(annotationDefinition)) {
                annotationDefinitions.add(annotationDefinition);
            }
        }
        return annotationDefinitions;
    }
    
    public static synchronized List<AnnotationDefinition> getAnnotations() {
        if (annotationCache == null) {
            annotationCache = new ArrayList<AnnotationDefinition>();
            
            IExtensionPoint extensionPoint = Platform.getExtensionRegistry().getExtensionPoint(
                    JAXWSCorePlugin.PLUGIN_ID, ANNOTATION_DEFINITION);
            if (extensionPoint != null) {
                IConfigurationElement[] elements = extensionPoint.getConfigurationElements();
                for (int i = 0; i < elements.length; i++) {
                    IConfigurationElement element = elements[i];
                    if (element.getName().equalsIgnoreCase(ANNOTATION)) {
                        annotationCache.add(new AnnotationDefinition(element, 
                                getAnnotationCategory(getAttributeValue(element, ATT_CATEGORY))));
                    }
                }
            }
        }
        return annotationCache;
    }
    
    private static void filterAnnotationsList(Object element, 
        List<AnnotationDefinition> annotationDefinitions) throws JavaModelException {
        Iterator<AnnotationDefinition> annotationIter = annotationDefinitions.iterator();
        while (annotationIter.hasNext()) {
            AnnotationDefinition annotationDefinition = annotationIter.next();
            
            if (element instanceof IType) {
                IType type = (IType) element;
                if (isClassRestricted(type, annotationDefinition)
                        || isInterfaceRestricted(type, annotationDefinition)
                        || isEnumRestricted(type, annotationDefinition)) {
                    annotationIter.remove();
                }
            }
            if (element instanceof IMethod) {
                IMethod method = (IMethod) element;
                if (method.isMainMethod()) {
                    annotationIter.remove();
                }
                if (method.isConstructor()
                        && !annotationDefinition.getTargets().contains(ElementType.CONSTRUCTOR)) {
                    annotationIter.remove();
                }

                if (isClassRestricted(method, annotationDefinition)
                        || isInterfaceRestricted(method, annotationDefinition)
                        || isEnumRestricted(method, annotationDefinition)) {
                    annotationIter.remove();
                }
            }
            
            if (element instanceof IField) {
                if(isClassRestricted((IField) element, annotationDefinition)
                        || isInterfaceRestricted((IField) element, annotationDefinition)
                        ||isEnumRestricted((IField) element, annotationDefinition)) {
                    annotationIter.remove();
                }
            }
        }
    }
    
    private static boolean isClassRestricted(IJavaElement javaElement, AnnotationDefinition annotationDefinition) 
            throws JavaModelException {
        if (javaElement.getElementType() == IJavaElement.TYPE) {
            return !((IType)javaElement).isClass() && annotationDefinition.isClassOnly();
        }
        if (javaElement.getElementType() == IJavaElement.METHOD) {
            IType type = (IType)javaElement.getParent();
            return !type.isClass() && annotationDefinition.isClassOnly();
        }
        if (javaElement.getElementType() == IJavaElement.FIELD) {
            IType type = (IType)javaElement.getParent();
            return !type.isClass() && annotationDefinition.isClassOnly();
        }
        return false;
    }
    
    private static boolean isInterfaceRestricted(IJavaElement javaElement, AnnotationDefinition annotationDefinition) 
            throws JavaModelException {
        if (javaElement.getElementType() == IJavaElement.TYPE) {
            return !((IType)javaElement).isInterface() && annotationDefinition.isInterfaceOnly();
        }
        if (javaElement.getElementType() == IJavaElement.METHOD) {
            IType type = (IType)javaElement.getParent();
            return !type.isInterface()  && annotationDefinition.isInterfaceOnly();
        }
        if (javaElement.getElementType() == IJavaElement.FIELD) {
            IType type = (IType)javaElement.getParent();
            return !type.isInterface() && annotationDefinition.isInterfaceOnly();
        }
        return false;
    }
    
    private static boolean isEnumRestricted(IJavaElement javaElement, AnnotationDefinition annotationDefinition) 
            throws JavaModelException {
        if (javaElement.getElementType() == IJavaElement.TYPE) {
            return !((IType)javaElement).isEnum() && annotationDefinition.isEnumOnly();            
        }
        if (javaElement.getElementType() == IJavaElement.METHOD) {
            IType type = (IType)javaElement.getParent();
            return !type.isEnum() && annotationDefinition.isEnumOnly();            
        }
        if (javaElement.getElementType() == IJavaElement.FIELD) {
            IType type = (IType)javaElement.getParent();
            return !type.isEnum() && annotationDefinition.isEnumOnly();            
        }
        return false;
    }
    
    //TODO Move the Deprecated option to preferences
    private static boolean isDeprecated(AnnotationDefinition annotationDefinition) {
        Class<?> annotationClass = annotationDefinition.getAnnotationClass();
        return annotationClass.getAnnotation(java.lang.Deprecated.class) != null;
    }
}
