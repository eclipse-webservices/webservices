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
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Binding;
import org.eclipse.wst.wsdl.BindingOperation;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.PortType;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Binding</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.impl.BindingImpl#getQName <em>QName</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.BindingImpl#isUndefined <em>Undefined</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.BindingImpl#isProxy <em>Proxy</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.BindingImpl#getResourceURI <em>Resource URI</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.BindingImpl#getEPortType <em>EPort Type</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.BindingImpl#getEBindingOperations <em>EBinding Operations</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BindingImpl extends ExtensibleElementImpl implements Binding
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
   * The cached value of the '{@link #getEPortType() <em>EPort Type</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEPortType()
   * @generated
   * @ordered
   */
  protected PortType ePortType = null;

  /**
   * The cached value of the '{@link #getEBindingOperations() <em>EBinding Operations</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEBindingOperations()
   * @generated
   * @ordered
   */
  protected EList eBindingOperations = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BindingImpl()
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
    return WSDLPackage.eINSTANCE.getBinding();
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING__QNAME, oldQName, qName));
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING__UNDEFINED, oldUndefined, undefined));
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING__PROXY, oldProxy, proxy));
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING__RESOURCE_URI, oldResourceURI, resourceURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PortType getEPortType()
  {
    if (ePortType != null && ePortType.eIsProxy())
    {
      PortType oldEPortType = ePortType;
      ePortType = (PortType)eResolveProxy((InternalEObject)ePortType);
      if (ePortType != oldEPortType)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, WSDLPackage.BINDING__EPORT_TYPE, oldEPortType, ePortType));
      }
    }
    return ePortType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PortType basicGetEPortType()
  {
    return ePortType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEPortType(PortType newEPortType)
  {
    PortType oldEPortType = ePortType;
    ePortType = newEPortType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.BINDING__EPORT_TYPE, oldEPortType, ePortType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEBindingOperations()
  {
    if (eBindingOperations == null)
    {
      eBindingOperations = new EObjectContainmentEList(BindingOperation.class, this, WSDLPackage.BINDING__EBINDING_OPERATIONS);
    }
    return eBindingOperations;
  }

  /**
   * <!-- begin-user-doc -->
   * Add an operation binding to binding.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addBindingOperation(javax.wsdl.BindingOperation bindingOperation)
  {
    getBindingOperations().add(bindingOperation);
  }

  /**
   * <!-- begin-user-doc -->
   * Get the specified operation binding. Note that operation names can
   * be overloaded within a PortType. In case of overloading, the
   * names of the input and output messages can be used to further
   * refine the search.
   * @param name the name of the desired operation binding.
   * @param inputName the name of the input message; if this is null
   * it will be ignored.
   * @param outputName the name of the output message; if this is null
   * it will be ignored.
   * @return the corresponding operation binding, or null if there wasn't
   * any matching operation binding
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.BindingOperation getBindingOperation(String name, String inputName, String outputName)
  {
    Iterator opBindingIterator = getBindingOperations().iterator();
    while (opBindingIterator.hasNext())
    {
      javax.wsdl.BindingOperation op = (javax.wsdl.BindingOperation) opBindingIterator.next();
      if (op == null)
        continue;

      String opName = op.getName();
      if (name != null && opName != null)
      {
        if (!name.equals(opName))
          op = null;
      }
      else if (name != null || opName != null)
      {
        op = null;
      }

      if (op != null && inputName != null)
      {
        javax.wsdl.BindingInput input = op.getBindingInput();
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
        javax.wsdl.BindingOutput output = op.getBindingOutput();
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
    return null; // binding operation not found
  }

  /**
   * <!-- begin-user-doc -->
   * Get all the operation bindings defined here.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getBindingOperations()
  {
    return getEBindingOperations();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.PortType getPortType()
  {
    return getEPortType();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setPortType(javax.wsdl.PortType portType)
  {
    setEPortType((PortType) portType);
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
        case WSDLPackage.BINDING__EEXTENSIBILITY_ELEMENTS:
          return ((InternalEList)getEExtensibilityElements()).basicRemove(otherEnd, msgs);
        case WSDLPackage.BINDING__EBINDING_OPERATIONS:
          return ((InternalEList)getEBindingOperations()).basicRemove(otherEnd, msgs);
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
      case WSDLPackage.BINDING__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case WSDLPackage.BINDING__ELEMENT:
        return getElement();
      case WSDLPackage.BINDING__EEXTENSIBILITY_ELEMENTS:
        return getEExtensibilityElements();
      case WSDLPackage.BINDING__QNAME:
        return getQName();
      case WSDLPackage.BINDING__UNDEFINED:
        return isUndefined() ? Boolean.TRUE : Boolean.FALSE;
      case WSDLPackage.BINDING__PROXY:
        return isProxy() ? Boolean.TRUE : Boolean.FALSE;
      case WSDLPackage.BINDING__RESOURCE_URI:
        return getResourceURI();
      case WSDLPackage.BINDING__EPORT_TYPE:
        if (resolve) return getEPortType();
        return basicGetEPortType();
      case WSDLPackage.BINDING__EBINDING_OPERATIONS:
        return getEBindingOperations();
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
      case WSDLPackage.BINDING__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case WSDLPackage.BINDING__ELEMENT:
        setElement((Element)newValue);
        return;
      case WSDLPackage.BINDING__EEXTENSIBILITY_ELEMENTS:
        getEExtensibilityElements().clear();
        getEExtensibilityElements().addAll((Collection)newValue);
        return;
      case WSDLPackage.BINDING__QNAME:
        setQName((QName)newValue);
        return;
      case WSDLPackage.BINDING__UNDEFINED:
        setUndefined(((Boolean)newValue).booleanValue());
        return;
      case WSDLPackage.BINDING__PROXY:
        setProxy(((Boolean)newValue).booleanValue());
        return;
      case WSDLPackage.BINDING__RESOURCE_URI:
        setResourceURI((String)newValue);
        return;
      case WSDLPackage.BINDING__EPORT_TYPE:
        setEPortType((PortType)newValue);
        return;
      case WSDLPackage.BINDING__EBINDING_OPERATIONS:
        getEBindingOperations().clear();
        getEBindingOperations().addAll((Collection)newValue);
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
      case WSDLPackage.BINDING__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.BINDING__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.BINDING__EEXTENSIBILITY_ELEMENTS:
        getEExtensibilityElements().clear();
        return;
      case WSDLPackage.BINDING__QNAME:
        setQName(QNAME_EDEFAULT);
        return;
      case WSDLPackage.BINDING__UNDEFINED:
        setUndefined(UNDEFINED_EDEFAULT);
        return;
      case WSDLPackage.BINDING__PROXY:
        setProxy(PROXY_EDEFAULT);
        return;
      case WSDLPackage.BINDING__RESOURCE_URI:
        setResourceURI(RESOURCE_URI_EDEFAULT);
        return;
      case WSDLPackage.BINDING__EPORT_TYPE:
        setEPortType((PortType)null);
        return;
      case WSDLPackage.BINDING__EBINDING_OPERATIONS:
        getEBindingOperations().clear();
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
      case WSDLPackage.BINDING__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case WSDLPackage.BINDING__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case WSDLPackage.BINDING__EEXTENSIBILITY_ELEMENTS:
        return eExtensibilityElements != null && !eExtensibilityElements.isEmpty();
      case WSDLPackage.BINDING__QNAME:
        return QNAME_EDEFAULT == null ? qName != null : !QNAME_EDEFAULT.equals(qName);
      case WSDLPackage.BINDING__UNDEFINED:
        return undefined != UNDEFINED_EDEFAULT;
      case WSDLPackage.BINDING__PROXY:
        return proxy != PROXY_EDEFAULT;
      case WSDLPackage.BINDING__RESOURCE_URI:
        return RESOURCE_URI_EDEFAULT == null ? resourceURI != null : !RESOURCE_URI_EDEFAULT.equals(resourceURI);
      case WSDLPackage.BINDING__EPORT_TYPE:
        return ePortType != null;
      case WSDLPackage.BINDING__EBINDING_OPERATIONS:
        return eBindingOperations != null && !eBindingOperations.isEmpty();
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

    QName qname = new QName(definition.getTargetNamespace(), changedElement.getAttribute("name"));
    setQName(qname);
    reconcileReferences(false);
  }

  public Collection getModelObjects(Object component)
  {
    Binding binding = (Binding) component;

    List list = new ArrayList();
    list.addAll(binding.getEBindingOperations());
    list.addAll(binding.getEExtensibilityElements());
    return list;
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    switch (WSDLUtil.getInstance().getWSDLType(child))
    {
      case WSDLConstants.OPERATION :
        BindingOperation operation = WSDLFactory.eINSTANCE.createBindingOperation();
        operation.setEnclosingDefinition(getEnclosingDefinition());
        operation.setElement(child);
        addBindingOperation(operation);
        break;
      default :
        super.handleUnreconciledElement(child,remainingModelObjects);
        break;
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
    Binding binding = (Binding) component;
    if (modelObject instanceof BindingOperation)
    {
      result = binding.getEBindingOperations();
    }
    else if (modelObject instanceof ExtensibilityElement)
    {
      result = binding.getEExtensibilityElements();
    }
    return result;
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
      if (eAttribute == null || eAttribute == WSDLPackage.eINSTANCE.getBinding_QName())
        niceSetAttribute(theElement, WSDLConstants.NAME_ATTRIBUTE, getQName().getLocalPart());
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
      if (eReference == null || eReference == WSDLPackage.eINSTANCE.getBinding_EPortType())
      {
        PortType portType = getEPortType();
        if (portType != null)
        {
          QName qName = portType.getQName();
          niceSetAttributeURIValue(theElement, WSDLConstants.TYPE_ATTRIBUTE, qName.getNamespaceURI() + "#" + qName.getLocalPart());
        }
      }
    }
  }

  protected Element createElement()
  {
    Element newElement = createElement(WSDLConstants.BINDING);
    setElement(newElement);
	
	Iterator iterator = getExtensibilityElements().iterator();
    while (iterator.hasNext())
    {
      ExtensibilityElement extensibilityElement = (ExtensibilityElement) iterator.next();
      Element child = ((ExtensibilityElementImpl) extensibilityElement).createElement();
      newElement.appendChild(child);
    }
	
    iterator = getEBindingOperations().iterator();
    while (iterator.hasNext())
    {
      BindingOperation operation = (BindingOperation) iterator.next();
      Element child = ((BindingOperationImpl) operation).createElement();
      newElement.appendChild(child);
    }

    return newElement;
  }

  public void reconcileReferences(boolean deep)
  {
    if (element != null)
    {
      Definition definition = (Definition) getEnclosingDefinition();
      QName portTypeQName = createQName(definition, element.getAttribute("type"));
      PortType newPortType = portTypeQName != null ? (PortType) definition.getPortType(portTypeQName) : null;
      if (newPortType != getEPortType())
      {
        setEPortType(newPortType);
      }
    }
    super.reconcileReferences(deep);
  }
} //BindingImpl
