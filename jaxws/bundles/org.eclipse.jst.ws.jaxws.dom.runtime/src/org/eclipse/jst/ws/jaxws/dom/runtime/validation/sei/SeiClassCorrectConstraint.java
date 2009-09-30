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

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Dom2ResourceMapper;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.SeiValidator;

/**
 * Checks the SEI underling interface if is capable to be used as SEI according to  
 * jsr-181 point 3.2
 * 
 * @author Georgi Vachkov
 */
public class SeiClassCorrectConstraint extends AbstractValidationConstraint
{
	/**
	 * Constructor - no specific processing here 
	 */
	public SeiClassCorrectConstraint() 
	{
		super(new SeiConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(final IValidationContext ctx) throws JavaModelException
	{
		final SeiValidator validator = new SeiValidator();
		final IServiceEndpointInterface sei = (IServiceEndpointInterface)ctx.getTarget();
		if (sei.isImplicit()) {
			return createOkStatus(sei);
		}
		
		final IType seiType = Dom2ResourceMapper.INSTANCE.findType(sei);
		if (seiType == null) {
			return createOkStatus(sei);
		}
		
		final IStatus status = validator.validate(seiType);
		if (!status.isOK()) {
			return createStatus(sei, status.getMessage(), WS_ANNOTATION, null);
		}
		
		return createOkStatus(sei);
	}
}
