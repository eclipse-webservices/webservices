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
package org.eclipse.jst.ws.annotations.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.annotations.core.initialization.IAnnotationAttributeInitializer;

/**
 * An <code>AnnotationDefinition</code> is a representation of the information contributed through the
 * <code>org.eclipse.jst.ws.annotations.core.annotationDefinition</code>,
 * <code>org.eclipse.jst.ws.annotations.core.annotationCategory</code> and
 * <code>org.eclipse.jst.ws.annotations.core.annotationInitializer</code> extension points.
 * <p>
 * It supplies the annotation class name, its annotation category, the applicable targets for the annotation
 * and an <code>IAnnotationAttributeInitializer</code> to initialize the annotations element-value pairs.
 * </p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under
 * development and expected to change significantly before reaching stability. It is being made available at
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 */
public final class AnnotationDefinition {
    private static final String ATT_CLASS = "class"; //$NON-NLS-1$
    private static final String ATT_NAME = "name"; //$NON-NLS-1$
    private static final String ATT_RESTRICTED_TO = "restrictedTo"; //$NON-NLS-1$

    private static final String RESTRICTED_TO_CLASS_ONLY = "CLASS_ONLY";
    private static final String RESTRICTED_TO_INTERFACE_ONLY = "INTERFACE_ONLY";
    private static final String RESTRICTED_TO_ENUM_ONLY = "ENUM_ONLY";

    private static final String ELEM_TARGET_FILTER = "targetFilter"; //$NON-NLS-1$
    private static final String ATT_TARGET = "target"; //$NON-NLS-1$

    private IConfigurationElement configurationElement;
    private String category;
    private String annotationClassName;
    private Class<? extends java.lang.annotation.Annotation> annotationClass;
    private List<ElementType> targets;
    private String name;
    private IAnnotationAttributeInitializer annotationInitializer;
    private String restictedTo;
    private boolean interfaceOnly;
    private boolean classOnly;
    private boolean enumOnly;

    private IType annotationType;
    private List<ElementType> annotationTypeTargets;
    private IJavaProject javaProject;
    private Boolean deprecated;

    /**
     * Constructs an <code>AnnotationDefinition</code> using information from the
     * <code>org.eclipse.jst.ws.annotations.core.annotationDefinition</code> extension point and category name.
     * @param configurationElement the <code>annotation</code> element from the <code>org.eclipse.jst.ws.annotations.core.annotationDefinition</code> extension point
     * @param category the category name
     */
    public AnnotationDefinition(IConfigurationElement configurationElement, String category) {
        this.configurationElement = configurationElement;
        this.category = category;

        this.annotationClassName = AnnotationsManager.getAttributeValue(configurationElement, ATT_CLASS);
        this.name = AnnotationsManager.getAttributeValue(configurationElement, ATT_NAME);
        this.restictedTo = AnnotationsManager.getAttributeValue(configurationElement, ATT_RESTRICTED_TO);
        this.classOnly = restictedTo.equals(RESTRICTED_TO_CLASS_ONLY);
        this.interfaceOnly = restictedTo.equals(RESTRICTED_TO_INTERFACE_ONLY);
        this.enumOnly = restictedTo.equals(RESTRICTED_TO_ENUM_ONLY);
    }

    /**
     * Returns the annotation name.
     * @return the annotation name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the category the annotation belongs to.
     * @return the annotation category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the fully qualified class name of the annotation.
     * @return the fully qualified class name of the annotation.
     */
    public String getAnnotationClassName() {
        return annotationClassName;
    }

    /**
     * Returns whether the annotation is restricted to class types.
     * @return <code>true</code> if the annotation is restricted to classes only.
     */
    public boolean isClassOnly() {
        return classOnly;
    }

    /**
     * Returns whether the annotation is restricted to interface types.
     * @return <code>true</code> if the annotation is restricted to interfaces only.
     */
    public boolean isInterfaceOnly() {
        return interfaceOnly;
    }

