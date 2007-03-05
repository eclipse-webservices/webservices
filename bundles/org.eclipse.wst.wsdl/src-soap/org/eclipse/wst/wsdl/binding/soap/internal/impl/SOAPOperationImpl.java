/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.wst.wsdl.binding.soap.SOAPOperation;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPOperationImpl#getSoapActionURI <em>Soap Action URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPOperationImpl#getStyle <em>Style</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SOAPOperationImpl extends ExtensibilityElementImpl implements SOAPOperation
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default value of the '{@link #getSoapActionURI() <em>Soap Action URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSoapActionURI()
   * @generated
   * @ordered
   */
  protected static final String SOAP_ACTION_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSoapActionURI() <em>Soap Action URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSoapActionURI()
   * @generated
   * @ordered
   */
  protected String soapActionURI = SOAP_ACTION_URI_EDEFAULT;

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
  protected SOAPOperationImpl()
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
    return SOAPPackage.eINSTANCE.getSOAPOperation();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSoapActionURI()
  {
    return soapActionURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSoapActionURI(String newSoapActionURI)
  {
    String oldSoapActionURI = soapActionURI;
    soapActionURI = newSoapActionURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_OPERATION__SOAP_ACTION_URI, oldSoapActionURI, soapActionURI));
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
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_OPERATION__STYLE, oldStyle, style));
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
      case SOAPPackage.SOAP_OPERATION__DOCUMENTATION_ELEMENT:
      return getDocumentationElement();
      case SOAPPackage.SOAP_OPERATION__ELEMENT:
      return getElement();
      case SOAPPackage.SOAP_OPERATION__REQUIRED:
      return isRequired() ? Boolean.TRUE : Boolean.FALSE;
      case SOAPPackage.SOAP_OPERATION__ELEMENT_TYPE:
      return getElementType();
      case SOAPPackage.SOAP_OPERATION__SOAP_ACTION_URI:
      return getSoapActionURI();
      case SOAPPackage.SOAP_OPERATION__STYLE:
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
      case SOAPPackage.SOAP_OPERATION__DOCUMENTATION_ELEMENT:
      setDocumentationElement((Element)newValue);
      return;
      case SOAPPackage.SOAP_OPERATION__ELEMENT:
      setElement((Element)newValue);
      return;
      case SOAPPackage.SOAP_OPERATION__REQUIRED:
      setRequired(((Boolean)newValue).booleanValue());
      return;
      case SOAPPackage.SOAP_OPERATION__ELEMENT_TYPE:
      setElementType((QName)newValue);
      return;
      case SOAPPackage.SOAP_OPERATION__SOAP_ACTION_URI:
      setSoapActionURI((String)newValue);
      return;
      case SOAPPackage.SOAP_OPERATION__STYLE:
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
      case SOAPPackage.SOAP_OPERATION__DOCUMENTATION_ELEMENT:
      setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
      return;
      case SOAPPackage.SOAP_OPERATION__ELEMENT:
      setElement(ELEMENT_EDEFAULT);
      return;
      case SOAPPackage.SOAP_OPERATION__REQUIRED:
      setRequired(REQUIRED_EDEFAULT);
      return;
      case SOAPPackage.SOAP_OPERATION__ELEMENT_TYPE:
      setElementType(ELEMENT_TYPE_EDEFAULT);
      return;
      case SOAPPackage.SOAP_OPERATION__SOAP_ACTION_URI:
      setSoapActionURI(SOAP_ACTION_URI_EDEFAULT);
      return;
      case SOAPPackage.SOAP_OPERATION__STYLE:
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
      case SOAPPackage.SOAP_OPERATION__DOCUMENTATION_ELEMENT:
      return DOCUMENTATION_ELEMENT_EDEFAULT == null
        ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case SOAPPackage.SOAP_OPERATION__ELEMENT:
      return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case SOAPPackage.SOAP_OPERATION__REQUIRED:
      return required != REQUIRED_EDEFAULT;
      case SOAPPackage.SOAP_OPERATION__ELEMENT_TYPE:
      return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
      case SOAPPackage.SOAP_OPERATION__SOAP_ACTION_URI:
      return SOAP_ACTION_URI_EDEFAULT == null ? soapActionURI != null : !SOAP_ACTION_URI_EDEFAULT.equals(soapActionURI);
      case SOAPPackage.SOAP_OPERATION__STYLE:
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
    if (eIsProxy())
      return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (soapActionURI: ");
    result.append(soapActionURI);
    result.append(", style: ");
    result.append(style);
    result.append(')');
    return result.toString();
  }

  //
  // Reconcile methods: DOM -> Model
  //

  public void reconcileAttributes(Element changedElement)
  {
    setSoapActionURI(SOAPConstants.getAttribute(changedElement, SOAPConstants.SOAP_ACTION_ATTRIBUTE));
    setStyle(SOAPConstants.getAttribute(changedElement, SOAPConstants.STYLE_ATTRIBUTE));
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
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPOperation_SoapActionURI())
        niceSetAttribute(theElement, SOAPConstants.SOAP_ACTION_ATTRIBUTE, getSoapActionURI());
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPOperation_Style())
        niceSetAttribute(theElement, SOAPConstants.STYLE_ATTRIBUTE, getStyle());
    }
  }

  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(SOAPConstants.SOAP_NAMESPACE_URI, SOAPConstants.OPERATION_ELEMENT_TAG);
    return elementType;
  }

} //SOAPOperationImpl
