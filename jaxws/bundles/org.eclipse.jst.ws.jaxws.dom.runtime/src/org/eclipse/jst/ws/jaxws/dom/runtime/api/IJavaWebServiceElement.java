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

import org.eclipse.emf.ecore.EObject;

/**
 * @model
 *  * @author Hristo Sabev
 *
 */
public interface IJavaWebServiceElement extends EObject
{
	/**
	 * @model changeable="true" suppressedSetVisibility="true" required="true"
	 * @return
	 */
	public String getImplementation();

	/**
	 * @model changeable="true" required="true"
	 * @return
	 */
	public String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.jst.ws.jaxws.dom.runtime.api.IJavaWebServiceElement#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);
}
