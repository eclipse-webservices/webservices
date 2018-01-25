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

import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;


/**
 * This interface should be implemented by all runtime which want to have DOM Tree 
 * representation on top of them.
 * 
 * @author Georgi Hristov I036201
 */
public interface IWsDOMRuntimeExtension 
{
	/**
	 * This method should instantiate the DOM instance for the runtime and loads the
	 * data into it. The method locks the workspace root during execution. If you call this
	 * method during loading (i.e. somebody already called it in different thread) the method 
	 * will block until the load finishes instead of triggering DOM reload. 
	 * 
	 * @param monitor progress monitor to be used during loading, cancelling the load via monitor is supported
	 * @throws IOException in case the DOM loading is not able to read required data
	 * @throws WsDOMLoadCanceledException in case the loading was cancelled via provided 
	 * <code>monitor</code>.
	 */
	public void createDOM(IProgressMonitor monitor) throws IOException, WsDOMLoadCanceledException;
	
	/**
	 * Adds a load listener to be notified in load events. In case the loading has already been started 
	 * this listener's methods are called depending on the current state of the load.
	 * {@link IWsDomLoadListener#finished()} method is called in any case regardless of whether loading
	 * has been cancelled or not. 
	 * @param listener
	 */
	public void addLoadListener(IWsDomLoadListener listener);
	
	/**
	 * Removes the listener from DOM loading listeners list
	 * 
	 * @param listener
	 */
	public void removeLoadListener(IWsDomLoadListener listener);
	
	/**
	 * To get a handle to the DOM instance available for this runtime.
	 * 
	 * @return the DOM instance or <code>null</code> in case the loading was not started yet or
	 * is not finished yet.
	 * @throws WsDOMLoadCanceledException in case loading has been called already but the load
	 * has been cancelled
	 */
	public IDOM getDOM() throws WsDOMLoadCanceledException;
}
