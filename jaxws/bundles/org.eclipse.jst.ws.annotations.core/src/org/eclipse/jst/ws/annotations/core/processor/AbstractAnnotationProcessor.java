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
package org.eclipse.jst.ws.annotations.core.processor;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;

/**
 * Abstract base class for processors contributed to the
 * <code>org.eclipse.jst.ws.annotations.core.annotationProcessor</code> extension point.
 *
 * <p>
 * <strong>Provisional API:</strong> This class/interface is part of an interim API that is still under 
 * development and expected to change significantly before reaching stability. It is being made available at 
 * this early stage to solicit feedback from pioneering adopters on the understanding that any code that uses 
 * this API will almost certainly be broken (repeatedly) as the API evolves.
 * </p>
 * @author sclarke
 * 
 */
public abstract class AbstractAnnotationProcessor implements AnnotationProcessor {
    protected AnnotationProcessorEnvironment environment;
    
    public void setAnnotationProcessorEnvironment(AnnotationProcessorEnvironment environment) {
        this.environment = environment;
    }
    
    public abstract void process();

}
