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
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Binding Output</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.impl.BindingOutputImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.BindingOutputImpl#getEOutput <em>EOutput</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BindingOutputImpl extends ExtensibleElementImpl implements BindingOutput
{
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
   * The cached value of the '{@link #getEOutput() <em>EOutput</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEOutput()
   * @generated
   * @ordered
   */
  protected Output eOutput = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BindingOutputImpl()
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
    return WSDLPackage.eINSTANCE.getBindingOutput();
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING_OUTPUT__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Output getEOutput()
  {
    if (eOutput != null && eOutput.eIsProxy())
    {
      Output oldEOutput = eOutput;
      eOutput = (Output)eResolveProxy((InternalEObject)eOutput);
      if (eOutput != oldEOutput)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, WSDLPackage.BINDING_OUTPUT__EOUTPUT, oldEOutput, eOutput));
      }
    }
    return eOutput;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Output basicGetEOutput()
  {
    return eOutput;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEOutput(Output newEOutput)
  {
    Output oldEOutput = eOutput;
    eOutput = newEOutput;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING_OUTPUT__EOUTPUT, oldEOutput, eOutput));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Output getOutput()
  {
    return getEOutput();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setOutput(javax.wsdl.Output output)
  {
    setEOutput((Output) output);
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
        case WSDLPackage.BINDING_OUTPUT__EEXTENSIBILITY_ELEMENTS:
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
      case WSDLPackage.BINDING_OUTPUT__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case WSDLPackage.BINDING_OUTPUT__ELEMENT:
        return getElement();
      case WSDLPackage.BINDING_OUTPUT__EEXTENSIBILITY_ELEMENTS:
        return getEExtensibilityElements();
      case WSDLPackage.BINDING_OUTPUT__NAME:
        return getName();
      case WSDLPackage.BINDING_OUTPUT__EOUTPUT:
        if (resolve) return getEOutput();
        return basicGetEOutput();
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
      case WSDLPackage.BINDING_OUTPUT__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case WSDLPackage.BINDING_OUTPUT__ELEMENT:
        setElement((Element)newValue);
        return;
      case WSDLPackage.BINDING_OUTPUT__EEXTENSIBILITY_ELEMENTS:
        getEExtensibilityElements().clear();
        getEExtensibilityElements().addAll((Collection)newValue);
        return;
      case WSDLPackage.BINDING_OUTPUT__NAME:
        setName((String)newValue);
        return;
      case WSDLPackage.BINDING_OUTPUT__EOUTPUT:
        setEOutput((Output)newValue);
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
      case WSDLPackage.BINDING_OUTPUT__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.BINDING_OUTPUT__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.BINDING_OUTPUT__EEXTENSIBILITY_ELEMENTS:
        getEExtensibilityElements().clear();
        return;
      case WSDLPackage.BINDING_OUTPUT__NAME:
        setName(NAME_EDEFAULT);
        return;
      case WSDLPackage.BINDING_OUTPUT__EOUTPUT:
        setEOutput((Output)null);
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
      case WSDLPackage.BINDING_OUTPUT__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case WSDLPackage.BINDING_OUTPUT__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case WSDLPackage.BINDING_OUTPUT__EEXTENSIBILITY_ELEMENTS:
        return eExtensibilityElements != null && !eExtensibilityElements.isEmpty();
      case WSDLPackage.BINDING_OUTPUT__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case WSDLPackage.BINDING_OUTPUT__EOUTPUT:
        return eOutput != null;
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
  // Reconcile methods
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
    BindingOutput bindingOutput = (BindingOutput) component;
    List list = new ArrayList();
    list.addAll(bindingOutput.getEExtensibilityElements());
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
      if (eAttribute == null || eAttribute == WSDLPackage.eINSTANCE.getBindingOutput_Name())
        niceSetAttribute(theElement, WSDLConstants.NAME_ATTRIBUTE, getName());
    }
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.OUTPUT);
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
  
  // Resolve the reference to Output
  public void reconcileReferences(boolean deep)
  {
    if (element != null)
    {
      BindingOperation bop = (BindingOperation)getContainer();
      Operation op = bop.getEOperation();
      if (op != null)
      {
        setOutput(op.getEOutput());
      }
    }
    super.reconcileReferences(deep);
  }
} //BindingOutputImpl
