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

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

/**
 * Checks if there is a name attribute on ednpoint that references SEI.
 * According to jsr-181 point 3.1 subpoint 5.
 * 
 * @author Georgi Vachkov
 */
public class NameRedundantConstraint extends AbstractValidationConstraint
{
	/**
	 * Constructor - no specific processing here 
	 */
	public NameRedundantConstraint() {
		super(new WsConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(final IValidationContext ctx) throws JavaModelException
	{
		final IWebService ws = (IWebService)ctx.getTarget();
		if (ws.getServiceEndpoint()!=null && ws.getServiceEndpoint().isImplicit()) {			
			return createOkStatus(ws);
		}	
		
		if (hasNameAttribute(ws)) {
			return createStatus(ws, ValidationMessages.WsValidation_NameShouldNotBeUsedWithSei, WS_ANNOTATION, NAME_ATTRIBUTE);
		}
		
		return createOkStatus(ws);
	}
	
	protected boolean hasNameAttribute(final IWebService webService)
	{
		final IAnnotation<? extends IJavaElement> annotation = findAnnotation(webService, WS_ANNOTATION);
		if (annotation==null) {
			return false;
		}
		
		return annotation.getPropertyValue(NAME_ATTRIBUTE)!=null;
	}
}
