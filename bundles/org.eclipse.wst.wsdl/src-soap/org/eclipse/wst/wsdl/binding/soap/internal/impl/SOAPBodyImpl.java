/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsdl.binding.soap.internal.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.wst.wsdl.BindingFault;
import org.eclipse.wst.wsdl.BindingInput;
import org.eclipse.wst.wsdl.BindingOutput;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.binding.soap.SOAPBody;
import org.eclipse.wst.wsdl.binding.soap.SOAPPackage;
import org.eclipse.wst.wsdl.binding.soap.internal.util.SOAPConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.eclipse.wst.wsdl.util.WSDLConstants;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Body</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl#getUse <em>Use</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl#getNamespaceURI <em>Namespace URI</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl#getEncodingStyles <em>Encoding Styles</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.soap.internal.impl.SOAPBodyImpl#getParts <em>Parts</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SOAPBodyImpl extends ExtensibilityElementImpl implements SOAPBody {
  /**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default value of the '{@link #getUse() <em>Use</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getUse()
   * @generated
   * @ordered
   */
	protected static final String USE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUse() <em>Use</em>}' attribute.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getUse()
   * @generated
   * @ordered
   */
	protected String use = USE_EDEFAULT;

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
   * The cached value of the '{@link #getEncodingStyles() <em>Encoding Styles</em>}' attribute list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getEncodingStyles()
   * @generated
   * @ordered
   */
	protected EList encodingStyles = null;

  /**
   * The cached value of the '{@link #getParts() <em>Parts</em>}' reference list.
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @see #getParts()
   * @generated NOT
   * @ordered
   */
	protected List parts = null;

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	protected SOAPBodyImpl()
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
    return SOAPPackage.eINSTANCE.getSOAPBody();
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public String getUse()
  {
    return use;
  }

  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public void setUse(String newUse)
  {
    String oldUse = use;
    use = newUse;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_BODY__USE, oldUse, use));
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
      eNotify(new ENotificationImpl(this, Notification.SET, SOAPPackage.SOAP_BODY__NAMESPACE_URI, oldNamespaceURI, namespaceURI));
  }

