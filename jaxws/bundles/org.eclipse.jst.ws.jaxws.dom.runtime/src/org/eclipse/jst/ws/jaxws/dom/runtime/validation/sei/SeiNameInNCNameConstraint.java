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
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.sei;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.utils.WsdlNamesValidator;

/**
 * Checks if 'name' attribute is valid NCName  
 * 
 * @author Georgi Vachkov
 */
public class SeiNameInNCNameConstraint extends AbstractValidationConstraint
{
	public SeiNameInNCNameConstraint() {
		super(new SeiConstraintDescriptor());
	}

	protected IStatus doValidate(final IValidationContext ctx) throws JavaModelException
	{
		final IServiceEndpointInterface sei = (IServiceEndpointInterface)ctx.getTarget();
		final IStatus status = WsdlNamesValidator.validateNCName2(NAME_ATTRIBUTE, sei.getName());
		if (!status.isOK()) {
			return createStatus(sei, status.getMessage(), WS_ANNOTATION, NAME_ATTRIBUTE);
		}	
		
		return createOkStatus(sei);
	}
}

