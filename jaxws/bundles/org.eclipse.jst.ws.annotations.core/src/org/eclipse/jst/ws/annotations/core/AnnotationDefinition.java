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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jst.ws.annotations.core.initialization.IAnnotationAttributeInitializer;

/**
 * 
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under 
 * development and expected to change significantly before reaching stability. It is being made available at 
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses 
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 * @author sclarke
 */
public class AnnotationDefinition {
    private static final String ATT_CLASS = "class"; //$NON-NLS-1$
    private static final String ATT_NAME = "name"; //$NON-NLS-1$
    private static final String ATT_RESTRICTED_TO = "restrictedTo"; //$NON-NLS-1$
    
    private static final String RESTRICTED_TO_CLASS_ONLY = "CLASS_ONLY";
    private static final String RESTRICTED_TO_INTERFACE_ONLY = "INTERFACE_ONLY";
    private static final String RESTRICTED_TO_ENUM_ONLY = "ENUM_ONLY";
    
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
    
    public AnnotationDefinition(IConfigurationElement configurationElement, String category) {
        this.category = category;
        this.annotationClassName = AnnotationsManager.getAttributeValue(configurationElement, ATT_CLASS);
        this.name = AnnotationsManager.getAttributeValue(configurationElement, ATT_NAME);
        this.restictedTo = AnnotationsManager.getAttributeValue(configurationElement, 
                ATT_RESTRICTED_TO);
        this.classOnly = restictedTo.equals(RESTRICTED_TO_CLASS_ONLY);
        this.interfaceOnly = restictedTo.equals(RESTRICTED_TO_INTERFACE_ONLY);
        this.enumOnly = restictedTo.equals(RESTRICTED_TO_ENUM_ONLY);
    }
        
    public String getName() {
        return name;
    }
    
    public String getCategory() {
        return category;
    }
    
    public String getAnnotationClassName() {
        return annotationClassName;
    }
    
    public boolean isClassOnly() {
        return classOnly;
    }
    
    public boolean isInterfaceOnly() {
        return interfaceOnly;
    }
    
    public boolean isEnumOnly() {
        return enumOnly;
    }
    
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
    
    public List<ElementType> getTargets() {
        if (targets == null) {
        	targets = Collections.emptyList();
        	
            Class<? extends java.lang.annotation.Annotation> annotation = getAnnotationClass();
            if (annotation != null) {
            	Target target = annotation.getAnnotation(Target.class);
            	if (target != null) {
            		targets = Arrays.asList(target.value());
            	} 
            }
        }
        return targets;
    }
    
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
}
