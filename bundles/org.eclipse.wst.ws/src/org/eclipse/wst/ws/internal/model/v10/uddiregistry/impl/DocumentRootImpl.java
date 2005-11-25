/**
 * <copyright>
 * </copyright>
 *
 * $Id: DocumentRootImpl.java,v 1.2 2005/12/03 04:06:50 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl;

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

import org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry;
import org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.DocumentRootImpl#getTaxonomies <em>Taxonomies</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.DocumentRootImpl#getUddiRegistry <em>Uddi Registry</em>}</li>
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
		return UDDIRegistryPackage.eINSTANCE.getDocumentRoot();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, UDDIRegistryPackage.DOCUMENT_ROOT__MIXED);
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
			xMLNSPrefixMap = new EcoreEMap(EcorePackage.eINSTANCE.getEStringToStringMapEntry(), EStringToStringMapEntryImpl.class, this, UDDIRegistryPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
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
			xSISchemaLocation = new EcoreEMap(EcorePackage.eINSTANCE.getEStringToStringMapEntry(), EStringToStringMapEntryImpl.class, this, UDDIRegistryPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Taxonomies getTaxonomies() {
		return (Taxonomies)getMixed().get(UDDIRegistryPackage.eINSTANCE.getDocumentRoot_Taxonomies(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTaxonomies(Taxonomies newTaxonomies, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(UDDIRegistryPackage.eINSTANCE.getDocumentRoot_Taxonomies(), newTaxonomies, null);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTaxonomies(Taxonomies newTaxonomies) {
		((FeatureMap.Internal)getMixed()).set(UDDIRegistryPackage.eINSTANCE.getDocumentRoot_Taxonomies(), newTaxonomies);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UDDIRegistry getUddiRegistry() {
		return (UDDIRegistry)getMixed().get(UDDIRegistryPackage.eINSTANCE.getDocumentRoot_UddiRegistry(), true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetUddiRegistry(UDDIRegistry newUddiRegistry, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(UDDIRegistryPackage.eINSTANCE.getDocumentRoot_UddiRegistry(), newUddiRegistry, null);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUddiRegistry(UDDIRegistry newUddiRegistry) {
		((FeatureMap.Internal)getMixed()).set(UDDIRegistryPackage.eINSTANCE.getDocumentRoot_UddiRegistry(), newUddiRegistry);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs) {
		if (featureID >= 0) {
			switch (eDerivedStructuralFeatureID(featureID, baseClass)) {
				case UDDIRegistryPackage.DOCUMENT_ROOT__MIXED:
					return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
				case UDDIRegistryPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
					return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
				case UDDIRegistryPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
					return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
				case UDDIRegistryPackage.DOCUMENT_ROOT__TAXONOMIES:
					return basicSetTaxonomies(null, msgs);
				case UDDIRegistryPackage.DOCUMENT_ROOT__UDDI_REGISTRY:
					return basicSetUddiRegistry(null, msgs);
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
			case UDDIRegistryPackage.DOCUMENT_ROOT__MIXED:
				return getMixed();
			case UDDIRegistryPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return getXMLNSPrefixMap();
			case UDDIRegistryPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return getXSISchemaLocation();
			case UDDIRegistryPackage.DOCUMENT_ROOT__TAXONOMIES:
				return getTaxonomies();
			case UDDIRegistryPackage.DOCUMENT_ROOT__UDDI_REGISTRY:
				return getUddiRegistry();
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
			case UDDIRegistryPackage.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				getMixed().addAll((Collection)newValue);
				return;
			case UDDIRegistryPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				getXMLNSPrefixMap().addAll((Collection)newValue);
				return;
			case UDDIRegistryPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				getXSISchemaLocation().addAll((Collection)newValue);
				return;
			case UDDIRegistryPackage.DOCUMENT_ROOT__TAXONOMIES:
				setTaxonomies((Taxonomies)newValue);
				return;
			case UDDIRegistryPackage.DOCUMENT_ROOT__UDDI_REGISTRY:
				setUddiRegistry((UDDIRegistry)newValue);
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
			case UDDIRegistryPackage.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case UDDIRegistryPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case UDDIRegistryPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case UDDIRegistryPackage.DOCUMENT_ROOT__TAXONOMIES:
				setTaxonomies((Taxonomies)null);
				return;
			case UDDIRegistryPackage.DOCUMENT_ROOT__UDDI_REGISTRY:
				setUddiRegistry((UDDIRegistry)null);
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
			case UDDIRegistryPackage.DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case UDDIRegistryPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case UDDIRegistryPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case UDDIRegistryPackage.DOCUMENT_ROOT__TAXONOMIES:
				return getTaxonomies() != null;
			case UDDIRegistryPackage.DOCUMENT_ROOT__UDDI_REGISTRY:
				return getUddiRegistry() != null;
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
