/**
 * <copyright>
 * </copyright>
 *
 * $Id: Category.java,v 1.2 2005/12/03 04:06:49 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.taxonomy;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Category</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Category#getNlname <em>Nlname</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Category#getCategory <em>Category</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Category#getCode <em>Code</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Category#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getCategory()
 * @model 
 * @generated
 */
public interface Category extends EObject {
	/**
	 * Returns the value of the '<em><b>Nlname</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Name}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nlname</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nlname</em>' containment reference list.
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getCategory_Nlname()
	 * @model type="org.eclipse.wst.ws.internal.model.v10.taxonomy.Name" containment="true" resolveProxies="false"
	 * @generated
	 */
	EList getNlname();

	/**
	 * Returns the value of the '<em><b>Category</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Category}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category</em>' containment reference list.
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getCategory_Category()
	 * @model type="org.eclipse.wst.ws.internal.model.v10.taxonomy.Category" containment="true" resolveProxies="false"
	 * @generated
	 */
	EList getCategory();

	/**
	 * Returns the value of the '<em><b>Code</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Code</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Code</em>' attribute.
	 * @see #setCode(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getCategory_Code()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	String getCode();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Category#getCode <em>Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code</em>' attribute.
	 * @see #getCode()
	 * @generated
	 */
	void setCode(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getCategory_Name()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Category#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // Category
