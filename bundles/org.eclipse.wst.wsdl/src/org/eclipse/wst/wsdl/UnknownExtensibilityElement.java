/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unknown Extensibility Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.UnknownExtensibilityElement#getParent <em>Parent</em>}</li>
 *   <li>{@link org.eclipse.wsdl.UnknownExtensibilityElement#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wsdl.WSDLPackage#getUnknownExtensibilityElement()
 * @model 
 * @generated
 */
public interface UnknownExtensibilityElement extends ExtensibilityElement{
  /**
   * Returns the value of the '<em><b>Parent</b></em>' reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.wsdl.UnknownExtensibilityElement#getChildren <em>Children</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parent</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parent</em>' reference.
   * @see #setParent(UnknownExtensibilityElement)
   * @see org.eclipse.wsdl.WSDLPackage#getUnknownExtensibilityElement_Parent()
   * @see org.eclipse.wsdl.UnknownExtensibilityElement#getChildren
   * @model opposite="children"
   * @generated
   */
  UnknownExtensibilityElement getParent();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.UnknownExtensibilityElement#getParent <em>Parent</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Parent</em>' reference.
   * @see #getParent()
   * @generated
   */
  void setParent(UnknownExtensibilityElement value);

  /**
   * Returns the value of the '<em><b>Children</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.wsdl.UnknownExtensibilityElement}.
   * It is bidirectional and its opposite is '{@link org.eclipse.wsdl.UnknownExtensibilityElement#getParent <em>Parent</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Children</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Children</em>' reference list.
   * @see org.eclipse.wsdl.WSDLPackage#getUnknownExtensibilityElement_Children()
   * @see org.eclipse.wsdl.UnknownExtensibilityElement#getParent
   * @model type="org.eclipse.wsdl.UnknownExtensibilityElement" opposite="parent"
   * @generated
   */
  EList getChildren();

} // UnknownExtensibilityElement
