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


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IServiceEndpointInterface;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebService;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>IWeb Service</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceImpl#getServiceEndpoint <em>Service Endpoint</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceImpl#getTargetNamespace <em>Target Namespace</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceImpl#getPortName <em>Port Name</em>}</li>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.IWebServiceImpl#getWsdlLocation <em>Wsdl Location</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IWebServiceImpl extends IJavaWebServiceElementImpl implements IWebService {
	/**
	 * The cached value of the '{@link #getServiceEndpoint() <em>Service Endpoint</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceEndpoint()
	 * @generated
	 * @ordered
	 */
	protected IServiceEndpointInterface serviceEndpoint;

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
	 * The default value of the '{@link #getPortName() <em>Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortName()
	 * @generated
	 * @ordered
	 */
	protected static final String PORT_NAME_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getPortName() <em>Port Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortName()
	 * @generated
	 * @ordered
	 */
	protected String portName = PORT_NAME_EDEFAULT;
	/**
	 * The default value of the '{@link #getWsdlLocation() <em>Wsdl Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWsdlLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String WSDL_LOCATION_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getWsdlLocation() <em>Wsdl Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWsdlLocation()
	 * @generated
	 * @ordered
	 */
	protected String wsdlLocation = WSDL_LOCATION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IWebServiceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DomPackage.Literals.IWEB_SERVICE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IServiceEndpointInterface getServiceEndpoint() {
		if (serviceEndpoint != null && serviceEndpoint.eIsProxy())
		{
			InternalEObject oldServiceEndpoint = (InternalEObject)serviceEndpoint;
			serviceEndpoint = (IServiceEndpointInterface)eResolveProxy(oldServiceEndpoint);
			if (serviceEndpoint != oldServiceEndpoint)
			{
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT, oldServiceEndpoint, serviceEndpoint));
			}
		}
		return serviceEndpoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IServiceEndpointInterface basicGetServiceEndpoint() {
		return serviceEndpoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetServiceEndpoint(IServiceEndpointInterface newServiceEndpoint, NotificationChain msgs) {
		IServiceEndpointInterface oldServiceEndpoint = serviceEndpoint;
		serviceEndpoint = newServiceEndpoint;
		if (eNotificationRequired())
		{
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT, oldServiceEndpoint, newServiceEndpoint);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceEndpoint(IServiceEndpointInterface newServiceEndpoint) {
		if (newServiceEndpoint != serviceEndpoint)
		{
			NotificationChain msgs = null;
			if (serviceEndpoint != null)
				msgs = ((InternalEObject)serviceEndpoint).eInverseRemove(this, DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES, IServiceEndpointInterface.class, msgs);
			if (newServiceEndpoint != null)
				msgs = ((InternalEObject)newServiceEndpoint).eInverseAdd(this, DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES, IServiceEndpointInterface.class, msgs);
			msgs = basicSetServiceEndpoint(newServiceEndpoint, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT, newServiceEndpoint, newServiceEndpoint));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_SERVICE__TARGET_NAMESPACE, oldTargetNamespace, targetNamespace));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPortName() {
		return portName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPortName(String newPortName) {
		String oldPortName = portName;
		portName = newPortName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_SERVICE__PORT_NAME, oldPortName, portName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWsdlLocation() {
		return wsdlLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWsdlLocation(String newWsdlLocation) {
		String oldWsdlLocation = wsdlLocation;
		wsdlLocation = newWsdlLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DomPackage.IWEB_SERVICE__WSDL_LOCATION, oldWsdlLocation, wsdlLocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID)
		{
			case DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT:
				if (serviceEndpoint != null)
					msgs = ((InternalEObject)serviceEndpoint).eInverseRemove(this, DomPackage.ISERVICE_ENDPOINT_INTERFACE__IMPLEMENTING_WEB_SERVICES, IServiceEndpointInterface.class, msgs);
				return basicSetServiceEndpoint((IServiceEndpointInterface)otherEnd, msgs);
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
			case DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT:
				return basicSetServiceEndpoint(null, msgs);
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
			case DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT:
				if (resolve) return getServiceEndpoint();
				return basicGetServiceEndpoint();
			case DomPackage.IWEB_SERVICE__TARGET_NAMESPACE:
				return getTargetNamespace();
			case DomPackage.IWEB_SERVICE__PORT_NAME:
				return getPortName();
			case DomPackage.IWEB_SERVICE__WSDL_LOCATION:
				return getWsdlLocation();
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
			case DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT:
				setServiceEndpoint((IServiceEndpointInterface)newValue);
				return;
			case DomPackage.IWEB_SERVICE__TARGET_NAMESPACE:
				setTargetNamespace((String)newValue);
				return;
			case DomPackage.IWEB_SERVICE__PORT_NAME:
				setPortName((String)newValue);
				return;
			case DomPackage.IWEB_SERVICE__WSDL_LOCATION:
				setWsdlLocation((String)newValue);
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
			case DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT:
				setServiceEndpoint((IServiceEndpointInterface)null);
				return;
			case DomPackage.IWEB_SERVICE__TARGET_NAMESPACE:
				setTargetNamespace(TARGET_NAMESPACE_EDEFAULT);
				return;
			case DomPackage.IWEB_SERVICE__PORT_NAME:
				setPortName(PORT_NAME_EDEFAULT);
				return;
			case DomPackage.IWEB_SERVICE__WSDL_LOCATION:
				setWsdlLocation(WSDL_LOCATION_EDEFAULT);
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
			case DomPackage.IWEB_SERVICE__SERVICE_ENDPOINT:
				return serviceEndpoint != null;
			case DomPackage.IWEB_SERVICE__TARGET_NAMESPACE:
				return TARGET_NAMESPACE_EDEFAULT == null ? targetNamespace != null : !TARGET_NAMESPACE_EDEFAULT.equals(targetNamespace);
			case DomPackage.IWEB_SERVICE__PORT_NAME:
				return PORT_NAME_EDEFAULT == null ? portName != null : !PORT_NAME_EDEFAULT.equals(portName);
			case DomPackage.IWEB_SERVICE__WSDL_LOCATION:
				return WSDL_LOCATION_EDEFAULT == null ? wsdlLocation != null : !WSDL_LOCATION_EDEFAULT.equals(wsdlLocation);
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
		result.append(" (targetNamespace: "); //$NON-NLS-1$
		result.append(targetNamespace);
		result.append(", portName: "); //$NON-NLS-1$
		result.append(portName);
		result.append(", wsdlLocation: "); //$NON-NLS-1$
		result.append(wsdlLocation);
		result.append(')');
		return result.toString();
	}

} //IWebServiceImpl
