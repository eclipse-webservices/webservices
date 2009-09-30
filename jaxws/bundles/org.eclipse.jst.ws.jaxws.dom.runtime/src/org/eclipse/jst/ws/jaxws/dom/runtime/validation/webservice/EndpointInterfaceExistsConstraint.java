/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.webservice;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.ENDPOINT_INTERFACE_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.AnnotationAdapterFactory;
import org.eclipse.jst.ws.jaxws.dom.runtime.persistence.annotation.IAnnotationAdapter;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

/**
 * Checks if referenced by endpoint SEI exists in the project. 
 * 
 * @author Georgi Vachkov
 */
public class EndpointInterfaceExistsConstraint  extends AbstractValidationConstraint
{
	/**
	 * Constructor - no specific processing here 
	 */
	public EndpointInterfaceExistsConstraint() {
		super(new WsConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(IValidationContext ctx) throws JavaModelException 
	{
		final IWebService ws = (IWebService)ctx.getTarget();  
		final IAnnotationAdapter adapter = (IAnnotationAdapter)AnnotationAdapterFactory.INSTANCE.adapt(ws, IAnnotationAdapter.class);
		final IAnnotation<? extends IJavaElement> annotation = adapter.getAnnotation(WS_ANNOTATION);
		if (annotation == null) {
			return createOkStatus(ws);
		}

		final String seiFQName = annotation.getPropertyValue(ENDPOINT_INTERFACE_ATTRIBUTE);
		if (seiFQName == null) {
			return createOkStatus(ws);
		}

		if (ws.getServiceEndpoint()==null) {
			final String msg = ValidationMessages.bind(ValidationMessages.WsValidation_SeiDoesNotExists, seiFQName);
			return createStatus(ws, msg, WS_ANNOTATION, ENDPOINT_INTERFACE_ATTRIBUTE);
		}
		
		return createOkStatus(ws);
	}
}