    /**
     * Returns whether the annotation is restricted to enum types.
     * @return <code>true</code> if the annotation is restricted to enums only.
     */
    public boolean isEnumOnly() {
        return enumOnly;
    }

    /**
     * Returns the annotation class as specified by the <code>class</code> attribute of the
     * <code>annotation<annotation> element in the <code>org.eclipse.jst.ws.annotations.core.annotationDefinition</code>
     * extension point.
     *
     * @return the annotation class or null if not found.
     * @deprecated As of 1.1 replaced by {@link #getAnnotationType()}
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public Class<? extends java.lang.annotation.Annotation> getAnnotationClass() {
        if (annotationClass == null) {
            try {
                Class<?> aClass = Class.forName(annotationClassName);
                if (aClass.isAnnotation()) {
                    annotationClass = (Class<java.lang.annotation.Annotation>) Class.forName(annotationClassName);
                }
            } catch(ClassNotFoundException cnfe) {
                AnnotationsCorePlugin.log(cnfe);
            }
        }
        return annotationClass;
    }

    /**
     * Returns the annotation type as specified by the <code>class</code> attribute of the
     * <code>annotation<annotation> element in the <code>org.eclipse.jst.ws.annotations.core.annotationDefinition</code>
     * extension point.
     * 
     * @return the <code>org.eclipse.jdt.core.IType</code> which represents an annotation type or null if the java project
     * has not been set, if the type cannot be found or if the type does not represent an annotation type.
     * 
     * @see #setJavaProject(IJavaProject)
     * @since 1.1
     */
    public IType getAnnotationType() {
        if (annotationType == null) {
            try {
                if (javaProject != null) {
                    IType type = javaProject.findType(annotationClassName);
                    if (type != null && type.isAnnotation()) {
                        annotationType = type;
                    }
                }
            } catch (JavaModelException jme) {
                AnnotationsCorePlugin.log(jme.getStatus());
            }
        }
        return annotationType;
    }

    /**
     * Returns a list of {@link ElementType} that specify the Java elements to which the annotation
     * can be applied.
     * <p>
     * The element types are retrieved from the annotations
     * {@link java.lang.annotation.Target} meta-annotation type. This list can be filtered using
     * the <code>targetFilter</code> element on the
     * <code>org.eclipse.jst.ws.annotations.core.annotationDefinition</code> extension point when defining
     * the annotation.
     * </p>
     * @return a list of element types.
     * @deprecated as of 1.1 replaced by {@link #getAnnotationTypeTargets()}
     */
    @Deprecated
    public List<ElementType> getTargets() {
        if (targets == null) {
            targets = new LinkedList<ElementType>();

            Class<? extends java.lang.annotation.Annotation> annotation = getAnnotationClass();
            if (annotation != null) {
                Target target = annotation.getAnnotation(Target.class);
                if (target != null) {
                    targets.addAll(Arrays.asList(target.value()));

                    List<ElementType> filteredTargets = getFilteredTargets(configurationElement);
                    if (targets.containsAll(filteredTargets) && filteredTargets.size() < targets.size()) {
                        targets.removeAll(filteredTargets);
                    }
                }
            }
        }
        return targets;
    }

