/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl;



import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IWeb Method</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebMethodImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebMethodImpl#isExcluded <em>Excluded</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebMethodImpl#getSoapBindingStyle <em>Soap Binding Style</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebMethodImpl#getSoapBindingUse <em>Soap Binding Use</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebMethodImpl#getSoapBindingParameterStyle <em>Soap Binding Parameter Style</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IWebMethodImpl extends IJavaWebServiceElementImpl implements IWebMethod {
	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<IWebParam> parameters;

	/**
	 * The default value of the '{@link #isExcluded() <em>Excluded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExcluded()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXCLUDED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isExcluded() <em>Excluded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExcluded()
	 * @generated
	 * @ordered
	 */
	protected boolean excluded = EXCLUDED_EDEFAULT;

	/**
	 * The default value of the '{@link #getSoapBindingStyle() <em>Soap Binding Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSoapBindingStyle()
	 * @generated
	 * @ordered
	 */
	protected static final SOAPBindingStyle SOAP_BINDING_STYLE_EDEFAULT = SOAPBindingStyle.DOCUMENT;

	/**
	 * The cached value of the '{@link #getSoapBindingStyle() <em>Soap Binding Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSoapBindingStyle()
	 * @generated
	 * @ordered
	 */
	protected SOAPBindingStyle soapBindingStyle = SOAP_BINDING_STYLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSoapBindingUse() <em>Soap Binding Use</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSoapBindingUse()
	 * @generated
	 * @ordered
	 */
	protected static final SOAPBindingUse SOAP_BINDING_USE_EDEFAULT = SOAPBindingUse.LITERAL;

	/**
	 * The cached value of the '{@link #getSoapBindingUse() <em>Soap Binding Use</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSoapBindingUse()
	 * @generated
	 * @ordered
	 */
	protected SOAPBindingUse soapBindingUse = SOAP_BINDING_USE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSoapBindingParameterStyle() <em>Soap Binding Parameter Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSoapBindingParameterStyle()
	 * @generated
	 * @ordered
	 */
	protected static final SOAPBindingParameterStyle SOAP_BINDING_PARAMETER_STYLE_EDEFAULT = SOAPBindingParameterStyle.WRAPPED;

	/**
	 * The cached value of the '{@link #getSoapBindingParameterStyle() <em>Soap Binding Parameter Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSoapBindingParameterStyle()
	 * @generated
	 * @ordered
	 */
	protected SOAPBindingParameterStyle soapBindingParameterStyle = SOAP_BINDING_PARAMETER_STYLE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IWebMethodImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DomPackage.Literals.IWEB_METHOD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IWebParam> getParameters() {
		if (parameters == null)
		{
			parameters = new EObjectContainmentEList<IWebParam>(IWebParam.class, this, DomPackage.IWEB_METHOD__PARAMETERS);
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isExcluded() {
		return excluded;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExcluded(boolean newExcluded) {
		boolean oldExcluded = excluded;
		excluded = newExcluded;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_METHOD__EXCLUDED, oldExcluded, excluded));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SOAPBindingStyle getSoapBindingStyle() {
		return soapBindingStyle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSoapBindingStyle(SOAPBindingStyle newSoapBindingStyle) {
		SOAPBindingStyle oldSoapBindingStyle = soapBindingStyle;
		soapBindingStyle = newSoapBindingStyle == null ? SOAP_BINDING_STYLE_EDEFAULT : newSoapBindingStyle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_METHOD__SOAP_BINDING_STYLE, oldSoapBindingStyle, soapBindingStyle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SOAPBindingUse getSoapBindingUse() {
		return soapBindingUse;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSoapBindingUse(SOAPBindingUse newSoapBindingUse) {
		SOAPBindingUse oldSoapBindingUse = soapBindingUse;
		soapBindingUse = newSoapBindingUse == null ? SOAP_BINDING_USE_EDEFAULT : newSoapBindingUse;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_METHOD__SOAP_BINDING_USE, oldSoapBindingUse, soapBindingUse));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SOAPBindingParameterStyle getSoapBindingParameterStyle() {
		return soapBindingParameterStyle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSoapBindingParameterStyle(SOAPBindingParameterStyle newSoapBindingParameterStyle) {
		SOAPBindingParameterStyle oldSoapBindingParameterStyle = soapBindingParameterStyle;
		soapBindingParameterStyle = newSoapBindingParameterStyle == null ? SOAP_BINDING_PARAMETER_STYLE_EDEFAULT : newSoapBindingParameterStyle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_METHOD__SOAP_BINDING_PARAMETER_STYLE, oldSoapBindingParameterStyle, soapBindingParameterStyle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID)
		{
			case DomPackage.IWEB_METHOD__PARAMETERS:
				return ((InternalEList<?>)getParameters()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
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
			case DomPackage.IWEB_METHOD__PARAMETERS:
				return getParameters();
			case DomPackage.IWEB_METHOD__EXCLUDED:
				return isExcluded();
			case DomPackage.IWEB_METHOD__SOAP_BINDING_STYLE:
				return getSoapBindingStyle();
			case DomPackage.IWEB_METHOD__SOAP_BINDING_USE:
				return getSoapBindingUse();
			case DomPackage.IWEB_METHOD__SOAP_BINDING_PARAMETER_STYLE:
				return getSoapBindingParameterStyle();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID)
		{
			case DomPackage.IWEB_METHOD__PARAMETERS:
				getParameters().clear();
				getParameters().addAll((Collection<? extends IWebParam>)newValue);
				return;
			case DomPackage.IWEB_METHOD__EXCLUDED:
				setExcluded((Boolean)newValue);
				return;
			case DomPackage.IWEB_METHOD__SOAP_BINDING_STYLE:
				setSoapBindingStyle((SOAPBindingStyle)newValue);
				return;
			case DomPackage.IWEB_METHOD__SOAP_BINDING_USE:
				setSoapBindingUse((SOAPBindingUse)newValue);
				return;
			case DomPackage.IWEB_METHOD__SOAP_BINDING_PARAMETER_STYLE:
				setSoapBindingParameterStyle((SOAPBindingParameterStyle)newValue);
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
			case DomPackage.IWEB_METHOD__PARAMETERS:
				getParameters().clear();
				return;
			case DomPackage.IWEB_METHOD__EXCLUDED:
				setExcluded(EXCLUDED_EDEFAULT);
				return;
			case DomPackage.IWEB_METHOD__SOAP_BINDING_STYLE:
				setSoapBindingStyle(SOAP_BINDING_STYLE_EDEFAULT);
				return;
			case DomPackage.IWEB_METHOD__SOAP_BINDING_USE:
				setSoapBindingUse(SOAP_BINDING_USE_EDEFAULT);
				return;
			case DomPackage.IWEB_METHOD__SOAP_BINDING_PARAMETER_STYLE:
				setSoapBindingParameterStyle(SOAP_BINDING_PARAMETER_STYLE_EDEFAULT);
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
			case DomPackage.IWEB_METHOD__PARAMETERS:
				return parameters != null && !parameters.isEmpty();
			case DomPackage.IWEB_METHOD__EXCLUDED:
				return excluded != EXCLUDED_EDEFAULT;
			case DomPackage.IWEB_METHOD__SOAP_BINDING_STYLE:
				return soapBindingStyle != SOAP_BINDING_STYLE_EDEFAULT;
			case DomPackage.IWEB_METHOD__SOAP_BINDING_USE:
				return soapBindingUse != SOAP_BINDING_USE_EDEFAULT;
			case DomPackage.IWEB_METHOD__SOAP_BINDING_PARAMETER_STYLE:
				return soapBindingParameterStyle != SOAP_BINDING_PARAMETER_STYLE_EDEFAULT;
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
		result.append(" (excluded: "); //$NON-NLS-1$
		result.append(excluded);
		result.append(", soapBindingStyle: "); //$NON-NLS-1$
		result.append(soapBindingStyle);
		result.append(", soapBindingUse: "); //$NON-NLS-1$
		result.append(soapBindingUse);
		result.append(", soapBindingParameterStyle: "); //$NON-NLS-1$
		result.append(soapBindingParameterStyle);
		result.append(')');
		return result.toString();
	}

} //IWebMethodImpl
