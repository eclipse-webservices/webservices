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
import org.eclipse.wst.wsdl.binding.soap.SOAPBinding;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Binding</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBindingImpl#getTransportURI <em>Transport URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBindingImpl#getStyle <em>Style</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SOAPBindingImpl extends ExtensibilityElementImpl implements SOAPBinding {
  /**
   * The default value of the '{@link #getTransportURI() <em>Transport URI</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getTransportURI()
   * @generated
   * @ordered
   */
	protected static final String TRANSPORT_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTransportURI() <em>Transport URI</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getTransportURI()
   * @generated
   * @ordered
   */
	protected String transportURI = TRANSPORT_URI_EDEFAULT;

  /**
   * The default value of the '{@link #getStyle() <em>Style</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getStyle()
   * @generated
   * @ordered
   */
	protected static final String STYLE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getStyle() <em>Style</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getStyle()
   * @generated
   * @ordered
   */
	protected String style = STYLE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected SOAPBindingImpl()
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
    return SOAPPackage.eINSTANCE.getSOAPBinding();
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getTransportURI()
  {
    return transportURI;
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setTransportURI(String newTransportURI)
  {
    String oldTransportURI = transportURI;
    transportURI = newTransportURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_BINDING__TRANSPORT_URI, oldTransportURI, transportURI));
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getStyle()
  {
    return style;
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setStyle(String newStyle)
  {
    String oldStyle = style;
    style = newStyle;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_BINDING__STYLE, oldStyle, style));
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
      case SOAPPackage.SOAP_BINDING__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case SOAPPackage.SOAP_BINDING__ELEMENT:
        return getElement();
      case SOAPPackage.SOAP_BINDING__REQUIRED:
        return isRequired() ? Boolean.TRUE : Boolean.FALSE;
      case SOAPPackage.SOAP_BINDING__ELEMENT_TYPE:
        return getElementType();
      case SOAPPackage.SOAP_BINDING__TRANSPORT_URI:
        return getTransportURI();
      case SOAPPackage.SOAP_BINDING__STYLE:
        return getStyle();
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
      case SOAPPackage.SOAP_BINDING__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case SOAPPackage.SOAP_BINDING__ELEMENT:
        setElement((Element)newValue);
        return;
      case SOAPPackage.SOAP_BINDING__REQUIRED:
        setRequired(((Boolean)newValue).booleanValue());
        return;
      case SOAPPackage.SOAP_BINDING__ELEMENT_TYPE:
        setElementType((QName)newValue);
        return;
      case SOAPPackage.SOAP_BINDING__TRANSPORT_URI:
        setTransportURI((String)newValue);
        return;
      case SOAPPackage.SOAP_BINDING__STYLE:
        setStyle((String)newValue);
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
      case SOAPPackage.SOAP_BINDING__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case SOAPPackage.SOAP_BINDING__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case SOAPPackage.SOAP_BINDING__REQUIRED:
        setRequired(REQUIRED_EDEFAULT);
        return;
      case SOAPPackage.SOAP_BINDING__ELEMENT_TYPE:
        setElementType(ELEMENT_TYPE_EDEFAULT);
        return;
      case SOAPPackage.SOAP_BINDING__TRANSPORT_URI:
        setTransportURI(TRANSPORT_URI_EDEFAULT);
        return;
      case SOAPPackage.SOAP_BINDING__STYLE:
        setStyle(STYLE_EDEFAULT);
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
      case SOAPPackage.SOAP_BINDING__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case SOAPPackage.SOAP_BINDING__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case SOAPPackage.SOAP_BINDING__REQUIRED:
        return required != REQUIRED_EDEFAULT;
      case SOAPPackage.SOAP_BINDING__ELEMENT_TYPE:
        return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
      case SOAPPackage.SOAP_BINDING__TRANSPORT_URI:
        return TRANSPORT_URI_EDEFAULT == null ? transportURI != null : !TRANSPORT_URI_EDEFAULT.equals(transportURI);
      case SOAPPackage.SOAP_BINDING__STYLE:
        return STYLE_EDEFAULT == null ? style != null : !STYLE_EDEFAULT.equals(style);
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
    result.append(" (transportURI: ");
    result.append(transportURI);
    result.append(", style: ");
    result.append(style);
    result.append(')');
    return result.toString();
  }
	
  //
  // Reconcile methods
  //

  public void reconcileAttributes(Element changedElement)
  {
    setStyle
      (SOAPConstants.getAttribute(changedElement, SOAPConstants.STYLE_ATTRIBUTE));
    setTransportURI
      (SOAPConstants.getAttribute(changedElement, SOAPConstants.TRANSPORT_ATTRIBUTE));
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
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPBinding_Style())
        niceSetAttribute(theElement,SOAPConstants.STYLE_ATTRIBUTE,getStyle());
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPBinding_TransportURI())
        niceSetAttribute(theElement,SOAPConstants.TRANSPORT_ATTRIBUTE,getTransportURI());
    }
  }
} //SOAPBindingImpl
