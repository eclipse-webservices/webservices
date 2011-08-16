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

public interface IWsDomObtainer
{
	/**
	 * Obtains a {@link IDOM} instance. The implementation may perform a lazy {@link IDOM} load and returns the {@link IDOM} instance via the
	 * {@link IWsDomCallback} specified
	 * 
	 * @param loadCallback
	 *            the callback which is notified on {@link IDOM} instance availability
	 * @see IWsDomCallback#dom(IDOM)
	 */
	public void getDom(final IWsDomCallback loadCallback);
}
