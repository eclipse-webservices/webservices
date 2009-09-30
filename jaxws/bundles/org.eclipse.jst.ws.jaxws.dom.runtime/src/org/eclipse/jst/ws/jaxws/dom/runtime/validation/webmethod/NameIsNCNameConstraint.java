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
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.utils.WsdlNamesValidator;

/**
 * Checks if <b>operationName</b> is valid NCName.
 * 
 * @author Georgi Vachkov
 */
public class NameIsNCNameConstraint extends AbstractValidationConstraint
{
	/**
	 * Constructor - no specific processing here 
	 */
	public NameIsNCNameConstraint() {
		super(new WmConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(final IValidationContext ctx) throws JavaModelException
	{
		final IWebMethod wm = (IWebMethod)ctx.getTarget();
		if(!wm.isExcluded()) 
		{
			final IStatus status = WsdlNamesValidator.validateNCName2(WM_NAME_ATTRIBUTE, wm.getName());
			if (!status.isOK()) {
				return createStatus(wm, status.getMessage(), WM_ANNOTATION, WM_NAME_ATTRIBUTE);
			}
		}
		
		return createOkStatus(wm);
	}
}

