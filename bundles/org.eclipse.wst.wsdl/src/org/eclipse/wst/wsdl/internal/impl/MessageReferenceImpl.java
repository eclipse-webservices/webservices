/**
 * <copyright>
 * </copyright>
 *
 * %W%
 * @version %I% %H%
 */
package org.eclipse.wst.wsdl.internal.impl;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.Message;
import org.eclipse.wst.wsdl.MessageReference;
import org.eclipse.wst.wsdl.WSDLPackage;
import org.eclipse.wst.wsdl.internal.util.WSDLConstants;
import org.w3c.dom.Element;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Message Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wsdl.impl.MessageReferenceImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wsdl.impl.MessageReferenceImpl#getEMessage <em>EMessage</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class MessageReferenceImpl extends WSDLElementImpl implements MessageReference
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
   * The cached value of the '{@link #getEMessage() <em>EMessage</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEMessage()
   * @generated
   * @ordered
   */
  protected Message eMessage = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MessageReferenceImpl()
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
    return WSDLPackage.eINSTANCE.getMessageReference();
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
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.MESSAGE_REFERENCE__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Message getEMessage()
  {
    if (eMessage != null && eMessage.eIsProxy())
    {
      Message oldEMessage = eMessage;
      eMessage = (Message)eResolveProxy((InternalEObject)eMessage);
      if (eMessage != oldEMessage)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, WSDLPackage.MESSAGE_REFERENCE__EMESSAGE, oldEMessage, eMessage));
      }
    }
    return eMessage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Message basicGetEMessage()
  {
    return eMessage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEMessage(Message newEMessage)
  {
    Message oldEMessage = eMessage;
    eMessage = newEMessage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, WSDLPackage.MESSAGE_REFERENCE__EMESSAGE, oldEMessage, eMessage));
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
      case WSDLPackage.MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT:
        return getDocumentationElement();
      case WSDLPackage.MESSAGE_REFERENCE__ELEMENT:
        return getElement();
      case WSDLPackage.MESSAGE_REFERENCE__NAME:
        return getName();
      case WSDLPackage.MESSAGE_REFERENCE__EMESSAGE:
        if (resolve) return getEMessage();
        return basicGetEMessage();
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
      case WSDLPackage.MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT:
        setDocumentationElement((Element)newValue);
        return;
      case WSDLPackage.MESSAGE_REFERENCE__ELEMENT:
        setElement((Element)newValue);
        return;
      case WSDLPackage.MESSAGE_REFERENCE__NAME:
        setName((String)newValue);
        return;
      case WSDLPackage.MESSAGE_REFERENCE__EMESSAGE:
        setEMessage((Message)newValue);
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
      case WSDLPackage.MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT:
        setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.MESSAGE_REFERENCE__ELEMENT:
        setElement(ELEMENT_EDEFAULT);
        return;
      case WSDLPackage.MESSAGE_REFERENCE__NAME:
        setName(NAME_EDEFAULT);
        return;
      case WSDLPackage.MESSAGE_REFERENCE__EMESSAGE:
        setEMessage((Message)null);
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
      case WSDLPackage.MESSAGE_REFERENCE__DOCUMENTATION_ELEMENT:
        return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
      case WSDLPackage.MESSAGE_REFERENCE__ELEMENT:
        return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
      case WSDLPackage.MESSAGE_REFERENCE__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case WSDLPackage.MESSAGE_REFERENCE__EMESSAGE:
        return eMessage != null;
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
    setName(WSDLConstants.getAttribute(changedElement, "name"));
    reconcileReferences(false);
  }

  public void handleUnreconciledElement(Element child, Collection remainingModelObjects)
  {
  }

  protected void handleReconciliation(Collection remainingModelObjects)
  {
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
      if (eAttribute == null || eAttribute == WSDLPackage.eINSTANCE.getMessageReference_Name())
        niceSetAttribute(theElement, WSDLConstants.NAME_ATTRIBUTE, getName());
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
      if (eReference == null || eReference == WSDLPackage.eINSTANCE.getMessageReference_EMessage())
      {
        Message message = getEMessage();
        if (message != null)
        {
          QName qName = message.getQName();
          niceSetAttributeURIValue(theElement, WSDLConstants.MESSAGE_ATTRIBUTE, qName.getNamespaceURI() + "#" + qName.getLocalPart());
        }
      }
    }
  }

  public void reconcileReferences(boolean deep)
  {
    if (element != null)
    {
      Definition definition = getEnclosingDefinition();      
      QName messageQName = createQName(definition, element.getAttribute("message"));
      Message newMessage = messageQName != null ? (Message) definition.getMessage(messageQName) : null;
      if (newMessage != getEMessage())
      {
        setEMessage(newMessage);
      }
    }
    super.reconcileReferences(deep);
  }
} //MessageReferenceImpl
