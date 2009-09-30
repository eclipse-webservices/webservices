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
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Jee5DomUtils;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

/**
 * Check if name attribute of {@link WebParam} annotation is required.
 * See {@link Jee5DomUtils#isNameRequired(IWebParam)} for more details.  
 * 
 * @author Georgi Vachkov
 */
public class NameIsRequiredConstraint  extends AbstractValidationConstraint
{
	private static final String RETURN = "return";//$NON-NLS-1$

	/**
	 * Constructor - no specific processing here 
	 */
	public NameIsRequiredConstraint() {
		super(new WpConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(final IValidationContext ctx) throws JavaModelException
	{
		final IWebParam webParam = (IWebParam)ctx.getTarget();
		if (RETURN.equals(webParam.getImplementation())) {
			return createOkStatus(webParam);
		}

		final IAnnotation<? extends IJavaElement> annotation = jee5DomUtil().findAnnotation(webParam, WP_ANNOTATION);
		if (annotation==null) {
			return createOkStatus(webParam);
		}
		
		if (jee5DomUtil().isNameRequired(webParam) && annotation.getPropertyValue(NAME_ATTRIBUTE)==null) {
			return createStatus(webParam, ValidationMessages.WebParamValidation_NameRequired, WP_ANNOTATION, null);
		}
		
		return createOkStatus(webParam);
	}
}
