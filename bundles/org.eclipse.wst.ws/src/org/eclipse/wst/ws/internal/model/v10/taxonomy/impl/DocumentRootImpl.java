/**
 * <copyright>
 * </copyright>
 *
 * $Id: DocumentRootImpl.java,v 1.2 2005/12/03 04:06:48 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.taxonomy.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.wst.ws.internal.model.v10.taxonomy.Category;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.DocumentRoot;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Name;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy;
import org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.DocumentRootImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.DocumentRootImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.impl.DocumentRootImpl#getTaxonomy <em>Taxonomy</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends EObjectImpl implements DocumentRoot {
	/**
	 * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMixed()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap mixed = null;

	/**
	 * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getXMLNSPrefixMap()
	 * @generated
	 * @ordered
	 */
	protected EMap xMLNSPrefixMap = null;

	/**
	 * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getXSISchemaLocation()
	 * @generated
	 * @ordered
	 */
	protected EMap xSISchemaLocation = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DocumentRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return TaxonomyPackage.eINSTANCE.getDocumentRoot();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, TaxonomyPackage.DOCUMENT_ROOT__MIXED);
		}
		return mixed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap getXMLNSPrefixMap() {
		if (xMLNSPrefixMap == null) {
			xMLNSPrefixMap = new EcoreEMap(EcorePackage.eINSTANCE.getEStringToStringMapEntry(), EStringToStringMapEntryImpl.class, this, TaxonomyPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		}
		return xMLNSPrefixMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap getXSISchemaLocation() {
		if (xSISchemaLocation == null) {
			xSISchemaLocation = new EcoreEMap(EcorePackage.eINSTANCE.getEStringToStringMapEntry(), EStringToStringMapEntryImpl.class, this, TaxonomyPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Category getCategory() {
		return (Category)getMixed().get(TaxonomyPackage.eINSTANCE.getDocumentRoot_Category(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCategory(Category newCategory, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(TaxonomyPackage.eINSTANCE.getDocumentRoot_Category(), newCategory, null);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCategory(Category newCategory) {
		((FeatureMap.Internal)getMixed()).set(TaxonomyPackage.eINSTANCE.getDocumentRoot_Category(), newCategory);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Name getName() {
		return (Name)getMixed().get(TaxonomyPackage.eINSTANCE.getDocumentRoot_Name(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetName(Name newName, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(TaxonomyPackage.eINSTANCE.getDocumentRoot_Name(), newName, null);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(Name newName) {
		((FeatureMap.Internal)getMixed()).set(TaxonomyPackage.eINSTANCE.getDocumentRoot_Name(), newName);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Taxonomy getTaxonomy() {
		return (Taxonomy)getMixed().get(TaxonomyPackage.eINSTANCE.getDocumentRoot_Taxonomy(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTaxonomy(Taxonomy newTaxonomy, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(TaxonomyPackage.eINSTANCE.getDocumentRoot_Taxonomy(), newTaxonomy, null);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTaxonomy(Taxonomy newTaxonomy) {
		((FeatureMap.Internal)getMixed()).set(TaxonomyPackage.eINSTANCE.getDocumentRoot_Taxonomy(), newTaxonomy);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case TaxonomyPackage.DOCUMENT_ROOT__MIXED:
					return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
				case TaxonomyPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
					return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
				case TaxonomyPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
					return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
				case TaxonomyPackage.DOCUMENT_ROOT__CATEGORY:
					return basicSetCategory(null, msgs);
				case TaxonomyPackage.DOCUMENT_ROOT__NAME:
					return basicSetName(null, msgs);
				case TaxonomyPackage.DOCUMENT_ROOT__TAXONOMY:
					return basicSetTaxonomy(null, msgs);
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
			case TaxonomyPackage.DOCUMENT_ROOT__MIXED:
				return getMixed();
			case TaxonomyPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return getXMLNSPrefixMap();
			case TaxonomyPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return getXSISchemaLocation();
			case TaxonomyPackage.DOCUMENT_ROOT__CATEGORY:
				return getCategory();
			case TaxonomyPackage.DOCUMENT_ROOT__NAME:
				return getName();
			case TaxonomyPackage.DOCUMENT_ROOT__TAXONOMY:
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
			case TaxonomyPackage.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				getMixed().addAll((Collection)newValue);
				return;
			case TaxonomyPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				getXMLNSPrefixMap().addAll((Collection)newValue);
				return;
			case TaxonomyPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				getXSISchemaLocation().addAll((Collection)newValue);
				return;
			case TaxonomyPackage.DOCUMENT_ROOT__CATEGORY:
				setCategory((Category)newValue);
				return;
			case TaxonomyPackage.DOCUMENT_ROOT__NAME:
				setName((Name)newValue);
				return;
			case TaxonomyPackage.DOCUMENT_ROOT__TAXONOMY:
				setTaxonomy((Taxonomy)newValue);
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
			case TaxonomyPackage.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case TaxonomyPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case TaxonomyPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case TaxonomyPackage.DOCUMENT_ROOT__CATEGORY:
				setCategory((Category)null);
				return;
			case TaxonomyPackage.DOCUMENT_ROOT__NAME:
				setName((Name)null);
				return;
			case TaxonomyPackage.DOCUMENT_ROOT__TAXONOMY:
				setTaxonomy((Taxonomy)null);
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
			case TaxonomyPackage.DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case TaxonomyPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case TaxonomyPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case TaxonomyPackage.DOCUMENT_ROOT__CATEGORY:
				return getCategory() != null;
			case TaxonomyPackage.DOCUMENT_ROOT__NAME:
				return getName() != null;
			case TaxonomyPackage.DOCUMENT_ROOT__TAXONOMY:
				return getTaxonomy() != null;
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
		result.append(" (mixed: ");
		result.append(mixed);
		result.append(')');
		return result.toString();
	}

} //DocumentRootImpl
