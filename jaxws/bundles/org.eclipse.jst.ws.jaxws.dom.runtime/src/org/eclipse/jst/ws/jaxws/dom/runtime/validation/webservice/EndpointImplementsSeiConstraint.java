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

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.ENDPOINT_INTERFACE_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Dom2ResourceMapper;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.provider.SeiImplementationValidator;

/**
 * Validates if the WebService implementation bean implements methods from
 * referenced service endpoint interface 
 * 
 * @author Georgi Vachkov
 */
public class EndpointImplementsSeiConstraint extends AbstractValidationConstraint
{
	/**
	 * Constructor - no specific processing here 
	 */
	public EndpointImplementsSeiConstraint() 
	{
		super(new WsConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(IValidationContext ctx) throws JavaModelException 
	{
		final IWebService ws = (IWebService) ctx.getTarget();
		
		if (ws.getServiceEndpoint()==null || ws.getServiceEndpoint().isImplicit()) {
			return createOkStatus(ws);
		}
			
		final IType wsType = findType(ws);
		final IType seiType = findType(ws.getServiceEndpoint());
		
		if (wsType==null || seiType==null) {
			return createOkStatus(ws);
		}
		
		final SeiImplementationValidator validator = new SeiImplementationValidator(seiType);		
		final IStatus status = validator.validate(wsType);
		if (!status.isOK()) {
			return createStatus(ws, status.getMessage(), WS_ANNOTATION, ENDPOINT_INTERFACE_ATTRIBUTE);
		}
		
		return createOkStatus(ws);
	}
	
	protected IType findType(final EObject eObject) throws JavaModelException {
		return Dom2ResourceMapper.INSTANCE.findType(eObject);
	}
}
