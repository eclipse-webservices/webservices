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



/**
 * Interface to be implemented by interested in DOM load process. Implementors can be added to
 * the {@link IWsDOMRuntimeExtension} to receive notifications on load startup/finish  
 * 
 * @author Georgi Vachkov
 */
public interface IWsDomLoadListener 
{
	/**
	 * Called by {@link IWsDOMRuntimeExtension} just before the DOM load start 
	 */
	public void started();
	
	/**
	 * Called by {@link IWsDOMRuntimeExtension} when the DOM load has finished
	 */
	public void finished();	
}
