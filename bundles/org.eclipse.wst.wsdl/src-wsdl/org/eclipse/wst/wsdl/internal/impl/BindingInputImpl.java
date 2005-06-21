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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Binding Input</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.impl.BindingInputImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.BindingInputImpl#getEInput <em>EInput</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BindingInputImpl extends ExtensibleElementImpl implements BindingInput
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getEInput() <em>EInput</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEInput()
   * @generated
   * @ordered
   */
  protected Input eInput = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BindingInputImpl()
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
    return WSDLPackage.eINSTANCE.getBindingInput();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING_INPUT__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Input getEInput()
  {
    if (eInput != null && eInput.eIsProxy())
    {
      Input oldEInput = eInput;
      eInput = (Input)eResolveProxy((InternalEObject)eInput);
      if (eInput != oldEInput)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, WSDLPackage.BINDING_INPUT__EINPUT, oldEInput, eInput));
      }
    }
    return eInput;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Input basicGetEInput()
  {
    return eInput;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEInput(Input newEInput)
  {
    Input oldEInput = eInput;
    eInput = newEInput;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING_INPUT__EINPUT, oldEInput, eInput));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Input getInput()
  {
    return getEInput();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setInput(javax.wsdl.Input input)
  {
    setEInput((Input) input);
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
        case WSDLPackage.BINDING_INPUT__EEXTENSIBILITY_ELEMENTS:
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
      case WSDLPackage.BINDING_INPUT__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case WSDLPackage.BINDING_INPUT__ELEMENT:
        return getElement();
      case WSDLPackage.BINDING_INPUT__EEXTENSIBILITY_ELEMENTS:
        return getEExtensibilityElements();
      case WSDLPackage.BINDING_INPUT__NAME:
        return getName();
      case WSDLPackage.BINDING_INPUT__EINPUT:
        if (resolve) return getEInput();
        return basicGetEInput();
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
      case WSDLPackage.BINDING_INPUT__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case WSDLPackage.BINDING_INPUT__ELEMENT:
        setElement((Element)newValue);
        return;
      case WSDLPackage.BINDING_INPUT__EEXTENSIBILITY_ELEMENTS:
        getEExtensibilityElements().clear();
        getEExtensibilityElements().addAll((Collection)newValue);
        return;
      case WSDLPackage.BINDING_INPUT__NAME:
        setName((String)newValue);
        return;
      case WSDLPackage.BINDING_INPUT__EINPUT:
        setEInput((Input)newValue);
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
      case WSDLPackage.BINDING_INPUT__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.BINDING_INPUT__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.BINDING_INPUT__EEXTENSIBILITY_ELEMENTS:
        getEExtensibilityElements().clear();
        return;
      case WSDLPackage.BINDING_INPUT__NAME:
        setName(NAME_EDEFAULT);
        return;
      case WSDLPackage.BINDING_INPUT__EINPUT:
        setEInput((Input)null);
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
      case WSDLPackage.BINDING_INPUT__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case WSDLPackage.BINDING_INPUT__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case WSDLPackage.BINDING_INPUT__EEXTENSIBILITY_ELEMENTS:
        return eExtensibilityElements != null && !eExtensibilityElements.isEmpty();
      case WSDLPackage.BINDING_INPUT__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case WSDLPackage.BINDING_INPUT__EINPUT:
        return eInput != null;
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
    result.append(" (name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

  //
  // Reconciliation methods
  //

  public void reconcileAttributes(Element changedElement)
  {
    String name = changedElement.getAttribute("name");
    if (name != null)
    {
      setName(name);
    }
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    super.handleUnreconciledElement(child,remainingModelObjects);
  }

  protected void handleReconciliation(Collection remainingModelObjects)
  {
    for (Iterator i = remainingModelObjects.iterator(); i.hasNext();)
    {
      getEExtensibilityElements().remove(i.next());
    }
  }

  public Collection getModelObjects(Object component)
  {
    BindingInput bindingInput = (BindingInput) component;
    List list = new ArrayList();
    list.addAll(bindingInput.getEExtensibilityElements());
    return list;
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
      if (eAttribute == null || eAttribute == WSDLPackage.eINSTANCE.getBindingInput_Name())
        niceSetAttribute(theElement, WSDLConstants.NAME_ATTRIBUTE, getName());
    }
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.INPUT);
    setElement(newElement);

    Iterator iterator = getExtensibilityElements().iterator();
    while (iterator.hasNext())
    {
      ExtensibilityElement extensibilityElement = (ExtensibilityElement) iterator.next();
      Element child = ((ExtensibilityElementImpl) extensibilityElement).createElement();
      newElement.appendChild(child);
    }

    return newElement;
  }
  
  // Resolve the reference to Input
  public void reconcileReferences(boolean deep)
  {
    if (element != null)
    {
      BindingOperation bop = (BindingOperation)getContainer();
      Operation op = bop.getEOperation();
      if (op != null)
      {
        setInput(op.getEInput());
      }
    }
    super.reconcileReferences(deep);
  }
} //BindingInputImpl
