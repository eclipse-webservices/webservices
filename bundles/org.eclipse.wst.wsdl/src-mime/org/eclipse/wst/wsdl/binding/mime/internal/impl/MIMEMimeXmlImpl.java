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
package org.eclipse.wst.wsdl.binding.mime.internal.impl;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.wst.wsdl.Part;
import org.eclipse.wst.wsdl.binding.mime.MIMEMimeXml;
import org.eclipse.wst.wsdl.binding.mime.MIMEPackage;
import org.eclipse.wst.wsdl.binding.mime.internal.util.MIMEConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mime Xml</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.mime.impl.MIMEMimeXmlImpl#getEPart <em>EPart</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MIMEMimeXmlImpl extends ExtensibilityElementImpl implements MIMEMimeXml {
	/**
   * This class is not intended to be serialized.
   * serialVersionUID is assigned with 1L to avoid
   * compiler warning messages.
   */
  private static final long serialVersionUID = 1L;

  /**
	 * The cached value of the '{@link #getEPart() <em>EPart</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEPart()
	 * @generated
	 * @ordered
	 */
	protected Part ePart = null;
	
    private String part;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MIMEMimeXmlImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return MIMEPackage.eINSTANCE.getMIMEMimeXml();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Part getEPart() {
		if (ePart != null && ePart.eIsProxy()) {
			Part oldEPart = ePart;
			ePart = (Part)eResolveProxy((InternalEObject)ePart);
			if (ePart != oldEPart) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MIMEPackage.MIME_MIME_XML__EPART, oldEPart, ePart));
			}
		}
		return ePart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Part basicGetEPart() {
		return ePart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEPart(Part newEPart) {
		Part oldEPart = ePart;
		ePart = newEPart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MIMEPackage.MIME_MIME_XML__EPART, oldEPart, ePart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void setPart(String part) {
      this.part = part;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getPart() {
      return part;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case MIMEPackage.MIME_MIME_XML__DOCUMENTATION_ELEMENT:
				return getDocumentationElement();
			case MIMEPackage.MIME_MIME_XML__ELEMENT:
				return getElement();
			case MIMEPackage.MIME_MIME_XML__REQUIRED:
				return isRequired() ? Boolean.TRUE : Boolean.FALSE;
			case MIMEPackage.MIME_MIME_XML__ELEMENT_TYPE:
				return getElementType();
			case MIMEPackage.MIME_MIME_XML__EPART:
				if (resolve) return getEPart();
				return basicGetEPart();
		}
		return eDynamicGet(eFeature, resolve);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(EStructuralFeature eFeature, Object newValue) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case MIMEPackage.MIME_MIME_XML__DOCUMENTATION_ELEMENT:
				setDocumentationElement((Element)newValue);
				return;
			case MIMEPackage.MIME_MIME_XML__ELEMENT:
				setElement((Element)newValue);
				return;
			case MIMEPackage.MIME_MIME_XML__REQUIRED:
				setRequired(((Boolean)newValue).booleanValue());
				return;
			case MIMEPackage.MIME_MIME_XML__ELEMENT_TYPE:
				setElementType((QName)newValue);
				return;
			case MIMEPackage.MIME_MIME_XML__EPART:
				setEPart((Part)newValue);
				return;
		}
		eDynamicSet(eFeature, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case MIMEPackage.MIME_MIME_XML__DOCUMENTATION_ELEMENT:
				setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
				return;
			case MIMEPackage.MIME_MIME_XML__ELEMENT:
				setElement(ELEMENT_EDEFAULT);
				return;
			case MIMEPackage.MIME_MIME_XML__REQUIRED:
				setRequired(REQUIRED_EDEFAULT);
				return;
			case MIMEPackage.MIME_MIME_XML__ELEMENT_TYPE:
				setElementType(ELEMENT_TYPE_EDEFAULT);
				return;
			case MIMEPackage.MIME_MIME_XML__EPART:
				setEPart((Part)null);
				return;
		}
		eDynamicUnset(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(EStructuralFeature eFeature) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case MIMEPackage.MIME_MIME_XML__DOCUMENTATION_ELEMENT:
				return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
			case MIMEPackage.MIME_MIME_XML__ELEMENT:
				return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
			case MIMEPackage.MIME_MIME_XML__REQUIRED:
				return required != REQUIRED_EDEFAULT;
			case MIMEPackage.MIME_MIME_XML__ELEMENT_TYPE:
				return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
			case MIMEPackage.MIME_MIME_XML__EPART:
				return ePart != null;
		}
		return eDynamicIsSet(eFeature);
	}

  //
  // Reconcile methods: DOM -> Model
  //

  public void reconcileAttributes(Element changedElement)
  {
  	setPart
      (MIMEConstants.getAttribute(changedElement, MIMEConstants.PART_ATTRIBUTE));
    reconcileReferences(false);
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
      if (eAttribute == null || eAttribute == MIMEPackage.eINSTANCE. getMIMEContent_EPart())
        niceSetAttribute(theElement,MIMEConstants.PART_ATTRIBUTE,getPart());
    }
  }

  public QName getElementType()
  {
    if (elementType == null)
      elementType = new QName(MIMEConstants.MIME_NAMESPACE_URI, MIMEConstants.MIME_XML_ELEMENT_TAG);
    return elementType;
  } 
} //MIMEMimeXmlImpl
