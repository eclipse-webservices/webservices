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

import org.eclipse.emf.ecore.EObject;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This class represents a WSDL language element.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.WSDLElement#getDocumentationElement <em>Documentation Element</em>}</li>
 *   <li>{@link org.eclipse.wsdl.WSDLElement#getElement <em>Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wsdl.WSDLPackage#getWSDLElement()
 * @model abstract="true"
 * @generated
 */
public interface WSDLElement extends EObject{
  /**
   * Returns the value of the '<em><b>Documentation Element</b></em>' attribute.
   * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Documentation Element</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
   * @return the value of the '<em>Documentation Element</em>' attribute.
   * @see #setDocumentationElement(Element)
   * @see org.eclipse.wsdl.WSDLPackage#getWSDLElement_DocumentationElement()
   * @model dataType="org.eclipse.wsdl.DOMElement"
   * @generated
   */
	Element getDocumentationElement();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.WSDLElement#getDocumentationElement <em>Documentation Element</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @param value the new value of the '<em>Documentation Element</em>' attribute.
   * @see #getDocumentationElement()
   * @generated
   */
	void setDocumentationElement(Element value);

  /**
   * Returns the value of the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Element</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Element</em>' attribute.
   * @see #setElement(Element)
   * @see org.eclipse.wsdl.WSDLPackage#getWSDLElement_Element()
   * @model dataType="org.eclipse.wsdl.DOMElement"
   * @generated
   */
  Element getElement();

  /**
   * Sets the value of the '{@link org.eclipse.wsdl.WSDLElement#getElement <em>Element</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Element</em>' attribute.
   * @see #getElement()
   * @generated
   */
  void setElement(Element value);

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @model parameters=""
   * @generated
   */
	Definition getEnclosingDefinition();

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @model 
   * @generated
   */
	void setEnclosingDefinition(Definition definition);

} // WSDLElement
