// $Id: ImportImpl.java,v 1.1 2004/11/01 23:07:29 csalter Exp $
package org.eclipse.wst.wsdl.internal.impl;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Import;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;
import org.eclipse.wst.wsdl.internal.util.WSDLModelLocator;
import org.eclipse.wst.wsdl.internal.util.WSDLResourceImpl;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Import</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.impl.ImportImpl#getNamespaceURI <em>Namespace URI</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.ImportImpl#getLocationURI <em>Location URI</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.ImportImpl#getEDefinition <em>EDefinition</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.ImportImpl#getESchema <em>ESchema</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ImportImpl extends WSDLElementImpl implements Import
{
  protected boolean resolved;

  /**
   * The default value of the '{@link #getNamespaceURI() <em>Namespace URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNamespaceURI()
   * @generated
   * @ordered
   */
  protected static final String NAMESPACE_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getNamespaceURI() <em>Namespace URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNamespaceURI()
   * @generated
   * @ordered
   */
  protected String namespaceURI = NAMESPACE_URI_EDEFAULT;

  /**
   * The default value of the '{@link #getLocationURI() <em>Location URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocationURI()
   * @generated
   * @ordered
   */
  protected static final String LOCATION_URI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLocationURI() <em>Location URI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocationURI()
   * @generated
   * @ordered
   */
  protected String locationURI = LOCATION_URI_EDEFAULT;

  /**
   * The cached value of the '{@link #getEDefinition() <em>EDefinition</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEDefinition()
   * @generated
   * @ordered
   */
  protected Definition eDefinition = null;

  /**
   * The cached value of the '{@link #getESchema() <em>ESchema</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getESchema()
   * @generated
   * @ordered
   */
  protected XSDSchema eSchema = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ImportImpl()
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
    return WSDLPackage.eINSTANCE.getImport();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getNamespaceURI()
  {
    return namespaceURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNamespaceURI(String newNamespaceURI)
  {
    String oldNamespaceURI = namespaceURI;
    namespaceURI = newNamespaceURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.IMPORT__NAMESPACE_URI, oldNamespaceURI, namespaceURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLocationURI()
  {
    return locationURI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocationURI(String newLocationURI)
  {
    String oldLocationURI = locationURI;
    locationURI = newLocationURI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.IMPORT__LOCATION_URI, oldLocationURI, locationURI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition getEDefinition()
  {
    if (eDefinition != null && eDefinition.eIsProxy())
    {
      Definition oldEDefinition = eDefinition;
      eDefinition = (Definition)eResolveProxy((InternalEObject)eDefinition);
      if (eDefinition != oldEDefinition)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, WSDLPackage.IMPORT__EDEFINITION, oldEDefinition, eDefinition));
      }
    }
    return eDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition basicGetEDefinition()
  {
    return eDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEDefinition(Definition newEDefinition)
  {
    Definition oldEDefinition = eDefinition;
    eDefinition = newEDefinition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.IMPORT__EDEFINITION, oldEDefinition, eDefinition));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XSDSchema getESchema()
  {
    if (eSchema != null && eSchema.eIsProxy())
    {
      XSDSchema oldESchema = eSchema;
      eSchema = (XSDSchema)eResolveProxy((InternalEObject)eSchema);
      if (eSchema != oldESchema)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, WSDLPackage.IMPORT__ESCHEMA, oldESchema, eSchema));
      }
    }
    return eSchema;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XSDSchema basicGetESchema()
  {
    return eSchema;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setESchema(XSDSchema newESchema)
  {
    XSDSchema oldESchema = eSchema;
    eSchema = newESchema;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.IMPORT__ESCHEMA, oldESchema, eSchema));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public XSDSchema getSchema()
  {
    return getESchema();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setSchema(XSDSchema schema)
  {
    setESchema(schema);
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
      case WSDLPackage.IMPORT__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case WSDLPackage.IMPORT__ELEMENT:
        return getElement();
      case WSDLPackage.IMPORT__NAMESPACE_URI:
        return getNamespaceURI();
      case WSDLPackage.IMPORT__LOCATION_URI:
        return getLocationURI();
      case WSDLPackage.IMPORT__EDEFINITION:
        if (resolve) return getEDefinition();
        return basicGetEDefinition();
      case WSDLPackage.IMPORT__ESCHEMA:
        if (resolve) return getESchema();
        return basicGetESchema();
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
      case WSDLPackage.IMPORT__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case WSDLPackage.IMPORT__ELEMENT:
        setElement((Element)newValue);
        return;
      case WSDLPackage.IMPORT__NAMESPACE_URI:
        setNamespaceURI((String)newValue);
        return;
      case WSDLPackage.IMPORT__LOCATION_URI:
        setLocationURI((String)newValue);
        return;
      case WSDLPackage.IMPORT__EDEFINITION:
        setEDefinition((Definition)newValue);
        return;
      case WSDLPackage.IMPORT__ESCHEMA:
        setESchema((XSDSchema)newValue);
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
      case WSDLPackage.IMPORT__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.IMPORT__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.IMPORT__NAMESPACE_URI:
        setNamespaceURI(NAMESPACE_URI_EDEFAULT);
        return;
      case WSDLPackage.IMPORT__LOCATION_URI:
        setLocationURI(LOCATION_URI_EDEFAULT);
        return;
      case WSDLPackage.IMPORT__EDEFINITION:
        setEDefinition((Definition)null);
        return;
      case WSDLPackage.IMPORT__ESCHEMA:
        setESchema((XSDSchema)null);
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
      case WSDLPackage.IMPORT__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case WSDLPackage.IMPORT__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case WSDLPackage.IMPORT__NAMESPACE_URI:
        return NAMESPACE_URI_EDEFAULT == null ? namespaceURI != null : !NAMESPACE_URI_EDEFAULT.equals(namespaceURI);
      case WSDLPackage.IMPORT__LOCATION_URI:
        return LOCATION_URI_EDEFAULT == null ? locationURI != null : !LOCATION_URI_EDEFAULT.equals(locationURI);
      case WSDLPackage.IMPORT__EDEFINITION:
        return eDefinition != null;
      case WSDLPackage.IMPORT__ESCHEMA:
        return eSchema != null;
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
    result.append(" (namespaceURI: ");
    result.append(namespaceURI);
    result.append(", locationURI: ");
    result.append(locationURI);
    result.append(')');
    return result.toString();
  }

  /**
   * @see Import#getDefinition()
   */
  public javax.wsdl.Definition getDefinition()
  {
    return getEDefinition();
  }

  /**
   * @see Import#setDefinition(Definition)
   */
  public void setDefinition(javax.wsdl.Definition definition)
  {
    setEDefinition((org.eclipse.wst.wsdl.Definition) definition);
  }

  //
  // Reconcile methods
  //
  public void reconcileAttributes(Element changedElement)
  {
    setNamespaceURI(WSDLConstants.getAttribute(changedElement, WSDLConstants.NAMESPACE_ATTRIBUTE));
    setLocationURI(WSDLConstants.getAttribute(changedElement, WSDLConstants.LOCATION_ATTRIBUTE));
  }

  //
  // For reconciliation: Model -> DOM
  //
  protected void changeAttribute(EAttribute eAttribute)
  {
    // We need to set this boolean to false because the Import may point to a different location.
    // So we need we should view this import as unresolved.
    resolved = false;
    
    if (isReconciling)
      return;

    super.changeAttribute(eAttribute);
    Element theElement = getElement();
    if (theElement != null)
    {
      if (eAttribute == null || eAttribute == WSDLPackage.eINSTANCE.getImport_NamespaceURI())
        niceSetAttribute(theElement, WSDLConstants.NAMESPACE_ATTRIBUTE, getNamespaceURI());

      if (eAttribute == null || eAttribute == WSDLPackage.eINSTANCE.getImport_LocationURI())
        niceSetAttribute(theElement, WSDLConstants.LOCATION_ATTRIBUTE, getLocationURI());
    }
  }

  public Element createElement()
  {
    Element newElement = createElement(WSDLConstants.IMPORT);
    setElement(newElement);
    return newElement;
  }

  public void importDefinitionOrSchema()
  {
    resolve(getNamespaceURI(), getLocationURI());
  }

  protected void resolve(String namespace, String location)
  {
    if (!resolved)
    {
      Definition definition = getEnclosingDefinition();
      if (definition != null && definition.getDocumentBaseURI() != null)
      {
        Resource resource = definition.eResource();
        if (resource != null)
        {
          ResourceSet resourceSet = resource.getResourceSet();
          if (resourceSet != null)
          {
            if (namespace == null)
            {
              namespace = definition.getTargetNamespace();
            }

            String resolvedLocation = resolveLocation(definition, namespace, location);

            URI uri = URI.createURI(resolvedLocation);
            Resource resolvedResource = resourceSet.getResource(uri, false);
            if (resolvedResource == null)
            {
              try
              {
                InputStream inputStream = resourceSet.getURIConverter().createInputStream(uri);
                resolvedResource = resourceSet.createResource(uri);
                resolvedResource.load(inputStream, resourceSet.getLoadOptions());
              }
              catch (IOException exception)
              {
                // It is generally not an error to fail to resolve.
                // If a resource is actually created, 
                // which happens only when we can create an input stream,
                // then it's an error if it's not a good wsdl or schema
              }
            }

            if (resolvedResource != null)
            {
              if (resolvedResource instanceof WSDLResourceImpl)
              {
                eDefinition = ((WSDLResourceImpl) resolvedResource).getDefinition();
              }
              else if (resolvedResource instanceof XSDResourceImpl)
              {
                eSchema = ((XSDResourceImpl) resolvedResource).getSchema();
              }
              else
              {
                eDefinition = WSDLFactory.eINSTANCE.createDefinition();
              }
            }
            resolved = true;
          }
        }
      }
    }
  }

  protected String resolveLocation(Definition definition, String namespace, String schemaLocation)
  {
    WSDLModelLocator locator = (WSDLModelLocator)EcoreUtil.getRegisteredAdapter(definition.eResource(), WSDLModelLocator.class);
    if (locator == null)
    {
      WSDLResourceImpl resourceImpl = (WSDLResourceImpl)definition.eResource();
      locator = resourceImpl.getURIResolver();
    }
    return locator.resolveURI(definition.getDocumentBaseURI(), namespace, schemaLocation);
  }
} //ImportImpl
