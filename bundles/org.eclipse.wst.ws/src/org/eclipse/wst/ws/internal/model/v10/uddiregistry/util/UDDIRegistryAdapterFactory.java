/**
 * <copyright>
 * </copyright>
 *
 * $Id: UDDIRegistryAdapterFactory.java,v 1.2 2005/12/03 04:06:50 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.uddiregistry.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.wst.ws.internal.model.v10.registry.Registry;

import org.eclipse.wst.ws.internal.model.v10.uddiregistry.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistryPackage
 * @generated
 */
public class UDDIRegistryAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static UDDIRegistryPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UDDIRegistryAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = UDDIRegistryPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch the delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UDDIRegistrySwitch modelSwitch =
		new UDDIRegistrySwitch() {
			public Object caseDocumentRoot(DocumentRoot object) {
				return createDocumentRootAdapter();
			}
			public Object caseTaxonomies(Taxonomies object) {
				return createTaxonomiesAdapter();
			}
			public Object caseUDDIRegistry(UDDIRegistry object) {
				return createUDDIRegistryAdapter();
			}
			public Object caseRegistry(Registry object) {
				return createRegistryAdapter();
			}
			public Object defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	public Adapter createAdapter(Notifier target) {
		return (Adapter)modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.DocumentRoot
	 * @generated
	 */
	public Adapter createDocumentRootAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies <em>Taxonomies</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.Taxonomies
	 * @generated
	 */
	public Adapter createTaxonomiesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry <em>UDDIRegistry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.wst.ws.internal.model.v10.uddiregistry.UDDIRegistry
	 * @generated
	 */
	public Adapter createUDDIRegistryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.wst.ws.internal.model.v10.registry.Registry <em>Registry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.wst.ws.internal.model.v10.registry.Registry
	 * @generated
	 */
	public Adapter createRegistryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //UDDIRegistryAdapterFactory
