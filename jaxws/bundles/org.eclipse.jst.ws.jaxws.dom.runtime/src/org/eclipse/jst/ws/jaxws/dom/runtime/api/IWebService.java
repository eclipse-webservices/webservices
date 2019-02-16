/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.api;


/**
 * @model
 * @author Hristo Sabev
 *
 */
public interface IWebService extends IJavaWebServiceElement 
{
	/**
	 * @model suppressedSetVisibility="true" opposite="implementingWebServices" type="IServiceEndpointInterface"
	 * @return
	 */
	public IServiceEndpointInterface getServiceEndpoint();
	
	/**
	 * @model changeable="true" required="true"
	 */
	public String getTargetNamespace();
	
	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService#getTargetNamespace <em>Target Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Namespace</em>' attribute.
	 * @see #getTargetNamespace()
	 * @generated
	 */
	void setTargetNamespace(String value);

	/**
	 * @model changeable="true" required="true"
	 */
	public String getPortName();
	
	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService#getPortName <em>Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Name</em>' attribute.
	 * @see #getPortName()
	 * @generated
	 */
	void setPortName(String value);

	/**
	 * @model changeable="true" suppressedSetVisibility="true"
	 */
	public String getWsdlLocation();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService#getWsdlLocation <em>Wsdl Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wsdl Location</em>' attribute.
	 * @see #getWsdlLocation()
	 * @generated
	 */
	void setWsdlLocation(String value);
}
