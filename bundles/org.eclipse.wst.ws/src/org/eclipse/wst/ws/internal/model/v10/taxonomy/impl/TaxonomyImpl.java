/**
 * <copyright>
 * </copyright>
 *
 * $Id: TaxonomyImpl.java,v 1.2 2005/12/03 04:06:48 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.taxonomy.impl;

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

import org.eclipse.wst.ws.internal.model.v10.taxonomy.Category;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Name;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Taxonomy</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.TaxonomyImpl#getNlname <em>Nlname</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.TaxonomyImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.TaxonomyImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.TaxonomyImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.TaxonomyImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.TaxonomyImpl#getRef <em>Ref</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.TaxonomyImpl#getTmodelKey <em>Tmodel Key</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TaxonomyImpl extends EObjectImpl implements Taxonomy {
	/**
	 * The cached value of the '{@link #getNlname() <em>Nlname</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNlname()
	 * @generated
	 * @ordered
	 */
	protected EList nlname = null;

	/**
	 * The cached value of the '{@link #getCategory() <em>Category</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected EList category = null;

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
	 * The default value of the '{@link #getTmodelKey() <em>Tmodel Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTmodelKey()
	 * @generated
	 * @ordered
	 */
	protected static final String TMODEL_KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTmodelKey() <em>Tmodel Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTmodelKey()
	 * @generated
	 * @ordered
	 */
	protected String tmodelKey = TMODEL_KEY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TaxonomyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return TaxonomyPackage.eINSTANCE.getTaxonomy();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getNlname() {
		if (nlname == null) {
			nlname = new EObjectContainmentEList(Name.class, this, TaxonomyPackage.TAXONOMY__NLNAME);
		}
		return nlname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getCategory() {
		if (category == null) {
			category = new EObjectContainmentEList(Category.class, this, TaxonomyPackage.TAXONOMY__CATEGORY);
		}
		return category;
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
			eNotify(new ENotificationImpl(this, Notification.SET, TaxonomyPackage.TAXONOMY__ID, oldId, id));
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
			eNotify(new ENotificationImpl(this, Notification.SET, TaxonomyPackage.TAXONOMY__LOCATION, oldLocation, location));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TaxonomyPackage.TAXONOMY__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, TaxonomyPackage.TAXONOMY__REF, oldRef, ref));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTmodelKey() {
		return tmodelKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTmodelKey(String newTmodelKey) {
		String oldTmodelKey = tmodelKey;
		tmodelKey = newTmodelKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TaxonomyPackage.TAXONOMY__TMODEL_KEY, oldTmodelKey, tmodelKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case TaxonomyPackage.TAXONOMY__NLNAME:
					return ((InternalEList)getNlname()).basicRemove(otherEnd, msgs);
				case TaxonomyPackage.TAXONOMY__CATEGORY:
					return ((InternalEList)getCategory()).basicRemove(otherEnd, msgs);
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
			case TaxonomyPackage.TAXONOMY__NLNAME:
				return getNlname();
			case TaxonomyPackage.TAXONOMY__CATEGORY:
				return getCategory();
			case TaxonomyPackage.TAXONOMY__ID:
				return getId();
			case TaxonomyPackage.TAXONOMY__LOCATION:
				return getLocation();
			case TaxonomyPackage.TAXONOMY__NAME:
				return getName();
			case TaxonomyPackage.TAXONOMY__REF:
				return getRef();
			case TaxonomyPackage.TAXONOMY__TMODEL_KEY:
				return getTmodelKey();
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
			case TaxonomyPackage.TAXONOMY__NLNAME:
				getNlname().clear();
				getNlname().addAll((Collection)newValue);
				return;
			case TaxonomyPackage.TAXONOMY__CATEGORY:
				getCategory().clear();
				getCategory().addAll((Collection)newValue);
				return;
			case TaxonomyPackage.TAXONOMY__ID:
				setId((String)newValue);
				return;
			case TaxonomyPackage.TAXONOMY__LOCATION:
				setLocation((String)newValue);
				return;
			case TaxonomyPackage.TAXONOMY__NAME:
				setName((String)newValue);
				return;
			case TaxonomyPackage.TAXONOMY__REF:
				setRef((String)newValue);
				return;
			case TaxonomyPackage.TAXONOMY__TMODEL_KEY:
				setTmodelKey((String)newValue);
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
			case TaxonomyPackage.TAXONOMY__NLNAME:
				getNlname().clear();
				return;
			case TaxonomyPackage.TAXONOMY__CATEGORY:
				getCategory().clear();
				return;
			case TaxonomyPackage.TAXONOMY__ID:
				setId(ID_EDEFAULT);
				return;
			case TaxonomyPackage.TAXONOMY__LOCATION:
				setLocation(LOCATION_EDEFAULT);
				return;
			case TaxonomyPackage.TAXONOMY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case TaxonomyPackage.TAXONOMY__REF:
				setRef(REF_EDEFAULT);
				return;
			case TaxonomyPackage.TAXONOMY__TMODEL_KEY:
				setTmodelKey(TMODEL_KEY_EDEFAULT);
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
			case TaxonomyPackage.TAXONOMY__NLNAME:
				return nlname != null && !nlname.isEmpty();
			case TaxonomyPackage.TAXONOMY__CATEGORY:
				return category != null && !category.isEmpty();
			case TaxonomyPackage.TAXONOMY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case TaxonomyPackage.TAXONOMY__LOCATION:
				return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
			case TaxonomyPackage.TAXONOMY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case TaxonomyPackage.TAXONOMY__REF:
				return REF_EDEFAULT == null ? ref != null : !REF_EDEFAULT.equals(ref);
			case TaxonomyPackage.TAXONOMY__TMODEL_KEY:
				return TMODEL_KEY_EDEFAULT == null ? tmodelKey != null : !TMODEL_KEY_EDEFAULT.equals(tmodelKey);
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
		result.append(", name: ");
		result.append(name);
		result.append(", ref: ");
		result.append(ref);
		result.append(", tmodelKey: ");
		result.append(tmodelKey);
		result.append(')');
		return result.toString();
	}

} //TaxonomyImpl
