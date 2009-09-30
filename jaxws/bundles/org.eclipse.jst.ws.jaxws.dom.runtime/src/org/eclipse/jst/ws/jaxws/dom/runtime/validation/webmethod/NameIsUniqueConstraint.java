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
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WMAnnotationFeatures.WM_NAME_ATTRIBUTE;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;

/**
 * Checks if the <b>operationName</b> attribute is unique in the scope of
 * SEI methods. According to WSDL specification operations should have different
 * names e.g. overloading is not supported. 
 * 
 * @author Georgi Vachkov
 */
public class NameIsUniqueConstraint extends AbstractValidationConstraint
{
	/**
	 * Constructor - no specific processing here 
	 */
	public NameIsUniqueConstraint() {
		super(new WmConstraintDescriptor());
	}
	
	@Override
	protected IStatus doValidate(final IValidationContext ctx) throws JavaModelException
	{
		final IWebMethod webMethod = (IWebMethod)ctx.getTarget();
		if (!webMethod.isExcluded() && !isNameUnique(webMethod)) 
		{
			final String msg = ValidationMessages.bind(ValidationMessages.WebMethodValidation_NameExists, webMethod.getName());
			return createStatus(webMethod, msg, WM_ANNOTATION, WM_NAME_ATTRIBUTE);
		}	
		
		return createOkStatus(webMethod);
	}
	
	protected boolean isNameUnique(final IWebMethod webMethod) 
	{
		final IServiceEndpointInterface sei = (IServiceEndpointInterface)webMethod.eContainer();
		for(IWebMethod wm : sei.getWebMethods()) 
		{
			if (wm==webMethod || wm.isExcluded()) {
				continue;
			}
			
			if (wm.getName().equals(webMethod.getName())) {
				return false;
			}
		}
		
		return true;
	}
}
