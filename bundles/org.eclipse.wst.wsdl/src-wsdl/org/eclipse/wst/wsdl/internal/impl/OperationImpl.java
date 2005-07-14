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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.OperationType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Fault;
import org.eclipse.wst.wsdl.Input;
import org.eclipse.wst.wsdl.Operation;
import org.eclipse.wst.wsdl.Output;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLUtil;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.impl.OperationImpl#getStyle <em>Style</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.OperationImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.OperationImpl#isUndefined <em>Undefined</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.OperationImpl#isProxy <em>Proxy</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.OperationImpl#getResourceURI <em>Resource URI</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.OperationImpl#getEInput <em>EInput</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.OperationImpl#getEOutput <em>EOutput</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.OperationImpl#getEFaults <em>EFaults</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.OperationImpl#getEParameterOrdering <em>EParameter Ordering</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OperationImpl extends WSDLElementImpl implements Operation
{
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default value of the '{@link #getStyle() <em>Style</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStyle()
   * @generated
   * @ordered
   */
  protected static final OperationType STYLE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getStyle() <em>Style</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStyle()
   * @generated
   * @ordered
   */
  protected OperationType style = STYLE_EDEFAULT;

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
   * The cached value of the '{@link #getEInput() <em>EInput</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEInput()
   * @generated
   * @ordered
   */
  protected Input eInput = null;

  /**
   * The cached value of the '{@link #getEOutput() <em>EOutput</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEOutput()
   * @generated
   * @ordered
   */
  protected Output eOutput = null;

  /**
   * The cached value of the '{@link #getEFaults() <em>EFaults</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEFaults()
   * @generated
   * @ordered
   */
  protected EList eFaults = null;

  /**
   * The cached value of the '{@link #getEParameterOrdering() <em>EParameter Ordering</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEParameterOrdering()
   * @generated
   * @ordered
   */
  protected EList eParameterOrdering = null;

  private Map faults;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OperationImpl()
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
    return WSDLPackage.eINSTANCE.getOperation();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public OperationType getStyle()
  {
    if (style == null)
      style = deduceOperationType(getElement());
    
    // The value of style is updated when
    // 1) the WSDL is loaded for the first time,
    // 2) the setStyle() method is called, and
    // 3) the children of the Operation (DOM) element are manipulated.
    return style;
  }

  private OperationType deduceOperationType(Element operation)
  {
    if (operation == null)
      return null;
       
  	int state = 0;
  	int messageRefType = -1;
  	Node child = null;
  	NodeList children = operation.getChildNodes();
  	
    for (int i=0; i<children.getLength(); i++)
    {
      child = children.item(i);
      if (child.getNodeType() != Node.ELEMENT_NODE)
        continue;
      
      messageRefType = WSDLUtil.getInstance().getWSDLType((Element)child);
      switch (state)
	  {
        case 0: // initial state     
          if (WSDLConstants.INPUT == messageRefType)
            state = 1; 
          else if (WSDLConstants.OUTPUT == messageRefType)
            state = 2; 
          else if (WSDLConstants.DOCUMENTATION == messageRefType)
          	state = 0;
          else
            state = -1; // cannot happen
          break;
        case 1: // one-way or request-response
          if (WSDLConstants.FAULT == messageRefType)
            state = 11; // one-way
          else if (WSDLConstants.OUTPUT == messageRefType)
            state = 3; // request-response
          else
            state = -1; // cannot happen
          break;
        case 11: // one-way
          if (WSDLConstants.FAULT == messageRefType)
            state = 11; // one-way
          else
            state = -1; // cannot happen
          break;
        case 2: // solicit-response or notification
          if (WSDLConstants.INPUT == messageRefType)
            state = 4; // solicit-response
          else if (WSDLConstants.FAULT == messageRefType)
            state = 21; // notification
          else
            state = -1; // cannot happen
          break;
        case 21: // notification
          if (WSDLConstants.FAULT == messageRefType)
            state = 21; // notification
          else
            state = -1; // cannot happen
          break;
        case 3: // request-response 
          if (WSDLConstants.FAULT == messageRefType)
            state = 3;
          else
            state = -1; // cannot happen
          break;
        case 4: // solicit-response
          if (WSDLConstants.FAULT == messageRefType)
            state = 4;
          else
            state = -1; // cannot happen
          break;
        default: // cannot happen (-1)
          break;
	  }
    }
    
    OperationType opType = null;
    switch (state)
	{
      case 1:
      case 11:
      	opType = OperationType.ONE_WAY;
        break;
      case 2:
      case 21:
      	opType = OperationType.NOTIFICATION;
        break;
      case 3:
      	opType = OperationType.REQUEST_RESPONSE;
        break;
      case 4:
      	opType = OperationType.SOLICIT_RESPONSE;
        break;
      default: // invalid
        break;
	}
    return opType;    
  }
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setStyle(OperationType newStyle)
  {
    OperationType oldStyle = style;
    style = newStyle;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.OPERATION__STYLE, oldStyle, style));
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.OPERATION__NAME, oldName, name));
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.OPERATION__UNDEFINED, oldUndefined, undefined));
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.OPERATION__PROXY, oldProxy, proxy));
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.OPERATION__RESOURCE_URI, oldResourceURI, resourceURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Input getEInput()
  {
    return eInput;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEInput(Input newEInput, NotificationChain msgs)
  {
    Input oldEInput = eInput;
    eInput = newEInput;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WSDLPackage.OPERATION__EINPUT, oldEInput, newEInput);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEInput(Input newEInput)
  {
    if (newEInput != eInput)
    {
      NotificationChain msgs = null;
      if (eInput != null)
        msgs = ((InternalEObject)eInput).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WSDLPackage.OPERATION__EINPUT, null, msgs);
      if (newEInput != null)
        msgs = ((InternalEObject)newEInput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WSDLPackage.OPERATION__EINPUT, null, msgs);
      msgs = basicSetEInput(newEInput, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.OPERATION__EINPUT, newEInput, newEInput));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Output getEOutput()
  {
    return eOutput;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEOutput(Output newEOutput, NotificationChain msgs)
  {
    Output oldEOutput = eOutput;
    eOutput = newEOutput;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, WSDLPackage.OPERATION__EOUTPUT, oldEOutput, newEOutput);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEOutput(Output newEOutput)
  {
    if (newEOutput != eOutput)
    {
      NotificationChain msgs = null;
      if (eOutput != null)
        msgs = ((InternalEObject)eOutput).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - WSDLPackage.OPERATION__EOUTPUT, null, msgs);
      if (newEOutput != null)
        msgs = ((InternalEObject)newEOutput).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - WSDLPackage.OPERATION__EOUTPUT, null, msgs);
      msgs = basicSetEOutput(newEOutput, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.OPERATION__EOUTPUT, newEOutput, newEOutput));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEFaults()
  {
    if (eFaults == null)
    {
      eFaults = new EObjectContainmentEList(Fault.class, this, WSDLPackage.OPERATION__EFAULTS);
    }
    return eFaults;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList getEParameterOrdering()
  {
    if (eParameterOrdering == null)
    {
      eParameterOrdering = new EObjectResolvingEList(Part.class, this, WSDLPackage.OPERATION__EPARAMETER_ORDERING);
    }
    return eParameterOrdering;
  }

  /**
   * <!-- begin-user-doc -->
   * Add a fault message that must be associated with this
   * operation.
   * @param fault the new fault message
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void addFault(javax.wsdl.Fault fault)
  {
    getEFaults().add((Fault)fault);
  }

  /**
   * <!-- begin-user-doc -->
   * Get the specified fault message.
   * @param name the name of the desired fault message.
   * @return the corresponding fault message, or null if there wasn't
   * any matching message
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public javax.wsdl.Fault getFault(String name)
  {
    Fault result = null;
    for (Iterator i = getEFaults().iterator(); i.hasNext();)
    {
      Fault fault = (Fault) i.next();
      if (name.equals(fault.getName()))
      {
        result = fault;
        break;
      }
    }
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * Get all the fault messages associated with this operation.
   * @return names of fault messages
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public Map getFaults()
  {
    HashMap hashMap = new HashMap();
    for (Iterator i = getEFaults().iterator(); i.hasNext();)
    {
      Fault fault = (Fault) i.next();
      hashMap.put(fault.getName(), fault);
    }
    return hashMap;
  }

  /**
   * <!-- begin-user-doc -->
   * Returns a collection of Part names. Note that 
   * getParameterEOrdering() returns a collection of Parts.
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getParameterOrdering()
  {
    List ordering = new ArrayList();
    for (Iterator i = getEParameterOrdering().iterator(); i.hasNext();)
    {
      try
      {
        Part part = (Part) i.next();
        ordering.add(part.getName());
      }
      catch (Exception e)
      {
        // TBD - handle exception
      }
    }
    return ordering.isEmpty() ? null : ordering;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setParameterOrdering(List parameterOrder)
  {
    getEParameterOrdering().clear();
    if (parameterOrder != null)
    {
      for (Iterator i = parameterOrder.iterator(); i.hasNext();)
      {
        Part part = WSDLFactory.eINSTANCE.createPart();
        part.setName((String) i.next());
        getEParameterOrdering().add(part);
      }
    }
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
        case WSDLPackage.OPERATION__EINPUT:
          return basicSetEInput(null, msgs);
        case WSDLPackage.OPERATION__EOUTPUT:
          return basicSetEOutput(null, msgs);
        case WSDLPackage.OPERATION__EFAULTS:
          return ((InternalEList)getEFaults()).basicRemove(otherEnd, msgs);
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
      case WSDLPackage.OPERATION__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case WSDLPackage.OPERATION__ELEMENT:
        return getElement();
      case WSDLPackage.OPERATION__STYLE:
        return getStyle();
      case WSDLPackage.OPERATION__NAME:
        return getName();
      case WSDLPackage.OPERATION__UNDEFINED:
        return isUndefined() ? Boolean.TRUE : Boolean.FALSE;
      case WSDLPackage.OPERATION__PROXY:
        return isProxy() ? Boolean.TRUE : Boolean.FALSE;
      case WSDLPackage.OPERATION__RESOURCE_URI:
        return getResourceURI();
      case WSDLPackage.OPERATION__EINPUT:
        return getEInput();
      case WSDLPackage.OPERATION__EOUTPUT:
        return getEOutput();
      case WSDLPackage.OPERATION__EFAULTS:
        return getEFaults();
      case WSDLPackage.OPERATION__EPARAMETER_ORDERING:
        return getEParameterOrdering();
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
      case WSDLPackage.OPERATION__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case WSDLPackage.OPERATION__ELEMENT:
        setElement((Element)newValue);
        return;
      case WSDLPackage.OPERATION__STYLE:
        setStyle((OperationType)newValue);
        return;
      case WSDLPackage.OPERATION__NAME:
        setName((String)newValue);
        return;
      case WSDLPackage.OPERATION__UNDEFINED:
        setUndefined(((Boolean)newValue).booleanValue());
        return;
      case WSDLPackage.OPERATION__PROXY:
        setProxy(((Boolean)newValue).booleanValue());
        return;
      case WSDLPackage.OPERATION__RESOURCE_URI:
        setResourceURI((String)newValue);
        return;
      case WSDLPackage.OPERATION__EINPUT:
        setEInput((Input)newValue);
        return;
      case WSDLPackage.OPERATION__EOUTPUT:
        setEOutput((Output)newValue);
        return;
      case WSDLPackage.OPERATION__EFAULTS:
        getEFaults().clear();
        getEFaults().addAll((Collection)newValue);
        return;
      case WSDLPackage.OPERATION__EPARAMETER_ORDERING:
        getEParameterOrdering().clear();
        getEParameterOrdering().addAll((Collection)newValue);
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
      case WSDLPackage.OPERATION__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.OPERATION__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.OPERATION__STYLE:
        setStyle(STYLE_EDEFAULT);
        return;
      case WSDLPackage.OPERATION__NAME:
        setName(NAME_EDEFAULT);
        return;
      case WSDLPackage.OPERATION__UNDEFINED:
        setUndefined(UNDEFINED_EDEFAULT);
        return;
      case WSDLPackage.OPERATION__PROXY:
        setProxy(PROXY_EDEFAULT);
        return;
      case WSDLPackage.OPERATION__RESOURCE_URI:
        setResourceURI(RESOURCE_URI_EDEFAULT);
        return;
      case WSDLPackage.OPERATION__EINPUT:
        setEInput((Input)null);
        return;
      case WSDLPackage.OPERATION__EOUTPUT:
        setEOutput((Output)null);
        return;
      case WSDLPackage.OPERATION__EFAULTS:
        getEFaults().clear();
        return;
      case WSDLPackage.OPERATION__EPARAMETER_ORDERING:
        getEParameterOrdering().clear();
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
      case WSDLPackage.OPERATION__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case WSDLPackage.OPERATION__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case WSDLPackage.OPERATION__STYLE:
        return STYLE_EDEFAULT == null ? style != null : !STYLE_EDEFAULT.equals(style);
      case WSDLPackage.OPERATION__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case WSDLPackage.OPERATION__UNDEFINED:
        return undefined != UNDEFINED_EDEFAULT;
      case WSDLPackage.OPERATION__PROXY:
        return proxy != PROXY_EDEFAULT;
      case WSDLPackage.OPERATION__RESOURCE_URI:
        return RESOURCE_URI_EDEFAULT == null ? resourceURI != null : !RESOURCE_URI_EDEFAULT.equals(resourceURI);
      case WSDLPackage.OPERATION__EINPUT:
        return eInput != null;
      case WSDLPackage.OPERATION__EOUTPUT:
        return eOutput != null;
      case WSDLPackage.OPERATION__EFAULTS:
        return eFaults != null && !eFaults.isEmpty();
      case WSDLPackage.OPERATION__EPARAMETER_ORDERING:
        return eParameterOrdering != null && !eParameterOrdering.isEmpty();
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
    result.append(" (style: ");
    result.append(style);
    result.append(", name: ");
    result.append(name);
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
    String name = changedElement.getAttribute("name");
    if (name != null)
    {
      setName(name);
    }
    
    if (changedElement.hasAttribute("parameterOrder"))
    {
      String parameterOrder = changedElement.getAttribute("parameterOrder");
      if (parameterOrder != null)
      {
        String[] array = parameterOrder.split(" ");
        List l = Arrays.asList(array);
        setParameterOrdering(l);
      }
    }

  }

  public void elementChanged(Element changedElement)
  {
    style = deduceOperationType(changedElement);
    super.elementChanged(changedElement);
  }
  
  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    Definition definition = getEnclosingDefinition();

    switch (WSDLUtil.getInstance().getWSDLType(child))
    {
      case WSDLConstants.INPUT :
        {
          Input input = WSDLFactory.eINSTANCE.createInput();
          input.setEnclosingDefinition(definition);
          input.setElement(child);
          setInput(input);
          break;
        }
      case WSDLConstants.OUTPUT :
        {
          Output output = WSDLFactory.eINSTANCE.createOutput();
          output.setEnclosingDefinition(definition);
          output.setElement(child);
          setOutput(output);
          break;
        }
      case WSDLConstants.FAULT :
        {
          Fault fault = WSDLFactory.eINSTANCE.createFault();
          fault.setEnclosingDefinition(definition);
          fault.setElement(child);
          addFault(fault);
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
    Operation operation = (Operation) component;
    if (modelObject instanceof Input)
    {
      operation.setEInput(null);
    }
    else if (modelObject instanceof Output)
    {
      operation.setEOutput(null);
    }
    else if (modelObject instanceof Fault)
    {
      operation.getEFaults().remove(modelObject);
    }
  }

  public Collection getModelObjects(Object component)
  {
    Operation operation = (Operation) component;

    List list = new ArrayList();

    if (operation.getEInput() != null)
    {
      list.add(operation.getEInput());
    }

    if (operation.getEOutput() != null)
    {
      list.add(operation.getEOutput());
    }

    list.addAll(operation.getEFaults());

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
      if (eAttribute == null || eAttribute == WSDLPackage.eINSTANCE.getOperation_Name())
        niceSetAttribute(theElement, WSDLConstants.NAME_ATTRIBUTE, getName());
      if (eAttribute == WSDLPackage.eINSTANCE.getOperation_Style())
      {
        OperationType targetType = getStyle();
        OperationType currentType = deduceOperationType(theElement);
        if (targetType.equals(currentType))
          return;
        else if ((targetType.equals(OperationType.REQUEST_RESPONSE) || targetType.equals(OperationType.SOLICIT_RESPONSE))
                  && (currentType.equals(OperationType.REQUEST_RESPONSE) || currentType.equals(OperationType.SOLICIT_RESPONSE)))
        	reorderChildren();
        else
          style = deduceOperationType(theElement); // switch back. no support for the other types
      }
    }
  }
  
  // Switch <input> and <output>
  private void reorderChildren()
  {
    // Find out the positions of <input> and <output>
    Node input = null;
    Node output = null;
    Node reference = null;
    for (Node current=getElement().getFirstChild(); current!= null;current=current.getNextSibling())
    {
      if (current.getNodeType() == Node.ELEMENT_NODE)
      {
      	if (WSDLConstants.INPUT == WSDLUtil.getInstance().getWSDLType((Element)current))
        {
          input = current;
          if (output != null)
          { 
            // cs.. for safety use current.getNextSibling() instead of nodeList.item(i+1)            
            reference = current.getNextSibling();
          }  
        }
        else if (WSDLConstants.OUTPUT == WSDLUtil.getInstance().getWSDLType((Element)current))
        {
          output = current;
          if (input != null)
          {
            // cs.. for safety use current.getNextSibling() instead of nodeList.item(i+1)            
            reference = current.getNextSibling();
          }  
        }
      }
    } // end for
    
    if (input != null && output != null)
    {
      Element parent = getElement();
      if (getStyle().equals(OperationType.REQUEST_RESPONSE))
      {
      	// current order: <output> <input>
        switchChildren(parent,output,input,reference);
      }
      else if (getStyle().equals(OperationType.SOLICIT_RESPONSE))
      {
        // current order: <input> <output>
        switchChildren(parent,input,output,reference);
      }
    }
  }
  
  private void switchChildren(Node parent, Node child1, Node child2, Node nextOfChild2)
  {
    // current node sequence: <child1> <child2> <nextOfChild2>
    niceRemoveChild(parent,child2);
    niceInsertBefore(parent,child2,child1);
    niceRemoveChild(parent,child1);
    niceInsertBefore(parent,child1,nextOfChild2);
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.OPERATION);
    setElement(newElement);

    Input input = getEInput();
    if (input != null)
    {
      Element child = ((InputImpl) input).createElement();
      newElement.appendChild(child);
    }

    Output output = getEOutput();
    if (output != null)
    {
      Element child = ((OutputImpl) output).createElement();
      newElement.appendChild(child);
    }

    Iterator iterator = getEFaults().iterator();
    while (iterator.hasNext())
    {
      Fault fault = (Fault) iterator.next();
      Element child = ((FaultImpl) fault).createElement();
      newElement.appendChild(child);
    }

    return newElement;
  } 
  
} //OperationImpl
