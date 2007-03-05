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
package org.eclipse.wst.wsdl.binding.mime.internal.impl;


import org.eclipse.wst.wsdl.binding.mime.internal.util.MIMEConstants;
import org.eclipse.wst.wsdl.binding.mime.MIMEFactory;

import java.util.Collection;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.binding.mime.MIMEMultipartRelated;
import org.eclipse.wst.wsdl.binding.mime.MIMEPackage;
import org.eclipse.wst.wsdl.binding.mime.MIMEPart;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multipart Related</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.mime.internal.impl.MIMEMultipartRelatedImpl#getEMIMEPart <em>EMIME Part</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MIMEMultipartRelatedImpl extends ExtensibilityElementImpl implements MIMEMultipartRelated
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The cached value of the '{@link #getEMIMEPart() <em>EMIME Part</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEMIMEPart()
   * @generated
   * @ordered
   */
  protected EList eMIMEPart = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MIMEMultipartRelatedImpl()
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
    return MIMEPackage.eINSTANCE.getMIMEMultipartRelated();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEMIMEPart()
  {
    if (eMIMEPart == null)
    {
      eMIMEPart = new EObjectContainmentEList(MIMEPart.class, this, MIMEPackage.MIME_MULTIPART_RELATED__EMIME_PART);
    }
    return eMIMEPart;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addMIMEPart(javax.wsdl.extensions.mime.MIMEPart mimePart)
  {
    getEMIMEPart().add(mimePart);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getMIMEParts()
  {
    // TODO: implement this method
    // Ensure that you remove @generated or mark it @generated NOT
    throw new UnsupportedOperationException();
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
        case MIMEPackage.MIME_MULTIPART_RELATED__EMIME_PART:
        return ((InternalEList)getEMIMEPart()).basicRemove(otherEnd, msgs);
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
      case MIMEPackage.MIME_MULTIPART_RELATED__DOCUMENTATION_ELEMENT:
      return getDocumentationElement();
      case MIMEPackage.MIME_MULTIPART_RELATED__ELEMENT:
      return getElement();
      case MIMEPackage.MIME_MULTIPART_RELATED__REQUIRED:
      return isRequired() ? Boolean.TRUE : Boolean.FALSE;
      case MIMEPackage.MIME_MULTIPART_RELATED__ELEMENT_TYPE:
      return getElementType();
      case MIMEPackage.MIME_MULTIPART_RELATED__EMIME_PART:
      return getEMIMEPart();
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
      case MIMEPackage.MIME_MULTIPART_RELATED__DOCUMENTATION_ELEMENT:
      setDocumentationElement((Element)newValue);
      return;
      case MIMEPackage.MIME_MULTIPART_RELATED__ELEMENT:
      setElement((Element)newValue);
      return;
      case MIMEPackage.MIME_MULTIPART_RELATED__REQUIRED:
      setRequired(((Boolean)newValue).booleanValue());
      return;
      case MIMEPackage.MIME_MULTIPART_RELATED__ELEMENT_TYPE:
      setElementType((QName)newValue);
      return;
      case MIMEPackage.MIME_MULTIPART_RELATED__EMIME_PART:
      getEMIMEPart().clear();
      getEMIMEPart().addAll((Collection)newValue);
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
      case MIMEPackage.MIME_MULTIPART_RELATED__DOCUMENTATION_ELEMENT:
      setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
      return;
      case MIMEPackage.MIME_MULTIPART_RELATED__ELEMENT:
      setElement(ELEMENT_EDEFAULT);
      return;
      case MIMEPackage.MIME_MULTIPART_RELATED__REQUIRED:
      setRequired(REQUIRED_EDEFAULT);
      return;
      case MIMEPackage.MIME_MULTIPART_RELATED__ELEMENT_TYPE:
      setElementType(ELEMENT_TYPE_EDEFAULT);
      return;
      case MIMEPackage.MIME_MULTIPART_RELATED__EMIME_PART:
      getEMIMEPart().clear();
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
      case MIMEPackage.MIME_MULTIPART_RELATED__DOCUMENTATION_ELEMENT:
      return DOCUMENTATION_ELEMENT_EDEFAULT == null
        ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case MIMEPackage.MIME_MULTIPART_RELATED__ELEMENT:
      return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case MIMEPackage.MIME_MULTIPART_RELATED__REQUIRED:
      return required != REQUIRED_EDEFAULT;
      case MIMEPackage.MIME_MULTIPART_RELATED__ELEMENT_TYPE:
      return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
      case MIMEPackage.MIME_MULTIPART_RELATED__EMIME_PART:
      return eMIMEPart != null && !eMIMEPart.isEmpty();
    }
    return eDynamicIsSet(eFeature);
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    if (MIMEConstants.PART_ELEMENT_TAG.equals(child.getLocalName()))
    {
      MIMEPart mimePart = MIMEFactory.eINSTANCE.createMIMEPart();
      mimePart.setEnclosingDefinition(getEnclosingDefinition());
      mimePart.setElement(child);
      addMIMEPart(mimePart);
    }
  }

  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(MIMEConstants.MIME_NAMESPACE_URI, MIMEConstants.MULTIPART_RELATED_ELEMENT_TAG);
    return elementType;
  }

} //MIMEMultipartRelatedImpl
