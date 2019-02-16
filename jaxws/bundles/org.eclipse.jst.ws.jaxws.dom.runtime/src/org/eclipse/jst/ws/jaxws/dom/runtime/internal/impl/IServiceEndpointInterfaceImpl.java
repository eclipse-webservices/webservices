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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebMethod;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingParameterStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingStyle;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.SOAPBindingUse;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IService Endpoint Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IServiceEndpointInterfaceImpl#isImplicit <em>Implicit</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IServiceEndpointInterfaceImpl#getImplementingWebServices <em>Implementing Web Services</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IServiceEndpointInterfaceImpl#getWebMethods <em>Web Methods</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IServiceEndpointInterfaceImpl#getTargetNamespace <em>Target Namespace</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IServiceEndpointInterfaceImpl#getSoapBindingStyle <em>Soap Binding Style</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IServiceEndpointInterfaceImpl#getSoapBindingUse <em>Soap Binding Use</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IServiceEndpointInterfaceImpl#getSoapBindingParameterStyle <em>Soap Binding Parameter Style</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IServiceEndpointInterfaceImpl extends IJavaWebServiceElementImpl implements IServiceEndpointInterface {
	/**
	 * The default value of the '{@link #isImplicit() <em>Implicit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isImplicit()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IMPLICIT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isImplicit() <em>Implicit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isImplicit()
	 * @generated
	 * @ordered
	 */
	protected boolean implicit = IMPLICIT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getImplementingWebServices() <em>Implementing Web Services</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImplementingWebServices()
	 * @generated
	 * @ordered
	 */
	protected EList<IWebService> implementingWebServices;

	/**
	 * The cached value of the '{@link #getWebMethods() <em>Web Methods</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWebMethods()
	 * @generated
	 * @ordered
	 */
	protected EList<IWebMethod> webMethods;

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
	protected IServiceEndpointInterfaceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DomPackage.Literals.ISERVICE_ENDPOINT_INTERFACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isImplicit() {
		return implicit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setImplicit(boolean newImplicit) {
		boolean oldImplicit = implicit;
		implicit = newImplicit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT, oldImplicit, implicit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IWebService> getImplementingWebServices() {
		if (implementingWebServices == null)
		{
			implementingWebServices = new EObjectWithInverseResolvingEList<IWebService>(IWebService.class, this, DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES, DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT);
		}
		return implementingWebServices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IWebMethod> getWebMethods() {
		if (webMethods == null)
		{
			webMethods = new EObjectContainmentEList<IWebMethod>(IWebMethod.class, this, DomPackage.ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS);
		}
		return webMethods;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE, oldTargetNamespace, targetNamespace));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_STYLE, oldSoapBindingStyle, soapBindingStyle));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_USE, oldSoapBindingUse, soapBindingUse));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_PARAMETER_STYLE, oldSoapBindingParameterStyle, soapBindingParameterStyle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID)
		{
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getImplementingWebServices()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
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
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES:
				return ((InternalEList<?>)getImplementingWebServices()).basicRemove(otherEnd, msgs);
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS:
				return ((InternalEList<?>)getWebMethods()).basicRemove(otherEnd, msgs);
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
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT:
				return isImplicit();
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES:
				return getImplementingWebServices();
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS:
				return getWebMethods();
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE:
				return getTargetNamespace();
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_STYLE:
				return getSoapBindingStyle();
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_USE:
				return getSoapBindingUse();
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_PARAMETER_STYLE:
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
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT:
				setImplicit((Boolean)newValue);
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES:
				getImplementingWebServices().clear();
				getImplementingWebServices().addAll((Collection<? extends IWebService>)newValue);
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS:
				getWebMethods().clear();
				getWebMethods().addAll((Collection<? extends IWebMethod>)newValue);
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE:
				setTargetNamespace((String)newValue);
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_STYLE:
				setSoapBindingStyle((SOAPBindingStyle)newValue);
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_USE:
				setSoapBindingUse((SOAPBindingUse)newValue);
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_PARAMETER_STYLE:
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
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT:
				setImplicit(IMPLICIT_EDEFAULT);
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES:
				getImplementingWebServices().clear();
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS:
				getWebMethods().clear();
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE:
				setTargetNamespace(TARGET_NAMESPACE_EDEFAULT);
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_STYLE:
				setSoapBindingStyle(SOAP_BINDING_STYLE_EDEFAULT);
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_USE:
				setSoapBindingUse(SOAP_BINDING_USE_EDEFAULT);
				return;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_PARAMETER_STYLE:
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
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLICIT:
				return implicit != IMPLICIT_EDEFAULT;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES:
				return implementingWebServices != null && !implementingWebServices.isEmpty();
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__WEB_METHODS:
				return webMethods != null && !webMethods.isEmpty();
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__TARGET_NAMESPACE:
				return TARGET_NAMESPACE_EDEFAULT == null ? targetNamespace != null : !TARGET_NAMESPACE_EDEFAULT.equals(targetNamespace);
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_STYLE:
				return soapBindingStyle != SOAP_BINDING_STYLE_EDEFAULT;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_USE:
				return soapBindingUse != SOAP_BINDING_USE_EDEFAULT;
			case DomPackage.ISERVICE_ENDPOINT_INTERFACE__SOAP_BINDING_PARAMETER_STYLE:
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
		result.append(" (implicit: "); //$NON-NLS-1$
		result.append(implicit);
		result.append(", targetNamespace: "); //$NON-NLS-1$
		result.append(targetNamespace);
		result.append(", soapBindingStyle: "); //$NON-NLS-1$
		result.append(soapBindingStyle);
		result.append(", soapBindingUse: "); //$NON-NLS-1$
		result.append(soapBindingUse);
		result.append(", soapBindingParameterStyle: "); //$NON-NLS-1$
		result.append(soapBindingParameterStyle);
		result.append(')');
		return result.toString();
	}
	
	@Override 
	public EObject eContainer()
	{
		if (super.eContainer()!=null) {
			return super.eContainer();
		}
		
		for (IWebService ws : getImplementingWebServices()) {
			if (ws.eContainer()!=null) {
				return ws.eContainer();
			}
		}
		
		return null;
	}

} //IServiceEndpointInterfaceImpl
