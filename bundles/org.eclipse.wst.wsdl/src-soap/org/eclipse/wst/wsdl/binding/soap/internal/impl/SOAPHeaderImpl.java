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


import java.util.Collection;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.binding.soap.SOAPFactory;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeader;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Header</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderImpl#getHeaderFaults <em>Header Faults</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SOAPHeaderImpl extends SOAPHeaderBaseImpl implements SOAPHeader
{
  /**
   * The cached value of the '{@link #getHeaderFaults() <em>Header Faults</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHeaderFaults()
   * @generated
   * @ordered
   */
  protected EList headerFaults = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SOAPHeaderImpl()
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
    return SOAPPackage.eINSTANCE.getSOAPHeader();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getHeaderFaults()
  {
    if (headerFaults == null)
    {
      headerFaults = new EObjectContainmentEList(SOAPHeaderFault.class, this, SOAPPackage.SOAP_HEADER__HEADER_FAULTS);
    }
    return headerFaults;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs)
  {
    if (featureID >= 0)
    {
      switch (eDerivedStructuralFeatureID(featureID, baseClass))
      {
        case SOAPPackage.SOAP_HEADER__HEADER_FAULTS:
        return ((InternalEList)getHeaderFaults()).basicRemove(otherEnd, msgs);
        default:
        return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
      }
    }
    return eBasicSetContainer(null, featureID, msgs);
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
      case SOAPPackage.SOAP_HEADER__DOCUMENTATION_ELEMENT:
      return getDocumentationElement();
      case SOAPPackage.SOAP_HEADER__ELEMENT:
      return getElement();
      case SOAPPackage.SOAP_HEADER__REQUIRED:
      return isRequired() ? Boolean.TRUE : Boolean.FALSE;
      case SOAPPackage.SOAP_HEADER__ELEMENT_TYPE:
      return getElementType();
      case SOAPPackage.SOAP_HEADER__USE:
      return getUse();
      case SOAPPackage.SOAP_HEADER__NAMESPACE_URI:
      return getNamespaceURI();
      case SOAPPackage.SOAP_HEADER__ENCODING_STYLES:
      return getEncodingStyles();
      case SOAPPackage.SOAP_HEADER__MESSAGE:
      if (resolve)
        return getMessage();
      return basicGetMessage();
      case SOAPPackage.SOAP_HEADER__PART:
      if (resolve)
        return getPart();
      return basicGetPart();
      case SOAPPackage.SOAP_HEADER__HEADER_FAULTS:
      return getHeaderFaults();
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
      case SOAPPackage.SOAP_HEADER__DOCUMENTATION_ELEMENT:
      setDocumentationElement((Element)newValue);
      return;
      case SOAPPackage.SOAP_HEADER__ELEMENT:
      setElement((Element)newValue);
      return;
      case SOAPPackage.SOAP_HEADER__REQUIRED:
      setRequired(((Boolean)newValue).booleanValue());
      return;
      case SOAPPackage.SOAP_HEADER__ELEMENT_TYPE:
      setElementType((QName)newValue);
      return;
      case SOAPPackage.SOAP_HEADER__USE:
      setUse((String)newValue);
      return;
      case SOAPPackage.SOAP_HEADER__NAMESPACE_URI:
      setNamespaceURI((String)newValue);
      return;
      case SOAPPackage.SOAP_HEADER__ENCODING_STYLES:
      getEncodingStyles().clear();
      getEncodingStyles().addAll((Collection)newValue);
      return;
      case SOAPPackage.SOAP_HEADER__MESSAGE:
      setMessage((Message)newValue);
      return;
      case SOAPPackage.SOAP_HEADER__PART:
      setPart((Part)newValue);
      return;
      case SOAPPackage.SOAP_HEADER__HEADER_FAULTS:
      getHeaderFaults().clear();
      getHeaderFaults().addAll((Collection)newValue);
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
      case SOAPPackage.SOAP_HEADER__DOCUMENTATION_ELEMENT:
      setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER__ELEMENT:
      setElement(ELEMENT_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER__REQUIRED:
      setRequired(REQUIRED_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER__ELEMENT_TYPE:
      setElementType(ELEMENT_TYPE_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER__USE:
      setUse(USE_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER__NAMESPACE_URI:
      setNamespaceURI(NAMESPACE_URI_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER__ENCODING_STYLES:
      getEncodingStyles().clear();
      return;
      case SOAPPackage.SOAP_HEADER__MESSAGE:
      setMessage((Message)null);
      return;
      case SOAPPackage.SOAP_HEADER__PART:
      setPart((Part)null);
      return;
      case SOAPPackage.SOAP_HEADER__HEADER_FAULTS:
      getHeaderFaults().clear();
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
      case SOAPPackage.SOAP_HEADER__DOCUMENTATION_ELEMENT:
      return DOCUMENTATION_ELEMENT_EDEFAULT == null
        ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case SOAPPackage.SOAP_HEADER__ELEMENT:
      return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case SOAPPackage.SOAP_HEADER__REQUIRED:
      return required != REQUIRED_EDEFAULT;
      case SOAPPackage.SOAP_HEADER__ELEMENT_TYPE:
      return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
      case SOAPPackage.SOAP_HEADER__USE:
      return USE_EDEFAULT == null ? use != null : !USE_EDEFAULT.equals(use);
      case SOAPPackage.SOAP_HEADER__NAMESPACE_URI:
      return NAMESPACE_URI_EDEFAULT == null ? namespaceURI != null : !NAMESPACE_URI_EDEFAULT.equals(namespaceURI);
      case SOAPPackage.SOAP_HEADER__ENCODING_STYLES:
      return encodingStyles != null && !encodingStyles.isEmpty();
      case SOAPPackage.SOAP_HEADER__MESSAGE:
      return message != null;
      case SOAPPackage.SOAP_HEADER__PART:
      return part != null;
      case SOAPPackage.SOAP_HEADER__HEADER_FAULTS:
      return headerFaults != null && !headerFaults.isEmpty();
    }
    return eDynamicIsSet(eFeature);
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    if (SOAPConstants.HEADER_FAULT_ELEMENT_TAG.equals(child.getLocalName()))
    {
      SOAPHeaderFault fault = SOAPFactory.eINSTANCE.createSOAPHeaderFault();
      fault.setEnclosingDefinition(getEnclosingDefinition());
      fault.setElement(child);
      getHeaderFaults().add(fault);
    }
  }

  protected void handleReconciliation(Collection remainingModelObjects)
  {
    for (Iterator i = remainingModelObjects.iterator(); i.hasNext();)
    {
      remove(this, i.next());
    }
  }

  protected void remove(Object component, Object modelObject)
  {
    SOAPHeader header = (SOAPHeader)component;
    if (modelObject instanceof SOAPHeaderFault)
    {
      header.getHeaderFaults().remove(modelObject);
    }
  }

  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(SOAPConstants.SOAP_NAMESPACE_URI, SOAPConstants.HEADER_ELEMENT_TAG);
    return elementType;
  }

} //SOAPHeaderImpl
