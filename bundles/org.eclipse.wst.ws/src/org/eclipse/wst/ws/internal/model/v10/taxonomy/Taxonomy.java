/**
 * <copyright>
 * </copyright>
 *
 * $Id: Taxonomy.java,v 1.1 2005/11/25 21:54:35 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.taxonomy;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Taxonomy</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy#getNlname <em>Nlname</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy#getCategory <em>Category</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy#getRef <em>Ref</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getTaxonomy()
 * @model 
 * @generated
 */
public interface Taxonomy extends EObject {
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
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getTaxonomy_Nlname()
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
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getTaxonomy_Category()
	 * @model type="org.eclipse.wst.ws.internal.model.v10.taxonomy.Category" containment="true" resolveProxies="false"
	 * @generated
	 */
	EList getCategory();

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getTaxonomy_Id()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' attribute.
	 * @see #setLocation(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getTaxonomy_Location()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 * @generated
	 */
	String getLocation();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy#getLocation <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' attribute.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(String value);

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
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getTaxonomy_Name()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ref</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ref</em>' attribute.
	 * @see #setRef(String)
	 * @see org.eclipse.wst.ws.internal.model.v10.taxonomy.TaxonomyPackage#getTaxonomy_Ref()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 * @generated
	 */
	String getRef();

	/**
	 * Sets the value of the '{@link org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy#getRef <em>Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref</em>' attribute.
	 * @see #getRef()
	 * @generated
	 */
	void setRef(String value);

} // Taxonomy
