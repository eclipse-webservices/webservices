/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap;


import org.eclipse.wst.wsdl.ExtensibilityElement;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Address</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.SOAPAddress#getLocationURI <em>Location URI</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPAddress()
 * @model superTypes="org.eclipse.wst.wsdl.ExtensibilityElement org.eclipse.wst.wsdl.binding.soap.ISOAPAddress"
 * @generated
 */
public interface SOAPAddress extends ExtensibilityElement, javax.wsdl.extensions.soap.SOAPAddress
{

  /**
   * Returns the value of the '<em><b>Location URI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Location URI</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Location URI</em>' attribute.
   * @see #setLocationURI(String)
   * @see org.eclipse.wst.wsdl.binding.soap.SOAPPackage#getSOAPAddress_LocationURI()
   * @model
   * @generated
   */
  String getLocationURI();

  /**
   * Sets the value of the '{@link org.eclipse.wst.wsdl.binding.soap.SOAPAddress#getLocationURI <em>Location URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Location URI</em>' attribute.
   * @see #getLocationURI()
   * @generated
   */
  void setLocationURI(String value);

} // SOAPAddress
