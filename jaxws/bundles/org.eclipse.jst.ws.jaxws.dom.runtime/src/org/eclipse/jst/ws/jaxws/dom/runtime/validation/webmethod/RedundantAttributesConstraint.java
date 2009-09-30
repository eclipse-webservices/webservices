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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.webmethod;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WMAnnotationFeatures.WM_ANNOTATION;

import javax.jws.WebMethod;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

/**
 * Check if in {@link WebMethod} annotation where <b>exclude=true</b> no other attributes
 * are used. According to jsr-181 - point 4.2.1, exclude attribute description this is not
 * allowed.
 * 
 * @author Georgi Vachkov
 */
public class RedundantAttributesConstraint extends AbstractValidationConstraint
{
	/**
	 * Constructor - no specific processing here 
	 */
	public RedundantAttributesConstraint() {
		super(new WmConstraintDescriptor());
	}
	
	@Override
	protected IStatus doValidate(final IValidationContext ctx) throws JavaModelException
	{
		final IWebMethod webMethod = (IWebMethod)ctx.getTarget();
		final IServiceEndpointInterface sei = (IServiceEndpointInterface)webMethod.eContainer();
		
		// if the method is not excluded we do not need this check
		if (!webMethod.isExcluded() || !sei.isImplicit()) {
			return createOkStatus(webMethod);
		}

		if (containsOtherAttributes(webMethod)) {
			return createStatus(webMethod, ValidationMessages.WebMethodValidation_ExcludedHasOtherAttributes, WM_ANNOTATION, null);
		}		
		
		return createOkStatus(webMethod);
	}
	
	protected boolean containsOtherAttributes(final IWebMethod webMethod) 
	{
		final IAnnotation<? extends IJavaElement> annotation = jee5DomUtil().findAnnotation(webMethod, WM_ANNOTATION);
		if (annotation==null) {
			return false;
		}
		
		if (annotation.getParamValuePairs().size() > 1) {
			return true;
		}
		
		return false;
	}
}
