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
 * Class that holds information to locate specific region in the source code 
 *
 */
public interface ILocator
{
	/**
	 * @return the line number in the source where the region pointed by this locator starts  
	 */
	public int getLineNumber();
	

	/**
	 * @return the index of the first character of this region in source code
	 */
	public int getStartPosition();
	
	/**
	 * @return the length of the region
	 */
	public int getLength();
}
