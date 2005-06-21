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
package org.eclipse.wst.wsdl.binding.soap.internal.impl;


import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.wst.wsdl.binding.soap.SOAPAddress;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Address</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPAddressImpl#getLocationURI <em>Location URI</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SOAPAddressImpl extends ExtensibilityElementImpl implements SOAPAddress {
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
	protected SOAPAddressImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected EClass eStaticClass()
  {
    return SOAPPackage.eINSTANCE.getSOAPAddress();
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getLocationURI()
  {
    return locationURI;
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setLocationURI(String newLocationURI)
  {
    String oldLocationURI = locationURI;
    locationURI = newLocationURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_ADDRESS__LOCATION_URI, oldLocationURI, locationURI));
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public Object eGet(EStructuralFeature eFeature, boolean resolve)
  {
    switch (eDerivedStructuralFeatureID(eFeature))
    {
      case SOAPPackage.SOAP_ADDRESS__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case SOAPPackage.SOAP_ADDRESS__ELEMENT:
        return getElement();
      case SOAPPackage.SOAP_ADDRESS__REQUIRED:
        return isRequired() ? Boolean.TRUE : Boolean.FALSE;
      case SOAPPackage.SOAP_ADDRESS__ELEMENT_TYPE:
        return getElementType();
      case SOAPPackage.SOAP_ADDRESS__LOCATION_URI:
        return getLocationURI();
    }
    return eDynamicGet(eFeature, resolve);
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void eSet(EStructuralFeature eFeature, Object newValue)
  {
    switch (eDerivedStructuralFeatureID(eFeature))
    {
      case SOAPPackage.SOAP_ADDRESS__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case SOAPPackage.SOAP_ADDRESS__ELEMENT:
        setElement((Element)newValue);
        return;
      case SOAPPackage.SOAP_ADDRESS__REQUIRED:
        setRequired(((Boolean)newValue).booleanValue());
        return;
      case SOAPPackage.SOAP_ADDRESS__ELEMENT_TYPE:
        setElementType((QName)newValue);
        return;
      case SOAPPackage.SOAP_ADDRESS__LOCATION_URI:
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
	public void eUnset(EStructuralFeature eFeature)
  {
    switch (eDerivedStructuralFeatureID(eFeature))
    {
      case SOAPPackage.SOAP_ADDRESS__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case SOAPPackage.SOAP_ADDRESS__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case SOAPPackage.SOAP_ADDRESS__REQUIRED:
        setRequired(REQUIRED_EDEFAULT);
        return;
      case SOAPPackage.SOAP_ADDRESS__ELEMENT_TYPE:
        setElementType(ELEMENT_TYPE_EDEFAULT);
        return;
      case SOAPPackage.SOAP_ADDRESS__LOCATION_URI:
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
	public boolean eIsSet(EStructuralFeature eFeature)
  {
    switch (eDerivedStructuralFeatureID(eFeature))
    {
      case SOAPPackage.SOAP_ADDRESS__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case SOAPPackage.SOAP_ADDRESS__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case SOAPPackage.SOAP_ADDRESS__REQUIRED:
        return required != REQUIRED_EDEFAULT;
      case SOAPPackage.SOAP_ADDRESS__ELEMENT_TYPE:
        return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
      case SOAPPackage.SOAP_ADDRESS__LOCATION_URI:
        return LOCATION_URI_EDEFAULT == null ? locationURI != null : !LOCATION_URI_EDEFAULT.equals(locationURI);
    }
    return eDynamicIsSet(eFeature);
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String toString()
  {
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
      (SOAPConstants.getAttribute(changedElement, SOAPConstants.LOCATION_ATTRIBUTE));
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
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPAddress_LocationURI())
        niceSetAttribute(theElement,SOAPConstants.LOCATION_ATTRIBUTE,getLocationURI());
    }
  }
  
  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(SOAPConstants.SOAP_NAMESPACE_URI, SOAPConstants.ADDRESS_ELEMENT_TAG);
    return elementType;
  } 
} //SOAPAddressImpl
