/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * yyyymmdd bug      Email and other contact information
 * -------- -------- -----------------------------------------------------------
 * 20060419   132905 cbrealey@ca.ibm.com - Chris Brealey          
 *******************************************************************************/
package org.eclipse.jst.ws.internal.conformance;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

/**
 * @author cbrealey
 * This object represents a simple or indexed property
 * as defined by the Java Bean specification. Every
 * IJavaBeanProperty is characterized by four things:
 * <ol>
 * <li>The name of the property.</li>
 * <li>Whether or not the property is indexed.</li>
 * <li>The JDT IMethod of the getter, if any.</li>
 * <li>The JDT IMethod of the setter, if any.</li>
 * </ol>
 */
public interface IJavaBeanProperty
{
	/**
	 * Returns the name of the property.
	 * @return The name of the property.
	 */
	public String getName ();

	/**
	 * Returns true if and only if the property is indexed.
	 * @return True if and only if the property is indexed.
	 */
	public boolean isIndexed ();

	/**
	 * Returns the JDT IMethod of the getter.
	 * For indexed properties,
	 * the return type of the getter will be an array.
	 * For simple properties,
	 * the return type of the getter will not be an array.
	 * @return The JDT IMethod of the getter,
	 * or null if this is a write-only property.
	 */
	public IMethod getGetter ();
	
	/**
	 * Returns the JDT IMethod of the indexed getter,
	 * or null if this is either not an indexed property,
	 * or is an indexed property but has only the array-
	 * based getter.
	 * @return The JDT IMethod of the indexed getter,
	 * or null if this is not an indexed property or
	 * if there is no indexed getter.
	 */
	public IMethod getIndexedGetter ();

	/**
	 * Returns the JDT IMethod of the setter.
	 * For indexed properties,
	 * the parameter type of the setter will be an array.
	 * For simple properties,
	 * the parameter type of the setter will not be an array.
	 * @return The JDT IMethod of the setter,
	 * or null if this is a read-only property.
	 */
	public IMethod getSetter ();
	
	/**
	 * Returns the JDT IMethod of the indexed setter,
	 * or null if this is either not an indexed property,
	 * or is an indexed property but has only the array-
	 * based setter.
	 * @return The JDT IMethod of the indexed setter,
	 * or null if this is not an indexed property or
	 * if there is no indexed setter.
	 */
	public IMethod getIndexedSetter ();
	
	/**
	 * Returns the type within which the getter and/or setter
	 * of this property is declared, or null if none.
	 * @return The type within which the getter and/or setter
	 * of this property is declared.
	 */
	public IType getDeclaringType ();
}
