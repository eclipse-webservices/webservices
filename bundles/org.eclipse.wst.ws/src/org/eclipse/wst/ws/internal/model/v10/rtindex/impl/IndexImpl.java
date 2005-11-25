/**
 * <copyright>
 * </copyright>
 *
 * $Id: IndexImpl.java,v 1.2 2005/12/03 04:06:48 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.rtindex.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.wst.ws.internal.model.v10.registry.Registry;

import org.eclipse.wst.ws.internal.model.v10.rtindex.Description;
import org.eclipse.wst.ws.internal.model.v10.rtindex.Index;
import org.eclipse.wst.ws.internal.model.v10.rtindex.Name;
import org.eclipse.wst.ws.internal.model.v10.rtindex.RTIndexPackage;

import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Index</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.rtindex.impl.IndexImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.rtindex.impl.IndexImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.rtindex.impl.IndexImpl#getRegistry <em>Registry</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.rtindex.impl.IndexImpl#getTaxonomy <em>Taxonomy</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IndexImpl extends EObjectImpl implements Index {
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
	 * The cached value of the '{@link #getRegistry() <em>Registry</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRegistry()
	 * @generated
	 * @ordered
	 */
	protected EList registry = null;

	/**
	 * The cached value of the '{@link #getTaxonomy() <em>Taxonomy</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTaxonomy()
	 * @generated
	 * @ordered
	 */
	protected EList taxonomy = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndexImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return RTIndexPackage.eINSTANCE.getIndex();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getName() {
		if (name == null) {
			name = new EObjectContainmentEList(Name.class, this, RTIndexPackage.INDEX__NAME);
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
			description = new EObjectContainmentEList(Description.class, this, RTIndexPackage.INDEX__DESCRIPTION);
		}
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getRegistry() {
		if (registry == null) {
			registry = new EObjectContainmentEList(Registry.class, this, RTIndexPackage.INDEX__REGISTRY);
		}
		return registry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getTaxonomy() {
		if (taxonomy == null) {
			taxonomy = new EObjectContainmentEList(Taxonomy.class, this, RTIndexPackage.INDEX__TAXONOMY);
		}
		return taxonomy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case RTIndexPackage.INDEX__NAME:
					return ((InternalEList)getName()).basicRemove(otherEnd, msgs);
				case RTIndexPackage.INDEX__DESCRIPTION:
					return ((InternalEList)getDescription()).basicRemove(otherEnd, msgs);
				case RTIndexPackage.INDEX__REGISTRY:
					return ((InternalEList)getRegistry()).basicRemove(otherEnd, msgs);
				case RTIndexPackage.INDEX__TAXONOMY:
					return ((InternalEList)getTaxonomy()).basicRemove(otherEnd, msgs);
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
			case RTIndexPackage.INDEX__NAME:
				return getName();
			case RTIndexPackage.INDEX__DESCRIPTION:
				return getDescription();
			case RTIndexPackage.INDEX__REGISTRY:
				return getRegistry();
			case RTIndexPackage.INDEX__TAXONOMY:
				return getTaxonomy();
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
			case RTIndexPackage.INDEX__NAME:
				getName().clear();
				getName().addAll((Collection)newValue);
				return;
			case RTIndexPackage.INDEX__DESCRIPTION:
				getDescription().clear();
				getDescription().addAll((Collection)newValue);
				return;
			case RTIndexPackage.INDEX__REGISTRY:
				getRegistry().clear();
				getRegistry().addAll((Collection)newValue);
				return;
			case RTIndexPackage.INDEX__TAXONOMY:
				getTaxonomy().clear();
				getTaxonomy().addAll((Collection)newValue);
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
			case RTIndexPackage.INDEX__NAME:
				getName().clear();
				return;
			case RTIndexPackage.INDEX__DESCRIPTION:
				getDescription().clear();
				return;
			case RTIndexPackage.INDEX__REGISTRY:
				getRegistry().clear();
				return;
			case RTIndexPackage.INDEX__TAXONOMY:
				getTaxonomy().clear();
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
			case RTIndexPackage.INDEX__NAME:
				return name != null && !name.isEmpty();
			case RTIndexPackage.INDEX__DESCRIPTION:
				return description != null && !description.isEmpty();
			case RTIndexPackage.INDEX__REGISTRY:
				return registry != null && !registry.isEmpty();
			case RTIndexPackage.INDEX__TAXONOMY:
				return taxonomy != null && !taxonomy.isEmpty();
		}
		return eDynamicIsSet(eFeature);
	}

} //IndexImpl
