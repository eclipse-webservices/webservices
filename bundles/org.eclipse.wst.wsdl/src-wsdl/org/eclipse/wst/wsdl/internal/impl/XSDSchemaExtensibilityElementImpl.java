/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.internal.impl;


import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.eclipse.xsd.util.XSDConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>XSD Schema Extensibility Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.internal.impl.XSDSchemaExtensibilityElementImpl#getSchema <em>Schema</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class XSDSchemaExtensibilityElementImpl extends ExtensibilityElementImpl implements XSDSchemaExtensibilityElement
{
  /**
   * The cached value of the '{@link #getSchema() <em>Schema</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSchema()
   * @generated
   * @ordered
   */
  protected XSDSchema schema = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected XSDSchemaExtensibilityElementImpl()
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
    return WSDLPackage.Literals.XSD_SCHEMA_EXTENSIBILITY_ELEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XSDSchema getSchema()
  {
    return schema;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSchema(XSDSchema newSchema, NotificationChain msgs)
  {
    XSDSchema oldSchema = schema;
    schema = newSchema;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(
        this,
        Notification.SET,
        WSDLPackage.XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA,
        oldSchema,
        newSchema);
      if (msgs == null)
        msgs = notification;
      else
        msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSchema(XSDSchema newSchema)
  {
    if (newSchema != schema)
    {
      NotificationChain msgs = null;
      if (schema != null)
        msgs = ((InternalEObject)schema).eInverseRemove(
          this,
          EOPPOSITE_FEATURE_BASE - WSDLPackage.XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA,
          null,
          msgs);
      if (newSchema != null)
        msgs = ((InternalEObject)newSchema).eInverseAdd(
          this,
          EOPPOSITE_FEATURE_BASE - WSDLPackage.XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA,
          null,
          msgs);
      msgs = basicSetSchema(newSchema, msgs);
      if (msgs != null)
        msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA, newSchema, newSchema));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case WSDLPackage.XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA:
      return basicSetSchema(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case WSDLPackage.XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA:
      return getSchema();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case WSDLPackage.XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA:
      setSchema((XSDSchema)newValue);
      return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case WSDLPackage.XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA:
      setSchema((XSDSchema)null);
      return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case WSDLPackage.XSD_SCHEMA_EXTENSIBILITY_ELEMENT__SCHEMA:
      return schema != null;
    }
    return super.eIsSet(featureID);
  }

  /* (non-Javadoc)
   * @see org.eclipse.wst.wsdl.internal.impl.WSDLElementImpl#reconcile(org.w3c.dom.Element)
   */
  protected void reconcile(Element changedElement)
  {
    element = changedElement; // This line may not needed.    
    if (schema == null)
    {
      XSDSchema newSchema = XSDSchemaImpl.createSchema(changedElement);
      setSchema(newSchema);
    }
  }

  public XSDSchema createSchema(Element element)
  {
    if (element.getLocalName().equals("schema") && XSDConstants.isSchemaForSchemaNamespace(element.getNamespaceURI()))
    {
      XSDSchema xsdSchema = XSDFactory.eINSTANCE.createXSDSchema();
      xsdSchema.setElement(element);
      return xsdSchema;
    }
    else
    {
      return null;
    }
  }

  public Element getElement()
  {
    if (element != null)
      return element;
    else if (getSchema() != null)
      element = getSchema().getElement();

    return element;
  }

  public void setElement(Element newElement)
  {
    if (newElement == null && !isReconciling)
    {
      element = null;
    }
    else
    {
      setElementGen(newElement);
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001, XSDConstants.SCHEMA_ELEMENT_TAG);

    return elementType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setElementType(QName newElementType)
  {
    // Should not do anything.
  }

  public void reconcileAttributes(Element changedElement)
  {
    super.reconcileAttributes(changedElement);
  }

  public Element createElement()
  {
    if (schema == null) // kb Note: This case is not supposed to happen.
    {
      // cs ... why do we do this if its not supposed to happen?
      // Is there a scenario where this is a usefull fallback?
      // Under what conditions does this code get executed (i.e. why would schema == null)?
      schema = XSDFactory.eINSTANCE.createXSDSchema();
      schema.setSchemaForSchemaQNamePrefix("xsd");
      schema.setTargetNamespace("http://tempuri.org/");
      java.util.Map qNamePrefixToNamespaceMap = schema.getQNamePrefixToNamespaceMap();
      qNamePrefixToNamespaceMap.put("", schema.getTargetNamespace());
      qNamePrefixToNamespaceMap.put(schema.getSchemaForSchemaQNamePrefix(), org.eclipse.xsd.util.XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001);
      adopt(schema);
      schema.updateElement(true);
      return schema.getElement();
    }
    else
    {
      element = schema.getElement();
      if (element == null)
      {
        adopt(schema);
        schema.updateElement(true);
        element = schema.getElement();
      }
      return element;
    }
  }

  private void adopt(XSDSchema xsdSchema)
  {
    Definition definition = getEnclosingDefinition();
    if (definition == null)
      return;

    Document document = definition.getDocument();
    if (document == null)
      document = ((DefinitionImpl)definition).updateDocument();

    if (xsdSchema.getDocument() != null)
      xsdSchema.setDocument(null);

    xsdSchema.setDocument(document);
  }

  /*
   private Element adopt(Element element)
   {
   Definition definition = getEnclosingDefinition();
   if (definition == null)
   return element;

   Document document = definition.getDocument();
   if (document == null)
   document = ((DefinitionImpl) definition).updateDocument();
   
   return (Element)document.importNode(element,true);
   }
   */

  protected void changeAttribute(EAttribute eAttribute)
  {
    if (isReconciling)
      return;

    // TODO... revist this block of code
    //
    if (eAttribute == null || eAttribute == WSDLPackage.eINSTANCE.getXSDSchemaExtensibilityElement_Schema())
    {
      // We got a new schema so re-parent it.
      // cs... are we really doing the right thing here?  this seems strange
      if (schema != null)
      {
        if ((schema.getElement() == null && getElement() == null) || (schema.getElement() != getElement()))
        {
          schema.setDocument(null);
          schema.setElement(null);
          adopt(schema);
          schema.updateElement();
        }
      }
    }
    else if (eAttribute == WSDLPackage.eINSTANCE.getWSDLElement_Element())
    {
      setSchema(createSchema(element)); // element is not null
    }
  }
} //XSDSchemaExtensibilityElementImpl
