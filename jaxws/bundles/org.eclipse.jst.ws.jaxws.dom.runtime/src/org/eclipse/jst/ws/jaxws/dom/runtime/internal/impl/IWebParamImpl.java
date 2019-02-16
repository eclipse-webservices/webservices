/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl;


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.WebParamKind;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IWeb Param</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebParamImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebParamImpl#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebParamImpl#getPartName <em>Part Name</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebParamImpl#getTargetNamespace <em>Target Namespace</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebParamImpl#isHeader <em>Header</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IWebParamImpl extends IJavaWebServiceElementImpl implements IWebParam {
	/**
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final WebParamKind KIND_EDEFAULT = WebParamKind.IN;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected WebParamKind kind = KIND_EDEFAULT;

	/**
	 * The default value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeName()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypeName()
	 * @generated
	 * @ordered
	 */
	protected String typeName = TYPE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPartName() <em>Part Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPartName()
	 * @generated
	 * @ordered
	 */
	protected static final String PART_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPartName() <em>Part Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPartName()
	 * @generated
	 * @ordered
	 */
	protected String partName = PART_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getTargetNamespace() <em>Target Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetNamespace()
	 * @generated
	 * @ordered
	 */
	protected static final String TARGET_NAMESPACE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTargetNamespace() <em>Target Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetNamespace()
	 * @generated
	 * @ordered
	 */
	protected String targetNamespace = TARGET_NAMESPACE_EDEFAULT;

	/**
	 * The default value of the '{@link #isHeader() <em>Header</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHeader()
	 * @generated
	 * @ordered
	 */
	protected static final boolean HEADER_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isHeader() <em>Header</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHeader()
	 * @generated
	 * @ordered
	 */
	protected boolean header = HEADER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IWebParamImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DomPackage.Literals.IWEB_PARAM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WebParamKind getKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKind(WebParamKind newKind) {
		WebParamKind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_PARAM__KIND, oldKind, kind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypeName(String newTypeName) {
		String oldTypeName = typeName;
		typeName = newTypeName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_PARAM__TYPE_NAME, oldTypeName, typeName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPartName() {
		return partName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPartName(String newPartName) {
		String oldPartName = partName;
		partName = newPartName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_PARAM__PART_NAME, oldPartName, partName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTargetNamespace() {
		return targetNamespace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetNamespace(String newTargetNamespace) {
		String oldTargetNamespace = targetNamespace;
		targetNamespace = newTargetNamespace;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_PARAM__TARGET_NAMESPACE, oldTargetNamespace, targetNamespace));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isHeader() {
		return header;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeader(boolean newHeader) {
		boolean oldHeader = header;
		header = newHeader;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_PARAM__HEADER, oldHeader, header));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID)
		{
			case DomPackage.IWEB_PARAM__KIND:
				return getKind();
			case DomPackage.IWEB_PARAM__TYPE_NAME:
				return getTypeName();
			case DomPackage.IWEB_PARAM__PART_NAME:
				return getPartName();
			case DomPackage.IWEB_PARAM__TARGET_NAMESPACE:
				return getTargetNamespace();
			case DomPackage.IWEB_PARAM__HEADER:
				return isHeader();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID)
		{
			case DomPackage.IWEB_PARAM__KIND:
				setKind((WebParamKind)newValue);
				return;
			case DomPackage.IWEB_PARAM__TYPE_NAME:
				setTypeName((String)newValue);
				return;
			case DomPackage.IWEB_PARAM__PART_NAME:
				setPartName((String)newValue);
				return;
			case DomPackage.IWEB_PARAM__TARGET_NAMESPACE:
				setTargetNamespace((String)newValue);
				return;
			case DomPackage.IWEB_PARAM__HEADER:
				setHeader((Boolean)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID)
		{
			case DomPackage.IWEB_PARAM__KIND:
				setKind(KIND_EDEFAULT);
				return;
			case DomPackage.IWEB_PARAM__TYPE_NAME:
				setTypeName(TYPE_NAME_EDEFAULT);
				return;
			case DomPackage.IWEB_PARAM__PART_NAME:
				setPartName(PART_NAME_EDEFAULT);
				return;
			case DomPackage.IWEB_PARAM__TARGET_NAMESPACE:
				setTargetNamespace(TARGET_NAMESPACE_EDEFAULT);
				return;
			case DomPackage.IWEB_PARAM__HEADER:
				setHeader(HEADER_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID)
		{
			case DomPackage.IWEB_PARAM__KIND:
				return kind != KIND_EDEFAULT;
			case DomPackage.IWEB_PARAM__TYPE_NAME:
				return TYPE_NAME_EDEFAULT == null ? typeName != null : !TYPE_NAME_EDEFAULT.equals(typeName);
			case DomPackage.IWEB_PARAM__PART_NAME:
				return PART_NAME_EDEFAULT == null ? partName != null : !PART_NAME_EDEFAULT.equals(partName);
			case DomPackage.IWEB_PARAM__TARGET_NAMESPACE:
				return TARGET_NAMESPACE_EDEFAULT == null ? targetNamespace != null : !TARGET_NAMESPACE_EDEFAULT.equals(targetNamespace);
			case DomPackage.IWEB_PARAM__HEADER:
				return header != HEADER_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (kind: "); //$NON-NLS-1$
		result.append(kind);
		result.append(", typeName: "); //$NON-NLS-1$
		result.append(typeName);
		result.append(", partName: "); //$NON-NLS-1$
		result.append(partName);
		result.append(", targetNamespace: "); //$NON-NLS-1$
		result.append(targetNamespace);
		result.append(", header: "); //$NON-NLS-1$
		result.append(header);
		result.append(')');
		return result.toString();
	}

} //IWebParamImpl
