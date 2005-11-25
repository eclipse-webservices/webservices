/**
 * <copyright>
 * </copyright>
 *
 * $Id: Index.java,v 1.2 2005/12/03 04:06:48 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.rtindex;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Index</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.rtindex.Index#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.rtindex.Index#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.rtindex.Index#getRegistry <em>Registry</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.rtindex.Index#getTaxonomy <em>Taxonomy</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.ws.internal.model.v10.rtindex.RTIndexPackage#getIndex()
 * @model 
 * @generated
 */
public interface Index extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.wst.ws.internal.model.v10.rtindex.Name}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' containment reference list.
	 * @see org.eclipse.wst.ws.internal.model.v10.rtindex.RTIndexPackage#getIndex_Name()
	 * @model type="org.eclipse.wst.ws.internal.model.v10.rtindex.Name" containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	EList getName();

	/**
	 * Returns the value of the '<em><b>Description</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.wst.ws.internal.model.v10.rtindex.Description}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' containment reference list.
	 * @see org.eclipse.wst.ws.internal.model.v10.rtindex.RTIndexPackage#getIndex_Description()
	 * @model type="org.eclipse.wst.ws.internal.model.v10.rtindex.Description" containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	EList getDescription();

	/**
	 * Returns the value of the '<em><b>Registry</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.wst.ws.internal.model.v10.registry.Registry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Registry</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Registry</em>' containment reference list.
	 * @see org.eclipse.wst.ws.internal.model.v10.rtindex.RTIndexPackage#getIndex_Registry()
	 * @model type="org.eclipse.wst.ws.internal.model.v10.registry.Registry" containment="true" resolveProxies="false"
	 * @generated
	 */
	EList getRegistry();

	/**
	 * Returns the value of the '<em><b>Taxonomy</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Taxonomy</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Taxonomy</em>' containment reference list.
	 * @see org.eclipse.wst.ws.internal.model.v10.rtindex.RTIndexPackage#getIndex_Taxonomy()
	 * @model type="org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy" containment="true" resolveProxies="false"
	 * @generated
	 */
	EList getTaxonomy();

} // Index
