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

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.PORT_NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.utils.WsdlNamesValidator;

/**
 * Checks if portName attribute of WebService annotation is valid NCName 
 * 
 * @author Georgi Vachkov
 */
public class PortNameIsNCNameConstraint extends AbstractValidationConstraint
{
	/**
	 * Constructor - no specific processing here 
	 */
	public PortNameIsNCNameConstraint() {
		super(new WsConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(IValidationContext ctx) throws JavaModelException 
	{
		final IWebService ws = (IWebService)ctx.getTarget();
		final IStatus status = WsdlNamesValidator.validateNCName2(PORT_NAME_ATTRIBUTE, ws.getPortName());
		if (!status.isOK()) {
			return createStatus(ws, status.getMessage(), WS_ANNOTATION, PORT_NAME_ATTRIBUTE);
		}	
		
		return createOkStatus(ws);
	}

}
