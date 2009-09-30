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

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.TARGET_NAMESPACE_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.WP_ANNOTATION;

import java.net.URI;
import java.net.URISyntaxException;

import javax.jws.WebParam;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.internal.plugin.JaxWsDomRuntimeMessages;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;

/**
 * Check if targetNamespace attribute of {@link WebParam} annotation is correct URI.  
 * 
 * @author Georgi Vachkov
 */
public class TargetNsValidUriConstraint  extends AbstractValidationConstraint
{
	private static final String RETURN = "return";//$NON-NLS-1$
	
	/**
	 * Constructor - no specific processing here 
	 */
	public TargetNsValidUriConstraint() {
		super(new WpConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(IValidationContext ctx) throws JavaModelException 
	{
		final IWebParam wp = (IWebParam)ctx.getTarget(); 
		if (RETURN.equals(wp.getImplementation())) {
			return createOkStatus(wp);
		}
		
		try {
			new URI(wp.getTargetNamespace());
		} catch (URISyntaxException e) { // $JL-EXC$
			return createStatus(wp, JaxWsDomRuntimeMessages.bind(
											JaxWsDomRuntimeMessages.TargetNsValidUriConstraint_URI_SYNTAX_ERROR,
					new Object[] {e.getIndex(), e.getInput(), e.getReason()}), WP_ANNOTATION, TARGET_NAMESPACE_ATTRIBUTE);
		}
		
		return createOkStatus(wp);
	}
}