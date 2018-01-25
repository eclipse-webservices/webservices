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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>IDOM</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM#getWebServiceProjects <em>Web Service Projects</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage#getIDOM()
 * @model
 * @generated
 */
public interface IDOM extends EObject {
	/**
	 * Returns the value of the '<em><b>Web Service Projects</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebServiceProject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Web Service Projects</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Web Service Projects</em>' containment reference list.
	 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage#getIDOM_WebServiceProjects()
	 * @model containment="true" suppressedSetVisibility="true"
	 * @generated
	 */
	EList<IWebServiceProject> getWebServiceProjects();

} // IDOM
