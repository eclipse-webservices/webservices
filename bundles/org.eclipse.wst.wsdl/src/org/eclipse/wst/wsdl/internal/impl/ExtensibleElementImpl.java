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
package org.eclipse.wst.wsdl.internal.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.ExtensibleElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Extensible Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.impl.ExtensibleElementImpl#getEExtensibilityElements <em>EExtensibility Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ExtensibleElementImpl extends WSDLElementImpl implements ExtensibleElement
{
  /**
   * The cached value of the '{@link #getEExtensibilityElements() <em>EExtensibility Elements</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEExtensibilityElements()
   * @generated
   * @ordered
   */
  protected EList eExtensibilityElements = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ExtensibleElementImpl()
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
    return WSDLPackage.eINSTANCE.getExtensibleElement();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEExtensibilityElements()
  {
    if (eExtensibilityElements == null)
    {
      eExtensibilityElements = new EObjectContainmentEList(ExtensibilityElement.class, this, WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS);
    }
    return eExtensibilityElements;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getExtensibilityElements()
  {
    return getEExtensibilityElements();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addExtensibilityElement(javax.wsdl.extensions.ExtensibilityElement extElement)
  {
    getExtensibilityElements().add(extElement);
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
        case WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS:
          return ((InternalEList)getEExtensibilityElements()).basicRemove(otherEnd, msgs);
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
      case WSDLPackage.EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case WSDLPackage.EXTENSIBLE_ELEMENT__ELEMENT:
        return getElement();
      case WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS:
        return getEExtensibilityElements();
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
      case WSDLPackage.EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case WSDLPackage.EXTENSIBLE_ELEMENT__ELEMENT:
        setElement((Element)newValue);
        return;
      case WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS:
        getEExtensibilityElements().clear();
        getEExtensibilityElements().addAll((Collection)newValue);
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
      case WSDLPackage.EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.EXTENSIBLE_ELEMENT__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS:
        getEExtensibilityElements().clear();
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
      case WSDLPackage.EXTENSIBLE_ELEMENT__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case WSDLPackage.EXTENSIBLE_ELEMENT__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case WSDLPackage.EXTENSIBLE_ELEMENT__EEXTENSIBILITY_ELEMENTS:
        return eExtensibilityElements != null && !eExtensibilityElements.isEmpty();
    }
    return eDynamicIsSet(eFeature);
  }

  //
  //
  //
  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    if (!WSDLConstants.isMatchingNamespace(child.getNamespaceURI(), WSDLConstants.WSDL_NAMESPACE_URI))
    {  
      ExtensibilityElement extensibilityElement = useExtensionFactories() ? 
          ((WSDLFactoryImpl)WSDLFactory.eINSTANCE).createExtensibilityElement(getNamespace(child),getLocalName(child)) :
            ((WSDLFactoryImpl)WSDLFactory.eINSTANCE).createUnknownExtensibilityElement();
          
          extensibilityElement.setEnclosingDefinition(getEnclosingDefinition());
          extensibilityElement.setElement(child);
          getEExtensibilityElements().add(extensibilityElement);
    }
  }
  
  private boolean useExtensionFactories()
  {
  	// Use extension factories by default.
    return getEnclosingDefinition() == null ? 
      true : 
      ((DefinitionImpl)getEnclosingDefinition()).getUseExtensionFactories();
  }
  
} //ExtensibleElementImpl
