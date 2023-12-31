/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;


/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>SOAP Binding Parameter Style</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.jst.ws.jaxws.dom.runtime.api.DomPackage#getSOAPBindingParameterStyle()
 * @model
 * @generated
 */
public enum SOAPBindingParameterStyle implements Enumerator {
	/**
	 * The '<em><b>WRAPPED</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #WRAPPED_VALUE
	 * @generated
	 * @ordered
	 */
	WRAPPED(0, "WRAPPED", "WRAPPED"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>BARE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #BARE_VALUE
	 * @generated
	 * @ordered
	 */
	BARE(1, "BARE", "BARE"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>WRAPPED</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>WRAPPED</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #WRAPPED
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int WRAPPED_VALUE = 0;

	/**
	 * The '<em><b>BARE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>BARE</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #BARE
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int BARE_VALUE = 1;

	/**
	 * An array of all the '<em><b>SOAP Binding Parameter Style</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final SOAPBindingParameterStyle[] VALUES_ARRAY =
		new SOAPBindingParameterStyle[]
		{
			WRAPPED,
			BARE,
		};

	/**
	 * A public read-only list of all the '<em><b>SOAP Binding Parameter Style</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<SOAPBindingParameterStyle> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>SOAP Binding Parameter Style</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SOAPBindingParameterStyle get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i)
		{
			SOAPBindingParameterStyle result = VALUES_ARRAY[i];
			if (result.toString().equals(literal))
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>SOAP Binding Parameter Style</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SOAPBindingParameterStyle getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i)
		{
			SOAPBindingParameterStyle result = VALUES_ARRAY[i];
			if (result.getName().equals(name))
			{
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>SOAP Binding Parameter Style</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SOAPBindingParameterStyle get(int value) {
		switch (value)
		{
			case WRAPPED_VALUE: return WRAPPED;
			case BARE_VALUE: return BARE;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private SOAPBindingParameterStyle(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //SOAPBindingParameterStyle
