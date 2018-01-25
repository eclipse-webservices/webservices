/**
 * <copyright>
 * </copyright>
 *
 * $Id: UDDIRegistryFactory.java,v 1.2 2005/12/03 04:06:49 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.uddiregistry;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage
 * @generated
 */
public interface UDDIRegistryFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	UDDIRegistryFactory eINSTANCE = new org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl.UDDIRegistryFactoryImpl();

	/**
	 * Returns a new object of class '<em>Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Document Root</em>'.
	 * @generated
	 */
	DocumentRoot createDocumentRoot();

	/**
	 * Returns a new object of class '<em>Taxonomies</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Taxonomies</em>'.
	 * @generated
	 */
	Taxonomies createTaxonomies();

	/**
	 * Returns a new object of class '<em>UDDIRegistry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>UDDIRegistry</em>'.
	 * @generated
	 */
	UDDIRegistry createUDDIRegistry();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	UDDIRegistryPackage getUDDIRegistryPackage();

} //UDDIRegistryFactory
