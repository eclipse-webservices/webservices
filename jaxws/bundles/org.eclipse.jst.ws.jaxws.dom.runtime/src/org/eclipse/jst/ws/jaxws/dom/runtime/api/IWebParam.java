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


/**
 * 
 * @author Hristo Sabev
 *
 * @model
 */
public interface IWebParam extends IJavaWebServiceElement
{
	/**
	 * @model required="true" many="false" suppressedSetVisibility="true"
	 * @return
	 */
	String getTypeName();
	
	/**
	 * Returns the value of the '<em><b>Part Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Part Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Part Name</em>' attribute.
	 * @see #setPartName(String)
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage#getIWebParam_PartName()
	 * @model required="true"
	 * @generated
	 */
	String getPartName();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#getPartName <em>Part Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Part Name</em>' attribute.
	 * @see #getPartName()
	 * @generated
	 */
	void setPartName(String value);

	/**
	 * Returns the value of the '<em><b>Target Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Namespace</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Namespace</em>' attribute.
	 * @see #setTargetNamespace(String)
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage#getIWebParam_TargetNamespace()
	 * @model required="true"
	 * @generated
	 */
	String getTargetNamespace();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#getTargetNamespace <em>Target Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Namespace</em>' attribute.
	 * @see #getTargetNamespace()
	 * @generated
	 */
	void setTargetNamespace(String value);

	/**
	 * Returns the value of the '<em><b>Header</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Header</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Header</em>' attribute.
	 * @see #setHeader(boolean)
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage#getIWebParam_Header()
	 * @model default="false" required="true"
	 * @generated
	 */
	boolean isHeader();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#isHeader <em>Header</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Header</em>' attribute.
	 * @see #isHeader()
	 * @generated
	 */
	void setHeader(boolean value);

	/**
	 * @model type="WebParamKind" required="true" many="false"
	 */
	WebParamKind getKind();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(WebParamKind value);
}
