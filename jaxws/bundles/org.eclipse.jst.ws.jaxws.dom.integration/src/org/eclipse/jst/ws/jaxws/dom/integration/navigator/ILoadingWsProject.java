/*******************************************************************************
 * Copyright (c) 2009, 2011 by SAP AG, Walldorf. 
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
package org.eclipse.jst.ws.jaxws.dom.integration.navigator;

import org.eclipse.core.resources.IProject;


/**
 * An interface presenting not loaded web service project. This interface is used 
 * to be added to the project explorer WS tree in cases when the project is relevant 
 * but it is not yet loaded by DOM loader. 
 * 
 * @author Georgi Vachkov
 */
public interface ILoadingWsProject 
{
	/**
	 * @return the relevant project
	 */
	public IProject getProject();
	
	/**
	 * Interface used to be added to the tree as a sub node of INotLoadedWsProject node
	 * and displayed with text "Loading...".
	 * 
	 * @author Georgi Vachkov
	 */
	public interface ILoadingDummy {};
}
