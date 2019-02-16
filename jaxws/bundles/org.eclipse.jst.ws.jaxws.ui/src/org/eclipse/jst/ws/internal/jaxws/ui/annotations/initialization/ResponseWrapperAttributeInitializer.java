/*******************************************************************************
 * Copyright (c) 2009, 2010 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.internal.jaxws.ui.annotations.initialization;

import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.OPERATION_NAME;
import static org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils.RESPONSE_SUFFIX;

import javax.jws.WebMethod;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;
import org.eclipse.jst.ws.internal.jaxws.core.utils.JAXWSUtils;
import org.eclipse.jst.ws.internal.jaxws.ui.JAXWSUIPlugin;

public class ResponseWrapperAttributeInitializer extends RequestWrapperAttributeInitializer {

    @Override
    protected String getClassName(IType type, IMethod method) {
        try {
            String methodName = method.getElementName() + RESPONSE_SUFFIX;
            return getPackageName(type) + methodName.substring(0, 1).toUpperCase()
                + methodName.substring(1) + JAXWSUtils.accountForOverloadedMethods(method);
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
        }
        return ""; //$NON-NLS-1$
    }
    
    @Override
    protected String getLocalName(IType type, IMethod method) {
        try {
            IAnnotation annotation = AnnotationUtils.getAnnotation(WebMethod.class, method);
            if (annotation != null) {
                String operationName = AnnotationUtils.getStringValue(annotation, OPERATION_NAME);
                if (operationName != null) {
                    return operationName + RESPONSE_SUFFIX;
                }
            }
            return method.getElementName() + RESPONSE_SUFFIX 
                    + JAXWSUtils.accountForOverloadedMethods(method);
        } catch (JavaModelException jme) {
            JAXWSUIPlugin.log(jme.getStatus());
        }
        return ""; //$NON-NLS-1$
    }
    
}
