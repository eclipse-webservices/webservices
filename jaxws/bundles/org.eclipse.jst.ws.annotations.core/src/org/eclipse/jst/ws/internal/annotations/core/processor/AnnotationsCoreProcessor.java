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
package org.eclipse.jst.ws.internal.annotations.core.processor;

import java.util.List;

import com.sun.mirror.apt.AnnotationProcessor;
/**
 * 
 * @author sclarke
 *
 */
public class AnnotationsCoreProcessor implements AnnotationProcessor {

    private List<AnnotationProcessor> annotationProcessors;
    
    public AnnotationsCoreProcessor(List<AnnotationProcessor> annotationProcessors) {
        this.annotationProcessors = annotationProcessors;
    }
    
    public void process() {
        for (AnnotationProcessor annotationProcessor : annotationProcessors) {
            annotationProcessor.process();
        }
    }
}