    /**
     * Returns a list of {@link java.lang.annotation.ElementType} that specify the Java elements to which the annotation can be applied.
     * <p>
     * The element types are retrieved from the annotations
     * {@link java.lang.annotation.Target} meta-annotation type. This list can
     * be filtered using the <code>targetFilter</code> element on the
     * <code>org.eclipse.jst.ws.annotations.core.annotationDefinition</code>
     * extension point when defining the annotation.
     * </p>
     * @return a list of element types or null if the java project has not been set or if the annotation type cannot be
     * found.
     * @see #setJavaProject(IJavaProject)
     * @since 1.1
     */
    public List<ElementType> getAnnotationTypeTargets() {
        if (annotationTypeTargets == null) {
            annotationTypeTargets = new LinkedList<ElementType>();
            try {
                IType type = getAnnotationType();
                if (type != null) {
                    IAnnotation target = type.getAnnotation(Target.class.getCanonicalName());
                    if (!target.exists()) {
                        target = type.getAnnotation(Target.class.getSimpleName());
                    }
                    if (target.exists()) {
                        IMemberValuePair[] memberValuePairs = target.getMemberValuePairs();
                        for (IMemberValuePair memberValuePair : memberValuePairs) {
                            Object value = memberValuePair.getValue();
                            if (value.getClass().isArray() && value.getClass().getComponentType().equals(String.class)) {
                                String[] objs = (String[]) value;
                                for (String obj : objs) {
                                    annotationTypeTargets.add(ElementType.valueOf(obj.substring(obj.lastIndexOf('.') + 1)));
                                }
                            } else {
                                annotationTypeTargets.add(ElementType.valueOf(value.toString().substring(value.toString().lastIndexOf('.') + 1)));
                            }
                        }
                    } else {
                        //Target meta-annotation is not present on an annotation type declaration, the declared type may be used on any program element.
                    	annotationTypeTargets.addAll(Arrays.asList(ElementType.values()));
                    }
                    List<ElementType> filteredTargets = getFilteredTargets(configurationElement);
                    if (annotationTypeTargets.containsAll(filteredTargets) && filteredTargets.size() < annotationTypeTargets.size()) {
                        annotationTypeTargets.removeAll(filteredTargets);
                    }
                }
            } catch (JavaModelException jme) {
                AnnotationsCorePlugin.log(jme.getStatus());
            }
        }
        return annotationTypeTargets;
    }

    /**
     * Returns the annotations attribute initializer as specified in the
     * <code>org.eclipse.jst.ws.annotations.core.annotationInitializer</code> extension point or null if no
     * initializer can be found.
     *
     * @return the <code>IAnnotationAttributeInitializer</code>
     */
    public IAnnotationAttributeInitializer getAnnotationAttributeInitializer() {
        if (annotationInitializer == null) {
            try {
                IConfigurationElement configurationElement =
                    AnnotationsManager.getAnnotationInitializerCache().get(getAnnotationClassName());
                if (configurationElement != null) {
                    annotationInitializer = (IAnnotationAttributeInitializer) configurationElement.createExecutableExtension(ATT_CLASS);
                }
            } catch (CoreException ce) {
                AnnotationsCorePlugin.log(ce.getStatus());
            }
        }
        return annotationInitializer;
    }

    private List<ElementType> getFilteredTargets(IConfigurationElement configurationElement) {
        List<ElementType> targets = new ArrayList<ElementType>(7);
        try {
            IConfigurationElement[] deprecatedTargets = configurationElement.getChildren(ELEM_TARGET_FILTER);
            for (IConfigurationElement deprecatedTargetElement : deprecatedTargets) {
                String target = AnnotationsManager.getAttributeValue(deprecatedTargetElement, ATT_TARGET);
                targets.add(ElementType.valueOf(target));
            }
        } catch (IllegalArgumentException iae) {
            AnnotationsCorePlugin.log(iae);
        }
        return targets;
    }

    boolean isDeprecated() {
        if (deprecated == null) {
            IType type = getAnnotationType();
            if (type != null) {
                IAnnotation annotation = type.getAnnotation(Deprecated.class.getCanonicalName());
                if (!annotation.exists()) {
                    annotation = type.getAnnotation(Deprecated.class.getSimpleName());
                }
                deprecated = annotation.exists();
            } else {
                deprecated = false;
            }
        }
        return deprecated;
    }

    /**
     * Sets the <code>org.eclipse.jdt.core.IJavaProject</code> which is used to find the annotation type.
     * 
     * @see #getAnnotationType()
     * @see #getAnnotationTypeTargets()
     * @since 1.1
     */
    public void setJavaProject(IJavaProject javaProject) {
        if (this.javaProject == null || !this.javaProject.equals(javaProject)) {
            this.javaProject = javaProject;
            this.annotationType = null;
            this.annotationTypeTargets = null;
            this.deprecated = null;
        }
    }
}
