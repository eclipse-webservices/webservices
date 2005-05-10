/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Header Base</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getUse <em>Use</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getNamespaceURI <em>Namespace URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getEncodingStyles <em>Encoding Styles</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getMessage <em>Message</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getPart <em>Part</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase()
 * @model 
 * @generated
 */
public interface SOAPHeaderBase extends ExtensibilityElement{
  /**
   * Returns the value of the '<em><b>Use</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Use</em>' attribute.
   * @see #setUse(String)
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_Use()
   * @model 
   * @generated
   */
	String getUse();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getUse <em>Use</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Use</em>' attribute.
   * @see #getUse()
   * @generated
   */
	void setUse(String value);

  /**
   * Returns the value of the '<em><b>Namespace URI</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Namespace URI</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Namespace URI</em>' attribute.
   * @see #setNamespaceURI(String)
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_NamespaceURI()
   * @model 
   * @generated
   */
	String getNamespaceURI();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getNamespaceURI <em>Namespace URI</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Namespace URI</em>' attribute.
   * @see #getNamespaceURI()
   * @generated
   */
	void setNamespaceURI(String value);

  /**
   * Returns the value of the '<em><b>Encoding Styles</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Encoding Styles</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Encoding Styles</em>' attribute list.
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_EncodingStyles()
   * @model type="java.lang.String" dataType="org.eclipse.wst.wsdl.binding.soap.IString"
   * @generated
   */
	EList getEncodingStyles();

  /**
   * Returns the value of the '<em><b>Message</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Message</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Message</em>' reference.
   * @see #setMessage(Message)
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_Message()
   * @model required="true"
   * @generated
   */
	Message getMessage();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getMessage <em>Message</em>}' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Message</em>' reference.
   * @see #getMessage()
   * @generated
   */
	void setMessage(Message value);

  /**
   * Returns the value of the '<em><b>Part</b></em>' reference.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Part</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Part</em>' reference.
   * @see #setPart(Part)
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPHeaderBase_Part()
   * @model required="true"
   * @generated
   */
	Part getPart();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase#getPart <em>Part</em>}' reference.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Part</em>' reference.
   * @see #getPart()
   * @generated
   */
	void setPart(Part value);

} // SOAPHeaderBase
