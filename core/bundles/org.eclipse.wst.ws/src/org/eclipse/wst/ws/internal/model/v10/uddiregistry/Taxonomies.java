/**
 * <copyright>
 * </copyright>
 *
 * $Id: Taxonomies.java,v 1.2 2005/12/03 04:06:49 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.uddiregistry;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Taxonomies</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies#getTaxonomy <em>Taxonomy</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getTaxonomies()
 * @model 
 * @generated
 */
public interface Taxonomies extends EObject {
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
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage#getTaxonomies_Taxonomy()
	 * @model type="org.eclipse.wst.ws.internal.model.v10.taxonomy.Taxonomy" containment="true" resolveProxies="false"
	 * @generated
	 */
	EList getTaxonomy();

} // Taxonomies
