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

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.ENDPOINT_INTERFACE_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.PORT_NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.SERVICE_NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WSDL_LOCATION_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WSAnnotationFeatures.WS_ANNOTATION;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.messages.ValidationMessages;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

/**
 * Checks if WebService annotation set on SEI contains portName, serviceName or endpointInterface attributes.
 * According to jst-181, point 3.2 sub-point 6 this is not allowed.  
 * 
 * @author Georgi Vachkov
 */
public class RedundantAttributesConstraint extends AbstractValidationConstraint
{
	private static final String [] REDUNDANT_ATTRIBUTES = {PORT_NAME_ATTRIBUTE, SERVICE_NAME_ATTRIBUTE, ENDPOINT_INTERFACE_ATTRIBUTE, WSDL_LOCATION_ATTRIBUTE}; 
	
	/**
	 * Constructor - no specific processing here 
	 */
	public RedundantAttributesConstraint() {
		super(new SeiConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(final IValidationContext ctx) throws JavaModelException
	{
		final IServiceEndpointInterface sei = (IServiceEndpointInterface)ctx.getTarget();
		if (sei.isImplicit()) {
			return createOkStatus(sei);
		}
		
		if (containsRedundantAttribute(sei)) {
			return createStatus(sei, ValidationMessages.SeiValidation_RedundantAttributes, WS_ANNOTATION, null);
		}	

		return createOkStatus(sei);
	}
	
	protected boolean containsRedundantAttribute(final IServiceEndpointInterface sei)
	{
		final IAnnotation<? extends IJavaElement> annotation = findAnnotation(sei, WS_ANNOTATION);
		if (annotation == null) {
			return false;
		}
		
		for (String redundantAttribute : REDUNDANT_ATTRIBUTES) {
			if (annotation.getPropertyValue(redundantAttribute)!=null) {
				return true;
			}
		}
		
		return false;
	}
}