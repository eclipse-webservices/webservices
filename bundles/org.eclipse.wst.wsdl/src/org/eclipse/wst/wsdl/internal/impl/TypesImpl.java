// $Id: TypesImpl.java,v 1.1 2004/11/01 23:07:29 csalter Exp $
package org.eclipse.wst.wsdl.internal.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.Types;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.XSDSchemaExtensibilityElement;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;
import org.eclipse.xsd.XSDSchema;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//import com.ibm.wsdl.factory.WSDLFactoryImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Types</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class TypesImpl extends ExtensibleElementImpl implements Types
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TypesImpl()
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
    return WSDLPackage.eINSTANCE.getTypes();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getSchemas()
  {
    List arrayList = new ArrayList();
    for (Iterator i = getEExtensibilityElements().iterator(); i.hasNext();)
    {
      XSDSchemaExtensibilityElement xsdee = (XSDSchemaExtensibilityElement) i.next();
      if (xsdee.getSchema() != null)
      {
        arrayList.add(xsdee.getSchema());
      }
    }
    return arrayList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getSchemas(String namespaceURI)
  {
    List schemas = new ArrayList();
    for (Iterator i = getSchemas().iterator(); i.hasNext();)
    {
      XSDSchema schema = (XSDSchema) i.next();
      if (namespaceURI.equals(schema.getTargetNamespace()))
        schemas.add(schema);
    }
    return schemas;
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
        case WSDLPackage.TYPES__EEXTENSIBILITY_ELEMENTS:
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
      case WSDLPackage.TYPES__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case WSDLPackage.TYPES__ELEMENT:
        return getElement();
      case WSDLPackage.TYPES__EEXTENSIBILITY_ELEMENTS:
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
      case WSDLPackage.TYPES__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case WSDLPackage.TYPES__ELEMENT:
        setElement((Element)newValue);
        return;
      case WSDLPackage.TYPES__EEXTENSIBILITY_ELEMENTS:
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
      case WSDLPackage.TYPES__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.TYPES__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.TYPES__EEXTENSIBILITY_ELEMENTS:
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
      case WSDLPackage.TYPES__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case WSDLPackage.TYPES__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case WSDLPackage.TYPES__EEXTENSIBILITY_ELEMENTS:
        return eExtensibilityElements != null && !eExtensibilityElements.isEmpty();
    }
    return eDynamicIsSet(eFeature);
  }
  
  public void eNotify(Notification msg)
  {
    super.eNotify(msg);
    
    // cs.. if we've added an XSDSchemaExtensibilityElementImpl and the Types object is already attached 
    // to a resource we need to set the schemaLocation for the inline schema.
    // If not yet attached to a resource, the schemaLocation's will be set via WSDLResourceImpl.attached(EObject o)
    //     
    if (msg.getFeature() == WSDLPackage.eINSTANCE.getExtensibleElement_EExtensibilityElements() &&
        msg.getEventType() == Notification.ADD)
    {
      if (msg.getNewValue() instanceof XSDSchemaExtensibilityElementImpl)
      {
        XSDSchemaExtensibilityElementImpl ee = (XSDSchemaExtensibilityElementImpl)msg.getNewValue();
        if (ee.getSchema() != null && ee.getSchema().eResource() != null)
        {
           ee.getSchema().setSchemaLocation(ee.getSchema().eResource().getURI().toString());
        }  
      }  
    }  
  }

  //
  // Reconcile methods
  //

  public void reconcileAttributes(Element changedElement)
  {
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    if ("schema".equals(child.getLocalName()))
    {
      try
      {
        XSDSchemaExtensibilityElement xsdee = WSDLFactory.eINSTANCE.createXSDSchemaExtensibilityElement();
        xsdee.setEnclosingDefinition(getEnclosingDefinition());
        xsdee.setElement(child); // cs : this has the side effect of creating the inline schema               
        addExtensibilityElement(xsdee);       
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  protected void handleReconciliation(Collection remainingModelObjects)
  {
    for (Iterator i = remainingModelObjects.iterator(); i.hasNext();)
    {
      remove(this, i.next());
    }
    ((DefinitionImpl)getEnclosingDefinition()).reconcileReferences(true);
  }

  protected void remove(Object component, Object modelObject)
  {
    Types types = (Types) component;
    List list = types.getEExtensibilityElements();
    list.remove(modelObject);
  }

  public Collection getModelObjects(Object component)
  {
    Types types = (Types) component;
    List list = new ArrayList();
    list.addAll(types.getEExtensibilityElements());
    return list;
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.TYPES);
    setElement(newElement);

    // Add children
    Definition definition = getEnclosingDefinition();
    if (definition == null)
      return newElement;

    Document document = definition.getDocument();
    if (document == null)
      document = ((DefinitionImpl) definition).updateDocument();

    Iterator iter = getEExtensibilityElements().iterator();
    Element el = null;
    while (iter.hasNext())
    {
      ExtensibilityElement extensibility = (ExtensibilityElement) iter.next();
      el = extensibility.getElement();
      if (el != null)
      {
        try
        {
          Element reParented = (Element) document.importNode(el, true);
          extensibility.setElement(reParented); // replace with the new one
          newElement.appendChild(reParented);
        }
        catch (DOMException e)
        {
          e.printStackTrace();
          return newElement;
        }
      }
      else
      {
      	Element child = ((ExtensibilityElementImpl)extensibility).createElement();
      	newElement.appendChild(child);
      }
    }

    return newElement;
  }
} //TypesImpl
