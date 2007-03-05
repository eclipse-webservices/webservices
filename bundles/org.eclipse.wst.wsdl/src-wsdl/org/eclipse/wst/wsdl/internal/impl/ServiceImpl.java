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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Port;
import org.eclipse.wst.wsdl.Service;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Service</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.impl.ServiceImpl#getQName <em>QName</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.ServiceImpl#isUndefined <em>Undefined</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.ServiceImpl#isProxy <em>Proxy</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.ServiceImpl#getResourceURI <em>Resource URI</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.ServiceImpl#getEPorts <em>EPorts</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ServiceImpl extends ExtensibleElementImpl implements Service
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
   * The cached value of the '{@link #getEPorts() <em>EPorts</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEPorts()
   * @generated
   * @ordered
   */
  protected EList ePorts = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ServiceImpl()
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
    return WSDLPackage.eINSTANCE.getService();
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.SERVICE__QNAME, oldQName, qName));
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.SERVICE__UNDEFINED, oldUndefined, undefined));
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.SERVICE__PROXY, oldProxy, proxy));
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.SERVICE__RESOURCE_URI, oldResourceURI, resourceURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEPorts()
  {
    if (ePorts == null)
    {
      ePorts = new EObjectContainmentEList(Port.class, this, WSDLPackage.SERVICE__EPORTS);
    }
    return ePorts;
  }

  /**
   * <!-- begin-user-doc -->
   * Add a port to this service.
   * @param port the port to be added
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addPort(javax.wsdl.Port port)
  {
    getEPorts().add((Port)port);
  }

  /**
   * <!-- begin-user-doc -->
   * Get all the ports defined here.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Map getPorts()
  {
    HashMap hashMap = new HashMap();
    for (Iterator i = getEPorts().iterator(); i.hasNext();)
    {
      Port port = (Port)i.next();
      hashMap.put(port.getName(), port);
    }
    return hashMap;
  }

  /**
   * <!-- begin-user-doc -->
   * Get the specified port.
   *
   * @param name the name of the desired port.
   * @return the corresponding port, or null if there wasn't
   * any matching port
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Port getPort(String name)
  {
    Port result = null;
    for (Iterator i = getEPorts().iterator(); i.hasNext();)
    {
      Port port = (Port)i.next();
      if (name.equals(port.getName()))
      {
        result = port;
        break;
      }
    }
    return result;
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
        case WSDLPackage.SERVICE__EEXTENSIBILITY_ELEMENTS:
        return ((InternalEList)getEExtensibilityElements()).basicRemove(otherEnd, msgs);
        case WSDLPackage.SERVICE__EPORTS:
        return ((InternalEList)getEPorts()).basicRemove(otherEnd, msgs);
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
      case WSDLPackage.SERVICE__DOCUMENTATION_ELEMENT:
      return getDocumentationElement();
      case WSDLPackage.SERVICE__ELEMENT:
      return getElement();
      case WSDLPackage.SERVICE__EEXTENSIBILITY_ELEMENTS:
      return getEExtensibilityElements();
      case WSDLPackage.SERVICE__QNAME:
      return getQName();
      case WSDLPackage.SERVICE__UNDEFINED:
      return isUndefined() ? Boolean.TRUE : Boolean.FALSE;
      case WSDLPackage.SERVICE__PROXY:
      return isProxy() ? Boolean.TRUE : Boolean.FALSE;
      case WSDLPackage.SERVICE__RESOURCE_URI:
      return getResourceURI();
      case WSDLPackage.SERVICE__EPORTS:
      return getEPorts();
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
      case WSDLPackage.SERVICE__DOCUMENTATION_ELEMENT:
      setDocumentationElement((Element)newValue);
      return;
      case WSDLPackage.SERVICE__ELEMENT:
      setElement((Element)newValue);
      return;
      case WSDLPackage.SERVICE__EEXTENSIBILITY_ELEMENTS:
      getEExtensibilityElements().clear();
      getEExtensibilityElements().addAll((Collection)newValue);
      return;
      case WSDLPackage.SERVICE__QNAME:
      setQName((QName)newValue);
      return;
      case WSDLPackage.SERVICE__UNDEFINED:
      setUndefined(((Boolean)newValue).booleanValue());
      return;
      case WSDLPackage.SERVICE__PROXY:
      setProxy(((Boolean)newValue).booleanValue());
      return;
      case WSDLPackage.SERVICE__RESOURCE_URI:
      setResourceURI((String)newValue);
      return;
      case WSDLPackage.SERVICE__EPORTS:
      getEPorts().clear();
      getEPorts().addAll((Collection)newValue);
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
      case WSDLPackage.SERVICE__DOCUMENTATION_ELEMENT:
      setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
      return;
      case WSDLPackage.SERVICE__ELEMENT:
      setElement(ELEMENT_EDEFAULT);
      return;
      case WSDLPackage.SERVICE__EEXTENSIBILITY_ELEMENTS:
      getEExtensibilityElements().clear();
      return;
      case WSDLPackage.SERVICE__QNAME:
      setQName(QNAME_EDEFAULT);
      return;
      case WSDLPackage.SERVICE__UNDEFINED:
      setUndefined(UNDEFINED_EDEFAULT);
      return;
      case WSDLPackage.SERVICE__PROXY:
      setProxy(PROXY_EDEFAULT);
      return;
      case WSDLPackage.SERVICE__RESOURCE_URI:
      setResourceURI(RESOURCE_URI_EDEFAULT);
      return;
      case WSDLPackage.SERVICE__EPORTS:
      getEPorts().clear();
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
      case WSDLPackage.SERVICE__DOCUMENTATION_ELEMENT:
      return DOCUMENTATION_ELEMENT_EDEFAULT == null
        ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case WSDLPackage.SERVICE__ELEMENT:
      return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case WSDLPackage.SERVICE__EEXTENSIBILITY_ELEMENTS:
      return eExtensibilityElements != null && !eExtensibilityElements.isEmpty();
      case WSDLPackage.SERVICE__QNAME:
      return QNAME_EDEFAULT == null ? qName != null : !QNAME_EDEFAULT.equals(qName);
      case WSDLPackage.SERVICE__UNDEFINED:
      return undefined != UNDEFINED_EDEFAULT;
      case WSDLPackage.SERVICE__PROXY:
      return proxy != PROXY_EDEFAULT;
      case WSDLPackage.SERVICE__RESOURCE_URI:
      return RESOURCE_URI_EDEFAULT == null ? resourceURI != null : !RESOURCE_URI_EDEFAULT.equals(resourceURI);
      case WSDLPackage.SERVICE__EPORTS:
      return ePorts != null && !ePorts.isEmpty();
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
      case WSDLConstants.PORT:
      {
        Port port = (Port)((WSDLPackage)EPackage.Registry.INSTANCE.getEPackage(WSDLPackage.eNS_URI)).getWSDLFactory().createPort();
        port.setEnclosingDefinition(getEnclosingDefinition());
        getEPorts().add(port);
        port.setElement(child);
        break;
      }
      case WSDLConstants.DOCUMENTATION:
      {
        setDocumentationElement(child);
        break;
      }
      default:
      {
        super.handleUnreconciledElement(child, remainingModelObjects);
        break;
      }
    }
  }

  protected void handleReconciliation(Collection remainingModelObjects)
  {
    for (Iterator i = remainingModelObjects.iterator(); i.hasNext();)
    {
      remove(i.next());
    }
  }

  private void remove(Object modelObject)
  {
    List list = getList(modelObject);
    if (list != null)
    {
      list.remove(modelObject);
    }
  }

  private List getList(Object modelObject)
  {
    List result = null;
    if (modelObject instanceof Port)
    {
      result = getEPorts();
    }
    else if (modelObject instanceof ExtensibilityElement)
    {
      result = getExtensibilityElements();
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
      if (eAttribute == null || eAttribute == WSDLPackage.eINSTANCE.getService_QName())
        niceSetAttribute(theElement, WSDLConstants.NAME_ATTRIBUTE, getQName().getLocalPart());
    }
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.SERVICE);
    setElement(newElement);

    Iterator iterator = getExtensibilityElements().iterator();
    while (iterator.hasNext())
    {
      ExtensibilityElement extensibilityElement = (ExtensibilityElement)iterator.next();
      Element child = ((ExtensibilityElementImpl)extensibilityElement).createElement();
      newElement.appendChild(child);
    }

    iterator = getEPorts().iterator();
    while (iterator.hasNext())
    {
      Port port = (Port)iterator.next();
      Element child = ((PortImpl)port).createElement();
      newElement.appendChild(child);
    }

    return newElement;
  }

} //ServiceImpl
