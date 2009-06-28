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
package org.eclipse.jst.ws.internal.jaxws.core.annotations.validation;

import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import org.eclipse.jst.ws.annotations.core.processor.AbstractAnnotationProcessor;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;

import com.sun.mirror.declaration.AnnotationMirror;

/**
 * 
 * @author sclarke
 *
 */
public abstract class AbstractJAXWSAnnotationProcessor extends AbstractAnnotationProcessor {

    protected static final String CLASS_NAME = "className"; //$NON-NLS-1$
    
    protected static final String ENDPOINT_INTERFACE = "endpointInterface"; //$NON-NLS-1$
    protected static final String EXCLUDE = "exclude"; //$NON-NLS-1$

    protected static final String FAULT_BEAN = "faultBean"; //$NON-NLS-1$
    protected static final String FINALIZE = "finalize"; //$NON-NLS-1$
    protected static final String HEADER = "header"; //$NON-NLS-1$

    protected static final String LOCAL_NAME = "localName"; //$NON-NLS-1$
    protected static final String MODE = "mode"; //$NON-NLS-1$
    protected static final String NAME = "name"; //$NON-NLS-1$    
    protected static final String OPERATION_NAME = "operationName"; //$NON-NLS-1$

    protected static final String PARAMETER_STYLE = "parameterStyle"; //$NON-NLS-1$
    protected static final String PORT_NAME = "portName"; //$NON-NLS-1$
    protected static final String SERVICE_NAME = "serviceName"; //$NON-NLS-1$

    protected static final String STYLE = "style"; //$NON-NLS-1$
    protected static final String TARGET_NAMESPACE = "targetNamespace"; //$NON-NLS-1$
    protected static final String TRUE = "true"; //$NON-NLS-1$

    protected static final String USE = "use"; //$NON-NLS-1$

    protected static final String VALUE = "value"; //$NON-NLS-1$
    protected static final String TYPE = "type"; //$NON-NLS-1$
    
    public abstract void process();

    protected boolean isDocumentBare(AnnotationMirror mirror) {
        String style = AnnotationUtils.getStringValue(mirror, STYLE);
        String use = AnnotationUtils.getStringValue(mirror, USE);
        String parameterStyle = AnnotationUtils.getStringValue(mirror, PARAMETER_STYLE);

        return (style == null || style.equals(Style.DOCUMENT.name()))
                && (use == null || use.equals(Use.LITERAL.name()))
                && (parameterStyle != null && parameterStyle.equals(ParameterStyle.BARE.name()));
    }
    
    protected boolean isDocumentWrapped(AnnotationMirror mirror) {
        String style = AnnotationUtils.getStringValue(mirror, STYLE);
        String use = AnnotationUtils.getStringValue(mirror, USE);
        String parameterStyle = AnnotationUtils.getStringValue(mirror, PARAMETER_STYLE);

        return (style == null || style.equals(Style.DOCUMENT.name()))
                && (use == null || use.equals(Use.LITERAL.name()))
                && (parameterStyle == null || parameterStyle.equals(ParameterStyle.WRAPPED.name()));
    }
}
