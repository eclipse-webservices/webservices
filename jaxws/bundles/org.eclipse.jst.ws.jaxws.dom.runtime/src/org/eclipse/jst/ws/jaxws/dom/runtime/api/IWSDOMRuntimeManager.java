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

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;


public interface IWSDOMRuntimeManager
{
	/**
	 * Instantiates the DOM Runtimes if not already.
	 * @param progress monitor to track progress 
	 */
	public void createDOMRuntimes(IProgressMonitor monitor);
	
	/**
	 * This method should return the DOM runtime instance corresponding to the runtime id
	 * and instantiate such DOM runtime instance if non existent at the 
	 * moment of invocation of this method.
	 * 
	 * @param runtimeId - string identifier of the runtime 
	 * @return the DOM runtime instance. Null if runtime with such id is not registered
	 */
	public  IWsDOMRuntimeExtension getDOMRuntime(String runtimeId);
	
	/**
	 * 
	 * @return a list of all DOM Runtimes currently instantiated
	 */
	public Collection<IWsDOMRuntimeExtension> getDOMRuntimes();

	/**
	 * This method should reload all instances of DOM Runtimes.
	 *
	 */
	public void reloadDOMRuntimes(IProgressMonitor monitor);
}
