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
package org.eclipse.jst.ws.jaxws.dom.runtime.provider;

/**
 * Interface representing parameter in method
 * 
 * @author Georgi Vachkov
 * 
 */
public interface IMethodParameter
{
	/**
	 * @return the parameter name
	 */
	public String getName();

	/**
	 * @return parameter fully qualified name
	 */
	public String getType();
}
