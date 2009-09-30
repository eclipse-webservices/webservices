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

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Dom2ResourceMapper;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.EndpointTypeValidator;

/**
 * Checks the endpoint class if it is capable to be used as service implementation bean
 * according to jsr-181 section 3.1
 * 
 * @author Georgi Vachkov
 */
public class EndpointCorrectConstraint  extends AbstractValidationConstraint
{
	private final EndpointTypeValidator validator;
	
	/**
	 * Constructor - no specific processing here 
	 */
	public EndpointCorrectConstraint() 
	{
		super(new WsConstraintDescriptor());
		validator = new EndpointTypeValidator();
	}

	@Override
	protected IStatus doValidate(IValidationContext ctx) throws JavaModelException 
	{
		final IWebService ws = (IWebService) ctx.getTarget();		
		final IType wsType = Dom2ResourceMapper.INSTANCE.findType(ws);
		if (wsType==null) {
			return createOkStatus(ws);
		}
		
		final IStatus status = validator.validate(wsType);
		if (!status.isOK()) {
			return createStatus(ws, status.getMessage(), WS_ANNOTATION, null);
		}
		
		return createOkStatus(ws);
	}
}
