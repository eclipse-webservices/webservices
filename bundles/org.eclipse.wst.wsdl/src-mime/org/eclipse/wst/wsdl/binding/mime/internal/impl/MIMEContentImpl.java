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
import org.eclipse.wst.wsdl.binding.mime.MIMEContent;
import org.eclipse.wst.wsdl.binding.mime.MIMEPackage;
import org.eclipse.wst.wsdl.binding.mime.internal.util.MIMEConstants;
import org.eclipse.wst.wsdl.internal.impl.ExtensibilityElementImpl;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Content</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.wsdl.binding.mime.impl.MIMEContentImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.wst.wsdl.binding.mime.impl.MIMEContentImpl#getEPart <em>EPart</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MIMEContentImpl extends ExtensibilityElementImpl implements MIMEContent {
	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getEPart() <em>EPart</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEPart()
	 * @generated
	 * @ordered
	 */
	protected Part ePart = null;
	
    private String part; // TBD

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MIMEContentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return MIMEPackage.eINSTANCE.getMIMEContent();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MIMEPackage.MIME_CONTENT__TYPE, oldType, type));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MIMEPackage.MIME_CONTENT__EPART, oldEPart, ePart));
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
			eNotify(new ENotificationImpl(this, Notification.SET, MIMEPackage.MIME_CONTENT__EPART, oldEPart, ePart));
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
			case MIMEPackage.MIME_CONTENT__DOCUMENTATION_ELEMENT:
				return getDocumentationElement();
			case MIMEPackage.MIME_CONTENT__ELEMENT:
				return getElement();
			case MIMEPackage.MIME_CONTENT__REQUIRED:
				return isRequired() ? Boolean.TRUE : Boolean.FALSE;
			case MIMEPackage.MIME_CONTENT__ELEMENT_TYPE:
				return getElementType();
			case MIMEPackage.MIME_CONTENT__TYPE:
				return getType();
			case MIMEPackage.MIME_CONTENT__EPART:
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
			case MIMEPackage.MIME_CONTENT__DOCUMENTATION_ELEMENT:
				setDocumentationElement((Element)newValue);
				return;
			case MIMEPackage.MIME_CONTENT__ELEMENT:
				setElement((Element)newValue);
				return;
			case MIMEPackage.MIME_CONTENT__REQUIRED:
				setRequired(((Boolean)newValue).booleanValue());
				return;
			case MIMEPackage.MIME_CONTENT__ELEMENT_TYPE:
				setElementType((QName)newValue);
				return;
			case MIMEPackage.MIME_CONTENT__TYPE:
				setType((String)newValue);
				return;
			case MIMEPackage.MIME_CONTENT__EPART:
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
			case MIMEPackage.MIME_CONTENT__DOCUMENTATION_ELEMENT:
				setDocumentationElement(DOCUMENTATION_ELEMENT_EDEFAULT);
				return;
			case MIMEPackage.MIME_CONTENT__ELEMENT:
				setElement(ELEMENT_EDEFAULT);
				return;
			case MIMEPackage.MIME_CONTENT__REQUIRED:
				setRequired(REQUIRED_EDEFAULT);
				return;
			case MIMEPackage.MIME_CONTENT__ELEMENT_TYPE:
				setElementType(ELEMENT_TYPE_EDEFAULT);
				return;
			case MIMEPackage.MIME_CONTENT__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case MIMEPackage.MIME_CONTENT__EPART:
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
			case MIMEPackage.MIME_CONTENT__DOCUMENTATION_ELEMENT:
				return DOCUMENTATION_ELEMENT_EDEFAULT == null ? documentationElement != null : !DOCUMENTATION_ELEMENT_EDEFAULT.equals(documentationElement);
			case MIMEPackage.MIME_CONTENT__ELEMENT:
				return ELEMENT_EDEFAULT == null ? element != null : !ELEMENT_EDEFAULT.equals(element);
			case MIMEPackage.MIME_CONTENT__REQUIRED:
				return required != REQUIRED_EDEFAULT;
			case MIMEPackage.MIME_CONTENT__ELEMENT_TYPE:
				return ELEMENT_TYPE_EDEFAULT == null ? elementType != null : !ELEMENT_TYPE_EDEFAULT.equals(elementType);
			case MIMEPackage.MIME_CONTENT__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case MIMEPackage.MIME_CONTENT__EPART:
				return ePart != null;
		}
		return eDynamicIsSet(eFeature);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

  //
  // Reconcile methods: DOM -> Model
  //

  public void reconcileAttributes(Element changedElement)
  {
  	setPart
      (MIMEConstants.getAttribute(changedElement, MIMEConstants.PART_ATTRIBUTE));
  	setType
      (MIMEConstants.getAttribute(changedElement, MIMEConstants.TYPE_ATTRIBUTE));
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
      if (eAttribute == null || eAttribute == MIMEPackage.eINSTANCE. getMIMEContent_Type())
        niceSetAttribute(theElement,MIMEConstants.TYPE_ATTRIBUTE,getType());
    }
  }

} //MIMEContentImpl
