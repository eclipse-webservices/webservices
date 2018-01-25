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
package org.eclipse.jst.ws.jaxws.dom.runtime.api;

import org.eclipse.emf.common.util.EList;


/**
 * @model
 * @author Hristo Sabev
 *
 */
public interface IServiceEndpointInterface extends IJavaWebServiceElement
{
	/**
	 * @model changeable="true" suppressedSetVisibility="true" required="true"
	 * @return
	 */
	public boolean isImplicit();

	/**
	 * @model changeable="true" suppressedSetVisibility="true" required="true" containment="false" many="true" type="IWebService" opposite="serviceEndpoint"
	 * @return
	 */
	public EList<IWebService> getImplementingWebServices();
	
	/**
	 * @model changeable="true" suppressedSetVisibility="true" required="true" containment="true" many="true" type="IWebMethod"
	 * @return
	 */
	public EList<IWebMethod> getWebMethods();
	
	/**
	 * @model changeable="true" required="true"
	 */
	public String getTargetNamespace();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getTargetNamespace <em>Target Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Namespace</em>' attribute.
	 * @see #getTargetNamespace()
	 * @generated
	 */
	void setTargetNamespace(String value);

	/**
	 * Returns the value of the '<em><b>Soap Binding Style</b></em>' attribute.
	 * The default value is <code>"DOCUMENT"</code>.
	 * The literals are from the enumeration {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Soap Binding Style</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Soap Binding Style</em>' attribute.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle
	 * @see #setSoapBindingStyle(SOAPBindingStyle)
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage#getIServiceEndpointInterface_SoapBindingStyle()
	 * @model default="DOCUMENT" required="true"
	 * @generated
	 */
	SOAPBindingStyle getSoapBindingStyle();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getSoapBindingStyle <em>Soap Binding Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Soap Binding Style</em>' attribute.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle
	 * @see #getSoapBindingStyle()
	 * @generated
	 */
	void setSoapBindingStyle(SOAPBindingStyle value);

	/**
	 * Returns the value of the '<em><b>Soap Binding Use</b></em>' attribute.
	 * The default value is <code>"LITERAL"</code>.
	 * The literals are from the enumeration {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Soap Binding Use</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Soap Binding Use</em>' attribute.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse
	 * @see #setSoapBindingUse(SOAPBindingUse)
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage#getIServiceEndpointInterface_SoapBindingUse()
	 * @model default="LITERAL" required="true"
	 * @generated
	 */
	SOAPBindingUse getSoapBindingUse();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getSoapBindingUse <em>Soap Binding Use</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Soap Binding Use</em>' attribute.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse
	 * @see #getSoapBindingUse()
	 * @generated
	 */
	void setSoapBindingUse(SOAPBindingUse value);

	/**
	 * Returns the value of the '<em><b>Soap Binding Parameter Style</b></em>' attribute.
	 * The default value is <code>"WRAPPED"</code>.
	 * The literals are from the enumeration {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Soap Binding Parameter Style</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Soap Binding Parameter Style</em>' attribute.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle
	 * @see #setSoapBindingParameterStyle(SOAPBindingParameterStyle)
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage#getIServiceEndpointInterface_SoapBindingParameterStyle()
	 * @model default="WRAPPED" required="true"
	 * @generated
	 */
	SOAPBindingParameterStyle getSoapBindingParameterStyle();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface#getSoapBindingParameterStyle <em>Soap Binding Parameter Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Soap Binding Parameter Style</em>' attribute.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle
	 * @see #getSoapBindingParameterStyle()
	 * @generated
	 */
	void setSoapBindingParameterStyle(SOAPBindingParameterStyle value);
}
