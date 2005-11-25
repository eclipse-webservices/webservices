/**
 * <copyright>
 * </copyright>
 *
 * $Id: DocumentRoot.java,v 1.2 2005/12/03 04:06:49 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.uddiregistry;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getTaxonomies <em>Taxonomies</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getUddiRegistry <em>Uddi Registry</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getDocumentRoot()
 * @model 
 * @generated
 */
public interface DocumentRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getDocumentRoot_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 * @generated
	 */
	FeatureMap getMixed();

	/**
	 * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XMLNS Prefix Map</em>' map.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getDocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 * @generated
	 */
	EMap getXMLNSPrefixMap();

	/**
	 * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XSI Schema Location</em>' map.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 * @generated
	 */
	EMap getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>Taxonomies</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Taxonomies</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Taxonomies</em>' containment reference.
	 * @see #setTaxonomies(Taxonomies)
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getDocumentRoot_Taxonomies()
	 * @model containment="true" resolveProxies="false" transient="true" volatile="true" derived="true"
	 * @generated
	 */
	Taxonomies getTaxonomies();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getTaxonomies <em>Taxonomies</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Taxonomies</em>' containment reference.
	 * @see #getTaxonomies()
	 * @generated
	 */
	void setTaxonomies(Taxonomies value);

	/**
	 * Returns the value of the '<em><b>Uddi Registry</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Uddi Registry</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Uddi Registry</em>' containment reference.
	 * @see #setUddiRegistry(UDDIRegistry)
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getDocumentRoot_UddiRegistry()
	 * @model containment="true" resolveProxies="false" transient="true" volatile="true" derived="true"
	 * @generated
	 */
	UDDIRegistry getUddiRegistry();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot#getUddiRegistry <em>Uddi Registry</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uddi Registry</em>' containment reference.
	 * @see #getUddiRegistry()
	 * @generated
	 */
	void setUddiRegistry(UDDIRegistry value);

} // DocumentRoot
