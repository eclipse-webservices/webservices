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
package org.eclipse.jst.ws.jaxws.dom.runtime.registry;

import java.util.Map;

import org.eclipse.jst.ws.jaxws.dom.runtime.INamedExtensionInfo;

/**
 * Defines the information structure which should be instantiated upon reading contributors
 * to the Supported Runtime extension point.
 * 
 * @author Georgi Hristov I036201
 *
 */
public interface IWsDOMRuntimeInfo extends INamedExtensionInfo
{
	/**
	 * 
	 * @return the map of project facets relevant for a particular runtime where the key is 
	 * the project facet id and the value is the version of the project facet if specified.
	 */
	public abstract Map<String, String> getProjectFacets();
}
