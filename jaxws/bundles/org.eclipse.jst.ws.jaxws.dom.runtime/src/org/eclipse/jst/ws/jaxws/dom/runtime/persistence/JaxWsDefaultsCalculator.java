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
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind;
import org.eclipse.jst.ws.jaxws.utils.annotations.IAnnotation;

/**
 * Contains utility methods for calculating default values of some attributes/properties in 
 * annotations and DOM objects.
 * 
 * @author Georgi Vachkov
 */
public class JaxWsDefaultsCalculator 
{
	private static final String ARG = "arg"; //$NON-NLS-1$
	private static final String RETURN = "return"; //$NON-NLS-1$
	private static final String RESPONSE = "Response"; //$NON-NLS-1$
	
	/**
	 * Defines the SOAPBindingStyle. If there is a value for style attribute in <code>sbAnnotation</code> returns that value
	 * otherwise returns the default value for this attribute.
	 * @param sbAnnotation
	 * @return the style
	 */
	public SOAPBindingStyle defineSBStyle(final IAnnotation<? extends IJavaElement> sbAnnotation)
	{
		final String value = sbAnnotation!=null ? sbAnnotation.getPropertyValue(SBAnnotationFeatures.STYLE_ATTRIBUTE) : null;
		if (value!=null && SBAnnotationFeatures.SB_STYLE_RPC.endsWith(value.trim())) {
			return SOAPBindingStyle.RPC;
		}
		
		return SOAPBindingStyle.DOCUMENT;
	}
	
	/**
	 * Defines the SOAPBindingUse. If there is a value for use attribute in <code>sbAnnotation</code> returns that value
	 * otherwise returns the default value for this attribute.
	 * @param sbAnnotation
	 * @return the style
	 */
	public SOAPBindingUse defineSBUse(final IAnnotation<? extends IJavaElement> sbAnnotation) {
		final String value = sbAnnotation!=null ? sbAnnotation.getPropertyValue(SBAnnotationFeatures.USE_ATTRIBUTE) : null;
		if(value!=null && SBAnnotationFeatures.SB_USE_ENCODED.endsWith(value.trim())) {
			return SOAPBindingUse.ENCODED;
		}
		
		return SOAPBindingUse.LITERAL;
	}
	
	/**
	 * Defines the SOAPBindingUse. If there is a value for parameterStyle attribute in <code>sbAnnotation</code> 
	 * returns that value otherwise returns the default value for this attribute.
	 * @param sbAnnotation
	 * @return the style
	 */	
	public SOAPBindingParameterStyle defineSBParameterStyle(final IAnnotation<? extends IJavaElement> sbAnnotation)
	{
		final String value = sbAnnotation!=null ? sbAnnotation.getPropertyValue(SBAnnotationFeatures.PARAMETER_STYLE_ATTRIBUTE) : null;
		if (value!=null && SBAnnotationFeatures.SB_PARAMETER_STYLE_BARE.endsWith(value.trim())) {
			return SOAPBindingParameterStyle.BARE;
		}
		
		return SOAPBindingParameterStyle.WRAPPED;		
	}	
	
	/**
	 * Defines the default operation parameter name according to JSR-181 point 4.4 - WebParam name
	 * attribute  
	 * @param webMethod the method holding the parameter
	 * @param paramPosition the parameter position in method arguments list starting with 0
	 * @return calculated name
	 */
	public String calcWebParamDefaultName(final IWebMethod webMethod, final int paramPosition)
	{
		if(webMethod.getSoapBindingStyle()==SOAPBindingStyle.DOCUMENT &&
		   webMethod.getSoapBindingParameterStyle()==SOAPBindingParameterStyle.BARE) 
		{
			return webMethod.getName();
		}
		
		return ARG + paramPosition; 
	}
	
	/**
	 * Defines the default operation parameter name according to JSR-181 point 4.4 - WebParam name
	 * attribute 
	 * @param webParam
	 * @return
	 */
	public String calcWebParamDefaultName(final IWebParam webParam) 
	{
		final IWebMethod webMethod = (IWebMethod)webParam.eContainer();
		return calcWebParamDefaultName(webMethod, getParamPosition(webMethod, webParam));		
	}
	
	/**
	 * Calculates the position of <code>param</code> in <code>method</code> signature starting from 0
	 * @param webMethod
	 * @param param
	 * @return the param position
	 * @throws IllegalArgumentException in case <code>webMethod</code> does not contain <code>param</code>
	 */
	protected int getParamPosition(final IWebMethod webMethod, final IWebParam param)
	{
		int position = 0;
		for (IWebParam webParam : webMethod.getParameters()) 
		{
			if (webParam.getImplementation().equals("return")) {//$NON-NLS-1$
				continue;
			}
			
			if (webParam==param) {
				return position;
			}
			
			position++;
		}
		
		throw new IllegalStateException("Parameter[" + param.getImplementation() + "] was not found in method [" + webMethod.getImplementation() + "]");//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}
	
	/**
	 * Defines the default operation parameter name according to JSR-181 point 4.4 - WebParam target NS.
	 * @param webMethod the method holding the parameter
	 * @param webParam
	 * @return calculated NS
	 */
	public String calcWebParamDefaultTargetNS(final IWebMethod webMethod, final IWebParam webParam)
	{
		if(webMethod.getSoapBindingStyle()==SOAPBindingStyle.DOCUMENT &&
		   webMethod.getSoapBindingParameterStyle()==SOAPBindingParameterStyle.WRAPPED &&
		   !webParam.isHeader()) 
		{
			return "";//$NON-NLS-1$
		}
		
		return ((IServiceEndpointInterface)webMethod.eContainer()).getTargetNamespace();
	}
	
	/**
	 * @return the default kind for web parameters - returns {@link WebParamKind#IN}
	 */
	public WebParamKind getDefaultParamKind() {
		return WebParamKind.IN;		
	}
	
	/**
	 * @return false because the default value for WebParam(header) is false
	 */
	public boolean getDefaultParamInHedaer() {
		return false;
	}
	
	/**
	 * Defines the default operation parameter name according to JSR-181 point 4.5 - WebResult name.
	 * @param webMethod the method holding the parameter
	 * @return calculated name
	 */	
	public String calcWebResultDefaultName(final IWebMethod webMethod) 
	{
		if(webMethod.getSoapBindingStyle()==SOAPBindingStyle.DOCUMENT && 
		   webMethod.getSoapBindingParameterStyle()==SOAPBindingParameterStyle.BARE)
		{
			return webMethod.getName() + RESPONSE; 
		}
		
		return RETURN;
	}
}
