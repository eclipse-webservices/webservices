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

import java.util.Collection;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Header Fault</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class SOAPHeaderFaultImpl extends SOAPHeaderBaseImpl implements SOAPHeaderFault {
  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected SOAPHeaderFaultImpl()
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
    return SOAPPackage.eINSTANCE.getSOAPHeaderFault();
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
      case SOAPPackage.SOAP_HEADER_FAULT__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case SOAPPackage.SOAP_HEADER_FAULT__ELEMENT:
        return getElement();
      case SOAPPackage.SOAP_HEADER_FAULT__REQUIRED:
        return isRequired() ? Boolean.TRUE : Boolean.FALSE;
      case SOAPPackage.SOAP_HEADER_FAULT__ELEMENT_TYPE:
        return getElementType();
      case SOAPPackage.SOAP_HEADER_FAULT__USE:
        return getUse();
      case SOAPPackage.SOAP_HEADER_FAULT__NAMESPACE_URI:
        return getNamespaceURI();
      case SOAPPackage.SOAP_HEADER_FAULT__ENCODING_STYLES:
        return getEncodingStyles();
      case SOAPPackage.SOAP_HEADER_FAULT__MESSAGE:
        if (resolve) return getMessage();
        return basicGetMessage();
      case SOAPPackage.SOAP_HEADER_FAULT__PART:
        if (resolve) return getPart();
        return basicGetPart();
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
      case SOAPPackage.SOAP_HEADER_FAULT__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__ELEMENT:
        setElement((Element)newValue);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__REQUIRED:
        setRequired(((Boolean)newValue).booleanValue());
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__ELEMENT_TYPE:
        setElementType((QName)newValue);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__USE:
        setUse((String)newValue);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__NAMESPACE_URI:
        setNamespaceURI((String)newValue);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__ENCODING_STYLES:
        getEncodingStyles().clear();
        getEncodingStyles().addAll((Collection)newValue);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__MESSAGE:
        setMessage((Message)newValue);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__PART:
        setPart((Part)newValue);
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
      case SOAPPackage.SOAP_HEADER_FAULT__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__REQUIRED:
        setRequired(REQUIRED_EDEFAULT);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__ELEMENT_TYPE:
        setElementType(ELEMENT_TYPE_EDEFAULT);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__USE:
        setUse(USE_EDEFAULT);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__NAMESPACE_URI:
        setNamespaceURI(NAMESPACE_URI_EDEFAULT);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__ENCODING_STYLES:
        getEncodingStyles().clear();
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__MESSAGE:
        setMessage((Message)null);
        return;
      case SOAPPackage.SOAP_HEADER_FAULT__PART:
        setPart((Part)null);
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
      case SOAPPackage.SOAP_HEADER_FAULT__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case SOAPPackage.SOAP_HEADER_FAULT__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case SOAPPackage.SOAP_HEADER_FAULT__REQUIRED:
        return required != REQUIRED_EDEFAULT;
      case SOAPPackage.SOAP_HEADER_FAULT__ELEMENT_TYPE:
        return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
      case SOAPPackage.SOAP_HEADER_FAULT__USE:
        return USE_EDEFAULT == null ? use != null : !USE_EDEFAULT.equals(use);
      case SOAPPackage.SOAP_HEADER_FAULT__NAMESPACE_URI:
        return NAMESPACE_URI_EDEFAULT == null ? namespaceURI != null : !NAMESPACE_URI_EDEFAULT.equals(namespaceURI);
      case SOAPPackage.SOAP_HEADER_FAULT__ENCODING_STYLES:
        return encodingStyles != null && !encodingStyles.isEmpty();
      case SOAPPackage.SOAP_HEADER_FAULT__MESSAGE:
        return message != null;
      case SOAPPackage.SOAP_HEADER_FAULT__PART:
        return part != null;
    }
    return eDynamicIsSet(eFeature);
  }

} //SOAPHeaderFaultImpl
