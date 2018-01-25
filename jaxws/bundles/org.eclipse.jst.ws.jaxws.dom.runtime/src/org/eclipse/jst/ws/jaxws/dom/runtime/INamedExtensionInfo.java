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
package org.eclipse.jst.ws.jaxws.dom.runtime;

/**
 * Interface representig and extention.
 */
public interface INamedExtensionInfo
{

	/**
	 * The unique ID to identify this consumer type when calling the <code>ProxyGeneratorFactory</code>, e.g. <code>jee/jaxws</code>
	 * 
	 * @return non-empty string containing the ID
	 */
	public abstract String getId();

	/**
	 * A verbose name for the type of consumer, e.g. <code>JAX-WS</code>
	 * 
	 * @return localized string used for the UI
	 */
	public abstract String getName();

}