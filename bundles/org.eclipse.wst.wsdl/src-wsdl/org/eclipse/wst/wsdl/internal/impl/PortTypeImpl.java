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

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Port Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.PortTypeImpl#getQName <em>QName</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.PortTypeImpl#isUndefined <em>Undefined</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.PortTypeImpl#isProxy <em>Proxy</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.PortTypeImpl#getResourceURI <em>Resource URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.PortTypeImpl#getEOperations <em>EOperations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PortTypeImpl extends WSDLElementImpl implements PortType
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default value of the '{@link #getQName() <em>QName</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQName()
   * @generated
   * @ordered
   */
  protected static final QName QNAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getQName() <em>QName</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getQName()
   * @generated
   * @ordered
   */
  protected QName qName = QNAME_EDEFAULT;

  /**
   * The default value of the '{@link #isUndefined() <em>Undefined</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUndefined()
   * @generated
   * @ordered
   */
  protected static final boolean UNDEFINED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isUndefined() <em>Undefined</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUndefined()
   * @generated
   * @ordered
   */
  protected boolean undefined = UNDEFINED_EDEFAULT;

  /**
   * The default value of the '{@link #isProxy() <em>Proxy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isProxy()
   * @generated
   * @ordered
   */
  protected static final boolean PROXY_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isProxy() <em>Proxy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isProxy()
   * @generated
   * @ordered
   */
  protected boolean proxy = PROXY_EDEFAULT;

  /**
   * The default value of the '{@link #getResourceURI() <em>Resource URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResourceURI()
   * @generated
   * @ordered
   */
  protected static final String RESOURCE_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getResourceURI() <em>Resource URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getResourceURI()
   * @generated
   * @ordered
   */
  protected String resourceURI = RESOURCE_URI_EDEFAULT;

  /**
   * The cached value of the '{@link #getEOperations() <em>EOperations</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEOperations()
   * @generated
   * @ordered
   */
  protected EList eOperations = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PortTypeImpl()
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
    return WSDLPackage.eINSTANCE.getPortType();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public QName getQName()
  {
    return qName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setQName(QName newQName)
  {
    QName oldQName = qName;
    qName = newQName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.PORT_TYPE__QNAME, oldQName, qName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isUndefined()
  {
    return undefined;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUndefined(boolean newUndefined)
  {
    boolean oldUndefined = undefined;
    undefined = newUndefined;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.PORT_TYPE__UNDEFINED, oldUndefined, undefined));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isProxy()
  {
    return proxy;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProxy(boolean newProxy)
  {
    boolean oldProxy = proxy;
    proxy = newProxy;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.PORT_TYPE__PROXY, oldProxy, proxy));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getResourceURI()
  {
    return resourceURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setResourceURI(String newResourceURI)
  {
    String oldResourceURI = resourceURI;
    resourceURI = newResourceURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.PORT_TYPE__RESOURCE_URI, oldResourceURI, resourceURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEOperations()
  {
    if (eOperations == null)
    {
      eOperations = new EObjectContainmentEList(Operation.class, this, WSDLPackage.PORT_TYPE__EOPERATIONS);
    }
    return eOperations;
  }

  /**
   * <!-- begin-user-doc -->
   * Add an operation to this port type.
   * @param operation the operation to be added
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addOperation(javax.wsdl.Operation operation)
  {
    if (!operation.isUndefined())
      getEOperations().add(operation);
  }

  /**
   * <!-- begin-user-doc -->
   * Get the specified operation. Note that operation names can
   * be overloaded within a PortType. In case of overloading, the
   * names of the input and output messages can be used to further
   * refine the search.
   * @param name the name of the desired operation.
   * @param inputName the name of the input message; if this is null
   *        it will be ignored.
   * @param outputName the name of the output message; if this is null
   *        it will be ignored.
   * @return the corresponding operation, or null if there wasn't
   *         any matching operation
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Operation getOperation(String name, String inputName, String outputName)
  {
    Iterator opIterator = getOperations().iterator();
    while (opIterator.hasNext())
    {
      javax.wsdl.Operation op = (javax.wsdl.Operation)opIterator.next();
      if (op == null)
        continue;

      String opName = op.getName();
      if (name != null && opName != null)
      {
        if (!name.equals(opName))
          op = null;
      }
      else if (name != null || opName != null)
        op = null;

      if (op != null && inputName != null)
      {
        javax.wsdl.Input input = op.getInput();
        if (input != null)
        {
          String opInputName = input.getName();
          if (opInputName == null || !opInputName.equals(inputName))
            op = null;
        }
        else
          op = null;
      }

      if (op != null && outputName != null)
      {
        javax.wsdl.Output output = op.getOutput();
        if (output != null)
        {
          String opOutputName = output.getName();
          if (opOutputName == null || !opOutputName.equals(outputName))
            op = null;
        }
        else
          op = null;
      }

      if (op != null)
        return op;
    } // end while
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * Get all the operations defined here.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getOperations()
  {
    if (!isUndefined())
      return getEOperations();
    else
      return new ArrayList();
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
        case WSDLPackage.PORT_TYPE__EOPERATIONS:
        return ((InternalEList)getEOperations()).basicRemove(otherEnd, msgs);
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
      case WSDLPackage.PORT_TYPE__DOCUMENTATION_ELEMENT:
      return getDocumentationElement();
      case WSDLPackage.PORT_TYPE__ELEMENT:
      return getElement();
      case WSDLPackage.PORT_TYPE__QNAME:
      return getQName();
      case WSDLPackage.PORT_TYPE__UNDEFINED:
      return isUndefined() ? Boolean.TRUE : Boolean.FALSE;
      case WSDLPackage.PORT_TYPE__PROXY:
      return isProxy() ? Boolean.TRUE : Boolean.FALSE;
      case WSDLPackage.PORT_TYPE__RESOURCE_URI:
      return getResourceURI();
      case WSDLPackage.PORT_TYPE__EOPERATIONS:
      return getEOperations();
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
      case WSDLPackage.PORT_TYPE__DOCUMENTATION_ELEMENT:
      setDocumentationElement((Element)newValue);
      return;
      case WSDLPackage.PORT_TYPE__ELEMENT:
      setElement((Element)newValue);
      return;
      case WSDLPackage.PORT_TYPE__QNAME:
      setQName((QName)newValue);
      return;
      case WSDLPackage.PORT_TYPE__UNDEFINED:
      setUndefined(((Boolean)newValue).booleanValue());
      return;
      case WSDLPackage.PORT_TYPE__PROXY:
      setProxy(((Boolean)newValue).booleanValue());
      return;
      case WSDLPackage.PORT_TYPE__RESOURCE_URI:
      setResourceURI((String)newValue);
      return;
      case WSDLPackage.PORT_TYPE__EOPERATIONS:
      getEOperations().clear();
      getEOperations().addAll((Collection)newValue);
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
      case WSDLPackage.PORT_TYPE__DOCUMENTATION_ELEMENT:
      setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
      return;
      case WSDLPackage.PORT_TYPE__ELEMENT:
      setElement(ELEMENT_EDEFAULT);
      return;
      case WSDLPackage.PORT_TYPE__QNAME:
      setQName(QNAME_EDEFAULT);
      return;
      case WSDLPackage.PORT_TYPE__UNDEFINED:
      setUndefined(UNDEFINED_EDEFAULT);
      return;
      case WSDLPackage.PORT_TYPE__PROXY:
      setProxy(PROXY_EDEFAULT);
      return;
      case WSDLPackage.PORT_TYPE__RESOURCE_URI:
      setResourceURI(RESOURCE_URI_EDEFAULT);
      return;
      case WSDLPackage.PORT_TYPE__EOPERATIONS:
      getEOperations().clear();
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
      case WSDLPackage.PORT_TYPE__DOCUMENTATION_ELEMENT:
      return DOCUMENTATION_ELEMENT_EDEFAULT == null
        ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case WSDLPackage.PORT_TYPE__ELEMENT:
      return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case WSDLPackage.PORT_TYPE__QNAME:
      return QNAME_EDEFAULT == null ? qName != null : !QNAME_EDEFAULT.equals(qName);
      case WSDLPackage.PORT_TYPE__UNDEFINED:
      return undefined != UNDEFINED_EDEFAULT;
      case WSDLPackage.PORT_TYPE__PROXY:
      return proxy != PROXY_EDEFAULT;
      case WSDLPackage.PORT_TYPE__RESOURCE_URI:
      return RESOURCE_URI_EDEFAULT == null ? resourceURI != null : !RESOURCE_URI_EDEFAULT.equals(resourceURI);
      case WSDLPackage.PORT_TYPE__EOPERATIONS:
      return eOperations != null && !eOperations.isEmpty();
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
    result.append(" (qName: ");
    result.append(qName);
    result.append(", undefined: ");
    result.append(undefined);
    result.append(", proxy: ");
    result.append(proxy);
    result.append(", resourceURI: ");
    result.append(resourceURI);
    result.append(')');
    return result.toString();
  }

  //
  // Reconcile methods
  //

  public void reconcileAttributes(Element changedElement)
  {
    Definition definition = getEnclosingDefinition();
    String name = changedElement.getAttribute(WSDLConstants.NAME_ATTRIBUTE);
    QName qname = new QName(definition.getTargetNamespace(), name == null ? "" : name); //$NON-NLS-1$
    setQName(qname);
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    switch (WSDLUtil.getInstance().getWSDLType(child))
    {
      case WSDLConstants.OPERATION:
      {
        Operation operation = WSDLFactory.eINSTANCE.createOperation();
        operation.setEnclosingDefinition(getEnclosingDefinition());
        operation.setElement(child);
        getEOperations().add(operation);
        break;
      }
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
    List list = getList(component, modelObject);
    if (list != null)
    {
      list.remove(modelObject);
    }
  }

  private List getList(Object component, Object modelObject)
  {
    List result = null;
    PortType portType = (PortType)component;
    if (modelObject instanceof Operation)
    {
      result = portType.getEOperations();
    }
    return result;
  }

  public Collection getModelObjects(Object component)
  {
    PortType portType = (PortType)component;

    List list = portType.getEOperations();
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
      if (eAttribute == null || eAttribute == WSDLPackage.eINSTANCE.getPortType_QName())
        niceSetAttribute(theElement, WSDLConstants.NAME_ATTRIBUTE, getQName().getLocalPart());
    }
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.PORT_TYPE);
    setElement(newElement);

    Iterator iterator = getEOperations().iterator();
    while (iterator.hasNext())
    {
      Operation operation = (Operation)iterator.next();
      Element child = ((OperationImpl)operation).createElement();
      newElement.appendChild(child);
    }

    return newElement;
  }
} //PortTypeImpl
