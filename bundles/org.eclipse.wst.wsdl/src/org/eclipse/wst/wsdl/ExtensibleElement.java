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

import java.util.List;

import javax.wsdl.extensions.ExtensibilityElement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extensible Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *  WSDL allows elements representing a specific technology (referred to here as extensibility elements) under various elements defined by WSDL. This class represents a WSDL point of extensibility.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.ExtensibleElement#getEExtensibilityElements <em>EExtensibility Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wsdl.WSDLPackage#getExtensibleElement()
 * @model abstract="true"
 * @generated
 */
public interface ExtensibleElement extends WSDLElement{
  /**
   * Returns the value of the '<em><b>EExtensibility Elements</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.wsdl.ExtensibilityElement}.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>EExtensibility Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>EExtensibility Elements</em>' containment reference list.
   * @see org.eclipse.wsdl.WSDLPackage#getExtensibleElement_EExtensibilityElements()
   * @model type="org.eclipse.wsdl.ExtensibilityElement" containment="true"
   * @generated
   */
	EList getEExtensibilityElements();

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @model parameters=""
   * @generated
   */
	List getExtensibilityElements();

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @model 
   * @generated
   */
	void addExtensibilityElement(ExtensibilityElement extElement);

} // ExtensibleElement
