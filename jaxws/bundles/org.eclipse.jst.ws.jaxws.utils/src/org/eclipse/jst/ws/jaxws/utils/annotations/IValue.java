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
package org.eclipse.jst.ws.jaxws.utils.annotations;

/**
 * Interface representing artifact value in annotation.
 * 
 * @author Plamen Pavlov
 */
public interface IValue
{
	/**
	 * Obtains the string representation of this value. The obtained string representation can later on be used to instantiate
	 * an instance of the type of the value that will be considedered equal to the returned value.
	 * <p>
	 * 	<c>@AnnotationExample(1)</c> //annoation with one attribute/parameter called "value" with value of "1". 
	 * </p>
	 * The toString method will return the string "1", which can later on be used by Integer.parse(String) to create an instance of Integer
	 * equal to the value represented by the integer literal 1
	 * @return the string representation of this value
	 */
	public String toString();
	
	/**
	 * Gets the information about location in the source code.
	 * 
	 * @return ILocator
	 */
	public ILocator getLocator();
	
}
