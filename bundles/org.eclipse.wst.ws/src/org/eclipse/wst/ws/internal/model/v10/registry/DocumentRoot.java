/**
 * <copyright>
 * </copyright>
 *
 * $Id: DocumentRoot.java,v 1.2 2005/12/03 04:06:49 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.registry;

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
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot#getRegistry <em>Registry</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.ws.internal.model.v10.registry.RegistryPackage#getDocumentRoot()
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
	 * @see org.eclipse.wst.ws.internal.model.v10.registry.RegistryPackage#getDocumentRoot_Mixed()
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
	 * @see org.eclipse.wst.ws.internal.model.v10.registry.RegistryPackage#getDocumentRoot_XMLNSPrefixMap()
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
	 * @see org.eclipse.wst.ws.internal.model.v10.registry.RegistryPackage#getDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 * @generated
	 */
	EMap getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' containment reference.
	 * @see #setDescription(Description)
	 * @see org.eclipse.wst.ws.internal.model.v10.registry.RegistryPackage#getDocumentRoot_Description()
	 * @model containment="true" resolveProxies="false" transient="true" volatile="true" derived="true"
	 * @generated
	 */
	Description getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot#getDescription <em>Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' containment reference.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(Description value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' containment reference.
	 * @see #setName(Name)
	 * @see org.eclipse.wst.ws.internal.model.v10.registry.RegistryPackage#getDocumentRoot_Name()
	 * @model containment="true" resolveProxies="false" transient="true" volatile="true" derived="true"
	 * @generated
	 */
	Name getName();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot#getName <em>Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' containment reference.
	 * @see #getName()
	 * @generated
	 */
	void setName(Name value);

	/**
	 * Returns the value of the '<em><b>Registry</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Registry</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Registry</em>' containment reference.
	 * @see #setRegistry(Registry)
	 * @see org.eclipse.wst.ws.internal.model.v10.registry.RegistryPackage#getDocumentRoot_Registry()
	 * @model containment="true" resolveProxies="false" transient="true" volatile="true" derived="true"
	 * @generated
	 */
	Registry getRegistry();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.registry.DocumentRoot#getRegistry <em>Registry</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Registry</em>' containment reference.
	 * @see #getRegistry()
	 * @generated
	 */
	void setRegistry(Registry value);

} // DocumentRoot
