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
package org.eclipse.wst.wsdl.binding.http.internal.impl;


import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.wst.wsdl.binding.http.HTTPPackage;
import org.eclipse.wst.wsdl.binding.http.HTTPUrlReplacement;

import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Url Replacement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class HTTPUrlReplacementImpl extends ExtensibilityElementImpl implements HTTPUrlReplacement {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HTTPUrlReplacementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return HTTPPackage.eINSTANCE.getHTTPUrlReplacement();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case HTTPPackage.HTTP_URL_REPLACEMENT__DOCUMENTATION_ELEMENT:
				return getDocumentationElement();
			case HTTPPackage.HTTP_URL_REPLACEMENT__ELEMENT:
				return getElement();
			case HTTPPackage.HTTP_URL_REPLACEMENT__REQUIRED:
				return isRequired() ? Boolean.TRUE : Boolean.FALSE;
			case HTTPPackage.HTTP_URL_REPLACEMENT__ELEMENT_TYPE:
				return getElementType();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case HTTPPackage.HTTP_URL_REPLACEMENT__DOCUMENTATION_ELEMENT:
				setDocumentationElement((Element)newValue);
				return;
			case HTTPPackage.HTTP_URL_REPLACEMENT__ELEMENT:
				setElement((Element)newValue);
				return;
			case HTTPPackage.HTTP_URL_REPLACEMENT__REQUIRED:
				setRequired(((Boolean)newValue).booleanValue());
				return;
			case HTTPPackage.HTTP_URL_REPLACEMENT__ELEMENT_TYPE:
				setElementType((QName)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case HTTPPackage.HTTP_URL_REPLACEMENT__DOCUMENTATION_ELEMENT:
				setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
				return;
			case HTTPPackage.HTTP_URL_REPLACEMENT__ELEMENT:
				setElement(ELEMENT_EDEFAULT);
				return;
			case HTTPPackage.HTTP_URL_REPLACEMENT__REQUIRED:
				setRequired(REQUIRED_EDEFAULT);
				return;
			case HTTPPackage.HTTP_URL_REPLACEMENT__ELEMENT_TYPE:
				setElementType(ELEMENT_TYPE_EDEFAULT);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case HTTPPackage.HTTP_URL_REPLACEMENT__DOCUMENTATION_ELEMENT:
				return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
			case HTTPPackage.HTTP_URL_REPLACEMENT__ELEMENT:
				return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
			case HTTPPackage.HTTP_URL_REPLACEMENT__REQUIRED:
				return required != REQUIRED_EDEFAULT;
			case HTTPPackage.HTTP_URL_REPLACEMENT__ELEMENT_TYPE:
				return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
		}
		return eDynamicIsSet(eFeature);
	}

} //HTTPUrlReplacementImpl
