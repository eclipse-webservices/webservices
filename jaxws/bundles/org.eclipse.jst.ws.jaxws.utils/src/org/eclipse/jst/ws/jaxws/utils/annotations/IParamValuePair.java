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
 * Interface representing param value pair in annotation. Param value pair has the following form: param=value
 * 
 * @author Plamen Pavlov
 */
public interface IParamValuePair
{
	/**
	 * @return param name
	 */
	public String getParam();

	/**
	 * @return the value
	 */
	public IValue getValue();
	
	/**
	 * Gets the information about location in the source code.
	 * 
	 * @return ILocator
	 */
	public ILocator getLocator();

}
