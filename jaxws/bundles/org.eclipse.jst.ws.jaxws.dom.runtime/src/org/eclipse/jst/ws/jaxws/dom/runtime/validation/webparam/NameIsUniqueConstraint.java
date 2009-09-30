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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.webparam;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.WP_ANNOTATION;

import javax.jws.WebParam;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;

/**
 * Check if name attribute of {@link WebParam} annotation is unique in the scope of
 * the method.
 * 
 * @author Georgi Vachkov
 */
public class NameIsUniqueConstraint extends AbstractValidationConstraint
{
	private static final String RETURN = "return";//$NON-NLS-1$
	
	/**
	 * Constructor - no specific processing here 
	 */
	public NameIsUniqueConstraint() {
		super(new WpConstraintDescriptor());
	}
	
	@Override
	protected IStatus doValidate(final IValidationContext ctx) throws JavaModelException
	{
		final IWebParam webParam = (IWebParam)ctx.getTarget();
		if (RETURN.equals(webParam.getImplementation())) {
			return createOkStatus(webParam);
		}
			
		if (!isNameUnique(webParam)) 
		{
			final String msg = ValidationMessages.bind(ValidationMessages.WebParamValidation_NameExists, webParam.getName());
			return createStatus(webParam, msg, WP_ANNOTATION, NAME_ATTRIBUTE);
		}	
		
		return createOkStatus(webParam);
	}
	
	protected boolean isNameUnique(final IWebParam webParam) 
	{
		final IWebMethod webMethod = (IWebMethod)webParam.eContainer();
		for (IWebParam wp : webMethod.getParameters()) 
		{
			if (wp==webParam) {
				continue;
			}
			
			if (wp.getName().equals(webParam.getName())) {
				return false;
			}
		}
		
		return true;
	}
}
