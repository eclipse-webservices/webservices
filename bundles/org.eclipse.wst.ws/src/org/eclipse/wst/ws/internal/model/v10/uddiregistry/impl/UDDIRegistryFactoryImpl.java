/**
 * <copyright>
 * </copyright>
 *
 * $Id: UDDIRegistryFactoryImpl.java,v 1.2 2005/12/03 04:06:50 cbrealey Exp $
 */
package org.eclipse.wst.ws.internal.model.v10.uddiregistry.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.wst.ws.internal.model.v10.uddiregistry.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UDDIRegistryFactoryImpl extends EFactoryImpl implements UDDIRegistryFactory {
	/**
	 * Creates and instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UDDIRegistryFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case UDDIRegistryPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case UDDIRegistryPackage.TAXONOMIES: return createTaxonomies();
			case UDDIRegistryPackage.UDDI_REGISTRY: return createUDDIRegistry();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DocumentRoot createDocumentRoot() {
		DocumentRootImpl documentRoot = new DocumentRootImpl();
		return documentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Taxonomies createTaxonomies() {
		TaxonomiesImpl taxonomies = new TaxonomiesImpl();
		return taxonomies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UDDIRegistry createUDDIRegistry() {
		UDDIRegistryImpl uddiRegistry = new UDDIRegistryImpl();
		return uddiRegistry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UDDIRegistryPackage getUDDIRegistryPackage() {
		return (UDDIRegistryPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static UDDIRegistryPackage getPackage() {
		return UDDIRegistryPackage.eINSTANCE;
	}

} //UDDIRegistryFactoryImpl
