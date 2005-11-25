/**
 * <copyright>
 * </copyright>
 *
 * $Id: RegistryImpl.java,v 1.2 2005/12/03 04:06:47 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.registry.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.wst.ws.internal.model.v10.registry.Description;
import org.eclipse.wst.ws.internal.model.v10.registry.Name;
import org.eclipse.wst.ws.internal.model.v10.registry.Registry;
import org.eclipse.wst.ws.internal.model.v10.registry.RegistryPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Registry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.registry.impl.RegistryImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.registry.impl.RegistryImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.registry.impl.RegistryImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.registry.impl.RegistryImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.registry.impl.RegistryImpl#getRef <em>Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RegistryImpl extends EObjectImpl implements Registry {
	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected EList name = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected EList description = null;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected String location = LOCATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getRef() <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRef()
	 * @generated
	 * @ordered
	 */
	protected static final String REF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRef() <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRef()
	 * @generated
	 * @ordered
	 */
	protected String ref = REF_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RegistryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return RegistryPackage.eINSTANCE.getRegistry();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getName() {
		if (name == null) {
			name = new EObjectContainmentEList(Name.class, this, RegistryPackage.REGISTRY__NAME);
		}
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getDescription() {
		if (description == null) {
			description = new EObjectContainmentEList(Description.class, this, RegistryPackage.REGISTRY__DESCRIPTION);
		}
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RegistryPackage.REGISTRY__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocation(String newLocation) {
		String oldLocation = location;
		location = newLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RegistryPackage.REGISTRY__LOCATION, oldLocation, location));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRef(String newRef) {
		String oldRef = ref;
		ref = newRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RegistryPackage.REGISTRY__REF, oldRef, ref));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case RegistryPackage.REGISTRY__NAME:
					return ((InternalEList)getName()).basicRemove(otherEnd, msgs);
				case RegistryPackage.REGISTRY__DESCRIPTION:
					return ((InternalEList)getDescription()).basicRemove(otherEnd, msgs);
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
	public Object eGet(EStructuralFeature eFeature, boolean resolve) {
		switch (eDerivedStructuralFeatureID(eFeature)) {
			case RegistryPackage.REGISTRY__NAME:
				return getName();
			case RegistryPackage.REGISTRY__DESCRIPTION:
				return getDescription();
			case RegistryPackage.REGISTRY__ID:
				return getId();
			case RegistryPackage.REGISTRY__LOCATION:
				return getLocation();
			case RegistryPackage.REGISTRY__REF:
				return getRef();
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
			case RegistryPackage.REGISTRY__NAME:
				getName().clear();
				getName().addAll((Collection)newValue);
				return;
			case RegistryPackage.REGISTRY__DESCRIPTION:
				getDescription().clear();
				getDescription().addAll((Collection)newValue);
				return;
			case RegistryPackage.REGISTRY__ID:
				setId((String)newValue);
				return;
			case RegistryPackage.REGISTRY__LOCATION:
				setLocation((String)newValue);
				return;
			case RegistryPackage.REGISTRY__REF:
				setRef((String)newValue);
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
			case RegistryPackage.REGISTRY__NAME:
				getName().clear();
				return;
			case RegistryPackage.REGISTRY__DESCRIPTION:
				getDescription().clear();
				return;
			case RegistryPackage.REGISTRY__ID:
				setId(ID_EDEFAULT);
				return;
			case RegistryPackage.REGISTRY__LOCATION:
				setLocation(LOCATION_EDEFAULT);
				return;
			case RegistryPackage.REGISTRY__REF:
				setRef(REF_EDEFAULT);
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
			case RegistryPackage.REGISTRY__NAME:
				return name != null && !name.isEmpty();
			case RegistryPackage.REGISTRY__DESCRIPTION:
				return description != null && !description.isEmpty();
			case RegistryPackage.REGISTRY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case RegistryPackage.REGISTRY__LOCATION:
				return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
			case RegistryPackage.REGISTRY__REF:
				return REF_EDEFAULT == null ? ref != null : !REF_EDEFAULT.equals(ref);
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
		result.append(" (id: ");
		result.append(id);
		result.append(", location: ");
		result.append(location);
		result.append(", ref: ");
		result.append(ref);
		result.append(')');
		return result.toString();
	}

} //RegistryImpl
