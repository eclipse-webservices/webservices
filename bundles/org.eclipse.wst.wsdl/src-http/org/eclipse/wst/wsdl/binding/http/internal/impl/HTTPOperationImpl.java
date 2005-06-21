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


import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.wst.wsdl.binding.http.HTTPOperation;
import org.eclipse.wst.wsdl.binding.http.HTTPPackage;
import org.eclipse.wst.wsdl.binding.http.internal.util.HTTPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.http.impl.HTTPOperationImpl#getLocationURI <em>Location URI</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HTTPOperationImpl extends ExtensibilityElementImpl implements HTTPOperation {
	/**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
	 * The default value of the '{@link #getLocationURI() <em>Location URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocationURI()
	 * @generated
	 * @ordered
	 */
	protected static final String LOCATION_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLocationURI() <em>Location URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocationURI()
	 * @generated
	 * @ordered
	 */
	protected String locationURI = LOCATION_URI_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HTTPOperationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return HTTPPackage.eINSTANCE.getHTTPOperation();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLocationURI() {
		return locationURI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocationURI(String newLocationURI) {
		String oldLocationURI = locationURI;
		locationURI = newLocationURI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, HTTPPackage.HTTP_OPERATION__LOCATION_URI, oldLocationURI, locationURI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case HTTPPackage.HTTP_OPERATION__DOCUMENTATION_ELEMENT:
				return getDocumentationElement();
			case HTTPPackage.HTTP_OPERATION__ELEMENT:
				return getElement();
			case HTTPPackage.HTTP_OPERATION__REQUIRED:
				return isRequired() ? Boolean.TRUE : Boolean.FALSE;
			case HTTPPackage.HTTP_OPERATION__ELEMENT_TYPE:
				return getElementType();
			case HTTPPackage.HTTP_OPERATION__LOCATION_URI:
				return getLocationURI();
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
			case HTTPPackage.HTTP_OPERATION__DOCUMENTATION_ELEMENT:
				setDocumentationElement((Element)newValue);
				return;
			case HTTPPackage.HTTP_OPERATION__ELEMENT:
				setElement((Element)newValue);
				return;
			case HTTPPackage.HTTP_OPERATION__REQUIRED:
				setRequired(((Boolean)newValue).booleanValue());
				return;
			case HTTPPackage.HTTP_OPERATION__ELEMENT_TYPE:
				setElementType((QName)newValue);
				return;
			case HTTPPackage.HTTP_OPERATION__LOCATION_URI:
				setLocationURI((String)newValue);
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
			case HTTPPackage.HTTP_OPERATION__DOCUMENTATION_ELEMENT:
				setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
				return;
			case HTTPPackage.HTTP_OPERATION__ELEMENT:
				setElement(ELEMENT_EDEFAULT);
				return;
			case HTTPPackage.HTTP_OPERATION__REQUIRED:
				setRequired(REQUIRED_EDEFAULT);
				return;
			case HTTPPackage.HTTP_OPERATION__ELEMENT_TYPE:
				setElementType(ELEMENT_TYPE_EDEFAULT);
				return;
			case HTTPPackage.HTTP_OPERATION__LOCATION_URI:
				setLocationURI(LOCATION_URI_EDEFAULT);
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
			case HTTPPackage.HTTP_OPERATION__DOCUMENTATION_ELEMENT:
				return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
			case HTTPPackage.HTTP_OPERATION__ELEMENT:
				return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
			case HTTPPackage.HTTP_OPERATION__REQUIRED:
				return required != REQUIRED_EDEFAULT;
			case HTTPPackage.HTTP_OPERATION__ELEMENT_TYPE:
				return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
			case HTTPPackage.HTTP_OPERATION__LOCATION_URI:
				return LOCATION_URI_EDEFAULT == null ? locationURI != null : !LOCATION_URI_EDEFAULT.equals(locationURI);
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (locationURI: ");
		result.append(locationURI);
		result.append(')');
		return result.toString();
	}

  //
  // Reconcile methods: DOM -> Model
  //

  public void reconcileAttributes(Element changedElement)
  {
  	setLocationURI
      (HTTPConstants.getAttribute(changedElement, HTTPConstants.LOCATION_URI_ATTRIBUTE));
    reconcileReferences(false);
  }
  
  //
  // For reconciliation: Model -> DOM
  //

  protected void changeAttribute(EAttribute eAttribute)
  {
    if (isReconciling)
      return;

    super.changeAttribute(eAttribute);
    Element theElement = getElement();
    if (theElement != null)
    {
      if (eAttribute == null || eAttribute == HTTPPackage.eINSTANCE.getHTTPOperation_LocationURI())
        niceSetAttribute(theElement,HTTPConstants.LOCATION_URI_ATTRIBUTE,getLocationURI());
    }
  }
  
  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(HTTPConstants.HTTP_NAMESPACE_URI, HTTPConstants.OPERATION_ELEMENT_TAG);
    return elementType;
  }
  
} //HTTPOperationImpl
