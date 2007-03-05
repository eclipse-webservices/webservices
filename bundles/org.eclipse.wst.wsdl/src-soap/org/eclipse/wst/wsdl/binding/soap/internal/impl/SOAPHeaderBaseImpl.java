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
import java.util.List;
import com.ibm.icu.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.binding.soap.SOAPHeaderBase;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Header Base</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getUse <em>Use</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getNamespaceURI <em>Namespace URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getEncodingStyles <em>Encoding Styles</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getMessage <em>Message</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPHeaderBaseImpl#getPart <em>Part</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SOAPHeaderBaseImpl extends ExtensibilityElementImpl implements SOAPHeaderBase
{
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
   * The cached value of the '{@link #getMessage() <em>Message</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMessage()
   * @generated
   * @ordered
   */
  protected Message message = null;

  /**
   * The cached value of the '{@link #getPart() <em>Part</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPart()
   * @generated
   * @ordered
   */
  protected Part part = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SOAPHeaderBaseImpl()
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
    return SOAPPackage.eINSTANCE.getSOAPHeaderBase();
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
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_HEADER_BASE__USE, oldUse, use));
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
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_HEADER_BASE__NAMESPACE_URI, oldNamespaceURI, namespaceURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEncodingStyles()
  {
    if (encodingStyles == null)
    {
      encodingStyles = new EDataTypeUniqueEList(String.class, this, SOAPPackage.SOAP_HEADER_BASE__ENCODING_STYLES);
    }
    return encodingStyles;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Message getMessage()
  {
    if (message != null && message.eIsProxy())
    {
      Message oldMessage = message;
      message = (Message)eResolveProxy((InternalEObject)message);
      if (message != oldMessage)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SOAPPackage.SOAP_HEADER_BASE__MESSAGE, oldMessage, message));
      }
    }
    return message;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Message basicGetMessage()
  {
    return message;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMessage(Message newMessage)
  {
    Message oldMessage = message;
    message = newMessage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_HEADER_BASE__MESSAGE, oldMessage, message));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Part getPart()
  {
    if (part != null && part.eIsProxy())
    {
      Part oldPart = part;
      part = (Part)eResolveProxy((InternalEObject)part);
      if (part != oldPart)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SOAPPackage.SOAP_HEADER_BASE__PART, oldPart, part));
      }
    }
    return part;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Part basicGetPart()
  {
    return part;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPart(Part newPart)
  {
    Part oldPart = part;
    part = newPart;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_HEADER_BASE__PART, oldPart, part));
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
      case SOAPPackage.SOAP_HEADER_BASE__DOCUMENTATION_ELEMENT:
      return getDocumentationElement();
      case SOAPPackage.SOAP_HEADER_BASE__ELEMENT:
      return getElement();
      case SOAPPackage.SOAP_HEADER_BASE__REQUIRED:
      return isRequired() ? Boolean.TRUE : Boolean.FALSE;
      case SOAPPackage.SOAP_HEADER_BASE__ELEMENT_TYPE:
      return getElementType();
      case SOAPPackage.SOAP_HEADER_BASE__USE:
      return getUse();
      case SOAPPackage.SOAP_HEADER_BASE__NAMESPACE_URI:
      return getNamespaceURI();
      case SOAPPackage.SOAP_HEADER_BASE__ENCODING_STYLES:
      return getEncodingStyles();
      case SOAPPackage.SOAP_HEADER_BASE__MESSAGE:
      if (resolve)
        return getMessage();
      return basicGetMessage();
      case SOAPPackage.SOAP_HEADER_BASE__PART:
      if (resolve)
        return getPart();
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
      case SOAPPackage.SOAP_HEADER_BASE__DOCUMENTATION_ELEMENT:
      setDocumentationElement((Element)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__ELEMENT:
      setElement((Element)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__REQUIRED:
      setRequired(((Boolean)newValue).booleanValue());
      return;
      case SOAPPackage.SOAP_HEADER_BASE__ELEMENT_TYPE:
      setElementType((QName)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__USE:
      setUse((String)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__NAMESPACE_URI:
      setNamespaceURI((String)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__ENCODING_STYLES:
      getEncodingStyles().clear();
      getEncodingStyles().addAll((Collection)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__MESSAGE:
      setMessage((Message)newValue);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__PART:
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
      case SOAPPackage.SOAP_HEADER_BASE__DOCUMENTATION_ELEMENT:
      setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__ELEMENT:
      setElement(ELEMENT_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__REQUIRED:
      setRequired(REQUIRED_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__ELEMENT_TYPE:
      setElementType(ELEMENT_TYPE_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__USE:
      setUse(USE_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__NAMESPACE_URI:
      setNamespaceURI(NAMESPACE_URI_EDEFAULT);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__ENCODING_STYLES:
      getEncodingStyles().clear();
      return;
      case SOAPPackage.SOAP_HEADER_BASE__MESSAGE:
      setMessage((Message)null);
      return;
      case SOAPPackage.SOAP_HEADER_BASE__PART:
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
      case SOAPPackage.SOAP_HEADER_BASE__DOCUMENTATION_ELEMENT:
      return DOCUMENTATION_ELEMENT_EDEFAULT == null
        ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case SOAPPackage.SOAP_HEADER_BASE__ELEMENT:
      return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case SOAPPackage.SOAP_HEADER_BASE__REQUIRED:
      return required != REQUIRED_EDEFAULT;
      case SOAPPackage.SOAP_HEADER_BASE__ELEMENT_TYPE:
      return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
      case SOAPPackage.SOAP_HEADER_BASE__USE:
      return USE_EDEFAULT == null ? use != null : !USE_EDEFAULT.equals(use);
      case SOAPPackage.SOAP_HEADER_BASE__NAMESPACE_URI:
      return NAMESPACE_URI_EDEFAULT == null ? namespaceURI != null : !NAMESPACE_URI_EDEFAULT.equals(namespaceURI);
      case SOAPPackage.SOAP_HEADER_BASE__ENCODING_STYLES:
      return encodingStyles != null && !encodingStyles.isEmpty();
      case SOAPPackage.SOAP_HEADER_BASE__MESSAGE:
      return message != null;
      case SOAPPackage.SOAP_HEADER_BASE__PART:
      return part != null;
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
    if (changedElement.hasAttribute(SOAPConstants.USE_ATTRIBUTE))
      setUse(changedElement.getAttribute(SOAPConstants.USE_ATTRIBUTE));
    if (changedElement.hasAttribute(SOAPConstants.NAMESPACE_ATTRIBUTE))
      setNamespaceURI(changedElement.getAttribute(SOAPConstants.NAMESPACE_ATTRIBUTE));
    if (changedElement.hasAttribute(SOAPConstants.ENCODING_STYLE_ATTRIBUTE))
    {
      String encodingStyles = changedElement.getAttribute(SOAPConstants.ENCODING_STYLE_ATTRIBUTE);
      StringTokenizer tokenizer = new StringTokenizer(encodingStyles);
      while (tokenizer.hasMoreTokens())
        getEncodingStyles().add(tokenizer.nextToken());
    }

    reconcileReferences(false);
  }

  public void reconcileReferences(boolean deep)
  {
    if (element != null && element.hasAttribute(SOAPConstants.MESSAGE_ATTRIBUTE))
    {
      Definition definition = getEnclosingDefinition();
      QName messageQName = createQName(definition, element.getAttribute(SOAPConstants.MESSAGE_ATTRIBUTE), element);
      Message newMessage = (messageQName != null) ? (Message)definition.getMessage(messageQName) : null;
      if (newMessage != null && newMessage != getMessage())
        setMessage(newMessage);

      if (element.hasAttribute(SOAPConstants.PART_ATTRIBUTE))
      {
        String partName = element.getAttribute(SOAPConstants.PART_ATTRIBUTE);
        Part newPart = (newMessage != null) ? (Part)newMessage.getPart(partName) : null;
        if (newPart != null && newPart != getPart())
          setPart(newPart);
      }
    }
    super.reconcileReferences(deep);
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
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPHeaderBase_Use())
        niceSetAttribute(theElement, SOAPConstants.USE_ATTRIBUTE, getUse());
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPHeaderBase_NamespaceURI())
        niceSetAttribute(theElement, SOAPConstants.NAMESPACE_ATTRIBUTE, getNamespaceURI());
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPHeaderBase_EncodingStyles())
      {
        List encodingStyleList = getEncodingStyles();
        String encodingStyles = "";
        Iterator iterator = encodingStyleList.iterator();
        while (iterator.hasNext())
        {
          if (encodingStyles.equals("")) // first iteration
            encodingStyles += (String)iterator.next();
          else
            encodingStyles += " " + (String)iterator.next();
        }
        if (!encodingStyles.equals(""))
          niceSetAttribute(theElement, SOAPConstants.ENCODING_STYLE_ATTRIBUTE, encodingStyles);
      }
    }
  }

  protected void changeReference(EReference eReference)
  {
    if (isReconciling)
      return;

    super.changeReference(eReference);
    Element theElement = getElement();
    if (theElement != null)
    {
      if (eReference == null || eReference == SOAPPackage.eINSTANCE.getSOAPHeaderBase_Message())
      {
        Message message = getMessage();
        if (message != null)
        {
          QName qName = message.getQName();
          niceSetAttributeURIValue(theElement, SOAPConstants.MESSAGE_ATTRIBUTE, qName.getNamespaceURI() + "#" + qName.getLocalPart());
        }
      }

      if (eReference == null || eReference == SOAPPackage.eINSTANCE.getSOAPHeaderBase_Part())
      {
        Part part = getPart();
        if (part != null)
        {
          String partName = part.getName();
          niceSetAttribute(theElement, SOAPConstants.PART_ATTRIBUTE, partName);
        }
      }
    }
  }
} //SOAPHeaderBaseImpl
