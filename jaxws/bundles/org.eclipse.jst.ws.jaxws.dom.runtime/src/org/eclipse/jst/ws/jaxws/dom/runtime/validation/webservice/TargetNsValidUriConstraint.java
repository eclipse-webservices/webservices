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

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.TARGET_NAMESPACE_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimeMessages;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;

/**
 * Checks if targetNamespace attribute of WebService annotation is valid URI
 * 
 * @author Georgi Vachkov
 */
public class TargetNsValidUriConstraint  extends AbstractValidationConstraint
{
	/**
	 * Constructor - no specific processing here 
	 */
	public TargetNsValidUriConstraint() {
		super(new WsConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(IValidationContext ctx) throws JavaModelException 
	{
		final IWebService ws = (IWebService)ctx.getTarget(); 
		try {
			new URI(ws.getTargetNamespace());
		} catch (URISyntaxException e) { // $JL-EXC$
			return createStatus(ws, JaxWsDomRuntimeMessages.bind(
											JaxWsDomRuntimeMessages.TargetNsValidUriConstraint_URI_SYNTAX_ERROR,
					new Object[] {e.getIndex(), e.getInput(), e.getReason()})
					, WS_ANNOTATION, TARGET_NAMESPACE_ATTRIBUTE);
		}
		
		return createOkStatus(ws);
	}
}
