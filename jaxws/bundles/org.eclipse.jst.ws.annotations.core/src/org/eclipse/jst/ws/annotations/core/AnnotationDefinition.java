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
import org.eclipse.jst.ws.annotations.core.initialization.IAnnotationAttributeInitializer;

/**
 * An <code>AnnotationDefinition</code> is a representation of the information contributed through the
 * <code>org.eclipse.jst.ws.annotations.core.annotationDefinition</code>,
 * <code>org.eclipse.jst.ws.annotations.core.annotationCategory</code> and
 * <code>org.eclipse.jst.ws.annotations.core.annotationInitializer</code> extension points.
 * for a <code>java.lang.annotation.Annotation</code> class.
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
        this.restictedTo = AnnotationsManager.getAttributeValue(configurationElement,
                ATT_RESTRICTED_TO);
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
     * @return the annotation class
     */
    @SuppressWarnings("unchecked")
    public Class<? extends java.lang.annotation.Annotation> getAnnotationClass() {
        if (annotationClass == null) {
            try {
                Class<?> aClass = Class.forName(annotationClassName);
                if (aClass.isAnnotation()) {
                    annotationClass = (Class<java.lang.annotation.Annotation>)Class.forName(
                            annotationClassName);
                }
            } catch(ClassNotFoundException cnfe) {
                AnnotationsCorePlugin.log(cnfe);
            }
        }
        return annotationClass;
    }

    /**
     * Returns a list of <code>ElementType</code> that specify the Java elements to which the annotation
     * can be applied.
     * <p>
     * The <code>ElementType</code> are retrieved from the annotations
     * <code>java.lang.annotation.Target</code> meta-annotation type. This list can be filtered using
     * the <code>targetFilter</code> element on the
     * <code>org.eclipse.jst.ws.annotations.core.annotationDefinition</code> extension point when defining
     * the annotation.
     * </p>
     * @return a list of <code>ElementType</code>
     */
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
                    annotationInitializer = (IAnnotationAttributeInitializer)configurationElement
                        .createExecutableExtension(ATT_CLASS);
                }
            } catch (CoreException ce) {
                AnnotationsCorePlugin.log(ce.getStatus());
            }
        }
        return annotationInitializer;
    }

	  /**
	   *
	   * @param configurationElement the  co
	   * @return a list of <code>ElementType</code>.
	   */
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
}
