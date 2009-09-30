/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.api;

import org.eclipse.emf.ecore.EFactory;


/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage
 * @generated
 */
public interface DomFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DomFactory eINSTANCE = org.eclipse.jst.ws.jaxws.dom.runtime.internal.impl.DomFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>IDOM</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IDOM</em>'.
	 * @generated
	 */
	IDOM createIDOM();

	/**
	 * Returns a new object of class '<em>IJava Web Service Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IJava Web Service Element</em>'.
	 * @generated
	 */
	IJavaWebServiceElement createIJavaWebServiceElement();

	/**
	 * Returns a new object of class '<em>IService Endpoint Interface</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IService Endpoint Interface</em>'.
	 * @generated
	 */
	IServiceEndpointInterface createIServiceEndpointInterface();

	/**
	 * Returns a new object of class '<em>IWeb Method</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IWeb Method</em>'.
	 * @generated
	 */
	IWebMethod createIWebMethod();

	/**
	 * Returns a new object of class '<em>IWeb Param</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IWeb Param</em>'.
	 * @generated
	 */
	IWebParam createIWebParam();

	/**
	 * Returns a new object of class '<em>IWeb Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IWeb Service</em>'.
	 * @generated
	 */
	IWebService createIWebService();

	/**
	 * Returns a new object of class '<em>IWeb Service Project</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IWeb Service Project</em>'.
	 * @generated
	 */
	IWebServiceProject createIWebServiceProject();

	/**
	 * Returns a new object of class '<em>IWeb Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IWeb Type</em>'.
	 * @generated
	 */
	IWebType createIWebType();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DomPackage getDomPackage();

} //DomFactory
