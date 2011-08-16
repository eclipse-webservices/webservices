/*******************************************************************************
 * Copyright (c) 2011 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.persistence.load;

import org.eclipse.jst.ws.jaxws.dom.runtime.api.IDOM;

/**
 * Interface for a callback which is notified upon a DOM instance being available
 * 
 * @author Danail Branekov
 * 
 */
public interface IWsDomCallback
{
	/**
	 * Notifies that the {@link IDOM} instance specified is now available
	 * 
	 * @param dom
	 *            the {@link IDOM} instance; never <code>null</code>
	 */
	public void dom(final IDOM dom);
	
	/**
	 * Notifies that the {@link IDOM} load routine is starting since {@link IDOM} is not available 
	 */
	public void domLoadStarting();

	/**
	 * Notifies the the {@link IDOM} load process has been cancelled and therefore the {@link IDOM} instance is not available
	 */
	public void domLoadCancelled();
	
	/**
	 * Notifies that DOM load has failed
	 */
	public void domLoadFailed();
}
