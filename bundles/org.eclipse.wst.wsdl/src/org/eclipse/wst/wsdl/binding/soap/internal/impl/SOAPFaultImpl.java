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
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.wst.wsdl.binding.soap.SOAPFault;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Fault</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl#getUse <em>Use</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl#getNamespaceURI <em>Namespace URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPFaultImpl#getEncodingStyles <em>Encoding Styles</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SOAPFaultImpl extends ExtensibilityElementImpl implements SOAPFault {
  /**
   * The default value of the '{@link #getUse() <em>Use</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getUse()
   * @generated
   * @ordered
   */
	protected static final String USE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUse() <em>Use</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getUse()
   * @generated
   * @ordered
   */
	protected String use = USE_EDEFAULT;

  /**
   * The default value of the '{@link #getNamespaceURI() <em>Namespace URI</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getNamespaceURI()
   * @generated
   * @ordered
   */
	protected static final String NAMESPACE_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getNamespaceURI() <em>Namespace URI</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getNamespaceURI()
   * @generated
   * @ordered
   */
	protected String namespaceURI = NAMESPACE_URI_EDEFAULT;

  /**
   * The cached value of the '{@link #getEncodingStyles() <em>Encoding Styles</em>}' attribute list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getEncodingStyles()
   * @generated
   * @ordered
   */
	protected EList encodingStyles = null;

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected SOAPFaultImpl()
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
    return SOAPPackage.eINSTANCE.getSOAPFault();
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getUse()
  {
    return use;
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setUse(String newUse)
  {
    String oldUse = use;
    use = newUse;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_FAULT__USE, oldUse, use));
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getNamespaceURI()
  {
    return namespaceURI;
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setNamespaceURI(String newNamespaceURI)
  {
    String oldNamespaceURI = namespaceURI;
    namespaceURI = newNamespaceURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_FAULT__NAMESPACE_URI, oldNamespaceURI, namespaceURI));
  }

private String name;
public String getName()
{
  return name;
}

public void setName(String name)
{
  this.name = name;
}

public void setEncodingStyles(List list)
{
  encodingStyles = (EList)list;
}
  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public List getEncodingStyles()
  {
    if (encodingStyles == null)
    {
      encodingStyles = new EDataTypeUniqueEList(String.class, this, SOAPPackage.SOAP_FAULT__ENCODING_STYLES);
    }
    return encodingStyles;
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
      case SOAPPackage.SOAP_FAULT__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case SOAPPackage.SOAP_FAULT__ELEMENT:
        return getElement();
      case SOAPPackage.SOAP_FAULT__REQUIRED:
        return isRequired() ? Boolean.TRUE : Boolean.FALSE;
      case SOAPPackage.SOAP_FAULT__ELEMENT_TYPE:
        return getElementType();
      case SOAPPackage.SOAP_FAULT__USE:
        return getUse();
      case SOAPPackage.SOAP_FAULT__NAMESPACE_URI:
        return getNamespaceURI();
      case SOAPPackage.SOAP_FAULT__ENCODING_STYLES:
        return getEncodingStyles();
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
      case SOAPPackage.SOAP_FAULT__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case SOAPPackage.SOAP_FAULT__ELEMENT:
        setElement((Element)newValue);
        return;
      case SOAPPackage.SOAP_FAULT__REQUIRED:
        setRequired(((Boolean)newValue).booleanValue());
        return;
      case SOAPPackage.SOAP_FAULT__ELEMENT_TYPE:
        setElementType((QName)newValue);
        return;
      case SOAPPackage.SOAP_FAULT__USE:
        setUse((String)newValue);
        return;
      case SOAPPackage.SOAP_FAULT__NAMESPACE_URI:
        setNamespaceURI((String)newValue);
        return;
      case SOAPPackage.SOAP_FAULT__ENCODING_STYLES:
        getEncodingStyles().clear();
        getEncodingStyles().addAll((Collection)newValue);
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
      case SOAPPackage.SOAP_FAULT__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case SOAPPackage.SOAP_FAULT__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case SOAPPackage.SOAP_FAULT__REQUIRED:
        setRequired(REQUIRED_EDEFAULT);
        return;
      case SOAPPackage.SOAP_FAULT__ELEMENT_TYPE:
        setElementType(ELEMENT_TYPE_EDEFAULT);
        return;
      case SOAPPackage.SOAP_FAULT__USE:
        setUse(USE_EDEFAULT);
        return;
      case SOAPPackage.SOAP_FAULT__NAMESPACE_URI:
        setNamespaceURI(NAMESPACE_URI_EDEFAULT);
        return;
      case SOAPPackage.SOAP_FAULT__ENCODING_STYLES:
        getEncodingStyles().clear();
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
      case SOAPPackage.SOAP_FAULT__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case SOAPPackage.SOAP_FAULT__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case SOAPPackage.SOAP_FAULT__REQUIRED:
        return required != REQUIRED_EDEFAULT;
      case SOAPPackage.SOAP_FAULT__ELEMENT_TYPE:
        return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
      case SOAPPackage.SOAP_FAULT__USE:
        return USE_EDEFAULT == null ? use != null : !USE_EDEFAULT.equals(use);
      case SOAPPackage.SOAP_FAULT__NAMESPACE_URI:
        return NAMESPACE_URI_EDEFAULT == null ? namespaceURI != null : !NAMESPACE_URI_EDEFAULT.equals(namespaceURI);
      case SOAPPackage.SOAP_FAULT__ENCODING_STYLES:
        return encodingStyles != null && !encodingStyles.isEmpty();
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
    result.append(" (use: ");
    result.append(use);
    result.append(", namespaceURI: ");
    result.append(namespaceURI);
    result.append(", encodingStyles: ");
    result.append(encodingStyles);
    result.append(')');
    return result.toString();
  }
	
  //
  // Reconcile methods: DOM -> Model
  //

  public void reconcileAttributes(Element changedElement)
  {
    setName
      (SOAPConstants.getAttribute(changedElement, SOAPConstants.NAME_ATTRIBUTE));
    setNamespaceURI
      (SOAPConstants.getAttribute(changedElement, SOAPConstants.NAMESPACE_URI_ATTRIBUTE));
    setUse
      (SOAPConstants.getAttribute(changedElement, SOAPConstants.USE_ATTRIBUTE));
    
    // TBD - handle encodingStyles
    
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
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPFault_NamespaceURI())
        niceSetAttribute(theElement,SOAPConstants.NAMESPACE_URI_ATTRIBUTE,getNamespaceURI());
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPFault_Use())
        niceSetAttribute(theElement,SOAPConstants.USE_ATTRIBUTE,getUse());
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPFault_EncodingStyles())
        ; // TBD - handle encodingStyles
      if (getName() != null)
      	niceSetAttribute(theElement,SOAPConstants.NAME_ATTRIBUTE,getName()); // Revisit Rose model
    }
  }

} //SOAPFaultImpl
