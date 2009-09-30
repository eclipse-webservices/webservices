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
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WMAnnotationFeatures.WM_EXCLUDED_ATTRIBUTE;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;

/**
 * Checks if there is a <b>exclude=true</b> attribute in WebMetod annotation in SEI.
 * According to jsr-181 section 4.2.1 attribute <b>exclude</b> this is not allowed.
 * 
 * @author Georgi Vachkov
 */
public class MethodNotExcludedInSeiConstraint extends AbstractValidationConstraint
{
	/**
	 * Constructor - no specific processing here 
	 */
	public MethodNotExcludedInSeiConstraint() {
		super(new WmConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(final IValidationContext ctx) throws JavaModelException
	{
		final IWebMethod wm = (IWebMethod)ctx.getTarget();
		final IServiceEndpointInterface sei = (IServiceEndpointInterface)wm.eContainer();
		
		if (wm.isExcluded() && !sei.isImplicit()) 
		{
			return createStatus(wm, ValidationMessages.WebMethodValidation_MethodShouldNotBeExcluded, WM_ANNOTATION, WM_EXCLUDED_ATTRIBUTE);
		}
		
		return createOkStatus(wm);
	}
}