public void setEncodingStyles(List list)
{
  encodingStyles = (EList)list;
}
  /**
   * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
   * @generated
   */
	public List getEncodingStyles()
  {
    if (encodingStyles == null)
    {
      encodingStyles = new EDataTypeUniqueEList(String.class, this, SOAPPackage.SOAP_BODY__ENCODING_STYLES);
    }
    return encodingStyles;
  }

  public void setParts(List list)
  {
    parts = list;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public List getParts()
  {
    if (parts == null)
    {
      parts = new EObjectResolvingEList(Part.class, this, SOAPPackage.SOAP_BODY__PARTS);
      return getImplicitParts();
    }
    
    if (parts.size() == 0)
    {
      return getImplicitParts();
    }
    
    return parts;
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
      case SOAPPackage.SOAP_BODY__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case SOAPPackage.SOAP_BODY__ELEMENT:
        return getElement();
      case SOAPPackage.SOAP_BODY__REQUIRED:
        return isRequired() ? Boolean.TRUE : Boolean.FALSE;
      case SOAPPackage.SOAP_BODY__ELEMENT_TYPE:
        return getElementType();
      case SOAPPackage.SOAP_BODY__USE:
        return getUse();
      case SOAPPackage.SOAP_BODY__NAMESPACE_URI:
        return getNamespaceURI();
      case SOAPPackage.SOAP_BODY__ENCODING_STYLES:
        return getEncodingStyles();
      case SOAPPackage.SOAP_BODY__PARTS:
        return getParts();
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
      case SOAPPackage.SOAP_BODY__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case SOAPPackage.SOAP_BODY__ELEMENT:
        setElement((Element)newValue);
        return;
      case SOAPPackage.SOAP_BODY__REQUIRED:
        setRequired(((Boolean)newValue).booleanValue());
        return;
      case SOAPPackage.SOAP_BODY__ELEMENT_TYPE:
        setElementType((QName)newValue);
        return;
      case SOAPPackage.SOAP_BODY__USE:
        setUse((String)newValue);
        return;
      case SOAPPackage.SOAP_BODY__NAMESPACE_URI:
        setNamespaceURI((String)newValue);
        return;
      case SOAPPackage.SOAP_BODY__ENCODING_STYLES:
        getEncodingStyles().clear();
        getEncodingStyles().addAll((Collection)newValue);
        return;
      case SOAPPackage.SOAP_BODY__PARTS:
        getParts().clear();
        getParts().addAll((Collection)newValue);
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
      case SOAPPackage.SOAP_BODY__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case SOAPPackage.SOAP_BODY__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case SOAPPackage.SOAP_BODY__REQUIRED:
        setRequired(REQUIRED_EDEFAULT);
        return;
      case SOAPPackage.SOAP_BODY__ELEMENT_TYPE:
        setElementType(ELEMENT_TYPE_EDEFAULT);
        return;
      case SOAPPackage.SOAP_BODY__USE:
        setUse(USE_EDEFAULT);
        return;
      case SOAPPackage.SOAP_BODY__NAMESPACE_URI:
        setNamespaceURI(NAMESPACE_URI_EDEFAULT);
        return;
      case SOAPPackage.SOAP_BODY__ENCODING_STYLES:
        getEncodingStyles().clear();
        return;
      case SOAPPackage.SOAP_BODY__PARTS:
        getParts().clear();
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
      case SOAPPackage.SOAP_BODY__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case SOAPPackage.SOAP_BODY__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case SOAPPackage.SOAP_BODY__REQUIRED:
        return required != REQUIRED_EDEFAULT;
      case SOAPPackage.SOAP_BODY__ELEMENT_TYPE:
        return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
      case SOAPPackage.SOAP_BODY__USE:
        return USE_EDEFAULT == null ? use != null : !USE_EDEFAULT.equals(use);
      case SOAPPackage.SOAP_BODY__NAMESPACE_URI:
        return NAMESPACE_URI_EDEFAULT == null ? namespaceURI != null : !NAMESPACE_URI_EDEFAULT.equals(namespaceURI);
      case SOAPPackage.SOAP_BODY__ENCODING_STYLES:
        return encodingStyles != null && !encodingStyles.isEmpty();
      case SOAPPackage.SOAP_BODY__PARTS:
        return parts != null && !parts.isEmpty();
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
    result.append(" (use: ");
    result.append(use);
    result.append(", namespaceURI: ");
    result.append(namespaceURI);
    result.append(", encodingStyles: ");
    result.append(encodingStyles);
    result.append(')');
    return result.toString();
  }
	
  //
  // Reconcile methods
  //

  public void reconcileAttributes(Element changedElement)
  {
    if (changedElement.hasAttribute(SOAPConstants.USE_ATTRIBUTE))
      setUse(changedElement.getAttribute(SOAPConstants.USE_ATTRIBUTE));
    if (changedElement.hasAttribute(SOAPConstants.NAMESPACE_ATTRIBUTE))
      setNamespaceURI(changedElement.getAttribute(SOAPConstants.NAMESPACE_ATTRIBUTE));
    if (changedElement.hasAttribute(SOAPConstants.ENCODING_STYLE_ATTRIBUTE)) 
    {
      String encodingStyles = changedElement.getAttribute(SOAPConstants.ENCODING_STYLE_ATTRIBUTE);
      StringTokenizer tokenizer = new StringTokenizer(encodingStyles);
      while (tokenizer.hasMoreTokens())
      	getEncodingStyles().add(tokenizer.nextToken());
    }   
    reconcileReferences(false);
  }
  
  public void reconcileReferences(boolean deep)
  {
    if (element != null && element.hasAttribute(SOAPConstants.PARTS_ATTRIBUTE))
    // Synchronize 'parts' variable from element's attribute.
    {
      Message message = getMessage();
      if (message == null)
        return;
      
      String partNames = element.getAttribute(SOAPConstants.PARTS_ATTRIBUTE);
      StringTokenizer parser = new StringTokenizer(partNames," ");
      String partName = null;
      Part newPart = null;
      getParts().clear();
      while(parser.hasMoreTokens())
      {
        partName = parser.nextToken();
        newPart = (message != null) ? (Part) message.getPart(partName) : null;
        if (newPart != null)
          // Do not use getParts() here since it will return a list of
          // implicitly collected parts.
          parts.add(newPart);
      }
    }
    super.reconcileReferences(deep);
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
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPBody_Use())
        niceSetAttribute(theElement,SOAPConstants.USE_ATTRIBUTE,getUse());
      if(eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPBody_NamespaceURI())
        niceSetAttribute(theElement,SOAPConstants.NAMESPACE_ATTRIBUTE,getNamespaceURI());
      if (eAttribute == null || eAttribute == SOAPPackage.eINSTANCE.getSOAPBody_EncodingStyles())
      {
        List encodingStyleList = getEncodingStyles();
        String encodingStyles = "";
        Iterator iterator = encodingStyleList.iterator();
        while (iterator.hasNext())
        {
          if (encodingStyles.equals("")) // first iteration
            encodingStyles += (String)iterator.next();
          else
          	encodingStyles += " " + (String)iterator.next();
        }
        if (!encodingStyles.equals(""))
          niceSetAttribute(theElement,SOAPConstants.ENCODING_STYLE_ATTRIBUTE,encodingStyles);
      }
    }
  }
  
  protected void changeReference(EReference eReference)
  {
    if (isReconciling)
      return;

    super.changeReference(eReference);
    //
    // Update the element's "parts" attribute value.
    //
    Element theElement = getElement();
    if (theElement != null)
    {
      if (eReference == null || eReference == SOAPPackage.eINSTANCE.getSOAPBody_Parts())
      {
        Part part;
        String partNames = "";
        Iterator iter = getParts().iterator();
        while (iter.hasNext())
        {
          part = (Part)iter.next();
          partNames = partNames + " " + part.getName();
        } 
        partNames.trim();
        niceSetAttribute(theElement, SOAPConstants.PARTS_ATTRIBUTE, partNames);
      }     
    }
  } 
  
  /*
  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
    switch (WSDLUtil.getInstance().getWSDLType(child))
    {
      case WSDLConstants.PART :
        Part part = WSDLFactory.eINSTANCE.createPart();
        part.setEnclosingDefinition(getEnclosingDefinition());
        part.setElement(child);
        getParts().add(part);
        break;
      default :
        break;
    }
  }
  */
  
  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(SOAPConstants.SOAP_NAMESPACE_URI, SOAPConstants.BODY_ELEMENT_TAG);
    return elementType;
  } 
 
  private List getImplicitParts()
  {
    List implicitParts = new java.util.Vector();
    Message message = getMessage();
    if(message != null && message.getEParts().size() > 0)
    {
      implicitParts.addAll(message.getEParts());
    }
    return implicitParts;
  }
  
  private Message getMessage()
  {
    Message message = null;
    
    if(eContainer() instanceof BindingInput)
    {
      if(((BindingInput)eContainer()).getEInput() != null)
  	    message = ((BindingInput)eContainer()).getEInput().getEMessage();
    }
    if(eContainer() instanceof BindingOutput)
    {
      if (((BindingOutput)eContainer()).getEOutput() != null)
  	  message = ((BindingOutput)eContainer()).getEOutput().getEMessage();
    }
    if(eContainer() instanceof BindingFault)
    {
      if(((BindingFault)eContainer()).getEFault() != null)
  	  message = ((BindingFault)eContainer()).getEFault().getEMessage();
    }
    return message;
  }


} //SOAPBodyImpl
